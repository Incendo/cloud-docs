# cloud-brigadier

[Brigadier](https://github.com/mojang/brigadier) is Mojang's command system. Cloud integrates with Brigadier on platforms
where this is supported. Unless you want to create a platform integration it is unlikely that you will want to depend
on `cloud-brigadier` directly. Instead, you are able to interact with Brigadier through the platform integration.

Where possible Cloud will map to platform-native argument types. Consult the documentation for your platform
implementation to find out what mappings exist. You may provide your own mappings to Brigadier argument types
through the `CloudBrigadierManager` that is available from the platform's `CommandManager`.

<figure markdown>
  ![Brigadier Native Arguments](../assets/images/minecraft/brigadier_native_types.png)
  <figcaption>Native Brigadier Argument Types</figcaption>
</figure>

Custom argument types are mapped to Brigadier strings. Cloud suggestions are fully supported and will behave
like Brigadier-native suggestions.

<figure markdown>
  ![Brigadier Custom Arguments](../assets/images/minecraft/brigadier_custom_types.png)
  <figcaption>Custom Argument Types</figcaption>
</figure>

Cloud commands map very accurately to Brigadier commands, which means that you get to benefit from
per-argument validation, coloring and syntax hinting.

<figure markdown>
  ![Brigadier Separate Arguments](../assets/images/minecraft/brigadier_separate_args.png)
  <figcaption>Accurate Command Structure</figcaption>
</figure>

!!! warning "Alias Registration"

    Only aliases for root nodes will be registered when using Brigadier. Due to how it's command
    tree works it can quickly become inflated when sub-commands have aliases.

## CloudBrigadierManager

You can get an instance of `CloudBrigadierManager` from the command manager for your platform of choice.
If the command manager is a `BrigadierManagerHolder`, then you can get the instance using `commandManager.brigadierManager()`.

The `CloudBrigadierManager` is how you interact with Brigadier to register mappings and configure settings.

### Settings

`CloudBrigadierManager` has settings that can be accessed using `CloudBrigadierManager.settings()`.

```java title="Example Setting Usage"
CloudBrigadierManager<YourSenderType> manager = commandManager.brigadierManager();
Configurable<BrigadierSetting> settings = manager.settings();
settings.set(BrigadierSetting.FORCE_EXECUTABLE, true);
```

### Native Suggestions

You may choose whether Cloud should delegate suggestions to Brigadier or use the Cloud suggestion provider
when generating suggestions for mapped Brigadier types.

You may enable or disable suggestions for all numerical types using
`CloudBrigadierManager.setNativeNumberSuggestion(Boolean)`. By default, this is `false` as Brigadier doesn't suggest
numbers whereas Cloud does.

You may use `CloudBrigadierManager.setNativeSuggestions(parserClass, true)` to enable native suggestions for
a specific parser.

### Mappings

You may map your custom parsers to Brigadier parsers, which means that you will have access to client-side
validation, suggestions, etc.

Mappings are registered through a `BrigadierMappings` instance which can be retrieved from the
`CloudBrigadierManager`.
