# cloud-kord

Cloud integration for [Kord](https://github.com/kordlib/kord) slash commands.

An example bot using cloud-kord can be found [here](https://github.com/Incendo/cloud-discord/tree/master/examples/example-kord).

## Installation

Cloud for Kord is available through [Maven Central](https://central.sonatype.com/artifact/org.incendo/cloud-kord).

{{ dependency_listing("kord") }}

## Usage

### Command Manager

```kotlin
// Using the "native" KordInteraction sender type:
val commandManager: KordCommandManager<KordInteraction> = KordCommandManager(executionCoordinator) {
  it
}

// Using a custom sender type:
val commandManager: KordCommandManager<KordInteraction> = KordCommandManager(executionCoordinator) {
  it -> senderType
}
```

where `executionCoordinator` is an
[ExecutionCoordinator](../core/index.md#execution-coordinators) instance.

You then register the command manager as a Kord event listener. Example:

```kotlin
commandManager.installListener(kord)
```

The event listener handles command synchronization, command execution and
autocompletion. You may manually synchronize commands using
`commandManager.commandFactory.createGuildCommands(Guild)` or
`commandManager.commandFactory.createGlobalCommands(Kord)`.

`cloud-kord` depends on [cloud-kotlin-extensions](../kotlin/extensions.md)
and [cloud-kotlin-coroutines](../kotlin/coroutines.md). Because Kord is coroutine-based it is recommended
that you make use of `MutableCommandBuilder` as well as suspending command execution handlers. If you use annotated
commands you will also want to depend on [cloud-kotlin-coroutines-annotations](../kotlin/annotations.md).

```kotlin title="Example command registration"
commandManager.buildAndRegister("command") {
    required("user", KordParser.userParser())

    suspendingHandler { context ->
        // Always available through an extension function.
        val interaction: KordInteraction = context.interaction
        val user: User = context.get("user")

        interaction.respondEphemeral {
          content = "Hello ${user.mention}"
        }
    }
}
```

### Parsers

Mappings to all existing option types are supported:

- INTEGER: `IntegerParser`
- NUMBER: `DoubleParser`
- BOOLEAN: `BooleanParser`
- STRING: `StringParser`
- USER: `KordParser.userParser()`
- CHANNEL: `KordParser.channelParser()`
- ROLE: `KordParser.roleParser()`
- MENTIONABLE: `KordParser.mentionableParser()`
- ATTACHMENT: `KordParser.attachmentParser()`

Subcommands and subcommand groups are mapped to Cloud literals.
Other parsers are by default be mapped to the string option type.

### Choices

The [suggestion providers](../core/index.md#suggestions) will be invoked for option types that support
auto completions.
You may also use constant choices by using `DiscordChoices` as the suggestion provider. Example:

```kotlin
commandBuilder.required(
    "integer",
    integerParser(),
    DiscordChoices.integers(1, 2, 3)
)
```

Choices may be used for custom parsers that get mapped to string choices.

### Command Scopes

You may choose where a command is registered to by using `CommandScope`. You apply the scope to the command builder using:

```java
// Available everywhere (DMs, guilds, etc):
mutableCommandBuilder.scope(CommandScope.global())

// Available in all guilds:
mutableCommandBuilder.scope(CommandScope.guilds())

// Available in specific guilds:
mutableCommandBuilder.scope(CommandScope.guilds(some, guild, ids))
```

You may implement custom filtering by overriding the command scope predicate for the command factory:

```java
commandManager.commandFactory().commandScopePredicate = { node, scope -> yourLogicHere }
```

#### Annotations:

If using annotated commands you may use the `@CommandScope` annotation. You must first install the builder modifier:

```kotlin
CommandScopeBuilderModifier.install(annotationParser)
```

You may then use the annotation:

```kotlin
@CommandScope(guilds = [1337])
@Command("command")
public suspend fun yourCommand() { /* ... */ }
```

### Permissions

You may set the default permissions of the command using `Command.Builder.permissions(Permissions)` or
`MutableCommandBuilder.permissions(Permissions)` using an instance of Kord's `Permissions` class:

```kotlin
commandManager.buildAndRegister("command") {
  // Using a permissions builder
  permissions(Permissions(Permission.Administrator))

  // Using the vararg overload
  permissions(Permission.Administrator)
}
```

You may use ordinary permissions by setting the permission function in `KordCommandManager`.
This will not hide the commands from the users, so you must do that manually through the server settings.

### Settings

You may modify certain behaviors using the `KordSetting`s. You may do this by
accessing the `Configurable<KordSetting>` instance using `KordCommandManager.kordSettings()`.
