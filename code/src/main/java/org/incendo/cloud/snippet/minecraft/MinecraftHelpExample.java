package org.incendo.cloud.snippet.minecraft;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.component.DefaultValue;
import org.incendo.cloud.help.result.CommandEntry;
import org.incendo.cloud.minecraft.extras.AudienceProvider;
import org.incendo.cloud.minecraft.extras.MinecraftHelp;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;

import java.util.stream.Collectors;

import static org.incendo.cloud.parser.standard.StringParser.greedyStringParser;

public class MinecraftHelpExample {

  public @NonNull MinecraftHelp<NativeSenderType> nativeHelp(final @NonNull CommandManager<NativeSenderType> commandManager) {
    // --8<-- [start:native]
    MinecraftHelp<NativeSenderType> help = MinecraftHelp.<NativeSenderType>builder()
      .commandManager(commandManager)
      .audienceProvider(AudienceProvider.nativeAudience())
      .commandPrefix("/helpcommand")
      .colors(MinecraftHelp.helpColors(NamedTextColor.GREEN, NamedTextColor.RED,
        NamedTextColor.AQUA, NamedTextColor.BLACK, NamedTextColor.WHITE
      ))
      /* other settings... */
      .build();
    // --8<-- [end:native]
    return help;
  }

  public void nonNativeHelp(final @NonNull CommandManager<SenderType> commandManager) {
    // --8<-- [start:non_native]
    AudienceProvider<SenderType> audienceProvider = SenderType::audience;
    MinecraftHelp<SenderType> help = MinecraftHelp.<SenderType>builder()
      .commandManager(commandManager)
      .audienceProvider(audienceProvider)
      .commandPrefix("/helpcommand")
      .colors(MinecraftHelp.helpColors(NamedTextColor.GREEN, NamedTextColor.RED,
        NamedTextColor.AQUA, NamedTextColor.BLACK, NamedTextColor.WHITE
      ))
      /* other settings... */
      .build();
    // --8<-- [end:non_native]
  }

  public void helpCommand(final @NonNull CommandManager<NativeSenderType> commandManager) {
    final MinecraftHelp<NativeSenderType> help = this.nativeHelp(commandManager);
    // --8<-- [start:help_command]
    commandManager.command(
      commandManager.commandBuilder("helpcommand")
        .optional("query", greedyStringParser(), DefaultValue.constant(""))
        .handler(context -> {
          help.queryCommands(context.get("query"), context.sender());
        })
    );
    // --8<-- [end:help_command]
    commandManager.command(
      commandManager.commandBuilder("helpcommand")
        // --8<-- [start:help_suggestions]
        .optional(
          "query",
          greedyStringParser(),
          DefaultValue.constant(""),
          SuggestionProvider.blocking((ctx, in) -> commandManager.createHelpHandler()
            .queryRootIndex(ctx.sender())
            .entries()
            .stream()
            .map(CommandEntry::syntax)
            .map(Suggestion::simple)
            .collect(Collectors.toList())
          )
        )
        // --8<-- [end:help_suggestions]
        .handler(context -> {
          help.queryCommands(context.get("query"), context.sender());
        })
    );
  }

  public static final class NativeSenderType implements Audience {

  }

  public static abstract class SenderType {

    public abstract @NonNull Audience audience();
  }
}
