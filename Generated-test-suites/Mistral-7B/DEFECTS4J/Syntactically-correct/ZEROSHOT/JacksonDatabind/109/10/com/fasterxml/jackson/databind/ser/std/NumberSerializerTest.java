package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider.TypeSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapperBuilder;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test class for NumberSerializer
 */
public class NumberSerializerTest {

    private ObjectMapper objectMapper;

    public NumberSerializerTest() {
        objectMapper = new JsonMapperBuilder().build();
    }

    @Test
    public void testSerializeBigDecimal() throws JsonGenerationException, JsonMappingException {
        BigDecimal bigDecimal = new BigDecimal("123.45");
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        numberSerializer.serialize(bigDecimal, jsonGenerator, objectMapper.getSerializerProvider());
        assertEquals("123.45", jsonGenerator.getOutputContent().toString());
    }

    @Test
    public void testSerializeBigInteger() throws JsonGenerationException, JsonMappingException {
        BigInteger bigInteger = BigInteger.valueOf(12345);
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        numberSerializer.serialize(bigInteger, jsonGenerator, objectMapper.getSerializerProvider());
        assertEquals("12345", jsonGenerator.getOutputContent().toString());
    }

    @Test
    public void testSerializeLong() throws JsonGenerationException, JsonMappingException {
        Long longValue = 1234L;
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        numberSerializer.serialize(longValue, jsonGenerator, objectMapper.getSerializerProvider());
        assertEquals("1234", jsonGenerator.getOutputContent().toString());
    }

    @Test
    public void testSerializeDouble() throws JsonGenerationException, JsonMappingException {
        Double doubleValue = 123.45;
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        numberSerializer.serialize(doubleValue, jsonGenerator, objectMapper.getSerializerProvider());
        assertEquals("123.45", jsonGenerator.getOutputContent().toString());
    }

    @Test
    public void testSerializeFloat() throws JsonGenerationException, JsonMappingException {
        Float floatValue = 123.45f;
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        numberSerializer.serialize(floatValue, jsonGenerator, objectMapper.getSerializerProvider());
        assertEquals("123.45", jsonGenerator.getOutputContent().toString());
    }

    @Test
    public void testSerializeInteger() throws JsonGenerationException, JsonMappingException {
        Integer integerValue = 123;
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        numberSerializer.serialize(integerValue, jsonGenerator, objectMapper.getSerializerProvider());
        assertEquals("123", jsonGenerator.getOutputContent().toString());
    }

    @Test
    public void testSerializeByte() throws JsonGenerationException, JsonMappingException {
        Byte byteValue = 12;
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        numberSerializer.serialize(byteValue, jsonGenerator, objectMapper.getSerializerProvider());
        assertEquals("12", jsonGenerator.getOutputContent().toString());
    }

    @Test
    public void testSerializeShort() throws JsonGenerationException, JsonMappingException {
        Short shortValue = 123;
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        numberSerializer.serialize(shortValue, jsonGenerator, objectMapper.getSerializerProvider());
        assertEquals("123", jsonGenerator.getOutputContent().toString());
    }

    @Test
    public void testSerializeCustomNumberType() throws JsonGenerationException, JsonMappingException {
        Object customNumber = new Object() {
            private static final long serialVersionUID = 1L;
            private BigDecimal value;

            public BigDecimal getValue() {
                return value;
            }

            public void setValue(BigDecimal value) {
                this.value = value;
            }
        };

        customNumber.setValue(new BigDecimal("123.45"));

        ObjectMapper objectMapper = new JsonMapperBuilder()
                .enable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .build();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);

        JsonSerializer<Object> serializer = objectMapper.findValueSerializer(Object.class, jsonGenerator, null);
        serializer.serialize(customNumber, jsonGenerator, objectMapper.getSerializerProvider());

        assertEquals("{\"value\":\"123.45\"}", jsonGenerator.getOutputContent().toString());
    }

    @Test
    public void testDeserializeBigDecimal() throws JsonGenerationException, JsonMappingException {
        String jsonString = "\"123.45\"";
        ObjectMapper objectMapper = new JsonMapperBuilder().build();
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        JsonDeserializer<Number> deserializer = NumberSerializer.instance;
        Number deserializedNumber = deserializer.deserialize(jsonParser, Number.class, null);
        assertEquals(new BigDecimal("123.45"), deserializedNumber);
    }

    @Test
    public void testDeserializeBigInteger() throws JsonGenerationException, JsonMappingException {
        String jsonString = "12345";
        ObjectMapper objectMapper = new JsonMapperBuilder().build();
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        JsonDeserializer<Number> deserializer = NumberSerializer.instance;
        Number deserializedNumber = deserializer.deserialize(jsonParser, Number.class, null);
        assertEquals(BigInteger.valueOf(12345), deserializedNumber);
    }

    @Test
    public void testDeserializeLong() throws JsonGenerationException, JsonMappingException {
        String jsonString = "1234";
        ObjectMapper objectMapper = new JsonMapperBuilder().build();
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        JsonDeserializer<Number> deserializer = NumberSerializer.instance;
        Number deserializedNumber = deserializer.deserialize(jsonParser, Number.class, null);
        assertEquals(Long.valueOf(1234), deserializedNumber);
    }

    @Test
    public void testDeserializeDouble() throws JsonGenerationException, JsonMappingException {
        String jsonString = "123.45";
        ObjectMapper objectMapper = new JsonMapperBuilder().build();
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        JsonDeserializer<Number> deserializer = NumberSerializer.instance;
        Number deserializedNumber = deserializer.deserialize(jsonParser, Number.class, null);
        assertEquals(Double.valueOf(123.45), deserializedNumber);
    }

    @Test
    public void testDeserializeFloat() throws JsonGenerationException, JsonMappingException {
        String jsonString = "123.45";
        ObjectMapper objectMapper = new JsonMapperBuilder().build();
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        JsonDeserializer<Number> deserializer = NumberSerializer.instance;
        Number deserializedNumber = deserializer.deserialize(jsonParser, Number.class, null);
        assertEquals(Float.valueOf(123.45f), deserializedNumber);
    }

    @Test
    public void testDeserializeInteger() throws JsonGenerationException, JsonMappingException {
        String jsonString = "123";
        ObjectMapper objectMapper = new JsonMapperBuilder().build();
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        JsonDeserializer<Number> deserializer = NumberSerializer.instance;
        Number deserializedNumber = deserializer.deserialize(jsonParser, Number.class, null);
        assertEquals(Integer.valueOf(123), deserializedNumber);
    }

    @Test
    public void testDeserializeByte() throws JsonGenerationException, JsonMappingException {
        String jsonString = "12";
        ObjectMapper objectMapper = new JsonMapperBuilder().build();
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        JsonDeserializer<Number> deserializer = NumberSerializer.instance;
        Number deserializedNumber = deserializer.deserialize(jsonParser, Number.class, null);
        assertEquals(Byte.valueOf((byte) 12), deserializedNumber);
    }

    @Test
    public void testDeserializeShort() throws JsonGenerationException, JsonMappingException {
        String jsonString = "123";
        ObjectMapper objectMapper = new JsonMapperBuilder().build();
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        JsonDeserializer<Number> deserializer = NumberSerializer.instance;
        Number deserializedNumber = deserializer.deserialize(jsonParser, Number.class, null);
        assertEquals(Short.valueOf((short) 123), deserializedNumber);
    }

    @Test
    public void testDeserializeCustomNumberType() throws JsonGenerationException, JsonMappingException {
        String jsonString = "{\"value\":\"123.45\"}";
        ObjectMapper objectMapper = new JsonMapperBuilder().build();
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        JsonDeserializer<Object> deserializer = objectMapper.findValueDeserializer(Object.class, jsonParser, null);
        Object deserializedObject = deserializer.deserialize(jsonParser, Object.class, null);
        assertEquals(new Object() {
            private static final long serialVersionUID = 1L;
            private BigDecimal value;

            public BigDecimal getValue() {
                return value;
            }

            public void setValue(BigDecimal value) {
                this.value = value;
            }
        }.getClass(), deserializedObject.getClass());

        Object customNumber = (Object) deserializedObject;
        assertEquals(new BigDecimal("123.45"), customNumber.getValue());
    }

    @Test
    public void testDeserializeNumberWithJsonFormat() throws JsonGenerationException, JsonMappingException {
        String jsonString = "{\"number\":123.45}";
        ObjectMapper objectMapper = new JsonMapperBuilder().build();
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);

        Object customNumber = new Object() {
            private static final long serialVersionUID = 1L;
            @JsonFormat(shape = JsonFormat.Shape.STRING)
            private Number number;

            public Number getNumber() {
                return number;
            }

            public void setNumber(Number number) {
                this.number = number;
            }
        };

        JsonDeserializer<Object> deserializer = objectMapper.findValueDeserializer(Object.class, jsonParser, null);
        Object deserializedObject = deserializer.deserialize(jsonParser, Object.class, null);
        assertEquals(customNumber.getClass(), deserializedObject.getClass());
        assertEquals(Double.valueOf(123.45), deserializedObject.getClass().getDeclaredField("number").get(deserializedObject));
    }
}