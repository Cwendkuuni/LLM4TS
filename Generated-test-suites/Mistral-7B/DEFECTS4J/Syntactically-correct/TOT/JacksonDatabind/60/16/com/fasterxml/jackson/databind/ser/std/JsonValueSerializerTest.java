package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;

public class JsonValueSerializerTest {

    @Test
    public void testJsonValueSerializer_StringValueMethod() throws Exception {
        StringEnum stringEnum = new StringEnum("stringValue");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonValueSerializer<StringEnum> serializer = mapper.findValueSerializer(StringEnum.class, null);

        String json = serializer.serializeWithType(stringEnum, null, null, null);
        Assert.assertEquals("\"stringValue\"", json);

        Assert.assertEquals("stringValue", stringEnum.getValue());
        Assert.assertEquals("\"stringValue\"", serializer.getValueSerializer().serializeValue(stringEnum.getValue(), null, null));

        Object deserialized = mapper.readValue(json, StringEnum.class);
        Assert.assertEquals(stringEnum.getValue(), deserialized.toString());
    }

    @Test
    public void testJsonValueSerializer_IntegerValueMethod() throws Exception {
        IntEnum intEnum = new IntEnum(42);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonValueSerializer<IntEnum> serializer = mapper.findValueSerializer(IntEnum.class, null);

        String json = serializer.serializeWithType(intEnum, null, null, null);
        Assert.assertEquals("42", json);

        Assert.assertEquals(42, intEnum.getValue());
        Assert.assertEquals(42, serializer.getValueSerializer().serializeValue(intEnum.getValue(), null, null).intValue());

        Object deserialized = mapper.readValue(json, IntEnum.class);
        Assert.assertEquals(intEnum.getValue(), deserialized.toString());
    }

    @Test
    public void testJsonValueSerializer_MethodWithException() throws Exception {
        ExceptionEnum exceptionEnum = new ExceptionEnum();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonValueSerializer<ExceptionEnum> serializer = mapper.findValueSerializer(ExceptionEnum.class, null);

        try {
            serializer.serializeWithType(exceptionEnum, null, null, null);
            Assert.fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }

        Object deserialized = mapper.readValue("{}", ExceptionEnum.class);
        Assert.assertNull(deserialized);
    }

    @Test
    public void testJsonValueSerializer_MethodWithNullValue() throws Exception {
        NullEnum nullEnum = new NullEnum();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonValueSerializer<NullEnum> serializer = mapper.findValueSerializer(NullEnum.class, null);

        String json = serializer.serializeWithType(nullEnum, null, null, null);
        Assert.assertEquals("null", json);

        Object deserialized = mapper.readValue(json, NullEnum.class);
        Assert.assertNull(deserialized);
    }

    @Test
    public void testJsonValueSerializer_MethodWithBooleanValue() throws Exception {
        BooleanEnum booleanEnum = new BooleanEnum(true);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonValueSerializer<BooleanEnum> serializer = mapper.findValueSerializer(BooleanEnum.class, null);

        String json = serializer.serializeWithType(booleanEnum, null, null, null);
        Assert.assertEquals("true", json);

        Assert.assertEquals(booleanEnum.getValue(), json.equals("true"));
        Assert.assertEquals(booleanEnum.getValue(), serializer.getValueSerializer().serializeValue(booleanEnum.getValue(), null, null).booleanValue());

        Object deserialized = mapper.readValue(json, BooleanEnum.class);
        Assert.assertEquals(booleanEnum.getValue(), deserialized.toString().equals("true"));
    }

    public static class StringEnum {
        private final String value;

        public StringEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static class IntEnum {
        private final int value;

        public IntEnum(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static class ExceptionEnum {
        public ExceptionEnum() {
            // Empty constructor to trigger error
        }
    }

    public static class NullEnum {
        public NullEnum() {
        }
    }

    public static class BooleanEnum {
        private final boolean value;

        public BooleanEnum(boolean value) {
            this.value = value;
        }

        public boolean getValue() {
            return value;
        }
    }
}