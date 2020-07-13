package ni.org.jug.subtiava.text;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * @author aalaniz
 * @version 1.0
 */
public final class Cedula {
    public static final int NATIONAL_ID_LENGTH = 14;
    public static final String VALID_CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXY";

    public final String cedula;
    public final String townCode;
    public final String date;
    public final String consecutive;
    public final char checkDigit;

    private Cedula(String cedula) {
        int offset;
        this.cedula = cedula;
        this.townCode = Strings.first(cedula, 3);
        this.date = Strings.substring(cedula, 6, (offset = this.townCode.length()));
        this.consecutive = Strings.substring(cedula, 4, (offset += this.date.length()));
        this.checkDigit = Strings.last(cedula);
    }

    public String getCedula(char separator) {
        return new StringBuilder(NATIONAL_ID_LENGTH + 2) // 2 separators for 3 different sections
                .append(townCode)
                .append(separator)
                .append(date)
                .append(separator)
                .append(consecutive).append(checkDigit)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cedula cedula = (Cedula) o;
        return townCode.equals(cedula.townCode) && date.equals(cedula.date) && consecutive.equals(cedula.consecutive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(townCode, date, consecutive);
    }

    public static boolean validate(String cedula) {
        if (cedula == null || cedula.isEmpty() || cedula.length() != NATIONAL_ID_LENGTH) {
            return false;
        }
        char[] chars = cedula.toCharArray();
        for (int i = 0; i < NATIONAL_ID_LENGTH; i++) {
            if (i >= 0 && i <= NATIONAL_ID_LENGTH - 2) {
                if (!Character.isDigit(chars[i])) {
                    return false;
                }
            } else {
                char last = chars[i];
                if (Character.isDigit(last)) {
                    return false;
                } else if (Character.isLetter(last)) {
                    if (VALID_CHARACTERS.indexOf(Character.toUpperCase(last)) == -1) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }

        // Apply date validation
        String shortIsoDate = shortIsoDate(cedula);
        String firstDate = new StringBuilder(10).append("19").append(shortIsoDate).toString();
        String secondDate = new StringBuilder(10).append("20").append(shortIsoDate).toString();
        boolean isIsoDate;

        isIsoDate = isIsoDate(firstDate);
        if (!isIsoDate) {
            isIsoDate = isIsoDate(secondDate);
        }
        if (!isIsoDate) {
            return false;
        }

        // Apply verification digit validation
        long base = Long.parseLong(Strings.first(cedula, NATIONAL_ID_LENGTH - 1));
        int index = (int) (base % VALID_CHARACTERS.length());
        char expectedCheckDigit = VALID_CHARACTERS.charAt(index);
        return expectedCheckDigit == Character.toUpperCase(Strings.last(cedula));
    }

    private static String shortIsoDate(String cedula) {
        char[] date = Strings.substring(cedula, 6, 3).toCharArray();
        return new StringBuilder(8)
                .append(date[4]).append(date[5]) // year
                .append('-')
                .append(date[2]).append(date[3]) // month
                .append('-')
                .append(date[0]).append(date[1]) // day
                .toString();
    }

    private static boolean isIsoDate(String aDate) {
        try {
            LocalDate.parse(aDate);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    public static Cedula of(String cedula) {
        if (!validate(cedula)) {
            throw new IllegalArgumentException("La cedula no es valida");
        }
        return new Cedula(cedula);
    }
}
