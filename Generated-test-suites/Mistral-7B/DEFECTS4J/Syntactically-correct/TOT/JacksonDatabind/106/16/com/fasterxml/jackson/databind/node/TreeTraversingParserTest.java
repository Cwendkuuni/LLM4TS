package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testBasicArrayParsing() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("[1, 2, 3, 4]");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        Assert.assertEquals(1, parser.getIntValue());
        Assert.assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        Assert.assertEquals(2, parser.getIntValue());
        Assert.assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        Assert.assertEquals(3, parser.getIntValue());
        Assert.assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        Assert.assertEquals(4, parser.getIntValue());
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testBasicObjectParsing() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("{\"name\": \"John\", \"age\": 30, \"children\": [1, 2, 3]}");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("name", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("John", parser.getText());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("age", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(30, parser.getIntValue());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("children", parser.getCurrentName());
        Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        Assert.assertEquals(1, parser.getIntValue());
        Assert.assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        Assert.assertEquals(2, parser.getIntValue());
        Assert.assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        Assert.assertEquals(3, parser.getIntValue());
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testEmptyObjectParsing() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("{}");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testEmptyArrayParsing() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("[]");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testMalformedJsonParsing() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("{[1, 2]");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        parser.nextToken();
    }

    @Test
    public void testTraverseClosedParser() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("[1, 2, 3]");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        parser.close();
        try {
            parser.nextToken();
        } catch (JsonParseException e) {
            Assert.assertEquals("Parser is closed", e.getMessage());
        }
    }
}