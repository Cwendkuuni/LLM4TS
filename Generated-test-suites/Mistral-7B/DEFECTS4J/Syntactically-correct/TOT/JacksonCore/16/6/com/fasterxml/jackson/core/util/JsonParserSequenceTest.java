package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.*;

public class JsonParserSequenceTest {

    @Test
    public void testJsonParserSequence() throws IOException {
        JsonParser firstParser = createTestParser("{'a': 1}");
        JsonParser secondParser = createTestParser("{'b': 2}");
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);

        // Test that both parsers are being used in sequence
        JsonToken token;
        while ((token = jsonParserSequence.nextToken()) != null) {
            switch (token.getId()) {
                case JsonToken.START_OBJECT:
                    Assert.assertEquals("a", jsonParserSequence.getCurrentName());
                    break;
                case JsonToken.VALUE_NUMBER_INT:
                    Assert.assertEquals(1, jsonParserSequence.getIntValue());
                    break;
                case JsonToken.FIELD_NAME:
                    Assert.assertEquals("b", jsonParserSequence.getCurrentName());
                    break;
                case JsonToken.VALUE_NUMBER_INT:
                    Assert.assertEquals(2, jsonParserSequence.getIntValue());
                    break;
                case JsonToken.END_OBJECT:
                    Assert.assertEquals(JsonToken.END_OBJECT, jsonParserSequence.nextToken());
                    break;
                default:
                    Assert.fail("Unexpected token: " + token);
            }
        }

        // Test that both parsers have been fully consumed
        Assert.assertEquals(JsonToken.END_OF_STREAM, jsonParserSequence.nextToken());

        // Test error handling
        JsonParser errorParser = createTestParser("{'a': [1, 2]}");
        JsonParserSequence errorJsonParserSequence = JsonParserSequence.createFlattened(firstParser, errorParser);
        try {
            errorJsonParserSequence.nextToken();
            Assert.fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            // Expected
        }

        // Test that only active parser is used when there's an error
        JsonParser correctedErrorParser = createTestParser("{'a': 1}"); // Corrected parser
        JsonParserSequence correctedErrorJsonParserSequence = JsonParserSequence.createFlattened(firstParser, errorParser, correctedErrorParser);
        JsonToken correctedToken;
        while ((correctedToken = correctedErrorJsonParserSequence.nextToken()) != null) {
            switch (correctedToken.getId()) {
                case JsonToken.START_OBJECT:
                    Assert.assertEquals("a", correctedErrorJsonParserSequence.getCurrentName());
                    break;
                case JsonToken.VALUE_NUMBER_INT:
                    Assert.assertEquals(1, correctedErrorJsonParserSequence.getIntValue());
                    break;
                case JsonToken.FIELD_NAME:
                    Assert.assertEquals("", correctedErrorJsonParserSequence.getCurrentName()); // Empty name when error parser is skipped
                    break;
                case JsonToken.VALUE_NULL:
                    Assert.assertNull(correctedErrorJsonParserSequence.getText());
                    break;
                case JsonToken.VALUE_STRING:
                    Assert.assertEquals("", correctedErrorJsonParserSequence.getText()); // Empty string when error parser is skipped
                    break;
                case JsonToken.VALUE_TRUE:
                    Assert.assertTrue(correctedErrorJsonParserSequence.isBooleanValue());
                    break;
                case JsonToken.VALUE_FALSE:
                    Assert.assertFalse(correctedErrorJsonParserSequence.isBooleanValue());
                    break;
                case JsonToken.VALUE_NUMBER_FLOAT:
                    Assert.assertEquals(0.0, correctedErrorJsonParserSequence.getDoubleValue(), 0.0);
                    break;
                case JsonToken.VALUE_NULL:
                    Assert.assertNull(correctedErrorJsonParserSequence.getText());
                    break;
                case JsonToken.VALUE_STRING:
                    Assert.assertEquals("", correctedErrorJsonParserSequence.getText());
                    break;
                case JsonToken.VALUE_EMBEDDED_ARRAY:
                    Assert.assertTrue(correctedErrorJsonParserSequence.isArray());
                    break;
                case JsonToken.START_ARRAY:
                    Assert.assertTrue(correctedErrorJsonParserSequence.isStartArray());
                    break;
                case JsonToken.END_ARRAY:
                    Assert.assertTrue(correctedErrorJsonParserSequence.isEndArray());
                    break;
                case JsonToken.START_OBJECT:
                    Assert.assertTrue(correctedErrorJsonParserSequence.isStartObject());
                    break;
                case JsonToken.END_OBJECT:
                    Assert.assertTrue(correctedErrorJsonParserSequence.isEndObject());
                    break;
                case JsonToken.FIELD_NAME:
                    Assert.assertEquals("", correctedErrorJsonParserSequence.getCurrentName()); // Empty name when error parser is skipped
                    break;
                case JsonToken.END_OF_STREAM:
                    Assert.assertEquals(JsonToken.END_OF_STREAM, correctedErrorJsonParserSequence.nextToken());
                    break;
                default:
                    Assert.fail("Unexpected token: " + correctedToken);
            }
        }
    }

    private JsonParser createTestParser(String jsonString) throws IOException {
        return new MapperFactory().getParserFactory().createParser(new ByteArrayInputStream(jsonString.getBytes()), new DefaultPrettyPrinter());
    }

    private ObjectMapper MapperFactory() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    @Test
    public void testEmptyJsonParserSequence() throws IOException {
        JsonParser emptyParser = createTestParser("");
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] {emptyParser});
        Assert.assertEquals(JsonToken.CURRENT_TOKEN, jsonParserSequence.nextToken());
        Assert.assertEquals(JsonToken.END_OF_STREAM, jsonParserSequence.nextToken());
    }

    @Test
    public void testJsonParserSequenceContainingErrorParser() throws IOException {
        JsonParser firstParser = createTestParser("{'a': 1}");
        JsonParser errorParser = createTestParser("{'a': [1, 2]}"); // Contains an error
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(firstParser, errorParser);
        try {
            jsonParserSequence.nextToken();
            Assert.fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            // Expected
        }
    }

    @Test
    public void testJsonParserSequenceWithEmptyStringParser() throws IOException {
        JsonParser firstParser = createTestParser("{'a': 1}");
        JsonParser emptyStringParser = createTestParser("");
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(firstParser, emptyStringParser);
        JsonToken token;
        while ((token = jsonParserSequence.nextToken()) != null) {
            switch (token.getId()) {
                case JsonToken.START_OBJECT:
                    Assert.assertEquals("a", jsonParserSequence.getCurrentName());
                    break;
                case JsonToken.VALUE_NUMBER_INT:
                    Assert.assertEquals(1, jsonParserSequence.getIntValue());
                    break;
                case JsonToken.FIELD_NAME:
                    Assert.assertEquals("", jsonParserSequence.getCurrentName()); // Empty name when empty string parser is hit
                    break;
                case JsonToken.VALUE_NULL:
                    Assert.assertNull(jsonParserSequence.getText());
                    break;
                case JsonToken.VALUE_STRING:
                    Assert.assertEquals("", jsonParserSequence.getText()); // Empty string when empty string parser is hit
                    break;
                case JsonToken.VALUE_TRUE:
                    Assert.assertTrue(jsonParserSequence.isBooleanValue());
                    break;
                case JsonToken.VALUE_FALSE:
                    Assert.assertFalse(jsonParserSequence.isBooleanValue());
                    break;
                case JsonToken.VALUE_NUMBER_FLOAT:
                    Assert.assertEquals(0.0, jsonParserSequence.getDoubleValue(), 0.0);
                    break;
                case JsonToken.VALUE_NULL:
                    Assert.assertNull(jsonParserSequence.getText());
                    break;
                case JsonToken.VALUE_STRING:
                    Assert.assertEquals("", jsonParserSequence.getText());
                    break;
                case JsonToken.VALUE_EMBEDDED_ARRAY:
                    Assert.assertTrue(jsonParserSequence.isArray());
                    break;
                case JsonToken.START_ARRAY:
                    Assert.assertTrue(jsonParserSequence.isStartArray());
                    break;
                case JsonToken.END_ARRAY:
                    Assert.assertTrue(jsonParserSequence.isEndArray());
                    break;
                case JsonToken.START_OBJECT:
                    Assert.assertTrue(jsonParserSequence.isStartObject());
                    break;
                case JsonToken.END_OBJECT:
                    Assert.assertTrue(jsonParserSequence.isEndObject());
                    break;
                case JsonToken.FIELD_NAME:
                    Assert.assertEquals("", jsonParserSequence.getCurrentName()); // Empty name when empty string parser is hit
                    break;
                case JsonToken.END_OF_STREAM:
                    Assert.assertEquals(JsonToken.END_OF_STREAM, jsonParserSequence.nextToken());
                    break;
                default:
                    Assert.fail("Unexpected token: " + token);
            }
        }
        Assert.assertEquals(JsonToken.END_OF_STREAM, jsonParserSequence.nextToken());
    }
}