package ni.org.jug.subtiava.validation;

import java.util.List;

/**
 * @author aalaniz
 * @version 1.0
 */
public interface BaseFieldValidator<T extends BaseFieldValidator> extends ValidatorBuilder {
    T notNull();

    T alwaysNull();

    List<ConstraintViolation> validate();
}
