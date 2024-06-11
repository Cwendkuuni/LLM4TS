package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JavaTypeTest {

    @Test
    public void testJavaTypeConstruction() {
        Class<String> stringClass = String.class;
        JavaType stringType = TypeFactory.defaultInstance().constructType(stringClass);

        assertEquals(String.class, stringType.getRawClass());
        assertEquals(false, stringType.isAbstract());
        assertEquals(false, stringType.isConcrete());
        assertEquals(false, stringType.isEnumType());
        assertEquals(false, stringType.isInterface());
        assertEquals(false, stringType.isPrimitive());
        assertEquals(false, stringType.isFinal());
        assertEquals(false, stringType.isContainerType());
        assertEquals(false, stringType.isCollectionLikeType());
        assertEquals(false, stringType.isMapLikeType());
        assertEquals(false, stringType.useStaticType());
    }

    @Test
    public void testJavaTypeNarrowing() {
        Class<String> stringClass = String.class;
        JavaType stringType = TypeFactory.defaultInstance().constructType(stringClass);

        Class<StringBuilder> stringBuilderClass = StringBuffer.class;
        JavaType stringBuilderType = stringType.narrowBy(stringBuilderClass);

        assertEquals(StringBuilder.class, stringBuilderType.getRawClass());
        assertEquals(stringType, stringBuilderType.getBaseType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowingWithInvalidClass() {
        Class<String> stringClass = String.class;
        JavaType stringType = TypeFactory.defaultInstance().constructType(stringClass);

        Class<Integer> integerClass = Integer.class;
        JavaType integerType = stringType.narrowBy(integerClass);
    }

    @Test
    public void testJavaTypeWidening() {
        Class<String> stringClass = String.class;
        JavaType stringType = TypeFactory.defaultInstance().constructType(stringClass);

        Class<Object> objectClass = Object.class;
        JavaType objectType = stringType.widenBy(objectClass);

        assertEquals(Object.class, objectType.getRawClass());
        assertEquals(stringType, objectType.getBaseType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWideningWithInvalidClass() {
        Class<String> stringClass = String.class;
        JavaType stringType = TypeFactory.defaultInstance().constructType(stringClass);

        Class<Integer> integerClass = Integer.class;
        JavaType integerType = stringType.widenBy(integerClass);
    }
}