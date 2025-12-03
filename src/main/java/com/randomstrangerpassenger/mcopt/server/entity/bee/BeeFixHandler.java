package com.randomstrangerpassenger.mcopt.server.entity.bee;

import com.randomstrangerpassenger.mcopt.MCOPT;
<<<<<<< HEAD
import com.randomstrangerpassenger.mcopt.config.GameplayConfig;
=======
import com.randomstrangerpassenger.mcopt.config.MCOPTConfig;
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

/**
 * Handles bee pathfinding stability logic.
<<<<<<< HEAD
 * Extracted from BeeStuckFixMixin to separate business logic from Mixin
 * injection code.
=======
 * Extracted from BeeStuckFixMixin to separate business logic from Mixin injection code.
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
 * <p>
 * Prevents bees from getting stuck trying to pathfind to unreachable hives.
 */
public final class BeeFixHandler {

    private BeeFixHandler() {
        // Utility class
    }

    /**
<<<<<<< HEAD
     * Checks if a bee is stuck trying to reach its hive and clears the stuck target
     * if needed.
     *
     * @param bee              The bee to check
=======
     * Checks if a bee is stuck trying to reach its hive and clears the stuck target if needed.
     *
     * @param bee The bee to check
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
     * @param pathingFailTicks The number of ticks the bee has failed to pathfind
     * @return The updated pathingFailTicks counter
     */
    public static int handleBeeStuckCheck(Bee bee, int pathingFailTicks) {
        BlockPos hive = bee.getHivePos();
        PathNavigation navigation = bee.getNavigation();

        if (hive == null || navigation == null) {
            return 0;
        }

        // Increment or reset fail counter based on navigation state
        int updatedFailTicks;
        if (navigation.isDone()) {
            updatedFailTicks = pathingFailTicks + 1;
        } else {
            updatedFailTicks = 0;
        }

        // Check if bee is stuck
<<<<<<< HEAD
        int timeout = GameplayConfig.BEE_STUCK_TIMEOUT_TICKS.get();
=======
        int timeout = MCOPTConfig.BEE_STUCK_TIMEOUT_TICKS.get();
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
        if (updatedFailTicks > timeout) {
            clearStuckHive(bee, timeout);
            return 0; // Reset counter after clearing
        }

        return updatedFailTicks;
    }

    /**
     * Clears a bee's stuck hive target and sets a cooldown.
     *
<<<<<<< HEAD
     * @param bee          The bee to clear the hive target for
     * @param timeoutTicks The timeout that triggered this clear (for logging)
     */
    private static void clearStuckHive(Bee bee, int timeoutTicks) {
        int cooldown = GameplayConfig.BEE_RELINK_COOLDOWN_TICKS.get();
        bee.setHivePos(null);
        bee.setStayOutOfHiveCountdown(cooldown);
        MCOPT.LOGGER.debug(
                "[BeeGuard] Cleared stalled hive target after {} ticks to prevent runaway pathing",
                timeoutTicks);
=======
     * @param bee The bee to clear the hive target for
     * @param timeoutTicks The timeout that triggered this clear (for logging)
     */
    private static void clearStuckHive(Bee bee, int timeoutTicks) {
        int cooldown = MCOPTConfig.BEE_RELINK_COOLDOWN_TICKS.get();
        bee.setHivePos(null);
        bee.setStayOutOfHiveCountdown(cooldown);
        MCOPT.LOGGER.debug(
            "[BeeGuard] Cleared stalled hive target after {} ticks to prevent runaway pathing",
            timeoutTicks
        );
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
    }
}
