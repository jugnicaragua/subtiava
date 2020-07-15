package ni.org.jug.subtiava.validation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * @author aalaniz
 * @version 1.0
 */
public class FieldValidator implements NumberFieldValidator<FieldValidator>, StringFieldValidator<FieldValidator> {
    public static final String FIELD_CANT_BE_NULL = "[fieldName] can't be null";
    public static final String FIELD_CANT_BE_NOT_NULL = "[fieldName] cant' be not null";
    public static final String FIELD_MUST_BE_GREATER_THAN_OR_EQUAL_TO = "[fieldName] must be greater than or equal to %s";
    public static final String FIELD_MUST_BE_LESS_THAN_OR_EQUAL_TO = "[fieldName] must be less than or equal to %s";

    private final ValidatorBuilder builder;
    private final Supplier<?> fieldSupplier;
    private final String attributeName;
    private boolean notNull;
    private boolean alwaysNull;
    private boolean notBlank;
    private Long min;
    private Long max;
    private BigDecimal minWithDecimals;
    private BigDecimal maxWithDecimals;
    private Integer minLength;
    private Integer maxLength;

    public FieldValidator(ValidatorBuilder builder, Supplier<?> fieldSupplier) {
        this(builder, fieldSupplier, "attribute");
    }

    public FieldValidator(ValidatorBuilder builder, Supplier<?> fieldSupplier, String attributeName) {
        this.builder = Objects.requireNonNull(builder, "[builder] is required");
        this.fieldSupplier = Objects.requireNonNull(fieldSupplier, "[fieldSupplier] is required");
        this.attributeName = Objects.requireNonNull(attributeName, "[attributeName] is required");
    }

    @Override
    public FieldValidator notNull() {
        notNull = true;
        return this;
    }

    @Override
    public FieldValidator alwaysNull() {
        alwaysNull = true;
        return this;
    }

    @Override
    public FieldValidator min(long value) {
        min = value;
        return this;
    }

    @Override
    public FieldValidator max(long value) {
        max = value;
        return this;
    }

    @Override
    public FieldValidator min(BigDecimal value) {
        min = null;
        minWithDecimals = value;
        return this;
    }

    @Override
    public FieldValidator max(BigDecimal value) {
        max = null;
        maxWithDecimals = value;
        return this;
    }

    @Override
    public FieldValidator positive() {
        min = 1l;
        return this;
    }

    @Override
    public FieldValidator negative() {
        max = -1l;
        return this;
    }

    @Override
    public FieldValidator values(long first, long second, long... moreOptions) {
        return null;
    }

    @Override
    public FieldValidator values(BigDecimal first, BigDecimal second, BigDecimal... moreOptions) {
        return null;
    }

    @Override
    public FieldValidator minLength(int value) {
        minLength = value;
        return this;
    }

    @Override
    public FieldValidator maxLength(int value) {
        maxLength = value;
        return this;
    }

    @Override
    public FieldValidator notEmpty() {
        minLength = 1;
        return this;
    }

    @Override
    public FieldValidator notBlank() {
        notBlank = true;
        return this;
    }

    @Override
    public FieldValidator values(String first, String second, String... moreOptions) {
        return null;
    }

    @Override
    public FieldValidator values(Enum first, Enum second, Enum... moreOptions) {
        return null;
    }

    @Override
    public FieldValidator regex(String pattern) {
        return null;
    }

    @Override
    public FieldValidator regex(Pattern pattern) {
        return null;
    }

    @Override
    public List<ConstraintViolation> validate() {
        validateInconsistencies();

        List<ConstraintViolation> violations = new LinkedList<>();

        violations.addAll(validateNullability());

        if (fieldValue() != null) {
            violations.addAll(validateMinAndMax());
        }

        return violations;
    }

    private void validateInconsistencies() throws IllegalArgumentException {
        if (notNull && alwaysNull) {
            throw new IllegalArgumentException("[field] can't be not null and null at the same time");
        }
    }

    private List<ConstraintViolation> validateNullability() {
        List<ConstraintViolation> violations = new ArrayList<>();

        if (notNull) {
            if (fieldValue() == null) {
                violations.add(ConstraintViolation.of(attributeName, FIELD_CANT_BE_NULL));
            }
        }
        if (alwaysNull) {
            if (fieldValue() != null) {
                violations.add(ConstraintViolation.of(attributeName, FIELD_CANT_BE_NOT_NULL));
            }
        }

        return violations;
    }

    private List<ConstraintViolation> validateMinAndMax() {
        List<ConstraintViolation> violations = new ArrayList<>();

        if (isNumber()) {
            if (min != null) {
                if (valueAsLong() < min) {
                    violations.add(ConstraintViolation.of(attributeName, FIELD_MUST_BE_GREATER_THAN_OR_EQUAL_TO, min));
                }
            }
            if (max != null) {
                if (valueAsLong() > max) {
                    violations.add(ConstraintViolation.of(attributeName, FIELD_MUST_BE_LESS_THAN_OR_EQUAL_TO, max));
                }
            }
        }
        if (fieldValue() instanceof BigDecimal) {
            if (minWithDecimals != null) {
                if (valueAsBigDecimal().compareTo(minWithDecimals) < 0) {
                    violations.add(ConstraintViolation.of(attributeName, FIELD_MUST_BE_GREATER_THAN_OR_EQUAL_TO,
                            minWithDecimals.toPlainString()));
                }
            }
            if (maxWithDecimals != null) {
                if (valueAsBigDecimal().compareTo(maxWithDecimals) > 0) {
                    violations.add(ConstraintViolation.of(attributeName, FIELD_MUST_BE_LESS_THAN_OR_EQUAL_TO,
                            maxWithDecimals.toPlainString()));
                }
            }
        }

        return violations;
    }

    private Object fieldValue() {
        return fieldSupplier.get();
    }

    private boolean isNumber() {
        Object value = fieldValue();
        return value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long ||
                value instanceof BigInteger || value instanceof BigDecimal;
    }

    private long valueAsLong() {
        return ((Number) fieldValue()).longValue();
    }

    private BigDecimal valueAsBigDecimal() {
        return (BigDecimal) fieldValue();
    }

    @Override
    public FieldValidator of(Supplier<?> fieldSupplier) {
        return builder.of(fieldSupplier);
    }

    @Override
    public <T extends String> StringFieldValidator ofString(Supplier<T> fieldSupplier) {
        return builder.ofString(fieldSupplier);
    }

    @Override
    public <T extends Number> NumberFieldValidator ofNumber(Supplier<T> fieldSupplier) {
        return builder.ofNumber(fieldSupplier);
    }

    @Override
    public Validator instance() {
        return builder.instance();
    }
}
