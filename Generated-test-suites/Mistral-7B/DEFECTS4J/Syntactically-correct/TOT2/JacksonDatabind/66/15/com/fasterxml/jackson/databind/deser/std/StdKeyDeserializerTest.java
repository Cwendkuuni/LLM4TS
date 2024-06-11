import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testBooleanDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        DeserializationContext context = objectMapper.getConfiguration().newContext();

        Object result = keyDeserializer.deserializeKey("true", context);
        assertEquals(Boolean.TRUE, result);
        Object resultFalse = keyDeserializer.deserializeKey("false", context);
        assertEquals(Boolean.FALSE, resultFalse);
    }

    @Test
    public void testByteDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        DeserializationContext context = objectMapper.getConfiguration().newContext();

        Object result = keyDeserializer.deserializeKey("1", context);
        assertEquals((byte) 1, result);

        try {
            Object resultNegative = keyDeserializer.deserializeKey("-1", context);
            assertInstanceOf(JsonMappingException.class, result);
        } catch (JsonMappingException e) {
            assertEquals("not a valid representation: value can not be represented as 8-bit value", e.getMessage());
        }
    }

    @Test
    public void testShortDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        DeserializationContext context = objectMapper.getConfiguration().newContext();

        Object result = keyDeserializer.deserializeKey("12345", context);
        assertEquals((short) 12345, result);

        try {
            Object resultNegative = keyDeserializer.deserializeKey("-12345", context);
            assertInstanceOf(JsonMappingException.class, result);
        } catch (JsonMappingException e) {
            assertEquals("overflow, value can not be represented as 16-bit value", e.getMessage());
        }
    }

    @Test
    public void testCharDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        DeserializationContext context = objectMapper.getConfiguration().newContext();

        Object result = keyDeserializer.deserializeKey("A", context);
        assertEquals('A', result);

        try {
            Object resultMultipleChars = keyDeserializer.deserializeKey("AB", context);
            assertInstanceOf(JsonMappingException.class, result);
        } catch (JsonMappingException e) {
            assertEquals("can only convert 1-character Strings", e.getMessage());
        }
    }

    @Test
    public void testIntDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext context = objectMapper.getConfiguration().newContext();

        Object result = keyDeserializer.deserializeKey("12345", context);
        assertEquals(12345, result);
    }

    @Test
    public void testLongDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        DeserializationContext context = objectMapper.getConfiguration().newContext();

        Object result = keyDeserializer.deserializeKey("12345678901234567890", context);
        assertEquals(12345678901234567890L, result);
    }

    @Test
    public void testFloatDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        DeserializationContext context = objectMapper.getConfiguration().newContext();

        Object result = keyDeserializer.deserializeKey("123.45", context);
        assertEquals(123.45f, result, 0.001f);
    }

    @Test
    public void testDoubleDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        DeserializationContext context = objectMapper.getConfiguration().newContext();

        Object result = keyDeserializer.deserializeKey("123.45", context);
        assertEquals(123.45, result, 0.001);
    }

    @Test
    public void testLocaleDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        DeserializationContext context = objectMapper.getConfiguration().newContext();

        Object result = keyDeserializer.deserializeKey("en_US", context);
        assertEquals(Locale.US, result);

        try {
            Object resultUnknown = keyDeserializer.deserializeKey("xx_XX", context);
            assertInstanceOf(JsonMappingException.class, result);
        } catch (JsonMappingException e) {
            assertEquals("unable to parse key as locale", e.getMessage());
        }
    }

    @Test
    public void testCurrencyDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Currency.class);
        DeserializationContext context = objectMapper.getConfiguration().newContext();

        Object result = keyDeserializer.deserializeKey("USD", context);
        assertEquals(Currency.getInstance("USD"), result);

        try {
            Object resultUnknown = keyDeserializer.deserializeKey("XXX", context);
            assertInstanceOf(JsonMappingException.class, result);
        } catch (JsonMappingException e) {
            assertEquals("unable to parse key as currency", e.getMessage());
        }
    }

    @Test
    public void testDateDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(java.util.Date.class);
        DeserializationContext context = objectMapper.getConfiguration().newContext();

        Object result = keyDeserializer.deserializeKey("2022-12-31", context);
        assertEquals("Sat Dec 31 00:00:00 CET 2022", result.toString());
    }

    @Test
    public void testCalendarDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(java.util.Calendar.class);
        DeserializationContext context = objectMapper.getConfiguration().newContext();

        Object result = keyDeserializer.deserializeKey("2022-12-31", context);
        assertEquals(java.util.Calendar.getInstance("UTC").getTime(), result.getTime());
    }

    @Test
    public void testUUIDDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);
        DeserializationContext context = objectMapper.getConfiguration().newContext();

        Object result = keyDeserializer.deserializeKey("5ef43bd3-b91b-42b6-9d4c-da67ba8e514d", context);
        assertEquals(new UUID("5ef43bd3-b91b-42b6-9d4c-da67ba8e514d"), result);
    }

    @Test
    public void testURIDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        DeserializationContext context = objectMapper.getConfiguration().newContext();

        Object result = keyDeserializer.deserializeKey("https://www.example.com", context);
        assertEquals(new URI("https://www.example.com"), result);
    }

    @Test
    public void testURLDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        DeserializationContext context = objectMapper.getConfiguration().newContext();

        Object result = keyDeserializer.deserializeKey("https://www.example.com", context);
        assertEquals(new URL("https://www.example.com"), result);
    }

    @Test
    public void testClassDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String[].class);
        DeserializationContext context = objectMapper.getConfiguration().newContext();

        Object result = keyDeserializer.deserializeKey("[Ljava.lang.String;", context);
        assertEquals(new String[]{"test"}, result);

        try {
            Object resultUnknown = keyDeserializer.deserializeKey("java.lang.Object", context);
            assertInstanceOf(JsonMappingException.class, result);
        } catch (JsonMappingException e) {
            assertEquals("unable to parse key as Class", e.getMessage());
        }
    }

    @Test
    public void testEnumDeserialization() throws Exception {
        EnumSet<java.time.DayOfWeek> days = EnumSet.allOf(java.time.DayOfWeek.class);
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(java.time.DayOfWeek[].class);
        DeserializationContext context = objectMapper.getConfiguration().newContext();

        Object result = keyDeserializer.deserializeKey("[Ljava.time.DayOfWeek;", context);
        assertEquals(days.toArray(new java.time.DayOfWeek[0]), result);
    }

    @Test
    public void testUnknownKeyDeserialization() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext context = objectMapper.getConfiguration().newContext();

        Object result = keyDeserializer.deserializeKey("unknown", context);
        assertNull(result);
    }
}