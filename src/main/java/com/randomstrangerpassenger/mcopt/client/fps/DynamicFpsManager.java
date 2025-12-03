package com.randomstrangerpassenger.mcopt.client.fps;

import com.randomstrangerpassenger.mcopt.MCOPT;
<<<<<<< HEAD
import com.randomstrangerpassenger.mcopt.config.PerformanceConfig;
import com.randomstrangerpassenger.mcopt.util.FeatureToggles;
import com.randomstrangerpassenger.mcopt.util.FeatureKey;
=======
import com.randomstrangerpassenger.mcopt.config.MCOPTConfig;
import com.randomstrangerpassenger.mcopt.util.FeatureToggles;
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
import com.randomstrangerpassenger.mcopt.util.MCOPTConstants;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;
<<<<<<< HEAD
import net.neoforged.neoforge.client.event.ClientTickEvent;

/**
 * Lightweight controller that dynamically adjusts the client framerate limit
 * based on window focus.
 * <p>
 * The goal is to mimic the usability of Dynamic FPS while keeping a fully
 * independent implementation
 * that favors compatibility: no custom game loop hooks, no alterations to
 * simulation state, and only
=======
import net.neoforged.neoforge.event.TickEvent;

/**
 * Lightweight controller that dynamically adjusts the client framerate limit based on window focus.
 * <p>
 * The goal is to mimic the usability of Dynamic FPS while keeping a fully independent implementation
 * that favors compatibility: no custom game loop hooks, no alterations to simulation state, and only
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
 * simple framerate cap adjustments on the render thread.
 */
public final class DynamicFpsManager {

    private static final int MIN_RENDER_DISTANCE = 4;
    private static final int RENDER_DISTANCE_REDUCTION = 4;

    private final Minecraft minecraft;
    private DynamicFpsState activeState;
    private int userDefinedFramerate;
    private int appliedFramerate;
    private long lastInteractionMillis;
    private int previousRenderDistance;
    private boolean renderDistanceReduced;

    public DynamicFpsManager() {
        this.minecraft = Minecraft.getInstance();
        this.userDefinedFramerate = readUserFramerateLimit();
        this.appliedFramerate = this.userDefinedFramerate;
        this.activeState = DynamicFpsState.IN_GAME;
        this.lastInteractionMillis = Util.getMillis();
        this.previousRenderDistance = -1;
        this.renderDistanceReduced = false;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onKeyInput(InputEvent.Key event) {
        if (event.getAction() != MCOPTConstants.Input.INPUT_ACTION_RELEASE) {
            markInteraction();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMouseButton(InputEvent.MouseButton.Pre event) {
        if (event.getAction() != MCOPTConstants.Input.INPUT_ACTION_RELEASE) {
            markInteraction();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMouseScroll(InputEvent.MouseScrollingEvent event) {
<<<<<<< HEAD
        if (event.getScrollDeltaY() != 0.0) {
=======
        if (event.getScrollDelta() != 0.0) {
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
            markInteraction();
        }
    }

    @SubscribeEvent
<<<<<<< HEAD
    public void onClientTick(ClientTickEvent.Post event) {
=======
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0

        if (minecraft.getWindow() == null) {
            return;
        }

<<<<<<< HEAD
        // Always keep the user setting in sync when the window is focused and no menu
        // is open.
=======
        // Always keep the user setting in sync when the window is focused and no menu is open.
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
        if (isForegroundGameplay()) {
            userDefinedFramerate = readUserFramerateLimit();
        }

<<<<<<< HEAD
        if (!FeatureToggles.isEnabled(FeatureKey.DYNAMIC_FPS)) {
=======
        if (!FeatureToggles.isDynamicFpsEnabled()) {
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
            restoreUserLimitIfNeeded();
            return;
        }

        DynamicFpsState detectedState = detectState();
        int targetLimit = resolveTargetLimit(detectedState);

        if (detectedState != activeState || targetLimit != appliedFramerate) {
            applyLimit(targetLimit, detectedState);
        }
    }

    private DynamicFpsState detectState() {
        if (minecraft.getWindow().isMinimized()) {
            return DynamicFpsState.MINIMIZED;
        }

        if (!minecraft.isWindowActive()) {
            return DynamicFpsState.UNFOCUSED;
        }

        Screen currentScreen = minecraft.screen;
        if (currentScreen != null) {
            return DynamicFpsState.MENU;
        }

        if (isIdleBoostActive()) {
            return DynamicFpsState.IDLE;
        }

        return DynamicFpsState.IN_GAME;
    }

    private int resolveTargetLimit(DynamicFpsState state) {
<<<<<<< HEAD
        if (!PerformanceConfig.ENABLE_BACKGROUND_THROTTLING.get()
=======
        if (!MCOPTConfig.ENABLE_BACKGROUND_THROTTLING.get()
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
                && (state == DynamicFpsState.UNFOCUSED || state == DynamicFpsState.MINIMIZED)) {
            return userDefinedFramerate;
        }

        return switch (state) {
<<<<<<< HEAD
            case MINIMIZED -> PerformanceConfig.MINIMIZED_FRAME_RATE_LIMIT.get();
            case UNFOCUSED -> PerformanceConfig.UNFOCUSED_FRAME_RATE_LIMIT.get();
            case MENU -> PerformanceConfig.MENU_FRAME_RATE_LIMIT.get();
            case IDLE -> PerformanceConfig.IDLE_FRAME_RATE_LIMIT.get();
=======
            case MINIMIZED -> MCOPTConfig.MINIMIZED_FRAME_RATE_LIMIT.get();
            case UNFOCUSED -> MCOPTConfig.UNFOCUSED_FRAME_RATE_LIMIT.get();
            case MENU -> MCOPTConfig.MENU_FRAME_RATE_LIMIT.get();
            case IDLE -> MCOPTConfig.IDLE_FRAME_RATE_LIMIT.get();
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
            case IN_GAME -> userDefinedFramerate;
        };
    }

    private void applyLimit(int targetLimit, DynamicFpsState detectedState) {
<<<<<<< HEAD
        minecraft.options.framerateLimit().set(Math.max(0, targetLimit));
=======
        minecraft.getWindow().setFramerateLimit(Math.max(0, targetLimit));
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
        appliedFramerate = targetLimit;

        // Apply render distance reduction when entering idle state
        if (detectedState == DynamicFpsState.IDLE && !renderDistanceReduced) {
            applyRenderDistanceReduction();
        } else if (detectedState != DynamicFpsState.IDLE && renderDistanceReduced) {
            restoreRenderDistance();
        }

        activeState = detectedState;
        MCOPT.LOGGER.debug("Dynamic FPS state: {} (FPS cap set to {})", detectedState, targetLimit);
    }

    private void applyRenderDistanceReduction() {
        Options options = minecraft.options;
        if (options != null) {
            previousRenderDistance = options.renderDistance().get();
            int newRenderDistance = Math.max(MIN_RENDER_DISTANCE, previousRenderDistance - RENDER_DISTANCE_REDUCTION);
            options.renderDistance().set(newRenderDistance);
            renderDistanceReduced = true;
<<<<<<< HEAD
            MCOPT.LOGGER.debug("[DynamicFPS] Idle state: reduced render distance {} -> {}", previousRenderDistance,
                    newRenderDistance);
=======
            MCOPT.LOGGER.debug("[DynamicFPS] Idle state: reduced render distance {} -> {}", previousRenderDistance, newRenderDistance);
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
        }
    }

    private void restoreRenderDistance() {
        if (previousRenderDistance > 0) {
            Options options = minecraft.options;
            if (options != null) {
                options.renderDistance().set(previousRenderDistance);
                renderDistanceReduced = false;
                MCOPT.LOGGER.debug("[DynamicFPS] Restored render distance to {}", previousRenderDistance);
            }
        }
    }

    private void restoreUserLimitIfNeeded() {
        if (appliedFramerate != userDefinedFramerate) {
            applyLimit(userDefinedFramerate, DynamicFpsState.IN_GAME);
        }
    }

    private int readUserFramerateLimit() {
        Options options = minecraft.options;
        if (options != null) {
            try {
                OptionInstance<Integer> framerateLimit = options.framerateLimit();
                return framerateLimit.get();
            } catch (Exception e) {
                MCOPT.LOGGER.debug("Failed to read options framerate limit, falling back to window state", e);
            }
        }
<<<<<<< HEAD
        return Math.max(0, minecraft.options.framerateLimit().get());
    }

    private boolean isIdleBoostActive() {
        if (!PerformanceConfig.ENABLE_IDLE_BOOST.get()) {
=======
        return Math.max(0, minecraft.getWindow().getFramerateLimit());
    }

    private boolean isIdleBoostActive() {
        if (!MCOPTConfig.ENABLE_IDLE_BOOST.get()) {
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
            return false;
        }

        if (!minecraft.isWindowActive() || minecraft.getWindow().isMinimized()) {
            return false;
        }

        if (minecraft.screen != null) {
            return false;
        }

        long idleDurationMillis = Util.getMillis() - lastInteractionMillis;
<<<<<<< HEAD
        long idleThresholdMillis = PerformanceConfig.IDLE_BOOST_INACTIVITY_SECONDS.get()
                * MCOPTConstants.Performance.MILLIS_PER_SECOND;
=======
        long idleThresholdMillis = MCOPTConfig.IDLE_BOOST_INACTIVITY_SECONDS.get() * MCOPTConstants.Performance.MILLIS_PER_SECOND;
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
        return idleDurationMillis >= idleThresholdMillis;
    }

    private void markInteraction() {
        lastInteractionMillis = Util.getMillis();

        if (activeState == DynamicFpsState.IDLE) {
            DynamicFpsState detectedState = detectState();
            int targetLimit = resolveTargetLimit(detectedState);
            applyLimit(targetLimit, detectedState);
        }
    }

    private boolean isForegroundGameplay() {
        return minecraft.isWindowActive() && !minecraft.getWindow().isMinimized() && minecraft.screen == null;
    }
}
