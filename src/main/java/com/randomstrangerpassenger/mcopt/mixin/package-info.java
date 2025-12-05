/**
 * Mixin injections for vanilla Minecraft optimizations.
 *
 * <p>
 * Contains Mixin classes that modify Minecraft behavior for better performance,
 * designed to be non-invasive and compatible with other mods.
 * </p>
 *
 * <h2>Client Mixins</h2>
 * <ul>
 * <li>{@code LevelRendererMixin} - Elliptical render distance culling</li>
 * <li>{@code RenderSectionMixin} - Per-chunk visibility decisions</li>
 * <li>{@code ChunkRenderDispatcherMixin} - Chunk rebuild rate limiting</li>
 * <li>{@code EntityRenderDispatcherMixin} - Entity distance/backface
 * culling</li>
 * <li>{@code ParticleEngineMixin} - Particle spawn rate limiting</li>
 * </ul>
 *
 * <h2>Server Mixins</h2>
 * <ul>
 * <li>{@code ExperienceOrbMixin} - XP orb merging</li>
 * <li>{@code MobAIMixin} - AI goal processing hook</li>
 * </ul>
 *
 * <h2>Design Principles</h2>
 * <ul>
 * <li>Minimal invasiveness: prefer @Inject over @Redirect</li>
 * <li>All mixins respect configuration flags</li>
 * <li>Use {@code mcopt$} prefix for all mixin members</li>
 * </ul>
 *
 * @see com.randomstrangerpassenger.mcopt.config.RenderingConfig
 * @see com.randomstrangerpassenger.mcopt.client.rendering.RenderFrameCache
 */
package com.randomstrangerpassenger.mcopt.mixin;
