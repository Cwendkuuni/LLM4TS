import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * JUnit test file for StdKeyDeserializer class.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    @Test
    public void testBooleanType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = deserializer.deserializeKey("true", new TestContext());
        assertTrue(key instanceof Boolean);
        assertEquals(Boolean.TRUE, key);

        key = deserializer.deserializeKey("false", new TestContext());
        assertTrue(key instanceof Boolean);
        assertEquals(Boolean.FALSE, key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testByteType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        Object key = deserializer.deserializeKey("127", new TestContext());
        assertTrue(key instanceof Byte);
        assertEquals((byte) 127, key);

        key = deserializer.deserializeKey("-128", new TestContext());
        assertTrue(key instanceof Byte);
        assertEquals((byte) -128, key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testShortType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        Object key = deserializer.deserializeKey("127", new TestContext());
        assertTrue(key instanceof Short);
        assertEquals((short) 127, key);

        key = deserializer.deserializeKey("-32768", new TestContext());
        assertTrue(key instanceof Short);
        assertEquals((short) -32768, key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testCharType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        Object key = deserializer.deserializeKey("A", new TestContext());
        assertTrue(key instanceof Character);
        assertEquals('A', key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testIntType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        Object key = deserializer.deserializeKey("12345", new TestContext());
        assertTrue(key instanceof Integer);
        assertEquals(12345, key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testLongType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        Object key = deserializer.deserializeKey("123456789012345", new TestContext());
        assertTrue(key instanceof Long);
        assertEquals(123456789012345L, key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testFloatType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        Object key = deserializer.deserializeKey("1.2345", new TestContext());
        assertTrue(key instanceof Float);
        assertEquals(1.2345f, key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testDoubleType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        Object key = deserializer.deserializeKey("1.23456789", new TestContext());
        assertTrue(key instanceof Double);
        assertEquals(1.23456789, key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testLocaleType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
        Object key = deserializer.deserializeKey("en_US", new TestContext());
        assertTrue(key instanceof Locale);
        assertEquals(Locale.US, key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testDateType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        Object key = deserializer.deserializeKey("2023-03-17", new TestContext());
        assertTrue(key instanceof Date);
        // Check equality with a known Date instance.
        Date expectedDate = new Date(1678800000L);
        assertEquals(expectedDate, key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testCalendarType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
        Object key = deserializer.deserializeKey("2023-03-17", new TestContext());
        assertTrue(key instanceof Calendar);
        // Check equality with a known Calendar instance.
        Calendar expectedCalendar = Calendar.getInstance();
        expectedCalendar.set(Calendar.YEAR, 2023);
        expectedCalendar.set(Calendar.MONTH, Calendar.MARCH);
        expectedCalendar.set(Calendar.DAY_OF_MONTH, 17);
        assertEquals(expectedCalendar, key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testUriType() throws IOException, JsonProcessingException, URISyntaxException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        Object key = deserializer.deserializeKey("http://example.com", new TestContext());
        assertTrue(key instanceof URI);
        assertEquals(new URI("http://example.com"), key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testUrlType() throws IOException, JsonProcessingException, MalformedURLException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        Object key = deserializer.deserializeKey("http://example.com", new TestContext());
        assertTrue(key instanceof URL);
        assertEquals(new URL("http://example.com"), key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testClassType() throws IOException, JsonProcessingException, ClassNotFoundException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String[].class);
        Object key = deserializer.deserializeKey("[Ljava.lang.String;@12345678", new TestContext());
        assertTrue(key instanceof Class);
        assertEquals(String[].class, key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testEnumType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(MyEnumClass.class);
        Object key = deserializer.deserializeKey("MyEnumClass.VALUE_1", new TestContext());
        assertTrue(key instanceof MyEnumClass);
        assertEquals(MyEnumClass.VALUE_1, key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testCustomTypeDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(MyCustomClass.class);
        Object key = deserializer.deserializeKey("{\"name\":\"value\"}", new TestContext());
        assertTrue(key instanceof MyCustomClass);
        // Check equality with a known MyCustomClass instance.
        MyCustomClass expectedInstance = new MyCustomClass("value");
        assertEquals(expectedInstance, key);

        key = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    private static class TestContext extends DeserializationContext {
        TestContext() {
            super(null, createObjectMapper());
        }

        @Override
        public AnnotationIntrospector introspector() {
            return createAnnotationIntrospector();
        }
    }

    private static ObjectMapper createObjectMapper() {
        return new ObjectMapper();
    }

    private static AnnotationIntrospector createAnnotationIntrospector() {
        return new StandardAnnotationIntrospector();
    }
}