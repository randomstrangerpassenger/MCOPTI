package com.randomstrangerpassenger.mcopt.server.entity.clearlag;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Projectile;

/**
 * Filters entities to determine which should be removed during cleanup.
 * <p>
 * This component encapsulates all the logic for deciding whether an entity
 * should be removed, including whitelist checks, custom name protection,
 * and entity type filtering.
 * </p>
 */
public class EntityTypeFilter {

    private final WhitelistManager whitelistManager;

    // Cached config values to avoid repeated .get() calls
    private boolean removeItems;
    private boolean removeXpOrbs;
    private boolean removeProjectiles;
    private boolean skipNamedItems;

    public EntityTypeFilter(WhitelistManager whitelistManager) {
        this.whitelistManager = whitelistManager;
    }

    /**
     * Refresh cached configuration values.
     * Call this when config is reloaded.
     */
    public void refreshConfig(boolean removeItems,
            boolean removeXpOrbs,
            boolean removeProjectiles,
            boolean skipNamedItems) {
        this.removeItems = removeItems;
        this.removeXpOrbs = removeXpOrbs;
        this.removeProjectiles = removeProjectiles;
        this.skipNamedItems = skipNamedItems;
    }

    /**
     * Determine if an entity should be removed.
     *
     * @param entity the entity to check
     * @return true if the entity should be removed
     */
    public boolean shouldRemove(Entity entity) {
        // Skip non-alive entities
        if (!entity.isAlive()) {
            return false;
        }

        // Check whitelist
        if (whitelistManager.isWhitelisted(entity)) {
            return false;
        }

        // Check named items protection
        if (skipNamedItems && entity.hasCustomName()) {
            return false;
        }

        // Check entity type
        // Note: These instanceof checks are already optimal in Java 21
        // No need for pattern matching since we don't use the casted variable
        if (entity instanceof ItemEntity) {
            return removeItems;
        }

        if (entity instanceof ExperienceOrb) {
            return removeXpOrbs;
        }

        if (entity instanceof Projectile) {
            return removeProjectiles;
        }

        return false;
    }
}
