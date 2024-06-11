package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Test class for NumberSerializers
 */
public class NumberSerializersTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testIntegerSerializer() throws JsonProcessingException, IOException {
        Integer intValue = 123;
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(intValue);

        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        JsonNode readValue = objectMapper.readTree(jsonParser);

        Assert.assertEquals(123, readValue.asInt());

        JsonSerializer<Integer> integerSerializer = objectMapper.findValueSerializer(Integer.class);
        JsonNode serializedNode = integerSerializer.serializeToNode(intValue, null);
        Assert.assertEquals(json, serializedNode.asText());
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException, IOException {
        Short shortValue = 127;
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(shortValue);

        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        JsonNode readValue = objectMapper.readTree(jsonParser);

        Assert.assertEquals(127, readValue.asInt());

        JsonSerializer<Short> shortSerializer = objectMapper.findValueSerializer(Short.class);
        JsonNode serializedNode = shortSerializer.serializeToNode(shortValue, null);
        Assert.assertEquals(json, serializedNode.asText());
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException, IOException {
        Long longValue = 9223372036854775807L;
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(longValue);

        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        JsonNode readValue = objectMapper.readTree(jsonParser);

        Assert.assertEquals(longValue, readValue.longValue());

        JsonSerializer<Long> longSerializer = objectMapper.findValueSerializer(Long.class);
        JsonNode serializedNode = longSerializer.serializeToNode(longValue, null);
        Assert.assertEquals(json, serializedNode.asText());
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException, IOException {
        Float floatValue = 3.14f;
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(floatValue);

        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        JsonNode readValue = objectMapper.readTree(jsonParser);

        Assert.assertEquals(3.14f, readValue.asFloat(), 0.01f);

        JsonSerializer<Float> floatSerializer = objectMapper.findValueSerializer(Float.class);
        JsonNode serializedNode = floatSerializer.serializeToNode(floatValue, null);
        Assert.assertEquals(json, serializedNode.asText());
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException, IOException {
        Double doubleValue = 1.732054841D;
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(doubleValue);

        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        JsonNode readValue = objectMapper.readTree(jsonParser);

        Assert.assertEquals(1.732054841, readValue.doubleValue(), 0.0001);

        JsonSerializer<Double> doubleSerializer = objectMapper.findValueSerializer(Double.class);
        JsonNode serializedNode = doubleSerializer.serializeToNode(doubleValue, null);
        Assert.assertEquals(json, serializedNode.asText());
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException, IOException {
        Byte byteValue = 127;
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(byteValue);

        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        JsonNode readValue = objectMapper.readTree(jsonParser);

        Assert.assertEquals(127, readValue.asInt());

        JsonSerializer<Byte> byteSerializer = objectMapper.findValueSerializer(Byte.class);
        JsonNode serializedNode = byteSerializer.serializeToNode(byteValue, null);
        Assert.assertEquals(json, serializedNode.asText());

        Short shortValue = 123;
        json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(shortValue);

        jsonParser = objectMapper.getFactory().createParser(json);
        readValue = objectMapper.readTree(jsonParser);

        Assert.assertEquals(123, readValue.asInt());

        JsonSerializer<Short> shortSerializer = objectMapper.findValueSerializer(Short.class);
        serializedNode = shortSerializer.serializeToNode(shortValue, null);
        Assert.assertEquals(json, serializedNode.asText());
    }

    @Test
    public void testAddAllMethod() throws JsonProcessingException, IOException {
        ObjectMapper objectMapperForTest = new ObjectMapper();
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        JsonSerializer<Integer> integerSerializer = allDeserializers.get(Integer.class.getName());
        JsonSerializer<Long> longSerializer = allDeserializers.get(Long.class.getName());

        Assert.assertNotNull(integerSerializer);
        Assert.assertNotNull(longSerializer);

        JsonNode intNode = integerSerializer.serializeToNode(123, null);
        JsonNode longNode = longSerializer.serializeToNode(9223372036854775807L, null);

        JsonNode rootNode = objectMapperForTest.createObjectNode();
        rootNode.set("integer", intNode);
        rootNode.set("long", longNode);

        String json = objectMapperForTest.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);

        JsonParser jsonParser = objectMapperForTest.getFactory().createParser(json);
        JsonNode readValue = objectMapperForTest.readTree(jsonParser);

        Assert.assertEquals(123, readValue.get("integer").asInt());
        Assert.assertEquals(9223372036854775807L, readValue.get("long").longValue());
    }

    @Test
    public void testJsonFormatAnnotation() throws JsonProcessingException, IOException {
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Integer stringIntValue = 123;

        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(stringIntValue);

        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        JsonNode readValue = objectMapper.readTree(jsonParser);

        Assert.assertEquals("123", readValue.asText());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonFormatAnnotationInvalidShape() throws JsonProcessingException, IOException {
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        Integer intValue = 123;

        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(intValue);

        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        JsonNode readValue = objectMapper.readTree(jsonParser);

        Assert.fail("Should fail with JsonMappingException");
    }
}