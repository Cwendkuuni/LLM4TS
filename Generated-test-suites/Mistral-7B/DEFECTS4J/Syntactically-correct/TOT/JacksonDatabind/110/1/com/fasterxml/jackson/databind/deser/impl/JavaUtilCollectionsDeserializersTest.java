package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.testng.annotations.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Test class for {@link JavaUtilCollectionsDeserializers}
 */
public class JavaUtilCollectionsDeserializersTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFindForCollectionSingletonSet() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);
        Object deserialized = mapper.readValue("[]", type);
        assertEquals(deserialized, JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET);
        assertEquals(deserializer.getClass().getSimpleName(), StdDelegatingDeserializer.class.getSimpleName());
    }

    @Test
    public void testFindForCollectionSingletonList() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);
        Object deserialized = mapper.readValue("[]", type);
        assertEquals(deserialized, JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST);
        assertEquals(deserializer.getClass().getSimpleName(), StdDelegatingDeserializer.class.getSimpleName());
    }

    @Test
    public void testFindForCollectionSingletonMap() throws Exception {
        JavaType keyType = mapper.getTypeFactory().constructType(String.class);
        JavaType valueType = mapper.getTypeFactory().constructType(String.class);
        JavaType type = mapper.getTypeFactory().constructType(Map.class, keyType, valueType);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);
        Object deserialized = mapper.readValue("{\"k\":\"v\"}", type);
        assertEquals(deserialized, JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP);
        assertEquals(deserializer.getClass().getSimpleName(), StdDelegatingDeserializer.class.getSimpleName());
    }

    @Test
    public void testFindForCollectionUnmodifiableSet() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);
        Object deserialized = mapper.readValue("[true]", type);
        assertTrue(deserialized instanceof Set);
        assertTrue(((Set<?>) deserialized).getClass().equals(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET));
        assertEquals(deserializer.getClass().getSimpleName(), StdDelegatingDeserializer.class.getSimpleName());
    }

    @Test
    public void testFindForCollectionUnmodifiableList() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(List.class, Integer.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);
        Object deserialized = mapper.readValue("[1, 2, 3]", type);
        assertTrue(deserialized instanceof List);
        assertTrue(((List<?>) deserialized).getClass().equals(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST));
        assertEquals(deserializer.getClass().getSimpleName(), StdDelegatingDeserializer.class.getSimpleName());
    }

    @Test
    public void testFindForCollectionUnmodifiableMap() throws Exception {
        JavaType keyType = mapper.getTypeFactory().constructType(String.class);
        JavaType valueType = mapper.getTypeFactory().constructType(String.class);
        JavaType type = mapper.getTypeFactory().constructType(Map.class, keyType, valueType);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);
        Object deserialized = mapper.readValue("{\"k1\":\"v1\", \"k2\":\"v2\"}", type);
        assertTrue(deserialized instanceof Map);
        assertTrue(((Map<?,?>) deserialized).getClass().equals(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP));
        assertEquals(deserializer.getClass().getSimpleName(), StdDelegatingDeserializer.class.getSimpleName());
    }

    @Test
    public void testFindForMapSingletonMap() throws Exception {
        JavaType keyType = mapper.getTypeFactory().constructType(String.class);
        JavaType valueType = mapper.getTypeFactory().constructType(String.class);
        JavaType type = mapper.getTypeFactory().constructType(Map.class, keyType, valueType);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), type);
        Object deserialized = mapper.readValue("{\"k\":\"v\"}", type);
        assertEquals(deserialized, JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP);
        assertEquals(deserializer.getClass().getSimpleName(), StdDelegatingDeserializer.class.getSimpleName());
    }

    @Test
    public void testFindForMapUnmodifiableMap() throws Exception {
        JavaType keyType = mapper.getTypeFactory().constructType(String.class);
        JavaType valueType = mapper.getTypeFactory().constructType(String.class);
        JavaType type = mapper.getTypeFactory().constructType(Map.class, keyType, valueType);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), type);
        Object deserialized = mapper.readValue("{\"k1\":\"v1\", \"k2\":\"v2\"}", type);
        assertTrue(deserialized instanceof Map);
        assertTrue(((Map<?,?>) deserialized).getClass().equals(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP));
        assertEquals(deserializer.getClass().getSimpleName(), StdDelegatingDeserializer.class.getSimpleName());
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForCollectionUnexpectedType() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(int[].class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForMapUnexpectedType() throws Exception {
        JavaType keyType = mapper.getTypeFactory().constructType(int[].class);
        JavaType valueType = mapper.getTypeFactory().constructType(String.class);
        JavaType type = mapper.getTypeFactory().constructType(Map.class, keyType, valueType);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), type);
    }
}