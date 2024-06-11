package com.example.junit;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testConstructor() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object[].class);
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);

        assertEquals(mapType, mapTypeInstance.getType());
        assertEquals(bindings, mapTypeInstance.getBindings());
        assertEquals(mapType.getRawClass(), mapTypeInstance.getRawClass());
        assertEquals("Map<java.lang.String, java.lang.Integer>", mapTypeInstance.toString());
    }

    @Test
    public void testDeprecatedConstruct() {
        Class<Map<String, Integer>> mapType = Map.class;
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, null, _bogusSuperClass(mapType.getRawClass()), null, keyType, valueType);

        assertEquals(mapType, mapTypeInstance.getType());
        assertEquals(null, mapTypeInstance.getBindings());
        assertEquals(mapType.getRawClass(), mapTypeInstance.getRawClass());
        assertEquals("Map<java.lang.String, java.lang.Integer>", mapTypeInstance.toString());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object[].class);
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);

        ObjectMapper mapperWithDefaultHandler = new ObjectMapper();
        Map<String, Integer> map = mapperWithDefaultHandler.readValue(new ObjectMapper().getFactory().createValueStream(new ByteArrayInputStream("{}".getBytes())), mapTypeInstance);

        mapTypeInstance = mapTypeInstance.withContentTypeHandler(String.class);

        ObjectMapper mapperWithCustomHandler = new ObjectMapper();
        mapperWithCustomHandler.registerModule(new SimpleModule().addSerializer(String.class, new StringSerializer()));
        mapperWithCustomHandler.registerModule(new SimpleModule().addDeserializer(Map.class, new MapDeserializer()));

        Map<String, Integer> mapWithCustomHandler = mapperWithCustomHandler.readValue(new ObjectMapper().getFactory().createValueStream(new ByteArrayInputStream("{\"key\":\"value\"}".getBytes())), mapTypeInstance);

        assertEquals(map.get("key"), mapWithCustomHandler.get("key"));
    }
}