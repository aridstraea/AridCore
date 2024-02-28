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
public enum OperationStage {

  PRE_INIT("Pre-Initialization", 1),
  INIT("Initialization", 2),
  POST_INIT("Post-Initialization", 3),

  COMMAND_CALL("Command Call", 4),

  FILE_UTIL("File Operations", 4),
  SHUTDOWN("Shut Down", 5),

  CONFIGURATION("Configuration Usage", 6);

  private final String name;
  private final int identifier;

  OperationStage(String name, int identifier) {
    this.name = name;
    this.identifier = identifier;
  }

  public String getName() {
    return name;
  }

  public int getIdentifier() {
    return identifier;
  }
}
