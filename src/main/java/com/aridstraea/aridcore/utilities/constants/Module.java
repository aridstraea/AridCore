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
 * Module Class of the AridCore project.
 * Defines modules supported by the Command System.
 *
 * @author aristraea
 * @since v0.1.0-S
 */
public enum Module {
  GENERIC("Generic Commands", "General use commands."),
  ADMIN("Administrative Commands", "High-level commands for use only by administration."),
  FUN("Fun Commands", "Commands with no practical purpose; only fun use."),
  UTILITY("Utility Commands", "Commands that are only for practical purpose."),
  MUSIC("Music Commands", "Commands for controlling the music player.");

  private final String name;
  private final String description;

  Module(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }
}