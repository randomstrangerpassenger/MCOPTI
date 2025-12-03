package com.randomstrangerpassenger.mcopt.server.ai;

import com.randomstrangerpassenger.mcopt.MCOPT;
import com.randomstrangerpassenger.mcopt.config.PerformanceConfig;
import com.randomstrangerpassenger.mcopt.server.ai.filters.GoalFilter;
import com.randomstrangerpassenger.mcopt.server.ai.modifiers.ModifierChain;
import com.randomstrangerpassenger.mcopt.server.ai.modifiers.RemoveGoalModifier;
import com.randomstrangerpassenger.mcopt.server.ai.strategy.StrategyRegistry;
import com.randomstrangerpassenger.mcopt.mixin.accessor.MobAccessor;
import com.randomstrangerpassenger.mcopt.util.FeatureToggles;
import com.randomstrangerpassenger.mcopt.util.FeatureKey;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.neoforged.neoforge.common.Tags;

import java.util.*;

/**
 * Main system for AI optimization and goal management using Strategy pattern.
 * <p>
 * This system handles:
 * - Math caching initialization
 * - LookControl replacement with optimized version
 * - AI goal filtering and removal based on configuration
 * - Entity-specific optimizations via Strategy pattern
 * <p>
 * Architecture:
 * -
 * {@link com.randomstrangerpassenger.mcopt.server.ai.strategy.OptimizationStrategy}
 * defines optimization behavior
 * -
 * {@link com.randomstrangerpassenger.mcopt.server.ai.strategy.StrategyRegistry}
 * manages strategies
 * - This class orchestrates the optimization process
 * <p>
 * Benefits of Strategy pattern:
 * - New entity types can be added by creating new Strategy classes
 * - No code modification needed in this file
 * - Each strategy is self-contained and testable
 * - Strategies can be enabled/disabled independently
 * - Better separation of concerns
 * <p>
 * Inspired by AI-Improvements mod, but with independent implementation using
 * Mixin
 * instead of Access Transformers.
 */
public class AIOptimizationSystem {

    // Thread-safe initialization flag (volatile ensures visibility across threads)
    private static volatile boolean initialized = false;

    // Global modifiers that apply to all mobs
    private static final ModifierChain globalModifiers = new ModifierChain();

    /**
     * Initialize the AI optimization system.
     * Must be called during FMLCommonSetupEvent.
     * Thread-safe using double-checked locking pattern.
     */
    public static void init() {
        // Double-checked locking for thread-safe initialization
        if (initialized) {
            MCOPT.LOGGER.warn("AIOptimizationSystem already initialized, skipping");
            return;
        }

        synchronized (AIOptimizationSystem.class) {
            // Check again inside synchronized block
            if (initialized) {
                return;
            }

            MCOPT.LOGGER.info("Initializing AI Optimization System (Strategy-based)...");

            if (!FeatureToggles.isEnabled(FeatureKey.AI_OPTIMIZATIONS)) {
                MCOPT.LOGGER.info("AI optimization disabled due to compatibility settings");
                initialized = true;
                return;
            }

            // Initialize math caching if enabled
            if (PerformanceConfig.ENABLE_MATH_CACHE.get()) {
                MathCache.init();
            }

            // Setup global modifiers (still apply to all mobs)
            setupGlobalModifiers();

            // Initialize strategy registry
            // This replaces the old setupAnimalModifiers() and setupAquaticModifiers()
            StrategyRegistry.init();

            initialized = true;
            MCOPT.LOGGER.info("AI Optimization System initialized successfully");
            MCOPT.LOGGER.info("  - Global modifiers: {}", globalModifiers.size());
            MCOPT.LOGGER.info("  - Registered strategies: {}", StrategyRegistry.getStrategies().size());
        }
    }

    /**
     * Setup modifiers that apply to all mobs.
     * These are applied before strategy-specific modifiers.
     */
    private static void setupGlobalModifiers() {
        // Remove LookAtPlayerGoal if configured
        globalModifiers.add(new RemoveGoalModifier(
                GoalFilter.matchClassHierarchy(LookAtPlayerGoal.class),
                PerformanceConfig.REMOVE_LOOK_AT_PLAYER));

        // Remove RandomLookAroundGoal if configured
        globalModifiers.add(new RemoveGoalModifier(
                GoalFilter.matchClassHierarchy(RandomLookAroundGoal.class),
                PerformanceConfig.REMOVE_RANDOM_LOOK_AROUND));
    }

    /**
     * Process a mob's AI goals after they have been registered.
     * Called from Mixin injection point.
     *
     * @param mob The mob whose goals to process
     */
    public static void processMobGoals(Mob mob) {
        if (!FeatureToggles.isEnabled(FeatureKey.AI_OPTIMIZATIONS)) {
            return;
        }

        if (!isOptimizationCandidate(mob)) {
            return;
        }

        // Replace LookControl if enabled
        if (PerformanceConfig.ENABLE_OPTIMIZED_LOOK_CONTROL.get() && PerformanceConfig.ENABLE_MATH_CACHE.get()) {
            replaceLookControl(mob);
        }

        // Process goals
        processGoalSelector(mob, mob.goalSelector);
        processGoalSelector(mob, mob.targetSelector);
    }

    /**
     * Replace mob's LookControl with optimized version.
     */
    private static void replaceLookControl(Mob mob) {
        try {
            ((MobAccessor) mob).setLookControl(OptimizedLookControl.createFrom(mob, mob.getLookControl()));
        } catch (Exception e) {
            MCOPT.LOGGER.error("Failed to replace LookControl for {}: {}", mob.getType(), e.getMessage());
        }
    }

    /**
     * Process a goal selector, applying all relevant modifiers.
     * <p>
     * This method uses the Strategy pattern:
     * 1. Apply global modifiers (all mobs)
     * 2. Get strategy-specific modifiers from StrategyRegistry
     * 3. Apply strategy modifiers
     * 4. Remove goals marked for deletion
     */
    private static void processGoalSelector(Mob mob, GoalSelector selector) {
        Set<WrappedGoal> goalsToRemove = new HashSet<>();

        // Get strategy-specific modifiers
        ModifierChain strategyModifiers = StrategyRegistry.getModifierChain(mob);

        // Iterate through all goals
        for (WrappedGoal wrappedGoal : selector.getAvailableGoals()) {
            Goal goal = wrappedGoal.getGoal();

            // Apply global modifiers first
            goal = globalModifiers.modify(mob, goal);

            // Apply strategy-specific modifiers
            if (goal != null) {
                goal = strategyModifiers.modify(mob, goal);
            }

            // Mark for removal if null
            if (goal == null) {
                goalsToRemove.add(wrappedGoal);
            }
        }

        // Remove marked goals
        for (WrappedGoal wrappedGoal : goalsToRemove) {
            selector.removeGoal(wrappedGoal.getGoal());
        }

        if (!goalsToRemove.isEmpty()) {
            MCOPT.LOGGER.debug("Removed {} AI goals from {}", goalsToRemove.size(), mob.getType());
        }
    }

    /**
     * Check if a mob should be considered for optimization.
     * <p>
     * Excludes:
     * - Boss mobs (performance impact minimal, behavior important)
     * - Villagers (complex interactions, better left unchanged)
     */
    private static boolean isOptimizationCandidate(Mob mob) {
        // Never optimize boss mobs
        if (isBossMob(mob)) {
            return false;
        }

        // Never optimize villagers (too complex, many mods interact with them)
        if (mob instanceof AbstractVillager) {
            return false;
        }

        // Optimize common mob types
        // Strategy registry will handle the specifics
        return mob instanceof Monster
                || mob instanceof Animal
                || mob instanceof WaterAnimal
                || mob instanceof Squid
                || mob instanceof Frog
                || mob instanceof AbstractHorse;
    }

    /**
     * Check if a mob is a boss.
     */
    private static boolean isBossMob(Mob mob) {
        return mob.getType().is(Tags.EntityTypes.BOSSES) || mob instanceof EnderDragon;
    }

    /**
     * Check if the system has been initialized.
     *
     * @return true if initialized
     */
    public static boolean isInitialized() {
        return initialized;
    }
}
