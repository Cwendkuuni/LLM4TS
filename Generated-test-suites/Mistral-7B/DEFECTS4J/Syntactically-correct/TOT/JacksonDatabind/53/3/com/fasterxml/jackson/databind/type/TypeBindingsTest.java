package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testCreateWithEmptyTypeList() {
        TypeBindings bindings = TypeBindings.create(String[].class, null);
        assertEquals("Expected empty bindings", TypeBindings.emptyBindings(), bindings);
    }

    @Test
    public void testCreateWithSingleTypeArgument() {
        String[] names = new String[]{"T"};
        JavaType[] types = new JavaType[]{JavaType.constructParametricType(List.class, JavaType.of(int.class))};
        TypeBindings bindings = new TypeBindings(names, types, null);

        assertEquals("Number of bindings should be 1", 1, bindings.size());
        assertEquals("Bound name for single type argument should be 'T'", "T", bindings.getBoundName(0));
        assertEquals("Bound type for single type argument should be List<int>", JavaType.constructParametricType(List.class, JavaType.of(int.class)), bindings.getBoundType(0));
    }

    @Test
    public void testCreateWithTwoTypeArguments() {
        String[] names = new String[]{"T1", "T2"};
        JavaType[] types = new JavaType[]{JavaType.constructParametricType(Map.class, JavaType.constructParametricType(List.class, JavaType.of(String.class)), JavaType.constructParametricType(List.class, JavaType.of(Integer.class))), JavaType.constructParametricType(ArrayList.class, JavaType.of(String.class))};
        TypeBindings bindings = new TypeBindings(names, types, null);

        assertEquals("Number of bindings should be 2", 2, bindings.size());
        assertEquals("Bound name for first type argument should be 'T1'", "T1", bindings.getBoundName(0));
        assertEquals("Bound type for first type argument should be Map<List<String>, List<Integer>>", JavaType.constructParametricType(Map.class, JavaType.constructParametricType(List.class, JavaType.of(String.class)), JavaType.constructParametricType(List.class, JavaType.of(Integer.class))), bindings.getBoundType(0));
        assertEquals("Bound name for second type argument should be 'T2'", "T2", bindings.getBoundName(1));
        assertEquals("Bound type for second type argument should be ArrayList<String>", JavaType.constructParametricType(ArrayList.class, JavaType.of(String.class)), bindings.getBoundType(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithTypeArgumentMismatch() {
        String[] names = new String[]{"T"};
        JavaType[] types = new JavaType[]{JavaType.constructParametricType(Map.class, JavaType.constructParametricType(List.class, JavaType.of(String.class)), JavaType.constructParametricType(List.class, JavaType.of(int.class))), JavaType.constructParametricType(List.class, JavaType.of(int.class))};
        new TypeBindings(names, types, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithTooFewTypeArguments() {
        String[] names = new String[]{"T1", "T2"};
        JavaType[] types = new JavaType[]{JavaType.constructParametricType(Map.class, JavaType.constructParametricType(List.class, JavaType.of(String.class)))};
        new TypeBindings(names, types, null);
    }

    @Test
    public void testFindBoundType() {
        String[] names = new String[]{"T1", "T2"};
        JavaType[] types = new JavaType[]{JavaType.constructParametricType(Map.class, JavaType.constructParametricType(List.class, JavaType.of(String.class)), JavaType.constructParametricType(List.class, JavaType.of(Integer.class))), JavaType.constructParametricType(List.class, JavaType.of(String.class))};
        TypeBindings bindings = new TypeBindings(names, types, null);

        assertEquals("Bound type for 'T1' should be Map<List<String>, List<Integer>>", JavaType.constructParametricType(Map.class, JavaType.constructParametricType(List.class, JavaType.of(String.class)), JavaType.constructParametricType(List.class, JavaType.of(Integer.class))), bindings.findBoundType("T1"));
        assertEquals("Bound type for 'T2' should be List<String>", JavaType.constructParametricType(List.class, JavaType.of(String.class)), bindings.findBoundType("T2"));
        assertNull("Expected null for unbound type", bindings.findBoundType("U"));
    }

    @Test
    public void testWithUnboundVariable() {
        String[] names = new String[]{"T1", "T2"};
        JavaType[] types = new JavaType[]{JavaType.constructParametricType(Map.class, JavaType.constructParametricType(List.class, JavaType.of(String.class)), JavaType.constructParametricType(List.class, JavaType.of(Integer.class))), JavaType.constructParametricType(List.class, JavaType.of(String.class))};
        TypeBindings bindings = new TypeBindings(names, types, null);

        assertEquals("Expected empty unbound variables array before adding a new one", null, bindings._unboundVariables);

        TypeBindings newBindings = bindings.withUnboundVariable("U");
        String[] newNames = newBindings._names;
        assertEquals("Expected array size of 3 after adding a new unbound variable", 3, newNames.length);
        assertEquals("Expected 'U' as the last unbound variable name", "U", newNames[2]);

        JavaType[] newTypes = newBindings._types;
        assertArrayEquals("Expected array size of 3 after adding a new unbound variable", new Types(3), newTypes);
    }
}