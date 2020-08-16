package ni.org.jug.subtiava.validation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * @author aalaniz
 * @version 1.0
 */
public class FieldValidator implements NumberFieldValidator<FieldValidator>, StringFieldValidator<FieldValidator>,
        DateFieldValidator<FieldValidator> {
    private final Validator validator;
    private final Supplier<?> fieldSupplier;
    private final ConstraintCoordinate coordinate;
    private final ValidatorMessage message;

    private boolean notNull;
    private boolean alwaysNull;
    private boolean notEmpty;
    private boolean notBlank;
    private Integer minLength;
    private Integer maxLength;
    private String[] stringOptions;
    private Pattern pattern;

    private Long min;
    private Long max;
    private BigDecimal minWithDecimals;
    private BigDecimal maxWithDecimals;
    private boolean positive;
    private boolean negative;
    private long[] longOptions;
    private BigDecimal[] bigDecimalOptions;

    private boolean past;
    private boolean pastOrPresent;
    private boolean future;
    private boolean futureOrPresent;
    private int minAge = -1;
    private int maxAge = -1;
    private int minYear = -1;
    private int maxYear = -1;
    private Month minMonth;
    private Month maxMonth;
    private int minDay = -1;
    private int maxDay = -1;

    public FieldValidator(Validator validator, Supplier<?> fieldSupplier, String attributeName) {
        this.validator = Objects.requireNonNull(validator, "[validator] is required");
        this.fieldSupplier = Objects.requireNonNull(fieldSupplier, "[fieldSupplier] is required");
        Objects.requireNonNull(attributeName, "[attributeName] is required");
        this.coordinate = new ConstraintCoordinate(validator.getPojo(), attributeName);
        this.message = new ValidatorMessage(this.coordinate, this.validator.getLocale());
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
        min = null;
        minWithDecimals = null;
        max = null;
        maxWithDecimals = null;
        positive = false;
        return this;
    }

    @Override
    public FieldValidator values(long first, long... moreOptions) {
        int size = Inputs.isEmpty(moreOptions) ? 1 : moreOptions.length + 1;
        longOptions = new long[size];
        longOptions[0] = first;
        if (Inputs.isNotEmpty(moreOptions)) {
            System.arraycopy(moreOptions, 0, longOptions, 1, moreOptions.length);
        }
        bigDecimalOptions = null;
        return this;
    }

    @Override
    public FieldValidator values(BigDecimal first, BigDecimal second, BigDecimal... moreOptions) {
        int size = Inputs.arraySize(first, second, moreOptions);
        if (size > 0) {
            bigDecimalOptions = new BigDecimal[size];
            Inputs.copyArgs(first, second, moreOptions, bigDecimalOptions);
        } else {
            bigDecimalOptions = null;
        }
        longOptions = null;
        return this;
    }

    @Override
    public FieldValidator minLength(int value) {
        minLength = Inputs.requirePositive(value, "minLength");
        return this;
    }

    @Override
    public FieldValidator maxLength(int value) {
        maxLength = Inputs.requirePositive(value, "maxLength");
        return this;
    }

    @Override
    public FieldValidator notEmpty() {
        notEmpty = true;
        notBlank = false;
        notNull = false;
        alwaysNull = false;
        return this;
    }

    @Override
    public FieldValidator notBlank() {
        notBlank = true;
        notEmpty = false;
        notNull = false;
        alwaysNull = false;
        return this;
    }

    @Override
    public FieldValidator values(String first, String second, String... moreOptions) {
        int size = Inputs.arraySize(first, second, moreOptions);
        if (size > 0) {
            stringOptions = new String[size];
            Inputs.copyArgs(first, second, moreOptions, stringOptions);
        } else {
            stringOptions = null;
        }
        return this;
    }

    @Override
    public FieldValidator regex(Pattern pattern) {
        this.pattern = pattern;
        return this;
    }

    @Override
    public FieldValidator past() {
        past = true;
        pastOrPresent = false;
        return this;
    }

    @Override
    public FieldValidator pastOrPresent() {
        pastOrPresent = true;
        past = false;
        return this;
    }

    @Override
    public FieldValidator future() {
        future = true;
        futureOrPresent = false;
        return this;
    }

    @Override
    public FieldValidator futureOrPresent() {
        futureOrPresent = true;
        future = false;
        return this;
    }

    @Override
    public FieldValidator age(int min, int max) {
        Inputs.requireValidRange(min, max);
        minAge = Inputs.requirePositive(min, "minAge");
        maxAge = Inputs.requirePositive(max, "maxAge");
        return this;
    }

    @Override
    public FieldValidator year(int min, int max) {
        Inputs.requireValidRange(min, max);
        minYear = Inputs.requirePositive(min, "minYear");
        maxYear = Inputs.requirePositive(max, "maxYear");
        return this;
    }

    @Override
    public FieldValidator month(Month min, Month max) {
        Objects.requireNonNull(min, "[minMonth] is required");
        Objects.requireNonNull(max, "[maxMonth] is required");
        Inputs.requireValidRange(min.getValue(), max.getValue());
        minMonth = min;
        maxMonth = max;
        return this;
    }

    @Override
    public FieldValidator day(int min, int max) {
        Inputs.requireValidRange(min, max);
        minDay = Inputs.requirePositive(min, "minDay");
        maxDay = Inputs.requirePositive(max, "maxDay");
        return this;
    }

    @Override
    public List<ConstraintViolation> check() {
        checkConstraints();

        List<ConstraintViolation> violations = new LinkedList<>();
        violations.addAll(validateNullability());
        if (fieldValue() != null && !alwaysNull) {
            violations.addAll(validateMinAndMax());
            violations.addAll(validateNumericOptions());
            violations.addAll(validateStringLength());
            violations.addAll(validateStringOptions());
            violations.addAll(validateStringPattern());
            violations.addAll(validateDateInPast());
            violations.addAll(validateDateInFuture());
            violations.addAll(validateAge());
            violations.addAll(validateYear());
            violations.addAll(validateMonth());
            violations.addAll(validateDay());
        }
        return Collections.unmodifiableList(violations);
    }

    private void checkConstraints() {
        if (!(min == null || max == null)) {
            Inputs.requireValidRange(min, max);
        }
        if (!(minWithDecimals == null || maxWithDecimals == null)) {
            Inputs.requireValidRange(minWithDecimals, maxWithDecimals);
        }
        if (!(minLength == null || maxLength == null)) {
            Inputs.requireValidRange(minLength, maxLength);
        }
    }

    private List<ConstraintViolation> validateNullability() {
        List<ConstraintViolation> violations = new ArrayList<>();

        if (notNull) {
            if (fieldValue() == null) {
                violations.add(createViolation(message.fieldNotNull()));
            }
        } else if (alwaysNull) {
            if (fieldValue() != null) {
                violations.add(createViolation(message.fieldAlwaysNull()));
            }
        } else if (isString()) {
            String value = valueAsString();
            if (notEmpty) {
                if (value == null || value.isEmpty()) {
                    violations.add(createViolation(message.fieldNotEmpty()));
                }
            } else if (notBlank) {
                if (value == null || value.isEmpty() || value.length() == spaceCount(value)) {
                    violations.add(createViolation(message.fieldNotBlank()));
                }
            }
        }

        return violations;
    }

    private List<ConstraintViolation> validateMinAndMax() {
        List<ConstraintViolation> violations = new ArrayList<>();

        if (isNumber()) {
            if (positive && valueAsLong() < 1) {
                violations.add(createViolation(message.fieldPositiveValue()));
            } else if (negative && valueAsLong() > -1) {
                violations.add(createViolation(message.fieldNegativeValue()));
            } else {
                if (min != null) {
                    if (valueAsLong() < min) {
                        violations.add(createViolation(message.fieldGreaterThanOrEqual(min)));
                    }
                }
                if (max != null) {
                    if (valueAsLong() > max) {
                        violations.add(createViolation(message.fieldLessThanOrEqual(max)));
                    }
                }
            }
        } else if (isBigDecimal()) {
            if (positive && valueAsBigDecimal().signum() < 1) {
                violations.add(createViolation(message.fieldPositiveValue()));
            } else if (negative && valueAsBigDecimal().signum() > -1) {
                violations.add(createViolation(message.fieldNegativeValue()));
            } else {
                if (minWithDecimals != null) {
                    if (Inputs.lessThan(valueAsBigDecimal(), minWithDecimals)) {
                        violations.add(createViolation(message.fieldGreaterThanOrEqual(minWithDecimals)));
                    }
                }
                if (maxWithDecimals != null) {
                    if (Inputs.greaterThan(valueAsBigDecimal(), maxWithDecimals)) {
                        violations.add(createViolation(message.fieldLessThanOrEqual(maxWithDecimals)));
                    }
                }
            }
        }

        return violations;
    }

    private List<ConstraintViolation> validateNumericOptions() {
        List<ConstraintViolation> violations = new ArrayList<>();

        if (isNumber() && longOptions != null) {
            boolean found = Inputs.searchKey(valueAsLong(), longOptions);
            if (!found) {
                violations.add(createViolation(message.fieldWithinOptions(longOptions)));
            }
        } else if (isBigDecimal() && bigDecimalOptions != null) {
            boolean found = Inputs.searchKey(Inputs::equals, valueAsBigDecimal(), bigDecimalOptions);
            if (!found) {
                violations.add(createViolation(message.fieldWithinOptions(bigDecimalOptions)));
            }
        }

        return violations;
    }

    private List<ConstraintViolation> validateStringLength() {
        List<ConstraintViolation> violations = new ArrayList<>();

        if (isString()) {
            if (minLength != null) {
                if (valueAsString().length() < minLength) {
                    violations.add(createViolation(message.fieldMinLength(minLength)));
                }
            }
            if (maxLength != null) {
                if (valueAsString().length() > maxLength) {
                    violations.add(createViolation(message.fieldMaxLength(maxLength)));
                }
            }
        }

        return violations;
    }

    private List<ConstraintViolation> validateStringOptions() {
        List<ConstraintViolation> violations = new ArrayList<>();

        if (isString() && stringOptions != null) {
            boolean found = Inputs.searchKey(valueAsString(), stringOptions);
            if (!found) {
                violations.add(createViolation(message.fieldWithinOptions(stringOptions)));
            }
        }

        return violations;
    }

    private List<ConstraintViolation> validateStringPattern() {
        List<ConstraintViolation> violations = new ArrayList<>();

        if (isString() && pattern != null) {
            if (!pattern.matcher(valueAsString()).matches()) {
                violations.add(createViolation(message.fieldPattern()));
            }
        }

        return violations;
    }

    private List<ConstraintViolation> validateDateInPast() {
        List<ConstraintViolation> violations = new ArrayList<>();

        if (isDate()) {
            DateComparator comparator = DateComparator.ofType(fieldClass());
            if (past) {
                if (!comparator.isBefore(fieldValue())) {
                    violations.add(createViolation(message.fieldInThePast()));
                }
            } else if (pastOrPresent) {
                if (!comparator.isBeforeOrEqual(fieldValue())) {
                    violations.add(createViolation(message.fieldInThePastOrPresent()));
                }
            }
        }

        return violations;
    }

    private List<ConstraintViolation> validateDateInFuture() {
        List<ConstraintViolation> violations = new ArrayList<>();

        if (isDate()) {
            DateComparator comparator = DateComparator.ofType(fieldClass());
            if (future) {
                if (!comparator.isAfter(fieldValue())) {
                    violations.add(createViolation(message.fieldInTheFuture()));
                }
            } else if (futureOrPresent) {
                if (!comparator.isAfterOrEqual(fieldValue())) {
                    violations.add(createViolation(message.fieldInTheFutureOrPresent()));
                }
            }
        }

        return violations;
    }

    private List<ConstraintViolation> validateAge() {
        List<ConstraintViolation> violations = new ArrayList<>();

        if (isDate() && !(minAge == -1 && maxAge == -1)) {
            DateComparator comparator = DateComparator.ofType(fieldClass());
            if (minAge == maxAge) {
                if (!comparator.isEqualToAge(fieldValue(), minAge)) {
                    violations.add(createViolation(message.fieldEqualAge(minAge)));
                }
            } else {
                if (minAge != -1) {
                    if (!comparator.isGreaterThanOrEqualToAge(fieldValue(), minAge)) {
                        violations.add(createViolation(message.fieldMinAge(minAge)));
                    }
                }
                if (maxAge != -1) {
                    if (!comparator.isLessThanOrEqualToAge(fieldValue(), maxAge)) {
                        violations.add(createViolation(message.fieldMaxAge(maxAge)));
                    }
                }
            }
        }

        return violations;
    }

    private List<ConstraintViolation> validateYear() {
        IntSupplier yearSupplier = () -> DateComparator.ofType(fieldClass()).getYear(fieldValue());
        return validateDateField(yearSupplier, minYear, maxYear, message.fieldMinYear(minYear), message.fieldMaxYear(maxYear),
                message.fieldEqualYear(minYear));
    }

    private List<ConstraintViolation> validateMonth() {
        IntSupplier monthSupplier = () -> DateComparator.ofType(fieldClass()).getMonth(fieldValue());
        int min = minMonth == null ? -1 : minMonth.getValue();
        int max = maxMonth == null ? -1 : maxMonth.getValue();
        return validateDateField(monthSupplier, min, max, message.fieldMinMonth(min), message.fieldMaxMonth(max),
                message.fieldEqualMonth(min));
    }

    private List<ConstraintViolation> validateDay() {
        IntSupplier daySupplier = () -> DateComparator.ofType(fieldClass()).getDay(fieldValue());
        return validateDateField(daySupplier, minDay, maxDay, message.fieldMinDay(minDay), message.fieldMaxDay(maxDay),
                message.fieldEqualDay(minDay));
    }

    private List<ConstraintViolation> validateDateField(IntSupplier dateFieldSupplier, int min, int max, String messageForMin,
            String messageForMax, String messageForEqual) {
        List<ConstraintViolation> violations = new ArrayList<>();

        if (isDate() && !(min == -1 && max == -1)) {
            int dateField = dateFieldSupplier.getAsInt();
            if (min == max) {
                if (dateField != min) {
                    violations.add(createViolation(messageForEqual));
                }
            } else {
                if (min != -1) {
                    if (dateField < min) {
                        violations.add(createViolation(messageForMin));
                    }
                }
                if (max != -1) {
                    if (dateField > max) {
                        violations.add(createViolation(messageForMax));
                    }
                }
            }
        }

        return violations;
    }

    private ConstraintViolation createViolation(String message) {
        return new ConstraintViolation(coordinate, fieldValue(), message);
    }

    private Object fieldValue() {
        return fieldSupplier.get();
    }

    private Class<?> fieldClass() {
        return fieldValue() == null ? null : fieldValue().getClass();
    }

    private boolean isNumber() {
        Object value = fieldValue();
        return value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long ||
                value instanceof BigInteger;
    }

    private boolean isDate() {
        Object value = fieldValue();
        return value instanceof GregorianCalendar || value instanceof Date || value instanceof LocalDate || value instanceof LocalDateTime
                || value instanceof ZonedDateTime;
    }

    private boolean isString() {
        return isTypeOf(String.class);
    }

    private boolean isBigDecimal() {
        return isTypeOf(BigDecimal.class);
    }

    private boolean isTypeOf(Class<?> type) {
        boolean assignable = false;
        if (!(type == null || fieldValue() == null)) {
            assignable = type.isAssignableFrom(fieldClass());
        }
        return assignable;
    }

    private long valueAsLong() {
        return ((Number) fieldValue()).longValue();
    }

    private BigDecimal valueAsBigDecimal() {
        return (BigDecimal) fieldValue();
    }

    private String valueAsString() {
        return (String) fieldValue();
    }

    @Override
    public FieldValidator of(Supplier<?> fieldSupplier) {
        return validator.of(fieldSupplier);
    }

    @Override
    public FieldValidator of(Supplier<?> fieldSupplier, String attributeName) {
        return validator.of(fieldSupplier, attributeName);
    }

    @Override
    public <T extends String> StringFieldValidator stringField(Supplier<T> fieldSupplier) {
        return validator.stringField(fieldSupplier);
    }

    @Override
    public <T extends String> StringFieldValidator stringField(Supplier<T> fieldSupplier, String attributeName) {
        return validator.stringField(fieldSupplier, attributeName);
    }

    @Override
    public <T extends Number> NumberFieldValidator numberField(Supplier<T> fieldSupplier) {
        return validator.numberField(fieldSupplier);
    }

    @Override
    public <T extends Number> NumberFieldValidator numberField(Supplier<T> fieldSupplier, String attributeName) {
        return validator.numberField(fieldSupplier, attributeName);
    }

    @Override
    public <T> DateFieldValidator dateField(Supplier<T> fieldSupplier) {
        return validator.dateField(fieldSupplier);
    }

    @Override
    public <T> DateFieldValidator dateField(Supplier<T> fieldSupplier, String attributeName) {
        return validator.dateField(fieldSupplier, attributeName);
    }

    @Override
    public Validator instance() {
        return validator.instance();
    }

    @Override
    public List<ConstraintViolation> validate() {
        return validator.validate();
    }

    private static int spaceCount(CharSequence value) {
        int count = 0;
        if (!(value == null || value.length() == 0)) {
            for (int i = 0; i < value.length(); i++) {
                if (Character.isSpaceChar(value.charAt(i))) {
                    ++count;
                }
            }
        }
        return count;
    }
}
