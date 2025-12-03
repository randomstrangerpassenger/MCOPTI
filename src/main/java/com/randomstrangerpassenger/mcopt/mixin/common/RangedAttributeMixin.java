package com.randomstrangerpassenger.mcopt.mixin.common;

<<<<<<< HEAD
import com.randomstrangerpassenger.mcopt.config.SafetyConfig;
=======
import com.randomstrangerpassenger.mcopt.config.MCOPTConfig;
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RangedAttribute.class)
public class RangedAttributeMixin {
<<<<<<< HEAD
    @Shadow
    @Final
    private double maxValue;

    @Inject(method = "getMaxValue", at = @At("HEAD"), cancellable = true)
    private void mcopt$expandReportedMaxValue(CallbackInfoReturnable<Double> cir) {
        if (!SafetyConfig.ENABLE_ATTRIBUTE_RANGE_EXPANSION.get()) {
            return;
        }

        double configuredLimit = SafetyConfig.ATTRIBUTE_MAX_LIMIT.get();
=======
    @Shadow @Final private double maxValue;

    @Inject(method = "getMaxValue", at = @At("HEAD"), cancellable = true)
    private void mcopt$expandReportedMaxValue(CallbackInfoReturnable<Double> cir) {
        if (!MCOPTConfig.ENABLE_ATTRIBUTE_RANGE_EXPANSION.get()) {
            return;
        }

        double configuredLimit = MCOPTConfig.ATTRIBUTE_MAX_LIMIT.get();
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
        if (configuredLimit > this.maxValue) {
            cir.setReturnValue(configuredLimit);
        }
    }

    @ModifyArg(method = "sanitizeValue", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(DDD)D"), index = 2)
    private double mcopt$expandClampedMax(double originalMax) {
<<<<<<< HEAD
        if (!SafetyConfig.ENABLE_ATTRIBUTE_RANGE_EXPANSION.get()) {
            return originalMax;
        }

        double configuredLimit = SafetyConfig.ATTRIBUTE_MAX_LIMIT.get();
=======
        if (!MCOPTConfig.ENABLE_ATTRIBUTE_RANGE_EXPANSION.get()) {
            return originalMax;
        }

        double configuredLimit = MCOPTConfig.ATTRIBUTE_MAX_LIMIT.get();
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
        double unclampedMax = Math.max(originalMax, configuredLimit);
        return Mth.clamp(unclampedMax, originalMax, Double.MAX_VALUE);
    }
}
