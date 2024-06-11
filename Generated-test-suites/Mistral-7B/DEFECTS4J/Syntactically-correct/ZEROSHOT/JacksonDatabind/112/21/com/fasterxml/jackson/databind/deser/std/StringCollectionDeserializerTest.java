package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@JacksonStdImpl
public final class StringCollectionDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @org.junit.Test
    public void testDefaultDeserialization() throws IOException {
        String json = "[\"foo\", \"bar\"]";
        JsonNode node = mapper.readTree(json);
        StringCollectionDeserializer deserializer =
                new StringCollectionDeserializer(
                        mapper.constructType(Collection.class, String.class),
                        null,
                        null,
                        null,
                        null,
                        null);

        Collection<String> result = deserializer.deserialize(mapper.parserForTesting(), mapper.getDeserializationContext());
        assertEquals(2, result.size());
        assertEquals("foo", result.iterator().next());
        assertEquals("bar", result.iterator().next());
    }

    @org.junit.Test
    public void testDeserializationWithCustomValueDeserializer() throws IOException {
        JsonDeserializer<String> valueDeserializer = mapper.getDeserializationContext().findValueDeserializer(String.class);
        StringCollectionDeserializer deserializer =
                new StringCollectionDeserializer(
                        mapper.constructType(Collection.class, String.class),
                        null,
                        null,
                        valueDeserializer,
                        null,
                        null);

        String json = "[\"foo\", \"bar\"]";
        JsonNode node = mapper.readTree(json);

        Collection<String> result = deserializer.deserialize(mapper.parserForTesting(), mapper.getDeserializationContext());
        assertEquals(2, result.size());
        assertEquals("foo", result.iterator().next());
        assertEquals("bar", result.iterator().next());
    }

    @org.junit.Test
    public void testDeserializationWithDelegateDeserializer() throws IOException {
        JsonDeserializer<Object> delegateDeserializer = mapper.getDeserializationContext().findDeserializerForBean(Object.class);
        StringCollectionDeserializer deserializer =
                new StringCollectionDeserializer(
                        mapper.constructType(Collection.class, String.class),
                        null,
                        delegateDeserializer,
                        null,
                        null,
                        null);

        String json = "[1, 2, 3]";
        JsonNode node = mapper.readTree(json);

        Collection<String> result = deserializer.deserialize(mapper.parserForTesting(), mapper.getDeserializationContext());
        assertEquals(3, result.size());
        assertEquals("1", result.iterator().next());
        assertEquals("2", result.iterator().next());
        assertEquals("3", result.iterator().next());
    }

    @org.junit.Test
    public void testDeserializationWithNullValueProvider() throws IOException {
        NullValueProvider nullValueProvider = mapper.getDeserializationConfig().findNullValueProvider();
        StringCollectionDeserializer deserializer =
                new StringCollectionDeserializer(
                        mapper.constructType(Collection.class, String.class),
                        null,
                        null,
                        null,
                        nullValueProvider,
                        null);

        String json = "[null]";
        JsonNode node = mapper.readTree(json);

        Collection<String> result = deserializer.deserialize(mapper.parserForTesting(), mapper.getDeserializationContext());
        assertEquals(1, result.size());
        assertNull(result.iterator().next());
    }

    @org.junit.Test
    public void testDeserializationWithUnwrappingSingleValue() throws IOException {
        StringCollectionDeserializer deserializer =
                new StringCollectionDeserializer(
                        mapper.constructType(List.class, String.class),
                        null,
                        null,
                        null,
                        null,
                        Boolean.TRUE);

        String json = "\"foo\"";
        JsonNode node = mapper.readTree(json);

        Collection<String> result = deserializer.deserialize(mapper.parserForTesting(), mapper.getDeserializationContext());
        assertEquals(1, result.size());
        assertEquals("foo", result.iterator().next());
    }

    @org.junit.Test
    public void testDeserializationWithFormatFeature() throws IOException {
        String json = "[\"foo\", null, \"bar\"]";
        JsonNode node = mapper.readTree(json);

        StringCollectionDeserializer deserializer =
                mapper.getDeserializationContext().findValueDeserializer(StringCollectionDeserializer.class);

        Collection<String> result = deserializer.deserialize(mapper.parserForTesting(), mapper.getDeserializationContext());
        assertEquals(3, result.size());
        assertEquals("foo", result.iterator().next());
        assertNull(result.iterator().next());
        assertEquals("bar", result.iterator().next());
    }

    @org.junit.Test(expected = IOException.class)
    public void testDeserializationWithNonArrayInput() throws IOException {
        String json = "\"foo\"";
        JsonNode node = mapper.readTree(json);

        StringCollectionDeserializer deserializer =
                new StringCollectionDeserializer(
                        mapper.constructType(Collection.class, String.class),
                        null,
                        null,
                        null,
                        null,
                        null);

        deserializer.deserialize(mapper.parserForTesting(), mapper.getDeserializationContext());
    }

    @org.junit.Test(expected = IOException.class)
    public void testDeserializationWithNullValueAndSkipNulls() throws IOException {
        String json = "[null]";
        JsonNode node = mapper.readTree(json);

        StringCollectionDeserializer deserializer =
                new StringCollectionDeserializer(
                        mapper.constructType(Collection.class, String.class),
                        null,
                        null,
                        null,
                        null,
                        Boolean.TRUE);

        deserializer.deserialize(mapper.parserForTesting(), mapper.getDeserializationContext());
    }
}