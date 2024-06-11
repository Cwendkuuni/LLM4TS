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
        Class<Integer> intType = Integer.TYPE;
        JavaType javaType = TypeFactory.defaultInstance().constructType(intType);

        assertEquals(intType, javaType.getRawClass());
        assertTrue(javaType.isConcrete());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isAbstract());
        assertFalse(javaType.isEnumType());
        assertFalse(javaType.isThrowable());
        assertFalse(javaType.isArrayType());
        assertFalse(javaType.isContainerType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.useStaticType());
        assertNull(javaType.getValueHandler());
        assertNull(javaType.getTypeHandler());
        assertNull(javaType.getKeyType());
        assertNull(javaType.getContentType());
        assertEquals(0, javaType.containedTypeCount());

        assertEquals("int", javaType.getErasedSignature());
        assertEquals("java.lang.Integer", javaType.getGenericSignature());
    }

    @Test
    public void testJavaTypeWithValueHandler() {
        Class<Integer> intType = Integer.TYPE;
        JavaType javaType = TypeFactory.defaultInstance().constructType(intType);
        Object valueHandler = new Object() { };

        JavaType javaTypeWithValueHandler = javaType.withValueHandler(valueHandler);

        assertNotNull(javaTypeWithValueHandler.getValueHandler());
        assertEquals(javaType, javaTypeWithValueHandler);
    }

    @Test
    public void testJavaTypeNarrowBy() {
        Class<Integer> intType = Integer.TYPE;
        Class<Long> longType = Long.TYPE;

        JavaType javaTypeInt = TypeFactory.defaultInstance().constructType(intType);
        JavaType javaTypeLong = TypeFactory.defaultInstance().constructType(longType);

        JavaType javaTypeIntNarrowed = javaTypeInt.narrowBy(longType);

        assertEquals(longType, javaTypeIntNarrowed.getRawClass());
        assertEquals(javaTypeLong, javaTypeIntNarrowed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowByInvalid() {
        Class<Integer> intType = Integer.TYPE;
        Class<String> stringType = String.class;

        JavaType javaTypeInt = TypeFactory.defaultInstance().constructType(intType);
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(stringType);

        JavaType javaTypeIntNarrowed = javaTypeInt.narrowBy(stringType);
    }

    @Test
    public void testJavaTypeWidenBy() {
        Class<Integer> intType = Integer.TYPE;
        Class<Long> longType = Long.TYPE;

        JavaType javaTypeInt = TypeFactory.defaultInstance().constructType(intType);
        JavaType javaTypeLong = TypeFactory.defaultInstance().constructType(longType);

        JavaType javaTypeLongWiden = javaTypeLong.widenBy(intType);

        assertEquals(intType, javaTypeLongWiden.getRawClass());
        assertEquals(javaTypeInt, javaTypeLongWiden);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWidenByInvalid() {
        Class<Integer> intType = Integer.TYPE;
        Class<String> stringType = String.class;

        JavaType javaTypeInt = TypeFactory.defaultInstance().constructType(intType);
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(stringType);

        JavaType javaTypeIntWiden = javaTypeInt.widenBy(stringType);
    }

    @Test
    public void testJavaTypeNarrowContentsBy() {
        Class<List<Integer>> listIntType = new ArrayList<Integer>().getClass();
        Class<List<Long>> listLongType = new ArrayList<Long>().getClass();

        JavaType javaTypeListInt = TypeFactory.defaultInstance().constructType(listIntType);
        JavaType javaTypeListLong = TypeFactory.defaultInstance().constructType(listLongType);

        JavaType javaTypeListLongNarrowed = javaTypeListInt.narrowContentsBy(listLongType);

        assertEquals(listLongType, javaTypeListLongNarrowed.getRawClass());
        assertEquals(javaTypeListLong, javaTypeListLongNarrowed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowContentsByInvalid() {
        Class<List<Integer>> listIntType = new ArrayList<Integer>().getClass();
        Class<Map<String, String>> mapStringStringType = HashMap.class; //$NON-NLS-1$

        JavaType javaTypeListInt = TypeFactory.defaultInstance().constructType(listIntType);
        JavaType javaTypeMapStringString = TypeFactory.defaultInstance().constructType(mapStringStringType);

        JavaType javaTypeListIntNarrowed = javaTypeListInt.narrowContentsBy(mapStringStringType);
    }

    @Test
    public void testJavaTypeWidenContentsBy() {
        Class<List<Integer>> listIntType = new ArrayList<Integer>().getClass();
        Class<List<Object>> listObjectType = List.class; //$NON-NLS-1$

        JavaType javaTypeListInt = TypeFactory.defaultInstance().constructType(listIntType);
        JavaType javaTypeListObject = TypeFactory.defaultInstance().constructType(listObjectType);

        JavaType javaTypeListObjectWiden = javaTypeListInt.widenContentsBy(listObjectType);

        assertEquals(listObjectType, javaTypeListObjectWiden.getRawClass());
        assertEquals(javaTypeListObject, javaTypeListObjectWiden);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWidenContentsByInvalid() {
        Class<List<Integer>> listIntType = new ArrayList<Integer>().getClass();
        Class<Map<String, String>> mapStringStringType = HashMap.class; //$NON-NLS-1$

        JavaType javaTypeListInt = TypeFactory.defaultInstance().constructType(listIntType);
        JavaType javaTypeMapStringString = TypeFactory.defaultInstance().constructType(mapStringStringType);

        JavaType javaTypeListIntWiden = javaTypeListInt.widenContentsBy(mapStringStringType);
    }
}