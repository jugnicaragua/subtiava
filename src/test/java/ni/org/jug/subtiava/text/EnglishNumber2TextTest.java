package ni.org.jug.subtiava.text;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author aalaniz
 * @version 1.0
 */
public class EnglishNumber2TextTest {

    @Test
    void toText_NumbersBetweenOneAndNine_SuccessfulConversion() {
        Number2Text converter = new EnglishNumber2Text(0);
        assertEquals("zero", converter.toText());

        converter = new EnglishNumber2Text(1);
        assertEquals("one", converter.toText());

        converter = new EnglishNumber2Text(9);
        assertEquals("nine", converter.toText());
    }

    @Test
    void toText_NumbersBetweenTenAndOneHundredExclusive_SuccessfulConversion() {
        Number2Text converter = new EnglishNumber2Text(10);
        assertEquals("ten", converter.toText());

        converter = new EnglishNumber2Text(13);
        assertEquals("thirteen", converter.toText());

        converter = new EnglishNumber2Text(15);
        assertEquals("fifteen", converter.toText());

        converter = new EnglishNumber2Text(17);
        assertEquals("seventeen", converter.toText());

        converter = new EnglishNumber2Text(18);
        assertEquals("eighteen", converter.toText());

        converter = new EnglishNumber2Text(40);
        assertEquals("forty", converter.toText());

        converter = new EnglishNumber2Text(59);
        assertEquals("fifty-nine", converter.toText());

        converter = new EnglishNumber2Text(73);
        assertEquals("seventy-three", converter.toText());

        converter = new EnglishNumber2Text(81);
        assertEquals("eighty-one", converter.toText());

        converter = new EnglishNumber2Text(99);
        assertEquals("ninety-nine", converter.toText());
    }

    @Test
    void toText_NumbersBetweenOneHundredAndOneThousandExclusive_SuccessfulConversion() {
        Number2Text converter = new EnglishNumber2Text(100);
        assertEquals("one hundred", converter.toText());

        converter = new EnglishNumber2Text(101);
        assertEquals("one hundred one", converter.toText());

        converter = new EnglishNumber2Text(114);
        assertEquals("one hundred fourteen", converter.toText());

        converter = new EnglishNumber2Text(123);
        assertEquals("one hundred twenty-three", converter.toText());

        converter = new EnglishNumber2Text(400);
        assertEquals("four hundred", converter.toText());

        converter = new EnglishNumber2Text(540);
        assertEquals("five hundred forty", converter.toText());

        converter = new EnglishNumber2Text(770);
        assertEquals("seven hundred seventy", converter.toText());

        converter = new EnglishNumber2Text(888);
        assertEquals("eight hundred eighty-eight", converter.toText());

        converter = new EnglishNumber2Text(933);
        assertEquals("nine hundred thirty-three", converter.toText());
    }

    @Test
    void toText_NumbersGreaterThanOrEqualToOneThousand_SuccessfulConversion() {
        Number2Text converter = new EnglishNumber2Text(5_456);
        assertEquals("five thousand four hundred fifty-six", converter.toText());

        converter = new EnglishNumber2Text(1_000);
        assertEquals("one thousand", converter.toText());

        converter = new EnglishNumber2Text(100_000);
        assertEquals("one hundred thousand", converter.toText());

        converter = new EnglishNumber2Text(101_000);
        assertEquals("one hundred one thousand", converter.toText());

        converter = new EnglishNumber2Text(33_977);
        assertEquals("thirty-three thousand nine hundred seventy-seven", converter.toText());

        converter = new EnglishNumber2Text(651_400);
        assertEquals("six hundred fifty-one thousand four hundred", converter.toText());

        converter = new EnglishNumber2Text(333_000_999);
        assertEquals("three hundred thirty-three million nine hundred ninety-nine", converter.toText());

        converter = new EnglishNumber2Text(21_001);
        assertEquals("twenty-one thousand one", converter.toText());

        converter = new EnglishNumber2Text(999_999);
        assertEquals("nine hundred ninety-nine thousand nine hundred ninety-nine", converter.toText());

        converter = new EnglishNumber2Text(1_000_000);
        assertEquals("one million", converter.toText());

        converter = new EnglishNumber2Text(1_153_625_999_567l);
        assertEquals("one trillion one hundred fifty-three billion six hundred twenty-five million nine hundred ninety-nine " +
                "thousand five hundred sixty-seven", converter.toText());

        converter = new EnglishNumber2Text(3_214_731);
        assertEquals("three million two hundred fourteen thousand seven hundred thirty-one", converter.toText());

        converter = new EnglishNumber2Text(15_711);
        assertEquals("fifteen thousand seven hundred eleven", converter.toText());

        converter = new EnglishNumber2Text(13_000);
        assertEquals("thirteen thousand", converter.toText());

        converter = new EnglishNumber2Text(5_000_000_000l);
        assertEquals("five billion", converter.toText());

        converter = new EnglishNumber2Text(5_001_000_000l);
        assertEquals("five billion one million", converter.toText());

        converter = new EnglishNumber2Text(5_000_020_000l);
        assertEquals("five billion twenty thousand", converter.toText());
    }

    @Test
    void toText_NumbersWithDecimalFraction_SuccessfulConversion() {
        Number2Text converter = new EnglishNumber2Text(new BigDecimal("45871.9444"));
        assertEquals("forty-five thousand eight hundred seventy-one with 94/100", converter.toText());

        converter = new EnglishNumber2Text(new BigDecimal("1000.745"));
        assertEquals("one thousand with 75/100", converter.toText());
    }

    @Test
    void toText_NumbersWithCustomFormatter_SuccessfulConversion() {
        Number2Text converter = new EnglishNumber2Text(new BigDecimal("87500.33"));
        BiFunction<String, Integer, CharSequence> formatter = (letter, decimal) -> {
            StringBuilder output = new StringBuilder(letter.length() + 50);
            output.append("** ").append(letter).append(" and ").append(decimal).append("/100 **");
            return output;
        };
        assertEquals("** eighty-seven thousand five hundred and 33/100 **", converter.toText(formatter));
    }

    @Test
    void toText_NumbersWithConversionCustomizer_SuccessfulConversion() {
        NumberConversionCustomizer conversionCustomizer = (eventType, conversionType, event, output) -> {
            if (eventType == EventType.BEFORE && conversionType != ConversionType.MAGNITUDE) {
                output.append("** ");
            }
        };
        Number2Text converter = new EnglishNumber2Text(34_899, conversionCustomizer);
        assertEquals("** thirty-four thousand ** eight hundred ninety-nine", converter.toText());

        converter = new EnglishNumber2Text(333, conversionCustomizer);
        assertEquals("** three hundred thirty-three", converter.toText());

        converter = new EnglishNumber2Text(1_001_899, conversionCustomizer);
        assertEquals("** one million ** one thousand ** eight hundred ninety-nine", converter.toText());
    }
}
