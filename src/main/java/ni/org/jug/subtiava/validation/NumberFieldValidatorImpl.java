package ni.org.jug.subtiava.validation;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author aalaniz
 * @version 1.0
 */
public class NumberFieldValidatorImpl implements NumberFieldValidator {
    private final FieldValidator delegate;

    public NumberFieldValidatorImpl(FieldValidator delegate) {
        this.delegate = delegate;
    }

    @Override
    public NumberFieldValidator min(long value) {
        delegate.min(value);
        return this;
    }

    @Override
    public NumberFieldValidator max(long value) {
        delegate.max(value);
        return this;
    }

    @Override
    public NumberFieldValidator min(BigDecimal value) {
        delegate.min(value);
        return this;
    }

    @Override
    public NumberFieldValidator max(BigDecimal value) {
        delegate.max(value);
        return this;
    }

    @Override
    public NumberFieldValidator positive() {
        delegate.positive();
        return this;
    }

    @Override
    public NumberFieldValidator negative() {
        delegate.negative();
        return this;
    }

    @Override
    public NumberFieldValidator values(long first, long... moreOptions) {
        delegate.values(first, moreOptions);
        return this;
    }

    @Override
    public NumberFieldValidator values(BigDecimal first, BigDecimal second, BigDecimal... moreOptions) {
        delegate.values(first, second, moreOptions);
        return this;
    }

    @Override
    public NumberFieldValidator notNull() {
        delegate.notNull();
        return this;
    }

    @Override
    public NumberFieldValidator alwaysNull() {
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
