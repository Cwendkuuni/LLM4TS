package com.yourpackage.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JavaUtilCollectionsDeserializersTest {

    private ObjectMapper objectMapper;

    @org.junit.Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @org.junit.Test
    public void testFindForCollection_SingletonList() throws JsonMappingException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(new TypeReference<List<Integer>>() {}.getType());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);

        assertNotNull(deserializer);
        assertEquals(List.class, deserializer.getTargetValueType(null));
    }

    @org.junit.Test
    public void testFindForCollection_SingletonSet() throws JsonMappingException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(new TypeReference<Set<Integer>>() {}.getType());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);

        assertNotNull(deserializer);
        assertEquals(Set.class, deserializer.getTargetValueType(null));
    }

    @org.junit.Test
    public void testFindForCollection_UnmodifiableList() throws JsonMappingException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(Collections.unmodifiableList(Arrays.asList(1, 2, 3)).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);

        assertNotNull(deserializer);
        assertEquals(List.class, deserializer.getTargetValueType(null));
    }

    @org.junit.Test
    public void testFindForCollection_UnmodifiableSet() throws JsonMappingException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(Collections.unmodifiableSet(Arrays.asList(1, 2, 3)).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);

        assertNotNull(deserializer);
        assertEquals(Set.class, deserializer.getTargetValueType(null));
    }

    @org.junit.Test
    public void testFindForMap_SingletonMap() throws JsonMappingException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(new TypeReference<Map<String, Integer>>() {}.getType());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getDeserializationContext(), javaType);

        assertNotNull(deserializer);
        assertEquals(Map.class, deserializer.getTargetValueType(null));
    }

    @org.junit.Test
    public void testFindForMap_UnmodifiableMap() throws JsonMappingException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(Collections.unmodifiableMap(Map.of("key", 1)).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getDeserializationContext(), javaType);

        assertNotNull(deserializer);
        assertEquals(Map.class, deserializer.getTargetValueType(null));
    }
}