package com.randomstrangerpassenger.mcopt.client.fps;

import com.randomstrangerpassenger.mcopt.MCOPT;
import com.randomstrangerpassenger.mcopt.util.MCOPTConstants;
import net.minecraft.client.Options;

/**
 * Manages render distance adjustments for the Dynamic FPS system.
 * <p>
 * When the game enters IDLE state, this component can reduce render distance
 * to save resources. The original render distance is restored when leaving
 * IDLE state.
 * </p>
 */
public class RenderDistanceAdjuster {

    private static final int RENDER_DISTANCE_REDUCTION = 4;

    private int previousRenderDistance = -1;
    private boolean renderDistanceReduced = false;

    /**
     * Apply render distance reduction for idle state.
     * <p>
     * Reduces render distance by a fixed amount, but never below the
     * minimum render distance.
     * </p>
     *
     * @param options Minecraft options to modify
     */
    public void applyRenderDistanceReduction(Options options) {
        if (options == null || renderDistanceReduced) {
            return;
        }

        previousRenderDistance = options.renderDistance().get();
        int newRenderDistance = Math.max(
                MCOPTConstants.Performance.MIN_RENDER_DISTANCE,
                previousRenderDistance - RENDER_DISTANCE_REDUCTION);

        options.renderDistance().set(newRenderDistance);
        renderDistanceReduced = true;

        MCOPT.LOGGER.debug("[DynamicFPS] Idle state: reduced render distance {} -> {}",
                previousRenderDistance, newRenderDistance);
    }

    /**
     * Restore the original render distance.
     * <p>
     * Called when leaving IDLE state to return render distance to its
     * original value.
     * </p>
     *
     * @param options Minecraft options to modify
     */
    public void restoreRenderDistance(Options options) {
        if (previousRenderDistance <= 0 || !renderDistanceReduced || options == null) {
            return;
        }

        options.renderDistance().set(previousRenderDistance);
        renderDistanceReduced = false;

        MCOPT.LOGGER.debug("[DynamicFPS] Restored render distance to {}", previousRenderDistance);
    }

    /**
     * Check if render distance is currently reduced.
     *
     * @return true if render distance has been reduced
     */
    public boolean isRenderDistanceReduced() {
        return renderDistanceReduced;
    }

    /**
     * Get the previous (original) render distance.
     *
     * @return the render distance before reduction, or -1 if never reduced
     */
    public int getPreviousRenderDistance() {
        return previousRenderDistance;
    }
}
