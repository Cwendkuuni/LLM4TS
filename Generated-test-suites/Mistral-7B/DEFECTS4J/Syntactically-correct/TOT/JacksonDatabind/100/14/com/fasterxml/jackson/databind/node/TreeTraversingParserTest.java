package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyArray() throws Exception {
        JsonNode jsonNode = JsonFactory.instance.createParser("[]").readValueAsTree();
        JsonParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // START_ARRAY
        Assert.assertFalse(parser.isClosed());
        parser.nextToken(); // END_ARRAY
        parser.close();
        Assert.assertTrue(parser.isClosed());
    }

    @Test
    public void testEmptyObject() throws Exception {
        JsonNode jsonNode = JsonFactory.instance.createParser("{}").readValueAsTree();
        JsonParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // START_OBJECT
        Assert.assertFalse(parser.isClosed());
        parser.nextToken(); // END_OBJECT
        parser.close();
        Assert.assertTrue(parser.isClosed());
    }

    @Test
    public void testArrayWithSingleValue() throws Exception {
        JsonNode jsonNode = JsonFactory.instance.createParser("[[1]]").readValueAsTree();
        JsonParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // START_ARRAY
        parser.nextToken(); // VALUE_INTEGER
        Assert.assertEquals(JsonToken.VALUE_INTEGER, parser.nextToken());
        Assert.assertEquals(1, parser.getIntValue());
        parser.nextToken(); // END_ARRAY
        parser.nextToken(); // END_ARRAY
        parser.close();
        Assert.assertTrue(parser.isClosed());
    }

    @Test
    public void testArrayWithMultipleValues() throws Exception {
        JsonNode jsonNode = JsonFactory.instance.createParser("[[1,2,3]]").readValueAsTree();
        JsonParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // START_ARRAY
        parser.nextToken(); // START_ARRAY
        Assert.assertEquals(JsonToken.VALUE_INTEGER, parser.nextToken());
        Assert.assertEquals(1, parser.getIntValue());
        parser.nextToken(); // VALUE_INTEGER
        Assert.assertEquals(2, parser.getIntValue());
        parser.nextToken(); // VALUE_INTEGER
        Assert.assertEquals(3, parser.getIntValue());
        parser.nextToken(); // END_ARRAY
        parser.nextToken(); // END_ARRAY
        parser.close();
        Assert.assertTrue(parser.isClosed());
    }

    @Test
    public void testObjectWithSingleValue() throws Exception {
        JsonNode jsonNode = JsonFactory.instance.createParser("{\"name\":\"John\"}").readValueAsTree();
        JsonParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // FIELD_NAME
        Assert.assertEquals("name", parser.getCurrentName());
        parser.nextToken(); // VALUE_STRING
        Assert.assertEquals("John", parser.getText());
        parser.nextToken(); // END_OBJECT
        parser.close();
        Assert.assertTrue(parser.isClosed());
    }

    @Test
    public void testObjectWithMultipleValues() throws Exception {
        JsonNode jsonNode = JsonFactory.instance.createParser("{\"name\":\"John\",\"age\":30}").readValueAsTree();
        JsonParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // FIELD_NAME
        Assert.assertEquals("name", parser.getCurrentName());
        parser.nextToken(); // VALUE_STRING
        Assert.assertEquals("John", parser.getText());
        parser.nextToken(); // FIELD_NAME
        Assert.assertEquals("age", parser.getCurrentName());
        parser.nextToken(); // VALUE_INTEGER
        Assert.assertEquals(30, parser.getIntValue());
        parser.nextToken(); // END_OBJECT
        parser.close();
        Assert.assertTrue(parser.isClosed());
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidJson() throws Exception {
        JsonParser parser = new TreeTraversingParser(JsonFactory.instance.createParser("{"));
        parser.nextToken(); // START_OBJECT
        Assert.fail();
    }
}