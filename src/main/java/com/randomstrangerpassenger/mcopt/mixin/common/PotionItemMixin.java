package com.randomstrangerpassenger.mcopt.mixin.common;

import com.randomstrangerpassenger.mcopt.config.GameplayConfig;
import net.minecraft.world.item.PotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Mixin to allow potions to stack.
 * <p>
 * Problem: In vanilla Minecraft, potions do not stack, making inventory
 * management
 * cumbersome when carrying multiple potions of the same type.
 * <p>
 * Solution: Modify the maxStackSize parameter in the PotionItem constructor
 * to allow stacking up to a configurable amount (default: 16).
 * <p>
 * Inspired by Potion Fixes mod but implemented independently for MCOPT.
 */
@Mixin(PotionItem.class)
public class PotionItemMixin {

    /**
     * Modifies the stack size parameter when creating a PotionItem.
     * 
     * @param original The original stack size (vanilla: 1)
     * @return The modified stack size from config, or original if feature disabled
     */
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;"), index = 0)
    private int modifyPotionStackSize(int original) {
        if (GameplayConfig.ENABLE_POTION_STACKING.get()) {
            return GameplayConfig.POTION_STACK_SIZE.get();
        }
        return original;
    }
}
