package ni.org.jug.subtiava.validation;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author aalaniz
 * @version 1.0
 */
public interface ValidatorBuilder {
    FieldValidator of(Supplier<?> fieldSupplier);

    FieldValidator of(Supplier<?> fieldSupplier, String attributeName);

    <T extends String> StringFieldValidator stringField(Supplier<T> fieldSupplier);

    <T extends String> StringFieldValidator stringField(Supplier<T> fieldSupplier, String attributeName);

    <T extends Number> NumberFieldValidator numberField(Supplier<T> fieldSupplier);

    <T extends Number> NumberFieldValidator numberField(Supplier<T> fieldSupplier, String attributeName);

    <T> DateFieldValidator dateField(Supplier<T> fieldSupplier);

    <T> DateFieldValidator dateField(Supplier<T> fieldSupplier, String attributeName);

    Validator instance();

    List<ConstraintViolation> validate();
}
