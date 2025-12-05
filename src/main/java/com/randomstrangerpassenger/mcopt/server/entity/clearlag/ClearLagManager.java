package com.randomstrangerpassenger.mcopt.server.entity.clearlag;

import com.randomstrangerpassenger.mcopt.config.SafetyConfig;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

/**
 * Custom clear-lag implementation inspired by server utilities, but tuned for
 * MCOPT.
 * <p>
 * Cleans up stray items, orbs, and projectiles on a configurable schedule with
 * opt-in protections to keep named or whitelisted entities intact.
 * </p>
 * <p>
 * Refactored to use separate components for better organization and
 * performance:
 * <ul>
 * <li>{@link CleanupExecutor} - Handles entity iteration and removal</li>
 * <li>{@link WhitelistManager} - Manages entity whitelist caching</li>
 * <li>{@link EntityTypeFilter} - Determines which entities to remove</li>
 * </ul>
 * </p>
 */
public class ClearLagManager {

    private int ticksUntilCleanup;
    private boolean warningIssued;

    // Component instances
    private final WhitelistManager whitelistManager;
    private final EntityTypeFilter entityFilter;
    private final CleanupExecutor cleanupExecutor;

    // Cached config values to avoid repeated .get() calls
    private boolean enableClearLag;
    private int intervalTicks;
    private int warningTicks;

    public ClearLagManager() {
        this.whitelistManager = new WhitelistManager();
        this.entityFilter = new EntityTypeFilter(whitelistManager);
        this.cleanupExecutor = new CleanupExecutor();

        refreshConfigCache();
        this.ticksUntilCleanup = intervalTicks;
        this.warningIssued = false;
    }

    /**
     * Refreshes all cached config values.
     * Call this when config is reloaded.
     */
    public void refreshConfigCache() {
        enableClearLag = SafetyConfig.ENABLE_CLEAR_LAG.get();
        intervalTicks = SafetyConfig.CLEAR_LAG_INTERVAL_TICKS.get();
        warningTicks = SafetyConfig.CLEAR_LAG_WARNING_TICKS.get();

        // Also refresh entity filter config
        entityFilter.refreshConfig(
                SafetyConfig.CLEAR_LAG_REMOVE_ITEMS.get(),
                SafetyConfig.CLEAR_LAG_REMOVE_XP_ORBS.get(),
                SafetyConfig.CLEAR_LAG_REMOVE_PROJECTILES.get(),
                SafetyConfig.CLEAR_LAG_SKIP_NAMED_ITEMS.get());

        // Force whitelist refresh
        whitelistManager.forceRefresh();
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        if (!enableClearLag) {
            return;
        }

        ticksUntilCleanup--;

        // Issue warning chat message
        if (ticksUntilCleanup == warningTicks && !warningIssued) {
            broadcastMessage(Component.translatable("mcopt.clearlag.warning", warningTicks / 20));
            warningIssued = true;
        }

        // Perform cleanup
        if (ticksUntilCleanup <= 0) {
            performCleanup();
            ticksUntilCleanup = intervalTicks;
            warningIssued = false;
        }
    }

    /**
     * Perform the cleanup operation.
     * <p>
     * Uses the CleanupExecutor component for efficient entity removal.
     * </p>
     */
    private void performCleanup() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return;
        }

        // Execute cleanup using component
        CleanupStats stats = cleanupExecutor.performCleanup(
                server.getAllLevels(),
                entityFilter);

        // Broadcast results if entities were removed
        if (stats.hasRemovedEntities()) {
            broadcastMessage(Component.translatable("mcopt.clearlag.complete", stats.totalRemoved()));
        }
    }

    /**
     * Broadcast a message to all players.
     *
     * @param message the message to broadcast
     */
    private void broadcastMessage(Component message) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            Component validMessage = java.util.Objects.requireNonNull(message, "Message cannot be null");
            server.getPlayerList().broadcastSystemMessage(validMessage, false);
        }
    }
}
