package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TreeTraversingParserTest {

    @Test
    public void testParseSimpleObject() throws JsonProcessingException {
        String json = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("city", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("New York", parser.getText());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testParseSimpleArray() throws JsonProcessingException {
        String json = "[1, 2, 3, 4]";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(4, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
    }

    @Test
    public void testParseEmptyObject() throws JsonProcessingException {
        String json = "{}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testParseEmptyArray() throws JsonProcessingException {
        String json = "[]";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
    }

    @Test
    public void testParseNullValue() throws JsonProcessingException {
        String json = "{\"name\": null}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertNull(parser.getText());
        assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test(expected = JsonParseException.class)
    public void testParseInvalidJson() throws JsonProcessingException {
        String json = "{name:John,age:30,city:New York}"; // missing quotes
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        parser.nextToken(); // skipped {
        parser.nextToken(); // skipped name:
        assertEquals("John", parser.getText());
        assertEquals("John", parser.getText()); // should not be here
        //... this test should fail with JsonParseException
    }
}