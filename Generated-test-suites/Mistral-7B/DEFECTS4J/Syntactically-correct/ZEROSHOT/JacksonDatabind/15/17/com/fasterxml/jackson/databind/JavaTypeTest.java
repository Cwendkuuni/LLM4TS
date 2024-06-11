package com.example.junit;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JavaTypeTest {

    @Test
    public void testJavaTypeConstruction() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testNarrowBy() {
        Class<String> stringClass = String.class;
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;

        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType stringBuilderJavaType = stringJavaType.narrowBy(stringBuilderClass);

        assertEquals(StringBuilder.class, stringBuilderJavaType.getRawClass());
        assertEquals(stringJavaType, stringBuilderJavaType.withTypeHandler(stringJavaType.getValueHandler()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowByFailure() {
        Class<String> stringClass = String.class;
        Class<Integer> integerClass = Integer.class;

        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType integerJavaType = stringJavaType.narrowBy(integerClass);
    }

    @Test
    public void testWidenBy() {
        Class<String> stringClass = String.class;
        Class<Object> objectClass = Object.class;

        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType objectJavaType = stringJavaType.widenBy(objectClass);

        assertEquals(Object.class, objectJavaType.getRawClass());
        assertEquals(stringJavaType, objectJavaType.withTypeHandler(stringJavaType.getValueHandler()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWidenByFailure() {
        Class<String> stringClass = String.class;
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;

        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType stringBuilderJavaType = stringJavaType.widenBy(stringBuilderClass);
    }

    @Test
    public void testIsContainerType() {
        Class<String[]> stringArrayClass = String[].class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringArrayClass);

        assertTrue(javaType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        Class<java.util.ArrayList<String>> listClass = java.util.ArrayList.class.getComponentType();
        JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);

        assertTrue(javaType.isCollectionLikeType());
    }

    @Test
    public void testIsMapLikeType() {
        Class<java.util.HashMap<String, Integer>> mapClass = java.util.HashMap.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(mapClass);

        assertTrue(javaType.isMapLikeType());
    }

    @Test
    public void testUseStaticType() {
        Class<Integer> integerClass = Integer.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(integerClass);

        assertFalse(javaType.useStaticType());
    }

    @Test
    public void testWithStaticTyping() {
        Class<Integer> integerClass = Integer.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(integerClass);

        JavaType staticJavaType = javaType.withStaticTyping();

        assertTrue(staticJavaType.useStaticType());
        assertEquals(integerClass, staticJavaType.getRawClass());
    }
}