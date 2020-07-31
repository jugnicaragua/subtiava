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
    public final Object fieldValue;
    public final String message;

    public ConstraintViolation(String pojo, String field, Object fieldValue, String message) {
        this.pojo = pojo;
        this.field = field;
        this.fieldValue = fieldValue;
        this.message = Objects.requireNonNull(message, "[message] is required");
    }

    public static ConstraintViolation of(String pojo, String field, Object fieldValue, String message, Object... args) {
        return new ConstraintViolation(pojo, field, fieldValue, resolveMessage(pojo, field, fieldValue, message, args));
    }

    private static String resolveMessage(String pojo, String field, Object fieldValue, String message, Object[] args) {
        if (message.contains(FIELD_TOKEN)) {
            message = message.replace(FIELD_TOKEN, '[' + field + ']');
        }
        return String.format(message, args);
    }

    public String getPojo() {
        return pojo;
    }

    public String getField() {
        return field;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ConstraintViolation{" +
                "pojo='" + pojo + '\'' +
                ", field='" + field + '\'' +
                ", fieldValue=<" + fieldValue + ">" +
                ", message='" + message + '\'' +
                '}';
    }
}
