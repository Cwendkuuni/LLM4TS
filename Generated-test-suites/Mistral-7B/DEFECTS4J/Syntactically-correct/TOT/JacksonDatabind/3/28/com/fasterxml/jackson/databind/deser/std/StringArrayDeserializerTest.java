package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class StringArrayDeserializerTest {

    private ObjectMapper objectMapper;

    public StringArrayDeserializerTest() {
        objectMapper = new ObjectMapper(new JsonFactory());
        objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testEmptyArrayDeserialization() throws IOException {
        String[] expected = new String[0];
        String[] actual = objectMapper.readValue("[]", String[].class);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testSingleElementArrayDeserialization() throws IOException {
        String element = "Element";
        String[] expected = {element};
        String[] actual = objectMapper.readValue("[\"Element\"]", String[].class);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testCustomElementDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper customMapper = new ObjectMapper(new JsonFactory());
        customMapper.registerValve(new CustomSerializableAnnotations());
        CustomElementDeserializer customDeserializer = new CustomElementDeserializer();
        StringArrayDeserializer serializer = new StringArrayDeserializer(customDeserializer);
        String[] expected = {"CustomElement"};
        String[] actual = customMapper.readValue("[\"CustomElement\"]", String[].class);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testNonArrayDeserialization() throws IOException {
        objectMapper.readValue("\"Element\"", String[].class);
    }

    @Test
    public void testNullAsEmptyArrayDeserialization() throws IOException {
        String[] expected = new String[0];
        String[] actual = objectMapper.readValue("null", String[].class);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testNullElementDeserialization() throws IOException {
        String[] expected = {"null"};
        String[] actual = objectMapper.readValue("[null]", String[].class);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testEmptyStringAsNullDeserialization() throws IOException {
        ObjectMapper emptyStringAsNullMapper = new ObjectMapper(new JsonFactory());
        emptyStringAsNullMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        String[] expected = {null};
        String[] actual = emptyStringAsNullMapper.readValue("[]", String[].class);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testSingleEmptyStringAsNullDeserialization() throws IOException {
        ObjectMapper emptyStringAsNullMapper = new ObjectMapper(new JsonFactory());
        emptyStringAsNullMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        String[] expected = {null};
        String[] actual = emptyStringAsNullMapper.readValue("[\"\"]", String[].class);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testArrayWithCustomDeserializer() throws IOException {
        StringArrayDeserializer serializer = new StringArrayDeserializer(new CustomElementDeserializer());
        String[] expected = {"CustomElement1", "CustomElement2"};
        String[] actual = serializer.deserialize(new JsonFactory().createParser("[\"" + CustomElement.CUSTOM_ELEMENT_1 + "\",\"" + CustomElement.CUSTOM_ELEMENT_2 + "\"]"), new TestContext());
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testArrayWithNullElement() throws IOException {
        StringArrayDeserializer serializer = new StringArrayDeserializer(null);
        String[] expected = {"null"};
        String[] actual = serializer.deserialize(new JsonFactory().createParser("[null]"), new TestContext());
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testArrayWithEmptyString() throws IOException {
        StringArrayDeserializer serializer = new StringArrayDeserializer(null);
        String[] expected = {"", ""};
        String[] actual = serializer.deserialize(new JsonFactory().createParser("[\"\",\"]"), new TestContext());
        Assert.assertArrayEquals(expected, actual);
    }

    static class CustomElementDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return "CustomElement";
        }
    }

    static class TestContext extends DeserializationContext {
        @Override
        public ObjectMapper getConfig() {
            return null;
        }

        @Override
        public JsonDeserializer<?> findValueDeserializer(Class<?> deserializerClass, BeanProperty property) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonDeserializer<?> findContextualValueDeserializer(Class<?> deserializerClass, BeanProperty property) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonDeserializer<?> handleSecondaryContextualization(JsonDeserializer<?> deserializer, BeanProperty property) throws JsonMappingException {
            return deserializer;
        }

        @Override
        public Object findContextualValueWithHandler(BeanProperty property, DeserializationContext contextHandler) throws JsonMappingException {
            return null;
        }

        @Override
        public Object findValueWithHandler(DeserializationContext contextHandler, BeanProperty property) throws JsonMappingException {
            return null;
        }

        @Override
        public ObjectPointer findValueWithType(DeserializationContext contextHandler, TypeDeserializer typeDeserializer) throws JsonMappingException {
            return null;
        }

        @Override
        public ObjectReader createContextualValueReader(DeserializationContext deserializationContext, BeanProperty property) throws JsonMappingException {
            return null;
        }
    }
}