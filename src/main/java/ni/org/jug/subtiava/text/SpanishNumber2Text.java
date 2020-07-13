package ni.org.jug.subtiava.text;

import java.math.BigDecimal;
import java.util.StringJoiner;

/**
 * @author aalaniz
 * @version 1.0
 */
public class SpanishNumber2Text extends Number2Text {
    private static final String NUMBER_OUT_OF_BOUNDS = "El numero [%s] se encuentra fuera de rango [%d, %d]";

    public SpanishNumber2Text(long number) {
        super(number);
    }

    public SpanishNumber2Text(long number, NumberConversionCustomizer customizer) {
        super(number, customizer);
    }

    public SpanishNumber2Text(long number, int decimal, NumberConversionCustomizer customizer) {
        super(number, decimal, customizer);
    }

    public SpanishNumber2Text(BigDecimal amount) {
        super(amount);
    }

    public SpanishNumber2Text(BigDecimal amount, NumberConversionCustomizer customizer) {
        super(amount, customizer);
    }

    @Override
    protected String tenToTheTwelfthPower(int value, long module) {
        return value == 1 ? "billon" : "billones";
    }

    @Override
    protected String tenToTheNinthPower(int value, long module) {
        return module >= 1_000_000 ? "mil" : "mil millones";
    }

    @Override
    protected String tenToTheSixthPower(int value, long module) {
        return value == 1 ? "millon" : "millones";
    }

    @Override
    protected String tenToTheThirdPower(int value, long module) {
        return "mil";
    }

    @Override
    protected String unit(int value, long module, int index) {
        switch (value) {
            case 0:
                return "cero";
            case 1:
                return module == 0 && index == 0 ? "uno" : "un";
            case 2:
                return "dos";
            case 3:
                return "tres";
            case 4:
                return "cuatro";
            case 5:
                return "cinco";
            case 6:
                return "seis";
            case 7:
                return "siete";
            case 8:
                return "ocho";
            case 9:
                return "nueve";
            default:
                throw new IllegalArgumentException(String.format(NUMBER_OUT_OF_BOUNDS, value, 0, 9));
        }
    }

    @Override
    protected String ten(int value, long module, int index) {
        if (value >= 11 && value <= 15) {
            switch (value) {
                case 11:
                    return "once";
                case 12:
                    return "doce";
                case 13:
                    return "trece";
                case 14:
                    return "catorce";
                case 15:
                    return "quince";
                default:
                    throw new IllegalArgumentException();
            }
        } else {
            StringJoiner output = new StringJoiner(" ");
            int first = value / 10;
            int second = value % 10;
            switch (first) {
                case 1:
                    output.add("diez");
                    break;
                case 2:
                    output.add("veinte");
                    break;
                case 3:
                    output.add("treinta");
                    break;
                case 4:
                    output.add("cuarenta");
                    break;
                case 5:
                    output.add("cincuenta");
                    break;
                case 6:
                    output.add("sesenta");
                    break;
                case 7:
                    output.add("setenta");
                    break;
                case 8:
                    output.add("ochenta");
                    break;
                case 9:
                    output.add("noventa");
                    break;
                default:
                    throw new IllegalArgumentException(String.format(NUMBER_OUT_OF_BOUNDS, first, 1, 9));
            }
            if (second > 0) {
                output.add("y").add(unit(second, module, index));
            }
            return output.toString();
        }
    }

    @Override
    protected String hundred(int value, long module, int index) {
        StringJoiner output = new StringJoiner(" ");
        int first = value / 100;
        int second = value % 100;
        switch (first) {
            case 1:
                output.add(second == 0 ? "cien" : "ciento");
                break;
            case 2:
                output.add("doscientos");
                break;
            case 3:
                output.add("trescientos");
                break;
            case 4:
                output.add("cuatrocientos");
                break;
            case 5:
                output.add("quinientos");
                break;
            case 6:
                output.add("seiscientos");
                break;
            case 7:
                output.add("setecientos");
                break;
            case 8:
                output.add("ochocientos");
                break;
            case 9:
                output.add("novecientos");
                break;
            default:
                throw new IllegalArgumentException(String.format(NUMBER_OUT_OF_BOUNDS, first, 1, 9));
        }
        if (second > 0) {
            if (isUnit(second)) {
                output.add(unit(second, module, index));
            } else {
                output.add(ten(second, module, index));
            }
        }
        return output.toString();
    }

    @Override
    protected String withDecimalFraction() {
        return new StringBuilder("con ").append(getDecimal()).append("/100").toString();
    }
}
