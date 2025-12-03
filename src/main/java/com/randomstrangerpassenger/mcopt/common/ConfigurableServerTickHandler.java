package com.randomstrangerpassenger.mcopt.common;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
<<<<<<< HEAD
import net.neoforged.neoforge.event.tick.ServerTickEvent;

/**
 * Specialized version of {@link ConfigurableTickHandler} for server-side ticks.
 * Provides convenient access to server tick events with automatic phase and
 * config filtering.
 * <p>
 * Example usage:
 * 
 * <pre>
 * {
 *     &#64;code
 *     &#64;EventBusSubscriber(modid = MCOPT.MOD_ID)
 *     public class MyServerHandler extends ConfigurableServerTickHandler {
 *         public MyServerHandler() {
 *             super(MCOPTConfig.ENABLE_MY_FEATURE);
 *         }
 *
 *         @Override
 *         protected void onConfiguredServerTick(TickEvent.ServerTickEvent event) {
 *             // Your server-side logic here
 *         }
 *     }
 * }
 * </pre>
=======
import net.neoforged.neoforge.event.tick.TickEvent;

/**
 * Specialized version of {@link ConfigurableTickHandler} for server-side ticks.
 * Provides convenient access to server tick events with automatic phase and config filtering.
 * <p>
 * Example usage:
 * <pre>{@code
 * @EventBusSubscriber(modid = MCOPT.MOD_ID)
 * public class MyServerHandler extends ConfigurableServerTickHandler {
 *     public MyServerHandler() {
 *         super(MCOPTConfig.ENABLE_MY_FEATURE);
 *     }
 *
 *     @Override
 *     protected void onConfiguredServerTick(TickEvent.ServerTickEvent event) {
 *         // Your server-side logic here
 *     }
 * }
 * }</pre>
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
 */
public abstract class ConfigurableServerTickHandler {

    private final ModConfigSpec.BooleanValue enableConfig;
<<<<<<< HEAD
    private boolean cachedEnabled;

    protected ConfigurableServerTickHandler(ModConfigSpec.BooleanValue enableConfig) {
        this.enableConfig = enableConfig;
=======
    private final TickEvent.Phase requiredPhase;
    private boolean cachedEnabled;

    /**
     * Creates a new server tick handler that runs on Phase.END.
     *
     * @param enableConfig The configuration value that controls whether this handler is enabled
     */
    protected ConfigurableServerTickHandler(ModConfigSpec.BooleanValue enableConfig) {
        this(enableConfig, TickEvent.Phase.END);
    }

    /**
     * Creates a new server tick handler with a specific phase.
     *
     * @param enableConfig  The configuration value that controls whether this handler is enabled
     * @param requiredPhase The tick phase this handler should run on
     */
    protected ConfigurableServerTickHandler(ModConfigSpec.BooleanValue enableConfig, TickEvent.Phase requiredPhase) {
        this.enableConfig = enableConfig;
        this.requiredPhase = requiredPhase;
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
        this.cachedEnabled = enableConfig.get();
    }

    /**
     * Refreshes the cached config value.
     * Call this when configuration is reloaded.
     */
    public void refreshConfigCache() {
        this.cachedEnabled = enableConfig.get();
    }

<<<<<<< HEAD
    @SubscribeEvent
    public final void onServerTick(ServerTickEvent.Post event) {
=======
    /**
     * Main event handler that performs common checks before delegating to subclass.
     */
    @SubscribeEvent
    public final void onServerTick(TickEvent.ServerTickEvent event) {
        // Check if this is the correct phase
        if (event.phase != requiredPhase) {
            return;
        }

>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
        // Check if the feature is enabled
        if (!cachedEnabled) {
            return;
        }

        // Delegate to subclass implementation
        onConfiguredServerTick(event);
    }

    /**
     * Subclasses implement this method with their specific server tick logic.
<<<<<<< HEAD
     * This method is only called when the feature is enabled in configuration.
     *
     * @param event The server tick event
     */
    protected abstract void onConfiguredServerTick(ServerTickEvent.Post event);
=======
     * This method is only called when:
     * - The event phase matches the required phase (default: Phase.END)
     * - The feature is enabled in configuration
     *
     * @param event The server tick event
     */
    protected abstract void onConfiguredServerTick(TickEvent.ServerTickEvent event);
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
}
