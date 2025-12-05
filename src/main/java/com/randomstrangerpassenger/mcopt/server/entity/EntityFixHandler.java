package com.randomstrangerpassenger.mcopt.server.entity;

import net.minecraft.world.entity.Entity;

/**
 * Interface for entity fix handlers.
 * <p>
 * Defines the contract for handlers that apply fixes or improvements to
 * specific entity types. This interface helps identify fix-type handlers
 * and provides metadata about their purpose.
 * </p>
 */
public interface EntityFixHandler {

    /**
     * Get the entity type that this fix applies to.
     *
     * @return the target entity class
     */
    Class<? extends Entity> getTargetEntityType();

    /**
     * Get a description of what this fix does.
     *
     * @return fix description
     */
    String getFixDescription();

    /**
     * Check if this fix is currently enabled.
     *
     * @return true if enabled, false otherwise
     */
    boolean isFixEnabled();
}
