package com.randomstrangerpassenger.mcopt.client.fps;

import com.randomstrangerpassenger.mcopt.config.PerformanceConfig;
import com.randomstrangerpassenger.mcopt.util.MCOPTConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

/**
 * Detects the current Dynamic FPS state based on window and game state.
 * <p>
 * The detector examines window focus, minimization status, active screens,
 * and user interaction timing to determine which FPS limit should apply.
 * </p>
 */
public class StateDetector {

    private final Minecraft minecraft;
    private final InputEventAdapter inputAdapter;

    public StateDetector(Minecraft minecraft, InputEventAdapter inputAdapter) {
        this.minecraft = minecraft;
        this.inputAdapter = inputAdapter;
    }

    /**
     * Detect the current Dynamic FPS state.
     * <p>
     * Priority order:
     * 1. MINIMIZED - window is minimized
     * 2. UNFOCUSED - window is not active
     * 3. MENU - a screen is open
     * 4. IDLE - no interaction for configured duration
     * 5. IN_GAME - default active gameplay
     * </p>
     *
     * @return the detected FPS state
     */
    public DynamicFpsState detectState() {
        // Check window state first
        if (minecraft.getWindow() == null) {
            return DynamicFpsState.IN_GAME;
        }

        if (minecraft.getWindow().isMinimized()) {
            return DynamicFpsState.MINIMIZED;
        }

        if (!minecraft.isWindowActive()) {
            return DynamicFpsState.UNFOCUSED;
        }

        // Check for open screens/menus
        Screen currentScreen = minecraft.screen;
        if (currentScreen != null) {
            return DynamicFpsState.MENU;
        }

        // Check idle state
        if (isIdleBoostActive()) {
            return DynamicFpsState.IDLE;
        }

        return DynamicFpsState.IN_GAME;
    }

    /**
     * Check if idle boost should be active.
     * <p>
     * Idle boost is active when:
     * - Feature is enabled in config
     * - Window is active and not minimized
     * - No screen is open
     * - User hasn't interacted for the configured duration
     * </p>
     *
     * @return true if idle boost should apply
     */
    private boolean isIdleBoostActive() {
        if (!PerformanceConfig.ENABLE_IDLE_BOOST.get()) {
            return false;
        }

        if (!minecraft.isWindowActive() || minecraft.getWindow().isMinimized()) {
            return false;
        }

        if (minecraft.screen != null) {
            return false;
        }

        long idleThresholdMillis = PerformanceConfig.IDLE_BOOST_INACTIVITY_SECONDS.get()
                * MCOPTConstants.Performance.MILLIS_PER_SECOND;

        return inputAdapter.getTimeSinceLastInteraction() >= idleThresholdMillis;
    }

    /**
     * Check if the game is in foreground gameplay mode.
     * <p>
     * Foreground gameplay means the window is active, not minimized,
     * and no screen/menu is open.
     * </p>
     *
     * @return true if in foreground gameplay
     */
    public boolean isForegroundGameplay() {
        return minecraft.isWindowActive()
                && !minecraft.getWindow().isMinimized()
                && minecraft.screen == null;
    }
}
