package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Map;

public class JavaUtilCollectionsDeserializersTest {

    private ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testFindForCollection_SingletonSet() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
        Assert.assertNotNull(deserializer);

        String json = "{\"class\":\"java.util.Collections$SingletonSet\", \"elements\":[true]}";
        Set<Boolean> set = mapper.readValue(json, Set.class);
        Assert.assertEquals(1, set.size());
        Assert.assertTrue(set.contains(Boolean.TRUE));

        List<Byte> list = mapper.readValue(mapper.writeValueAsString(set), new TypeReference<List<Byte>>(){});
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(Byte.parseByte("1"), list.get(0));

        Object deserialized = mapper.readValue(json, Object.class, deserializer);
        Assert.assertEquals(set, deserialized);
    }

    @Test
    public void testFindForCollection_SingletonList() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
        Assert.assertNotNull(deserializer);

        String json = "{\"class\":\"java.util.Collections$SingletonList\", \"element\":true}";
        List<Boolean> list = mapper.readValue(json, List.class);
        Assert.assertEquals(1, list.size());
        Assert.assertTrue(list.get(0).booleanValue());

        Object deserialized = mapper.readValue(json, Object.class, deserializer);
        Assert.assertEquals(list, deserialized);
    }

    @Test
    public void testFindForCollection_SingletonMap() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
        Assert.assertNotNull(deserializer);

        String json = "{\"class\":\"java.util.Collections$SingletonMap\", \"value\":{\"a\":\"b\"}}";
        Map<String, String> map = mapper.readValue(json, Map.class);
        Assert.assertEquals(1, map.size());
        Assert.assertEquals("b", map.get("a"));

        Object deserialized = mapper.readValue(json, Object.class, deserializer);
        Assert.assertEquals(map, deserialized);
    }

    @Test
    public void testFindForCollection_UnmodifiableSet() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Set.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
        Assert.assertNotNull(deserializer);

        Set<String> set = Collections.unmodifiableSet(Arrays.asList("a", "b"));
        String json = mapper.writeValueAsString(set);

        Map<String, Object> deserializedMap = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
        Assert.assertEquals(1, deserializedMap.size());
        Assert.assertTrue(deserializedMap.containsKey("class"));
        Assert.assertEquals(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET.getName(), deserializedMap.get("class"));
        Assert.assertTrue(deserializedMap.containsKey("value"));
        Assert.assertEquals(set, mapper.readValue(mapper.writeValueAsString(deserializedMap.get("value")), Set.class));
    }

    @Test
    public void testFindForCollection_UnmodifiableList() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(List.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
        Assert.assertNotNull(deserializer);

        List<String> list = Collections.unmodifiableList(Arrays.asList("a", "b"));
        String json = mapper.writeValueAsString(list);

        Map<String, Object> deserializedMap = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
        Assert.assertEquals(1, deserializedMap.size());
        Assert.assertTrue(deserializedMap.containsKey("class"));
        Assert.assertEquals(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST.getName(), deserializedMap.get("class"));
        Assert.assertTrue(deserializedMap.containsKey("value"));
        Assert.assertEquals(list, mapper.readValue(mapper.writeValueAsString(deserializedMap.get("value")), List.class));
    }

    @Test
    public void testFindForCollection_UnmodifiableMap() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
        Assert.assertNotNull(deserializer);

        Map<String, String> map = Collections.unmodifiableMap(Collections.singletonMap("a", "b"));
        String json = mapper.writeValueAsString(map);

        Map<String, Object> deserializedMap = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
        Assert.assertEquals(1, deserializedMap.size());
        Assert.assertTrue(deserializedMap.containsKey("class"));
        Assert.assertEquals(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP.getName(), deserializedMap.get("class"));
        Assert.assertTrue(deserializedMap.containsKey("value"));
        Assert.assertEquals(map, mapper.readValue(mapper.writeValueAsString(deserializedMap.get("value")), Map.class));
    }

    @Test
    public void testFindForCollection_InvalidSingletonList() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
        Assert.assertNotNull(deserializer);

        String json = "{\"class\":\"java.util.Collections$SingletonList\", \"elements\":[true, false]}"; // invalid single element list
        try {
            mapper.readValue(json, Object.class, deserializer);
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("Unexpected number of elements (2) in input: was expecting exactly 1", e.getMessage());
        }
    }

    @Test
    public void testFindForCollection_InvalidSingletonSet() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
        Assert.assertNotNull(deserializer);

        String json = "{\"class\":\"java.util.Collections$SingletonSet\", \"elements\":[true, false]}"; // invalid single element set
        try {
            mapper.readValue(json, Object.class, deserializer);
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("Unexpected number of elements (2) in input: was expecting exactly 1", e.getMessage());
        }
    }

    @Test
    public void testFindForCollection_InvalidUnmodifiableList() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(List.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
        Assert.assertNotNull(deserializer);

        String json = "{\"class\":\"java.util.Collections$UnmodifiableList\", \"value\":[\"a\", \"b\", \"c\"]}"; // invalid unmodifiable list
        try {
            mapper.readValue(json, Object.class, deserializer);
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("Unexpected number of elements (3) in value", e.getMessage());
        }
    }

    @Test
    public void testFindForCollection_InvalidUnmodifiableSet() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Set.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
        Assert.assertNotNull(deserializer);

        String json = "{\"class\":\"java.util.Collections$UnmodifiableSet\", \"value\":[\"a\", \"b\", \"c\"]}"; // invalid unmodifiable set
        try {
            mapper.readValue(json, Object.class, deserializer);
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("Unexpected number of elements (3) in value", e.getMessage());
        }
    }

    @Test
    public void testFindForCollection_UnknownType() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, Integer.TYPE, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
        Assert.assertNull(deserializer);
    }
}