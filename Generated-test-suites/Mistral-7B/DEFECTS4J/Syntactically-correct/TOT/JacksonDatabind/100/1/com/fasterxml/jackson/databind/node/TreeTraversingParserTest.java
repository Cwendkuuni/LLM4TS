package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testArrayParser() throws JsonProcessingException {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonArray());
        // Test basic traversal
        Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(1, parser.getIntValue());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(2, parser.getIntValue());
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        Assert.assertEquals(JsonToken.END_OF_STREAM, parser.nextToken());

        // Test error handling
        parser.nextToken(); // consume START_ARRAY
        parser.nextToken(); // consume VALUE_NUMBER_INT
        parser.nextToken(); // consume VALUE_NUMBER_INT
        parser.nextToken(); // consume VALUE_NUMBER_INT
        parser.nextToken(); // consume END_ARRAY
        Assert.assertThrows(JsonProcessingException.class, parser::nextToken);
    }

    @Test
    public void testObjectParser() throws JsonProcessingException {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonObject());
        // Test basic traversal
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals("name1", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("value1", parser.getText());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("name2", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(3, parser.getIntValue());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.END_OF_STREAM, parser.nextToken());

        // Test error handling
        parser.nextToken(); // consume START_OBJECT
        parser.nextToken(); // consume FIELD_NAME
        parser.nextToken(); // consume VALUE_STRING
        parser.nextToken(); // consume FIELD_NAME
        parser.nextToken(); // consume VALUE_NUMBER_INT
        parser.nextToken(); // consume END_OBJECT
        Assert.assertThrows(JsonProcessingException.class, parser::nextToken);
    }

    private JsonNode createJsonArray() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.valueToTree(new int[]{1, 2});
    }

    private JsonNode createJsonObject() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.valueToTree(Map.of("name1", "value1", "name2", 3));
    }
}