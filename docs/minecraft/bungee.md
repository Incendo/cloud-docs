# cloud-bungee

`cloud-bungee` allows you to write commands for [BungeeCord](https://github.com/SpigotMC/BungeeCord).

## Links

<div class="grid cards" markdown>

- [:fontawesome-brands-github: Source Code](https://github.com/Incendo/cloud-minecraft/tree/master/cloud-bungee)
- [:fontawesome-brands-java: JavaDoc](https://javadoc.io/doc/org.incendo/cloud-bungee)
- [:fontawesome-brands-github: Example Plugin](https://github.com/Incendo/cloud-minecraft/tree/master/examples/example-bungee)

</div>

## Installation

Cloud for BungeeCord is available through [Maven Central](https://central.sonatype.com/artifact/org.incendo/cloud-bungee).

<!-- prettier-ignore -->
=== "Maven"

    ```xml
    <dependencies>
        <dependency>
            <groupId>org.incendo</groupId>
            <artifactId>cloud-bungee</artifactId>
            <version>2.0.0-beta.1</version>
        </dependency>
    </dependencies>
    ```

=== "Gradle (Kotlin)"

    ```kotlin
    implementation("org.incendo:cloud-bungee:2.0.0-beta.1")
    ```

=== "Gradle (Groovy)"

    ```groovy
    implementation 'org.incendo:cloud-bungee:2.0.0-beta.1'
    ```

## Usage

`cloud-bungee` has a command manager implementation called
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-bungee/latest/org/incendo/cloud/bungee/BungeeCommandManager.html", "BungeeCommandManager") }}
that can be created like:

```{ .java .annotate }
BungeeCommandManager<YourSenderType> commandManager = new BungeeCommandManager<>(
  plugin,
  executionCoordinator, /* (1)! */
  senderMapper /* (2)! */
);
```

1. Information about execution coordinators in general can be found
   [here](../core/index.md#execution-coordinators).
2. The sender mapper is a two-way mapping between BungeeCord's
   {{ javadoc("https://ci.md-5.net/job/BungeeCord/ws/api/target/apidocs/net/md_5/bungee/api/CommandSender.html", "CommandSender") }} and your custom sender type.
   You may use {{ javadoc("<https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/SenderMapper.html#identity()>", "SenderMapper.identity()") }} if using {{ javadoc("https://ci.md-5.net/job/BungeeCord/ws/api/target/apidocs/net/md_5/bungee/api/CommandSender.html", "CommandSender") }} as the sender type.

## Parsers

| Parser                                                                                                                   | Type                                                                                                                        |
| ------------------------------------------------------------------------------------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------- |
| [PlayerParser](https://javadoc.io/doc/org.incendo/cloud-bungee/latest/org/incendo/cloud/bungee/parser/PlayerParser.html) | [ProxiedPlayer](https://ci.md-5.net/job/BungeeCord/ws/api/target/apidocs/net/md_5/bungee/api/connection/ProxiedPlayer.html) |
| [ServerParser](https://javadoc.io/doc/org.incendo/cloud-bungee/latest/org/incendo/cloud/bungee/parser/ServerParser.html) | [ServerInfo](https://ci.md-5.net/job/BungeeCord/ws/api/target/apidocs/net/md_5/bungee/api/config/ServerInfo.html)           |

## Localization

`cloud-bungee` provides additional caption keys for the [localization](../localization/index.md) system.
These can be found in
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-bungee/latest/org/incendo/cloud/bungee/BungeeCaptionKeys.html", "BungeeCaptionKeys") }}.
The default caption values can be found in
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-bungee/latest/org/incendo/cloud/bungee/BungeeCommandManager.html", "BungeeCommandManager") }}.
