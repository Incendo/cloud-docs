package org.incendo.cloud.snippet.minecraft;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.minecraft.extras.MinecraftExceptionHandler;

import static net.kyori.adventure.text.Component.text;

public class MinecraftExceptionHandlerExample {

  public void nativeExceptionHandler() {
    // --8<-- [start:native]
    MinecraftExceptionHandler.createNative()
      .decorator(component -> text()
        .append(text("[Example] ", NamedTextColor.DARK_RED))
        .append(component)
        .build()
      );
    // --8<-- [end:native]
  }

  public void completeExample(final @NonNull CommandManager<NativeSenderType> manager) {
    // --8<-- [start:complete]
    MinecraftExceptionHandler.<NativeSenderType>createNative()
      .defaultHandlers()
      .decorator(component -> text()
        .append(text("[Example] ", NamedTextColor.DARK_RED))
        .append(component)
        .build()
      ).registerTo(manager);
    // --8<-- [end:complete]
  }

  public static final class NativeSenderType implements Audience {

  }
}
