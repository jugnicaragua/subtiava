package ni.org.jug.subtiava.validation;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    public BigDecimal payment;

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

    public BigDecimal getPayment() {
        return payment;
    }
}
