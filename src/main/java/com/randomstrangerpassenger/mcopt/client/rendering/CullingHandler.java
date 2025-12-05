package com.randomstrangerpassenger.mcopt.client.rendering;

import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;

/**
 * Generic interface for culling handlers.
 * <p>
 * This interface defines the contract for all culling handlers, allowing them
 * to be used polymorphically and reducing code duplication.
 * </p>
 *
 * @param <T> the type of object to cull (Entity, BlockPos, etc.)
 */
public interface CullingHandler<T> {

    /**
     * Determine if the given target should be culled from rendering.
     *
     * @param target          the object to check for culling
     * @param camera          the current camera
     * @param cullingDistance the maximum render distance
     * @return true if the target should be culled, false otherwise
     */
    boolean shouldCull(T target, Camera camera, int cullingDistance);

    /**
     * Get the position of the target as a Vec3.
     *
     * @param target the target object
     * @return the position of the target
     */
    Vec3 getPosition(T target);

    /**
     * Check if this handler supports backface culling.
     *
     * @return true if backface culling is supported and enabled
     */
    boolean supportsBackfaceCulling();

    /**
     * Check if the target has special conditions that prevent culling.
     * <p>
     * For example, entities that are vehicles or passengers should not be culled
     * even if they are far away.
     * </p>
     *
     * @param target          the target to check
     * @param distanceSquared the squared distance from camera
     * @return true if the target should never be culled
     */
    boolean hasSpecialCullingExemption(T target, double distanceSquared);
}
