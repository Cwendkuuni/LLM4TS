package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaTypeTest {

    @org.junit.Test
    public void testJavaTypeWithValueHandler() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(String.class);
        JavaType javaTypeWithValueHandler = javaType.withValueHandler(new Object() {});

        assertEquals(javaType.getClass(), javaTypeWithValueHandler.getClass());
        assertNotNull(javaTypeWithValueHandler.getValueHandler());
    }

    @org.junit.Test
    public void testJavaTypeWithTypeHandler() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(List.class);
        JavaType javaTypeWithTypeHandler = javaType.withTypeHandler(new ArrayList<>());

        assertEquals(javaType.getClass(), javaTypeWithTypeHandler.getClass());
        assertNotNull(javaTypeWithTypeHandler.getTypeHandler());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWithInvalidSubclass() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(String.class);
        JavaType javaTypeWithInvalidSubclass = javaType.narrowBy(Integer.class);
    }

    @org.junit.Test
    public void testJavaTypeWithAsStatic() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(String.class);
        JavaType javaTypeWithAsStatic = javaType.withStaticTyping();

        assertTrue(javaTypeWithAsStatic.isStaticTyping());
    }

    @org.junit.Test
    public void testJavaTypeNarrowBy() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaTypeString = typeFactory.constructType(String.class);
        JavaType javaTypeStringBuffer = typeFactory.constructType(StringBuffer.class);

        JavaType javaTypeStringNarrow = javaTypeString.narrowBy(StringBuffer.class);

        assertEquals(javaTypeStringNarrow.getClass(), javaTypeStringBuffer.getClass());
    }

    @org.junit.Test
    public void testJavaTypeWidenBy() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaTypeList = typeFactory.constructType(List.class);
        JavaType javaTypeArrayList = typeFactory.constructType(ArrayList.class);

        JavaType javaTypeListWiden = javaTypeList.widenBy(ArrayList.class);

        assertEquals(javaTypeListWiden.getClass(), javaTypeArrayList.getClass());
    }

    @org.junit.Test
    public void testJavaTypeNarrowContentsBy() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaTypeListOfString = typeFactory.constructType(new TypeReference<List<String>>(){}.getType());
        JavaType javaTypeListOfInteger = typeFactory.constructType(new TypeReference<List<Integer>>(){}.getType());

        JavaType javaTypeListOfStringNarrow = javaTypeListOfString.narrowContentsBy(Integer.class);

        assertEquals(javaTypeListOfStringNarrow.getClass(), javaTypeListOfInteger.getClass());
    }

    @org.junit.Test
    public void testJavaTypeWidenContentsBy() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaTypeMapOfStringString = typeFactory.constructType(new TypeReference<Map<String, String>>(){}.getType());
        JavaType javaTypeMapOfStringInteger = typeFactory.constructType(new TypeReference<Map<String, Integer>>(){}.getType());

        JavaType javaTypeMapOfStringStringWiden = javaTypeMapOfStringString.widenContentsBy(Integer.class);

        assertEquals(javaTypeMapOfStringStringWiden.getClass(), javaTypeMapOfStringInteger.getClass());
    }

    @org.junit.Test
    public void testJavaTypeGetRawClass() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaTypeString = typeFactory.constructType(String.class);

        assertEquals(String.class, javaTypeString.getRawClass());
    }

    @org.junit.Test
    public void testJavaTypeIsInterface() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaTypeList = typeFactory.constructType(List.class);

        assertTrue(javaTypeList.isInterface());
    }

    @org.junit.Test
    public void testJavaTypeIsConcrete() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaTypeString = typeFactory.constructType(String.class);

        assertTrue(javaTypeString.isConcrete());
    }

    @org.junit.Test
    public void testJavaTypeIsEnumType() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaTypeEnum = typeFactory.constructType(Enum.class);

        assertTrue(javaTypeEnum.isEnumType());
    }

    @org.junit.Test
    public void testJavaTypeIsArrayType() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaTypeIntArray = typeFactory.constructType(int[].class);

        assertTrue(javaTypeIntArray.isArrayType());
    }

    @org.junit.Test
    public void testJavaTypeIsMapLikeType() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaTypeMap = typeFactory.constructType(HashMap.class);

        assertTrue(javaTypeMap.isMapLikeType());
    }

    @org.junit.Test
    public void testJavaTypeIsCollectionLikeType() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaTypeList = typeFactory.constructType(ArrayList.class);

        assertTrue(javaTypeList.isCollectionLikeType());
    }

    @org.junit.Test
    public void testJavaTypeIsThrowable() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaTypeException = typeFactory.constructType(Exception.class);

        assertTrue(javaTypeException.isThrowable());
    }

    @org.junit.Test
    public void testJavaTypeIsAbstract() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaTypeAbstractClass = typeFactory.constructType(AbstractClass.class);

        assertTrue(javaTypeAbstractClass.isAbstract());
    }

    @org.junit.Test
    public void testJavaTypeIsFinal() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaTypeFinalClass = typeFactory.constructType(FinalClass.class);

        assertTrue(javaTypeFinalClass.isFinal());
    }

    @org.junit.Test
    public void testJavaTypeGenericSignature() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaTypeListOfString = typeFactory.constructType(new TypeReference<List<String>>(){}.getType());

        StringBuilder stringBuilder = new StringBuilder();
        javaTypeListOfString.getGenericSignature(stringBuilder);

        assertEquals("java.util.List<java.lang.String>", stringBuilder.toString());
    }

    @org.junit.Test
    public void testJavaTypeErasedSignature() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaTypeListOfString = typeFactory.constructType(new TypeReference<List<String>>(){}.getType());

        StringBuilder stringBuilder = new StringBuilder();
        javaTypeListOfString.getErasedSignature(stringBuilder);

        assertEquals("java.util.List", stringBuilder.toString());
    }
}