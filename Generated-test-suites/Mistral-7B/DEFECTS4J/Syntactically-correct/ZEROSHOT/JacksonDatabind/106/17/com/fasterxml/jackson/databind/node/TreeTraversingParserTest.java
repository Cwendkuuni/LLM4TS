package com.fasterxml.jackson.databind.node;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectParser;
import com.fasterxml.jackson.databind.node.TextNode;

public class TreeTraversingParserTest {

    private JsonFactory jsonFactory;
    private ObjectMapper objectMapper;
    private JsonParser parser;
    private JsonNode rootNode;

    @Before
    public void setUp() throws Exception {
        jsonFactory = new JsonFactory();
        objectMapper = new ObjectMapper(jsonFactory);
        rootNode = objectMapper.readTree("{\"name\": \"John\", \"age\": 30, \"cities\": [\"New York\", \"Los Angeles\", \"Chicago\"]}");
        parser = new TreeTraversingParser(rootNode);
    }

    @After
    public void tearDown() throws Exception {
        parser.close();
    }

    @Test
    public void testNextToken() throws Exception {
        JsonToken token;
        for (int i = 0; i < 10; i++) {
            token = parser.nextToken();
            assertEquals("Unexpected token: " + token, getExpectedToken(i), token);
        }
    }

    @Test
    public void testNextValue() throws Exception {
        String text;
        int intValue;
        double doubleValue;
        Object value;

        text = parser.nextValue().asText();
        assertEquals("name", text);

        text = parser.nextValue().asText();
        assertEquals("John", text);

        intValue = parser.nextValue().asInt();
        assertEquals(30, intValue);

        doubleValue = parser.nextValue().asDouble();
        assertEquals(30.0, doubleValue, 0.0);

        value = parser.nextValue();
        assertArrayEquals(new String[]{"New York", "Los Angeles", "Chicago"}, ((TextNode[]) value).getTexts());

        parser.skipChildren();
        text = parser.nextValue().asText();
        assertNull(text);
    }

    @Test
    public void testGetText() throws Exception {
        String text = parser.getText();
        assertEquals("name", text);

        text = parser.getText();
        assertEquals("John", text);

        text = parser.getText();
        assertEquals("30", text);

        text = parser.getText();
        assertEquals("New York", text);

        parser.skipChildren();
        text = parser.getText();
        assertNull(text);
    }

    private JsonToken getExpectedToken(int index) {
        JsonToken token;
        switch (index) {
            case 0:
                token = JsonToken.FIELD_NAME;
                break;
            case 1:
                token = JsonToken.VALUE_STRING;
                break;
            case 2:
                token = JsonToken.VALUE_STRING;
                break;
            case 3:
                token = JsonToken.VALUE_NUMBER_INT;
                break;
            case 4:
                token = JsonToken.START_ARRAY;
                break;
            case 5:
                token = JsonToken.VALUE_STRING;
                break;
            case 6:
                token = JsonToken.VALUE_STRING;
                break;
            case 7:
                token = JsonToken.VALUE_STRING;
                break;
            case 8:
                token = JsonToken.END_ARRAY;
                break;
            case 9:
                token = JsonToken.END_OBJECT;
                break;
            default:
                throw new RuntimeException("Invalid index: " + index);
        }
        return token;
    }

    private void assertArrayEquals(Object[] expected, Object[] actual) {
        if (expected.length != actual.length) {
            throw new AssertionError("Array size mismatch. Expected: " + expected.length + ", got: " + actual.length);
        }
        for (int i = 0; i < expected.length; i++) {
            if (!expected[i].equals(actual[i])) {
                throw new AssertionError("Array mismatch at index: " + i + ". Expected: " + expected[i] + ", got: " + actual[i]);
            }
        }
    }
}