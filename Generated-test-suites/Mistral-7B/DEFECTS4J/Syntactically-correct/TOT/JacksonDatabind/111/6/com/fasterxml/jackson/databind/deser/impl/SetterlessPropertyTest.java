package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SetterlessPropertyTest {

    private ObjectTestSubject objectUnderTest;
    private JsonFactory jsonFactory;
    private JsonParser parser;
    private ObjectMapper objectMapper;
    private AnnotationIntrospector introspector;

    @Before
    public void setup() {
        jsonFactory = new JsonFactory();
        parser = jsonFactory.createParser(getClass().getResource("/test-input.json").openStream());
        objectMapper = new ObjectMapper();
        introspector = objectMapper.getAnnotationIntrospector();
        objectUnderTest = new ObjectTestSubject();
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Integer> map = new HashMap<>();
        map.put("key1", 1);
        map.put("key2", 2);
        ObjectWriter writer = mapper.writerWithTypeValidator(false);
        String json = writer.writeValueAsString(map);

        SetterlessProperty property = introspect(objectUnderTest.getClass(), "testProperty");
        property.setValueDeserializer(mapper.getFactory().getValueDeserializer(Integer.class, null));

        parser.nextValue(); // consume start object
        parser.nextToken(); // consume null key
        parser.nextValue(); // consume array start
        parser.nextValue(); // consume int 1
        parser.nextValue(); // consume null key
        parser.nextValue(); // consume int 2
        parser.nextToken(JsonToken.END_ARRAY);

        property.deserializeAndSet(parser, Mockito.mock(DeserializationContext.class), objectUnderTest);

        @SuppressWarnings("unchecked")
        Map<String, Integer> result = (Map<String, Integer>) objectUnderTest.getTestProperty();
        assertEquals(map.size(), result.size());
        assertEquals(map.get("key1"), result.get("key1"));
        assertEquals(map.get("key2"), result.get("key2"));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Integer> map = new HashMap<>();
        map.put("key1", 1);
        map.put("key2", 2);
        ObjectWriter writer = mapper.writerWithTypeValidator(false);
        String json = writer.writeValueAsString(map);

        SetterlessProperty property = introspect(objectUnderTest.getClass(), "testProperty");
        property.setValueDeserializer(mapper.getFactory().getValueDeserializer(Integer.class, null));

        Object result = property.deserializeSetAndReturn(parser, Mockito.mock(DeserializationContext.class), objectUnderTest);

        assertEquals(objectUnderTest.getClass(), result.getClass());
        Map<String, Integer> mapResult = (Map<String, Integer>) result;
        assertEquals(map.size(), mapResult.size());
        assertEquals(map.get("key1"), mapResult.get("key1"));
        assertEquals(map.get("key2"), mapResult.get("key2"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        SetterlessProperty property = introspect(objectUnderTest.getClass(), "testProperty");
        property.setValueDeserializer(null);
        property.set(objectUnderTest, "test");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws IOException {
        SetterlessProperty property = introspect(objectUnderTest.getClass(), "testProperty");
        property.setValueDeserializer(null);
        Object result = property.setAndReturn(objectUnderTest, "test");

        assertEquals(objectUnderTest.getClass(), result.getClass());
    }

    @Test
    public void testNullValue() throws IOException {
        SetterlessProperty property = introspect(objectUnderTest.getClass(), "testProperty");
        property.setValueDeserializer(null);

        parser.nextValue(); // consume start object
        parser.nextToken(); // consume null key
        parser.nextToken(JsonToken.VALUE_NULL);

        property.deserializeAndSet(parser, Mockito.mock(DeserializationContext.class), objectUnderTest);

        assertNull(objectUnderTest.getTestProperty());
    }

    @Test
    public void testEmptyArray() throws IOException {
        SetterlessProperty property = introspect(objectUnderTest.getClass(), "testProperty");
        property.setValueDeserializer(null);

        parser.nextValue(); // consume start object
        parser.nextToken(); // consume null key
        parser.nextToken(JsonToken.START_ARRAY);
        parser.nextToken(JsonToken.END_ARRAY);

        property.deserializeAndSet(parser, Mockito.mock(DeserializationContext.class), objectUnderTest);

        assertNull(objectUnderTest.getTestProperty());
    }

    @Test
    public void testEmptyMap() throws IOException {
        SetterlessProperty property = introspect(objectUnderTest.getClass(), "testProperty");
        property.setValueDeserializer(null);

        parser.nextValue(); // consume start object
        parser.nextToken(); // consume null key
        parser.nextToken(JsonToken.START_OBJECT);
        parser.nextToken(JsonToken.END_OBJECT);

        property.deserializeAndSet(parser, Mockito.mock(DeserializationContext.class), objectUnderTest);

        assertNull(objectUnderTest.getTestProperty());
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<String> list = new ArrayList<>();
        list.add("key1");
        list.add("key2");
        ObjectWriter writer = mapper.writerWithTypeValidator(false);
        String json = writer.writeValueAsString(list);

        SetterlessProperty property = introspect(objectUnderTest.getClass(), "testProperty");
        property.setValueDeserializer(mapper.getFactory().getValueDeserializer(List.class, null));

        ObjectTypeDeserializer objectTypeDeserializer = Mockito.mock(ObjectTypeDeserializer.class);
        when(objectTypeDeserializer.deserializeUsing(Mockito.eq(parser), Mockito.eq(Mockito.any(DeserializationContext.class)), Mockito.eq(List.class)))
                .thenReturn(list);
        when(parser.getDeserializerProvider().findValueDeserializer(Mockito.eq(List.class), Mockito.eq(null), Mockito.eq(JsonDeserializer.None.class)))
                .thenReturn(objectTypeDeserializer);

        property.deserializeAndSet(parser, Mockito.mock(DeserializationContext.class), objectUnderTest);

        List<String> result = (List<String>) objectUnderTest.getTestProperty();
        assertEquals(list.size(), result.size());
        assertEquals(list.get(0), result.get(0));
        assertEquals(list.get(1), result.get(1));
    }

    @Test
    public void testDeserializeWithTypeCustomDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = JsonFactory.instance.createObjectNode();
        node.put("key1", "value1");
        node.put("key2", "value2");

        SetterlessProperty property = introspect(objectUnderTest.getClass(), "testProperty");
        property.setValueDeserializer(mapper.getFactory().getValueDeserializer(ObjectNode.class, new MyCustomDeserializer()));

        ObjectNode nodeResult = (ObjectNode) property.deserializeAndSet(parser, Mockito.mock(DeserializationContext.class), null);

        assertEquals(node.size(), nodeResult.size());
        assertEquals(node.get("key1").asText(), nodeResult.get("key1").asText());
        assertEquals(node.get("key2").asText(), nodeResult.get("key2").asText());
    }

    @Test
    public void testGetAnnotation() {
        Class<?> testClass = objectUnderTest.getClass();
        SetterlessProperty property = introspect(testClass, "testProperty");
        Annotation testAnnotation = Mockito.mock(Annotation.class);
        Method testMethod = Mockito.mock(Method.class);
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(testMethod, testAnnotation);
        when(property.getAnnotated()).thenReturn(annotatedMethod);

        assertEquals(testAnnotation, property.getAnnotation(testAnnotation.getClass()));
    }

    private SetterlessProperty introspect(Class<?> clazz, String propertyName) {
        BeanPropertyDefinition beanPropertyDefinition = Annotations.findValue(clazz, JsonProperty.class, propertyName);
        return new SetterlessProperty(beanPropertyDefinition, JsonTypeFactory.instance.constructType(clazz), null, null, null);
    }

    private static class MyCustomDeserializer extends StdSerializer<ObjectNode> {
        public MyCustomDeserializer() {
            this(null);
        }

        public MyCustomDeserializer(Class<ObjectNode> t) {
            this(null);
        }

        public MyCustomDeserializer(Class<?> t, Converter<?> converter) {
            super(t, converter);
        }

        @Override
        public ObjectNode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            ObjectNode result = JsonFactory.instance.createObjectNode();
            result.put("key1", "value1");
            result.put("key2", "value2");
            return result;
        }
    }

    private static class ObjectTestSubject {
        Object testProperty;

        public Object getTestProperty() {
            return testProperty;
        }
    }
}