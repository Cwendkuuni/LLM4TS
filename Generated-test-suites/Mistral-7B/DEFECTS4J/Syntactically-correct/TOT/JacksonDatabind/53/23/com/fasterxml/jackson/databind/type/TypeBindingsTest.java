package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals("", bindings.toString());
        assertEquals(0, bindings.size());
        assertFalse(bindings.hasUnbound("T"));
    }

    @Test
    public void testCreateForSimpleTypes() {
        Class<List<String>> listOfStringsClass = new ArrayList<>() {}.getClass();
        JavaType listOfStringsType = TypeFactory.defaultInstance().constructType(listOfStringsClass);
        TypeBindings bindings = TypeBindings.create(listOfStringsClass, listOfStringsType);
        assertEquals("[Ljava.lang.String;", bindings.toString());
        assertEquals(1, bindings.size());
        String name = "T";
        assertEquals(JavaType.getType(String.class), bindings.findBoundType(name));
        assertFalse(bindings.hasUnbound(name));
    }

    @Test
    public void testCreateForCollectionTypes() {
        Class<List<List<Integer>>> listOfListIntsClass = new ArrayList<>() {}.getClass();
        JavaType listOfListIntsType = TypeFactory.defaultInstance().constructType(listOfListIntsClass);
        TypeBindings bindings = TypeBindings.create(listOfListIntsClass, listOfListIntsType);
        assertEquals("[Ljava.util.List;[I", bindings.toString());
        assertEquals(2, bindings.size());
        String name1 = "K";
        assertEquals(JavaType.getTypeCollectionParam(List.class, 0), bindings.findBoundType(name1));
        String name2 = "T";
        assertEquals(JavaType.getTypeCollectionElementType(JavaType.getTypeCollectionParam(List.class, 0)), bindings.findBoundType(name2));
        assertFalse(bindings.hasUnbound(name1));
        assertFalse(bindings.hasUnbound(name2));
    }

    @Test
    public void testCreateForMapTypes() {
        Class<Map<String, List<Integer>>> stringListMapClass = new HashMap<>() {}.getClass();
        JavaType stringListMapType = TypeFactory.defaultInstance().constructType(stringListMapClass);
        TypeBindings bindings = TypeBindings.create(stringListMapClass, stringListMapType);
        assertEquals("{Ljava.lang.String;Ljava.util.List;}[I", bindings.toString());
        assertEquals(3, bindings.size());
        String name1 = "K";
        assertEquals(JavaType.getType(String.class), bindings.findBoundType(name1));
        String name2 = "V";
        assertEquals(JavaType.getTypeCollectionParam(List.class, 0), bindings.findBoundType(name2));
        assertFalse(bindings.hasUnbound(name1));
        assertFalse(bindings.hasUnbound(name2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidNumberOfTypeParameters() {
        Class<List<List<List<Integer>>>> listOfListListIntsClass = new ArrayList<>() {}.getClass();
        JavaType listOfListListIntsType = TypeFactory.defaultInstance().constructType(listOfListListIntsClass);
        TypeBindings.create(listOfListListIntsClass, listOfListListIntsType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedNumberOfTypeParameters() {
        Class<Map<String, List<Integer>>> stringListMapClass = new HashMap<>() {}.getClass();
        JavaType stringListMapType = TypeFactory.defaultInstance().constructType(stringListMapClass);
        TypeBindings.create(List.class, stringListMapType);
    }

    @Test
    public void testCreateWithUnboundTypeVariable() {
        Class<Map<String, List<Integer>>> stringListMapClass = new HashMap<>() {}.getClass();
        JavaType stringListMapType = TypeFactory.defaultInstance().constructType(stringListMapClass);
        TypeBindings bindings = TypeBindings.createIfNeeded(stringListMapClass, null);
        assertEquals("{Ljava.lang.String;Ljava.util.List;}[I", bindings.toString());
        assertEquals(3, bindings.size());
        String name1 = "K";
        assertEquals(JavaType.getType(String.class), bindings.findBoundType(name1));
        String name2 = "V";
        assertEquals(JavaType.getTypeCollectionParam(List.class, 0), bindings.findBoundType(name2));
        assertTrue(bindings.hasUnbound("T"));
    }

    @Test
    public void testWithUnboundVariable() {
        Class<Map<String, List<Integer>>> stringListMapClass = new HashMap<>() {}.getClass();
        JavaType stringListMapType = TypeFactory.defaultInstance().constructType(stringListMapClass);
        TypeBindings bindings = TypeBindings.createIfNeeded(stringListMapClass, null);
        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("T");
        assertEquals("{Ljava.lang.String;Ljava.util.List;}[Ljava.lang.Object;]"
                , bindingsWithUnbound.toString());
        assertEquals(4, bindingsWithUnbound.size());
        String name1 = "K";
        assertEquals(JavaType.getType(String.class), bindingsWithUnbound.findBoundType(name1));
        String name2 = "V";
        assertEquals(JavaType.getTypeCollectionParam(List.class, 0), bindingsWithUnbound.findBoundType(name2));
        String name3 = "U";
        assertNull(bindingsWithUnbound.findBoundType(name3));
        assertTrue(bindingsWithUnbound.hasUnbound(name3));
    }
}