# cloud-core

`cloud-core` contains the main cloud API.
Generally you'll want to depend on a platform module which implements Cloud for your specific platform, as
`cloud-core` does not have any platform-specific code.

## Installation

Cloud is available through [Maven Central](https://search.maven.org/search?q=cloud.commandframework).

<!-- prettier-ignore -->
=== "Maven"

    ```xml
    <dependency>
      <groupId>cloud.commandframework</groupId>
      <artifactId>cloud-core</artifactId>
      <version>dCLOUD_BASE_VERSIONd</version>
    </dependency>
    ```

=== "Gradle (Kotlin)"

    ```kotlin
    implementation("cloud.commandframework:cloud-core:dCLOUD_BASE_VERSIONd")
    ```

=== "Gradle (Groovy)"

    ```groovy
    implementation 'cloud.commandframework:cloud-core:dCLOUD_BASE_VERSIONd'
    ```

## Command

A command is a chain of components. Each unique chain makes up a unique command.
A command can have some properties associated with it, such as a permission, description, etc.

<!-- prettier-ignore -->
!!! example
    All of these are examples of unique commands:
    ```
    /foo bar one
    /foo bar two <arg1> -- Command with a required variable
    /bar [arg] -- Command with an optional variable
    ```

### Components

There are four different types of components:

1. Literals: A string literal with optional aliases.
2. Required variables: A variable component which gets parsed by a parser, that must be present.
3. Optional variables: A variable component which gets parsed by a parser, that must not necessarily be present.
   An optional component may have a default value.
4. Flags: Named components that are always optional. They may have an associated variable component.
   Examples: `--flag value`, `-abc`.

Cloud requires that the command chains are unambiguous.
This means that you may not have a required component following an optional component.
You may also not have two conflicting variable components on the same level, as it would not be clear
which of them gets to parse the input.
You may have _one_ variable component alongside literals, where the literals always get priority.

### Variable Components

A variable component is associated with an output type.
It takes the incoming input and attempts to parse it into the output type using a parser.
See the section on [parsers](#parsers) for more information.

#### Suggestions

Cloud can generate suggestions for values which, depending on the platform, can be displayed
to the player to help them complete the command input.
Most standard parsers generate suggestions, and by default Cloud will ask the parser for suggestions.
You may provide your own suggestions using a suggestion provider.

## Command Manager

### Execution coordinators

The execution coordinator is responsible for coordinating command parsing and execution.
Cloud ships with two different command execution coordinators:

- **SimpleCommandExecutionCoordinator**: Performs all actions on the calling thread.
- **AsynchronousCommandExecutionCoordinator**: Uses an executor to dispatch the parsing and execution tasks.
  You can change the default executor, and also force command parsing to take place on the calling thread.

You may also create your own execution coordinator by implementing `CommandExecutionCoordinator`.

### Building a command

#### Descriptions

#### Permissions

#### Sender types

#### Components

#### Literals

#### Variable

##### Required

##### Optional

### Customizing the command manager

#### Pre- & Postprocessing

When a command is entered by a command sender, it goes through the following stages:

1. It is converted into a `CommandInput` instance.
2. A command context is created for the input.
3. The context is passed to the preprocessors, which may alter the command input or write to the context.
   - If a command processor causes an interrupt using `ConsumerService.interrupt()` the context will be filtered out
     and the command will not be parsed.
4. The input is parsed into a command chain, and the parsed values are written to the context.
   - If the input cannot be parsed into a command that the sender is allowed to execute, the sender is
     notified and the parsing is canceled.
5. The command postprocessors get to act on the command, and may alter the command context.
   They may also postpone command execution, which can be used to require confirmations, etc.
   - If a postprocessor causes an interrupt using `ConsumerService.interrupt()` the command will not be executed.
6. The command is executed using the command executor.

The pre- and post-processors can be registered to the command manager using `CommandManager#registerCommandPreProcessor`
and `CommandManager#registerCommandPostProcessor`.

#### Exception handling

Cloud v2 introduced a new exception handling system.
You may register exception handlers through the exception controller, which can be retrieved using
`CommandManager#exceptionController`.

Cloud will attempt to match a thrown exception to any of the registered exception handlers,
giving preference to the most specific exception type and to the last registered handler.
This means that it is possible to register a fallback handler for `Throwable`/`Exception` and more
precise handlers for specific exception types.
You may register multiple exception handlers for the same exception type.
Cloud will iterate over the exception handlers (giving preference to the last registered handler) until a handler
consumes the exception, which allows for the registration of default handlers.

Some exception types, such as `ArgumentParseException` and `CommandExecutionException` wrap the actual exceptions
thrown by the parser or command handler.
By default, Cloud will forward the wrapper exceptions.
If you instead want to be able to register exception handlers for the causes, then you may use the
`ExceptionHandler.unwrappingHandler()` methods to unwrap these exceptions.
You can choose to unwrap all instances of a given exception type, all instances with a given cause type or
all instances that pass a given predicate.

Command exceptions are thrown whenever a command cannot be parsed or executed normally.
This can be for several reasons, such as:

- The command sender does not have the required permission (NoPermissionException)
- The command sender is of the wrong type (InvalidCommandSenderException)
- The requested command does not exist (NoSuchCommandException)
- The provided command input is invalid (InvalidSyntaxException)
- The parser cannot parse the input provided (ArgumentParseException)

##### Parser Errors

`ArgumentParseException` makes use of Cloud's caption system.
(Nearly) all argument parsers in Cloud will throw `ParserException` on invalid input, in which case you're able
to override the exception message by configuring the manager's [CaptionRegistry](TODO).
By default, Cloud uses a [FactoryDelegatingCaptionRegistry](TODO), which allows you to override the exception handling
per caption key.
All standard caption keys can be found in [StandardCaptionKey](TODO).
Some platform adapters have their own caption key classes as well.

The JavaDoc for the caption keys list their replacement variables.
The message registered for the caption will have those variables replaced with variables specific to the parser.
`{input}` is accepted by all parser captions, and will be replaced with the input that caused the exception
to be thrown.

<!-- prettier-ignore -->
!!! example annotate "Example caption registry usage"
    ```java
    final CaptionRegistry<SenderType> registry = manager.captionRegistry();
    if (registry instanceof FactoryDelegatingCaptionRegistry) /* (1)! */ {
        final FactoryDelegatingCaptionRegistry<SenderType> factoryRegistry =
            (FactoryDelegatingCaptionRegistry<SenderType>) manager.captionRegistry();
        factoryRegistry.registerMessageFactroy(
            StandardCaptionKeys.ARGUMENT_PARSE_FAILURE_BOOLEAN,
            (context, key) -> "'{input}' ain't a boolean >:("
        );
    }
    ```

1. Some platforms may opt to use a different caption registry implementation that does not delegate to factories.

## Parsers

### Parser Registry

### Standard Parsers

Cloud ships with parsers for all Java primitives as well as strings, enums, UUIDs and durations.

#### String

Cloud has four different string "modes":

- **Single**: A single space-delimited string.
- **Quoted**: Either a single space-delimited string, or multiple space-delimited
  strings surrounded by a pair of single or double quotes.
- **Greedy**: All remaining input.
- **Greedy flag yielding**: All remaining input until a [flag](#flags) is encountered.

The string parsers do not produce suggestions by default.

The string parsers can be created using the static factory methods found in [StringParser](TODO).

#### String Array

Cloud can parse string arrays, in which case it captures all remaining input.
A string array parser may also be flag yielding, in which case it will only capture input until
it encounters a [flag](#flags).

The string array parser does not produce suggestions by default.

The string array parser can be created using the static factory methods found in [StringArrayParser](TODO).

#### Character

This parses a single space-delimited character.
The character parser does not produce suggestions by default.

The character parser can be created using the static factory methods found in [CharacterParser](TODO).

#### Numbers

Cloud has parsers for bytes, shorts, integers, longs, doubles and floats.
The numerical values may have min- and max-values set.

All numerical parsers except for `FloatParser` and `DoubleParser` will produce suggestions.

The numerical parsers can be created using the static factory methods found in:

- [ByteParser](TODO)
- [ShortParser](TODO)
- [IntegerParser](TODO)
- [LongParser](TODO)
- [DoubleParser](TODO)
- [FloatParser](TODO)

#### Boolean

The boolean parser can either be strict or liberal.
A strict boolean parser only accepts (independent of the case) `true` and `false`.
A liberal boolean parser also accepts `yes`, `no`, `on` and `off`.

The boolean parser can be created using the static factory methods found in [BooleanParser](TODO).

#### Enum

The enum parser matches the input (independent of the case) to the names of an enum. The parser will return
the enum values as suggestions.

The enum parser can be created using the static factory methods found in [EnumParser](TODO).

#### Duration

Durations can be parsed.

#### UUID

The UUID parser parses dash-separated UUIDs.

The UUID parser can be created using the static factory methods found in [UUIDParser](TODO).

### Flags

Flags are named optional values that can either have an associated value (value flags),
or have their value be determined by whether the flag is present (presence flags).

Flags are always optional.
You cannot have required flags.
If you want required values, then they should be part of a deterministic command chain.
Flags are parsed at the tail of a command chain.

Flags can have aliases alongside their full names.
When referring to the full name of a flag, you use `--name` whereas an alias uses the syntax `-a`.
You can chain the aliases of multiple presence flags together, such that `-a -b -c` is equivalent to `-abc`.

The flag values are contained in `FlagContext` which can be retrieved using `CommandContext.flags()`.

<!-- prettier-ignore -->
!!! example "Example of a command with a presence flag"
    ```java
    manager.commandBuilder("command")
        .flag(manager.flagBuilder("flag").withAliases("f"))
        .handler(context -> {
            boolean present = context.flags().isPresent("flag");
        ));
    ```

### Aggregate Parsers

Aggregate parsers are a new concept as of Cloud v2, and they supersede the old compound argument concept.
An aggregate parser is a combination of multiple parsers that maps the intermediate results into an output
type using a mapper.

You may either implement the `AggregateCommandParser` interface, or using construct the parser by using a builder
that you create by calling `AggregateCommandParser.builder()`.

<!-- prettier-ignore -->
!!! example "Example Aggregate Parser"
    ```java
    final AggregateCommandParser<CommandSender, Location> locationParser = AggregateCommandParser.<CommandSender>builder()
        .withComponent("world", worldParser())
        .withComponent("x", integerParser())
        .withComponent("y", integerParser())
        .withComponent("z", integerParser())
        .withMapper(Location.class, (commandContext, aggregateCommandContext) -> {
            final World world = aggregateCommandContext.get("world");
            final int x = aggregateCommandContext.get("x");
            final int y = aggregateCommandContext.get("y");
            final int z = aggregateCommandContext.get("z");
            return CompletableFuture.completedFuture(new Location(world, x, y, z));
    }).build();
    ```

### Custom Parsers

Cloud allows you to create your own parsers.
A parser accepts a command context and a command input,
and produces a result (or a future that completes with a result).

The context allows the parser to accept parsed results from other command components, which can be useful when
the result of the parser depends on other parsed components.
The command input is a structure that allows you to consume the input supplied by the command sender by peeking &
then reading primitive values and strings.

A parser can fail when the input does not match the expectations.
The command manager will turn the failure into a command syntax exception which can then be displayed to the
sender, informing them about what went wrong.

The recommended way of parsing an argument is to:

1. Peek the command input.
2. Attempt to parse the object.
   - If the object cannot be parsed, a failure is returned.
3. Pop from the command input.
4. Return the parsed value.
5. <!-- prettier-ignore -->
   !!! warning
    If the read values are not popped from the command input the command engine will assume that the syntax is wrong
    and an error message is sent to the command sender.

The parser has two different choices when it comes to which method to implement.
If the parser implements `ArgumentParser` then the signature looks like

```java
public ArgumentParseResult<OutputType> parse(
        CommandContext<SenderType> context,
        CommandInput input) { ... }
```

where the `ArgumentParseResult` can either be a `ArgumentParseResult.success(OutputType)` or
`ArgumentParseResult.failure(Exception)`.

The parser may also implement `ArgumentParser.FutureParser` in which case the signature looks like

```java
public CompletableFuture<OutputType> parseFuture(
        CommandContext<SenderType> context,
        CommandInput input) { ... }
```

in which case, a successful result is returned as a completed future, and a failure is instead returned as an
exceptionally completed future.
Returning a future is useful when the parsing needs to take place on a specific thread.

<!-- prettier-ignore -->
!!! example annotate "Example Parser"
    ```java
    public class UUIDParser<C /* (1)! */> implements ArgumentParser<C, UUID> {

        @Override
        public ArgumentParseResult<UUID> parse(
                CommandContext<C> context,
                CommandInput input
        ) {
            final String input = input.peekString(); // Does not remove the string from the input!
            try {
                final UUID uuid = UUID.fromString(input);
                input.readString(); // Removes the string from the input.
                return ArgumentParseResult.success(uuid);
            } catch(final IllegalArgumentException e) {
                return ArgumentParseResult.failure(new UUIDParseException(input, commandContext));
            }
        }
    }
    ```

1. The command sender type.

#### Exceptions

It is recommended to make use of `ParserException` when returning a failed result.
This allows for integration with the caption system, see [exception handling](#exception-handling) for more information.

#### Suggestions

The parser may implement the `suggestions` method to produce suggestions.
These suggestions will be used to provide suggestions for the component using the parser,
unless the component is created using a custom suggestion provider.

## Extra

### Confirmations

### Help generation
