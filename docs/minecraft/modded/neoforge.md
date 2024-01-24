## Installation

Cloud for NeoForge is available through [Maven Central](https://central.sonatype.com/artifact/cloud.commandframework/cloud-neoforge).

=== "Gradle (Kotlin, NeoGradle)"

    ```kotlin
    repositories {
      mavenCentral()
    }

    jarJar.enable()

    dependencies {
      val cloudFabric = "cloud.commandframework:cloud-neoforge:VERSION"
      implementation(cloudFabric)
      jarJar(cloudFabric)
    }
    ```

=== "Gradle (Groovy, NeoGradle)"

    ```groovy
    repositories {
      mavenCentral()
    }

    jarJar.enable()

    dependencies {
      def cloudFabric = 'cloud.commandframework:cloud-neoforge:VERSION'
      implementation(cloudFabric)
      jarJar(cloudFabric)
    }
    ```

=== "Gradle (Kotlin, Architectury Loom)"

    ```kotlin
    repositories {
      mavenCentral()
    }

    dependencies {
      val cloudFabric = "cloud.commandframework:cloud-neoforge:VERSION"
      modImplementation(cloudFabric)
      include(cloudFabric)
    }
    ```

=== "Gradle (Groovy, Architectury Loom)"

    ```groovy
    repositories {
      mavenCentral()
    }

    dependencies {
      def cloudFabric = 'cloud.commandframework:cloud-neoforge:VERSION'
      modImplementation(cloudFabric)
      include(cloudFabric)
    }
    ```

### Versions

See [here](./index.md#compatibility) for Minecraft version compatibility.

### `mods.toml`

Add the following to your `mods.toml`:

```toml
[[dependencies.your_mod_id]]
modId = "cloud"
type = "required"
versionRange = "[1.0,)"
ordering = "NONE"
side = "BOTH"
```
