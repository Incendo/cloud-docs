package org.incendo.cloud.snippet;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;

import java.util.UUID;

// --8<-- [start:snippet]
public class UUIDParser<C> implements ArgumentParser<C, UUID> {

    @Override
    public @NonNull ArgumentParseResult<UUID> parse(
            @NonNull CommandContext<C> context,
            @NonNull CommandInput commandInput
    ) {
        final String input = commandInput.peekString(); // Does not remove the string from the input!
        try {
            final UUID uuid = UUID.fromString(input);
            commandInput.readString(); // Removes the string from the input.
            return ArgumentParseResult.success(uuid);
        } catch (final IllegalArgumentException e) {
            return ArgumentParseResult.failure(new UUIDParseException(input, context));
        }
    }
}
// --8<-- [end:snippet]
