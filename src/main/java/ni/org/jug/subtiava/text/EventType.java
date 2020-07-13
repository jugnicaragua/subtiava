package ni.org.jug.subtiava.text;

/**
 * Describe si el evento sucede antes o despues de una operacion de transformacion.
 *
 * @author aalaniz
 * @version 1.0
 */
public enum EventType {
    /**
     * Antes de realizar alguna operacion de transformacion (ya sea convertir el {@code grupo} o la magnitud).
     */
    BEFORE,

    /**
     * Despues de realizar alguna operacion de transformacion (ya sea convertir el {@code grupo} o la magnitud).
     */
    AFTER;
}
