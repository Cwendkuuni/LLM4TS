package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonParserSequenceTest {

    @Test
    public void testNextToken_FirstParserReturnsToken() throws IOException {
        JsonParser firstParser = new MockJsonParser(JsonToken.START_ARRAY);
        JsonParser secondParser = new MockJsonParser(JsonToken.VALUE_STRING);
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);
        JsonToken actual = jsonParserSequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, actual);
    }

    @Test
    public void testNextToken_SecondParserReturnsToken() throws IOException {
        JsonParser firstParser = new MockJsonParser(JsonToken.START_ARRAY);
        JsonParser secondParser = new MockJsonParser(JsonToken.VALUE_STRING);
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);
        jsonParserSequence.nextToken(); // consume START_ARRAY
        JsonToken actual = jsonParserSequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, actual);
    }

    @Test
    public void testNextToken_MultipleParsersReturnsNull() throws IOException {
        JsonParser firstParser = new MockJsonParser(JsonToken.START_ARRAY);
        JsonParser secondParser = new MockJsonParser(JsonToken.START_OBJECT);
        JsonParser thirdParser = new MockJsonParser(JsonToken.END_OBJECT);
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser, thirdParser);
        jsonParserSequence.nextToken(); // consume START_ARRAY
        jsonParserSequence.nextToken(); // consume START_OBJECT
        jsonParserSequence.nextToken(); // consume KEY_NAME
        jsonParserSequence.nextToken(); // consume VALUE_STRING
        jsonParserSequence.nextToken(); // consume KEY_NAME
        jsonParserSequence.nextToken(); // consume VALUE_STRING
        jsonParserSequence.nextToken(); // consume END_OBJECT
        jsonParserSequence.nextToken(); // consume END_ARRAY
        assertNull(jsonParserSequence.nextToken());
    }

    @Test(expected = IOException.class)
    public void testNextToken_IOExceptionThrown() throws IOException {
        JsonParser firstParser = new MockJsonParserThrowingIOException();
        JsonParser secondParser = new MockJsonParser(JsonToken.START_OBJECT);
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);
        jsonParserSequence.nextToken(); // consume exception from first parser
        jsonParserSequence.nextToken(); // consume START_OBJECT
    }

    @Test(expected = JsonParseException.class)
    public void testNextToken_JsonParseExceptionThrown() throws IOException {
        JsonParser firstParser = new MockJsonParserThrowingJsonParseException();
        JsonParser secondParser = new MockJsonParser(JsonToken.START_OBJECT);
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);
        jsonParserSequence.nextToken(); // consume exception from first parser
    }
}

class MockJsonParser implements JsonParser {
    private JsonToken _token;

    MockJsonParser(JsonToken token) {
        _token = token;
    }

    @Override
    public void close() throws IOException {
        // empty
    }

    @Override
    public JsonToken nextToken() throws IOException, JsonParseException {
        return _token;
    }

    @Override
    public void reset() throws IOException {
        // empty
    }
}

class MockJsonParserThrowingIOException extends MockJsonParser {
    @Override
    public JsonToken nextToken() throws IOException, JsonParseException {
        throw new IOException("Mock IOException");
    }
}

class MockJsonParserThrowingJsonParseException extends MockJsonParser {
    @Override
    public JsonToken nextToken() throws IOException, JsonParseException {
        throw new JsonParseException("Mock JsonParseException");
    }
}