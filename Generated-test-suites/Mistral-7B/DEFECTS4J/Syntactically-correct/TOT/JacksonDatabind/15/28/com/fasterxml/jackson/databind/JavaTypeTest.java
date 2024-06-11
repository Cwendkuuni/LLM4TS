package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link JavaType} class.
 */
public class JavaTypeTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        TypeFactory typeFactory = new TypeFactory();

        // Test null argument
        try {
            JavaType javaType = typeFactory.constructType(null);
            assertFalse("Expected null argument to throw exception", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test construct valid JavaType
        JavaType javaType = typeFactory.constructType(String.class);
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
        assertTrue(javaType.isConcrete());
        assertFalse(javaType.isAbstract());
        assertFalse(javaType.isEnumType());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isFinal());
        assertFalse(javaType.isThrowable());
        assertFalse(javaType.isArrayType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isContainerType());
        assertFalse(javaType.useStaticType());
    }

    /**
     * Test method 'withTypeHandler'.
     */
    @Test
    public void testWithTypeHandler() {
        TypeFactory typeFactory = new TypeFactory();

        JavaType javaType = typeFactory.constructType(String.class);
        JavaType javaTypeWithHandler = javaType.withTypeHandler("Handler");

        assertEquals(javaType.getClass(), javaTypeWithHandler.getClass());
        assertSame(javaType.getRawClass(), javaTypeWithHandler.getRawClass());
        assertNotSame(javaType._typeHandler, javaTypeWithHandler._typeHandler);
        assertEquals("Handler", javaTypeWithHandler._typeHandler);
    }

    /**
     * Test method 'copy'.
     */
    @Test
    public void testCopy() {
        TypeFactory typeFactory = new TypeFactory();

        JavaType javaType = typeFactory.constructType(String.class);

        JavaType javaTypeCopy = javaType.copy();

        assertEquals(javaType.getClass(), javaTypeCopy.getClass());
        assertSame(javaType.getRawClass(), javaTypeCopy.getRawClass());
        assertSame(javaType._class, javaTypeCopy._class);
        assertSame(javaType._hash, javaTypeCopy._hash);
        assertSame(javaType._valueHandler, javaTypeCopy._valueHandler);
        assertSame(javaType._typeHandler, javaTypeCopy._typeHandler);
        assertSame(javaType._asStatic, javaTypeCopy._asStatic);
    }

    /**
     * Test method 'withContentTypeHandler'.
     */
    @Test
    public void testWithContentTypeHandler() {
        TypeFactory typeFactory = new TypeFactory();

        JavaType javaType = typeFactory.constructType(String[].class);
        JavaType javaTypeWithContentHandler = javaType.withContentTypeHandler("Handler");

        assertEquals(javaType.getClass(), javaTypeWithHandler.getClass());
        assertSame(javaType.getRawClass(), javaTypeWithHandler.getRawClass());
        assertNotSame(javaType._typeHandler, javaTypeWithHandler._typeHandler);
        assertEquals("Handler", javaTypeWithHandler._typeHandler);
    }

    /**
     * Test method 'withValueHandler'.
     */
    @Test
    public void testWithValueHandler() {
        TypeFactory typeFactory = new TypeFactory();

        JavaType javaType = typeFactory.constructType(String.class);
        JavaType javaTypeWithHandler = javaType.withValueHandler("Handler");

        assertEquals(javaType.getClass(), javaTypeWithHandler.getClass());
        assertSame(javaType.getRawClass(), javaTypeWithHandler.getRawClass());
        assertNotSame(javaType._valueHandler, javaTypeWithHandler._valueHandler);
        assertEquals("Handler", javaTypeWithHandler._valueHandler);
    }

    /**
     * Test method 'withContentValueHandler'.
     */
    @Test
    public void testWithContentValueHandler() {
        TypeFactory typeFactory = new TypeFactory();

        JavaType javaType = typeFactory.constructType(String[].class);
        JavaType javaTypeWithContentValueHandler = javaType.withContentValueHandler("Handler");

        assertEquals(javaType.getClass(), javaTypeWithContentValueHandler.getClass());
        assertSame(javaType.getRawClass(), javaTypeWithContentValueHandler.getRawClass());
        assertNotSame(javaType._valueHandler, javaTypeWithContentValueHandler._valueHandler);
        assertEquals("Handler", javaTypeWithContentValueHandler._valueHandler);
    }

    /**
     * Test method 'withStaticTyping'.
     */
    @Test
    public void testWithStaticTyping() {
        TypeFactory typeFactory = new TypeFactory();

        JavaType javaType = typeFactory.constructType(String.class);
        JavaType javaTypeWithStaticTyping = javaType.withStaticTyping();

        assertEquals(javaType.getClass(), javaTypeWithStaticTyping.getClass());
        assertSame(javaType.getRawClass(), javaTypeWithStaticTyping.getRawClass());
        assertSame(javaType._class, javaTypeWithStaticTyping._class);
        assertSame(javaType._hash, javaTypeWithStaticTyping._hash);
        assertSame(javaType._valueHandler, javaTypeWithStaticTyping._valueHandler);
        assertSame(javaType._typeHandler, javaTypeWithStaticTyping._typeHandler);
        assertTrue(javaTypeWithStaticTyping._asStatic);
    }

    /**
     * Test method 'narrowBy'.
     */
    @Test
    public void testNarrowBy() {
        TypeFactory typeFactory = new TypeFactory();

        JavaType parentType = typeFactory.constructType(Object.class);
        JavaType childType = typeFactory.constructType(String.class);

        JavaType narrowedType = parentType.narrowBy(childType.getRawClass());

        assertEquals(childType.getClass(), narrowedType.getClass());
        assertSame(childType.getRawClass(), narrowedType.getRawClass());
        assertNotSame(childType._valueHandler, narrowedType._valueHandler);
        assertSame(childType._typeHandler, narrowedType._typeHandler);
        assertSame(childType._asStatic, narrowedType._asStatic);
    }

    /**
     * Test method 'widenBy'.
     */
    @Test
    public void testWidenBy() {
        TypeFactory typeFactory = new TypeFactory();

        JavaType childType = typeFactory.constructType(String.class);
        JavaType parentType = typeFactory.constructType(Object.class);

        JavaType widenedType = childType.widenBy(parentType.getRawClass());

        assertEquals(parentType.getClass(), widenedType.getClass());
        assertSame(parentType.getRawClass(), widenedType.getRawClass());
        assertSame(childType._class, widenedType._class);
        assertSame(childType._hash, widenedType._hash);
        assertNotSame(childType._valueHandler, widenedType._valueHandler);
        assertSame(childType._typeHandler, widenedType._typeHandler);
        assertSame(childType._asStatic, widenedType._asStatic);
    }

    /**
     * Test method 'narrowContentsBy'.
     */
    @Test
    public void testNarrowContentsBy() {
        TypeFactory typeFactory = new TypeFactory();

        JavaType containerType = typeFactory.constructType(String[].class);
        JavaType containedType = typeFactory.constructType(Integer.class);

        JavaType narrowedContainerType = containerType.narrowContentsBy(containedType.getRawClass());

        assertEquals(containerType.getClass(), narrowedContainerType.getClass());
        assertSame(containerType.getRawClass(), narrowedContainerType.getRawClass());
        assertTrue(narrowedContainerType.isContainerType());
        assertFalse(narrowedContainerType.isMapLikeType());
        assertFalse(narrowedContainerType.isCollectionLikeType());
        assertTrue(narrowedContainerType.containsType(containedType));
    }

    /**
     * Test method 'widenContentsBy'.
     */
    @Test
    public void testWidenContentsBy() {
        TypeFactory typeFactory = new TypeFactory();

        JavaType containerType = typeFactory.constructType(String[].class);
        JavaType containedType = typeFactory.constructType(Integer.class);

        JavaType widenedContainerType = containerType.widenContentsBy(Object[].class);

        assertEquals(containerType.getClass(), widenedContainerType.getClass());
        assertSame(containerType.getRawClass(), widenedContainerType.getRawClass());
        assertTrue(widenedContainerType.isContainerType());
        assertTrue(widenedContainerType.isMapLikeType());
        assertTrue(widenedContainerType.isCollectionLikeType());
        assertFalse(widenedContainerType.containsType(containedType));
    }
}