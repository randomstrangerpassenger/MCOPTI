package com.randomstrangerpassenger.mcopt.common;

import com.randomstrangerpassenger.mcopt.MCOPT;
import com.randomstrangerpassenger.mcopt.config.SafetyConfig;
import com.randomstrangerpassenger.mcopt.safety.ActionGuardHandler;
import com.randomstrangerpassenger.mcopt.safety.LeakGuardModule;
import com.randomstrangerpassenger.mcopt.safety.ResourceCleanupModule;
import com.randomstrangerpassenger.mcopt.safety.SafetyModuleRegistry;
import com.randomstrangerpassenger.mcopt.server.entity.clearlag.ClearLagManager;
import com.randomstrangerpassenger.mcopt.server.entity.golem.GolemSpawnFixHandler;
import com.randomstrangerpassenger.mcopt.server.entity.limiter.PerChunkEntityLimiter;
import com.randomstrangerpassenger.mcopt.command.MCOPTStatusCommand;
import com.randomstrangerpassenger.mcopt.util.HandlerRegistry;

import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * Centralized event handler registration.
 * <p>
 * Consolidates all server-compatible event handler registration logic that was
 * previously scattered throughout MCOPT.java. Handles both game event bus and
 * mod event bus registrations.
 * </p>
 */
public class EventRegistrar {

    /**
     * Register all safety modules with the unified framework.
     * <p>
     * This should be called early in mod initialization, before setup events.
     * </p>
     */
    public static void registerSafetyModules() {
        MCOPT.LOGGER.info("Registering safety modules");

        // Register all safety modules
        SafetyModuleRegistry.register(new ActionGuardHandler());
        SafetyModuleRegistry.register(new LeakGuardModule());
        SafetyModuleRegistry.register(new ResourceCleanupModule());

        MCOPT.LOGGER.debug("Safety module registration complete");
    }

    /**
     * Register server-side and common event handlers.
     * <p>
     * These handlers work on both client and server, or are server-specific.
     * Client-only handlers should be registered via MCOPTClient instead.
     * </p>
     */
    public static void registerServerHandlers() {
        MCOPT.LOGGER.info("Registering server-side event handlers");

        // Always register these handlers (they have internal feature checks)
        HandlerRegistry.registerAlways(
                GolemSpawnFixHandler::new,
                "GolemSpawnFixHandler");

        HandlerRegistry.registerAlways(
                ClearLagManager::new,
                "ClearLagManager");

        // Conditional registration based on config
        HandlerRegistry.registerWithDetails(
                SafetyConfig.ENABLE_PER_CHUNK_ENTITY_LIMIT::get,
                PerChunkEntityLimiter::new,
                "PerChunkEntityLimiter",
                "max " + SafetyConfig.MAX_ENTITIES_PER_CHUNK.get() + " per chunk");

        MCOPT.LOGGER.info("Server-side event handlers registered");
    }

    /**
     * Register command handlers.
     * <p>
     * Sets up command registration listener for the /mcopt status command.
     * </p>
     */
    public static void registerCommands() {
        NeoForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> {
            MCOPTStatusCommand.register(event.getDispatcher());
            MCOPT.LOGGER.info("MCOPT status command registered: /mcopt status");
        });
    }

    /**
     * Register safety module event handlers after initialization.
     * <p>
     * This registers the ActionGuardHandler with the event bus if enabled.
     * Other safety modules use @EventBusSubscriber for auto-registration.
     * </p>
     * <p>
     * Called after SafetyModuleRegistry.initializeAll() to ensure only
     * enabled modules are registered.
     * </p>
     */
    public static void registerSafetyHandlers() {
        MCOPT.LOGGER.debug("Registering safety handler event listeners");

        // ActionGuardHandler needs manual registration
        // (LeakGuard and ResourceCleanup use @EventBusSubscriber)
        ActionGuardHandler actionGuard = new ActionGuardHandler();
        if (actionGuard.isEnabled()) {
            NeoForge.EVENT_BUS.register(actionGuard);
            MCOPT.LOGGER.debug("  - ActionGuardHandler event listener registered");
        }
    }
}
