# cloud-bukkit

The `cloud-bukkit` module is home to parsers and other classes that make up the base of Cloud for Bukkit-based platforms.
`cloud-bukkit` is not intended to be consumed as a direct dependency, instead it should be consumed as
a transitive dependency of [`cloud-paper`](paper.md).

## Links

<div class="grid cards" markdown>

- [:fontawesome-brands-github: Source Code](https://github.com/Incendo/cloud-minecraft/tree/master/cloud-bukkit)
- [:fontawesome-brands-java: JavaDoc](https://javadoc.io/doc/org.incendo/cloud-bukkit)

</div>

## Parsers

| Parser | Type (\* = cloud type) | Brigadier Type | Note |
| ---------------------------- | ---------------------------------------------------------------------------------------- | ------------------- | |
| UUIDParser | [UUID](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/UUID.html) | `uuid` | [link](#namespacedkeyparser) |
| NamespacedKeyParser | [NamespacedKey](https://jd.papermc.io/paper/1.20/org/bukkit/NamespacedKey.html) | `resource_location` | |
| EnchantmentParser | [Enchantment](https://jd.papermc.io/paper/1.20/org/bukkit/enchantments/Enchantment.html) | `enchantment` | |
| ItemStackParser | [ProtoItemStack\*](https://javadoc.io/doc/org.incendo/cloud-bukkit/latest/org/incendo/cloud/bukkit/data/ProtoItemStack.html) | `item_stack` | |
| ItemStackPredicateParser | [ItemStackPredicate\*](https://javadoc.io/doc/org.incendo/cloud-bukkit/latest/org/incendo/cloud/bukkit/data/ItemStackPredicate.html) | `item_predicate` | |
| BlockPredicateParser | [BlockPredicate\*](https://javadoc.io/doc/org.incendo/cloud-bukkit/latest/org/incendo/cloud/bukkit/data/BlockPredicate.html) | `block_predicate` | |
| SingleEntitySelectorParser | [SingleEntitySelector\*](https://javadoc.io/doc/org.incendo/cloud-bukkit/latest/org/incendo/cloud/bukkit/data/SingleEntitySelector.html) | `entity` | [link](#selectors) |
| SinglePlayerSelectorParser | [SinglePlayerSelector\*](https://javadoc.io/doc/org.incendo/cloud-bukkit/latest/org/incendo/cloud/bukkit/data/SinglePlayerSelector.html) | `entity` | [link](#selectors) |
| MultipleEntitySelectorParser | [MultipleEntitySelector\*](https://javadoc.io/doc/org.incendo/cloud-bukkit/latest/org/incendo/cloud/bukkit/data/MultipleEntitySelector.html) | `entity` | [link](#selectors) |
| MultiplePlayerSelectorParser | [MultiplePlayerSelector\*](https://javadoc.io/doc/org.incendo/cloud-bukkit/latest/org/incendo/cloud/bukkit/data/MultiplePlayerSelector.html) | `entity` | [link](#selectors) |
| LocationParser | [Location](https://jd.papermc.io/paper/1.20/org/bukkit/Location.html) | `vec3` | |
| Location2DParser | [Location2D\*](https://javadoc.io/doc/org.incendo/cloud-bukkit/latest/org/incendo/cloud/bukkit/parser/location/Location2D.html) | `vec2` | |
| MaterialParser | [Material](https://jd.papermc.io/paper/1.20/org/bukkit/Material.html) | | |
| OfflinePlayerParser | [OfflinePlayer](https://jd.papermc.io/paper/1.20/org/bukkit/OfflinePlayer.html) | | |
| PlayerPayer | [Player](https://jd.papermc.io/paper/1.20/org/bukkit/entity/Player.html) | | |
| WorldParser | [World](https://jd.papermc.io/paper/1.20/org/bukkit/World.html) | | |

### NamespacedKeyParser

The
[`NamespacedKeyParser`](https://javadoc.io/doc/org.incendo/cloud-bukkit/latest/org/incendo/cloud/bukkit/parser/NamespacedKeyParser.html)
parses namespaced key in the form `namespace:key`.

#### Annotations

###### `@DefaultNamespace`

Use
[`@DefaultNamespace("namespace")`](https://javadoc.io/doc/org.incendo/cloud-bukkit/latest/org/incendo/cloud/bukkit/annotation/specifier/DefaultNamespace.html)
on a component to set the namespace which will be used in the case that no
namespace is supplied by the command sender.

###### `@RequireExplicitNamespace`

Use
[`@RequireExplicitNamespace`](https://javadoc.io/doc/org.incendo/cloud-bukkit/latest/org/incendo/cloud/bukkit/annotation/specifier/RequireExplicitNamespace.html)
to fail parsing if the command sender does not supply a namespace.

### Selectors

#### Annotations

##### `@AllowEmptySelection`

Use
[`@AllowEmptySelection`](https://javadoc.io/doc/org.incendo/cloud-bukkit/latest/org/incendo/cloud/bukkit/annotation/specifier/AllowEmptySelection.html)
to allow the command sender to execute a command with a selector which selects zero entities.

## Descriptions

Cloud will register all root literals to the Bukkit [CommandMap](https://jd.papermc.io/paper/1.20/org/bukkit/command/CommandMap.html)
which means that they will show up in the Bukkit help menu.
Cloud will try to determine the description for the Bukkit help menu by:

1. Use the [`BukkitCommandMeta.BUKKIT_DESCRIPTION`](https://javadoc.io/doc/org.incendo/cloud-bukkit/latest/org/incendo/cloud/bukkit/BukkitCommandMeta.html) [meta](../core/index.md#command-meta) value of the command, if it exists.
2. Using the [CommandDescription](../core/index.md#command-descriptions), if a command is attached directly to the root literal.
3. Use the root literal [Description](../core/index.md#component-descriptions), if it's non-empty.

## Localization

`cloud-bukkit` provides additional caption keys for the [localization](../localization/index.md) system.
These can be found in
[`BukkitCaptionKeys`](https://javadoc.io/doc/org.incendo/cloud-bukkit/latest/org/incendo/cloud/bukkit/BukkitCaptionKeys.html).
The default caption values can be found in
[`BukkitDefaultCaptionsProvider`](https://javadoc.io/doc/org.incendo/cloud-bukkit/latest/org/incendo/cloud/bukkit/BukkitDefaultCaptionsProvider.html).
