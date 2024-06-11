package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class TreeTraversingParserTest {

    @Test
    public void testTreeTraversingParser_EmptyArray() throws Exception {
        JsonNode jsonNode = new TextNode("");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertTrue(parser.isClosed());
        assertNull(parser.nextToken());
    }

    @Test
    public void testTreeTraversingParser_EmptyObject() throws Exception {
        JsonNode jsonNode = ObjectNode.emptyObject();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertTrue(parser.isClosed());
        assertNull(parser.nextToken());
    }

    @Test
    public void testTreeTraversingParser_ObjectWithProperty() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{'name': 'John Doe'}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John Doe", parser.getText());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertTrue(parser.isClosed());
        assertNull(parser.nextToken());
    }

    @Test
    public void testTreeTraversingParser_ArrayWithValues() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("[1, 2, 3.5]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(3.5, parser.getDoubleValue(), 0.0);
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertTrue(parser.isClosed());
        assertNull(parser.nextToken());
    }

    @Test
    public void testTreeTraversingParser_ObjectWithNullValue() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{'name': null}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
        assertEquals(null, parser.getEmbeddedObject());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertTrue(parser.isClosed());
        assertNull(parser.nextToken());
    }

    @Test(expected = JsonParseException.class)
    public void testTreeTraversingParser_InvalidJson() throws Exception {
        JsonNode jsonNode = new TextNode("{abcd}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testTreeTraversingParser_InvalidJson_UnexpectedToken() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("[1, 2, {3}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testTreeTraversingParser_InvalidJson_UnexpectedValueType() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{'name': 1}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
    }

}