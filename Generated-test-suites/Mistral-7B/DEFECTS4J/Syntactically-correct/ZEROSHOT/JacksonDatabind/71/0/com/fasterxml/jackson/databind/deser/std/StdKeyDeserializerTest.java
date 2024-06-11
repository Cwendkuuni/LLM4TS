package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

/**
 * Unit tests for {@link StdKeyDeserializer}.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest
{
    @Test
    public void testBooleanDeserializer() throws Exception
    {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);

        Object result = serializer.deserializeKey("true", new TestContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        result = serializer.deserializeKey("false", new TestContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        try {
            serializer.deserializeKey("invalid", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testByteDeserializer() throws Exception
    {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);

        Object result = serializer.deserializeKey("1", new TestContext());
        assertTrue(result instanceof Byte);
        assertEquals((byte) 1, result);

        result = serializer.deserializeKey("127", new TestContext());
        assertTrue(result instanceof Byte);
        assertEquals((byte) 127, result);

        try {
            serializer.deserializeKey("128", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testShortDeserializer() throws Exception
    {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);

        Object result = serializer.deserializeKey("1", new TestContext());
        assertTrue(result instanceof Short);
        assertEquals((short) 1, result);

        result = serializer.deserializeKey("32767", new TestContext());
        assertTrue(result instanceof Short);
        assertEquals((short) 32767, result);

        try {
            serializer.deserializeKey("32768", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testCharDeserializer() throws Exception
    {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);

        Object result = serializer.deserializeKey("A", new TestContext());
        assertTrue(result instanceof Character);
        assertEquals('A', result);

        try {
            serializer.deserializeKey("ABC", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testIntDeserializer() throws Exception
    {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);

        Object result = serializer.deserializeKey("1", new TestContext());
        assertTrue(result instanceof Integer);
        assertEquals(1, result);

        result = serializer.deserializeKey("2147483647", new TestContext());
        assertTrue(result instanceof Integer);
        assertEquals(2147483647, result);

        try {
            serializer.deserializeKey("2147483648", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testLongDeserializer() throws Exception
    {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);

        Object result = serializer.deserializeKey("1", new TestContext());
        assertTrue(result instanceof Long);
        assertEquals(1L, result);

        result = serializer.deserializeKey("9223372036854775807", new TestContext());
        assertTrue(result instanceof Long);
        assertEquals(9223372036854775807L, result);

        try {
            serializer.deserializeKey("9223372036854775808", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testFloatDeserializer() throws Exception
    {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);

        Object result = serializer.deserializeKey("1.0", new TestContext());
        assertTrue(result instanceof Float);
        assertEquals(1.0f, result);

        try {
            serializer.deserializeKey("Invalid", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testDoubleDeserializer() throws Exception
    {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);

        Object result = serializer.deserializeKey("1.0", new TestContext());
        assertTrue(result instanceof Double);
        assertEquals(1.0, result, 0.001);

        try {
            serializer.deserializeKey("Invalid", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testLocaleDeserializer() throws Exception
    {
        Object result = StdKeyDeserializer.forType(Locale.class).deserializeKey("en_US", new TestContext());
        assertTrue(result instanceof Locale);
        assertEquals(Locale.US, result);

        try {
            StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
            result = serializer.deserializeKey("Invalid", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testDateDeserializer() throws Exception
    {
        Object result = StdKeyDeserializer.forType(Date.class).deserializeKey("2018-12-31T23:59:59.999Z", new TestContext());
        assertTrue(result instanceof Date);
        assertEquals(new Date(1545872399999L), result);

        try {
            StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
            result = serializer.deserializeKey("Invalid", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testCalendarDeserializer() throws Exception
    {
        Calendar calendar = StdKeyDeserializer.forType(Calendar.class).deserializeKey("2018-12-31", new TestContext());
        assertEquals(Calendar.getInstance(), calendar);
        assertEquals(2018, calendar.get(Calendar.YEAR));
        assertEquals(12, calendar.get(Calendar.MONTH));
        assertEquals(31, calendar.get(Calendar.DAY_OF_MONTH));

        try {
            StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
            serializer.deserializeKey("Invalid", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testURIDeserializer() throws Exception
    {
        Object result = StdKeyDeserializer.forType(URI.class).deserializeKey("http://example.com", new TestContext());
        assertTrue(result instanceof URI);
        assertEquals(new URI("http://example.com"), result);

        try {
            StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
            serializer.deserializeKey("Invalid", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testURLDeserializer() throws Exception
    {
        Object result = StdKeyDeserializer.forType(URL.class).deserializeKey("http://example.com", new TestContext());
        assertTrue(result instanceof URL);
        assertEquals(new URL("http://example.com"), result);

        try {
            StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
            serializer.deserializeKey("Invalid", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testUUIDDeserializer() throws Exception
    {
        Object result = StdKeyDeserializer.forType(UUID.class).deserializeKey("123e4567-e89b-12d3-a456-426614174000", new TestContext());
        assertTrue(result instanceof UUID);
        assertEquals(new UUID("123e4567-e89b-12d3-a456-426614174000"), result);

        try {
            StdKeyDeserializer serializer = StdKeyDeserializer.forType(UUID.class);
            serializer.deserializeKey("Invalid", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testEnumDeserializer() throws Exception
    {
        EnumSet<TestEnum> knownValues = EnumSet.allOf(TestEnum.class);

        Object result = new EnumKeyDeserializer(EnumResolver.construct(TestEnum.class), new TestMethodFactory()).deserializeKey("FIRST", new TestContext());
        assertTrue(result instanceof TestEnum);
        assertEquals(TestEnum.FIRST, result);

        try {
            result = new EnumKeyDeserializer(EnumResolver.construct(TestEnum.class), new TestMethodFactory()).deserializeKey("UNKNOWN", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            result = new EnumKeyDeserializer(EnumResolver.constructUsingToString(TestEnum.class), new TestMethodFactory()).deserializeKey("FIRST", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            result = new EnumKeyDeserializer(EnumResolver.constructUsingToString(TestEnum.class), new TestMethodFactory()).deserializeKey("SECOND", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            result = new EnumKeyDeserializer(EnumResolver.constructUsingToString(TestEnum.class), new TestMethodFactory()).deserializeKey("THIRD", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testCustomDeserializer() throws Exception
    {
        Object result = new CustomKeyDeserializer().deserializeKey("CustomKey", new TestContext());
        assertTrue(result instanceof CustomKey);
        assertEquals("CustomValue", result);
    }

    private static class CustomKeyDeserializer extends StdKeyDeserializer
    {
        CustomKeyDeserializer() { super(0, CustomKey.class); }

        @Override
        public Object _parse(String key, DeserializationContext ctxt) throws Exception
        {
            return new CustomKey("CustomValue");
        }
    }

    private static class CustomKey { String value; CustomKey(String value) { this.value = value; } }

    private static class TestMethodFactory
    {
        @JacksonStdImpl
        public static AnnotatedMethod createFactoryMethod()
        {
            try {
                Class<?> clazz = TestEnum.class;
                Method method = clazz.getMethod("valueOf", String.class);
                return new AnnotatedMethod(method, clazz);
            } catch (Exception e) {
                // should not happen
                fail(e);
                return null;
            }
        }
    }

    private static class TestEnum
    {
        public static final TestEnum FIRST = new TestEnum("FIRST");
        public static final TestEnum SECOND = new TestEnum("SECOND");
        public static final TestEnum THIRD = new TestEnum("THIRD");

        private final String name;

        TestEnum(String name) { this.name = name; }

        public static TestEnum valueOf(String name)
        {
            return EnumSet.valueOf(name);
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    private static class TestContext extends TestBaseContext
    {
    }

    private static class TestBaseContext extends BasicTestContext
    {
    }
}