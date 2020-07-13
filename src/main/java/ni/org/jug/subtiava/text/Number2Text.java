package ni.org.jug.subtiava.text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.StringJoiner;
import java.util.function.BiFunction;

/**
 * <p>
 * Define un punto de abstraccion para estandarizar la conversion de un numero a texto sin depender en el idioma.
 * Las operaciones abstractas declaradas en esta clase se usaran como base para obtener la traduccion de un numero a un idioma
 * en particular. Las implementaciones de esta clase estaran a cargo de proveer dichas traducciones y para facilitar este
 * proceso, los metodos abstractos recibiran como parametros la informacion contextual acerca del proceso de conversion. Tomar
 * en cuenta que estos metodos abstractos podran ser invocados varias veces en dependencia de la cantidad de digitos que compongan
 * el numero a convertir.
 * </p>
 * <p>
 * El algoritmo de conversion usa el numero original y calcula grupos (los cuales estan compuestos de 1 a 3 digitos). La agrupacion
 * del numero en trios se realiza de derecha a izquierda de la misma manera que se usan los separadores de miles para marcar los
 * grupos en un numero.
 * </p>
 * <p>
 * Se calcula el 1er grupo, se convierte a texto, se obtiene la magnitud del numero (si es mil, millon, etc), se agrega este resultado
 * parcial a un {@link StringBuilder} y se resta el valor de este grupo del numero original. Se repite el proceso anterior hasta dejar
 * en cero el numero.
 * </p>
 * <p>
 * Por ejemplo: para convertir el numero 4,525 a texto, la clase realiza lo siguiente: se obtiene el 1er grupo (valor = 4).
 * Se transforma el valor de este grupo, lo cual produce el texto {@code cuatro}; se determina su magnitud o la cantidad de ceros
 * a la derecha del grupo, en este caso hay 3 ceros (o digitos), lo cual produce el texto {@code mil}. Se resta el valor de este grupo
 * multiplicado por su magnitud del numero original: {@code 4,525 - 4 * 1,000 = 525}. El valor resultante se somete al mismo
 * proceso de conversion y se termina el proceso hasta dejar el numero en 0. Continuando con el ejemplo, el siguiente grupo es 525.
 * En este caso, solo se necesitaron 2 iteraciones para convertir el numero a texto.
 * </p>
 * <p>
 * Esta clase y sus implementaciones son <strong>threadsafe</strong>.
 * </p>
 *
 * @author aalaniz
 * @version 1.0
 */
public abstract class Number2Text {
    /**
     * Valor maximo del numero a convertir.
     */
    public static final long MAX_VALUE = 999_999_999_999_999l;

    /**
     * El valor 100.
     */
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    /**
     * Implementacion sin comportamiento de la interfaz funcional {@link NumberConversionCustomizer}.
     */
    private static final NumberConversionCustomizer NO_OP_CUSTOMIZER = (eventType, conversionType, event, output) -> {};

    /**
     * Instancia de {@link NumberConversionCustomizer} para personalizar el texto resultante de la conversion.
     */
    private final NumberConversionCustomizer customizer;

    /**
     * Numero a convertir.
     */
    private final long number;

    /**
     * Fraccion o parte decimal del numero. Si su valor es mayor que -1, se incluira en el texto final.
     */
    private final int decimal;

    /**
     * Crea una nueva instancia a partir de un numero.
     *
     * @param number El numero a convertir
     */
    protected Number2Text(long number) {
        this(number, null);
    }

    /**
     * Crea una nueva instancia a partir de un numero y una instancia de {@link NumberConversionCustomizer}.
     *
     * @param number El numero a convertir
     * @param customizer Instancia de {@link NumberConversionCustomizer}
     */
    protected Number2Text(long number, NumberConversionCustomizer customizer) {
        this(number, -1, customizer);
    }

    /**
     * Crea una nueva instancia a partir de un numero, la parte decimal y una instancia de {@link NumberConversionCustomizer}.
     *
     * @param number El numero a convertir
     * @param decimal La parte decimal del numero
     * @param customizer Instancia de {@link NumberConversionCustomizer}
     * @throws IllegalArgumentException Si el numero es menor que cero o la parte decimal es menor que -1
     */
    protected Number2Text(long number, int decimal, NumberConversionCustomizer customizer) {
        if (number < 0) {
            throw new IllegalArgumentException("Number [" + number + "] must be greater than or equal to zero");
        }
        if (decimal < -1) {
            throw new IllegalArgumentException("Decimal fraction [" + decimal + "] must be greater than or equal to zero");
        }
        this.number = number;
        this.decimal = decimal;
        this.customizer = customizer == null ? NO_OP_CUSTOMIZER : customizer;
    }

    /**
     * Crea una nueva instancia a partir de un numero de tipo {@link BigDecimal}.
     *
     * @param amount El numero a convertir
     */
    protected Number2Text(BigDecimal amount) {
        this(amount, null);
    }

    /**
     * Crea una nueva instancia a partir de un numero de tipo {@link BigDecimal} y una instancia de {@link NumberConversionCustomizer}.
     *
     * @param amount El numero a convertir
     * @param customizer Instancia de {@link NumberConversionCustomizer}
     */
    protected Number2Text(BigDecimal amount, NumberConversionCustomizer customizer) {
        this(amount.longValue(), amount
                .setScale(2, RoundingMode.HALF_UP)
                .remainder(BigDecimal.ONE)
                .multiply(ONE_HUNDRED)
                .intValue(), customizer);
    }

    public final long getNumber() {
        return number;
    }

    public final int getDecimal() {
        return decimal;
    }

    private String resolveNumber(int value, long module, int index) {
        NumberConversionEvent event = new NumberConversionEvent(value, module, index);
        StringBuilder output = new StringBuilder();

        if (isUnit(value)) {
            customizer.onConversion(EventType.BEFORE, ConversionType.UNIT, event, output);
            output.append(unit(value, module, index));
            customizer.onConversion(EventType.AFTER, ConversionType.UNIT, event, output);
        } else if (isTen(value)) {
            customizer.onConversion(EventType.BEFORE, ConversionType.TEN, event, output);
            output.append(ten(value, module, index));
            customizer.onConversion(EventType.AFTER, ConversionType.TEN, event, output);
        } else {
            customizer.onConversion(EventType.BEFORE, ConversionType.HUNDRED, event, output);
            output.append(hundred(value, module, index));
            customizer.onConversion(EventType.AFTER, ConversionType.HUNDRED, event, output);
        }

        customizer.onConversion(EventType.BEFORE, ConversionType.MAGNITUDE, event, output);
        String magnitude = magnitude(value, module, index);
        if (magnitude != null) {
            output.append(' ').append(magnitude);
        }
        customizer.onConversion(EventType.AFTER, ConversionType.MAGNITUDE, event, output);

        return output.toString();
    }

    /**
     * Devuelve {@code true} si el numero se encuentra entre 0 y 9.
     *
     * @param value El numero
     * @return {@code true} si el numero se encuentra entre 0 y 9, {@code false} en caso contrario.
     */
    public static boolean isUnit(int value) {
        return value >= 0 && value < 10;
    }

    /**
     * Devuelve {@code true} si el numero se encuentra entre 10 y 99.
     *
     * @param value El numero
     * @return {@code true} si el numero se encuentra entre 10 y 99, {@code false} en caso contrario.
     */
    public static boolean isTen(int value) {
        return value >= 10 && value < 100;
    }

    /**
     * Devuelve {@code true} si el numero se encuentra entre 100 y 999.
     *
     * @param value El numero
     * @return {@code true} si el numero se encuentra entre 100 y 999, {@code false} en caso contrario.
     */
    public static boolean isHundred(int value) {
        return value >= 100 && value < 1000;
    }

    public String magnitude(int value, long module, int index) {
        if (index == 12) {
            return tenToTheTwelfthPower(value, module);
        } else if (index == 9) {
            return tenToTheNinthPower(value, module);
        } else if (index == 6) {
            return tenToTheSixthPower(value, module);
        } else if (index == 3) {
            return tenToTheThirdPower(value, module);
        } else {
            return null;
        }
    }

    /**
     * Devuelve la traduccion de billon. La representacion de un billon es: {@code grupo * 10^12}.
     *
     * @param value El valor de un grupo: numero compuesto de 1 a 3 digitos. Posibles valores oscilan entre 1 a 999
     * @param module El residuo de dividir el numero que se esta procesando entre 10^12
     * @return Traduccion de la palabra billon en el idioma de la clase hija
     */
    protected abstract String tenToTheTwelfthPower(int value, long module);

    /**
     * Devuelve la traduccion de mil millon. La representacion de un mil millon es: {@code grupo * 10^9}.
     *
     * @param value El valor de un grupo: numero compuesto de 1 a 3 digitos. Posibles valores oscilan entre 1 a 999
     * @param module El residuo de dividir el numero que se esta procesando entre 10^9
     * @return Traduccion de la palabra mil millon en el idioma de la clase hija
     */
    protected abstract String tenToTheNinthPower(int value, long module);

    /**
     * Devuelve la traduccion de millon. La representacion de un millon es: {@code grupo * 10^6}.
     *
     * @param value El valor de un grupo: numero compuesto de 1 a 3 digitos. Posibles valores oscilan entre 1 a 999
     * @param module El residuo de dividir el numero que se esta procesando entre 10^6
     * @return Traduccion de la palabra millon en el idioma de la clase hija
     */
    protected abstract String tenToTheSixthPower(int value, long module);

    /**
     * Devuelve la traduccion de mil. La representacion de un mil es: {@code grupo * 10^3}.
     *
     * @param value El valor de un grupo: numero compuesto de 1 a 3 digitos. Posibles valores oscilan entre 1 a 999
     * @param module El residuo de dividir el numero que se esta procesando entre 10^3
     * @return Traduccion de la palabra mil en el idioma de la clase hija
     */
    protected abstract String tenToTheThirdPower(int value, long module);

    /**
     * Devuelve la traduccion de un numero cuyos valores oscilan entre 0 a 9.
     *
     * @param value El valor de un grupo: numeros de un solo digito
     * @param module El residuo de dividir el numero que se esta procesando entre 10^index
     * @param index La magnitud o cantidad de ceros a la derecha del grupo. Posibles valores: 0, 3, 6, 9, 12
     * @return Traduccion del numero en el idioma de la clase hija
     */
    protected abstract String unit(int value, long module, int index);

    /**
     * Devuelve la traduccion de un numero cuyos valores oscilan entre 10 a 99.
     *
     * @param value El valor de un grupo: numeros de dos digitos
     * @param module El residuo de dividir el numero que se esta procesando entre 10^index
     * @param index La magnitud o cantidad de ceros a la derecha del grupo. Posibles valores: 0, 3, 6, 9, 12
     * @return Traduccion del numero en el idioma de la clase hija
     */
    protected abstract String ten(int value, long module, int index);

    /**
     * Devuelve la traduccion de un numero cuyos valores oscilan entre 100 a 999.
     *
     * @param value El valor de un grupo: numeros de tres digitos
     * @param module El residuo de dividir el numero que se esta procesando entre 10^index
     * @param index La magnitud o cantidad de ceros a la derecha del grupo. Posibles valores: 0, 3, 6, 9, 12
     * @return Traduccion del numero en el idioma de la clase hija
     */
    protected abstract String hundred(int value, long module, int index);

    /**
     * Devuelve la conversion de la parte decimal a texto. Se delega la representacion de este valor a los implementadores
     * con el fin de tener la flexibilidad de expresar este valor en cualquier formato. Una manera de representar esta parte
     * podria ser: {@code con 45/100}.
     *
     * @return La parte decimal en texto
     */
    protected abstract String withDecimalFraction();

    /**
     * Convierte el numero a texto. La conversion a texto se realiza cada vez que se invoca este metodo. Este metodo calcula los
     * grupos que componen el numero original y uno a uno los transforma a texto. Este metodo acepta 2 parametros que permiten
     * modificar el valor devuelto.
     *
     * @param formatter Interfaz funcional que permite modificar el resultado final. Recibe como parametros el numero convertido
     *                  a texto y la parte decimal. El usuario es libre de retornar lo que desea
     * @param throwExceptionIfMaxExceeded Se usa este parametro si y solo si el numero a convertir excede {@link #MAX_VALUE}.
     *                                    {@code true} para lanzar un error en caso de exceder el maximo o {@code false} para
     *                                    agregar el numero al resultado final sin realizar conversion
     * @return El numero en texto o el numero original si excede {@link #MAX_VALUE}
     * @throws IllegalArgumentException Si el numero excede {@link #MAX_VALUE} y el parametro {@code throwExceptionIfMaxExceeded}
     *                                  es {@code true}
     */
    public final String toText(BiFunction<String, Integer, CharSequence> formatter, boolean throwExceptionIfMaxExceeded) {
        StringJoiner letter = new StringJoiner(" ");

        if (number > MAX_VALUE) {
            if (throwExceptionIfMaxExceeded) {
                throw new IllegalArgumentException("Number [" + number + "] is greater than the maximum value allowed for conversion");
            } else {
                letter.add(Long.toString(number));
            }
        } else {
            long currentNumber = number;
            long magnitude = -1;
            long result = -1;
            long module = -1;

            for (int i = 0; i <= 12; i += 3) {
                magnitude = (long) Math.pow(10, i); // 10^i -> 10^0 = 1, 10^3 = 1000,...etc
                result = currentNumber / magnitude;
                if (result >= 0 && result < 1000) {
                    module = currentNumber % magnitude;
                    letter.add(resolveNumber((int) result, module, i));

                    // Reset for next iteration
                    currentNumber -= result * magnitude;
                    if (currentNumber == 0) {
                        break;
                    }
                    i = -3;
                }
            }
        }

        if (formatter == null) {
            if (decimal != -1) {
                letter.add(withDecimalFraction());
            }
            return letter.toString();
        } else {
            return formatter.apply(letter.toString(), decimal).toString();
        }
    }

    public final String toText() {
        return toText(null, false);
    }

    public final String toText(BiFunction<String, Integer, CharSequence> formatter) {
        return toText(formatter, false);
    }

    public final String toText(boolean throwExceptionIfMaxExceeded) {
        return toText(null, throwExceptionIfMaxExceeded);
    }
}
