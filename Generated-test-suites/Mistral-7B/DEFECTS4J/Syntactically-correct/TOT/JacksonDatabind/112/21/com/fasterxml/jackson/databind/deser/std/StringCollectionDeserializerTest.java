package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class StringCollectionDeserializerTest {

    private ObjectMapper mapper;
    private JsonNodeFactory nodeFactory;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        nodeFactory = mapper.getNodeFactory();
    }

    // Test for empty collection
    @Test
    public void testEmptyCollectionDeserialization() throws JsonMappingException, IOException {
        JsonParser p = mapper.getFactory().createParser("[ ]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(p, mapper.getDeserializationContext());
        assertTrue(result.isEmpty());
    }

    // Test for simple collection deserialization
    @Test
    public void testSimpleCollectionDeserialization() throws JsonMappingException, JsonProcessingException {
        JsonParser p = mapper.getFactory().createParser("[\"string1\",\"string2\",\"string3\"]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(p, mapper.getDeserializationContext());
        ArrayList<String> expected = new ArrayList<>();
        expected.add("string1");
        expected.add("string2");
        expected.add("string3");
        assertEquals(expected, result);
    }

    // Test for null collection deserialization
    @Test
    public void testNullCollectionDeserialization() throws JsonMappingException, IOException {
        JsonParser p = mapper.getFactory().createParser("[null]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(p, mapper.getDeserializationContext());
        assertNull(result);
    }

    // Test for empty string deserialization
    @Test
    public void testEmptyStringDeserialization() throws JsonMappingException, IOException {
        JsonParser p = mapper.getFactory().createParser("[\"\"]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(p, mapper.getDeserializationContext());
        assertTrue(result.isEmpty());
    }

    // Test for deserialization with custom value deserializer
    @Test
    public void testCustomValueDeserializer() throws JsonMappingException, JsonProcessingException {
        String valueDeserializerClassName = CustomStringDeserializer.class.getName();
        JsonParser p = mapper.getFactory().createParser("[{\"name\": \"value1\"}]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, mapper.getDeserializationContext().findValueDeserializer(valueDeserializerClassName),
                null, null);
        Collection<String> result = deserializer.deserialize(p, mapper.getDeserializationContext());
        ArrayList<String> expected = new ArrayList<>();
        expected.add("value1");
        assertEquals(expected, result);
    }

    // Test for deserialization with custom deserializer for array
    @Test
    public void testCustomArrayDeserializer() throws JsonMappingException, JsonProcessingException {
        String arrayDeserializerClassName = CustomArrayDeserializer.class.getName();
        JsonParser p = mapper.getFactory().createParser("[{\"name\": \"array1\", \"elements\": [\"string1\", \"string2\"]}]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, mapper.getDeserializationContext().findValueDeserializer(arrayDeserializerClassName),
                null, null, null);
        Collection<String> result = deserializer.deserialize(p, mapper.getDeserializationContext());
        ArrayList<String> expected = new ArrayList<>();
        expected.add("string1");
        expected.add("string2");
        assertEquals(expected, result);
    }

    // Test for non-empty collection with null value
    @Test
    public void testCollectionWithNullValue() throws JsonMappingException, IOException {
        JsonParser p = mapper.getFactory().createParser("[\"string1\", null, \"string3\"]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(p, mapper.getDeserializationContext());
        ArrayList<String> expected = new ArrayList<>();
        expected.add("string1");
        expected.add(null);
        expected.add("string3");
        assertEquals(expected, result);
    }

    // Test for collection with non-string elements
    @Test(expected = JsonMappingException.class)
    public void testNonStringCollection() throws JsonMappingException, IOException {
        JsonParser p = mapper.getFactory().createParser("[1, 2, 3]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, null);
        deserializer.deserialize(p, mapper.getDeserializationContext());
    }

    // Test for collection with single value and unwrapping feature enabled
    @Test
    public void testCollectionWithSingleValueAndUnwrapping() throws JsonMappingException, IOException {
        JsonParser p = mapper.getFactory().createParser("[\"string1\"]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(String.class),
                null, null, null, null, Boolean.TRUE);
        String result = deserializer.deserialize(p, mapper.getDeserializationContext());
        assertEquals("string1", result);
    }

    // Test for collection with single value and unwrapping feature disabled
    @Test
    public void testCollectionWithSingleValueAndUnwrappingDisabled() throws JsonMappingException, IOException {
        JsonParser p = mapper.getFactory().createParser("[\"string1\"]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(p, mapper.getDeserializationContext());
        assertTrue(result.isEmpty());
    }

    // Test for collection with non-JSON value
    @Test(expected = JsonMappingException.class)
    public void testCollectionWithNonJsonValue() throws JsonMappingException, IOException {
        JsonParser p = mapper.getFactory().createParser("[123]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, null);
        deserializer.deserialize(p, mapper.getDeserializationContext());
    }

    // Test for deserialization with null value provider
    @Test
    public void testNullValueProvider() throws JsonMappingException, IOException {
        JsonParser p = mapper.getFactory().createParser("[null]");
        NullValueProvider nullValueProvider = new StandardNullValueProvider();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, null);
        deserializer = deserializer.withNullProvider(nullValueProvider);
        Collection<String> result = deserializer.deserialize(p, mapper.getDeserializationContext());
        assertNull(result);
    }

    // Test for deserialization with custom null value provider
    @Test
    public void testCustomNullValueProvider() throws JsonMappingException, IOException {
        JsonParser p = mapper.getFactory().createParser("[null]");
        NullValueProvider nullValueProvider = new CustomNullValueProvider();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, null, null);
        deserializer = deserializer.withNullProvider(nullValueProvider);
        Collection<String> result = deserializer.deserialize(p, mapper.getDeserializationContext());
        assertEquals("CustomNull", result.toString());
    }

    // Test for deserialization with type deserializer
    @Test
    public void testTypeDeserializer() throws JsonMappingException, JsonProcessingException {
        JsonParser p = mapper.getFactory().createParser("[{\"name\": \"element1\", \"type\": \"MyType\"}]");
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueTypeDeserializer(MyType.class, null);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class),
                null, null, null, typeDeserializer, null);
        Collection<String> result = deserializer.deserialize(p, mapper.getDeserializationContext());
        ArrayList<MyType> expected = new ArrayList<>();
        expected.add(new MyType());
        assertEquals(expected, result);
    }

    static class CustomStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    static class CustomArrayDeserializer extends StdDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            ArrayNode arrayNode = nodeFactory.textNode("");
            while (p.nextToken() != JsonToken.END_ARRAY) {
                Object value = ctxt.handlePrimitiveValue((String) null);
                if (value != null) {
                    arrayNode.add(new TextNode(value.toString()));
                }
            }
            Object result = mapper.getTypeFactory().constructValueType(ArrayList.class, String.class);
            return mapper.readerForUpdating(result).readValue(arrayNode);
        }
    }

    static class CustomNullValueProvider implements NullValueProvider {
        @Override
        public Object getNullValue(DeserializationContext ctxt) throws IOException {
            return "CustomNull";
        }
    }

    static class MyType {
    }

}