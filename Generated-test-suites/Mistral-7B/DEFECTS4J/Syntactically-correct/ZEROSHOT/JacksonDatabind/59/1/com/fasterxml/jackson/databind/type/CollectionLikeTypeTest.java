package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Test for {@link CollectionLikeType} class
 */
public class CollectionLikeTypeTest {

    @Test
    public void testCollectionLikeTypeConstruction() {
        // test default constructor
        CollectionLikeType collectionLikeType1 = new CollectionLikeType(null, null, null, null, null, null, null, false);
        assertNotNull(collectionLikeType1);

        // test constructor with TypeBindings and elementType
        JavaType javaListType = TypeFactory.defaultInstance().constructType(java.util.List.class, new TypeVariable<String>() {});
        CollectionLikeType collectionLikeType2 = new CollectionLikeType(java.util.List.class, TypeBindings.emptyBindings(), null, null, javaListType, null, null, false);
        assertEquals(java.util.List.class, collectionLikeType2.getRawClass());
        assertEquals(javaListType, collectionLikeType2.getElementType());

        // test constructor with TypeBase and elementType
        TypeBase typeBase = new TypeBase(java.util.List.class, null, null, 0, null, null, false);
        CollectionLikeType collectionLikeType3 = new CollectionLikeType(typeBase, javaListType);
        assertEquals(java.util.List.class, collectionLikeType3.getRawClass());
        assertEquals(javaListType, collectionLikeType3.getElementType());

        // test constructor with JavaType and elementType
        JavaType javaType = TypeFactory.defaultInstance().constructType(java.util.ArrayList.class, new TypeVariable<String>() {});
        CollectionLikeType collectionLikeType4 = new CollectionLikeType(javaType, javaListType);
        assertEquals(java.util.ArrayList.class, collectionLikeType4.getRawClass());
        assertEquals(javaListType, collectionLikeType4.getElementType());

        // test constructor from JavaType
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        CollectionLikeType collectionLikeType5 = CollectionLikeType.construct(javaType, javaListType);
        assertEquals(java.util.ArrayList.class, collectionLikeType5.getRawClass());
        assertEquals(javaListType, collectionLikeType5.getElementType());
    }

    @Test
    public void testCollectionLikeTypeMethods() {
        JavaType javaListType = TypeFactory.defaultInstance().constructType(java.util.List.class, new TypeVariable<String>() {});
        CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.List.class, TypeBindings.emptyBindings(), null, null, javaListType, null, null, false);

        assertEquals(java.util.List.class, collectionLikeType.getRawClass());
        assertEquals(javaListType, collectionLikeType.getElementType());
        assertTrue(collectionLikeType.isContainerType());
        assertTrue(collectionLikeType.isCollectionLikeType());
        assertFalse(collectionLikeType.isTrueCollectionType());

        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        CollectionLikeType intCollectionLikeType = new CollectionLikeType(intType, javaListType);
        assertEquals(intType, intCollectionLikeType.getRawClass());
        assertEquals(javaListType, intCollectionLikeType.getElementType());
        assertFalse(intCollectionLikeType.isTrueCollectionType());

        JavaType listIntType = TypeFactory.defaultInstance().constructType(java.util.List.class, new TypeVariable<Integer>() {});
        CollectionLikeType listIntCollectionLikeType = new CollectionLikeType(listIntType, null, null, null, javaListType, null, null, false);
        assertTrue(listIntCollectionLikeType.isTrueCollectionType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCollectionLikeTypeConstruction() {
        TypeBase typeBase = new TypeBase(java.io.File.class, null, null, 0, null, null, false);
        CollectionLikeType collectionLikeType = new CollectionLikeType(typeBase, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(intType, null);
        JavaType strType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionLikeType collectionLikeTypeUpgraded = CollectionLikeType.upgradeFrom(strType, intType);
    }

    @Test
    public void testUpgradeFromValidBaseType() {
        JavaType javaListType = TypeFactory.defaultInstance().constructType(java.util.List.class, new TypeVariable<String>() {});
        CollectionLikeType collectionLikeTypeBase = new CollectionLikeType(java.util.ArrayList.class, null, null, null, javaListType, null, null, false);
        JavaType javaArrayType = TypeFactory.defaultInstance().constructType(String[].class);
        CollectionLikeType collectionLikeTypeUpgraded = CollectionLikeType.upgradeFrom(javaArrayType, javaListType);
        assertEquals(java.util.List.class, collectionLikeTypeUpgraded.getRawClass());
        assertEquals(javaListType, collectionLikeTypeUpgraded.getElementType());
    }

    @Test
    public void testToString() {
        JavaType javaListType = TypeFactory.defaultInstance().constructType(java.util.List.class, new TypeVariable<String>() {});
        CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, javaListType, null, null, false);
        String toString = collectionLikeType.toString();
        assertTrue(toString.contains("collection-like type"));
        assertTrue(toString.contains("java.util.ArrayList"));
        assertTrue(toString.contains("Contains " + javaListType.toCanonical()));
    }

    @Test
    public void testIsContainerType() {
        JavaType javaListType = TypeFactory.defaultInstance().constructType(java.util.List.class, new TypeVariable<String>() {});
        CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, javaListType, null, null, false);
        assertTrue(collectionLikeType.isContainerType());

        JavaType strType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionLikeType strCollectionLikeType = new CollectionLikeType(strType, null);
        assertFalse(strCollectionLikeType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType javaListType = TypeFactory.defaultInstance().constructType(java.util.List.class, new TypeVariable<String>() {});
        CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, javaListType, null, null, false);
        assertTrue(collectionLikeType.isCollectionLikeType());

        JavaType strType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionLikeType strCollectionLikeType = new CollectionLikeType(strType, null);
        assertTrue(strCollectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType javaListType = TypeFactory.defaultInstance().constructType(java.util.List.class, new TypeVariable<String>() {});
        CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, javaListType, null, null, false);
        assertFalse(collectionLikeType.isTrueCollectionType());

        JavaType javaUtilListType = TypeFactory.defaultInstance().constructType(java.util.List.class);
        CollectionLikeType javaUtilCollectionLikeType = new CollectionLikeType(javaUtilListType, null, null, null, javaListType, null, null, false);
        assertTrue(javaUtilCollectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaListType = TypeFactory.defaultInstance().constructType(java.util.List.class, new TypeVariable<String>() {});
        CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, javaListType, null, null, false);
        assertEquals(javaListType, collectionLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType javaListType = TypeFactory.defaultInstance().constructType(java.util.List.class, new TypeVariable<String>() {});
        CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, javaListType, null, null, false);
        assertNull(collectionLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType javaListType = TypeFactory.defaultInstance().constructType(java.util.List.class, new TypeVariable<String>() {});
        CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, javaListType, null, null, false);
        assertNull(collectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType javaListType = TypeFactory.defaultInstance().constructType(java.util.List.class, new TypeVariable<String>() {});
        CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, javaListType, null, null, false);
        assertFalse(collectionLikeType.hasHandlers());

        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        CollectionLikeType intCollectionLikeType = new CollectionLikeType(intType, javaListType);
        assertFalse(intCollectionLikeType.hasHandlers());

        JavaType javaListStringType = TypeFactory.defaultInstance().constructType(java.util.List.class, new TypeVariable<String>() {});
        TypeDeserializer valueHandler = new TypeDeserializer();
        CollectionLikeType javaListStringCollectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, javaListStringType, valueHandler, null, false);
        assertTrue(javaListStringCollectionLikeType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaListType = TypeFactory.defaultInstance().constructType(java.util.List.class, new TypeVariable<String>() {});
        CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, javaListType, null, null, false);
        StringBuilder sb = new StringBuilder();
        collectionLikeType.getErasedSignature(sb);
        String erasedSignature = sb.toString();
        assertTrue(erasedSignature.contains("java.util.ArrayList"));
        assertTrue(erasedSignature.contains("java.util.List"));
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaListType = TypeFactory.defaultInstance().constructType(java.util.List.class, new TypeVariable<String>() {});
        CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, javaListType, null, null, false);
        StringBuilder sb = new StringBuilder();
        collectionLikeType.getGenericSignature(sb);
        String genericSignature = sb.toString();
        assertTrue(genericSignature.contains("java.util.List<T>"));
        assertTrue(genericSignature.contains("java.util.ArrayList<T>"));
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType javaListType = TypeFactory.defaultInstance().constructType(java.util.List.class, new TypeVariable<String>() {});
        CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, javaListType, null, null, false);
        String canonicalName = collectionLikeType.buildCanonicalName();
        assertTrue(canonicalName.contains("java.util.ArrayList"));
        assertTrue(canonicalName.contains("java.util.List"));
    }
}