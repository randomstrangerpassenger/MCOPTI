package com.randomstrangerpassenger.mcopt.server.entity.clearlag;

import java.util.Map;

/**
 * Immutable record holding cleanup statistics.
 * <p>
 * This data class encapsulates the results of a cleanup operation,
 * including total entities removed and a breakdown by category.
 * </p>
 */
public record CleanupStats(int totalRemoved, Map<EntityTypeCategory, Integer> countsByCategory) {

    /**
     * Get the count for a specific entity type category.
     *
     * @param category the entity type category
     * @return the count for that category, or 0 if not present
     */
    public int getCountForCategory(EntityTypeCategory category) {
        return countsByCategory.getOrDefault(category, 0);
    }

    /**
     * Check if any entities were removed.
     *
     * @return true if at least one entity was removed
     */
    public boolean hasRemovedEntities() {
        return totalRemoved > 0;
    }
}
