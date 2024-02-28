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

package com.aridstraea.aridcore.utilities;

import com.aridstraea.aridcore.utilities.constants.OperationStage;
import com.aridstraea.aridcore.core.AridCoreInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logger class of the AridCore project.
 *
 * @author aristraea
 * @since 0.1.0-S
 */
public class InternalLogger {

  private static final String title = "AridCore";
  private static boolean log = true;
  public static final String VERSION = "0";
  public static final String BUILD_NUMBER = "0.2S";
  public static final String JVM = System.getProperty("java.version");

  private static Logger internalLogger;

  public InternalLogger(String name) {
    internalLogger = LoggerFactory.getLogger(name);
  }

  public InternalLogger() {
    internalLogger = LoggerFactory.getLogger(title);
  }

  /**
   * Welcomes user to the Core.
   */
  public void welcome() {
    blank("", ""
        + "                                     ,--,   ,----..            \n"
        + "      ,---,.     ,----..           ,--.'|  /   /   \\          \n"
        + "    ,'  .' |    /   /   \\       ,--,  | : /   .     :         \n"
        + "  ,---.'   |   |   :     :   ,---.'|  : '.   /   ;.  \\        \n"
        + "  |   |   .'   .   |  ;. /   |   | : _' .   ;   /  ` ;         \n"
        + "  :   :  |-,   .   ; /--`    :   : |.'  ;   |  ; \\ ; |        \n"
        + "  :   |  ;/|   ;   | ;       |   ' '  ; |   :  | ; | '         \n"
        + "  |   :   .'   |   : |       '   |  .'. .   |  ' ' ' :         \n"
        + "  |   |  |-,   .   | '___    |   | :  | '   ;  \\; /  |        \n"
        + "  '   :  ;/|   '   ; : .'|   '   : |  : ;\\   \\  ',  /        \n"
        + "  |   |    \\___'   | '/  ___ |   | '  ,___;   :    /          \n"
        + "  |   :   ./  .|   :    /  .\\;   : ;--/  .\\   \\ .'          \n"
        + "  |   | ,' \\  ; \\   \\ .'\\  ; |   ,/   \\  ; |`---`         \n"
        + "  `----'    `--\" `---`   `--\"'---'     `--\"                 \n"
        + "");

    blank("", "E  C  H  O  E  D  C  O  R  E  -  V  E  R  S  I  O  N  " + VERSION);
    blank("", "\t\t\t\tVersion: \t" + AridCoreInfo.VERSION_MAJOR);
    blank("", "\t\t\t\tBuild: \t\t" + AridCoreInfo.VERSION_MINOR);
    blank("", "\n\n\n");
  }

  /**
   * Toggles if internal logging should be used.
   *
   * @param logging true if yes, false if no
   */
  public void setLogging(boolean logging) {
    log = logging;
  }

  public static boolean isLogging() {
    return log;
  }

  /**
   * Logs an informational message.
   *
   * @param message Message to log
   */
  public void info(String message, OperationStage stage) {
    if (isLogging()) {
      internalLogger.info(formatString(message, stage));
    }
  }

  /**
   * Logs a warning message.
   *
   * @param message Message to log
   */
  public void warning(String message, OperationStage stage) {
    if (isLogging()) {
      internalLogger.warn(formatString(message, stage));
    }
  }

  /**
   * Logs an error message.
   *
   * @param message Message to log
   * @param e       Exception encountered
   */
  public void error(String message, OperationStage stage, Exception e) {
    if (isLogging()) {
      internalLogger.error(formatString(message, stage), e);
    }
  }

  /**
   * Logs a debug message.
   *
   * @param message Message to log
   * @param stage   Stage of log
   */
  public void debug(String message, OperationStage stage) {
    if (isLogging()) {
      internalLogger.debug(formatString(message, stage));
    }
  }

  /**
   * Logs a custom message.
   *
   * @param message message to log
   * @param header  header of message, eg. "[EchoedBot/Utils]"
   */
  public void blank(String header, String message) {
    if (isLogging()) {
      System.out.println(header + message);
    }
  }

  /**
   * Formats a string to be logged.
   *
   * @param message Message to log
   * @param stage  Stage of log
   * @return Formatted string
   */
  private String formatString(String message, OperationStage stage) {
    StringBuilder format = new StringBuilder();

    format.append("[").append(stage.getName()).append("]\t");

    // Tab Time
    if (stage.equals(OperationStage.SHUTDOWN) || stage.equals(OperationStage.COMMAND_CALL)) {
      format.append("\t\t");
    } else if (stage.equals(OperationStage.INIT) || stage.equals(OperationStage.FILE_UTIL)) {
      format.append("\t");
    }

    format.append(message);
    return format.toString();
  }
}