# Cloud

<!-- prettier-ignore -->
!!! note
    These are the docs for Cloud v2, which is a major rewrite of Cloud. Cloud v2 is currently only available
    as snapshots. You may find the legacy documentation [here](https://github.com/Incendo/cloud/tree/1.9.0-dev/docs).

Cloud is a JVM framework for creating user commands.
A command is a chain of parsed arguments and a handler that gets invoked with the parsed values.
Cloud is not made to be used in any specific software, but it has modules with support for Minecraft
(Bukkit/Paper, BungeeCord, Velocity, Fabric, CloudBurst, Sponge & NeoForge),
Discord (Discord4J, JavaCord, JDA4, JDA5 & Kord), IRC (PIrcBotX) and Spring Shell.

Cloud allows you to write commands either using builders or annotated methods, and has special support for Kotlin.
Cloud allows you to customize the command execution pipeline by injecting custom behavior along the entire
execution path.

This document does not aim to cover every single detail of Cloud, but will instead introduce you to various different
concepts and explain how they can be used in your software
For technical details, we ask you to look at the [JavaDoc](https://javadocs.dev/cloud.commandframework).

We have a set of examples that introduce some useful Cloud concepts.
They are written for the Bukkit Minecraft API but the examples are not specific to Minecraft:
[example-bukkit](https://github.com/Incendo/cloud-minecraft/tree/master/examples/example-bukkit).

## Structure

The documentation is split into different sections for the different Cloud modules.
It is highly recommended that you get started with the [cloud-core](./core/index.md) docs before you delve into the
platform-specific docs.
If you want to use annotated command methods, then you should start out with [cloud-core](./core/index.md) and then
move over to [cloud-annotations](./annotations/index.md).

## Development Builds

Development builds of Cloud are available on the Sonatype Snapshots Repository:

<!-- prettier-ignore -->
=== "Maven"

    ```xml
    <repository>
      <id>sonatype-snapshots</id>
      <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
    </repository>
    ```

=== "Gradle (Kotlin)"

    ```kotlin
    maven("https://oss.sonatype.org/content/repositories/snapshots/") {
      name = "sonatype-snapshots"
      mavenContent {
        snapshotsOnly()
      }
    }
    ```

=== "Gradle (Groovy)"

    ```groovy
    maven {
      url "https://oss.sonatype.org/content/repositories/snapshots/"
      name "sonatype-snapshots"
      mavenContent {
        snapshotsOnly()
      }
    }
    ```
