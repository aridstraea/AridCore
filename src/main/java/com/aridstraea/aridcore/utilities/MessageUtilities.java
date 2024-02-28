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

import com.aridstraea.aridcore.core.AridCore;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

/**
 * MessageUtilities class of the AridCore project.
 *
 * @author aristraea
 * @since 0.1.0-S
 */
public class MessageUtilities {

  /**
   * Adds default values to a given embed.
   *
   * @param embed EmbedBuilder to add defaults to
   */
  public static void addEmbedDefaults(EmbedBuilder embed) {
    // Add defaults.
    embed.setFooter("AridCore by aristraea#1840", null);
    embed.setAuthor("Try `" + AridCore.getConfig().getPrefix() + "help [command]` for more.");
    setTimestamp(embed);
  }

  /**
   * Sets the timestamp of the embed to the current time.
   *
   * @param embed EmbedBuilder to set the timestamp of.
   */
  public static void setTimestamp(EmbedBuilder embed) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
    String timestamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());
    TemporalAccessor temporalAccessor = formatter.parse(timestamp);

    embed.setTimestamp(temporalAccessor);
  }

  /**
   * Sends a message to the user if the message is not in a private message.
   *
   * @param mre MessageReceivedEvent to use.
   */
  public static void sendIfNotPrivate(MessageReceivedEvent mre) {
    // Grab user's tag.
    // TODO: When Discord finishes rolling out the update, use getName() instead of getAsTag() at all.
    String tag = mre.getAuthor().getAsTag();
    if (tag.endsWith("0000")) tag = mre.getAuthor().getName();

    // Bypass sending message if it is already in a private message.
    if (!mre.isFromType(ChannelType.PRIVATE)) {
      // Send help message
      mre.getChannel().asTextChannel().sendMessage(new MessageCreateBuilder()
          .addContent("Hey, ")
          .addContent(tag)
          .addContent(": Help information was sent as a private message.")
          .build()).queue();
    }
  }

  /**
   * Creates a basic embed with the Core information.
   *
   * @return EmbedBuilder with the Core information.
   */
  public static EmbedBuilder embedCoreInfo() {
    EmbedBuilder embed = new EmbedBuilder();

    addEmbedDefaults(embed);

    // Add information fields
    embed.setThumbnail(
        "https://cdn.discordapp.com/attachments/693741051327807549/711698762682073108/image0.png");
    embed.addField("Version", InternalLogger.VERSION + "." + InternalLogger.BUILD_NUMBER, true);
    embed.addField("Current Prefix", AridCore.getConfig().getPrefix(), true);
    embed.addField("AridCore Author", "aristraea#1840 ->", true);

    return embed;
  }

  /**
   * Sends a message telling the user their search doesn't exist.
   *
   * @param channel channel to send message
   * @param args    arguments to build message
   */
  public static void doesNotExist(PrivateChannel channel, String args, String doesntExist) {
    // If it reaches this point, the command searched for does not exist.
    channel.sendMessage(new MessageCreateBuilder()
        .addContent("The provided ")
        .addContent(doesntExist)
        .addContent(" '**")
        .addContent(args)
        .addContent("**' does not exist. Use `")
        .addContent(AridCore.getConfig().getPrefix())
        .addContent(doesntExist)
        .addContent("` to list all ")
        .addContent(doesntExist).addContent(".")
        .build()).queue();
  }
}
