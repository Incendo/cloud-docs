# cloud-minecraft-extras

`cloud-minecraft-extras` contains some optional utilities for the Minecraft integrations.

<!-- prettier-ignore -->
!!! note
    These utilities depend on [adventure](https://docs.advntr.dev/). You may need to depend on Adventure and shade it
    into your project, depending on the platform you're targeting. Many Minecraft platforms, such as Paper, ship with
    native implementations of Adventure, in which case you do not need to include it yourself.

<div class="grid cards" markdown>

- [:fontawesome-brands-github: Source Code](https://github.com/Incendo/cloud-minecraft/tree/master/cloud-minecraft-extras)
- [:fontawesome-brands-java: JavaDoc](https://javadoc.io/doc/org.incendo/cloud-minecraft-extras)
- [MinecraftHelp](#minecraft-help)
- [MinecraftExceptionHandler](#minecraft-exception-handler)
- [RichDescription](#rich-description)
- [TextColorParser](#text-color-parser)
- [ComponentParser](#component-parser)

</div>

## Installation

Cloud Minecraft Extras is available through [Maven Central](https://central.sonatype.com/artifact/org.incendo/cloud-minecraft-extras).

{{ dependency_listing("minecraft-extras") }}

## Minecraft Help

`MinecraftHelp` is an opinionated implementation of the [help system](../core/index.md#help-generation) using
Adventure components for styling and click handling.

{{ figure("../assets/images/minecraft/mce_help_index.png", "Index View") }}
{{ figure("../assets/images/minecraft/mce_help_verbose.png", "Verbose View") }}

All interactions with the Minecraft help system will take place through a `MinecraftHelp` instance.

You may create an instance with the default styling:

<!-- prettier-ignore -->
=== "Native Audience"

    ```java
    // Assuming YourSenderType extends Audience
    MinecraftHelp<YourSenderType> help = MinecraftHelp.createNative(
      "/helpcommand",
      commandManager
    );
    ```

=== "Other"

    ```java
    MinecraftHelp<YourSenderType> help = MinecraftHelp.create(
      "/helpcommand",
      commandManager,
      audienceMapper // YourSenderType -> Audience
    );
    ```

or you may override the defaults by using a builder:

<!-- prettier-ignore -->
=== "Native Audience"

    {{ snippet("minecraft/MinecraftHelpExample.java", section = "native", title = "", indent = 4) }}

=== "Other"

    {{ snippet("minecraft/MinecraftHelpExample.java", section="non_native", title = "", indent = 4) }}

You then want to invoke `MinecraftHelp.queryCommands(query, recipient)` in order to query the commands
and display the results to the recipient.

{{ snippet("minecraft/MinecraftHelpExample.java", section = "help_command", title = "Example Help Command") }}

You may choose to add suggestions to the query argument as well:

{{ snippet("minecraft/MinecraftHelpExample.java", section = "help_suggestions", title = "Query Suggestions") }}

## Minecraft Exception Handler

`MinecraftExceptionHandler` is an opinionated collection of [exception handlers](../core/index.md#exception-handling)
that uses Adventure components for styling.

All interactions with the system are done through an instance of `MinecraftExceptionHandler`.

<!-- prettier-ignore -->
=== "Native Audience"

    ```java
    // Assuming your sender type extends Audience
    MinecraftExceptionHandler.createNative();
    ```

=== "Other"

    ```java
    MinecraftExceptionHandler.create(audienceProvider // C -> Audience);
    ```

You then use the fluent methods to create handlers for the different exception types.
If you want to register the default handlers for all types you may use `defaultHandlers()`.

You may supply a decorator which will transform the created components. This is useful if you
want to prefix all messages, or apply specific styling.

{{ snippet("minecraft/MinecraftExceptionHandlerExample.java", section = "native", title = "Example decorator") }}

When you're done configuring the builder you need to apply it to the command manager by using
`registerTo(CommandManager)`.

{{ snippet("minecraft/MinecraftExceptionHandlerExample.java", section = "complete", title = "Complete example") }}

### Localization

`MinecraftExceptionHandler` uses the [localization](../localization/index.md) system. By default, the exception
handler will make use of a
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-minecraft-extras/latest/org/incendo/cloud/minecraft/extras/ComponentCaptionFormatter.html", "ComponentCaptionFormatter") }}
that wraps the caption value in a text component.

You may choose to replace the caption formatter with a component formatter that uses [MiniMessage](https://docs.advntr.dev/minimessage/index.html) by using
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-minecraft-extras/latest/org/incendo/cloud/minecraft/extras/ComponentCaptionFormatter.html", "ComponentCaptionFormatter.miniMessage()") }}.
[MiniMessage](https://docs.advntr.dev/minimessage/index.html) will then be used for both styling and placeholder replacements.

## Rich Description

`RichDescription` allows for the use of Adventure components in both component and command descriptions.
These descriptions are supported by [MinecraftHelp](#minecraft-help). If descriptions are used in other places
they are likely to be transformed into plain text.

```java
// Static factory:
Description description = RichDescription.of(text("Hello world!"));

// Statically importable alias:
Description description = RichDescription.richDescription(text("Hello world!"));

// Utility for translatable components:
Description description = RichDescription.translatable("some.key", text("an arg"));
```

## Text Color Parser

`TextColorParser` is a [parser](../core/index.md#parsers) which parses Adventure `TextColor`s.
It parses `NamedTextColor`, legacy color codes using `&` as well as hex codes.

```java
ParserDescriptor<?, TextColor> parser = TextColorParser.textColorParser();
```

## Component Parser

`ComponentParser` is a [parser](../core/index.md#parsers) which parses Adventure `Component`s via
a specified "decoder" function.

```java
// This creates a parser that uses MiniMessage for decoding the argument, and uses
// greedy string parsing.
ParserDescriptor<?, Component> parser = ComponentParser.componentParser(
  MiniMessage.miniMessage(),
  StringParser.StringMode.GREEDY_FLAG_YIELDING
);
```
