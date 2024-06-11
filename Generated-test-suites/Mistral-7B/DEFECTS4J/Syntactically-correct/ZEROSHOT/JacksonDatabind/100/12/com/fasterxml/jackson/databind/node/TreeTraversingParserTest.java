package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

/**
 * Unit tests for {@link TreeTraversingParser} class
 */
public class TreeTraversingParserTest {

    @Test
    public void testEmptyObject() throws IOException {
        JsonNode rootNode = JsonNodeFactory.instance.objectNode();
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        // empty object should not have any children
        assertFalse(parser.nextToken().isStartArray());
        assertFalse(parser.nextToken().isStartObject());
        assertNull(parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testEmptyArray() throws IOException {
        JsonNode rootNode = JsonNodeFactory.instance.arrayNode();
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        // empty array should not have any children
        assertFalse(parser.nextToken().isStartArray());
        assertFalse(parser.nextToken().isStartObject());
        assertNull(parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testSimpleValue() throws IOException {
        JsonNode rootNode = JsonNodeFactory.instance.numberNode(42);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(42, parser.getIntValue());
        assertEquals("42", parser.getText());
        assertNull(parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testStringValue() throws IOException {
        JsonNode rootNode = JsonNodeFactory.instance.textNode("Hello World");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Hello World", parser.getText());
        assertNull(parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testNullValue() throws IOException {
        JsonNode rootNode = JsonNodeFactory.instance.nullNode();
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
        assertNull(parser.getText());
        assertNull(parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testBooleanTrueValue() throws IOException {
        JsonNode rootNode = JsonNodeFactory.instance.booleanNode(true);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertEquals(JsonToken.VALUE_TRUE, parser.nextToken());
        assertTrue(parser.isBoolean());
        assertEquals("true", parser.getText());
        assertNull(parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testBooleanFalseValue() throws IOException {
        JsonNode rootNode = JsonNodeFactory.instance.booleanNode(false);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertEquals(JsonToken.VALUE_FALSE, parser.nextToken());
        assertFalse(parser.isBoolean());
        assertEquals("false", parser.getText());
        assertNull(parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testArray() throws IOException {
        JsonNode rootNode = JsonNodeFactory.instance.arrayNode(
                JsonNodeFactory.instance.numberNode(1),
                JsonNodeFactory.instance.textNode("Two"),
                JsonNodeFactory.instance.booleanNode(true),
                JsonNodeFactory.instance.nullNode());
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertNull(parser.getText());

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals("1", parser.getText());

        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Two", parser.getText());

        assertEquals(JsonToken.VALUE_TRUE, parser.nextToken());
        assertTrue(parser.isBoolean());
        assertEquals("true", parser.getText());

        assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
        assertNull(parser.getText());

        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertNull(parser.getText());

        assertNull(parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testObject() throws IOException {
        JsonNode rootNode = JsonNodeFactory.instance.objectNode(
                JsonNodeFactory.instance.fieldName("Name"),
                JsonNodeFactory.instance.textNode("John"),
                JsonNodeFactory.instance.fieldName("Age"),
                JsonNodeFactory.instance.numberNode(30));
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertNull(parser.getText());

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("Name", parser.getText());

        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("Age", parser.getText());

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());
        assertEquals("30", parser.getText());

        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.getText());

        assertNull(parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testNestedObjectsAndArrays() throws IOException {
        JsonNode rootNode = JsonNodeFactory.instance.objectNode(
                JsonNodeFactory.instance.fieldName("Name"),
                JsonNodeFactory.instance.textNode("John"),
                JsonNodeFactory.instance.fieldName("Age"),
                JsonNodeFactory.instance.numberNode(30),
                JsonNodeFactory.instance.fieldName("Children"),
                JsonNodeFactory.instance.arrayNode(
                    JsonNodeFactory.instance.objectNode(
                        JsonNodeFactory.instance.fieldName("Name"),
                        JsonNodeFactory.instance.textNode("Alice"),
                        JsonNodeFactory.instance.fieldName("Age"),
                        JsonNodeFactory.instance.numberNode(25)),
                    JsonNodeFactory.instance.objectNode(
                        JsonNodeFactory.instance.fieldName("Name"),
                        JsonNodeFactory.instance.textNode("Bob"),
                        JsonNodeFactory.instance.fieldName("Age"),
                        JsonNodeFactory.instance.numberNode(22))));
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertNull(parser.getText());

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("Name", parser.getText());

        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("Age", parser.getText());

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());
        assertEquals("30", parser.getText());

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("Children", parser.getText());

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertNull(parser.getText());

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertNull(parser.getText());

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("Name", parser.getText());

        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Alice", parser.getText());

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("Age", parser.getText());

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(25, parser.getIntValue());
        assertEquals("25", parser.getText());

        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.getText());

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertNull(parser.getText());

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("Name", parser.getText());

        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Bob", parser.getText());

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("Age", parser.getText());

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(22, parser.getIntValue());
        assertEquals("22", parser.getText());

        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.getText());

        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertNull(parser.getText());

        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.getText());

        assertNull(parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test(expected = JsonParseException.class)
    public void testNullRootNode() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.nextToken();
    }
}