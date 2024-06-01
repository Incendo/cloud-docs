package org.incendo.cloud.snippet.processors;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.processors.cooldown.Cooldown;
import org.incendo.cloud.processors.cooldown.CooldownConfiguration;
import org.incendo.cloud.processors.cooldown.CooldownGroup;
import org.incendo.cloud.processors.cooldown.CooldownManager;
import org.incendo.cloud.processors.cooldown.CooldownRepository;
import org.incendo.cloud.processors.cooldown.DurationFunction;
import org.incendo.cloud.processors.cooldown.listener.ScheduledCleanupCreationListener;
import org.incendo.cloud.processors.cooldown.profile.CooldownProfile;
import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Example of Cooldown usage.
 */
public class CooldownExample {

  public void configurationExample() {
    // --8<-- [start:configuration]
    CooldownRepository<YourSenderType> repository = CooldownRepository.forMap(new HashMap<>());
    CooldownConfiguration<YourSenderType> configuration = CooldownConfiguration.<YourSenderType>builder()
      // ...
      .repository(repository)
      .addActiveCooldownListener(((sender, command, cooldown, remainingTime) -> { /* ... */}))
      .build();
    // --8<-- [end:configuration]
  }

  public void cleanupExample() {
    // --8<-- [start:cleanup]
    CooldownRepository<YourSenderType> repository = CooldownRepository.forMap(new HashMap<>());
    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    CooldownConfiguration<YourSenderType> configuration = CooldownConfiguration.<YourSenderType>builder()
      // ...
      .repository(repository)
      .addCreationListener(new ScheduledCleanupCreationListener<>(executorService, repository))
      .build();
    // --8<-- [end:cleanup]
  }

  public void registrationExample(
    final @NonNull CommandManager<YourSenderType> commandManager,
    final @NonNull CooldownManager<YourSenderType> cooldownManager
  ) {
    // --8<-- [start:registration]
    commandManager.registerCommandPostProcessor(
      cooldownManager.createPostprocessor()
    );
    // --8<-- [end:registration]
  }

  public void creationExample(final @NonNull CooldownConfiguration<YourSenderType> configuration) {
    // --8<-- [start:creation]
    CooldownManager<YourSenderType> cooldownManager = CooldownManager.cooldownManager(
      configuration
    );
    // --8<-- [end:creation]
  }

  public void mappingExample() {
    // --8<-- [start:mapping]
    CooldownRepository<YourSenderType> repository = CooldownRepository.mapping(
      YourSenderType::uuid,
      CooldownRepository.forMap(new HashMap<>())
    );
    // --8<-- [end:mapping]
  }

  public void cooldownExample() {
    // --8<-- [start:cooldown]
    Cooldown<YourSenderType> cooldown = Cooldown.of(
      DurationFunction.constant(Duration.ofMinutes(5L))
    );
    Cooldown<YourSenderType> grouped = Cooldown.of(
      DurationFunction.constant(Duration.ofMinutes(5L)),
      CooldownGroup.named("group-name")
    );
    // --8<-- [end:cooldown]
  }

  private record YourSenderType(@NonNull UUID uuid) {

  }
}
