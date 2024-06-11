package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    @Test
    public void testJsonParserSequence() throws IOException {
        JsonParser firstParser = createMockParser("{ \"name\": \"John\" }");
        JsonParser secondParser = createMockParser("{ \"age\": 25 }");
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);

        // Test containedParsersCount
        assertEquals(2, jsonParserSequence.containedParsersCount());

        // Test nextToken
        JsonToken token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());

        token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("name", token.getText().toString());

        token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("John", token.getText().toString());

        token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("age", token.getText().toString());

        token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        assertEquals(25, token.getIntValue());

        token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.END_OBJECT, token.getId());

        // Test close
        jsonParserSequence.close();
        jsonParserSequence.nextToken(); // Consume any remaining token
    }

    private JsonParser createMockParser(String jsonString) throws IOException {
        CharArrayBuilder cb = new CharArrayBuilder(128);
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(new ByteArrayInputStream(jsonString.getBytes()));
        parser.setCodec(jsonFactory);
        parser.nextToken();
        return parser;
    }

    @Test(expected = JsonParseException.class)
    public void testJsonParserSequence_EmptyParsers() throws IOException {
        JsonParser[] parsers = new JsonParser[0];
        JsonParserSequence jsonParserSequence = new JsonParserSequence(parsers);
        jsonParserSequence.nextToken();
    }

    @Test(expected = IOException.class)
    public void testJsonParserSequence_NullParsers() throws IOException {
        JsonParser[] parsers = null;
        JsonParserSequence jsonParserSequence = new JsonParserSequence(parsers);
        jsonParserSequence.nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testJsonParserSequence_InvalidParsersType() throws IOException {
        Object[] parsers = new Object[2];
        parsers[0] = new Object();
        parsers[1] = new Object();
        JsonParserSequence jsonParserSequence = new JsonParserSequence((JsonParser[])parsers);
        jsonParserSequence.nextToken();
    }

    @Test(expected = IOException.class)
    public void testJsonParserSequence_InvalidParsersArrayLength() throws IOException {
        JsonParser[] parsers = new JsonParser[1];
        JsonParserSequence jsonParserSequence = new JsonParserSequence(parsers);
        jsonParserSequence.nextToken();
        jsonParserSequence.add(new JsonParser(null));
        jsonParserSequence.nextToken();
    }

    @Test
    public void testJsonParserSequence_AddParser() throws IOException {
        JsonParser firstParser = createMockParser("{ \"name\": \"John\" }");
        JsonParser secondParser = createMockParser("{ \"age\": 25 }");
        JsonParser thirdParser = createMockParser("{ \"city\": \"New York\" }");
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[0]);
        jsonParserSequence.add(firstParser);
        jsonParserSequence.add(secondParser);
        jsonParserSequence.add(thirdParser);

        // Test containedParsersCount
        assertEquals(3, jsonParserSequence.containedParsersCount());

        // Test nextToken
        JsonToken token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());

        token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("name", token.getText().toString());

        token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("John", token.getText().toString());

        token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("age", token.getText().toString());

        token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        assertEquals(25, token.getIntValue());

        token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("city", token.getText().toString());

        token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("New York", token.getText().toString());

        token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.END_OBJECT, token.getId());

        // Test close
        jsonParserSequence.close();
        jsonParserSequence.nextToken(); // Consume any remaining token
    }

    @Test
    public void testJsonParserSequence_SwitchParsers() throws IOException {
        JsonParser firstParser = createMockParser("{ \"name\": \"John\" }");
        JsonParser secondParser = createMockParser("{ \"age\": 25 }");
        JsonParser jsonParserSequence = new JsonParserSequence(new JsonParser[0]);
        jsonParserSequence.add(firstParser);
        jsonParserSequence.add(secondParser);

        // Test nextToken
        JsonToken token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());

        token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("name", token.getText().toString());

        token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("John", token.getText().toString());

        // Switch to second parser
        jsonParserSequence.switchParser();

        token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("age", token.getText().toString());

        token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        assertEquals(25, token.getIntValue());

        // Test containedParsersCount
        assertEquals(2, jsonParserSequence.containedParsersCount());

        // Test switchParser when at end of first parser
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.switchParser();
        token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("age", token.getText().toString());

        // Test switchParser when at end of both parsers
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.switchParser();
        token = jsonParserSequence.nextToken();
        assertNull(token);

        // Test close
        jsonParserSequence.close();
    }

    @Test
    public void testJsonParserSequence_SwitchParsers_EmptyParsers() throws IOException {
        JsonParser jsonParserSequence = new JsonParserSequence(new JsonParser[0]);
        jsonParserSequence.switchParser();
        JsonToken token = jsonParserSequence.nextToken();
        assertNull(token);
    }

    @Test(expected = IllegalStateException.class)
    public void testJsonParserSequence_SwitchParsers_NoParsersLeft() throws IOException {
        JsonParser firstParser = createMockParser("{ \"name\": \"John\" }");
        JsonParser secondParser = createMockParser("{ \"age\": 25 }");
        JsonParser jsonParserSequence = new JsonParserSequence(new JsonParser[]{firstParser, secondParser});
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.switchParser();
    }
}