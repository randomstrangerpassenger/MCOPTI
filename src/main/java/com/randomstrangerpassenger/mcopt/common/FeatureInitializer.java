package com.randomstrangerpassenger.mcopt.common;

import com.randomstrangerpassenger.mcopt.MCOPT;
import com.randomstrangerpassenger.mcopt.config.PerformanceConfig;
import com.randomstrangerpassenger.mcopt.config.RenderingConfig;
import com.randomstrangerpassenger.mcopt.config.SafetyConfig;
import com.randomstrangerpassenger.mcopt.safety.SafetyModuleRegistry;
import com.randomstrangerpassenger.mcopt.server.ai.AIOptimizationSystem;
import com.randomstrangerpassenger.mcopt.util.FeatureToggles;
import com.randomstrangerpassenger.mcopt.util.FeatureKey;

import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Centralized feature initialization handler.
 * <p>
 * Handles common and client setup logic that was previously in MCOPT.java.
 * Responsible for initializing features and logging their status.
 * </p>
 */
public class FeatureInitializer {

    /**
     * Common setup - runs on both client and server.
     * <p>
     * Initializes features that work in both environments, such as AI optimizations
     * and safety features.
     * </p>
     *
     * @param event the common setup event
     */
    public static void onCommonSetup(final FMLCommonSetupEvent event) {
        MCOPT.LOGGER.info("MCOPT common setup");

        // Initialize AI optimization system
        if (FeatureToggles.isEnabled(FeatureKey.AI_OPTIMIZATIONS)) {
            event.enqueueWork(() -> {
                AIOptimizationSystem.init();
                MCOPT.LOGGER.info("AI optimization system: ENABLED");
            });
        }

        // Initialize safety modules
        SafetyModuleRegistry.initializeAll();

        // Log safety feature status
        if (SafetyConfig.ENABLE_MAX_HEALTH_STABILITY.get()) {
            MCOPT.LOGGER.info("Max health stability fix: ENABLED");
        }

        if (SafetyConfig.ENABLE_ATTRIBUTE_RANGE_EXPANSION.get()) {
            MCOPT.LOGGER.info("Attribute cap expansion: ENABLED (max {})",
                    SafetyConfig.ATTRIBUTE_MAX_LIMIT.get());
        }
    }

    /**
     * Client setup - runs only on the client.
     * <p>
     * Logs the status of client-only features like rendering optimizations.
     * Actual client handler registration happens in MCOPTClient.
     * </p>
     *
     * @param event the client setup event
     */
    public static void onClientSetup(final FMLClientSetupEvent event) {
        MCOPT.LOGGER.info("MCOPT client setup - Loading client-side optimizations");

        // Log rendering optimization status
        if (RenderingConfig.ENABLE_CHUNK_OPTIMIZATIONS.get()) {
            MCOPT.LOGGER.info("Chunk rendering optimizations: ENABLED");
        }

        if (RenderingConfig.ENABLE_ENTITY_CULLING.get()) {
            MCOPT.LOGGER.info("Entity culling optimizations: ENABLED");
        }

        if (RenderingConfig.ENABLE_PARTICLE_OPTIMIZATIONS.get()) {
            MCOPT.LOGGER.info("Particle system optimizations: ENABLED");
        }

        // Log gameplay feature status
        if (FeatureToggles.isEnabled(FeatureKey.XP_ORB_MERGING)) {
            MCOPT.LOGGER.info("Experience orb merging optimizations: ENABLED");
        }

        // Log AI optimization details
        if (FeatureToggles.isEnabled(FeatureKey.AI_OPTIMIZATIONS)) {
            MCOPT.LOGGER.info("AI optimization: ENABLED");
            if (PerformanceConfig.ENABLE_MATH_CACHE.get()) {
                MCOPT.LOGGER.info("  - Math caching: ENABLED");
            }
            if (PerformanceConfig.ENABLE_OPTIMIZED_LOOK_CONTROL.get()) {
                MCOPT.LOGGER.info("  - Optimized LookControl: ENABLED");
            }
        }

        MCOPT.LOGGER.info("Client setup complete");
    }
}
