package com.randomstrangerpassenger.mcopt.util;

/**
 * Centralized constants for MCOPT.
 * <p>
 * This class consolidates all magic numbers, hardcoded strings, and common
 * constants used throughout the codebase. This improves maintainability and
 * reduces the chance of typos or inconsistencies.
 * </p>
 */
public final class MCOPTConstants {

        private MCOPTConstants() {
                // Utility class - no instantiation
        }

        /**
         * NBT tag keys used by MCOPT.
         */
        public static final class NBT {
                private NBT() {
                }

                /**
                 * Extended amplifier key for potion effects (int instead of byte).
                 * Used to support amplifier values beyond 127.
                 */
                public static final String AMPLIFIER_INT = "mcopt:AmplifierInt";
        }

        /**
         * Time-related constants (in ticks unless otherwise specified).
         */
        public static final class Time {
                private Time() {
                }

                /** Ticks per second in Minecraft */
                public static final int TICKS_PER_SECOND = 20;

                /** Cooldown for displaying messages to prevent spam (2 seconds) */
                public static final int MESSAGE_COOLDOWN_TICKS = 40;
        }

        /**
         * Minecraft world constants.
         */
        public static final class Minecraft {
                private Minecraft() {
                }

                /** Number of blocks in one chunk section (width/length/height) */
                public static final int CHUNK_SECTION_SIZE = 16;

                /** Chunk section size as a double for calculations */
                public static final double CHUNK_SECTION_SIZE_DOUBLE = 16.0;

                /** Half of chunk section size - offset to chunk center */
                public static final double CHUNK_CENTER_OFFSET = 8.0;

                /** Ticks per second in Minecraft (20 ticks = 1 second) */
                public static final double TICKS_PER_SECOND_DOUBLE = 20.0;

                /** Distance threshold for entity back-face culling (in blocks squared) */
                public static final double ENTITY_BACKFACE_CULLING_DISTANCE_SQ = 256.0; // 16 blocks squared
        }

        /**
         * World and dimension constants.
         */
        public static final class World {
                private World() {
                }

                /** Minimum build height in the Overworld */
                public static final int OVERWORLD_MIN_HEIGHT = -64;

                /** Maximum build height in the Overworld */
                public static final int OVERWORLD_MAX_HEIGHT = 320;
        }

        /**
         * Resource location and namespace constants.
         */
        public static final class Resources {
                private Resources() {
                }

                /** MCOPT mod ID */
                public static final String MOD_ID = "mcopt";

                /** Minecraft namespace */
                public static final String MINECRAFT = "minecraft";
        }

        /**
         * Memory-related constants.
         */
        public static final class Memory {
                private Memory() {
                }

                /** Bytes per megabyte */
                public static final long BYTES_PER_MB = 1024L * 1024L;

                /** Bytes per gigabyte */
                public static final long BYTES_PER_GB = 1024L * 1024L * 1024L;
        }

        /**
         * UI and rendering constants.
         */
        public static final class UI {
                private UI() {
                }

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
        }

        /**
         * Performance and timing constants.
         */
        public static final class Performance {
                private Performance() {
                }

                /** Cooldown period for panic button to prevent spam (milliseconds) */
                public static final long PANIC_BUTTON_COOLDOWN_MS = 5000L;

                /** Wait time after triggering garbage collection (milliseconds) */
                public static final long GC_WAIT_TIME_MS = 100L;

                /** Back-face culling dot product threshold */
                public static final double BACKFACE_CULLING_DOT_THRESHOLD = -0.5;

                /** Maximum number of particles that can spawn per tick */
                public static final int PARTICLE_SPAWN_LIMIT_PER_TICK = 256;

                /** Milliseconds per second */
                public static final long MILLIS_PER_SECOND = 1000L;

                /** Default render distance minimum */
                public static final int MIN_RENDER_DISTANCE = 4;

                /** Default render distance maximum */
                public static final int MAX_RENDER_DISTANCE = 32;

                /** Render distance reduction for idle state */
                public static final int RENDER_DISTANCE_REDUCTION = 4;
        }

        /**
         * Input constants.
         */
        public static final class Input {
                private Input() {
                }

                /** GLFW Input action: Release */
                public static final int INPUT_ACTION_RELEASE = 0; // GLFW_RELEASE
        }

        /**
         * Fishing constants.
         */
        public static final class Fishing {
                private Fishing() {
                }

                /**
                 * Maximum squared distance for fishing hook before it's considered
                 * broken/detached
                 */
                public static final double MAX_FISHING_DISTANCE_SQUARED = 1024.0; // 32 blocks
        }
}
