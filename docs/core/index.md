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

### Creating a command manager

#### Execution coordinators

### Building a command

#### Descriptions

#### Permissions

#### Sender types

#### Components

#### Literals

#### Variable

##### Required

##### Optional

### Executing a command

### Customizing the command manager

#### Pre-processing

#### Post-processing

#### Exception handling

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
!!! example
    Example of a command with a presence flag.
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
!!! example
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

## Extra

### Configurations

### Help generation
