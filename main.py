def define_env(env):
  @env.macro
  def dependency_listing(name: str, version: str= None) -> str:
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
    """.format(name=name, version=env.variables.version[version])

  @env.macro
  def javadoc(link: str, title: str = None) -> str:
    if title is None:
      split = link.split("/")
      title = split[len(split) - 1].replace(".html", "")
    if link.startswith("<"):
      link = link[1 : len(link) - 1]

    return '[`{title}`](<{link}> "Click to open the JavaDoc")'.format(link=link, title=title)
