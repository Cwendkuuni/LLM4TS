package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TreeTraversingParserTest {

    private ObjectMapper mapper;
    private JsonFactory factory;
    private TreeTraversingParser parser;
    private JsonNode rootNode;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        factory = mapper.getFactory();
        rootNode = factory.createParser("[]").readValueAsTree();
        parser = new TreeTraversingParser(rootNode);
    }

    @After
    public void tearDown() throws Exception {
        parser.close();
    }

    @Test
    public void testEmptyParser() {
        parser = new TreeTraversingParser(null);
        assertFalse(parser.isClosed());
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithNullNode() {
        parser = new TreeTraversingParser(null);
        assertFalse(parser.isClosed());
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithArrayNode() throws JsonParseException {
        JsonNode node = JsonNodeFactory.instance.textNode("[1, 2, 3]");
        parser = new TreeTraversingParser(node);

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertFalse(parser.isClosed());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParserWithObjectNode() throws JsonParseException {
        JsonNode node = JsonNodeFactory.instance.objectNode();
        node.put("name", TextNode.valueOf("John"));
        node.put("age", TextNode.valueOf(30));
        parser = new TreeTraversingParser(node);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertFalse(parser.isClosed());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test(expected = JsonParseException.class)
    public void testParserWithNullToken() throws JsonParseException {
        parser = new TreeTraversingParser(rootNode);
        parser.nextToken(); // consume START_ARRAY
        assertFalse(parser.isClosed());
        parser.nextToken(); // consume VALUE_NUMBER_INT (1)
        parser.nextToken(); // consume VALUE_NUMBER_INT (2)
        parser.nextToken(); // consume VALUE_NUMBER_INT (3)
        parser.nextToken(); // consume END_ARRAY
        parser.nextToken(); // consume null token
    }

    @Test(expected = JsonParseException.class)
    public void testParserWithClosedParser() throws JsonParseException {
        parser = new TreeTraversingParser(rootNode);
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.close();
        parser.nextToken();
    }
}