/*
 *  Copyright 2024 aridstraea
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

package com.aridstraea.aridcore.configuration;

import com.aridstraea.aridcore.core.AridCore;
import com.aridstraea.aridcore.utilities.FileUtilities;
import com.aridstraea.aridcore.utilities.constants.OperationStage;
import com.aridstraea.aridcore.utilities.constants.ShutdownStatus;
import com.aridstraea.aridcore.utilities.exceptions.NoConfigurationFileException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Configuration class of the AridCore project.
 *
 * @author aridstraea
 * @since 0.1.0-S
 */
@SuppressWarnings("unused")
public abstract class Configuration {

  protected abstract void createConfigurationFile();

  /**
   * Checks usability of Configuration file.
   *
   * @return true if usable, false if not.
   */
  protected abstract boolean checkConfigurationUsability();

  /**
   * Retrieves the name of the Configuration File.
   *
   * @return name of the Configuration File.
   */
  public abstract String getFileName();

  /**
   * Retrieves the name of the JSON array in the Configuration File.
   *
   * @return name of the JSON array in the Configuration File.
   */
  public abstract String getArrayName();

  /**
   * Sets a new value in the Configuration File.
   *
   * @param key key to the value to set
   * @param value new value
   */
  public void setValue(String key, String value) {
    JSONObject object = FileUtilities.getJsonFileObject(getFileName());
    if (object != null) {
      object.put(key, value);
      writeToFile(object);
    } else {
      AridCore.getLog().error("Configuration file not found.", OperationStage.CONFIGURATION,
        new NoConfigurationFileException("Configuration file not found."));
    }
  }

  /**
   * Retrieves a value from the Configuration File.
   *
   * @param key key to the value to retrieve
   * @return value of the key
   */
  public String retrieveValue(String key) {
    String value = FileUtilities.getValueByKey(getFileName(), key, getArrayName());

    if (value.contains("" + ShutdownStatus.NO_CONFIG.getIdentifier())) {
      AridCore.getLog().warning(
          "Failed to grab value for: **"
              + key
              + "** on file>array **"
              + getFileName()
              + ">"
              + getArrayName()
              + "**. Requesting default value for given key.",
            OperationStage.CONFIGURATION
      );
      return "" + ShutdownStatus.NO_CONFIG.getIdentifier();
    }

    return value;
  }

  /**
   * Adds a JSON Array to the Configuration File.
   *
   * @param obj JSONObject to add to file.
   */
  @SuppressWarnings("unused")
  protected int writeToFile(JSONArray obj) {
    // Write to the file.
    return FileUtilities.writeToFile(obj, getFileName()).getIdentifier();
  }

  /**
   * Adds a JSON Object to the Configuration File.
   *
   * @param obj JSONObject to add to file.
   */
  @SuppressWarnings("all")
  protected int writeToFile(JSONObject obj) {
    // Write to the file.
    return FileUtilities.writeToFile(obj, getFileName()).getIdentifier();
  }

  /**
   * Retrieves the JSONObject to read the JSON File.
   *
   * @return JSONObject
   */
  @SuppressWarnings("unused")
  private JSONArray getJsonFile(String fileName) {
    return FileUtilities.getJsonFileArray(fileName);
  }

}
