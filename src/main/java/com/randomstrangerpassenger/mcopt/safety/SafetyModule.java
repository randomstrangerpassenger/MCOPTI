package com.randomstrangerpassenger.mcopt.safety;

/**
 * Unified interface for all safety-related modules in MCOPT.
 * <p>
 * This interface establishes a consistent lifecycle pattern for safety
 * features,
 * replacing the previous ad-hoc registration approach. All safety modules
 * should
 * implement this interface to enable centralized management.
 * </p>
 * <p>
 * Lifecycle: register → isEnabled check → initialize → (runtime) → shutdown
 * </p>
 */
public interface SafetyModule {

    /**
     * Get the human-readable name of this safety module.
     * Used for logging and debugging purposes.
     *
     * @return module identifier (e.g., "Action Guard", "Leak Guard")
     */
    String getModuleName();

    /**
     * Check if this module should be enabled based on config and feature toggles.
     * <p>
     * This method is called before {@link #initialize()} to determine if the
     * module should be activated. Modules should check their respective config
     * values and feature toggles here.
     * </p>
     *
     * @return true if the module is enabled, false otherwise
     */
    boolean isEnabled();

    /**
     * Initialize the module during mod setup.
     * <p>
     * This is where modules should register event handlers, set up internal state,
     * and perform any necessary initialization work. This method is only called
     * if {@link #isEnabled()} returns true.
     * </p>
     * <p>
     * Implementation note: For modules using {@code @EventBusSubscriber}, this
     * method can be a no-op since registration happens automatically.
     * </p>
     */
    void initialize();

    /**
     * Clean up resources when the module is shutting down.
     * <p>
     * Default implementation does nothing. Override this if your module needs
     * to perform cleanup operations (e.g., clearing caches, unregistering
     * listeners).
     * </p>
     */
    default void shutdown() {
        // Default: no cleanup needed
    }
}
