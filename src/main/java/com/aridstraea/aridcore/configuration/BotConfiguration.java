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

/**
 * BotConfiguration class of the AridCore project.
 * This class is intended for use with custom Discord Bots
 * using this library. Configuration files created using
 * this class can be loaded into AridCore instead of the
 * default configuration file.
 *
 * @author aridstraea
 * @since 0.2.0-S
 */
@SuppressWarnings("unused")
public abstract class BotConfiguration extends Configuration {

  public abstract String getPrefix();

  public abstract void setPrefix(String newPrefix);

  public abstract String getToken();

  public abstract boolean getDebug();

  public abstract void setDebug(boolean newDebug);

  public abstract int getShards();

  public abstract String getOwnerId();

  public abstract void setOwnerId(String newOwnerId);
}
