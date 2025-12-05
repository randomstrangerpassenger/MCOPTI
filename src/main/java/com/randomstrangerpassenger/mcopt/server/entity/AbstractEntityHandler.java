package com.randomstrangerpassenger.mcopt.server.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class for all entity-related handlers in MCOPT.
 * <p>
 * Provides common functionality like logging, enabled state checks, and
 * lifecycle management. All entity handlers should extend this class to
 * maintain consistency across the codebase.
 * </p>
 */
public abstract class AbstractEntityHandler {

    protected final Logger logger;

    protected AbstractEntityHandler() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    /**
     * Get the human-readable name of this handler.
     * Used for logging and debugging purposes.
     *
     * @return handler name
     */
    public abstract String getHandlerName();

    /**
     * Check if this handler is currently enabled based on configuration.
     *
     * @return true if enabled, false otherwise
     */
    public abstract boolean isEnabled();

    /**
     * Initialize the handler.
     * <p>
     * Called during mod setup. Perform any necessary initialization here,
     * such as setting up config value caches or registering listeners.
     * </p>
     */
    public void initialize() {
        logger.debug("{} initialized", getHandlerName());
    }

    /**
     * Called when configuration is reloaded.
     * <p>
     * Override this method to refresh cached config values or update
     * handler behavior based on new settings.
     * </p>
     */
    public void onConfigReload() {
        logger.debug("{} config reloaded", getHandlerName());
    }

    /**
     * Shutdown the handler and cleanup resources.
     * <p>
     * Called during mod shutdown. Override this if your handler needs
     * to perform cleanup operations.
     * </p>
     */
    public void shutdown() {
        logger.debug("{} shutdown", getHandlerName());
    }

    /**
     * Log a debug message with the handler name prefix.
     *
     * @param message debug message
     * @param args    message arguments
     */
    protected void debugLog(String message, Object... args) {
        logger.debug("[{}] " + message, prependHandlerName(args));
    }

    /**
     * Log an info message with the handler name prefix.
     *
     * @param message info message
     * @param args    message arguments
     */
    protected void infoLog(String message, Object... args) {
        logger.info("[{}] " + message, prependHandlerName(args));
    }

    /**
     * Log a warning message with the handler name prefix.
     *
     * @param message warning message
     * @param args    message arguments
     */
    protected void warnLog(String message, Object... args) {
        logger.warn("[{}] " + message, prependHandlerName(args));
    }

    /**
     * Helper method to prepend handler name to log arguments.
     */
    private Object[] prependHandlerName(Object[] args) {
        Object[] newArgs = new Object[args.length + 1];
        newArgs[0] = getHandlerName();
        System.arraycopy(args, 0, newArgs, 1, args.length);
        return newArgs;
    }
}
