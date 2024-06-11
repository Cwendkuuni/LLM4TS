package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class JavaUtilCollectionsDeserializersTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFindForCollection_SingletonSet() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);

        Object result = mapper.readValue("[true]", DeserializationContext.construct(type, null, deserializer));
        Assertions.assertEquals(Collections.singleton(true), result);
    }

    @Test
    public void testFindForCollection_SingletonList() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);

        Object result = mapper.readValue("[true]", DeserializationContext.construct(type, null, deserializer));
        Assertions.assertEquals(Collections.singletonList(true), result);
    }

    @Test
    public void testFindForCollection_SingletonMap() throws Exception {
        JavaType keyType = mapper.getTypeFactory().constructType(String.class);
        JavaType valueType = mapper.getTypeFactory().constructType(String.class);
        JavaType type = mapper.getTypeFactory().constructType(Map.class, keyType, valueType);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);

        Object result = mapper.readValue("{\"k\":\"v\"}", DeserializationContext.construct(type, null, deserializer));
        Map<String, String> expected = Collections.singletonMap("k", "v");
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testFindForCollection_UnmodifiableSet() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);

        Object result = mapper.readValue(Collections.unmodifiableSet(Collections.singleton(true)).toString(), DeserializationContext.construct(type, null, deserializer));
        Assertions.assertEquals(Collections.unmodifiableSet(Collections.singleton(true)), result);
    }

    @Test
    public void testFindForCollection_UnmodifiableList() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);

        Object result = mapper.readValue(Collections.unmodifiableList(Collections.singletonList(true)).toString(), DeserializationContext.construct(type, null, deserializer));
        Assertions.assertEquals(Collections.unmodifiableList(Collections.singletonList(true)), result);
    }

    @Test
    public void testFindForCollection_UnmodifiableMap() throws Exception {
        JavaType keyType = mapper.getTypeFactory().constructType(String.class);
        JavaType valueType = mapper.getTypeFactory().constructType(String.class);
        JavaType type = mapper.getTypeFactory().constructType(Map.class, keyType, valueType);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);

        Object result = mapper.readValue(Collections.unmodifiableMap(Collections.singletonMap("k", "v")).toString(), DeserializationContext.construct(type, null, deserializer));
        Map<String, String> expected = Collections.unmodifiableMap(Collections.singletonMap("k", "v"));
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testFindForCollection_InvalidCollectionType() throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(List.class, String.class, Integer.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);

        Assertions.assertNull(deserializer);
    }

    @Test
    public void testFindForMap_SingletonMap() throws Exception {
        JavaType keyType = mapper.getTypeFactory().constructType(String.class);
        JavaType valueType = mapper.getTypeFactory().constructType(String.class);
        JavaType type = mapper.getTypeFactory().constructType(Map.class, keyType, valueType);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), type);

        Object result = mapper.readValue("{\"k\":\"v\"}", DeserializationContext.construct(type, null, deserializer));
        Map<String, String> expected = Collections.singletonMap("k", "v");
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testFindForMap_UnmodifiableMap() throws Exception {
        JavaType keyType = mapper.getTypeFactory().constructType(String.class);
        JavaType valueType = mapper.getTypeFactory().constructType(String.class);
        JavaType type = mapper.getTypeFactory().constructType(Map.class, keyType, valueType);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), type);

        Object result = mapper.readValue(Collections.unmodifiableMap(Collections.singletonMap("k", "v")).toString(), DeserializationContext.construct(type, null, deserializer));
        Map<String, String> expected = Collections.unmodifiableMap(Collections.singletonMap("k", "v"));
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testFindForMap_InvalidMapType() throws Exception {
        JavaType keyType = mapper.getTypeFactory().constructType(String.class);
        JavaType valueType = mapper.getTypeFactory().constructType(List.class);
        JavaType type = mapper.getTypeFactory().constructType(Map.class, keyType, valueType);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), type);

        Assertions.assertNull(deserializer);
    }
}