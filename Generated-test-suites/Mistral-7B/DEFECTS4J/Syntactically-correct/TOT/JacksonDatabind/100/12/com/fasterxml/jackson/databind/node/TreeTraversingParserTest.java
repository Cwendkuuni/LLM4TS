package com.fasterxml.jackson.databind.node;// imports omitted for brevity

import org.junit.Assert;
import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        Assert.assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testSimpleParser() throws IOException {
        String json = "{\"name\": \"John\", \"age\": 27}";
        TreeTraversingParser parser = new TreeTraversingParser(JsonFactory.instance.parse(json));

        // Test token ordering
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("name", parser.getCurrentName());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        Assert.assertEquals("John", parser.getText());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("age", parser.getCurrentName());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        Assert.assertEquals(27, parser.getIntValue());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);

        parser.close();
    }

    @Test
    public void testArrayParser() throws IOException {
        String json = "[1, 2.5, 3, null, true]";
        TreeTraversingParser parser = new TreeTraversingParser(JsonFactory.instance.parse(json));

        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        Assert.assertEquals(1, parser.getIntValue());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        Assert.assertEquals(2.5, parser.getDoubleValue(), 0.001);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        Assert.assertEquals(3, parser.getIntValue());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NULL, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_BOOLEAN, token);
        Assert.assertTrue(parser.getBooleanValue());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token);

        parser.close();
    }

    @Test(expected = IOException.class)
    public void testMalformedParser() throws IOException {
        String json = "{name: \"John\""; // missing end quote
        TreeTraversingParser parser = new TreeTraversingParser(JsonFactory.instance.parse(json));
        parser.nextToken(); // "{"
        parser.nextToken(); // "name:"
        parser.nextToken(); // "name"
        parser.nextToken(); // "John"
        parser.nextToken(); // expected end object here
        parser.nextToken(); // nothing, parser reaches end of input
        parser.close();
    }
}