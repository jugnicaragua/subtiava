package ni.org.jug.subtiava.text;

/**
 * Representa un {@code listener} o {@code callback} a los eventos de conversion de un {@code grupo}. Esta interfaz permite
 * agregar dinamicamente comportamiento adicional al proceso de conversion. Este comportamiento sera definido externamente
 * por un cliente y le permitira realizar ajustes al texto producido por la conversion. Se puede usar para insertar caracteres
 * adicionales como una coma o palabras adicionales en cualquier parte del texto.
 *
 * @author aalaniz
 * @version 1.0
 */
@FunctionalInterface
public interface NumberConversionCustomizer {
    /**
     * Este metodo se invoca antes y despues de la traduccion/conversion de un {@code grupo}.
     *
     * @param eventType Describe el momento de la operacion (antes o despues)
     * @param conversionType Describe lo que se esta convirtiendo (ya sea un numero de 'n' digitos o la magnitud)
     * @param event Informacion contextual sobre el numero que se esta procesando
     * @param output Almacena el resultado de la conversion del {@code grupo} que se esta procesando
     */
    void onConversion(EventType eventType, ConversionType conversionType, NumberConversionEvent event, StringBuilder output);
}
