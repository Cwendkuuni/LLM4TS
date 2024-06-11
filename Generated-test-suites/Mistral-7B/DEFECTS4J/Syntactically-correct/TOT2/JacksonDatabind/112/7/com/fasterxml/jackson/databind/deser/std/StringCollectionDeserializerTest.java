package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringCollectionDeserializerTest {

    @Test
    public void testEmptyCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonEmpty = mapper.readTree("[]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        List<String> result = deserializer.deserialize(JsonParserFactory.instance.createParser(jsonEmpty),
                new DeserializationContext(mapper.getFactory()));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testStringCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonStringCollection = "[\"apple\", \"banana\", \"cherry\"]";
        JsonNode json = mapper.readTree(jsonStringCollection);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        List<String> result = deserializer.deserialize(JsonParserFactory.instance.createParser(json),
                new DeserializationContext(mapper.getFactory()));
        assertEquals(Arrays.asList("apple", "banana", "cherry"), result);
    }

    @Test
    public void testNullCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNull = mapper.readTree("null");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        List<String> result = deserializer.deserialize(JsonParserFactory.instance.createParser(jsonNull),
                new DeserializationContext(mapper.getFactory()));
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testInvalidJsonFormat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInvalid = "[1, 2, 3]";
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        deserializer.deserialize(JsonParserFactory.instance.createParser(jsonInvalid),
                new DeserializationContext(mapper.getFactory()));
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializingEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonEmptyString = "[\"\", \"\", \"\"]";
        JsonNode json = mapper.readTree(jsonEmptyString);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
        deserializer.deserialize(JsonParserFactory.instance.createParser(json),
                new DeserializationContext(mapper.getFactory()));
    }
}