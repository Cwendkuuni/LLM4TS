package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.*;

import static org.junit.Assert.*;

public class JavaUtilCollectionsDeserializersTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @org.junit.Test
    public void testFindForCollection_SingletonSet() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(Collection.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);

        Object result = mapper.readValue("{\"className\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$1\",\"value\":[true]}", new TypeReference<Object>() {});
        assertEquals(Collections.singleton(Boolean.TRUE), deserializer.deserialize(mapper, result, JavaType.constructCollectionType(Map.class, Object.class)));
    }

    @org.junit.Test
    public void testFindForCollection_SingletonList() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(Collection.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);

        Object result = mapper.readValue("{\"className\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$7\",\"value\":[\"hello\"]}", new TypeReference<Object>() {});
        assertEquals(Collections.singletonList("hello"), deserializer.deserialize(mapper, result, JavaType.constructCollectionType(Map.class, Object.class)));
    }

    @org.junit.Test
    public void testFindForCollection_SingletonMap() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);

        Object result = mapper.readValue("{\"className\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$3\",\"value\":{\"key\":\"key\",\"value\":\"value\"}}", new TypeReference<Object>() {});
        Map<String, String> expected = new HashMap<>();
        expected.put("key", "value");
        assertEquals(expected, deserializer.deserialize(mapper, result, JavaType.constructCollectionType(Map.class, Object.class)));
    }

    @org.junit.Test
    public void testFindForCollection_UnmodifiableSet() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(Collection.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);

        Object result = mapper.readValue("{\"className\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$4\",\"value\":[true]}", new TypeReference<Object>() {});
        assertNotNull(deserializer);
        assertInstanceOf(StdDelegatingDeserializer.class, deserializer);
        assertTrue(((StdDelegatingDeserializer) deserializer).getDelegate() instanceof JavaUtilCollectionsConverter);

        Object deserialized = deserializer.deserialize(mapper, result, JavaType.constructCollectionType(Map.class, Object.class));
        assertTrue(deserialized instanceof Collection);
        assertFalse(((Collection) deserialized).isEmpty());
        assertFalse(((Collection) deserialized).add(Boolean.FALSE));
        assertFalse(((Collection) deserialized).add(Boolean.TRUE));
    }

    @org.junit.Test
    public void testFindForCollection_UnmodifiableList() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(Collection.class, Integer.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);

        Object result = mapper.readValue("{\"className\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$5\",\"value\":[1]}", new TypeReference<Object>() {});
        assertNotNull(deserializer);
        assertInstanceOf(StdDelegatingDeserializer.class, deserializer);
        assertTrue(((StdDelegatingDeserializer) deserializer).getDelegate() instanceof JavaUtilCollectionsConverter);

        Object deserialized = deserializer.deserialize(mapper, result, JavaType.constructCollectionType(Map.class, Object.class));
        assertTrue(deserialized instanceof Collection);
        assertFalse(((Collection) deserialized).isEmpty());
        assertFalse(((Collection) deserialized).add(Integer.valueOf(2)));
        assertFalse(((Collection) deserialized).add(Integer.valueOf(1)));
    }

    @org.junit.Test
    public void testFindForCollection_UnmodifiableMap() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(Map.class, String.class, Integer.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);

        Object result = mapper.readValue("{\"className\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$6\",\"value\":{\"key\":\"key\",\"value\":1}}", new TypeReference<Object>() {});
        assertNotNull(deserializer);
        assertInstanceOf(StdDelegatingDeserializer.class, deserializer);
        assertTrue(((StdDelegatingDeserializer) deserializer).getDelegate() instanceof JavaUtilCollectionsConverter);

        Object deserialized = deserializer.deserialize(mapper, result, JavaType.constructCollectionType(Map.class, Object.class));
        assertTrue(deserialized instanceof Map);
        assertFalse(((Map) deserialized).isEmpty());
        try {
            ((Map) deserialized).put("key2", Integer.valueOf(2));
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            ((Map) deserialized).remove("key");
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testFindForMap_SingletonMap() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), type);

        Object result = mapper.readValue("{\"className\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$3\",\"value\":{\"key\":\"key\",\"value\":\"value\"}}", new TypeReference<Object>() {});
        Map<String, String> expected = new HashMap<>();
        expected.put("key", "value");
        assertEquals(expected, deserializer.deserialize(mapper, result, JavaType.constructCollectionType(Map.class, Object.class)));
    }

    @org.junit.Test
    public void testFindForMap_UnmodifiableMap() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), type);

        Object result = mapper.readValue("{\"className\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$6\",\"value\":{\"key\":\"key\",\"value\":\"value\"}}", new TypeReference<Object>() {});
        Map<String, String> expected = new HashMap<>();
        expected.put("key", "value");

        Object deserialized = deserializer.deserialize(mapper, result, JavaType.constructCollectionType(Map.class, Object.class));
        assertTrue(deserialized instanceof Map);
        assertFalse(((Map) deserialized).isEmpty());
        try {
            ((Map) deserialized).put("key2", "value2");
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            ((Map) deserialized).remove("key");
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testFindForCollection_UnknownType() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(Collection.class, Integer[].class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);

        Object result = mapper.readValue("{\"className\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$1\",\"value\":[1, 2, 3]}", new TypeReference<Object>() {});
        assertNull(deserializer);
    }

    @org.junit.Test
    public void testFindForMap_UnknownType() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(Map.class, Integer[].class, String[].class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), type);

        Object result = mapper.readValue("{\"className\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$3\",\"value\":{\"key\":[1, 2],\"value\":[\"value1\", \"value2\"]}}", new TypeReference<Object>() {});
        assertNull(deserializer);
    }

    @org.junit.Test
    public void testFindForCollection_EmptyList() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(Collection.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);

        Object result = mapper.readValue("{\"className\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$7\",\"value\":[]}", new TypeReference<Object>() {});
        assertEquals(Collections.emptyList(), deserializer.deserialize(mapper, result, JavaType.constructCollectionType(Map.class, Object.class)));
    }

    @org.junit.Test
    public void testFindForCollection_EmptySet() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(Collection.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);

        Object result = mapper.readValue("{\"className\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$1\",\"value\":{}}", new TypeReference<Object>() {});
        assertEquals(Collections.emptySet(), deserializer.deserialize(mapper, result, JavaType.constructCollectionType(Map.class, Object.class)));
    }

    @org.junit.Test
    public void testFindForMap_EmptyMap() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), type);

        Object result = mapper.readValue("{\"className\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$3\",\"value\":{}}", new TypeReference<Object>() {});
        assertEquals(Collections.emptyMap(), deserializer.deserialize(mapper, result, JavaType.constructCollectionType(Map.class, Object.class)));
    }

    @org.junit.Test
    public void testFindForCollection_Null() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(Collection.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);

        Object result = mapper.readValue("{}", new TypeReference<Object>() {});
        assertNull(deserializer);
    }

    @org.junit.Test
    public void testFindForMap_Null() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), type);

        Object result = mapper.readValue("{}", new TypeReference<Object>() {});
        assertNull(deserializer);
    }
}