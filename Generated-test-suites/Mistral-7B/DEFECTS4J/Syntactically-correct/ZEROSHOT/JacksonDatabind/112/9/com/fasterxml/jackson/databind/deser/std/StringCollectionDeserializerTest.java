package com.example.jackson.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StringCollectionDeserializerTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_Empty_BEANS, false);
    }

    @Test
    public void testEmptyStringCollectionDeserialization() throws IOException {
        String json = "[]";
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                new TypeReference<Collection<String>>() {}.getType(), null, null);

        Collection<String> result = deserializer.deserialize(objectMapper.getFactory(), new JsonParser(json), new DeserializationContext());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void testNullStringCollectionDeserialization() throws IOException {
        String json = "null";
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                new TypeReference<Collection<String>>() {}.getType(), null, null);

        Collection<String> result = deserializer.deserialize(objectMapper.getFactory(), new JsonParser(json), new DeserializationContext());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void testStringCollectionDeserializationWithStandardValueDeserializer() throws IOException {
        String json = "[\"Hello\",\"World\"]";
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                new TypeReference<Collection<String>>() {}.getType(), null, null);

        Collection<String> result = deserializer.deserialize(objectMapper.getFactory(), new JsonParser(json), new DeserializationContext());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Hello", result.get(0));
        Assertions.assertEquals("World", result.get(1));
    }

    @Test
    public void testStringCollectionDeserializationWithCustomValueDeserializer() throws IOException {
        String json = "[{\"value\":\"Hello\"},{\"value\":\"World\"}]";
        MyStringDeserializer customValueDeserializer = new MyStringDeserializer();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                new TypeReference<Collection<String>>() {}.getType(), null, null, null, null, null, customValueDeserializer);

        Collection<String> result = deserializer.deserialize(objectMapper.getFactory(), new JsonParser(json), new DeserializationContext());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Hello", result.get(0));
        Assertions.assertEquals("World", result.get(1));
    }

    @Test
    public void testStringCollectionDeserializationWithNullValueProvider() throws IOException {
        String json = "[null, null]";
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                new TypeReference<Collection<String>>() {}.getType(), null, null, null, new NullStringProvider(), null);

        Collection<String> result = deserializer.deserialize(objectMapper.getFactory(), new JsonParser(json), new DeserializationContext());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        for (String s : result) {
            Assertions.assertEquals(null, s);
        }
    }

    @Test
    public void testStringCollectionDeserializationWithCustomValueInstantiator() throws IOException {
        String json = "[\"List\",\"Collection\"]";
        Collection<String> expectedResult = new ArrayList<>();
        expectedResult.add("List");
        expectedResult.add("Collection");

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                new TypeReference<Collection<String>>() {}.getType(), new MyStringValueInstantiator(), null, null, null, null, null);

        Collection<String> result = deserializer.deserialize(objectMapper.getFactory(), new JsonParser(json), new DeserializationContext());

        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void testStringCollectionDeserializationWithDelegateDeserializer() throws IOException {
        String json = "[{\"name\": \"John\"}, {\"name\": \"Mary\"}]";
        MyPersonDeserializer delegateDeserializer = new MyPersonDeserializer();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                new TypeReference<Collection<String>>() {}.getType(), null, delegateDeserializer, null, null, null, null);

        Collection<String> result = deserializer.deserialize(objectMapper.getFactory(), new JsonParser(json), new DeserializationContext());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        for (String s : result) {
            Assertions.assertEquals(s.startsWith("{\"name\": "), true);
        }
    }

    @Test
    public void testStringCollectionDeserializationWithCustomDeserializerAndDelegateDeserializer() throws IOException {
        String json = "[{\"name\": \"John\"}, {\"name\": \"Mary\"}]";
        MyStringDeserializer customValueDeserializer = new MyStringDeserializer();
        MyPersonDeserializer delegateDeserializer = new MyPersonDeserializer();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                new TypeReference<Collection<String>>() {}.getType(), null, null, delegateDeserializer, customValueDeserializer, null, null);

        Collection<String> result = deserializer.deserialize(objectMapper.getFactory(), new JsonParser(json), new DeserializationContext());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        for (String s : result) {
            Assertions.assertEquals(s.startsWith("{\"name\": "), true);
            Assertions.assertEquals(s.contains("\"name\": \"John\""), true);
            Assertions.assertEquals(s.contains("\"name\": \"Mary\""), true);
        }
    }

    private static class MyStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            JsonNode node = parser.getCodec().readTree(parser);
            return node.get("value").asText();
        }
    }

    private static class MyStringValueInstantiator implements ValueInstantiator {
        @Override
        public Object createValueInstance(DeserializationContext context) throws IOException {
            return new ArrayList<>();
        }
    }

    private static class MyNullStringProvider implements NullValueProvider {
        @Override
        public Object getNullValue(DeserializationContext context) {
            return null;
        }
    }

    private static class MyPersonDeserializer extends StdDeserializer<MyPerson> {
        @Override
        public MyPerson deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            JsonNode node = parser.getCodec().readTree(parser);
            return new MyPerson(node.get("name").asText());
        }
    }

    private static class MyPerson {
        private final String name;

        public MyPerson(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}