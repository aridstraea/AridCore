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

import com.aridstraea.aridcore.utilities.constants.OperationStage;

/**
 * Example Main class for the AridCore project.
 *
 * @author aristraea
 * @since 0.1.0-S
 * @version 0.2.0-S
 */
public class MainExample {

  private static final AridCore main = new AridCore();

  /**
   * Main method.
   *
   * @param args Command line arguments.
   */
  public static void main(String[] args) {
    // Start the bot.
    main.enableInternalLogging("AridCore");
    main.enableInternalConfig();
    main.startup();

    /* Add an event listener
    main.registerEventListener(new YourListenerHere);
    */

    /* Register a set of commands
    List<Command> commandList = new Arrays.asList(
        new YourCommand1();
        new YourCommand2();
    );
    main.registerCommands(commandList);
     */

    // Enables music commands. This is false by default.
    // This adds default commands:
    // Play, Pause, Queue, Skip
    // main.enableMusicCommands();

    // Disable internal logging. The bot logs by default.
    // main.disableInternalLogging();
    // You can re-enable it at any time
    // main.enableInternalLogging();

    // To create an external logger instance:
    // Logger externalLogger = new Logger("BotTitle");
    // externalLogger.info("Online!");

    // Change the title
    // externalLogger.setTitle("New Bot Title");

    /* LOGGING */
    AridCore core = new AridCore();
    //core.enableInternalConfig();
    core.enableInternalLogging("AridCore");

    System.out.println("Hi");
    AridCore.getLog().debug("This is a test debug message.", OperationStage.PRE_INIT);
    AridCore.getLog().info("This is a test info message.", OperationStage.INIT);
    AridCore.getLog().warning("This is a test warning message.", OperationStage.POST_INIT);
    AridCore.getLog().info("This is a test info message.", OperationStage.SHUTDOWN);
    AridCore.getLog().info("This is a test info message.", OperationStage.COMMAND_CALL);
    AridCore.getLog().info("This is a test info message.", OperationStage.FILE_UTIL);
    AridCore.getLog().info(AridCoreInfo.VERSION, OperationStage.INIT);
  }
}