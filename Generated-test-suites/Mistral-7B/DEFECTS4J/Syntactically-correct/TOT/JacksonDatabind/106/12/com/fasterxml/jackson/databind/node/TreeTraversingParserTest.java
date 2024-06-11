package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Assert;
import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testParseEmptyArray() {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNodeFactory.instance.emptyArray());
        Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParseEmptyObject() {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNodeFactory.instance.objectNode());
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParseSimpleString() {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNodeFactory.instance.textNode("hello"));
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("hello", parser.getText());
        parser.nextToken(); // next should be end token
        parser.close();
    }

    @Test
    public void testParseSimpleNumber() {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNodeFactory.instance.numberNode(123));
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(123, parser.getIntValue());
        parser.nextToken(); // next should be end token
        parser.close();
    }

    @Test
    public void testParseSimpleObject() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"name\":\"John\", \"age\":30}");
        TreeTraversingParser parser = new TreeTraversingParser(node);

        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals("name", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("John", parser.getText());
        Assert.assertEquals("age", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(30, parser.getIntValue());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        parser.close();
    }

    @Test
    public void testParseEmptyStream() {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNodeFactory.instance.nullNode());
        Assert.assertEquals(JsonToken.START_NULL, parser.nextToken());
        Assert.assertEquals(JsonToken.END_NULL, parser.nextToken());
        parser.close();
    }

    @Test(expected = JsonProcessingException.class)
    public void testParseInvalidJson() {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNodeFactory.instance.textNode("{abcd"));
        parser.nextToken();
        parser.close();
    }
}