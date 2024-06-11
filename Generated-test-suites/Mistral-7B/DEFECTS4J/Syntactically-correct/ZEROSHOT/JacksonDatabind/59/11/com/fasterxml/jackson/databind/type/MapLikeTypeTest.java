package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.*;

import org.junit.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.type.*;
import com.fasterxml.jackson.databind.util.*;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeCreation() throws Exception {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = MapLikeType.construct(mapType.getRawClass(), String.class, Integer.class);
        assertEquals(mapType, mapLikeType.getRawType());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getValueType());
        assertFalse(mapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeUpgrade() throws Exception {
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
        JavaType mapKeyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType mapValueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(listType, mapKeyType, mapValueType);
        assertEquals(mapKeyType, mapLikeType.getKeyType());
        assertEquals(mapValueType, mapLikeType.getValueType());
        assertFalse(mapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeCreationWithBindings() throws Exception {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, null, null, null, null, null, null, false);
        assertEquals(mapType, mapLikeType.getRawType());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getValueType());
        assertFalse(mapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeCreationWithValueHandler() throws Exception {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        Object valueHandler = new JacksonAnnotationIntrospector().findValueHandler(mapType, null);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, null, null, valueHandler, null, false);
        assertEquals(mapType, mapLikeType.getRawType());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getValueType());
        assertNotNull(mapLikeType.getValueHandler());
        assertNull(mapLikeType.getTypeHandler());
        assertFalse(mapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeCreationWithTypeHandler() throws Exception {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        Object typeHandler = new JacksonAnnotationIntrospector().findTypeHandler(mapType, null);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, null, null, null, typeHandler, false);
        assertEquals(mapType, mapLikeType.getRawType());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getValueType());
        assertNull(mapLikeType.getValueHandler());
        assertNotNull(mapLikeType.getTypeHandler());
        assertFalse(mapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeCreationWithKeyType() throws Exception {
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        MapLikeType mapLikeType = mapLikeType.withKeyType(stringType);
        assertEquals(stringType, mapLikeType.getKeyType());
        assertSame(listType, mapLikeType.getRawType());
        assertFalse(mapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeCreationWithValueType() throws Exception {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        JavaType integerType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = mapLikeType.withContentType(integerType);
        assertEquals(mapType, mapLikeType.getRawType());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(integerType, mapLikeType.getValueType());
        assertFalse(mapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeCreationWithTypeHandlerAndValueHandler() throws Exception {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        Object typeHandler = new JacksonAnnotationIntrospector().findTypeHandler(mapType, null);
        Object valueHandler = new JacksonAnnotationIntrospector().findValueHandler(mapType, null);
        MapLikeType mapLikeType = mapLikeType.withTypeHandler(typeHandler).withValueHandler(valueHandler);
        assertEquals(mapType, mapLikeType.getRawType());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getValueType());
        assertNotNull(mapLikeType.getTypeHandler());
        assertNotNull(mapLikeType.getValueHandler());
        assertFalse(mapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeCreationWithStaticTyping() throws Exception {
        JavaType mapType = TypeFactory.defaultInstance().constructType(HashMap.class, String.class, Integer.class);
        MapLikeType mapLikeType = mapLikeType.withStaticTyping();
        assertTrue(mapLikeType.isTrueMapType());
        assertEquals(HashMap.class, mapLikeType.getRawType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeCreationWithInvalidBaseType() throws Exception {
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(listType, stringType, Integer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeCreationWithInvalidKeyType() throws Exception {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String[].class, Integer[].class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, stringType, null, null, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeCreationWithInvalidValueType() throws Exception {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer[].class);
        JavaType integerType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, null, integerType, null, null, false);
    }

    @Test
    public void testMapLikeTypeCanonicalName() throws Exception {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        String canonicalName = mapLikeType.buildCanonicalName();
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", canonicalName);
    }

    @Test
    public void testMapLikeTypeGenericName() throws Exception {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        String genericName = mapLikeType.getGenericSignature().toString();
        assertEquals("[Ljava.util.Map<Ljava.lang.String;Ljava.lang.Integer;>;]", genericName);
    }

    @Test
    public void testMapLikeTypeErasedName() throws Exception {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        String erasedName = mapLikeType.getErasedSignature().toString();
        assertEquals("[Ljava.util.Map;", erasedName);
    }

    @Test(expected = JsonMappingException.class)
    public void testMapLikeTypeNarrowing() throws Exception {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, stringType, null, null, null, false);
        MapLikeType narrowedType = mapLikeType.withKeyType(String.class);
        JavaType narrowedRawType = narrowedType.narrow(ArrayList.class);
    }

    @Test
    public void testMapLikeTypeToString() throws Exception {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        String toString = mapLikeType.toString();
        assertEquals("[-map-like type; class java.util.Map<java.lang.String, java.lang.Integer, null, null>, java.lang.String -> java.lang.Integer]", toString);
    }

    @Test
    public void testMapLikeTypeEquals() throws Exception {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        JavaType mapType2 = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, null, null, null, null, false);
        MapLikeType mapLikeType2 = new MapLikeType(mapType2, null, null, null, null, null, null, null, false);
        MapLikeType mapLikeType3 = new MapLikeType(mapType, null, null, null, null, null, null, null, true);
        assertTrue(mapLikeType.equals(mapLikeType));
        assertTrue(mapLikeType.equals(mapLikeType2));
        assertFalse(mapLikeType.equals(mapLikeType3));
    }
}