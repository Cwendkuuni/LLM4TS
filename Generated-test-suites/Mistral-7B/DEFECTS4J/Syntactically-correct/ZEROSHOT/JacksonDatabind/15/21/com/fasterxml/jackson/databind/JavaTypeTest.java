package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.Serializable;
import java.lang.reflect.Modifier;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ResolvedType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaTypeTest {

    @Test
    public void testJavaTypeConstruction() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);

        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
        assertEquals(String.class.getName(), javaType.getErasedSignature());
        assertEquals(String.class.getName(), javaType.getGenericSignature());
        assertNull(javaType.getValueHandler());
        assertNull(javaType.getTypeHandler());
        assertFalse(javaType.isContainerType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isAbstract());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isFinal());
        assertEquals(Modifier.CLASS, javaType.getRawClass().getModifiers());
    }

    @Test
    public void testJavaTypeNarrowing() {
        Class<String> stringClass = String.class;
        Class<Integer> intClass = Integer.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);

        JavaType narrowedType = javaType.narrowBy(intClass);
        assertNotNull(narrowedType);
        assertEquals(Integer.class, narrowedType.getRawClass());
        assertEquals(Integer.class.getName(), narrowedType.getErasedSignature());
        assertEquals(Integer.class.getName(), narrowedType.getGenericSignature());
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandler());
        assertFalse(narrowedType.isContainerType());
        assertFalse(narrowedType.isCollectionLikeType());
        assertFalse(narrowedType.isMapLikeType());
        assertFalse(narrowedType.isInterface());
        assertFalse(narrowedType.isAbstract());
        assertFalse(narrowedType.isPrimitive());
        assertFalse(narrowedType.isFinal());
        assertEquals(Modifier.CLASS, narrowedType.getRawClass().getModifiers());

        // Check that handlers are copied over
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};
        JavaType narrowedTypeWithHandlers = javaType.narrowBy(intClass, valueHandler, typeHandler);
        assertEquals(valueHandler, narrowedTypeWithHandlers.getValueHandler());
        assertEquals(typeHandler, narrowedTypeWithHandlers.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowingInvalidClass() {
        Class<String> stringClass = String.class;
        Class<Double> doubleClass = Double.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);

        javaType.narrowBy(doubleClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowingNullClass() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);

        javaType.narrowBy(null);
    }

    @Test
    public void testJavaTypeWidening() {
        Class<String> stringClass = String.class;
        Class<Object> objectClass = Object.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);

        JavaType widenedType = javaType.widenBy(objectClass);
        assertNotNull(widenedType);
        assertEquals(Object.class, widenedType.getRawClass());
        assertEquals(Object.class.getName(), widenedType.getErasedSignature());
        assertEquals(Object.class.getName(), widenedType.getGenericSignature());
        assertNull(widenedType.getValueHandler());
        assertNull(widenedType.getTypeHandler());
        assertFalse(widenedType.isContainerType());
        assertFalse(widenedType.isCollectionLikeType());
        assertFalse(widenedType.isMapLikeType());
        assertFalse(widenedType.isInterface());
        assertFalse(widenedType.isAbstract());
        assertFalse(widenedType.isPrimitive());
        assertFalse(widenedType.isFinal());
        assertEquals(Modifier.CLASS, widenedType.getRawClass().getModifiers());
    }

    @Test
    public void testJavaTypeWideningInvalidClass() {
        Class<Object> objectClass = Object.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(Object.class);

        javaType.widenBy(String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWideningNullClass() {
        Class<Object> objectClass = Object.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(Object.class);

        javaType.widenBy(null);
    }
}