import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static com.fasterxml.jackson.core.util.TestUtils.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NumberSerializerTest {

    private ObjectMapper mapper;

    @BeforeAll
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
    }

    @Test
    public void testSerializeNumber() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        // Test serializing different Number types
        BigDecimal bd = new BigDecimal("3.14");
        BigInteger bi = BigInteger.valueOf(12345);
        Integer intVal = 42;
        Long longVal = 42L;
        Double doubleVal = 3.14;
        Float floatVal = 3.14F;
        Byte byteVal = 42;
        Short shortVal = 42;
        Number complexNumber = new Complex(1, 2);

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), getStandardParserConfig());
        numberSerializer.serialize(bd, gen, mapper.getSerializerProvider());
        numberSerializer.serialize(bi, gen, mapper.getSerializerProvider());
        numberSerializer.serialize(intVal, gen, mapper.getSerializerProvider());
        numberSerializer.serialize(longVal, gen, mapper.getSerializerProvider());
        numberSerializer.serialize(doubleVal, gen, mapper.getSerializerProvider());
        numberSerializer.serialize(floatVal, gen, mapper.getSerializerProvider());
        numberSerializer.serialize(byteVal, gen, mapper.getSerializerProvider());
        numberSerializer.serialize(shortVal, gen, mapper.getSerializerProvider());
        numberSerializer.serialize(complexNumber, gen, mapper.getSerializerProvider());

        String expectedOutput = "\"3.14\""; // BigDecimal
        assertEquals(expectedOutput, gen.toString());
        gen.reset();

        expectedOutput = "12345"; // BigInteger
        assertEquals(expectedOutput, gen.toString());
        gen.reset();

        expectedOutput = "42"; // Integer, Long, Byte, Short
        assertEquals(expectedOutput, gen.toString());
        gen.reset();

        expectedOutput = "42.0"; // Double, Float
        assertEquals(expectedOutput, gen.toString());
        gen.reset();

        expectedOutput = "{\"real\":42.0,\"imaginary\":2.0}"; // Complex
        assertEquals(expectedOutput, gen.toString());
        gen.close();
    }

    @Test
    public void testGetSchema() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        // Test getSchema method with different Number types
        JsonNode schema1 = numberSerializer.getSchema(mapper.getSerializerProvider(), Number.class.getType());
        JsonNode schema2 = numberSerializer.getSchema(mapper.getSerializerProvider(), BigDecimal.class.getType());
        JsonNode schema3 = numberSerializer.getSchema(mapper.getSerializerProvider(), BigInteger.class.getType());

        String expectedSchema = "\"number\""; // Base schema
        String expectedSchema1 = "\"number\""; // Integer, Long, Float, Double, BigDecimal, BigInteger
        String expectedSchema2 = "\"number\""; // BigDecimal
        String expectedSchema3 = "\"number\""; // BigInteger

        assertEquals(expectedSchema, schema1.toString());
        assertEquals(expectedSchema1, schema1.toString());
        assertEquals(expectedSchema2, schema2.toString());
        assertEquals(expectedSchema3, schema3.toString());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException, IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        // Test acceptJsonFormatVisitor method with different Number types
        JsonFormatVisitorWrapper visitor = mapper.getSerializationConfig().findRootValueSerializer(Number.class.getType());
        numberSerializer.acceptJsonFormatVisitor(visitor, Number.class.getType());

        // TODO: Test custom Number classes and edge cases
    }

    @Test
    public void testSerializingEdgeCases() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        // Test serializing Double.NaN, Double.POSITIVE_INFINITY, and Double.NEGATIVE_INFINITY
        Double nan = Double.NaN;
        Double posInf = Double.POSITIVE_INFINITY;
        Double negInf = Double.NEGATIVE_INFINITY;

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), getStandardParserConfig());
        numberSerializer.serialize(nan, gen, mapper.getSerializerProvider());
        numberSerializer.serialize(posInf, gen, mapper.getSerializerProvider());
        numberSerializer.serialize(negInf, gen, mapper.getSerializerProvider());
        String output = gen.toString();

        String expectedOutput = "null"; // Double.NaN
        String expectedOutputInf = "null"; // Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY

        assertEquals(expectedOutput, output.substring(0, output.length() - 1));
        assertEquals(expectedOutputInf, output.substring(output.length() - 13, output.length()));
        gen.close();
    }

    @Test
    public void testSerializingNullNumber() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        // Test serializing a null Number
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), getStandardParserConfig());
        numberSerializer.serialize(null, gen, mapper.getSerializerProvider());
        String output = gen.toString();

        String expectedOutput = "null";
        assertEquals(expectedOutput, output);
        gen.close();
    }

    @Test
    public void testSerializingLargeValues() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        // Test serializing very large BigDecimal and BigInteger values
        BigDecimal largeBd = new BigDecimal("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
        BigInteger largeBi = BigInteger.valueOf(Long.parseLong("9223372036854775807"));

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), getStandardParserConfig());
        numberSerializer.serialize(largeBd, gen, mapper.getSerializerProvider());
        numberSerializer.serialize(largeBi, gen, mapper.getSerializerProvider());
        String output = gen.toString();

        String expectedOutputBd = "\"1.234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789\"";
        String expectedOutputBi = "\"-9.223372036854775808e+18\"";

        assertEquals(expectedOutputBd, output.substring(0, output.length() - 1));
        assertEquals(expectedOutputBi, output.substring(output.length() - 15, output.length()));
        gen.close();
    }

    @Test
    public void testSerializingInvalidValues() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        // Test serializing invalid values: String, Character, Boolean
        String invalidVal1 = "invalidString";
        Character invalidVal2 = 'invalidCharacter';
        Boolean invalidVal3 = false;

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), getStandardParserConfig());
        numberSerializer.serialize(invalidVal1, gen, mapper.getSerializerProvider());
        numberSerializer.serialize(invalidVal2, gen, mapper.getSerializerProvider());
        numberSerializer.serialize(invalidVal3, gen, mapper.getSerializerProvider());
        String output = gen.toString();

        String expectedOutput1 = "\"invalidString\"";
        String expectedOutput2 = "\"NaN\"";
        String expectedOutput3 = "false";

        // TODO: Create a custom Number class and test its serialization

        assertEquals(expectedOutput1, output.substring(0, output.length() - 1));
        assertEquals(expectedOutput2, output.substring(output.length() - 13, output.length()));
        assertEquals(expectedOutput3, output.substring(output.length() - 5, output.length()));
        gen.close();
    }

    @Test
    public void testHandlingCustomNumberClasses() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        // TODO: Create a custom Number class, test its serialization, and add the test case here
    }

    @Test
    public void testExceptionHandling() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        // Test handling of unsupported Number subclasses by the fallback writeNumber
        MyCustomNumber myCustomNumber = new MyCustomNumber();

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), getStandardParserConfig());
        try {
            numberSerializer.serialize(myCustomNumber, gen, mapper.getSerializerProvider());
            fail("An exception should have been thrown, as MyCustomNumber is an unsupported Number subclass.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        String output = gen.toString();

        String expectedOutput = myCustomNumber.toString();
        assertEquals(expectedOutput, output);
        gen.close();
    }
}