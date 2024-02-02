# Localization

## Exceptions

### Captions

The exception handlers shipped with Cloud (including [`MinecraftExceptionHandler`](../minecraft/minecraft-extras.md#minecraft-exception-handler))
uses the
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/caption/Caption.html", "Caption") }} system.
A {{ javadoc("https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/caption/Caption.html", "Caption") }} is a key
to a configurable message, which allows you to override the default messages on a per-sender level.
The default messages are simple strings, but it's possible to use a custom [formatter](#formatting) to format
the messages into rich objects.

The messages retrieved using caption providers that are registered to the
command manager's {{ javadoc("https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/caption/CaptionRegistry.html", "CaptionRegistry") }}.
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
{{ javadoc("https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/caption/StandardCaptionKeys.html", "StandardCaptionKeys") }}.
The platform adapters may include additional captions. See the platform-specific documentation for more information.

#### Formatting

The configured caption messages may contain placeholders, most often in the form `<key>`.
The JavaDoc for the caption keys list the available placeholders for the caption.
The message registered for the caption will have those variables replaced with variables specific to the parser.

You can replace the default formatter if you want to, this can be done by invoking
{{ javadoc("<https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/CommandManager.html#captionFormatter(org.incendo.cloud.caption.CaptionFormatter)>", "CommandManager#captionFormatter(CaptionFormatter)") }}.

You may create a custom caption formatter that generates more complex output types than strings.
This is particularly useful if you want to route the captions through some external system to generate
platform-native message types (i.e. `Component` for Minecraft). You may format captions using this custom
type by invoking
{{ javadoc("<https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/exception/parsing/ParserException.html#formatCaption(org.incendo.cloud.caption.CaptionFormatter)>", "ParserException#formatCaption") }}
or
{{ javadoc("<https://javadoc.io/doc/org.incendo/cloud-core/latest/org/incendo/cloud/context/CommandContext.html#formatCaption(org.incendo.cloud.caption.CaptionFormatter,org.incendo.cloud.caption.Caption,org.incendo.cloud.caption.CaptionVariable...)>", "CommandContext#formatCaption") }}.

## Translations

{{ javadoc("https://github.com/incendo/cloud-translations", "cloud-translations") }} contains community-contributed translations of the [captions](#captions)
used in the official Cloud modules. You may see the translation progress (and also contribute) yourself at
our [Crowdin](https://crowdin.com/project/incendo-cloud) page.

### Usage

These modules are available on [Maven Central](https://search.maven.org/search?q=g:org.incendo%20AND%20a:cloud-translations-*).

#### [cloud-translations-core](https://github.com/Incendo/cloud-translations/tree/main/cloud-translations-core)

This module contains tooling for creating caption providers from files with translations.
The module also contains translations for the captions in `cloud-core`.

```java title="Registration of cloud-core translations"
// You need to create an extractor which maps the sender type to a locale.
LocaleExtractor<YourSenderType> extractor = yourSenderType::locale;
// You then create the translation bundle, which is a CaptionProvider.
TranslationBundle<YourSenderType> bundle = TranslationBundle.core(extractor);
// Then you register the caption provider.
manager.captionRegistry().registerProvider(bundle);
```

#### Minecraft Modules

##### [cloud-translations-bukkit](https://github.com/Incendo/cloud-translations/tree/main/cloud-translations-bukkit)

This module contains translations for the captions in `cloud-bukkit`.

```java title="Registration of cloud-bukkit translations"
// You need to create an extractor which maps the sender type to a locale.
LocaleExtractor<YourSenderType> extractor = yourSenderType::locale;
// You then create the translation bundle, which is a CaptionProvider.
TranslationBundle<YourSenderType> bundle = BukkitTranslationBundle.bukkit(extractor);
// Then you register the caption provider.
manager.captionRegistry().registerProvider(bundle);
```

##### [cloud-translations-bungee](https://github.com/Incendo/cloud-translations/tree/main/cloud-translations-bungee)

This module contains translations for the captions in `cloud-bungee`.

```java title="Registration of cloud-bungee translations"
// You need to create an extractor which maps the sender type to a locale.
LocaleExtractor<YourSenderType> extractor = yourSenderType::locale;
// You then create the translation bundle, which is a CaptionProvider.
TranslationBundle<YourSenderType> bundle = BungeeTranslationBundle.bungee(extractor);
// Then you register the caption provider.
manager.captionRegistry().registerProvider(bundle);
```

##### [cloud-translations-velocity](https://github.com/Incendo/cloud-translations/tree/main/cloud-translations-velocity)

This module contains translations for the captions in `cloud-velocity`.

```java title="Registration of cloud-velocity translations"
// You need to create an extractor which maps the sender type to a locale.
LocaleExtractor<YourSenderType> extractor = yourSenderType::locale;
// You then create the translation bundle, which is a CaptionProvider.
TranslationBundle<YourSenderType> bundle = VelocityTranslationBundle.velocity(extractor);
// Then you register the caption provider.
manager.captionRegistry().registerProvider(bundle);
```
