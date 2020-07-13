package ni.org.jug.subtiava.text;

import java.math.BigDecimal;
import java.util.StringJoiner;

/**
 * @author aalaniz
 * @version 1.0
 */
public class EnglishNumber2Text extends Number2Text {
    private static final String NUMBER_OUT_OF_BOUNDS = "Number [%s] is out of bounds [%d, %d]";

    public EnglishNumber2Text(long number) {
        super(number);
    }

    public EnglishNumber2Text(long number, NumberConversionCustomizer customizer) {
        super(number, customizer);
    }

    public EnglishNumber2Text(long number, int decimal, NumberConversionCustomizer customizer) {
        super(number, decimal, customizer);
    }

    public EnglishNumber2Text(BigDecimal amount) {
        super(amount);
    }

    public EnglishNumber2Text(BigDecimal amount, NumberConversionCustomizer customizer) {
        super(amount, customizer);
    }

    @Override
    protected String tenToTheTwelfthPower(int value, long module) {
        return "trillion";
    }

    @Override
    protected String tenToTheNinthPower(int value, long module) {
        return "billion";
    }

    @Override
    protected String tenToTheSixthPower(int value, long module) {
        return "million";
    }

    @Override
    protected String tenToTheThirdPower(int value, long module) {
        return "thousand";
    }

    @Override
    protected String unit(int value, long module, int index) {
        switch (value) {
            case 0:
                return "zero";
            case 1:
                return "one";
            case 2:
                return "two";
            case 3:
                return "three";
            case 4:
                return "four";
            case 5:
                return "five";
            case 6:
                return "six";
            case 7:
                return "seven";
            case 8:
                return "eight";
            case 9:
                return "nine";
            default:
                throw new IllegalArgumentException(String.format(NUMBER_OUT_OF_BOUNDS, value, 0, 9));
        }
    }

    @Override
    protected String ten(int value, long module, int index) {
        if (value >= 11 && value <= 19) {
            if (value == 11 || value == 12 || value == 13 || value == 15 || value == 18) {
                switch (value) {
                    case 11:
                        return "eleven";
                    case 12:
                        return "twelve";
                    case 13:
                        return "thirteen";
                    case 15:
                        return "fifteen";
                    case 18:
                        return "eighteen";
                    default:
                        throw new IllegalArgumentException();
                }
            } else {
                int second = value % 10;
                return new StringBuilder(unit(second, module, index)).append("teen").toString();
            }
        } else {
            StringBuilder output = new StringBuilder();
            int first = value / 10;
            int second = value % 10;
            switch (first) {
                case 1:
                    output.append("ten");
                    break;
                case 2:
                    output.append("twenty");
                    break;
                case 3:
                    output.append("thirty");
                    break;
                case 4:
                    output.append("forty");
                    break;
                case 5:
                    output.append("fifty");
                    break;
                case 6:
                    output.append("sixty");
                    break;
                case 7:
                    output.append("seventy");
                    break;
                case 8:
                    output.append("eighty");
                    break;
                case 9:
                    output.append("ninety");
                    break;
                default:
                    throw new IllegalArgumentException(String.format(NUMBER_OUT_OF_BOUNDS, first, 1, 9));
            }
            if (second > 0) {
                output.append("-").append(unit(second, module, index));
            }
            return output.toString();
        }
    }

    @Override
    protected String hundred(int value, long module, int index) {
        StringJoiner output = new StringJoiner(" ");
        int first = value / 100;
        int second = value % 100;

        output.add(unit(first, module, index)).add("hundred");
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
        return new StringBuilder("with ").append(getDecimal()).append("/100").toString();
    }
}
