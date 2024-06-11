import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.EnumSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testBooleanDeserialization() throws IOException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        DeserializationContext context = mapper.getDeserializationContext();

        Object result = serializer.deserializeKey("true", context);
        assertEquals(Boolean.TRUE, result);

        result = serializer.deserializeKey("false", context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testByteDeserialization() throws IOException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        DeserializationContext context = mapper.getDeserializationContext();

        Object result = serializer.deserializeKey("1", context);
        assertEquals((byte) 1, result);

        result = serializer.deserializeKey("128", context);
        assertEquals((byte) 128, result);

        result = serializer.deserializeKey("256", context);
        assertNull(result);
    }

    @Test
    public void testShortDeserialization() throws IOException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class);
        DeserializationContext context = mapper.getDeserializationContext();

        Object result = serializer.deserializeKey("123", context);
        assertEquals((short) 123, result);

        result = serializer.deserializeKey("32767", context);
        assertEquals((short) 32767, result);

        result = serializer.deserializeKey("32768", context);
        assertNull(result);
    }

    @Test
    public void testCharDeserialization() throws IOException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);
        DeserializationContext context = mapper.getDeserializationContext();

        Object result = serializer.deserializeKey("a", context);
        assertEquals('a', result);

        result = serializer.deserializeKey("\\u0061", context);
        assertEquals('a', result);

        result = serializer.deserializeKey("z", context);
        assertEquals('z', result);

        result = serializer.deserializeKey("\\u007A", context);
        assertEquals('z', result);

        result = serializer.deserializeKey(" ", context);
        assertNull(result);
    }

    @Test
    public void testIntDeserialization() throws IOException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext context = mapper.getDeserializationContext();

        Object result = serializer.deserializeKey("123", context);
        assertEquals(123, result);

        result = serializer.deserializeKey("-123", context);
        assertEquals(-123, result);

        result = serializer.deserializeKey("1000000000", context);
        assertEquals(1000000000, result);

        result = serializer.deserializeKey("Integer.MIN_VALUE", context);
        assertEquals(Integer.MIN_VALUE, result);

        result = serializer.deserializeKey("Integer.MAX_VALUE", context);
        assertEquals(Integer.MAX_VALUE, result);

        result = serializer.deserializeKey("123456789012345678901234567890", context);
        assertNull(result);
    }

    @Test
    public void testLongDeserialization() throws IOException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        DeserializationContext context = mapper.getDeserializationContext();

        Object
            result = serializer.deserializeKey("123", context),
            longResult = 123;

        assertEquals(longResult, result);

        result = serializer.deserializeKey("-123", context);
        assertEquals(-123, result);

        result = serializer.deserializeKey("1000000000000000000", context);
        assertEquals(1000000000000000000L, result);

        result = serializer.deserializeKey("Long.MIN_VALUE", context);
        assertEquals(Long.MIN_VALUE, result);

        result = serializer.deserializeKey("Long.MAX_VALUE", context);
        assertEquals(Long.MAX_VALUE, result);

        result = serializer.deserializeKey("1234567890123456789012345678901234567890123456789012345678901234567890", context);
        assertNull(result);
    }

    @Test
    public void testDateDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        DeserializationContext context = mapper.getDeserializationContext();

        Object result = serializer.deserializeKey("2022-01-01", context);
        assertEquals(new Date(2022, 0, 1), result);

        result = serializer.deserializeKey("2022-12-31", context);
        assertEquals(new Date(2022, 11, 31), result);

        result = serializer.deserializeKey("2022-02-29", context); // assuming it's a leap year
        assertEquals(new Date(2022, 1, 29), result);

        result = serializer.deserializeKey("2022-02-28", context);
        assertNull(result);
    }

    @Test
    public void testUriDeserialization() throws IOException, URISyntaxException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        DeserializationContext context = mapper.getDeserializationContext();

        Object result = serializer.deserializeKey("http://example.com", context);
        assertEquals(new URI("http://example.com"), result);

        result = serializer.deserializeKey("https://example.com", context);
        assertEquals(new URI("https://example.com"), result);

        result = serializer.deserializeKey("ftp://example.com", context);
        assertEquals(new URI("ftp://example.com"), result);

        result = serializer.deserializeKey("mailto:user@example.com", context);
        assertEquals(new URI("mailto:user@example.com"), result);
    }

    @Test
    public void testUrlDeserialization() throws IOException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        DeserializationContext context = mapper.getDeserializationContext();

        Object result = serializer.deserializeKey("http://example.com", context);
        assertEquals(new URL("http://example.com"), result);

        result = serializer.deserializeKey("https://example.com", context);
        assertEquals(new URL("https://example.com"), result);

        result = serializer.deserializeKey("ftp://example.com", context);
        assertEquals(new URL("ftp://example.com"), result);

        result = serializer.deserializeKey("mailto:user@example.com", context);
        assertNull(result);

        result = serializer.deserializeKey("invalid:example.com", context);
        assertNull(result);
    }

    @Test
    public void testUrlDeserializationWithSpecialChars() throws IOException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        DeserializationContext context = mapper.getDeserializationContext();

        Object result = serializer.deserializeKey("http://user:password@example.com:8080/path%2Fwith%2Fslashes?query=%65%6e%63%6F%64%65%64", context);
        assertEquals(new URL("http://user:password@example.com:8080/path%2Fwith%2Fslashes?query=echoes"), result);
    }

    @Test
    public void testClassDeserialization() throws IOException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, String.class);
        DeserializationContext context = mapper.getDeserializationContext();

        Object result = serializer.deserializeKey("String.class", context);
        assertEquals(String.class, result);

        result = serializer.deserializeKey("Integer.class", context);
        assertEquals(Integer.class, result);

        result = serializer.deserializeKey("java.util.Date.class", context);
        assertEquals(Date.class, result);

        result = serializer.deserializeKey("java.util.HashMap.class", context);
        assertEquals(HashMap.class, result);

        result = serializer.deserializeKey("java.lang.Object.class", context);
        assertEquals(Object.class, result);

        result = serializer.deserializeKey("java.util.List.class", context);
        assertEquals(List.class, result);

        result = serializer.deserializeKey("java.util.ArrayList.class", context);
        assertEquals(ArrayList.class, result);

        result = serializer.deserializeKey("java.util.Collections.class", context);
        assertEquals(Collections.class, result);

        result = serializer.deserializeKey("java.util.EnumSet.class", context);
        assertEquals(EnumSet.class, result);

        result = serializer.deserializeKey("java.util.EnumSet.allOf(java.lang.Enum.class).class", context);
        assertEquals(EnumSet.allOf(Enum.class), result);
    }

    @Test
    public void testEnumDeserialization() throws IOException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, MyEnum.class);
        DeserializationContext context = mapper.getDeserializationContext();

        Object result = serializer.deserializeKey("MY_ENUM_VALUE_1", context);
        assertEquals(MyEnum.MY_ENUM_VALUE_1, result);

        result = serializer.deserializeKey("MY_ENUM_VALUE_2", context);
        assertEquals(MyEnum.MY_ENUM_VALUE_2, result);

        result = serializer.deserializeKey("INVALID_VALUE", context);
        assertNull(result);
    }

    @Test
    public void testExceptionHandling() throws IOException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);

        try {
            serializer.deserializeKey("invalid", null);
        } catch (IllegalArgumentException e) {
            assertEquals("not a valid representation", e.getMessage());
        }
    }

    enum MyEnum {
        MY_ENUM_VALUE_1,
        MY_ENUM_VALUE_2
    }
}