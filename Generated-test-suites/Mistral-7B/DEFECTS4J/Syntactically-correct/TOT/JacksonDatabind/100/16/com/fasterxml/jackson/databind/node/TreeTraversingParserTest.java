package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import org.junit.Assert;
import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testParsingJsonArray() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3, 4]";
        TreeTraversingParser parser = mapper.treeAsTokens(json);

        Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        for (int i = 0; i < 4; i++) {
            Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
            Assert.assertEquals(i + 1, parser.getIntValue());
        }
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
    }

    @Test
    public void testParsingJsonObject() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}";
        TreeTraversingParser parser = mapper.treeAsTokens(json);

        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals("name", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("John", parser.getText());

        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("age", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(30, parser.getIntValue());

        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("city", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("New York", parser.getText());

        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testParsingComplexJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"person\": {\"name\":\"John\", \"age\":30,\"address\":{\"street\":\"Main St\", \"city\":\"New York\", \"country\":\"USA\"}} }";
        TreeTraversingParser parser = mapper.treeAsTokens(json);

        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals("person", parser.getCurrentName());
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals("name", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("John", parser.getText());

        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("age", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(30, parser.getIntValue());

        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("address", parser.getCurrentName());
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals("street", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("Main St", parser.getText());

        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("city", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("New York", parser.getText());

        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("country", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("USA", parser.getText());

        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test(expected = JsonParseException.class)
    public void testParsingInvalidJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{name:\"John\"";
        TreeTraversingParser parser = mapper.treeAsTokens(json);
        parser.nextToken(); // consumes "{"

        // The test should fail with JsonParseException as the JSON is not valid
        parser.nextToken();
    }

    @Test
    public void testClosingParser() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        TreeTraversingParser parser = mapper.treeAsTokens(json);

        Assert.assertFalse(parser.isClosed());
        parser.close();
        Assert.assertTrue(parser.isClosed());
    }
}