package ni.org.jug.subtiava.validation;

enum ConstraintFieldToken {
    POJO_TOKEN("${pojo}"),
    FIELD_TOKEN("${fieldName}"),
    LENGTH_TOKEN("${length}"),
    NUMBER_TOKEN("${number}"),
    OPTIONS_TOKEN("${options}"),
    AGE_TOKEN("${age}"),
    YEAR_TOKEN("${year}"),
    MONTH_TOKEN("${month}"),
    DAY_TOKEN("${day}");

    final String token;

    ConstraintFieldToken(String token) {
        this.token = token;
    }

    ReservedWord asWord(Object value) {
        return new ReservedWord(this, value);
    }
}
