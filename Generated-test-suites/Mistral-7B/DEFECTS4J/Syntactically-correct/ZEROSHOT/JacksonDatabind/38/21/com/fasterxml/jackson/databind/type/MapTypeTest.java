package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        assertEquals(mapType, mapTypeInstance.getRawClass());
        assertEquals(keyType, mapTypeInstance.getKeyType());
        assertEquals(valueType, mapTypeInstance.getValueType());
        assertEquals(bindings, mapTypeInstance.getBindings());
    }

    @Test
    public void testConstructWithTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object valueHandler = new Object() {};
        JavaType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType, valueHandler);
        assertEquals(mapType, mapTypeInstance.getRawClass());
        assertEquals(keyType, mapTypeInstance.getKeyType());
        assertEquals(valueType, mapTypeInstance.getValueType());
        assertEquals(bindings, mapTypeInstance.getBindings());
        assertEquals(valueHandler, mapTypeInstance.getValueHandler());
    }

    @Test
    public void testConstructWithKeyTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object keyHandler = new Object() {};
        JavaType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType, null, keyHandler, false);
        assertEquals(mapType, mapTypeInstance.getRawClass());
        assertEquals(keyType, mapTypeInstance.getKeyType());
        assertEquals(valueType, mapTypeInstance.getValueType());
        assertEquals(bindings, mapTypeInstance.getBindings());
        assertEquals(keyHandler, mapTypeInstance.getKeyHandler());
    }

    @Test
    public void testConstructWithSuperclassAndInterfaces() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType superclass = new TypeReference<Object>() {}.getType();
        JavaType[] superInterfaces = new JavaType[] {new TypeReference<Map<Object, Object>>() {}.getType()};
        JavaType mapTypeInstance = MapType.construct(mapType, bindings, superclass, superInterfaces, keyType, valueType);
        assertEquals(mapType, mapTypeInstance.getRawClass());
        assertEquals(keyType, mapTypeInstance.getKeyType());
        assertEquals(valueType, mapTypeInstance.getValueType());
        assertEquals(bindings, mapTypeInstance.getBindings());
        assertEquals(superclass, mapTypeInstance.getSuperclass());
        assertArrayEquals(superInterfaces, mapTypeInstance.getInterfaces());
    }

    @Test
    public void testNarrowWithSubclass() {
        Class<Map<String, Integer>> mapType = Map.class;
        Class<Map<String, Long>> mapSubclass = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        JavaType narrowedInstance = mapTypeInstance.narrow(mapSubclass);
        assertEquals(mapSubclass, narrowedInstance.getRawClass());
        assertEquals(keyType, narrowedInstance.getKeyType());
        assertEquals(valueType, narrowedInstance.getValueType());
        assertEquals(bindings, narrowedInstance.getBindings());
    }

    @Test
    public void testWithTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object valueHandler = new Object() {};
        JavaType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        JavaType withTypeHandlerInstance = mapTypeInstance.withTypeHandler(valueHandler);
        assertEquals(mapType, withTypeHandlerInstance.getRawClass());
        assertEquals(keyType, withTypeHandlerInstance.getKeyType());
        assertEquals(valueType, withTypeHandlerInstance.getValueType());
        assertEquals(bindings, withTypeHandlerInstance.getBindings());
        assertEquals(valueHandler, withTypeHandlerInstance.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object contentTypeHandler = new Object() {};
        JavaType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        JavaType withContentTypeHandlerInstance = mapTypeInstance.withContentTypeHandler(contentTypeHandler);
        assertEquals(mapType, withContentTypeHandlerInstance.getRawClass());
        assertEquals(keyType, withContentTypeHandlerInstance.getKeyType());
        JavaType valueTypeWithHandler = withContentTypeHandlerInstance.getValueType().withTypeHandler(contentTypeHandler);
        assertEquals(valueTypeWithHandler, withContentTypeHandlerInstance.getValueType());
        assertEquals(bindings, withContentTypeHandlerInstance.getBindings());
        assertEquals(contentTypeHandler, withContentTypeHandlerInstance.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object valueHandler = new Object() {};
        JavaType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        JavaType withValueHandlerInstance = mapTypeInstance.withValueHandler(valueHandler);
        assertEquals(mapType, withValueHandlerInstance.getRawClass());
        assertEquals(keyType, withValueHandlerInstance.getKeyType());
        assertEquals(valueType, withValueHandlerInstance.getValueType());
        assertEquals(bindings, withValueHandlerInstance.getBindings());
        assertEquals(valueHandler, withValueHandlerInstance.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object contentValueHandler = new Object() {};
        JavaType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        JavaType withContentValueHandlerInstance = mapTypeInstance.withContentValueHandler(contentValueHandler);
        assertEquals(mapType, withContentValueHandlerInstance.getRawClass());
        assertEquals(keyType, withContentValueHandlerInstance.getKeyType());
        JavaType valueTypeWithHandler = withContentValueHandlerInstance.getValueType().withValueHandler(contentValueHandler);
        assertEquals(valueTypeWithHandler, withContentValueHandlerInstance.getValueType());
        assertEquals(bindings, withContentValueHandlerInstance.getBindings());
        assertEquals(contentValueHandler, withContentValueHandlerInstance.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        JavaType withStaticTypingInstance = mapTypeInstance.withStaticTyping();
        assertEquals(mapType, withStaticTypingInstance.getRawClass());
        assertEquals(keyType, withStaticTypingInstance.getKeyType());
        assertEquals(valueType, withStaticTypingInstance.getValueType());
        assertEquals(bindings, withStaticTypingInstance.getBindings());
        assertTrue(withStaticTypingInstance.isStatic());
    }

    @Test
    public void testWithContentType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType contentType = new TypeReference<String>() {}.getType();
        JavaType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        JavaType withContentTypeInstance = mapTypeInstance.withContentType(contentType);
        assertEquals(mapType, withContentTypeInstance.getRawClass());
        assertEquals(keyType, withContentTypeInstance.getKeyType());
        JavaType valueTypeWithContentType = withContentTypeInstance.getValueType();
        assertEquals(contentType, valueTypeWithContentType.getType());
        assertEquals(bindings, withContentTypeInstance.getBindings());
    }

    @Test
    public void testWithKeyType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType newKeyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        JavaType withNewKeyTypeInstance = mapTypeInstance.withKeyType(newKeyType);
        assertEquals(mapType, withNewKeyTypeInstance.getRawClass());
        assertEquals(newKeyType, withNewKeyTypeInstance.getKeyType());
        assertEquals(keyType, mapTypeInstance.getKeyType());
        assertEquals(valueType, withNewKeyTypeInstance.getValueType());
        assertEquals(bindings, withNewKeyTypeInstance.getBindings());
    }

    @Test
    public void testRefine() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        JavaType refinedInstance = mapTypeInstance.refine(Map.class, bindings, null, null);
        assertEquals(mapType, refinedInstance.getRawClass());
        assertEquals(keyType, refinedInstance.getKeyType());
        assertEquals(valueType, refinedInstance.getValueType());
        assertEquals(bindings, refinedInstance.getBindings());
    }

    @Test
    public void testToString() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
        String toString = mapTypeInstance.toString();
        assertTrue(toString.startsWith("[map type; class " + mapType.getName() + ", " + keyType + " -> " + valueType + "]"));
    }
}