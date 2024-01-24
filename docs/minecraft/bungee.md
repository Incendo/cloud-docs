# cloud-bungee

`cloud-bungee` allows you to write commands for [BungeeCord](https://github.com/SpigotMC/BungeeCord).

## Links

<div class="grid cards" markdown>

- [:fontawesome-brands-github: Source Code](https://github.com/Incendo/cloud-minecraft/tree/master/cloud-bungee)
- [:fontawesome-brands-github: Example Plugin](https://github.com/Incendo/cloud-minecraft/tree/master/examples/example-bungee)

</div>

## Installation

Cloud for BungeeCord is available through [Maven Central](https://central.sonatype.com/artifact/cloud.commandframework/cloud-bungee).

<!-- prettier-ignore -->
=== "Maven"

    ```xml
    <dependencies>
        <dependency>
            <groupId>cloud.commandframework</groupId>
            <artifactId>cloud-bungee</artifactId>
            <version>2.0.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
    ```

=== "Gradle (Kotlin)"

    ```kotlin
    implementation("cloud.commandframework:cloud-bungee:2.0.0-SNAPSHOT")
    ```

=== "Gradle (Groovy)"

    ```groovy
    implementation 'cloud.commandframework:cloud-bungee:2.0.0-SNAPSHOT'
    ```

## Usage

`cloud-bungee` has a command manager implementation called `BungeeCommandManager` that can be created like:

```{ .java .annotate }
BungeeCommandManager<YourSenderType> commandManager = new BungeeCommandManager<>(
  plugin,
  executionCoordinator, /* (1) */
  senderMapper /* (2) */
);
```

1. Information about execution coordinators in general can be found
   [here](../core/index.md#execution-coordinators).
2. The sender mapper is a two-way mapping between BungeeCord's
   [`CommandSender`](https://ci.md-5.net/job/BungeeCord/ws/api/target/apidocs/net/md_5/bungee/api/CommandSender.html) and your custom sender type.
   You may use `SenderMapper.identity()` if using `CommandSender` as the sender type.

## Parsers

| Parser       | Type                                                                                                                        |
| ------------ | --------------------------------------------------------------------------------------------------------------------------- |
| PlayerParser | [ProxiedPlayer](https://ci.md-5.net/job/BungeeCord/ws/api/target/apidocs/net/md_5/bungee/api/connection/ProxiedPlayer.html) |
| ServerParser | [ServerInfo](https://ci.md-5.net/job/BungeeCord/ws/api/target/apidocs/net/md_5/bungee/api/config/ServerInfo.html)           |
