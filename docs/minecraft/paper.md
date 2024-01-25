# cloud-paper

`cloud-paper` is an extension of `cloud-bukkit` with additional support for
[Paper](https://papermc.io/software/paper)-based platforms. `cloud-paper` maintains support for all platforms supported
by `cloud-bukkit`, and therefore is the recommended dependency to use Cloud on any Bukkit-based platform.
The following documentation is written with the assumption that you have already read and understand the
[`cloud-bukkit` documentation](bukkit.md).

## Links

<div class="grid cards" markdown>

- [:fontawesome-brands-github: Source Code](https://github.com/Incendo/cloud-minecraft/tree/master/cloud-paper)
- [:fontawesome-brands-github: Example Plugin](https://github.com/Incendo/cloud-minecraft/tree/master/examples/example-bukkit)

</div>

## Installation

Cloud for Paper is available through [Maven Central](https://central.sonatype.com/artifact/org.incendo/cloud-paper).

<!-- prettier-ignore -->
=== "Maven"

    ```xml
    <dependencies>
        <dependency>
            <groupId>org.incendo</groupId>
            <artifactId>cloud-paper</artifactId>
            <version>2.0.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
    ```

=== "Gradle (Kotlin)"

    ```kotlin
    implementation("org.incendo:cloud-paper:2.0.0-SNAPSHOT")
    ```

=== "Gradle (Groovy)"

    ```groovy
    implementation 'org.incendo:cloud-paper:2.0.0-SNAPSHOT'
    ```

## Usage

`cloud-paper` has a command manager implementation called `PaperCommandManager` that can be created in two ways.

With a custom sender type:

```java
PaperCommandManager<YourSenderType> commandManager = new PaperCommandManager<>(
  yourPlugin, /* 1 */
  executionCoordinator, /* 2 */
  senderMapper /* 3 */
);
```

Or, using Bukkit's [`CommandSender`](https://jd.papermc.io/paper/1.20/org/bukkit/command/CommandSender.html):

```java
PaperCommandManager<CommandSender> commandManager = PaperCommandManager.createNative(
  yourPlugin, /* 1 */
  executionCoordinator /* 2 */
);
```

1. You need to pass an instance of the plugin that is constructing the command manager. This is used to register
   the commands and the different event listeners.
2. Information about execution coordinators in general can be found
   [here](../core/index.md#execution-coordinators). See [below](#execution-coordinators) for info specific to
   Bukkit-based platforms.
3. The sender mapper is a two-way mapping between Bukkit's
   [`CommandSender`](https://jd.papermc.io/paper/1.20/org/bukkit/command/CommandSender.html) and your custom sender type.
   Using `SenderMapper.identity()` is equivalent to the `createNative` static factory method.

## Brigadier

Paper exposes [Brigadier](https://github.com/mojang/brigadier), which means that you may use the features
from [cloud-brigadier](brigadier.md) on Paper servers.

You may enable Brigadier mappings using `PaperCommandManager#registerBrigadier()`. You should make use of the
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

You may enable asynchronous completions using `PaperCommandManager#registerAsynchronousCompletions()`.
You should make use of the capability system to make sure that this is available on the server your plugin is running on:

```java
if (commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
  commandManager.registerAsynchronousCompletions();
}
```

## Execution coordinators

Due to Bukkit blocking the main thread for suggestion requests, it's potentially unsafe to use anything other than
`ExecutionCoordinator.nonSchedulingExecutor()` for
`ExecutionCoordinator.Builder#suggestionsExecutor(Executor)`. Once the coordinator, a suggestion provider, parser,
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
