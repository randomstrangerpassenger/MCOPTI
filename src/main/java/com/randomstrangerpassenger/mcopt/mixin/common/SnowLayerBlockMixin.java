package com.randomstrangerpassenger.mcopt.mixin.common;

<<<<<<< HEAD
import com.randomstrangerpassenger.mcopt.util.FeatureToggles;
import com.randomstrangerpassenger.mcopt.util.FeatureKey;
=======
import com.randomstrangerpassenger.mcopt.config.MCOPTConfig;
import com.randomstrangerpassenger.mcopt.util.FeatureToggles;
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Implements "Simple Snowy Fix" style snow layer stacking optimization.
<<<<<<< HEAD
 * Reduces unnecessary neighbor notifications when snow accumulates, which
 * mitigates
=======
 * Reduces unnecessary neighbor notifications when snow accumulates, which mitigates
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
 * widespread chunk rebuilds during large-scale snowstorms.
 */
@Mixin(SnowLayerBlock.class)
public abstract class SnowLayerBlockMixin {

    /**
<<<<<<< HEAD
     * Completely skips server updates when adding a snow layer doesn't change the
     * state.
=======
     * Completely skips server updates when adding a snow layer doesn't change the state.
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
     * This prevents unnecessary rebuilds when setBlock is repeatedly called on
     * positions where snow is already at maximum height.
     */
    @Inject(method = "handlePrecipitation", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"), cancellable = true)
<<<<<<< HEAD
    private void mcopt$skipNoopSnowUpdates(BlockState state, Level level, BlockPos pos,
            Biome.Precipitation precipitation, CallbackInfo ci) {
        if (!FeatureToggles.isEnabled(FeatureKey.BETTER_SNOW_LOGIC)) {
            return;
        }

        if (precipitation != Biome.Precipitation.SNOW || level.isClientSide()) {
=======
    private void mcopt$skipNoopSnowUpdates(BlockState state, Level level, BlockPos pos, Biome.Precipitation precipitation, CallbackInfo ci) {
        if (!FeatureToggles.isBetterSnowLogicEnabled()) {
            return;
        }

        if (precipitation != Biome.Precipitation.SNOW || level.isClientSide) {
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
            return;
        }

        int currentLayers = state.getValue(SnowLayerBlock.LAYERS);
        if (currentLayers >= 8) {
            // Already at max height - no state change, skip update
            ci.cancel();
            return;
        }

        BlockState currentState = level.getBlockState(pos);
        BlockState newState = state.setValue(SnowLayerBlock.LAYERS, Math.min(8, currentLayers + 1));

        // Snow layer count unchanged - no rebuild needed
        if (currentState.equals(newState)) {
            ci.cancel();
        }
    }

    /**
<<<<<<< HEAD
     * Reduces neighbor notification flags when snow layers increase to minimize
     * chunk re-rendering.
     */
    @ModifyArg(method = "handlePrecipitation", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"), index = 2)
    private int mcopt$reduceNeighborSpam(int originalFlags) {
        if (!FeatureToggles.isEnabled(FeatureKey.BETTER_SNOW_LOGIC)) {
=======
     * Reduces neighbor notification flags when snow layers increase to minimize chunk re-rendering.
     */
    @ModifyArg(method = "handlePrecipitation", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"), index = 2)
    private int mcopt$reduceNeighborSpam(int originalFlags) {
        if (!FeatureToggles.isBetterSnowLogicEnabled()) {
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
            return originalFlags;
        }

        // Only perform minimal client sync, skip massive neighbor notifications
        return Block.UPDATE_CLIENTS;
    }
}
