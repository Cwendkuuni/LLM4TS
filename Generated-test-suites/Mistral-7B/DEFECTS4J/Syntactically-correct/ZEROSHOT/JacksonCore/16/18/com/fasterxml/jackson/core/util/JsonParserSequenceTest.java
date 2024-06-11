package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    @Test
    public void testJsonParserSequenceConstruction() throws IOException {
        JsonParser parser1 = createMockParser("{'key1':'value1'}");
        JsonParser parser2 = createMockParser("{'key2':'value2'}");

        JsonParserSequence parserSequence = JsonParserSequence.createFlattened(parser1, parser2);

        assertEquals(2, parserSequence.containedParsersCount());
        assertEquals(parser1, parserSequence.getDelegate());
    }

    @Test
    public void testJsonParserSequenceNextToken() throws IOException {
        JsonParser parser1 = createMockParser("{'key1':'value1'}");
        JsonParser parser2 = createMockParser("{'key2':'value2'}");

        JsonParserSequence parserSequence = JsonParserSequence.createFlattened(parser1, parser2);

        JsonToken token = parserSequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getClass());

        token = parserSequence.nextToken();
        assertEquals("key1", token.getName());

        token = parserSequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getClass());
        assertEquals("value1", token.getText());

        token = parserSequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getClass());
        assertEquals("key2", token.getName());

        token = parserSequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getClass());
        assertEquals("value2", token.getText());

        token = parserSequence.nextToken();
        assertEquals(JsonToken.END_OBJECT, token.getClass());
    }

    @Test
    public void testJsonParserSequenceSwitchToNextParser() throws IOException {
        JsonParser parser1 = createMockParser("{'key1':'value1'}");
        JsonParser parser2 = createMockParser("{'key2':'value2'}");

        JsonParserSequence parserSequence = JsonParserSequence.createFlattened(parser1, parser2);

        JsonToken token = parserSequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getClass());

        parserSequence.switchToNext();

        token = parserSequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getClass());

        parserSequence.switchToNext();

        token = parserSequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getClass());
        assertEquals("key2", token.getName());

        token = parserSequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getClass());
        assertEquals("value2", token.getText());

        parserSequence.switchToNext();

        token = parserSequence.nextToken();
        assertNull(token);
    }

    @Test
    public void testJsonParserSequenceClose() throws IOException {
        JsonParser parser1 = createMockParser("{'key1':'value1'}");
        JsonParser parser2 = createMockParser("{'key2':'value2'}");

        JsonParserSequence parserSequence = JsonParserSequence.createFlattened(parser1, parser2);

        parserSequence.close();
        parser1.close();
        parser2.close();
    }

    private JsonParser createMockParser(String jsonString) throws IOException {
        return new MockJsonParser(new StringReader(jsonString));
    }
}

class MockJsonParser extends JsonParser {
    public MockJsonParser(Reader reader) {
        super(reader);
    }

    @Override
    public boolean nextBoolean() throws IOException, JsonProcessingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int nextByte() throws IOException, JsonProcessingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean nextValue(Class<?> valueType) throws IOException, JsonProcessingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public char nextChar() throws IOException, JsonProcessingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int nextInt() throws IOException, JsonProcessingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long nextLong() throws IOException, JsonProcessingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public float nextFloat() throws IOException, JsonProcessingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public double nextDouble() throws IOException, JsonProcessingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public JsonToken nextToken() throws IOException, JsonProcessingException {
        return new JsonToken(JsonToken.START_OBJECT);
    }

    @Override
    public String getText() throws IOException, JsonProcessingException {
        return "{}";
    }

    @Override
    public String getText(int len) throws IOException, JsonProcessingException {
        return getText();
    }

    @Override
    public String getValueAsString() throws IOException, JsonProcessingException {
        return getText();
    }

    @Override
    public int getBinaryValueAsInt(int byteOffset, int len) throws IOException, JsonProcessingException {
        throw new UnsupportedOperationException();
    }
}