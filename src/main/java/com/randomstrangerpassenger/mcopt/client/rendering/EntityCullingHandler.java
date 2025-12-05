package com.randomstrangerpassenger.mcopt.client.rendering;

import com.randomstrangerpassenger.mcopt.config.RenderingConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

/**
 * Handles entity culling logic for rendering optimization.
 * <p>
 * Refactored to use {@link AbstractCullingHandler} to eliminate code
 * duplication.
 * </p>
 */
public final class EntityCullingHandler extends AbstractCullingHandler<Entity> {

    private static final EntityCullingHandler INSTANCE = new EntityCullingHandler();

    private EntityCullingHandler() {
        // Singleton
    }

    /**
     * Determines if an entity should be culled from rendering.
     * 
     * @param entity          Entity to check
     * @param camera          Current camera
     * @param cullingDistance Maximum render distance
     * @return true if entity should be culled, false if it should be rendered
     */
    public static boolean shouldCullEntity(
            Entity entity,
            net.minecraft.client.Camera camera,
            int cullingDistance) {
        return INSTANCE.shouldCull(entity, camera, cullingDistance);
    }

    @Override
    public Vec3 getPosition(Entity entity) {
        return entity.position();
    }

    @Override
    public boolean supportsBackfaceCulling() {
        return RenderingConfig.CULL_ENTITIES_BEHIND_WALLS.get();
    }

    @Override
    protected boolean isEnabled() {
        return RenderingConfig.ENABLE_ENTITY_CULLING.get();
    }

    @Override
    public boolean hasSpecialCullingExemption(Entity entity, double distanceSquared) {
        // Don't cull players or vehicles the player is in
        return entity.isVehicle() || entity.isPassenger();
    }
}
