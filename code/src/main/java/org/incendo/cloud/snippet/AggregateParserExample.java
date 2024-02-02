package org.incendo.cloud.snippet;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.Command;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.aggregate.AggregateParser;

import static org.incendo.cloud.parser.standard.IntegerParser.integerParser;
import static org.incendo.cloud.parser.standard.StringParser.stringParser;

/**
 * Example of {@link AggregateParser}.
 */
public class AggregateParserExample {

    public void example(final Command.@NonNull Builder<CommandSender> commandBuilder) {
        // --8<-- [start:snippet]
        final AggregateParser<CommandSender, Location> locationParser = AggregateParser
                .<CommandSender>builder()
                .withComponent("world", stringParser())
                .withComponent("x", integerParser())
                .withComponent("y", integerParser())
                .withComponent("z", integerParser())
                .withMapper(Location.class, (commandContext, aggregateCommandContext) -> {
                    final String world = aggregateCommandContext.get("world");
                    final int x = aggregateCommandContext.get("x");
                    final int y = aggregateCommandContext.get("y");
                    final int z = aggregateCommandContext.get("z");
                    return ArgumentParseResult.successFuture(new Location(world, x, y, z));
                }).build();
        // --8<-- [end:snippet]
    }

    public static final class Location {

        public Location(final String world, final int x, final int y, final int z) {
        }
    }

    public static final class CommandSender {

    }
}
