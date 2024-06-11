package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        SimpleType stringSimpleType = (SimpleType) stringType;

        assertEquals(String.class, stringSimpleType.getRawClass());
        assertFalse(stringSimpleType.isContainerType());
        assertNull(stringSimpleType.getContentType());

        // Test withTypeHandler and withValueHandler
        JavaType stringTypeWithHandler = TypeFactory.defaultInstance()
                .constructType(String.class, new StringValueHandler());
        SimpleType stringSimpleTypeWithHandler = (SimpleType) stringTypeWithHandler;
        assertEquals(String.class, stringSimpleTypeWithHandler.getRawClass());
        assertFalse(stringSimpleTypeWithHandler.isContainerType());
        assertNotNull(stringSimpleTypeWithHandler.getValueHandler());

        // Test constructUnsafe method
        JavaType unsafeStringType = SimpleType.constructUnsafe(String.class);
        SimpleType unsafeStringSimpleType = (SimpleType) unsafeStringType;
        assertEquals(String.class, unsafeStringSimpleType.getRawClass());
        assertFalse(unsafeStringSimpleType.isContainerType());
        assertNull(unsafeStringSimpleType.getValueHandler());
    }

    @Test
    public void testSimpleTypeFromParentClass() {
        JavaType objectType = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType parentType = TypeFactory.defaultInstance().constructType(Object.class);
        SimpleType objectSimpleType = (SimpleType) objectType;
        SimpleType stringSimpleType = (SimpleType) stringType;
        SimpleType parentSimpleType = (SimpleType) parentType;

        // Test isContainerType
        assertFalse(objectSimpleType.isContainerType());
        assertFalse(stringSimpleType.isContainerType());
        assertTrue(parentSimpleType.isContainerType());

        // Test getSuperclass and getSuperInterfaces
        JavaType parentSuperclass = parentSimpleType.getSuperclass();
        assertNull(parentSuperclass);
        JavaType[] parentSuperinterfaces = parentSimpleType.getSuperinterfaces();
        assertArrayEquals(new Class[0], parentSuperinterfaces);

        JavaType objectSuperclass = objectSimpleType.getSuperclass();
        assertSame(parentSimpleType, objectSuperclass);
        JavaType[] objectSuperinterfaces = objectSimpleType.getSuperinterfaces();
        assertArrayEquals(new Class[0], objectSuperinterfaces);

        JavaType stringSuperclass = stringSimpleType.getSuperclass();
        assertSame(objectSimpleType, stringSuperclass);
        JavaType[] stringSuperinterfaces = stringSimpleType.getSuperinterfaces();
        assertArrayEquals(new Class[0], stringSuperinterfaces);

        // Test narrow method
        JavaType stringStringType = TypeFactory.defaultInstance().constructType(String.class, String.class);
        SimpleType stringStringSimpleType = (SimpleType) stringStringType;
        SimpleType narrowedStringStringSimpleType = stringSimpleType.narrow(StringStringType.class);
        assertEquals(stringStringType, narrowedStringStringSimpleType);

        // Test construct method
        assertThrows(IllegalArgumentException.class, () -> SimpleType.construct(String.class));
    }

    @Test
    public void testSimpleTypeWithBindings() {
        JavaType listType = TypeFactory.defaultInstance()
                .constructParametricType(ArrayList.class, Integer.class);
        SimpleType listSimpleType = (SimpleType) listType;
        TypeBindings bindings = listSimpleType.getBindings();
        assertEquals(1, bindings.size());
        assertEquals(Integer.class, bindings.getBindings().get(0).getType());

        // Test refine method
        JavaType listTypeWithGeneric = TypeFactory.defaultInstance()
                .constructParametricType(ArrayList.class, String.class);
        SimpleType listSimpleTypeWithGeneric = (SimpleType) listTypeWithGeneric;
        JavaType refinedListSimpleType = listSimpleType.refine(
                ArrayList.class, bindings, null, null);
        assertEquals(listTypeWithGeneric, refinedListSimpleType);
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        SimpleType stringSimpleType = (SimpleType) stringType;
        Object valueHandler = new StringValueHandler();
        JavaType stringTypeWithHandler = stringSimpleType.withValueHandler(valueHandler);
        SimpleType stringSimpleTypeWithHandler = (SimpleType) stringTypeWithHandler;
        assertSame(stringSimpleType, stringSimpleTypeWithHandler);
        assertNotNull(stringSimpleTypeWithHandler.getValueHandler());
        assertEquals(valueHandler, stringSimpleTypeWithHandler.getValueHandler());
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        SimpleType stringSimpleType = (SimpleType) stringType;
        Object typeHandler = new StringTypeHandler();
        JavaType stringTypeWithHandler = stringSimpleType.withTypeHandler(typeHandler);
        SimpleType stringSimpleTypeWithHandler = (SimpleType) stringTypeWithHandler;
        assertSame(stringSimpleType, stringSimpleTypeWithHandler);
        assertNotNull(stringSimpleTypeWithHandler.getTypeHandler());
        assertEquals(typeHandler, stringSimpleTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        SimpleType stringSimpleType = (SimpleType) stringType;
        JavaType stringTypeStatic = TypeFactory.defaultInstance()
                .constructType(String.class, (TypeBindings) null, null, null, null, null, true);
        SimpleType stringSimpleTypeStatic = (SimpleType) stringTypeStatic;
        assertSame(stringSimpleType, stringSimpleTypeStatic);
        assertFalse(stringSimpleType.isStatic());
        assertTrue(stringSimpleTypeStatic.isStatic());
    }

    @Test
    public void testSimpleTypeCanonicalName() {
        JavaType listType = TypeFactory.defaultInstance()
                .constructParametricType(ArrayList.class, Integer.class);
        SimpleType listSimpleType = (SimpleType) listType;
        assertEquals("java.util.ArrayList<java.lang.Integer>", listSimpleType.getCanonicalName());
    }
}