package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testTreeTraversingParserEmpty() throws IOException {
        JsonNode jsonNode = null;
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        JsonToken token = parser.nextToken();
        assertNull(token);
        assertTrue(parser.isClosed());
    }

    @Test
    public void testTreeTraversingParserString() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\": \"John\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("name", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("John", parser.getText());

        parser.close();
    }

    @Test
    public void testTreeTraversingParserNumber() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("age", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(30, parser.getIntValue());

        parser.close();
    }

    @Test
    public void testTreeTraversingParserObject() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\": {\"first\": \"John\", \"last\": \"Doe\"}}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("name", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("first", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("John", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("last", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("Doe", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        parser.close();
    }

    @Test
    public void testTreeTraversingParserArray() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("[1, 2, 3]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(1, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(2, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(3, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        parser.close();
    }

    @Test(expected = IllegalStateException.class)
    public void testTreeTraversingParserInvalidObject() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{invalid: \"Hello\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken(); // should throw an exception
    }

    @Test(expected = IllegalStateException.class)
    public void testTreeTraversingParserInvalidArray() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("[invalid]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken(); // should throw an exception
    }
}