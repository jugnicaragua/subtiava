package ni.org.jug.subtiava.validation;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author aalaniz
 * @version 1.0
 */
public class DateFieldValidatorImpl implements DateFieldValidator {
    private final FieldValidator delegate;

    public DateFieldValidatorImpl(FieldValidator delegate) {
        this.delegate = delegate;
    }

    @Override
    public DateFieldValidator notNull() {
        delegate.notNull();
        return this;
    }

    @Override
    public DateFieldValidator alwaysNull() {
        delegate.alwaysNull();
        return this;
    }

    @Override
    public DateFieldValidator past() {
        delegate.past();
        return this;
    }

    @Override
    public DateFieldValidator pastOrPresent() {
        delegate.pastOrPresent();
        return this;
    }

    @Override
    public DateFieldValidator future() {
        delegate.future();
        return this;
    }

    @Override
    public DateFieldValidator futureOrPresent() {
        delegate.futureOrPresent();
        return this;
    }

    @Override
    public DateFieldValidator age(int min, int max) {
        delegate.age(min, max);
        return this;
    }

    @Override
    public DateFieldValidator year(int min, int max) {
        delegate.year(min, max);
        return this;
    }

    @Override
    public DateFieldValidator month(int min, int max) {
        delegate.month(min, max);
        return this;
    }

    @Override
    public DateFieldValidator day(int min, int max) {
        delegate.day(min, max);
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
    public <T> DateFieldValidator ofDate(Supplier<T> fieldSupplier) {
        return delegate.ofDate(fieldSupplier);
    }

    @Override
    public Validator instance() {
        return delegate.instance();
    }
}
