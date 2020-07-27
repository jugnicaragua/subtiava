package ni.org.jug.subtiava.validation;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidatorTest {

    @Test
    void validate_AlwaysNullAndNotNullFields_PojoWithDataIssues() {
        Student student = new Student("Duke", null, 25, Gender.MALE);
        List<ConstraintViolation> violations = new Validator()
                .ofString(student::getFirstName)
                .notNull()
                .ofString(student::getLastName) // last name is required
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
                .ofString(student::getMiddleName) // middle name must be null
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
                .ofString(student::getLastName) // last name not blank
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
                .minLength(10) // last name min 10 chars
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
    void validate_NumberBasedConstraintsAndReuseValidatorInstance_PojoWithDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        student.status = 5;
        Validator validator = new Validator()
                .ofNumber(student::getAge) // age must match range values
                .notNull()
                .min(12)
                .max(18)
                .ofNumber(student::getClassesCount)
                .notNull() // classes count must be not null
                .positive()
                .ofNumber(student::getStatus) // although status is not mandatory, validation is performed by the presence of a value
                .values(10, 20, 30) // status must match passed in options
                .instance();
        assertEquals(3, validator.validate().size());

        student.classesCount = 0; // classes count must be a positive value
        assertEquals(3, validator.validate().size());

        student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        student.classesCount = 0;
        student.payment = BigDecimal.ZERO;
        validator = new Validator()
                .ofNumber(student::getAge)
                .notNull()
                .min(12)
                .ofNumber(student::getClassesCount)
                .notNull()
                .positive() // classes count must be a positive value
                .ofNumber(student::getPayment)
                .positive() // payment must be a positive value
                .instance();
        assertEquals(2, validator.validate().size());

        student.payment = new BigDecimal("0.01"); // payment is now a positive value
        assertEquals(1, validator.validate().size());

        student.payment = new BigDecimal("1250.55"); // payment is out of range
        List<ConstraintViolation> violations = new Validator()
                .ofNumber(student::getAge)
                .notNull()
                .positive()
                .min(30) // age must be at least 30 (this constraint overrides previous one)
                .ofNumber(student::getClassesCount)
                .notNull()
                .positive() // classes count must be a positive value
                .ofNumber(student::getPayment)
                .positive()
                .min(new BigDecimal("5000.00"))
                .max(new BigDecimal("10000.00"))
                .validate();
        assertEquals(3, violations.size());
    }

    @Test
    void validate_NumberBasedConstraintsAndReuseValidatorInstance_PojoWithoutDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        student.status = 20;
        student.classesCount = 5;
        student.payment = new BigDecimal("1000.00");
        Validator validator = new Validator()
                .ofNumber(student::getAge)
                .min(12)
                .ofNumber(student::getClassesCount)
                .positive()
                .ofNumber(student::getStatus)
                .values(10, 20, 30)
                .ofNumber(student::getPayment)
                .min(new BigDecimal("1000"))
                .instance();
        assertEquals(0, validator.validate().size());

        student.payment = new BigDecimal("3000.00");
        validator = new Validator()
                .ofNumber(student::getAge)
                .min(12)
                .ofNumber(student::getClassesCount)
                .positive()
                .ofNumber(student::getStatus)
                .values(10, 20, 30)
                .ofNumber(student::getPayment)
                .values(new BigDecimal("1000"), new BigDecimal("2000"), new BigDecimal("3000"))
                .instance();
        assertEquals(0, validator.validate().size());
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
