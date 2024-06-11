package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class NumberSerializersTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testIntegerSerialization() throws JsonProcessingException, IOException {
        JsonSerializer<Integer> intSerializer = mapper.findValueSerializer(Integer.class);
        JsonNode node = mapper.createObjectNode();
        intSerializer.serialize(123, node, null);
        JsonNode intValue = node.get("123");
        Assert.assertEquals(JsonNodeType.NUMBER, intValue.getNodeType());
        Assert.assertEquals(123.0, intValue.doubleValueValue());
    }

    @Test
    public void testIntegerDeserialization() throws JsonProcessingException, IOException {
        String json = "{\"123\": 123}";
        Map<String, JsonSerializer<?>> deserializers = mapper.getDeserializationConfig().findValueDeserializers();
        JsonDeserializer<Integer> intDeserializer = deserializers.get(Integer.class.getName());
        Integer deserialized = mapper.readValue(json, Integer.class);
        Assert.assertEquals(123, deserialized.intValue());
    }

    @Test
    public void testLongSerialization() throws JsonProcessingException, IOException {
        JsonSerializer<Long> longSerializer = mapper.findValueSerializer(Long.class);
        JsonNode node = mapper.createObjectNode();
        longSerializer.serialize(123456789L, node, null);
        JsonNode longValue = node.get("123456789");
        Assert.assertEquals(JsonNodeType.NUMBER, longValue.getNodeType());
        Assert.assertEquals(123456789L, longValue.longValueValue());
    }

    @Test
    public void testLongDeserialization() throws JsonProcessingException, IOException {
        String json = "{\"123456789\": 123456789}";
        Map<String, JsonDeserializer<?>> deserializers = mapper.getDeserializationConfig().findValueDeserializers();
        JsonDeserializer<Long> longDeserializer = deserializers.get(Long.class.getName());
        Long deserialized = mapper.readValue(json, Long.class);
        Assert.assertEquals(123456789L, deserialized.longValue());
    }

    @Test
    public void testShortSerialization() throws JsonProcessingException, IOException {
        JsonSerializer<Short> shortSerializer = mapper.findValueSerializer(Short.class);
        JsonNode node = mapper.createObjectNode();
        shortSerializer.serialize((short) 123, node, null);
        JsonNode shortValue = node.get("123");
        Assert.assertEquals(JsonNodeType.NUMBER, shortValue.getNodeType());
        Assert.assertEquals(123.0, shortValue.doubleValueValue());
    }

    @Test
    public void testShortDeserialization() throws JsonProcessingException, IOException {
        String json = "{\"123\": 123}";
        Map<String, JsonDeserializer<?>> deserializers = mapper.getDeserializationConfig().findValueDeserializers();
        JsonDeserializer<Short> shortDeserializer = deserializers.get(Short.class.getName());
        Short deserialized = mapper.readValue(json, Short.class);
        Assert.assertEquals((short) 123, deserialized.shortValue());
    }

    @Test
    public void testFloatSerialization() throws JsonProcessingException, IOException {
        JsonSerializer<Float> floatSerializer = mapper.findValueSerializer(Float.class);
        JsonNode node = mapper.createObjectNode();
        floatSerializer.serialize(123.45f, node, null);
        JsonNode floatValue = node.get("123.45");
        Assert.assertEquals(JsonNodeType.NUMBER, floatValue.getNodeType());
        Assert.assertEquals(123.45f, floatValue.floatValueValue(), 0.001f);
    }

    @Test
    public void testFloatDeserialization() throws JsonProcessingException, IOException {
        String json = "{\"123.45\": 123.45}";
        Map<String, JsonDeserializer<?>> deserializers = mapper.getDeserializationConfig().findValueDeserializers();
        JsonDeserializer<Float> floatDeserializer = deserializers.get(Float.class.getName());
        Float deserialized = mapper.readValue(json, Float.class);
        Assert.assertEquals(123.45f, deserialized, 0.001f);
    }

    @Test
    public void testDoubleSerialization() throws JsonProcessingException, IOException {
        JsonSerializer<Double> doubleSerializer = mapper.findValueSerializer(Double.class);
        JsonNode node = mapper.createObjectNode();
        doubleSerializer.serialize(123.456789d, node, null);
        JsonNode doubleValue = node.get("123.456789");
        Assert.assertEquals(JsonNodeType.NUMBER, doubleValue.getNodeType());
        Assert.assertEquals(123.456789d, doubleValue.doubleValueValue(), 0.00001);
    }

    @Test
    public void testDoubleDeserialization() throws JsonProcessingException, IOException {
        String json = "{\"123.456789\": 123.456789}";
        Map<String, JsonDeserializer<?>> deserializers = mapper.getDeserializationConfig().findValueDeserializers();
        JsonDeserializer<Double> doubleDeserializer = deserializers.get(Double.class.getName());
        Double deserialized = mapper.readValue(json, Double.class);
        Assert.assertEquals(123.456789d, deserialized, 0.00001);
    }
}