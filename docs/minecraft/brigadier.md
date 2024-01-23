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
