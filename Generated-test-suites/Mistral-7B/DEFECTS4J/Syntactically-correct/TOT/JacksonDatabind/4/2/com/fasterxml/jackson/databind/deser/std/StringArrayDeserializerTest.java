package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void testDefaultSerialization() throws IOException {
        StringJsonProvider provider = new StringJsonProvider();
        ObjectMapper mapper = new ObjectMapper(provider);
        String[] input = {"one", "two", "three"};
        String json = mapper.writeValueAsString(input);

        ObjectMapper roundTripMapper = new ObjectMapper();
        String[] output = roundTripMapper.readValue(json, String[].class);

        Assert.assertArrayEquals(input, output);
    }

    @Test
    public void testCustomElementDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> customDeserializer = mapper.getFactory()
                .createValueDeserializer(String.class, new SimpleModule().addDeserializer(String.class, new MyStringDeserializer()));
        StringArrayDeserializer serializer = new StringArrayDeserializer(customDeserializer);

        String json = "[\"" + "custom" + "\"]";
        String[] output = mapper.readerForArrayOf(String.class).readValue(json);

        Assert.assertArrayEquals(new String[]{"custom"}, output);
    }

    @Test
    public void testEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringArrayDeserializer serializer = new StringArrayDeserializer();

        String json = "[]";
        String[] output = mapper.readerForArrayOf(String.class).readValue(json);

        Assert.assertArrayEquals(new String[0], output);
    }

    @Test(expected = IOException.class)
    public void testNullInput() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringArrayDeserializer serializer = new StringArrayDeserializer();

        String json = "null";
        serializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testInvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringArrayDeserializer serializer = new StringArrayDeserializer();

        String json = "{invalid}";
        serializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullElementDeserializer() {
        StringArrayDeserializer serializer = new StringArrayDeserializer(null);
        serializer.createContextual(null, null);
    }

    static class MyStringDeserializer extends StdDeserializer<String> {
        public MyStringDeserializer() {
            this(null);
        }

        protected MyStringDeserializer(Class<String> vc) {
            super(vc);
        }

        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getText().toUpperCase();
        }
    }
}