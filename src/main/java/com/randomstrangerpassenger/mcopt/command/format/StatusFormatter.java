package com.randomstrangerpassenger.mcopt.command.format;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Objects;

/**
 * Centralized formatting utilities for command output.
 * <p>
 * Provides consistent styling for status messages, module reports,
 * and configuration displays.
 * </p>
 */
public class StatusFormatter {

    private static final String HEADER_LINE = "═══════════════════════════════════";

    private StatusFormatter() {
        // Utility class
    }

    /**
     * Format a section header.
     */
    public static MutableComponent formatHeader(String title) {
        Objects.requireNonNull(title, "Title cannot be null");
        return Component.literal(title)
                .withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD);
    }

    /**
     * Format the main title with decorations.
     */
    public static MutableComponent formatTitle(String title) {
        Objects.requireNonNull(title, "Title cannot be null");
        return Component.literal(title)
                .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD);
    }

    /**
     * Format a decorative separator line.
     */
    public static MutableComponent formatSeparator() {
        return Component.literal(HEADER_LINE)
                .withStyle(ChatFormatting.GOLD);
    }

    /**
     * Format an enabled feature status.
     */
    public static MutableComponent formatEnabled(String feature) {
        Objects.requireNonNull(feature, "Feature cannot be null");
        String formatted = Objects.requireNonNull(
                String.format("  %s: ON", feature), "Formatted string cannot be null");
        return Component.literal(formatted)
                .withStyle(ChatFormatting.GREEN);
    }

    /**
     * Format a disabled feature status.
     */
    public static MutableComponent formatDisabled(String feature) {
        Objects.requireNonNull(feature, "Feature cannot be null");
        String formatted = Objects.requireNonNull(
                String.format("  %s: OFF", feature), "Formatted string cannot be null");
        return Component.literal(formatted)
                .withStyle(ChatFormatting.GRAY);
    }

    /**
     * Format a feature with custom status.
     */
    public static MutableComponent formatModule(String name, boolean enabled) {
        Objects.requireNonNull(name, "Name cannot be null");
        String status = enabled ? "ON" : "OFF";
        ChatFormatting color = enabled ? ChatFormatting.GREEN : ChatFormatting.GRAY;
        String formatted = Objects.requireNonNull(
                String.format("  %s: %s", name, status), "Formatted string cannot be null");
        return Component.literal(formatted)
                .withStyle(color);
    }

    /**
     * Format a configuration value.
     */
    public static MutableComponent formatConfig(String label, Object value) {
        Objects.requireNonNull(label, "Label cannot be null");
        String formatted = Objects.requireNonNull(
                String.format("  %s: %s", label, value), "Formatted string cannot be null");
        return Component.literal(formatted)
                .withStyle(ChatFormatting.GRAY);
    }

    /**
     * Format a warning message.
     */
    public static MutableComponent formatWarning(String message) {
        Objects.requireNonNull(message, "Message cannot be null");
        return Component.literal(message)
                .withStyle(ChatFormatting.YELLOW);
    }

    /**
     * Format an info message.
     */
    public static MutableComponent formatInfo(String message) {
        Objects.requireNonNull(message, "Message cannot be null");
        return Component.literal(message)
                .withStyle(ChatFormatting.GREEN);
    }

    /**
     * Format a conflict/error message.
     */
    public static MutableComponent formatConflict(String message) {
        Objects.requireNonNull(message, "Message cannot be null");
        return Component.literal(message)
                .withStyle(ChatFormatting.RED, ChatFormatting.BOLD);
    }

    /**
     * Format an empty line.
     */
    public static MutableComponent formatEmptyLine() {
        return Component.literal("");
    }

    /**
     * Format memory usage with color based on percentage.
     */
    public static MutableComponent formatMemoryUsage(long used, long max, int percent) {
        ChatFormatting color;
        if (percent > 90) {
            color = ChatFormatting.RED;
        } else if (percent > 75) {
            color = ChatFormatting.YELLOW;
        } else {
            color = ChatFormatting.GREEN;
        }

        String formatted = Objects.requireNonNull(
                String.format("  Used: %dMB / %dMB (%d%%)", used, max, percent),
                "Formatted string cannot be null");
        return Component.literal(formatted)
                .withStyle(color);
    }
}
