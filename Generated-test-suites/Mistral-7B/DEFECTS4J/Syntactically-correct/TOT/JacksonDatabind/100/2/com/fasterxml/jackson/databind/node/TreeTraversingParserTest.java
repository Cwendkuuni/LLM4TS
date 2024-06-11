package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TreeTraversingParserTest {

    private ObjectMapper mapper;
    private JsonNodeFactory nodeFactory;
    private ObjectCodec objectCodec;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        nodeFactory = mapper.getNodeFactory();
        objectCodec = mapper.getObjectCodec();
    }

    @After
    public void tearDown() throws Exception {
        mapper.close();
    }

    @Test
    public void testEmptyParser() throws IOException, JsonParseException {
        TreeTraversingParser parser = new TreeTraversingParser(null, objectCodec);
        parser.nextToken();
        parser.close();
        assertFalse(parser.isClosed()); // should be false before close call
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParserWithRootNode() throws IOException, JsonParseException {
        String json = "{}";
        TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree(json), objectCodec);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("", parser.getCurrentName());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken()); // for good measure

        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParserWithArrayNode() throws IOException, JsonParseException {
        String json = "[1,2,3]";
        TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree(json), objectCodec);

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken()); // for good measure

        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParserWithStringNode() throws IOException, JsonParseException {
        String json = "{\"name\": \"John\"}";
        TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree(json), objectCodec);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken()); // for good measure

        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParserWithNullNode() throws IOException, JsonParseException {
        String json = "null";
        TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree(json), objectCodec);

        assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
        assertEquals(JsonToken.VALUE_NULL, parser.nextToken());

        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParserWithNumberNode() throws IOException, JsonParseException {
        String json = "42";
        TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree(json), objectCodec);

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(42, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());

        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParserWithFloatNode() throws IOException, JsonParseException {
        String json = "3.1415";
        TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree(json), objectCodec);

        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(3.1415, parser.getDoubleValue(), 0.0001);
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());

        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParserWithBooleanNode() throws IOException, JsonParseException {
        String json = "true";
        TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree(json), objectCodec);

        assertEquals(JsonToken.VALUE_BOOLEAN, parser.nextToken());
        assertEquals(true, parser.getBooleanValue());
        assertEquals(JsonToken.VALUE_BOOLEAN, parser.nextToken());

        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParserWithBinaryNode() throws IOException, JsonParseException {
        String json = "{\"binary\": [1,2,3]}" ;
        TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree(json), objectCodec);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("binary", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken()); // for good measure

        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParserWithNestedObjects() throws IOException, JsonParseException {
        String json = "{\"name\": \"John\", \"address\": { \"street\": \"123 Main\", \"city\": \"Anytown\"}}";
        TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree(json), objectCodec);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("address", parser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("street", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("123 Main", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("city", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Anytown", parser.getText());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken()); // for good measure

        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParserWithNestedArrays() throws IOException, JsonParseException {
        String json = "{\"name\": \"John\", \"family\": [\"Miller\", [\"Doe\", \"Smith\"]]}}";
        TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree(json), objectCodec);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("family", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Miller", parser.getText());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Doe", parser.getText());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Smith", parser.getText());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken()); // for good measure

        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test(expected = JsonParseException.class)
    public void testParserWithInvalidJson() throws IOException, JsonParseException {
        String json = "{name:123}";
        TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree(json), objectCodec);
        parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        parser.nextToken();
        parser.nextToken(); // should fail here
    }
}