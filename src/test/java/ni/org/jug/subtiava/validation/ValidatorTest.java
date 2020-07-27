package ni.org.jug.subtiava.validation;

import org.junit.jupiter.api.Test;

import java.util.List;

public class ValidatorTest {

    @Test
    void validate_ApplySomeConstraints_PojoWithDataIssues() {
        Student student = new Student(10, 0, "Armando Jose 123");
        student.gender = "This is a simple test";
        student.status = 15;

        Validator validator = new Validator("student")
                .ofNumber(student::getAge, "age")
                .min(12)
                .max(17)
                .notNull()
                .of(student::getClassesCount)
                .notNull()
                .positive()
                .ofString(student::getName, "name")
                .notBlank()
                .minLength(10)
                .maxLength(100)
                .regex("^[A-Za-z ]+")
                .ofString(student::getGender)
                .notNull()
                .values(Gender.class)
                .ofNumber(student::getStatus, "status")
                .notNull()
                .values(10, 20)
                .instance();

        List<ConstraintViolation> violations = validator.validate();
        System.out.println(violations);
    }
}
