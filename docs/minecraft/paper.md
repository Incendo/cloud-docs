# cloud-paper

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
