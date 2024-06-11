package com.yourpackage.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test file for the {@link NumberSerializer} class.
 */
public class NumberSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerializeInteger() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class)
                .getSerializers().nextValueSerializer(Integer.class, null);
        assertEquals(jsonGenerator, numberSerializer);

        Integer integer = 123;
        String json = mapper.writeValueAsString(integer);
        assertEquals("123", json);
    }

    @Test
    public void testSerializeLong() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class)
                .getSerializers().nextValueSerializer(Long.class, null);
        assertEquals(jsonGenerator, numberSerializer);

        Long longNumber = 123L;
        String json = mapper.writeValueAsString(longNumber);
        assertEquals("123", json);
    }

    @Test
    public void testSerializeFloat() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class)
                .getSerializers().nextValueSerializer(Float.class, null);
        assertEquals(jsonGenerator, numberSerializer);

        Float floatNumber = 123.45f;
        String json = mapper.writeValueAsString(floatNumber);
        assertEquals("123.45", json);
    }

    @Test
    public void testSerializeDouble() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class)
                .getSerializers().nextValueSerializer(Double.class, null);
        assertEquals(jsonGenerator, numberSerializer);

        Double doubleNumber = 123.45;
        String json = mapper.writeValueAsString(doubleNumber);
        assertEquals("123.45", json);
    }

    @Test
    public void testSerializeBigInteger() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class)
                .getSerializers().nextValueSerializer(BigInteger.class, null);
        assertEquals(jsonGenerator, numberSerializer);

        BigInteger bigInteger = new BigInteger("123456789012345678901234567890");
        String json = mapper.writeValueAsString(bigInteger);
        assertEquals("123456789012345678901234567890", json);
    }

    @Test
    public void testSerializeBigDecimal() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class)
                .getSerializers().nextValueSerializer(BigDecimal.class, null);
        assertEquals(jsonGenerator, numberSerializer);

        BigDecimal bigDecimal = new BigDecimal("123.456");
        String json = mapper.writeValueAsString(bigDecimal);
        assertEquals("123.456", json);
    }

    @Test
    public void testSerializeUnknownNumberType() throws JsonMappingException, JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        Object unknownNumber = new Object() {
            @JsonFormat(shape = JsonFormat.Shape.STRING)
            public String toString() {
                return "123.45";
            }
        };

        String json = mapper.writeValueAsString(unknownNumber);
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        JsonToken currentToken = jsonParser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, currentToken);
        assertEquals("123.45", jsonParser.getText());

        Number deserializedNumber = mapper.readValue(json, Number.class);
        SerializerProvider serializerProvider = mapper.getSerializerProvider();
        JsonGenerator jsonGenerator = serializerProvider.getGenerator();
        TypeSerializer typeSerializer = serializerProvider.findValueSerializer(Number.class, null);
        TypeSerializer numberTypeSerializer = serializerProvider.findValueSerializer(Number.class, typeSerializer);

        jsonGenerator.setCurrentValue(deserializedNumber);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("numberType");
        numberTypeSerializer.serialize(null, jsonGenerator, serializerProvider);
        jsonGenerator.writeEndObject();
        jsonGenerator.flush();

        String deserializedJson = jsonGenerator.getOutputShell().toString();
        JsonParser deserializedJsonParser = mapper.getFactory().createParser(deserializedJson);
        currentToken = deserializedJsonParser.nextToken();
        assertEquals(JsonToken.START_OBJECT, currentToken);
        currentToken = deserializedJsonParser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, currentToken);
        assertEquals("numberType", deserializedJsonParser.getCurrentName());
        currentToken = deserializedJsonParser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, currentToken);
        assertEquals("number", deserializedJsonParser.getText());
        currentToken = deserializedJsonParser.nextToken();
        assertEquals(JsonToken.END_OBJECT, currentToken);
    }
}