package com.randomstrangerpassenger.mcopt.safety;

import com.randomstrangerpassenger.mcopt.MCOPT;
import com.randomstrangerpassenger.mcopt.config.PerformanceConfig;

/**
 * SafetyModule adapter for ResourceCleanupHandler.
 * <p>
 * Since ResourceCleanupHandler uses @EventBusSubscriber with static methods,
 * it self-registers. This adapter provides the SafetyModule interface for
 * consistency with the unified framework.
 * </p>
 */
public class ResourceCleanupModule implements SafetyModule {

    @Override
    public String getModuleName() {
        return "Resource Cleanup";
    }

    @Override
    public boolean isEnabled() {
        return PerformanceConfig.ENABLE_MEMORY_OPTIMIZATIONS.get()
                && PerformanceConfig.ENABLE_RESOURCE_CLEANUP.get();
    }

    @Override
    public void initialize() {
        // ResourceCleanupHandler uses @EventBusSubscriber, so registration is automatic
        // No additional initialization needed
        MCOPT.LOGGER.debug("ResourceCleanup module registered (auto-subscribed)");
    }

    @Override
    public void shutdown() {
        // ResourceCleanupHandler doesn't require explicit cleanup
    }
}
