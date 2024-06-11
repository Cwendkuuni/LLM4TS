package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

import org.junit.Test;

/**
 * Unit tests for {@link StdKeyDeserializer} class.
 */
@JacksonAnnotationsWithOverrides
public class StdKeyDeserializerTest {

    @Test
    public void testBooleanKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        Object deserializedKey = serializer.deserializeKey("true", new TestContext());
        assertTrue((Boolean) deserializedKey);

        deserializedKey = serializer.deserializeKey("false", new TestContext());
        assertFalse((Boolean) deserializedKey);
    }

    @Test
    public void testByteKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        Object deserializedKey = serializer.deserializeKey("127", new TestContext());
        assertEquals((Byte) 127, deserializedKey);

        deserializedKey = serializer.deserializeKey("-128", new TestContext());
        assertEquals((Byte) -128, deserializedKey);

        deserializedKey = serializer.deserializeKey("256", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testShortKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        Object deserializedKey = serializer.deserializeKey("32767", new TestContext());
        assertEquals((Short) 32767, deserializedKey);

        deserializedKey = serializer.deserializeKey("-32768", new TestContext());
        assertEquals((Short) -32768, deserializedKey);

        deserializedKey = serializer.deserializeKey("65536", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testCharKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        Object deserializedKey = serializer.deserializeKey("A", new TestContext());
        assertEquals((Character) 'A', deserializedKey);

        deserializedKey = serializer.deserializeKey("€", new TestContext());
        assertEquals((Character) 8364, deserializedKey);

        deserializedKey = serializer.deserializeKey("abc", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testIntKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        Object deserializedKey = serializer.deserializeKey("1234567890", new TestContext());
        assertEquals((Integer) 1234567890, deserializedKey);

        deserializedKey = serializer.deserializeKey("-1234567890", new TestContext());
        assertEquals((Integer) -1234567890, deserializedKey);
    }

    @Test
    public void testLongKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        Object deserializedKey = serializer.deserializeKey("9223372036854775807", new TestContext());
        assertEquals((Long) 9223372036854775807L, deserializedKey);

        deserializedKey = serializer.deserializeKey("-9223372036854775808", new TestContext());
        assertEquals((Long) -9223372036854775808L, deserializedKey);

        deserializedKey = serializer.deserializeKey("9223372036854775809", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testFloatKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        Object deserializedKey = serializer.deserializeKey("3.14159265358979323846", new TestContext());
        assertEquals((Float) 3.14159265358979323846f, deserializedKey);
    }

    @Test
    public void testDoubleKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        Object deserializedKey = serializer.deserializeKey("6.2831853071854767982", new TestContext());
        assertEquals((Double) 6.2831853071854767982, deserializedKey);
    }

    @Test
    public void testLocaleKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        Object deserializedKey = serializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, deserializedKey);

        deserializedKey = serializer.deserializeKey("fr_FR", new TestContext());
        assertEquals(Locale.FRENCH, deserializedKey);
    }

    @Test
    public void testDateKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
        Object deserializedKey = serializer.deserializeKey("2022-12-31", new TestContext());
        assertNotNull(deserializedKey);
    }

    @Test
    public void testCalendarKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
        Object deserializedKey = serializer.deserializeKey("2022-12-31", new TestContext());
        assertNotNull(deserializedKey);
    }

    @Test
    public void testUUIDKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(UUID.class);
        Object deserializedKey = serializer.deserializeKey("123e4567-789b-12d3-a456-426614174000", new TestContext());
        assertEquals(new UUID("123e4567-789b-12d3-a456-426614174000"), deserializedKey);
    }

    @Test
    public void testURIKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        Object deserializedKey = serializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URI("http://example.com"), deserializedKey);
    }

    @Test
    public void testURLKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        Object deserializedKey = serializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URL("http://example.com"), deserializedKey);
    }

    @Test
    public void testClassKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String[].class);
        Object deserializedKey = serializer.deserializeKey("[Ljava.lang.String;@3b5f4c", new TestContext());
        assertArrayEquals(new String[]{"test1", "test2"}, deserializedKey);
    }

    @Test
    public void testEnumKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Color.class);
        Object deserializedKey = serializer.deserializeKey("RED", new TestContext());
        assertEquals(Color.RED, deserializedKey);

        deserializedKey = serializer.deserializeKey("GREEN", new TestContext());
        assertEquals(Color.GREEN, deserializedKey);

        deserializedKey = serializer.deserializeKey("INVALID", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testInvalidKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        assertNull(serializer.deserializeKey(null, new TestContext()));

        try {
            serializer.deserializeKey("", new TestContext());
            fail();
        } catch (IOException e) {
            // expected
        }

        try {
            serializer.deserializeKey("invalid", new TestContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCustomKeyDeserialization() throws Exception {
        Class<?> customClass = CustomKey.class;
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(customClass);
        Object deserializedKey = serializer.deserializeKey("CUSTOM_KEY_VALUE", new TestContext());
        assertEquals("custom_key_value", deserializedKey);
    }

    private static class TestContext extends DeserializationContext {
        public TestContext() {
            super(new BaseTestAnnotationIntrospector());
        }

        @Override
        public ObjectInstanceFinder getInstanceFinder() {
            return new JacksonAnnotationInstanceFinder();
        }

        @Override
        public Object findValue(Class<?> type) throws IOException {
            return null;
        }

        @Override
        public ObjectBuilder<?> createObjectMapper() {
            return null;
        }

        @Override
        public ObjectReader createObjectReader() {
            return null;
        }
    }

    private static class CustomKey {
        private final String value;

        public CustomKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}