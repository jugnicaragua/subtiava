package ni.org.jug.subtiava.validation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author aalaniz
 * @version 1.0
 */
public class Validator implements ValidatorBuilder {
    private static final String FIELD_DEFAULT_NAME = "attribute";

    private final List<FieldValidator> validators = new LinkedList<>();
    private final String pojo;
    private final Locale locale;

    public Validator() {
        this.pojo = null;
        this.locale = Locale.getDefault();
    }

    public Validator(String pojo) {
        this(pojo, Locale.getDefault());
    }

    public Validator(String pojo, Locale locale) {
        this.pojo = Objects.requireNonNull(pojo, "[pojo] is required");
        this.locale = Objects.requireNonNull(locale, "[locale] is required");
    }

    public Validator(Class<?> type) {
        this(type, Locale.getDefault());
    }

    public Validator(Class<?> type, Locale locale) {
        this(Objects.requireNonNull(type, "[type] is required").getSimpleName(), locale);
    }

    public String getPojo() {
        return pojo;
    }

    public Locale getLocale() {
        return locale;
    }

    @Override
    public FieldValidator of(Supplier<?> fieldSupplier) {
        return of(fieldSupplier, FIELD_DEFAULT_NAME);
    }

    @Override
    public FieldValidator of(Supplier<?> fieldSupplier, String attributeName) {
        return addValidator(fieldSupplier, attributeName);
    }

    @Override
    public <T extends String> StringFieldValidator stringField(Supplier<T> fieldSupplier) {
        return stringField(fieldSupplier, FIELD_DEFAULT_NAME);
    }

    @Override
    public <T extends String> StringFieldValidator stringField(Supplier<T> fieldSupplier, String attributeName) {
        return new StringFieldValidatorImpl(addValidator(fieldSupplier, attributeName));
    }

    @Override
    public <T extends Number> NumberFieldValidator numberField(Supplier<T> fieldSupplier) {
        return numberField(fieldSupplier, FIELD_DEFAULT_NAME);
    }

    @Override
    public <T extends Number> NumberFieldValidator numberField(Supplier<T> fieldSupplier, String attributeName) {
        return new NumberFieldValidatorImpl(addValidator(fieldSupplier, attributeName));
    }

    @Override
    public <T> DateFieldValidator dateField(Supplier<T> fieldSupplier) {
        return dateField(fieldSupplier, FIELD_DEFAULT_NAME);
    }

    @Override
    public <T> DateFieldValidator dateField(Supplier<T> fieldSupplier, String attributeName) {
        return new DateFieldValidatorImpl(addValidator(fieldSupplier, attributeName));
    }

    @Override
    public Validator instance() {
        return this;
    }

    @Override
    public List<ConstraintViolation> validate() {
        List<ConstraintViolation> violations = new LinkedList<>();
        for (FieldValidator fieldValidator : validators) {
            violations.addAll(fieldValidator.check());
        }
        return Collections.unmodifiableList(violations);
    }

    private FieldValidator addValidator(Supplier<?> fieldSupplier, String attributeName) {
        FieldValidator fieldValidator = new FieldValidator(this, fieldSupplier, attributeName);
        validators.add(fieldValidator);
        return fieldValidator;
    }
}
