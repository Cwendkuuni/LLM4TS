package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.deser.impl.PropertyNameBasedSettableAnyProperty;
import com.fasterxml.jackson.databind.deser.impl.ReferenceableSettableAnyProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SettableAnyPropertyTest {

    private static ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    @org.junit.BeforeClass
    public static void setup() throws Exception {
        mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(new AnnotationIntrospector());
    }

    /**
     * Test for setting a String value through a setter method using {@link SettableAnyProperty}
     * @throws IOException
     * @throws JsonProcessingException
     */
    @org.junit.Test
    public void testSettableAnyPropertyString() throws IOException, JsonProcessingException {
        Class<?> testClass = com.example.TestClass.class;
        BeanProperty property = new BeanProperty.Std(testClass, "testProperty", String.class);
        AnnotatedMember setter = new AnnotatedField(testClass, "testProperty");
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class);
        KeyDeserializer keyDeserializer = mapper.getFactory().getBeanPropertyKeyDeserializer();
        JsonDeserializer<Object> valueDeserializer = mapper.findValueDeserializer(String.class);
        TypeDeserializer typeDeserializer = mapper.getFactory().getPolymorphicTypeDeserializer(Map.class);
        SettableAnyProperty anyProperty = new PropertyNameBasedSettableAnyProperty(property, setter, javaType, keyDeserializer, valueDeserializer, typeDeserializer);

        Object instance = new Object();
        DeserializationConfig config = mapper.getDeserializationConfig();
        config.findValueDeserializer(anyProperty.getType());
        anyProperty.fixAccess(config);

        String testValue = "testValue";
        String testKey = "testKey";

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("key", testKey);
        node.put("value", testValue);

        anyProperty.deserializeAndSet(mapper.getFactory().createParser(node), config, instance, "testProperty");

        Map<String, Object> map = (Map<String, Object>) setter.getValue(instance);
        assertEquals(testValue, map.get(testKey));
    }

    /**
     * Test for setting an Integer value through a setter method using {@link SettableAnyProperty}
     * @throws IOException
     * @throws JsonProcessingException
     */
    @org.junit.Test
    public void testSettableAnyPropertyInteger() throws IOException, JsonProcessingException {
        Class<?> testClass = com.example.TestClass.class;
        BeanProperty property = new BeanProperty.Std(testClass, "testProperty", Integer.class);
        AnnotatedMember setter = new AnnotatedField(testClass, "testProperty");
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class);
        KeyDeserializer keyDeserializer = mapper.getFactory().getBeanPropertyKeyDeserializer();
        JsonDeserializer<Object> valueDeserializer = mapper.findValueDeserializer(Integer.class);
        TypeDeserializer typeDeserializer = mapper.getFactory().getPolymorphicTypeDeserializer(Map.class);
        SettableAnyProperty anyProperty = new PropertyNameBasedSettableAnyProperty(property, setter, javaType, keyDeserializer, valueDeserializer, typeDeserializer);

        Object instance = new Object();
        DeserializationConfig config = mapper.getDeserializationConfig();
        config.findValueDeserializer(anyProperty.getType());
        anyProperty.fixAccess(config);

        int testValue = 123;
        String testKey = "testKey";

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("key", testKey);
        node.put("value", testValue);

        anyProperty.deserializeAndSet(mapper.getFactory().createParser(node), config, instance, "testProperty");

        Map<String, Object> map = (Map<String, Object>) setter.getValue(instance);
        assertEquals(testValue, map.get(testKey));
    }

    /**
     * Test for setting a null value through a setter method using {@link SettableAnyProperty}
     * @throws IOException
     * @throws JsonProcessingException
     */
    @org.junit.Test
    public void testSettableAnyPropertyNull() throws IOException, JsonProcessingException {
        Class<?> testClass = com.example.TestClass.class;
        BeanProperty property = new BeanProperty.Std(testClass, "testProperty", String.class);
        AnnotatedMember setter = new AnnotatedField(testClass, "testProperty");
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class);
        KeyDeserializer keyDeserializer = mapper.getFactory().getBeanPropertyKeyDeserializer();
        JsonDeserializer<Object> valueDeserializer = mapper.findNullValueSerializer(String.class);
        TypeDeserializer typeDeserializer = mapper.getFactory().getPolymorphicTypeDeserializer(Map.class);
        SettableAnyProperty anyProperty = new PropertyNameBasedSettableAnyProperty(property, setter, javaType, keyDeserializer, valueDeserializer, typeDeserializer);

        Object instance = new Object();
        DeserializationConfig config = mapper.getDeserializationConfig();
        config.findValueDeserializer(anyProperty.getType());
        anyProperty.fixAccess(config);

        String testKey = "testKey";

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("key", testKey);

        anyProperty.deserializeAndSet(mapper.getFactory().createParser(node), config, instance, "testProperty");

        Map<String, Object> map = (Map<String, Object>) setter.getValue(instance);
        assertNull(map.get(testKey));
    }

    /**
     * Test for setting an invalid value through a setter method using {@link SettableAnyProperty}
     * @throws IOException
     * @throws JsonProcessingException
     */
    @org.junit.Test
    public void testSettableAnyPropertyInvalidValue() throws IOException, JsonProcessingException {
        Class<?> testClass = com.example.TestClass.class;
        BeanProperty property = new BeanProperty.Std(testClass, "testProperty", String.class);
        AnnotatedMember setter = new AnnotatedField(testClass, "testProperty");
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class);
        KeyDeserializer keyDeserializer = mapper.getFactory().getBeanPropertyKeyDeserializer();
        JsonDeserializer<Object> valueDeserializer = mapper.findValueDeserializer(String.class);
        TypeDeserializer typeDeserializer = mapper.getFactory().getPolymorphicTypeDeserializer(Map.class);
        SettableAnyProperty anyProperty = new PropertyNameBasedSettableAnyProperty(property, setter, javaType, keyDeserializer, valueDeserializer, typeDeserializer);

        Object instance = new Object();
        DeserializationConfig config = mapper.getDeserializationConfig();
        config.findValueDeserializer(anyProperty.getType());
        anyProperty.fixAccess(config);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("key", "testKey");
        node.put("value", new Object());

        try {
            anyProperty.deserializeAndSet(mapper.getFactory().createParser(node), config, instance, "testProperty");
            fail("Expected JsonMappingException but no exception was thrown.");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    /**
     * Test for setting a value through a setter method using {@link SettableAnyProperty} with
     * a {@link TypeDeserializer}
     * @throws IOException
     * @throws JsonProcessingException
     */
    @org.junit.Test
    public void testSettableAnyPropertyTypeDeserializer() throws IOException, JsonProcessingException {
        Class<?> testClass = com.example.TestClass.class;
        BeanProperty property = new BeanProperty.Std(testClass, "testProperty", Integer[].class);
        AnnotatedMember setter = new AnnotatedField(testClass, "testProperty");
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class);
        KeyDeserializer keyDeserializer = mapper.getFactory().getBeanPropertyKeyDeserializer();
        JsonDeserializer<Object> valueDeserializer = mapper.findValueDeserializer(Integer[].class);
        TypeDeserializer typeDeserializer = mapper.getFactory().getPolymorphicTypeDeserializer(Map.class);
        SettableAnyProperty anyProperty = new PropertyNameBasedSettableAnyProperty(property, setter, javaType, keyDeserializer, valueDeserializer, typeDeserializer);

        Object instance = new Object();
        DeserializationConfig config = mapper.getDeserializationConfig();
        config.findValueDeserializer(anyProperty.getType());
        anyProperty.fixAccess(config);

        int[] testValues = {1, 2, 3};
        String testKey = "testKey";

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("key", testKey);
        node.put("value", mapper.valueToTree(testValues));

        anyProperty.deserializeAndSet(mapper.getFactory().createParser(node), config, instance, "testProperty");

        Map<String, Object> map = (Map<String, Object>) setter.getValue(instance);
        assert ArrayEquals(testValues, (Object[]) map.get(testKey));
    }

    /**
     * Test for setting a value through a setter method using {@link SettableAnyProperty} with
     * a {@link ReferenceableSettableAnyProperty} when the value is a reference
     * @throws IOException
     * @throws JsonProcessingException
     */
    @org.junit.Test
    public void testSettableAnyPropertyReference() throws IOException, JsonProcessingException {
        Class<?> testClass = com.example.TestClass.class;
        BeanProperty property = new BeanProperty.Std(testClass, "testProperty", String.class);
        AnnotatedMember setter = new AnnotatedField(testClass, "testProperty");
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class);
        KeyDeserializer keyDeserializer = mapper.getFactory().getBeanPropertyKeyDeserializer();
        JsonDeserializer<Object> valueDeserializer = mapper.findValueDeserializer(String.class);
        TypeDeserializer typeDeserializer = mapper.getFactory().getPolymorphicTypeDeserializer(Map.class);
        SettableAnyProperty anyProperty = new PropertyNameBasedSettableAnyProperty(property, setter, javaType, keyDeserializer, valueDeserializer, typeDeserializer);

        Object instance = new Object();
        DeserializationConfig config = mapper.getDeserializationConfig();
        config.findValueDeserializer(anyProperty.getType());
        anyProperty.fixAccess(config);

        String testValue = "testValue";
        String testKey = "testKey";

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("key", testKey);
        node.put("value", mapper.valueToTree("testValueRef"));

        anyProperty.deserializeAndSet(mapper.getFactory().createParser(node), config, instance, "testProperty");

        Map<String, Object> map = (Map<String, Object>) setter.getValue(instance);
        assertEquals(testValue, map.get(testKey));

        String refValue = "refValue";
        String refPropName = "refProperty";
        Object refInstance = new Object();
        SettableAnyProperty refAnyProperty = new PropertyNameBasedSettableAnyProperty(new BeanProperty.Std(testClass, refPropName, String.class), new AnnotatedField(testClass, refPropName), javaType, keyDeserializer, valueDeserializer, typeDeserializer);
        Object refInstanceValue = mapper.valueToTree(refValue);
        DeserializationConfig refConfig = mapper.getDeserializationConfig();
        refConfig.findValueDeserializer(anyProperty.getType());
        refAnyProperty.fixAccess(refConfig);
        mapper.setPropertyValue(instance, "testProperty", anyProperty);
        mapper.setPropertyValue(refInstance, "refProperty", refAnyProperty);

        ObjectMapper refMapper = new ObjectMapper();
        ObjectNode refNode = refMapper.createObjectNode();
        refNode.put("key", refKey);
        refNode.put("value", refInstanceValue);

        refAnyProperty.deserializeAndSet(refMapper.getFactory().createParser(refNode), refConfig, refInstance, refPropName);

        map = (Map<String, Object>) setter.getValue(instance);
        assertEquals(testValue, map.get(testKey));
        assertEquals(refValue, map.get("refProperty"));
    }
}

class ArrayEquals {
    public static void assertArrayEquals(Object expected, Object actual) {
        if (expected == null) {
            assertNull(actual);
        } else if (actual == null) {
            fail("Expected array but got null.");
        } else if (expected.getClass() != actual.getClass()) {
            fail("Expected array of class " + expected.getClass().getName() + " but got array of class " + actual.getClass().getName());
        } else if (expected.length != actual.length) {
            fail("Expected array of length " + expected.length + " but got array of length " + actual.length);
        } else {
            for (int i = 0; i < expected.length; ++i) {
                if (!expected[i].equals(actual[i])) {
                    fail("Expected array element at index " + i + " to equal " + expected[i] + " but got " + actual[i] + ".");
                }
            }
        }
    }
}

class TestClass {
    private Map<String, Object> testProperty;

    public Map<String, Object> getTestProperty() {
        return testProperty;
    }
}