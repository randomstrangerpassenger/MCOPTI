package com.randomstrangerpassenger.mcopt.server.entity.clearlag;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Projectile;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Executes entity cleanup operations.
 * <p>
 * This component handles the actual cleanup logic, including entity iteration,
 * filtering, and removal. It uses reusable data structures to reduce GC
 * pressure
 * and improve performance.
 * </p>
 */
public class CleanupExecutor {

    // Reusable data structures to reduce GC pressure
    private final List<Entity> toRemove = new ArrayList<>(1000);
    private final Map<EntityTypeCategory, Integer> counts = new EnumMap<>(EntityTypeCategory.class);

    /**
     * Perform cleanup across all provided levels.
     *
     * @param levels the server levels to clean
     * @param filter the filter to determine which entities to remove
     * @return statistics about the cleanup operation
     */
    public CleanupStats performCleanup(Iterable<ServerLevel> levels, EntityTypeFilter filter) {
        // Clear reusable collections
        toRemove.clear();
        counts.clear();

        // Collect entities to remove (single pass)
        for (ServerLevel level : levels) {
            for (Entity entity : level.getAllEntities()) {
                if (filter.shouldRemove(entity)) {
                    toRemove.add(entity);
                    EntityTypeCategory category = categorize(entity);
                    // Use explicit lambda to avoid Integer::sum unboxing warning
                    counts.merge(category, 1, (a, b) -> a + b);
                }
            }
        }

        // Remove all collected entities (single pass)
        for (Entity entity : toRemove) {
            entity.discard();
        }

        // Return immutable statistics
        return new CleanupStats(toRemove.size(), Map.copyOf(counts));
    }

    /**
     * Categorize an entity by type.
     *
     * @param entity the entity to categorize
     * @return the entity's category
     */
    private EntityTypeCategory categorize(Entity entity) {
        if (entity instanceof ItemEntity) {
            return EntityTypeCategory.ITEM;
        }
        if (entity instanceof ExperienceOrb) {
            return EntityTypeCategory.XP_ORB;
        }
        if (entity instanceof Projectile) {
            return EntityTypeCategory.PROJECTILE;
        }
        return EntityTypeCategory.OTHER;
    }

    /**
     * Get current capacity of the toRemove list.
     * Useful for monitoring and debugging.
     *
     * @return the current list capacity
     */
    public int getToRemoveCapacity() {
        return toRemove.size();
    }
}
