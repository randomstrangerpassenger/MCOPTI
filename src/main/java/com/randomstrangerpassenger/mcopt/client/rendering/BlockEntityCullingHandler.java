package com.randomstrangerpassenger.mcopt.client.rendering;

import com.randomstrangerpassenger.mcopt.config.RenderingConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

/**
 * Handles block entity culling logic for rendering optimization.
 * <p>
 * Block entities (chests, signs, skulls, etc.) can cause significant FPS drops
 * in large warehouses or storage rooms. This handler culls block entities that:
 * - Are too far from the camera
 * - Are behind the camera
 * - Are behind walls (optional)
 * </p>
 * <p>
 * Refactored to use {@link AbstractCullingHandler} to eliminate code
 * duplication.
 * </p>
 */
public final class BlockEntityCullingHandler extends AbstractCullingHandler<BlockPos> {

    private static final BlockEntityCullingHandler INSTANCE = new BlockEntityCullingHandler();

    private BlockEntityCullingHandler() {
        // Singleton
    }

    /**
     * Determines if a block entity should be culled from rendering.
     *
     * @param blockPos        Position of the block entity
     * @param camera          Current camera
     * @param cullingDistance Maximum render distance
     * @return true if block entity should be culled, false if it should be
     *         rendered
     */
    public static boolean shouldCullBlockEntity(
            BlockPos blockPos,
            net.minecraft.client.Camera camera,
            int cullingDistance) {
        return INSTANCE.shouldCull(blockPos, camera, cullingDistance);
    }

    @Override
    public Vec3 getPosition(BlockPos blockPos) {
        BlockPos validPos = Objects.requireNonNull(blockPos, "BlockPos cannot be null");
        return Vec3.atCenterOf(validPos);
    }

    @Override
    public boolean supportsBackfaceCulling() {
        return RenderingConfig.CULL_BLOCK_ENTITIES_BEHIND_WALLS.get();
    }

    @Override
    protected boolean isEnabled() {
        return RenderingConfig.ENABLE_BLOCK_ENTITY_CULLING.get();
    }

    // Block entities have no special exemptions, use default implementation
}
