package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.AbstractBaseNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeTraversingParserTest {

    @Test
    public void testParseEmptyArray() throws JsonProcessingException {
        TreeTraversingParser parser = new TreeTraversingParser(createEmptyArray());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParseEmptyObject() throws JsonProcessingException {
        TreeTraversingParser parser = new TreeTraversingParser(createEmptyObject());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParseSimpleString() throws JsonProcessingException {
        TreeTraversingParser parser = new TreeTraversingParser(createSimpleStringNode("Hello World"));
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Hello World", parser.getText());
        parser.close();
    }

    @Test
    public void testParseSimpleNumber() throws JsonProcessingException {
        TreeTraversingParser parser = new TreeTraversingParser(createSimpleNumberNode(123));
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(123, parser.getIntValue());
        parser.close();
    }

    @Test
    public void testParseSimpleBoolean() throws JsonProcessingException {
        TreeTraversingParser parser = new TreeTraversingParser(createSimpleBooleanNode(true));
        assertEquals(JsonToken.VALUE_Boolean, parser.nextToken());
        assertEquals(true, parser.getBooleanValue());
        parser.close();
    }

    @Test
    public void testParseSimpleNull() throws JsonProcessingException {
        TreeTraversingParser parser = new TreeTraversingParser(createNullNode());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken()); // for null node
        parser.close();
    }

    @Test
    public void testParseNestedObjects() throws JsonProcessingException {
        TreeTraversingParser parser = new TreeTraversingParser(createNestedObjects());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("object1", parser.getCurrentName());
        parser.nextToken();
        assertEquals("object2", parser.getCurrentName());
        parser.nextToken();
        assertEquals("value3", parser.getCurrentName());
        assertEquals("3", parser.getText());
        parser.close();
    }

    @Test
    public void testParseNestedArrays() throws JsonProcessingException {
        TreeTraversingParser parser = new TreeTraversingParser(createNestedArrays());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("value2", parser.getText());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test(expected = JsonProcessingException.class)
    public void testParseInvalidJson() throws JsonProcessingException {
        TreeTraversingParser parser = new TreeTraversingParser(createInvalidJson());
        parser.nextToken();
        fail("Expected JsonProcessingException");
    }

    @Test(expected = JsonProcessingException.class)
    public void testParseClosedParser() throws JsonProcessingException {
        TreeTraversingParser parser = new TreeTraversingParser(createEmptyObject());
        parser.nextToken();
        parser.close();
        parser.nextToken();
        fail("Expected JsonProcessingException");
    }

    private JsonNode createEmptyArray() {
        return new ArrayNode(new ObjectMapper().getFactory().getEmptyArrayCreator().createArrayNode());
    }

    private JsonNode createEmptyObject() {
        return new ObjectMapper().getFactory().getEmptyObjectCreator().createObjectNode();
    }

    private JsonNode createSimpleStringNode(String value) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.createTextNode(value);
    }

    private JsonNode createSimpleNumberNode(int value) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.valueToTree(value);
    }

    private JsonNode createSimpleBooleanNode(boolean value) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.valueToTree(value);
    }

    private JsonNode createNullNode() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.valueToTree(null);
    }

    private JsonNode createNestedObjects() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root = objectMapper.createObjectNode();
        ObjectNode object1 = objectMapper.createObjectNode();
        ObjectNode object2 = objectMapper.createObjectNode();
        object1.put("object2", object2);
        root.set("object1", object1);
        root.put("value3", createSimpleNumberNode(3));
        return root;
    }

    private JsonNode createNestedArrays() {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode root = objectMapper.createArrayNode();
        ArrayNode arr1 = objectMapper.createArrayNode();
        arr1.add(createSimpleNumberNode(1));
        root.add(arr1);
        ArrayNode arr2 = objectMapper.createArrayNode();
        arr2.add(createSimpleStringNode("value2"));
        root.add(arr2);
        return root;
    }

    private JsonNode createInvalidJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree("{'status':'success', 'data':[]}"); // intentionally missing comma
    }
}