package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeBindings;

import java.util.HashMap;
import java.util.Map;

public class MapTypeTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testConstruct() throws Exception {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        assertEquals("Map<java.lang.String, java.lang.Integer>", mapType.getRawClass().getName());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testConstructWithBinding() throws Exception {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);

        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(map);
        JavaType mapTypeFromJson = objectMapper.getTypeFactory().constructType(new TypeReference<Map<String, Integer>>() {}.getType());
        JavaType deserializedType = mapTypeFromJson.withValueHandler(mapType.getValueHandler());

        assertEquals(mapType, mapTypeFromJson.withValueHandler(mapType.getValueHandler()));
        JsonParser parser = objectMapper.getFactory().createParser(json);
        Map<String, Integer> deserialized = objectMapper.readValue(parser, deserializedType);
        assertEquals(map, deserialized);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidBindings() throws Exception {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = new TypeBindings() {{
            bindValueType(String.class, long.class);
        }};
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
    }

    @Test
    public void testNarrow() throws Exception {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType baseType = new TypeReference<Map<String, Object>>() {}.getType();
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        JavaType narrowedType = mapType._narrow(Map.class);
        assertEquals(MapType.class, narrowedType.getRawClass());
        assertEquals(keyType, narrowedType.getKeyType());
        assertEquals(valueType, narrowedType.getValueType());
    }

    @Test
    public void testWithTypeHandler() throws Exception {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType valueHandler = new TypeReference<ObjectWriter>() {}.getType();
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        JavaType withTypeHandler = mapType.withTypeHandler(valueHandler);
        assertEquals(MapType.class, withTypeHandler.getRawClass());
        assertEquals(keyType, withTypeHandler.getKeyType());
        assertEquals(valueType, withTypeHandler.getValueType());
        assertEquals(valueHandler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() throws Exception {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer[]>() {}.getType();
        JavaType valueHandler = new TypeReference<ObjectWriter>() {}.getType();
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        JavaType withContentTypeHandler = mapType.withContentTypeHandler(valueHandler);
        assertEquals(MapType.class, withContentTypeHandler.getRawClass());
        assertEquals(keyType, withContentTypeHandler.getKeyType());
        assertEquals(valueType, withContentTypeHandler.getValueType());
        assertEquals(valueHandler, withContentTypeHandler.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() throws Exception {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType valueHandler = new TypeReference<NumberInput>() {}.getType();
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        JavaType withValueHandler = mapType.withValueHandler(valueHandler);
        assertEquals(MapType.class, withValueHandler.getRawClass());
        assertEquals(keyType, withValueHandler.getKeyType());
        assertEquals(valueType, withValueHandler.getValueType());
        assertEquals(valueHandler, withValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() throws Exception {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer[]>() {}.getType();
        JavaType valueHandler = new TypeReference<NumberInput>() {}.getType();
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        JavaType withContentValueHandler = mapType.withContentValueHandler(valueHandler);
        assertEquals(MapType.class, withContentValueHandler.getRawClass());
        assertEquals(keyType, withContentValueHandler.getKeyType());
        assertEquals(valueType, withContentValueHandler.getValueType());
        assertEquals(valueHandler, withContentValueHandler.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() throws Exception {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        JavaType withStaticTyping = mapType.withStaticTyping();
        assertEquals(MapType.class, withStaticTyping.getRawClass());
        assertTrue(withStaticTyping.isStatic());
        assertEquals(keyType, withStaticTyping.getKeyType());
        assertEquals(valueType, withStaticTyping.getValueType());
    }

    @Test
    public void testWithContentType() throws Exception {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer[]>() {}.getType();
        JavaType contentType = new TypeReference<Integer[]>() {}.getType();
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        JavaType withContentType = mapType.withContentType(contentType);
        assertEquals(MapType.class, withContentType.getRawClass());
        assertEquals(keyType, withContentType.getKeyType());
        assertEquals(contentType, withContentType.getValueType());
    }

    @Test
    public void testWithKeyType() throws Exception {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType1 = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType keyType2 = new TypeReference<Integer>() {}.getType();
        MapType mapType = new MapType(rawType, null, null, null, keyType1, valueType);
        JavaType withKeyType = mapType.withKeyType(keyType2);
        assertEquals(MapType.class, withKeyType.getRawClass());
        assertEquals(keyType2, withKeyType.getKeyType());
        assertEquals(valueType, withKeyType.getValueType());
    }

    @Test
    public void testRefine() throws Exception {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        JavaType refinedType = mapType.refine(Map.class, null, null, null);
        assertEquals(MapType.class, refinedType.getRawClass());
        assertEquals(keyType, refinedType.getKeyType());
        assertEquals(valueType, refinedType.getValueType());
    }

    @Test
    public void testWithKeyTypeHandler() throws Exception {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object valueHandler = new Object();
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        JavaType withKeyTypeHandler = mapType.withKeyTypeHandler(valueHandler);
        assertEquals(MapType.class, withKeyTypeHandler.getRawClass());
        assertEquals(keyType, withKeyTypeHandler.getKeyType());
        assertEquals(valueType, withKeyTypeHandler.getValueType());
        assertNull(withKeyTypeHandler.getKeyTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() throws Exception {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object keyValueHandler = new Object();
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        JavaType withKeyValueHandler = mapType.withKeyValueHandler(keyValueHandler);
        assertEquals(MapType.class, withKeyValueHandler.getRawClass());
        assertEquals(keyType, withKeyValueHandler.getKeyType());
        assertEquals(valueType, withKeyValueHandler.getValueType());
        assertNull(withKeyValueHandler.getKeyValueHandler());
    }
}