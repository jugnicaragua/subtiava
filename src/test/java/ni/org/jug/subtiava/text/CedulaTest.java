package ni.org.jug.subtiava.text;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author aalaniz
 * @version 1.0
 */
public class CedulaTest {

    @Test
    void validate_ValidCedula_True() {
        assertTrue(Cedula.validate("2811311830009v"));
        assertTrue(Cedula.validate("2811311830009V"));
    }

    @Test
    void validate_InvalidCedula_False() {
        assertFalse(Cedula.validate(null));
        assertFalse(Cedula.validate(""));
        assertFalse(Cedula.validate(" "));
        assertFalse(Cedula.validate("1234567890    "));
        assertFalse(Cedula.validate("abcdefghijklmn"));
        assertFalse(Cedula.validate("this_is_just_a_test"));
        assertFalse(Cedula.validate("2811311830009f"));
        assertFalse(Cedula.validate("28113118300090"));
    }

    @Test
    void of_InvalidCedula_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Cedula.of("");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Cedula.of("2811311830009f");
        });
    }

    @Test
    void of_ValidCedula_SuccessfulObjectCreation() {
        Cedula cedula = Cedula.of("2811311830009v");
        assertEquals("281", cedula.townCode);
        assertEquals("131183", cedula.date);
        assertEquals("0009", cedula.consecutive);
        assertEquals('v', cedula.checkDigit);
        assertEquals("281-131183-0009v", cedula.getCedula('-'));

        cedula = Cedula.of("2811311830009V");
        assertEquals("281", cedula.townCode);
        assertEquals("131183", cedula.date);
        assertEquals("0009", cedula.consecutive);
        assertEquals('V', cedula.checkDigit);
        assertEquals("281-131183-0009V", cedula.getCedula('-'));
    }
}
