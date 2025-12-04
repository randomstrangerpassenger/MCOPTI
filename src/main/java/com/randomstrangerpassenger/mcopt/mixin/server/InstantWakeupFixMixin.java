package com.randomstrangerpassenger.mcopt.mixin.server;

import com.randomstrangerpassenger.mcopt.config.SafetyConfig;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

/**
 * Mixin to instantly sync time to clients when all players wake up from sleep.
 * <p>
 * Problem: In vanilla Minecraft, when all players wake up from sleeping, the
 * server
 * changes the time to morning (setDayTime), but the ClientboundSetTimePacket is
 * only
 * sent periodically (every 20 ticks), causing up to 1 second delay before the
 * client
 * screen updates to daytime.
 * <p>
 * Solution: This mixin injects immediately after the sleep completion logic and
 * broadcasts a ClientboundSetTimePacket to all players, ensuring instant visual
 * feedback.
 * <p>
 * Inspired by the InstantSky mod but implemented independently for MCOPT.
 */
@Mixin(ServerLevel.class)
public abstract class InstantWakeupFixMixin {

    @Shadow
    public abstract long getDayTime();

    /**
     * Instantly syncs time to all clients after all players wake up from sleep.
     * <p>
     * Injection point: After the setDayTime call when all players finish sleeping.
     * Target method signature in ServerLevel:
     * - tick(BooleanSupplier hasTimeLeft)
     * - Inside the method, there's logic that checks if all players are sleeping
     * - When sleep completes, it calls setDayTime to change the time to morning
     * <p>
     * We inject immediately after setDayTime is called to broadcast the time
     * packet.
     *
     * @param hasTimeLeft Vanilla parameter
     * @param ci          Callback info
     */
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setDayTime(J)V", shift = At.Shift.AFTER))
    private void onWakeupFromSleep(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        if (!SafetyConfig.ENABLE_INSTANT_WAKEUP.get()) {
            return;
        }

        try {
            // Get the current game time and day time
            long gameTime = ((ServerLevel) (Object) this).getGameTime();
            long dayTime = this.getDayTime();

            // Create and broadcast the time packet to all players
            ClientboundSetTimePacket packet = new ClientboundSetTimePacket(
                    gameTime,
                    dayTime,
                    ((ServerLevel) (Object) this).getGameRules()
                            .getBoolean(net.minecraft.world.level.GameRules.RULE_DAYLIGHT));
            ((ServerLevel) (Object) this).getServer().getPlayerList().broadcastAll(packet);

        } catch (Exception e) {
            // Fail silently to avoid breaking the game
            // This is a quality-of-life feature, not critical functionality
        }
    }
}
