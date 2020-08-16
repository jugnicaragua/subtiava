package ni.org.jug.subtiava.validation;

/**
 * @author aalaniz
 * @version 1.0
 */
final class ConstraintCoordinate {
    final String pojo;
    final String field;

    ConstraintCoordinate(String pojo, String field) {
        this.pojo = pojo;
        this.field = field;
    }

    ReservedWord asPojoReservedWord() {
        return new ReservedWord(ConstraintFieldToken.POJO_TOKEN, pojo);
    }

    ReservedWord asFieldReservedWord() {
        return new ReservedWord(ConstraintFieldToken.FIELD_TOKEN, field);
    }
}
