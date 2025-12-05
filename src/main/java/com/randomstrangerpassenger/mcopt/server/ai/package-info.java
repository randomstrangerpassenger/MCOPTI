/**
 * AI optimization system for mob behavior performance.
 *
 * <p>
 * Reduces CPU usage from mob AI processing through math caching,
 * goal filtering, and optimized controllers.
 * </p>
 *
 * <h2>Components</h2>
 * <ul>
 * <li>{@link com.randomstrangerpassenger.mcopt.server.ai.MathCache} - atan2
 * lookup table</li>
 * <li>{@link com.randomstrangerpassenger.mcopt.server.ai.AIOptimizationSystem}
 * - Goal management</li>
 * <li>{@link com.randomstrangerpassenger.mcopt.server.ai.OptimizedLookControl}
 * - Fast look control</li>
 * </ul>
 *
 * <h2>Subpackages</h2>
 * <ul>
 * <li>{@code filters} - Goal filtering interfaces</li>
 * <li>{@code modifiers} - Goal modification chain</li>
 * <li>{@code strategy} - AI optimization strategies</li>
 * </ul>
 *
 * <h2>Performance</h2>
 * <p>
 * 5-15% reduction in mob AI tick time, most effective with 100+ mobs.
 * </p>
 *
 * @see com.randomstrangerpassenger.mcopt.config.PerformanceConfig
 */
package com.randomstrangerpassenger.mcopt.server.ai;
