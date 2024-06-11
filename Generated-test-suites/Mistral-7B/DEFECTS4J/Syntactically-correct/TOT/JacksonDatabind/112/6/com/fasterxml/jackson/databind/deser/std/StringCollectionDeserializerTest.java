package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class StringCollectionDeserializerTest {

    @Test
    public void testEmptyCollectionDeserialization() throws JsonProcessingException, IOException {
        String json = "[]";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        ObjectMapper mapper = new ObjectMapper();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        Collection<String> actual = deserializer.deserialize(parser, null);
        Collection<String> expected = new ArrayList<>();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testSingleStringDeserialization() throws JsonProcessingException, IOException {
        String json = "[\"Hello World\"]";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        ObjectMapper mapper = new ObjectMapper();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        Collection<String> actual = deserializer.deserialize(parser, null);
        Collection<String> expected = Arrays.asList("Hello World");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testNullValueDeserialization() throws JsonProcessingException, IOException {
        String json = "[null]";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        ObjectMapper mapper = new ObjectMapper();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        Collection<String> actual = deserializer.deserialize(parser, null);
        Collection<String> expected = Arrays.asList(null);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testMultipleStringsDeserializationWithDefaultValueDeserializer() throws JsonProcessingException, IOException {
        String json = "[\"Apple\", \"Banana\", \"Orange\"]";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        ObjectMapper mapper = new ObjectMapper();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        Collection<String> actual = deserializer.deserialize(parser, null);
        Collection<String> expected = Arrays.asList("Apple", "Banana", "Orange");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testMultipleStringsDeserializationWithCustomValueDeserializer() throws JsonProcessingException, IOException {
        String json = "[{\"value\":\"Apple\"}, {\"value\":\"Banana\"}, {\"value\":\"Orange\"}]";
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> customDeserializer = new StdDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString();
            }
        };
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, customDeserializer, null, null);
        Collection<String> actual = deserializer.deserialize(parser, null);
        Collection<String> expected = Arrays.asList("Apple", "Banana", "Orange");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testJsonFormatFeatureDeserialization() throws JsonProcessingException, IOException {
        String json = "[\"Apple\", \"Banana\", {\"value\":\"Orange\", \"format\":\"raw\"}]";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        JsonDeserializer<String> customDeserializer = new StdDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                if (ctxt.isCurrentName("format")) {
                    JsonToken token = p.nextToken();
                    if (token.isValueIdentifier()) {
                        String formatName = p.getValueAsString();
                        if (formatName.equals("raw")) {
                            return null;
                        }
                    }
                    p.nextToken(JsonToken.VALUE_NULL);
                }
                return p.getValueAsString();
            }
        };
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, customDeserializer, null, null);
        Collection<String> actual = deserializer.deserialize(parser, null);
        Collection<String> expected = Arrays.asList("Apple", null, "Orange");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testBase64EncodedStringsDeserialization() throws JsonProcessingException, IOException {
        String json = "[\"Apple\", \"SGVsbG8gV29ybGQh\", \"Orange\"]";
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDefaultBase64Variants(Base64Variants.STANDARD);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        Collection<String> actual = deserializer.deserialize(parser, null);
        Collection<String> expected = Arrays.asList("Apple", "dGhlIGlzIGh1bWFuLCBidXQgdG8gZXhwIGZvciBmaWxlIGlzIG15IC4NCg==", "Orange");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testExceptionThrownOnInvalidJson() throws JsonProcessingException, IOException {
        String json = "{\"invalid\":\"json\"}";
        ObjectMapper mapper = new ObjectMapper();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        Assertions.assertThrows(JsonMappingException.class, () -> deserializer.deserialize(parser, null));
    }
}