package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testParsingEmptyJsonObject() throws JsonProcessingException {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode("{}"));
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);
        parser.close();
    }

    @Test
    public void testParsingEmptyJsonArray() throws JsonProcessingException {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode("[]"));
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token);
        parser.close();
    }

    @Test(expected = JsonProcessingException.class)
    public void testParsingInvalidJson() throws JsonProcessingException {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode("{"));
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);
        parser.close();

        parser = new TreeTraversingParser(createJsonNode("{a:"));
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        parser.nextToken(); // should be VALUE_STRING or VALUE_NUMBER
        parser.close();

        parser = new TreeTraversingParser(createJsonNode("[1,]"));
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);
        parser.nextToken(); // should be VALUE_NUMBER
        parser.nextToken(); // should be END_ARRAY
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token);
        parser.close();
    }

    @Test
    public void testParsingJsonObject() throws JsonProcessingException {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode("{\"a\":1,\"b\":[1,2,3]}"));
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("a", token.asString());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        Assert.assertEquals(1, token.asInt());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("b", token.asString());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        Assert.assertEquals(1, token.asInt());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        Assert.assertEquals(2, token.asInt());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        Assert.assertEquals(3, token.asInt());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token);

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);

        parser.close();
    }

    @Test
    public void testParsingJsonArray() throws JsonProcessingException {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode("[1,2,\"3\",null]"));
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        Assert.assertEquals(1, token.asInt());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        Assert.assertEquals(2, token.asInt());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        Assert.assertEquals("3", token.asString());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NULL, token);

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token);

        parser.close();
    }

    @Test
    public void testParsingJsonObjectWithBinaryData() throws JsonProcessingException {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode("{\"binaryData\":{\"a\":[0x01,0x02,0x03]}}"));
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("binaryData", token.asString());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("a", token.asString());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_BINARY_DATA, token);
        byte[] binaryData = token.asBinary();
        Assert.assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, binaryData);

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token);

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);

        parser.close();
    }

    private JsonNode createJsonNode(String jsonString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(jsonString);
    }
}