package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Test class for {@link JsonValueSerializer}.
 */
public class JsonValueSerializerTest {

    private static final ObjectMapper objectMapper;
    static {
        objectMapper = JsonMapperFactory.getMapper();
    }

    private JsonValueSerializer serializer;
    private BeanSerializerBase delegateSerializer;

    @Before
    public void setUp() throws Exception {
        Class<?> testClass = JsonValueSerializer.class;
        Method accessorMethod = testClass.getDeclaredMethod("getValue");
        accessorMethod.setAccessible(true);

        Class<?> enumClass = Enum.class;
        Class<?> annotatedClass = AnnotatedClass.class;

        JavaType javaType = objectMapper.getTypeFactory().constructType(annotatedClass);
        serializer = new JsonValueSerializer(accessorMethod, null);
        delegateSerializer = new StdSerializer<Object>(javaType) {};
    }

    @After
    public void tearDown() throws Exception {
        serializer = null;
        delegateSerializer = null;
    }

    @Test
    public void testSerializationOfEnum() throws IOException {
        Enum enumValue = Enum.values()[0];
        Object serialized = serializer.serialize(enumValue, objectMapper.getFactory().getGenerator(), objectMapper.getSerializerProvider());
        assertEquals(enumValue.name(), serialized);
    }

    @Test
    public void testDeserializationOfEnum() throws IOException {
        String json = "\"RED\"";
        Enum enumValue = objectMapper.readValue(json, Enum.class);
        assertEquals(Enum.RED, enumValue);
    }

    @Test
    public void testSerializationOfCustomClass() throws IOException {
        Object bean = new CustomClass("Custom value");
        Object serialized = serializer.serialize(bean, objectMapper.getFactory().getGenerator(), objectMapper.getSerializerProvider());
        assertEquals("Custom value", serialized);
    }

    @Test
    public void testDeserializationOfCustomClass() throws IOException {
        String json = "\"Custom value\"";
        Object deserialized = objectMapper.readValue(json, CustomClass.class);
        assertEquals("Custom value", deserialized);
    }

    @Test
    public void testSerializationWithTypeInformation() throws IOException {
        Object bean = new CustomClass("Custom value");
        Object serialized = serializer.serializeWithType(bean, objectMapper.getFactory().getGenerator(), objectMapper.getSerializerProvider(), objectMapper.getTypeSerializer());
        assertEquals("CustomClass", serialized);
    }

    @Test
    public void testDeserializationWithTypeInformation() throws IOException {
        String json = "{\"@class\":\"CustomClass\", \"value\":\"Custom value\"}";
        Object deserialized = objectMapper.readValue(json, CustomClass.class);
        assertEquals("Custom value", deserialized);
    }

    @Test
    public void testSerializationOfNullValue() throws IOException {
        Object bean = null;
        Object serialized = serializer.serialize(bean, objectMapper.getFactory().getGenerator(), objectMapper.getSerializerProvider());
        assertNull(serialized);
    }

    @Test
    public void testSerializationOfEnumWithNoJsonValueAnnotatedMethod() throws IOException {
        Enum enumValue = Enum.values()[1];
        Object serialized = serializer.serialize(enumValue, objectMapper.getFactory().getGenerator(), objectMapper.getSerializerProvider());
        assertNotSame(enumValue.name(), serialized);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationOfEnumWithNoJsonValueAnnotatedMethod() throws IOException {
        String json = "\"UnknownValue\"";
        objectMapper.readValue(json, Enum.class);
    }

    @Test
    public void testSerializationOfBeanWithJsonValueAnnotatedMethod() throws IOException {
        Object bean = new BeanWithJsonValueAnnotatedMethod();
        Object serialized = serializer.serialize(bean, objectMapper.getFactory().getGenerator(), objectMapper.getSerializerProvider());
        assertEquals("value", serialized);
    }

    @Test
    public void testSerializationOfBeanWithValueSerializer() throws IOException {
        Object bean = new BeanWithValueSerializer();
        JsonSerializer<String> valueSerializer = objectMapper.getSerializerProvider().findValueSerializer(String.class);
        JsonSerializer<BeanWithValueSerializer> serializer = new JsonValueSerializer(new MethodRefSerializer(BeanWithValueSerializer.class, "getValue"), valueSerializer);
        Object serialized = serializer.serialize(bean, objectMapper.getFactory().getGenerator(), objectMapper.getSerializerProvider());
        assertEquals("value", serialized);
    }

    @Test
    public void testDeserializationOfBeanWithJsonValueAnnotatedMethod() throws IOException {
        String json = "{\"value\":\"test\"}";
        BeanWithJsonValueAnnotatedMethod deserialized = objectMapper.readValue(json, BeanWithJsonValueAnnotatedMethod.class);
        assertEquals("test", deserialized.getValue());
    }

    @Test
    public void testDeserializationOfBeanWithValueSerializer() throws IOException {
        String json = "{\"value\":\"test\"}";
        BeanWithValueSerializer deserialized = objectMapper.readValue(json, BeanWithValueSerializer.class);
        assertEquals("test", deserialized.getValue());
    }

    @Test
    public void testSerializationOfBeanWithDelegateSerializer() throws IOException {
        ObjectMapper delegateMapper = JsonMapperFactory.getMapper();
        ObjectMapper objectMapperWithDelegate = new ObjectMapper(delegateMapper.getSerializerProvider());
        ObjectMapper objectMapperWithSerializer = new ObjectMapper(delegateSerializer);

        Object bean = new CustomClass("Custom value");
        Object serialized = objectMapperWithSerializer.writeValueAsBytes(bean);
        assertNotNull(serialized);

        Object deserialized = objectMapperWithDelegate.readValue(serialized, CustomClass.class);
        assertEquals(deserialized.toString(), bean.toString());
    }

    @Test
    public void testSerializationOfEnumWithStringValue() throws IOException {
        Enum enumValue = Enum.RED;
        Object serialized = serializer.serialize(enumValue, objectMapper.getFactory().getGenerator(), objectMapper.getSerializerProvider());
        assertEquals("RED", serialized);
    }

    @Test
    public void testSerializationOfEnumWithIntValue() throws IOException {
        Enum enumValue = Enum.GREEN;
        Object serialized = serializer.serialize(enumValue, objectMapper.getFactory().getGenerator(), objectMapper.getSerializerProvider());
        assertEquals(Integer.valueOf(enumValue.ordinal()), serialized);
    }

    @Test
    public void testSerializationOfEnumWithLongValue() throws IOException {
        Enum enumValue = Enum.BLUE;
        Object serialized = serializer.serialize(enumValue, objectMapper.getFactory().getGenerator(), objectMapper.getSerializerProvider());
        assertEquals(Long.valueOf(enumValue.ordinal()), serialized);
    }

    @Test
    public void testSerializationOfEnumWithDoubleValue() throws IOException {
        Enum enumValue = Enum.YELLOW;
        Object serialized = serializer.serialize(enumValue, objectMapper.getFactory().getGenerator(), objectMapper.getSerializerProvider());
        assertEquals(Double.valueOf(enumValue.ordinal()), serialized);
    }

    @Test
    public void testSerializationOfEnumWithCharValue() throws IOException {
        Enum enumValue = Enum.WHITE;
        Object serialized = serializer.serialize(enumValue, objectMapper.getFactory().getGenerator(), objectMapper.getSerializerProvider());
        assertEquals(Character.valueOf(enumValue.charAt(0)), serialized);
    }

    @Test
    public void testSerializationOfEnumWithBooleanValue() throws IOException {
        Enum enumValue = Enum.FALSE;
        Object serialized = serializer.serialize(enumValue, objectMapper.getFactory().getGenerator(), objectMapper.getSerializerProvider());
        assertEquals(Boolean.FALSE, serialized);
    }

    @Test
    public void testSerializationOfEnumWithBooleanValueTrue() throws IOException {
        Enum enumValue = Enum.TRUE;
        Object serialized = serializer.serialize(enumValue, objectMapper.getFactory().getGenerator(), objectMapper.getSerializerProvider());
        assertEquals(Boolean.TRUE, serialized);
    }

    private static class BeanWithJsonValueAnnotatedMethod {
        @JsonValue
        public String getValue() {
            return "value";
        }
    }

    private static class BeanWithValueSerializer {
        public String value;

        public String getValue() {
            return value;
        }
    }

    private static class CustomClass {
        private final String value;

        public CustomClass(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private static class AnnotatedClass {
        @JsonValue
        public int getValue() {
            return 1;
        }
    }

    private static class MethodRefSerializer<T> implements JsonSerializer<T> {
        private final Class<T> clazz;
        private final JsonSerializer<Object> delegateSerializer;

        public MethodRefSerializer(Class<T> clazz, JsonSerializer<Object> delegateSerializer) {
            this.clazz = clazz;
            this.delegateSerializer = delegateSerializer;
        }

        @Override
        public void serialize(T value, JavaType type, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            delegateSerializer.serialize(ClassUtil.getRawClass(clazz), value, gen, serializers);
        }
    }
}