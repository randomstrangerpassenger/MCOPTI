package com.randomstrangerpassenger.mcopt.config.builder;

import net.neoforged.neoforge.common.ModConfigSpec;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Helper class to reduce boilerplate when defining configuration options.
 * <p>
 * This class provides simplified methods for common config patterns,
 * replacing verbose BUILDER chaining with concise helper methods.
 * </p>
 * <p>
 * Expected LOC reduction: ~350 lines across all config classes.
 * </p>
 */
public final class ConfigBuilder {

    private ConfigBuilder() {
        // Utility class
    }

    /**
     * Define a boolean configuration option with a single-line description.
     *
     * @param builder      the ModConfigSpec Builder
     * @param key          the config key
     * @param description  brief description of the setting
     * @param defaultValue default boolean value
     * @return the configured BooleanValue
     */
    public static ModConfigSpec.BooleanValue bool(ModConfigSpec.Builder builder,
            String key,
            String description,
            boolean defaultValue) {
        Objects.requireNonNull(key, "Config key cannot be null");
        Objects.requireNonNull(description, "Description cannot be null");
        return builder.comment(description).define(key, defaultValue);
    }

    /**
     * Define a boolean configuration option with multiple description lines.
     *
     * @param builder      the ModConfigSpec Builder
     * @param key          the config key
     * @param description  multi-line description (varargs)
     * @param defaultValue default boolean value
     * @return the configured BooleanValue
     */
    public static ModConfigSpec.BooleanValue bool(ModConfigSpec.Builder builder,
            String key,
            boolean defaultValue,
            String... description) {
        Objects.requireNonNull(key, "Config key cannot be null");
        Objects.requireNonNull(description, "Description cannot be null");
        return builder.comment(description).define(key, defaultValue);
    }

    /**
     * Define an integer configuration option with range constraints.
     *
     * @param builder      the ModConfigSpec Builder
     * @param key          the config key
     * @param description  single-line description
     * @param defaultValue default integer value
     * @param min          minimum allowed value
     * @param max          maximum allowed value
     * @return the configured IntValue
     */
    public static ModConfigSpec.IntValue intRange(ModConfigSpec.Builder builder,
            String key,
            String description,
            int defaultValue,
            int min,
            int max) {
        Objects.requireNonNull(key, "Config key cannot be null");
        Objects.requireNonNull(description, "Description cannot be null");
        return builder.comment(description).defineInRange(key, defaultValue, min, max);
    }

    /**
     * Define an integer configuration option with range constraints and multi-line
     * description.
     *
     * @param builder      the ModConfigSpec Builder
     * @param key          the config key
     * @param defaultValue default integer value
     * @param min          minimum allowed value
     * @param max          maximum allowed value
     * @param description  multi-line description (varargs)
     * @return the configured IntValue
     */
    public static ModConfigSpec.IntValue intRange(ModConfigSpec.Builder builder,
            String key,
            int defaultValue,
            int min,
            int max,
            String... description) {
        Objects.requireNonNull(key, "Config key cannot be null");
        Objects.requireNonNull(description, "Description cannot be null");
        return builder.comment(description).defineInRange(key, defaultValue, min, max);
    }

    /**
     * Define a double configuration option with range constraints.
     *
     * @param builder      the ModConfigSpec Builder
     * @param key          the config key
     * @param description  single-line description
     * @param defaultValue default double value
     * @param min          minimum allowed value
     * @param max          maximum allowed value
     * @return the configured DoubleValue
     */
    public static ModConfigSpec.DoubleValue doubleRange(ModConfigSpec.Builder builder,
            String key,
            String description,
            double defaultValue,
            double min,
            double max) {
        Objects.requireNonNull(key, "Config key cannot be null");
        Objects.requireNonNull(description, "Description cannot be null");
        return builder.comment(description).defineInRange(key, defaultValue, min, max);
    }

    /**
     * Define a double configuration option with range constraints and multi-line
     * description.
     *
     * @param builder      the ModConfigSpec Builder
     * @param key          the config key
     * @param defaultValue default double value
     * @param min          minimum allowed value
     * @param max          maximum allowed value
     * @param description  multi-line description (varargs)
     * @return the configured DoubleValue
     */
    public static ModConfigSpec.DoubleValue doubleRange(ModConfigSpec.Builder builder,
            String key,
            double defaultValue,
            double min,
            double max,
            String... description) {
        Objects.requireNonNull(key, "Config key cannot be null");
        Objects.requireNonNull(description, "Description cannot be null");
        return builder.comment(description).defineInRange(key, defaultValue, min, max);
    }

    /**
     * Define a list configuration option.
     *
     * @param builder     the ModConfigSpec Builder
     * @param key         the config key
     * @param description description of the list
     * @param defaultList default list values
     * @param validator   validator predicate for list elements
     * @param <T>         type of list elements
     * @return the configured ConfigValue
     */
    public static <T> ModConfigSpec.ConfigValue<List<? extends T>> list(ModConfigSpec.Builder builder,
            String key,
            String description,
            List<T> defaultList,
            Predicate<Object> validator) {
        Objects.requireNonNull(key, "Config key cannot be null");
        Objects.requireNonNull(description, "Description cannot be null");
        Objects.requireNonNull(defaultList, "Default list cannot be null");
        Objects.requireNonNull(validator, "Validator cannot be null");
        return builder.comment(description).define(key, defaultList, validator);
    }

    /**
     * Define a list configuration option with multi-line description.
     *
     * @param builder     the ModConfigSpec Builder
     * @param key         the config key
     * @param defaultList default list values
     * @param validator   validator predicate for list elements
     * @param description multi-line description (varargs)
     * @param <T>         type of list elements
     * @return the configured ConfigValue
     */
    public static <T> ModConfigSpec.ConfigValue<List<? extends T>> list(ModConfigSpec.Builder builder,
            String key,
            List<T> defaultList,
            Predicate<Object> validator,
            String... description) {
        Objects.requireNonNull(key, "Config key cannot be null");
        Objects.requireNonNull(defaultList, "Default list cannot be null");
        Objects.requireNonNull(validator, "Validator cannot be null");
        Objects.requireNonNull(description, "Description cannot be null");
        return builder.comment(description).define(key, defaultList, validator);
    }

    /**
     * Start a new configuration section.
     *
     * @param builder     the ModConfigSpec Builder
     * @param name        section name
     * @param description section description
     * @return the builder for chaining
     */
    public static ModConfigSpec.Builder section(ModConfigSpec.Builder builder,
            String name,
            String description) {
        Objects.requireNonNull(name, "Section name cannot be null");
        Objects.requireNonNull(description, "Description cannot be null");
        return builder.comment(description).push(name);
    }

    /**
     * Start a new configuration section with multi-line description.
     *
     * @param builder     the ModConfigSpec Builder
     * @param name        section name
     * @param description multi-line description (varargs)
     * @return the builder for chaining
     */
    public static ModConfigSpec.Builder section(ModConfigSpec.Builder builder,
            String name,
            String... description) {
        Objects.requireNonNull(name, "Section name cannot be null");
        Objects.requireNonNull(description, "Description cannot be null");
        return builder.comment(description).push(name);
    }

    /**
     * End the current configuration section.
     *
     * @param builder the ModConfigSpec Builder
     * @return the builder for chaining
     */
    public static ModConfigSpec.Builder endSection(ModConfigSpec.Builder builder) {
        return builder.pop();
    }
}
