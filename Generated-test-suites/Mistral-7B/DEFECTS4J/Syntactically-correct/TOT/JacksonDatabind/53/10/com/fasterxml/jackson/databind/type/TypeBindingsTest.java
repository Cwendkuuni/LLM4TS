package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testCreateEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateBindingsForSimpleType() {
        Class<?> simpleType = String.class;
        TypeBindings bindings = TypeBindings.create(simpleType, null);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.String", bindings.toString());
    }

    @Test
    public void testCreateBindingsForGenericType() {
        Class<?> listType = ArrayList.class;
        TypeBindings bindings = TypeBindings.create(listType, null);
        assertEquals(1, bindings.size());
        assertEquals("<E>", bindings.toString());
    }

    @Test
    public void testCreateBindingsForGenericTypeWithTypeArguments() {
        Class<?> listTypeWithTypeArguments = ArrayList.class;
        JavaType javaTypeListInteger = TypeFactory.defaultInstance().constructType(Integer.class, List.class);
        TypeBindings bindings = TypeBindings.create(listTypeWithTypeArguments, javaTypeListInteger);
        assertEquals(1, bindings.size());
        assertEquals("<E extends Object>, java.util.List<E>", bindings.toString());
    }

    @Test
    public void testCreateBindingsForGenericTypeWithMultipleTypeArguments() {
        Class<?> mapTypeWithTypeArguments = HashMap.class;
        JavaType javaTypeMapStringString = TypeFactory.defaultInstance().constructType(String.class, String.class, Map.class);
        TypeBindings bindings = TypeBindings.create(mapTypeWithTypeArguments, javaTypeMapStringString);
        assertEquals(2, bindings.size());
        assertEquals("<K extends Object>, <V extends Object>, java.util.Map<K, V>", bindings.toString());

        String key = "testKey";
        String value = "testValue";
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        Object mapObject = map;

        JavaType boundKeyType = bindings.findBoundType("K");
        JavaType boundValueType = bindings.findBoundType("V");

        assertEquals(String.class, boundKeyType.getRawClass());
        assertEquals(String.class, boundValueType.getRawClass());

        TypeBindings unboundBindings = bindings.withUnboundVariable("K");
        JavaType unboundKeyType = unboundBindings.findBoundType("K");
        assertNull(unboundKeyType);
        assertEquals("K", unboundBindings.getBoundName(0));
    }
}