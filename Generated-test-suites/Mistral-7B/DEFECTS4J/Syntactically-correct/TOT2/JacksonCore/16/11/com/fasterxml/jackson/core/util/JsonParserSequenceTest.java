package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {
    @Test
    public void testNextToken_FirstParserReturnToken() throws IOException, JsonParseException {
        JsonParser firstParser = mock(JsonParser.class); // Assuming you have a mock for JsonParser
        JsonParser secondParser = mock(JsonParser.class);
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });

        JsonToken expectedToken = JsonToken.START_OBJECT; // Replace with expected token
        JsonToken actualToken = jsonParserSequence.nextToken();

        verify(firstParser).nextToken();
        assertEquals(expectedToken, actualToken);
    }

    @Test
    public void testNextToken_FirstParserReturnsNull() throws IOException, JsonParseException {
        JsonParser firstParser = mock(JsonParser.class);
        JsonParser secondParser = mock(JsonParser.class);
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });

        when(firstParser.nextToken()).thenReturn(null);

        JsonToken actualToken = jsonParserSequence.nextToken();

        verify(firstParser).nextToken();
        verify(secondParser).nextToken();
        assertNull(actualToken);
    }

    @Test
    public void testNextToken_SecondParserReturnToken() throws IOException, JsonParseException {
        JsonParser firstParser = mock(JsonParser.class);
        JsonParser secondParser = mock(JsonParser.class);
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });

        JsonToken expectedToken = JsonToken.START_ARRAY; // Replace with expected token
        when(secondParser.nextToken()).thenReturn(expectedToken);

        JsonToken actualToken = jsonParserSequence.nextToken();

        verify(firstParser).nextToken();
        verify(secondParser).nextToken();
        assertEquals(expectedToken, actualToken);
    }

    @Test(expected = IOException.class)
    public void testNextToken_IOExceptionThrownByFirstParser() throws IOException, JsonParseException {
        JsonParser firstParser = mock(JsonParser.class);
        JsonParser secondParser = mock(JsonParser.class);
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });

        when(firstParser.nextToken()).thenThrow(new IOException("First parser error"));

        jsonParserSequence.nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testNextToken_JsonParseExceptionThrownByFirstParser() throws IOException, JsonParseException {
        JsonParser firstParser = mock(JsonParser.class);
        JsonParser secondParser = mock(JsonParser.class);
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });

        when(firstParser.nextToken()).thenThrow(new JsonParseException("First parser exception"));

        jsonParserSequence.nextToken();
    }

    @Test
    public void testNextToken_AllParsersReturnsNull() throws IOException, JsonParseException {
        JsonParser firstParser = mock(JsonParser.class);
        JsonParser secondParser = mock(JsonParser.class);
        JsonParser thirdParser = mock(JsonParser.class);
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser, thirdParser });

        when(firstParser.nextToken()).thenReturn(null);
        when(secondParser.nextToken()).thenReturn(null);
        when(thirdParser.nextToken()).thenReturn(null);

        JsonToken actualToken = jsonParserSequence.nextToken();

        verify(firstParser).nextToken();
        verify(secondParser).nextToken();
        verify(thirdParser).nextToken();
        assertNull(actualToken);
    }

    @Test
    public void testContainedParsersCount() throws IOException, JsonParseException {
        JsonParser firstParser = mock(JsonParser.class);
        JsonParser secondParser = mock(JsonParser.class);
        JsonParser thirdParser = mock(JsonParser.class);
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser, thirdParser });

        int actualCount = jsonParserSequence.containedParsersCount();

        assertEquals(3, actualCount);
    }
}