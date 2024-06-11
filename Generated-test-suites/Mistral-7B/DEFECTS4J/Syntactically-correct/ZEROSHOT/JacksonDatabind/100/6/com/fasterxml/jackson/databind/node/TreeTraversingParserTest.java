package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for TreeTraversingParser.
 */
public class TreeTraversingParserTest {

    private ObjectMapper mapper;
    private JsonNode jsonNode;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        jsonNode = mapper.readTree("{\"a\":1,\"b\":[2,3],\"c\":{\"d\":4}}");
    }

    @After
    public void teardown() {
        // empty
    }

    @Test
    public void testParseArray() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode.get("b"));
        JsonToken token;

        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_INTEGER, token);
        assertEquals(2, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_INTEGER, token);
        assertEquals(3, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);
    }

    @Test
    public void testParseObject() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        token:
        while (true) {
            JsonToken token = parser.nextToken();
            switch (token) {
            case START_OBJECT:
                parser.nextToken(); // "a"
                assertEquals(JsonToken.VALUE_INTEGER, parser.nextToken());
                assertEquals(1, parser.getIntValue());
                parser.skipChildren();
                break token;
            case START_ARRAY:
                fail("Unexpected array");
            case END_OBJECT:
                fail("Unexpected end of object");
            default:
                parser.close();
                return;
            }
        }

        token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals("a", parser.getCurrentName());
        assertEquals(1, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("c", parser.getCurrentName());

        token = parser.nextToken();
        parser.skipChildren();

        token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals("d", parser.getCurrentName());
        assertEquals(4, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
    }

    @Test(expected = JsonParseException.class)
    public void testParseError() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        parser.nextToken(); // "a"
        parser.nextToken(); // 1

        parser.nextToken(); // expected START_OBJECT but found VALUE_NUMBER_INT
        parser.nextToken(); // VALUE_NUMBER_INT: 1

        parser.nextToken(); // expected FIELD_NAME but found VALUE_NUMBER_INT
        parser.nextToken(); // VALUE_NUMBER_INT: 1

        parser.nextToken(); // expected START_OBJECT but found END_OBJECT
        parser.nextToken(); // END_OBJECT
    }
}