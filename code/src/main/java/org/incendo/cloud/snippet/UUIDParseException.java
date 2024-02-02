package org.incendo.cloud.snippet;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.caption.StandardCaptionKeys;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.exception.parsing.ParserException;

import java.util.Objects;

public final class UUIDParseException extends ParserException {

    private final String input;

    public UUIDParseException(final @NonNull String input, final @NonNull CommandContext<?> context) {
        super(
                UUIDParser.class,
                context,
                StandardCaptionKeys.ARGUMENT_PARSE_FAILURE_UUID,
                CaptionVariable.of("input", input));
        this.input = input;
    }

    public String input() {
        return this.input;
    }

    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            final UUIDParseException that = (UUIDParseException) o;
            return this.input.equals(that.input());
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.input});
    }
}
