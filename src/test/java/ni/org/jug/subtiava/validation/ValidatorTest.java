package ni.org.jug.subtiava.validation;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
                .values(Gender.class) // you could use an enum to specify the list of values
                .validate();
        assertEquals(1, violations.size());
    }

    @Test
    void validate_UnknownGenderValue_PojoWithDataIssues() {
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
                .values(Gender.MALE.name(), Gender.FEMALE.name()) // expected string values
                .validate();
        assertEquals(1, violations.size());
    }

    @Test
    void validate_AgeIsNot25_PojoWithDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 15, Gender.MALE);
        Validator validator = new Validator()
                .ofNumber(student::getAge)
                .notNull()
                .min(25)
                .max(25)
                .instance();
        assertEquals(1, validator.validate().size());
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
                .values(10, 20, 30)
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
    void validate_DateOfBirthIsToday_PojoWithDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        student.dateOfBirth = LocalDate.now();
        List<ConstraintViolation> violations = new Validator()
                .ofDate(student::getDateOfBirth)
                .past()
                .validate();
        assertEquals(1, violations.size());
    }

    @Test
    void validate_DateOfBirthIsTomorrow_PojoWithDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        student.dateOfBirth = LocalDate.now().plusDays(1);
        List<ConstraintViolation> violations = new Validator()
                .ofDate(student::getDateOfBirth)
                .pastOrPresent()
                .validate();
        assertEquals(1, violations.size());
    }

    @Test
    void validate_InscriptionDateIsToday_PojoWithDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        student.inscriptionDate = LocalDate.now();
        List<ConstraintViolation> violations = new Validator()
                .ofDate(student::getInscriptionDate)
                .future()
                .validate();
        assertEquals(1, violations.size());
    }

    @Test
    void validate_InscriptionDateIsYesterday_PojoWithDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        student.inscriptionDate = LocalDate.now().plusDays(-1);
        List<ConstraintViolation> violations = new Validator()
                .ofDate(student::getInscriptionDate)
                .futureOrPresent()
                .validate();
        assertEquals(1, violations.size());
    }

    @Test
    void validate_YearOfBirthIs1990_PojoWithDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        student.dateOfBirth = LocalDate.of(1990, Month.JANUARY, 1);
        List<ConstraintViolation> violations = new Validator()
                .ofDate(student::getDateOfBirth)
                .year(1991)
                .validate();
        assertEquals(1, violations.size());
    }

    @Test
    void validate_YearOfBirthIsOutOfBounds_PojoWithDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        student.dateOfBirth = LocalDate.of(1991, Month.JANUARY, 1);
        List<ConstraintViolation> violations = new Validator()
                .ofDate(student::getDateOfBirth)
                .year(1995, 1998)
                .validate();
        assertEquals(1, violations.size());
    }

    @Test
    void validate_MonthOfBirthIsJanuary_PojoWithDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        student.dateOfBirth = LocalDate.of(1990, Month.FEBRUARY, 1);
        List<ConstraintViolation> violations = new Validator()
                .ofDate(student::getDateOfBirth)
                .month(Month.JANUARY)
                .validate();
        assertEquals(1, violations.size());
    }

    @Test
    void validate_MonthOfBirthIsOutOfBounds_PojoWithDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        student.dateOfBirth = LocalDate.of(1990, Month.JANUARY, 1);
        List<ConstraintViolation> violations = new Validator()
                .ofDate(student::getDateOfBirth)
                .month(Month.MARCH, Month.JUNE)
                .validate();
        assertEquals(1, violations.size());
    }

    @Test
    void validate_DayOfBirthIsNot15_PojoWithDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        student.dateOfBirth = LocalDate.of(1990, Month.JANUARY, 25);
        List<ConstraintViolation> violations = new Validator()
                .ofDate(student::getDateOfBirth)
                .day(15)
                .validate();
        assertEquals(1, violations.size());
    }

    @Test
    void validate_DayOfBirthIsOutOfBounds_PojoWithDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        student.dateOfBirth = LocalDate.of(1990, Month.JANUARY, 25);
        List<ConstraintViolation> violations = new Validator()
                .ofDate(student::getDateOfBirth)
                .day(1, 15)
                .validate();
        assertEquals(1, violations.size());
    }

    @Test
    void validate_CheckListOfAttributesWithErrors_PojoWithDataIssues() {
        Student student = new Student("Duke", "Nicaragua", 25, "This is a test");
        student.classesCount = 0;
        student.status = 15;

        List<ConstraintViolation> violations = new Validator("student")
                .ofNumber(student::getAge, "age")
                .min(12)
                .max(17)
                .notNull()
                .of(student::getClassesCount, "classes count")
                .notNull()
                .positive()
                .ofString(student::getFirstName, "first name")
                .notBlank()
                .minLength(10)
                .maxLength(100)
                .regex("^[A-Za-z ]+")
                .ofString(student::getGender, "gender")
                .notNull()
                .values(Gender.class)
                .ofNumber(student::getStatus, "status")
                .notNull()
                .values(10, 20)
                .validate();

        assertEquals("age classes count first name gender status", violations
                .stream()
                .map(ConstraintViolation::getField)
                .collect(Collectors.joining(" ")));
    }

    @Test
    void validate_NonPositiveArguments_ExceptionThrown() {
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        student.dateOfBirth = LocalDate.of(1990, Month.JANUARY, 25);

        assertThrows(IllegalArgumentException.class, () -> {
            new Validator()
                    .ofString(student::getFirstName, "first name")
                    .minLength(-1)
                    .validate();
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Validator()
                    .ofString(student::getFirstName, "first name")
                    .maxLength(-1)
                    .validate();
        });

        student.dateOfBirth = LocalDate.now();
        assertThrows(IllegalArgumentException.class, () -> {
            new Validator()
                    .ofDate(student::getDateOfBirth, "date of birth")
                    .year(-1)
                    .validate();
        });

        student.dateOfBirth = LocalDate.now();
        assertThrows(IllegalArgumentException.class, () -> {
            new Validator()
                    .ofDate(student::getDateOfBirth, "date of birth")
                    .year(-5, -1)
                    .validate();
        });
    }

    @Test
    void validate_InvalidRange_ExceptionThrown() {
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        student.dateOfBirth = LocalDate.of(1990, Month.JANUARY, 25);

        assertThrows(IllegalArgumentException.class, () -> {
            new Validator()
                    .ofString(student::getFirstName, "first name")
                    .minLength(15)
                    .maxLength(3)
                    .validate();
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Validator()
                    .ofNumber(student::getAge, "age")
                    .min(20)
                    .max(5)
                    .validate();
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Validator()
                    .ofNumber(student::getPayment, "payment")
                    .min(new BigDecimal("2500.25"))
                    .max(new BigDecimal("1250.55"))
                    .validate();
        });

        student.dateOfBirth = LocalDate.now();
        assertThrows(IllegalArgumentException.class, () -> {
            new Validator()
                    .ofDate(student::getDateOfBirth, "date of birth")
                    .year(2020, 1991)
                    .validate();
        });
    }
}
