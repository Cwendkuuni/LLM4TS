package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

/**
 * Test for {@link StdKeyDeserializer} class.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    @Test
    public void testForTypeBoolean() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Boolean.class);
        Object actual = kd.deserializeKey("true", new TestContext());
        assertTrue((Boolean) actual);

        actual = kd.deserializeKey("false", new TestContext());
        assertFalse((Boolean) actual);
    }

    @Test
    public void testForTypeByte() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Byte.class);
        Object actual = kd.deserializeKey("12", new TestContext());
        assertEquals((Byte) new Byte((byte) 12), actual);

        actual = kd.deserializeKey("127", new TestContext());
        assertEquals((Byte) new Byte((byte) 127), actual);

        actual = kd.deserializeKey("128", new TestContext());
        assertNull(actual);
    }

    @Test
    public void testForTypeShort() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Short.class);
        Object actual = kd.deserializeKey("12345", new TestContext());
        assertEquals((Short) new Short((short) 12345), actual);

        actual = kd.deserializeKey("32767", new TestContext());
        assertEquals((Short) new Short((short) 32767), actual);

        actual = kd.deserializeKey("32768", new TestContext());
        assertNull(actual);
    }

    @Test
    public void testForTypeChar() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Character.class);
        Object actual = kd.deserializeKey("A", new TestContext());
        assertEquals((Character) 'A', actual);

        actual = kd.deserializeKey("Z", new TestContext());
        assertEquals((Character) 'Z', actual);

        actual = kd.deserializeKey("a", new TestContext());
        assertEquals((Character) 'a', actual);

        actual = kd.deserializeKey("z", new TestContext());
        assertEquals((Character) 'z', actual);

        actual = kd.deserializeKey("ö", new TestContext());
        assertEquals((Character) 'ö', actual);

        actual = kd.deserializeKey("\u03B4", new TestContext());
        assertEquals((Character) '\u03B4', actual);
    }

    @Test
    public void testForTypeInt() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Integer.class);
        Object actual = kd.deserializeKey("123456", new TestContext());
        assertEquals((Integer) new Integer(123456), actual);
    }

    @Test
    public void testForTypeLong() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Long.class);
        Object actual = kd.deserializeKey("12345678901234567890", new TestContext());
        assertEquals((Long) new Long(12345678901234567890L), actual);
    }

    @Test
    public void testForTypeFloat() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Float.class);
        Object actual = kd.deserializeKey("1.23456789E8", new TestContext());
        assertEquals((Float) new Float(1.23456789E8f), actual);
    }

    @Test
    public void testForTypeDouble() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Double.class);
        Object actual = kd.deserializeKey("1.23456789E8", new TestContext());
        assertEquals((Double) new Double(1.23456789E8d), actual);
    }

    @Test
    public void testForTypeLocale() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Locale.class);
        Object actual = kd.deserializeKey("en_US", new TestContext());
        assertEquals((Locale) Locale.ENGLISH, actual);
    }

    @Test
    public void testForTypeDate() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Date.class);
        Object actual = kd.deserializeKey("2023-03-31", new TestContext());
        assertEquals(new Date(1679475200000L), actual);
    }

    @Test
    public void testForTypeCalendar() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Calendar.class);
        Object actual = kd.deserializeKey("2023-03-31", new TestContext());
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.YEAR, 2023);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DATE, 31);
        assertEquals(calendar, actual);
    }

    @Test
    public void testForTypeUUID() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(UUID.class);
        Object actual = kd.deserializeKey("uuid:2a32a3-0a2a-11d0-80b4-00c0apmraptic", new TestContext());
        assertEquals(new UUID("2a32a3-0a2a-11d0-80b4-00c0apmraptic"), actual);
    }

    @Test
    public void testForTypeURI() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(URI.class);
        Object actual = kd.deserializeKey("https://example.com", new TestContext());
        assertEquals(new URI("https://example.com"), actual);
    }

    @Test
    public void testForTypeURL() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(URL.class);
        Object actual = kd.deserializeKey("https://example.com", new TestContext());
        assertEquals(new URL("https://example.com"), actual);
    }

    @Test
    public void testForTypeClass() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(String.class);
        Object actual = kd.deserializeKey("java.lang.String", new TestContext());
        assertEquals(String.class, actual);
    }

    @Test
    public void testForTypeEnum() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(MyEnum.class);
        Object actual = kd.deserializeKey("MyEnum.VALUE_2", new TestContext());
        assertEquals(MyEnum.VALUE_2, actual);
    }

    @Test
    public void testForTypeByteArray() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(byte[].class);
        Object actual = kd.deserializeKey("Rm9udCAoCiAgICA8cGFzLg==", new TestContext());
        byte[] expected = "{B, 42, N, 1, (B, 42), K}".getBytes();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testNullKey() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(String.class);
        Object actual = kd.deserializeKey(null, new TestContext());
        assertNull(actual);
    }

    @Test
    public void testInvalidKey() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Integer.class);
        Object actual = kd.deserializeKey("invalidKey", new TestContext());
        assertNotNull(actual);
        fail("Expected an exception, but none was thrown.");
    }

    @Test
    public void testParseLong() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Long.class);
        long maxLong = Long.MAX_VALUE;
        long expected = maxLong + 1;
        Object actual = kd.deserializeKey(Long.toString(expected), new TestContext());
        assertNull(actual);
    }

    @Test
    public void testParseDouble() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Double.class);
        double maxDouble = Double.MAX_VALUE;
        double expected = maxDouble + 1.0;
        Object actual = kd.deserializeKey(Double.toString(expected), new TestContext());
        assertNull(actual);
    }

    private static class TestContext extends DeserializationContext {
        private TestContext() {}

        @Override
        public Object findValueInContext(String key, Class<?> valueType) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object findValueInContext(String key, Type valueType) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public AnnotatedMethod findMethodAnnotation(String name, Class<?> cls) {
            return null;
        }

        @Override
        public AnnotatedMethod findMethodAnnotation(Method m) {
            return null;
        }

        @Override
        public AnnotatedField findFieldAnnotation(Field f) {
            return null;
        }

        @Override
        public MapperConfig<?> config() {
            return null;
        }

        @Override
        public Object findValueInDeserializationProblemHandlers(DeserializationProblem p) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void reportError(DeserializationProblem p) throws IOException {
            throw new UnsupportedOperationException();
        }
    }
}