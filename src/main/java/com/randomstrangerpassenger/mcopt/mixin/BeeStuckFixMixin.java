package com.randomstrangerpassenger.mcopt.mixin;

import com.randomstrangerpassenger.mcopt.MCOPTMod;
import com.randomstrangerpassenger.mcopt.config.McoptCommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bee.class)
public abstract class BeeStuckFixMixin extends Animal {
    @Shadow @Nullable BlockPos hivePos;
    @Shadow int remainingCooldownBeforeLocatingNewHive;

    protected BeeStuckFixMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public abstract boolean hasHive();

    @Shadow
    protected abstract PathNavigation getNavigation();

    @Inject(method = "customServerAiStep", at = @At("TAIL"))
    private void mcopt$resetBrokenHiveLink(CallbackInfo ci) {
        if (!McoptCommonConfig.BEE_FIX.enableBeeStuckFix.get()) {
            return;
        }

        if (!this.level().isClientSide && this.hasHive() && this.hivePos != null) {
            monitorHiveProgress();
        } else {
            mcopt$stuckToHiveTicks = 0;
            mcopt$lastHiveTarget = null;
        }
    }

    @Unique
    private void monitorHiveProgress() {
        if (!this.level().isLoaded(this.hivePos)) {
            return;
        }

        if (!this.hivePos.equals(this.mcopt$lastHiveTarget)) {
            this.mcopt$lastHiveTarget = this.hivePos;
            this.mcopt$stuckToHiveTicks = 0;
            this.mcopt$lastDistanceToHive = this.position().distanceToSqr(this.hivePos.getCenter());
            return;
        }

        PathNavigation navigation = this.getNavigation();
        Path path = navigation.getPath();
        double distance = this.position().distanceToSqr(this.hivePos.getCenter());
        boolean noPath = navigation.isDone() || path == null || path.isDone();
        boolean notMovingCloser = distance >= this.mcopt$lastDistanceToHive - 0.25D;

        if (noPath && notMovingCloser && distance > 9.0D) {
            mcopt$stuckToHiveTicks++;
        } else {
            mcopt$stuckToHiveTicks = 0;
        }

        this.mcopt$lastDistanceToHive = distance;

        int threshold = McoptCommonConfig.BEE_FIX.stuckTimeoutTicks.get();
        if (mcopt$stuckToHiveTicks >= threshold) {
            clearHiveReference();
            mcopt$stuckToHiveTicks = 0;
        }
    }

    @Unique
    private void clearHiveReference() {
        int penalty = McoptCommonConfig.BEE_FIX.failedHiveSearchPenalty.get();
        int cooldown = McoptCommonConfig.BEE_FIX.relinkCooldownTicks.get();
        this.remainingCooldownBeforeLocatingNewHive = Math.max(this.remainingCooldownBeforeLocatingNewHive, cooldown) + penalty;
        MCOPTMod.LOGGER.debug("[BeeFix] Hive link reset at {} after {} stuck ticks", this.hivePos, mcopt$stuckToHiveTicks);
        this.hivePos = null;
    }

    @Unique
    private int mcopt$stuckToHiveTicks;

    @Unique
    private double mcopt$lastDistanceToHive;

    @Unique
    @Nullable
    private BlockPos mcopt$lastHiveTarget;
}
