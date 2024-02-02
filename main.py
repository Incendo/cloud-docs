def define_env(env):

  @env.macro
  def dependency_listing(name, version=None):
    if version is None:
      version = name

    return """
<!-- prettier-ignore -->
=== "Maven"

    ```xml
    <dependencies>
        <dependency>
            <groupId>org.incendo</groupId>
            <artifactId>cloud-{name}</artifactId>
            <version>{version}</version>
        </dependency>
    </dependencies>
    ```

=== "Gradle (Kotlin)"

    ```kotlin
    implementation("org.incendo:cloud-{name}:{version}")
    ```

=== "Gradle (Groovy)"

    ```groovy
    implementation 'org.incendo:cloud-{name}:{version}'
    ```
    """.format(name = name, version = env.variables.version[version])
