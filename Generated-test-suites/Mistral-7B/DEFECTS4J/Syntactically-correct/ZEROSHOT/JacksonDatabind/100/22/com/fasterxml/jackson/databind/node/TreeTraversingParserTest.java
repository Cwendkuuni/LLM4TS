package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class TreeTraversingParserTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @org.junit.Test
    public void testEmptyParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertFalse(parser.isClosed());
        assertNull(parser.nextToken());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @org.junit.Test
    public void testParserWithRootNode() throws IOException {
        JsonNode node = new ObjectNode(MAPPER.getFactory().getObjectMapperConfig());
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @org.junit.Test
    public void testParserWithArrayNode() throws IOException {
        JsonNode node = new ArrayNode(MAPPER.getFactory().getObjectMapperConfig());
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @org.junit.Test
    public void testParserWithStringNode() throws IOException {
        JsonNode node = new TextNode("Hello World!");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Hello World!", parser.getText());
        parser.close();
    }

    @org.junit.Test
    public void testParserWithNumberIntNode() throws IOException {
        JsonNode node = new NumberNode(123);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(123, parser.getIntValue());
        parser.close();
    }

    @org.junit.Test
    public void testParserWithNumberFloatNode() throws IOException {
        JsonNode node = new NumberNode(123.45);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(123.45, parser.getDoubleValue(), 0.0);
        parser.close();
    }

    @org.junit.Test
    public void testParserWithNumberBigDecimalNode() throws IOException {
        JsonNode node = new NumberNode(new BigDecimal("456.78"));
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals("456.78", parser.getText(), "Expected string representation of BigDecimal");
        parser.close();
    }

    @org.junit.Test
    public void testParserWithNumberBigIntegerNode() throws IOException {
        JsonNode node = new NumberNode(new BigInteger("789"));
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(789, parser.getLongValue());
        parser.close();
    }

    @org.junit.Test
    public void testParserWithObjectNode() throws IOException {
        JsonNode node = new ObjectNode(MAPPER.getFactory().getObjectMapperConfig());
        node.put("name", new TextNode("John"));
        node.put("age", new NumberNode(30));
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @org.junit.Test
    public void testParserWithBinaryNode() throws IOException {
        JsonNode node = new BinaryNode(new byte[] { 0x01, 0x02, 0x03 }, Base64Variant.STANDARD);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        byte[] data = parser.getBinaryValue(Base64Variant.STANDARD);
        assertEquals(new String(new byte[] { 0x01, 0x02, 0x03 }), new String(data));
        parser.close();
    }

    @org.junit.Test
    public void testParserWithEmbeddedObjectNode() throws IOException {
        JsonNode rootNode = new ObjectNode(MAPPER.getFactory().getObjectMapperConfig());
        JsonNode embeddedNode = new ObjectNode(MAPPER.getFactory().getObjectMapperConfig());
        embeddedNode.put("name", new TextNode("Mike"));
        rootNode.set("embedded", embeddedNode);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("embedded", parser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Mike", parser.getText());
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @org.junit.Test
    public void testParserWithClosedParser() throws IOException {
        JsonNode node = new ObjectNode(MAPPER.getFactory().getObjectMapperConfig());
        TreeTraversingParser parser = new TreeTraversingParser(node);
        parser.close();
        assertFalse(parser.isClosed()); // Should not be closed yet
        assertEquals(JsonToken.CLOSED, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @org.junit.Test
    public void testParserWithParserWithChildren() throws IOException {
        JsonNode node = new ObjectNode(MAPPER.getFactory().getObjectMapperConfig());
        node.put("name", new TextNode("John"));
        node.put("children", new ArrayNode(MAPPER.getFactory().getObjectMapperConfig()));
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("children", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @org.junit.Test
    public void testParserWithParserWithNestedChildren() throws IOException {
        JsonNode node = new ObjectNode(MAPPER.getFactory().getObjectMapperConfig());
        node.put("name", new TextNode("John"));
        node.put("children", new ArrayNode(MAPPER.getFactory().getObjectMapperConfig()));
        JsonNode childNode = new ObjectNode(MAPPER.getFactory().getObjectMapperConfig());
        childNode.put("grandchildren", new ArrayNode(MAPPER.getFactory().getObjectMapperConfig()));
        childNode.put("value", new TextNode("some value"));
        ((ArrayNode) node.get("children")).add(childNode);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("children", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("grandchildren", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("value", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("some value", parser.getText());
        parser.skipChildren();
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @org.junit.Test
    public void testParserWithParserWithNullNode() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.nextToken());
        parser.close();
    }

    @org.junit.Test
    public void testParserWithParserWithInvalidNode() throws IOException {
        JsonNode node = new ObjectNode(MAPPER.getFactory().getObjectMapperConfig());
        node.put("name", new TextNode("John"));
        node.put("invalid", 42);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        _throwTestParseException(parser);
        parser.close();
    }

    @org.junit.Test
    public void testParserWithParserWithInvalidNodeWithEmptyStream() throws IOException {
        JsonFactory factory = MAPPER.getFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        JsonParser parser = factory.createParser(inputStream);
        parser.close();
        TreeTraversingParser treeParser = new TreeTraversingParser(parser);
        _throwTestParseException(treeParser);
        treeParser.close();
    }

    private void _throwTestParseException(TreeTraversingParser parser) throws IOException {
        try {
            parser.nextToken();
        } catch (JsonParseException e) {
            throw e;
        }
        fail("Expected JsonParseException");
    }
}