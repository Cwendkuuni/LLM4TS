package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JsonParserSequenceTest {

    private JsonParser firstParser;
    private JsonParser secondParser;
    private JsonParser sequenceParser;

    @Before
    public void setUp() {
        firstParser = Mockito.mock(JsonParser.class);
        secondParser = Mockito.mock(JsonParser.class);
        sequenceParser = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });
    }

    @Test
    public void testCreateFlattened() {
        JsonParser firstSequence = Mockito.spy(new JsonParserSequence(new JsonParser[] { firstParser }));
        JsonParser secondSequence = Mockito.spy(new JsonParserSequence(new JsonParser[] { secondParser }));

        JsonParser expectedParserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });

        JsonParser actualParserSequence = JsonParserSequence.createFlattened(firstSequence, secondSequence);

        assertEquals(expectedParserSequence, actualParserSequence);
    }

    @Test
    public void testContainsParser() {
        JsonParser[] parsers = new JsonParser[] { firstParser, secondParser };
        JsonParser sequenceParser = new JsonParserSequence(parsers);

        assertEquals(2, sequenceParser.containedParsersCount());
    }

    @Test
    public void testClose() throws IOException {
        givenParsersCanParseSequence();

        sequenceParser.close();

        firstParser.close();
        secondParser.close();

        verifyParsersClosedInOrder();
    }

    @Test
    public void testNextTokenNull() throws IOException, JsonParseException {
        givenParsersCanParseNull();

        JsonToken token = sequenceParser.nextToken();

        assertNull(token);
    }

    @Test
    public void testNextTokenFirstParser() throws IOException, JsonParseException {
        givenParsersCanParseSequence();

        JsonToken token = sequenceParser.nextToken();

        assertNotNull(token);
        assertEquals(JsonToken.START_ARRAY, token.getId());
    }

    @Test
    public void testNextTokenSecondParser() throws IOException, JsonParseException {
        givenParsersCanParseSequence();

        JsonToken token = sequenceParser.nextToken();

        assertNotNull(token);
        assertEquals(JsonToken.VALUE_STRING, token.getId());

        token = sequenceParser.nextToken();

        assertNotNull(token);
        assertEquals(JsonToken.END_ARRAY, token.getId());

        JsonToken tokenFromFirstParser = firstParser.nextToken();

        assertNotNull(tokenFromFirstParser);
        assertEquals(JsonToken.VALUE_NUMBER_INT, tokenFromFirstParser.getId());
    }

    @Test
    public void testNextTokenEndOfParsers() throws IOException, JsonParseException {
        givenParsersCanParseSequence();

        JsonToken token = sequenceParser.nextToken();
        JsonToken tokenFromFirstParser = firstParser.nextToken();
        JsonToken tokenFromSecondParser = secondParser.nextToken();

        assertNotNull(token);
        assertEquals(JsonToken.END_ARRAY, token.getId());

        assertFalse(firstParser.hasNext());
        assertFalse(secondParser.hasNext());

        assertNull(sequenceParser.nextToken());
    }

    private void givenParsersCanParseSequence() throws IOException {
        givenParserCanParseStartArray(firstParser);
        givenParserCanParseValueString(secondParser);
        givenParserCanParseEndArray(firstParser);
        givenParserCanParseValueNumberInt(secondParser);
    }

    private void givenParserCanParseStartArray(JsonParser parser) throws IOException {
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
    }

    private void givenParserCanParseValueString(JsonParser parser) throws IOException {
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING).thenReturn(JsonToken.END_VALUE);
    }

    private void givenParserCanParseEndArray(JsonParser parser) throws IOException {
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);
    }

    private void givenParserCanParseValueNumberInt(JsonParser parser) throws IOException {
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT).thenReturn(JsonToken.END_VALUE);
    }

    private void verifyParsersClosedInOrder() throws IOException {
        Mockito.verify(firstParser).close();
        Mockito.verify(secondParser).close();
    }
}