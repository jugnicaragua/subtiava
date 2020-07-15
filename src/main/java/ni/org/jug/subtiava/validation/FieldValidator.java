package ni.org.jug.subtiava.validation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
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
    public static final String FIELD_MUST_BE_WITHIN_OPTIONS = "[fieldName] must be one of the following values %s";
    public static final String FIELD_MUST_BE_A_POSITIVE_VALUE = "[fieldName] must be a positive value";
    public static final String FIELD_MUST_BE_A_NEGATIVE_VALUE = "[fieldName] must be a negative value";

    private final ValidatorBuilder builder;
    private final Supplier<?> fieldSupplier;
    private final String attributeName;

    private boolean notNull;
    private boolean alwaysNull;
    private boolean notBlank;
    private Integer minLength;
    private Integer maxLength;

    private Long min;
    private Long max;
    private BigDecimal minWithDecimals;
    private BigDecimal maxWithDecimals;
    private boolean positive;
    private boolean negative;
    private long[] longValues;
    private BigDecimal[] bigDecimalValues;

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
        alwaysNull = false;
        return this;
    }

    @Override
    public FieldValidator alwaysNull() {
        alwaysNull = true;
        notNull = false;
        return this;
    }

    @Override
    public FieldValidator min(long value) {
        min = value;
        minWithDecimals = null;
        positive = false;
        negative = false;
        return this;
    }

    @Override
    public FieldValidator max(long value) {
        max = value;
        maxWithDecimals = null;
        positive = false;
        negative = false;
        return this;
    }

    @Override
    public FieldValidator min(BigDecimal value) {
        minWithDecimals = value;
        min = null;
        positive = false;
        negative = false;
        return this;
    }

    @Override
    public FieldValidator max(BigDecimal value) {
        maxWithDecimals = value;
        max = null;
        positive = false;
        negative = false;
        return this;
    }

    @Override
    public FieldValidator positive() {
        positive = true; // rather than use minWithDecimals
        min = null;
        minWithDecimals = null;
        max = null;
        maxWithDecimals = null;
        negative = false;
        return this;
    }

    @Override
    public FieldValidator negative() {
        negative = true; // rather than use maxWithDecimals
        max = null;
        maxWithDecimals = null;
        min = null;
        minWithDecimals = null;
        positive = false;
        return this;
    }

    @Override
    public FieldValidator values(long first, long... moreOptions) {
        int size = moreOptions == null || moreOptions.length == 0 ? 1 : moreOptions.length + 1;

        longValues = new long[size];
        longValues[0] = first;
        if (!(moreOptions == null || moreOptions.length == 0)) {
            System.arraycopy(moreOptions, 0, longValues, 1, moreOptions.length);
        }
        bigDecimalValues = null;

        return this;
    }

    @Override
    public FieldValidator values(BigDecimal first, BigDecimal second, BigDecimal... moreOptions) {
        int size = (first != null ? 1 : 0) + (second != null ? 1 : 0)
                + (moreOptions == null || moreOptions.length == 0 ? 0 : moreOptions.length);
        int i = 0;

        bigDecimalValues = new BigDecimal[size];
        if (first != null) {
            bigDecimalValues[i++] = first;
        }
        if (second != null) {
            bigDecimalValues[i++] = second;
        }
        if (!(moreOptions == null || moreOptions.length == 0)) {
            System.arraycopy(moreOptions, 0, bigDecimalValues, i, moreOptions.length);
        }
        longValues = null;

        return this;
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
        List<ConstraintViolation> violations = new LinkedList<>();

        violations.addAll(validateNullability());

        if (fieldValue() != null) {
            violations.addAll(validateMinAndMax());
            violations.addAll(validateNumericOptions());
        }

        return violations;
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
            if (positive && valueAsLong() < 1) {
                violations.add(ConstraintViolation.of(attributeName, FIELD_MUST_BE_A_POSITIVE_VALUE));
            }
            if (negative && valueAsLong() > -1) {
                violations.add(ConstraintViolation.of(attributeName, FIELD_MUST_BE_A_NEGATIVE_VALUE));
            }
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
        if (isTypeOf(BigDecimal.class)) {
            if (positive && valueAsBigDecimal().signum() < 1) {
                violations.add(ConstraintViolation.of(attributeName, FIELD_MUST_BE_A_POSITIVE_VALUE));
            }
            if (negative && valueAsBigDecimal().signum() > -1) {
                violations.add(ConstraintViolation.of(attributeName, FIELD_MUST_BE_A_NEGATIVE_VALUE));
            }
            if (minWithDecimals != null) {
                if (lessThan(valueAsBigDecimal(), minWithDecimals)) {
                    violations.add(ConstraintViolation.of(attributeName, FIELD_MUST_BE_GREATER_THAN_OR_EQUAL_TO,
                            minWithDecimals.toPlainString()));
                }
            }
            if (maxWithDecimals != null) {
                if (greaterThan(valueAsBigDecimal(), maxWithDecimals)) {
                    violations.add(ConstraintViolation.of(attributeName, FIELD_MUST_BE_LESS_THAN_OR_EQUAL_TO,
                            maxWithDecimals.toPlainString()));
                }
            }
        }

        return violations;
    }

    private List<ConstraintViolation> validateNumericOptions() {
        List<ConstraintViolation> violations = new ArrayList<>();

        if (isNumber() && longValues != null) {
            boolean found = false;
            long value = valueAsLong();
            for (int i = 0; i < longValues.length; i++) {
                if (value == longValues[i]) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                violations.add(ConstraintViolation.of(attributeName, FIELD_MUST_BE_WITHIN_OPTIONS, Arrays.toString(longValues)));
            }
        }
        if (isTypeOf(BigDecimal.class) && bigDecimalValues != null) {
            boolean found = false;
            BigDecimal value = valueAsBigDecimal();
            for (int i = 0; i < bigDecimalValues.length; i++) {
                if (equals(value, bigDecimalValues[i])) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                violations.add(ConstraintViolation.of(attributeName, FIELD_MUST_BE_WITHIN_OPTIONS, Arrays.toString(bigDecimalValues)));
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

    private boolean isTypeOf(Class<?> type) {
        return type.isAssignableFrom(fieldValue().getClass());
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

    private static boolean lessThan(BigDecimal left, BigDecimal right) {
        return !(left == null || right == null) ? left.compareTo(right) < 0 : false;
    }

    private static boolean greaterThan(BigDecimal left, BigDecimal right) {
        return !(left == null || right == null) ? left.compareTo(right) > 0 : false;
    }

    private static boolean equals(BigDecimal left, BigDecimal right) {
        return !(left == null || right == null) ? left.compareTo(right) == 0 : false;
    }
}
