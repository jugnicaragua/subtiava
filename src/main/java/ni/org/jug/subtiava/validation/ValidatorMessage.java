package ni.org.jug.subtiava.validation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author aalaniz
 * @version 1.0
 */
public class ValidatorMessage {
    private static final String BUNDLE_NAME = "ni.org.jug.subtiava.validation.ConstraintViolationMessage";

    private final Locale locale;
    private final ResourceBundle bundle;
    private final ConstraintCoordinate coordinate;
    private final Map<BundleConstraintKey, String> constraintKeys = new HashMap<>(BundleConstraintKey.BUNDLE_KEYS.size());

    public ValidatorMessage(ConstraintCoordinate coordinate) {
        this(coordinate, Locale.getDefault());
    }

    public ValidatorMessage(ConstraintCoordinate coordinate, Locale locale) {
        this.locale = Objects.requireNonNull(locale, "[locale] is required");
        this.bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
        this.coordinate = Objects.requireNonNull(coordinate, "[coordinate] is required");
        this.constraintKeys.putAll(BundleConstraintKey.BUNDLE_KEYS);
    }

    public void constraintKey(BundleConstraintKey key, String newKey) {
        Objects.requireNonNull(key, "[key] is required");
        Objects.requireNonNull(newKey, "[newKey] is required");
        constraintKeys.put(key, newKey);
    }

    public String fieldNotNull() {
        return errorMessage(BundleConstraintKey.FIELD_NOT_NULL);
    }

    public String fieldAlwaysNull() {
        return errorMessage(BundleConstraintKey.FIELD_ALWAYS_NULL);
    }

    public String fieldNotEmpty() {
        return errorMessage(BundleConstraintKey.FIELD_NOT_EMPTY);
    }

    public String fieldNotBlank() {
        return errorMessage(BundleConstraintKey.FIELD_NOT_BLANK);
    }

    public String fieldMinLength(int length) {
        return errorMessage(BundleConstraintKey.FIELD_MIN_LENGTH, ConstraintFieldToken.LENGTH_TOKEN.asWord(length));
    }

    public String fieldMaxLength(int length) {
        return errorMessage(BundleConstraintKey.FIELD_MAX_LENGTH, ConstraintFieldToken.LENGTH_TOKEN.asWord(length));
    }

    public String fieldPattern() {
        return errorMessage(BundleConstraintKey.FIELD_PATTERN);
    }

    public String fieldGreaterThanOrEqual(Long number) {
        return errorMessage(BundleConstraintKey.FIELD_GREATER_OR_EQUAL, ConstraintFieldToken.NUMBER_TOKEN.asWord(number));
    }

    public String fieldGreaterThanOrEqual(BigDecimal number) {
        return errorMessage(BundleConstraintKey.FIELD_GREATER_OR_EQUAL, ConstraintFieldToken.NUMBER_TOKEN.asWord(number.toPlainString()));
    }

    public String fieldLessThanOrEqual(Long number) {
        return errorMessage(BundleConstraintKey.FIELD_LESS_OR_EQUAL, ConstraintFieldToken.NUMBER_TOKEN.asWord(number));
    }

    public String fieldLessThanOrEqual(BigDecimal number) {
        return errorMessage(BundleConstraintKey.FIELD_LESS_OR_EQUAL, ConstraintFieldToken.NUMBER_TOKEN.asWord(number.toPlainString()));
    }

    public String fieldWithinOptions(long[] numbers) {
        return errorMessage(BundleConstraintKey.FIELD_WITHIN_OPTIONS,
                ConstraintFieldToken.OPTIONS_TOKEN.asWord(Arrays.toString(numbers)));
    }

    public String fieldWithinOptions(BigDecimal[] numbers) {
        return errorMessage(BundleConstraintKey.FIELD_WITHIN_OPTIONS,
                ConstraintFieldToken.OPTIONS_TOKEN.asWord(Arrays.toString(numbers)));
    }

    public String fieldWithinOptions(String[] options) {
        return errorMessage(BundleConstraintKey.FIELD_WITHIN_OPTIONS,
                ConstraintFieldToken.OPTIONS_TOKEN.asWord(Arrays.toString(options)));
    }

    public String fieldPositiveValue() {
        return errorMessage(BundleConstraintKey.FIELD_POSITIVE_VALUE);
    }

    public String fieldNegativeValue() {
        return errorMessage(BundleConstraintKey.FIELD_NEGATIVE_VALUE);
    }

    public String fieldInThePast() {
        return errorMessage(BundleConstraintKey.FIELD_IN_THE_PAST);
    }

    public String fieldInThePastOrPresent() {
        return errorMessage(BundleConstraintKey.FIELD_IN_THE_PAST_OR_PRESENT);
    }

    public String fieldInTheFuture() {
        return errorMessage(BundleConstraintKey.FIELD_IN_THE_FUTURE);
    }

    public String fieldInTheFutureOrPresent() {
        return errorMessage(BundleConstraintKey.FIELD_IN_THE_FUTURE_OR_PRESENT);
    }

    public String fieldMinAge(int age) {
        return errorMessage(BundleConstraintKey.FIELD_MIN_AGE, ConstraintFieldToken.AGE_TOKEN.asWord(age));
    }

    public String fieldMaxAge(int age) {
        return errorMessage(BundleConstraintKey.FIELD_MAX_AGE, ConstraintFieldToken.AGE_TOKEN.asWord(age));
    }

    public String fieldEqualAge(int age) {
        return errorMessage(BundleConstraintKey.FIELD_EQUAL_AGE, ConstraintFieldToken.AGE_TOKEN.asWord(age));
    }

    public String fieldMinYear(int year) {
        return errorMessage(BundleConstraintKey.FIELD_MIN_YEAR, ConstraintFieldToken.YEAR_TOKEN.asWord(year));
    }

    public String fieldMaxYear(int year) {
        return errorMessage(BundleConstraintKey.FIELD_MAX_YEAR, ConstraintFieldToken.YEAR_TOKEN.asWord(year));
    }

    public String fieldEqualYear(int year) {
        return errorMessage(BundleConstraintKey.FIELD_EQUAL_YEAR, ConstraintFieldToken.YEAR_TOKEN.asWord(year));
    }

    public String fieldMinMonth(int month) {
        return errorMessage(BundleConstraintKey.FIELD_MIN_MONTH, ConstraintFieldToken.MONTH_TOKEN.asWord(month));
    }

    public String fieldMaxMonth(int month) {
        return errorMessage(BundleConstraintKey.FIELD_MAX_MONTH, ConstraintFieldToken.MONTH_TOKEN.asWord(month));
    }

    public String fieldEqualMonth(int month) {
        return errorMessage(BundleConstraintKey.FIELD_EQUAL_MONTH, ConstraintFieldToken.MONTH_TOKEN.asWord(month));
    }

    public String fieldMinDay(int day) {
        return errorMessage(BundleConstraintKey.FIELD_MIN_DAY, ConstraintFieldToken.DAY_TOKEN.asWord(day));
    }

    public String fieldMaxDay(int day) {
        return errorMessage(BundleConstraintKey.FIELD_MAX_DAY, ConstraintFieldToken.DAY_TOKEN.asWord(day));
    }

    public String fieldEqualDay(int day) {
        return errorMessage(BundleConstraintKey.FIELD_EQUAL_DAY, ConstraintFieldToken.DAY_TOKEN.asWord(day));
    }

    private String errorMessage(BundleConstraintKey key) {
        return errorMessage(key, null);
    }

    private String errorMessage(BundleConstraintKey key, ReservedWord... args) {
        int size = 2 + (args == null ? 0 : args.length);
        List<ReservedWord> words = new ArrayList<>(size);
        words.add(coordinate.asPojoReservedWord());
        words.add(coordinate.asFieldReservedWord());
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                words.add(args[i]);
            }
        }
        String message = getBundleString(bundle, constraintKeys.get(key));
        return resolveMessage(message, words);
    }

    private static String getBundleString(ResourceBundle bundle, String key) {
        return getBundleString(bundle, key, key);
    }

    private static String getBundleString(ResourceBundle bundle, String key, String defaultValue) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException ex) {
            return defaultValue;
        }
    }

    private static String resolveMessage(String message, List<ReservedWord> words) {
        for (ReservedWord word : words) {
            if (word.contains(message)) {
                message = word.replace(message);
            }
        }
        return message;
    }

    enum BundleConstraintKey {
        FIELD_NOT_NULL,
        FIELD_ALWAYS_NULL,
        FIELD_NOT_EMPTY,
        FIELD_NOT_BLANK,
        FIELD_MIN_LENGTH,
        FIELD_MAX_LENGTH,
        FIELD_PATTERN,
        FIELD_GREATER_OR_EQUAL,
        FIELD_LESS_OR_EQUAL,
        FIELD_WITHIN_OPTIONS,
        FIELD_POSITIVE_VALUE,
        FIELD_NEGATIVE_VALUE,
        FIELD_IN_THE_PAST,
        FIELD_IN_THE_PAST_OR_PRESENT,
        FIELD_IN_THE_FUTURE,
        FIELD_IN_THE_FUTURE_OR_PRESENT,
        FIELD_MIN_AGE,
        FIELD_MAX_AGE,
        FIELD_EQUAL_AGE,
        FIELD_MIN_YEAR,
        FIELD_MAX_YEAR,
        FIELD_EQUAL_YEAR,
        FIELD_MIN_MONTH,
        FIELD_MAX_MONTH,
        FIELD_EQUAL_MONTH,
        FIELD_MIN_DAY,
        FIELD_MAX_DAY,
        FIELD_EQUAL_DAY;

        static final Map<BundleConstraintKey, String> BUNDLE_KEYS = new HashMap<>(40);
        static {
            for (BundleConstraintKey key : BundleConstraintKey.values()) {
                BUNDLE_KEYS.put(key, key.name().toLowerCase());
            }
        }
    }
}
