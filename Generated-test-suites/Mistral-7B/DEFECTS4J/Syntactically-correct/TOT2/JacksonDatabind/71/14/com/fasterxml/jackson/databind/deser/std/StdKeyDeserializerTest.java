import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;

public class StdKeyDeserializerTest {

    private ObjectMapper mapper;

    public StdKeyDeserializerTest() throws Exception {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.findAndRegisterModules();
    }

    @Test
    public void testBooleanDeserialization() throws IOException {
        StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String key = "true";
        Object result = booleanDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        key = "false";
        result = booleanDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        key = "invalid";
        try {
            booleanDeserializer.deserializeKey(key, ctxt);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testByteDeserialization() throws IOException {
        StdKeyDeserializer byteDeserializer = StdKeyDeserializer.forType(Byte.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String key = "10";
        Object result = byteDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof Byte);
        assertEquals((Byte) 10, result);

        key = "-50";
        result = byteDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof Byte);
        assertEquals((Byte) -50, result);

        key = "256";
        try {
            byteDeserializer.deserializeKey(key, ctxt);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testShortDeserialization() throws IOException {
        StdKeyDeserializer shortDeserializer = StdKeyDeserializer.forType(Short.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String key = "100";
        Object result = shortDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof Short);
        assertEquals((Short) 100, result);

        key = "-200";
        result = shortDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof Short);
        assertEquals((Short) -200, result);

        key = "32768";
        try {
            shortDeserializer.deserializeKey(key, ctxt);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testCharDeserialization() throws IOException {
        StdKeyDeserializer charDeserializer = StdKeyDeserializer.forType(Character.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String key = "a";
        Object result = charDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof Character);
        assertEquals((Character) 'a', result);

        key = "z";
        result = charDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof Character);
        assertEquals((Character) 'z', result);

        key = "❄";
        try {
            charDeserializer.deserializeKey(key, ctxt);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testIntDeserialization() throws IOException {
        StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String key = "1000";
        Object result = intDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof Integer);
        assertEquals((Integer) 1000, result);

        key = "-2000";
        result = intDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof Integer);
        assertEquals((Integer) -2000, result);

        key = "Integer.MAX_VALUE";
        try {
            intDeserializer.deserializeKey(key, ctxt);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testLongDeserialization() throws IOException {
        StdKeyDeserializer longDeserializer = StdKeyDeserializer.forType(Long.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String key = "1000000";
        Object result = longDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof Long);
        assertEquals((Long) 1000000, result);

        key = "-2000000";
        result = longDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof Long);
        assertEquals((Long) -2000000, result);

        key = "Long.MAX_VALUE";
        try {
            longDeserializer.deserializeKey(key, ctxt);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testFloatDeserialization() throws IOException {
        StdKeyDeserializer floatDeserializer = StdKeyDeserializer.forType(Float.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String key = "10.5";
        Object result = floatDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof Float);
        assertEquals((Float) 10.5f, result);

        key = "-2.5";
        result = floatDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof Float);
        assertEquals((Float) -2.5f, result);

        key = "Float.MAX_VALUE";
        try {
            floatDeserializer.deserializeKey(key, ctxt);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testDoubleDeserialization() throws IOException {
        StdKeyDeserializer doubleDeserializer = StdKeyDeserializer.forType(Double.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String key = "10.5";
        Object result = doubleDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof Double);
        assertEquals((Double) 10.5, result);

        key = "-2.5";
        result = doubleDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof Double);
        assertEquals((Double) -2.5, result);

        key = "Double.MAX_VALUE";
        try {
            doubleDeserializer.deserializeKey(key, ctxt);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleDeserialization() throws IOException, JsonProcessingException {
        StdKeyDeserializer localeDeserializer = StdKeyDeserializer.forType(Locale.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String key = "en_US";
        Object result = localeDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof Locale);
        assertEquals(Locale.US, result);

        key = "fr_CA";
        result = localeDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof Locale);
        assertEquals(Locale.CANADA, result);

        key = "invalid";
        try {
            localeDeserializer.deserializeKey(key, ctxt);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testDateDeserialization() throws IOException, ParseException {
        StdKeyDeserializer dateDeserializer = StdKeyDeserializer.forType(Date.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String key = "2022-01-01";
        Object result = dateDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof Date);
        assertEquals(new Date(0), result); // Date.from(Instant.parse("2022-01-01T00:00:00Z")));

        key = "2022-01-31";
        result = dateDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof Date);
        assertEquals(new Date(0), result); // Date.from(Instant.parse("2022-01-31T00:00:00Z")));

        key = "InvalidDate";
        try {
            dateDeserializer.deserializeKey(key, ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testCalendarDeserialization() throws IOException, ParseException {
        StdKeyDeserializer calendarDeserializer = StdKeyDeserializer.forType(Calendar.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String key = "2022-01-01";
        Object result = calendarDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof Calendar);
        Calendar calendar = (Calendar) result;
        assertEquals(Calendar.getInstance(), calendar);
        assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH));
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(2022, calendar.get(Calendar.YEAR));

        key = "InvalidDate";
        try {
            calendarDeserializer.deserializeKey(key, ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testUUIDDeserialization() throws IOException {
        StdKeyDeserializer uuidDeserializer = StdKeyDeserializer.forType(UUID.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String key = "123e4567-e89b-12d3-a456-426614174000";
        Object result = uuidDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof UUID);
        assertEquals(new UUID("123e4567-e89b-12d3-a456-426614174000"), result);

        key = "Invalid UUID";
        try {
            uuidDeserializer.deserializeKey(key, ctxt);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testURIDeserialization() throws IOException {
        StdKeyDeserializer uriDeserializer = StdKeyDeserializer.forType(URI.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String key = "http://example.com";
        Object result = uriDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof URI);
        assertEquals(new URI("http://example.com"), result);

        key = "Invalid URI";
        try {
            uriDeserializer.deserializeKey(key, ctxt);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testURLDeserialization() throws IOException {
        StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String key = "http://example.com";
        Object result = urlDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof URL);
        assertEquals(new URL("http://example.com"), result);

        key = "Invalid URL";
        try {
            urlDeserializer.deserializeKey(key, ctxt);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testClassDeserialization() throws IOException {
        StdKeyDeserializer classDeserializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String key = "java.lang.String";
        Object result = classDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof Class<?>);
        assertEquals(String.class, result);

        key = "com.example.MyClass";
        try {
            classDeserializer.deserializeKey(key, ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testEnumDeserialization() throws IOException {
        EnumSet<DayOfWeek> daysOfWeek = EnumSet.allOf(DayOfWeek.class);
        String[] keys = {"MONDAY", "TUESDAY", "FRIDAY"};

        StdKeyDeserializer enumDeserializer = new EnumKD(mapper.getAnnotationIntrospector(),
                new AnnotatedMethod(DayOfWeek.class.getDeclaredMethod("valueOf", String.class)));

        for (String key : keys) {
            Object result = enumDeserializer.deserializeKey(key, mapper.getDeserializationContext());
            assertTrue(result instanceof Enum);
            assertEquals(Enum.valueOf(DayOfWeek.class, key), result);
        }

        try {
            enumDeserializer.deserializeKey("INVALID", mapper.getDeserializationContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testStringDeserialization() throws IOException {
        StdKeyDeserializer stringDeserializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String key = "Hello, World!";
        Object result = stringDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof String);
        assertEquals(key, result);
    }
}