package com.randomstrangerpassenger.mcopt.mixin.server;

import com.randomstrangerpassenger.mcopt.server.entity.bee.BeeFixHandler;
<<<<<<< HEAD
import com.randomstrangerpassenger.mcopt.config.GameplayConfig;
=======
import com.randomstrangerpassenger.mcopt.config.MCOPTConfig;
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Prevents bees from getting stuck trying to pathfind to unreachable hives.
 * <p>
<<<<<<< HEAD
 * Business logic is delegated to {@link BeeFixHandler} for better
 * maintainability.
=======
 * Business logic is delegated to {@link BeeFixHandler} for better maintainability.
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
 */
@Mixin(Bee.class)
public abstract class BeeStuckFixMixin {
    @Unique
    private int mcopt$pathingFailTicks = 0;

    @Inject(method = "tick", at = @At("TAIL"))
    private void mcopt$stabilityGuard(CallbackInfo ci) {
<<<<<<< HEAD
        if (!GameplayConfig.ENABLE_BEE_STUCK_FIX.get()) {
=======
        if (!MCOPTConfig.ENABLE_BEE_STUCK_FIX.get()) {
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
            return;
        }

        Bee self = (Bee) (Object) this;
<<<<<<< HEAD
        if (self.level().isClientSide()) {
=======
        if (self.level().isClientSide) {
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
            return;
        }

        // Delegate stuck check logic to handler
        mcopt$pathingFailTicks = BeeFixHandler.handleBeeStuckCheck(self, mcopt$pathingFailTicks);
    }
}
