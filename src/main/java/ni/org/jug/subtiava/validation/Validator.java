package ni.org.jug.subtiava.validation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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

    public Validator() {
        this.pojo = null;
    }

    public Validator(String pojo) {
        this.pojo = Objects.requireNonNull(pojo, "[pojo] is required");
    }

    public String getPojo() {
        return pojo;
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
    public <T extends String> StringFieldValidator ofString(Supplier<T> fieldSupplier) {
        return ofString(fieldSupplier, FIELD_DEFAULT_NAME);
    }

    @Override
    public <T extends String> StringFieldValidator ofString(Supplier<T> fieldSupplier, String attributeName) {
        return new StringFieldValidatorImpl(addValidator(fieldSupplier, attributeName));
    }

    @Override
    public <T extends Number> NumberFieldValidator ofNumber(Supplier<T> fieldSupplier) {
        return ofNumber(fieldSupplier, FIELD_DEFAULT_NAME);
    }

    @Override
    public <T extends Number> NumberFieldValidator ofNumber(Supplier<T> fieldSupplier, String attributeName) {
        return new NumberFieldValidatorImpl(addValidator(fieldSupplier, attributeName));
    }

    @Override
    public <T> DateFieldValidator ofDate(Supplier<T> fieldSupplier) {
        return ofDate(fieldSupplier, FIELD_DEFAULT_NAME);
    }

    @Override
    public <T> DateFieldValidator ofDate(Supplier<T> fieldSupplier, String attributeName) {
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
