package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.EnumSet;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

/**
 * Unit tests for {@link StdKeyDeserializer} class.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    @Test
    public void testStringKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        assertEquals(String.class, keyDeserializer.getKeyClass());

        Object key = keyDeserializer.deserializeKey("test", new TestContext());
        assertEquals("test", key);
    }

    @Test
    public void testBooleanKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        assertEquals(Boolean.class, keyDeserializer.getKeyClass());

        Object key = keyDeserializer.deserializeKey("true", new TestContext());
        assertTrue((Boolean) key);

        key = keyDeserializer.deserializeKey("false", new TestContext());
        assertFalse((Boolean) key);

        key = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testByteKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        assertEquals(Byte.class, keyDeserializer.getKeyClass());

        Object key = keyDeserializer.deserializeKey("10", new TestContext());
        assertEquals((byte) 10, key);

        key = keyDeserializer.deserializeKey("256", new TestContext());
        assertNull(key);
    }

    @Test
    public void testShortKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        assertEquals(Short.class, keyDeserializer.getKeyClass());

        Object key = keyDeserializer.deserializeKey("100", new TestContext());
        assertEquals((short) 100, key);

        key = keyDeserializer.deserializeKey("32768", new TestContext());
        assertNull(key);
    }

    @Test
    public void testCharKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        assertEquals(Character.class, keyDeserializer.getKeyClass());

        Object key = keyDeserializer.deserializeKey("a", new TestContext());
        assertEquals('a', key);

        key = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(key);
    }

    @Test
    public void testIntKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        assertEquals(Integer.class, keyDeserializer.getKeyClass());

        Object key = keyDeserializer.deserializeKey("1000", new TestContext());
        assertEquals(1000, key);

        key = keyDeserializer.deserializeKey("Integer.MIN_VALUE", new TestContext());
        assertEquals(Integer.MIN_VALUE, key);

        key = keyDeserializer.deserializeKey("Integer.MAX_VALUE", new TestContext());
        assertNull(key);
    }

    @Test
    public void testLongKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        assertEquals(Long.class, keyDeserializer.getKeyClass());

        Object key = keyDeserializer.deserializeKey("10000000000", new TestContext());
        assertEquals(10000000000L, key);

        key = keyDeserializer.deserializeKey("Long.MIN_VALUE", new TestContext());
        assertEquals(Long.MIN_VALUE, key);

        key = keyDeserializer.deserializeKey("Long.MAX_VALUE", new TestContext());
        assertNull(key);
    }

    @Test
    public void testFloatKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        assertEquals(Float.class, keyDeserializer.getKeyClass());

        Object key = keyDeserializer.deserializeKey("1.23", new TestContext());
        assertEquals(1.23f, key, 0.001f);

        key = keyDeserializer.deserializeKey("Float.NaN", new TestContext());
        assertEquals(Float.NaN, key);

        key = keyDeserializer.deserializeKey("Float.POSITIVE_INFINITY", new TestContext());
        assertEquals(Float.POSITIVE_INFINITY, key);

        key = keyDeserializer.deserializeKey("Float.NEGATIVE_INFINITY", new TestContext());
        assertNull(key);
    }

    @Test
    public void testDoubleKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        assertEquals(Double.class, keyDeserializer.getKeyClass());

        Object key = keyDeserializer.deserializeKey("1.23", new TestContext());
        assertEquals(1.23, key, 0.001);

        key = keyDeserializer.deserializeKey("Double.NaN", new TestContext());
        assertEquals(Double.NaN, key);

        key = keyDeserializer.deserializeKey("Double.POSITIVE_INFINITY", new TestContext());
        assertEquals(Double.POSITIVE_INFINITY, key);

        key = keyDeserializer.deserializeKey("Double.NEGATIVE_INFINITY", new TestContext());
        assertNull(key);
    }

    @Test
    public void testLocaleKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        assertEquals(Locale.class, keyDeserializer.getKeyClass());

        Object key = keyDeserializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, key);

        key = keyDeserializer.deserializeKey("fr_FR", new TestContext());
        assertEquals(Locale.FRANCE, key);

        key = keyDeserializer.deserializeKey(" InvalidLocale ", new TestContext());
        assertNull(key);
    }

    @Test
    public void testDateKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
        assertEquals(Date.class, keyDeserializer.getKeyClass());

        Object key = keyDeserializer.deserializeKey("2019-11-11T12:13:14.123Z", new TestContext());
        assertEquals("Mon Nov 11 12:13:14 CET 2019", key.toString());

        key = keyDeserializer.deserializeKey("2019-11-11", new TestContext());
        assertEquals("Mon Nov 11 00:00:00 CET 2019", key.toString());

        key = keyDeserializer.deserializeKey("InvalidDate", new TestContext());
        assertNull(key);
    }

    @Test
    public void testCalendarKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
        assertEquals(Calendar.class, keyDeserializer.getKeyClass());

        Object key = keyDeserializer.deserializeKey("2019-11-11", new TestContext());
        assertEquals(2019, key.get(Calendar.YEAR));
        assertEquals(11, key.get(Calendar.MONTH));
        assertEquals(11, key.get(Calendar.DAY_OF_MONTH));

        key = keyDeserializer.deserializeKey("InvalidDate", new TestContext());
        assertNull(key);
    }

    @Test
    public void testUUIDKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);
        assertEquals(UUID.class, keyDeserializer.getKeyClass());

        Object key = keyDeserializer.deserializeKey("00000000-0000-0000-0000-000000000000", new TestContext());
        assertEquals(new UUID("00000000-0000-0000-0000-000000000000"), key);

        key = keyDeserializer.deserializeKey("11111111-1111-1111-1111-111111111111", new TestContext());
        assertEquals(new UUID("11111111-1111-1111-1111-111111111111"), key);

        key = keyDeserializer.deserializeKey("InvalidUUID", new TestContext());
        assertNull(key);
    }

    @Test
    public void testURIKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        assertEquals(URI.class, keyDeserializer.getKeyClass());

        Object key = keyDeserializer.deserializeKey("http://example.com", new TestContext());
        assertEquals("http://example.com", key.toURI().toASCIIString());

        key = keyDeserializer.deserializeKey("https://example.com", new TestContext());
        assertEquals("https://example.com", key.toURI().toASCIIString());

        key = keyDeserializer.deserializeKey("InvalidURI", new TestContext());
        assertNull(key);
    }

    @Test
    public void testURLKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        assertEquals(URL.class, keyDeserializer.getKeyClass());

        Object key = keyDeserializer.deserializeKey("http://example.com", new TestContext());
        assertEquals("http://example.com", key.toString());

        key = keyDeserializer.deserializeKey("https://example.com", new TestContext());
        assertEquals("https://example.com", key.toString());

        key = keyDeserializer.deserializeKey("InvalidURL", new TestContext());
        assertNull(key);
    }

    @Test
    public void testClassKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String[].class);
        assertEquals(String[].class, keyDeserializer.getKeyClass());

        Object key = keyDeserializer.deserializeKey("[Ljava.lang.String;@1", new TestContext());
        assertArrayEquals(new String[] {"one", "two", "three"}, key);

        key = keyDeserializer.deserializeKey("java.util.ArrayList", new TestContext());
        assertEquals(ArrayList.class, key);

        key = keyDeserializer.deserializeKey("InvalidClass", new TestContext());
        assertNull(key);
    }

    @Test
    public void testEnumKeyDeserializer() throws Exception {
        EnumSet<DaysOfWeek> days = EnumSet.allOf(DaysOfWeek.class);
        KeyDeserializer keyDeserializer = new EnumKD(new SimpleEnumResolver(days), null);
        assertEquals(EnumSet.class, keyDeserializer.getKeyClass());

        Object key = keyDeserializer.deserializeKey("MONDAY", new TestContext());
        assertEquals(DaysOfWeek.MONDAY, key);

        key = keyDeserializer.deserializeKey("FRIDAY", new TestContext());
        assertEquals(DaysOfWeek.FRIDAY, key);

        key = keyDeserializer.deserializeKey("INVALID_VALUE", new TestContext());
        assertNull(key);
    }

    @Test
    public void testCustomKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = new DelegatingKD(String.class, new SimpleStringDeserializer());
        assertEquals(String.class, keyDeserializer.getKeyClass());

        Object key = keyDeserializer.deserializeKey("test", new TestContext());
        assertEquals("test", key);
    }

    @Test
    public void testCustomConstructorKeyDeserializer() throws Exception {
        AnnotatedClass annotatedClass = new TestContext().findAnnotation(MyClass.class);
        Constructor<MyClass> constructor = annotatedClass.getDeclaredConstructors()[0];
        AnnotatedConstructor<MyClass> annotatedConstructor = annotatedClass.getDeclaredConstructors()[0];
        KeyDeserializer keyDeserializer = new StringCtorKeyDeserializer(constructor);
        assertEquals(MyClass.class, keyDeserializer.getKeyClass());

        Object key = keyDeserializer.deserializeKey("test", new TestContext());
        assertEquals(new MyClass("test"), key);
    }

    @Test
    public void testCustomFactoryMethodKeyDeserializer() throws Exception {
        AnnotatedClass annotatedClass = new TestContext().findAnnotation(MyClassFactory.class);
        Method method = annotatedClass.findMethod("createInstance");
        KeyDeserializer keyDeserializer = new StringFactoryKeyDeserializer(method);
        assertEquals(MyClass.class, keyDeserializer.getKeyClass());

        Object key = keyDeserializer.deserializeKey("test", new TestContext());
        assertEquals(new MyClass("test"), key);
    }

    private static class TestContext extends TestBase {
        @Override
        public ObjectMapper getObjectMapper() {
            return new ObjectMapper();
        }
    }

    private static class MyClass {
        private final String name;

        MyClass(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "MyClass{" + name + '}';
        }
    }

    private static class MyClassFactory {
        @JsonStatic
        public static MyClass createInstance(String name) {
            return new MyClass(name);
        }
    }

    private static enum DaysOfWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    private static class SimpleEnumResolver extends EnumResolver {
        private final EnumSet<DaysOfWeek> enumValues;

        SimpleEnumResolver(EnumSet<DaysOfWeek> enumValues) {
            this.enumValues = enumValues;
        }

        @Override
        public Enum<?> findEnum(String name) {
            return enumValues.stream().filter(e -> e.name().equalsIgnoreCase(name)).findFirst().orElse(null);
        }

        @Override
        public EnumSet<Object> getEnumIds() {
            return enumValues;
        }
    }

    private static class SimpleStringDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }
}