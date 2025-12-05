package com.randomstrangerpassenger.mcopt.server.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;

import java.util.Objects;

/**
 * Handler for world generation fixes, particularly for lake feature generation.
 * <p>
 * Prevents crashes caused by lake generation attempting to check biomes in
 * unloaded chunks.
 */
public class WorldGenFixes {

    /**
     * Safely checks if a biome at a given position should freeze water.
     * <p>
     * This method ensures that the chunk at the position is loaded before
     * attempting
     * to retrieve biome information, preventing crashes during custom terrain
     * generation.
     *
     * @param level The world generation level
     * @param pos   The block position to check
     * @return true if the biome should freeze water, false if chunk is unloaded or
     *         biome doesn't freeze
     */
    public static boolean shouldBiomeFreezeWaterSafely(WorldGenLevel level, BlockPos pos) {
        try {
            // Check if the chunk at this position is loaded
            int chunkX = pos.getX() >> 4;
            int chunkZ = pos.getZ() >> 4;

            // Attempt to get the chunk without forcing generation
            ChunkStatus emptyStatus = Objects.requireNonNull(ChunkStatus.EMPTY, "ChunkStatus.EMPTY cannot be null");
            ChunkAccess chunk = level.getChunk(chunkX, chunkZ, emptyStatus, false);

            if (chunk == null) {
                // Chunk is not loaded, return false (don't freeze)
                return false;
            }

            // Chunk is loaded, safe to check biome
            Biome biome = level.getBiome(pos).value();
            return biome.shouldFreeze(level, pos);

        } catch (Exception e) {
            // If any exception occurs, log and return false as a safe default
            // This prevents crashes at the cost of potentially not freezing a lake edge
            return false;
        }
    }

    /**
     * Safely checks if a biome at a given position should snow.
     * <p>
     * Similar to {@link #shouldBiomeFreezeWaterSafely}, but checks for snow
     * precipitation.
     *
     * @param level The world generation level
     * @param pos   The block position to check
     * @return true if the biome should have snow, false if chunk is unloaded or
     *         biome doesn't snow
     */
    public static boolean shouldBiomeSnowSafely(WorldGenLevel level, BlockPos pos) {
        try {
            // Check if the chunk at this position is loaded
            int chunkX = pos.getX() >> 4;
            int chunkZ = pos.getZ() >> 4;

            // Attempt to get the chunk without forcing generation
            ChunkStatus emptyStatus = Objects.requireNonNull(ChunkStatus.EMPTY, "ChunkStatus.EMPTY cannot be null");
            ChunkAccess chunk = level.getChunk(chunkX, chunkZ, emptyStatus, false);

            if (chunk == null) {
                // Chunk is not loaded, return false (no snow)
                return false;
            }

            // Chunk is loaded, safe to check biome
            Biome biome = level.getBiome(pos).value();
            return biome.shouldSnow(level, pos);

        } catch (Exception e) {
            // If any exception occurs, return false as a safe default
            return false;
        }
    }
}
