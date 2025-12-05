package com.randomstrangerpassenger.mcopt.safety;

import com.randomstrangerpassenger.mcopt.MCOPT;
import com.randomstrangerpassenger.mcopt.util.FeatureToggles;
import com.randomstrangerpassenger.mcopt.util.FeatureKey;

/**
 * SafetyModule adapter for LeakGuard.
 * <p>
 * Since LeakGuard uses @EventBusSubscriber with static methods,
 * it self-registers. This adapter just provides the SafetyModule
 * interface for consistency with the unified framework.
 * </p>
 */
public class LeakGuardModule implements SafetyModule {

    @Override
    public String getModuleName() {
        return "Leak Guard";
    }

    @Override
    public boolean isEnabled() {
        return FeatureToggles.isEnabled(FeatureKey.LEAK_GUARD);
    }

    @Override
    public void initialize() {
        // LeakGuard uses @EventBusSubscriber, so registration is automatic
        // No additional initialization needed
        MCOPT.LOGGER.debug("LeakGuard module registered (auto-subscribed)");
    }

    @Override
    public void shutdown() {
        // LeakGuard doesn't require explicit cleanup
        // Static fields will be cleared by GC when mod unloads
    }
}
