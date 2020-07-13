package ni.org.jug.subtiava.text;

/**
 * Se usa para describir lo que se esta transformando: ya sea un numero (denota cuantos digitos) o la magnitud del numero
 * que se esta procesando.
 *
 * @author aalaniz
 * @version 1.0
 */
public enum ConversionType {
    /**
     * El {@code grupo} que se esta convirtiendo esta compuesto por un digito.
     */
    UNIT,

    /**
     * El {@code grupo} que se esta convirtiendo esta compuesto por dos digitos.
     */
    TEN,

    /**
     * El {@code grupo} que se esta convirtiendo esta compuesto por tres digitos.
     */
    HUNDRED,

    /**
     * Se esta convirtiendo la magnitud del numero que se esta procesando. Estos son los posibles valores de magnitud:
     * billon, mil millon, millon, mil.
     */
    MAGNITUDE;
}
