package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        JavaType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        JavaType simpleType = SimpleType.construct(Map.class);
    }

    @Test
    public void testCopyConstructor() {
        Class<?> clazz = Integer.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType originalType = new SimpleType(clazz, bindings, null, null);
        JavaType copiedType = new SimpleType(originalType);
        assertEquals(clazz, copiedType.getRawClass());
        assertEquals(bindings, copiedType._bindings);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> clazz = Integer.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType originalType = new SimpleType(clazz, bindings, null, null);
        Object handler = new Object();
        JavaType typeWithHandler = originalType.withTypeHandler(handler);
        assertNotNull(typeWithHandler);
        assertEquals(clazz, typeWithHandler.getRawClass());
        assertEquals(bindings, typeWithHandler._bindings);
        assertEquals(handler, typeWithHandler._typeHandler);
    }

    @Test
    public void testNarrow() {
        Class<?> intClass = Integer.class;
        Class<?> longClass = Long.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType intType = new SimpleType(intClass, bindings, null, null);
        JavaType longType = intType.narrow(longClass);
        assertNotNull(longType);
        assertEquals(longClass, longType.getRawClass());
        assertEquals(bindings, longType._bindings);
        assertEquals(intType._class, longType._superClass);
        assertNull(longType._superInterfaces);
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> booleanClass = Boolean.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType dynamicType = new SimpleType(booleanClass, bindings, null, null, null, null, false);
        JavaType staticType = dynamicType.withStaticTyping();
        assertEquals(booleanClass, staticType.getRawClass());
        assertEquals(bindings, staticType._bindings);
        assertFalse(staticType._asStatic);
        JavaType staticType2 = dynamicType.withStaticTyping();
        assertTrue(staticType2._asStatic);
        assertEquals(staticType, staticType2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        Class<?> clazz = Integer.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType type = new SimpleType(clazz, bindings, null, null);
        type.withContentType(String.class);
    }
}