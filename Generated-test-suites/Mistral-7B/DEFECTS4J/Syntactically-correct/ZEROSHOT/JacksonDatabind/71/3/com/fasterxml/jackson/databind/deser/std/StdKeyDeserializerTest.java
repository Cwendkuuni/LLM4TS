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
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer;

/**
 * Test for StdKeyDeserializer class.
 */
public class StdKeyDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testBooleanDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);

        DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();

        assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", ctxt));
        assertEquals(Boolean.FALSE, deserializer.deserializeKey("false", ctxt));
        assertNull(deserializer.deserializeKey("invalid", ctxt));
    }

    @Test
    public void testByteDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);

        DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();

        assertEquals((byte) 5, deserializer.deserializeKey("5", ctxt));
        assertEquals((byte) -5, deserializer.deserializeKey("-5", ctxt));
        assertNull(deserializer.deserializeKey("invalid", ctxt));
    }

    @Test
    public void testShortDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);

        DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();

        assertEquals((short) 5, deserializer.deserializeKey("5", ctxt));
        assertEquals((short) -5, deserializer.deserializeKey("-5", ctxt));
        assertNull(deserializer.deserializeKey("invalid", ctxt));
    }

    @Test
    public void testCharDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);

        DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();

        assertEquals('A', deserializer.deserializeKey("A", ctxt));
        assertNull(deserializer.deserializeKey("invalid", ctxt));
    }

    @Test
    public void testIntDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);

        DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();

        assertEquals(5, deserializer.deserializeKey("5", ctxt));
        assertEquals(-5, deserializer.deserializeKey("-5", ctxt));
        assertNull(deserializer.deserializeKey("invalid", ctxt));
    }

    @Test
    public void testLongDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);

        DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();

        assertEquals(5L, deserializer.deserializeKey("5", ctxt));
        assertEquals(-5L, deserializer.deserializeKey("-5", ctxt));
        assertNull(deserializer.deserializeKey("invalid", ctxt));
    }

    @Test
    public void testFloatDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);

        DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();

        assertEquals(5.0f, deserializer.deserializeKey("5.0", ctxt), 0.01f);
        assertNull(deserializer.deserializeKey("invalid", ctxt));
    }

    @Test
    public void testDoubleDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);

        DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();

        assertEquals(5.0, deserializer.deserializeKey("5.0", ctxt), 0.01);
        assertNull(deserializer.deserializeKey("invalid", ctxt));
    }

    @Test
    public void testLocaleDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);

        DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();

        assertEquals(Locale.US, deserializer.deserializeKey("en_US", ctxt));
        assertNull(deserializer.deserializeKey("invalid", ctxt));
    }

    @Test
    public void testDateDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);

        DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();

        assertEquals(new Date(), deserializer.deserializeKey("2022-01-01", ctxt));
        assertNull(deserializer.deserializeKey("invalid", ctxt));
    }

    @Test
    public void testCalendarDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);

        DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2022);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        assertEquals(calendar, deserializer.deserializeKey("2022-01-01", ctxt));
        assertNull(deserializer.deserializeKey("invalid", ctxt));
    }

    @Test
    public void testUUIDDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);

        DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();

        assertEquals(new UUID("123e4567-e89b-12d3-a456-426614174000"), deserializer.deserializeKey("123e4567-e89b-12d3-a456-426614174000", ctxt));
        assertNull(deserializer.deserializeKey("invalid", ctxt));
    }

    @Test
    public void testURIDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);

        DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();

        assertEquals(new URI("http://example.com"), deserializer.deserializeKey("http://example.com", ctxt));
        assertNull(deserializer.deserializeKey("invalid", ctxt));
    }

    @Test
    public void testURLDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);

        DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();

        assertEquals(new URL("http://example.com"), deserializer.deserializeKey("http://example.com", ctxt));
        assertNull(deserializer.deserializeKey("invalid", ctxt));
    }

    @Test
    public void testClassDeserializer() throws Exception {
        Class<?> clazz = Class.forName("java.util.Date");

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(clazz);

        DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();

        assertEquals(Date.class, deserializer.getKeyClass());

        assertEquals(Date.class, deserializer.deserializeKey("java.util.Date", ctxt));
        assertNull(deserializer.deserializeKey("invalid", ctxt));
    }

    @Test
    public void testEnumDeserializer() throws Exception {
        enum TestEnum {
            VALUE1, VALUE2
        }

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(TestEnum.class);

        DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();

        assertEquals(TestEnum.VALUE1, deserializer.deserializeKey("VALUE1", ctxt));
        assertEquals(TestEnum.VALUE2, deserializer.deserializeKey("VALUE2", ctxt));
        assertNull(deserializer.deserializeKey("invalid", ctxt));
    }

    @JsonDeserialize(using = StdKeyDeserializer.class)
    static class TestClass {
        private int intValue;

        @JsonDeserialize(using = StringCtorKeyDeserializer.class)
        private final TestEnum enumValue;

        public TestClass(int intValue, TestEnum enumValue) {
            this.intValue = intValue;
            this.enumValue = enumValue;
        }

        public int getIntValue() {
            return intValue;
        }

        public TestEnum getEnumValue() {
            return enumValue;
        }
    }

    @Test
    public void testCustomDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(TestClass.class);

        AnnotatedClass annotatedClass = mapper.getTypeFactory().constructType(TestClass.class);
        AnnotatedField intField = annotatedClass.findField("intValue");
        AnnotatedMethod enumMethod = annotatedClass.findMember("enumValue");

        DeserializationContext ctxt = mapper.getSerializationConfig().getDefaultDeserializationContext();

        assertEquals(TestClass.class, deserializer.getKeyClass());

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer();

        String json = "{\"intValue\":5,\"enumValue\":\"VALUE1\"}";
        TestClass testClass = objectMapper.readValue(json, TestClass.class);

        assertEquals(5, deserializer.deserializeKey("intValue", ctxt).getValue());
        assertEquals(TestEnum.VALUE1, deserializer.deserializeKey("enumValue", ctxt).getValue());

        ObjectReader objectReader = mapper.readerForType(TestClass.class);
        TestClass deserializedTestClass = objectReader.readValue(json);

        assertEquals(testClass.getIntValue(), deserializedTestClass.getIntValue());
        assertEquals(testClass.getEnumValue(), deserializedTestClass.getEnumValue());

        AnnotatedMethod intSetter = annotatedClass.findMethod("setIntValue", intField.getType());
        AnnotatedMethod enumSetter = annotatedClass.findMethod("setEnumValue", enumMethod.getType());

        Constructor<TestClass> constructor = TestClass.class.getDeclaredConstructor(int.class, TestEnum.class);
        constructor.setAccessible(true);

        TestClass testClassToDeserialize = new TestClass(1, TestEnum.VALUE1);

        String jsonToDeserialize = mapper.writerForTypeValue(testClassToDeserialize).writeValueAsString();

        assertEquals(testClassToDeserialize.getIntValue(), deserializer.deserializeKey("intValue", ctxt, jsonToDeserialize).getValue());
        assertEquals(testClassToDeserialize.getEnumValue(), deserializer.deserializeKey("enumValue", ctxt, jsonToDeserialize).getValue());

        constructor.setAccessible(false);

        TestClass testClassToDeserializeWithSetters = new TestClass(1, TestEnum.VALUE1);

        AnnotatedMethod setIntValue = intSetter.getStaticAnnotation(JsonDeserialize.class);
        AnnotatedMethod setEnumValue = enumSetter.getStaticAnnotation(JsonDeserialize.class);

        JsonDeserializer<?> intDeserializer = setIntValue.getAnnotation(JsonDeserialize.class).using();
        JsonDeserializer<?> enumDeserializer = setEnumValue.getAnnotation(JsonDeserialize.class).using();

        StdKeyDeserializer intKeyDeserializer = (StdKeyDeserializer) intDeserializer;
        StdKeyDeserializer enumKeyDeserializer = (StdKeyDeserializer) enumDeserializer;

        deserializer = new DelegatingKD(TestClass.class, intKeyDeserializer);
        deserializer = new DelegatingKD(TestClass.class, enumKeyDeserializer);

        assertEquals(testClassToDeserializeWithSetters.getIntValue(), deserializer.deserializeKey("intValue", ctxt, jsonToDeserialize).getValue());
        assertEquals(testClassToDeserializeWithSetters.getEnumValue(), deserializer.deserializeKey("enumValue", ctxt, jsonToDeserialize).getValue());
    }
}