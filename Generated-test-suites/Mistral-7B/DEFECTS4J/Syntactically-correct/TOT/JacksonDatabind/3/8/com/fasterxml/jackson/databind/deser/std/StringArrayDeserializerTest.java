package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

public class StringArrayDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeEmptyArray() throws JsonMappingException, JsonProcessingException {
        String json = "[]";
        String[] result = mapper.readValue(new TextNode(json).asText(), String[].class);
        Assertions.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeOneElementArray() throws JsonMappingException, JsonProcessingException {
        String json = "[\"hello\"]";
        String[] result = mapper.readValue(new TextNode(json).asText(), String[].class);
        Assertions.assertArrayEquals(new String[]{"hello"}, result);
    }

    @Test
    public void testDeserializeNullElementArray() throws JsonMappingException, JsonProcessingException {
        String json = "[null]";
        String[] result = mapper.readValue(new TextNode(json).asText(), String[].class);
        Assertions.assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testDeserializeCustomElementDeserializer() throws JsonMappingException, JsonProcessingException {
        String json = "[\"apple\",\"banana\"]";
        JsonDeserializer<String> elementDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getValueAsString().toUpperCase();
            }
        };
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        String[] result = mapper.readValue(new TextNode(json).asText(), String[].class, deserializer);
        Assertions.assertArrayEquals(new String[]{"APPLE", "BANANA"}, result);
    }

    @Test
    public void testDeserializeWithType() throws JsonMappingException, JsonProcessingException {
        String json = "[{\"type\":\"String\",\"value\":\"hello\"},{\"type\":\"String\",\"value\":\"world\"}]";
        Object result = mapper.readValue(new TextNode(json).asText(), String[].class, mapper.getTypeFactory().constructType(String[].class));
        Assertions.assertArrayEquals(new String[]{"hello", "world"}, (String[])result);
    }

    @Test
    public void testDeserializeInvalidJson() throws JsonMappingException, JsonProcessingException {
        String json = "[]]"; // missing starting '['
        Assertions.assertThrows(JsonMappingException.class, () -> mapper.readValue(new TextNode(json).asText(), String[].class));

        json = "[\"hello\",[\"world\""; // missing ']' for inner array
        Assertions.assertThrows(JsonProcessingException.class, () -> mapper.readValue(new TextNode(json).asText(), String[].class));
    }

    @Test
    public void testDeserializeSingleValueAsArray() throws JsonMappingException, JsonProcessingException {
        mapper.disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        String json = "[\"hello\"]";
        Assertions.assertThrows(JsonMappingException.class, () -> mapper.readValue(new TextNode(json).asText(), String[].class));

        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        result = mapper.readValue(new TextNode(json).asText(), String[].class);
        Assertions.assertArrayEquals(new String[]{"hello"}, result);
    }

    @Test
    public void testDeserializeEmptyStringAsNull() throws JsonMappingException, JsonProcessingException {
        mapper.disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        String json = "[\"\"]";
        Assertions.assertArrayEquals(new String[]{""}, mapper.readValue(new TextNode(json).asText(), String[].class));

        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        result = mapper.readValue(new TextNode(json).asText(), String[].class);
        Assertions.assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testContextualDeserialization() throws JsonMappingException, JsonProcessingException {
        String json = "[null, \"apple\", \"banana\"]";
        JsonDeserializer<String> contextualDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                if (ctxt.isCurrentName("fruit")) {
                    return jp.getValueAsString().toUpperCase();
                } else {
                    return jp.getValueAsString();
                }
            }
        };
        StringArrayDeserializer deserializer = new StringArrayDeserializer(contextualDeserializer);
        String[] result = mapper.readValue(new TextNode(json).asText(), String[].class, deserializer);
        Assertions.assertArrayEquals(new String[]{"APPLE", null, "BANANA"}, result);
    }
}