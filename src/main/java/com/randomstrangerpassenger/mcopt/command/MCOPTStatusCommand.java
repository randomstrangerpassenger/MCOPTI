package com.randomstrangerpassenger.mcopt.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.randomstrangerpassenger.mcopt.command.format.StatusFormatter;
import com.randomstrangerpassenger.mcopt.command.reporters.MemoryStatusReporter;
import com.randomstrangerpassenger.mcopt.command.reporters.ModuleStatusReporter;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

/**
 * Command to display MCOPT's current status and configuration.
 * <p>
 * Usage: /mcopt status
 * <p>
 * This class acts as an orchestrator, delegating specific reporting tasks
 * to dedicated reporter classes for better maintainability.
 *
 * @see ModuleStatusReporter for active module reporting
 * @see MemoryStatusReporter for memory and config reporting
 * @see StatusFormatter for consistent output formatting
 */
public class MCOPTStatusCommand {

        public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
                dispatcher.register(
                                Commands.literal("mcopt")
                                                .then(Commands.literal("status")
                                                                .executes(MCOPTStatusCommand::executeStatus))
                                                .then(Commands.literal("report")
                                                                .executes(MCOPTStatusCommand::executeStatus)));
        }

        private static int executeStatus(CommandContext<CommandSourceStack> context) {
                CommandSourceStack source = context.getSource();

                // Header
                source.sendSuccess(() -> StatusFormatter.formatSeparator(), false);
                source.sendSuccess(() -> StatusFormatter.formatTitle("MCOPT Status Report"), false);
                source.sendSuccess(() -> StatusFormatter.formatSeparator(), false);

                // Memory Status
                MemoryStatusReporter.sendMemoryStatus(source);

                // Active Modules
                ModuleStatusReporter.sendModuleStatus(source);

                // Configuration Details
                MemoryStatusReporter.sendConfigurationStatus(source);

                // Incompatible Mods
                ModuleStatusReporter.sendConflictStatus(source);

                // Footer
                source.sendSuccess(() -> StatusFormatter.formatSeparator(), false);

                return 1;
        }
}
