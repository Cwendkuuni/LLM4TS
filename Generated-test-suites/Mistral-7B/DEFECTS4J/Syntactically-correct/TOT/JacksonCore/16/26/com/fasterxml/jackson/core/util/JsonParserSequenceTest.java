package com.fasterxml.jackson.core.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.*;
import com.fasterxml.jackson.core.json.*;
import com.fasterxml.jackson.core.util.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Test cases for {@link JsonParserSequence} class.
 *
 * @author John Doe
 */
public class JsonParserSequenceTest
{
    private List<JsonParser> _parsers;
    private JsonParser _parser1;
    private JsonParser _parser2;
    private JsonParser _parser3;
    private JsonParser _parserSequence;

    @Before
    public void setUp() throws IOException
    {
        _parser1 = new JsonFactory().createParser(getClass().getResourceAsStream("JsonParserSequenceTest.json"));
        _parser2 = new JsonFactory().createParser(getClass().getResourceAsStream("JsonParserSequenceTest2.json"));
        _parser3 = new JsonFactory().createParser(getClass().getResourceAsStream("JsonParserSequenceTest3.json"));
        _parsers = new ArrayList<>();
        _parsers.add(_parser1);
        _parsers.add(_parser2);
        _parsers.add(_parser3);
        _parserSequence = JsonParserSequence.createFlattened(_parser1, _parser2, _parser3);
    }

    @Test
    public void testContainedParsersCount()
    {
        int parsersCount = _parserSequence.containedParsersCount();
        assertEquals(3, parsersCount);
    }

    @Test
    public void testClose() throws IOException
    {
        _parserSequence.close();
        for (JsonParser parser : _parsers) {
            parser.close();
        }
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException
    {
        JsonToken token = _parserSequence.nextToken();
        assertNotNull(token);
        JsonToken expectedToken = _parser1.nextToken();
        assertEquals(expectedToken.getClass(), token.getClass());
        assertEquals(expectedToken.getValue(), token.getValue());
        assertEquals(expectedToken.getName(), token.getName());

        token = _parserSequence.nextToken();
        assertNotNull(token);
        expectedToken = _parser2.nextToken();
        assertEquals(expectedToken.getClass(), token.getClass());
        assertEquals(expectedToken.getValue(), token.getValue());
        assertEquals(expectedToken.getName(), token.getName());

        token = _parserSequence.nextToken();
        assertNotNull(token);
        expectedToken = _parser3.nextToken();
        assertEquals(expectedToken.getClass(), token.getClass());
        assertEquals(expectedToken.getValue(), token.getValue());
        assertEquals(expectedToken.getName(), token.getName());

        token = _parserSequence.nextToken();
        assertNull(token);
    }
}