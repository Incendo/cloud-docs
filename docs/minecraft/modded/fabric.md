# cloud-fabric

## Installation

Cloud for Fabric is available through [Maven Central](https://central.sonatype.com/artifact/cloud.commandframework/cloud-fabric).

=== "Gradle (Kotlin)"

    ```kotlin
    repositories {
      mavenCentral()
    }

    dependencies {
      val cloudFabric = "cloud.commandframework:cloud-fabric:VERSION"
      modImplementation(cloudFabric)
      include(cloudFabric)
    }
    ```

=== "Gradle (Groovy)"

    ```groovy
    repositories {
      mavenCentral()
    }

    dependencies {
      def cloudFabric = 'cloud.commandframework:cloud-fabric:VERSION'
      modImplementation(cloudFabric)
      include(cloudFabric)
    }
    ```

### Versions

See [here](./index.md#compatibility) for Minecraft version compatibility.

### `fabric.mod.json`

Merge the following into your `fabric.mod.json`:

```json
{
  "depends": {
    "cloud": "*"
  }
}
```
