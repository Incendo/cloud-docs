# cloud-discord4j

Cloud integration for [Discord4J](https://github.com/Discord4J/Discord4J) slash commands.

An example bot using cloud-discord4j can be found [here](https://github.com/Incendo/cloud-discord/tree/master/examples/example-discord4j).

## Installation

Cloud for Discord4J is available through [Maven Central](https://central.sonatype.com/artifact/org.incendo/cloud-discord4j).

{{ dependency_listing("discord4j") }}

## Usage

### Command Manager

```java
// Using the "native" Discord4JInteraction sender type:
Discord4JCommandManager<Discord4JInteraction> commandManager = new Discord4JCommandManager<>(
    executionCoordinator,
    Discord4JInteraction.InteractionMapper.identity()
);

// Using a custom sender type:
Discord4JCommandManager<YourSenderType> commandManager = new Discord4JCommandManager<>(
    executionCoordinator,
    interaction -> yourSenderType
);
```

where `executionCoordinator` is an
[ExecutionCoordinator](../core/index.md#execution-coordinators) instance.

You then register the command manager as a Discord4J event listener. Example:

```java
discordClient.withGateway(commandManager::installEventListener).block();
```

The event listener handles command synchronization, command execution and autocompletion.

### Parsers

Mappings to all existing option types are supported:

- INTEGER: `IntegerParser`
- NUMBER: `DoubleParser`
- BOOLEAN: `BooleanParser`
- STRING: `StringParser`
- USER: `Discord4JParser.userParser()`
- CHANNEL: `Discord4JParser.channelParser()`
- ROLE: `Discord4JParser.roleParser()`
- MENTIONABLE: `Discord4JParser.mentionableParser()`
- ATTACHMENT: `Discord4JParser.attachmentParser()`

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

### Reactive Execution Handlers

As Discord4J makes extensive use of reactor classes it is recommended that you make use of
`Discord4JCommandExecutionHandler.reactiveHandler(Function)`, which allows you to return a `Producer` from
your command handler. Example:

```java
 .handler(reactiveHandler(context -> {
    final Discord4JInteraction interaction = context.sender();
    final String message = context.get(COMPONENT_MESSAGE);
    return interaction.commandEvent()
            .map(event -> (Mono<?>) event.reply(message).withEphemeral(true))
            .orElseGet(Mono::empty);
})
```

If you use annotated commands then you may return `CompletableFuture<Void>`. You'll have to convert
the reactor classes to futures. Example:

```java
@Command("command")
public CompletableFuture<Void> command(
  final Discord4JInteraction interaction
) {
        return interaction.commandEvent()
                .map(event -> (Mono<?>) event.reply("Hello world"))
                .orElseGet(Mono::empty)
                .then()
                .toFuture();
}
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
// Using Discord4J's PermissionSet helper class.
commandBuilder.permission(DiscordPermission.of(PermissionSet.all().getRawValue()))
```

You may use ordinary permissions by setting the permission function in `Discord4JCommandManager`.

### Settings

You may modify certain behaviors using the `DiscordSetting`s. You may do this by
accessing the `Configurable<DiscordSetting>` instance using `Discord4JCommandManager.discordSettings()`.
