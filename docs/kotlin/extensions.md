# cloud-kotlin-extensions

This module contains extensions to different parts of Cloud.

## Installation

Cloud is available through [Maven Central](https://central.sonatype.com/artifact/cloud.commandframework/cloud-kotlin-extensions).

<!-- prettier-ignore -->
=== "Gradle (Kotlin)"

    ```kotlin
    implementation("cloud.commandframework:cloud-kotlin-extensions:2.0.0-SNAPSHOT")
    ```

=== "Gradle (Groovy)"

    ```groovy
    implementation 'cloud.commandframework:cloud-kotlin-extensions:2.0.0-SNAPSHOT'
    ```

## MutableCommandBuilder

`MutableCommandBuilder` is a small DSL for `Command.Builder` which allows for the creation of commands
in more idiomatic Kotlin.

You can initiate the mutable command builder using `CommandManager.commandBuilder` or
create and register the command in one step by using `CommandManager.buildAndRegister`.

```kotlin title="Example MutableCommandBuilder usage"
manager.buildAndRegister("command") {
    senderType<OverriddenSenderType>()

    required("string", stringParser()) {
        description(argumentDescription("A string argument"))
    }

    handler { ctx ->
        // ...
    }
}
```

## Extension Functions

The `cloud.commandframework.kotlin.extension` package contains extensions to:

- `CloudKeyContainer`
- `CloudKey`
- `Command.Builder`
- `CommandManager`
- `ExceptionController`
- `ArgumentParser`

The extensions exist to make it easier to use Cloud in Kotlin.
