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

    <T extends String> StringFieldValidator ofString(Supplier<T> fieldSupplier);

    <T extends String> StringFieldValidator ofString(Supplier<T> fieldSupplier, String attributeName);

    <T extends Number> NumberFieldValidator ofNumber(Supplier<T> fieldSupplier);

    <T extends Number> NumberFieldValidator ofNumber(Supplier<T> fieldSupplier, String attributeName);

    <T> DateFieldValidator ofDate(Supplier<T> fieldSupplier);

    <T> DateFieldValidator ofDate(Supplier<T> fieldSupplier, String attributeName);

    Validator instance();

    List<ConstraintViolation> validate();
}
