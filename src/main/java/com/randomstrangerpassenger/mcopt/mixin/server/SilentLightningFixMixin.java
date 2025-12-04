package com.randomstrangerpassenger.mcopt.mixin.server;

import com.randomstrangerpassenger.mcopt.config.SafetyConfig;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Mixin to prevent silent lightning from playing sound.
 * <p>
 * Problem: In vanilla Minecraft, lightning bolts always play sound, even when
 * they have the Silent NBT tag set to true. This means commands like
 * `/summon minecraft:lightning_bolt ~ ~ ~ {Silent:1b}` don't work as expected.
 * <p>
 * Solution: Redirect the playLocalSound call in the tick method to check if
 * the lightning is silent before playing the sound.
 * <p>
 * Inspired by SilentLightningFix mod but implemented independently for MCOPT.
 */
@Mixin(LightningBolt.class)
public class SilentLightningFixMixin {

    /**
     * Redirects the sound playing call to check for silent lightning.
     * <p>
     * This method intercepts the playLocalSound call in LightningBolt.tick()
     * and only plays the sound if the lightning is not silent.
     *
     * @param level         The level/world
     * @param x             X coordinate
     * @param y             Y coordinate
     * @param z             Z coordinate
     * @param sound         The sound event to play
     * @param source        The sound source
     * @param volume        Sound volume
     * @param pitch         Sound pitch
     * @param distanceDelay Whether to apply distance delay
     */
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V"))
    private void redirectPlaySound(Level level, double x, double y, double z,
            SoundEvent sound, SoundSource source,
            float volume, float pitch, boolean distanceDelay) {
        // If feature is disabled, play sound normally
        if (!SafetyConfig.ENABLE_SILENT_LIGHTNING_FIX.get()) {
            level.playLocalSound(x, y, z, sound, source, volume, pitch, distanceDelay);
            return;
        }

        // Cast this to LightningBolt to access isSilent()
        LightningBolt lightning = (LightningBolt) (Object) this;

        // Only play sound if the lightning is not silent
        if (!lightning.isSilent()) {
            level.playLocalSound(x, y, z, sound, source, volume, pitch, distanceDelay);
        }
    }
}
