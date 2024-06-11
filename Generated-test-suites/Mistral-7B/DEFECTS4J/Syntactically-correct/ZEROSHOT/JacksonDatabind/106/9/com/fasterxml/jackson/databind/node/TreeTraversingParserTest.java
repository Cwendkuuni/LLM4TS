package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyJson() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode("{}"));
        JsonParser jsonParser = parser;

        assertEquals(JsonToken.START_OBJECT, jsonParser.nextToken());
        assertEquals(JsonToken.END_OBJECT, jsonParser.nextToken());
        assertTrue(jsonParser.isClosed());
    }

    @Test
    public void testJsonWithString() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode("{\"name\": \"John\"}"));
        JsonParser jsonParser = parser;

        assertEquals(JsonToken.START_OBJECT, jsonParser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, jsonParser.nextToken());
        assertEquals("name", jsonParser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, jsonParser.nextToken());
        assertEquals("John", jsonParser.getText());
        assertEquals(JsonToken.END_OBJECT, jsonParser.nextToken());
        assertTrue(jsonParser.isClosed());
    }

    @Test
    public void testJsonWithNumber() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode("{\"age\": 25}"));
        JsonParser jsonParser = parser;

        assertEquals(JsonToken.START_OBJECT, jsonParser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, jsonParser.nextToken());
        assertEquals("age", jsonParser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, jsonParser.nextToken());
        assertEquals(25, jsonParser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, jsonParser.nextToken());
        assertTrue(jsonParser.isClosed());
    }

    @Test
    public void testJsonWithArray() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode("{\"colors\": [\"red\", \"green\", \"blue\"]}"));
        JsonParser jsonParser = parser;

        assertEquals(JsonToken.START_OBJECT, jsonParser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, jsonParser.nextToken());
        assertEquals("colors", jsonParser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, jsonParser.nextToken());

        assertEquals(JsonToken.VALUE_STRING, jsonParser.nextToken());
        assertEquals("red", jsonParser.getText());

        assertEquals(JsonToken.VALUE_STRING, jsonParser.nextToken());
        assertEquals("green", jsonParser.getText());

        assertEquals(JsonToken.VALUE_STRING, jsonParser.nextToken());
        assertEquals("blue", jsonParser.getText());

        assertEquals(JsonToken.END_ARRAY, jsonParser.nextToken());
        assertEquals(JsonToken.END_OBJECT, jsonParser.nextToken());
        assertTrue(jsonParser.isClosed());
    }

    @Test
    public void testJsonWithNestedObjects() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode("{\"person\": {\"name\": \"John\", \"age\": 25}}"));
        JsonParser jsonParser = parser;

        assertEquals(JsonToken.START_OBJECT, jsonParser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, jsonParser.nextToken());
        assertEquals("person", jsonParser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, jsonParser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, jsonParser.nextToken());
        assertEquals("name", jsonParser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, jsonParser.nextToken());
        assertEquals("John", jsonParser.getText());
        assertEquals(JsonToken.FIELD_NAME, jsonParser.nextToken());
        assertEquals("age", jsonParser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, jsonParser.nextToken());
        assertEquals(25, jsonParser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, jsonParser.nextToken());
        assertEquals(JsonToken.END_OBJECT, jsonParser.nextToken());
        assertTrue(jsonParser.isClosed());
    }

    @Test(expected = JsonParseException.class)
    public void testJsonWithInvalidValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode("{\"invalid\": null}"));
        JsonParser jsonParser = parser;

        assertEquals(JsonToken.START_OBJECT, jsonParser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, jsonParser.nextToken());
        assertEquals("invalid", jsonParser.getCurrentName());
        jsonParser.nextToken(); // should be null, but not supported
        fail();
    }

    @Test
    public void testJsonWithBinaryData() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode("{\"data\": \"VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgew0K\"}"));
        JsonParser jsonParser = parser;

        assertEquals(JsonToken.START_OBJECT, jsonParser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, jsonParser.nextToken());
        assertEquals("data", jsonParser.getCurrentName());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, jsonParser.nextToken());
        byte[] binaryData = jsonParser.getBinaryValue(Base64Variants.STANDARD);
        assertTrue(binaryData.length > 0);
        assertEquals("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgew0K", new String(binaryData, "UTF-8"));
        assertEquals(JsonToken.END_OBJECT, jsonParser.nextToken());
        assertTrue(jsonParser.isClosed());
    }

    private JsonNode createJsonNode(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(jsonString);
    }
}