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

package com.aridstraea.aridcore.core.listeners;

import com.aridstraea.aridcore.utilities.MessageUtilities;
import com.aridstraea.aridcore.core.AridCore;
import java.util.List;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * TagListener class of the AridCore project.
 *
 * @author aristraea
 * @since 0.1.0-S
 */
public class TagListener extends ListenerAdapter {

  final User botMention = AridCore.getApi().getSelfUser();
  String messageContent;

  @Override
  @SuppressWarnings("ConstantConditions")
  public void onMessageReceived(MessageReceivedEvent event) {
    Message message = event.getMessage();
    User author = event.getAuthor();
    List<User> mentions = message.getMentions().getUsers();

    // Check if 1) message is null, 2) author is null, 3) message doesn't mention bot
    if (message.equals(null) || author.equals(null) || !mentions.contains(botMention)) {
      return;
    }

    messageContent = message.getContentRaw();

    // Check for prefix
    if (messageContent.contains("prefix")) {
      event.getChannel().sendMessage(
          author.getAsMention() + ", the prefix is " + AridCore.getConfig().getPrefix()
      ).queue();
    } else if (messageContent.contains("info")) {
      event.getChannel().sendMessageEmbeds(MessageUtilities.embedCoreInfo().build()).queue();
    }
  }
}
