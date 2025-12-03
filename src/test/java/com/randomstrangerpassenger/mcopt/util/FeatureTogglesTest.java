package com.randomstrangerpassenger.mcopt.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for FeatureToggles utility.
 * <p>
 * Note: These tests verify the public API contract and thread-safety.
 * Full integration testing requires a complete NeoForge environment with
 * ModList and MCOPTConfig initialization, which is beyond unit test scope.
 * <p>
 * Tests cover:
 * - Public API availability and contract
 * - Thread-safety of concurrent reads
 * - Method consistency
 */
@DisplayName("FeatureToggles Unit Tests")
class FeatureTogglesTest {

    // ========== API Contract Tests ==========

    @Test
<<<<<<< HEAD
    @DisplayName("Should provide isEnabled method with FeatureKey enum")
    void testPublicApiExists() {
        // Verify isEnabled method exists and accepts FeatureKey
        assertThatCode(() -> {
            Method m1 = FeatureToggles.class.getMethod("isEnabled", FeatureKey.class);
            Method m2 = FeatureToggles.class.getMethod("refreshFromConfig");

            assertThat(m1.getReturnType()).isEqualTo(boolean.class);
            assertThat(m2.getReturnType()).isEqualTo(void.class);
        })
                .as("isEnabled(FeatureKey) and refreshFromConfig() should exist with correct signatures")
=======
    @DisplayName("Should provide all expected feature toggle methods")
    void testPublicApiExists() {
        // Verify all expected public methods exist
        assertThatCode(() -> {
            Method m1 = FeatureToggles.class.getMethod("isXpOrbMergingEnabled");
            Method m2 = FeatureToggles.class.getMethod("isAiOptimizationsEnabled");
            Method m3 = FeatureToggles.class.getMethod("isLeakGuardEnabled");
            Method m4 = FeatureToggles.class.getMethod("isDynamicFpsEnabled");
            Method m5 = FeatureToggles.class.getMethod("isBetterSnowLogicEnabled");
            Method m6 = FeatureToggles.class.getMethod("isActionGuardEnabled");
            Method m7 = FeatureToggles.class.getMethod("refreshFromConfig");

            assertThat(m1.getReturnType()).isEqualTo(boolean.class);
            assertThat(m2.getReturnType()).isEqualTo(boolean.class);
            assertThat(m3.getReturnType()).isEqualTo(boolean.class);
            assertThat(m4.getReturnType()).isEqualTo(boolean.class);
            assertThat(m5.getReturnType()).isEqualTo(boolean.class);
            assertThat(m6.getReturnType()).isEqualTo(boolean.class);
            assertThat(m7.getReturnType()).isEqualTo(void.class);
        })
                .as("All expected public methods should exist with correct signatures")
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
                .doesNotThrowAnyException();
    }

    @Test
<<<<<<< HEAD
    @DisplayName("Should return boolean values for all feature keys")
    void testToggleMethodsReturnBoolean() {
        // All FeatureKey values should work with isEnabled
        assertThatCode(() -> {
            boolean result1 = FeatureToggles.isEnabled(FeatureKey.XP_ORB_MERGING);
            boolean result2 = FeatureToggles.isEnabled(FeatureKey.AI_OPTIMIZATIONS);
            boolean result3 = FeatureToggles.isEnabled(FeatureKey.LEAK_GUARD);
            boolean result4 = FeatureToggles.isEnabled(FeatureKey.DYNAMIC_FPS);
            boolean result5 = FeatureToggles.isEnabled(FeatureKey.BETTER_SNOW_LOGIC);
            boolean result6 = FeatureToggles.isEnabled(FeatureKey.ACTION_GUARD);

            // Results should be valid boolean values
=======
    @DisplayName("Should return boolean values for all toggle methods")
    void testToggleMethodsReturnBoolean() {
        // All methods should return without throwing exceptions
        assertThatCode(() -> {
            boolean result1 = FeatureToggles.isXpOrbMergingEnabled();
            boolean result2 = FeatureToggles.isAiOptimizationsEnabled();
            boolean result3 = FeatureToggles.isLeakGuardEnabled();
            boolean result4 = FeatureToggles.isDynamicFpsEnabled();
            boolean result5 = FeatureToggles.isBetterSnowLogicEnabled();
            boolean result6 = FeatureToggles.isActionGuardEnabled();

            // Results should be valid boolean values (implicitly tested by type system)
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
            assertThat(result1).isIn(true, false);
            assertThat(result2).isIn(true, false);
            assertThat(result3).isIn(true, false);
            assertThat(result4).isIn(true, false);
            assertThat(result5).isIn(true, false);
            assertThat(result6).isIn(true, false);
        })
<<<<<<< HEAD
                .as("All feature keys should return valid boolean values without errors")
=======
                .as("All toggle methods should return valid boolean values without errors")
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should return consistent values across multiple calls")
    void testConsistency() {
<<<<<<< HEAD
        // Call with same FeatureKey multiple times and verify consistency
        boolean xpMerging1 = FeatureToggles.isEnabled(FeatureKey.XP_ORB_MERGING);
        boolean xpMerging2 = FeatureToggles.isEnabled(FeatureKey.XP_ORB_MERGING);
        boolean xpMerging3 = FeatureToggles.isEnabled(FeatureKey.XP_ORB_MERGING);

        assertThat(xpMerging1)
                .as("Multiple calls with same FeatureKey should return consistent results")
                .isEqualTo(xpMerging2)
                .isEqualTo(xpMerging3);

        boolean aiOpt1 = FeatureToggles.isEnabled(FeatureKey.AI_OPTIMIZATIONS);
        boolean aiOpt2 = FeatureToggles.isEnabled(FeatureKey.AI_OPTIMIZATIONS);
=======
        // Call each method multiple times and verify consistency
        boolean xpMerging1 = FeatureToggles.isXpOrbMergingEnabled();
        boolean xpMerging2 = FeatureToggles.isXpOrbMergingEnabled();
        boolean xpMerging3 = FeatureToggles.isXpOrbMergingEnabled();

        assertThat(xpMerging1)
                .as("Multiple calls to the same toggle should return consistent results")
                .isEqualTo(xpMerging2)
                .isEqualTo(xpMerging3);

        boolean aiOpt1 = FeatureToggles.isAiOptimizationsEnabled();
        boolean aiOpt2 = FeatureToggles.isAiOptimizationsEnabled();
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0

        assertThat(aiOpt1).isEqualTo(aiOpt2);
    }

    @Test
    @DisplayName("Should be callable from multiple threads without errors")
    void testThreadSafeReads() throws InterruptedException {
        int threadCount = 20;
        int iterationsPerThread = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger errorCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < iterationsPerThread; j++) {
<<<<<<< HEAD
                        // Call all feature keys
                        FeatureToggles.isEnabled(FeatureKey.XP_ORB_MERGING);
                        FeatureToggles.isEnabled(FeatureKey.AI_OPTIMIZATIONS);
                        FeatureToggles.isEnabled(FeatureKey.LEAK_GUARD);
                        FeatureToggles.isEnabled(FeatureKey.DYNAMIC_FPS);
                        FeatureToggles.isEnabled(FeatureKey.BETTER_SNOW_LOGIC);
                        FeatureToggles.isEnabled(FeatureKey.ACTION_GUARD);
=======
                        // Call all toggle methods
                        FeatureToggles.isXpOrbMergingEnabled();
                        FeatureToggles.isAiOptimizationsEnabled();
                        FeatureToggles.isLeakGuardEnabled();
                        FeatureToggles.isDynamicFpsEnabled();
                        FeatureToggles.isBetterSnowLogicEnabled();
                        FeatureToggles.isActionGuardEnabled();
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
                    }
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        assertThat(errorCount.get())
                .as("No errors should occur during concurrent reads")
                .isZero();
    }

    // ========== State Management Tests ==========

    @Test
    @DisplayName("Should handle refreshFromConfig calls without errors")
    void testRefreshFromConfig() {
<<<<<<< HEAD
        // Note: This test verifies the method can be called without throwing
        // exceptions.
=======
        // Note: This test verifies the method can be called without throwing exceptions.
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
        // Full functionality testing requires NeoForge environment.
        assertThatCode(() -> {
            FeatureToggles.refreshFromConfig();
            FeatureToggles.refreshFromConfig();
            FeatureToggles.refreshFromConfig();
        })
                .as("refreshFromConfig should be callable multiple times without errors")
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should maintain state consistency after refresh")
    void testStateConsistencyAfterRefresh() {
        // Get initial states
<<<<<<< HEAD
        boolean xpMergingBefore = FeatureToggles.isEnabled(FeatureKey.XP_ORB_MERGING);
        boolean aiOptBefore = FeatureToggles.isEnabled(FeatureKey.AI_OPTIMIZATIONS);
=======
        boolean xpMergingBefore = FeatureToggles.isXpOrbMergingEnabled();
        boolean aiOptBefore = FeatureToggles.isAiOptimizationsEnabled();
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0

        // Refresh config (may or may not change state depending on config)
        FeatureToggles.refreshFromConfig();

        // Get states after refresh
<<<<<<< HEAD
        boolean xpMergingAfter = FeatureToggles.isEnabled(FeatureKey.XP_ORB_MERGING);
        boolean aiOptAfter = FeatureToggles.isEnabled(FeatureKey.AI_OPTIMIZATIONS);
=======
        boolean xpMergingAfter = FeatureToggles.isXpOrbMergingEnabled();
        boolean aiOptAfter = FeatureToggles.isAiOptimizationsEnabled();
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0

        // States should be valid booleans (consistency check)
        assertThat(xpMergingAfter).isIn(true, false);
        assertThat(aiOptAfter).isIn(true, false);

        // After refresh, multiple reads should still be consistent
<<<<<<< HEAD
        assertThat(FeatureToggles.isEnabled(FeatureKey.XP_ORB_MERGING)).isEqualTo(xpMergingAfter);
        assertThat(FeatureToggles.isEnabled(FeatureKey.AI_OPTIMIZATIONS)).isEqualTo(aiOptAfter);
=======
        assertThat(FeatureToggles.isXpOrbMergingEnabled()).isEqualTo(xpMergingAfter);
        assertThat(FeatureToggles.isAiOptimizationsEnabled()).isEqualTo(aiOptAfter);
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
    }

    // ========== Robustness Tests ==========

    @Test
    @DisplayName("Should handle concurrent refreshFromConfig calls safely")
    void testConcurrentRefresh() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger errorCount = new AtomicInteger(0);

        // Simulate multiple threads calling refreshFromConfig simultaneously
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    FeatureToggles.refreshFromConfig();
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        assertThat(errorCount.get())
                .as("Concurrent refreshFromConfig calls should not cause errors")
                .isZero();

        // After concurrent refresh, reads should still work correctly
        assertThatCode(() -> {
<<<<<<< HEAD
            FeatureToggles.isEnabled(FeatureKey.XP_ORB_MERGING);
            FeatureToggles.isEnabled(FeatureKey.AI_OPTIMIZATIONS);
=======
            FeatureToggles.isXpOrbMergingEnabled();
            FeatureToggles.isAiOptimizationsEnabled();
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should handle mixed concurrent reads and refreshes")
    void testMixedConcurrentOperations() throws InterruptedException {
        int readerThreads = 15;
        int refresherThreads = 5;
        int iterationsPerThread = 500;
        ExecutorService executor = Executors.newFixedThreadPool(readerThreads + refresherThreads);
        CountDownLatch latch = new CountDownLatch(readerThreads + refresherThreads);
        AtomicInteger errorCount = new AtomicInteger(0);

        // Reader threads
        for (int i = 0; i < readerThreads; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < iterationsPerThread; j++) {
<<<<<<< HEAD
                        FeatureToggles.isEnabled(FeatureKey.XP_ORB_MERGING);
                        FeatureToggles.isEnabled(FeatureKey.AI_OPTIMIZATIONS);
                        FeatureToggles.isEnabled(FeatureKey.LEAK_GUARD);
=======
                        FeatureToggles.isXpOrbMergingEnabled();
                        FeatureToggles.isAiOptimizationsEnabled();
                        FeatureToggles.isLeakGuardEnabled();
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
                    }
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        // Refresher threads
        for (int i = 0; i < refresherThreads; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < iterationsPerThread / 10; j++) {
                        FeatureToggles.refreshFromConfig();
                        Thread.sleep(1); // Small delay to reduce contention
                    }
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(15, TimeUnit.SECONDS);
        executor.shutdown();

        assertThat(errorCount.get())
                .as("Mixed concurrent operations should not cause errors")
                .isZero();
    }

    // ========== Integration Verification Tests ==========

    @Test
    @DisplayName("Should have proper class structure (utility class pattern)")
    void testUtilityClassPattern() {
<<<<<<< HEAD
        // FeatureToggles should be a utility class (all static methods, no public
        // constructor)
=======
        // FeatureToggles should be a utility class (all static methods, no public constructor)
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
        assertThatThrownBy(() -> {
            // Try to instantiate via reflection (should fail)
            var constructor = FeatureToggles.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        })
                .as("FeatureToggles should not be instantiable (utility class pattern)")
                .isInstanceOfAny(
                        IllegalAccessException.class,
                        InstantiationException.class,
<<<<<<< HEAD
                        java.lang.reflect.InvocationTargetException.class);
    }

    @Test
    @DisplayName("Should provide complete feature coverage via FeatureKey enum")
    void testFeatureCoverage() {
        // This test verifies all FeatureKey enum values work with isEnabled
        // If new features are added to FeatureKey, they should automatically be covered

        FeatureKey[] allFeatures = FeatureKey.values();

        assertThat(allFeatures)
                .as("FeatureKey enum should have at least 6 features")
                .hasSizeGreaterThanOrEqualTo(6);

        // Verify each FeatureKey works with isEnabled
        for (FeatureKey key : allFeatures) {
            assertThatCode(() -> {
                boolean result = FeatureToggles.isEnabled(key);
                assertThat(result).isIn(true, false);
            })
                    .as("FeatureKey.%s should work with isEnabled()", key.name())
=======
                        java.lang.reflect.InvocationTargetException.class
                );
    }

    @Test
    @DisplayName("Should provide complete feature coverage")
    void testFeatureCoverage() {
        // This test documents all known features
        // If new features are added, this test should be updated

        String[] expectedFeatures = {
                "XpOrbMerging",
                "AiOptimizations",
                "LeakGuard",
                "DynamicFps",
                "BetterSnowLogic",
                "ActionGuard"
        };

        for (String feature : expectedFeatures) {
            String methodName = "is" + feature + "Enabled";
            assertThatCode(() -> {
                Method method = FeatureToggles.class.getMethod(methodName);
                assertThat(method.getReturnType()).isEqualTo(boolean.class);
            })
                    .as("Feature toggle method '%s' should exist", methodName)
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
                    .doesNotThrowAnyException();
        }
    }
}
