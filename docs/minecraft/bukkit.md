# cloud-bukkit

The `cloud-bukkit` module is home to parsers and other classes that make up the base of Cloud for Bukkit-based platforms.
`cloud-bukkit` is not intended to be consumed as a direct dependency, instead it should be consumed as
a transitive dependency of [`cloud-paper`](paper.md).

## Parsers

| Parser | Type (\* = cloud type) | Brigadier Type | Note |
| ---------------------------- | ---------------------------------------------------------------------------------------- | ------------------- | |
| UUIDParser | [UUID](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/UUID.html) | `uuid` | [link](#namespacedkeyparser) |
| NamespacedKeyParser | [NamespacedKey](https://jd.papermc.io/paper/1.20/org/bukkit/NamespacedKey.html) | `resource_location` | |
| EnchantmentParser | [Enchantment](https://jd.papermc.io/paper/1.20/org/bukkit/enchantments/Enchantment.html) | `enchantment` | |
| ItemStackParser | ProtoItemStack\* | `item_stack` | |
| ItemStackPredicateParser | ItemStackPredicate\* | `item_predicate` | |
| BlockPredicateParser | BlockPredicate\* | `block_predicate` | |
| SingleEntitySelectorParser | SingleEntitySelector\* | `entity` | [link](#selectors) |
| SinglePlayerSelectorParser | SinglePlayerSelector\* | `entity` | [link](#selectors) |
| MultipleEntitySelectorParser | MultipleEntitySelector\* | `entity` | [link](#selectors) |
| MultiplePlayerSelectorParser | MultiplePlayerSelector\* | `entity` | [link](#selectors) |
| LocationParser | [Location](https://jd.papermc.io/paper/1.20/org/bukkit/Location.html) | `vec3` | |
| Location2DParser | Location2D\* | `vec2` | |
| MaterialParser | [Material](https://jd.papermc.io/paper/1.20/org/bukkit/Material.html) | | |
| OfflinePlayerParser | [OfflinePlayer](https://jd.papermc.io/paper/1.20/org/bukkit/OfflinePlayer.html) | | |
| PlayerPayer | [Player](https://jd.papermc.io/paper/1.20/org/bukkit/entity/Player.html) | | |
| WorldParser | [World](https://jd.papermc.io/paper/1.20/org/bukkit/World.html) | | |

### NamespacedKeyParser

The `NamespacedKeyParser` parses namespaced key in the form `namespace:key`.

#### Annotations

###### `@DefaultNamespace`

Use `@DefaultNamespace("namespace")` on a component to set the namespace which will be used in the case that no
namespace is supplied by the command sender.

###### `@RequireExplicitNamespace`

Use `@RequireExplicitNamespace` to fail parsing if the command sender does not supply a namespace.

### Selectors

#### Annotations

##### `@AllowEmptySelection`

Use `@AllowEmptySelection` to allow the command sender to execute a command with a selector which selects zero entities.

## Descriptions

Cloud will register all root literals to the Bukkit [CommandMap](https://jd.papermc.io/paper/1.20/org/bukkit/command/CommandMap.html)
which means that they will show up in the Bukkit help menu.
Cloud will try to determine the description for the Bukkit help menu by:

1. Use the `BukkitCommandMeta.BUKKIT_DESCRIPTION` [meta](../core/index.md#command-meta) value of the command, if it exists.
2. Using the [CommandDescription](../core/index.md#command-descriptions), if a command is attached directly to the root literal.
3. Use the root literal [Description](../core/index.md#component-descriptions), if it's non-empty.
