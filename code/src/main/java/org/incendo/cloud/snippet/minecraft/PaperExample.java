package org.incendo.cloud.snippet.minecraft;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.LegacyPaperCommandManager;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PaperSimpleSenderMapper;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.paper.util.sender.Source;

public class PaperExample {

  public void exampleLegacyNative(final @NonNull JavaPlugin yourPlugin) {
    final ExecutionCoordinator<CommandSender> executionCoordinator = ExecutionCoordinator.simpleCoordinator();
    // --8<-- [start:legacy_native]
    LegacyPaperCommandManager<CommandSender> commandManager = LegacyPaperCommandManager.createNative(
      yourPlugin, /* 1 */
      executionCoordinator /* 2 */
    );
    // --8<-- [end:legacy_native]
  }

  public void exampleLegacyCustom(
    final @NonNull JavaPlugin yourPlugin,
    final @NonNull SenderMapper<CommandSender, YourSenderType> senderMapper
  ) {
    final ExecutionCoordinator<YourSenderType> executionCoordinator = ExecutionCoordinator.simpleCoordinator();
    // --8<-- [start:legacy_custom]
    LegacyPaperCommandManager<YourSenderType> commandManager = new LegacyPaperCommandManager<>(
      yourPlugin, /* 1 */
      executionCoordinator, /* 2 */
      senderMapper /* 3 */
    );
    // --8<-- [end:legacy_custom]
  }

  public void exampleModernNative(final @NonNull JavaPlugin javaPlugin) {
    final ExecutionCoordinator<CommandSourceStack> executionCoordinator = ExecutionCoordinator.simpleCoordinator();
    // --8<-- [start:modern_native]
    PaperCommandManager<CommandSourceStack> commandManager = PaperCommandManager.builder()
      .executionCoordinator(executionCoordinator)
      .buildOnEnable(javaPlugin);
      // or: .buildBootstrapped(bootstrapContext);
    // --8<-- [end:modern_native]
  }

  public void exampleModernCustom(
    final @NonNull JavaPlugin javaPlugin,
    final @NonNull SenderMapper<CommandSourceStack, YourSenderType> senderMapper
  ) {
    final ExecutionCoordinator<YourSenderType> executionCoordinator = ExecutionCoordinator.simpleCoordinator();
    // --8<-- [start:modern_custom]
    PaperCommandManager<YourSenderType> commandManager = PaperCommandManager.builder(senderMapper)
      .executionCoordinator(executionCoordinator)
      .buildOnEnable(javaPlugin);
      // or: .buildBootstrapped(bootstrapContext);
    // --8<-- [end:modern_custom]
  }

  public void exampleModernSimpleSenderMapper(
    final @NonNull JavaPlugin javaPlugin
  ) {
    final ExecutionCoordinator<Source> executionCoordinator = ExecutionCoordinator.simpleCoordinator();
    // --8<-- [start:modern_simple_sender_mapper]
    PaperCommandManager<Source> commandManager = PaperCommandManager
      .builder(PaperSimpleSenderMapper.simpleSenderMapper())
      .executionCoordinator(executionCoordinator)
      .buildOnEnable(javaPlugin);
      // or: .buildBootstrapped(bootstrapContext);

      // this command will only be available to players, and the player type is directly available.
      commandManager.command(commandManager.commandBuilder("player_command")
        .senderType(PlayerSource.class)
        .handler(context -> {
          Player player = context.sender().source();
          player.sendMessage("Hello, player!");
        })
      );
    // --8<-- [end:modern_simple_sender_mapper]
  }

  public record YourSenderType() {
  }
}
