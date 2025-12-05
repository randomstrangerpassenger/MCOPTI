package com.randomstrangerpassenger.mcopt.command.reporters;

import com.randomstrangerpassenger.mcopt.command.format.StatusFormatter;
import com.randomstrangerpassenger.mcopt.config.PerformanceConfig;
import com.randomstrangerpassenger.mcopt.config.RenderingConfig;
import com.randomstrangerpassenger.mcopt.config.SafetyConfig;
import net.minecraft.commands.CommandSourceStack;

/**
 * Reports on memory usage and performance configuration.
 */
public class MemoryStatusReporter {

    private MemoryStatusReporter() {
        // Utility class
    }

    /**
     * Send memory status to the command source.
     */
    public static void sendMemoryStatus(CommandSourceStack source) {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long totalMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long usedMemory = totalMemory - freeMemory;
        int memoryPercent = (int) ((usedMemory * 100) / maxMemory);

        source.sendSuccess(() -> StatusFormatter.formatEmptyLine(), false);
        source.sendSuccess(() -> StatusFormatter.formatHeader("Memory Usage:"), false);
        source.sendSuccess(() -> StatusFormatter.formatMemoryUsage(usedMemory, maxMemory, memoryPercent), false);
    }

    /**
     * Send configuration details to the command source.
     */
    public static void sendConfigurationStatus(CommandSourceStack source) {
        source.sendSuccess(() -> StatusFormatter.formatEmptyLine(), false);
        source.sendSuccess(() -> StatusFormatter.formatHeader("Configuration:"), false);

        if (RenderingConfig.ENABLE_ENTITY_CULLING.get()) {
            source.sendSuccess(() -> StatusFormatter.formatConfig(
                    "Entity Culling Distance",
                    RenderingConfig.ENTITY_CULLING_DISTANCE.get() + " blocks"), false);
        }

        if (RenderingConfig.ENABLE_BLOCK_ENTITY_CULLING.get()) {
            source.sendSuccess(() -> StatusFormatter.formatConfig(
                    "Block Entity Culling Distance",
                    RenderingConfig.BLOCK_ENTITY_CULLING_DISTANCE.get() + " blocks"), false);
        }

        if (SafetyConfig.ENABLE_PER_CHUNK_ENTITY_LIMIT.get()) {
            source.sendSuccess(() -> StatusFormatter.formatConfig(
                    "Max Entities Per Chunk",
                    SafetyConfig.MAX_ENTITIES_PER_CHUNK.get()), false);
        }

        if (PerformanceConfig.ENABLE_MEMORY_OPTIMIZATIONS.get()) {
            source.sendSuccess(() -> StatusFormatter.formatConfig(
                    "Memory Optimizations", "Active"), false);
        }
    }
}
