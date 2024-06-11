package com.fasterxml.jackson.databind.node; // import statements go here

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.AbstractJsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testBasicFunctionality_EmptyJson() throws IOException {
        JsonParser parser = new TreeTraversingParser(null);
        assertNull(parser.nextToken()); // Check for EOF
        parser.close();
    }

    @Test
    public void testBasicFunctionality_StringValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"name\": \"John\"}");
        JsonParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals("John", parser.getText());
        parser.nextToken(); // Skip value
        parser.nextToken(); // Skip end object
        parser.close();
    }

    @Test
    public void testBasicFunctionality_NumberValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"number\": 123.45}");
        JsonParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("number", parser.getCurrentName());
        assertEquals("123.45", parser.getText());
        parser.nextToken(); // Skip value
        parser.nextToken(); // Skip end object
        parser.close();
    }

    @Test
    public void testBasicFunctionality_ArrayValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[1, 2, 3]");
        JsonParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        parser.nextToken(); // Skip first value

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        parser.nextToken(); // Skip second value

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        parser.nextToken(); // Skip third value

        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.nextToken(); // Skip end array
        parser.close();
    }

    @Test
    public void testEdgeCases_EmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[]");
        JsonParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.nextToken(); // Skip end array
        parser.close();
    }

    @Test
    public void testEdgeCases_EmptyObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{}");
        JsonParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.nextToken(); // Skip end object
        parser.close();
    }

    @Test
    public void testEdgeCases_NullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"value\": null}");
        JsonParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("value", parser.getCurrentName());
        assertNull(parser.getText());
        parser.nextToken(); // Skip null value
        parser.nextToken(); // Skip end object
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testExceptionHandling_InvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = new TreeTraversingParser(mapper.readTree("invalidJson"));
        parser.nextToken(); // Should throw JsonParseException
    }

    @Test
    public void testExceptionHandling_ClosedParser() throws IOException {
        JsonParser parser = new TreeTraversingParser(null);
        parser.nextToken(); // Open parser
        parser.close(); // Close parser
        parser.nextToken(); // Should throw IOException
    }

    @Test
    public void testExceptionHandling_NotClosedParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"name\": \"John\"}");
        JsonParser parser = new TreeTraversingParser(jsonNode);
        parser.close(); // Close parser manually

        assertEquals(JsonToken.NULL, parser.nextToken()); // Should return null token
        parser.nextToken(); // Should throw IOException
    }
}