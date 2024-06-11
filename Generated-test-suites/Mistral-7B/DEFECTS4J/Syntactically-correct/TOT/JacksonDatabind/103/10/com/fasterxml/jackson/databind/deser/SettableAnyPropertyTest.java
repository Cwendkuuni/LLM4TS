package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SettableAnyPropertyTest {

    private JsonFactory jsonFactory;
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        jsonFactory = new JsonFactory();
        objectMapper = new ObjectMapper(jsonFactory);
    }

    @Test
    public void testSettableAnyPropertyWithSimpleType() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(Map.class);
        BeanProperty property = new BeanProperty("testProperty", javaType);
        AnnotatedMember setter = PropertyName.forName("testProperty", Map.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, javaType, null, null, null);

        JsonParser parser = jsonFactory.createParser("{\"testProperty\": 42}");
        Object pojo = new HashMap<>();
        settableAnyProperty.deserializeAndSet(parser, null, pojo, "testProperty");

        assertEquals(42, pojo.get("testProperty"));
    }

    @Test
    public void testSettableAnyPropertyWithCustomTypeDeserializer() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(Map.class);
        BeanProperty property = new BeanProperty("testProperty", javaType);
        AnnotatedMember setter = PropertyName.forName("testProperty", Map.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, javaType, null, null, new IntTypeDeserializer());

        JsonParser parser = jsonFactory.createParser("{\"testProperty\": 42}");
        Object pojo = new HashMap<>();
        settableAnyProperty.deserializeAndSet(parser, null, pojo, "testProperty");

        assertEquals(42, pojo.get("testProperty"));
    }

    @Test(expected = JsonMappingException.class)
    public void testSettableAnyPropertyWithInvalidType() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(int[].class);
        BeanProperty property = new BeanProperty("testProperty", javaType);
        AnnotatedMember setter = PropertyName.forName("testProperty", Map.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, javaType, null, null, null);

        JsonParser parser = jsonFactory.createParser("{\"testProperty\": 42}");
        Object pojo = new HashMap<>();

        settableAnyProperty.deserializeAndSet(parser, null, pojo, "testProperty");
    }

    @Test
    public void testSettableAnyPropertyWithKeyDeserializer() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(Map.class);
        BeanProperty property = new BeanProperty("testProperty", javaType);
        AnnotatedMember setter = PropertyName.forName("testProperty", Map.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, javaType, null, null, null, new StringKeyDeserializer());

        JsonParser parser = jsonFactory.createParser("{\"key\": \"testProperty\", \"value\": 42}");
        Object pojo = new HashMap<>();
        settableAnyProperty.deserializeAndSet(parser, null, pojo, null);

        assertEquals("testProperty", pojo.keySet().iterator().next());
        assertEquals(42, pojo.get("testProperty"));
    }

    @Test(expected = JsonMappingException.class)
    public void testSettableAnyPropertyWithMissingSetter() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(Map.class);
        BeanProperty property = new BeanProperty("testProperty", javaType);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, null, javaType, null, null, null);

        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
        Object pojo = new HashMap<>();

        String json = "{\"testProperty\": 42}";
        objectMapper.readValue(new StringReader(json), SettableAnyProperty.class, pojo);
    }

    @Test
    public void testSettableAnyPropertyWithMissingProperty() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(Map.class);
        BeanProperty property = new BeanProperty("testProperty", javaType);
        AnnotatedMember setter = PropertyName.forName("testProperty", Map.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, javaType, null, null, null);

        Object pojo = new HashMap<>();

        settableAnyProperty.deserializeAndSet(jsonFactory.createParser("{}"), null, pojo, "missingProperty");
    }

    public static class IntTypeDeserializer extends StdTypeDeserializer<Integer> {

        public IntTypeDeserializer() {
            this(null);
        }

        protected IntTypeDeserializer(TypeDeserializer delegate) {
            super(Integer.class, null);
        }

        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsInt();
        }
    }

    public static class StringKeyDeserializer extends StdKeyDeserializer<String> {

        public StringKeyDeserializer() {
            this(null);
        }

        protected StringKeyDeserializer(KeyDeserializer delegate) {
            super(String.class, null);
        }

        @Override
        public String deserializeKey(String key, DeserializationContext ctxt) throws IOException {
            return key;
        }
    }
}