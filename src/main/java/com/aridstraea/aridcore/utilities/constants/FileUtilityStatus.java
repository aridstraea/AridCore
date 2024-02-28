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
public enum FileUtilityStatus {

  WRITE_SUCCESS("Writing to file success.", 1),
  WRITE_FAIL("Writing to file failed.", -1);

  private final String description;
  private final int identifier;

  FileUtilityStatus(String description, int identifier) {
    this.description = description;
    this.identifier = identifier;
  }

  public String getReason() {
    return description;
  }

  public int getIdentifier() {
    return identifier;
  }
}
