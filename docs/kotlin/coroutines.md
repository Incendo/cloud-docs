# cloud-kotlin-coroutines

This module adds coroutine support to commands using builders.
For suspending commands methods, see [cloud-kotlin-coroutines-annotations](./annotations.md).

## Installation

Cloud is available through [Maven Central](https://central.sonatype.com/artifact/org.incendo/cloud-kotlin-coroutines).

<!-- prettier-ignore -->
=== "Gradle (Kotlin)"

    ```kotlin
    implementation("org.incendo:cloud-kotlin-coroutines:2.0.0-SNAPSHOT")
    ```

=== "Gradle (Groovy)"

    ```groovy
    implementation 'org.incendo:cloud-kotlin-coroutines:2.0.0-SNAPSHOT'
    ```

## Suspending command execution handlers

You may create a suspending command execution handler by implementing `SuspendingExecutionHandler`.
The `SuspendingExecutionHandler` interface does not extend `CommandExecutionHandler`, but you can convert
it to a `CommandExecutionHandler` by using `SuspendingExecutionHandler#asCommandExecutionHandler`.

There are extensions for both `Command.Builder` and `MutableCommandBuilder` that allow you to make direct
use of suspending execution handlers.

```kotlin title="Example"
manager.commandBuilder("command")
    .suspendingHandler { context ->
        // ...
    }
```

## Suspending parsers

You may create a suspending argument parser by implementing `SuspendingArgumentParser`.
The `SuspendingArgumentParser` interface does not extend `ArgumentParser`, but you can convert
it to an `ArgumentParser` by using `SuspendingArgumentParser#asArgumentParser`.

You may also create a suspending argument parser by making use of the global factory function:

```kotlin title="Creating a suspending parser"
suspendingArgumentParser<CommandSender, Int> { ctx, input ->
    ArgumentParseResult.success(input.readInteger())
}
```

## Suspending suggestion providers

You may create a suspending suggestion provider by implementing `SuspendingSuggestionProvider`.
The `SuspendingSuggestionProvider` interface does not extend `SuggestionProvider`, but you can convert
it to a `SuggestionProvider` by using `SuspendingSuggestionProvider#asSuggestionProvider`.

You may also create a suspending suggestion provider by making use of the global factory function:

```kotlin title="Creating a suspending suggestion provider"
suspendingSuggestionProvider<CommandSender> { ctx, input ->
    (1..3).asSequence()
        .map(Number::toString)
        .map(Suggestion::simple)
        .asIterable()
}
```
