package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertEquals(0, empty.size());
        assertNull(empty.findBoundType("T"));
    }

    @Test
    public void testCreateWithNulls() {
        JavaType[] types = new JavaType[0];
        assertNull(TypeBindings.create(Object.class, types));
    }

    @Test
    public void testCreateWithOneType() {
        JavaType integerType = TypeBindings.constructType(int.class);
        TypeBindings bindings = TypeBindings.create(String[].class, integerType);
        assertEquals(2, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(integerType, bindings.getBoundType(0));
    }

    @Test
    public void testCreateWithTwoTypes() {
        JavaType listType = TypeBindings.constructType(List.class);
        JavaType mapType = TypeBindings.constructType(Map.class);
        TypeBindings bindings = TypeBindings.create(String[].class, listType, mapType);
        assertEquals(3, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(listType, bindings.getBoundType(0));
        assertEquals("K", bindings.getBoundName(1));
        assertEquals(mapType.getComponentType().getRawClass(), bindings.getBoundType(1));
        assertEquals("V", bindings.getBoundName(2));
        assertEquals(mapType.getValueType(), bindings.getBoundType(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedCounts() {
        JavaType listType = TypeBindings.constructType(List.class);
        JavaType mapType = TypeBindings.constructType(Map.class);
        TypeBindings bindings = TypeBindings.create(String[].class, listType, mapType, new JavaType[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedNamesAndTypes() {
        JavaType listType = TypeBindings.constructType(List.class);
        JavaType mapType = TypeBindings.constructType(Map.class);
        String[] names = {"S", "T"};
        TypeBindings bindings = TypeBindings.create(names, listType, mapType);
    }

    @Test
    public void testCreateIfNeededWithEmptyTypes() {
        JavaType listType = TypeBindings.constructType(List.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(List.class, java.util.List.class);
        assertNotNull(bindings);
        assertEquals(1, bindings.size());
        assertEquals(listType, bindings.getBoundType(0));
    }

    @Test
    public void testCreateIfNeededWithTypes() {
        JavaType listType = TypeBindings.constructType(List.class);
        JavaType mapType = TypeBindings.constructType(Map.class);
        JavaType listOfMapsType = TypeBindings.constructType(List.class, Map.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(List.class, listOfMapsType);
        assertEquals(2, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(Map.class, bindings.getBoundType(0));
        assertEquals("K", bindings.getBoundName(1));
        assertEquals(JavaType.constructRawType(Object.class), bindings.getBoundType(1));
    }

    @Test
    public void testCreateWithUnboundVariable() {
        JavaType listType = TypeBindings.constructType(List.class);
        JavaType mapType = TypeBindings.constructType(Map.class);
        JavaType unboundType = TypeBindings.constructType(Object.class);
        TypeBindings bindings = TypeBindings.create(String[].class, listType, mapType);
        TypeBindings unboundBindings = bindings.withUnboundVariable("U");
        assertEquals("U", unboundBindings.getBoundName(2));
        assertNull(unboundBindings.getBoundType(2));
    }
}