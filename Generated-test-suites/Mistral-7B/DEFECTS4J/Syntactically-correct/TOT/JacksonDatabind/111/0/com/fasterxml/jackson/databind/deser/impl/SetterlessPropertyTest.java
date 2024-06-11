package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SetterlessPropertyTest {

    private ObjectMapper objectMapper;
    private SetterlessProperty property;

    @Before
    public void setup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Class<?> clazz = SetterlessProperty.class;
        Object instance = mapper.readValue(getClass().getResource("/test-data.json"), clazz);
        property = (SetterlessProperty) mapper.findProperty("listProperty", instance);
        AccessibleObject.setAccessible(property._getter, instance);
        objectMapper = mapper;
    }

    @Test
    public void testDeserializeAndSetWithValidJson() throws IOException {
        JsonParser parser = objectMapper.getFactory().createParser(getClass().getResource("/test-data.json").openStream());
        parser.nextToken(); // skip START_OBJECT
        parser.nextToken(); // skip "listProperty": []
        parser.nextToken(); // skip END_ARRAY
        parser.nextToken(); // skip END_OBJECT

        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("listProperty", new LinkedList<>(List.of(1, 2, 3)));
        Object result = property.deserializeAndSet(parser, objectMapper.getDeserializationContext(), new Object());

        Assert.assertEquals(expectedMap, result);
    }

    @Test
    public void testDeserializeAndSetWithNullJson() throws IOException {
        JsonParser parser = objectMapper.getFactory().createParser(getClass().getResource("/test-data-null.json").openStream());
        parser.nextToken(); // skip START_OBJECT
        parser.nextToken(); // skip "listProperty": null
        parser.nextToken(); // skip END_OBJECT

        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("listProperty", new LinkedList<>());
        Object result = property.deserializeAndSet(parser, objectMapper.getDeserializationContext(), new Object());

        Assert.assertEquals(expectedMap, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSetWithInvalidJson() throws IOException {
        JsonParser parser = objectMapper.getFactory().createParser(getClass().getResource("/test-data-invalid.json").openStream());
        parser.nextToken(); // skip START_OBJECT
        parser.nextToken(); // skip "listProperty": {...} - wrong type
        parser.nextToken(); // skip END_OBJECT

        property.deserializeAndSet(parser, objectMapper.getDeserializationContext(), new Object());
    }

    @Test
    public void testDeserializeAndSetWithCustomDeserializer() throws IOException {
        JsonParser parser = objectMapper.getFactory().createParser(getClass().getResource("/test-data.json").openStream());
        parser.nextToken(); // skip START_OBJECT
        parser.nextToken(); // skip "listProperty": []
        parser.nextToken(); // skip END_ARRAY
        parser.nextToken(); // skip END_OBJECT

        TypeDeserializer customDeserializer = new StubTypeDeserializer();
        property.setValueDeserializer(customDeserializer);

        Object result = property.deserializeAndSet(parser, objectMapper.getDeserializationContext(), new Object());

        Assert.assertEquals(result.getClass(), customDeserializer.getType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        property.set(new Object(), new Object());
    }

    private static class StubTypeDeserializer extends TypeDeserializer {
        private final Class<?> type;

        public StubTypeDeserializer(Class<?> type) {
            this.type = type;
        }

        @Override
        public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
                throws IOException {
            throw new UnsupportedOperationException("Not implemented");
        }

        @Override
        public Class<?> getType() {
            return type;
        }
    }
}