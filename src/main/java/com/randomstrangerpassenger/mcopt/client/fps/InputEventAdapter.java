package com.randomstrangerpassenger.mcopt.client.fps;

import com.randomstrangerpassenger.mcopt.util.MCOPTConstants;
import net.minecraft.Util;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;

/**
 * Adapter for input events that tracks user interaction timestamps.
 * <p>
 * This component listens to keyboard, mouse button, and mouse scroll events
 * to determine when the user last interacted with the game. This information
 * is used to detect idle states in the Dynamic FPS system.
 * </p>
 */
public class InputEventAdapter {

    private long lastInteractionMillis;

    public InputEventAdapter() {
        this.lastInteractionMillis = Util.getMillis();
    }

    /**
     * Listen for keyboard input events.
     * Any key press (non-release) counts as interaction.
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onKeyInput(InputEvent.Key event) {
        if (event.getAction() != MCOPTConstants.Input.INPUT_ACTION_RELEASE) {
            markInteraction();
        }
    }

    /**
     * Listen for mouse button events.
     * Any mouse button press (non-release) counts as interaction.
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMouseButton(InputEvent.MouseButton.Pre event) {
        if (event.getAction() != MCOPTConstants.Input.INPUT_ACTION_RELEASE) {
            markInteraction();
        }
    }

    /**
     * Listen for mouse scroll events.
     * Any scroll movement counts as interaction.
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        if (event.getScrollDeltaY() != 0.0) {
            markInteraction();
        }
    }

    /**
     * Mark that the user has interacted with the game.
     * Updates the timestamp to the current time.
     */
    public void markInteraction() {
        this.lastInteractionMillis = Util.getMillis();
    }

    /**
     * Get the timestamp of the last user interaction.
     *
     * @return milliseconds since epoch of last interaction
     */
    public long getLastInteractionMillis() {
        return lastInteractionMillis;
    }

    /**
     * Get the time elapsed since the last interaction.
     *
     * @return milliseconds since last interaction
     */
    public long getTimeSinceLastInteraction() {
        return Util.getMillis() - lastInteractionMillis;
    }
}
