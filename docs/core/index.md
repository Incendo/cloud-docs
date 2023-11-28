# cloud-core

`cloud-core` contains the main cloud API.
Generally you'll want to depend on a platform module which implements Cloud for your specific platform, as
`cloud-core` does not have any platform-specific code.

## Installation

Cloud is available through [Maven Central](https://search.maven.org/search?q=cloud.commandframework).

<!-- prettier-ignore -->
=== "Maven"
    ```xml
    <dependency>
      <groupId>cloud.commandframework</groupId>
      <artifactId>cloud-core</artifactId>
      <version>dCLOUD_BASE_VERSIONd</version>
    </dependency>
    ```
=== "Gradle (Kotlin)"
    ```kotlin
    implementation("cloud.commandframework:cloud-core:dCLOUD_BASE_VERSIONd")
    ```
=== "Gradle (Groovy)"
    ```groovy
    implementation 'cloud.commandframework:cloud-core:dCLOUD_BASE_VERSIONd'
    ```

## Command

A command is a chain of components. Each unique chain makes up a unique command.

<!-- prettier-ignore -->
!!! example
    All of these are examples of unique commands:
    ```
    /foo bar one
    /foo bar two <arg1> -- Command with a required variable
    /bar [arg] -- Command with an optional variable
    ```

### Components

There are four different types of components:

1. Literals: A string literal with optional aliases.
2. Required variables: A variable component which gets parsed by a parser, that must be present.
3. Optional variables: A variable component which gets parsed by a parser, that must not necessarily be present.
   An optional component may have a default value.
4. Flags: Named components that are always optional. They may have an associated variable component.
   Examples: `--flag value`, `-abc`.

Cloud requires that the command chains are unambiguous.
This means that you may not have a required component following an optional component.
You may also not have two conflicting variable components on the same level, as it would not be clear
which of them gets to parse the input.
You may have _one_ variable component alongside literals, where the literals always get priority.

## Command Manager
