package ni.org.jug.subtiava.validation;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

public class ValidatorTest {

    @Test
    void validate_ApplySomeConstraints_PojoWithDataIssues() {
        Student student = new Student(10, 0, "Armando");
        student.gender = "This is a simple test";

        Validator validator = new Validator(Locale.getDefault())
                .ofNumber(student::getAge)
                .min(12)
                .max(17)
                .notNull()
                .of(student::getClassesCount)
                .notNull()
                .positive()
                .ofString(student::getName)
                .notBlank()
                .minLength(15)
                .ofString(student::getGender)
                .notNull()
                .values(Gender.class)
                .instance();

        List<ConstraintViolation> violations = validator.validate();
        System.out.println(violations);
    }
}
