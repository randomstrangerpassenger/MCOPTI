package com.randomstrangerpassenger.mcopt.server.entity.xp;

<<<<<<< HEAD
import com.randomstrangerpassenger.mcopt.config.GameplayConfig;
import com.randomstrangerpassenger.mcopt.mixin.accessor.ExperienceOrbAccessor;
=======
import com.randomstrangerpassenger.mcopt.config.MCOPTConfig;
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * Handles experience orb merging logic.
<<<<<<< HEAD
 * Extracted from ExperienceOrbMixin to separate business logic from Mixin
 * injection code.
=======
 * Extracted from ExperienceOrbMixin to separate business logic from Mixin injection code.
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
 */
public final class XpOrbHandler {

    // Cache config values to avoid repeated .get() calls
<<<<<<< HEAD
    private static double cachedMergeRadius = GameplayConfig.XP_ORB_MERGE_RADIUS.get();
    private static int cachedMergeDelay = GameplayConfig.XP_ORB_MERGE_DELAY.get();
=======
    private static double cachedMergeRadius = MCOPTConfig.XP_ORB_MERGE_RADIUS.get();
    private static int cachedMergeDelay = MCOPTConfig.XP_ORB_MERGE_DELAY.get();
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0

    private XpOrbHandler() {
        // Utility class
    }

    /**
     * Refreshes cached config values.
     * Call this when config is reloaded.
     */
    public static void refreshConfigCache() {
<<<<<<< HEAD
        cachedMergeRadius = GameplayConfig.XP_ORB_MERGE_RADIUS.get();
        cachedMergeDelay = GameplayConfig.XP_ORB_MERGE_DELAY.get();
=======
        cachedMergeRadius = MCOPTConfig.XP_ORB_MERGE_RADIUS.get();
        cachedMergeDelay = MCOPTConfig.XP_ORB_MERGE_DELAY.get();
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
    }

    /**
     * Attempts to merge nearby experience orbs into the given orb.
     *
     * @param targetOrb The orb that will absorb nearby orbs
     * @return The number of orbs that were merged
     */
    public static int mergeNearbyOrbs(ExperienceOrb targetOrb) {
        // Don't process if this orb is already being removed
        if (!targetOrb.isAlive() || targetOrb.isRemoved()) {
            return 0;
        }

        // Create bounding box for searching nearby orbs using cached radius
        AABB searchBox = targetOrb.getBoundingBox().inflate(cachedMergeRadius);

        // Find nearby experience orbs
        List<ExperienceOrb> nearbyOrbs = targetOrb.level().getEntitiesOfClass(
                ExperienceOrb.class,
                searchBox,
<<<<<<< HEAD
                orb -> orb != targetOrb && orb.isAlive() && !orb.isRemoved());
=======
                orb -> orb != targetOrb && orb.isAlive() && !orb.isRemoved()
        );
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0

        // Merge nearby orbs into the target one
        int mergedCount = 0;
        for (ExperienceOrb nearbyOrb : nearbyOrbs) {
            // Add the value of the nearby orb to the target
            int targetValue = targetOrb.getValue();
            int nearbyValue = nearbyOrb.getValue();
<<<<<<< HEAD
            ((ExperienceOrbAccessor) targetOrb).setValue(targetValue + nearbyValue);
=======
            targetOrb.value = targetValue + nearbyValue;
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0

            // Remove the merged orb
            nearbyOrb.discard();
            mergedCount++;
        }

        // If we merged any orbs, reset the count to prevent premature despawn
        if (mergedCount > 0) {
            // The count field affects despawn timing, reset it after merging
<<<<<<< HEAD
            ((ExperienceOrbAccessor) targetOrb).setCount(0);
=======
            targetOrb.count = 0;
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
        }

        return mergedCount;
    }

    /**
     * Gets the configured merge check delay in ticks.
     *
     * @return The number of ticks to wait between merge checks
     */
    public static int getMergeCheckDelay() {
        return cachedMergeDelay;
    }
}
