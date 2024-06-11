package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertFalse(bindings.hasUnbound("T"));
        assertEquals(0, bindings.size());
        assertTrue(bindings.isEmpty());
        assertEquals("", bindings.toString());
    }

    @Test
    public void testCreateWithNoTypeParameters() {
        String className = "java.util.ArrayList";
        TypeBindings bindings = TypeBindings.create(Class.forName(className), null);
        assertFalse(bindings.hasUnbound("E"));
        assertEquals(0, bindings.size());
        assertTrue(bindings.isEmpty());
        assertEquals("[]", bindings.toString());
    }

    @Test
    public void testCreateWithSingleTypeParameter() {
        String className1 = "java.util.List";
        TypeVariable<Object> typeVariable1 = TypeVariable.of("T");
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(className1, typeVariable1);
        TypeBindings bindings1 = TypeBindings.create(Class.forName(className1), javaType1);
        assertTrue(bindings1.hasUnbound("T"));
        assertEquals(1, bindings1.size());
        assertNotNull(bindings1.findBoundType("T"));
        assertEquals("T", bindings1.getBoundName(0));
        assertEquals(javaType1, bindings1.getBoundType(0));
        assertEquals("List<T>", bindings1.toString());

        String className2 = "java.util.Map";
        TypeVariable<Object> keyTypeVariable = TypeVariable.of("K");
        TypeVariable<Object> valueTypeVariable = TypeVariable.of("V");
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(className2, keyTypeVariable, valueTypeVariable);
        TypeBindings bindings2 = TypeBindings.create(Class.forName(className2), javaType2);
        assertTrue(bindings2.hasUnbound("K"));
        assertTrue(bindings2.hasUnbound("V"));
        assertEquals(2, bindings2.size());
        assertNotNull(bindings2.findBoundType("K"));
        assertNotNull(bindings2.findBoundType("V"));
        assertEquals("K", bindings2.getBoundName(0));
        assertEquals("V", bindings2.getBoundName(1));
        assertEquals(keyType, bindings2.getBoundType(0));
        assertEquals(valueType, bindings2.getBoundType(1));
        assertEquals("Map<K, V>", bindings2.toString());

    }

    @Test
    public void testCreateWithMultipleTypeParameters() {
        String className = "java.lang.Class";
        TypeVariable<Object> typeVariable1 = TypeVariable.of("T");
        TypeVariable<Object> typeVariable2 = TypeVariable.of("U");
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(className, typeVariable1, typeVariable2);
        TypeBindings bindings = TypeBindings.create(Class.forName(className), javaType1);
        assertTrue(bindings.hasUnbound("T"));
        assertTrue(bindings.hasUnbound("U"));
        assertEquals(2, bindings.size());
        assertNotNull(bindings.findBoundType("T"));
        assertNotNull(bindings.findBoundType("U"));
        assertEquals("T", bindings.getBoundName(0));
        assertEquals("U", bindings.getBoundName(1));
        assertEquals(javaType1, bindings.getBoundType(0));
        assertEquals(javaType1.getTypeArguments()[1], bindings.getBoundType(1));
        assertEquals("Class<T, U>", bindings.toString());
    }

    @Test
    public void testCreateIfNeeded() {
        String className = "java.util.ArrayList";
        TypeBindings bindings = TypeBindings.createIfNeeded(Class.forName(className), null);
        assertFalse(bindings.hasUnbound("E"));
        assertEquals(0, bindings.size());
        assertTrue(bindings.isEmpty());
        assertEquals("[]", bindings.toString());

        className = "java.util.HashMap";
        JavaType javaType = TypeFactory.defaultInstance().constructType(className, TypeVariable.of("K"), TypeVariable.of("V"));
        bindings = TypeBindings.createIfNeeded(Class.forName(className), javaType);
        assertTrue(bindings.hasUnbound("K"));
        assertTrue(bindings.hasUnbound("V"));
        assertEquals(2, bindings.size());
        assertNotNull(bindings.findBoundType("K"));
        assertNotNull(bindings.findBoundType("V"));
        assertEquals("K", bindings.getBoundName(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(javaType.getTypeArguments()[0], bindings.getBoundType(0));
        assertEquals(javaType.getTypeArguments()[1], bindings.getBoundType(1));
        assertEquals("HashMap<K, V>", bindings.toString());
    }

    @Test
    public void testCreateWithUnboundVariable() {
        String className = "java.util.ArrayList";
        TypeVariable<Object> typeVariable = TypeVariable.of("T");
        TypeBindings bindings = TypeBindings.create(Class.forName(className), null);
        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("U");
        assertTrue(bindingsWithUnboundVariable.hasUnbound("U"));
        assertFalse(bindingsWithUnboundVariable.hasUnbound("T"));
        assertEquals(1, bindingsWithUnboundVariable.size());
        assertEquals("U", bindingsWithUnboundVariable.getBoundName(0));
        assertNull(bindingsWithUnboundVariable.findBoundType("T"));
        assertNull(bindingsWithUnboundVariable.findBoundType("U"));
        assertEquals("U", bindingsWithUnboundVariable.getBoundName(0));
        assertEquals(Collections.emptyList(), bindingsWithUnboundVariable.getTypeParameters());
        assertEquals("TypeBindings[U]", bindingsWithUnboundVariable.toString());
    }

    @Test
    public void testFindBoundType() {
        String className = "java.util.List";
        TypeVariable<Object> typeVariable = TypeVariable.of("T");
        JavaType javaType = TypeFactory.defaultInstance().constructType(className, typeVariable);
        TypeBindings bindings = TypeBindings.create(Class.forName(className), javaType);
        assertEquals(javaType, bindings.findBoundType("T"));
    }

    @Test(expected = IllegalStateException.class)
    public void testFindBoundTypeUnresolvedType() {
        String className = "com.fasterxml.jackson.databind.type.TypeBindings";
        TypeBindings bindings = TypeBindings.create(Class.forName(className), null);
        bindings.findBoundType("N");
    }

}