package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testTreeTraversingParser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{'foo': [1, 2.5, 3.14], 'bar': {'baz': 42}}");

        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());

        assertEquals("foo", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());

        assertEquals(1, parser.getIntValue());
        assertEquals(2.5, parser.getDoubleValue(), 0.001);
        assertEquals(3.14, parser.getDoubleValue(), 0.001);

        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertNull(parser.nextToken());

        assertEquals("bar", parser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());

        assertEquals("baz", parser.getCurrentName());
        assertEquals(42, parser.getIntValue());

        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.nextToken());

        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testTreeTraversingParserInvalid() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{'foo': [1, 2.5, 3.14], 'bar': {'baz': 42}}");

        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume START_OBJECT
        parser.nextToken(); // consume "foo"
        parser.nextToken(); // consume START_ARRAY
        parser.nextToken(); // consume INT

        // This should fail as we are not at the end of the array
        parser.nextToken();
    }
}