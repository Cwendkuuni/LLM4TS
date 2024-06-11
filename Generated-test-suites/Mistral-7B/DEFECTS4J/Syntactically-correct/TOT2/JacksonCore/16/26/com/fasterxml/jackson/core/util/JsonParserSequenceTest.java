package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    @Test
    public void testNextToken_SingleParser() throws IOException, JsonParseException {
        JsonParser parser1 = createMockParser();
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { parser1 });
        JsonToken token = parserSequence.nextToken();

        Assert.assertEquals(JsonToken.START_OBJECT, token);
        parserSequence.close();
    }

    @Test
    public void testNextToken_TwoParsers() throws IOException, JsonParseException {
        JsonParser parser1 = createMockParser();
        JsonParser parser2 = createMockParser();
        JsonParserSequence parserSequence = JsonParserSequence.createFlattened(parser1, parser2);
        JsonToken token = parserSequence.nextToken();

        Assert.assertEquals(JsonToken.START_ARRAY, token);
        parserSequence.close();
    }

    @Test
    public void testSwitchToNextParser() throws IOException, JsonParseException {
        JsonParser parser1 = createMockParser();
        JsonParser parser2 = createMockParser();
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        JsonToken token1 = parserSequence.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token1);

        parserSequence.switchToNext();

        JsonToken token2 = parserSequence.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token2);

        parserSequence.close();
    }

    @Test(expected = JsonParseException.class)
    public void testNextToken_NoParsersLeft() throws IOException, JsonParseException {
        JsonParser parser1 = createMockParser();
        JsonParser parser2 = createMockParser();
        JsonParser parser3 = createMockParser();
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2, parser3 });

        JsonToken token1 = parserSequence.nextToken();
        JsonToken token2 = parserSequence.nextToken();
        JsonToken token3 = parserSequence.nextToken();

        parserSequence.switchToNext();

        parserSequence.nextToken(); // expected exception
        parserSequence.close();
    }

    private JsonParser createMockParser() {
        // Create a mock parser that returns a known token on nextToken() call
        // for testing purposes
        return mock(JsonParser.class);
    }

    
    // Add any additional tests here
    
}