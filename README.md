## Funcionalidad disponible

### Convertir n&uacute;mero a texto
Actualmente se soportan 2 idiomas: espa&ntilde;ol e ingl&eacute;s. Estas implementaciones (`ni.org.jug.subtiava.text.SpanishNumber2Text`,
`ni.org.jug.subtiava.text.EnglishNumber2Text`) extienden la clase abstracta `ni.org.jug.subtiava.text.Number2Text` y proveen las
traducciones a su idioma correspondiente. Nuevas implementaciones pueden ser agregadas con s&oacute;lo extender la clase abstracta.
La clase padre ofrece varios mecanismos para personalizar el texto final, ya sea durante el proceso de conversi&oacute;n (mediante
un **callback** que es llamado antes y despu&eacute;s de la conversi&oacute;n de cada grupo que compone el n&uacute;mero original) o al
final de la conversi&oacute;n (a trav&eacute;s de una interfaz funcional que permite personalizar la salida).

Ejemplos de uso:
* Conversi&oacute;n simple de un n&uacute;mero en espa&ntilde;ol:

        Number2Text converter = new SpanishNumber2Text(933);
        assertEquals("novecientos treinta y tres", converter.toText());
        
        converter = new SpanishNumber2Text(101_000);
        assertEquals("ciento un mil", converter.toText());
        
        converter = new SpanishNumber2Text(3_214_731);
        assertEquals("tres millones doscientos catorce mil setecientos treinta y uno", converter.toText());

* Conversi&oacute;n de n&uacute;meros con decimales:

        Number2Text converter = new SpanishNumber2Text(new BigDecimal("45871.9444"));
        assertEquals("cuarenta y cinco mil ochocientos setenta y uno con 94/100", converter.toText());

        converter = new SpanishNumber2Text(new BigDecimal("1000.745"));
        assertEquals("un mil con 75/100", converter.toText());

* Personalizar la salida de la conversi&oacute;n:

        Number2Text converter = new SpanishNumber2Text(new BigDecimal("87500.33"));
        BiFunction<String, Integer, CharSequence> formatter = (letter, decimal) -> {
            StringBuilder output = new StringBuilder(letter.length() + 50);
            output.append("** ").append(letter).append(" con ").append(decimal).append("/100 **");
            return output;
        };
        assertEquals("** ochenta y siete mil quinientos con 33/100 **", converter.toText(formatter));

* Personalizar el proceso de conversi&oacute;n mediante el **callback**:

        NumberConversionCustomizer conversionCustomizer = (eventType, conversionType, event, output) -> {
            if (eventType == EventType.BEFORE && conversionType != ConversionType.MAGNITUDE) {
                output.append("** ");
            }
        };
        Number2Text converter = new SpanishNumber2Text(34_899, conversionCustomizer);
        assertEquals("** treinta y cuatro mil ** ochocientos noventa y nueve", converter.toText());

        converter = new SpanishNumber2Text(333, conversionCustomizer);
        assertEquals("** trescientos treinta y tres", converter.toText());

* Ejemplos de conversi&oacute;n en ingl&eacute;s:

        Number2Text converter = new EnglishNumber2Text(33_977);
        assertEquals("thirty-three thousand nine hundred seventy-seven", converter.toText());

        converter = new EnglishNumber2Text(1_000_000);
        assertEquals("one million", converter.toText());
        
        converter = new EnglishNumber2Text(5_001_000_000l);
        assertEquals("five billion one million", converter.toText());

### Validar c&eacute;dula
Permite validar n&uacute;meros de c&eacute;dula: se valida la sintaxis de la c&eacute;dula (longitud, caracteres, fecha, etc). Esta clase
no garantiza en ning&uacute;n momento que una c&eacute;dula realmente exista y haya sido emitida por el **CSE**. Se puede usar como una
forma r&aacute;pida de validar entradas y garantizar que sint&aacute;cticamente sean correctas.

* Validar entradas:

        assertFalse(Cedula.validate(null));
        assertFalse(Cedula.validate(""));
        assertFalse(Cedula.validate(" "));
        assertFalse(Cedula.validate("1234567890    "));
        assertFalse(Cedula.validate("abcdefghijklmn"));
        assertFalse(Cedula.validate("this_is_just_a_test"));

* Crear un objeto Cedula para tener acceso a los segmentos que la componen:

        assertThrows(IllegalArgumentException.class, () -> {
            Cedula.of("2811311830009f"); // si la cedula no es valida, se lanza un error
        });

        Cedula cedula = Cedula.of("2811311830009v");
        assertEquals("281", cedula.townCode);
        assertEquals("131183", cedula.date);
        assertEquals("0009", cedula.consecutive);
        assertEquals('v', cedula.checkDigit);
        assertEquals("281-131183-0009v", cedula.getCedula('-'));

### Validator API
Permite aplicar **constraints** a los atributos de un `POJO` con el fin de validar la integridad de sus datos. Siguiendo el patr&oacute;n
*builder*, se puede encadenar la configuraci&oacute;n de m&uacute;ltiples **constraints** sobre un mismo atributo y agregar nuevas
configuraciones sobre otros atributos. Una vez que las validaciones hayan sido configuradas sobre sus respectivos atributos, se invoca
el m&eacute;todo `validate` sobre el objeto `Validator` para disparar el conjunto de reglas de validaci&oacute;n y devolver
una lista de las violaciones encontradas durante su an&aacute;lisis.

La clase `ni.org.jug.subtiava.validation.Validator` soporta validaciones para 3 grandes *grupos*:
* **String**.
* **Number**: byte, short, int, long (y sus respectivos wrappers), BigDecimal y BigInteger.
* **Date**: Date, java.sql.Date, java.sql.Timestamp, GregorianCalendar, ZonedDateTime, LocalDate, LocalDateTime.

> **Nota**: Cualquier tipo de datos que no est&eacute; incluido en la lista anterior, no est&aacute; oficialmente soportado y los **constraints** configurados ser&aacute;n ignorados.

Cada *grupo* est&aacute; representado por una interfaz donde se declaran las operaciones de validaci&oacute;n soportadas para su lista
de tipo de datos:
* **String**: `ni.org.jug.subtiava.validation.StringFieldValidator`.
* **Number**: `ni.org.jug.subtiava.validation.NumberFieldValidator`.
* **Date**: `ni.org.jug.subtiava.validation.DateFieldValidator`.

La clase `ni.org.jug.subtiava.validation.Validator` expone 3 m&eacute;todos para agregar reglas de validaci&oacute;n sobre el atributo
de un `POJO`. Estos m&eacute;todos retornan cualquiera de las interfaces enumerados en la lista anterior. El valor del atributo se pasa a
trav&eacute;s de una funci&oacute;n `Supplier`, lo cual nos permite tener acceso al valor del atributo y leerlo **ene** veces en caso de
sufrir modificaciones. El uso de la funci&oacute;n `Supplier` permite tener acceso directo a la referencia del `POJO` y tener
visibilidad sobre las escrituras o cambios realizados en el atributo. Por ejemplo, se pueden especificar reglas de validaci&oacute;n
para el atributo **primer nombre**, validar el `POJO` y volver a modificar el **primer nombre** y validar nuevamente los resultados.
A continuaci&oacute;n el ejemplo:

        Student student = new Student("Duke", "Nicaragua", 25, "Duke Gender");
        Validator validator = new Validator()
                .ofString(student::getFirstName)
                .notBlank()
                .minLength(3)
                .instance();
        
        List<ConstraintViolation> violations = validator.validate(); // lista vacia, no hay errores
        
        student.firstName = "Du"; // actualizar el primer nombre con un dato errado segun la regla de minLength
        violations = validator.validate(); // lista con un 1 error

A continuaci&oacute;n la lista de las operaciones de validaci&oacute;n soportadas por tipo de dato:

Tipo de Dato | Validador | Validaci&oacute;n
------------ | ----------------- | -----------------
String | `ni.org.jug.subtiava.validation.StringFieldValidator` | <ul><li>notNull</li><li>alwaysNull</li><li>minLength</li><li>maxLength</li><li>notEmpty</li><li>notBlank</li><li>values</li><li>regex</li></ul>
Number | `ni.org.jug.subtiava.validation.NumberFieldValidator` | <ul><li>notNull</li><li>alwaysNull</li><li>min</li><li>max</li><li>positive</li><li>negative</li><li>values</li></ul>
Date | `ni.org.jug.subtiava.validation.DateFieldValidator` | <ul><li>notNull</li><li>alwaysNull</li><li>past</li><li>pastOrPresent</li><li>future</li><li>futureOrPresent</li><li>age</li><li>year</li><li>month</li><li>day</li></ul>

Ejemplos de uso:

Los ejemplos mostrados a continuaci&oacute;n se basan en los siguientes tipos:

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
            
            public Student(String firstName, String lastName, Integer age, Gender gender)
            
            public Student(String firstName, String lastName, Integer age, String gender)
            
            ...
            Getters
            ...
        }

        public enum Gender {
            MALE, FEMALE;
        }

* Validar que `firstName`, `lastName`, `age` y `gender` sea requerido:

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

* Validar que `firstName`, `middleName` y `lastName` contengan siempre un valor:

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

* Validar que `firstName` coincida con la expresi&oacute;n regular:

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

* Validar que `lastName` contenga entre 15 a 50 caracteres:

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

* Definir las reglas de validaci&oacute;n directamente en el `POJO`:

        public class Student {
            ...
            Atributos, constructores y getters
            ...
            
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
        }
                
        Student student = new Student("Duke", "Nicaragua", 25, Gender.MALE);
        student.createdOn = LocalDateTime.now();

        assertEquals(0, student.validateBeforeInsert().size());

Para consultar otros ejemplos de uso, referirse a los tests unitarios.