package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;

public class JavaUtilCollectionsDeserializersTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testFindForCollection_SingletonSet() throws JsonMappingException {
        Set<Boolean> expectedSet = Collections.singleton(true);
        JavaType javaTypeSet = TypeFactory.defaultInstance().constructType(Set.class, Boolean.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(MAPPER.getContext(), javaTypeSet);
        Object deserialized = MAPPER.readValue("[]", javaTypeSet, deserializer);
        Set<Boolean> actualSet = (Set<Boolean>) deserialized;
        assertNotNull(actualSet);
        assertEquals(expectedSet, actualSet);
    }

    @Test
    public void testFindForCollection_SingletonList() throws JsonMappingException {
        List<Boolean> expectedList = Arrays.asList(true);
        JavaType javaTypeList = TypeFactory.defaultInstance().constructType(List.class, Boolean.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(MAPPER.getContext(), javaTypeList);
        Object deserialized = MAPPER.readValue("[]", javaTypeList, deserializer);
        List<Boolean> actualList = (List<Boolean>) deserialized;
        assertNotNull(actualList);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindForCollection_SingletonMap() throws JsonMappingException {
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("key1", "value1");
        JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(MAPPER.getContext(), javaTypeMap);
        Object deserialized = MAPPER.readValue("{}", javaTypeMap, deserializer);
        Map<String, String> actualMap = (Map<String, String>) deserialized;
        assertNotNull(actualMap);
        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testFindForCollection_UnmodifiableSet() throws JsonMappingException {
        Set<Boolean> expectedSet = Collections.unmodifiableSet(Collections.singleton(true));
        JavaType javaTypeSet = TypeReference.collectionType(Set.class, Boolean.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(MAPPER.getContext(), javaTypeSet);
        Object deserialized = MAPPER.readValue("[]", javaTypeSet, deserializer);
        Set<Boolean> actualSet = (Set<Boolean>) deserialized;
        assertNotNull(actualSet);
        assertEquals(expectedSet, actualSet);
    }

    @Test
    public void testFindForCollection_UnmodifiableList() throws JsonMappingException {
        List<Boolean> expectedList = Collections.unmodifiableList(Arrays.asList(true));
        JavaType javaTypeList = TypeReference.collectionType(List.class, Boolean.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(MAPPER.getContext(), javaTypeList);
        Object deserialized = MAPPER.readValue("[]", javaTypeList, deserializer);
        List<Boolean> actualList = (List<Boolean>) deserialized;
        assertNotNull(actualList);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindForCollection_UnmodifiableMap() throws JsonMappingException {
        Map<String, String> expectedMap = Collections.unmodifiableMap(new HashMap<String, String>() {{
            put("key1", "value1");
        }});
        JavaType javaTypeMap = MapType.constructMapType(String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(MAPPER.getContext(), javaTypeMap);
        Object deserialized = MAPPER.readValue("{}", javaTypeMap, deserializer);
        Map<String, String> actualMap = (Map<String, String>) deserialized;
        assertNotNull(actualMap);
        assertEquals(expectedMap, actualMap);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForCollection_EmptyList() throws JsonMappingException {
        JavaType javaTypeList = TypeReference.collectionType(List.class, Boolean.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(MAPPER.context(), javaTypeList);
        Object deserialized = MAPPER.readValue("[]", javaTypeList, deserializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForCollection_EmptySet() throws JsonMappingException {
        JavaType javaTypeSet = TypeReference.collectionType(Set.class, Boolean.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(MAPPER.context(), javaTypeSet);
        Object deserialized = MAPPER.readValue("[]", javaTypeSet, deserializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForCollection_EmptyMap() throws JsonMappingException {
        JavaType javaTypeMap = MapType.constructMapType(String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(MAPPER.context(), javaTypeMap);
        Object deserialized = MAPPER.readValue("{}", javaTypeMap, deserializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForCollection_InvalidType() throws JsonMappingException {
        JavaType javaTypeInvalid = TypeFactory.defaultInstance().constructType(String[].class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(MAPPER.context(), javaTypeInvalid);
        Object deserialized = MAPPER.readValue("[]", javaTypeInvalid, deserializer);
    }

    @Test
    public void testFindForMap_EmptyMap() throws JsonMappingException {
        Map<String, String> expectedMap = new HashMap<>();
        JavaType javaTypeMap = MapType.constructMapType(String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(MAPPER.context(), javaTypeMap);
        Object deserialized = MAPPER.readValue("{}", javaTypeMap, deserializer);
        Map<String, String> actualMap = (Map<String, String>) deserialized;
        assertNotNull(actualMap);
        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testFindForMap_SingletonMap() throws JsonMappingException {
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("key1", "value1");
        JavaType javaTypeMap = MapType.constructMapType(String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(MAPPER.context(), javaTypeMap);
        String json = "{\"key1\":\"value1\"}";
        Object deserialized = MAPPER.readValue(json, javaTypeMap, deserializer);
        Map<String, String> actualMap = (Map<String, String>) deserialized;
        assertNotNull(actualMap);
        assertEquals(expectedMap, actualMap);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForMap_EmptyCollectionType() throws JsonMappingException {
        JavaType javaTypeMap = MapType.constructMapType(String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(MAPPER.context(), javaTypeMap);
        String json = "{}";
        Object deserialized = MAPPER.readValue(json, javaTypeMap, deserializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForMap_InvalidType() throws JsonMappingException {
        JavaType javaTypeMap = MapType.constructMapType(String[].class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(MAPPER.context(), javaTypeMap);
        String json = "{\"key1\":\"value1\"}";
        Object deserialized = MAPPER.readValue(json, javaTypeMap, deserializer);
    }
}