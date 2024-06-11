package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanKind() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);

        DeserializationContext context = new TestDeserializationContext();
        Object result = keyDeserializer.deserializeKey("true", context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        result = keyDeserializer.deserializeKey("false", context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testByteKind() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);

        DeserializationContext context = new TestDeserializationContext();
        Object result = keyDeserializer.deserializeKey("127", context);
        assertTrue(result instanceof Byte);
        assertEquals((byte) 127, result);

        result = keyDeserializer.deserializeKey("-128", context);
        assertTrue(result instanceof Byte);
        assertEquals((byte) -128, result);

        result = keyDeserializer.deserializeKey("256", context);
        assertNull(result);
    }

    @Test
    public void testShortKind() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);

        DeserializationContext context = new TestDeserializationContext();
        Object result = keyDeserializer.deserializeKey("32767", context);
        assertTrue(result instanceof Short);
        assertEquals((short) 32767, result);

        result = keyDeserializer.deserializeKey("-32768", context);
        assertTrue(result instanceof Short);
        assertEquals((short) -32768, result);

        result = keyDeserializer.deserializeKey("65536", context);
        assertNull(result);
    }

    @Test
    public void testCharKind() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);

        DeserializationContext context = new TestDeserializationContext();
        Object result = keyDeserializer.deserializeKey("a", context);
        assertTrue(result instanceof Character);
        assertEquals('a', result);

        result = keyDeserializer.deserializeKey("z", context);
        assertTrue(result instanceof Character);
        assertEquals('z', result);

        result = keyDeserializer.deserializeKey(" invalid", context);
        assertNull(result);
    }

    @Test
    public void testIntKind() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);

        DeserializationContext context = new TestDeserializationContext();
        Object result = keyDeserializer.deserializeKey("12345", context);
        assertTrue(result instanceof Integer);
        assertEquals(12345, result.intValue());

        result = keyDeserializer.deserializeKey("-12345", context);
        assertTrue(result instanceof Integer);
        assertEquals(-12345, result.intValue());

        result = keyDeserializer.deserializeKey("Integer.MAX_VALUE", context);
        assertTrue(result instanceof Integer);
        assertEquals(Integer.MAX_VALUE, result.intValue());

        result = keyDeserializer.deserializeKey("Integer.MIN_VALUE", context);
        assertTrue(result instanceof Integer);
        assertEquals(Integer.MIN_VALUE, result.intValue());

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testLongKind() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);

        DeserializationContext context = new TestDeserializationContext();
        Object result = keyDeserializer.deserializeKey("1234567890123456789", context);
        assertTrue(result instanceof Long);
        assertEquals(1234567890123456789L, result.longValue());

        result = keyDeserializer.deserializeKey("-1234567890123456789", context);
        assertTrue(result instanceof Long);
        assertEquals(-1234567890123456789L, result.longValue());

        result = keyDeserializer.deserializeKey("Long.MAX_VALUE", context);
        assertTrue(result instanceof Long);
        assertEquals(Long.MAX_VALUE, result.longValue());

        result = keyDeserializer.deserializeKey("Long.MIN_VALUE", context);
        assertTrue(result instanceof Long);
        assertEquals(Long.MIN_VALUE, result.longValue());

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testFloatKind() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);

        DeserializationContext context = new TestDeserializationContext();
        Object result = keyDeserializer.deserializeKey("123.45", context);
        assertTrue(result instanceof Float);
        assertEquals(123.45f, result.floatValue(), 0);

        result = keyDeserializer.deserializeKey("-123.45", context);
        assertTrue(result instanceof Float);
        assertEquals(-123.45f, result.floatValue(), 0);

        result = keyDeserializer.deserializeKey("Float.MAX_VALUE", context);
        assertTrue(result instanceof Float);
        assertEquals(Float.MAX_VALUE, result.floatValue(), 0);

        result = keyDeserializer.deserializeKey("Float.MIN_VALUE", context);
        assertTrue(result instanceof Float);
        assertEquals(Float.MIN_VALUE, result.floatValue(), 0);

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testDoubleKind() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);

        DeserializationContext context = new TestDeserializationContext();
        Object result = keyDeserializer.deserializeKey("123.45", context);
        assertTrue(result instanceof Double);
        assertEquals(123.45, result.doubleValue(), 0);

        result = keyDeserializer.deserializeKey("-123.45", context);
        assertTrue(result instanceof Double);
        assertEquals(-123.45, result.doubleValue(), 0);

        result = keyDeserializer.deserializeKey("Double.MAX_VALUE", context);
        assertTrue(result instanceof Double);
        assertEquals(Double.MAX_VALUE, result.doubleValue(), 0);

        result = keyDeserializer.deserializeKey("Double.MIN_VALUE", context);
        assertTrue(result instanceof Double);
        assertEquals(Double.MIN_VALUE, result.doubleValue(), 0);

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testLocaleKind() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);

        DeserializationContext context = new TestDeserializationContext();
        Object result = keyDeserializer.deserializeKey("en_US", context);
        assertTrue(result instanceof Locale);
        assertEquals(Locale.US, result);

        result = keyDeserializer.deserializeKey("fr_FR", context);
        assertTrue(result instanceof Locale);
        assertEquals(Locale.FRANCE, result);

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testDateKind() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);

        DeserializationContext context = new TestDeserializationContext();
        Object result = keyDeserializer.deserializeKey("2022-12-31", context);
        assertTrue(result instanceof Date);
        assertEquals(new Date(2022, 11, 31), result);

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testCalendarKind() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);

        DeserializationContext context = new TestDeserializationContext();
        Object result = keyDeserializer.deserializeKey("2022-12-31", context);
        assertTrue(result instanceof Calendar);
        Calendar cal = (Calendar) result;
        assertEquals(2022, cal.get(Calendar.YEAR));
        assertEquals(11, cal.get(Calendar.MONTH));
        assertEquals(31, cal.get(Calendar.DATE));

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testUUIDKind() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);

        DeserializationContext context = new TestDeserializationContext();
        Object result = keyDeserializer.deserializeKey("00000000-0000-0000-0000-000000000000", context);
        assertTrue(result instanceof UUID);
        assertEquals(new UUID("00000000-0000-0000-0000-000000000000"), result);

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testURIKind() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);

        DeserializationContext context = new TestDeserializationContext();
        Object result = keyDeserializer.deserializeKey("http://example.com/", context);
        assertTrue(result instanceof URI);
        assertEquals(new URI("http://example.com/"), result);

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testURLKind() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);

        DeserializationContext context = new TestDeserializationContext();
        Object result = keyDeserializer.deserializeKey("http://example.com/", context);
        assertTrue(result instanceof URL);
        assertEquals(new URL("http://example.com/"), result);

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    @Test
    public void testClassKind() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);

        DeserializationContext context = new TestDeserializationContext();
        Object result = keyDeserializer.deserializeKey("java.lang.String", context);
        assertTrue(result instanceof String);
        assertEquals("java.lang.String", result.toString());

        result = keyDeserializer.deserializeKey("java.util.List", context);
        assertNull(result);
    }

    @Test
    public void testEnumKind() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(MyEnum.class);

        DeserializationContext context = new TestDeserializationContext();
        Object result = keyDeserializer.deserializeKey("VALUE_1", context);
        assertTrue(result instanceof MyEnum);
        assertEquals(MyEnum.VALUE_1, result);

        result = keyDeserializer.deserializeKey("VALUE_2", context);
        assertTrue(result instanceof MyEnum);
        assertEquals(MyEnum.VALUE_2, result);

        result = keyDeserializer.deserializeKey("invalid", context);
        assertNull(result);
    }

    private static class TestDeserializationContext extends DeserializationContextBase {
        TestDeserializationContext() {
            super(new TestAnnotationIntrospector(), null, null, null, null);
        }

        @Override
        public Object findValueInMap(Map<String, Object> m, String key) throws IOException {
            // not applicable for testing this class
            throw new UnsupportedOperationException();
        }
    }

    private static class TestAnnotationIntrospector extends JsonAnnotationIntrospector {
        @Override
        public AnnotatedMethod findMethodAnnotation(BeanDescription beanDesc, Method m) {
            // not applicable for testing this class
            return null;
        }

        @Override
        public AnnotatedField findFieldAnnotation(BeanDescription beanDesc, Field f) {
            // not applicable for testing this class
            return null;
        }

        @Override
        public AnnotatedClass findClassAnnotation(Class<?> c) {
            // not applicable for testing this class
            return null;
        }
    }

    private enum MyEnum {
        VALUE_1,
        VALUE_2
    }
}