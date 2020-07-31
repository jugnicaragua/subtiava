package ni.org.jug.subtiava.validation;

import java.time.Month;

/**
 * @author aalaniz
 * @version 1.0
 */
public interface DateFieldValidator<T extends DateFieldValidator> extends BaseFieldValidator<T> {
    @Override
    T notNull();

    @Override
    T alwaysNull();

    T past();

    T pastOrPresent();

    T future();

    T futureOrPresent();

    T age(int min, int max);

    default T age(int value) {
        return age(value, value);
    }

    T year(int min, int max);

    default T year(int value) {
        return year(value, value);
    }

    default T month(int min, int max) {
        return month(Month.of(min), Month.of(max));
    }

    default T month(int value) {
        return month(value, value);
    }

    default T month(Month value) {
        return month(value, value);
    }

    T month(Month min, Month max);

    T day(int min, int max);

    default T day(int value) {
        return day(value, value);
    }
}
