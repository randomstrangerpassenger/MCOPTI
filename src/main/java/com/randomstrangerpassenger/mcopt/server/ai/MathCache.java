package com.randomstrangerpassenger.mcopt.server.ai;

import com.randomstrangerpassenger.mcopt.MCOPT;

/**
 * High-performance atan2 caching system for AI calculations.
 * <p>
 * This class pre-computes atan2 lookup tables to avoid expensive runtime
 * calculations
 * during entity AI tick processing (specifically in
 * {@link OptimizedLookControl}).
 * <p>
 * <b>Optimization Note:</b> sin/cos lookup tables were removed as they were
 * completely unused,
 * saving 32KB of memory. Java 21's native Math.sin/cos implementations are fast
 * enough that
 * lookup tables provide negligible benefit on modern CPUs.
 * <p>
 * <b>Memory Usage:</b>
 * <ul>
 * <li>atan2: ~1KB (256 entries × 4 bytes)</li>
 * <li>Total: 1KB (down from 33KB)</li>
 * </ul>
 * <p>
 * <b>Trade-offs:</b>
 * <ul>
 * <li>Precision: Slight loss due to table interpolation vs hardware FPU</li>
 * <li>Performance: atan2 caching still beneficial for frequent AI
 * calculations</li>
 * </ul>
 *
 * @see OptimizedLookControl
 */
public class MathCache {

    // atan2 lookup table configuration
    private static final int ATAN2_BITS = 8; // 8 bits = 256 entries
    private static final int ATAN2_DIM = 1 << (ATAN2_BITS >> 1); // 16x16 = 256
    private static final float[] ATAN2_TABLE = new float[ATAN2_DIM * ATAN2_DIM];

    // Thread-safe initialization flag
    // volatile ensures visibility across threads without requiring synchronization
    // on reads
    private static volatile boolean initialized = false;

    /**
     * Initialize all math lookup tables.
     * Must be called during mod initialization before any AI processing.
     * Thread-safe using double-checked locking pattern.
     */
    public static void init() {
        // Double-checked locking for thread-safe initialization
        if (initialized) {
            MCOPT.LOGGER.warn("MathCache already initialized, skipping");
            return;
        }

        synchronized (MathCache.class) {
            // Check again inside synchronized block
            if (initialized) {
                return;
            }

            long startTime = System.nanoTime();

            // Pre-compute atan2 table
            initAtan2Table();

            long endTime = System.nanoTime();
            double elapsedMs = (endTime - startTime) / 1_000_000.0;

            initialized = true;
            MCOPT.LOGGER.info("MathCache initialized in {:.2f}ms (atan2: {} entries)",
                    elapsedMs, ATAN2_TABLE.length);
        }
    }

    /**
     * Pre-compute atan2 lookup table.
     * Covers all possible angle combinations in a normalized square.
     */
    private static void initAtan2Table() {
        for (int i = 0; i < ATAN2_DIM; i++) {
            for (int j = 0; j < ATAN2_DIM; j++) {
                float x = (float) i / ATAN2_DIM;
                float y = (float) j / ATAN2_DIM;
                ATAN2_TABLE[j * ATAN2_DIM + i] = (float) Math.atan2(y, x);
            }
        }
    }

    /**
     * Fast atan2 approximation using lookup table.
     *
     * @param y Y coordinate
     * @param x X coordinate
     * @return Angle in radians [-PI, PI]
     */
    public static float atan2(double y, double x) {
        // Handle edge cases
        if (x == 0.0) {
            if (y > 0.0)
                return (float) Math.PI / 2;
            if (y < 0.0)
                return -(float) Math.PI / 2;
            return 0.0f;
        }

        // Normalize to first quadrant
        float absY = (float) Math.abs(y);
        float absX = (float) Math.abs(x);

        // Determine which axis is larger for better precision
        boolean swapped = absY > absX;
        float minVal = swapped ? absX : absY;
        float maxVal = swapped ? absY : absX;

        // Normalize to [0, 1] range
        float normalized = minVal / maxVal;

        // Lookup in table
        int index = (int) (normalized * (ATAN2_DIM - 1));
        float angle = ATAN2_TABLE[swapped ? (index * ATAN2_DIM + (ATAN2_DIM - 1)) : index];

        // Adjust for swapped axes
        if (swapped) {
            angle = (float) Math.PI / 2 - angle;
        }

        // Adjust for quadrant
        if (x < 0.0) {
            angle = (float) Math.PI - angle;
        }
        if (y < 0.0) {
            angle = -angle;
        }

        return angle;
    }

    /**
     * Check if MathCache has been initialized.
     *
     * @return true if initialized, false otherwise
     */
    public static boolean isInitialized() {
        return initialized;
    }
}
