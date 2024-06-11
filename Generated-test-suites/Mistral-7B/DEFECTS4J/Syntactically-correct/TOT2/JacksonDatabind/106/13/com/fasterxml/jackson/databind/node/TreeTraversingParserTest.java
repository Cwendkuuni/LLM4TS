package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class TreeTraversingParserTest
{
    @Test
    public void testBasicFunctionality_EmptyArray() throws Exception
    {
        JsonNode rootNode = JsonNodeFactory.instance.arrayNode();
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
        parser.close();
    }

    @Test
    public void testBasicFunctionality_EmptyObject() throws Exception
    {
        JsonNode rootNode = JsonNodeFactory.instance.objectNode();
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        parser.close();
    }

    @Test
    public void testBasicFunctionality_SimpleValues() throws Exception
    {
        JsonNode rootNode = JsonNodeFactory.instance.objectNode();
        rootNode.put("stringKey", new TextNode("Hello"));
        rootNode.put("intKey", 42);
        rootNode.put("floatKey", 3.14F);
        rootNode.put("doubleKey", 1.23);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        assertEquals("Hello", parser.getText());
        parser.nextToken(); // SKIP: stringKey

        assertEquals(42, parser.getIntValue());
        parser.nextToken(); // SKIP: intKey

        assertEquals(3.14, parser.getFloatValue(), 0.01);
        parser.nextToken(); // SKIP: floatKey

        assertEquals(1.23, parser.getDoubleValue(), 0.001);
        parser.nextToken(); // SKIP: doubleKey

        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testErrorHandling_NullRootNode() throws Exception
    {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.nextToken(); // null node should be handled silently
        Assert.fail("Expected JsonParseException");
    }

    @Test(expected = JsonParseException.class)
    public void testErrorHandling_UnknownToken() throws Exception
    {
        JsonNode rootNode = JsonNodeFactory.instance.objectNode();
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        parser.nextToken(); // read START_OBJECT
        parser.nextToken(); // read key
        parser.nextToken(); // read unknown token (e.g., VALUE_INT)
        parser.close();

        Assert.fail("Expected JsonParseException");
    }

    @Test(expected = JsonParseException.class)
    public void testErrorHandling_EOF() throws Exception
    {
        JsonNode rootNode = JsonNodeFactory.instance.objectNode();
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        parser.nextToken(); // read START_OBJECT
        parser.nextToken(); // read key
        parser.nextToken(); // read value
        parser.nextToken(); // read START_ARRAY
        parser.nextToken(); // read value
        parser.close(); // should be an END_ARRAY, but we've already closed

        Assert.fail("Expected JsonParseException");
    }

    @Test
    public void testSkipChildren() throws Exception
    {
        JsonNode rootNode = JsonNodeFactory.instance.objectNode();
        rootNode.put("arrayKey", JsonNodeFactory.instance.arrayNode());
        rootNode.put("objectKey", JsonNodeFactory.instance.objectNode());

        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        parser.nextToken(); // read START_OBJECT

        parser.skipChildren();

        // Read the next token, which should be END_OBJECT
        JsonToken nextToken = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, nextToken);

        parser.close();
    }

    @Test
    public void testIsClosed() throws Exception
    {
        JsonNode rootNode = JsonNodeFactory.instance.arrayNode();
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        // Is closed should be false since we haven't called close() yet
        assertFalse(parser.isClosed());

        parser.close();

        // Is closed should be true after calling close()
        assertTrue(parser.isClosed());
    }
}