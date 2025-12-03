package com.randomstrangerpassenger.mcopt.server.ai;

import com.randomstrangerpassenger.mcopt.MCOPT;
<<<<<<< HEAD
import com.randomstrangerpassenger.mcopt.config.PerformanceConfig;
import com.randomstrangerpassenger.mcopt.server.ai.filters.GoalFilter;
import com.randomstrangerpassenger.mcopt.server.ai.modifiers.ModifierChain;
import com.randomstrangerpassenger.mcopt.server.ai.modifiers.RemoveGoalModifier;
import com.randomstrangerpassenger.mcopt.server.ai.strategy.StrategyRegistry;
import com.randomstrangerpassenger.mcopt.mixin.accessor.MobAccessor;
import com.randomstrangerpassenger.mcopt.util.FeatureToggles;
import com.randomstrangerpassenger.mcopt.util.FeatureKey;
=======
import com.randomstrangerpassenger.mcopt.MCOPTEntityTypeTags;
import com.randomstrangerpassenger.mcopt.config.MCOPTConfig;
import com.randomstrangerpassenger.mcopt.server.ai.filters.GoalFilter;
import com.randomstrangerpassenger.mcopt.server.ai.modifiers.ModifierChain;
import com.randomstrangerpassenger.mcopt.server.ai.modifiers.RemoveGoalModifier;
import com.randomstrangerpassenger.mcopt.util.FeatureToggles;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
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
<<<<<<< HEAD

/**
 * Main system for AI optimization and goal management using Strategy pattern.
 * <p>
=======
import java.util.concurrent.ConcurrentHashMap;

/**
 * Main system for AI optimization and goal management.
 *
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
 * This system handles:
 * - Math caching initialization
 * - LookControl replacement with optimized version
 * - AI goal filtering and removal based on configuration
<<<<<<< HEAD
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
=======
 * - Entity-specific optimizations
 *
 * Inspired by AI-Improvements mod, but with independent implementation using Mixin
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
 * instead of Access Transformers.
 */
public class AIOptimizationSystem {

    // Thread-safe initialization flag (volatile ensures visibility across threads)
    private static volatile boolean initialized = false;

<<<<<<< HEAD
    // Global modifiers that apply to all mobs
=======
    // Tag-based modifiers for better mod compatibility and data pack support
    // Using ConcurrentHashMap for thread-safe access during multi-threaded entity processing
    private static final Map<TagKey<EntityType<?>>, ModifierChain> tagModifiers = new ConcurrentHashMap<>();
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
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

<<<<<<< HEAD
            MCOPT.LOGGER.info("Initializing AI Optimization System (Strategy-based)...");

            if (!FeatureToggles.isEnabled(FeatureKey.AI_OPTIMIZATIONS)) {
=======
            MCOPT.LOGGER.info("Initializing AI Optimization System...");

            if (!FeatureToggles.isAiOptimizationsEnabled()) {
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
                MCOPT.LOGGER.info("AI optimization disabled due to compatibility settings");
                initialized = true;
                return;
            }

            // Initialize math caching if enabled
<<<<<<< HEAD
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
=======
            if (MCOPTConfig.ENABLE_MATH_CACHE.get()) {
                MathCache.init();
            }

            // Setup global modifiers (apply to all mobs)
            setupGlobalModifiers();

            // Setup entity-specific modifiers
            setupAnimalModifiers();
            setupAquaticModifiers();

            initialized = true;
            MCOPT.LOGGER.info("AI Optimization System initialized with {} global modifiers and {} tag-based modifier groups",
                    globalModifiers.size(), tagModifiers.size());
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
        }
    }

    /**
     * Setup modifiers that apply to all mobs.
<<<<<<< HEAD
     * These are applied before strategy-specific modifiers.
=======
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
     */
    private static void setupGlobalModifiers() {
        // Remove LookAtPlayerGoal if configured
        globalModifiers.add(new RemoveGoalModifier(
                GoalFilter.matchClassHierarchy(LookAtPlayerGoal.class),
<<<<<<< HEAD
                PerformanceConfig.REMOVE_LOOK_AT_PLAYER));
=======
                MCOPTConfig.REMOVE_LOOK_AT_PLAYER
        ));
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0

        // Remove RandomLookAroundGoal if configured
        globalModifiers.add(new RemoveGoalModifier(
                GoalFilter.matchClassHierarchy(RandomLookAroundGoal.class),
<<<<<<< HEAD
                PerformanceConfig.REMOVE_RANDOM_LOOK_AROUND));
=======
                MCOPTConfig.REMOVE_RANDOM_LOOK_AROUND
        ));
    }

    /**
     * Setup modifiers for farm animals using tag-based system.
     * This allows better mod compatibility and data pack customization.
     */
    private static void setupAnimalModifiers() {
        // Base farm animal modifiers (applies to all farm animals)
        ModifierChain farmAnimalChain = new ModifierChain();

        // Float goal
        farmAnimalChain.add(new RemoveGoalModifier(
                GoalFilter.matchClassHierarchy(FloatGoal.class),
                MCOPTConfig.REMOVE_ANIMAL_FLOAT
        ));

        // Panic goal
        farmAnimalChain.add(new RemoveGoalModifier(
                GoalFilter.matchClassHierarchy(PanicGoal.class),
                MCOPTConfig.REMOVE_ANIMAL_PANIC
        ));

        // Breed goal (class-based filtering for better version compatibility)
        farmAnimalChain.add(new RemoveGoalModifier(
                GoalFilter.matchClassHierarchy(BreedGoal.class),
                MCOPTConfig.REMOVE_ANIMAL_BREED
        ));

        // Tempt goal (class-based filtering for better version compatibility)
        farmAnimalChain.add(new RemoveGoalModifier(
                GoalFilter.matchClassHierarchy(TemptGoal.class),
                MCOPTConfig.REMOVE_ANIMAL_TEMPT
        ));

        // Follow parent goal (class-based filtering for better version compatibility)
        farmAnimalChain.add(new RemoveGoalModifier(
                GoalFilter.matchClassHierarchy(FollowParentGoal.class),
                MCOPTConfig.REMOVE_ANIMAL_FOLLOW_PARENT
        ));

        // Random stroll goal (class-based, catches RandomStrollGoal and WaterAvoidingRandomStrollGoal)
        farmAnimalChain.add(new RemoveGoalModifier(
                GoalFilter.matchClassHierarchy(RandomStrollGoal.class),
                MCOPTConfig.REMOVE_ANIMAL_STROLL
        ));

        // Apply to all farm animals via tag
        tagModifiers.put(MCOPTEntityTypeTags.FARM_ANIMALS, farmAnimalChain);

        // Wool-growing animals (Sheep): Additional modifiers for EatBlock behavior
        ModifierChain woolGrowingChain = new ModifierChain();
        woolGrowingChain.addAll(farmAnimalChain);  // Inherit all farm animal modifiers
        woolGrowingChain.add(new RemoveGoalModifier(
                GoalFilter.matchClassHierarchy(EatBlockGoal.class),
                MCOPTConfig.REMOVE_SHEEP_EAT_BLOCK
        ));

        tagModifiers.put(MCOPTEntityTypeTags.WOOL_GROWING_ANIMALS, woolGrowingChain);
    }

    /**
     * Setup modifiers for aquatic mobs using tag-based system.
     * This allows better mod compatibility and data pack customization.
     */
    private static void setupAquaticModifiers() {
        // Fish modifiers
        ModifierChain fishChain = new ModifierChain();

        // Random swimming goal (class-based filtering for better version compatibility)
        fishChain.add(new RemoveGoalModifier(
                GoalFilter.matchClassHierarchy(RandomSwimmingGoal.class),
                MCOPTConfig.REMOVE_FISH_SWIM
        ));

        fishChain.add(new RemoveGoalModifier(
                GoalFilter.matchClassHierarchy(PanicGoal.class),
                MCOPTConfig.REMOVE_FISH_PANIC
        ));

        // Apply to all fish via tag
        tagModifiers.put(MCOPTEntityTypeTags.FISH, fishChain);

        // Squid modifiers
        ModifierChain squidChain = new ModifierChain();

        // Squid's RandomMovementGoal is an inner class, so we use pattern matching as fallback
        // This provides better compatibility across different Minecraft versions
        squidChain.add(new RemoveGoalModifier(
                GoalFilter.matchNamePattern("RandomMovement"),
                MCOPTConfig.REMOVE_SQUID_RANDOM_MOVEMENT
        ));

        // Flee goal (class-based filtering)
        squidChain.add(new RemoveGoalModifier(
                GoalFilter.matchClassHierarchy(FleeGoal.class),
                MCOPTConfig.REMOVE_SQUID_FLEE
        ));

        // Apply to all squid-like entities via tag
        tagModifiers.put(MCOPTEntityTypeTags.SQUID_LIKE, squidChain);
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
    }

    /**
     * Process a mob's AI goals after they have been registered.
     * Called from Mixin injection point.
     *
     * @param mob The mob whose goals to process
     */
    public static void processMobGoals(Mob mob) {
<<<<<<< HEAD
        if (!FeatureToggles.isEnabled(FeatureKey.AI_OPTIMIZATIONS)) {
=======
        if (!FeatureToggles.isAiOptimizationsEnabled()) {
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
            return;
        }

        if (!isOptimizationCandidate(mob)) {
            return;
        }

        // Replace LookControl if enabled
<<<<<<< HEAD
        if (PerformanceConfig.ENABLE_OPTIMIZED_LOOK_CONTROL.get() && PerformanceConfig.ENABLE_MATH_CACHE.get()) {
=======
        if (MCOPTConfig.ENABLE_OPTIMIZED_LOOK_CONTROL.get() && MCOPTConfig.ENABLE_MATH_CACHE.get()) {
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
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
<<<<<<< HEAD
            ((MobAccessor) mob).setLookControl(OptimizedLookControl.createFrom(mob, mob.getLookControl()));
=======
            mob.lookControl = OptimizedLookControl.createFrom(mob, mob.lookControl);
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
        } catch (Exception e) {
            MCOPT.LOGGER.error("Failed to replace LookControl for {}: {}", mob.getType(), e.getMessage());
        }
    }

    /**
     * Process a goal selector, applying all relevant modifiers.
<<<<<<< HEAD
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

=======
     */
    private static void processGoalSelector(Mob mob, net.minecraft.world.entity.ai.goal.GoalSelector selector) {
        Set<WrappedGoal> goalsToRemove = new HashSet<>();

>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
        // Iterate through all goals
        for (WrappedGoal wrappedGoal : selector.getAvailableGoals()) {
            Goal goal = wrappedGoal.getGoal();

<<<<<<< HEAD
            // Apply global modifiers first
            goal = globalModifiers.modify(mob, goal);

            // Apply strategy-specific modifiers
            if (goal != null) {
                goal = strategyModifiers.modify(mob, goal);
=======
            // Apply global modifiers
            goal = globalModifiers.modify(mob, goal);

            // Apply tag-based modifiers if any
            if (goal != null) {
                goal = applyTagModifiers(mob, goal);
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
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
<<<<<<< HEAD
     * Check if a mob should be considered for optimization.
     * <p>
     * Excludes:
     * - Boss mobs (performance impact minimal, behavior important)
     * - Villagers (complex interactions, better left unchanged)
     */
    private static boolean isOptimizationCandidate(Mob mob) {
        // Never optimize boss mobs
=======
     * Apply tag-based modifiers to a goal.
     * Checks if the mob's entity type matches any registered tags.
     *
     * @param mob The mob to check tags for
     * @param goal The goal to modify
     * @return Modified goal (or null if removed)
     */
    private static Goal applyTagModifiers(Mob mob, Goal goal) {
        EntityType<?> entityType = mob.getType();
        Goal current = goal;

        // Apply modifiers for each matching tag
        for (Map.Entry<TagKey<EntityType<?>>, ModifierChain> entry : tagModifiers.entrySet()) {
            if (entityType.is(entry.getKey())) {
                current = entry.getValue().modify(mob, current);

                // If goal was removed, stop processing
                if (current == null) {
                    return null;
                }
            }
        }

        return current;
    }

    private static boolean isOptimizationCandidate(Mob mob) {
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
        if (isBossMob(mob)) {
            return false;
        }

<<<<<<< HEAD
        // Never optimize villagers (too complex, many mods interact with them)
=======
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
        if (mob instanceof AbstractVillager) {
            return false;
        }

<<<<<<< HEAD
        // Optimize common mob types
        // Strategy registry will handle the specifics
=======
        // Hostile and passive mobs that commonly use AI goals we tweak
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
        return mob instanceof Monster
                || mob instanceof Animal
                || mob instanceof WaterAnimal
                || mob instanceof Squid
                || mob instanceof Frog
                || mob instanceof AbstractHorse;
    }

<<<<<<< HEAD
    /**
     * Check if a mob is a boss.
     */
    private static boolean isBossMob(Mob mob) {
        return mob.getType().is(Tags.EntityTypes.BOSSES) || mob instanceof EnderDragon;
=======
    private static boolean isBossMob(Mob mob) {
        EntityType<?> type = mob.getType();

        if (type.is(Tags.EntityTypes.BOSSES)) {
            return true;
        }

        return mob instanceof EnderDragon;
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
    }

    /**
     * Check if the system has been initialized.
<<<<<<< HEAD
     *
     * @return true if initialized
=======
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
     */
    public static boolean isInitialized() {
        return initialized;
    }
}
