package ni.org.jug.subtiava.text;

/**
 * @author aalaniz
 * @version 1.0
 */
public final class Strings {
    private String string;

    private Strings(String string) {
        this.string = string;
    }

    // -------------------------------------------------------------------------------------------
    // Instance methods wrapping static methods
    // -------------------------------------------------------------------------------------------

    public Strings first(int length) {
        string = first(string, length);
        return this;
    }

    public Strings substring(int length, int offset) {
        string = substring(string, length, offset);
        return this;
    }

    public String value() {
        return string;
    }

    // -------------------------------------------------------------------------------------------
    // Static helper methods
    // -------------------------------------------------------------------------------------------

    public static String first(String value, int length) {
        return value == null ? null : value.substring(0, length);
    }

    public static char last(String value) {
        return value == null || value.isEmpty() ? Character.MIN_VALUE : value.charAt(value.length() - 1);
    }

    public static String substring(String value, int length, int offset) {
        return value == null ? null : value.substring(offset, offset + length);
    }

    public static Strings of(String string) {
        return new Strings(string);
    }
}
