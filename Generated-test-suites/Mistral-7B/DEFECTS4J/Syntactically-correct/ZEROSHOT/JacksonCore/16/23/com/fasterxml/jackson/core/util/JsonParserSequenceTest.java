package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    @Test
    public void testJsonParserSequence() throws IOException, JsonProcessingException {
        JsonParser parser1 = createMockParser("{ \"name\": \"John\" }");
        JsonParser parser2 = createMockParser("{ \"age\": 30 }");

        JsonParserSequence parserSequence = JsonParserSequence.createFlattened(parser1, parser2);

        // Test containedParsersCount()
        assertEquals(2, parserSequence.containedParsersCount());

        // Test nextToken()
        JsonToken token = parserSequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());
        assertNull(token.getValue());

        token = parserSequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("name", token.getValue());

        token = parserSequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("John", token.getValue());

        token = parserSequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("age", token.getValue());

        token = parserSequence.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        assertEquals(30, token.getIntValue());

        // Test nextToken() after all tokens have been consumed
        token = parserSequence.nextToken();
        assertNull(token);

        // Test close()
        parserSequence.close();
        parser1.close();
        parser2.close();
    }

    private JsonParser createMockParser(String jsonString) throws IOException {
        return mock(JsonParser.class).stubs().method("nextToken").andReturn(parseMockToken(jsonString));
    }

    private JsonToken parseMockToken(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(jsonString).nextToken();
    }
}