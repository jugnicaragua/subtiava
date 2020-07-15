package ni.org.jug.subtiava.validation;

/**
 * @author aalaniz
 * @version 1.0
 */
public class Student {
    // TODO Probar con tipos primitivos
    public Integer age;
    public Integer classesCount;
    public String name;
    public String gender;
    public Integer status;

    public Student() {
    }

    public Student(Integer age, Integer classesCount, String name) {
        this.age = age;
        this.classesCount = classesCount;
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getClassesCount() {
        return classesCount;
    }

    public void setClassesCount(Integer classesCount) {
        this.classesCount = classesCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
