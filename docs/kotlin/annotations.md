# cloud-kotlin-coroutines-annotations

This module adds coroutine support to [cloud-annotations](../annotations/index.md), allowing you to make
the annotated command methods and suggestion providers suspending.

The module also adds the ability to use Kotlin default values in both suspending and non-suspending methods.

```kotlin title="Example of a suspending command method"
@Command("command [argument]")
suspend fun yourCommand(
    argument: String = "default value"
): Unit = withContext(Dispatchers.IO) {
  // ...
}
```

## Installation

Cloud is available through [Maven Central](https://search.maven.org/search?q=cloud.commandframework).

<!-- prettier-ignore -->
=== "Gradle (Kotlin)"

    ```kotlin
    implementation("cloud.commandframework:cloud-kotlin-coroutines-annotations:2.0.0-SNAPSHOT")
    ```

=== "Gradle (Groovy)"

    ```groovy
    implementation 'cloud.commandframework:cloud-kotlin-coroutines-annotations:2.0.0-SNAPSHOT'
    ```

You then need to install the `AnnotationParser` extension:

```kotlin
annotationParser.installCoroutineSupport()
```

You may override the default coroutine scope (`GlobalScope`) and context (`EmptyCoroutineContext`)
when invoking `installCoroutineSupport`.

## Suggestion Providers

The coroutine support extends the allowed signatures for `@Suggestions`-annotated methods, letting you return
sequences of `Suggestion` objects as well as strings.

```kotlin title="Example of a suspending suggestion provider"
@Suggestions("custom-suggestions")
suspend fun suggestionMethod(
    context: CommandContext<CommandSender>,
    input: String
): Sequence<Suggestion> = sequenceOf("a", "b", "c").map(Suggestion::simple)
```
