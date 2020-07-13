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
Permite validar n&uacute;meros de c&eacute;dula: se valida la sintaxis de la c&eacute;dula (longitud, caracteres, fecha, etc), esta clase
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
            Cedula.of("2811311830009f");
        });

        Cedula cedula = Cedula.of("2811311830009v");
        assertEquals("281", cedula.townCode);
        assertEquals("131183", cedula.date);
        assertEquals("0009", cedula.consecutive);
        assertEquals('v', cedula.checkDigit);
        assertEquals("281-131183-0009v", cedula.getCedula('-'));
