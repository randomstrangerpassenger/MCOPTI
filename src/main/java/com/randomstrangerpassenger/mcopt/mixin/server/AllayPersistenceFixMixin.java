package com.randomstrangerpassenger.mcopt.mixin.server;

import com.randomstrangerpassenger.mcopt.config.SafetyConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.allay.Allay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to prevent Allay from despawning when holding items.
 * <p>
 * Problem: In vanilla Minecraft, Allay can despawn even when holding items that
 * the player instructed it to collect. This causes players to lose their Allay
 * helpers during long-distance item collection tasks.
 * <p>
 * Solution: Override the requiresCustomPersistence method to return true when
 * the Allay is holding an item in its main hand, preventing it from despawning.
 * <p>
 * Inspired by the AllayFix mod but implemented independently for MCOPT.
 */
@Mixin(Allay.class)
public class AllayPersistenceFixMixin {

    /**
     * Prevents Allay from despawning when it's holding an item.
     * <p>
     * This method injects at the HEAD of requiresCustomPersistence and returns
     * true if the Allay has an item in its main hand, preventing despawn.
     * If no item is held, the vanilla logic proceeds normally.
     *
     * @param cir Callback info returnable for the boolean result
     */
    @Inject(method = "requiresCustomPersistence", at = @At("HEAD"), cancellable = true)
    private void onRequiresCustomPersistence(CallbackInfoReturnable<Boolean> cir) {
        if (!SafetyConfig.ENABLE_ALLAY_PERSISTENCE_FIX.get()) {
            return;
        }

        // Cast this to Allay to access its methods
        Allay allay = (Allay) (Object) this;

        // If the Allay is holding an item, prevent despawn
        if (!allay.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            cir.setReturnValue(true);
        }
    }
}
