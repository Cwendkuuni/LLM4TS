package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFindForCollectionSingletonSet() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers
                .converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, javaType, Set.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        assertEquals(converter, deserializer.getDelegate());
    }

    @Test
    public void testFindForCollectionSingletonList() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers
                .converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, javaType, List.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        assertEquals(converter, deserializer.getDelegate());
    }

    @Test
    public void testFindForCollectionSingleMap() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers
                .converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, javaType, Map.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        assertEquals(converter, deserializer.getDelegate());
    }

    @Test
    public void testFindForCollectionUnmodifiableSet() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollectionUnmodifiableList() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollectionUnmodifiableMap() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        assertNotNull(deserializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForCollectionInvalidType() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Integer.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForMapSingletonMap() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers
                .converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, javaType, Map.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);
        assertEquals(converter, deserializer.getDelegate());
    }

    @Test
    public void testFindForMapUnmodifiableMap() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);
        assertNotNull(deserializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForMapInvalidType() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Integer.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);
        assertNotNull(deserializer);
    }

    @Test
    public void testDeserializeSingletonSet() throws JsonMappingException {
        String json = "{\"class\":\"java.util.Collections$singletonSet\",\"size\":1,\"iterator\":[true]}";
        Set<Boolean> set = mapper.readValue(json, Set.class);
        assertEquals(Collections.singleton(Boolean.TRUE), set);
    }

    @Test
    public void testDeserializeSingletonList() throws JsonMappingException {
        String json = "{\"class\":\"java.util.Collections$singletonList\",\"size\":1,\"list\":[true]}";
        List<Boolean> list = mapper.readValue(json, List.class);
        assertEquals(Collections.singletonList(Boolean.TRUE), list);
    }

    @Test
    public void testDeserializeSingletonMap() throws JsonMappingException {
        String json = "{\"class\":\"java.util.Collections$singletonMap\",\"size\":1,\"entrySet\":[{\"key\":\"a\",\"value\":\"b\"}]}";
        Map<String, String> map = mapper.readValue(json, Map.class);
        assertEquals(Collections.singletonMap("a", "b"), map);
    }

    @Test
    public void testDeserializeUnmodifiableSet() throws JsonMappingException {
        String json = "[true]";
        Set<Boolean> set = mapper.readValue(json, Set.class);
        assertEquals(Collections.unmodifiableSet(Arrays.asList(Boolean.TRUE)), set);
    }

    @Test
    public void testDeserializeUnmodifiableList() throws JsonMappingException {
        String json = "[true]";
        List<Boolean> list = mapper.readValue(json, List.class);
        assertEquals(Collections.unmodifiableList(Arrays.asList(Boolean.TRUE)), list);
    }

    @Test
    public void testDeserializeUnmodifiableMap() throws JsonMappingException {
        String json = "[{\"k\":\"a\",\"v\":\"b\"}]";
        Map<String, String> map = mapper.readValue(json, Map.class);
        assertEquals(Collections.unmodifiableMap(map), map);
    }
}