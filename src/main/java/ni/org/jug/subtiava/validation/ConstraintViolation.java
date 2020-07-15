package ni.org.jug.subtiava.validation;

import java.util.Objects;

/**
 * @author aalaniz
 * @version 1.0
 */
public final class ConstraintViolation {
    private static final String FIELD_TOKEN = "[fieldName]";

    public final String pojo;
    public final String field;
    public final String message;
    // TODO Agregar el valor del atributo y pintarlo en el toString

    public ConstraintViolation(String field, String message) {
        this(null, Objects.requireNonNull(field, "[field] is required"), message);
    }

    public ConstraintViolation(String message) {
        this(null, null, message);
    }

    public ConstraintViolation(String pojo, String field, String message) {
        this.pojo = pojo;
        this.field = field;
        this.message = Objects.requireNonNull(message, "[message] is required");
    }

    public static ConstraintViolation of(String field, String message, Object... args) {
        return new ConstraintViolation(field, resolveMessage(field, message, args));
    }

    public static ConstraintViolation of(String message, Object... args) {
        return new ConstraintViolation(String.format(message, args));
    }

    private static String resolveMessage(String field, String message, Object... args) {
        if (message.contains(FIELD_TOKEN)) {
            message = message.replace(FIELD_TOKEN, '[' + field + ']');
        }
        return String.format(message, args);
    }

    @Override
    public String toString() {
        return "ConstraintViolation{" +
                "pojo='" + pojo + '\'' +
                ", field='" + field + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
