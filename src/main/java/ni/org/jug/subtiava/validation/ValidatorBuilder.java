package ni.org.jug.subtiava.validation;

import java.util.function.Supplier;

/**
 * @author aalaniz
 * @version 1.0
 */
public interface ValidatorBuilder {
    FieldValidator of(Supplier<?> fieldSupplier);

    <T extends String> StringFieldValidator ofString(Supplier<T> fieldSupplier);

    <T extends Number> NumberFieldValidator ofNumber(Supplier<T> fieldSupplier);

    Validator instance();
}
