package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializerTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerializeBigDecimal() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(JsonGenerator.Type.STREAM, null);
        BigDecimal bigDecimal = new BigDecimal("123.45");
        numberSerializer.serialize(bigDecimal, generator, null);
        assertEquals("123.45", generator.getOutputStandaloneString());
    }

    @Test
    public void testSerializeBigInteger() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(JsonGenerator.Type.STREAM, null);
        BigInteger bigInteger = new BigInteger("12345678901234567890");
        numberSerializer.serialize(bigInteger, generator, null);
        assertEquals("12345678901234567890", generator.getOutputStandaloneString());
    }

    @Test
    public void testSerializeLong() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(JsonGenerator.Type.STREAM, null);
        long longValue = 1234567890L;
        numberSerializer.serialize(longValue, generator, null);
        assertEquals("1234567890", generator.getOutputStandaloneString());
    }

    @Test
    public void testSerializeDouble() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(JsonGenerator.Type.STREAM, null);
        double doubleValue = 123.45;
        numberSerializer.serialize(doubleValue, generator, null);
        assertEquals("123.45", generator.getOutputStandaloneString());
    }

    @Test
    public void testSerializeFloat() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(JsonGenerator.Type.STREAM, null);
        float floatValue = 123.45f;
        numberSerializer.serialize(floatValue, generator, null);
        assertEquals("123.45", generator.getOutputStandaloneString());
    }

    @Test
    public void testSerializeInteger() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(JsonGenerator.Type.STREAM, null);
        int intValue = 123;
        numberSerializer.serialize(intValue, generator, null);
        assertEquals("123", generator.getOutputStandaloneString());
    }

    @Test
    public void testSerializeByte() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(JsonGenerator.Type.STREAM, null);
        byte byteValue = 123;
        numberSerializer.serialize(byteValue, generator, null);
        assertEquals("123", generator.getOutputStandaloneString());
    }

    @Test
    public void testSerializeShort() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(JsonGenerator.Type.STREAM, null);
        short shortValue = 123;
        numberSerializer.serialize(shortValue, generator, null);
        assertEquals("123", generator.getOutputStandaloneString());
    }

    @Test
    public void testSerializeNaN() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(JsonGenerator.Type.STREAM, null);
        double nanValue = Double.NaN;
        numberSerializer.serialize(nanValue, generator, null);
        assertNull(generator.getOutputStandaloneString());
    }

    @Test
    public void testSerializeInfinity() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator generator = mapper.getPolymorphicSerializer(Number.class).getSerializerProvider().getGeneratorFactory().createGenerator(JsonGenerator.Type.STREAM, null);
        double infinityValue = Double.POSITIVE_INFINITY;
        numberSerializer.serialize(infinityValue, generator, null);
        assertEquals("Infinity", generator.getOutputStandaloneString());
    }

    @Test
    public void testDeserializeBigDecimal() throws JsonProcessingException {
        JsonParser jsonParser = mapper.getFactory().createParser("123.45");
        JsonDeserializer<Number> numberDeserializer = mapper.getDeserializationConfig().findValueDeserializer(JsonParser.NumberType.BIG_DECIMAL, Number.class);
        Number deserializedValue = numberDeserializer.deserialize(jsonParser, Number.class, null);
        assertEquals(new BigDecimal("123.45"), deserializedValue);
    }

    @Test
    public void testDeserializeBigInteger() throws JsonProcessingException {
        JsonParser jsonParser = mapper.getFactory().createParser("12345678901234567890");
        JsonDeserializer<Number> numberDeserializer = mapper.getDeserializationConfig().findValueDeserializer(JsonParser.NumberType.BIG_INTEGER, Number.class);
        Number deserializedValue = numberDeserializer.deserialize(jsonParser, Number.class, null);
        assertEquals(new BigInteger("12345678901234567890"), deserializedValue);
    }

    @Test
    public void testDeserializeLong() throws JsonProcessingException {
        JsonParser jsonParser = mapper.getFactory().createParser("1234567890");
        JsonDeserializer<Number> numberDeserializer = mapper.getDeserializationConfig().findValueDeserializer(JsonParser.NumberType.LONG, Number.class);
        Number deserializedValue = numberDeserializer.deserialize(jsonParser, Number.class, null);
        assertEquals(1234567890L, deserializedValue.longValue());
    }

    @Test
    public void testDeserializeDouble() throws JsonProcessingException {
        JsonParser jsonParser = mapper.getFactory().createParser("123.45");
        JsonDeserializer<Number> numberDeserializer = mapper.getDeserializationConfig().findValueDeserializer(JsonParser.NumberType.DOUBLE, Number.class);
        Number deserializedValue = numberDeserializer.deserialize(jsonParser, Number.class, null);
        assertEquals(123.45, deserializedValue.doubleValue(), 0.001);
    }

    @Test
    public void testDeserializeFloat() throws JsonProcessingException {
        JsonParser jsonParser = mapper.getFactory().createParser("123.45");
        mapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JsonDeserializer<Number> numberDeserializer = mapper.getDeserializationConfig().findValueDeserializer(JsonParser.NumberType.FLOAT, Number.class);
        Number deserializedValue = numberDeserializer.deserialize(jsonParser, Number.class, null);
        assertEquals(123.45f, deserializedValue.floatValue(), 0.001);
    }

    @Test
    public void testDeserializeInteger() throws JsonProcessingException {
        JsonParser jsonParser = mapper.getFactory().createParser("123");
        JsonDeserializer<Number> numberDeserializer = mapper.getDeserializationConfig().findValueDeserializer(JsonParser.NumberType.INT, Number.class);
        Number deserializedValue = numberDeserializer.deserialize(jsonParser, Number.class, null);
        assertEquals(123, deserializedValue.intValue());
    }

    @Test
    public void testDeserializeByte() throws JsonProcessingException {
        JsonParser jsonParser = mapper.getFactory().createParser("123");
        JsonDeserializer<Number> numberDeserializer = mapper.getDeserializationConfig().findValueDeserializer(JsonParser.NumberType.BYTE, Number.class);
        Number deserializedValue = numberDeserializer.deserialize(jsonParser, Number.class, null);
        assertEquals(123, deserializedValue.byteValue());
    }

    @Test
    public void testDeserializeShort() throws JsonProcessingException {
        JsonParser jsonParser = mapper.getFactory().createParser("123");
        JsonDeserializer<Number> numberDeserializer = mapper.getDeserializationConfig().findValueDeserializer(JsonParser.NumberType.SHORT, Number.class);
        Number deserializedValue = numberDeserializer.deserialize(jsonParser, Number.class, null);
        assertEquals(123, deserializedValue.shortValue());
    }

    @Test
    public void testDeserializeNaN() throws JsonProcessingException {
        JsonParser jsonParser = mapper.getFactory().createParser("NaN");
        JsonDeserializer<Number> numberDeserializer = mapper.getDeserializationConfig().findValueDeserializer(JsonParser.NumberType.NUMBER, Number.class);
        Number deserializedValue = numberDeserializer.deserialize(jsonParser, Number.class, null);
        assertNotNull(deserializedValue);
        assertEquals(Double.NaN, deserializedValue.doubleValue());
    }

    @Test
    public void testDeserializeInfinity() throws JsonProcessingException {
        JsonParser jsonParser = mapper.getFactory().createParser("Infinity");
        JsonDeserializer<Number> numberDeserializer = mapper.getDeserializationConfig().findValueDeserializer(JsonParser.NumberType.NUMBER, Number.class);
        Number deserializedValue = numberDeserializer.deserialize(jsonParser, Number.class, null);
        assertEquals(Double.POSITIVE_INFINITY, deserializedValue.doubleValue());
    }
}