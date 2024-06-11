package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Assert;
import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyArray() throws JsonParseException {
        JsonNode jsonNode = new ObjectMapper().readTree("[]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        Assert.assertFalse(parser.isClosed());
        Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
        Assert.assertTrue(parser.isClosed());
    }

    @Test
    public void testSimpleObject() throws JsonParseException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        Assert.assertFalse(parser.isClosed());
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals("name", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("John", parser.getText());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("age", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(30, parser.getIntValue());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
        Assert.assertTrue(parser.isClosed());
    }

    @Test
    public void testArrayWithObjectsAndSimpleValues() throws JsonParseException {
        JsonNode jsonNode = new ObjectMapper().readTree("[1, \"two\", null, 3.5, {\"name\": \"John\"}]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        Assert.assertFalse(parser.isClosed());
        Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(1, parser.getIntValue());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("two", parser.getText());
        Assert.assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        Assert.assertEquals(3.5, parser.getDoubleValue(), 0.001);
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals("name", parser.getCurrentName());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("name", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("John", parser.getText());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
        Assert.assertTrue(parser.isClosed());
    }

    @Test
    public void testEmptyObject() throws JsonParseException {
        JsonNode jsonNode = new ObjectMapper().readTree("{}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        Assert.assertFalse(parser.isClosed());
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
        Assert.assertTrue(parser.isClosed());
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidJson() throws JsonParseException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name}: 30"); // missing ':' in value
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        // Test should throw JsonParseException here
    }
}