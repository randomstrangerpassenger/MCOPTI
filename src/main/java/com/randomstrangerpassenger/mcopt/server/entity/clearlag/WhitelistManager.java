package com.randomstrangerpassenger.mcopt.server.entity.clearlag;

import com.randomstrangerpassenger.mcopt.config.SafetyConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Manages the entity whitelist for ClearLag.
 * <p>
 * This component handles caching and validation of whitelisted entity types.
 * The whitelist is refreshed only when the configuration changes, avoiding
 * unnecessary parsing on every cleanup cycle.
 * </p>
 */
public class WhitelistManager {

    private Set<ResourceLocation> cachedWhitelist = Set.of();
    private List<? extends String> lastConfig = List.of();

    /**
     * Check if an entity is whitelisted and should not be removed.
     *
     * @param entity the entity to check
     * @return true if the entity is whitelisted
     */
    public boolean isWhitelisted(Entity entity) {
        updateCacheIfNeeded();
        EntityType<?> entityType = Objects.requireNonNull(entity.getType(), "Entity type cannot be null");
        ResourceLocation entityTypeKey = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
        return cachedWhitelist.contains(entityTypeKey);
    }

    /**
     * Update the whitelist cache if the configuration has changed.
     * <p>
     * Uses a for-loop instead of Stream API for better performance.
     * </p>
     */
    private void updateCacheIfNeeded() {
        List<? extends String> currentConfig = SafetyConfig.CLEAR_LAG_ENTITY_WHITELIST.get();

        // Only update if config has changed
        if (currentConfig.equals(lastConfig)) {
            return;
        }

        // Build new whitelist using for-loop for better performance
        Set<ResourceLocation> newWhitelist = new HashSet<>();
        for (String entry : currentConfig) {
            String validEntry = Objects.requireNonNull(entry, "Entry cannot be null");
            ResourceLocation loc = ResourceLocation.tryParse(validEntry);
            if (loc != null) {
                newWhitelist.add(loc);
            }
        }

        cachedWhitelist = Set.copyOf(newWhitelist);
        lastConfig = currentConfig;
    }

    /**
     * Get the current size of the whitelist.
     *
     * @return number of whitelisted entity types
     */
    public int getWhitelistSize() {
        updateCacheIfNeeded();
        return cachedWhitelist.size();
    }

    /**
     * Force a refresh of the whitelist cache.
     * Useful when config is reloaded externally.
     */
    public void forceRefresh() {
        lastConfig = List.of(); // Clear to force update on next check
    }
}
