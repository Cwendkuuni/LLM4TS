package com.example.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;

public class StringCollectionDeserializerTest {

    @Test
    public void testDefaultDeserialization() throws IOException {
        String json = "[\"abc\",\"def\",null,\"ghi\",\"jkl\"]";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, null);
        List<String> result = deserializer.deserialize(parser, new TestContext());
        assertEquals(Arrays.asList("abc", "def", null, "ghi", "jkl"), result);
    }

    @Test
    public void testCustomDeserialization() throws IOException {
        String json = "[{\"value\":\"abc\"},{\"value\":\"def\"},null,{\"value\":\"ghi\"},{\"value\":\"jkl\"}]";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        JsonDeserializer<String> valueDeserializer = new StdDeserializer<String>() {};
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, valueDeserializer, null);
        List<String> result = deserializer.deserialize(parser, new TestContext());
        assertEquals(Arrays.asList("abc", "def", null, "ghi", "jkl"), result);
    }

    @Test
    public void testNullValue() throws IOException {
        String json = "[null]";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, null);
        List<String> result = deserializer.deserialize(parser, new TestContext());
        assertEquals(Collections.singletonList(null), result);
    }

    @Test
    public void testEmptyArray() throws IOException {
        String json = "[]";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, null);
        List<String> result = deserializer.deserialize(parser, new TestContext());
        assertEquals(Collections.emptyList(), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJson() throws IOException {
        String json = "[\":abc\"";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class), null, null, null, null, null);
        deserializer.deserialize(parser, new TestContext());
    }

    @JsonDeserialize(using = StringCollectionDeserializer.class)
    static class TestBean {
        private List<String> strings;

        public List<String> getStrings() {
            return strings;
        }

        public void setStrings(List<String> strings) {
            this.strings = strings;
        }
    }

    static class TestContext extends DeserializationContext {
        @Override
        public JsonDeserializer<?> findContentDeserializer(Class<?> desiredType, BeanProperty property)
                throws JsonMappingException {
            return null;
        }

        @Override
        public JsonDeserializer<?> findRootValueDeserializer(Class<?> desiredType) {
            return null;
        }

        @Override
        public JavaType handleSecondaryContextualization(BeanProperty property, JavaType type)
                throws JsonMappingException {
            return type;
        }

        @Override
        public Object findValueDeserializer(Class<?> valueClass) {
            return null;
        }

        @Override
        public ValueInstantiator findValueInstantiator(Class<?> valueType) throws JsonMappingException {
            return null;
        }

        @Override
        public NullValueProvider getNullProvider() {
            return new NullValueProviderDefault();
        }

        @Override
        public boolean isEnabled(DeserializationFeature feature) {
            return true;
        }

        @Override
        public void reportError(String msg) throws JsonMappingException {
            throw new JsonMappingException(msg);
        }
    }

    static class NullValueProviderDefault extends BaseNullValueProvider {
        @Override
        public Object getNullValue(DeserializationContext ctxt) {
            return null;
        }
    }
}