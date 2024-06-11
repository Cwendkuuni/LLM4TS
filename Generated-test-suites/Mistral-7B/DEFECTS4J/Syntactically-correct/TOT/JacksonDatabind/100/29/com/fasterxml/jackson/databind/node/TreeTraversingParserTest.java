package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link TreeTraversingParser} class.
 */
public class TreeTraversingParserTest {

    /**
     * Test empty JSON array parsing.
     */
    @Test
    public void testEmptyArray() throws JsonProcessingException {
        String json = "[]";
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectMapper().readTree(json));
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token);
    }

    /**
     * Test JSON array with single element.
     */
    @Test
    public void testArrayWithSingleElement() throws JsonProcessingException {
        String json = "[\"Hello\"]";
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectMapper().readTree(json));
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        Assert.assertEquals("Hello", parser.getText());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token);
    }

    /**
     * Test JSON array with multiple elements.
     */
    @Test
    public void testArrayWithMultipleElements() throws JsonProcessingException {
        String json = "[1, 2.5, \"Hello\", null, true]";
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectMapper().readTree(json));
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        Assert.assertEquals(1, parser.getIntValue());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        Assert.assertEquals(2.5, parser.getDoubleValue(), 0.0);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        Assert.assertEquals("Hello", parser.getText());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NULL, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_BOOLEAN, token);
        Assert.assertTrue(parser.getBooleanValue());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token);
    }

    /**
     * Test JSON object parsing.
     */
    @Test
    public void testEmptyObject() throws JsonProcessingException {
        String json = "{}";
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectMapper().readTree(json));
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);
    }

    /**
     * Test JSON object with single property.
     */
    @Test
    public void testObjectWithSingleProperty() throws JsonProcessingException {
        String json = "{\"name\": \"John\"}";
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectMapper().readTree(json));
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("name", parser.getText());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        Assert.assertEquals("John", parser.getText());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);
    }

    /**
     * Test JSON object with multiple properties.
     */
    @Test
    public void testObjectWithMultipleProperties() throws JsonProcessingException {
        String json = "{\"age\": 25, \"name\": \"John\", \"isStudent\": false}";
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectMapper().readTree(json));
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("age", parser.getText());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        Assert.assertEquals(25, parser.getIntValue());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("name", parser.getText());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        Assert.assertEquals("John", parser.getText());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("isStudent", parser.getText());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_BOOLEAN, token);
        Assert.assertFalse(parser.getBooleanValue());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);
    }
}