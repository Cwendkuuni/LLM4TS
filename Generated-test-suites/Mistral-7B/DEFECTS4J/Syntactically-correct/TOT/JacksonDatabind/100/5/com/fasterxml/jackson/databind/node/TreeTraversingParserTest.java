package com.fasterxml.jackson.databind.node; // import statements go here

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

    private JsonFactory jsonFactory;
    private ObjectMapper objectMapper;
    private JsonParser treeParaser;

    @Before
    public void setup() {
        jsonFactory = new JsonFactory();
        objectMapper = new ObjectMapper(jsonFactory);
        JsonNode rootNode = JsonNodeFactory.instance.objectNode();
        treeParaser = new TreeTraversingParser(rootNode);
    }

    @Test
    public void testTraversingParser_EmptyObject() throws IOException {
        JsonNode emptyObjectNode = JsonNodeFactory.instance.objectNode();
        treeParaser = new TreeTraversingParser(emptyObjectNode);

        assertEquals(JsonToken.START_OBJECT, treeParaser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, treeParaser.nextToken());
        assertEquals("", treeParaser.getCurrentName());
        assertEquals(JsonToken.END_OBJECT, treeParser.nextToken());
        assertNull(treeParaser.getText());
        assertTrue(treeParaser.isClosed());
    }

    @Test
    public void testTraversingParser_EmptyArray() throws IOException {
        JsonNode emptyArrayNode = JsonNodeFactory.instance.arrayNode();
        treeParaser = new TreeTraversingParser(emptyArrayNode);

        assertEquals(JsonToken.START_ARRAY, treeParaser.nextToken());
        assertEquals(JsonToken.END_ARRAY, treeParaser.nextToken());
        assertNull(treeParaser.getText());
        assertTrue(treeParaser.isClosed());
    }

    @Test
    public void testTraversingParser_ComplexObject() throws IOException {
        JsonNode complexObject = objectMapper.readTree("{\"name\": \"John\", \"age\": 30, \"hobbies\": [\"reading\", \"swimming\"]}");
        treeParaser = new TreeTraversingParser(complexObject);

        assertEquals(JsonToken.START_OBJECT, treeParser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, treeParser.nextToken());
        assertEquals("name", treeParser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, treeParser.nextToken());
        assertEquals("John", treeParser.getText());
        assertEquals(JsonToken.FIELD_NAME, treeParser.nextToken());
        assertEquals("age", treeParser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, treeParser.nextToken());
        assertEquals(30, treeParser.getIntValue());
        assertEquals(JsonToken.FIELD_NAME, treeParser.nextToken());
        assertEquals("hobbies", treeParser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, treeParser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, treeParser.nextToken());
        assertEquals("reading", treeParser.getText());
        assertEquals(JsonToken.VALUE_STRING, treeParser.nextToken());
        assertEquals("swimming", treeParser.getText());
        assertEquals(JsonToken.END_ARRAY, treeParser.nextToken());
        assertEquals(JsonToken.END_OBJECT, treeParser.nextToken());
        assertTrue(treeParser.isClosed());
    }

    @Test
    public void testTraversingParser_ComplexArray() throws IOException {
        JsonNode complexArray = objectMapper.readTree("[1, 2.5, 3, \"four\", {\"name\": \"John\", \"age\": 30}]");
        treeParaser = new TreeTraversingParser(complexArray);

        assertEquals(JsonToken.START_ARRAY, treeParser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, treeParser.nextToken());
        assertEquals(1, treeParser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, treeParser.nextToken());
        assertEquals(2.5, treeParser.getDoubleValue(), 0.01);
        assertEquals(JsonToken.VALUE_NUMBER_INT, treeParser.nextToken());
        assertEquals(3, treeParser.getIntValue());
        assertEquals(JsonToken.VALUE_STRING, treeParser.nextToken());
        assertEquals("four", treeParser.getText());
        assertEquals(JsonToken.START_OBJECT, treeParser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, treeParser.nextToken());
        assertEquals("name", treeParser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, treeParser.nextToken());
        assertEquals("John", treeParser.getText());
        assertEquals(JsonToken.FIELD_NAME, treeParser.nextToken());
        assertEquals("age", treeParser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, treeParser.nextToken());
        assertEquals(30, treeParser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, treeParser.nextToken());
        assertEquals(JsonToken.END_ARRAY, treeParser.nextToken());
        assertTrue(treeParser.isClosed());
    }

    @Test
    public void testTraversingParser_ClosedParser() throws IOException {
        treeParaser.close();
        assertEquals(JsonToken.CLOSED, treeParaser.nextToken());
        assertTrue(treeParser.isClosed());
    }

    @Test(expected = JsonParseException.class)
    public void testTraversingParser_InvalidJson() throws IOException {
        treeParaser = new TreeTraversingParser(JsonNodeFactory.instance.textNode("{"));
        treeParser.nextToken();
    }
}