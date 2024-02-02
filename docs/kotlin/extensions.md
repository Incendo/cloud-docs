# cloud-kotlin-extensions

This module contains extensions to different parts of Cloud.

## Links

<div class="grid cards" markdown>

- [:fontawesome-brands-github: Source Code](https://github.com/Incendo/cloud)
- [:material-language-kotlin: Dokka](https://javadoc.io/doc/org.incendo/cloud-kotlin-extensions/latest)

</div>

## Installation

Cloud is available through [Maven Central](https://central.sonatype.com/artifact/org.incendo/cloud-kotlin-extensions).

{{ dependency_listing("kotlin-extensions", "core") }}

## MutableCommandBuilder

`MutableCommandBuilder` is a small DSL for `Command.Builder` which allows for the creation of commands
in more idiomatic Kotlin.

You can initiate the mutable command builder using `CommandManager#commandBuilder` or
create and register the command in one step by using `CommandManager#buildAndRegister`.

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

The `org.incendo.kotlin.extension` package contains extensions to:

- `CloudKeyContainer`
- `CloudKey`
- `Command.Builder`
- `CommandManager`
- `ExceptionController`
- `ArgumentParser`
- `ParserDescriptor`
- `Either`

The extensions exist to make it easier to use Cloud in Kotlin.
