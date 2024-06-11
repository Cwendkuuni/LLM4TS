package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Assert;
import org.junit.Test;

import static com.fasterxml.jackson.databind.JsonTestUtils.*;

public class TreeTraversingParserTest {

    @Test
    public void testArrayParser() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree("[1, 2, 3]");
        TreeTraversingParser parser = new TreeTraversingParser(root);

        // Test nextToken with no containers
        parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, parser.getCurrentToken());
        parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_INT, parser.getCurrentToken());
        Assert.assertEquals(1, parser.getIntValue());
        parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_INT, parser.getCurrentToken());
        Assert.assertEquals(2, parser.getIntValue());
        parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_INT, parser.getCurrentToken());
        Assert.assertEquals(3, parser.getIntValue());
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken()); // End of stream

        // Test nextToken with containers
        parser = new TreeTraversingParser(root);
        parser.nextToken(); // START_ARRAY
        parser.nextToken(); // VALUE_INT (1)
        parser.skipChildren();
        parser.nextToken(); // VALUE_INT (2)
        parser.skipChildren();
        parser.nextToken(); // VALUE_INT (3)
        parser.nextToken(); // END_ARRAY
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken()); // End of stream

        // Test skipChildren on empty container
        parser = new TreeTraversingParser(mapper.createObjectNode().set("items", mapper.createArrayNode()));
        parser.nextToken();
        parser.nextToken();
        parser.skipChildren();
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        // Test skipChildren on non-empty container
        parser = new TreeTraversingParser(mapper.createObjectNode().set("items", mapper.createArrayNode().add(new TextNode("foo"))));
        parser.nextToken();
        parser.nextToken();
        parser.skipChildren();
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testObjectParser() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree("{\"name\": \"John\", \"age\": 30, \"hobbies\": [\"reading\", \"painting\"]}");
        TreeTraversingParser parser = new TreeTraversingParser(root);

        // Test nextToken with no containers
        parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, parser.getCurrentToken());
        parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.getCurrentToken());
        Assert.assertEquals("name", parser.getCurrentName());
        parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.getCurrentToken());
        Assert.assertEquals("John", parser.getText());
        parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.getCurrentToken());
        Assert.assertEquals("age", parser.getCurrentName());
        parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.getCurrentToken());
        Assert.assertEquals(30, parser.getIntValue());
        parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.getCurrentToken());
        Assert.assertEquals("hobbies", parser.getCurrentName());
        parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, parser.getCurrentToken());
        parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.getCurrentToken());
        Assert.assertEquals("reading", parser.getText());
        parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.getCurrentToken());
        Assert.assertEquals("painting", parser.getText());
        parser.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken()); // End of stream

        // Test nextToken with containers
        parser = new TreeTraversingParser(root);
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        Assert.assertEquals("name", parser.getCurrentName());
        parser.nextToken();
        Assert.assertEquals("John", parser.getText());
        parser.nextToken();
        parser.nextToken();
        Assert.assertEquals("age", parser.getCurrentName());
        parser.nextToken();
        Assert.assertEquals(30, parser.getIntValue());
        parser.nextToken();
        parser.nextToken();
        Assert.assertEquals("hobbies", parser.getCurrentName());
        parser.nextToken();
        parser.nextToken();
        Assert.assertEquals("reading", parser.getText());
        parser.nextToken();
        parser.nextToken();
        Assert.assertEquals("painting", parser.getText());
        parser.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken()); // End of stream

        // Test skipChildren on empty container
        parser = new TreeTraversingParser(mapper.createObjectNode().set("hobbies", mapper.createArrayNode()));
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.skipChildren();
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        // Test skipChildren on non-empty container
        parser = new TreeTraversingParser(mapper.createObjectNode().set("hobbies", mapper.createArrayNode().add(new TextNode("reading"))));
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.skipChildren();
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        // Test null value
        parser = new TreeTraversingParser(mapper.createObjectNode().set("name", null));
        parser.nextToken();
        parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.getCurrentToken());
        parser.nextToken();
        Assert.assertEquals(JsonToken.NULL, parser.getCurrentToken());
        parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }
}