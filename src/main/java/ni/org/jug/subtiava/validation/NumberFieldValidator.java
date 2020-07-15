package ni.org.jug.subtiava.validation;

import java.math.BigDecimal;

/**
 * @author aalaniz
 * @version 1.0
 */
public interface NumberFieldValidator<T extends NumberFieldValidator> extends BaseFieldValidator<T> {
    @Override
    T notNull();

    @Override
    T alwaysNull();

    T min(long value);

    T max(long value);

    T min(BigDecimal value);

    T max(BigDecimal value);

    T positive();

    T negative();

    default T values(long value) {
        return values(value, -1, null);
    }

    default T values(long first, long second) {
        return values(first, second, null);
    }

    T values(long first, long second, long... moreOptions);

    default T values(BigDecimal value) {
        return values(value, null, null);
    }

    default T values(BigDecimal first, BigDecimal second) {
        return values(first, second, null);
    }

    T values(BigDecimal first, BigDecimal second, BigDecimal... moreOptions);
}
