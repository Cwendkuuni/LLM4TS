package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.*;

public class StringCollectionDeserializerTest {

    @Test
    public void testEmptyCollection() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[]");
        ContextualDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        Collection<String> collection = deserializer.deserialize(mapper.parser(), mapper.getDeserializationContext());
        assertTrue(collection.isEmpty());
    }

    @Test
    public void testSingleElementCollection() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("name", "element");
        Collection<String> collection = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null).deserialize(mapper.parser(), mapper.getDeserializationContext(), new ArrayList<>());
        collection.add("element");
        assertEquals(1, collection.size());
        assertEquals("element", collection.get(0));
    }

    @Test
    public void testNullElementInCollection() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.parser();
        parser.nextToken();
        JsonToken currentToken = parser.getCurrentToken();
        parser.nextToken(); // skip current token
        parser.nextValue();
        parser.nextNull();
        ContextualDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        Collection<String> collection = deserializer.deserialize(parser, mapper.getDeserializationContext(), new ArrayList<>());
        assertTrue(collection.contains(null));
    }

    @Test
    public void testNullValueProvider() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ContextualDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        Collection<String> collection = deserializer.deserialize(mapper.parser(), mapper.getDeserializationContext(), new ArrayList<>());
        assertFalse(collection.contains(null));
    }

    @Test
    public void testEmptyStringArrayAsCollection() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        Collection<String> collection = mapper.readValue(json, String[].class);
        ContextualDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        Collection<String> deserializedCollection = deserializer.deserialize(mapper.parser(), mapper.getDeserializationContext(), new ArrayList<>());
        assertEquals(Collections.emptyList(), deserializedCollection);
    }

    @Test
    public void testEmptyJsonArrayAsCollection() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        JsonNode node = mapper.readTree(json);
        ContextualDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        Collection<String> collection = deserializer.deserialize(mapper.parser(), mapper.getDeserializationContext(), new ArrayList<>());
        assertTrue(collection.isEmpty());
    }

    @Test
    public void testSingleElementJsonArrayAsCollection() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("elements", mapper.createArrayNode().add(mapper.valueToTree("element")));
        ContextualDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        Collection<String> collection = deserializer.deserialize(mapper.parser(), mapper.getDeserializationContext());
        assertEquals(1, collection.size());
        assertEquals("element", collection.get(0));
    }

    @Test
    public void testCollectionWithNullAndStrings() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("elements", mapper.createArrayNode().add(mapper.valueToTree("element1"))
                .add(mapper.valueToTree(null)));
        ContextualDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        Collection<String> collection = deserializer.deserialize(mapper.parser(), mapper.getDeserializationContext());
        assertEquals(2, collection.size());
        assertTrue(collection.contains("element1"));
        assertTrue(collection.contains(null));
    }

    @Test
    public void testCollectionWithStringsAndCustomDeserializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("elements", mapper.createArrayNode().add(mapper.valueToTree("element1"))
                .add(mapper.valueToTree("element2")));
        CustomStringDeserializer customDeserializer = new CustomStringDeserializer();
        ContextualDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, customDeserializer);
        Collection<String> collection = deserializer.deserialize(mapper.parser(), mapper.getDeserializationContext());
        assertEquals(2, collection.size());
        assertTrue(collection.contains("element1"));
        assertTrue(collection.contains("element2"));
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJsonArray() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.parser();
        parser.nextToken();
        parser.nextToken(); // skip current token
        parser.nextValue();
        parser.nextToken(); // skip current token
        ContextualDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        Collection<String> collection = deserializer.deserialize(parser, mapper.getDeserializationContext(), new ArrayList<>());
    }

    @Test
    public void testCustomDeserializerNullValue() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("elements", mapper.createArrayNode().add(mapper.valueToTree("element1"))
                .add(mapper.valueToTree(null)));
        CustomStringDeserializer customDeserializer = new CustomStringDeserializer();
        ContextualDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, customDeserializer);
        Collection<String> collection = deserializer.deserialize(mapper.parser(), mapper.getDeserializationContext());
        assertEquals(2, collection.size());
        assertTrue(collection.contains("element1"));
        assertTrue(collection.contains(null));
    }

    @Test
    public void testCustomDeserializerEmptyArray() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("elements", mapper.createArrayNode());
        CustomStringDeserializer customDeserializer = new CustomStringDeserializer();
        ContextualDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, customDeserializer);
        Collection<String> collection = deserializer.deserialize(mapper.parser(), mapper.getDeserializationContext());
        assertTrue(collection.isEmpty());
    }

    public static class CustomStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getText();
        }
    }
}