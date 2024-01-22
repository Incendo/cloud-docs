# cloud-minecraft-extras

`cloud-minecraft-extras` contains some optional utilities for the Minecraft integrations.

<div class="grid cards" markdown>

- [MinecraftHelp](#minecraft-help)
- [MinecraftExceptionHandler](#minecraft-exception-handler)
- [RichDescription](#rich-description)
- [TextColorParser](#text-color-parser)

</div>

<!-- prettier-ignore -->
!!! note
    These utilities depend on [adventure](https://docs.advntr.dev/). You may need to depend on Adventure and shade it
    into your project, depending on the platform you're targeting. Many Minecraft platforms, such as Paper, ship with
    native implementations of Adventure, in which case you do not need to include it yourself.

## Minecraft Help

`MinecraftHelp` is an opinionated implementation of the [help system](../core/index.md#help-generation) using
Adventure components for styling and click handling.

![Minecraft Help 1](../assets/images/mce_help_1_dark.png#only-dark)
![Minecraft Help 1](../assets/images/mce_help_1_light.png#only-light)
![Minecraft Help 2](../assets/images/mce_help_2_dark.png#only-dark)
![Minecraft Help 2](../assets/images/mce_help_2_light.png#only-light)

All interactions with the Minecraft help system will take place through a `MinecraftHelp` instance.

You may create an instance with the default styling:

<!-- prettier-ignore -->
=== "Native Audience"

    ```java
    // Assuming YourSenderType extends Audience
    MinecraftHelp<YourSenderType> help = MinecraftHelp.createNative(
      "helpcommand",
      commandManager
    );
    ```

=== "Other"

    ```java
    MinecraftHelp<YourSenderType> help = MinecraftHelp.create(
      "helpcommand",
      commandManager,
      audienceMapper // YourSenderType -> Audience
    );
    ```

or you may override the defaults by using a builder:

<!-- prettier-ignore -->
=== "Native Audience"

    ```java
    MinecraftHelp<YourSenderType> help = MinecraftHelp.<YourSenderType>builder()
      .commandManager(commandManager)
      .audienceProvider(AudienceProvider.nativeProvider())
      .commandPrefix("/helpcommand")
      /* other settings... */
      .build();
    ```

=== "Other"

    ```java
    MinecraftHelp<YourSenderType> help = MinecraftHelp.<YourSenderType>builder()
      .commandManager(commandManager)
      .audienceProvider(yourAudienceProvider)
      .commandPrefix("/helpcommand")
      /* other settings... */
      .build();
    ```

You then want to invoke `MinecraftHelp.queryCommands(query, recipient)` in order to query the commands
and display the results to the recipient.

```java title="Example Help Command"
commandManager.command(
  commandManager.commandBuilder("helpcommand")
    .optional("query", greedyStringParser(), DefaultValue.constant(""))
    .handler(context -> {
      help.queryCommands(context.get("query"), context.sender());
    })
);
```

You may choose to add suggestions to the query argument as well:

```java title="Query Suggestions"
.optional(
  "query",
  greedyStringParser(),
  DefaultValue.constant(""),
  SuggestionProvider.blocking((ctx, in) -> commandManager.createHelpHandler()
      .queryRootIndex(ctx.sender())
      .entries()
      .stream()
      .map(CommandEntry::syntax)
      .map(Suggestion::simple)
      .collect(Collectors.toList())
  )
)
```

## Minecraft Exception Handler

## Rich Description

## Text Color Parser
