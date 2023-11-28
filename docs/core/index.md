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

## Extra

### Configurations

### Help generation

## Parsers

### Parser Registry

### Standard Parsers

#### String

#### Character

#### Numbers

#### Boolean

#### Enum

### Flags

### Aggregate Parsers

Aggregate parsers are a new concept as of Cloud v2, and they supersede the old compound argument concept.
An aggregate parser is a combination of multiple parsers that maps the intermediate results into an output
type using a mapper.

You may either implement the `AggregateCommandParser` interface, or using construct the parser by using a builder
that you create by calling `AggregateCommandParser.builder()`.

<!-- prettier-ignore -->
!!! example
    ```java
    final AggregateCommandParser<CommandSender, Location> locationParser = AggregateCommandParser<CommandSender>builder()
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
