package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.*;

/**
 * Test class for StdKeyDeserializer
 */
@JacksonAnnotationsProcessingTest
public class StdKeyDeserializerTest {

    @SuppressWarnings("unchecked")
    private static <T> T createInstance(Class<T> type) throws Exception {
        Constructor<T> constructor = (Constructor<T>) Class.forName("com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer")
                .getDeclaredConstructor(int.class, Class.class);
        constructor.setAccessible(true);
        return constructor.newInstance(TypeMatcher.constructTypeMatcher(type).match(int.class) ? 1 : -1, type);
    }

    @Test
    public void testForType() throws Exception {
        Class<?>[] types = new Class<?>[]{String.class, Object.class, Boolean.class, Byte.TYPE, Character.TYPE,
                Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, UUID.class, URI.class, URL.class,
                Class.class, Locale.class, Currency.class, Date.class, Calendar.class};

        for (Class<?> type : types) {
            StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(type);
            assertEquals(type, keyDeserializer.getKeyClass());
        }
    }

    @Test
    public void testBooleanKeyDeserialization() throws Exception {
        StdKeyDeserializer booleanKeyDeserializer = createInstance(StdKeyDeserializer.class);
        assertTrue((Boolean) booleanKeyDeserializer.deserializeKey("true", new TestContext()));
        assertTrue((Boolean) booleanKeyDeserializer.deserializeKey("false", new TestContext()));
        assertEquals(null, booleanKeyDeserializer.deserializeKey(null, new TestContext()));
        assertEquals(null, booleanKeyDeserializer.deserializeKey("unknown", new TestContext()));
    }

    @Test
    public void testByteKeyDeserialization() throws Exception {
        StdKeyDeserializer byteKeyDeserializer = createInstance(StdKeyDeserializer.class);
        assertEquals((Byte) Byte.valueOf((byte) 1), byteKeyDeserializer.deserializeKey("1", new TestContext()));
        assertEquals((Byte) Byte.valueOf((byte) 127), byteKeyDeserializer.deserializeKey("127", new TestContext()));
        assertEquals((Byte) Byte.valueOf((byte) 128), byteKeyDeserializer.deserializeKey("-128", new TestContext()));
        assertEquals(null, byteKeyDeserializer.deserializeKey(null, new TestContext()));
        assertEquals(null, byteKeyDeserializer.deserializeKey("129", new TestContext()));
    }

    @Test
    public void testCharKeyDeserialization() throws Exception {
        StdKeyDeserializer charKeyDeserializer = createInstance(StdKeyDeserializer.class);
        assertEquals((Character) Character.valueOf('A'), charKeyDeserializer.deserializeKey("A", new TestContext()));
        assertEquals((Character) Character.valueOf('_'), charKeyDeserializer.deserializeKey("_", new TestContext()));
        assertEquals(null, charKeyDeserializer.deserializeKey(null, new TestContext()));
        assertEquals(null, charKeyDeserializer.deserializeKey("B", new TestContext()));
    }

    @Test
    public void testShortKeyDeserialization() throws Exception {
        StdKeyDeserializer shortKeyDeserializer = createInstance(StdKeyDeserializer.class);
        assertEquals((Short) Short.valueOf((short) 1), shortKeyDeserializer.deserializeKey("1", new TestContext()));
        assertEquals((Short) Short.valueOf((short) 32767), shortKeyDeserializer.deserializeKey("32767", new TestContext()));
        assertEquals((Short) Short.valueOf((short) 32768), shortKeyDeserializer.deserializeKey("-32768", new TestContext()));
        assertEquals(null, shortKeyDeserializer.deserializeKey(null, new TestContext()));
        assertEquals(null, shortKeyDeserializer.deserializeKey("32769", new TestContext()));
    }

    @Test
    public void testIntegerKeyDeserialization() throws Exception {
        StdKeyDeserializer integerKeyDeserializer = createInstance(StdKeyDeserializer.class);
        assertEquals((Integer) Integer.valueOf(1), integerKeyDeserializer.deserializeKey("1", new TestContext()));
        assertEquals((Integer) Integer.valueOf(2147483647), integerKeyDeserializer.deserializeKey("2147483647", new TestContext()));
        assertEquals((Integer) Integer.valueOf(-2147483648), integerKeyDeserializer.deserializeKey("-2147483648", new TestContext()));
        assertEquals(null, integerKeyDeserializer.deserializeKey(null, new TestContext()));
        assertEquals(null, integerKeyDeserializer.deserializeKey("2147483649", new TestContext()));
    }

    @Test
    public void testLongKeyDeserialization() throws Exception {
        StdKeyDeserializer longKeyDeserializer = createInstance(StdKeyDeserializer.class);
        assertEquals((Long) Long.valueOf(1L), longKeyDeserializer.deserializeKey("1", new TestContext()));
        assertEquals((Long) Long.valueOf(9223372036854775807L), longKeyDeserializer.deserializeKey("9223372036854775807", new TestContext()));
        assertEquals((Long) Long.valueOf(-9223372036854775808L), longKeyDeserializer.deserializeKey("-9223372036854775808", new TestContext()));
        assertEquals(null, longKeyDeserializer.deserializeKey(null, new TestContext()));
        assertEquals(null, longKeyDeserializer.deserializeKey("9223372036854775809", new TestContext()));
    }

    @Test
    public void testFloatKeyDeserialization() throws Exception {
        StdKeyDeserializer floatKeyDeserializer = createInstance(StdKeyDeserializer.class);
        assertEquals((Float) Float.valueOf(1.0f), floatKeyDeserializer.deserializeKey("1.0", new TestContext()));
        assertEquals((Float) Float.valueOf(3.14159265358979323846f), floatKeyDeserializer.deserializeKey("3.14159265358979323846", new TestContext()));
        assertEquals(null, floatKeyDeserializer.deserializeKey(null, new TestContext()));
        assertEquals(null, floatKeyDeserializer.deserializeKey("3.14159265358979323847", new TestContext()));
    }

    @Test
    public void testDoubleKeyDeserialization() throws Exception {
        StdKeyDeserializer doubleKeyDeserializer = createInstance(StdKeyDeserializer.class);
        assertEquals((Double) Double.valueOf(1.0), doubleKeyDeserializer.deserializeKey("1.0", new TestContext()));
        assertEquals((Double) Double.valueOf(3.14159265358979323846), doubleKeyDeserializer.deserializeKey("3.14159265358979323846", new TestContext()));
        assertEquals(null, doubleKeyDeserializer.deserializeKey(null, new TestContext()));
        assertEquals(null, doubleKeyDeserializer.deserializeKey("3.14159265358979323847", new TestContext()));
    }

    @Test
    public void testLocaleKeyDeserialization() throws Exception {
        StdKeyDeserializer localeKeyDeserializer = createInstance(StdKeyDeserializer.class);
        Locale locale = new Locale("en", "US");
        assertEquals(locale, localeKeyDeserializer.deserializeKey("en_US", new TestContext()));
        assertEquals(locale, localeKeyDeserializer.deserializeKey("en_US", new TestContext()));
        assertEquals(null, localeKeyDeserializer.deserializeKey(null, new TestContext()));
        assertEquals(null, localeKeyDeserializer.deserializeKey("es_MX", new TestContext()));
    }

    @Test
    public void testURLKeyDeserialization() throws Exception {
        StdKeyDeserializer urlKeyDeserializer = createInstance(StdKeyDeserializer.class);
        URL url = new URL("http://localhost:8080");
        assertEquals(url, urlKeyDeserializer.deserializeKey("http://localhost:8080", new TestContext()));
        assertEquals(url, urlKeyDeserializer.deserializeKey("http://localhost:8080", new TestContext()));
        assertEquals(null, urlKeyDeserializer.deserializeKey(null, new TestContext()));
        assertEquals(null, urlKeyDeserializer.deserializeKey("http://example.com", new TestContext()));
    }

    @Test
    public void testURIKeyDeserialization() throws Exception {
        StdKeyDeserializer uriKeyDeserializer = createInstance(StdKeyDeserializer.class);
        URI uri = new URI("http://localhost:8080/path");
        assertEquals(uri, uriKeyDeserializer.deserializeKey("http://localhost:8080/path", new TestContext()));
        assertEquals(uri, uriKeyDeserializer.deserializeKey("http://localhost:8080/path", new TestContext()));
        assertEquals(null, uriKeyDeserializer.deserializeKey(null, new TestContext()));
        assertEquals(null, uriKeyDeserializer.deserializeKey("http://example.com", new TestContext()));
    }

    @Test
    public void testClassKeyDeserialization() throws Exception {
        StdKeyDeserializer classKeyDeserializer = createInstance(StdKeyDeserializer.class);
        assertEquals(String.class, classKeyDeserializer.deserializeKey("java.lang.String", new TestContext()));
        assertEquals(String.class, classKeyDeserializer.deserializeKey("java.lang.String", new TestContext()));
        assertEquals(null, classKeyDeserializer.deserializeKey(null, new TestContext()));
        assertEquals(null, classKeyDeserializer.deserializeKey("java.util.List", new TestContext()));
    }

    @Test
    public void testDateKeyDeserialization() throws Exception {
        StdKeyDeserializer dateKeyDeserializer = createInstance(StdKeyDeserializer.class);
        Date date = new Date();
        assertEquals(date, dateKeyDeserializer.deserializeKey("2023-03-31T12:00:00.000Z", new TestContext()));
        assertEquals(date, dateKeyDeserializer.deserializeKey("2023-03-31T12:00:00.000Z", new TestContext()));
        assertEquals(null, dateKeyDeserializer.deserializeKey(null, new TestContext()));
        assertEquals(null, dateKeyDeserializer.deserializeKey("2023-03-31T12:00:00.001Z", new TestContext()));
    }

    @Test
    public void testCalendarKeyDeserialization() throws Exception {
        StdKeyDeserializer calendarKeyDeserializer = createInstance(StdKeyDeserializer.class);
        GregorianCalendar calendar = new GregorianCalendar(2023, Calendar.MARCH, 31);
        assertEquals(calendar, calendarKeyDeserializer.deserializeKey("2023-03-31", new TestContext()));
        assertEquals(calendar, calendarKeyDeserializer.deserializeKey("2023-03-31", new TestContext()));
        assertEquals(null, calendarKeyDeserializer.deserializeKey(null, new TestContext()));
        assertEquals(null, calendarKeyDeserializer.deserializeKey("2023-03-30", new TestContext()));
    }

    @Test
    public void testUUIDKeyDeserialization() throws Exception {
        StdKeyDeserializer uuidKeyDeserializer = createInstance(StdKeyDeserializer.class);
        UUID uuid = UUID.randomUUID();
        assertEquals(uuid, uuidKeyDeserializer.deserializeKey(uuid.toString(), new TestContext()));
        assertEquals(uuid, uuidKeyDeserializer.deserializeKey(uuid.toString(), new TestContext()));
        assertEquals(null, uuidKeyDeserializer.deserializeKey(null, new TestContext()));
        assertEquals(null, uuidKeyDeserializer.deserializeKey("random", new TestContext()));
    }

    @Test
    public void testEnumKeyDeserialization() throws Exception {
        StdKeyDeserializer enumKeyDeserializer = createInstance(StdKeyDeserializer.class);
        testEnumKeyDeserialization(enumKeyDeserializer, MyEnum.class, "FOO", MyEnum.FOO);
        testEnumKeyDeserialization(enumKeyDeserializer, MyEnum.class, "BAR", null);
        testEnumKeyDeserialization(enumKeyDeserializer, MyEnum.class, "FOO_BAR", null);
    }

    private void testEnumKeyDeserialization(StdKeyDeserializer enumKeyDeserializer, Class<? extends Enum> enumType,
                                          String enumKey, Enum expectedEnumValue) throws Exception {
        assertEquals(expectedEnumValue, enumKeyDeserializer.deserializeKey(enumKey, new TestContext()));
        assertEquals(expectedEnumValue, enumKeyDeserializer.deserializeKey(enumKey, new TestContext()));
        if (expectedEnumValue != null) {
            assertEquals(expectedEnumValue.name(), enumKey);
        } else {
            assertEquals(null, enumKeyDeserializer.deserializeKey(enumKey, new TestContext()));
        }
    }

    static class TestContext extends DeserializationContextBase {
        @Override
        public AnnotationIntrospector _annotationIntrospector() {
            return TestAnnotationIntrospector.getInstance();
        }

        @Override
        public SerializerProvider _serializers() {
            return new TestSerializerProvider();
        }

        @Override
        public ObjectMapper _objectMapper() {
            return new TestObjectMapper();
        }
    }

    static class TestAnnotationIntrospector extends StdBeanPropertyAnnotationIntrospector {
        @Override
        public Annotation[] findAnnotationAttributes(JavaType type, String name) {
            return new Annotation[0];
        }

        @Override
        public Annotation[] findAnnotationMembers(JavaType type, boolean allowClassMembers) {
            return new Annotation[0];
        }

        @Override
        public Annotation[] findAnnotationMembers(Class<?> type, boolean allowClassMembers) {
            return new Annotation[0];
        }

        @Override
        public Annotation[] findAnnotationMembers(Method method, boolean allowClassMembers) {
            return new Annotation[0];
        }
    }

    static class TestSerializerProvider extends TestBaseSerializerProvider {
        @Override
        public JsonSerializer<?> findValueSerializer(Class<?> type) {
            return null;
        }
    }

    static class TestObjectMapper extends TestBaseObjectMapper {
        @Override
        public ObjectReader getReaderForValueType(Class<?> valueType) {
            return null;
        }

        @Override
        public ObjectMapper setSerializerInstance(JavaType type, Object serializer) {
            return this;
        }
    }

    static class MyEnum {
        public static enum FOO {
        }

        public static enum BAR {
        }
    }
}