package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Modifier;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;

public class JavaTypeTest {

    @Test
    public void testJavaTypeInstance() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
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
        assertFalse(javaType.isContainerType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.hasGenericTypes());
        assertNull(javaType.getKeyType());
        assertNull(javaType.getContentType());
        assertEquals(0, javaType.containedTypeCount());

        assertEquals("Ljava/lang/String;", javaType.getErasedSignature());
        assertEquals("Ljava/lang/String;<T:Ljava/lang/Object;>", javaType.getGenericSignature());
    }

    @Test
    public void testJavaTypeNarrowing() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType stringBufferType = stringType.narrowBy(StringBuffer.class);
        assertNotNull(stringBufferType);
        assertEquals(StringBuffer.class, stringBufferType.getRawClass());
        assertTrue(stringBufferType.isConcrete());
        assertFalse(stringBufferType.isAbstract());
        assertFalse(stringBufferType.isEnumType());
        assertFalse(stringBufferType.isInterface());
        assertFalse(stringBufferType.isPrimitive());
        assertFalse(stringBufferType.isFinal());
        assertFalse(stringBufferType.isThrowable());
        assertFalse(stringBufferType.isArrayType());
        assertFalse(stringBufferType.isContainerType());
        assertFalse(stringBufferType.isCollectionLikeType());
        assertFalse(stringBufferType.isMapLikeType());
        assertFalse(stringBufferType.hasGenericTypes());
        assertNull(stringBufferType.getKeyType());
        assertNull(stringBufferType.getContentType());
        assertEquals(0, stringBufferType.containedTypeCount());

        assertEquals("Ljava/lang/StringBuffer;", stringType.getErasedSignature());
        assertEquals("Ljava/lang/String;<T:Ljava/lang/Object;>", javaType.getGenericSignature());
        assertEquals("Ljava/lang/StringBuffer;<T:Ljava/lang/Object;>", stringBufferType.getGenericSignature());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowingFailure() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = stringType.narrowBy(int.class);

    }

    @Test
    public void testJavaTypeWidening() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType objectType = stringType.widenBy(Object.class);
        assertNotNull(objectType);
        assertEquals(Object.class, objectType.getRawClass());
        assertTrue(objectType.isConcrete());
        assertFalse(objectType.isAbstract());
        assertFalse(objectType.isEnumType());
        assertFalse(objectType.isInterface());
        assertFalse(objectType.isPrimitive());
        assertFalse(objectType.isFinal());
        assertFalse(objectType.isThrowable());
        assertFalse(objectType.isArrayType());
        assertFalse(objectType.isContainerType());
        assertFalse(objectType.isCollectionLikeType());
        assertFalse(objectType.isMapLikeType());
        assertFalse(objectType.hasGenericTypes());
        assertNull(objectType.getKeyType());
        assertNull(objectType.getContentType());
        assertEquals(0, objectType.containedTypeCount());

        assertEquals("Ljava/lang/String;", stringType.getErasedSignature());
        assertEquals("Ljava/lang/String;<T:Ljava/lang/Object;>", javaType.getGenericSignature());
        assertEquals("Ljava/lang/Object;<T:Ljava/lang/Object;>", objectType.getGenericSignature());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWideningFailure() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = stringType.widenBy(int[].class);

    }

    @Test
    public void testJavaTypeDynamicTyping() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType objectType = stringType.withStaticTyping();
        assertNotNull(objectType);
        assertEquals(String.class, objectType.getRawClass());
        assertTrue(objectType.isConcrete());
        assertFalse(objectType.isAbstract());
        assertFalse(objectType.isEnumType());
        assertFalse(objectType.isInterface());
        assertFalse(objectType.isPrimitive());
        assertFalse(objectType.isFinal());
        assertFalse(objectType.isThrowable());
        assertFalse(objectType.isArrayType());
        assertFalse(objectType.isContainerType());
        assertFalse(objectType.isCollectionLikeType());
        assertFalse(objectType.isMapLikeType());
        assertFalse(objectType.hasGenericTypes());
        assertNull(objectType.getKeyType());
        assertNull(objectType.getContentType());
        assertEquals(0, objectType.containedTypeCount());

        assertEquals("Ljava/lang/String;", stringType.getErasedSignature());
        assertEquals("Ljava/lang/String;<T:Ljava/lang/Object;>", javaType.getGenericSignature());
        assertEquals("Ljava/lang/String;", objectType.getErasedSignature());
        assertEquals("Ljava/lang/String;<T:Ljava/lang/Object;>", objectType.getGenericSignature());
    }

    @Test
    public void testJavaTypeStaticTyping() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType stringTypeStatic = stringType.withStaticTyping();
        assertNotNull(stringTypeStatic);
        assertEquals(String.class, stringTypeStatic.getRawClass());
        assertTrue(stringTypeStatic.isConcrete());
        assertFalse(stringTypeStatic.isAbstract());
        assertFalse(stringTypeStatic.isEnumType());
        assertFalse(stringTypeStatic.isInterface());
        assertFalse(stringTypeStatic.isPrimitive());
        assertFalse(stringTypeStatic.isFinal());
        assertFalse(stringTypeStatic.isThrowable());
        assertFalse(stringTypeStatic.isArrayType());
        assertFalse(stringTypeStatic.isContainerType());
        assertFalse(stringTypeStatic.isCollectionLikeType());
        assertFalse(stringTypeStatic.isMapLikeType());
        assertFalse(stringTypeStatic.hasGenericTypes());
        assertNull(stringTypeStatic.getKeyType());
        assertNull(stringTypeStatic.getContentType());
        assertEquals(0, stringTypeStatic.containedTypeCount());

        assertEquals("Ljava/lang/String;", stringType.getErasedSignature());
        assertEquals("Ljava/lang/String;<T:Ljava/lang/Object;>", javaType.getGenericSignature());
        assertEquals("Ljava/lang/String;", stringTypeStatic.getErasedSignature());
        assertEquals("Ljava/lang/String;<T:Ljava/lang/String;>", stringTypeStatic.getGenericSignature());
    }

}