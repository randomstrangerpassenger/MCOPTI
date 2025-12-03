package com.randomstrangerpassenger.mcopt.util;

import com.randomstrangerpassenger.mcopt.MCOPT;
import com.randomstrangerpassenger.mcopt.config.PerformanceConfig;
import com.randomstrangerpassenger.mcopt.config.GameplayConfig;
import com.randomstrangerpassenger.mcopt.config.SafetyConfig;
import net.neoforged.fml.ModList;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

/**
 * Centralized feature toggle evaluator with data-driven approach.
 * <p>
 * Resolves whether a feature should be active by combining user configuration
 * with compatibility checks against other mods.
 * <p>
 * Thread-safe: uses AtomicBoolean for all toggle states to ensure visibility
 * across threads without synchronization overhead on reads.
 */
public final class FeatureToggles {

    private static final Map<FeatureKey, FeatureToggle> TOGGLES = new EnumMap<>(FeatureKey.class);

    static {
        // Register all feature toggles with their configuration and incompatible mods
        register(FeatureKey.XP_ORB_MERGING,
                GameplayConfig.ENABLE_XP_ORB_MERGING::get,
                "clumps");

        register(FeatureKey.AI_OPTIMIZATIONS,
                PerformanceConfig.ENABLE_AI_OPTIMIZATIONS::get,
                "aiimprovements");

        register(FeatureKey.LEAK_GUARD,
                PerformanceConfig.ENABLE_LEAK_GUARD::get,
                "alltheleaks", "memoryleakfix");

        register(FeatureKey.DYNAMIC_FPS,
                PerformanceConfig.ENABLE_DYNAMIC_FPS::get,
                "dynamic_fps", "fps_reducer");

        register(FeatureKey.BETTER_SNOW_LOGIC,
                () -> GameplayConfig.ENABLE_SNOW_ACCUMULATION_FIX.get()
                        && GameplayConfig.ENABLE_BETTER_SNOW_LOGIC.get());

        register(FeatureKey.ACTION_GUARD,
                SafetyConfig.ENABLE_ACTION_GUARD::get,
                "dontdothat");
    }

    private FeatureToggles() {
    }

    /**
     * Register a feature toggle.
     *
     * @param key              Enum key for the feature
     * @param configSupplier   Supplier that returns the config value
     * @param incompatibleMods Mod IDs that conflict with this feature
     */
    private static void register(FeatureKey key, BooleanSupplier configSupplier,
            String... incompatibleMods) {
        TOGGLES.put(key, new FeatureToggle(configSupplier, key.getDisplayName(), incompatibleMods));
    }

    /**
     * Refresh all feature toggles based on current configuration and loaded mods.
     * Called during initialization and when configuration is reloaded.
     */
    public static void refreshFromConfig() {
        ModList modList = ModList.get();

        for (Map.Entry<FeatureKey, FeatureToggle> entry : TOGGLES.entrySet()) {
            FeatureKey key = entry.getKey();
            FeatureToggle toggle = entry.getValue();

            boolean configEnabled = toggle.configSupplier.getAsBoolean();
            boolean hasIncompatibleMod = toggle.hasIncompatibleMod(modList);

            toggle.enabled.set(configEnabled && !hasIncompatibleMod);

            // Log when feature is disabled due to mod conflict
            if (configEnabled && hasIncompatibleMod) {
                String conflictingMods = String.join(", ", toggle.getLoadedIncompatibleMods(modList));
                MCOPT.LOGGER.info("{} 모드가 감지되었습니다. 충돌 방지를 위해 {}을(를) 비활성화합니다.",
                        conflictingMods, toggle.displayName);
            }
        }
    }

    /**
     * Check if a feature is enabled.
     *
     * @param key Feature key
     * @return true if the feature is enabled, false otherwise
     */
    public static boolean isEnabled(FeatureKey key) {
        FeatureToggle toggle = TOGGLES.get(key);
        return toggle != null && toggle.enabled.get();
    }

    public static void onModConfigReloaded(ModConfigEvent event) {
        ModConfig config = event.getConfig();
        if (config.getModId().equals(MCOPT.MOD_ID)) {
            refreshFromConfig();
        }
    }

    /**
     * Represents a single feature toggle with its configuration and compatibility
     * rules.
     */
    private static class FeatureToggle {
        private final BooleanSupplier configSupplier;
        private final String displayName;
        private final Set<String> incompatibleMods;
        private final AtomicBoolean enabled;

        FeatureToggle(BooleanSupplier configSupplier, String displayName, String... incompatibleMods) {
            this.configSupplier = configSupplier;
            this.displayName = displayName;
            this.incompatibleMods = incompatibleMods.length > 0
                    ? Set.of(incompatibleMods)
                    : Collections.emptySet();
            this.enabled = new AtomicBoolean(false);
        }

        /**
         * Check if any incompatible mod is loaded.
         *
         * @param modList The mod list to check against
         * @return true if at least one incompatible mod is loaded
         */
        boolean hasIncompatibleMod(ModList modList) {
            return incompatibleMods.stream().anyMatch(modList::isLoaded);
        }

        /**
         * Get list of loaded incompatible mods.
         *
         * @param modList The mod list to check against
         * @return List of loaded incompatible mod IDs
         */
        List<String> getLoadedIncompatibleMods(ModList modList) {
            return incompatibleMods.stream()
                    .filter(modList::isLoaded)
                    .toList();
        }
    }
}
