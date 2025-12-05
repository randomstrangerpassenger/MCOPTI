package com.randomstrangerpassenger.mcopt.server.entity.clearlag;

/**
 * Categories of entities for clear-lag tracking.
 * <p>
 * Used to categorize and count different types of entities during cleanup
 * operations.
 * </p>
 */
public enum EntityTypeCategory {
    /** Item entities (dropped items) */
    ITEM,

    /** Experience orbs */
    XP_ORB,

    /** Projectiles (arrows, snowballs, etc.) */
    PROJECTILE,

    /** Other miscellaneous entities */
    OTHER
}
