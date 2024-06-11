package com.fasterxml.jackson.databind.node; // imports omitted for brevity

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TreeTraversingParserTest {

    @Test
    public void testBasicParsing() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Check basic functionality
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals("name", parser.getCurrentName());
        Assert.assertEquals("John", parser.nextValue());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("age", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        Assert.assertEquals(30, parser.getIntValue());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        // Close parser
        parser.close();
    }

    @Test(expected = IOException.class)
    public void testClosedParserException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Consume all tokens
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();

        // Verify parser is closed
        Assert.assertTrue(parser.isClosed());

        // Attempt to read next token should throw IOException
        parser.nextToken();
    }

    @Test
    public void testEmptyJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Verify no tokens are present
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        Assert.assertTrue(parser.isClosed());
    }

    @Test
    public void testNullJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(null);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Verify no tokens are present
        Assert.assertTrue(parser.isClosed());
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{name:\"John\", age: 30}"); // missing comma
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Verify parser throws JsonParseException
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();

        // Verify error message contains "Unexpected character"
        Assert.assertEquals("Unexpected character ('a' (code 97)): expected a valid value (number, String, array, object, 'true', 'false' or 'null')", parser.getCurrentLocation().getFormattedMessage());

        // Close parser
        parser.close();
    }
}