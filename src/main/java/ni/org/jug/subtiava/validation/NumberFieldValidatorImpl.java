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
    public NumberFieldValidator values(long first, long second, long... moreOptions) {
        delegate.values(first, second, moreOptions);
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
    public List<ConstraintViolation> validate() {
        return delegate.validate();
    }

    @Override
    public FieldValidator of(Supplier<?> fieldSupplier) {
        return delegate.of(fieldSupplier);
    }

    @Override
    public <T extends String> StringFieldValidator ofString(Supplier<T> fieldSupplier) {
        return delegate.ofString(fieldSupplier);
    }

    @Override
    public <T extends Number> NumberFieldValidator ofNumber(Supplier<T> fieldSupplier) {
        return delegate.ofNumber(fieldSupplier);
    }

    @Override
    public Validator instance() {
        return delegate.instance();
    }
}
