package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    @Test
    public void testJsonParserSequence() throws IOException {
        JsonParser parser1 = new JsonFactory().createParser("{\"a\":1}");
        JsonParser parser2 = new JsonFactory().createParser("{\"b\":2}");
        JsonParser parser3 = new JsonFactory().createParser("{\"c\":3}");
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2, parser3);

        // Test basic functionality
        JsonToken token = sequence.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);
        token = sequence.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("a", sequence.getCurrentName());
        token = sequence.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        Assert.assertEquals(1, sequence.getIntValue());

        token = sequence.nextToken(); // should be null, as there's no more tokens in parser1
        Assert.assertNull(token);

        token = sequence.nextToken(); // should get the first token from parser2
        Assert.assertEquals(JsonToken.START_OBJECT, token);

        token = sequence.nextToken(); // should get the first token from parser3, as parser2 has no more tokens
        Assert.assertEquals(JsonToken.START_OBJECT, token);

        // Test error scenarios
        try {
            sequence.nextToken(); // should throw an IOException when there's no more tokens in all parsers
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }
}