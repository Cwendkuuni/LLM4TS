package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.construct(String.class, bindings);
        JavaType valueType = JavaType.construct(Integer.class, bindings);
        JavaType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testConstructWithTypeBindings() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = JavaType.construct(String.class, bindings);
        JavaType valueType = JavaType.construct(Integer.class, bindings);
        JavaType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testConstructWithSuperClass() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.construct(String.class, bindings);
        JavaType valueType = JavaType.construct(Integer.class, bindings);
        JavaType superClass = JavaType.construct(LinkedHashMap.class, bindings);
        JavaType mapType = MapType.construct(rawType, bindings, superClass, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(superClass, mapType.getSuperClass());
    }

    @Test
    public void testConstructWithSuperInterfaces() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.construct(String.class, bindings);
        JavaType valueType = JavaType.construct(Integer.class, bindings);
        JavaType[] superInterfaces = new JavaType[]{Collection.class};
        JavaType mapType = MapType.construct(rawType, bindings, null, superInterfaces, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertArrayEquals(new JavaType[]{Collection.class}, mapType.getSuperInterfaces());
    }

    @Test
    public void testConstructWithValueHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.construct(String.class, bindings);
        JavaType valueType = JavaType.construct(Integer.class, bindings);
        Object valueHandler = new Object();
        JavaType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType, valueHandler, null, false);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(valueHandler, mapType.getValueHandler());
    }

    @Test
    public void testConstructWithTypeHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.construct(String.class, bindings);
        JavaType valueType = JavaType.construct(Integer.class, bindings);
        Object typeHandler = new Object();
        JavaType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType, null, typeHandler, false);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(typeHandler, mapType.getTypeHandler());
    }

    @Test
    public void testNarrow() {
        Class<?> rawType = HashMap.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.construct(String.class, bindings);
        JavaType valueType = JavaType.construct(Integer.class, bindings);
        JavaType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        JavaType narrowedType = mapType.narrow(LinkedHashMap.class);
        assertEquals(LinkedHashMap.class, narrowedType.getRawClass());
        assertEquals(keyType, narrowedType.getKeyType());
        assertEquals(valueType, narrowedType.getValueType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrowWithUnsupportedType() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.construct(String.class, bindings);
        JavaType valueType = JavaType.construct(Integer.class, bindings);
        JavaType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        JavaType narrowedType = mapType.narrow(ArrayList.class);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.construct(String.class, bindings);
        JavaType valueType = JavaType.construct(Integer.class, bindings);
        Object typeHandler = new Object();
        JavaType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        JavaType newMapType = mapType.withTypeHandler(typeHandler);
        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(keyType, newMapType.getKeyType());
        assertEquals(valueType, newMapType.getValueType());
        assertEquals(typeHandler, newMapType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.construct(String.class, bindings);
        JavaType valueType = JavaType.construct(Integer.class, bindings);
        Object contentTypeHandler = new Object();
        JavaType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        JavaType newMapType = mapType.withContentTypeHandler(contentTypeHandler);
        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(keyType, newMapType.getKeyType());
        JavaType newValueType = newMapType.getValueType();
        assertEquals(valueType, newValueType);
        assertEquals(contentTypeHandler, newValueType.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.construct(String.class, bindings);
        JavaType valueType = JavaType.construct(Integer.class, bindings);
        Object valueHandler = new Object();
        JavaType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        JavaType newMapType = mapType.withValueHandler(valueHandler);
        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(keyType, newMapType.getKeyType());
        assertEquals(valueHandler, newMapType.getValueHandler());
        assertEquals(valueType, newMapType.getValueType());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.construct(String.class, bindings);
        JavaType valueType = JavaType.construct(Integer.class, bindings);
        Object contentValueHandler = new Object();
        JavaType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        JavaType newMapType = mapType.withContentValueHandler(contentValueHandler);
        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(keyType, newMapType.getKeyType());
        JavaType newValueType = newMapType.getValueType();
        assertEquals(valueType, newValueType);
        assertEquals(contentValueHandler, newValueType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.construct(String.class, bindings);
        JavaType valueType = JavaType.construct(Integer.class, bindings);
        JavaType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        JavaType newMapType = mapType.withStaticTyping();
        assertTrue(newMapType.isStatic());
        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(keyType, newMapType.getKeyType());
        assertEquals(valueType, newMapType.getValueType());
    }

    @Test
    public void testWithContentType() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.construct(String.class, bindings);
        JavaType valueType = JavaType.construct(Integer.class, bindings);
        JavaType contentType = JavaType.construct(String[].class, bindings);
        JavaType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        JavaType newMapType = mapType.withContentType(contentType);
        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(keyType, newMapType.getKeyType());
        JavaType newValueType = newMapType.getValueType();
        assertEquals(contentType, newValueType);
    }

    @Test
    public void testWithKeyType() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType1 = JavaType.construct(String.class, bindings);
        JavaType valueType = JavaType.construct(Integer.class, bindings);
        JavaType mapType = MapType.construct(rawType, bindings, null, null, keyType1, valueType);
        JavaType newMapType = mapType.withKeyType(JavaType.construct(Integer.class, bindings));
        assertEquals(Map.class, newMapType.getRawClass());
        JavaType newKeyType = newMapType.getKeyType();
        assertEquals(Integer.class, newKeyType.getRawClass());
        assertEquals(valueType, newMapType.getValueType());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = HashMap.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.construct(String.class, bindings);
        JavaType valueType = JavaType.construct(Integer.class, bindings);
        JavaType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        JavaType refinedMapType = mapType.refine(LinkedHashMap.class, bindings, null, null);
        assertEquals(LinkedHashMap.class, refinedMapType.getRawClass());
        assertEquals(keyType, refinedMapType.getKeyType());
        assertEquals(valueType, refinedMapType.getValueType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.construct(String.class, bindings);
        JavaType valueType = JavaType.construct(Integer.class, bindings);
        Object keyTypeHandler = new Object();
        JavaType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        JavaType newMapType = mapType.withKeyTypeHandler(keyTypeHandler);
        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(keyTypeHandler, newMapType.getKeyTypeHandler());
        assertEquals(keyType, newMapType.getKeyType());
        assertEquals(valueType, newMapType.getValueType());
    }

    @Test
    public void testWithKeyValueHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.construct(String.class, bindings);
        JavaType valueType = JavaType.construct(Integer.class, bindings);
        Object keyValueHandler = new Object();
        JavaType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        JavaType newMapType = mapType.withKeyValueHandler(keyValueHandler);
        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(keyValueHandler, newMapType.getKeyValueHandler());
        assertEquals(keyType, newMapType.getKeyType());
        assertEquals(valueType, newMapType.getValueType());
    }

    @Test
    public void testToString() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.construct(String.class, bindings);
        JavaType valueType = JavaType.construct(Integer.class, bindings);
        JavaType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        String toString = mapType.toString();
        assertTrue(toString.startsWith("[map type; class " + rawType.getName() + ", " + keyType + " -> " + valueType + "]"));
    }
}