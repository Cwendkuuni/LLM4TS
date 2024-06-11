package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testCreateBindingsForSimpleType() {
        Class<List<Number>> listOfNumbers = new ArrayList<Number>().getClass();
        JavaType[] types = new JavaType[] { new TypeReference<List<Number>>() {}.getType() };
        TypeBindings bindings = TypeBindings.create(listOfNumbers, types);
        assertEquals(1, bindings.size());
        assertEquals("java.util.List", bindings.getBoundName(0));
        assertEquals(List.class, bindings.getBoundType(0).getRawClass());
    }

    @Test
    public void testCreateBindingsForTwoTypeParameters() {
        Class<Map<String, List<Number>>> mapOfStringListNumbers = new HashMap<String, List<Number>>().getClass();
        JavaType[] types = new JavaType[] { new TypeReference<Map<String, List<Number>>>() {}.getType() };
        TypeBindings bindings = TypeBindings.create(mapOfStringListNumbers, types);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0).getRawClass());
        assertEquals("java.util.List", bindings.getBoundName(1));
        assertEquals(List.class, bindings.getBoundType(1).getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsForMismatchedNumberOfTypeParameters() {
        Class<Map<String, List<Number>>> mapOfStringListNumbers = new HashMap<String, List<Number>>().getClass();
        JavaType[] types = new JavaType[] { new TypeReference<String>() {}.getType() };
        TypeBindings.create(mapOfStringListNumbers, types);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsForMismatchedTypeNames() {
        Class<Map<String, List<Number>>> mapOfStringListNumbers = new HashMap<String, List<Number>>().getClass();
        JavaType[] types = new JavaType[] { new TypeReference<Map<Integer, List<Number>>>() {}.getType() };
        TypeBindings.create(mapOfStringListNumbers, types);
    }

    @Test
    public void testCreateIfNeeded() {
        Class<ArrayList<Number>> listOfNumbers = new ArrayList<Number>().getClass();
        JavaType typeArg1 = new TypeReference<ArrayList<Number>>() {}.getType();
        TypeBindings bindings = TypeBindings.createIfNeeded(listOfNumbers, typeArg1);
        assertEquals(1, bindings.size());
        assertEquals("java.util.List", bindings.getBoundName(0));
        assertEquals(List.class, bindings.getBoundType(0).getRawClass());
    }

    @Test
    public void testCreateIfNeededForNoTypeParameters() {
        Class<String> stringClass = String.class;
        JavaType[] types = new JavaType[0];
        TypeBindings bindings = TypeBindings.createIfNeeded(stringClass, types);
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testFindBoundType() {
        Class<Map<String, List<Number>>> mapOfStringListNumbers = new HashMap<String, List<Number>>().getClass();
        JavaType[] types = new JavaType[] { new TypeReference<Map<String, List<Number>>>() {}.getType() };
        TypeBindings bindings = TypeBindings.create(mapOfStringListNumbers, types);
        assertEquals("java.lang.String", bindings.findBoundType("String").getType().getName());
        assertEquals("java.util.List", bindings.findBoundType("List").getType().getName());
        assertNull(bindings.findBoundType("NonExistentType"));
    }

    @Test
    public void testWithUnboundVariable() {
        Class<Map<String, List<Number>>> mapOfStringListNumbers = new HashMap<String, List<Number>>().getClass();
        JavaType[] types = new JavaType[] { new TypeReference<Map<String, List<Number>>>() {}.getType() };
        TypeBindings bindings = TypeBindings.create(mapOfStringListNumbers, types);
        TypeBindings newBindings = bindings.withUnboundVariable("NonExistentType");
        assertTrue(newBindings.hasUnbound("NonExistentType"));
        assertFalse(bindings.hasUnbound("NonExistentType"));
    }

}