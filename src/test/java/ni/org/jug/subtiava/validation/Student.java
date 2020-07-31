package ni.org.jug.subtiava.validation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author aalaniz
 * @version 1.0
 */
public class Student {
    public String firstName;
    public String middleName;
    public String lastName;
    public Integer age;
    public Integer classesCount;
    public String gender;
    public Integer status;
    public LocalDate dateOfBirth;
    public LocalDate inscriptionDate;
    public BigDecimal payment;
    public LocalDateTime createdOn;
    public LocalDateTime updatedOn;

    public Student() {
    }

    public Student(String firstName, String lastName, Integer age, Gender gender) {
        this(firstName, lastName, age, gender.name());
    }

    public Student(String firstName, String lastName, Integer age, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getAge() {
        return age;
    }

    public Integer getClassesCount() {
        return classesCount;
    }

    public String getGender() {
        return gender;
    }

    public Integer getStatus() {
        return status;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public LocalDate getInscriptionDate() {
        return inscriptionDate;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public List<ConstraintViolation> validateBeforeInsert() {
        return new Validator("student")
                .ofString(this::getFirstName, "first name")
                .notBlank()
                .minLength(3)
                .maxLength(50)
                .ofString(this::getLastName, "last name")
                .notBlank()
                .minLength(3)
                .maxLength(50)
                .ofString(this::getGender, "gender")
                .notNull()
                .values(Gender.class)
                .ofNumber(this::getAge, "age")
                .notNull()
                .min(0)
                .max(150)
                .ofDate(this::getCreatedOn, "created on")
                .notNull()
                .pastOrPresent()
                .ofDate(this::getUpdatedOn, "updated on")
                .alwaysNull()
                .validate();
    }

    public List<ConstraintViolation> validateBeforeUpdate() {
        return new Validator("student")
                .ofString(this::getFirstName, "first name")
                .notBlank()
                .minLength(3)
                .maxLength(50)
                .ofString(this::getLastName, "last name")
                .notBlank()
                .minLength(3)
                .maxLength(50)
                .ofString(this::getGender, "gender")
                .notNull()
                .values(Gender.class)
                .ofNumber(this::getAge, "age")
                .notNull()
                .min(0)
                .max(150)
                .ofDate(this::getCreatedOn, "created on")
                .notNull()
                .pastOrPresent()
                .ofDate(this::getUpdatedOn, "updated on")
                .notNull()
                .pastOrPresent()
                .validate();
    }
}
