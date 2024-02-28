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
@SuppressWarnings("unused")
public enum ConfigurationDefaults {

  DEBUG("debug_mode", "true"),
  TOKEN("token", "place your bots token here"),
  PREFIX("prefix", "e!"),
  GAME_STATUS("game_status", "with my friends"),
  SHARDS("shards", "0"),
  OWNER_ID("owner_id", "place your discord id here");

  private final String key;
  private final String value;

  ConfigurationDefaults(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }
}