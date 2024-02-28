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

package com.aridstraea.aridcore.core;

import com.aridstraea.aridcore.configuration.BotConfiguration;
import com.aridstraea.aridcore.configuration.Configuration;
import com.aridstraea.aridcore.configuration.CoreConfiguration;
import com.aridstraea.aridcore.core.commands.Command;
import com.aridstraea.aridcore.core.commands.HelpCommand;
import com.aridstraea.aridcore.core.listeners.TagListener;
import com.aridstraea.aridcore.utilities.InternalLogger;
import com.aridstraea.aridcore.utilities.constants.OperationStage;
import com.aridstraea.aridcore.utilities.constants.ShutdownStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;

/**
 * AridCore class of the AridCore project The very Core of the framework.
 * Contains all commands for instantiating the Bot.
 *
 * @author aristraea
 * @version 0.2.0-S
 * @since 0.1.0-S
 */
@SuppressWarnings("unused")
public class AridCore {

  // jda specific
  private static JDA api;
  private static JDABuilder builder;

  // core specific
  private static InternalLogger log;
  private static BotConfiguration config;
  private static final HelpCommand help = new HelpCommand();
  private static long time = 0;

  private static final ArrayList<Command> availableCommands = new ArrayList<>() {
    {
      // Add mandatory Core commands

      // GENERIC
      add(getHelp());
    }
  };
  private static final ArrayList<Command> availableGuildSlashCommands = new ArrayList<>() {
    {
      // Add mandatory Core Slash commands
    }
  };

  private static final ArrayList<Command> availableGlobalSlashCommands = new ArrayList<>() {
    {
      // Add mandatory Core Slash commands
    }
  };

  public AridCore() {
    time = System.currentTimeMillis();
  }

  // ----- Accessible Bot Methods -----

  /**
   * Disables the use of the {@link InternalLogger} internally.
   *
   * @return AridCore instance
   */
  @SuppressWarnings("UnusedReturnValue")
  public AridCore disableInternalLogging() {
    getLog().setLogging(false);
    return this;
  }

  /**
   * Enabled the use of the {@link InternalLogger} internally.
   *
   * @param loggerName Name of the {@link InternalLogger} instance
   * @return AridCore instance
   */
  @SuppressWarnings("UnusedReturnValue")
  public AridCore enableInternalLogging(String loggerName) {
    log = new InternalLogger(loggerName);

    getLog().setLogging(true);
    return this;
  }

  /**
   * Enables the use of the {@link Configuration} internally.
   * Do not enable this if you need to add your own values.
   *
   * @return AridCore instance
   */
  @SuppressWarnings("UnusedReturnValue")
  public AridCore enableInternalConfig() {
    config = new CoreConfiguration();
    return this;
  }

  /**
   * Registers an external {@link Configuration} for use by the core.
   * This is optional and only needed if you need to add your own values.
   *
   * @param externalConfig The custom {@link Configuration} file
   * @return AridCore instance
   */
  public AridCore registerConfiguration(BotConfiguration externalConfig) {
    config = externalConfig;
    return this;
  }

  /**
   * Registers an event listener.
   *
   * @param listener Event listener to register
   * @return AridCore instance
   */
  @SuppressWarnings("UnusedReturnValue")
  public AridCore registerEventListener(Object... listener) {
    try {
      api.addEventListener(listener);
    } catch (Exception e) {
      log.error("Unable to register Event Listeners.", OperationStage.COMMAND_CALL, e);
      shutdown(ShutdownStatus.NO_EVENT);
    }
    return this;
  }

  /**
   * Adds a list of (non-slash) commands.
   *
   * @param commands Commands to register
   * @return AridCore instance
   */
  @SuppressWarnings("UnusedReturnValue")
  public AridCore addCommands(List<Command> commands) {
    for (Command command : commands) {
      addCommand(command);
    }
    return this;
  }

  /**
   * Adds a single (non-slash) command.
   *
   * @param command Command to register
   * @return AridCore instance
   */
  @SuppressWarnings("UnusedReturnValue")
  public AridCore addCommand(Command command) {
    availableCommands.add(command);
    return this;
  }

  /**
   * Adds a list of (global-use) slash commands.
   *
   * @param commands Commands to register
   * @return AridCore instance
   */
  @SuppressWarnings("UnusedReturnValue")
  public AridCore addGlobalSlashCommands(List<Command> commands) {
    for (Command command : commands) {
      addGlobalSlashCommand(command);
    }
    return this;
  }

  /**
   * Adds a (global-use) slash command.
   *
   * @param command Command to register
   * @return AridCore instance
   */
  @SuppressWarnings("UnusedReturnValue")
  public AridCore addGlobalSlashCommand(Command command) {
    availableGlobalSlashCommands.add(command);
    return this;
  }

  /**
   * Adds a list of (guild-use) slash commands.
   */
  @SuppressWarnings("UnusedReturnValue")
  public AridCore addGuildSlashCommands(List<Command> commands) {
    for (Command command : commands) {
      addGuildSlashCommand(command);
    }
    return this;
  }

  /**
   * Adds a (guild-use) slash command.
   */
  @SuppressWarnings("UnusedReturnValue")
  public AridCore addGuildSlashCommand(Command command) {
    availableGuildSlashCommands.add(command);
    return this;
  }

  /**
   * Registers all added commands. Slash commands include global & guild-only sets.
   *
   * @param guildId the ID of the guild to register
   * @return AridCore instance
   */
  public AridCore registerCommands(String guildId) {
    // Register all non-slash commands
    for (Command command : availableCommands) {
      registerEventListener(getHelp().registerCommand(command));
    }

    Guild guild = api.getGuildById(guildId);

    // Register GUILD-ONLY Slash Commands
    if (guild != null) {
      ArrayList<CommandData> guildOnlyCommands = new ArrayList<>();
      for (Command command : availableGuildSlashCommands) {
        guildOnlyCommands.add(command.getSlashCommandData());
        registerEventListener(getHelp().registerCommand(command));
      }
      guild.updateCommands().addCommands(guildOnlyCommands).queue();
    } else {
      getLog().error("Cannot update guild-only commands.", OperationStage.INIT,
          new NullPointerException("Guild is null."));
    }

    // Register GLOBAL Slash Commands
    ArrayList<CommandData> globalCommands = new ArrayList<>();
    for (Command command : availableGlobalSlashCommands) {
      globalCommands.add(command.getSlashCommandData());
      registerEventListener(getHelp().registerCommand(command));
    }
    getApi().updateCommands().addCommands(globalCommands).queue();

    return this;
  }

  /**
   * Starts the Bot.
   *
   * @return EchoedCore instance
   */
  @SuppressWarnings("UnusedReturnValue")
  public AridCore startup() {
    getLog().welcome();
    debugOnlyInitialization();
    preInitialization();
    initialization();
    postInitialization();
    return this;
  }

  // ----- Internal Methods -----

  /**
   * Prints debug information upon startup.
   */
  private void debugOnlyInitialization() {
    if (config.getDebug()) {
      getLog().debug("Welcome to EchoedCore! \n \n", OperationStage.PRE_INIT);
      getLog().debug("Prefix: " + config.getPrefix(), OperationStage.PRE_INIT);
      getLog().debug("Game Status: " + "config.getGameStatus()", OperationStage.PRE_INIT);
      getLog().debug("Debug Status: " + "true", OperationStage.PRE_INIT);
      getLog().debug("Token: " + config.getToken(), OperationStage.PRE_INIT);
    }
  }

  /**
   * Initialize the Bot instance and define startup time.
   */
  private void preInitialization() {
    getLog().debug("Beginning Pre-Initialization.", OperationStage.PRE_INIT);

    time = System.currentTimeMillis();

    builder = JDABuilder.createDefault(config.getToken())
        .enableIntents(GatewayIntent.MESSAGE_CONTENT)
        .setAutoReconnect(true);
    //.setActivity(Activity.watching("time pass by"));
  }

  /**
   * Initialize the JDA instance.
   */
  private void initialization() {
    getLog().debug("Beginning initialization.", OperationStage.INIT);
    // Define the JDA Instance.
    try {
      getLog().debug("Defining JDA instance.", OperationStage.INIT);

      if (getConfig().getShards() > 0) {
        // Adding event listeners.
        registerEventListeners();
        // Sharding.
        for (int i = 0; i < getConfig().getShards(); i++) {
          api = builder.useSharding(i, config.getShards())
              .build();
          api.awaitReady();
        }
      } else {
        api = builder.build();
        registerEventListeners();
        api.awaitReady();
      }
    } catch (InterruptedException ie) {
      getLog().error("Interrupted upon waiting JDA Instance.", OperationStage.INIT, ie);
      shutdown(ShutdownStatus.NO_JDA);
    } catch (ErrorResponseException ere) {
      getLog().error("Unable to connect.", OperationStage.INIT, ere);
      shutdown(ShutdownStatus.UNABLE_TO_CONNECT);
    }

  }

  /**
   * Post-initialization.
   */
  private void postInitialization() {
    getLog().debug("Beginning post-initialization.", OperationStage.POST_INIT);

    // Set the Bot's ID.
    try {
      getLog().debug("Bot ID: " + getId(), OperationStage.POST_INIT);
    } catch (Exception e) {
      getLog().error(
          "Error retrieving Bot ID. This is not a vital step, but may cause issues later.",
          OperationStage.POST_INIT, e);
    }

    // Set auto-reconnect to true & set game status.
    api.setAutoReconnect(true);
    api.getPresence().setActivity(Activity.watching("time pass by"));
  }

  /**
   * Adds default event listeners.
   */
  private void registerEventListeners() {
    registerEventListener(new TagListener());
  }

  // ----- Getter Methods -----

  /**
   * Retrieve the {@link Configuration} instance.
   *
   * @return The {@link Configuration} instance used by the bot
   */
  public static BotConfiguration getConfig() {
    return config;
  }

  /**
   * Retrieve the {@link InternalLogger} instance.
   *
   * @return The {@link InternalLogger} instance used by the bot
   */
  public static InternalLogger getLog() {
    return log;
  }

  /**
   * Retrieve the {@link HelpCommand} instance.
   *
   * @return The {@link HelpCommand} instance used by the bot
   */
  public static HelpCommand getHelp() {
    return help;
  }

  /**
   * Retrieve the Bot's ID.
   *
   * @return The Bot's ID
   */
  public static String getId() {
    return getApi().getSelfUser().getId();
  }

  /**
   * Retrieve the {@link JDA} instance.
   *
   * @return The {@link JDA} instance used by the bot
   */
  public static JDA getApi() {
    return api;
  }

  private static void checkDependencies() {
    // TODO find a way to compare maven dependencies
    String jdaVersion = JDAInfo.VERSION;
  }

  /**
   * Shutdown the Bot Instance and exit the program.
   *
   * @param status Status of the shutdown
   */
  public static void shutdown(ShutdownStatus status) {
    long endTime = System.currentTimeMillis();
    long timeActive = endTime - time;

    getLog().info("Active for " + ((timeActive / 1000) / 60) + " minutes. (" + (timeActive / 1000)
        + " seconds)", OperationStage.SHUTDOWN);
    getLog().info("Beginning shutdown.", OperationStage.SHUTDOWN);

    // Remove event listeners. The Bot can shut down before these are defined.
    try {
      api.removeEventListener(api.getRegisteredListeners());
    } catch (NullPointerException npe) {
      getLog().debug("No Event Listeners to remove.", OperationStage.SHUTDOWN);
    }

    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException ie) {
      getLog().debug("Ignored InterruptedException on shut down.", OperationStage.SHUTDOWN);
    }

    if (status != ShutdownStatus.NO_JDA && status != ShutdownStatus.CONFIG_UNUSABLE
        && status != ShutdownStatus.UNABLE_TO_CONNECT) {
      api.shutdownNow();
    }
    if (status.getIdentifier() != 0) {
      getLog().warning(
          "This shutdown was caused by an error. Please review the reason for shutdown:\n"
              + status.getReason(), OperationStage.SHUTDOWN);
    }
    System.exit(status.getIdentifier());
  }

}