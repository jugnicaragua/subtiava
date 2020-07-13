package ni.org.jug.subtiava.text;

/**
 * Representa un evento de conversion de un numero (grupo) a texto.
 *
 * @author aalaniz
 * @version 1.0
 */
public final class NumberConversionEvent {

    /**
     * Numero cuyos valores oscilan entre 0 a 999.
     */
    public final int value;

    /**
     * El residuo de dividir el numero que se esta procesando entre 10^index.
     */
    public final long module;

    /**
     * La magnitud o cantidad de ceros a la derecha del grupo. Posibles valores: 0, 3, 6, 9, 12.
     */
    public final int index;

    public NumberConversionEvent(int value, long module, int index) {
        this.value = value;
        this.module = module;
        this.index = index;
    }
}
