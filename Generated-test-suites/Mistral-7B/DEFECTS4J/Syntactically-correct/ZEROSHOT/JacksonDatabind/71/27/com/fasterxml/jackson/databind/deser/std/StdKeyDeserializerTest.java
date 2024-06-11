package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

/**
 * Test class for StdKeyDeserializer
 */
public class StdKeyDeserializerTest {

    @Test
    public void testBooleanKeyDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);

        Object deserialzedKey = serializer.deserializeKey("true", new TestContext());
        assertEquals(Boolean.TRUE, deserialzedKey);

        deserialzedKey = serializer.deserializeKey("false", new TestContext());
        assertEquals(Boolean.FALSE, deserialzedKey);

        deserialzedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserialzedKey);
    }

    @Test
    public void testByteKeyDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);

        Object deserialzedKey = serializer.deserializeKey("123", new TestContext());
        assertEquals((Byte) 123, deserialzedKey);

        deserialzedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserialzedKey);
    }

    @Test
    public void testShortKeyDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);

        Object deserialzedKey = serializer.deserializeKey("12345", new TestContext());
        assertEquals((Short) 12345, deserialzedKey);

        deserialzedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserialzedKey);
    }

    @Test
    public void testCharKeyDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);

        Object deserialzedKey = serializer.deserializeKey("A", new TestContext());
        assertEquals(Character.valueOf('A'), deserialzedKey);

        deserialzedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserialzedKey);
    }

    @Test
    public void testIntKeyDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);

        Object deserialzedKey = serializer.deserializeKey("123456", new TestContext());
        assertEquals((Integer) 123456, deserialzedKey);

        deserialzedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserialzedKey);
    }

    @Test
    public void testLongKeyDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);

        Object deserialzedKey = serializer.deserializeKey("1234567890123456", new TestContext());
        assertEquals((Long) 1234567890123456L, deserialzedKey);

        deserialzedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserialzedKey);
    }

    @Test
    public void testFloatKeyDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);

        Object deserialzedKey = serializer.deserializeKey("3.14", new TestContext());
        assertEquals((Float) 3.14f, deserialzedKey);

        deserialzedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserialzedKey);
    }

    @Test
    public void testDoubleKeyDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);

        Object deserialzedKey = serializer.deserializeKey("3.14159265358979323846", new TestContext());
        assertEquals((Double) 3.14159265358979323846, deserialzedKey);

        deserialzedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserialzedKey);
    }

    @Test
    public void testLocaleKeyDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);

        Object deserialzedKey = serializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, deserialzedKey);

        deserialzedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserialzedKey);
    }

    @Test
    public void testDateKeyDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);

        Object deserialzedKey = serializer.deserializeKey("2022-01-01", new TestContext());
        assertEquals(new Date(1640995200000L), deserialzedKey);

        deserialzedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserialzedKey);
    }

    @Test
    public void testCalendarKeyDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);

        Object deserialzedKey = serializer.deserializeKey("2022-01-01", new TestContext());
        Calendar calendar = (Calendar) deserialzedKey;
        assertEquals(Calendar.getInstance(Locale.US), calendar);
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(1, calendar.get(Calendar.MONTH));
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));

        deserialzedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserialzedKey);
    }

    @Test
    public void testUUIDKeyDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(UUID.class);

        Object deserialzedKey = serializer.deserializeKey("00000000-0000-0000-0000-000000000001", new TestContext());
        assertEquals(new UUID("00000000-0000-0000-0000-000000000001"), deserialzedKey);

        deserialzedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserialzedKey);
    }

    @Test
    public void testUriKeyDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);

        Object deserialzedKey = serializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URI("http://example.com"), deserialzedKey);

        deserialzedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserialzedKey);
    }

    @Test
    public void testUrlKeyDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);

        Object deserialzedKey = serializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URL("http://example.com"), deserialzedKey);

        deserialzedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserialzedKey);
    }

    @Test
    public void testClassKeyDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(String.class);

        Object deserialzedKey = serializer.deserializeKey("com.example.MyClass", new TestContext());
        assertEquals(String.class, deserialzedKey.getClass());

        deserialzedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserialzedKey);
    }

    @Test
    public void testEnumKeyDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(MyEnum.class);

        Object deserialzedKey = serializer.deserializeKey("MY_VALUE", new TestContext());
        assertEquals(MyEnum.MY_VALUE, deserialzedKey);

        deserialzedKey = serializer.deserializeKey("invalid", new TestContext());
        assertNull(deserialzedKey);
    }

    @Test(expected = JsonMappingException.class)
    public void testUnknownEnumValueKeyDeserializer() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(MyEnum.class);

        Object deserialzedKey = serializer.deserializeKey("UNKNOWN_VALUE", new TestContext());
        // should throw JsonMappingException
    }

    @Test
    public void testUriDeserializerWithTrailingSlash() throws Exception {
        KeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);

        Object deserialzedKey = serializer.deserializeKey("http://example.com/", new TestContext());
        assertEquals(new URI("http://example.com/"), deserialzedKey);
    }

    @Test
    public void testDelegateKeyDeserializer() throws Exception {
        class MyClass {
            private String name;

            public MyClass(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }

        class MyKeyDeserializer extends StdKeyDeserializer {
            private final JsonDeserializer<MyClass> _delegate;

            public MyKeyDeserializer(JsonDeserializer<MyClass> deserializer) {
                super(-1, MyClass.class);
                _delegate = deserializer;
            }

            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return _delegate.deserialize(ctxt.getParser(), ctxt);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<MyClass> deserializer = mapper.deserializationContext().findValueDeserializer(MyClass.class);

        KeyDeserializer serializer = new MyKeyDeserializer(deserializer);

        Object deserialzedKey = serializer.deserializeKey("{\"name\": \"MyClassValue\"}", new TestContext());
        assertEquals(new MyClass("MyClassValue"), deserialzedKey);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectFactory getFactory() {
            return new StandardSerializerFactory();
        }

        @Override
        public Object findValueDeserializer(Class<?> valueType) throws JsonMappingException {
            return null;
        }

        @Override
        public Object findValueDeserializer(Class<?> valueType, String propertyName) throws JsonMappingException {
            return null;
        }

        @Override
        public java.lang.reflect.TypeHandler findTypeHandler(DeserializationConfig config, Type descriptor) {
            return null;
        }

        @Override
        public boolean isEnabled(DeserializationFeature feature) {
            return true;
        }
    }

    private static class MyEnum {
        public static final MyEnum MY_VALUE = new MyEnum();
        public static final MyEnum OTHER_VALUE = new MyEnum();

        private MyEnum() {
        }
    }
}