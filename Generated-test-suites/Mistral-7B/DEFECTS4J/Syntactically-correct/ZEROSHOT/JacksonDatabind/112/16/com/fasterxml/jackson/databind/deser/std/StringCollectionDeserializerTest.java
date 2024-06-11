package com.example.jackson.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@JacksonStdImpl
public final class StringCollectionDeserializerTest {

    @Test
    public void testEmptyCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("[]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null);
        List<String> result = deserializer.deserialize(mapper.treeToValues(json), null);
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testNullCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("null");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null);
        List<String> result = deserializer.deserialize(mapper.treeToValues(json), null);
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testEmptyCollectionWithNullValues() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("[]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null);
        ObjectNullProvider nullProvider = new ObjectNullProvider();
        deserializer = deserializer.withNullProvider(nullProvider);
        List<String> result = deserializer.deserialize(mapper.treeToValues(json), null);
        Assert.assertEquals(Collections.singletonList(null), result);
    }

    @Test
    public void testSingleValueCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("['abc']");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null);
        List<String> result = deserializer.deserialize(mapper.treeToValues(json), null);
        Assert.assertEquals(Arrays.asList("abc"), result);
    }

    @Test
    public void testSingleValueCollectionWithNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("['abc', null]");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null);
        ObjectNullProvider nullProvider = new ObjectNullProvider();
        deserializer = deserializer.withNullProvider(nullProvider);
        List<String> result = deserializer.deserialize(mapper.treeToValues(json), null);
        Assert.assertEquals(Arrays.asList("abc", null), result);
    }

    @Test
    public void testCustomValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("['The quick brown fox', 'jumps over the lazy dog']");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                new MyStringDeserializer(), null);
        List<String> result = deserializer.deserialize(mapper.treeToValues(json), null);
        Assert.assertEquals(Arrays.asList("The quick brown fox", "jumps over the lazy dog"), result);
    }

    @Test
    public void testDelegateDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("{'strings': ['The quick brown fox', 'jumps over the lazy dog']}");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null,
                new MyDelegateDeserializer(), null, null, null);
        List<String> result = deserializer.deserialize(mapper.treeToValues(json), null);
        Assert.assertEquals(Arrays.asList("The quick brown fox", "jumps over the lazy dog"), result);
    }

    @Test(expected = IOException.class)
    public void testInvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("{'abc': ['The quick brown fox', 'jumps over the lazy dog']}");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null);
        deserializer.deserialize(mapper.treeToValues(json), null);
    }

    private static class MyStringDeserializer
            extends StdDeserializer<String> {

        public MyStringDeserializer() {
            this(null);
        }

        protected MyStringDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    private static class MyDelegateDeserializer
            extends StdDeserializer<Object> {

        private final JsonDeserializer<String> _valueDeserializer;

        public MyDelegateDeserializer() {
            this(null);
        }

        protected MyDelegateDeserializer(Class<?> vc) {
            super(vc);
            _valueDeserializer = new StringDeserializer();
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return _valueDeserializer.deserialize(p, ctxt);
        }
    }

    private static class StringDeserializer
            extends JsonDeserializer<String> {

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    private static class ObjectNullProvider
            implements NullValueProvider {

        @Override
        public Object getNullValue(DeserializationContext ctxt) throws IOException {
            return null;
        }
    }
}