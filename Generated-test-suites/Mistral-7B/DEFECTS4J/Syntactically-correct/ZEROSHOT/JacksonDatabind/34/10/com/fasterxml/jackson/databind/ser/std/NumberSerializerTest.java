package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

/**
 * Test case for {@link NumberSerializer} class.
 */
public class NumberSerializerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSerializeBigDecimal() throws JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = this.objectMapper.getFactory().createGenerator(new StringWriter());
        SerializerProvider serializerProvider = new SerializerProvider() {};

        BigDecimal bigDecimal = new BigDecimal("123.45");
        serializer.serialize(bigDecimal, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String json = jsonGenerator.getOutputContent();
        JsonParser jsonParser = this.objectMapper.getFactory().createParser(new StringReader(json));
        JsonNode result = jsonParser.readValueAs(JsonNode.class);

        JsonNode expected = JsonNodeFactory.instance.numberNode("123.45");
        assertEquals(expected, result);
    }

    @Test
    public void testSerializeBigInteger() throws JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = this.objectMapper.getFactory().createGenerator(new StringWriter());
        SerializerProvider serializerProvider = new SerializerProvider() {};

        BigInteger bigInteger = new BigInteger("12345678901234567890");
        serializer.serialize(bigInteger, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String json = jsonGenerator.getOutputContent();
        JsonParser jsonParser = this.objectMapper.getFactory().createParser(new StringReader(json));
        JsonNode result = jsonParser.readValueAs(JsonNode.class);

        JsonNode expected = JsonNodeFactory.instance.numberNode("12345678901234567890");
        assertEquals(expected, result);
    }

    @Test
    public void testSerializeInteger() throws JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = this.objectMapper.getFactory().createGenerator(new StringWriter());
        SerializerProvider serializerProvider = new SerializerProvider() {};

        Integer integer = 123;
        serializer.serialize(integer, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String json = jsonGenerator.getOutputContent();
        JsonParser jsonParser = this.objectMapper.getFactory().createParser(new StringReader(json));
        JsonNode result = jsonParser.readValueAs(JsonNode.class);

        JsonNode expected = JsonNodeFactory.instance.numberNode("123");
        assertEquals(expected, result);
    }

    @Test
    public void testSerializeLong() throws JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = this.objectMapper.getFactory().createGenerator(new StringWriter());
        SerializerProvider serializerProvider = new SerializerProvider() {};

        Long longNumber = 1234567890123456L;
        serializer.serialize(longNumber, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String json = jsonGenerator.getOutputContent();
        JsonParser jsonParser = this.objectMapper.getFactory().createParser(new StringReader(json));
        JsonNode result = jsonParser.readValueAs(JsonNode.class);

        JsonNode expected = JsonNodeFactory.instance.numberNode("1234567890123456");
        assertEquals(expected, result);
    }

    @Test
    public void testSerializeDouble() throws JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = this.objectMapper.getFactory().createGenerator(new StringWriter());
        SerializerProvider serializerProvider = new SerializerProvider() {};

        Double doubleNumber = 123.45;
        serializer.serialize(doubleNumber, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String json = jsonGenerator.getOutputContent();
        JsonParser jsonParser = this.objectMapper.getFactory().createParser(new StringReader(json));
        JsonNode result = jsonParser.readValueAs(JsonNode.class);

        JsonNode expected = JsonNodeFactory.instance.numberNode("123.45");
        assertEquals(expected, result);
    }

    @Test
    public void testSerializeFloat() throws JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = this.objectMapper.getFactory().createGenerator(new StringWriter());
        SerializerProvider serializerProvider = new SerializerProvider() {};

        Float floatNumber = 123.45f;
        serializer.serialize(floatNumber, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String json = jsonGenerator.getOutputContent();
        JsonParser jsonParser = this.objectMapper.getFactory().createParser(new StringReader(json));
        JsonNode result = jsonParser.readValueAs(JsonNode.class);

        JsonNode expected = JsonNodeFactory.instance.numberNode("123.45");
        assertEquals(expected, result);
    }

    @Test
    public void testSerializeByte() throws JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = this.objectMapper.getFactory().createGenerator(new StringWriter());
        SerializerProvider serializerProvider = new SerializerProvider() {};

        Byte byteNumber = 127;
        serializer.serialize(byteNumber, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String json = jsonGenerator.getOutputContent();
        JsonParser jsonParser = this.objectMapper.getFactory().createParser(new StringReader(json));
        JsonNode result = jsonParser.readValueAs(JsonNode.class);

        JsonNode expected = JsonNodeFactory.instance.numberNode("127");
        assertEquals(expected, result);
    }

    @Test
    public void testSerializeShort() throws JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = this.objectMapper.getFactory().createGenerator(new StringWriter());
        SerializerProvider serializerProvider = new SerializerProvider() {};

        Short shortNumber = 123;
        serializer.serialize(shortNumber, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String json = jsonGenerator.getOutputContent();
        JsonParser jsonParser = this.objectMapper.getFactory().createParser(new StringReader(json));
        JsonNode result = jsonParser.readValueAs(JsonNode.class);

        JsonNode expected = JsonNodeFactory.instance.numberNode("123");
        assertEquals(expected, result);
    }

    @Test
    public void testSerializeUnsupportedNumberType() throws JsonMappingException, IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = this.objectMapper.getFactory().createGenerator(new StringWriter());
        SerializerProvider serializerProvider = new SerializerProvider() {};

        Object unsupportedNumber = new Object() {};
        try {
            serializer.serialize(unsupportedNumber, jsonGenerator, serializerProvider);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }

        jsonGenerator.flush();

        String json = jsonGenerator.getOutputContent();
        JsonParser jsonParser = this.objectMapper.getFactory().createParser(new StringReader(json));
        JsonNode result = jsonParser.readValueAs(JsonNode.class);

        JsonNode expected = JsonNodeFactory.instance.nullNode();
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeBigDecimal() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectReadDelegate readDelegate = objectMapper.getDeserializationConfig().getRootValueFactory().getValueInstantiator();

        String json = "\"123.45\"";
        JsonNode jsonNode = JsonNodeFactory.instance.parse(json);

        Number deserializedNumber = objectMapper.readValue(jsonNode, Number.class);
        assertEquals(deserializedNumber.getClass(), BigDecimal.class);
        assertEquals(deserializedNumber.toString(), "123.45");
    }

    @Test
    public void testDeserializeBigInteger() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectReadDelegate readDelegate = objectMapper.getDeserializationConfig().getRootValueFactory().getValueInstantiator();

        String json = "\"12345678901234567890\"";
        JsonNode jsonNode = JsonNodeFactory.instance.parse(json);

        Number deserializedNumber = objectMapper.readValue(jsonNode, Number.class);
        assertEquals(deserializedNumber.getClass(), BigInteger.class);
        assertEquals(deserializedNumber.toString(), "12345678901234567890");
    }

    @Test
    public void testDeserializeInteger() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectReadDelegate readDelegate = objectMapper.getDeserializationConfig().getRootValueFactory().getValueInstantiator();

        String json = "123";
        JsonNode jsonNode = JsonNodeFactory.instance.parse(json);

        Number deserializedNumber = objectMapper.readValue(jsonNode, Number.class);
        assertEquals(deserializedNumber.getClass(), Integer.class);
        assertEquals(deserializedNumber.intValue(), 123);
    }

    @Test
    public void testDeserializeLong() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectReadDelegate readDelegate = objectMapper.getDeserializationConfig().getRootValueFactory().getValueInstantiator();

        String json = "1234567890123456";
        JsonNode jsonNode = JsonNodeFactory.instance.parse(json);

        Number deserializedNumber = objectMapper.readValue(jsonNode, Number.class);
        assertEquals(deserializedNumber.getClass(), Long.class);
        assertEquals(deserializedNumber.longValue(), 1234567890123456);
    }

    @Test
    public void testDeserializeDouble() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectReadDelegate readDelegate = objectMapper.getDeserializationConfig().getRootValueFactory().getValueInstantiator();

        String json = "123.45";
        JsonNode jsonNode = JsonNodeFactory.instance.parse(json);

        Number deserializedNumber = objectMapper.readValue(jsonNode, Number.class);
        assertEquals(deserializedNumber.getClass(), Double.class);
        assertEquals(deserializedNumber.doubleValue(), 123.45, 0.0001);
    }

    @Test
    public void testDeserializeFloat() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectReadDelegate readDelegate = objectMapper.getDeserializationConfig().getRootValueFactory().getValueInstantiator();

        String json = "123.45";
        JsonNode jsonNode = JsonNodeFactory.instance.parse(json);

        Number deserializedNumber = objectMapper.readValue(jsonNode, Number.class);
        assertEquals(deserializedNumber.getClass(), Float.class);
        assertEquals(deserializedNumber.floatValue(), 123.45f, 0.0001f);
    }

    @Test
    public void testDeserializeByte() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectReadDelegate readDelegate = objectMapper.getDeserializationConfig().getRootValueFactory().getValueInstantiator();

        String json = "127";
        JsonNode jsonNode = JsonNodeFactory.instance.parse(json);

        Number deserializedNumber = objectMapper.readValue(jsonNode, Number.class);
        assertEquals(deserializedNumber.getClass(), Byte.class);
        assertEquals(deserializedNumber.byteValue(), 127);
    }

    @Test
    public void testDeserializeShort() throws JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectReadDelegate readDelegate = objectMapper.getDeserializationConfig().getRootValueFactory().getValueInstantiator();

        String json = "123";
        JsonNode jsonNode = JsonNodeFactory.instance.parse(json);

        Number deserializedNumber = objectMapper.readValue(jsonNode, Number.class);
        assertEquals(deserializedNumber.getClass(), Short.class);
        assertEquals(deserializedNumber.shortValue(), 123);
    }
}