package ni.org.jug.subtiava.validation;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidatorTest {

    @Test
    void validate_MissingLastName_PojoWithDataIssues() {
        Student student = new Student("Duke", null, 25, Gender.MALE);
        List<ConstraintViolation> violations = new Validator()
                .ofString(student::getFirstName)
                .notNull()
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
    void validate_NotNullMiddleName_PojoWithDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        student.middleName = "Java";
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
        assertEquals(1, violations.size());
    }

    @Test
    void validate_EmptyMiddleName_PojoWithDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        student.middleName = "";
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
        assertEquals(1, violations.size());
    }

    @Test
    void validate_BlankLastName_PojoWithDataIssues() {
        Student student = new Student("Duke", "     ", 25, Gender.MALE);
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
        assertEquals(1, violations.size());
    }

    @Test
    void validate_FirstNameWithInvalidPattern_PojoWithDataIssues() {
        Student student = new Student("Duke2020", "Nicaragua", 25, "Duke Gender");
        List<ConstraintViolation> violations = new Validator()
                .ofString(student::getFirstName)
                .notBlank()
                .regex("^[A-Za-z ]+")
                .ofString(student::getMiddleName)
                .alwaysNull()
                .ofString(student::getLastName)
                .notBlank()
                .ofNumber(student::getAge)
                .notNull()
                .validate();
        assertEquals(1, violations.size());
    }

    @Test
    void validate_LastNameTooShort_PojoWithDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 25, "Duke Gender");
        List<ConstraintViolation> violations = new Validator()
                .ofString(student::getFirstName)
                .notBlank()
                .minLength(3)
                .regex("^[A-Za-z ]+")
                .ofString(student::getMiddleName)
                .alwaysNull()
                .ofString(student::getLastName)
                .notBlank()
                .minLength(15)
                .maxLength(50)
                .ofNumber(student::getAge)
                .notNull()
                .validate();
        assertEquals(1, violations.size());
    }

    @Test
    void validate_InvalidGenderValue_PojoWithDataIssues() {
        Student student = new Student("Duke", null, null, "Duke Gender");
        List<ConstraintViolation> violations = new Validator()
                .ofString(student::getFirstName)
                .notBlank()
                .minLength(3)
                .regex("^[A-Za-z ]+")
                .ofString(student::getMiddleName)
                .alwaysNull()
                .ofString(student::getLastName)
                .minLength(10)
                .maxLength(50)
                .ofNumber(student::getAge)
                .min(10)
                .ofString(student::getGender)
                .notNull()
                .values(Gender.class)
                .validate();
        assertEquals(1, violations.size());
    }

    @Test
    void validate_AgeOutOfBounds_PojoWithDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        Validator validator = new Validator()
                .ofNumber(student::getAge)
                .notNull()
                .min(12)
                .max(18)
                .instance();
        assertEquals(1, validator.validate().size());
    }

    @Test
    void validate_NonPositiveClassesCount_PojoWithDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        student.classesCount = 0;
        Validator validator = new Validator()
                .ofNumber(student::getAge)
                .notNull()
                .min(12)
                .ofNumber(student::getClassesCount) // validation is performed by presence of a value
                .positive()
                .instance();
        assertEquals(1, validator.validate().size());
    }

    @Test
    void validate_InvalidStatusValue_PojoWithDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        student.classesCount = 3;
        student.status = 5;
        Validator validator = new Validator()
                .ofNumber(student::getAge)
                .notNull()
                .min(12)
                .ofNumber(student::getClassesCount)
                .notNull()
                .positive()
                .ofNumber(student::getStatus)
                .values(10, 20, 30) // status must match passed in options
                .instance();
        assertEquals(1, validator.validate().size());
    }

    @Test
    void validate_NonPositivePayment_PojoWithDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        student.classesCount = 3;
        student.payment = BigDecimal.ZERO;
        Validator validator = new Validator()
                .ofNumber(student::getAge)
                .notNull()
                .min(12)
                .ofNumber(student::getClassesCount)
                .notNull()
                .positive()
                .ofNumber(student::getPayment)
                .positive()
                .instance();
        assertEquals(1, validator.validate().size());
    }

    @Test
    void validate_PaymentOutOfBounds_PojoWithDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        student.classesCount = 3;
        student.payment = new BigDecimal("1250.55");
        Validator validator = new Validator()
                .ofNumber(student::getAge)
                .notNull()
                .min(12)
                .ofNumber(student::getClassesCount)
                .notNull()
                .positive()
                .ofNumber(student::getPayment)
                .positive()
                .min(new BigDecimal("5000.00"))
                .max(new BigDecimal("10000.00"))
                .instance();
        assertEquals(1, validator.validate().size());
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
