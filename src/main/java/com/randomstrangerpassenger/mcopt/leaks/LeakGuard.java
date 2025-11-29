package com.randomstrangerpassenger.mcopt.leaks;

import com.randomstrangerpassenger.mcopt.MCOPT;
import com.randomstrangerpassenger.mcopt.client.RenderFrameCache;
import com.randomstrangerpassenger.mcopt.config.MCOPTConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.lang.ref.WeakReference;

/**
 * Leak mitigation system inspired by the ideas of the AllTheLeaks mod.
 *
 * <p>This implementation focuses on two goals:</p>
 * <ul>
 *     <li>Detect client-level references that survive world switches (common leak pattern)</li>
 *     <li>Proactively clear MCOPT caches when a world unloads to give the GC less to track</li>
 * </ul>
 */
@EventBusSubscriber(modid = MCOPT.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class LeakGuard {

    private static WeakReference<ClientLevel> lastUnloadedLevel = new WeakReference<>(null);
    private static int ticksSinceUnload = 0;
    private static boolean gcAttempted = false;
    private static int lastLeakWarningTick = -1;
    private static long lastMemorySample = 0L;
    private static long lastMemoryAlert = 0L;

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        if (!event.getLevel().isClientSide() || !MCOPTConfig.ENABLE_LEAK_GUARD.get()) {
            return;
        }

        if (event.getLevel() instanceof ClientLevel clientLevel) {
            watchForLeaks(clientLevel);
        }
    }

    @SubscribeEvent
    public static void onClientLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        if (!MCOPTConfig.ENABLE_LEAK_GUARD.get()) {
            return;
        }

        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            watchForLeaks(level);
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !MCOPTConfig.ENABLE_LEAK_GUARD.get()) {
            return;
        }

        monitorOldLevel();
        sampleMemory();
    }

    private static void watchForLeaks(ClientLevel level) {
        lastUnloadedLevel = new WeakReference<>(level);
        ticksSinceUnload = 0;
        gcAttempted = false;
        lastLeakWarningTick = -1;

        // Clear MCOPT-owned static caches so they don't keep world references
        RenderFrameCache.reset();

        MCOPT.LOGGER.info("Leak guard watching unloaded level for lingering references ({} chunks loaded)", level.getChunkSource().getLoadedChunksCount());
    }

    private static void monitorOldLevel() {
        ClientLevel oldLevel = lastUnloadedLevel.get();
        if (oldLevel == null) {
            if (ticksSinceUnload > 0) {
                MCOPT.LOGGER.info("Leak guard: previously unloaded level released after {} ticks", ticksSinceUnload);
            }
            ticksSinceUnload = 0;
            return;
        }

        ticksSinceUnload++;
        int delay = MCOPTConfig.LEAK_CHECK_DELAY_TICKS.get();
        if (ticksSinceUnload < delay) {
            return;
        }

        int interval = Math.max(40, MCOPTConfig.LEAK_WARNING_INTERVAL_TICKS.get());
        if (ticksSinceUnload == delay || ticksSinceUnload - lastLeakWarningTick >= interval) {
            lastLeakWarningTick = ticksSinceUnload;
            MCOPT.LOGGER.warn("Leak guard: client level still referenced {} ticks after unload (dimension: {})", ticksSinceUnload, oldLevel.dimension().location());

            if (!gcAttempted && MCOPTConfig.LEAK_GC_NUDGE.get()) {
                gcAttempted = true;
                System.gc();
                MCOPT.LOGGER.warn("Leak guard triggered System.gc() to help release the old level");
            }
        }
    }

    private static void sampleMemory() {
        long now = System.nanoTime();
        if (now - lastMemorySample < 1_000_000_000L) {
            return; // Sample once per second
        }

        lastMemorySample = now;
        Runtime runtime = Runtime.getRuntime();
        long usedMb = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);

        if (usedMb >= MCOPTConfig.LEAK_MEMORY_ALERT_MB.get()) {
            long cooldown = MCOPTConfig.LEAK_MEMORY_ALERT_COOLDOWN_SECONDS.get() * 1_000_000_000L;
            if (now - lastMemoryAlert >= cooldown) {
                lastMemoryAlert = now;
                MCOPT.LOGGER.warn("Leak guard: high memory usage detected ({} MB used)", usedMb);
            }
        }
    }
}
