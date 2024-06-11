package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Test;

public class JsonParserSequenceTest {
    // Test case for constructor
    @Test
    public void testJsonParserSequenceConstruction() {
        JsonParser parser1 = new JsonFactory().createParser("[]");
        JsonParser parser2 = new JsonFactory().createParser("{}");
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        Assert.assertEquals(parser1.getClass(), jsonParserSequence.getDelegate().getClass());
        Assert.assertEquals(parser2.getClass(), jsonParserSequence._parsers[1].getClass());
        Assert.assertEquals(2, jsonParserSequence.containedParsersCount());
    }

    // Test case for nextToken() method with basic usage
    @Test
    public void testJsonParserSequenceNextTokenBasicUsage() throws IOException {
        JsonParser parser1 = new JsonFactory().createParser("[]");
        JsonParser parser2 = new JsonFactory().createParser("{}");
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(parser1, parser2);
        JsonToken actualToken = jsonParserSequence.nextToken();
        JsonToken expectedToken = JsonToken.START_ARRAY;
        Assert.assertEquals(expectedToken, actualToken);

        actualToken = jsonParserSequence.nextToken();
        expectedToken = JsonToken.END_ARRAY;
        parser1.nextToken(); // consume [
        Assert.assertEquals(expectedToken, actualToken);

        actualToken = jsonParserSequence.nextToken();
        expectedToken = JsonToken.START_OBJECT;
        parser2.nextToken(); // consume {
        Assert.assertEquals(expectedToken, actualToken);

        actualToken = jsonParserSequence.nextToken();
        expectedToken = JsonToken.FIELD_NAME;
        Assert.assertEquals(JsonToken.FIELD_NAME, actualToken);

        actualToken = jsonParserSequence.nextToken();
        expectedToken = JsonToken.VALUE_STRING;
        Assert.assertEquals(expectedToken, actualToken);

        actualToken = jsonParserSequence.nextToken();
        expectedToken = JsonToken.END_OBJECT;
        Assert.assertEquals(expectedToken, actualToken);

        actualToken = jsonParserSequence.nextToken();
        Assert.assertNull(actualToken);
    }

    // Test case for nextToken() method with edge cases and exceptions
    @Test(expected = IOException.class)
    public void testJsonParserSequenceNextTokenEdgeCasesAndExceptions() throws IOException {
        JsonParser parser1 = new JsonFactory().createParser("[]");
        JsonParser parser2 = new JsonFactory().createParser("{\"key\": null}");
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(parser1, parser2);

        jsonParserSequence.nextToken(); // consume [
        jsonParserSequence.nextToken(); // consume ]

        jsonParserSequence.nextToken(); // consume {
        jsonParserSequence.nextToken(); // consume "key"

        jsonParserSequence.nextToken(); // consume :
        jsonParserSequence.nextToken(); // consume null

        jsonParserSequence.nextToken(); // consume }

        jsonParserSequence.nextToken(); // should throw IOException
    }
}