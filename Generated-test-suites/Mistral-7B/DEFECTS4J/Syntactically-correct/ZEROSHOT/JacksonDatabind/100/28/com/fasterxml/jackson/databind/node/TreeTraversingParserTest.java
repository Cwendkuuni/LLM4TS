package com.fasterxml.jackson.databind.node;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonBuffer;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.AbstractNodeTestCase;
import com.fasterxml.jackson.databind.node.NodeCursor;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;

public class TreeTraversingParserTest extends AbstractNodeTestCase {

    private JsonFactory factory;
    private TreeTraversingParser parser;
    private JsonParser baseParser;
    private JsonNode rootNode;

    @Before
    public void setUp() throws Exception {
        factory = new JsonFactory();
        baseParser = factory.getParser();
        rootNode = parseTestData("/test-data/simpleTree.json");
        parser = new TreeTraversingParser(rootNode);
    }

    @After
    public void tearDown() throws Exception {
        parser.close();
    }

    @Test
    public void testInitialState() throws Exception {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParseObject() throws Exception {
        parser.nextToken(); // skip START_OBJECT

        assertTrue(parser.isObject());
        assertEquals("obj1", parser.getCurrentName());

        parser.skipChildren();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParseArray() throws Exception {
        parser.nextToken(); // skip START_ARRAY

        assertFalse(parser.isObject());
        assertNull(parser.getCurrentName());

        parser.nextToken(); // skip VALUE_STRING
        assertEquals("elem1", parser.getText());

        parser.skipChildren();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParseValue() throws Exception {
        parser.nextToken(); // skip START_OBJECT
        parser.nextToken(); // skip "name1": VALUE_STRING

        assertEquals("value1", parser.getText());

        parser.nextToken(); // skip END_OBJECT
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParseNumericValue() throws Exception {
        parser.nextToken(); // skip START_OBJECT
        parser.nextToken(); // skip "number1": VALUE_NUMBER_INT

        assertEquals(1, parser.getIntValue());

        parser.nextToken(); // skip END_OBJECT
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParseNullValue() throws Exception {
        parser.nextToken(); // skip START_OBJECT
        parser.nextToken(); // skip "null1": NULL

        assertNull(parser.getText());
        assertNull(parser.getNumberValue());
        assertFalse(parser.isNaN());

        parser.nextToken(); // skip END_OBJECT
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParseBooleanValue() throws Exception {
        parser.nextToken(); // skip START_OBJECT
        parser.nextToken(); // skip "boolean1": VALUE_TRUE

        assertTrue(parser.getBooleanValue());

        parser.nextToken(); // skip END_OBJECT
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParseEndOfStream() throws Exception {
        parser.nextToken(); // skip START_OBJECT
        parser.nextToken(); // skip "name1": VALUE_STRING

        parser.nextToken(); // skip END_OBJECT
        parser.nextToken(); // skip START_ARRAY

        parser.nextToken(); // skip VALUE_STRING
        assertEquals("elem1", parser.getText());

        parser.nextToken(); // skip END_ARRAY
        parser.nextToken(); // skip START_OBJECT

        parser.nextToken(); // skip END_OBJECT
        parser.nextToken(); // skip START_ARRAY

        parser.nextToken(); // skip END_ARRAY
        parser.nextToken(); // skip START_OBJECT

        parser.nextToken(); // skip END_OBJECT
        parser.nextToken(); // skip START_ARRAY

        parser.nextToken(); // skip END_ARRAY
        parser.nextToken(); // skip END_OBJECT

        assertTrue(parser.isClosed());
    }

    @Test(expected = JsonParseException.class)
    public void testParseInvalidToken() throws Exception {
        parser.nextToken(); // skip START_OBJECT
        parser.nextToken(); // skip "name1": VALUE_STRING

        parser.nextToken(); // skip END_OBJECT
        parser.nextToken(); // skip START_ARRAY

        parser.nextToken(); // skip VALUE_STRING
        parser.nextToken(); // skip VALUE_STRING

        parser.nextToken(); // skip END_ARRAY
        parser.nextToken(); // skip START_OBJECT

        parser.nextToken(); // skip END_OBJECT
        parser.nextToken(); // skip START_ARRAY

        parser.nextToken(); // skip VALUE_STRING
        parser.nextToken(); // skip VALUE_STRING

        parser.nextToken(); // should be END_ARRAY, but it is not

        fail("Unexpected token: " + parser.nextToken());
    }
}