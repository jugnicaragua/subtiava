package ni.org.jug.subtiava.validation;

import java.util.regex.Pattern;

/**
 * @author aalaniz
 * @version 1.0
 */
public interface StringFieldValidator<T extends StringFieldValidator> extends BaseFieldValidator<T> {
    @Override
    T notNull();

    @Override
    T alwaysNull();

    T minLength(int value);

    T maxLength(int value);

    T notEmpty();

    T notBlank();

    default T values(String value) {
        return values(value, null, null);
    }

    default T values(String first, String second) {
        return values(first, second, null);
    }

    T values(String first, String second, String... moreOptions);

    default T values(Enum value) {
        return values(value, null, null);
    }

    default T values(Enum first, Enum second) {
        return values(first, second, null);
    }

    T values(Enum first, Enum second, Enum... moreOptions);

    default T values(Class<Enum> enumType) {
        return values(null, null, enumType.getEnumConstants());
    }

    T regex(String pattern);

    T regex(Pattern pattern);
}
