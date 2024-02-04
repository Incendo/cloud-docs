package org.incendo.cloud.snippet;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.Command;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.type.Either;

import static org.incendo.cloud.parser.standard.BooleanParser.booleanParser;
import static org.incendo.cloud.parser.standard.IntegerParser.integerParser;

/**
 * Example of {@link Either}.
 */
public class EitherExample<C> {

  public void example(final Command.@NonNull Builder<C> commandBuilder) {
    // --8<-- [start:snippet]
    commandBuilder.required("either", ArgumentParser.firstOf(integerParser(), booleanParser()))
      .handler(context -> {
        Either<Integer, Boolean> either = context.get("either");
        if (either.primary().isPresent()) {
          int integer = either.primary().get();
        } else {
          boolean bool = either.fallback().get();
        }
      });
    // --8<-- [end:snippet]
  }
}
