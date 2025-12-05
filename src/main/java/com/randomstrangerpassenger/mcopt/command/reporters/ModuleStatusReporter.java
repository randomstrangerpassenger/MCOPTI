package com.randomstrangerpassenger.mcopt.command.reporters;

import com.randomstrangerpassenger.mcopt.command.format.StatusFormatter;
import com.randomstrangerpassenger.mcopt.config.GameplayConfig;
import com.randomstrangerpassenger.mcopt.config.RenderingConfig;
import com.randomstrangerpassenger.mcopt.config.SafetyConfig;
import com.randomstrangerpassenger.mcopt.util.FeatureKey;
import com.randomstrangerpassenger.mcopt.util.FeatureToggles;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModList;

import java.util.ArrayList;
import java.util.List;

/**
 * Reports on active modules and detected mod conflicts.
 */
public class ModuleStatusReporter {

    private ModuleStatusReporter() {
        // Utility class
    }

    /**
     * Send all module statuses to the command source.
     */
    public static void sendModuleStatus(CommandSourceStack source) {
        source.sendSuccess(() -> StatusFormatter.formatEmptyLine(), false);
        source.sendSuccess(() -> StatusFormatter.formatHeader("Active Modules:"), false);

        // Rendering modules
        sendModule(source, "Chunk Optimizations", RenderingConfig.ENABLE_CHUNK_OPTIMIZATIONS.get());
        sendModule(source, "Entity Culling", RenderingConfig.ENABLE_ENTITY_CULLING.get());
        sendModule(source, "Block Entity Culling", RenderingConfig.ENABLE_BLOCK_ENTITY_CULLING.get());
        sendModule(source, "Particle Optimizations", RenderingConfig.ENABLE_PARTICLE_OPTIMIZATIONS.get());
        sendModule(source, "Smart Leaves", RenderingConfig.ENABLE_SMART_LEAVES.get());

        // Performance modules
        sendModule(source, "AI Optimizations", FeatureToggles.isEnabled(FeatureKey.AI_OPTIMIZATIONS));
        sendModule(source, "Dynamic FPS", FeatureToggles.isEnabled(FeatureKey.DYNAMIC_FPS));
        sendModule(source, "XP Orb Merging", FeatureToggles.isEnabled(FeatureKey.XP_ORB_MERGING));

        // Safety modules
        sendModule(source, "Leak Guard", FeatureToggles.isEnabled(FeatureKey.LEAK_GUARD));
        sendModule(source, "Action Guard", FeatureToggles.isEnabled(FeatureKey.ACTION_GUARD));
        sendModule(source, "Clear Lag", SafetyConfig.ENABLE_CLEAR_LAG.get());
        sendModule(source, "Per-Chunk Entity Limit", SafetyConfig.ENABLE_PER_CHUNK_ENTITY_LIMIT.get());

        // Fixes
        sendModule(source, "Login Timeout Fix", GameplayConfig.ENABLE_LOGIN_TIMEOUT_FIX.get());
    }

    private static void sendModule(CommandSourceStack source, String name, boolean enabled) {
        source.sendSuccess(() -> StatusFormatter.formatModule(name, enabled), false);
    }

    /**
     * Send detected mod conflicts to the command source.
     */
    public static void sendConflictStatus(CommandSourceStack source) {
        List<Component> conflicts = detectConflicts();

        if (conflicts.isEmpty()) {
            source.sendSuccess(() -> StatusFormatter.formatEmptyLine(), false);
            source.sendSuccess(() -> StatusFormatter.formatInfo("No Conflicts Detected"), false);
        } else {
            source.sendSuccess(() -> StatusFormatter.formatEmptyLine(), false);
            source.sendSuccess(() -> StatusFormatter.formatConflict("Detected Conflicts:"), false);
            for (Component conflict : conflicts) {
                source.sendSuccess(() -> conflict, false);
            }
        }
    }

    private static List<Component> detectConflicts() {
        List<Component> conflicts = new ArrayList<>();

        if (ModList.get().isLoaded("clumps") && FeatureToggles.isEnabled(FeatureKey.XP_ORB_MERGING)) {
            conflicts.add(StatusFormatter.formatWarning("  [Clumps] -> XP Merging Disabled"));
        }

        if (ModList.get().isLoaded("dynamic_fps") && FeatureToggles.isEnabled(FeatureKey.DYNAMIC_FPS)) {
            conflicts.add(StatusFormatter.formatWarning("  [Dynamic FPS] -> MCOPT DynamicFPS Disabled"));
        }

        if (ModList.get().isLoaded("aiimprovements") && FeatureToggles.isEnabled(FeatureKey.AI_OPTIMIZATIONS)) {
            conflicts.add(StatusFormatter.formatWarning("  [AI Improvements] -> MCOPT AI Disabled"));
        }

        return conflicts;
    }
}
