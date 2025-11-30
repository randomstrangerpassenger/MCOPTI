package com.randomstrangerpassenger.mcopt.util;

/**
 * Central location for commonly used constants in MCOPT.
 * Extracting these values improves maintainability and reduces magic numbers in the codebase.
 */
public final class MCOPTConstants {

    private MCOPTConstants() {
        // Utility class - prevent instantiation
    }

    /**
     * Minecraft world constants
     */
    public static final class Minecraft {
        private Minecraft() {}

        /** Number of blocks in one chunk section (width/length/height) */
        public static final int CHUNK_SECTION_SIZE = 16;

        /** Chunk section size as a double for calculations */
        public static final double CHUNK_SECTION_SIZE_DOUBLE = 16.0;

        /** Half of chunk section size - offset to chunk center */
        public static final double CHUNK_CENTER_OFFSET = 8.0;

        /** Distance threshold for entity back-face culling (in blocks squared) */
        public static final double ENTITY_BACKFACE_CULLING_DISTANCE_SQ = 256.0; // 16 blocks squared
    }

    /**
     * UI and rendering constants
     */
    public static final class UI {
        private UI() {}

        /** Default horizontal margin for HUD elements */
        public static final int HUD_MARGIN_X = 5;

        /** Default vertical margin for HUD elements */
        public static final int HUD_MARGIN_Y = 5;

        /** White color (ARGB format) */
        public static final int COLOR_WHITE = 0xFFFFFF;

        /** Black color (ARGB format) */
        public static final int COLOR_BLACK = 0x000000;

        /** Memory HUD update interval in milliseconds */
        public static final long MEMORY_HUD_UPDATE_INTERVAL_MS = 1000L;

        /** Bytes per megabyte conversion factor */
        public static final long BYTES_PER_MB = 1024L * 1024L;
    }

    /**
     * Performance and timing constants
     */
    public static final class Performance {
        private Performance() {}

        /** Cooldown period for panic button to prevent spam (milliseconds) */
        public static final long PANIC_BUTTON_COOLDOWN_MS = 5000L;

        /** Wait time after triggering garbage collection (milliseconds) */
        public static final long GC_WAIT_TIME_MS = 100L;

        /** Back-face culling dot product threshold */
        public static final double BACKFACE_CULLING_DOT_THRESHOLD = -0.5;

        /** Milliseconds per second conversion factor */
        public static final long MILLIS_PER_SECOND = 1000L;
    }

    /**
     * Fishing mechanics constants
     */
    public static final class Fishing {
        private Fishing() {}

        /**
         * Maximum distance squared between player and fishing hook before the line breaks.
         * This matches vanilla Minecraft's fishing rod range limit (32 blocks squared = 1024).
         */
        public static final double MAX_FISHING_DISTANCE_SQUARED = 1024.0D;
    }

    /**
     * World and dimension constants
     */
    public static final class World {
        private World() {}

        /**
         * Minimum Y coordinate in Minecraft worlds (build limit).
         * Valid range: -64 to 320 (as of Minecraft 1.18+)
         */
        public static final int MIN_WORLD_Y = -64;

        /**
         * Maximum Y coordinate in Minecraft worlds (build limit).
         * Valid range: -64 to 320 (as of Minecraft 1.18+)
         */
        public static final int MAX_WORLD_Y = 320;

        /**
         * Maximum X/Z coordinate before reaching world border limit.
         * Minecraft's max world border is ±29,999,984 blocks.
         */
        public static final int MAX_WORLD_COORDINATE = 29_999_984;

        /**
         * Offset to center of a block (0.5 blocks from corner).
         * Used for teleportation and entity placement.
         */
        public static final double BLOCK_CENTER_OFFSET = 0.5;

        /**
         * Small Y offset for portal teleportation to prevent falling through blocks.
         * 0.1 blocks above the portal block ensures stable landing.
         */
        public static final double PORTAL_Y_OFFSET = 0.1;
    }

    /**
     * Input and event handling constants
     */
    public static final class Input {
        private Input() {}

        /**
         * GLFW key/mouse action code for key release.
         * Used to detect when a key or mouse button is released.
         */
        public static final int INPUT_ACTION_RELEASE = 0;
    }
}
