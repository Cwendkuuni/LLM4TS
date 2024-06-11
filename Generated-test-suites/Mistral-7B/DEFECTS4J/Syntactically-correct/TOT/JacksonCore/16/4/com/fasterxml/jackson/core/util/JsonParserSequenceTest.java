package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Test class for {@link JsonParserSequence} class. This class tests the basic functionality, edge cases, and error handling of the JsonParserSequence class.
 */
public class JsonParserSequenceTest {

    private JsonParser[] parsers;
    private JsonParserSequence parserSequence;

    @Before
    public void setup() {
        JsonParser firstParser = new JsonFactory(null).createParser(new ByteArrayInputStream(new byte[]{}));
        JsonParser secondParser = new JsonFactory(null).createParser(new ByteArrayInputStream(new byte[]{'{', 'a', ':', '1', '}'}));
        parsers = new JsonParser[]{firstParser, secondParser};
        parserSequence = JsonParserSequence.createFlattened(parsers[0], parsers[1]);
    }

    /**
     * Test for {@link JsonParserSequence#nextToken()} method with valid input from first parser.
     */
    @Test
    public void testNextTokenFromFirstParser() throws IOException, JsonProcessingException {
        JsonToken token = parserSequence.nextToken();
        Assert.assertNull(token);

        parserSequence.nextToken();
        token = parserSequence.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token.getId());

        parserSequence.nextToken();
        token = parserSequence.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NULL, token.getId());

        parserSequence.nextToken();
        token = parserSequence.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token.getId());

        parserSequence.nextToken();
        token = parserSequence.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token.getId());
    }

    /**
     * Test for {@link JsonParserSequence#nextToken()} method with valid input from second parser.
     */
    @Test
    public void testNextTokenFromSecondParser() throws IOException, JsonProcessingException {
        parserSequence.nextToken();
        parserSequence.nextToken();
        parserSequence.nextToken();
        parserSequence.nextToken();

        JsonToken token = parserSequence.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token.getId());

        token = parserSequence.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token.getId());
        Assert.assertEquals("a", token.getText().toString());

        token = parserSequence.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        Assert.assertEquals(1, token.getValueAsInt());

        token = parserSequence.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token.getId());

        token = parserSequence.nextToken();
        Assert.assertNull(token);
    }

    /**
     * Test for {@link JsonParserSequence#nextToken()} method with empty input from both parsers.
     */
    @Test
    public void testNextTokenFromEmptyParsers() throws IOException, JsonProcessingException {
        parserSequence.nextToken();
        parserSequence.nextToken();
        parserSequence.nextToken();
        parserSequence.nextToken();

        parserSequence.nextToken();
        JsonToken token = parserSequence.nextToken();
        Assert.assertNull(token);

        parserSequence.nextToken();
        token = parserSequence.nextToken();
        Assert.assertNull(token);

        parserSequence.nextToken();
        token = parserSequence.nextToken();
        Assert.assertNull(token);

        parserSequence.nextToken();
        token = parserSequence.nextToken();
        Assert.assertNull(token);
    }

    /**
     * Test for {@link JsonParserSequence#nextToken()} method with invalid input from both parsers.
     */
    @Test(expected = JsonParseException.class)
    public void testNextTokenFromInvalidParsers() throws IOException, JsonProcessingException {
        parserSequence.nextToken();
        parserSequence.nextToken();
        parserSequence.nextToken();
        parserSequence.nextToken();

        parserSequence.nextToken();
        parserSequence.nextToken(); // consume first invalid token

        parserSequence.nextToken(); // consume second invalid token

        parserSequence.nextToken();
        parserSequence.nextToken();

        JsonToken token = parserSequence.nextToken();
        Assert.assertNull(token);

        parserSequence.nextToken();
        token = parserSequence.nextToken();
        Assert.assertNull(token);

        parserSequence.nextToken();
        token = parserSequence.nextToken();
        Assert.assertNull(token);

        parserSequence.nextToken();
        parserSequence.nextToken();

        ObjectMapper mapper = new ObjectMapper();
        mapper.readValue(new ByteArrayInputStream(new byte[]{'[', 'a', ']'}.concat(new byte[]{'\u0000'})), Object.class); // throwing exception
    }
}