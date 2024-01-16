# cloud-jda5

Cloud integration for [JDA5](https://github.com/discord-jda/JDA) slash commands.

An example bot using cloud-jda5 can be found [here](https://github.com/Incendo/cloud-discord/tree/master/examples/example-jda5).

## Installation

Cloud Annotations is available through [Maven Central](https://search.maven.org/search?q=cloud.commandframework).

<!-- prettier-ignore -->
=== "Maven"

    ```xml
    <dependencies>
        <dependency>
            <groupId>org.incendo</groupId>
            <artifactId>cloud-jda5</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
    ```

=== "Gradle (Kotlin)"

    ```kotlin
    implementation("org.incendo:cloud-jda5:1.0.0-SNAPSHOT")
    ```

=== "Gradle (Groovy)"

    ```groovy
    implementation 'org.incendo:cloud-jda5:1.0.0-SNAPSHOT'
    ```

## Usage

### Command Manager

```java
// Using the "native" JDAInteraction sender type:
JDA5CommandManager<JDAInteraction> commandManager = new JDA5CommandManager<>(
    executionCoordinator,
    JDAInteraction.InteractionMapper.identity()
);

// Using a custom sender type:
JDA5CommandManager<YourSenderType> commandManager = new JDA5CommandManager<>(
    executionCoordinator,
    interaction -> yourSenderType
);
```

where `executionCoordinator` is an
[ExecutionCoordinator](../core/index.md#execution-coordinators) instance.

You then register the command manager as a JDA event listener. Example:

```java
JDABuilder.createDefault(yourToken)
    .addEventListeners(commandManager)
    .build();
```

The event listener handles command synchronization, command execution and
autocompletion. You may manually synchronize commands using
`JDA5CommandManager.registerGuildCommands(Guild)` or
`JDA5CommandManager.registerGlobalCommands(JDA)`.

### Parsers

Mappings to all existing option types are supported:

- INTEGER: `IntegerParser`
- NUMBER: `DoubleParser`
- BOOLEAN: `BooleanParser`
- STRING: `StringParser`
- USER: `JdaParser.userParser()`
- CHANNEL: `JdaParser.channelParser()`
- ROLE: `JdaParser.roleParser()`
- MENTIONABLE: `JdaParser.mentionableParser()`
- ATTACHMENT: `JdaParser.attachmentParser()`

Subcommands and subcommand groups are mapped to Cloud literals.
Other parsers are by default be mapped to `OptionType.STRING`.

### Choices

The [suggestion providers](../core/index.md#suggestions) will be invoked for option types that support
auto completions.
You may also use constant choices by using `DiscordChoices` as the suggestion provider. Example:

```java
commandBuilder.required(
    "integer",
    integerParser(),
    DiscordChoices.integers(1, 2, 3)
)
```

Choices may be used for custom parsers that get mapped to `OptionType.STRING`.

### Reply Setting

`ReplySetting` may be used to have the slash command interactions be deferred automatically.
You apply the setting to the command builder using:

```java
// Don't defer the reply:
commandBuilder.apply(ReplySetting.doNotDefer());

// Defer with an ephemeral response:
commandBuilder.apply(ReplySetting.defer(true));

// Defer with a non-ephemeral response:
commandBuilder.apply(ReplySetting.defer(false));
```

#### Annotations:

If using annotated commands you may use the `@ReplySetting` annotation. You must first install
the builder modifier:

```java
ReplySettingBuilderModifier.install(annotationParser);
```

You may then use the annotation:

```java
@ReplySetting(defer = true, ephemeral = true)
@Command("command")
public void yourCommand() { /* ... */ }
```

### Command Scopes

You may choose where a command is registered to by using `CommandScope`. You apply the scope to the command builder using:

```java
// Available everywhere (DMs, guilds, etc):
commandBuilder.apply(CommandScope.global());

// Available in all guilds:
commandBuilder.apply(CommandScope.guilds());

// Available in specific guilds:
commandBuilder.apply(CommandScope.guilds(some, guild, ids));
```

You may implement custom filtering by overriding the command scope predicate for the command factory:

```java
commandManager.commandFactory()
        .commandScopePredicate((node, scope) -> yourLogicHere);
```

#### Annotations:

If using annotated commands you may use the `@CommandScope` annotation. You must first install the builder modifier:

```java
CommandScopeBuilderModifier.install(annotationParser);
```

You may then use the annotation:

```java
@CommandScope(guilds = { 1337 })
@Command("command")
public void yourCommand() { /* ... */ }
```

### Permissions

You may set the default permissions by using `DiscordPermission.of(Long)` as the command permission. Example:

```java
// Using JDA's Permission helper class.
commandBuilder.permission(DiscordPermission.of(Permission.ALL_PERMISSION))
```

You may use ordinary permissions by setting the permission function in `JDA5CommandManager`.

### Settings

You may modify certain behaviors using the `DiscordSetting`s. You may do this by
accessing the `Configurable<DiscordSetting>` instance using `JDA5CommandManager.discordSettings()`.
