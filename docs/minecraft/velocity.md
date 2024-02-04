# cloud-velocity

`cloud-velocity` allows you to write commands for [Velocity](https://papermc.io/software/velocity).

## Links

<div class="grid cards" markdown>

- [:fontawesome-brands-github: Source Code](https://github.com/Incendo/cloud-minecraft/tree/master/cloud-velocity)
- [:fontawesome-brands-java: JavaDoc](https://javadoc.io/doc/org.incendo/cloud-velocity)
- [:fontawesome-brands-github: Example Plugin](https://github.com/Incendo/cloud-minecraft/tree/master/examples/example-velocity)

</div>

## Installation

Cloud for Velocity is available through [Maven Central](https://central.sonatype.com/artifact/org.incendo/cloud-paper).

{{ dependency_listing("velocity") }}

## Usage

`cloud-velocity` has a command manager implementation called
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-velocity/latest/org/incendo/cloud/velocity/VelocityCommandManager.html", "VelocityCommandManager") }}
that can be created in two ways.

By using a Guice injector:

```java
// @Inject private Injector injector;
Injector childInjector = injector.createChildInjector(
  new CloudInjectionModule<>(
    YourSenderType.class,
    executionCoordinator, /* 1 */
    senderMapper /* 2 */
  )
);

VelocityCommandManager<YourSenderType> commandManager = childInjector.getInstance(
  Key.get(new TypeLiteral<VelocityCommandManager<YourSenderType>>() {})
);
```

Or, manually creating an instance:

```java
VelocityCommandManager<YourSenderType> commandManager = new VelocityCommandManager<>(
  pluginContainer,
  proxyServer,
  executionCoordinator, /* 1 */
  senderMapper /* 2 */
);
```

1. Information about execution coordinators in general can be found
   [here](../core/index.md#execution-coordinators).
2. The sender mapper is a two-way mapping between Velocity's
   {{ javadoc("https://jd.papermc.io/velocity/3.0.0/com/velocitypowered/api/command/CommandSource.html", "CommandSource") }} and your custom sender type.
   You may use {{ javadoc("<https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/SenderMapper.html#identity()>", "SenderMapper.identity()") }}
   if using {{ javadoc("https://jd.papermc.io/velocity/3.0.0/com/velocitypowered/api/command/CommandSource.html", "CommandSource") }} as the sender type.

## Brigadier

Velocity commands are powered by [Brigadier](https://github.com/mojang/brigadier), which means that you may
use the features from [cloud-brigadier](brigadier.md).

## Parsers

| Parser                                                                                                                       | Type                                                                                                                |
| ---------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------- |
| [PlayerParser](https://javadoc.io/doc/org.incendo/cloud-velocity/latest/org/incendo/cloud/velocity/parser/PlayerParser.html) | [Player](https://jd.papermc.io/velocity/3.0.0/com/velocitypowered/api/proxy/Player.html)                            |
| [ServerParser](https://javadoc.io/doc/org.incendo/cloud-velocity/latest/org/incendo/cloud/velocity/parser/ServerParser.html) | [RegisteredServer](https://jd.papermc.io/velocity/3.0.0/com/velocitypowered/api/proxy/server/RegisteredServer.html) |

## Localization

`cloud-velocity` provides additional caption keys for the [localization](../localization/index.md) system.
These can be found in
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-velocity/latest/org/incendo/cloud/velocity/VelocityCaptionKeys.html", "VelocityCaptionKeys") }}.
The default caption values can be found in
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-velocity/latest/org/incendo/cloud/velocity/VelocityCommandManager.html", "VelocityCommandManager") }}.
