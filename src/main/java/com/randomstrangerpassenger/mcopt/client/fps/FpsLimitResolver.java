package com.randomstrangerpassenger.mcopt.client.fps;

import com.randomstrangerpassenger.mcopt.config.PerformanceConfig;

/**
 * Resolves the target FPS limit based on the detected Dynamic FPS state.
 * <p>
 * This component encapsulates the logic for determining which FPS cap
 * should apply given a particular state and user preferences.
 * </p>
 */
public class FpsLimitResolver {

    /**
     * Resolve the target FPS limit for a given state.
     * <p>
     * If background throttling is disabled and the window is unfocused/minimized,
     * the user's defined framerate is returned instead of throttling.
     * </p>
     *
     * @param state                the detected FPS state
     * @param userDefinedFramerate the user's preferred FPS limit
     * @return the target FPS limit to apply
     */
    public int resolveTargetLimit(DynamicFpsState state, int userDefinedFramerate) {
        // Check if background throttling is disabled
        if (!PerformanceConfig.ENABLE_BACKGROUND_THROTTLING.get()
                && (state == DynamicFpsState.UNFOCUSED || state == DynamicFpsState.MINIMIZED)) {
            return userDefinedFramerate;
        }

        // Map state to configured FPS limit
        return switch (state) {
            case MINIMIZED -> PerformanceConfig.MINIMIZED_FRAME_RATE_LIMIT.get();
            case UNFOCUSED -> PerformanceConfig.UNFOCUSED_FRAME_RATE_LIMIT.get();
            case MENU -> PerformanceConfig.MENU_FRAME_RATE_LIMIT.get();
            case IDLE -> PerformanceConfig.IDLE_FRAME_RATE_LIMIT.get();
            case IN_GAME -> userDefinedFramerate;
        };
    }

    /**
     * Check if the resolved limit differs from the current applied limit.
     *
     * @param state                current state
     * @param userDefinedFramerate user's FPS preference
     * @param currentLimit         currently applied FPS limit
     * @return true if the limit should be updated
     */
    public boolean shouldUpdateLimit(DynamicFpsState state, int userDefinedFramerate, int currentLimit) {
        int targetLimit = resolveTargetLimit(state, userDefinedFramerate);
        return targetLimit != currentLimit;
    }
}
