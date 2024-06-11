package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeTraversingParserTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testBasicArray() throws JsonProcessingException {
        JsonNode rootNode = mapper.valueToTree("[1, 2, 3]");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        parser.close();
    }

    @Test
    public void testBasicObject() throws JsonProcessingException {
        JsonNode rootNode = mapper.valueToTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        parser.close();
    }

    @Test
    public void testEmptyArray() throws JsonProcessingException {
        JsonNode rootNode = mapper.valueToTree("[]");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testNullNode() throws JsonProcessingException {
        JsonNode rootNode = mapper.valueToTree(null);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        parser.nextToken(); // consume START_OBJECT
        parser.nextToken(); // consume null value
        parser.nextToken(); // expected END_OBJECT
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidJson() throws JsonProcessingException {
        JsonNode rootNode = mapper.valueToTree("{ABC: 1}"); // invalid JSON
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        parser.nextToken(); // consume START_OBJECT
        parser.nextToken(); // consume "ABC"
        parser.nextToken(); // expected COLON
    }

    @Test
    public void testClosedParser() throws JsonProcessingException {
        JsonNode rootNode = mapper.valueToTree("[1, 2, 3]");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        parser.nextToken(); // consume START_ARRAY
        parser.nextToken(); // consume VALUE_NUMBER_INT 1
        parser.nextToken(); // consume VALUE_NUMBER_INT 2
        parser.nextToken(); // consume VALUE_NUMBER_INT 3
        parser.nextToken(); // consume END_ARRAY
        parser.close();

        // Parser is closed, cannot read further
        assertNull(parser.nextToken());
    }
}