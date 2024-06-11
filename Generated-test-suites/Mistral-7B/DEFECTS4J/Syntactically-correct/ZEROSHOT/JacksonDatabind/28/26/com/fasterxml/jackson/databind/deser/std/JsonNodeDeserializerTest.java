package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Assert;
import org.junit.Test;

import static com.fasterxml.jackson.core.JsonToken.*;

public class JsonNodeDeserializerTest {

    @Test
    public void testJsonNodeDeserializer() throws IOException {
        JsonParser parser = new ObjectMapper().getFactory().createParser("{\"name\":\"John\", \"age\":30}");
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();

        // Test null value
        JsonNode result = deserializer.deserialize(parser, new DeserializationContext(), null);
        Assert.assertNull(result);

        // Test empty object
        parser.nextToken();
        result = deserializer.deserialize(parser, new DeserializationContext(), null);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isObject());
        Assert.assertEquals(0, result.size());

        // Test deserializing object
        parser.nextToken();
        String fieldName = parser.nextFieldName();
        Assert.assertEquals("name", fieldName);
        parser.nextToken();
        Assert.assertEquals("John", parser.getText());
        fieldName = parser.nextFieldName();
        Assert.assertEquals("age", fieldName);
        parser.nextInt();
        Assert.assertEquals(30, parser.getIntValue());
        result = deserializer.deserialize(parser, new DeserializationContext(), null);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isObject());
        Assert.assertEquals("John", result.get("name").asText());
        Assert.assertEquals(30, result.get("age").asInt());

        // Test deserializing array
        parser.nextToken();
        parser.nextToken();
        JsonNode array = deserializer.deserialize(parser, new DeserializationContext(), null);
        Assert.assertNotNull(array);
        Assert.assertTrue(array.isArray());
        Assert.assertEquals(2, array.size());

        // Test deserializing any
        parser.nextToken();
        Assert.assertEquals(ID_START_OBJECT, parser.getCurrentTokenId());
        result = deserializer.deserialize(parser, new DeserializationContext(), null);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isObject());
        parser.nextToken();
        Assert.assertEquals(ID_NULL, parser.getCurrentTokenId());
        result = deserializer.deserialize(parser, new DeserializationContext(), null);
        Assert.assertNull(result);
        parser.nextToken();
        Assert.assertEquals(ID_TRUE, parser.getCurrentTokenId());
        result = deserializer.deserialize(parser, new DeserializationContext(), null);
        Assert.assertTrue(result.isBoolean());
        Assert.assertEquals(true, result.asBoolean());
        parser.nextToken();
        Assert.assertEquals(ID_START_ARRAY, parser.getCurrentTokenId());
        result = deserializer.deserialize(parser, new DeserializationContext(), null);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isArray());
        Assert.assertEquals(2, result.size());
    }
}