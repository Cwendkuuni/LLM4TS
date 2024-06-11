package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test default constructor
        SimpleType simpleType1 = new SimpleType(String.class);
        assertNotNull(simpleType1);
        assertEquals(String.class, simpleType1.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType1.getBindings());
        assertNull(simpleType1.getSuperClass());
        assertNull(simpleType1.getSuperInterfaces());
        assertFalse(simpleType1.isStatic());

        // Test constructor with bindings, superClass, and superInterfaces
        Class<?> intClass = Integer.class;
        TypeBindings bindings = TypeBindings.builder()
                .bindValueHandler(Integer.class, new IntValueHandler())
                .build();
        SimpleType simpleType2 = new SimpleType(intClass, bindings, null, null);
        assertNotNull(simpleType2);
        assertEquals(intClass, simpleType2.getRawClass());
        assertEquals(bindings, simpleType2.getBindings());
        assertNull(simpleType2.getSuperClass());
        assertNull(simpleType2.getSuperInterfaces());
        assertFalse(simpleType2.isStatic());

        // Test copy constructor
        SimpleType simpleType3 = new SimpleType(simpleType1);
        assertNotNull(simpleType3);
        assertEquals(simpleType1.getRawClass(), simpleType3.getRawClass());
        assertEquals(simpleType1.getBindings(), simpleType3.getBindings());
        assertSame(simpleType1.getSuperClass(), simpleType3.getSuperClass());
        assertArrayEquals(simpleType1.getSuperInterfaces(), simpleType3.getSuperInterfaces());
        assertEquals(simpleType1.isStatic(), simpleType3.isStatic());

        // Test pass-through constructor
        JavaType javaType = TypeFactory.defaultInstance().constructType(int[].class);
        SimpleType simpleType4 = new SimpleType(javaType.getRawClass(), bindings, null, null, 0, null, false);
        assertNotNull(simpleType4);
        assertEquals(int[].class, simpleType4.getRawClass());
        assertEquals(bindings, simpleType4.getBindings());
        assertNull(simpleType4.getSuperClass());
        assertNull(simpleType4.getSuperInterfaces());
        assertFalse(simpleType4.isStatic());
    }

    @Test
    public void testUnsafeConstruction() {
        try {
            SimpleType simpleType = SimpleType.constructUnsafe(String[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            SimpleType simpleType = SimpleType.construct(String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            SimpleType simpleType = new SimpleType(new Class[0]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNarrowing() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String[].class);

        JavaType narrowedType = simpleType1.narrow(String[].class);
        assertNull(narrowedType);

        narrowedType = simpleType2.narrow(String.class);
        assertNotNull(narrowedType);
        assertEquals(simpleType1, narrowedType);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);

        try {
            simpleType.withContentType(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            simpleType.withContentType(new SimpleType(Integer.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        SimpleType contentType = new SimpleType(List.class);
        JavaType result = simpleType.withContentType(contentType);
        assertNotNull(result);
        assertEquals(List.class, result.getRawClass());
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);

        try {
            simpleType.withTypeHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        SimpleType handlerType = new SimpleType(String.class);
        handlerType.setValueHandler(new StringHandler());
        JavaType result = simpleType.withTypeHandler(handlerType);
        assertNotNull(result);
        assertEquals(simpleType.getRawClass(), result.getRawClass());
        assertEquals(handlerType.getValueHandler(), result.getValueHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);

        try {
            simpleType.withContentTypeHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        SimpleType handlerType = new SimpleType(String.class);
        handlerType.setValueHandler(new StringHandler());
        JavaType result = simpleType.withContentTypeHandler(handlerType);
        assertNotNull(result);
        assertEquals(simpleType.getRawClass(), result.getRawClass());
        assertEquals(handlerType.getValueHandler(), result.getValueHandler());
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);

        try {
            simpleType.withValueHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        SimpleType handlerType = new SimpleType(String.class);
        handlerType.setValueHandler(new StringHandler());
        JavaType result = simpleType.withValueHandler(handlerType);
        assertNotNull(result);
        assertEquals(simpleType.getRawClass(), result.getRawClass());
        assertEquals(handlerType.getValueHandler(), result.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType staticType = simpleType.withStaticTyping();
        assertNotNull(staticType);
        assertTrue(staticType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);

        try {
            simpleType.refine(Integer.class, null, null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        SimpleType refinedType = new SimpleType(String[].class);
        JavaType result = simpleType.refine(String[].class, null, null, null);
        assertNotNull(result);
        assertEquals(refinedType, result);
    }
}