package com.example.junit;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(Arrays.asList(), simpleType.getBindings());
        assertEquals(null, simpleType.getSuperClass());
        assertEquals(null, simpleType.getSuperInterfaces());
        assertEquals(0, simpleType.getExtraHash());
        assertEquals(null, simpleType.getValueHandler());
        assertEquals(null, simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeConstructionWithBindings() {
        SimpleType simpleType = new SimpleType(int[].class, Arrays.asList(new SimpleType(int.class)), null, null);
        assertNotNull(simpleType);
        assertEquals(int[].class, simpleType.getRawClass());
        assertEquals(Arrays.asList(new SimpleType(int.class)), simpleType.getBindings());
        assertEquals(null, simpleType.getSuperClass());
        assertEquals(null, simpleType.getSuperInterfaces());
        assertEquals(0, simpleType.getExtraHash());
        assertEquals(null, simpleType.getValueHandler());
        assertEquals(null, simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        SimpleType original = new SimpleType(int[].class, Arrays.asList(new SimpleType(int.class)), null, null);
        SimpleType copy = new SimpleType(original);
        assertNotNull(copy);
        assertEquals(original.getRawClass(), copy.getRawClass());
        assertEquals(original.getBindings(), copy.getBindings());
        assertEquals(original.getSuperClass(), copy.getSuperClass());
        assertEquals(original.getSuperInterfaces(), copy.getSuperInterfaces());
        assertEquals(original.getExtraHash(), copy.getExtraHash());
        assertEquals(original.getValueHandler(), copy.getValueHandler());
        assertEquals(original.getTypeHandler(), copy.getTypeHandler());
        assertEquals(original.isStatic(), copy.isStatic());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(Arrays.asList(), simpleType.getBindings());
        assertEquals(null, simpleType.getSuperClass());
        assertEquals(null, simpleType.getSuperInterfaces());
        assertEquals(0, simpleType.getExtraHash());
        assertEquals(null, simpleType.getValueHandler());
        assertEquals(null, simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        new SimpleType(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        new SimpleType(List.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        new SimpleType(int[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType stringType = new SimpleType(String.class);
        SimpleType stringSubclass = new SimpleType(StringBuilder.class);
        SimpleType narrowedType = stringType.narrow(StringBuilder.class);
        assertEquals(stringSubclass, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        SimpleType stringType = new SimpleType(String.class);
        stringType.narrow(Integer.class);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType contentType = new SimpleType(Integer.class);
        try {
            simpleType.withContentType(contentType);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object typeHandler = new Object() {};
        SimpleType withTypeHandler = simpleType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object contentTypeHandler = new Object() {};
        try {
            simpleType.withContentTypeHandler(contentTypeHandler);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object valueHandler = new Object() {};
        SimpleType withValueHandler = simpleType.withValueHandler(valueHandler);
        assertEquals(valueHandler, withValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object contentValueHandler = new Object() {};
        try {
            simpleType.withContentValueHandler(contentValueHandler);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType staticType = simpleType.withStaticTyping();
        assertTrue(staticType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNull(simpleType.refine(Integer.class, null, null, null));
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(String[].class);
        assertEquals("[Ljava.lang.String;", simpleType.buildCanonicalName());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("String", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String[].class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("[Ljava.lang.String;", sb.toString());
    }
}