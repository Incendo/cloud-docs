# cloud-processors-cooldown

Postprocessor that adds command cooldowns.

## Installation

{{ dependency_listing("processors-cooldown", "processors") }}

## Usage

The cooldown system is managed by a `CooldownManager`, so the first step is to create an instance of that:

{{ snippet("processors/CooldownExample.java", section = "creation", title = "") }}

The configuration is an instance of `CooldownConfiguration`. Refer to the JavaDocs for information about specific options,
but an example would be:

{{ snippet("processors/CooldownExample.java", section = "configuration", title = "") }}

The listeners are invoked when different events take place. The active cooldown listener in particular may be used to
inform the command sender that their command execution got blocked due to an active cooldown.

The repository stores active cooldowns for a command sender in the form of cooldown profiles.
The cooldowns are grouped by their `CooldownGroup`, by default a unique group will be created per command.
You may create a named group by using `CooldownGroup.named(name)`. Commands that use the same cooldown group
will have their cooldowns shared by the command sender.

You may create a repository from a map, `CloudCache` or even implement your own. If you want to persist the cooldowns
across multiple temporary sessions then you may use a mapping repository to store the cooldown profiles for a persistent key,
rather than the potentially temporary command sender objects:

{{ snippet("processors/CooldownExample.java", section = "mapping", title = "") }}

You may also customize how the cooldown profiles are created by passing a `CooldownProfileFactory` to the `CooldownConfiguration`.

If you want to have the cooldowns automatically removed from the repository to prevent unused profiles from taking up memory you
may register a `ScheduledCleanupCreationListener` to the configuration, using

{{ snippet("processors/CooldownExample.java", section = "cleanup", title = "") }}

You then need to register the postprocessor:

{{ snippet("processors/CooldownExample.java", section = "registration", title = "") }}

### Builders

The cooldowns are configured using a `Cooldown` instance:

{{ snippet("processors/CooldownExample.java", section = "cooldown", title = "") }}

which can then be applied to the command by either manually setting the meta value:

```java
commandBuilder.meta(CooldownManager.META_COOLDOWN_DURATION, cooldown);
```

or by applying the cooldown to the builder:

```java
commandBuilder.apply(cooldown);
```

### Annotations

Annotated commands may use the `@Cooldown` annotation:

```java
@Cooldown(duration = 5, timeUnit = ChronoUnit.MINUTES, group = "some-group")
public void yourCommand() {
    // ...
}
```

You need to install the builder modifier for this to work:

```java
CooldownBuilderModifier.install(annotationParser);
```
