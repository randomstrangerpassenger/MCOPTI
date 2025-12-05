package com.randomstrangerpassenger.mcopt.client;

import com.randomstrangerpassenger.mcopt.MCOPT;
import com.randomstrangerpassenger.mcopt.client.bucket.BucketPreviewHandler;
import com.randomstrangerpassenger.mcopt.client.fps.DynamicFpsManager;
import com.randomstrangerpassenger.mcopt.client.hud.MemoryHudRenderer;
import com.randomstrangerpassenger.mcopt.config.GameplayConfig;
import com.randomstrangerpassenger.mcopt.safety.PanicButtonHandler;
import com.randomstrangerpassenger.mcopt.util.FeatureKey;
import com.randomstrangerpassenger.mcopt.util.HandlerRegistry;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;

/**
 * Client-side entry point for MCOPT.
 * <p>
 * This class handles all client-only event registrations, solving the dist
 * check
 * issues that previously caused NoClassDefFoundError when the server tried to
 * load client classes.
 * </p>
 * <p>
 * Uses @EventBusSubscriber(Dist.CLIENT) to ensure this class is only loaded
 * on the client side.
 * </p>
 */
public class MCOPTClient {

    /**
     * Mod event bus subscriber for client-only mod events.
     * <p>
     * Handles events on the mod event bus (like RegisterKeyMappingsEvent).
     * Note: In NeoForge, mod event bus subscription is determined by the event
     * type,
     * not by a 'bus' parameter.
     * </p>
     */
    @EventBusSubscriber(modid = MCOPT.MOD_ID, value = Dist.CLIENT)
    public static class ModEvents {

        /**
         * Register GUI layers (Memory HUD).
         */
        @SubscribeEvent
        public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
            MemoryHudRenderer.onRegisterGuiLayers(event);
        }

        /**
         * Register key mappings (Panic button).
         */
        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            PanicButtonHandler.ModEventHandler.onRegisterKeyMappings(event);
        }
    }

    /**
     * Game event bus subscriber for client-only game events.
     * <p>
     * Handles events on the game event bus (NeoForge.EVENT_BUS).
     * </p>
     */
    @EventBusSubscriber(modid = MCOPT.MOD_ID, value = Dist.CLIENT)
    public static class GameEvents {
        // Game events are registered via NeoForge.EVENT_BUS in registerClientHandlers()
    }

    /**
     * Register client-only event handlers.
     * <p>
     * This method should be called during client setup to register handlers
     * that need manual registration (not using @EventBusSubscriber).
     * </p>
     */
    public static void registerClientHandlers() {
        MCOPT.LOGGER.info("Registering client-side event handlers");

        // Register Dynamic FPS Manager with its input event adapter
        HandlerRegistry.registerWithFeatureToggle(
                FeatureKey.DYNAMIC_FPS,
                () -> {
                    DynamicFpsManager fpsManager = new DynamicFpsManager();
                    // Also register the input event adapter component
                    NeoForge.EVENT_BUS.register(fpsManager.getInputAdapter());
                    return fpsManager;
                },
                "Dynamic FPS controller");

        // Register Bucket Preview Handler
        HandlerRegistry.registerWithCondition(
                GameplayConfig.ENABLE_BUCKET_PREVIEW::get,
                BucketPreviewHandler::new,
                "Bucket preview tooltips");

        // Register Panic Button Handler (game events)
        // Key mapping is registered via ModEvents.onRegisterKeyMappings
        NeoForge.EVENT_BUS.register(PanicButtonHandler.GameEventHandler.class);

        MCOPT.LOGGER.info("Client-side event handlers registered");
    }
}
