package com.randomstrangerpassenger.mcopt.client.rendering;

import com.randomstrangerpassenger.mcopt.util.MCOPTConstants;
import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Objects;

/**
 * Abstract base class for culling handlers.
 * <p>
 * This class implements the common culling logic shared between all culling
 * handlers, reducing code duplication and improving maintainability.
 * </p>
 *
 * @param <T> the type of object to cull
 */
public abstract class AbstractCullingHandler<T> implements CullingHandler<T> {

    /**
     * Determine if the target should be culled based on common culling rules.
     * <p>
     * This method implements:
     * - Distance culling
     * - Backface culling (if enabled)
     * - Special exemptions (e.g., vehicles, passengers)
     * </p>
     */
    @Override
    public boolean shouldCull(T target, Camera camera, int cullingDistance) {
        if (!isEnabled()) {
            return false;
        }

        Vec3 cameraPos = Objects.requireNonNull(camera.getPosition(), "Camera position cannot be null");
        Vec3 targetPos = Objects.requireNonNull(getPosition(target), "Target position cannot be null");

        // Distance culling
        double distanceSquared = cameraPos.distanceToSqr(targetPos);
        double maxDistanceSquared = cullingDistance * cullingDistance;

        if (distanceSquared > maxDistanceSquared) {
            // Check for special exemptions (vehicles, passengers, etc.)
            if (!hasSpecialCullingExemption(target, distanceSquared)) {
                return true; // Cull
            }
        }

        // Backface culling (objects behind camera)
        if (supportsBackfaceCulling()) {
            if (isBackfacing(camera, cameraPos, targetPos, distanceSquared)) {
                return true; // Cull
            }
        }

        return false; // Don't cull
    }

    /**
     * Check if the target is behind the camera (backface culling).
     *
     * @param camera          the camera
     * @param cameraPos       the camera position
     * @param targetPos       the target position
     * @param distanceSquared the squared distance from camera to target
     * @return true if the target is backfacing and should be culled
     */
    protected boolean isBackfacing(Camera camera, Vec3 cameraPos, Vec3 targetPos, double distanceSquared) {
        Vector3f look = camera.getLookVector();
        Vec3 viewVector = Objects.requireNonNull(
                new Vec3(look.x(), look.y(), look.z()), "View vector cannot be null");
        Vec3 validCameraPos = Objects.requireNonNull(cameraPos, "Camera position cannot be null");
        Vec3 diff = Objects.requireNonNull(targetPos.subtract(validCameraPos), "Difference vector cannot be null");
        Vec3 toTarget = Objects.requireNonNull(diff.normalize(), "To-target vector cannot be null");

        double dotProduct = viewVector.dot(toTarget);

        // Cull if target is significantly behind camera and far enough away
        return dotProduct < MCOPTConstants.Performance.BACKFACE_CULLING_DOT_THRESHOLD
                && distanceSquared > MCOPTConstants.Minecraft.ENTITY_BACKFACE_CULLING_DISTANCE_SQ;
    }

    /**
     * Check if culling is enabled for this handler.
     * <p>
     * Subclasses should override this to check the appropriate config value.
     * </p>
     *
     * @return true if culling is enabled
     */
    protected abstract boolean isEnabled();

    /**
     * Default implementation: no special exemptions.
     * <p>
     * Subclasses can override this to provide custom exemption logic.
     * </p>
     */
    @Override
    public boolean hasSpecialCullingExemption(T target, double distanceSquared) {
        return false;
    }
}
