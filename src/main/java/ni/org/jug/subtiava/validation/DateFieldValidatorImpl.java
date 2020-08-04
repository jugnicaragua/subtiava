package ni.org.jug.subtiava.validation;

import java.time.Month;
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
    public DateFieldValidator month(Month min, Month max) {
        delegate.month(min, max);
        return this;
    }

    @Override
    public DateFieldValidator day(int min, int max) {
        delegate.day(min, max);
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
