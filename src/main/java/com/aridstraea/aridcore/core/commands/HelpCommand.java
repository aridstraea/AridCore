/*
 *  Copyright 2024 aristraea
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.aridstraea.aridcore.core.commands;

import com.aridstraea.aridcore.utilities.MessageUtilities;
import com.aridstraea.aridcore.utilities.constants.Module;
import com.aridstraea.aridcore.utilities.constants.OperationStage;
import com.aridstraea.aridcore.core.AridCore;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * HelpCommand class of the AridCore project.
 * All methods are explained in {@link Command}
 *
 * @author aristraea
 * @since 0.1.0-S
 */
public class HelpCommand extends Command {

  private static final String NO_NAME = "No name provided for this command.";
  private static final String NO_DESCRIPTION = "No description provided for this command.";
  private static final String NO_USAGE = "No usage instructions provided for this command.";

  public final HashMap<String, Command> commands;
  public List<Module> modules;

  /**
   * Creates a new HelpCommand.
   */
  public HelpCommand() {
    commands = new HashMap<>();
    registerModules();
  }

  /**
   * Adds command to TreeMap.
   *
   * @param command command to add
   * @return command added.
   */
  public Command registerCommand(Command command) {
    commands.put(command.getAliases().get(0), command);
    return command;
  }

  private void registerModules() {
    modules = Arrays.asList(
        Module.ADMIN,
        Module.FUN,
        Module.GENERIC,
        Module.MUSIC
    );
  }

  @Override
  public void onSlashCommand(@NotNull SlashCommandInteractionEvent sce) {
    // TODO slash help command
    AridCore.getLog().info("SLASH / HELP", OperationStage.COMMAND_CALL);
    //sce.reply(AridCore.getHelp().buildEmbed()).setEphemeral(true).queue();
  }

  @Override
  public CommandData getSlashCommandData() {
    return Commands.slash("help", "Help from the bot.");
  }

  @Override
  public void onCommand(MessageReceivedEvent mre, String[] args) {
    AridCore.getLog().info("HELP", OperationStage.COMMAND_CALL);
    // Bypass sending message if it is already in a private message.
    MessageUtilities.sendIfNotPrivate(mre);
    // Send help message
    EmbedBuilder embed = AridCore.getHelp().buildEmbed(args);
    PrivateChannel channel = mre.getAuthor().openPrivateChannel().complete();

    if (embed == null) {
      MessageUtilities.doesNotExist(channel, args[1], "commands");
      return;
    }
    channel.sendMessageEmbeds(embed.build()).queue();
  }

  @Override
  public List<String> getAliases() {
    return Arrays.asList("help", "commands");
  }

  @Override
  public String getDescription() {
    return "Command that helps use all other commands!";
  }

  @Override
  public String getName() {
    return "Help Command";
  }

  @Override
  public List<String> getUsage() {
    return Collections.singletonList(
        AridCore.getConfig().getPrefix() + "help   **OR**  " + AridCore.getConfig().getPrefix()
            + "help *<command>*\n"
            + AridCore.getConfig().getPrefix()
            + "help - returns the list of commands along with a simple description of each.\n"
            + AridCore.getConfig().getPrefix()
            + "help <command> - returns the name, description, aliases and usage of a command.\n"
            + "   - This can use the aliases of a command as input as well.\n"
            + "__Example:__ " + AridCore.getConfig().getPrefix() + "help ping");
  }

  @Override
  public boolean getDefaultPermission() {
    return true;
  }

  @Override
  public Module getModule() {
    return Module.GENERIC;
  }

  /**
   * Sends a message to a private channel.
   *
   * @param args    arguments used to build the message.
   */
  private EmbedBuilder buildEmbed(String[] args) {
    if (args.length < 2) {
      AridCore.getLog().info("Creating Modules Supported embed.", OperationStage.COMMAND_CALL);
      EmbedBuilder embed = new EmbedBuilder().setTitle("Modules Supported").setColor(Color.RED);
      MessageUtilities.addEmbedDefaults(embed);

      // For each Module, add its values to embed.
      for (Module m : modules) {
        AridCore.getLog().debug("Adding module: " + m, OperationStage.COMMAND_CALL);
        if (commandsInModule(m) > 0) {
          embed.addField(m.getName(), commandsInModule(m) + " commands active", true);
        }
      }

      // Send embed.
      return embed;
    } else {
      EmbedBuilder embed = new EmbedBuilder();

      MessageUtilities.addEmbedDefaults(embed);

      String command = args[1].toLowerCase();
      // Check each command. If it is the command searched for, build embed.
      for (Command c : commands.values()) {
        if (c.getAliases().contains(command)) {
          // Define values.
          addCommandValues(embed, c);

          // Send embed.
          return embed;
        }
      }

      // Needs to find module, then list all commands with that module.
      boolean hasCommand = false;
      for (Module module : modules) {
        if (module.getName().toLowerCase().contains(command)) {
          List<Command> commandsInModule = getCommandsInModule(module);
          for (Command c : commandsInModule) {
            // Define values.
            embed.addField(c.getName(), "", false);
            hasCommand = true;
          }
        }
        // Send embed.
        if (hasCommand) {
          return embed;
        }
      }

      // If it reaches this point, the command/module searched for does not exist.
      return null;
    }
  }

  /**
   * Add Commands to the embed.
   *
   * @param embed embed to add Commands to
   * @param c     Command to add
   */
  private void addCommandValues(EmbedBuilder embed, Command c) {
    String name = c.getName();
    String description = c.getDescription();
    List<String> usageInstructions = c.getUsage();

    // Null checks.
    if (name == null || name.isEmpty()) {
      name = NO_NAME;
    }
    if (description == null || description.isEmpty()) {
      description = NO_DESCRIPTION;
    }
    if (usageInstructions == null || usageInstructions.isEmpty()) {
      usageInstructions = Collections.singletonList(NO_USAGE);
    }

    // Add values to embed.
    embed.setTitle(name);
    embed.addField(description, "", false);
    embed.addField("Aliases", StringUtils.join(c.getAliases(), ", ") + "\n", false);
    embed.addField("Usage", usageInstructions.get(0), false);

    // Add remaining usage instructions.
    for (int i = 1; i < usageInstructions.size(); i++) {
      embed.addField("", usageInstructions.get(i), false);
    }
  }

  /**
   * Returns a list of Commands within a given module.
   *
   * @param module Module to search in
   * @return List of commands
   */
  private List<Command> getCommandsInModule(Module module) {
    List<Command> activeCommands = new ArrayList<>();
    AridCore.getLog()
        .debug("Retrieving commands in module " + module, OperationStage.COMMAND_CALL);

    for (Command command : commands.values()) {
      AridCore.getLog()
          .debug("Checking command: " + command.getName(), OperationStage.COMMAND_CALL);
      if (command.getModule().equals(module)) {
        AridCore.getLog().debug("Command is in module " + module, OperationStage.COMMAND_CALL);
        activeCommands.add(command);
      } else {
        AridCore.getLog()
            .debug("Command is not in module " + module, OperationStage.COMMAND_CALL);
      }
    }

    return activeCommands;
  }

  /**
   * Returns the number of commands in a module.
   *
   * @param module Module to search in
   * @return Number of commands in Module
   */
  private int commandsInModule(Module module) {
    return getCommandsInModule(module).size();
  }
}