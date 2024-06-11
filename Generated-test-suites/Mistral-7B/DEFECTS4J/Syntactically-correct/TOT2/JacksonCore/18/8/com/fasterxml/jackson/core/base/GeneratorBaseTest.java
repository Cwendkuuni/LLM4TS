import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.*;
import com.fasterxml.jackson.core.json.DupDetector;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Test class for {@link GeneratorBase} class.
 */
public class GeneratorBaseTest {

    private GeneratorBase generator;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        generator = new GeneratorBase(DERIVED_FEATURES_MASK, new ObjectMapper.DefaultObjectMapper());
        outputStream = new ByteArrayOutputStream();
        generator.setOutputStd(outputStream);
    }

    /**
     * Test writing a string using a basic JUnit 4 test case.
     */
    @Test
    public void testWriteString() throws IOException {
        generator.writeString("Hello World");
        String actual = outputStream.toString();
        assertEquals("Hello World", actual);
    }

    /**
     * Test writing a number using a basic JUnit 4 test case.
     */
    @Test
    public void testWriteNumber() throws IOException {
        generator.writeNumber(42);
        String actual = outputStream.toString();
        assertEquals("42", actual);
    }

    /**
     * Test writing a boolean using a basic JUnit 4 test case.
     */
    @Test
    public void testWriteBoolean() throws IOException {
        generator.writeBoolean(true);
        String actual = outputStream.toString();
        assertEquals("true", actual);

        generator.writeBoolean(false);
        actual = outputStream.toString();
        assertEquals("false", actual);
    }

    /**
     * Test writing a null using a basic JUnit 4 test case.
     */
    @Test
    public void testWriteNull() throws IOException {
        generator.writeNull();
        String actual = outputStream.toString();
        assertEquals("null", actual);
    }

    /**
     * Test writing an array using a basic JUnit 4 test case.
     */
    @Test
    public void testWriteArray() throws IOException {
        int[] array = { 1, 2, 3 };
        generator.writeStartArray();
        for (int value : array) {
            generator.writeNumber(value);
        }
        generator.writeEndArray();
        String actual = outputStream.toString();
        assertEquals("[1, 2, 3]", actual);
    }

    /**
     * Test writing an object using a basic JUnit 4 test case.
     */
    @Test
    public void testWriteObject() throws IOException {
        MyClass obj = new MyClass("Test", 42);
        generator.writeObject(obj);
        String actual = outputStream.toString();
        assertEquals("{\"name\":\"Test\",\"age\":42}", actual);
    }

    /**
     * Test edge cases: write a BigDecimal value.
     */
    @Test
    public void testWriteBigDecimal() throws IOException {
        BigDecimal bigDecimal = new BigDecimal("123456789012345678901234567890");
        generator.writeNumber(bigDecimal);
        String actual = outputStream.toString();
        assertEquals("123456789012345678901234567890", actual);
    }

    /**
     * Test exception handling: write a null value when 'Feature.WRITE_NULLS_AS_EMPTY_JSON_OBJECT' is disabled.
     */
    @Test(expected = IOException.class)
    public void testWriteNullWithDisabledNullsAsEmptyJsonObject() throws IOException {
        generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NULLS_AS_EMPTY_JSON_OBJECT.getMask() ^ GeneratorBase.Feature.WRITE_NULLS_AS_EMPTY_JSON_OBJECT.getMask(), new ObjectMapper.DefaultObjectMapper());
        generator.writeNull();
    }

    /**
     * Test exception handling: write a BigDecimal value with a too large scale.
     */
    @Test(expected = IOException.class)
    public void testWriteBigDecimalWithTooLargeScale() throws IOException {
        BigDecimal bigDecimal = new BigDecimal("123456789012345678901234567890123456789012345678901234567890");
        generator.writeNumber(bigDecimal);
    }

    /**
     * Test exception handling: write a String with invalid UTF-8 characters.
     */
    @Test(expected = IOException.class)
    public void testWriteInvalidUtf8String() throws IOException {
        generator.writeString("\uD800\uDC00");
    }

    /**
     * Test exception handling: write a String larger than the configured limit.
     */
    @Test(expected = IOException.class)
    public void testWriteStringLargerThanConfiguredLimit() throws IOException {
        String largeString = new String(new char[1024 * 1024 * 1024]);
        generator.writeString(largeString);
    }

    /**
     * Test exception handling: write a raw string larger than the configured limit.
     */
    @Test(expected = IOException.class)
    public void testWriteRawStringLargerThanConfiguredLimit() throws IOException {
        String largeString = new String(new char[1024 * 1024 * 1024]);
        generator.writeRawValue(largeString);
    }

    /**
     * Test exception handling: write a raw byte array larger than the configured limit.
     */
    @Test(expected = IOException.class)
    public void testWriteRawByteArrayLargerThanConfiguredLimit() throws IOException {
        byte[] largeByteArray = new byte[1024 * 1024 * 1024];
        generator.writeRawValue(largeByteArray);
    }

    /**
     * Test exception handling: write a binary data larger than the configured limit.
     */
    @Test(expected = IOException.class)
    public void testWriteBinaryLargerThanConfiguredLimit() throws IOException {
        byte[] binaryData = new byte[1024 * 1024 * 1024];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(binaryData);
        generator.writeBinary("Base64Variant.STANDARD", inputStream, binaryData.length);
    }

    /**
     * Test exception handling: write a duplicate key when 'Feature.STRICT_DUPLICATE_DETECTION' is enabled.
     */
    @Test(expected = IOException.class)
    public void testWriteDuplicateKey() throws IOException {
        DupDetector dupDetector = DupDetector.rootDetector(generator);
        generator = new GeneratorBase(GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION.getMask(), new ObjectMapper.DefaultObjectMapper(), dupDetector);
        generator.writeFieldName("test");
        generator.writeFieldName("test");
    }
}

class MyClass {
    private final String name;
    private final int age;

    public MyClass(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}