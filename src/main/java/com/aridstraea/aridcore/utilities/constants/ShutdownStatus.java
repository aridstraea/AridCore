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

package com.aridstraea.aridcore.utilities.constants;

/**
 * Constants used by the AridCore project.
 *
 * @author aristraea
 * @since 0.2.0-S
 */
public enum ShutdownStatus {

  FRIENDLY("", 0),
  NO_EVENT("No event listeners were found.", -1),
  NO_JDA("The JDA instance could not be defined or found.", -2),
  NO_CONFIG(
      "The configuration file does not exist. Please restart with a defined configuration file.",
      -3),
  CONFIG_UNUSABLE("The configuration was not usable.", -4),
  UNABLE_TO_CONNECT("Unable to connect to the server.", -5);

  private final String reason;
  private final int identifier;

  ShutdownStatus(String reason, int identifier) {
    this.reason = reason;
    this.identifier = identifier;
  }

  public String getReason() {
    return reason;
  }

  public int getIdentifier() {
    return identifier;
  }
}
