package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeConstructorWithBindings() {
        Class<?> cls = String[].class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        SimpleType simpleType = new SimpleType(cls, bindings);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(new JavaType[0], simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        SimpleType copiedSimpleType = new SimpleType(simpleType);
        assertEquals(simpleType, copiedSimpleType);
    }

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMapOrCollectionClass() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArrayClass() {
        SimpleType.construct(int[].class);
    }

    @Test
    public void testNarrowMethodWithSubclass() {
        Class<?> supperClass = Object.class;
        Class<?> subclass = String.class;
        SimpleType supperType = new SimpleType(supperClass);
        SimpleType subType = new SimpleType(subclass);
        SimpleType narrowedType = subType._narrow(supperClass);
        assertEquals(subType, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowMethodWithNonSubclass() {
        Class<?> supperClass = Object.class;
        Class<?> subclass = Integer.class;
        SimpleType supperType = new SimpleType(supperClass);
        SimpleType subType = new SimpleType(subclass);
        subType._narrow(supperClass);
    }

    @Test
    public void testWithContentTypeMethod() {
        JavaType javaType = new SimpleType(String.class).getTypeFactory()
                .constructType(Integer.class);
        SimpleType simpleType = new SimpleType(String.class);
        assertNull(simpleType.withContentType(javaType));
    }

    @Test
    public void testWithTypeHandlerMethod() {
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType simpleTypeWithHandler = simpleType.withTypeHandler(handler);
        assertNotSame(simpleType, simpleTypeWithHandler);
        assertEquals(handler, simpleTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testWithStaticTypingMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType staticType = simpleType.withStaticTyping();
        assertTrue(staticType.isStatic());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testRefineMethod() {
        JavaType javaType = new SimpleType(String.class).getTypeFactory()
                .constructType(List.class);
        SimpleType simpleType = new SimpleType(String.class);
        assertNull(simpleType.refine(String[].class, TypeBindings.emptyBindings(), null, null));
        assertNotNull(simpleType.refine(List.class, TypeBindings.emptyBindings(), null, null));
    }

    @Test
    public void testBuildCanonicalNameMethod() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        String actualCanonicalName = simpleType.buildCanonicalName();
        String expectedCanonicalName = "java.lang.String";
        assertEquals(expectedCanonicalName, actualCanonicalName);

        TypeBindings bindings = new TypeBindings();
        bindings.putBinding(0, new SimpleType(Integer.class));
        SimpleType simpleTypeWithBindings = new SimpleType(cls, bindings);
        String actualCanonicalNameWithBindings = simpleTypeWithBindings.buildCanonicalName();
        StringBuilder expectedCanonicalNameWithBindings = new StringBuilder();
        expectedCanonicalNameWithBindings.append("[simple type, class ");
        expectedCanonicalNameWithBindings.append(cls.getName());
        expectedCanonicalNameWithBindings.append('<');
        expectedCanonicalNameWithBindings.append('I');
        expectedCanonicalNameWithBindings.append('>');
        expectedCanonicalNameWithBindings.append("]");
        assertEquals(expectedCanonicalNameWithBindings.toString(), actualCanonicalNameWithBindings);
    }

    @Test
    public void testIsContainerTypeMethod() {
        assertFalse(new SimpleType(String.class).isContainerType());
    }

    @Test
    public void testGetErasedSignatureMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder expectedErasedSignature = new StringBuilder();
        expectedErasedSignature.append("Ljava/lang/String;");
        StringBuilder actualErasedSignature = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expectedErasedSignature.toString(), actualErasedSignature.toString());
    }

    @Test
    public void testGetGenericSignatureMethod() {
        SimpleType simpleType = new SimpleType(String[].class);
        StringBuilder expectedGenericSignature = new StringBuilder();
        expectedGenericSignature.append("[Ljava/lang/String;");
        StringBuilder actualGenericSignature = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expectedGenericSignature.toString(), actualGenericSignature.toString());
    }
}