package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for {@link StringCollectionDeserializer}.
 */
public class StringCollectionDeserializerTest {

    @Test
    public void testEmptyCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonEmpty = "[]";
        JsonNode jsonNode = mapper.readTree(jsonEmpty);
        JsonParser parser = jsonNode.asParser();
        DeserializationContext context = mapper.getDeserializationContext();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        Collection<String> result = deserializer.createContextual(context, null);
        deserializer.deserialize(parser, context, result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testStringCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonStringCollection = "[\"abc\", \"def\", \"ghi\"]";
        JsonNode jsonNode = mapper.readTree(jsonStringCollection);
        JsonParser parser = jsonNode.asParser();
        DeserializationContext context = mapper.getDeserializationContext();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        Collection<String> result = deserializer.createContextual(context, null);
        deserializer.deserialize(parser, context, result);
        List<String> expectedList = new ArrayList<>();
        expectedList.add("abc");
        expectedList.add("def");
        expectedList.add("ghi");
        assertEquals(expectedList, result);
    }

    @Test
    public void testCustomStringDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonStringCollection = "[{\"custom\":\"abc\"}, {\"custom\":\"def\"}, {\"custom\":\"ghi\"}]";
        JsonNode jsonNode = mapper.readTree(jsonStringCollection);
        JsonParser parser = jsonNode.asParser();
        DeserializationContext context = mapper.getDeserializationContext();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        JsonDeserializer<String> customDeserializer = new StdDeserializer<String>() {};
        StringCollectionDeserializer testDeserializer = deserializer.withResolved(null, customDeserializer, null, null);
        Collection<String> result = testDeserializer.createContextual(context, null);
        deserializer.deserialize(parser, context, result);
        List<String> expectedList = new ArrayList<>();
        expectedList.add("abc");
        expectedList.add("def");
        expectedList.add("ghi");
        assertEquals(expectedList, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testNullValueInCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonStringCollectionWithNull = "[\"abc\", null, \"def\"]";
        JsonNode jsonNode = mapper.readTree(jsonStringCollectionWithNull);
        JsonParser parser = jsonNode.asParser();
        DeserializationContext context = mapper.getDeserializationContext();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        Collection<String> result = deserializer.createContextual(context, null);
        deserializer.deserialize(parser, context, result);
        List<String> expectedList = new ArrayList<>();
        expectedList.add("abc");
        expectedList.add(null);
        expectedList.add("def");
        assertEquals(expectedList, result);
    }

    @Test
    public void testStringCollectionDeserializationWithNullProvider() throws IOException {
        ObjectMapper mapper = new ObjectMapper() {{
            setNullValueProvider(new StringNullValueProvider());
        }};
        String jsonStringCollection = "[null, \"def\", \"ghi\"]";
        JsonNode jsonNode = mapper.readTree(jsonStringCollection);
        JsonParser parser = jsonNode.asParser();
        DeserializationContext context = mapper.getDeserializationContext();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        Collection<String> result = deserializer.createContextual(context, null);
        deserializer.deserialize(parser, context, result);
        List<String> expectedList = new ArrayList<>();
        expectedList.add(null);
        expectedList.add("def");
        expectedList.add("ghi");
        assertEquals(expectedList, result);
    }

    @Test
    public void testStringCollectionDeserializationWithUnwrapping() throws IOException {
        ObjectMapper mapper = new ObjectMapper() {{
            findAndRegisterModules();
            setSerializationInclusion(FindNullValue.NON_NULL);
        }};
        String jsonStringCollectionWithSingleValue = "[\"abc\"]";
        JsonNode jsonNode = mapper.readTree(jsonStringCollectionWithSingleValue);
        JsonParser parser = jsonNode.asParser();
        DeserializationContext context = mapper.getDeserializationContext();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, Boolean.TRUE);
        Collection<String> result = deserializer.createContextual(context, null);
        deserializer.deserialize(parser, context, result);
        List<String> expectedList = new ArrayList<>();
        expectedList.add("abc");
        assertEquals(expectedList, result);
    }

    @Test
    public void testInvalidJsonFormat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInvalidJsonFormat = "{\"format\":\"invalid\"}";
        JsonNode jsonNode = mapper.readTree(jsonInvalidJsonFormat);
        JsonParser parser = jsonNode.asParser();
        DeserializationContext context = mapper.getDeserializationContext();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        try {
            deserializer.createContextual(context, null);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Failed to deserialize Collection of type [simple type, class java.util.Collection<? extends java.lang.String>] from JSON String '{\"format\":\"invalid\"}'", e.getMessage());
        }
    }

    /**
     * Custom null value provider that returns a String instance instead of a null value.
     * This is used to test the behavior of {@link StringCollectionDeserializer} when it encounters a null value.
     */
    static class StringNullValueProvider extends NullValueProvider.Base {
        @Override
        public Object getNullValue(DeserializationContext ctxt) throws IOException {
            return new TextNode("nullValue");
        }
    }
}