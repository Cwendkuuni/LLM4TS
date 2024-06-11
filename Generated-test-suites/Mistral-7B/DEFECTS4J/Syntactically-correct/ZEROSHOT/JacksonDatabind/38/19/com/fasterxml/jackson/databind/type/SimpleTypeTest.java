package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
        assertEquals("java.lang.String", simpleType.toString());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType copiedSimpleType = new SimpleType(simpleType);
        assertEquals(simpleType, copiedSimpleType);
        assertEquals(stringClass, copiedSimpleType.getRawClass());
        assertEquals(simpleType.getRawClass(), copiedSimpleType.getRawClass());
        assertEquals(simpleType.getBindings(), copiedSimpleType.getBindings());
        assertEquals(simpleType.getValueHandler(), copiedSimpleType.getValueHandler());
        assertEquals(simpleType.getTypeHandler(), copiedSimpleType.getTypeHandler());
        assertEquals(simpleType.isStatic(), copiedSimpleType.isStatic());
    }

    @Test
    public void testSimpleTypeUnsafeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(stringClass);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
        assertEquals("java.lang.String", simpleType.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeDeprecatedConstruction() {
        Class<List<String>> listClass = new ArrayList<String>().getClass();
        SimpleType simpleType = SimpleType.construct(listClass);
    }

    @Test
    public void testSimpleTypeWithContentType() {
        Class<String> stringClass = String.class;
        JavaType contentType = new SimpleType(String[].class);
        SimpleType simpleType = new SimpleType(stringClass, null, null, null, null, null, false);
        assertNull(simpleType.withContentType(contentType));
        try {
            simpleType.withContentType(contentType);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(stringClass, null, null, null, null, null, false);
        SimpleType simpleTypeWithHandler = simpleType.withTypeHandler(handler);
        assertEquals(handler, simpleTypeWithHandler.getTypeHandler());
        assertNotEquals(handler, simpleType.getTypeHandler());
        assertEquals(simpleType, simpleType.withTypeHandler(simpleType.getTypeHandler()));
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<String> stringClass = String.class;
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(stringClass, null, null, null, null, null, false);
        SimpleType simpleTypeWithHandler = simpleType.withValueHandler(handler);
        assertEquals(handler, simpleTypeWithHandler.getValueHandler());
        assertNotEquals(handler, simpleType.getValueHandler());
        assertEquals(simpleType, simpleType.withValueHandler(simpleType.getValueHandler()));
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, null, null, null, null, null, false);
        SimpleType staticSimpleType = simpleType.withStaticTyping();
        assertEquals(simpleType, simpleType.withStaticTyping());
        assertTrue(staticSimpleType.isStatic());
        assertFalse(simpleType.isStatic());
    }
}