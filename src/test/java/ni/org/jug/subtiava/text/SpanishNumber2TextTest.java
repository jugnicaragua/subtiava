package ni.org.jug.subtiava.text;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author aalaniz
 * @version 1.0
 */
public class SpanishNumber2TextTest {

    @Test
    void toText_NumbersBetweenOneAndNine_SuccessfulConversion() {
        Number2Text converter = new SpanishNumber2Text(0);
        assertEquals("cero", converter.toText());

        converter = new SpanishNumber2Text(1);
        assertEquals("uno", converter.toText());

        converter = new SpanishNumber2Text(9);
        assertEquals("nueve", converter.toText());
    }

    @Test
    void toText_NumbersBetweenTenAndOneHundredExclusive_SuccessfulConversion() {
        Number2Text converter = new SpanishNumber2Text(10);
        assertEquals("diez", converter.toText());

        converter = new SpanishNumber2Text(13);
        assertEquals("trece", converter.toText());

        converter = new SpanishNumber2Text(15);
        assertEquals("quince", converter.toText());

        converter = new SpanishNumber2Text(17);
        assertEquals("diez y siete", converter.toText());

        converter = new SpanishNumber2Text(28);
        assertEquals("veinte y ocho", converter.toText());

        converter = new SpanishNumber2Text(40);
        assertEquals("cuarenta", converter.toText());

        converter = new SpanishNumber2Text(59);
        assertEquals("cincuenta y nueve", converter.toText());

        converter = new SpanishNumber2Text(73);
        assertEquals("setenta y tres", converter.toText());

        converter = new SpanishNumber2Text(81);
        assertEquals("ochenta y uno", converter.toText());

        converter = new SpanishNumber2Text(99);
        assertEquals("noventa y nueve", converter.toText());
    }

    @Test
    void toText_NumbersBetweenOneHundredAndOneThousandExclusive_SuccessfulConversion() {
        Number2Text converter = new SpanishNumber2Text(100);
        assertEquals("cien", converter.toText());

        converter = new SpanishNumber2Text(101);
        assertEquals("ciento uno", converter.toText());

        converter = new SpanishNumber2Text(114);
        assertEquals("ciento catorce", converter.toText());

        converter = new SpanishNumber2Text(123);
        assertEquals("ciento veinte y tres", converter.toText());

        converter = new SpanishNumber2Text(400);
        assertEquals("cuatrocientos", converter.toText());

        converter = new SpanishNumber2Text(540);
        assertEquals("quinientos cuarenta", converter.toText());

        converter = new SpanishNumber2Text(610);
        assertEquals("seiscientos diez", converter.toText());

        converter = new SpanishNumber2Text(770);
        assertEquals("setecientos setenta", converter.toText());

        converter = new SpanishNumber2Text(888);
        assertEquals("ochocientos ochenta y ocho", converter.toText());

        converter = new SpanishNumber2Text(933);
        assertEquals("novecientos treinta y tres", converter.toText());
    }

    @Test
    void toText_NumbersGreaterThanOrEqualToOneThousand_SuccessfulConversion() {
        Number2Text converter = new SpanishNumber2Text(5_456);
        assertEquals("cinco mil cuatrocientos cincuenta y seis", converter.toText());

        converter = new SpanishNumber2Text(1_000);
        assertEquals("un mil", converter.toText());

        converter = new SpanishNumber2Text(100_000);
        assertEquals("cien mil", converter.toText());

        converter = new SpanishNumber2Text(101_000);
        assertEquals("ciento un mil", converter.toText());

        converter = new SpanishNumber2Text(33_977);
        assertEquals("treinta y tres mil novecientos setenta y siete", converter.toText());

        converter = new SpanishNumber2Text(651_400);
        assertEquals("seiscientos cincuenta y un mil cuatrocientos", converter.toText());

        converter = new SpanishNumber2Text(333_000_999);
        assertEquals("trescientos treinta y tres millones novecientos noventa y nueve", converter.toText());

        converter = new SpanishNumber2Text(21_001);
        assertEquals("veinte y un mil uno", converter.toText());

        converter = new SpanishNumber2Text(999_999);
        assertEquals("novecientos noventa y nueve mil novecientos noventa y nueve", converter.toText());

        converter = new SpanishNumber2Text(1_000_000);
        assertEquals("un millon", converter.toText());

        converter = new SpanishNumber2Text(1_153_625_999_567l);
        assertEquals("un billon ciento cincuenta y tres mil seiscientos veinte y cinco millones novecientos noventa y nueve " +
                "mil quinientos sesenta y siete", converter.toText());

        converter = new SpanishNumber2Text(3_214_731);
        assertEquals("tres millones doscientos catorce mil setecientos treinta y uno", converter.toText());

        converter = new SpanishNumber2Text(15_711);
        assertEquals("quince mil setecientos once", converter.toText());

        converter = new SpanishNumber2Text(13_000);
        assertEquals("trece mil", converter.toText());

        converter = new SpanishNumber2Text(5_000_000_000l);
        assertEquals("cinco mil millones", converter.toText());

        converter = new SpanishNumber2Text(5_001_000_000l);
        assertEquals("cinco mil un millon", converter.toText());

        converter = new SpanishNumber2Text(5_000_020_000l);
        assertEquals("cinco mil millones veinte mil", converter.toText());
    }

    @Test
    void toText_NumbersWithDecimalFraction_SuccessfulConversion() {
        Number2Text converter = new SpanishNumber2Text(new BigDecimal("45871.9444"));
        assertEquals("cuarenta y cinco mil ochocientos setenta y uno con 94/100", converter.toText());

        converter = new SpanishNumber2Text(new BigDecimal("1000.745"));
        assertEquals("un mil con 75/100", converter.toText());
    }

    @Test
    void toText_NumbersWithCustomFormatter_SuccessfulConversion() {
        Number2Text converter = new SpanishNumber2Text(new BigDecimal("87500.33"));
        BiFunction<String, Integer, CharSequence> formatter = (letter, decimal) -> {
            StringBuilder output = new StringBuilder(letter.length() + 50);
            output.append("** ").append(letter).append(" con ").append(decimal).append("/100 **");
            return output;
        };
        assertEquals("** ochenta y siete mil quinientos con 33/100 **", converter.toText(formatter));
    }

    @Test
    void toText_NumbersWithConversionCustomizer_SuccessfulConversion() {
        NumberConversionCustomizer conversionCustomizer = (eventType, conversionType, event, output) -> {
            if (eventType == EventType.BEFORE && conversionType != ConversionType.MAGNITUDE) {
                output.append("** ");
            }
        };
        Number2Text converter = new SpanishNumber2Text(34_899, conversionCustomizer);
        assertEquals("** treinta y cuatro mil ** ochocientos noventa y nueve", converter.toText());

        converter = new SpanishNumber2Text(333, conversionCustomizer);
        assertEquals("** trescientos treinta y tres", converter.toText());

        converter = new SpanishNumber2Text(1_001_899, conversionCustomizer);
        assertEquals("** un millon ** un mil ** ochocientos noventa y nueve", converter.toText());
    }
}
