package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.core.type.ResolvedType;

import org.junit.Test;
import static org.junit.Assert.*;

public class JavaTypeTest {

    @Test
    public void testJavaTypeConstruction() {
        Class<String> rawStringClass = String.class;
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(rawStringClass);

        assertEquals(String.class, javaType.getRawClass());
        assertTrue(javaType instanceof JavaType);
        assertTrue(Modifier.isFinal(javaType.getRawClass().getModifiers()));
        assertFalse(javaType.isAbstract());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isFinal());
        assertFalse(javaType.isThrowable());
        assertFalse(javaType.isArrayType());
        assertFalse(javaType.isEnumType());
        assertFalse(javaType.isContainerType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.useStaticType());

        assertTrue(javaType.hasRawClass(String.class));
        assertFalse(javaType.hasRawClass(Integer.class));
        assertFalse(javaType.hasRawClass(List.class));
        assertFalse(javaType.hasRawClass(Map.class));

        JavaType narrowedType = javaType.narrowBy(String[].class);
        assertEquals(String[].class, narrowedType.getRawClass());
        assertSame(javaType, narrowedType);

        JavaType widenedType = javaType.widenBy(Object.class);
        assertEquals(Object.class, widenedType.getRawClass());
        assertSame(javaType, widenedType);
    }

    @Test
    public void testJavaTypeWithHandlers() {
        Class<String> rawStringClass = String.class;
        Class<StringBuilder> sbClass = StringBuilder.class;
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(rawStringClass);

        JavaType javaTypeWithValueHandler = javaType.withValueHandler(new StringBuilder());
        assertEquals(javaType.getRawClass(), javaTypeWithValueHandler.getRawClass());
        assertNotNull(javaTypeWithValueHandler.getValueHandler());
        assertTrue(javaTypeWithValueHandler.getValueHandler() instanceof StringBuilder);

        JavaType javaTypeWithTypeHandler = javaType.withTypeHandler(new StringBuffer("test"));
        assertEquals(javaType.getRawClass(), javaTypeWithTypeHandler.getRawClass());
        assertNotNull(javaTypeWithTypeHandler.getTypeHandler());
        assertTrue(javaTypeWithTypeHandler.getTypeHandler() instanceof StringBuffer);

        JavaType javaTypeWithStaticTyping = javaType.withStaticTyping();
        assertEquals(javaType.getRawClass(), javaTypeWithStaticTyping.getRawClass());
        assertTrue(javaTypeWithStaticTyping.useStaticType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNarrowing() {
        Class<String> rawStringClass = String.class;
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(rawStringClass);

        JavaType invalidNarrowedType = javaType.narrowBy(List.class);
        fail("Expected IllegalArgumentException, but no exception was thrown.");
    }

    @Test
    public void testNarrowingAndWidening() {
        Class<String> rawStringClass = String.class;
        Class<StringBuilder> sbClass = StringBuilder.class;
        Class<List<String>> listClass = List.class.getTypeParams()[0];
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(rawStringClass);

        JavaType stringArrayType = javaType.narrowBy(String[].class);
        JavaType stringListType = typeFactory.constructType(listClass);
        JavaType stringListWithStaticTyping = stringListType.withStaticTyping();

        JavaType widenByString = stringArrayType.widenBy(String.class);
        assertEquals(String.class, widenByString.getRawClass());
        assertTrue(widenByString.isInstance(new String("test")));

        JavaType widenByList = stringListType.widenBy(List.class);
        assertEquals(List.class, widenByList.getRawClass());
        assertTrue(widenByList.isInstance(new ArrayList<>()));

        JavaType narrowByStringList = stringListType.narrowBy(String[].class);
        assertEquals(String[].class, narrowByStringList.getRawClass());
        assertTrue(narrowByStringList.isInstance(new String[]{}));

        JavaType narrowByStringArray = stringArrayType.narrowBy(StringList.class);
        assertFalse(narrowByStringArray.isInstance(new String[]{}));
        assertFalse(narrowByStringArray.isInstance(new ArrayList<>()));
    }

    @Test
    public void testNarrowingAndWideningWithHandlers() {
        Class<String> rawStringClass = String.class;
        Class<StringBuilder> sbClass = StringBuilder.class;
        Class<List<String>> listClass = List.class.getTypeParams()[0];
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(rawStringClass);

        JavaType stringArrayTypeWithValueHandler = javaType.narrowBy(String[].class).withValueHandler(new StringBuilder());
        JavaType stringArrayTypeWithTypeHandler = javaType.narrowBy(String[].class).withTypeHandler(new StringBuffer("test"));
        JavaType stringArrayTypeWithStaticTyping = javaType.narrowBy(String[].class).withStaticTyping();

        JavaType widenByStringWithValueHandler = stringArrayTypeWithValueHandler.widenBy(String.class).withValueHandler(new StringBuilder());
        assertEquals(String.class, widenByStringWithValueHandler.getRawClass());
        assertEquals(sbClass, widenByStringWithValueHandler.getValueHandler().getClass());

        JavaType widenByStringWithTypeHandler = stringArrayTypeWithTypeHandler.widenBy(String.class).withTypeHandler(new StringBuffer("test"));
        assertEquals(String.class, widenByStringWithTypeHandler.getRawClass());
        assertEquals(sbClass, widenByStringWithTypeHandler.getTypeHandler().getClass());

        JavaType widenByStringWithStaticTyping = stringArrayTypeWithStaticTyping.widenBy(String.class).withStaticTyping();
        assertEquals(String.class, widenByStringWithStaticTyping.getRawClass());
        assertTrue(widenByStringWithStaticTyping.useStaticType());

        JavaType narrowByStringListWithValueHandler = stringListType.narrowBy(String[].class).withValueHandler(new StringBuilder());
        JavaType narrowByStringListWithTypeHandler = stringListType.narrowBy(String[].class).withTypeHandler(new StringBuffer("test"));
        JavaType narrowByStringListWithStaticTyping = stringListType.narrowBy(String[].class).withStaticTyping();

        JavaType widenByListWithValueHandler = narrowByStringListWithValueHandler.widenBy(List.class).withValueHandler(new StringBuilder());
        assertEquals(List.class, widenByListWithValueHandler.getRawClass());
        assertEquals(sbClass, widenByListWithValueHandler.getValueHandler().getClass());

        JavaType widenByListWithTypeHandler = narrowByStringListWithTypeHandler.widenBy(List.class).withTypeHandler(new StringBuffer("test"));
        assertEquals(List.class, widenByListWithTypeHandler.getRawClass());
        assertEquals(sbClass, widenByListWithTypeHandler.getTypeHandler().getClass());

        JavaType widenByListWithStaticTyping = narrowByStringListWithStaticTyping.widenBy(List.class).withStaticTyping();
        assertEquals(List.class, widenByListWithStaticTyping.getRawClass());
        assertTrue(widenByListWithStaticTyping.useStaticType());
    }

    @Test
    public void testContainedTypeAccess() {
        Class<List<String>> listClass = List.class.getTypeParams()[0];
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(listClass);

        JavaType containedType = javaType.narrowContentsBy(String.class);
        assertEquals(String.class, containedType.getRawClass());
        assertTrue(containedType.isInstance(new String("test")));
    }

    @Test
    public void testErasedSignature() {
        Class<List<List<String>>> listOfListOfStringsClass = List.class.getComponentType().getComponentType();
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(listOfListOfStringsClass);

        StringBuilder signatureBuilder = new StringBuilder();
        javaType.getErasedSignature(signatureBuilder);
        assertEquals("java.util.List<java.util.List<java.lang.String>>", signatureBuilder.toString());
    }

    @Test
    public void testGenericSignature() {
        Class<List<List<String>>> listOfListOfStringsClass = List.class.getComponentType().getComponentType();
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(listOfListOfStringsClass);

        StringBuilder signatureBuilder = new StringBuilder();
        javaType.getGenericSignature(signatureBuilder);
        assertEquals("<java.util.List<java.util.List<T>>, T extends java.lang.Object>", signatureBuilder.toString());
    }
}