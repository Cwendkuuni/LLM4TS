package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for StringArrayDeserializer class.
 */
public class StringArrayDeserializerTest {

    @Test
    public void testEmptyArrayDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = mapper.readerForArrayOf(String.class).readValue(json, deserializer);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testSingleValueDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"element\"";
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = mapper.readerForArrayOf(String.class).readValue(json, deserializer);
        Assert.assertEquals("element", result[0]);
    }

    @Test
    public void testArrayDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"element1\", \"element2\", null]";
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = mapper.readerForArrayOf(String.class).readValue(json, deserializer);
        Assert.assertArrayEquals(new String[]{"element1", "element2", null}, result);
    }

    @Test
    public void testNullArrayDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "null";
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = mapper.readerForArrayOf(String.class).readValue(json, deserializer);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testCustomElementDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> customDeserializer = new StandardDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getText() + "_custom";
            }
        };
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        String json = "[\"element1\", \"element2\"]";
        String[] result = mapper.readerForArrayOf(String.class).readValue(json, deserializer);
        Assert.assertArrayEquals(new String[]{"element1_custom", "element2_custom"}, result);
    }

    @Test(expected = IOException.class)
    public void testInvalidJsonDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"invalid\":\"json\"}";
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        mapper.readerForArrayOf(String.class).readValue(json, deserializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testUnexpectedTypeDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        mapper.readerForArrayOf(Integer.class).readValue(json, deserializer);
    }

    @Test
    public void testDeserializationWithTypeHandler() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"element1\", \"element2\", null]";
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(String[].class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = mapper.readerForArrayOf(String.class).withTypeHandler(typeDeserializer).readValue(json, deserializer);
        Assert.assertArrayEquals(new String[]{"element1", "element2", null}, result);
    }

    @Test
    public void testDeserializationWithContextualization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> customDeserializer = new StandardDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getText() + "_contextual";
            }
        };
        mapper.registerModule(new SimpleModule() {{
            addDeserializer(String.class, customDeserializer);
        }});
        String json = "[\"element1\", \"element2\"]";
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = mapper.readerForArrayOf(String.class).readValue(json, deserializer);
        Assert.assertArrayEquals(new String[]{"element1_contextual", "element2_contextual"}, result);
    }
}