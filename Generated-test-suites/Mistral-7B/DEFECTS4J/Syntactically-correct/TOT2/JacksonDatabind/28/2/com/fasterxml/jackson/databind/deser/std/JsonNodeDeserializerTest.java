package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class JsonNodeDeserializerTest {

    private JsonParser parser;
    private ObjectMapper mapper;

    @Before
    public void setup() throws IOException {
        mapper = new ObjectMapper();
        parser = mapper.getFactory().createParser("{}");
    }

    // Test the getDeserializer method
    @Test
    public void testGetDeserializer() {
        JsonDeserializer<?> objDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        JsonDeserializer<?> arrDeserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);

        Assert.assertEquals(ObjectNode.class, objDeserializer.getType(null));
        Assert.assertEquals(ArrayNode.class, arrDeserializer.getType(null));

        Assert.assertNotSame(objDeserializer, arrDeserializer);
    }

    // Test the getNullValue method
    @Test
    public void testGetNullValue() throws IOException {
        JsonNode actual = JsonNodeDeserializer.instance.getNullValue(parser, null);
        JsonNode expected = NullNode.getInstance();

        Assert.assertEquals(expected.getClass(), actual.getClass());
        Assert.assertEquals(expected, actual);
    }

    // Test the deserializeObject method
    @Test
    public void testDeserializeObject() throws IOException {
        ObjectNode expected = mapper.readValue(
                "{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}", ObjectNode.class);

        ObjectNode actual = (ObjectNode) JsonNodeDeserializer.instance.deserialize(parser, null);

        Assert.assertEquals(expected.getClass(), actual.getClass());
        Assert.assertEquals(expected, actual);

        Assert.assertEquals("John", actual.get("name").asText());
        Assert.assertEquals(30, actual.get("age").asInt());
        Assert.assertEquals("New York", actual.get("city").asText());
    }

    // Test the deserializeArray method
    @Test
    public void testDeserializeArray() throws IOException {
        ArrayNode expected = mapper.readValue(
                "[1, 2, 3, 4, 5]", ArrayNode.class);

        ArrayNode actual = (ArrayNode) JsonNodeDeserializer.instance.deserialize(parser, null);

        Assert.assertEquals(expected.getClass(), actual.getClass());
        Assert.assertEquals(expected, actual);

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    // Test the deserializeAny method
    @Test
    public void testDeserializeAny() throws IOException {
        // Test with an object
        JsonNode expectedObject = mapper.readValue(
                "{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}", JsonNode.class);

        JsonNode actualObject = JsonNodeDeserializer.instance.deserialize(parser, null);

        Assert.assertEquals(expectedObject.getClass(), actualObject.getClass());
        Assert.assertEquals(expectedObject, actualObject);

        Assert.assertEquals("John", actualObject.get("name").asText());
        Assert.assertEquals(30, actualObject.get("age").asInt());
        Assert.assertEquals("New York", actualObject.get("city").asText());

        // Test with an array
        JsonNode expectedArray = mapper.readValue(
                "[1, 2, 3, 4, 5]", JsonNode.class);

        JsonNode actualArray = JsonNodeDeserializer.instance.deserialize(parser, null);

        Assert.assertEquals(expectedArray.getClass(), actualArray.getClass());
        Assert.assertEquals(expectedArray, actualArray);

        Assert.assertArrayEquals(expectedArray.toArray(), actualArray.toArray());

        // Test with a string
        String expectedString = "Hello, World!";
        JsonNode actualString = JsonNodeDeserializer.instance.deserialize(parser, null);

        Assert.assertEquals(JsonNode.ValueType.STRING, actualString.getValueType());
        Assert.assertEquals(expectedString, actualString.asText());

        // Test with a number (int)
        int expectedInt = 42;
        JsonNode actualInt = JsonNodeDeserializer.instance.deserialize(parser, null);

        Assert.assertEquals(JsonNode.ValueType.NUMBER, actualInt.getValueType());
        Assert.assertEquals(expectedInt, actualInt.asInt());

        // Test with a number (float)
        float expectedFloat = 3.14f;
        JsonNode actualFloat = JsonNodeDeserializer.instance.deserialize(parser, null);

        Assert.assertEquals(JsonNode.ValueType.NUMBER, actualFloat.getValueType());
        Assert.assertEquals(expectedFloat, actualFloat.asFloat(), 0.001f);

        // Test with a boolean (true)
        boolean expectedBoolean = true;
        JsonNode actualBoolean = JsonNodeDeserializer.instance.deserialize(parser, null);

        Assert.assertEquals(JsonNode.ValueType.BOOLEAN, actualBoolean.getValueType());
        Assert.assertEquals(expectedBoolean, actualBoolean.asBoolean());

        // Test with a boolean (false)
        boolean expectedBoolean2 = false;
        JsonNode actualBoolean2 = JsonNodeDeserializer.instance.deserialize(parser, null);

        Assert.assertEquals(JsonNode.ValueType.BOOLEAN, actualBoolean2.getValueType());
        Assert.assertEquals(expectedBoolean2, actualBoolean2.asBoolean());

        // Test with a null value
        JsonNode actualNull = JsonNodeDeserializer.instance.deserialize(parser, null);

        Assert.assertEquals(JsonNode.ValueType.NULL, actualNull.getValueType());
    }

    // Test edge cases and exception handling
    @Test(expected = JsonMappingException.class)
    public void testDeserializeFailed() throws IOException {
        parser.nextToken(); // skip [{}]
        JsonNodeDeserializer.instance.deserialize(parser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws IOException {
        parser.setText("{name: John, age: 30, city: New York}"); // missing comma
        JsonNodeDeserializer.instance.deserialize(parser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidObjectKey() throws IOException {
        parser.setText("{name: John, age: 30, city: New York, invalid: hello}");
        JsonNodeDeserializer.instance.deserialize(parser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidArrayIndex() throws IOException {
        parser.setText("[1, 2, 3, 4, 5, invalid]");
        JsonNodeDeserializer.instance.deserialize(parser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidNullValue() throws IOException {
        parser.setText("{null}");
        JsonNodeDeserializer.instance.deserialize(parser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidBooleanValue() throws IOException {
        parser.setText("{invalid: true}");
        JsonNodeDeserializer.instance.deserialize(parser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidIntValue() throws IOException {
        parser.setText("{invalid: a}");
        JsonNodeDeserializer.instance.deserialize(parser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidFloatValue() throws IOException {
        parser.setText("{invalid: a.b}");
        JsonNodeDeserializer.instance.deserialize(parser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidStringValue() throws IOException {
        parser.setText("{invalid: 'a'}");
        JsonNodeDeserializer.instance.deserialize(parser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidArrayValue() throws IOException {
        parser.setText("[1, 2, {invalid: 'hello'}, 3]");
        JsonNodeDeserializer.instance.deserialize(parser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidArrayValue2() throws IOException {
        parser.setText("[1, 2, null, 3]");
        JsonNodeDeserializer.instance.deserialize(parser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidArrayValue3() throws IOException {
        parser.setText("[1, 2, true, 3]");
        JsonNodeDeserializer.instance.deserialize(parser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidArrayValue4() throws IOException {
        parser.setText("[1, 2, 3, invalid]");
        JsonNodeDeserializer.instance.deserialize(parser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidArrayValue5() throws IOException {
        parser.setText("[1, 2, invalid, 3]");
        JsonNodeDeserializer.instance.deserialize(parser, null);
    }
}