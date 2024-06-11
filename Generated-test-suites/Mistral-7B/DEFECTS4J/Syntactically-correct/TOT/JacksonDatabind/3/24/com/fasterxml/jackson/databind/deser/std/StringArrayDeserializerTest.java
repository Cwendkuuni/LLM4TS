package com.fasterxml.jackson.databind.deser.std.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

import static com.fasterxml.jackson.core.JsonToken.*;

public class StringArrayDeserializerTest {

    @Test
    public void testEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringJsonNode jsonEmpty = mapper.createObjectNode().putPOJOInObject("data", new String[0]);
        String[] result = mapper.treeToValue(jsonEmpty, StringArrayDeserializer.class);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testSingleStringElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringJsonNode jsonSingle = mapper.createObjectNode().putPOJOInObject("data", new ObjectNode(mapper.createObjectNode().put("element", "test")));
        String[] result = mapper.treeToValue(jsonSingle, StringArrayDeserializer.class);
        Assert.assertArrayEquals(new String[]{"test"}, result);
    }

    @Test
    public void testMultipleStringElements() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringJsonNode jsonMultiple = mapper.createObjectNode().putPOJOInObject("data", mapper.createObjectNode().put("elements", mapper.createArrayNode().add("test1").add("test2").add("test3")));
        String[] result = mapper.treeToValue(jsonMultiple, StringArrayDeserializer.class);
        Assert.assertArrayEquals(new String[]{"test1", "test2", "test3"}, result);
    }

    @Test(expected = IOException.class)
    public void testNullToken() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.parser().readValueAsTree("null");
        jp.nextToken(); // consume START_OBJECT
        jp.nextToken(); // consume "data" field name
        jp.nextToken(); // consume NULL token
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.deserialize(jp, null);
    }

    @Test(expected = IOException.class)
    public void testNonArrayToken() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.parser().readValueAsTree("{\"data\": \"test\"}");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.deserialize(jp, null);
    }

    @Test(expected = IOException.class)
    public void testInvalidStringElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringJsonNode jsonInvalid = mapper.createObjectNode().putPOJOInObject("data", mapper.createArrayNode().add("test").add(JsonToken.VALUE_NULL).add("test2"));
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.deserialize(mapper.treeToToken(jsonInvalid), null);
    }

    @Test
    public void testCustomDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectCodec codec = mapper.getCodec();
        JsonDeserializer<String> customDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getText() + "Custom";
            }
        };
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        StringJsonNode jsonCustom = mapper.createObjectNode().putPOJOInObject("data", mapper.createArrayNode().add("test"));
        String[] result = mapper.treeToValue(jsonCustom, deserializer);
        Assert.assertArrayEquals(new String[]{"testCustom"}, result);
    }

    @Test
    public void testContextualization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringJsonNode jsonContextual = mapper.createObjectNode().putPOJOInObject("data", mapper.createObjectNode().put("elements", mapper.createArrayNode().add(JsonToken.VALUE_STRING.name()).add(JsonToken.VALUE_NULL)));
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonDeserializer<String> contextualDeserializer = mapper.findValueDeserializer(JsonToken.VALUE_STRING, String.class);
        deserializer = new StringArrayDeserializer(contextualDeserializer);
        String[] result = mapper.treeToValue(jsonContextual, deserializer);
        Assert.assertArrayEquals(new String[]{"test", null}, result);
    }

    @Test
    public void testTypeDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringJsonNode jsonType = mapper.createObjectNode().putPOJOInObject("data", mapper.createArrayNode().add(1).add(2).add(3));
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(String[].class);
        String[] result = mapper.treeToValue(jsonType, typeDeserializer);
        Assert.assertArrayEquals(new String[]{"1", "2", "3"}, result);
    }

    @Test
    public void testEmptyStringAsNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringJsonNode jsonEmptyStringAsNull = mapper.createObjectNode().putPOJOInObject("data", mapper.createArrayNode().add(""));
        String[] result = mapper.treeToValue(jsonEmptyStringAsNull, StringArrayDeserializer.class);
        Assert.assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testInlineDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectCodec codec = mapper.getCodec();
        StringJsonNode jsonInline = mapper.createObjectNode().putPOJOInObject("data", mapper.createArrayNode().add("test1").add("test2").add("test3"));
        String[] result = mapper.treeToValue(jsonInline, String[].class);
        Assert.assertArrayEquals(new String[]{"test1", "test2", "test3"}, result);
    }
}