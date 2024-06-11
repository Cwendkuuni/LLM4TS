package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = keyDeserializer.deserializeKey("true", new TestContext());
        assertEquals(Boolean.TRUE, key);

        key = keyDeserializer.deserializeKey("false", new TestContext());
        assertEquals(Boolean.FALSE, key);

        key = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testByteKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);

        Object key = keyDeserializer.deserializeKey("5", new TestContext());
        assertEquals((byte) 5, key);

        key = keyDeserializer.deserializeKey("-128", new TestContext());
        assertEquals((byte) -128, key);

        key = keyDeserializer.deserializeKey("256", new TestContext());
        assertNull(key);
    }

    @Test
    public void testShortKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);

        Object key = keyDeserializer.deserializeKey("123", new TestContext());
        assertEquals((short) 123, key);

        key = keyDeserializer.deserializeKey("-32768", new TestContext());
        assertEquals((short) -32768, key);

        key = keyDeserializer.deserializeKey("32768", new TestContext());
        assertNull(key);
    }

    @Test
    public void testCharKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);

        Object key = keyDeserializer.deserializeKey("A", new TestContext());
        assertEquals('A', key);

        key = keyDeserializer.deserializeKey("\\u0061", new TestContext());
        assertEquals('a', key);

        key = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testIntKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);

        Object key = keyDeserializer.deserializeKey("12345", new TestContext());
        assertEquals(12345, key);

        key = keyDeserializer.deserializeKey("-12345", new TestContext());
        assertEquals(-12345, key);

        key = keyDeserializer.deserializeKey("2147483647", new TestContext());
        assertNull(key);
    }

    @Test
    public void testLongKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);

        Object key = keyDeserializer.deserializeKey("1234567890123456789", new TestContext());
        assertEquals(1234567890123456789L, key);

        key = keyDeserializer.deserializeKey("-1234567890123456789", new TestContext());
        assertEquals(-1234567890123456789L, key);

        key = keyDeserializer.deserializeKey("9223372036854775807", new TestContext());
        assertNull(key);
    }

    @Test
    public void testFloatKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);

        Object key = keyDeserializer.deserializeKey("1.23", new TestContext());
        assertEquals(1.23f, key);

        key = keyDeserializer.deserializeKey("-3.14", new TestContext());
        assertEquals(-3.14f, key);

        key = keyDeserializer.deserializeKey("NaN", new TestContext());
        assertNull(key);
    }

    @Test
    public void testDoubleKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);

        Object key = keyDeserializer.deserializeKey("1.23", new TestContext());
        assertEquals(1.23, key);

        key = keyDeserializer.deserializeKey("-3.14", new TestContext());
        assertEquals(-3.14, key);

        key = keyDeserializer.deserializeKey("NaN", new TestContext());
        assertNull(key);
    }

    @Test
    public void testLocaleKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);

        Object key = keyDeserializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, key);

        key = keyDeserializer.deserializeKey("es_ES", new TestContext());
        assertEquals(Locale.SPAIN, key);

        key = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testDateKeyDeserialization() throws JsonProcessingException, ParseException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);

        Object key = keyDeserializer.deserializeKey("2022-01-01", new TestContext());
        assertEquals(new Date(0), key);

        key = keyDeserializer.deserializeKey("2022-01-01T00:00:00Z", new TestContext());
        assertEquals(new Date(0), key);

        key = keyDeserializer.deserializeKey("InvalidDate", new TestContext());
        assertNull(key);
    }

    @Test
    public void testCalendarKeyDeserialization() throws JsonProcessingException, ParseException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);

        Object key = keyDeserializer.deserializeKey("2022-01-01", new TestContext());
        assertEquals(Calendar.getInstance().getTime(), key);

        key = keyDeserializer.deserializeKey("2022-01-01T00:00:00Z", new TestContext());
        assertEquals(Calendar.getInstance().getTime(), key);

        key = keyDeserializer.deserializeKey("InvalidDate", new TestContext());
        assertNull(key);
    }

    @Test
    public void testURIKeyDeserialization() throws JsonProcessingException, URISyntaxException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);

        Object key = keyDeserializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URI("http://example.com"), key);

        key = keyDeserializer.deserializeKey("https://example.com:8080/path/to/resource?query=params#fragment", new TestContext());
        assertEquals(new URI("https://example.com:8080/path/to/resource?query=params#fragment"), key);

        key = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testURLKeyDeserialization() throws JsonProcessingException, MalformedURLException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);

        Object key = keyDeserializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URL("http://example.com"), key);

        key = keyDeserializer.deserializeKey("https://example.com:8080/path/to/resource?query=params#fragment", new TestContext());
        assertEquals(new URL("https://example.com:8080/path/to/resource?query=params#fragment"), key);

        key = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testClassKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);

        Object key = keyDeserializer.deserializeKey("java.lang.String", new TestContext());
        assertEquals(String.class, key);

        key = keyDeserializer.deserializeKey("com.example.MyClass", new TestContext());
        assertEquals(Class.forName("com.example.MyClass"), key);

        key = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testEnumKeyDeserialization() throws JsonProcessingException {
        enum TestEnum {
            VALUE1, VALUE2, VALUE3
        }

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(TestEnum.class);

        Object key = keyDeserializer.deserializeKey("VALUE1", new TestContext());
        assertEquals(TestEnum.VALUE1, key);

        key = keyDeserializer.deserializeKey("VALUE2", new TestContext());
        assertEquals(TestEnum.VALUE2, key);

        key = keyDeserializer.deserializeKey("VALUE3", new TestContext());
        assertEquals(TestEnum.VALUE3, key);

        key = keyDeserializer.deserializeKey("INVALID", new TestContext());
        assertNull(key);
    }

    @Test
    public void testByteArrayKeyDeserialization() throws JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(byte[].class);

        Object key = keyDeserializer.deserializeKey("QW1234567890ABCDEF", new TestContext());
        assertEquals(new byte[]{65, 87, 50, 51, 52, 53, 54, 55, 56, 57}, key);

        key = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public Object findValueHandler(DeserializationConfig config, BeanDescription beanDesc, JavaType valueType) {
            return null;
        }

        @Override
        public Object findBeanDeserializer(BeanDescription beanDesc, TypeDeserializer vd, JavaType beanType) {
            return null;
        }

        @Override
        public Object findValueDeserializer(DeserializationConfig config, BeanDescription beanDesc, JavaType type) {
            return new StdScalarDeserializer(type);
        }

        @Override
        public Object findValueDeserializer(DeserializationConfig config, JavaType type) {
            return new StdScalarDeserializer(type);
        }

        @Override
        public Object findValueDeserializer(DeserializationConfig config, AnnotatedType type, JsonDeserializer.None unwrappingHandler) {
            return new StdScalarDeserializer(type);
        }
    }
}