package com.randomstrangerpassenger.mcopt.client.rendering;

import com.randomstrangerpassenger.mcopt.config.RenderingConfig;
import com.randomstrangerpassenger.mcopt.util.MCOPTConstants;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

/**
 * Handles entity culling logic for rendering optimization.
 * <p>
 * Separates business logic from EntityRenderDispatcherMixin to improve
 * testability and maintainability.
 */
public final class EntityCullingHandler {

    private EntityCullingHandler() {
        // Utility class
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
            Camera camera,
            int cullingDistance) {

        if (!RenderingConfig.ENABLE_ENTITY_CULLING.get()) {
            return false;
        }

        Vec3 cameraPos = camera.getPosition();
        Vec3 entityPos = entity.position();

        // Distance culling
        double distanceSquared = cameraPos.distanceToSqr(entityPos);
        double maxDistanceSquared = cullingDistance * cullingDistance;

        if (distanceSquared > maxDistanceSquared) {
            // Don't cull players or vehicles the player is in
            if (!entity.isVehicle() && !entity.isPassenger()) {
                return true; // Cull
            }
        }

        // Backface culling (entities behind camera)
        if (RenderingConfig.CULL_ENTITIES_BEHIND_WALLS.get()) {
            Vector3f look = camera.getLookVector();
            Vec3 viewVector = new Vec3(look.x(), look.y(), look.z());
            Vec3 toEntity = entityPos.subtract(cameraPos).normalize();

            double dotProduct = viewVector.dot(toEntity);
            if (dotProduct < MCOPTConstants.Performance.BACKFACE_CULLING_DOT_THRESHOLD
                    && distanceSquared > MCOPTConstants.Minecraft.ENTITY_BACKFACE_CULLING_DISTANCE_SQ) {
                return true; // Cull
            }
        }

        return false; // Don't cull
    }
}
