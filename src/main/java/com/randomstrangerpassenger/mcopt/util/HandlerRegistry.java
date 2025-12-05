package com.randomstrangerpassenger.mcopt.util;

import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * Centralized handler registration system.
 * <p>
 * Provides a metadata-driven approach to handler registration, eliminating
 * repetitive if-checks and logging patterns.
 * </p>
 * <p>
 * This class handles the common pattern of:
 * 1. Check if feature is enabled
 * 2. Log the feature status
 * 3. Register the handler
 * </p>
 */
public class HandlerRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerRegistry.class);

    private HandlerRegistry() {
        // Utility class
    }

    /**
     * Register a handler with feature toggle check.
     * <p>
     * This is the most common registration pattern: check if a FeatureKey is
     * enabled,
     * then register the handler.
     * </p>
     *
     * @param featureKey      the feature key to check
     * @param handlerSupplier supplier for the handler instance
     * @param description     human-readable description for logging
     */
    public static void registerWithFeatureToggle(
            FeatureKey featureKey,
            Supplier<Object> handlerSupplier,
            String description) {

        if (FeatureToggles.isEnabled(featureKey)) {
            Object handler = handlerSupplier.get();
            NeoForge.EVENT_BUS.register(handler);
            LOGGER.info("{}: ENABLED", description);
        } else {
            LOGGER.debug("{}: DISABLED", description);
        }
    }

    /**
     * Register a handler with a custom condition.
     * <p>
     * For handlers that don't use FeatureToggles but have other config-based
     * conditions.
     * </p>
     *
     * @param condition       condition to check before registration
     * @param handlerSupplier supplier for the handler instance
     * @param description     human-readable description for logging
     */
    public static void registerWithCondition(
            BooleanSupplier condition,
            Supplier<Object> handlerSupplier,
            String description) {

        if (condition.getAsBoolean()) {
            Object handler = handlerSupplier.get();
            NeoForge.EVENT_BUS.register(handler);
            LOGGER.info("{}: ENABLED", description);
        } else {
            LOGGER.debug("{}: DISABLED", description);
        }
    }

    /**
     * Register a handler with additional details in the log message.
     * <p>
     * Useful for features that want to log configuration values.
     * </p>
     *
     * @param condition       condition to check before registration
     * @param handlerSupplier supplier for the handler instance
     * @param description     human-readable description for logging
     * @param details         additional details to append to the log message
     */
    public static void registerWithDetails(
            BooleanSupplier condition,
            Supplier<Object> handlerSupplier,
            String description,
            String details) {

        if (condition.getAsBoolean()) {
            Object handler = handlerSupplier.get();
            NeoForge.EVENT_BUS.register(handler);
            LOGGER.info("{}: ENABLED ({})", description, details);
        } else {
            LOGGER.debug("{}: DISABLED", description);
        }
    }

    /**
     * Register a handler unconditionally.
     * <p>
     * For handlers that should always be registered but want consistent logging.
     * </p>
     *
     * @param handlerSupplier supplier for the handler instance
     * @param description     human-readable description for logging
     */
    public static void registerAlways(
            Supplier<Object> handlerSupplier,
            String description) {

        Object handler = handlerSupplier.get();
        NeoForge.EVENT_BUS.register(handler);
        LOGGER.info("{}: REGISTERED", description);
    }

    /**
     * Register a handler only if it implements a specific interface for conditional
     * enabling.
     * <p>
     * Useful for handlers that have their own isEnabled() method.
     * </p>
     *
     * @param handler     the handler instance (must have isEnabled() method)
     * @param description human-readable description for logging
     */
    public static void registerConditionalHandler(
            Object handler,
            String description,
            BooleanSupplier enabledCheck) {

        if (enabledCheck.getAsBoolean()) {
            NeoForge.EVENT_BUS.register(handler);
            LOGGER.info("{}: ENABLED", description);
        } else {
            LOGGER.debug("{}: DISABLED", description);
        }
    }
}
