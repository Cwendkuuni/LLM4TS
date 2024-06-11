package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonParserSequenceTest {
    @Test
    public void testNextToken_SingleParser() throws IOException {
        JsonParser parser = createMockParser(); // replace with real parser instance
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { parser });
        JsonToken token = parserSequence.nextToken();
        assertEquals(parser.nextToken(), token);
    }

    @Test
    public void testNextToken_MultipleParsers_FirstParserHasToken() throws IOException {
        JsonParser parser1 = createMockParserWithToken(); // replace with real parser instance having a token
        JsonParser parser2 = createMockParser(); // replace with real parser instance
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        JsonToken token = parserSequence.nextToken();
        assertEquals(parser1.nextToken(), token);
    }

    @Test
    public void testNextToken_MultipleParsers_FirstParserDoesntHaveToken() throws IOException {
        JsonParser parser1 = createMockParser(); // replace with real parser instance
        JsonParser parser2 = createMockParserWithToken(); // replace with real parser instance having a token
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        JsonToken token = parserSequence.nextToken();
        assertEquals(parser2.nextToken(), token);
    }

    @Test(expected = IllegalStateException.class)
    public void testNextToken_EmptyParserSequence() throws IOException {
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[0]);
        parserSequence.nextToken();
    }

    @Test(expected = IOException.class)
    public void testNextToken_FirstParserThrowsIOException() throws IOException {
        JsonParser parser1 = createMockParserThrowingIOException(); // replace with real parser instance throwing IOException
        JsonParser parser2 = createMockParser(); // replace with real parser instance
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        parserSequence.nextToken();
        parser1.close();
        parser2.nextToken(); // should not be reached
    }

    // Add edge cases and exception handling scenarios testing
    // containsParsersCount(), switchToNext() and createFlattened() methods here
}