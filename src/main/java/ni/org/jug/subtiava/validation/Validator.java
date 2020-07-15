package ni.org.jug.subtiava.validation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

/**
 * @author aalaniz
 * @version 1.0
 */
public class Validator implements ValidatorBuilder {
    // TODO Considerar el uso de Set
    private final List<FieldValidator> validators = new LinkedList<>();
    private final Locale locale;

    // TODO Cambiar el alcance del constructor
    public Validator(Locale locale) {
        this.locale = locale;
    }

    @Override
    public FieldValidator of(Supplier<?> fieldSupplier) {
        return addValidator(fieldSupplier);
    }

    @Override
    public <T extends String> StringFieldValidator ofString(Supplier<T> fieldSupplier) {
        return new StringFieldValidatorImpl(addValidator(fieldSupplier));
    }

    @Override
    public <T extends Number> NumberFieldValidator ofNumber(Supplier<T> fieldSupplier) {
        return new NumberFieldValidatorImpl(addValidator(fieldSupplier));
    }

    @Override
    public Validator instance() {
        return this;
    }

    private FieldValidator addValidator(Supplier<?> fieldSupplier) {
        FieldValidator fieldValidator = new FieldValidator(this, fieldSupplier);
        validators.add(fieldValidator);
        return fieldValidator;
    }

    public List<ConstraintViolation> validate() {
        List<ConstraintViolation> violations = new LinkedList<>();
        for (FieldValidator fieldValidator : validators) {
            violations.addAll(fieldValidator.validate());
        }
        return Collections.unmodifiableList(violations);
    }
}
