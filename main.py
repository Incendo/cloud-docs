def define_env(env):
    @env.macro
    def dependency_listing(name: str, version: str = None) -> str:
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
            link = link[1: len(link) - 1]

        return '[`{title}`](<{link}> "Click to open the JavaDoc")'.format(link=link, title=title)

    @env.macro
    def snippet(path: str, section: str = "snippet", title: str = None, indent = 0) -> str:
        if title is None:
            title = path
        if title:
            title = 'title="{title}"'.format(title = title)

        if section is not None:
            path = path + ":" + section

        return ''.join((' ' * indent) + line for line in """
```java {title}
--8<-- "{path}"
```
    """.format(path=path, title=title).splitlines(True))

    @env.macro
    def figure(path: str, caption: str) -> str:
        return ("<figure markdown>![{caption}]({path})<figcaption>{caption}</figcaption></figure>"
                .format(path = path, caption = caption))
