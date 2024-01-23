# cloud-paper

<!-- prettier-ignore -->
!!! note
    It is recommended that you read the [cloud-bukkit](bukkit.md) documentation as well, as the different topics
    covered there apply to `cloud-paper` as well.

`cloud-paper` is an extension of [cloud-bukkit](bukkit.md) with additions for Paper servers. You may use
`cloud-paper` on non-Paper servers, in which case the Paper-specific features will be disabled.

## Installation

Cloud for Paper is available through [Maven Central](https://search.maven.org/search?q=cloud.commandframework).

<!-- prettier-ignore -->
=== "Maven"

    ```xml
    <dependencies>
        <dependency>
            <groupId>cloud.commandframework</groupId>
            <artifactId>cloud-paper</artifactId>
            <version>2.0.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
    ```

=== "Gradle (Kotlin)"

    ```kotlin
    implementation("cloud.commandframework:cloud-paper:2.0.0-SNAPSHOT")
    ```

=== "Gradle (Groovy)"

    ```groovy
    implementation 'cloud.commandframework:cloud-paper:2.0.0-SNAPSHOT'
    ```

## Usage

`cloud-paper` has a command manager implementation called `PaperCommandManager`, which you construct like:

```java
PaperCommandManager<YourSenderType> commandManager = new PaperCommandManager<>(
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

## Brigadier

Paper exposes Brigadier, which means that you may use the features from [cloud-brigadier](brigadier.md) on Paper
servers.

You may enable Brigadier mappings using `PaperCommandManager.registerBrigadier()`. You should make use of the
capability system to make sure that Brigadier is available on the server your plugin is running on:

```java
if (commandManager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
  commandManager.registerBrigadier();
}
```

## Asynchronous Completions

<!-- prettier-ignore -->
!!! note
    You should not use asynchronous completions together with Brigadier. Brigadier suggestions are already non-blocking.

Paper allows for non-blocking suggestions. You are highly recommended to make use of this, as Cloud will invoke
the argument parsers during suggestion generation which ideally should not take place on the main server thread.

You may enable asynchronous completions using `PaperCommandManager.registerAsynchronousCompletions()`.
You should make use of the capability system to make sure that this is available on the server your plugin is running on:

```java
if (commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
  commandManager.registerAsynchronousCompletions();
}
```

## Parsers

`cloud-paper` has access to all the parsers from [cloud-bukkit](bukkit.md#parsers).
