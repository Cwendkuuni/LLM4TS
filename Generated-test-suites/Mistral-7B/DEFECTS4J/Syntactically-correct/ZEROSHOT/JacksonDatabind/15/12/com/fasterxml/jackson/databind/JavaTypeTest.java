package com.example.junit;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class JavaTypeTest {

    private TypeFactory typeFactory = new TypeFactory();

    @Test
    public void testJavaTypeConstruction() {
        Class<?> rawType = String[].class;
        JavaType javaType = typeFactory.constructType(rawType);

        assertEquals(rawType, javaType.getRawClass());
        assertEquals(String[].class.getName(), javaType.getErasedSignature());
        assertEquals(String[].class.getName(), javaType.getGenericSignature());
        assertFalse(javaType.isAbstract());
        assertFalse(javaType.isArrayType());
        assertFalse(javaType.isEnumType());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isFinal());
        assertFalse(javaType.isContainerType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.useStaticType());
        assertNull(javaType.getValueHandler());
        assertNull(javaType.getTypeHandler());
        assertEquals(0, javaType.containedTypeCount());
    }

    @Test
    public void testJavaTypeNarrowing() {
        Class<?> arrayType = String[].class;
        JavaType arrayJavaType = typeFactory.constructType(arrayType);

        JavaType stringJavaType = arrayJavaType.narrowBy(String.class);

        assertSame(stringJavaType.getRawClass(), String.class);
        assertEquals(String.class.getName(), stringJavaType.getErasedSignature());
        assertEquals(String.class.getName(), stringJavaType.getGenericSignature());
        assertFalse(stringJavaType.isAbstract());
        assertFalse(stringJavaType.isArrayType());
        assertFalse(stringJavaType.isEnumType());
        assertFalse(stringJavaType.isInterface());
        assertFalse(stringJavaType.isPrimitive());
        assertFalse(stringJavaType.isFinal());
        assertFalse(stringJavaType.isContainerType());
        assertFalse(stringJavaType.isCollectionLikeType());
        assertFalse(stringJavaType.isMapLikeType());
        assertFalse(stringJavaType.useStaticType());
        assertNull(stringJavaType.getValueHandler());
        assertNull(stringJavaType.getTypeHandler());
        assertEquals(0, stringJavaType.containedTypeCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowingInvalid() {
        Class<?> arrayType = String[].class;
        JavaType arrayJavaType = typeFactory.constructType(arrayType);

        JavaType integerJavaType = arrayJavaType.narrowBy(Integer.class);
    }

    @Test
    public void testJavaTypeWidening() {
        Class<?> stringType = String.class;
        JavaType stringJavaType = typeFactory.constructType(stringType);

        JavaType arrayJavaType = stringJavaType.widenBy(String[].class);

        assertEquals(arrayJavaType.getRawClass(), String[].class);
        assertEquals(String[].class.getName(), arrayJavaType.getErasedSignature());
        assertEquals(String[].class.getName(), arrayJavaType.getGenericSignature());
        assertFalse(arrayJavaType.isAbstract());
        assertFalse(arrayJavaType.isArrayType());
        assertFalse(arrayJavaType.isEnumType());
        assertFalse(arrayJavaType.isInterface());
        assertFalse(arrayJavaType.isPrimitive());
        assertFalse(arrayJavaType.isFinal());
        assertFalse(arrayJavaType.isContainerType());
        assertFalse(arrayJavaType.isCollectionLikeType());
        assertFalse(arrayJavaType.isMapLikeType());
        assertFalse(arrayJavaType.useStaticType());
        assertNull(arrayJavaType.getValueHandler());
        assertNull(arrayJavaType.getTypeHandler());
        assertEquals(0, arrayJavaType.containedTypeCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWideningInvalid() {
        Class<?> stringType = String.class;
        JavaType stringJavaType = typeFactory.constructType(stringType);

        JavaType intType = typeFactory.constructType(int.class);
        JavaType intArrayJavaType = intType.withArrayType(1);

        JavaType stringJavaTypeWidened = stringJavaType.widenBy(intArrayJavaType.getRawClass());
    }

    @Test
    public void testJavaTypeCopyMethods() {
        Class<?> arrayType = String[].class;
        JavaType arrayJavaType = typeFactory.constructType(arrayType);

        JavaType valueHandlerSetJavaType = arrayJavaType.withValueHandler(new Object());

        assertEquals(arrayJavaType.getRawClass(), valueHandlerSetJavaType.getRawClass());
        assertFalse(arrayJavaType.getValueHandler() == valueHandlerSetJavaType.getValueHandler());
        assertNull(arrayJavaType.getTypeHandler());
        assertFalse(arrayJavaType.useStaticType());
        assertEquals(0, arrayJavaType.containedTypeCount());

        JavaType typeHandlerSetJavaType = arrayJavaType.withTypeHandler(new Object());

        assertEquals(arrayJavaType.getRawClass(), typeHandlerSetJavaType.getRawClass());
        assertNull(arrayJavaType.getTypeHandler() == typeHandlerSetJavaType.getTypeHandler());
        assertNull(arrayJavaType.getValueHandler());
        assertFalse(arrayJavaType.useStaticType());
        assertEquals(0, arrayJavaType.containedTypeCount());

        JavaType staticTypingJavaType = arrayJavaType.withStaticTyping();

        assertEquals(arrayJavaType.getRawClass(), staticTypingJavaType.getRawClass());
        assertEquals(arrayJavaType.getRawClass(), staticTypingJavaType.getRawClass());
        assertNull(arrayJavaType.getTypeHandler());
        assertNull(arrayJavaType.getValueHandler());
        assertFalse(arrayJavaType.useStaticType() == staticTypingJavaType.useStaticType());
        assertEquals(0, arrayJavaType.containedTypeCount());
    }

    @Test
    public void testJavaTypeMapLikeType() {
        Class<?> stringMapType = Map.class.getTypeParameters()[0];
        JavaType keyType = typeFactory.constructType(String.class);
        JavaType valueType = typeFactory.constructType(Integer.class);
        JavaType mapJavaType = typeFactory.constructType(Map.class, keyType, valueType);

        assertEquals(Map.class, mapJavaType.getRawClass());
        assertEquals(Map.class.getName(), mapJavaType.getErasedSignature());
        assertEquals("<" + String.class.getName() + ", " + Integer.class.getName() + ">", mapJavaType.getGenericSignature());
        assertFalse(mapJavaType.isAbstract());
        assertFalse(mapJavaType.isArrayType());
        assertFalse(mapJavaType.isEnumType());
        assertFalse(mapJavaType.isInterface());
        assertFalse(mapJavaType.isPrimitive());
        assertFalse(mapJavaType.isFinal());
        assertTrue(mapJavaType.isContainerType());
        assertTrue(mapJavaType.isMapLikeType());
        assertFalse(mapJavaType.useStaticType());
        assertNull(mapJavaType.getValueHandler());
        assertNull(mapJavaType.getTypeHandler());
        assertEquals(2, mapJavaType.containedTypeCount());
        assertEquals(keyType, mapJavaType.containedType(0));
        assertEquals(valueType, mapJavaType.containedType(1));
    }

    @Test
    public void testJavaTypeCollectionLikeType() {
        Class<?> stringCollectionType = Collection.class.getTypeParameters()[0];
        JavaType stringJavaType = typeFactory.constructType(String.class);
        JavaType collectionJavaType = typeFactory.constructType(Collection.class, stringJavaType);

        assertEquals(Collection.class, collectionJavaType.getRawClass());
        assertEquals(Collection.class.getName(), collectionJavaType.getErasedSignature());
        assertEquals("<" + String.class.getName() + ">", collectionJavaType.getGenericSignature());
        assertFalse(collectionJavaType.isAbstract());
        assertFalse(collectionJavaType.isArrayType());
        assertFalse(collectionJavaType.isEnumType());
        assertFalse(collectionJavaType.isInterface());
        assertFalse(collectionJavaType.isPrimitive());
        assertFalse(collectionJavaType.isFinal());
        assertTrue(collectionJavaType.isContainerType());
        assertTrue(collectionJavaType.isCollectionLikeType());
        assertFalse(collectionJavaType.useStaticType());
        assertNull(collectionJavaType.getValueHandler());
        assertNull(collectionJavaType.getTypeHandler());
        assertEquals(1, collectionJavaType.containedTypeCount());
        assertEquals(stringJavaType, collectionJavaType.containedType(0));
    }
}