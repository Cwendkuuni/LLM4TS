package com.example.jsonvalueserializer;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JsonValueSerializerTest {

    private ObjectNode testObject;
    private JsonGenerator testGenerator;
    private SerializerProvider testProvider;
    private ObjectMapper testMapper;

    @BeforeEach
    void setup() throws Exception {
        testObject = JsonProcessing.objectNodeFactory.objectNode();
        testGenerator = mock(JsonGenerator.class);
        testProvider = new StandardSerializerProvider();
        testMapper = new ObjectMapper();
        testMapper.setSerializerProvider(testProvider);
    }

    @Test
    void testSerializeWithNullValue() throws JsonProcessingException {
        SerializableJsonValue nullJsonValue = new SerializableJsonValue(null);
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);

        serializer.serialize(nullJsonValue, testGenerator, testProvider);
        verify(testGenerator, times(1)).writeNull();
    }

    @Test
    void testSerializeWithStringValue() throws Exception {
        String stringValue = "test";
        JsonValueSerializer serializer = new JsonValueSerializer(
                createJsonValueSerializerAnnotatedMethod(String.class, "getStringValue"),
                new StringSerializer(testProvider),
                null,
                false
        );

        Object testObject = new TestStringValue();
        serializer.serialize(new SerializableJsonValue(testObject, createGetMethod(TestStringValue.class, "getStringValue")), testGenerator, testProvider);

        verify(testGenerator, times(1)).writeString(stringValue);
    }

    @Test
    void testSerializeWithIntegerValue() throws Exception {
        int integerValue = 42;
        JsonValueSerializer serializer = new JsonValueSerializer(
                createJsonValueSerializerAnnotatedMethod(Integer.class, "getIntValue"),
                new IntegerSerializer(testProvider),
                null,
                false
        );

        Object testObject = new TestIntegerValue();
        serializer.serialize(new SerializableJsonValue(testObject, createGetMethod(TestIntegerValue.class, "getIntValue")), testGenerator, testProvider);

        verify(testGenerator, times(1)).writeNumber(integerValue);
    }

    @Test
    void testSerializeWithBooleanValue() throws Exception {
        boolean booleanValue = true;
        JsonValueSerializer serializer = new JsonValueSerializer(
                createJsonValueSerializerAnnotatedMethod(Boolean.class, "getBooleanValue"),
                new BooleanSerializer(testProvider),
                null,
                false
        );

        Object testObject = new TestBooleanValue();
        serializer.serialize(new SerializableJsonValue(testObject, createGetMethod(TestBooleanValue.class, "getBooleanValue")), testGenerator, testProvider);

        verify(testGenerator, times(1)).writeBoolean(booleanValue);
    }

    @Test
    void testSerializeWithEnumValue() throws Exception {
        TestEnumValue testEnumValue = TestEnumValue.VALUE_1;
        JsonValueSerializer serializer = new JsonValueSerializer(
                createJsonValueSerializerAnnotatedMethod(TestEnumValue.class, "getName"),
                new BaseSerializer(testProvider),
                null,
                false
        );

        Object testObject = testEnumValue;
        serializer.serialize(new SerializableJsonValue(testObject, createGetMethod(TestEnumValue.class, "getName")), testGenerator, testProvider);

        verify(testGenerator, times(1)).writeString(testEnumValue.getName());
    }

    @Test
    void testSerializeWithCustomSerializer() throws Exception {
        String testString = "test";
        JsonValueSerializer serializer = new JsonValueSerializer(
                createJsonValueSerializerAnnotatedMethod(CustomSerializableValue.class, "getValue"),
                new CustomSerializer(testProvider),
                null,
                false
        );

        Object testObject = new CustomSerializableValue(testString);
        serializer.serialize(new SerializableJsonValue(testObject, createGetMethod(CustomSerializableValue.class, "getValue")), testGenerator, testProvider);

        verify(testGenerator, times(1)).writeString(testString);
    }

    @Test
    void testCreateContextualSerializerWithNullValue() {
        JsonSerializer<Object> serializer = new JsonValueSerializer.DefaultSerializer();

        JsonSerializer<Object> result = serializer.createContextual(testProvider, null);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result instanceof JsonValueSerializer);
    }

    @Test
    void testCreateContextualSerializerWithStringValue() {
        JsonSerializer<Object> valueSerializer = new StringSerializer(testProvider);
        JsonValueSerializer serializer = new JsonValueSerializer(
                createJsonValueSerializerAnnotatedMethod(String.class, "getStringValue"),
                valueSerializer,
                null,
                false
        );

        JsonSerializer<Object> result = serializer.createContextual(testProvider, null);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof JsonValueSerializer);
        Assertions.assertEquals(valueSerializer, result._valueSerializer);
    }

    @Test
    void testCreateContextualSerializerWithEnumValue() {
        JsonSerializer<Object> valueSerializer = new EnumSerializer(testProvider, TestEnumValue.class);
        JsonValueSerializer serializer = new JsonValueSerializer(
                createJsonValueSerializerAnnotatedMethod(TestEnumValue.class, "getName"),
                valueSerializer,
                null,
                false
        );

        JsonSerializer<Object> result = serializer.createContextual(testProvider, null);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof JsonValueSerializer);
        Assertions.assertEquals(valueSerializer, result._valueSerializer);
    }

    @Test
    void testCreateContextualSerializerWithCustomSerializer() {
        JsonSerializer<Object> valueSerializer = new CustomSerializer(testProvider);
        JsonValueSerializer serializer = new JsonValueSerializer(
                createJsonValueSerializerAnnotatedMethod(CustomSerializableValue.class, "getValue"),
                valueSerializer,
                null,
                false
        );

        JsonSerializer<Object> result = serializer.createContextual(testProvider, null);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof JsonValueSerializer);
        Assertions.assertEquals(valueSerializer, result._valueSerializer);
    }

    @Test
    void testGetSchema() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(
                createJsonValueSerializerAnnotatedMethod(String.class, "getStringValue"),
                new StringSerializer(testProvider),
                null, false
        );

        ObjectNode schema = serializer.getSchema(testProvider, null);

        Assertions.assertEquals("string", schema.get("type").textValue());
    }

    private Method createJsonValueSerializerAnnotatedMethod(Class<?> clazz, String methodName) {
        try {
            return TestUtil.findAnnotatedMethod(clazz, methodName, JsonValue.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Method createGetMethod(Class<?> clazz, String methodName) {
        try {
            return clazz.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static class TestStringValue {
        @JsonValue
        public String getStringValue() {
            return "test";
        }
    }

    private static class TestIntegerValue {
        @JsonValue
        public int getIntValue() {
            return 42;
        }
    }

    private static class TestBooleanValue {
        @JsonValue
        public boolean getBooleanValue() {
            return true;
        }
    }

    private static class TestEnumValue {
        public static enum EnumType {
            VALUE_1, VALUE_2
        }

        @JsonValue
        public String getName() {
            return name();
        }

        public EnumType value() {
            return this;
        }
    }

    private static class CustomSerializableValue {
        @JsonSerialize
        public String value;

        public CustomSerializableValue(String value) {
            this.value = value;
        }
    }

    private static class CustomSerializer extends StdSerializer<CustomSerializableValue> {
        public CustomSerializer(SerializerProvider provider) {
            this(null);
            setProvider(provider);
        }

        public CustomSerializer(Class<?> clazz) {
            this(null, clazz);
        }

        public CustomSerializer(SerializerProvider provider, Class<?> clazz) {
            super(clazz);
            setProvider(provider);
        }

        @Override
        public void serializeWithType(CustomSerializableValue value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSerializer) throws IOException {
            typeSerializer.writeTypePrefixForScalar(value, gen);
            gen.writeString(value.value);
            typeSerializer.writeTypeSuffixForScalar(value, gen);
        }
    }
}

class SerializableJsonValue {
    private final Object value;
    private final Method getter;

    public SerializableJsonValue(Object value, Method getter) {
        this.value = value;
        this.getter = getter;
    }

    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return getter.invoke(value);
    }
}