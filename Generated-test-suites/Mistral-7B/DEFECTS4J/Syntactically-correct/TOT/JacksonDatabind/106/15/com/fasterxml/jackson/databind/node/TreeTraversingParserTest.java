package com.fasterxml.jackson.databind.node; // import statements go here

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

/**
 * Test cases for {@link TreeTraversingParser}.
 */
public class TreeTraversingParserTest {

    /**
     * Test parsing basic JSON object using {@link TreeTraversingParser}.
     */
    @Test
    public void testParseJsonObject() throws JsonProcessingException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Test getting token types
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
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

        // Test getting textual content
        assertEquals("John", parser.getText());
        assertEquals("New York", parser.getText());

        // Test getting numeric values
        assertEquals(30, parser.getIntValue());

        // Test skipping children
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    /**
     * Test parsing empty JSON object using {@link TreeTraversingParser}.
     */
    @Test
    public void testParseEmptyJsonObject() throws JsonProcessingException {
        JsonNode jsonNode = new ObjectMapper().readTree("{}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    /**
     * Test parsing JSON array using {@link TreeTraversingParser}.
     */
    @Test
    public void testParseJsonArray() throws JsonProcessingException {
        JsonNode jsonNode = new ObjectMapper().readTree("[1, 2, 3, 4]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(4, parser.getIntValue());

        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
    }

    /**
     * Test parsing JSON fragment using {@link TreeTraversingParser}.
     */
    @Test
    public void testParseJsonFragment() throws JsonProcessingException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\":\"John\",\"city\":\"New York\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Test parsing until reaching the end of the fragment
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("city", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("New York", parser.getText());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        // Test parsing beyond the end of the fragment
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken(); // should throw an exception
        parser.close();
    }

    /**
     * Test handling null JSON node using {@link TreeTraversingParser}.
     */
    @Test(expected = JsonProcessingException.class)
    public void testParseNullJsonNode() throws JsonProcessingException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.nextToken();
    }

    /**
     * Test closing parser.
     */
    @Test
    public void testCloseParser() throws JsonProcessingException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.close();
        parser.nextToken(); // should throw an exception
    }

    /**
     * Test handling unexpected token.
     */
    @Test(expected = JsonParseException.class)
    public void testHandleUnexpectedToken() throws JsonProcessingException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken(); // should throw an exception
    }
}