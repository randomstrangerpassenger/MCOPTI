package com.randomstrangerpassenger.mcopt.client.fps;

import com.randomstrangerpassenger.mcopt.MCOPT;
import com.randomstrangerpassenger.mcopt.util.FeatureToggles;
import com.randomstrangerpassenger.mcopt.util.FeatureKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import java.util.Objects;

/**
 * Lightweight controller that dynamically adjusts the client framerate limit
 * based on window focus.
 * <p>
 * The goal is to mimic the usability of Dynamic FPS while keeping a fully
 * independent implementation that favors compatibility: no custom game loop
 * hooks,
 * no alterations to simulation state, and only simple framerate cap adjustments
 * on the render thread.
 * </p>
 * <p>
 * This class has been refactored to use separate components for different
 * responsibilities, improving maintainability and testability.
 * </p>
 */
public final class DynamicFpsManager {

    private final Minecraft minecraft;
    private final InputEventAdapter inputAdapter;
    private final StateDetector stateDetector;
    private final FpsLimitResolver fpsResolver;
    private final RenderDistanceAdjuster renderAdjuster;

    private DynamicFpsState activeState;
    private int userDefinedFramerate;
    private int appliedFramerate;

    public DynamicFpsManager() {
        this.minecraft = Objects.requireNonNull(Minecraft.getInstance(), "Minecraft instance cannot be null");
        this.inputAdapter = new InputEventAdapter();
        InputEventAdapter validInputAdapter = Objects.requireNonNull(inputAdapter, "InputEventAdapter cannot be null");
        Minecraft validMinecraft = Objects.requireNonNull(minecraft, "Minecraft cannot be null");
        this.stateDetector = new StateDetector(validMinecraft, validInputAdapter);
        this.fpsResolver = new FpsLimitResolver();
        this.renderAdjuster = new RenderDistanceAdjuster();

        this.userDefinedFramerate = readUserFramerateLimit();
        this.appliedFramerate = this.userDefinedFramerate;
        this.activeState = DynamicFpsState.IN_GAME;
    }

    /**
     * Get the input event adapter for event registration.
     * The adapter needs to be registered with the event bus externally.
     *
     * @return the input event adapter
     */
    public InputEventAdapter getInputAdapter() {
        return inputAdapter;
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        if (minecraft.getWindow() == null) {
            return;
        }

        // Always keep the user setting in sync when in foreground gameplay
        if (stateDetector.isForegroundGameplay()) {
            userDefinedFramerate = readUserFramerateLimit();
        }

        // If feature is disabled, restore user limit if needed
        if (!FeatureToggles.isEnabled(FeatureKey.DYNAMIC_FPS)) {
            restoreUserLimitIfNeeded();
            return;
        }

        // Detect current state and resolve target FPS limit
        DynamicFpsState detectedState = Objects.requireNonNull(
                stateDetector.detectState(), "Detected state cannot be null");
        int targetLimit = fpsResolver.resolveTargetLimit(detectedState, userDefinedFramerate);

        // Apply changes if state or limit changed
        DynamicFpsState currentActive = Objects.requireNonNull(activeState, "Active state cannot be null");
        if (!detectedState.equals(currentActive) || targetLimit != appliedFramerate) {
            applyLimit(targetLimit, detectedState);
        }
    }

    /**
     * Apply the FPS limit and handle state-specific adjustments.
     *
     * @param targetLimit   the FPS limit to apply
     * @param detectedState the detected FPS state
     */
    private void applyLimit(int targetLimit, DynamicFpsState detectedState) {
        // Set the frame rate limit
        minecraft.options.framerateLimit().set(Math.max(0, targetLimit));
        appliedFramerate = targetLimit;

        // Apply or restore render distance based on state
        Options options = Objects.requireNonNull(minecraft.options, "Options cannot be null");
        if (detectedState == DynamicFpsState.IDLE && !renderAdjuster.isRenderDistanceReduced()) {
            renderAdjuster.applyRenderDistanceReduction(options);
        } else if (detectedState != DynamicFpsState.IDLE && renderAdjuster.isRenderDistanceReduced()) {
            renderAdjuster.restoreRenderDistance(options);
        }

        activeState = detectedState;
        MCOPT.LOGGER.debug("Dynamic FPS state: {} (FPS cap set to {})", detectedState, targetLimit);
    }

    /**
     * Restore the user's framerate limit if it differs from the applied limit.
     */
    private void restoreUserLimitIfNeeded() {
        if (appliedFramerate != userDefinedFramerate) {
            applyLimit(userDefinedFramerate, DynamicFpsState.IN_GAME);
        }
    }

    /**
     * Read the user's framerate limit from Minecraft options.
     * <p>
     * Includes error handling for cases where options might not be available.
     * </p>
     *
     * @return the user's framerate limit, or 0 if unavailable
     */
    private int readUserFramerateLimit() {
        Options options = minecraft.options;
        if (options == null) {
            return 0;
        }

        try {
            OptionInstance<Integer> framerateLimit = options.framerateLimit();
            Integer value = framerateLimit.get();
            return value != null ? Math.max(0, value) : 0;
        } catch (Exception e) {
            MCOPT.LOGGER.warn("Failed to read framerate limit from options, defaulting to 0", e);
            return 0;
        }
    }
}
