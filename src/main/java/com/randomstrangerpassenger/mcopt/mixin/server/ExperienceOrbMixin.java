package com.randomstrangerpassenger.mcopt.mixin.server;

<<<<<<< HEAD
import com.randomstrangerpassenger.mcopt.util.FeatureToggles;
import com.randomstrangerpassenger.mcopt.util.FeatureKey;
=======
import com.randomstrangerpassenger.mcopt.server.entity.xp.XpOrbHandler;
import com.randomstrangerpassenger.mcopt.util.FeatureToggles;
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
import net.minecraft.world.entity.ExperienceOrb;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
<<<<<<< HEAD
 * Optimizes experience orb performance by merging nearby orbs into single
 * entities.
 * This significantly reduces entity count and improves performance when many
 * orbs spawn.
 * <p>
 * Business logic is delegated to {@link XpOrbHandler} for better
 * maintainability.
=======
 * Optimizes experience orb performance by merging nearby orbs into single entities.
 * This significantly reduces entity count and improves performance when many orbs spawn.
 * <p>
 * Business logic is delegated to {@link XpOrbHandler} for better maintainability.
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
 */
@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin {

    @Unique
    private int mcopt$mergeCheckTimer = 0;

    /**
<<<<<<< HEAD
     * Periodically checks for nearby experience orbs and merges them to reduce
     * entity count.
=======
     * Periodically checks for nearby experience orbs and merges them to reduce entity count.
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
     * This is especially helpful during mob farming or mining operations.
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
<<<<<<< HEAD
        if (!FeatureToggles.isEnabled(FeatureKey.XP_ORB_MERGING)) {
=======
        if (!FeatureToggles.isXpOrbMergingEnabled()) {
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
            return;
        }

        ExperienceOrb thisOrb = (ExperienceOrb) (Object) this;

        // Only process on server side or in singleplayer
<<<<<<< HEAD
        if (thisOrb.level().isClientSide()) {
=======
        if (thisOrb.level().isClientSide) {
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
            return;
        }

        // Increment timer and check if it's time to merge
        mcopt$mergeCheckTimer++;
<<<<<<< HEAD
        int mergeDelay = 40; // Default delay since handler is disabled
=======
        int mergeDelay = XpOrbHandler.getMergeCheckDelay();
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0

        if (mcopt$mergeCheckTimer < mergeDelay) {
            return;
        }

        // Reset timer
        mcopt$mergeCheckTimer = 0;

        // Delegate merging logic to handler
<<<<<<< HEAD
        // XpOrbHandler.mergeNearbyOrbs(thisOrb);
=======
        XpOrbHandler.mergeNearbyOrbs(thisOrb);
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
    }
}
