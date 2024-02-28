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

import com.aridstraea.aridcore.utilities.constants.FileUtilityStatus;
import com.aridstraea.aridcore.utilities.constants.OperationStage;
import com.aridstraea.aridcore.utilities.constants.ShutdownStatus;
import com.aridstraea.aridcore.utilities.exceptions.NoConfigurationFileException;
import com.aridstraea.aridcore.core.AridCore;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * FileUtilities class of the AridCore project.
 *
 * @author aristraea
 * @since 0.1.0-S
 */
@SuppressWarnings("unused")
public class FileUtilities {

  /**
   * Create a Configuration File from default keys and values.
   */
  public static void createConfigurationFile(String fileName, String arrayName, JSONObject object) {
    // Create Bot config options.
    JSONArray array = new JSONArray();

    array.put(object);

    JSONObject obj2 = new JSONObject();
    obj2.put(arrayName, object);

    // Write to the file.
    if (writeToFile(obj2, fileName).getIdentifier() < 0) {
      AridCore.getLog()
          .error("Unable to create configuration file.", OperationStage.CONFIGURATION,
              new NoConfigurationFileException("Unusable configuration file."));
      AridCore.shutdown(ShutdownStatus.NO_CONFIG);
    }
  }

  /**
   * Adds a JSON Array to the Configuration File.
   *
   * @param obj JSONObject to add to file
   */
  @SuppressWarnings("Duplicates")
  public static FileUtilityStatus writeToFile(JSONObject obj, String fileName) {
    // Write to the file.
    try {
      FileWriter fileWriter = new FileWriter(fileName);
      fileWriter.write(
          obj.toString(4)
      );
      fileWriter.flush();
      fileWriter.close();
      return FileUtilityStatus.WRITE_SUCCESS;
    } catch (IOException ioe) {
      AridCore.getLog().error("Unable to write to file.", OperationStage.FILE_UTIL, ioe);
      return FileUtilityStatus.WRITE_FAIL;
    }
  }

  /**
   * Adds a JSON Array to the Configuration File.
   *
   * @param object JSONObject to add to file
   */
  @SuppressWarnings("Duplicates")
  public static FileUtilityStatus writeToFile(JSONArray object, String fileName) {
    // Write to the file.
    try {
      FileWriter fileWriter = new FileWriter(fileName);
      fileWriter.write(
          object.toString(4)
      );
      fileWriter.flush();
      fileWriter.close();
      return FileUtilityStatus.WRITE_SUCCESS;
    } catch (IOException ioe) {
      AridCore.getLog().error("Unable to write to file.", OperationStage.FILE_UTIL, ioe);
      return FileUtilityStatus.WRITE_FAIL;
    }
  }

  /**
   * Retrieves a specific value by key in a given file.
   *
   * @param fileName      Name of the file to retrieve key from
   * @param key           Key to retrieve
   * @param arrayLocation Array the key is located in
   * @return Value of the key
   */
  @SuppressWarnings("ConstantConditions")
  public static String getValueByKey(String fileName, String key, String arrayLocation) {
    JSONArray object = getJsonFileArray(fileName);

    if (object.equals(null)) {
      AridCore.getLog().error(key + " is null.", OperationStage.FILE_UTIL,
          new NoConfigurationFileException("Failed to grab " + key));
      return "" + ShutdownStatus.NO_CONFIG.getIdentifier();
    }

    JSONObject jsonObject = object.getJSONObject(0);
    JSONObject array = (JSONObject) jsonObject.opt(arrayLocation);

    return array.optString(key);
  }

  /**
   * Retrieves the JSONObject to read the JSON File.
   *
   * @return JSONObject
   */
  public static JSONArray getJsonFileArray(String fileName) {
    JSONObject obj;
    JSONArray array = new JSONArray();
    StringBuilder sb = new StringBuilder();

    try {
      FileReader reader = new FileReader(fileName);

      int i;
      while ((i = reader.read()) != -1) {
        sb.append((char) i);
      }

      obj = new JSONObject(sb.toString().replace("[", "").replace("]", ""));
      array.put(obj);

    } catch (FileNotFoundException fnfe) {
      AridCore.getLog().error("File not found.", OperationStage.FILE_UTIL, fnfe);
      AridCore.shutdown(ShutdownStatus.NO_CONFIG);
    } catch (Exception e) {
      AridCore.getLog().error("File could not be read.", OperationStage.FILE_UTIL, e);
      AridCore.shutdown(ShutdownStatus.NO_CONFIG);
    }

    return array;
  }

  /**
   * Retrieves the JSONObject to read the JSON File.
   *
   * @return JSONObject
   */
  public static JSONObject getJsonFileObject(String fileName) {
    JSONObject obj;
    StringBuilder sb = new StringBuilder();

    try {
      FileReader reader = new FileReader(fileName);

      int i;
      while ((i = reader.read()) != -1) {
        sb.append((char) i);
      }

      obj = new JSONObject(sb.toString().replace("[", "").replace("]", ""));

      return obj;
    } catch (FileNotFoundException fnfe) {
      AridCore.getLog().error("File not found.", OperationStage.FILE_UTIL, fnfe);
      AridCore.shutdown(ShutdownStatus.NO_CONFIG);
    } catch (Exception e) {
      AridCore.getLog().error("File could not be read.", OperationStage.FILE_UTIL, e);
      AridCore.shutdown(ShutdownStatus.NO_CONFIG);
    }

    return null;
  }

  /**
   * Checks if a given file exists.
   *
   * @param directory directory of file
   * @return true if it exists, false if not
   */
  public static boolean checkIfFileExists(String directory) {
    File temp = new File(directory);
    return temp.exists();
  }

}