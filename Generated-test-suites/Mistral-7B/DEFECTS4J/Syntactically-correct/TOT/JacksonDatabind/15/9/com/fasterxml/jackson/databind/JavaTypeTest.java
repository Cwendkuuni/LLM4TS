package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class JavaTypeTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testJavaTypeCreation() {
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        assertEquals(String.class, type.getRawClass());
        assertFalse(type.isAbstract());
        assertFalse(type.isInterface());
        assertFalse(type.isPrimitive());
        assertFalse(type.isFinal());
        assertTrue(type.isConcrete());
        assertFalse(type.isEnumType());
        assertFalse(type.isContainerType());
        assertFalse(type.isCollectionLikeType());
        assertFalse(type.isMapLikeType());
        assertTrue(type.hasRawClass(String.class));
        assertTrue(type.isThrowable());
        assertFalse(type.isArrayType());
        assertFalse(type.isFinal());
        assertFalse(type.useStaticType());
        assertFalse(type.hasGenericTypes());
        assertNull(type.getKeyType());
        assertNull(type.getContentType());
        assertEquals(0, type.containedTypeCount());
        assertEquals(null, type.containedType(0));
        assertEquals(null, type.containedTypeName(0));
        assertEquals(null, type.getParameterSource());

        JavaType integerType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType substringType = TypeFactory.defaultInstance().constructType(String.class).narrowBy(String.class.getSuperclass());
        JavaType arrayType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType collectionType = TypeFactory.defaultInstance().constructType(java.util.ArrayList.class);
        JavaType mapType = TypeFactory.defaultInstance().constructType(java.util.HashMap.class);
        JavaType enumType = TypeFactory.defaultInstance().constructType(MyEnum.class);

        assertEquals(int.class, integerType.getRawClass());
        assertEquals(int[].class, arrayType.getRawClass());
        assertFalse(arrayType.isInterface());
        assertTrue(arrayType.isArrayType());
        assertTrue(arrayType.isConcrete());
        assertFalse(arrayType.isEnumType());
        assertFalse(arrayType.isContainerType());
        assertFalse(arrayType.isCollectionLikeType());
        assertFalse(arrayType.isMapLikeType());
        assertTrue(arrayType.hasRawClass(int[].class));
        assertTrue(arrayType.isThrowable());
        assertFalse(arrayType.isArrayType());
        assertFalse(arrayType.isFinal());
        assertFalse(arrayType.useStaticType());
        assertFalse(arrayType.hasGenericTypes());
        assertNull(arrayType.getKeyType());
        assertNull(arrayType.getContentType());
        assertEquals(0, arrayType.containedTypeCount());
        assertEquals(null, arrayType.containedType(0));
        assertEquals(null, arrayType.containedTypeName(0));
        assertEquals(null, arrayType.getParameterSource());

        assertEquals(java.util.ArrayList.class, collectionType.getRawClass());
        assertTrue(collectionType.isInterface());
        assertFalse(collectionType.isPrimitive());
        assertFalse(collectionType.isFinal());
        assertTrue(collectionType.isConcrete());
        assertTrue(collectionType.isContainerType());
        assertTrue(collectionType.isCollectionLikeType());
        assertFalse(collectionType.isMapLikeType());
        assertTrue(collectionType.hasRawClass(java.util.ArrayList.class));
        assertTrue(collectionType.isThrowable());
        assertFalse(collectionType.isArrayType());
        assertFalse(collectionType.isFinal());
        assertFalse(collectionType.useStaticType());
        assertTrue(collectionType.hasGenericTypes());
        assertEquals(java.lang.reflect.Type.class, collectionType.getParameterSource());
        assertEquals(1, collectionType.containedTypeCount());
        assertEquals(java.lang.Class.class, collectionType.containedType(0).getRawClass());
        assertEquals("java.lang.Class<T>", collectionType.containedTypeName(0));

        assertEquals(java.util.HashMap.class, mapType.getRawClass());
        assertTrue(mapType.isInterface());
        assertFalse(mapType.isPrimitive());
        assertFalse(mapType.isFinal());
        assertTrue(mapType.isConcrete());
        assertTrue(mapType.isContainerType());
        assertTrue(mapType.isMapLikeType());
        assertFalse(mapType.isCollectionLikeType());
        assertTrue(mapType.hasRawClass(java.util.HashMap.class));
        assertTrue(mapType.isThrowable());
        assertFalse(mapType.isArrayType());
        assertFalse(mapType.isFinal());
        assertFalse(mapType.useStaticType());
        assertTrue(mapType.hasGenericTypes());
        assertEquals(java.lang.Map.Entry.class, mapType.getParameterSource());
        assertEquals(2, mapType.containedTypeCount());
        assertEquals(java.lang.Class.class, mapType.containedType(0).getRawClass());
        assertEquals("K", mapType.containedTypeName(0));
        assertEquals(java.lang.Class.class, mapType.containedType(1).getRawClass());
        assertEquals("V", mapType.containedTypeName(1));

        assertEquals(MyEnum.class, enumType.getRawClass());
        assertTrue(enumType.isInterface());
        assertFalse(enumType.isPrimitive());
        assertFalse(enumType.isFinal());
        assertTrue(enumType.isConcrete());
        assertTrue(enumType.isEnumType());
        assertFalse(enumType.isContainerType());
        assertFalse(enumType.isCollectionLikeType());
        assertFalse(enumType.isMapLikeType());
        assertTrue(enumType.hasRawClass(MyEnum.class));
        assertTrue(enumType.isThrowable());
        assertFalse(enumType.isArrayType());
        assertFalse(enumType.isFinal());
        assertFalse(enumType.useStaticType());
        assertFalse(enumType.hasGenericTypes());
        assertEquals(null, enumType.getParameterSource());
        assertEquals(0, enumType.containedTypeCount());
        assertEquals(null, enumType.containedType(0));
        assertEquals(null, enumType.containedTypeName(0));
    }

    @Test
    public void testJavaTypeNarrowing() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType stringSubclassType = TypeFactory.defaultInstance().constructType(StringBuilder.class);

        JavaType narrowedType = stringType.narrowBy(StringBuilder.class);
        JavaType forcedNarrowedType = stringType.forcedNarrowBy(StringBuilder.class);

        assertEquals(StringBuilder.class, narrowedType.getRawClass());
        assertEquals(StringBuilder.class, forcedNarrowedType.getRawClass());
        assertTrue(narrowedType.isAssignableFrom(stringType));
        assertTrue(narrowedType.isAssignableFrom(forcedNarrowedType));
        assertTrue(stringType.isAssignableFrom(narrowedType));
        assertTrue(stringType.isAssignableFrom(forcedNarrowedType));
        assertFalse(narrowedType.equals(stringType));
        assertFalse(narrowedType.equals(forcedNarrowedType));
        assertTrue(narrowedType.hashCode() != stringType.hashCode());
        assertTrue(narrowedType.hashCode() != forcedNarrowedType.hashCode());
        assertEquals(narrowedType, narrowedType.withValueHandler(null));
        assertEquals(narrowedType, narrowedType.withTypeHandler(null));
        assertEquals(narrowedType, narrowedType.withContentTypeHandler(null));
        assertEquals(narrowedType, narrowedType.withContentValueHandler(null));

        JavaType wideType = stringType.widenBy(StringBuilder.class);
        JavaType forcedWideType = stringType.forcedNarrowBy(StringBuilder.class);

        assertEquals(String.class, wideType.getRawClass());
        assertEquals(String.class, forcedWideType.getRawClass());
        assertTrue(wideType.isAssignableFrom(stringType));
        assertTrue(wideType.isAssignableFrom(forcedWideType));
        assertTrue(stringType.isAssignableFrom(wideType));
        assertTrue(stringType.isAssignableFrom(forcedWideType));
        assertFalse(wideType.equals(stringType));
        assertFalse(wideType.equals(forcedWideType));
        assertTrue(wideType.hashCode() != stringType.hashCode());
        assertTrue(wideType.hashCode() != forcedWideType.hashCode());
        assertEquals(wideType, wideType.withValueHandler(null));
        assertEquals(wideType, wideType.withTypeHandler(null));
        assertEquals(wideType, wideType.withContentTypeHandler(null));
        assertEquals(wideType, wideType.withContentValueHandler(null));
    }

    @Test
    public void testJavaTypeWidening() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType stringSubclassType = TypeFactory.defaultInstance().constructType(StringBuilder.class);

        JavaType widenedType = stringSubclassType.widenBy(String.class);
        JavaType forcedWidenedType = stringSubclassType.forcedNarrowBy(String.class);

        assertEquals(String.class, widenedType.getRawClass());
        assertEquals(String.class, forcedWidenedType.getRawClass());
        assertTrue(widenedType.isAssignableFrom(stringSubclassType));
        assertTrue(widenedType.isAssignableFrom(forcedWidenedType));
        assertTrue(stringSubclassType.isAssignableFrom(widenedType));
        assertTrue(stringSubclassType.isAssignableFrom(forcedWidenedType));
        assertFalse(widenedType.equals(stringSubclassType));
        assertFalse(widenedType.equals(forcedWidenedType));
        assertTrue(widenedType.hashCode() != stringSubclassType.hashCode());
        assertTrue(widenedType.hashCode() != forcedWidenedType.hashCode());
        assertEquals(widenedType, widenedType.withValueHandler(null));
        assertEquals(widenedType, widenedType.withTypeHandler(null));
        assertEquals(widenedType, widenedType.withContentTypeHandler(null));
        assertEquals(widenedType, widenedType.withContentValueHandler(null));
    }

    @Test
    public void testJavaTypeContainedType() {
        JavaType arrayListType = TypeFactory.defaultInstance().constructType(java.util.ArrayList.class);
        JavaType listType = arrayListType.narrowContentsBy(List.class);

        assertEquals(List.class, listType.getRawClass());
        assertEquals(List.class, listType.getRawClass());
        assertTrue(listType.isAssignableFrom(arrayListType));
        assertTrue(listType.isAssignableFrom(arrayListType));
        assertTrue(arrayListType.isAssignableFrom(listType));
        assertTrue(arrayListType.isAssignableFrom(listType));
        assertFalse(listType.equals(arrayListType));
        assertFalse(listType.equals(arrayListType));
        assertTrue(listType.hashCode() != arrayListType.hashCode());
        assertTrue(listType.hashCode() != arrayListType.hashCode());
        assertEquals(listType, listType.withValueHandler(null));
        assertEquals(listType, listType.withTypeHandler(null));
        assertEquals(listType, listType.withContentTypeHandler(null));
        assertEquals(listType, listType.withContentValueHandler(null));

        assertEquals(1, listType.containedTypeCount());
        assertEquals(java.lang.Class.class, listType.containedType(0).getRawClass());
        assertEquals("java.lang.Class<E>", listType.containedTypeName(0));
    }

    @Test
    public void testJavaTypeWidenedContainedType() {
        JavaType arrayListType = TypeFactory.defaultInstance().constructType(java.util.ArrayList.class);
        JavaType listType = arrayListType.narrowContentsBy(List.class);
        JavaType listWithRawTypeType = listType.widenContentsBy(java.util.List.class);

        assertEquals(List.class, listType.getRawClass());
        assertEquals(java.util.List.class, listWithRawTypeType.getRawClass());
        assertTrue(listType.isAssignableFrom(listWithRawTypeType));
        assertTrue(listWithRawTypeType.isAssignableFrom(listType));
        assertTrue(listType.isAssignableFrom(listWithRawTypeType));
        assertTrue(listWithRawTypeType.isAssignableFrom(listType));
        assertFalse(listType.equals(listWithRawTypeType));
        assertFalse(listWithRawTypeType.equals(listType));
        assertTrue(listType.hashCode() != listWithRawTypeType.hashCode());
        assertTrue(listType.hashCode() != listWithRawTypeType.hashCode());
        assertEquals(listType, listType.withValueHandler(null));
        assertEquals(listType, listType.withTypeHandler(null));
        assertEquals(listType, listType.withContentTypeHandler(null));
        assertEquals(listType, listType.withContentValueHandler(null));

        assertEquals(1, listWithRawTypeType.containedTypeCount());
        assertEquals(java.lang.Class.class, listWithRawTypeType.containedType(0).getRawClass());
        assertEquals("java.lang.Class<E>", listWithRawTypeType.containedTypeName(0));
    }
}