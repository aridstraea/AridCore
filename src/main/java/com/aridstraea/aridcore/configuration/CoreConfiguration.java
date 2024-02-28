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

import com.aridstraea.aridcore.utilities.FileUtilities;
import com.aridstraea.aridcore.utilities.constants.ConfigurationDefaults;
import com.aridstraea.aridcore.utilities.constants.OperationStage;
import com.aridstraea.aridcore.utilities.constants.ShutdownStatus;
import com.aridstraea.aridcore.utilities.exceptions.NoConfigurationFileException;
import com.aridstraea.aridcore.core.AridCore;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * CoreConfiguration class of the AridCore project.
 *
 * @author aridstraea
 * @since 0.1.0-S
 */
@SuppressWarnings("unused")
public class CoreConfiguration extends BotConfiguration {

  /*
   * File constants
   */
  private static final String fileName = "config.json";
  private static final String arrayName = "bot";
  private static final HashMap<String, String> keyValuePairs = new HashMap<>() {
    {
      put(ConfigurationDefaults.TOKEN.getKey(), ConfigurationDefaults.TOKEN.getValue());
      put(ConfigurationDefaults.PREFIX.getKey(), ConfigurationDefaults.PREFIX.getValue());
      put(ConfigurationDefaults.DEBUG.getKey(), ConfigurationDefaults.DEBUG.getValue());
      put(ConfigurationDefaults.GAME_STATUS.getKey(), ConfigurationDefaults.GAME_STATUS.getValue());
      put(ConfigurationDefaults.SHARDS.getKey(), ConfigurationDefaults.SHARDS.getValue());
      put(ConfigurationDefaults.OWNER_ID.getKey(), ConfigurationDefaults.OWNER_ID.getValue());
    }
  };

  /**
   * Creates a new CoreConfiguration object.
   */
  public CoreConfiguration() {
    // Check if Configuration File exists.
    File f = new File(getFileName());
    if (!f.exists() || f.isDirectory()) {
      // Create file.
      createConfigurationFile();
    }

    // Check if Configuration File is usable.
    if (!checkConfigurationUsability()) {
      AridCore.getLog().error(
          "Configuration File is not usable.", OperationStage.CONFIGURATION,
          new NoConfigurationFileException(
              "Unusable configuration file. Please check the file at " + getFileName())
      );
      AridCore.shutdown(ShutdownStatus.CONFIG_UNUSABLE);
    }
  }

  @Override
  protected void createConfigurationFile() {
    JSONObject object = new JSONObject();
    JSONArray array = new JSONArray();

    // Put default keys and values into the object.
    for (Map.Entry<String, String> entry : keyValuePairs.entrySet()) {
      object.put(entry.getKey(), entry.getValue());
    }

    FileUtilities.createConfigurationFile(getFileName(), getArrayName(), object);
  }

  /**
   * Checks usability of Configuration file.
   *
   * @return true if usable, false if not.
   */
  @Override
  protected boolean checkConfigurationUsability() {
    // Check critical values

    // If it's null, the method returns default
    // If it isn't the default, it was changed
    // This is the only code-breaking config.
    // Everything else can be the default value;
    // it will run fine.
    return !getToken().contains(ConfigurationDefaults.TOKEN.getValue());
  }

  /**
   * Retrieves prefix for the Bot.
   *
   * @return prefix from Configuration.
   */
  @Override
  public String getPrefix() {
    String value = retrieveValue(ConfigurationDefaults.PREFIX.getKey());
    if (value.equals("" + ShutdownStatus.NO_CONFIG)) {
      return ConfigurationDefaults.PREFIX.getValue();
    } else {
      return value;
    }
  }

  /**
   * Sets a new prefix for the Bot to use.
   *
   * @param newPrefix new prefix for the Bot to use
   */
  @Override
  public void setPrefix(String newPrefix) {
    setValue(ConfigurationDefaults.PREFIX.getKey(), newPrefix);
  }

  /**
   * Retrieves token for the Bot.
   *
   * @return token from Configuration.
   */
  public String getToken() {
    String value = retrieveValue(ConfigurationDefaults.TOKEN.getKey());
    if (value.equals("" + ShutdownStatus.NO_CONFIG.getIdentifier())) {
      AridCore.getLog()
          .info("The requested value was the Token, required for function. Calling for shut down.",
              OperationStage.CONFIGURATION);
      AridCore.getLog().error("No Token in Configuration File.", OperationStage.CONFIGURATION,
          new NoConfigurationFileException("Unusable configuration file. No Token provided."));
      AridCore.shutdown(ShutdownStatus.NO_CONFIG);
    }
    return value;
  }

  /**
   * Retrieves debug status for the Bot.
   *
   * @return debug status from Configuration.
   */
  public boolean getDebug() {
    String value = retrieveValue(ConfigurationDefaults.DEBUG.getKey());
    if (value.equals("" + ShutdownStatus.NO_CONFIG)) {
      return ConfigurationDefaults.DEBUG.getValue().toLowerCase().contains("true");
    } else {
      return value.toLowerCase().contains("true");
    }
  }

  /**
   * Sets a new debug value for the Bot.
   *
   * @param debug true if debug is on, false if not
   */
  public void setDebug(boolean debug) {
    setValue(ConfigurationDefaults.DEBUG.getKey(), "" + debug);
  }

  /**
   * Retrieves shards for the Bot.
   *
   * @return number of shards from Configuration.
   */
  public int getShards() {
    String value = retrieveValue(ConfigurationDefaults.SHARDS.getKey());
    if (value.equals("" + ShutdownStatus.NO_CONFIG)) {
      // Attempt to parse default instead
      try {
        return Integer.parseInt(ConfigurationDefaults.SHARDS.getValue());
      } catch (NumberFormatException nfe) {
        AridCore.getLog()
            .error("Could not parse Shard Count as an integer. Returning 0, regardless of default.",
                OperationStage.CONFIGURATION, nfe);
        return 0;
      }
    } else {
      // Parse returned value
      try {
        return Integer.parseInt(value);
      } catch (NumberFormatException nfe) {
        AridCore.getLog()
            .error("Could not parse Shard Count as an integer. Returning 0, regardless of default.",
                OperationStage.CONFIGURATION, nfe);
        return 0;
      }
    }
  }

  @Override
  public String getOwnerId() {
    String value = retrieveValue(ConfigurationDefaults.OWNER_ID.getKey());
    if (value.equals("" + ShutdownStatus.NO_CONFIG)) {
      return "0";
    } else {
      return value;
    }
  }

  @Override
  public void setOwnerId(String newOwnerId) {
    setValue(ConfigurationDefaults.OWNER_ID.getKey(), newOwnerId);
  }

  /**
   * Retrieves file name of the Configuration.
   *
   * @return file name
   */
  @Override
  public String getFileName() {
    return fileName;
  }

  /**
   * Retrieves array name of the Configuration.
   *
   * @return array name
   */
  @Override
  public String getArrayName() {
    return arrayName;
  }
}
