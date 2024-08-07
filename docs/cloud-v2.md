# Cloud 2

Cloud 2 is a major release of Cloud with significant changes. Cloud 2 is _not_ compatible with Cloud 1.

## Changelog

### Core

**Move argument requirement out of CommandArgument ([#489](https://github.com/Incendo/cloud/pull/489)) - Proposal: [#504](https://github.com/Incendo/cloud/pull/504)**
Before, you'd indicate that an argument was required when constructing the argument (`YourArgument.of()` vs `YourArgument.optional()`).

This information is no longer stored in the argument itself, nor is the default value. This has been moved into the command components, and you indicate whether a command is required or optional in the command builder, ex: `builder.required(IntegerArgument.of("name"))` or `builder.optional(IntegerArgument.of("name"))`.

**Complex suggestions ([#490](https://github.com/Incendo/cloud/pull/490))**
Suggestions are no longer just strings. The suggestions now implement a `Suggestion` interface, which will allow us to add more complex suggestions, such as suggestions with tooltips. The various different methods that handle suggestions have been updated to no longer return strings. The parsers can still return string suggestions by implement `stringSuggestions()` instead of `suggestions()`.

**Command beans ([#494](https://github.com/Incendo/cloud/pull/494))**
Command beans offer an alternative way of creating commands by implementing single-command classes in a DI-friendly manner.

**Refactoring/Clean-up ([#497](https://github.com/Incendo/cloud/pull/497))**
Various components have been refactored to be easier to work with. In particular, the command tree is now a fair bit less complex and also far more documented.

**Dynamic default values ([#505](https://github.com/Incendo/cloud/pull/505)) - Proposal: [#500](https://github.com/Incendo/cloud/pull/500)**
In Cloud v1, the default values are stored in the arguments, and the argument builder has the ability to serialize objects into strings. These values are then pushed onto the input queue when parsing. In Cloud v2, the default values are stored in the command components instead of the arguments. This means they bypass the argument entirely, and we thus lose the ability to pre-serialize these objects. The simple string default values have thus been replaced with objects instead.

We offer three different types:

- constant: `DefaultValue.constant(5)`, `DefaultValue.constant(Duration.ofSeconds(5L))`
- dynamic: `DefaultValue.dynamic(ctx -> ctx.sender().getLocation())`
- parsed: `DefaultValue.parsed("5")`, `DefaultValue.parsed("@p")`

Constant/Dynamic default values bypass the optional argument parser entirely, whereas parsed still pushes the default value onto the input.

**Replace input queue with an input reader ([#498](https://github.com/Incendo/cloud/pull/498))**
The old `Queue<String>` which was sent to the parsers has been replaced with `CommandInput`, which is a structure that wraps a string. The command input allows us to move a cursor along the input string, and gives us more control over how we consume the input.

**Removal of deprecated methods and classes ([#515](https://github.com/Incendo/cloud/pull/515))**
Many deprecated methods and classes have been removed. Before migrating to Cloud v2, make sure you're not using deprecated methods, in order to make your life a little easier.

**Replace CommandArgument with CommandComponent**
`CommandComponent` has existed for a while, but in 2.0 all the information previously contained in `CommandArgument` has been moved over to the components. There are several reasons for this, with the two main ones being less boilerplating when creating new parsers and also to lessen the differences between the builder and annotation patterns.

Where you'd previously supply a command argument, you now supply a name and a parser (or a component). The standard parsers are no longer contained within arguments, but are instead standalone.

**Type-safe required senders ([#518](https://github.com/Incendo/cloud/pull/518))**
When specifying a command sender type in the command builder, a builder targeting the given command sender type will be returned.

**Allow parsers and suggestion handlers to return futures ([#521](https://github.com/Incendo/cloud/pull/521))**
The command tree has been rewritten to work with completable futures, and the argument parser and suggestion provider interfaces now have future-returning methods. This has been done to allow parsers to return futures with specific executors (for example, to parse on the main Minecraft thread).

**Aggregate Parsers ([#522](https://github.com/Incendo/cloud/pull/522))**
Aggregate parsers have been introduced as a better version of the compound parsers. You may either implement the aggregate parser interface, or construct an aggregate parser using a builder. The aggregate parsers support suggestion provider overrides for the inner components, which was not possible with the compound parsers.

**CommandDescription ([#530](https://github.com/Incendo/cloud/pull/530))**
Commands now have descriptions that live outside the command meta. This is different from the description of the root literal of a command. The new `CommandDescription` class replaces the old `DESCRIPTION` and `LONG_DESCRIPTION` meta values.

**ArgumentDescription -> Description ([#531](https://github.com/Incendo/cloud/pull/531))**
`ArgumentDescription`has been renamed to `Description`, and the old `Description` has been renamed to `DescriptionImpl`. We no longer have an argument concept, and the descriptions are used in both commands and command components.

**Exception handling ([#536](https://github.com/Incendo/cloud/pull/536))**
The way exceptions are handled has been rewritten to be more flexible and robust. You may now register multiple exception handlers for the same exception type, and also exception handlers for supertypes of the exceptions. There are new utility functions for generating exception handlers that unwrap the exception cause as well.

**Clean up CommandMeta, CommandContext and CloudKey ([#548](https://github.com/Incendo/cloud/pull/548), [#549](https://github.com/Incendo/cloud/pull/549))**
Methods have been moved around, new static factory methods have been created and methods have been renamed to make these classes easier to use.

**Refactor the help system ([#543](https://github.com/Incendo/cloud/pull/543))**
It is now possible to replace the implementation of the help handler. The result implementations are generated using immutable classes, and can now be customized as well. There's a new `HelpRenderer` concept for displaying the results to senders.

**Caption refactoring ([#550](https://github.com/Incendo/cloud/pull/550))**
The caption system has been refactored to make it easier to expand. There's a new `CaptionFormatter` which can be used to allow external tools (such as Adventure) to format the captions.

**Clean up suggestion hierarchy & make parsers not implement `SuggestionProvider by default` ([#567](https://github.com/Incendo/cloud/pull/567))**
Parsers no longer implement `SuggestionProvider` by default, but they do implement `SuggestionProviderHolder`. The parser may implement one of the parser interfaces and they will automatically function like suggestion providers.

**Make suggestion processors act on individual suggestions ([#572](https://github.com/Incendo/cloud/pull/572))**
The suggestion processors get to act on the suggestions as soon as they're stored in the suggestion context, and they no longer need unwrap the incoming list and collect before returning.

**Suggestion provider return iterables ([#570](https://github.com/Incendo/cloud/pull/570))**
Suggestion providers now return iterables rather than lists.

**Permission refactoring ([#578](https://github.com/Incendo/cloud/pull/578))**
`CommandPermission` was renamed to `Permission` and the old permission implementation is no longer exposed as API. The static factory methods were moved and renamed: `AndPermission.of` -> `Permission.allOf`, `OrPermission.of` -> `Permission.anyOf`.

**Remove ArgumentParser#isContextFree ([#578](https://github.com/Incendo/cloud/pull/578))**
No longer used.

**Refactor injection services ([#580](https://github.com/Incendo/cloud/pull/580))**
Use an immutable interface as context rather than a generic triplet. Allow for injections using type tokens. This
also adds support for qualified Guice injection.

**Extract command execution from CommandManager ([#581](https://github.com/Incendo/cloud/pull/581))**
Command execution is now handled by a `CommandExecutor` which can be retrieved from the command manager.

**Make suggestion providers accept CommandInput ([#594](https://github.com/Incendo/cloud/pull/594))**
This allows suggestion providers to consume more than a single token at a time, and also gives them access to whitespace
information. This allows for better multi-token suggestions.

**Simplify execution coordination API ([#609](https://github.com/Incendo/cloud/pull/609))**
It is easier to create & modify execution coordinators.

**Make ParserRegistry easier to use ((#592)[https://github.com/Incendo/cloud/pull/592])**
You may now register `ParserDescriptor` instances.

**Move confirmation system to external repository ([#620](https://github.com/Incendo/cloud/pull/620))**
The confirmation system now lives in [cloud-processors](https://github.com/Incendo/cloud-processors).

**Add Either ([#647](https://github.com/Incendo/cloud/pull/647))**
`Either<A, B>` can be parsed by `ArgumentParser.firstOf(ParserDescriptor, ParserDescriptor)` which allows
for multiple variable components on the same level.

**Refactor numerical parsing ([#649](https://github.com/Incendo/cloud/pull/649))**
The numerical parsers now extend `NumberParser`. Ranges have been moved into a `Range` object.

**Move standard captions to a provider ([#658](https://github.com/Incendo/cloud/pull/658/files))**
The standard caption values are now managed by a caption provider.

**Package cleanup ([#659](https://github.com/Incendo/cloud/pull/659), [#660](https://github.com/Incendo/cloud/pull/660))**
We've moved around and standardized the package names in `cloud-core`. The namespace of the project has been
changed from `cloud.commandframework` to `org.incendo`.

**Use captions in the default exception handlers ((#671)[https://github.com/Incendo/cloud/issues/671])**
This allows for the exception handlers to be translated in the same way as any other caption,
without requiring the entire exception handler to be replaced.

**Flag building cleanup ([#708](https://github.com/Incendo/cloud/pull/708))**
The process of building command flags has been improved.

### Annotations

**Lenient `@Suggestions` methods ([#496](https://github.com/Incendo/cloud/pull/496), [#617](https://github.com/Incendo/cloud/pull/617))**
Methods producing suggestions can now return iterable/stream rather than just a list of suggestions.
The methods can either return suggestion objects, or strings that will be mapped to simple suggestions.
The suggestion methods may also accept injected parameters and omit both the sender and command input.

**More flexible annotation parser ([#509](https://github.com/Incendo/cloud/pull/509)) - Proposal: [#510](https://github.com/Incendo/cloud/pull/510)**
The annotation parser now allows you to swap out the components that make up the annotation parsing process. This means that we're no longer bound to the cloud annotations, and that users can swap out how command methods are detected, arguments are bound, flags are assembled, etc.

**`@ExceptionHandler` methods ([#537](https://github.com/Incendo/cloud/pull/537))**
You may now use annotated methods as exception handlers.

**Repeatable `@Command` ([#541](https://github.com/Incendo/cloud/pull/541))**
`@Command` is now repeatable which allows you to define multiple commands that target the same method.

**Arguments without `@Argument` ([#551](https://github.com/Incendo/cloud/pull/551))**
Command methods can skip `@Argument` when the parameter name matches the name specified in the command syntax string. This requires that the code is compiled with type parameters preserved.

**Description mappers ([#551](https://github.com/Incendo/cloud/pull/551))**
You may register a description mapper to `AnnotationParser` which will be used to map strings to `Description` objects. This is used for arguments, commands, and flags.

**Move default value into `@Default` ([#552](https://github.com/Incendo/cloud/pull/552))**
Default values are no longer declared in `@Argument`.

**Builder decorators ([#559](https://github.com/Incendo/cloud/pull/559))**
Decorators are like builder modifiers, but they're applied to _all_ builders constructed by the annotation parser.

**CommandContainer priorities ([#583](https://github.com/Incendo/cloud/pull/583))**
`@CommandContainer` may not have a priority specified which determines the order in which the containers are initialized.

**Allow for a different class loader when parsing command containers**
This fixes issues with platforms that use custom class loaders for plugins.

**Use the same parameter matching logic everywhere ([617](https://github.com/Incendo/cloud/pull/617))**
This allows for injections and more flexible signatures in more places.

**Support Gradle incremental compilation ([#624](https://github.com/Incendo/cloud/pull/624))**
Faster compilation.

**Organize & rename annotations ((#631)[https://github.com/Incendo/cloud/pull/631])**
`@CommandMethod` has been renamed to `@Command` and `@CommandPermission` has been renamed to `@Permission`

**Annotated default-returning methods ([#670](https://github.com/Incendo/cloud/issues/670))**
Methods annotated with `@Default` can be used to register `DefaultValue` instances.

**Allow for multiple permissions ([#675](https://github.com/Incendo/cloud/pull/675))**
`@Permission` can be used to specify multiple permissions.

### Kotlin

**Support default values ([#511](https://github.com/Incendo/cloud/pull/511))**
The coroutine supporting command execution handler can now handle default values. You need to enable coroutine support to use this, but the execution handler also supports non-suspending functions.

**Suspending suggestion methods ([#521](https://github.com/Incendo/cloud/pull/521))**
With suggestion providers having the ability to return futures, we can now properly support suspending annotated suggestion methods.

**Suspending suggestion providers & argument parsers ([#562](https://github.com/Incendo/cloud/pull/562))**
There are new Kotlin-specific utilities for working with suspending suggestion providers and argument parsers.

### Minecraft

The Minecraft modules have been moved into two separate GitHub repositories: [incendo/cloud-minecraft](https://github.com/Incendo/cloud-minecraft)
and [incendo/cloud-minecraft-modded](https://github.com/Incendo/cloud-minecraft-modded).

#### Brigadier

**Suggestions with tooltips ([#529](https://github.com/Incendo/cloud/pull/529))**
Tooltips are now supported in cloud-brigadier and the platforms that support Brigadier.

**Refactoring ([#525](https://github.com/Incendo/cloud/pull/525))**
`cloud-brigadier` has been refactored to make it easier to maintain. The exposed API was updated (and documented) in the process.

**Allow disabling forced executors ([#601](https://github.com/Incendo/cloud/pull/601))**
Adds settings to the Brigadier manager. You may now disable forced executors.

**Use cloud number suggestions by default ([#602](https://github.com/Incendo/cloud/pull/602))**
Brigadier doesn't display suggestions for numbers.

**Better inner node construction ([#610](https://github.com/Incendo/cloud/pull/610))**
Issues with inner nodes in aggregate components have been resolved and these nodes are now constructed in the
same way as other nodes.

**Remove checked exception from creating Bukkit/Paper command managers**
It simplifies the setup process. They may still throw runtime exceptions, so make sure to read the documentation.

#### Bukkit

**Description resolution ([#530](https://github.com/Incendo/cloud/pull/530))**
Bukkit will now look up command descriptions differently than before, it will:
This also changes how Bukkit attempts to resolve descriptions for root commands. It will:

1. Check for the presence of a new `BUKKIT_DESCRIPTION` command meta key.
2. Attempt to resolve the command description.
3. Use the argument description of the root command node (the description you supply when you do `CommandManager.commandBuilder(...)`).

**Parse selectors in correct thread context ([#590](https://github.com/Incendo/cloud/pull/590))**
Selectors can only be parsed on the main server thread. The parser will now switch to the correct thread when parsing.

### Discord

The Discord modules have been moved in a separate repository: [incendo/cloud-discord](https://github.com/Incendo/cloud-discord).

New modules have been created for JDA5, Kord, Javacord and Discord4J. These modules support Discord slash commands.
The old implementations have been deprecated.
