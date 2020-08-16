package ni.org.jug.subtiava.validation;

import java.util.Objects;

class ReservedWord {
    private static final String PARAMETER_MESSAGE_REQUIRED = "[message] is required";

    final ConstraintFieldToken fieldToken;
    final Object value;

    ReservedWord(ConstraintFieldToken fieldToken, Object value) {
        this.fieldToken = fieldToken;
        this.value = value;
    }

    boolean contains(String message) {
        return Objects.requireNonNull(message, PARAMETER_MESSAGE_REQUIRED).contains(fieldToken.token);
    }

    String replace(String message) {
        // TODO use replaceAll or a custom implementation
        return Objects.requireNonNull(message, PARAMETER_MESSAGE_REQUIRED).replace(fieldToken.token, value.toString());
    }
}
