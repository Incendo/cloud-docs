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
      <version>2.0.0-SNAPSHOT</version>
    </dependency>
    ```

=== "Gradle (Kotlin)"

    ```kotlin
    implementation("cloud.commandframework:cloud-core:2.0.0-SNAPSHOT")
    ```

=== "Gradle (Groovy)"

    ```groovy
    implementation 'cloud.commandframework:cloud-core:2.0.0-SNAPSHOT'
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

Commands are created using a command builder.
You may either create a new builder by calling `Command.newBuilder` or through the command manager using
`CommandManager.commandBuilder`.
It is recommended to use the command manager to create a new command builder, as this ties the command builder
to the [parser registry](#parser-registry).

The command builders are immutable, and each method returns a new command builder instance.
This allows you to store intermediate steps and reuse them to build multiple distinct commands.

You must register your command to the command manager for it to be recognized.
You do this by calling `CommandManager.command(Command)` or `CommandManager.command(Command.Builder)`.

#### Descriptions

Both commands (chains of components) and the individual components can have descriptions.
These descriptions show up in the [help system](#help-generation) as well as in the platform-native help
systems for platforms that support that.

##### Component descriptions

Component descriptions can be specified both through the component builder, or through the command builder methods.

##### Command descriptions

Command descriptions can be added through the command builder by calling
`Command.Builder.commandDescription(CommandDescription)`.
The `CommandDescription` instance contains two instances of `Description`, one short version
and an optional verbose version.

<!-- prettier-ignore -->
!!! note
    It is important to differentiate between the description for the root command literal,
    and the command descriptions. The root literal may be used by multiple command chains, and
    is therefore not bound to a specific command. The command description describes a unique command chain.

#### Permissions

A command may have a permission attached to it.
This determines who is and isn't allowed to execute the command.
Depending on the platform, it might also determine who is allowed to _see_ the command.

The permission is ultimately evaluated by the platform integration.
Though, cloud has support for some more complex permission types, such as:

- `Permission.anyOf(Permission...)`: Takes in multiple permissions and evaluates to `true` if any of the permissions evaluate to `true`.
- `Permission.allOf(Permission...)`: Takes in multiple permissions and evaluates to `true` if all the permissions evaluate to `true`.
- `PredicatePermission.of(Predicate)`: Evaluates to `true` if the predicate evaluates to `true`.

#### Sender types

Most classes in Cloud take in a generic parameter type `<C>`, which is used for the "command sender type."
The command sender is the entity executing the command, and this represents different things depending
on the platform.

You may use a sender type that is not native to the platform, and the platform command managers take in a function
that maps between your custom type and the native command sender type.

When you create a command you may override the sender type for that specific command, as long as the new
sender type has `<C>` as its supertype.
This is done by using the `Command.Builder.senderType(Class)` method.
Cloud will make sure that the sender is of the right type when executing the command, and will fail exceptionally
if it isn't.

<!-- prettier-ignore -->
!!! example annotate "Example sender type usage"
    Assume that `SubSender` extends `Sender`.

    ```java
    Command.Builder<Sender> builder = manager.commandBuilder("command");
    Command.Builder<SubSender> subBuilder = builder.senderType(SubSender.class);
    ```

#### Command meta

Command meta-data is used to attach key-value pairs to the commands, which may then be used by different components
throughout the command execution chain.
Examples of systems that make use of command meta-data are [confirmations](#confirmations) and the Bukkit help menu.

The meta-data can be configured in the command builder:

```java
final CloudKey<String> metaKey = CloudKey.of("your-key", String.class);
commandBuilder.meta(metaKey, "your value");
```

or when creating the command builder:

```java
final CloudKey<String> metaKey = CloudKey.of("your-key", String.class);
commandManager.commandBuilder("command", CommandMeta.builder().with(metaKey, "your value").build());
```

#### Components

#### Literals

Command literals are fixed strings, and represent what you might think of as a "subcommand."
They may have secondary aliases, depending on the platform you're targeting.
Literals may be placed after required variable components, but never after optional variable components.

The literals are created by using the various different `Command.Builder.literal` methods, for example:

```java title="Example of literals"
builder
  .literal("foo")
  .literal(
          "bar",
          Description.of("A literal with a description and an alias"),
          "b"
  );
```

Literals are always required.

#### Variable

Variable components are parsed using parsers.
You can create a variable component either by using a `CommandComponent.Builder` that you create using
`CommandComponent.builder`, or by using one of the many different `Command.Builder` overloads.

The component wraps a [parser](#parsers), but in many cases you will want to work with a `ParserDescriptor` instead.
A `ParserDescriptor` is a structure containing an `ArgumentParser` as well as a `TypeToken` that describes the object
produced by the parser.
If you do not provide a parser descriptor, then you will have to manually specify the value type.

All variable components have a name.
When you want to extract the parsed values in a [command handler](#handler) you do so using the component name.
You may use a `CloudKey<T>` instead of the name, which then allows you to retrieve the parsed values
in a type-safe manner.

##### Required

You can create a required variable component either by using `CommandComponent.Builder.required()` or any
of the many different overloaded `required` factory methods in `Command.Builder`.

##### Optional

You can create a required variable component either by using `CommandComponent.Builder.optional()` or any
of the many different overloaded `optional` factory methods in `Command.Builder`.

When creating an optional variable component you may supply a default value. The default value will be used in the case
that the user has not supplied any input for the component. There are three different types of default values:

- **DefaultValue.constant(Value)**: A constant default value.
- **DefaultValue.dynamic(Function)**: A dynamic value that is evaluated when the command is parsed.
- **DynamicValue.parsed(String)**: A string that is parsed by the component parser when the command is parsed.

##### Component pre-processing

You may attach pre-processors to your command components, either when building the component or after it has been built.
The pre-processor gets to filter out the input before it reaches the component parser.
This allows you to easily add input validation to existing parsers.

Cloud has a built-in processor that validates the input using a regular expression.
You can find it here:
[RegexPreprocessor](https://github.com/Incendo/cloud/blob/2.0.0-dev/cloud-core/src/main/java/cloud/commandframework/arguments/preprocessor/RegexPreprocessor.java)

#### Command context

The command context is used to store values throughout the parsing process, such as parsed component values,
values from preprocessors, parsed flags, etc.

You can fetch values from the command context using both strings and `CloudKey`s. It is recommended to use
keys to access values from the context as they are type-safe.

```java title="Example context usage"
// Access a parsed value.
final CloudKey<String> nameKey = CloudKey.of("name", String.class);
final String parsedName = commandContext.getOrDefault(nameKey, "Default Name");

// Check for the presence of a flag.
final boolean overrideFlag = commandContext.flags().hasFlag("override");

// Get the sender.
final CommandSender sender = commandContext.sender();

// Inject a value from the injection services.
final List<Cat> cats = commandContext.inject(new TypeToken<List<Cat>>() {});
```

#### Handler

The command handler is an instance of `CommandExecutionHandler` and is invoked when a command has been parsed
successfully.
Depending on the command execution coordinator the handler might be invoked asynchronously.
The handler is passed an instance of the [command context](#command-context).

```java
builder.handler(ctx -> {
  // your command handling...
});
```

You may implement `CommandExecutionHandler.FutureCommandExecutionHandler` to have the handler be a future-returning
function. Cloud will wait for the future to complete and will handle any completion exceptions gracefully.

You may delegate to other handlers using `CommandExecutionHandler.delegatingHandler`.
The command builder also has some utility functions for creating handlers that delegate to the existing handler, like
`Command.Builder.prependHandler` and `Command.Builder.appendHandler`.

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

##### Captions

`ParserException` makes use of Cloud's caption system.
(Nearly) all argument parsers in Cloud will throw `ParserException` on invalid input, in which case you're able
to override the exception message by configuring the manager's [CaptionRegistry](TODO).

The caption registry allows you to register caption providers that provide values for caption keys.
You may register multiple caption providers and the registry will iterate over them until one responds
with a non-`null` value.
There are some static factory methods in `CaptionProvider` that help generating providers for constant values.
All standard caption keys can be found in [StandardCaptionKey](TODO).
Some platform adapters have their own caption key classes as well.

The JavaDoc for the caption keys list their replacement variables.
The message registered for the caption will have those variables replaced with variables specific to the parser.
`<input>` is accepted by all parser captions, and will be replaced with the input that caused the exception
to be thrown.

<!-- prettier-ignore -->
!!! example annotate "Example caption registry usage"
    ```java
    final CaptionRegistry<SenderType> registry = manager.captionRegistry();
    registry.registerProvider(CaptionProvider.constantProvider(
            StandardCaptionKeys.ARGUMENT_PARSE_FAILURE_BOOLEAN,
            "'<input>' ain't a boolean >:("
    ));
    ```

You may create a custom caption formatter that generates more complex output types than strings.
This is particularly useful if you want to route the captions through some external system to generate
platform-native message types (i.e. `Component` for Minecraft). You may format captions using this custom
type by invoking `ParserException.formatCaption` or `CommandContext.formatCaption`.

## Parsers

### Parser Registry

The parser registry stores mappings between types and suppliers of parsers that produce those types.
The parser registry is primarily used in two different places:

- When only a value type has been supplied to a command component to look up the relevant parser.
- When using annotated command methods.

If you are creating a library or using `cloud-annotations`, it is recommended to register your parser
in the parser registry.
You can access the parser registry via `CommandManager#parserRegistry`.
The parser suppliers get access to a structure containing parser parameters.
These parameters are most often mapped to annotations, and allow for the customization of the parsers
when using `cloud-annotations`.

<!-- prettier-ignore -->
!!! example "Example parser registration"
    ```java
    parserRegistry.registerParserSupplier(TypeToken.get(Integer.class), options ->
        new IntegerParser<>(
                (int) options.get(StandardParameters.RANGE_MIN, Integer.MIN_VALUE),
                (int) options.get(StandardParameters.RANGE_MAX, Integer.MAX_VALUE)
        ));
    ```

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

<!-- prettier-ignore -->
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

Parsers implement `SuggestionProviderHolder` which means that they can return a suggestion provider by overriding
the `suggestionProvider` method.
However, the recommended way of providing suggestions is by implementing one of the suggestion provider
interfaces (`SuggestionProvider`, `SuggestionProvider.Strings`,
`BlockingSuggestionProvider` or `BlockingSuggestionProvider.Strings`).
If the parser implements a suggestion provider interface it does not need to override the `suggestionProvider`
method, as it'll return `this` by default.

## Extra

### Confirmations

Cloud has a [preprocessor](#pre---postprocessing) that allows you to create commands that require
an extra confirmation.

You can find examples of how make use of this system on GitHub, for either
[Builders](https://github.com/Incendo/cloud/blob/2.0.0-dev/examples/example-bukkit/src/main/java/cloud/commandframework/examples/bukkit/builder/feature/ConfirmationExample.java) or
[Annotations](https://github.com/Incendo/cloud/blob/2.0.0-dev/examples/example-bukkit/src/main/java/cloud/commandframework/examples/bukkit/annotations/feature/ConfirmationExample.java).

### Help generation

Cloud has a system that assists in querying for command information.
This is accessible through the `HelpHandler` that can be accessed using `CommandManager.createHelpHandler`.
This invokes a `HelpHandlerFactory`.
You may replace the default `HelpHandlerFactory` using `CommandManager.helpHandlerFactory(HelpHandlerFactory)`
to change how the information is generated.

The help handler will try to output as much information as it can, depending on how precise the query is.
There are three types of query results:

- **Index**: Returns a list of commands.
- **Multiple**: Returns a list of partial results.
- **Verbose**: Returns verbose information about a specific command.

You may query for results by using `HelpHandler.query(HelpQuery)`.
The help handler does not display any information, this is instead done by a `HelpRenderer`.
`cloud-core` does not contain any implementations of the help renderer as this is highly platform-specific,
but `cloud-minecraft-extras` contains an opinionated implementation of the help system for Minecraft.

You can find examples on GitHub for either
[Builders](https://github.com/Incendo/cloud/blob/2.0.0-dev/examples/example-bukkit/src/main/java/cloud/commandframework/examples/bukkit/builder/feature/HelpExample.java) or
[Annotations](https://github.com/Incendo/cloud/blob/2.0.0-dev/examples/example-bukkit/src/main/java/cloud/commandframework/examples/bukkit/annotations/feature/HelpExample.java).
