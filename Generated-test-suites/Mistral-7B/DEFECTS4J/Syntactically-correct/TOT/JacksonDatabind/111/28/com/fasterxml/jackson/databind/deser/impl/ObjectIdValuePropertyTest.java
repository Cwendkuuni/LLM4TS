package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.BaseIpAddressTest;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

public class ObjectIdValuePropertyTest {

    private static final ObjectIdReader objectIdReader = new SimpleObjectIdReader();
    private JavaBeanContext ctxt;
    private ObjectMapper mapper;
    private JsonFactory jsonFactory;

    @Before
    public void setup() throws Exception {
        ctxt = new JavaBeanContext();
        mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule() {
            {
                addDeserializer(String.class, new TestStringDeserializer());
                addDeserializer(Integer.class, new TestIntDeserializer());
            }
        });
        mapper.setAnnotationIntrospector(new AnnotationIntrospector());
        mapper.configure(MapperFeature.AUTO_DETECT_GETTERS_AND_SETTERS, true);
        jsonFactory = mapper.getFactory();
    }

    @Test
    public void testBasicDeserialization() throws IOException, JsonProcessingException {
        ObjectNode node = jsonFactory.createObjectNode();
        node.put("id", 123);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);
        JsonDeserializer<?> deserializer = mapper.findValueDeserializer(Integer.class, property);
        ObjectReader reader = mapper.readerForUpdating(Object.class)
                .forProperty("idProperty")
                .withValueDeserializer(deserializer);

        Object result = reader.readValue(node);
        assertEquals(123, result);
    }

    @Test
    public void testNullDeserialization() throws IOException, JsonProcessingException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);
        JsonDeserializer<?> deserializer = mapper.findValueDeserializer(Object.class, property);
        ObjectReader reader = mapper.readerForUpdating(Object.class)
                .forProperty("idProperty")
                .withValueDeserializer(deserializer);

        ObjectNode node = jsonFactory.createObjectNode();
        Object result = reader.readValue(node);
        assertNull(result);
    }

    @Test
    public void testInvalidDeserialization() throws IOException, JsonProcessingException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);
        JsonDeserializer<?> deserializer = mapper.findValueDeserializer(String.class, property);
        ObjectReader reader = mapper.readerForUpdating(Object.class)
                .forProperty("idProperty")
                .withValueDeserializer(deserializer);

        ObjectNode node = jsonFactory.createObjectNode();
        try {
            reader.readValue(node);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Cannot deserialize value of type java.lang.String from ObjectIdValueProperty (deserializer com.fasterxml.jackson.databind.deser.TestIntDeserializer)", e.getMessage());
        }
    }

    @Test
    public void testPropertyNameChange() throws IOException, JsonProcessingException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);
        JsonDeserializer<?> deserializer = mapper.findValueDeserializer(Integer.class, property);
        ObjectReader reader = mapper.readerForUpdating(Object.class)
                .forProperty("newPropertyName")
                .withValueDeserializer(deserializer);

        ObjectNode node = jsonFactory.createObjectNode();
        node.put("id", 123);
        Object result = reader.readValue(node);
        assertEquals(123, result);
    }

    static class TestIntDeserializer extends JsonDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new Random().nextInt();
        }
    }

    static class TestStringDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value = p.getValueAsString();
            return ToStringSerializer.instance.getValue(value);
        }
    }
}