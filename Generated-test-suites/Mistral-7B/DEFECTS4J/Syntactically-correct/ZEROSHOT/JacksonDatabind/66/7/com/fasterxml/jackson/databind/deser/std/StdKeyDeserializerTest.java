package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializerProvider;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorImpl;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TestBase;

public class StdKeyDeserializerTest extends TestBase {

    public void testForType() {
        Class<?>[] types = {String.class, Object.class, Integer.class, Long.class, Date.class, Calendar.class,
                Boolean.class, Byte.class, Character.class, Short.class, Float.class, Double.class, URI.class,
                URL.class, Locale.class, Currency.class};

        for (Class<?> type : types) {
            KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(type);
            assertEquals(type, keyDeserializer.getKeyClass());
            if (type.isEnum()) {
                assertEquals(StdKeyDeserializer.Type.TYPE_LOCALE, keyDeserializer._kind);
            } else {
                assertEquals(StdKeyDeserializer.Type.UNDEFINED, keyDeserializer._kind);
            }
        }

        assertNull(StdKeyDeserializer.forType(String[].class));
        assertNull(StdKeyDeserializer.forType(Map.class));
    }

    public void testDeserializeKey() throws IOException, JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        DeserializerProvider provider = mock(DeserializerProvider.class);
        when(ctxt.getDeserializerProvider()).thenReturn(provider);
        when(provider.findValueDeserializer(URI.class)).thenReturn(mock(JsonDeserializer.class));

        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.Type.TYPE_URI, URI.class);

        // Test null key
        String key = null;
        Object result = keyDeserializer.deserializeKey(key, ctxt);
        assertEquals(null, result);

        // Test valid key
        key = "http://www.example.com";
        result = keyDeserializer.deserializeKey(key, ctxt);
        URI uri = new URI(key);
        assertEquals(uri, result);

        // Test invalid key
        key = "invalid_uri";
        result = keyDeserializer.deserializeKey(key, ctxt);
        assertNull(result);
    }

    public void testParse() throws IOException, JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.getParser()).thenReturn(mock(NumberInput.class));

        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.Type.TYPE_INT, Integer.class);

        // Test int parse
        String key = "123";
        Object result = keyDeserializer._parse(key, ctxt);
        assertEquals(123, result);

        // Test long parse
        key = "12345678901234567890";
        result = keyDeserializer._parse(key, ctxt);
        assertEquals(12345678901234567890L, result);

        // Test double parse
        key = "3.14159265358979323846";
        result = keyDeserializer._parse(key, ctxt);
        assertEquals(3.14159265358979323846, result, 0.0001);

        // Test invalid parse
        key = "invalid_number";
        result = keyDeserializer._parse(key, ctxt);
        assertNull(result);
    }

    public void testStringKD() {
        StdKeyDeserializer stringKD = StdKeyDeserializer.StringKD.forType(String.class);

        assertEquals(String.class, stringKD.getKeyClass());
        assertEquals(StdKeyDeserializer.Type.TYPE_STRING, stringKD._kind);

        Object result = stringKD.deserializeKey("test", mock(DeserializationContext.class));
        assertEquals("test", result);
    }

    public void testDelegatingKD() throws IOException {
        BeanDescription beanDesc = new BeanDescription(TestBean.class);
        AnnotationIntrospector introspector = new AnnotationIntrospectorImpl();
        JsonDeserializer<TestBean> deserializer = introspector.findBeanDeserializer(TestBean.class, beanDesc);

        StdKeyDeserializer delegatingKD = new StdKeyDeserializer(StdKeyDeserializer.Type.UNDEFINED, TestBean.class);
        delegatingKD._delegate = deserializer;

        TestBean bean = new TestBean();
        String key = "test";
        Object result = delegatingKD.deserializeKey(key, mock(DeserializationContext.class));
        assertEquals(bean, result);
    }

    @JsonAutoDetect
    static class TestBean {
        @JsonProperty
        private String test;

        @JsonCreator
        public TestBean(@JsonProperty("test") String test) {
            this.test = test;
        }

        @JsonValue
        public String toString() {
            return test;
        }
    }

    public void testEnumKD() throws IOException {
        EnumSet<TestEnum> enumValues = EnumSet.allOf(TestEnum.class);
        Map<String, TestEnum> enumMap = new HashMap<>();
        for (TestEnum value : enumValues) {
            enumMap.put(value.name(), value);
        }

        AnnotationIntrospector introspector = new AnnotationIntrospectorImpl();
        EnumResolver enumResolver = introspector.findValueSerializer(TestEnum.class, null);

        StdKeyDeserializer enumKD = new StdKeyDeserializer(StdKeyDeserializer.Type.TYPE_LOCALE, TestEnum.class);
        enumKD._byNameResolver = enumResolver;

        for (TestEnum value : enumValues) {
            Object result = enumKD.deserializeKey(value.name(), mock(DeserializationContext.class));
            assertEquals(value, result);
        }

        try {
            enumKD.deserializeKey("not_valid_value", mock(DeserializationContext.class));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    public void testStringCtorKeyDeserializer() throws IOException, InstantiationException, IllegalAccessException {
        Class<?> testClass = TestStringCtor.class;
        Constructor<?> constructor = testClass.getDeclaredConstructor(String.class);

        StdKeyDeserializer stringCtorKeyDeserializer = new StdKeyDeserializer(constructor);

        Object result = stringCtorKeyDeserializer.deserializeKey("test", mock(DeserializationContext.class));
        assertEquals(new TestStringCtor("test"), result);
    }

    public void testStringFactoryKeyDeserializer() throws IOException, InstantiationException, IllegalAccessException {
        Class<?> testClass = TestStringFactory.class;
        Method factoryMethod = testClass.getDeclaredMethod("createInstance", String.class);

        StdKeyDeserializer stringFactoryKeyDeserializer = new StdKeyDeserializer(factoryMethod);

        Object result = stringFactoryKeyDeserializer.deserializeKey("test", mock(DeserializationContext.class));
        assertEquals(TestStringFactory.createInstance("test"), result);
    }

    static class TestEnum {
        public String name() {
            return name();
        }
    }

    static class TestStringCtor {
        private final String value;

        TestStringCtor(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    static class TestStringFactory {
        public static TestStringFactory createInstance(String value) {
            return new TestStringFactory();
        }

        private String value;

        public TestStringFactory() {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}