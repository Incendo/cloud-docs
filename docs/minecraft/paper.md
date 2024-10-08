# cloud-paper

`cloud-paper` is an extension of `cloud-bukkit` with additional support for
[Paper](https://papermc.io/software/paper)-based platforms. `cloud-paper` maintains support for all platforms supported
by `cloud-bukkit`, and therefore is the recommended dependency to use Cloud on any Bukkit-based platform.
The following documentation is written with the assumption that you have already read and understand the
[`cloud-bukkit` documentation](bukkit.md).

## Links

<div class="grid cards" markdown>

- [:fontawesome-brands-github: Source Code](https://github.com/Incendo/cloud-minecraft/tree/master/cloud-paper)
- [:fontawesome-brands-java: JavaDoc](https://javadoc.io/doc/org.incendo/cloud-paper)
- [:fontawesome-brands-github: Example Plugin](https://github.com/Incendo/cloud-minecraft/tree/master/examples/example-bukkit)

</div>

## Installation

Cloud for Paper is available through [Maven Central](https://central.sonatype.com/artifact/org.incendo/cloud-paper).

{{ dependency_listing("paper") }}

## Usage

`cloud-paper` has two different command manager implementations:

- {{ javadoc("https://javadoc.io/doc/org.incendo/cloud-paper/latest/org/incendo/cloud/paper/PaperCommandManager.html", "PaperCommandManager") }}: Paper command API
- {{ javadoc("https://javadoc.io/doc/org.incendo/cloud-paper/latest/org/incendo/cloud/paper/LegacyPaperCommandManager.html", "LegacyPaperCommandManager") }}: Legacy command API

{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-paper/latest/org/incendo/cloud/paper/PaperCommandManager.html", "PaperCommandManager") }} should be preferred
when targeting Paper 1.20.6+ exclusively. The new manager allows registering commands at bootstrapping time in addition to `onEnable`,
which allows for using those commands in datapack functions.

If the plugin is targeting older Paper versions or non-paper servers, then
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-paper/latest/org/incendo/cloud/paper/LegacyPaperCommandManager.html", "LegacyPaperCommandManager") }}
should be used.

!!! tip "Plugin Configuration Files"

    Do not register your commands in your plugin.yml or paper-plugin.yml, Cloud handles the registration
    itself and doing it yourself will cause issues.

### Legacy

The legacy command manager can be instantiated in two different ways.

With a custom sender type:

{{ snippet("minecraft/PaperExample.java", section = "legacy_custom", title = "") }}

Or, using Bukkit's {{ javadoc("https://jd.papermc.io/paper/1.20/org/bukkit/command/CommandSender.html", "CommandSender") }}:

{{ snippet("minecraft/PaperExample.java", section = "legacy_native", title = "") }}

1. You need to pass an instance of the plugin that is constructing the command manager. This is used to register
   the commands and the different event listeners.
2. Information about execution coordinators in general can be found
   [here](../core/index.md#execution-coordinators). See [below](#execution-coordinators) for info specific to
   Bukkit-based platforms.
3. The sender mapper is a two-way mapping between Bukkit's
   {{ javadoc("https://jd.papermc.io/paper/1.20/org/bukkit/command/CommandSender.html", "CommandSender") }} and your custom sender type.
   Using {{ javadoc("<https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/SenderMapper.html#identity()>", "SenderMapper.identity()") }}
   is equivalent to the {{ javadoc("<https://javadoc.io/doc/org.incendo/cloud-paper/latest/org/incendo/cloud/paper/LegacyPaperCommandManager.html#createNative(org.bukkit.plugin.Plugin,org.incendo.cloud.execution.ExecutionCoordinator)>", "createNative") }}
   static factory method.

### Modern

The modern command manager is created using a builder. You may either use the native
{{ javadoc("https://jd.papermc.io/paper/1.20.6/io/papermc/paper/command/brigadier/CommandSourceStack.html", "CommandSourceStack") }}:
{{ snippet("minecraft/PaperExample.java", section = "modern_native", title = "") }}

or a custom type:
{{ snippet("minecraft/PaperExample.java", section = "modern_custom", title = "") }}

## Brigadier

Paper exposes [Brigadier](https://github.com/mojang/brigadier), which means that you may use the features
from [cloud-brigadier](brigadier.md) on Paper servers. When using the modern Paper manager, you do not need to explicitly
enable Brigadier.

When using the legacy command manager you may enable Brigadier mappings using
{{ javadoc("<https://javadoc.io/doc/org.incendo/cloud-paper/latest/org/incendo/cloud/paper/LegacyPaperCommandManager.html#registerBrigadier()>", "LegacyPaperCommandManager#registerBrigadier()") }}.
You should make use of the
capability system to make sure that Brigadier is available on the server your plugin is running on:

```java
if (commandManager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
  commandManager.registerBrigadier();
}
```

## Asynchronous completions

<!-- prettier-ignore -->
!!! note
    You should not use asynchronous completions together with Brigadier. Brigadier suggestions are already non-blocking,
    and the asynchronous completion API reduces the fidelity of suggestions compared to Brigadier alone.

Paper allows for non-blocking suggestions. You are highly recommended to make use of this, as Cloud will invoke
the argument parsers during suggestion generation which ideally should not take place on the main server thread.

You may enable asynchronous completions using
{{ javadoc("<https://javadoc.io/doc/org.incendo/cloud-paper/latest/org/incendo/cloud/paper/LegacyPaperCommandManager.html#registerAsynchronousCompletions()>", "LegacyPaperCommandManager#registerAsynchronousCompletions()") }}.
You should make use of the capability system to make sure that this is available on the server your plugin is running on:

```java
if (commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
  commandManager.registerAsynchronousCompletions();
}
```

## Execution coordinators

Due to Bukkit blocking the main thread for suggestion requests, it's potentially unsafe to use anything other than
{{ javadoc("<https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/execution/ExecutionCoordinator.html#nonSchedulingExecutor()>", "ExecutionCoordinator.nonSchedulingExecutor()") }}
for
{{ javadoc("<https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/execution/ExecutionCoordinator.Builder.html#suggestionsExecutor(java.util.concurrent.Executor)>", "ExecutionCoordinator.Builder#suggestionsExecutor(Executor)") }}.
Once the coordinator, a suggestion provider, parser,
or similar routes suggestion logic off of the calling \(main) thread, it won't be possible to schedule further logic
back to the main thread without a deadlock. When Brigadier support is active, this issue is avoided, as it allows
for non-blocking suggestions. Paper's [asynchronous completions](#asynchronous-completions) API can also be used to
avoid this issue, however when Brigadier is available it should be preferred (for reasons mentioned above).

Example code to avoid this problem:

```java
if (commandManager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
  commandManager.registerBrigadier();
} else if (commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
  commandManager.registerAsynchronousCompletions();
}
// else: we can't avoid the problem, very old Paper or Spigot
```

## Parsers

`cloud-paper` has access to all the parsers from [cloud-bukkit](bukkit.md#parsers).

## Provided Sender Mapper

Cloud includes a built-in sender mapper designed for the command manager. Due to the CommandSourceStack having no exposed implementations it can be difficult to work,
here's an example of creating a command manager with the sender mapper and using the provided mapped sender:

{{ snippet("minecraft/PaperExample.java", section = "modern_simple_sender_mapper", title = "") }}

This will give you access to Source with the included extensions: PlayerSource, ConsoleSource, EntitySource and GenericSource
