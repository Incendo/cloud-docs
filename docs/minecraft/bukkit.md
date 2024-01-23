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

## Parsers

| Parser                       | Type (\* = cloud type)                                                                   | Brigadier Type      |
| ---------------------------- | ---------------------------------------------------------------------------------------- | ------------------- |
| UUIDParser                   | [UUID](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/UUID.html) | `uuid`              |
| NamespacedKeyParser          | [NamespacedKey](https://jd.papermc.io/paper/1.20/org/bukkit/NamespacedKey.html)          | `resource_location` |
| EnchantmentParser            | [Enchantment](https://jd.papermc.io/paper/1.20/org/bukkit/enchantments/Enchantment.html) | `enchantment`       |
| ItemStackParser              | ProtoItemStack\*                                                                         | `item_stack`        |
| ItemStackPredicateParser     | ItemStackPredicate\*                                                                     | `item_predicate`    |
| BlockPredicateParser         | BlockPredicate\*                                                                         | `block_predicate`   |
| SingleEntitySelectorParser   | SingleEntitySelector\*                                                                   | `entity`            |
| SinglePlayerSelectorParser   | SinglePlayerSelector\*                                                                   | `entity`            |
| MultipleEntitySelectorParser | MultipleEntitySelector\*                                                                 | `entity`            |
| MultiplePlayerSelectorParser | MultiplePlayerSelector\*                                                                 | `entity`            |
| LocationParser               | [Location](https://jd.papermc.io/paper/1.20/org/bukkit/Location.html)                    | `vec3`              |
| Location2DParser             | Location2D\*                                                                             | `vec2`              |
| MaterialParser               | [Material](https://jd.papermc.io/paper/1.20/org/bukkit/Material.html)                    |                     |
| OfflinePlayerParser          | [OfflinePlayer](https://jd.papermc.io/paper/1.20/org/bukkit/OfflinePlayer.html)          |                     |
| PlayerPayer                  | [Player](https://jd.papermc.io/paper/1.20/org/bukkit/entity/Player.html)                 |                     |
| WorldParser                  | [World](https://jd.papermc.io/paper/1.20/org/bukkit/World.html)                          |                     |
