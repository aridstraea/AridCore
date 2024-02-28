[license]: https://img.shields.io/badge/License-Apache%202.0-lightgrey.svg
[license-link]: https://github.com/aristraea/AridCore/blob/master/LICENSE
[issues]: https://img.shields.io/github/issues/ajstri/AridCore.svg 
[issues-link]: https://github.com/aristraea/AridCore/issues
[discord-widget]: https://discord.com/api/guilds/562612266856349696/widget.png
[discord-invite]: https://discord.gg/HH9EmGD

[ ![license][] ][license-link]
[ ![issues][] ][issues-link]

# AridCore - Framework for Discord Bots in Java
#### A simple-to-use Discord Bot framework written in Java using [JDA](https://github.com/DV8FromTheWorld/JDA).

The current build is using JDA Version `5.0.0-beta.20`. For more details, see AridCore Dependencies below.
<br><br>

#### Examples using AridCore
* [EchoedDungeons](https://github.com/aridstraea/EchoedDungeons/) by [aridstraea](https://github.com/aristraea)
* [E.C.H.O's Utility Bot](https://github.com/aridstraea/ECHO-Community-Discord-Bot) by [aridstraea](https://github.com/aristraea)


## Features
### **Command framework**
Commands can be easily created and defined using [Command](https://github.com/aristraea/AridCore/blob/master/src/main/java/core/commands/Command.java).
The [Help Command](https://github.com/aristraea/AridCore/blob/master/src/main/java/core/commands/HelpCommand.java)
automatically registers developer-defined commands for further use in its methods.

To register a set of Commands, assuming a new instance of [AridCore](https://github.com/aristraea/AridCore/blob/master/src/main/java/core/EchoedCore.java) called `myBot`:


**Register commands with both traditional & slash command counterparts:**
```java_holder_method_tree
    List<Command> commandList = new ArrayList<>(){{
            new YourCommand1();
            new YourCommand2();
    }}

    myBot.addGuildSlashCommands(commandList); // Registers Guild-Only slash commands
    // OR
    myBot.addGlobalSlashCommands(commandList); // Registers Global slash commands
```

**Register commands with only traditional commands:**
```java_holder_method_tree
    List<Command> commandList = new ArrayList<>(){{
            new YourCommand1();
            new YourCommand2();
    }}

    myBot.addCommands(commandList); // Registers traditional-only commands
```


Commands created using the EchoedCore framework support both traditional text-based commands and modern Slash Commands.
It is recommended to use Slash Commands, as they are easier to use and more visually appealing.
<br>
However, it is possible to register a command as only a traditional command.
In this case, please note the following:
 * You currently cannot extend [Command](https://github.com/aristraea/EchoedCore/blob/master/src/main/java/core/commands/Command.java)
without assuming a Slash Command counterpart.
 * Logging output is likely to throw errors if `getSlashCommandData()` method is not set.
It is technically possible to leave this empty or null, but it is not recommended as it will cause errors
every time a command is called.
 * You can leave `onSlashCommand()` empty if you do not register the command as a Slash Command.
It will be ignored.

<br>

**Commands registered as Slash Commands will be assumed to have a traditional command counterpart.**
This is subject to change.


### **Creating a custom command**


Creating a custom command is easy. Simply extend the [Command](https://github.com/aristraea/EchoedCore/blob/master/src/main/java/core/commands/Command.java).

Shown below is a simple example of a command that will respond with "Hello World!" when called.

```java_holder_method_tree
public class YourCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent event, String[] args) {
        mre.getChannel().sendMessage("Hello World!").queue();
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        sce.reply("Hello World!").queue();
    }

    @Override
    public CommandData getSlashCommandData() {
        return Commands.slash("hello", "Responds with \"Hello World!\"");
        // Syntax: Commands.slash(String name, String description)
    }
    
    @Override
    public List<String> getAliases() {
        return Collections.singletonList("info");
        // How to call the command via traditional text-based commands.
        // prefix + alias; e.g. !hello if the prefix is set to !
    }

    @Override
    public Module getModule() {
        return Module.GENERIC;
        // See Module section below.
    }

    @Override
    public String getDescription() {
        return "Hello World!";
        // Used in the Help Command.
    }

    @Override
    public String getName() {
        return "Hello Command";
        // Used in the Help Command.
    }

    @Override
    public List<String> getUsage() {
        return Collections.singletonList("`" + Main.getBotConfig().getPrefix() + "hello`");
        // Prefix is defined in the config.json file. See Configuration section below.
    }
    
    @Override
    public boolean getDefaultPermission() {
        return true;
        // Default usage is true. If you want to disable a command by default, set this to false.
        // This is for traditional commands only. Slash commands are always enabled by the Permission set in getSlashCommandData().
    }
}
```

### **Module system**

### **Configuration System**

EchoedCore uses a configuration system to store bot-specific information. 
This includes:
 * Bot Token
 * Owner ID
 * Prefix
 * Bot Status
 * Shards
 * Debug Mode

It is possible to extend the Configuration class to add more information to the configuration file.

## Usage

Creating a Bot is easy. In your main method, assuming a new instance of [EchoedCore](https://github.com/aristraea/EchoedCore/blob/master/src/main/java/core/EchoedCore.java) called `yourBot`:
```java_holder_method_tree
    // Starts the Bot.
    yourBot.startup();
```
Failing on the very first run is normal. On this run, the [Configuration](https://github.com/aristraea/EchoedCore/blob/master/src/main/java/configuration/Configuration.java)
generates, but is not usable in its default state. After the first run, edit the generated `config.json` file
to include your bot's token and the new Owner ID. Additionally, you can change the prefix and the bot's status.

## Including this dependency in your project

Be sure to replace **VERSION** below with the latest version as shown [here](https://github.com/aristraea/EchoedCore/packages/).

**Maven**
```xml
<dependency>
    <groupId>com.aridstraea</groupId>
    <artifactId>echoedcore</artifactId>
    <version>VERSION</version>
</dependency>
```

### Notes about the SFL4J Logging Framework
Like JDA, EchoedCore uses SFL4J to log its messages. As such, you should add an implementation to your build path in addition to EchoedCore & JDA. If none is found, you will see the following messages in your console on startup:

```
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See https://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
```
JDA does include a fallback logger if SLF4J does not have an implementation. However, it is still recommended to use one.

Please review [JDA's guide for logback-classic](https://github.com/DV8FromTheWorld/JDA/wiki/Logging-Setup), the implementation that EchoedCore uses.

## EchoedCore Dependencies

This project is built using Java 11.

Dependencies are managed by Maven. The following dependencies are used by EchoedCore:

 * [JDA](https://github.com/DV8FromTheWorld/JDA)
   * A Java Discord API wrapper.
   * Author: [DV8FromTheWorld](https://github.com/DV8FromTheWorld)
   * Version: 5.0.0-beta.20
 * [SLF4J](https://www.slf4j.org/)
   * Simple Logging Facade for Java.
   * Author: [QOS.ch](https://www.qos.ch/)
   * Version: 2.0.5
 * [JSON](https://github.com/stleary/JSON-java)
   * JSON in Java.
   * Author: [Stleary](https://github.com/stleary)
   * Version: 20231013