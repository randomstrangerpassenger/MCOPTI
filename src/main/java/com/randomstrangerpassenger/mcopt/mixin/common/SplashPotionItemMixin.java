package com.randomstrangerpassenger.mcopt.mixin.common;

import com.randomstrangerpassenger.mcopt.config.GameplayConfig;
import net.minecraft.world.item.SplashPotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Mixin to allow splash potions to stack.
 * <p>
 * Similar to PotionItemMixin but for throwable splash potions.
 */
@Mixin(SplashPotionItem.class)
public class SplashPotionItemMixin {

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;"), index = 0)
    private int modifySplashPotionStackSize(int original) {
        if (GameplayConfig.ENABLE_POTION_STACKING.get()) {
            return GameplayConfig.POTION_STACK_SIZE.get();
        }
        return original;
    }
}
