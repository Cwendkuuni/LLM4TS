package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

/**
 * Test class for SimpleType.
 */
public class SimpleTypeTest {

    /**
     * Test method for {@link SimpleType#SimpleType(Class)}.
     */
    @Test
    public void testSimpleTypeConstructor() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperclass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    /**
     * Test method for {@link SimpleType#SimpleType(Class, TypeBindings, JavaType, JavaType[])}.
     */
    @Test
    public void testSimpleTypeConstructorWithBindings() {
        Class<String> stringClass = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.uncheckedType(Object.class);
        JavaType[] superInterfaces = new JavaType[] { TypeFactory.uncheckedType(List.class) };
        SimpleType simpleType = new SimpleType(stringClass, bindings, superClass, superInterfaces);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperclass());
        assertArrayEquals(superInterfaces, simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    /**
     * Test method for {@link SimpleType#SimpleType(TypeBase)}.
     */
    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType originalSimpleType = new SimpleType(stringClass);
        SimpleType copiedSimpleType = new SimpleType(originalSimpleType);
        assertNotNull(copiedSimpleType);
        assertEquals(originalSimpleType.getRawClass(), copiedSimpleType.getRawClass());
        assertEquals(originalSimpleType.getBindings(), copiedSimpleType.getBindings());
        assertEquals(originalSimpleType.getSuperclass(), copiedSimpleType.getSuperclass());
        assertArrayEquals(originalSimpleType.getSuperInterfaces(), copiedSimpleType.getSuperInterfaces());
        assertEquals(originalSimpleType.getValueHandler(), copiedSimpleType.getValueHandler());
        assertEquals(originalSimpleType.getTypeHandler(), copiedSimpleType.getTypeHandler());
        assertEquals(originalSimpleType.isStatic(), copiedSimpleType.isStatic());
    }

    /**
     * Test method for {@link SimpleType#SimpleType(Class, TypeBindings, JavaType, JavaType[], Object, Object, boolean)}.
     */
    @Test
    public void testSimpleTypeConstructorWithValueAndTypeHandlers() {
        Class<String> stringClass = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.uncheckedType(Object.class);
        JavaType[] superInterfaces = new JavaType[] { TypeFactory.uncheckedType(List.class) };
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};
        SimpleType simpleType = new SimpleType(stringClass, bindings, superClass, superInterfaces, valueHandler, typeHandler, false);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperclass());
        assertArrayEquals(superInterfaces, simpleType.getSuperInterfaces());
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    /**
     * Test method for {@link SimpleType#SimpleType(Class, TypeBindings, JavaType, JavaType[], int, Object, Object, boolean)}.
     */
    @Test
    public void testSimpleTypeConstructorWithExtraHashCode() {
        Class<String> stringClass = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.uncheckedType(Object.class);
        JavaType[] superInterfaces = new JavaType[] { TypeFactory.uncheckedType(List.class) };
        int extraHash = 42;
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};
        SimpleType simpleType = new SimpleType(stringClass, bindings, superClass, superInterfaces, extraHash, valueHandler, typeHandler, false);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperclass());
        assertArrayEquals(superInterfaces, simpleType.getSuperInterfaces());
        assertEquals(extraHash, simpleType.getExtraHashCode());
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    /**
     * Test method for {@link SimpleType#constructUnsafe(Class)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeIllegalArgumentException() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(stringClass);
    }

    /**
     * Test method for {@link SimpleType#construct(Class)}.
     *
     * @deprecated Since 2.7
     */
    @Test(expected = IllegalArgumentException.class)
    @Deprecated
    public void testConstructIllegalArgumentException() {
        Class<String> stringClass = String.class;
        SimpleType.construct(stringClass);
    }

    /**
     * Test method for {@link SimpleType#_narrow(Class)}.
     */
    @Test
    public void testNarrow() {
        Class<String> stringClass = String.class;
        Class<Integer> integerClass = Integer.class;
        SimpleType simpleTypeString = new SimpleType(stringClass);
        SimpleType simpleTypeInteger = new SimpleType(integerClass);
        SimpleType narrowedType = simpleTypeString._narrow(integerClass);
        assertNotNull(narrowedType);
        assertNotSame(simpleTypeString, narrowedType);
        assertEquals(integerClass, narrowedType.getRawClass());
        assertEquals(simpleTypeString.getBindings(), narrowedType.getBindings());
        assertEquals(simpleTypeString.getSuperclass(), narrowedType.getSuperclass());
        assertArrayEquals(simpleTypeString.getSuperInterfaces(), narrowedType.getSuperInterfaces());
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandler());
        assertFalse(narrowedType.isStatic());
    }

    /**
     * Test method for {@link SimpleType#withContentType(JavaType)}.
     *
     * @since 2.7
     */
    @Test
    public void testWithContentTypeIllegalArgumentException() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType javaType = TypeFactory.uncheckedType(List.class);
        try {
            simpleType.withContentType(javaType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link SimpleType#withTypeHandler(Object)}.
     */
    @Test
    public void testWithTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Object handler = new Object() {};
        SimpleType withTypeHandler = simpleType.withTypeHandler(handler);
        assertNotNull(withTypeHandler);
        assertNotSame(simpleType, withTypeHandler);
        assertEquals(simpleType.getRawClass(), withTypeHandler.getRawClass());
        assertEquals(simpleType.getBindings(), withTypeHandler.getBindings());
        assertEquals(simpleType.getSuperclass(), withTypeHandler.getSuperclass());
        assertArrayEquals(simpleType.getSuperInterfaces(), withTypeHandler.getSuperInterfaces());
        assertSame(handler, withTypeHandler.getTypeHandler());
        assertFalse(withTypeHandler.isStatic());
    }

    /**
     * Test method for {@link SimpleType#withContentTypeHandler(Object)}.
     *
     * @since 2.7
     */
    @Test
    public void testWithContentTypeHandlerIllegalArgumentException() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Object handler = new Object() {};
        try {
            simpleType.withContentTypeHandler(handler);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link SimpleType#withValueHandler(Object)}.
     */
    @Test
    public void testWithValueHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Object handler = new Object() {};
        SimpleType withValueHandler = simpleType.withValueHandler(handler);
        assertNotNull(withValueHandler);
        assertNotSame(simpleType, withValueHandler);
        assertEquals(simpleType.getRawClass(), withValueHandler.getRawClass());
        assertEquals(simpleType.getBindings(), withValueHandler.getBindings());
        assertEquals(simpleType.getSuperclass(), withValueHandler.getSuperclass());
        assertArrayEquals(simpleType.getSuperInterfaces(), withValueHandler.getSuperInterfaces());
        assertSame(handler, withValueHandler.getValueHandler());
        assertFalse(withValueHandler.isStatic());
    }

    /**
     * Test method for {@link SimpleType#withContentValueHandler(Object)}.
     *
     * @since 2.7
     */
    @Test
    public void testWithContentValueHandlerIllegalArgumentException() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Object handler = new Object() {};
        try {
            simpleType.withContentValueHandler(handler);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link SimpleType#withStaticTyping()}.
     */
    @Test
    public void testWithStaticTyping() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType staticType = simpleType.withStaticTyping();
        assertNotNull(staticType);
        assertNotSame(simpleType, staticType);
        assertEquals(simpleType.getRawClass(), staticType.getRawClass());
        assertEquals(simpleType.getBindings(), staticType.getBindings());
        assertEquals(simpleType.getSuperclass(), staticType.getSuperclass());
        assertArrayEquals(simpleType.getSuperInterfaces(), staticType.getSuperInterfaces());
        assertSame(simpleType.getValueHandler(), staticType.getValueHandler());
        assertSame(simpleType.getTypeHandler(), staticType.getTypeHandler());
        assertTrue(staticType.isStatic());
    }

    /**
     * Test method for {@link SimpleType#refine(Class, TypeBindings, JavaType, JavaType[])}.
     */
    @Test
    public void testRefineNull() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType refinedType = simpleType.refine(null, bindings, superClass, superInterfaces);
        assertNull(refinedType);
    }

    /**
     * Test method for {@link SimpleType#buildCanonicalName()}.
     */
    @Test
    public void testBuildCanonicalName() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals("class java.lang.String", simpleType.buildCanonicalName());

        TypeBindings bindings = TypeBindings.emptyBindings();
        SimpleType simpleTypeWithBindings = new SimpleType(stringClass, bindings);
        StringBuilder stringBuilder = new StringBuilder();
        simpleTypeWithBindings.buildCanonicalName(stringBuilder);
        assertEquals("class java.lang.String<></>", stringBuilder.toString());

        JavaType javaType = TypeFactory.uncheckedType(List.class);
        SimpleType simpleTypeWithGenericType = new SimpleType(stringClass, bindings, javaType);
        StringBuilder stringBuilderWithGenericType = new StringBuilder();
        simpleTypeWithGenericType.buildCanonicalName(stringBuilderWithGenericType);
        assertEquals("class java.lang.String<java.util.List>", stringBuilderWithGenericType.toString());
    }

    /**
     * Test method for {@link SimpleType#isContainerType()}.
     */
    @Test
    public void testIsContainerType() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertFalse(simpleType.isContainerType());
    }

    /**
     * Test method for {@link SimpleType#getErasedSignature(StringBuilder)}.
     */
    @Test
    public void testGetErasedSignature() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        StringBuilder stringBuilder = new StringBuilder();
        simpleType.getErasedSignature(stringBuilder);
        assertEquals("java.lang.String", stringBuilder.toString());
    }

    /**
     * Test method for {@link SimpleType#getGenericSignature(StringBuilder)}.
     */
    @Test
    public void testGetGenericSignature() {
        Class<String> stringClass = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        SimpleType simpleType = new SimpleType(stringClass, bindings);
        StringBuilder stringBuilder = new StringBuilder();
        simpleType.getGenericSignature(stringBuilder);
        assertEquals("String", stringBuilder.toString());

        JavaType javaType = TypeFactory.uncheckedType(List.class);
        SimpleType simpleTypeWithGenericType = new SimpleType(stringClass, bindings, javaType);
        StringBuilder stringBuilderWithGenericType = new StringBuilder();
        simpleTypeWithGenericType.getGenericSignature(stringBuilderWithGenericType);
        assertEquals("String<java.util.List>", stringBuilderWithGenericType.toString());
    }
}