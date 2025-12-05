package com.randomstrangerpassenger.mcopt.util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Utility for handling interaction fallthrough logic.
 * <p>
 * When the main hand interaction fails (returns FAIL), this handler
 * automatically attempts to use the off-hand item instead.
 * </p>
 * <p>
 * This centralizes the logic that was previously duplicated in both
 * ServerPlayerGameModeMixin and MultiPlayerGameModeMixin.
 * </p>
 */
public class InteractionFallthroughHandler {

    private InteractionFallthroughHandler() {
        // Utility class
    }

    /**
     * Check if fallthrough should occur for this interaction.
     * <p>
     * Fallthrough occurs when:
     * 1. The hand is MAIN_HAND (not already off-hand)
     * 2. The result is FAIL
     * 3. The player has an item in their off-hand
     * </p>
     *
     * @param hand   the hand used for interaction
     * @param result the interaction result
     * @param player the player
     * @return true if fallthrough should occur
     */
    public static boolean shouldFallthrough(InteractionHand hand, InteractionResult result, Player player) {
        // Only process main hand failures
        if (hand != InteractionHand.MAIN_HAND) {
            return false;
        }

        // Only fallthrough on FAIL result
        if (result != InteractionResult.FAIL) {
            return false;
        }

        // Check if off-hand has an item
        ItemStack offHandItem = player.getOffhandItem();
        return !offHandItem.isEmpty();
    }

    /**
     * Check if an off-hand interaction result should replace the main hand result.
     * <p>
     * This occurs when the off-hand interaction consumes an action (success or
     * consume).
     * </p>
     *
     * @param offHandResult the result from the off-hand interaction
     * @return true if the off-hand result should be used
     */
    public static boolean shouldUseOffHandResult(InteractionResult offHandResult) {
        return offHandResult.consumesAction();
    }
}
