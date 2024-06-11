package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonParserSequenceTest {

    @Test
    public void testConstructorWithSingleParser() throws IOException {
        JsonParser parser = new JsonFactory().createParser("{}");
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser });
        assertEquals(parser, sequence.delegate);
        assertArrayEquals(new JsonParser[] { parser }, sequence._parsers);
    }

    @Test
    public void testConstructorWithTwoParsers() throws IOException {
        JsonParser parser1 = new JsonFactory().createParser("{}");
        JsonParser parser2 = new JsonFactory().createParser("[]");
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        assertEquals(parser1, sequence.delegate);
        assertArrayEquals(new JsonParser[] { parser1, parser2 }, sequence._parsers);
    }

    @Test
    public void testNextToken() throws IOException {
        JsonParser parser1 = new JsonFactory().createParser("{\"name\": \"John\"}");
        JsonParser parser2 = new JsonFactory().createParser("{\"age\": 30}");
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());
        assertEquals("name", token.getName());
        token = sequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("John", token.getText());
        sequence.nextToken(); // skip ":"
        token = sequence.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER, token.getId());
        assertEquals(30, token.getNumberValue());
        token = sequence.nextToken();
        assertEquals(JsonToken.END_OBJECT, token.getId());
        assertNull(sequence.nextToken());
        assertNull(sequence.nextToken());
    }

    @Test
    public void testSwitchToNextParser() throws IOException {
        JsonParser parser1 = new JsonFactory().createParser("{\"name\": \"John\"}");
        JsonParser parser2 = new JsonFactory().createParser("{\"age\": 30}");
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());
        assertFalse(sequence.switchToNext()); // no next parser
        assertNull(sequence.nextToken());

        sequence.nextToken(); // consume "{" from parser1

        assertTrue(sequence.switchToNext()); // switch to parser2
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken().getId());
        assertTrue(sequence.switchToNext()); // switch back to parser1
        assertNull(sequence.nextToken()); // consume "}" from parser1
        assertNull(sequence.nextToken()); // consume "{" from parser2
        assertNull(sequence.nextToken()); // consume "age" from parser2
        assertNull(sequence.nextToken()); // consume ":" from parser2
        assertNull(sequence.nextToken()); // consume "30" from parser2
        assertNull(sequence.nextToken()); // consume "}" from parser2
        assertNull(sequence.nextToken()); // consume null from parser1
    }
}