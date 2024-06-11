package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.core.util.JsonParserSequence;

public class JsonParserSequenceTest {

    private JsonParser firstParser;
    private JsonParser secondParser;
    private JsonParser thirdParser;
    private JsonParser sequenceParser;

    @Before
    public void setup() {
        firstParser = mock(JsonParser.class);
        secondParser = mock(JsonParser.class);
        thirdParser = mock(JsonParser.class);

        ParserMinimalBase emptyDelegate = mock(ParserMinimalBase.class);
        when(firstParser.getDelegate()).thenReturn(emptyDelegate);
        when(secondParser.getDelegate()).thenReturn(emptyDelegate);
        when(thirdParser.getDelegate()).thenReturn(emptyDelegate);

        sequenceParser = JsonParserSequence.createFlattened(firstParser, secondParser, thirdParser);
    }

    @Test
    public void testContainedParsersCount() {
        int expectedParsersCount = 3;
        assertEquals(expectedParsersCount, sequenceParser.containedParsersCount());
    }

    @Test
    public void testClose() throws IOException {
        sequenceParser.close();

        verify(firstParser).close();
        verify(secondParser).close();
        verify(thirdParser).close();
    }

    @Test
    public void testSwitchToNextWhenAllParsersClosed() throws IOException {
        sequenceParser.close();

        assertNull(sequenceParser.nextToken());

        assertFalse(sequenceParser.switchToNext());
    }

    @Test
    public void testSwitchToNextWhenFirstParserHasToken() throws IOException {
        when(firstParser.nextToken()).thenReturn(JsonToken.START_OBJECT);

        JsonToken token = sequenceParser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        assertTrue(sequenceParser.switchToNext());
    }

    @Test
    public void testSwitchToNextWhenFirstParserDoesNotHaveToken() throws IOException {
        when(firstParser.nextToken()).thenReturn(null);

        JsonToken token = sequenceParser.nextToken();
        assertNull(token);

        assertFalse(sequenceParser.switchToNext());
    }

    @Test
    public void testSwitchToNextWhenSecondParserHasToken() throws IOException {
        when(firstParser.nextToken()).thenReturn(JsonToken.INVALID_TOKEN);
        when(secondParser.nextToken()).thenReturn(JsonToken.START_ARRAY);

        JsonToken token = sequenceParser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        assertTrue(sequenceParser.switchToNext());
    }

    @Test
    public void testSwitchToNextWhenSecondParserDoesNotHaveToken() throws IOException {
        when(firstParser.nextToken()).thenReturn(JsonToken.INVALID_TOKEN);
        when(secondParser.nextToken()).thenReturn(null);

        JsonToken token = sequenceParser.nextToken();
        assertNull(token);

        assertFalse(sequenceParser.switchToNext());
    }

    @Test
    public void testSwitchToNextWhenThirdParserHasToken() throws IOException {
        when(firstParser.nextToken()).thenReturn(JsonToken.INVALID_TOKEN);
        when(secondParser.nextToken()).thenReturn(JsonToken.INVALID_TOKEN);
        when(thirdParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        JsonToken token = sequenceParser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        assertTrue(sequenceParser.switchToNext());
    }

    @Test
    public void testSwitchToNextWhenThirdParserDoesNotHaveToken() throws IOException {
        when(firstParser.nextToken()).thenReturn(JsonToken.INVALID_TOKEN);
        when(secondParser.nextToken()).thenReturn(JsonToken.INVALID_TOKEN);
        when(thirdParser.nextToken()).thenReturn(null);

        JsonToken token = sequenceParser.nextToken();
        assertNull(token);

        assertFalse(sequenceParser.switchToNext());
    }
}