package ni.org.jug.subtiava.validation;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidatorTest {

    @Test
    void validate_AlwaysNullAndNotNullFields_PojoWithDataIssues() {
        Student student = new Student("Duke", null, 25, Gender.MALE);
        List<ConstraintViolation> violations = new Validator()
                .ofString(student::getFirstName)
                .notNull()
                .ofString(student::getLastName) // lastname is required
                .notNull()
                .ofNumber(student::getAge)
                .notNull()
                .ofString(student::getGender)
                .notNull()
                .validate();
        assertEquals(1, violations.size());

        student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        student.middleName = "Java";
        violations = new Validator()
                .ofString(student::getFirstName)
                .notNull()
                .ofString(student::getMiddleName) // middleName must be null
                .alwaysNull()
                .ofString(student::getLastName)
                .notNull()
                .ofNumber(student::getAge)
                .notNull()
                .ofString(student::getGender)
                .notNull()
                .validate();
        assertEquals(1, violations.size());
    }

    @Test
    void validate_AlwaysNullAndNotNullFields_PojoWithoutDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        List<ConstraintViolation> violations = new Validator()
                .ofString(student::getFirstName)
                .notNull()
                .ofString(student::getMiddleName)
                .alwaysNull()
                .ofString(student::getLastName)
                .notNull()
                .ofNumber(student::getAge)
                .notNull()
                .ofString(student::getGender)
                .notNull()
                .validate();
        assertEquals(0, violations.size());
    }

    @Test
    void validate_NotEmptyAndNotBlankFields_PojoWithDataIssues() {
        Student student = new Student("Duke", "     ", 25, Gender.MALE);
        student.middleName = "             ";
        List<ConstraintViolation> violations = new Validator()
                .ofString(student::getFirstName)
                .notBlank()
                .ofString(student::getMiddleName)
                .notEmpty()
                .ofString(student::getLastName) // lastname not blank
                .notBlank()
                .ofNumber(student::getAge)
                .notNull()
                .ofString(student::getGender)
                .notNull()
                .validate();
        assertEquals(1, violations.size());
    }

    @Test
    void validate_NotEmptyAndNotBlankFields_PojoWithoutDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        student.middleName = "             ";
        List<ConstraintViolation> violations = new Validator()
                .ofString(student::getFirstName)
                .notBlank()
                .ofString(student::getMiddleName)
                .notEmpty()
                .ofString(student::getLastName)
                .notBlank()
                .ofNumber(student::getAge)
                .notNull()
                .ofString(student::getGender)
                .notNull()
                .validate();
        assertEquals(0, violations.size());
    }

    @Test
    void validate_StringBasedConstraints_PojoWithDataIssues() {
        Student student = new Student("Duke2020", "Nicaragua", 25, "Duke Gender");
        List<ConstraintViolation> violations = new Validator()
                .ofString(student::getFirstName)
                .notBlank()
                .minLength(3)
                .regex("^[A-Za-z ]+") // first name must match pattern
                .ofString(student::getMiddleName)
                .alwaysNull()
                .ofString(student::getLastName)
                .notBlank()
                .minLength(10) // lastname min 10 chars
                .maxLength(50)
                .ofNumber(student::getAge)
                .notNull()
                .ofString(student::getGender)
                .notNull()
                .values(Gender.class) // gender must match gender enum values
                .validate();
        assertEquals(3, violations.size());

        student = new Student("Duke", null, 25, "Duke Gender");
        student.middleName = "JUG2020";
        violations = new Validator()
                .ofString(student::getFirstName)
                .notBlank()
                .minLength(3)
                .regex("^[A-Za-z ]+")
                .ofString(student::getMiddleName)
                .alwaysNull() // middle name must be null
                .regex("^[A-Za-z ]+") // constraint must be omitted
                .ofString(student::getLastName)
                .minLength(10)
                .maxLength(50)
                .regex("^[A-Za-z ]+")
                .ofNumber(student::getAge)
                .notNull()
                .ofString(student::getGender)
                .notNull()
                .values(Gender.MALE.name(), Gender.FEMALE.name()) // gender must match passed in string values
                .validate();
        assertEquals(2, violations.size());
    }

    @Test
    void validate_StringBasedConstraints_PojoWithoutDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        List<ConstraintViolation> violations = new Validator()
                .ofString(student::getFirstName)
                .notBlank()
                .minLength(3)
                .maxLength(50)
                .regex("^[A-Za-z ]+")
                .ofString(student::getMiddleName)
                .alwaysNull()
                .ofString(student::getLastName)
                .notBlank()
                .minLength(5)
                .maxLength(50)
                .regex("^[A-Za-z ]+")
                .ofNumber(student::getAge)
                .notNull()
                .ofString(student::getGender)
                .notNull()
                .values(Gender.class)
                .validate();
        assertEquals(0, violations.size());
    }

    @Test
    void validate_ApplySomeConstraints_PojoWithDataIssues() {
        Student student = new Student();
        student.firstName = "Armando Jose 123";
        student.age = 10;
        student.classesCount = 0;
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
                .ofString(student::getFirstName, "name")
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
