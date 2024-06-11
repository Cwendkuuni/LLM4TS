package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection_SingletonList() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"class\":\"java.util.Arrays\\$ArrayList\", \"size\":1, \"value\":[true]}";
        JavaType type = mapper.getTypeFactory().constructType(new TypeReference<List<Boolean>>(){}.getType());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        Object result = mapper.treeToValue(mapper.readerForTree(deserializer).readValue(node), null);
        List<Boolean> list = (List<Boolean>) result;
        assertEquals(Collections.singletonList(Boolean.TRUE), list);
    }

    @Test
    public void testFindForCollection_SingletonSet() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"class\":\"java.util.Collections$SingletonSet\", \"element\":true}";
        JavaType type = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        Object result = mapper.treeToValue(mapper.readerForTree(deserializer).readValue(node), null);
        Set<Boolean> set = (Set<Boolean>) result;
        assertEquals(Collections.singleton(Boolean.TRUE), set);
    }

    @Test
    public void testFindForCollection_UnmodifiableList() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"class\":\"java.util.Collections$UnmodifiableList\", \"list\":[true]}";
        JavaType type = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        Object result = mapper.treeToValue(mapper.readerForTree(deserializer).readValue(node), null);
        List<Boolean> list = (List<Boolean>) result;
        assertEquals(Collections.unmodifiableList(Arrays.asList(Boolean.TRUE)), list);
    }

    @Test
    public void testFindForCollection_UnmodifiableSet() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"class\":\"java.util.Collections$UnmodifiableSet\", \"set\":[true]}";
        JavaType type = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        Object result = mapper.treeToValue(mapper.readerForTree(deserializer).readValue(node), null);
        Set<Boolean> set = (Set<Boolean>) result;
        assertEquals(Collections.unmodifiableSet(Arrays.asList(Boolean.TRUE)), set);
    }

    @Test
    public void testFindForMap_SingletonMap() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"class\":\"java.util.Collections$SingletonMap\", \"key\":\"a\", \"value\":\"b\"}";
        JavaType type = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), type);
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        Object result = mapper.treeToValue(mapper.readerForTree(deserializer).readValue(node), null);
        Map<String, String> map = (Map<String, String>) result;
        assertEquals(Collections.singletonMap("a", "b"), map);
    }

    @Test
    public void testFindForMap_UnmodifiableMap() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"class\":\"java.util.Collections$UnmodifiableMap\", \"map\":{\"a\":\"b\"}}";
        JavaType type = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), type);
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        Object result = mapper.treeToValue(mapper.readerForTree(deserializer).readValue(node), null);
        Map<String, String> map = (Map<String, String>) result;
        assertEquals(Collections.unmodifiableMap(Collections.singletonMap("a", "b")), map);
    }

    @Test
    public void testFindForCollection_InvalidCollection() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"class\":\"java.util.ArrayList\", \"size\":1, \"value\":[true]}";
        JavaType type = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        try {
            mapper.readerForTree(deserializer).readValue(node);
        } catch (JsonMappingException ex) {
            assertInstanceOf(JsonMappingException.class, ex);
            assertEquals("Unable to handle collections with raw type: java.util.ArrayList", ex.getMessage());
        }
        assertNull(JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type));
    }

    @Test
    public void testFindForMap_InvalidMap() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"class\":\"java.util.HashMap\", \"size\":1, \"entryMap\":{\"a\":\"b\"}}";
        JavaType type = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), type);
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        try {
            mapper.readerForTree(deserializer).readValue(node);
        } catch (JsonMappingException ex) {
            assertInstanceOf(JsonMappingException.class, ex);
            assertEquals("Unable to handle collections with raw type: java.util.HashMap<K, V>", ex.getMessage());
        }
        assertNull(JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), type));
    }
}