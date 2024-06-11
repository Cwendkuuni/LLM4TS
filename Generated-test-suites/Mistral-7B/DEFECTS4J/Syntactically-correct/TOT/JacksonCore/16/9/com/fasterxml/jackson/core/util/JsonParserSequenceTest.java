package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonParserSequenceTest {

    private List<JsonParser> jsonParsers;
    private JsonParserSequence jsonParserSequence;

    @Before
    public void setup() throws IOException {
        JsonParser firstParser = new JsonFactory().createParser("{\"name\":\"John\", \"age\":30}");
        JsonParser secondParser = new JsonFactory().createParser("{\"city\":\"New York\", \"country\":\"USA\"}");

        jsonParsers = new ArrayList<>();
        jsonParsers.add(firstParser);
        jsonParsers.add(secondParser);

        jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);
    }

    @Test
    public void testJsonParserSequence_NextToken_ReturnsExpectedTokenType() throws IOException {
        JsonToken actual = jsonParserSequence.nextToken();
        JsonToken expected = JsonParser.CURRENT_TOKEN;

        Assert.assertEquals("Unexpected token type.", expected, actual);

        actual = jsonParserSequence.nextToken();
        expected = JsonToken.START_OBJECT;

        Assert.assertEquals("Unexpected token type.", expected, actual);

        // Testing JSON parsing of the first parser
        actual = jsonParserSequence.nextToken();
        expected = JsonToken.FIELD_NAME;
        String fieldName = "name";
        Assert.assertEquals(expected.getName(), fieldName, actual.getName());

        actual = jsonParserSequence.nextToken();
        expected = JsonToken.VALUE_STRING;
        String value = "John";
        Assert.assertEquals(expected.getText(), value, actual.getText());

        actual = jsonParserSequence.nextToken();
        expected = JsonToken.FIELD_NAME;
        fieldName = "age";
        Assert.assertEquals(expected.getName(), fieldName, actual.getName());

        actual = jsonParserSequence.nextToken();
        expected = JsonToken.VALUE_NUMBER_INT;
        int expectedValue = 30;
        Assert.assertEquals(expected.getIntValue(), expectedValue, actual.getIntValue());

        actual = jsonParserSequence.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, actual.getTypeId(), actual.getTypeId());

        // Testing JSON parsing of the second parser
        actual = jsonParserSequence.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, actual.getTypeId(), actual.getTypeId());
        fieldName = "city";
        Assert.assertEquals(fieldName, actual.getName(), actual.getName());

        actual = jsonParserSequence.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, actual.getTypeId(), actual.getTypeId());
        value = "New York";
        Assert.assertEquals(value, actual.getText(), actual.getText());

        actual = jsonParserSequence.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, actual.getTypeId(), actual.getTypeId());
        fieldName = "country";
        Assert.assertEquals(fieldName, actual.getName(), actual.getName());

        actual = jsonParserSequence.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, actual.getTypeId(), actual.getTypeId());
        value = "USA";
        Assert.assertEquals(value, actual.getText(), actual.getText());

        actual = jsonParserSequence.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, actual.getTypeId(), actual.getTypeId());
    }

    @Test
    public void testJsonParserSequence_NextToken_ReturnsNullWhenEndOfStreamIsReached() throws IOException {
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();

        JsonToken actual = jsonParserSequence.nextToken();
        Assert.assertNull("Expected null.", actual);
    }

    @Test
    public void testJsonParserSequence_ContainsExpectedNumberOfParsers() {
        int expectedParsersCount = jsonParsers.size();

        int actualParsersCount = jsonParserSequence.containedParsersCount();

        Assert.assertEquals("Unexpected number of parsers.", expectedParsersCount, actualParsersCount);
    }
}