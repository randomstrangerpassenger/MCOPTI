package com.randomstrangerpassenger.mcopt.common;

import net.neoforged.bus.api.SubscribeEvent;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * Specialized version of {@link ConfigurableTickHandler} for player ticks.
 * Provides convenient access to player tick events with automatic phase, side,
 * and config filtering.
 * <p>
 * By default, this handler only processes server-side events to avoid
 * client/server conflicts.
 * <p>
 * Example usage:
 * 
 * <pre>
 * {
 *     &#64;code
 *     &#64;EventBusSubscriber(modid = MCOPT.MOD_ID)
 *     public class MyPlayerHandler extends ConfigurablePlayerTickHandler {
 *         public MyPlayerHandler() {
 *             super(MCOPTConfig.ENABLE_MY_FEATURE);
 *         }
 *
 *         @Override
 *         protected void onConfiguredPlayerTick(TickEvent.PlayerTickEvent event) {
 *             Player player = event.player;
 *             // Your player-specific logic here
 *         }
 *     }
 * }
 * </pre>
 */
public abstract class ConfigurablePlayerTickHandler {

    private final ModConfigSpec.BooleanValue enableConfig;
    private final boolean serverSideOnly;
    private boolean cachedEnabled;

    protected ConfigurablePlayerTickHandler(ModConfigSpec.BooleanValue enableConfig) {
        this(enableConfig, true);
    }

    /**
     * Creates a new player tick handler with specific side requirements.
     *
     * @param enableConfig   The configuration value that controls whether this
     *                       handler is enabled
     * @param serverSideOnly Whether this handler should only run on the server side
     */
    protected ConfigurablePlayerTickHandler(ModConfigSpec.BooleanValue enableConfig, boolean serverSideOnly) {
        this.enableConfig = enableConfig;
        this.serverSideOnly = serverSideOnly;
        this.cachedEnabled = enableConfig.get();
    }

    /**
     * Refreshes the cached config value.
     * Call this when configuration is reloaded.
     */
    public void refreshConfigCache() {
        this.cachedEnabled = enableConfig.get();
    }

    @SubscribeEvent
    public final void onPlayerTick(PlayerTickEvent.Post event) {
        // Check if this is the correct side (server vs client)
        if (serverSideOnly && event.getEntity().level().isClientSide()) {
            return;
        }

        // Check if the feature is enabled
        if (!cachedEnabled) {
            return;
        }

        // Delegate to subclass implementation
        onConfiguredPlayerTick(event);
    }

    /**
     * Subclasses implement this method with their specific player tick logic.
     * This method is only called when:
     * - The event side matches the required side (default: SERVER)
     * - The feature is enabled in configuration
     *
     * @param event The player tick event
     */
    protected abstract void onConfiguredPlayerTick(PlayerTickEvent.Post event);
}
