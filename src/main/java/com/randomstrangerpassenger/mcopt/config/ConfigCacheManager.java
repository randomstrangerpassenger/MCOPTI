package com.randomstrangerpassenger.mcopt.config;

import com.randomstrangerpassenger.mcopt.MCOPT;
import com.randomstrangerpassenger.mcopt.server.entity.xp.XpOrbHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.common.EventBusSubscriber;

/**
 * Centralized config cache manager that refreshes all cached config values
 * when configuration is reloaded.
 * <p>
 * This ensures consistent cache invalidation strategy across all handlers
 * and eliminates the need for per-tick config value comparisons.
 */
@EventBusSubscriber(modid = MCOPT.MOD_ID)
public final class ConfigCacheManager {

    private ConfigCacheManager() {
        // Utility class
    }

    /**
     * Listens for config reload events and refreshes all cached values.
     * This is triggered when configs are changed and reloaded via /reload or config
     * GUI.
     */
    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {
        if (!event.getConfig().getModId().equals(MCOPT.MOD_ID)) {
            return;
        }

        MCOPT.LOGGER.info("Config reloaded, refreshing cached values...");

        // Refresh all handler caches
        XpOrbHandler.refreshConfigCache();

        // Note: ClearLagManager is currently not registered in MCOPT.java
        // When it gets activated, add its cache refresh here

        MCOPT.LOGGER.info("Config cache refresh complete");
    }
}
