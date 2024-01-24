# cloud-annotations

The annotations module offers a different way of creating commands, parsers, suggestion providers and exception
handlers by using annotated methods.

The module can also function as an [annotation processor](#annotation-processing) which has some added benefits.

There are extensions to `cloud-annotations` for Kotlin, more information [here](../kotlin/annotations.md).

Examples can be found on
[GitHub](https://github.com/Incendo/cloud-minecraft/tree/master/examples/example-bukkit/src/main/java/cloud/commandframework/examples/bukkit/annotations)

## Installation

Cloud Annotations is available through [Maven Central](https://central.sonatype.com/artifact/org.incendo/cloud-annotations).

<!-- prettier-ignore -->
=== "Maven"

    ```xml
    <dependencies>
        <dependency>
            <groupId>org.incendo</groupId>
            <artifactId>cloud-annotations</artifactId>
            <version>2.0.0-SNAPSHOT</version>
        </dependency>
    </dependencies>

    <!-- Optional -->
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.incendo</groupId>
                            <artifactId>cloud-annotations</artifactId>
                            <version>2.0.0-SNAPSHOT</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
        </plugins>
    </build>
    ```

=== "Gradle (Kotlin)"

    ```kotlin
    implementation("org.incendo:cloud-annotations:2.0.0-SNAPSHOT")
    // Optional:
    annotationProcessor("org.incendo:cloud-annotations:2.0.0-SNAPSHOT")
    ```

=== "Gradle (Groovy)"

    ```groovy
    implementation 'org.incendo:cloud-annotations:2.0.0-SNAPSHOT'
    // Optional:
    annotationProcessor 'org.incendo:cloud-annotations:2.0.0-SNAPSHOT'
    ```

You then need to create an `AnnotationParser` instance.
When creating the annotation parser you can supply an optional
function that maps parser parameters to [command meta](../core/index.md#command-meta), these
parameters can be set using [annotation mappers](#annotation-mappers) and allow you to map annotations
to meta values.

```java title="Creating an annotation parser"
// Parser without a CommandMeta mapper.
AnnotationParser<C> annotationParser = new AnnotationParser(commandManager);

// Parser with a CommandMeta mapper.
AnnotationParser<C> annotationParser = new AnnotationParser(
    commandManager,
    parameters -> CommandMeta.empty()
);
```

To parse & register the different annotated methods you simply invoke `AnnotationParser.parse` with an
instance of the class that you wish to parse.

## Command Methods

![Example Annotations](https://github.com/Incendo/cloud/blob/master/img/code/annotations_java_dark.png?raw=true#only-dark){ loading = lazy }
![Example Annotations](https://github.com/Incendo/cloud/blob/master/img/code/annotations_java_light.png?raw=true#only-light){ loading = lazy }

Command methods are annotated methods that are used to construct and handle commands.
The method has to be annotated with a `@Command` annotation that specifies the command
syntax.
The parsed command components are mapped to the method parameters.
The parameters may also be mapped to [injected](#injections) values, such as the command sender instance,
the `CommandContext` or custom injections.

The annotation may be repeated in order to generate multiple commands from the same method.

### Syntax

There are three different parts that make up the command syntax:

- **Literals**: `literal`, `literal|alias`
- **Required variable components**: `<variable>`
- **Optional variable components**: `[variable]`

Examples:

- `@Command("command <string> [int]")`
- `@Command("command <string> literal|alias [int]")`

The ordering of the method parameters does not matter, the command structure is entirely determined from the
syntax string.

The types of the variable components are determined from the method parameters.

### Command Components

`@Argument` annotations on method parameters is used to map the method parameter to the corresponding syntax fragment.

If you compile with the `-parameters` compiler option then you do not need to specify the name in the annotation and
it will instead be inferred from the parameter name (though you can override it if you want to).
You may also choose not to add the annotation at all.

The argument annotation also allows you to specify non-default parsers and suggestion providers.
You may specify the argument description through the annotation as well.

```java
@Command("command <required> [optional]")
public void yourCommand(
  @Argument(value = "required", description = "A string") String string, // Uses a name override!
  @Nullable String optional // Name is inferred, and so is @Argument!
) {
  // ...
}
```

#### Default values

[Default values](../core/index.md#optional) can be specified using the `@DefaultValue` annotation.
These values will always be parsed:

```java
@DefaultValue("foo") @Argument String string
```

#### Either

[Either](../core/index.md#either) may be used as a parameter type. Cloud will use the generic parameters to
determine which parsers to use.

```java
@Command("command <either>")
public void yourCommand(Either<Integer, Boolean> either) {
  // ...
}
```

### Flags

Flags can be generated by using the `@Flag` annotation.
Similarly to `@Argument`, this annotation can be used to specify suggestion providers, parsers, etc.

If a boolean parameter is annotated with `@Flag` then it will generate a presence flag.
Otherwise, it will become a value flag with the parameter type as the value type.

Flags should _not_ be annotated with `@Argument` and should not present in the `@Command` syntax.

### Descriptions

`@Command` can be added to an annotated command method to set the command description.

You can override how the descriptions are mapped by setting replacing the description mapper:

```java
annotationParser.descriptionMapper(string -> Description.of("blablabla " + string));
```

### Permissions

`@Permission` can be added to a command method to set the command permission.
Only simple string-permissions can be used.
You may use a [builder modifier](#builder-modifiers) to do more complex mappings.

### Proxies

`@ProxiedBy` can be used to generate a command proxy.
In most cases it is recommended to use multiple `@Command` annotations instead as it allows for better control
over the generated command.

## Parsers

You may create [parsers](../core/index.md#parsers) from annotated methods by using the `@Parser` annotation.
If no value is passed to the annotation then the parser will become the default parser for the method return type.
You may also pass a suggestion provider name to the annotation to bind the parser to a specific suggestion provider.

The signature of the method must be exactly:

```java
// Named parser: @Parser("parserName")
@Parser
public YourParsedType parserName(CommandContext<C> context, CommandInput input) {
  // ...
}
```

Exceptions will be wrapped in `ArgumentParseResult.failure`.

## Suggestion Providers

You may create [suggestion providers](../core/index.md#suggestions) from annotated methods by using the `@Suggestions`
annotation.

The parameters of the method must be `CommandContext<C> context, String input`
or `CommandContext<C> context, CommandInput input` but the return type can be an iterable
(or stream) of suggestion objects, or strings.

```java title="Example signatures"
@Suggestions("name")
public List<String> suggestions(CommandContext<C> context, CommandInput input) { /* ... */ }

@Suggestions("name")
public Stream<String> suggestions(CommandContext<C> context, String input) { /* ... */ }

@Suggestions("name")
public Set<Suggestion> suggestions(CommandContext<C> context, CommandInput input) { /* ... */ }

@Suggestions("name")
public Iterable<String> suggestions(CommandContext<C> context, String input) { /* ... */ }
```

## Exception Handlers

You may create [exception handlers](../core/index.md#exception-handling) from annotated methods by using the
`@ExceptionHandler` annotation. You must specify which exception you want to handle.

The method parameter can be any [injected](#injections) value, the command sender, `CommandContext`,
`ExceptionContext` or the exception type specified in the annotation.

```java title="Example exception handler"
@ExceptionHandler(CutenessException.class)
public void handleCutenessOverload(CommandSender sender) {
    sender.sendMessage("You are too cute!");
}
```

## Injections

Command methods may have parameters that are not mapped to command components.
Common examples are the command sender objects as well as the command context.
These values are referred to as _injected values_.

Injected values are retrieved from the `ParameterInjectorRegistry` using injector services.
You may register parameter injectors to the default service, or register your own injection service
that hooks into an external dependency injection system.

The injectors get access to the annotations of the parameter that is being injected, as well as the command context.

```java title="Example injector"
manager.parameterInjectorRegistry().registerInjector(
  TypeToken.get(String.class),
  (context, annotations) -> annotations.annotation(Blueberry.class) == null
        ? "raspberry"
        : "blueberry"
);
```

Cloud has an injection service implementation for Guice:
[GuiceInjectionService](https://github.com/Incendo/cloud/blob/master/cloud-core/src/main/java/cloud/commandframework/annotations/injection/GuiceInjectionService.java).
You may register injection services to the parameter registry using

```java
manager.parameterInjectionRegistry().registerInjectionService(theService);
```

All injection services will be invoked until one returns a non-null result.

## Customization

### Builder Decorators

Builder decorators are used to modify _all_ command builders before the arguments are added.
They allow you to configure default permissions, descriptions, etc.

```java title="Example builder decorators"
annotationParser.registerBuilderDecorator(
    BuilderDecorator.defaultDescription(commandDescription("Default description"))
);
annotationParser.registerBuilderDecorator(
    builder -> builder.meta("wee", "woo")
);
```

### Builder Modifiers

Builder modifiers are used to modify the command builders using annotations.
They act on the command builders after all command components have been generated.
This allows for modifications of the builder instance before it's registered to the command manager.

```java title="Example builder modifier"
annotationParser.registerBuilderModifier(
    YourAnnotation.class,
    (yourAnnotation, builder) -> builder.meta("wee", "woo")
);
```

### Annotation Mappers

Annotation mappers are used to map custom annotations to parser parameters.
This allows for the use of annotations to customize the component parsers.

```java title="Example annotation mapper"
annotationParser.registerAnnotationMapper(
    MinValue.class,
    minValue -> ParserParameters.single(
        StandardParameters.RANGE_MIN,
        minValue.value()
    )
);
```

### Pre-processor mappers

It is possible to register annotations that will bind a given argument pre-processor to an annotated parameter.

```java title="Example preprocessor binding"
annotationParser.registerPreprocessorMapper(
    YourAnnotation.class,
    yourAnnotation -> yourPreprocessor
);
```

## Annotation Processing

If `cloud-annotations` is registered as an annotation processor then it will perform compile-time validation
of `@Command`-annotated methods.

### Command Containers

When using `cloud-annotations` as an annotation processor it is possible to make use of command containers.

For more information see the [JavaDoc](TODO).
