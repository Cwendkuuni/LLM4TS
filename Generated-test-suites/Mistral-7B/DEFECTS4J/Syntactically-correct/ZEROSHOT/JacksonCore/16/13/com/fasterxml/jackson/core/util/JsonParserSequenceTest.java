package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    @Test
    public void testJsonParserSequenceBasic() throws IOException {
        JsonParser firstParser = createMockJsonParser("{\"name\":\"John\"}");
        JsonParser secondParser = createMockJsonParser("{\"age\":30}");
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);

        List<JsonToken> tokens = new ArrayList<>();

        JsonToken token;
        while ((token = jsonParserSequence.nextToken()) != null) {
            tokens.add(token);
        }

        assertEquals(7, tokens.size());
        assertEquals(JsonToken.START_OBJECT, tokens.get(0));
        assertEquals(JsonToken.FIELD_NAME, tokens.get(1));
        assertEquals("name", tokens.get(1).getText().toString());
        assertEquals(JsonToken.VALUE_STRING, tokens.get(2));
        assertEquals("John", tokens.get(2).getText().toString());
        assertEquals(JsonToken.FIELD_NAME, tokens.get(3));
        assertEquals("age", tokens.get(3).getText().toString());
        assertEquals(JsonToken.VALUE_NUMBER_INT, tokens.get(4));
        assertEquals(30, tokens.get(4).getIntValue());
        assertEquals(JsonToken.END_OBJECT, tokens.get(5));
        assertEquals(JsonToken.END_DOCUMENT, tokens.get(6));
    }

    @Test
    public void testJsonParserSequenceSwitchParsers() throws IOException {
        JsonParser firstParser = createMockJsonParser("{\"name\":\"John\"}");
        JsonParser secondParser = createMockJsonParser("{\"age\":30}");
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);

        JsonToken token = jsonParserSequence.nextToken();
        assertNotNull(token);

        jsonParserSequence.close();

        jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);

        token = jsonParserSequence.nextToken();
        assertNotNull(token);

        jsonParserSequence.switchToNext();
        token = jsonParserSequence.nextToken();
        assertNotNull(token);

        jsonParserSequence.switchToNext();
        token = jsonParserSequence.nextToken();
        assertNull(token);
    }

    @Test(expected = IOException.class)
    public void testJsonParserSequenceIOException() throws IOException {
        JsonParser firstParser = createMockJsonParser("{\"name\":\"John\"}");
        JsonParser secondParser = createMockJsonParserThrowingIOException();
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);

        jsonParserSequence.nextToken();
    }

    @Test
    public void testJsonParserSequenceContainedParsersCount() {
        JsonParser firstParser = createMockJsonParser("{\"name\":\"John\"}");
        JsonParser secondParser = createMockJsonParser("{\"age\":30}");
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);

        assertEquals(2, jsonParserSequence.containedParsersCount());
    }

    private JsonParser createMockJsonParser(String jsonString) {
        return mock(JsonParser.class)
                .given(parser -> parser.nextToken())
                .willReturn(parseMockJsonToken(jsonString));
    }

    private JsonParser createMockJsonParserThrowingIOException() {
        return mock(JsonParser.class)
                .given(parser -> parser.nextToken())
                .willThrow(new IOException("Test error"));
    }

    private JsonToken parseMockJsonToken(String jsonString) {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(jsonString);
        return parser.nextToken();
    }
}