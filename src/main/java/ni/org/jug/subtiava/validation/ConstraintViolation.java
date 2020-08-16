package ni.org.jug.subtiava.validation;

import java.util.Objects;

/**
 * @author aalaniz
 * @version 1.0
 */
public final class ConstraintViolation {
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

    public ConstraintViolation(ConstraintCoordinate coordinate, Object fieldValue, String message) {
        this(Objects.requireNonNull(coordinate, "[coordinate] is required").pojo, coordinate.field, fieldValue, message);
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
