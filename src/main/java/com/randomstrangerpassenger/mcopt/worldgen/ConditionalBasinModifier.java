package com.randomstrangerpassenger.mcopt.worldgen;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.randomstrangerpassenger.mcopt.config.GameplayConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * BiomeModifier that conditionally adds basin (stone disk) features to biomes
 * based on config.
 * <p>
 * This modifier checks the {@link GameplayConfig#ENABLE_BASIN_FIX} setting at
 * world generation time
 * and only adds the basin feature if enabled. This restores natural stone disk
 * generation in
 * overworld biomes like Plains, Forest, and Savanna.
 * </p>
 *
 * <p>
 * Implementation inspired by Basin Generation Fix mod, but completely
 * independent.
 * </p>
 */
public record ConditionalBasinModifier(HolderSet<Biome> biomes, Holder<PlacedFeature> feature,
                GenerationStep.Decoration step) implements BiomeModifier {

        /**
         * Codec for serializing and deserializing ConditionalBasinModifier from JSON.
         */
        public static final MapCodec<ConditionalBasinModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
                        .group(Biome.LIST_CODEC.fieldOf("biomes").forGetter(ConditionalBasinModifier::biomes),
                                        PlacedFeature.CODEC.fieldOf("feature")
                                                        .forGetter(ConditionalBasinModifier::feature),
                                        GenerationStep.Decoration.CODEC.fieldOf("step")
                                                        .forGetter(ConditionalBasinModifier::step))
                        .apply(instance, ConditionalBasinModifier::new));

        @Override
        public void modify(@Nonnull Holder<Biome> biome, @Nonnull Phase phase,
                        @Nonnull ModifiableBiomeInfo.BiomeInfo.Builder builder) {
                // Only modify during ADD phase
                if (phase != Phase.ADD) {
                        return;
                }

                // Check if this biome is in our target list
                Holder<Biome> validBiome = Objects.requireNonNull(biome, "Biome holder cannot be null");
                if (!biomes.contains(validBiome)) {
                        return;
                }

                // Check config setting
                if (!GameplayConfig.ENABLE_BASIN_FIX.get()) {
                        return;
                }

                // Ensure non-null feature holder
                Holder<PlacedFeature> validFeature = Objects.requireNonNull(feature, "Feature cannot be null");

                // Ensure non-null step
                GenerationStep.Decoration validStep = Objects.requireNonNull(step, "Step cannot be null");

                // Add the basin feature to the biome
                builder.getGenerationSettings().addFeature(validStep, validFeature);
        }

        @Override
        public MapCodec<? extends BiomeModifier> codec() {
                return CODEC;
        }
}
