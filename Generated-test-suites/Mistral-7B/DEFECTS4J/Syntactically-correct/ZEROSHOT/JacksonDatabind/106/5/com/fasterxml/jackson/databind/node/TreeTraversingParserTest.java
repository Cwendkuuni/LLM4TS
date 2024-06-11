package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectCodec;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testConstructorWithJsonNode() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        // Test currentName accessor
        assertEquals("name", parser.getCurrentName());
        parser.nextToken();
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken().asInt());
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken().asInt());
        assertEquals(30, parser.getIntValue());
        parser.close();
    }

    @Test
    public void testConstructorWithJsonNodeAndObjectCodec() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\": \"John\", \"age\": 30}");
        ObjectCodec objectCodec = new ObjectMapper().getFactory().getCodec();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        // The constructor with ObjectCodec is identical to the one without it
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        parser.nextToken();
        assertEquals("John", parser.getText());
        parser.nextToken();
        assertEquals("age", parser.getCurrentName());
        parser.nextToken();
        assertEquals(30, parser.getIntValue());
        parser.close();
    }

    @Test
    public void testIsClosed() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertFalse(parser.isClosed());
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testNextToken() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        parser.nextToken();
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken().asInt());
        assertEquals("age", parser.getCurrentName());
        parser.nextToken();
        assertEquals(30, parser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test(expected = JsonParseException.class)
    public void testNextTokenAfterClosedParser() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.close();
        parser.nextToken();
    }

    @Test
    public void testSkipChildren() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\": \"John\", \"age\": 30, \"children\": [1, 2, 3]}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        assertEquals("name", parser.getCurrentName());
        parser.nextToken();
        assertEquals("John", parser.getText());
        parser.nextToken();
        parser.nextToken(); // Skip the "age" field
        parser.skipChildren(); // Skip the "children" array
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testTextAccessors() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        assertEquals("name", parser.getCurrentName());
        parser.nextToken();
        assertEquals("John", parser.getText());
        parser.nextToken();
        assertEquals("age", parser.getCurrentName());
        assertEquals("30", parser.getText());
        parser.nextToken();
        assertEquals(30, parser.getIntValue());
        parser.close();
    }

    @Test
    public void testNumberAccessors() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"number\": 1.5, \"integer\": 42}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, parser.nextToken().asInt());
        parser.nextToken();
        assertEquals("number", parser.getCurrentName());
        parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken().asInt());
        assertEquals(1.5, parser.getDoubleValue(), 0.001);
        parser.nextToken();
        assertEquals("integer", parser.getCurrentName());
        parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken().asInt());
        assertEquals(42, parser.getIntValue());
        parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testBinaryAccessors() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"binary\": [1, 2, 3]}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, parser.nextToken().asInt());
        parser.nextToken();
        assertEquals("binary", parser.getCurrentName());
        parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, parser.nextToken().asInt());
        byte[] binaryData = parser.getBinaryValue(Base64Variant.STANDARD);
        assertTrue(binaryData.length > 0);
        parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testNamedCurrentName() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.overrideCurrentName("fieldA");
        parser.nextToken();
        assertEquals("name", "fieldA");
        parser.nextToken();
        assertEquals("John", parser.getText());
        parser.nextToken();
        assertEquals("age", "fieldA");
        parser.nextToken();
        assertEquals(30, parser.getIntValue());
        parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParseEmptyObject() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParseEmptyArray() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("[]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParseNullNode() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree(null);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_NULL, parser.nextToken());
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testParseInvalidJson() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("\"invalid JSON\"");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.close();
    }
}