package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.*;

import static org.junit.Assert.*;

public class JavaUtilCollectionsDeserializersTest {

    private ObjectMapper objectMapper;

    @org.junit.Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @org.junit.Test
    public void testDeserializeSingletonSet() throws JsonMappingException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);
        String json = "{\"class\":\"java.util.Collections$SingletonSet\", \"elements\":[true]}";

        Set<Boolean> result = objectMapper.readValue(json, javaType);
        assertEquals(Collections.singleton(true), result);
    }

    @org.junit.Test
    public void testDeserializeUnmodifiableSet() throws JsonMappingException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);
        String json = "{\"class\":\"java.util.Collections$UnmodifiableSet\", \"elements\":[true]}";

        Set<Boolean> result = objectMapper.readValue(json, javaType);
        assertTrue(result instanceof Collections.UnmodifiableSet);
        assertEquals(Collections.singleton(true), result);
        try {
            result.add(false);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testDeserializeSingletonList() throws JsonMappingException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);
        String json = "{\"class\":\"java.util.Collections$SingletonList\", \"elements\":[true]}";

        List<Boolean> result = objectMapper.readValue(json, javaType);
        assertEquals(Collections.singletonList(true), result);
    }

    @org.junit.Test
    public void testDeserializeUnmodifiableList() throws JsonMappingException {
        JavaType javaType = TypeReference.of(new TypeReference<List<Boolean>>() {});
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);
        String json = "{\"class\":\"java.util.Collections$UnmodifiableList\", \"elements\":[true]}";

        List<Boolean> result = objectMapper.readValue(json, javaType);
        assertTrue(result instanceof Collections.UnmodifiableList);
        assertEquals(Collections.singletonList(true), result);
        try {
            result.add(false);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testDeserializeSingletonMap() throws JsonMappingException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getDeserializationContext(), javaType);
        String json = "{\"class\":\"java.util.Collections$SingletonMap\", \"entry\":{\"key\":\"a\", \"value\":\"b\"}}";

        Map<String, String> result = objectMapper.readValue(json, javaType);
        assertEquals(Collections.singletonMap("a", "b"), result);
    }

    @org.junit.Test
    public void testDeserializeUnmodifiableMap() throws JsonMappingException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getDeserializationContext(), javaType);
        String json = "{\"class\":\"java.util.Collections$UnmodifiableMap\", \"entry\":{\"key\":\"a\", \"value\":\"b\"}}";

        Map<String, String> result = objectMapper.readValue(json, javaType);
        assertTrue(result instanceof Collections.UnmodifiableMap);
        assertEquals(Collections.singletonMap("a", "b"), result);
        try {
            result.put("c", "d");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testDeserializeEmptyAsList() throws JsonMappingException {
        JavaType javaType = TypeReference.of(new TypeReference<List<Object>>() {});
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);
        String json = "{\"class\":\"java.util.Arrays$asList\"}";

        List<Object> result = objectMapper.readValue(json, javaType);
        assertTrue(result instanceof ArrayList);
        assertEquals(0, result.size());
    }

    @org.junit.Test
    public void testDeserializeNullAsList() throws JsonMappingException {
        JavaType javaType = TypeReference.of(new TypeReference<List<Object>>() {});
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);
        String json = "null";

        List<Object> result = objectMapper.readValue(json, javaType);
        assertNull(result);
    }
}