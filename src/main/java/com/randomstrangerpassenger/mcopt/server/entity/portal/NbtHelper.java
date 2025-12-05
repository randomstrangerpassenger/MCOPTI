package com.randomstrangerpassenger.mcopt.server.entity.portal;

import net.minecraft.nbt.CompoundTag;

import java.util.Objects;
import java.util.Optional;

/**
 * Helper methods for safe NBT tag access.
 * <p>
 * This utility class handles potential Optional returns from NBT getters
 * and provides fallback values for missing or invalid data.
 * </p>
 */
public class NbtHelper {

    private NbtHelper() {
        // Utility class
    }

    /**
     * Get a CompoundTag from another CompoundTag, returning empty tag if not found.
     *
     * @param tag the parent tag
     * @param key the key to lookup
     * @return the compound tag, or empty tag if not found
     */
    @SuppressWarnings("unchecked")
    public static CompoundTag getCompoundOrNew(CompoundTag tag, String key) {
        try {
            String validKey = Objects.requireNonNull(key, "Key cannot be null");
            Object result = tag.getCompound(validKey);
            if (result instanceof Optional) {
                return ((Optional<CompoundTag>) result).orElse(new CompoundTag());
            } else if (result instanceof CompoundTag) {
                return (CompoundTag) result;
            } else {
                return new CompoundTag();
            }
        } catch (Exception e) {
            return new CompoundTag();
        }
    }

    /**
     * Get an integer from a CompoundTag, returning 0 if not found.
     *
     * @param tag the tag
     * @param key the key to lookup
     * @return the integer value, or 0 if not found
     */
    @SuppressWarnings("unchecked")
    public static int getIntOrZero(CompoundTag tag, String key) {
        try {
            String validKey = Objects.requireNonNull(key, "Key cannot be null");
            Object result = tag.getInt(validKey);
            if (result instanceof Optional) {
                return ((Optional<Integer>) result).orElse(0);
            } else if (result instanceof Integer) {
                return (Integer) result;
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Get a float from a CompoundTag, returning 0.0f if not found.
     *
     * @param tag the tag
     * @param key the key to lookup
     * @return the float value, or 0.0f if not found
     */
    @SuppressWarnings("unchecked")
    public static float getFloatOrZero(CompoundTag tag, String key) {
        try {
            String validKey = Objects.requireNonNull(key, "Key cannot be null");
            Object result = tag.getFloat(validKey);
            if (result instanceof Optional) {
                return ((Optional<Float>) result).orElse(0.0f);
            } else if (result instanceof Float) {
                return (Float) result;
            } else {
                return 0.0f;
            }
        } catch (Exception e) {
            return 0.0f;
        }
    }

    /**
     * Ensure a CompoundTag exists in persistent data, creating if necessary.
     *
     * @param data the parent data tag
     * @param key  the key for the compound tag
     * @return the existing or newly created compound tag
     */
    public static CompoundTag ensureCompound(CompoundTag data, String key) {
        String validKey = Objects.requireNonNull(key, "Key cannot be null");
        if (!data.contains(validKey)) {
            data.put(validKey, new CompoundTag());
        }
        return getCompoundOrNew(data, validKey);
    }
}
