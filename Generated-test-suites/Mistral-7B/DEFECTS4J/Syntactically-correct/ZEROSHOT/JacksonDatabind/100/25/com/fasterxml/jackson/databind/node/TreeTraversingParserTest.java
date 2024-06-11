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
 * Test class for {@link TreeTraversingParser}.
 */
public class TreeTraversingParserTest {

    private ObjectMapper objectMapper;
    private JsonNode jsonNode;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        jsonNode = objectMapper.readTree("{\"name\":\"John\",\"age\":30,\"cities\":[\"New York\",\"Los Angeles\",\"Chicago\"]}");
    }

    @After
    public void tearDown() throws Exception {
        // Release resources
        jsonNode.close();
    }

    @Test
    public void testTreeTraversingParser_Array() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode.get("cities"));
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        for (int i = 0; i < 3; i++) {
            assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
            String city = parser.getText();
            assertEquals(i + 1, Integer.parseInt(city)); // Convert string to integer for comparison
            assertEquals(city, parser.getText());
            parser.skipChildren();
        }
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testTreeTraversingParser_Object() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        String name = parser.getText();
        assertEquals("John", name);
        parser.skipChildren();
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        int age = parser.getIntValue();
        assertEquals(30, age);
        parser.skipChildren();
        assertEquals("cities", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        // Test array as in the previous test
        parser.close();
    }

    @Test(expected = IllegalStateException.class)
    public void testTreeTraversingParser_Closed() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        parser.close();
        parser.nextToken(); // This should throw an exception
    }

}