package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.NodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class NumberSerializerTest {

    @Test
    public void testNumberSerializer_Integer() throws JsonProcessingException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer numberSerializer = NumberSerializer.instance;
        Random random = new Random();
        int testValue = random.nextInt(100);

        JsonNode actual = mapper.writerWithRootValue(numberSerializer).writeValueAsTree(testValue);
        JsonNode expected = mapper.createObjectNode();
        expected.put("@type", "number");
        expected.put("numberValue", testValue);

        assertEquals(expected, actual);
    }

    @Test
    public void testNumberSerializer_BigInteger() throws JsonProcessingException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode testData = mapper.createObjectNode();
        testData.put("bigInteger", new BigInteger("123456789012345678901234567890"));
        JsonNode actual = mapper.writerWithRootValue(new NumberSerializer(BigInteger.class))
                .writeValueAsTree(testData.get("bigInteger"));
        JsonNode expected = mapper.createObjectNode();
        expected.put("@type", "number");
        expected.put("numberValue", testData.get("bigInteger").asText());

        assertEquals(expected, actual);
    }

    @Test
    public void testNumberSerializer_BigDecimal() throws JsonProcessingException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode testData = mapper.createObjectNode();
        testData.put("bigDecimal", new BigDecimal("1234567890.123456789012345678901234567890"));
        JsonSerializer<Number> numberSerializer = new NumberSerializer(BigDecimal.class);
        JsonNode actual = mapper.writerWithRootValue(numberSerializer)
                .writeValueAsTree(testData.get("bigDecimal"));
        JsonNode expected = mapper.createObjectNode();
        expected.put("@type", "number");
        expected.put("numberValue", testData.get("bigDecimal").asText());
        expected.put("scale", 10);

        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testNumberSerializer_NonNumber() throws JsonProcessingException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode testData = mapper.createObjectNode();
        String nonNumber = "Test String";
        testData.put("nonNumber", nonNumber);
        JsonSerializer<Number> numberSerializer = NumberSerializer.instance;

        mapper.writerWithRootValue(numberSerializer).writeValueAsString(testData);
    }

    @Test(expected = JsonGenerationException.class)
    public void testNumberSerializer_IOException() throws JsonProcessingException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectSerializerOut testSerializerOut = new ObjectSerializerOut();
        ObjectNode testData = mapper.createObjectNode();
        testData.put("test", testSerializerOut);
        JsonSerializer<Number> numberSerializer = NumberSerializer.instance;

        mapper.writerWithRootValue(numberSerializer).writeValueAsString(testData);
    }
}

class ObjectSerializerOut extends JsonSerializer<Object> {

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        throw new IOException();
    }
}