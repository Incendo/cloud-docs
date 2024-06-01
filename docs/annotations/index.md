# cloud-annotations

The annotations module offers a different way of creating commands, parsers, suggestion providers and exception
handlers by using annotated methods.

The module can also function as an [annotation processor](#annotation-processing) which has some added benefits.

There are extensions to `cloud-annotations` for Kotlin, more information [here](../kotlin/annotations.md).

Examples can be found on
[GitHub](https://github.com/Incendo/cloud-minecraft/tree/master/examples/example-bukkit/src/main/java/org/incendo/cloud/examples/bukkit/annotations)

## Links

<div class="grid cards" markdown>

- [:fontawesome-brands-github: Source Code](https://github.com/Incendo/cloud)
- [:fontawesome-brands-java: JavaDoc](https://javadoc.io/doc/org.incendo/cloud-annotations/latest)

</div>

## Installation

Cloud Annotations is available through [Maven Central](https://central.sonatype.com/artifact/org.incendo/cloud-annotations).

{{ dependency_listing("annotations", "core") }}

You then need to create an
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-annotations/latest/org/incendo/cloud/annotations/AnnotationParser.html", "AnnotationParser") }}
instance.
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

To parse & register the different annotated methods you simply invoke
{{ javadoc("<https://javadoc.io/doc/org.incendo/cloud-annotations/latest/org/incendo/cloud/annotations/AnnotationParser.html#parse(T)>", "AnnotationParser#parse(Object)") }}
with an instance of the class that you wish to parse.

## Command Methods

![Example Annotations](https://github.com/Incendo/cloud/blob/master/img/code/annotations_java_dark.png?raw=true#only-dark){ loading = lazy }
![Example Annotations](https://github.com/Incendo/cloud/blob/master/img/code/annotations_java_light.png?raw=true#only-light){ loading = lazy }

Command methods are annotated methods that are used to construct and handle commands.
The method has to be annotated with a
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-annotations/latest/org/incendo/cloud/annotations/Command.html", "@Command") }}
annotation that specifies the command syntax.
The parsed command components are mapped to the method parameters.
The parameters may also be mapped to [injected](#injections) values, such as the command sender instance,
the
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/context/CommandContext.html", "CommandContext") }}
or custom injections.

The annotation may be repeated in order to generate multiple commands from the same method.

The command method may return {{ javadoc("https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/CompletableFuture.html", "CompletableFuture<Void>") }}
in which case the execution coordinator will wait for the returned future to complete:

```java
@Command("command")
public CompletableFuture<Void> command() {
  return CompletableFuture.supplyAsync(() -> null);
}
```

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

{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-annotations/latest/org/incendo/cloud/annotations/Argument.html", "@Argument") }}
annotations on method parameters is used to map the method parameter to the corresponding syntax fragment.

If you compile with the `-parameters` compiler option then you do not need to specify the name in the annotation, and
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

[Default values](../core/index.md#optional) can be specified using the
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-annotations/latest/org/incendo/cloud/annotations/Default.html", "@Default") }}
annotation. You may choose to either supply a value that will get parsed, or refer to a named default-providing method.

```java
// Parsed:
@Default("foo") @Argument String string

// Referencing a method:
@Default(name = "method") @Argument String string
```

Methods annotated with {{ javadoc("https://javadoc.io/doc/org.incendo/cloud-annotations/latest/org/incendo/cloud/annotations/Default.html", "@Default") }}
will get parsed by the annotation parser. The only accepted method parameter is
{{ javadoc("https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/reflect/Parameter.html", "Parameter") }}, which
refers to the parameter that the default value is being generated for.
The method must return an instance of {{ javadoc("https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/component/DefaultValue.html", "DefaultValue") }}.
You may choose to specify a name. If you do not specify a name, the method name will be used as the name of the provider.

```java title="Creating a default-providing method"
@Default(name = "method") // Could also be @Default without an explicit name!
public DefaultValue<YourType> method(Parameter parameter) {
  return DefaultValue.dynamic(context -> /* your logic */);
}
```

<!-- TODO(City): Refer to manual registration once the JavaDoc are available... -->

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

Flags can be generated by using the
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-annotations/latest/org/incendo/cloud/annotations/Flag.html", "@Flag") }}
annotation. Similarly to
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-annotations/latest/org/incendo/cloud/annotations/Argument.html", "@Argument") }},
this annotation can be used to specify suggestion providers, parsers, etc.

If a boolean parameter is annotated with
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-annotations/latest/org/incendo/cloud/annotations/Flag.html", "@Flag") }}
then it will generate a presence flag.
Otherwise, it will become a value flag with the parameter type as the value type.

Flags should _not_ be annotated with
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-annotations/latest/org/incendo/cloud/annotations/Argument.html", "@Argument") }}
and should not present in the
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-annotations/latest/org/incendo/cloud/annotations/Command.html", "@Command") }}
syntax.

### Descriptions

{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-annotations/latest/org/incendo/cloud/annotations/CommandDescription.html", "@CommandDescription") }}
can be added to an annotated command method to set the command description.

You can override how the descriptions are mapped by setting replacing the description mapper:

```java
annotationParser.descriptionMapper(string -> Description.of("blablabla " + string));
```

### Permissions

{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-annotations/latest/org/incendo/cloud/annotations/Permission.html", "@Permission") }}
can be added to a command method to set the command permission.

```java
// Simple string permission.
@Permission("the.permission")

// Compound permissions are also supported.
// - Equivalent to Permission.anyOf:
@Permission(value = { "permission.1", "permission.2" }, mode = Permission.Mode.ANY_OF)
// - Equivalent to Permission.allOf:
@Permission(value = { "permission.1", "permission.2" }, mode = Permission.Mode.ALL_OF)
```

You may use a [builder modifier](#builder-modifiers) to do more complex mappings.

### Proxies

{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-annotations/latest/org/incendo/cloud/annotations/ProxiedBy.html", "@ProxiedBy") }}
can be used to generate a command proxy.
In most cases it is recommended to use multiple
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-annotations/latest/org/incendo/cloud/annotations/Command.html", "@Command") }}
annotations instead as it allows for better control
over the generated command.

## Parsers

You may create [parsers](../core/index.md#parsers) from annotated methods by using the
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-annotations/latest/org/incendo/cloud/annotations/parser/Parser.html", "@Parser") }}
annotation.
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

Exceptions will be wrapped in
{{ javadoc("<https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/parser/ArgumentParseResult.html#failure()>", "ArgumentParseResult.failure") }}.

## Suggestion Providers

You may create [suggestion providers](../core/index.md#suggestions) from annotated methods by using the
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-annotations/latest/org/incendo/cloud/annotations/suggestion/Suggestions.html", "@Suggestions") }}
annotation.

The signature of the suggestion methods is quite flexible, and you may use [injected values](#injections).
The return type can be an iterable (or stream) of suggestion objects, or strings. You can find more
information in the [JavaDoc](https://javadoc.io/doc/org.incendo/cloud-annotations/latest/org/incendo/cloud/annotations/suggestion/Suggestions.html).

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
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-annotations/latest/org/incendo/cloud/annotations/exception/ExceptionHandler.html", "@ExceptionHandler") }}
annotation. You must specify which exception you want to handle.

The method parameter can be any [injected](#injections) value, the command sender,
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/context/CommandContext.html", "CommandContext") }},
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/exception/handling/ExceptionContext.html", "ExceptionContext") }},
or the exception type specified in the annotation.

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

Injected values are retrieved from the
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/injection/ParameterInjectorRegistry.html", "ParameterInjectorRegistry") }}
using injector services.
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
[GuiceInjectionService](https://github.com/Incendo/cloud/blob/master/cloud-core/src/main/java/org/incendo/cloud/injection/GuiceInjectionService.java).
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

For more information see the [JavaDoc](https://javadoc.io/doc/org.incendo/cloud-annotations/latest/org/incendo/cloud/annotations/processing/CommandContainer.html).
