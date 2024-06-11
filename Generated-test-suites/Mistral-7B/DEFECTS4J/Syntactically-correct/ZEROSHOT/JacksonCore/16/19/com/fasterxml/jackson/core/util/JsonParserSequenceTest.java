package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.Base64Varint;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;

public class JsonParserSequenceTest {

    private JsonParser firstParser;
    private JsonParser secondParser;
    private JsonParser sequenceParser;

    @Before
    public void setUp() throws IOException {
        firstParser = new JsonFactory().createParser(getClass().getResourceAsStream("/JsonParserSequenceTest1.json"));
        secondParser = new JsonFactory().createParser(getClass().getResourceAsStream("/JsonParserSequenceTest2.json"));
        sequenceParser = JsonParserSequence.createFlattened(firstParser, secondParser);
    }

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser[] parsers = new JsonParser[2];
        parsers[0] = firstParser;
        parsers[1] = secondParser;

        JsonParser sequence = new JsonParserSequence(parsers);
        assertEquals(sequenceParser, sequence);
    }

    @Test
    public void testContainedParsersCount() throws IOException {
        assertEquals(2, sequenceParser.containedParsersCount());
    }

    @Test
    public void testClose() throws IOException {
        sequenceParser.close();
    }

    @Test
    public void testNextTokenWhenFirstParserHasToken() throws IOException {
        JsonToken token = sequenceParser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
    }

    @Test
    public void testNextTokenWhenFirstParserDoesntHaveToken() throws IOException {
        sequenceParser.nextToken();

        JsonToken token = sequenceParser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
    }

    @Test
    public void testNextTokenWhenBothParsersDontHaveToken() throws IOException {
        sequenceParser.nextToken();
        sequenceParser.nextToken();

        JsonToken token = sequenceParser.nextToken();
        assertNull(token);
    }

    @Test
    public void testSwitchToNextParserWhenFirstParserHasToken() throws IOException {
        sequenceParser.nextToken();

        assertTrue(sequenceParser.switchToNext());

        JsonToken token = sequenceParser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
    }

    @Test
    public void testSwitchToNextParserWhenFirstParserDoesntHaveToken() throws IOException {
        sequenceParser.nextToken();
        sequenceParser.nextToken();

        assertFalse(sequenceParser.switchToNext());
    }

    @Test
    public void testSwitchToNextParserWhenBothParsersDontHaveToken() throws IOException {
        sequenceParser.nextToken();
        sequenceParser.nextToken();
        sequenceParser.nextToken();

        assertFalse(sequenceParser.switchToNext());
    }

    @Test
    public void testSwitchToNextParserWhenFirstParserHasMultipleTokens() throws IOException {
        sequenceParser.nextToken();
        sequenceParser.nextToken();

        assertTrue(sequenceParser.switchToNext());

        JsonToken token = sequenceParser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
    }

    @Test
    public void testIsCurrentParserFirstParser() throws IOException {
        assertTrue(sequenceParser.isCurrentParserFirst());

        sequenceParser.switchToNext();

        assertFalse(sequenceParser.isCurrentParserFirst());
    }

    @Test
    public void testIsCurrentParserLastParser() throws IOException {
        assertFalse(sequenceParser.isCurrentParserLast());

        sequenceParser.switchToNext();

        assertFalse(sequenceParser.isCurrentParserLast());

        sequenceParser.close();

        assertFalse(sequenceParser.isCurrentParserLast());
    }

    @Test
    public void testReadValueAsArray() throws IOException {
        JsonNode expected = JsonNodeFactory.instance.readValue(getClass().getResourceAsStream("/JsonParserSequenceTest3.json"), JsonNode.class);
        JsonNode actual = sequenceParser.readValueAsTree();
        assertEquals(expected, actual);
    }

    @Test
    public void testReadValueAsString() throws IOException {
        String expected = "test";
        String actual = sequenceParser.readValueAsString();
        assertEquals(expected, actual);
    }

    @Test
    public void testReadValueAsInt() throws IOException {
        int expected = 42;
        int actual = sequenceParser.readValueAsInt();
        assertEquals(expected, actual);
    }

    @Test
    public void testReadValueAsBase64Varint() throws IOException {
        Base64Varint expected = new Base64Varint("test");
        Base64Varint actual = sequenceParser.readValueAs(Base64Varint.class);
        assertEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testReadValueAsBoolean() throws IOException {
        sequenceParser.nextToken();
        sequenceParser.nextToken();

        sequenceParser.readValueAsBoolean();
    }

    @Test(expected = IOException.class)
    public void testReadValueAsLong() throws IOException {
        sequenceParser.nextToken();
        sequenceParser.nextToken();

        sequenceParser.readValueAsLong();
    }

    @Test(expected = IOException.class)
    public void testReadValueAsDouble() throws IOException {
        sequenceParser.nextToken();
        sequenceParser.nextToken();

        sequenceParser.readValueAsDouble();
    }

    @Test(expected = IOException.class)
    public void testReadValueAsFloat() throws IOException {
        sequenceParser.nextToken();
        sequenceParser.nextToken();

        sequenceParser.readValueAsFloat();
    }

    @Test(expected = IOException.class)
    public void testReadValueAsChar() throws IOException {
        sequenceParser.nextToken();
        sequenceParser.nextToken();

        sequenceParser.readValueAsChar();
    }

    @Test
    public void testReadValueAsByteArray() throws IOException {
        byte[] expected = new byte[] { 1, 2, 3 };
        byte[] actual = sequenceParser.readValueAsBinary();
        assertArrayEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testReadValueAsJsonNode() throws IOException {
        sequenceParser.nextToken();
        sequenceParser.nextToken();
        sequenceParser.nextToken();

        sequenceParser.readValueAsTree();
    }

    @Test(expected = IOException.class)
    public void testReadValueAsObject() throws IOException {
        sequenceParser.nextToken();
        sequenceParser.nextToken();
        sequenceParser.nextToken();

        sequenceParser.readValueAs(String.class);
    }

    @Test(expected = IOException.class)
    public void testReadValueAsArrayWithGenericType() throws IOException {
        sequenceParser.nextToken();
        sequenceParser.nextToken();
        sequenceParser.nextToken();

        sequenceParser.readValueAs(int[].class);
    }

    @Test(expected = IOException.class)
    public void testReadValueAsArrayWithWildcardType() throws IOException {
        sequenceParser.nextToken();
        sequenceParser.nextToken();
        sequenceParser.nextToken();

        sequenceParser.readValueAs(List.class);
    }

    @Test
    public void testReadValueAsArrayWithKnownType() throws IOException {
        JsonNode expected = JsonNodeFactory.instance.textNode("test");
        JsonNode actual = sequenceParser.readValueAsTree();
        assertEquals(expected, actual);

        Object[] actualArray = sequenceParser.readValueAs(Object[].class);
        assertArrayEquals(new Object[] { actual }, actualArray);
    }

    @Test
    public void testReadValueAsArrayWithKnownTypeCommonSuperclass() throws IOException {
        JsonNode expected = JsonNodeFactory.instance.textNode("test");
        JsonNode actual = sequenceParser.readValueAsTree();
        assertEquals(expected, actual);

        Number[] actualArray = sequenceParser.readValueAs(Number[].class);
        assertArrayEquals(new Number[] { new Double(1.0) }, actualArray);
    }

    @Test(expected = IOException.class)
    public void testReadValueAsArrayWithUnknownType() throws IOException {
        sequenceParser.nextToken();
        sequenceParser.nextToken();
        sequenceParser.nextToken();

        sequenceParser.readValueAs(String[].class);
    }
}