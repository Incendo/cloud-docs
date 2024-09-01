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
    def shade_dependency() -> str:
        return """
=== "Maven"
    
    ```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.6.0</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <relocations>
                                <relocation>
                                    <pattern>org.incendo.cloud</pattern>
                                    <shadedPattern>com.yourpackage.libs.cloud</shadedPattern>
                                </relocation>
                            </relocations>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
    ```

=== "Gradle (Kotlin)"

    ```kotlin
    plugins {
        id("com.gradleup.shadow") version "8.3.0"
    }

    tasks {
        assemble {
            dependsOn(shadowJar)
        }

        shadowJar {
            relocate("org.incendo.cloud", "com.yourpackage.libs.cloud")
        }
    }
    ```

=== "Gradle (Groovy)"

    ```groovy
    plugins {
        id 'com.gradleup.shadow' version '8.3.0'
    }

    tasks {
        assemble {
            dependsOn shadowJar
        }

        shadowJar {
            relocate 'org.incendo.cloud', 'com.yourpackage.libs.cloud'
        }
    }
    ```
"""

    @env.macro
    def javac_parameters() -> str:
        return """
=== "Maven"
    
    ```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <compilerArgs>
                        <arg>-parameters</arg>
                    </compilerArgs>
                </configuration>
            </plugin>

            <!-- For kotlin plugins -->
            <plugin>
                <groupId>org.jetbrains.kotlin</groupId>
                <artifactId>kotlin-maven-plugin</artifactId>
                <version>1.9.0</version> <!-- Replace with your Kotlin version -->
                <executions>
                    <execution>
                        <id>compile</id>
                        <goals>
                            <goal>compile</goal>
                        </goals>
                        <configuration>
                            <args>
                                <arg>-java-parameters</arg>
                            </args>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
    ```

=== "Gradle (Kotlin)"

    ```kotlin
    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-parameters")
    }

    // only needed if your project uses Kotlin
    tasks.withType<KotlinCompile> {
        compilerOptions {
            freeCompilerArgs.add("-java-parameters")
        }
    }
    ```

=== "Gradle (Groovy)"

    ```groovy
    tasks.withType(JavaCompile) {
        options.compilerArgs << "-parameters"
    }

    // only needed if your project uses Kotlin
    tasks.withType(KotlinCompile) {
        kotlinOptions {
            freeCompilerArgs << "-java-parameters"
        }
    }
    ```
"""

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
