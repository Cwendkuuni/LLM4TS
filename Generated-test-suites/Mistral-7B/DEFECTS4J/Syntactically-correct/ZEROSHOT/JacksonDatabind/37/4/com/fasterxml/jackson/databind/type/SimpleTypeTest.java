package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertNotNull(simpleType.getBindings());
        assertEquals(0, simpleType.getSuperClass().size());
        assertEquals(0, simpleType.getSuperInterfaces().length);
    }

    @Test
    public void testSimpleTypeConstructionWithBindings() {
        Class<String> stringClass = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        SimpleType simpleType = new SimpleType(stringClass, bindings);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(0, simpleType.getSuperClass().size());
        assertEquals(0, simpleType.getSuperInterfaces().length);
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType original = new SimpleType(stringClass);
        SimpleType copy = new SimpleType(original);
        assertEquals(String.class, copy.getRawClass());
        assertEquals(original.getBindings(), copy.getBindings());
        assertEquals(original.getSuperClass(), copy.getSuperClass());
        assertArrayEquals(original.getSuperInterfaces(), copy.getSuperInterfaces());
    }

    @Test
    public void testSimpleTypeWithContentType() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType contentType = JavaType.constructType(int.class);
        SimpleType result = simpleType.withContentType(contentType);
        assertNotNull(result);
        assertEquals(stringClass, result.getRawClass());
        assertNotNull(result.getBindings());
        assertEquals(0, result.getSuperClass().size());
        assertEquals(0, result.getSuperInterfaces().length);
        assertEquals(contentType, result.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithContentTypeThrowsException() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        simpleType.withContentType(null);
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Object typeHandler = new Object();
        SimpleType result = simpleType.withTypeHandler(typeHandler);
        assertNotNull(result);
        assertEquals(stringClass, result.getRawClass());
        assertNotNull(result.getBindings());
        assertEquals(0, result.getSuperClass().size());
        assertEquals(0, result.getSuperInterfaces().length);
        assertEquals(typeHandler, result.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithTypeHandlerThrowsException() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        simpleType.withTypeHandler(null);
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Object valueHandler = new Object();
        SimpleType result = simpleType.withValueHandler(valueHandler);
        assertNotNull(result);
        assertEquals(stringClass, result.getRawClass());
        assertNotNull(result.getBindings());
        assertEquals(0, result.getSuperClass().size());
        assertEquals(0, result.getSuperInterfaces().length);
        assertEquals(valueHandler, result.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithValueHandlerThrowsException() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        simpleType.withValueHandler(null);
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType result = simpleType.withStaticTyping();
        assertNotNull(result);
        assertEquals(stringClass, result.getRawClass());
        assertNotNull(result.getBindings());
        assertEquals(0, result.getSuperClass().size());
        assertEquals(0, result.getSuperInterfaces().length);
        assertTrue(result.isStatic());
    }

    @Test
    public void testNarrowMethod() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType narrowedType = simpleType.narrow(StringBuilder.class);
        assertNotNull(narrowedType);
        assertEquals(StringBuilder.class, narrowedType.getRawClass());
        assertNotNull(narrowedType.getBindings());
        assertEquals(0, narrowedType.getSuperClass().size());
        assertEquals(0, narrowedType.getSuperInterfaces().length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowMethodThrowsException() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        simpleType.narrow(LinkedList.class);
    }

    @Test
    public void testConstructUnsafeMethod() {
        Class<String> stringClass = String.class;
        SimpleType result = SimpleType.constructUnsafe(stringClass);
        assertNotNull(result);
        assertEquals(stringClass, result.getRawClass());
        assertNotNull(result.getBindings());
        assertEquals(0, result.getSuperClass().size());
        assertEquals(0, result.getSuperInterfaces().length);
    }

    @Test
    public void testConstructMethod() {
        Class<String> stringClass = String.class;
        SimpleType result = SimpleType.construct(stringClass);
        assertNotNull(result);
        assertEquals(stringClass, result.getRawClass());
        assertNotNull(result.getBindings());
        assertEquals(0, result.getSuperClass().size());
        assertEquals(0, result.getSuperInterfaces().length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMethodThrowsException() {
        Class<List<String>> listClass = new ArrayList<String>().getClass();
        SimpleType.construct(listClass);
    }

    @Test
    public void testBuildCanonicalNameMethod() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        String result = simpleType.buildCanonicalName();
        assertEquals("java.lang.String", result);
    }

    @Test
    public void testIsContainerTypeMethod() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignatureMethod() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        StringBuilder result = simpleType.getErasedSignature(new StringBuilder());
        StringBuilder expected = new StringBuilder("Ljava/lang/String;");
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetGenericSignatureMethod() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        StringBuilder result = simpleType.getGenericSignature(new StringBuilder());
        StringBuilder expected = new StringBuilder("Ljava/lang/String;");
        assertEquals(expected.toString(), result.toString());
    }

    private JavaType containedType(int index) {
        SimpleType simpleType = new SimpleType(String.class);
        TypeBindings bindings = new TypeBindings();
        bindings.put(0, JavaType.constructType(int.class));
        simpleType = new SimpleType(String.class, bindings);
        return simpleType.getContentType();
    }

    private void assertArrayEquals(JavaType[] expected, JavaType[] actual) {
        Arrays.sort(expected);
        Arrays.sort(actual);
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}