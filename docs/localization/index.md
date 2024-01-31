# Localization

## Exceptions

### Captions

The exception handlers shipped with Cloud (including [`MinecraftExceptionHandler`](../minecraft/minecraft-extras.md#minecraft-exception-handler))
uses the
[`Caption`](https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/caption/Caption.html) system.
A [`Caption`](https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/caption/Caption.html) is a key
to a configurable message, which allows you to override the default messages on a per-sender level.

The messages retrieved using caption providers that are registered to the
command manager's [`CaptionRegistry`](https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/caption/CaptionRegistry.html).
You may register however many providers you want. The system will iterate over the providers until one returns a non-`null`
result.

```java title="Example caption registration"
manager.captionRegistry().registerProvider((caption, sender) -> {
  // You may want to map your sender to a locale,
  // and look up the translations using a locale-based system:
  Locale locale = sender.getLocale();
  return yourTranslationSystem.getTranslation(locale, caption.key());
});
```

There are also utilities for registering caption-specific providers:

```java title="Per-caption provider"
manager.captionRegistry().registerProvider(
  CaptionProvider.forCaption(theCaption, sender -> "the value")
);
```

If your application does not require translations, you may also register constant caption values:

```java title="Constant captions"
manager.captionRegistry().registerProvider(
  CaptionProvider.constantProvider(theCaption, "the value")
);
```

The captions for [`cloud-core`](../core/index.md) can be found in
[`StandardCaptionKeys`](https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/caption/StandardCaptionKeys.html).
The platform adapters may include additional captions. See the platform-specific documentation for more information.

#### Formatting

Captions may contain placeholders, most often in the form `<key>`.
The JavaDoc for the caption keys list the available placeholders for the caption.
The message registered for the caption will have those variables replaced with variables specific to the parser.

You can replace the default formatter if you want to, this can be done by invoking
[`CommandManager#captionFormatter(CaptionFormatter)`](<https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/CommandManager.html#captionFormatter(org.incendo.cloud.caption.CaptionFormatter)>).

You may create a custom caption formatter that generates more complex output types than strings.
This is particularly useful if you want to route the captions through some external system to generate
platform-native message types (i.e. `Component` for Minecraft). You may format captions using this custom
type by invoking
[`ParserException#formatCaption`](<https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/exception/parsing/ParserException.html#formatCaption(org.incendo.cloud.caption.CaptionFormatter)>)
or
[`CommandContext#formatCaption`](<https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/context/CommandContext.html#formatCaption(org.incendo.cloud.caption.CaptionFormatter,org.incendo.cloud.caption.Caption,org.incendo.cloud.caption.CaptionVariable...)>).
