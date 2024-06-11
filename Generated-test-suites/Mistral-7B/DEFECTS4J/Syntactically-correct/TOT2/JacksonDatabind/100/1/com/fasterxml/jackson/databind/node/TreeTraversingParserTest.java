package com.fasterxml.jackson.databind.node; // import statements go here

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.AbstractJsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyJsonArray() throws Exception {
        JsonParser parser = new TreeTraversingParser(new JsonFactory().createParser(getClass().getResourceAsStream("/test-empty-array.json")));
        JsonToken token;

        // Basic test: empty array
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token);

        // Test getting text from empty array
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);
        Assert.assertNull(parser.getText());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token);

        // Test reading empty array
        parser.close();
        parser = new TreeTraversingParser(new JsonFactory().createParser(getClass().getResourceAsStream("/test-empty-array.json")));
        ArrayNode arrayNode = (ArrayNode) parser.readValueAs(ArrayNode.class);
        Assert.assertEquals(0, arrayNode.size());

        // Test iteration through empty array
        AbstractJsonNode currentNode = arrayNode.iterator().next();
        Assert.assertNull(currentNode.getTextValue());
        parser.close();
    }

    @Test
    public void testEmptyJsonObject() throws Exception {
        JsonParser parser = new TreeTraversingParser(new JsonFactory().createParser(getClass().getResourceAsStream("/test-empty-object.json")));
        JsonToken token;

        // Basic test: empty object
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);

        // Test getting text from empty object
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);
        Assert.assertNull(parser.getText());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);

        // Test reading empty object
        parser.close();
        parser = new TreeTraversingParser(new JsonFactory().createParser(getClass().getResourceAsStream("/test-empty-object.json")));
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.readValue(parser, ObjectNode.class);

        // Test iteration through empty object
        Assert.assertEquals(0, objectNode.size());
        parser.close();
    }

    @Test
    public void testJsonArrayWithOneElement() throws Exception {
        JsonParser parser = new TreeTraversingParser(new JsonFactory().createParser(getClass().getResourceAsStream("/test-array-one-element.json")));
        JsonToken token;

        // Basic test: array with one element
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        String stringValue = parser.getText();
        Assert.assertEquals("one", stringValue);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token);

        // Test getting text from array with one element
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);
        Assert.assertEquals("one", parser.getText());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token);

        // Test reading array with one element
        parser.close();
        parser = new TreeTraversingParser(new JsonFactory().createParser(getClass().getResourceAsStream("/test-array-one-element.json")));
        ArrayNode arrayNode = (ArrayNode) parser.readValueAs(ArrayNode.class);
        TextNode textNode = (TextNode) arrayNode.get(0);
        Assert.assertEquals("one", textNode.getTextValue());

        // Test iteration through array with one element
        AbstractJsonNode currentNode = arrayNode.iterator().next();
        Assert.assertEquals("one", currentNode.getTextValue());
        parser.close();
    }

    @Test
    public void testJsonArrayWithMultipleElements() throws Exception {
        JsonParser parser = new TreeTraversingParser(new JsonFactory().createParser(getClass().getResourceAsStream("/test-array-multiple-elements.json")));
        JsonToken token;

        // Basic test: array with multiple elements
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        String stringValue1 = parser.getText();
        Assert.assertEquals("one", stringValue1);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        int intValue1 = parser.getIntValue();
        Assert.assertEquals(1, intValue1);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        float floatValue1 = parser.getFloatValue();
        Assert.assertEquals(1.0f, floatValue1, 0.001f);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_TRUE, token);
        boolean boolValue1 = parser.getBooleanValue();
        Assert.assertTrue(boolValue1);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NULL, token);
        ObjectNode nullNode = (ObjectNode) parser.readValueAs(ObjectNode.class);
        Assert.assertNull(nullNode.getTextValue());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        String stringValue2 = parser.getText();
        Assert.assertEquals("two", stringValue2);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token);

        // Test getting text from array with multiple elements
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);
        Assert.assertEquals("one", parser.getText());
        token = parser.nextToken();
        Assert.assertEquals(1, parser.getIntValue());
        token = parser.nextToken();
        Assert.assertEquals(1.0f, parser.getFloatValue(), 0.001f);
        token = parser.nextToken();
        Assert.assertTrue(parser.getBooleanValue());
        token = parser.nextToken();
        Assert.assertNull(parser.getText());
        token = parser.nextToken();
        Assert.assertEquals("two", parser.getText());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token);

        // Test reading array with multiple elements
        parser.close();
        parser = new TreeTraversingParser(new JsonFactory().createParser(getClass().getResourceAsStream("/test-array-multiple-elements.json")));
        ArrayNode arrayNode = (ArrayNode) parser.readValueAs(ArrayNode.class);
        TextNode textNode1 = (TextNode) arrayNode.get(0);
        Assert.assertEquals("one", textNode1.getTextValue());
        IntNode intNode1 = (IntNode) arrayNode.get(1);
        Assert.assertEquals(1, intNode1.intValue());
        NumberNode floatNode1 = (NumberNode) arrayNode.get(2);
        Assert.assertEquals(1.0f, floatNode1.floatValue(), 0.001f);
        BooleanNode boolNode1 = (BooleanNode) arrayNode.get(3);
        Assert.assertTrue(boolNode1.booleanValue());
        ObjectNode nullNode1 = (ObjectNode) arrayNode.get(4);
        Assert.assertNull(nullNode1.getTextValue());
        TextNode textNode2 = (TextNode) arrayNode.get(5);
        Assert.assertEquals("two", textNode2.getTextValue());

        // Test iteration through array with multiple elements
        AbstractJsonNode currentNode = arrayNode.iterator().next();
        Assert.assertEquals("one", currentNode.getTextValue());
        currentNode = arrayNode.iterator().next();
        Assert.assertEquals(1, currentNode.getIntValue());
        currentNode = arrayNode.iterator().next();
        Assert.assertEquals(1.0f, currentNode.getFloatValue(), 0.001f);
        currentNode = arrayNode.iterator().next();
        Assert.assertTrue(currentNode.getBooleanValue());
        currentNode = arrayNode.iterator().next();
        Assert.assertNull(currentNode.getTextValue());
        currentNode = arrayNode.iterator().next();
        Assert.assertEquals("two", currentNode.getTextValue());
        parser.close();
    }

    @Test
    public void testJsonObjectWithOneElement() throws Exception {
        JsonParser parser = new TreeTraversingParser(new JsonFactory().createParser(getClass().getResourceAsStream("/test-object-one-element.json")));
        JsonToken token;

        // Basic test: object with one element
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        String fieldName = parser.getText();
        Assert.assertEquals("fieldName", fieldName);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        String stringValue = parser.getText();
        Assert.assertEquals("one", stringValue);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);

        // Test getting text from object with one element
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);
        Assert.assertEquals("fieldName", parser.getText());
        Assert.assertEquals("one", parser.getText());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);

        // Test reading object with one element
        parser.close();
        parser = new TreeTraversingParser(new JsonFactory().createParser(getClass().getResourceAsstream("/test-object-one-element.json")));
        ObjectNode objectNode = (ObjectNode) parser.readValueAs(ObjectNode.class);
        TextNode textNode = (TextNode) objectNode.get("fieldName");
        Assert.assertEquals("one", textNode.getTextValue());

        // Test iteration through object with one element
        AbstractJsonNode currentNode = objectNode.iterator().next();
        Assert.assertEquals("fieldName", currentNode.getName());
        Assert.assertEquals("one", currentNode.getTextValue());
        parser.close();
    }

    @Test
    public void testJsonObjectWithMultipleElements() throws Exception {
        JsonParser parser = new TreeTraversingParser(new JsonFactory().createParser(getClass().getResourceAsStream("/test-object-multiple-elements.json")));
        JsonToken token;

        // Basic test: object with multiple elements
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        String fieldName1 = parser.getText();
        Assert.assertEquals("fieldName1", fieldName1);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        int intValue1 = parser.getIntValue();
        Assert.assertEquals(1, intValue1);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        String fieldName2 = parser.getText();
        Assert.assertEquals("fieldName2", fieldName2);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        float floatValue1 = parser.getFloatValue();
        Assert.assertEquals(1.0f, floatValue1, 0.001f);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);

        // Test getting text from object with multiple elements
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);
        Assert.assertEquals("fieldName1", parser.getText());
        Assert.assertEquals(1, parser.getIntValue());
        token = parser.nextToken();
        Assert.assertEquals("fieldName2", parser.getText());
        Assert.assertEquals(1.0f, parser.getFloatValue(), 0.001f);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);

        // Test reading object with multiple elements
        parser.close();
        parser = new TreeTraversingParser(new JsonFactory().createParser(getClass().getResourceAsStream("/test-object-multiple-elements.json")));
        ObjectNode objectNode = (ObjectNode) parser.readValueAs(ObjectNode.class);
        IntNode intNode1 = (IntNode) objectNode.get("fieldName1");
        Assert.assertEquals(1, intNode1.intValue());
        TextNode textNode1 = (TextNode) objectNode.get("fieldName2");
        Assert.assertEquals("fieldName2", textNode1.getName());
        Assert.assertEquals("one", textNode1.getTextValue());

        // Test iteration through object with multiple elements
        AbstractJsonNode currentNode = objectNode.iterator().next();
        Assert.assertEquals("fieldName1", currentNode.getName());
        Assert.assertEquals(1, currentNode.getIntValue());
        currentNode = objectNode.iterator().next();
        Assert.assertEquals("fieldName2", currentNode.getName());
        Assert.assertEquals("one", currentNode.getTextValue());
        parser.close();
    }

    @Test
    public void testJsonObjectWithNullValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(new JsonFactory().createParser(getClass().getResourceAsStream("/test-object-null-value.json")));
        JsonToken token;

        // Basic test: object with null value
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        String fieldName = parser.getText();
        Assert.assertEquals("fieldName", fieldName);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NULL, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);

        // Test getting text from object with null value
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);
        Assert.assertEquals("fieldName", parser.getText());
        Assert.assertNull(parser.getText());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);

        // Test reading object with null value
        parser.close();
        parser = new TreeTraversingParser(new JsonFactory().createParser(getClass().getResourceAsStream("/test-object-null-value.json")));
        ObjectNode objectNode = (ObjectNode) parser.readValueAs(ObjectNode.class);
        ObjectNode nullNode = (ObjectNode) objectNode.get("fieldName");
        Assert.assertNull(nullNode.getTextValue());

        // Test iteration through object with null value
        AbstractJsonNode currentNode = objectNode.iterator().next();
        Assert.assertEquals("fieldName", currentNode.getName());
        Assert.assertNull(currentNode.getTextValue());
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testJsonObjectWithInvalidFields() throws Exception {
        JsonParser parser = new TreeTraversingParser(new JsonFactory().createParser(getClass().getResourceAsStream("/test-object-invalid-fields.json")));

        // Test reading object with invalid fields
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken(); // should throw exception
    }
}