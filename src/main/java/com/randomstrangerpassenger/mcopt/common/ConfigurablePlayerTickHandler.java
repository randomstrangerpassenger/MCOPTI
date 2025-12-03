package com.randomstrangerpassenger.mcopt.common;

import net.neoforged.bus.api.SubscribeEvent;
<<<<<<< HEAD

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
=======
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.tick.TickEvent;

/**
 * Specialized version of {@link ConfigurableTickHandler} for player ticks.
 * Provides convenient access to player tick events with automatic phase, side, and config filtering.
 * <p>
 * By default, this handler only processes server-side events to avoid client/server conflicts.
 * <p>
 * Example usage:
 * <pre>{@code
 * @EventBusSubscriber(modid = MCOPT.MOD_ID)
 * public class MyPlayerHandler extends ConfigurablePlayerTickHandler {
 *     public MyPlayerHandler() {
 *         super(MCOPTConfig.ENABLE_MY_FEATURE);
 *     }
 *
 *     @Override
 *     protected void onConfiguredPlayerTick(TickEvent.PlayerTickEvent event) {
 *         Player player = event.player;
 *         // Your player-specific logic here
 *     }
 * }
 * }</pre>
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
 */
public abstract class ConfigurablePlayerTickHandler {

    private final ModConfigSpec.BooleanValue enableConfig;
<<<<<<< HEAD
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
=======
    private final TickEvent.Phase requiredPhase;
    private final LogicalSide requiredSide;
    private boolean cachedEnabled;

    /**
     * Creates a new player tick handler that runs on server-side, Phase.END.
     *
     * @param enableConfig The configuration value that controls whether this handler is enabled
     */
    protected ConfigurablePlayerTickHandler(ModConfigSpec.BooleanValue enableConfig) {
        this(enableConfig, LogicalSide.SERVER, TickEvent.Phase.END);
    }

    /**
     * Creates a new player tick handler with specific side and phase requirements.
     *
     * @param enableConfig  The configuration value that controls whether this handler is enabled
     * @param requiredSide  The logical side this handler should run on (CLIENT or SERVER)
     * @param requiredPhase The tick phase this handler should run on
     */
    protected ConfigurablePlayerTickHandler(ModConfigSpec.BooleanValue enableConfig,
                                            LogicalSide requiredSide,
                                            TickEvent.Phase requiredPhase) {
        this.enableConfig = enableConfig;
        this.requiredSide = requiredSide;
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
    public final void onPlayerTick(PlayerTickEvent.Post event) {
        // Check if this is the correct side (server vs client)
        if (serverSideOnly && event.getEntity().level().isClientSide()) {
=======
    /**
     * Main event handler that performs common checks before delegating to subclass.
     */
    @SubscribeEvent
    public final void onPlayerTick(TickEvent.PlayerTickEvent event) {
        // Check if this is the correct side (server vs client)
        if (event.side != requiredSide) {
            return;
        }

        // Check if this is the correct phase
        if (event.phase != requiredPhase) {
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
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
<<<<<<< HEAD
=======
     * - The event phase matches the required phase (default: Phase.END)
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
     * - The feature is enabled in configuration
     *
     * @param event The player tick event
     */
<<<<<<< HEAD
    protected abstract void onConfiguredPlayerTick(PlayerTickEvent.Post event);
=======
    protected abstract void onConfiguredPlayerTick(TickEvent.PlayerTickEvent event);
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
}
