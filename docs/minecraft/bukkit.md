# cloud-bukkit

<!-- prettier-ignore -->
!!! note
    It is not recommended to use `cloud-bukkit`. Instead, we recommend using [cloud-paper](paper.md) which works on both
    Spigot & Paper servers, and offers access to modern features such as Brigadier and asynchronous suggestions.

## Installation

Cloud for Bukkit is available through [Maven Central](https://search.maven.org/search?q=cloud.commandframework).

<!-- prettier-ignore -->
=== "Maven"

    ```xml
    <dependencies>
        <dependency>
            <groupId>cloud.commandframework</groupId>
            <artifactId>cloud-bukkit</artifactId>
            <version>2.0.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
    ```

=== "Gradle (Kotlin)"

    ```kotlin
    implementation("cloud.commandframework:cloud-bukkit:2.0.0-SNAPSHOT")
    ```

=== "Gradle (Groovy)"

    ```groovy
    implementation 'cloud.commandframework:cloud-bukkit:2.0.0-SNAPSHOT'
    ```

## Usage

`cloud-bukkit` has a command manager implementation called `BukkitCommandManager`, which you construct like:

```java
BukkitCommandManager<YourSenderType> commandManager = new BukkitCommandManager<>(
  yourPlugin /* 1 */,
  executionCoordinator /* 2 */,
  senderMapper /* 3 */
);
```

1. You need to pass an instance of the plugin that is constructing the command manager. This is used to register
   the commands and the different event listeners.
2. Information about execution coordinators can be found
   [here](../core/index.md#execution-coordinators).
3. The sender mapper is a two-way mapping between Bukkit's
   [CommandSender](https://jd.papermc.io/paper/1.20/org/bukkit/command/CommandSender.html) and your custom sender type.
   If you use `CommandSender` as the sender type, then you may use `SenderMapper.identity()`.

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
