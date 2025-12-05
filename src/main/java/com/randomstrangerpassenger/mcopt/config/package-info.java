/**
 * Configuration system for MCOPT optimization features.
 *
 * <p>
 * Centralized configuration with 65+ options organized by category.
 * Configuration stored in {@code config/mcopt-*.toml}.
 * </p>
 *
 * <h2>Config Classes</h2>
 * <ul>
 * <li>{@link com.randomstrangerpassenger.mcopt.config.RenderingConfig} -
 * Entity/block culling, particles</li>
 * <li>{@link com.randomstrangerpassenger.mcopt.config.PerformanceConfig} - FPS,
 * AI, memory</li>
 * <li>{@link com.randomstrangerpassenger.mcopt.config.SafetyConfig} - Clear
 * lag, entity limits</li>
 * <li>{@link com.randomstrangerpassenger.mcopt.config.GameplayConfig} - Login
 * fix, bucket preview</li>
 * </ul>
 *
 * <h2>Key Features</h2>
 * <ul>
 * <li>Dynamic FPS controller (menu/unfocused/minimized limits)</li>
 * <li>Chunk rendering limits and elliptical culling</li>
 * <li>Entity/block entity culling with configurable distances</li>
 * <li>Particle spawn rate limiting</li>
 * <li>AI goal removal (17 entity-specific options)</li>
 * <li>XP orb merging with configurable radius</li>
 * </ul>
 *
 * @see com.randomstrangerpassenger.mcopt.config.RenderingConfig
 * @see com.randomstrangerpassenger.mcopt.config.PerformanceConfig
 */
package com.randomstrangerpassenger.mcopt.config;
