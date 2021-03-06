package ni.org.jug.subtiava.validation;

import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * @author aalaniz
 * @version 1.0
 */
public class StringFieldValidatorImpl implements StringFieldValidator {
    private final FieldValidator delegate;

    public StringFieldValidatorImpl(FieldValidator delegate) {
        this.delegate = delegate;
    }

    @Override
    public StringFieldValidator minLength(int value) {
        delegate.minLength(value);
        return this;
    }

    @Override
    public StringFieldValidator maxLength(int value) {
        delegate.maxLength(value);
        return this;
    }

    @Override
    public StringFieldValidator notEmpty() {
        delegate.notEmpty();
        return this;
    }

    @Override
    public StringFieldValidator notBlank() {
        delegate.notBlank();
        return this;
    }

    @Override
    public StringFieldValidator values(String first, String second, String... moreOptions) {
        delegate.values(first, second, moreOptions);
        return this;
    }

    @Override
    public StringFieldValidator regex(Pattern pattern) {
        delegate.regex(pattern);
        return this;
    }

    @Override
    public StringFieldValidator notNull() {
        delegate.notNull();
        return this;
    }

    @Override
    public StringFieldValidator alwaysNull() {
        delegate.alwaysNull();
        return this;
    }

    @Override
    public List<ConstraintViolation> check() {
        return delegate.check();
    }

    @Override
    public FieldValidator of(Supplier<?> fieldSupplier) {
        return delegate.of(fieldSupplier);
    }

    @Override
    public FieldValidator of(Supplier<?> fieldSupplier, String attributeName) {
        return delegate.of(fieldSupplier, attributeName);
    }

    @Override
    public <T extends String> StringFieldValidator stringField(Supplier<T> fieldSupplier) {
        return delegate.stringField(fieldSupplier);
    }

    @Override
    public <T extends String> StringFieldValidator stringField(Supplier<T> fieldSupplier, String attributeName) {
        return delegate.stringField(fieldSupplier, attributeName);
    }

    @Override
    public <T extends Number> NumberFieldValidator numberField(Supplier<T> fieldSupplier) {
        return delegate.numberField(fieldSupplier);
    }

    @Override
    public <T extends Number> NumberFieldValidator numberField(Supplier<T> fieldSupplier, String attributeName) {
        return delegate.numberField(fieldSupplier, attributeName);
    }

    @Override
    public <T> DateFieldValidator dateField(Supplier<T> fieldSupplier) {
        return delegate.dateField(fieldSupplier);
    }

    @Override
    public <T> DateFieldValidator dateField(Supplier<T> fieldSupplier, String attributeName) {
        return delegate.dateField(fieldSupplier, attributeName);
    }

    @Override
    public Validator instance() {
        return delegate.instance();
    }

    @Override
    public List<ConstraintViolation> validate() {
        return delegate.validate();
    }
}
