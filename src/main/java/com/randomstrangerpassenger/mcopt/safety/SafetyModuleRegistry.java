package com.randomstrangerpassenger.mcopt.safety;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Centralized registry and lifecycle manager for all safety modules.
 * <p>
 * This class manages the registration, initialization, and shutdown of all
 * safety-related features in MCOPT. It replaces the previous scattered
 * registration pattern with a unified approach.
 * </p>
 */
public class SafetyModuleRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(SafetyModuleRegistry.class);
    private static final List<SafetyModule> MODULES = new ArrayList<>();
    private static boolean initialized = false;

    /**
     * Register a safety module with the registry.
     * <p>
     * Modules should be registered during mod construction, before initialization.
     * Attempting to register after initialization will log a warning.
     * </p>
     *
     * @param module the safety module to register
     */
    public static void register(SafetyModule module) {
        if (initialized) {
            LOGGER.warn("Attempted to register safety module '{}' after initialization",
                    module.getModuleName());
            return;
        }

        MODULES.add(module);
        LOGGER.debug("Registered safety module: {}", module.getModuleName());
    }

    /**
     * Initialize all registered and enabled safety modules.
     * <p>
     * This method checks each module's {@link SafetyModule#isEnabled()} status
     * and initializes only the enabled ones. Disabled modules are logged but
     * not initialized.
     * </p>
     * <p>
     * Should be called once during mod setup (common or client setup).
     * </p>
     */
    public static void initializeAll() {
        if (initialized) {
            LOGGER.warn("Safety modules already initialized, skipping duplicate initialization");
            return;
        }

        LOGGER.info("Initializing safety modules ({} registered)", MODULES.size());

        int enabledCount = 0;
        for (SafetyModule module : MODULES) {
            try {
                if (module.isEnabled()) {
                    LOGGER.info("  - {}: ENABLED", module.getModuleName());
                    module.initialize();
                    enabledCount++;
                } else {
                    LOGGER.debug("  - {}: DISABLED", module.getModuleName());
                }
            } catch (Exception e) {
                LOGGER.error("Failed to initialize safety module: {}",
                        module.getModuleName(), e);
            }
        }

        initialized = true;
        LOGGER.info("Safety module initialization complete ({}/{} enabled)",
                enabledCount, MODULES.size());
    }

    /**
     * Shutdown all active safety modules.
     * <p>
     * Calls {@link SafetyModule#shutdown()} on all registered modules.
     * Used for cleanup when the mod is unloading or the game is shutting down.
     * </p>
     */
    public static void shutdownAll() {
        if (!initialized) {
            return;
        }

        LOGGER.info("Shutting down safety modules");

        for (SafetyModule module : MODULES) {
            try {
                module.shutdown();
                LOGGER.debug("  - {} shutdown complete", module.getModuleName());
            } catch (Exception e) {
                LOGGER.error("Error during shutdown of safety module: {}",
                        module.getModuleName(), e);
            }
        }

        initialized = false;
    }

    /**
     * Get the count of registered modules.
     * Used for testing and status reporting.
     *
     * @return number of registered modules
     */
    public static int getRegisteredCount() {
        return MODULES.size();
    }

    /**
     * Check if the registry has been initialized.
     * Used for testing and verification.
     *
     * @return true if initialized, false otherwise
     */
    public static boolean isInitialized() {
        return initialized;
    }

    /**
     * Reset the registry (for testing purposes only).
     * <p>
     * This method clears all registered modules and resets the initialization
     * state.
     * Should only be used in unit tests.
     * </p>
     */
    public static void reset() {
        MODULES.clear();
        initialized = false;
        LOGGER.debug("Safety module registry reset");
    }
}
