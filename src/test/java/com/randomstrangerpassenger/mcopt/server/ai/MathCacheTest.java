package com.randomstrangerpassenger.mcopt.server.ai;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for MathCache lookup table functionality.
 * <p>
 * Tests cover:
 * - Initialization (thread-safe, idempotent)
 * - Mathematical accuracy (compared to Math.* functions)
 * - Edge cases (zero, negative, special angles)
 * - Thread safety
 */
@DisplayName("MathCache Unit Tests")
class MathCacheTest {

    private static final float ATAN2_TOLERANCE = 0.05f; // ~3 degrees error tolerance
    private static final float TRIG_TOLERANCE = 0.001f; // 0.1% error tolerance for sin/cos

    @BeforeAll
    static void setUp() {
        // Ensure MathCache is initialized before all tests
        MathCache.init();
    }

    // ========== Initialization Tests ==========

    @Test
    @DisplayName("Should initialize successfully")
    void testInitialization() {
        assertThat(MathCache.isInitialized())
                .as("MathCache should be initialized")
                .isTrue();
    }

    @Test
    @DisplayName("Should be idempotent (multiple init calls are safe)")
    void testIdempotentInitialization() {
        // Should not throw or cause issues
        assertThatCode(() -> {
            MathCache.init();
            MathCache.init();
            MathCache.init();
        }).doesNotThrowAnyException();

        assertThat(MathCache.isInitialized()).isTrue();
    }

    @Test
    @DisplayName("Should be thread-safe during initialization")
    void testThreadSafeInitialization() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);

        // Simulate multiple threads trying to initialize simultaneously
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    MathCache.init();
                    successCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        assertThat(successCount.get())
                .as("All threads should complete initialization without errors")
                .isEqualTo(threadCount);
        assertThat(MathCache.isInitialized()).isTrue();
    }

    // ========== atan2 Accuracy Tests ==========

    @ParameterizedTest
    @DisplayName("Should calculate atan2 accurately for common cases")
    @CsvSource({
            "1.0, 1.0", // 45 degrees
            "1.0, 0.0", // 90 degrees
            "0.0, 1.0", // 0 degrees
            "-1.0, 1.0", // -45 degrees
            "1.0, -1.0", // 135 degrees
            "-1.0, -1.0", // -135 degrees
            "5.0, 3.0", // Random positive
            "-3.0, 7.0", // Random mixed
    })
    void testAtan2Accuracy(double y, double x) {
        float cached = MathCache.atan2(y, x);
        double expected = Math.atan2(y, x);

        assertThat(cached)
                .as("atan2(%f, %f) should be close to Math.atan2", y, x)
                .isCloseTo((float) expected, within(ATAN2_TOLERANCE));
    }

    @Test
    @DisplayName("Should handle atan2 edge case: zero y")
    void testAtan2ZeroY() {
        assertThat(MathCache.atan2(0.0, 1.0))
                .as("atan2(0, 1) should be 0")
                .isCloseTo(0.0f, within(ATAN2_TOLERANCE));

        assertThat(MathCache.atan2(0.0, -1.0))
                .as("atan2(0, -1) should be PI")
                .isCloseTo((float) Math.PI, within(ATAN2_TOLERANCE));
    }

    @Test
    @DisplayName("Should handle atan2 edge case: zero x")
    void testAtan2ZeroX() {
        assertThat(MathCache.atan2(1.0, 0.0))
                .as("atan2(1, 0) should be PI/2")
                .isCloseTo((float) Math.PI / 2, within(ATAN2_TOLERANCE));

        assertThat(MathCache.atan2(-1.0, 0.0))
                .as("atan2(-1, 0) should be -PI/2")
                .isCloseTo(-(float) Math.PI / 2, within(ATAN2_TOLERANCE));

        assertThat(MathCache.atan2(0.0, 0.0))
                .as("atan2(0, 0) should be 0")
                .isCloseTo(0.0f, within(ATAN2_TOLERANCE));
    }

    @Test
    @DisplayName("Should handle atan2 with very small values")
    void testAtan2SmallValues() {
        double y = 0.0001;
        double x = 0.0001;

        float cached = MathCache.atan2(y, x);
        double expected = Math.atan2(y, x);

        assertThat(cached)
                .as("atan2 should work with very small values")
                .isCloseTo((float) expected, within(ATAN2_TOLERANCE));
    }

    @Test
    @DisplayName("Should handle atan2 with very large values")
    void testAtan2LargeValues() {
        double y = 10000.0;
        double x = 10000.0;

        float cached = MathCache.atan2(y, x);
        double expected = Math.atan2(y, x);

        assertThat(cached)
                .as("atan2 should work with very large values")
                .isCloseTo((float) expected, within(ATAN2_TOLERANCE));
    }

    // ========== sin/cos tests removed (tables were removed from MathCache)
    // ==========

    // sin/cos tests removed - those methods were deleted from MathCache

    // ========== Performance Characteristics Tests ==========

    @Test
    @DisplayName("Should be thread-safe for concurrent reads")
    void testThreadSafeConcurrentReads() throws InterruptedException {
        int threadCount = 20;
        int iterationsPerThread = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger errorCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < iterationsPerThread; j++) {
                        // Mix of different angles for atan2 testing
                        float angle = (threadId * 37 + j) % 360;
                        MathCache.atan2(angle, angle + 1);
                        MathCache.atan2(angle + 1, angle);
                        MathCache.atan2(-angle, angle);
                    }
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        assertThat(errorCount.get())
                .as("No errors should occur during concurrent access")
                .isZero();
    }

    // ========== Edge Cases and Robustness ==========

    @Test
    @DisplayName("Should handle NaN and Infinity gracefully")
    void testSpecialFloatingPointValues() {
        // These shouldn't throw exceptions, though results may not be meaningful
        assertThatCode(() -> {
            MathCache.atan2(Double.NaN, 1.0);
            MathCache.atan2(1.0, Double.NaN);
            MathCache.atan2(Double.POSITIVE_INFINITY, 1.0);
            MathCache.atan2(1.0, Double.NEGATIVE_INFINITY);
            MathCache.atan2(0.0, 0.0); // Edge case: both zero
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should produce consistent results for repeated calls")
    void testConsistency() {
        double y = 3.14;
        double x = 2.71;

        float result1 = MathCache.atan2(y, x);
        float result2 = MathCache.atan2(y, x);
        float result3 = MathCache.atan2(y, x);

        assertThat(result1)
                .as("Repeated calls with same input should return identical results")
                .isEqualTo(result2)
                .isEqualTo(result3);
    }
}