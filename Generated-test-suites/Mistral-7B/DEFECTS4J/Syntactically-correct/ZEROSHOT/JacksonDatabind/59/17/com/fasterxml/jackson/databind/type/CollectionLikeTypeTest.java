package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testConstructCollectionLikeType() {
        Class<List<Integer>> listType = List.class.getTypeParameters()[0].getType();
        JavaType javaType = CollectionLikeType.construct(listType, TypeBindings.emptyBindings(), null, null, JavaType.construct(int.class));
        CollectionLikeType actual = (CollectionLikeType) javaType;
        JavaType expectedElementType = JavaType.construct(int.class);
        assertEquals(JavaType.construct(List.class), actual.getRawType());
        assertEquals(TypeBindings.emptyBindings(), actual.getBindings());
        assertEquals(JavaType.construct(List.class), actual.getSuperClass()) ;
        assertEquals(new JavaType[0], actual.getSuperInterfaces()) ;
        assertEquals(expectedElementType.hashCode(), actual.getElementType().hashCode()) ;
    }

    @Test
    public void testConstructCollectionLikeTypeWithTypeBindings() {
        Class<List<String>> listType = List.class.getTypeParameters()[0].getType();
        JavaType javaType = CollectionLikeType.construct(listType, TypeBindings.defaultBindings(), null, null, JavaType.construct(String.class));
        CollectionLikeType actual = (CollectionLikeType) javaType;
        JavaType expectedElementType = JavaType.construct(String.class);
        assertEquals(JavaType.construct(List.class), actual.getRawType()) ;
        assertEquals(TypeBindings.defaultBindings(), actual.getBindings()) ;
        assertEquals(JavaType.construct(List.class), actual.getSuperClass()) ;
        assertEquals(new JavaType[0], actual.getSuperInterfaces()) ;
        assertEquals(expectedElementType.hashCode(), actual.getElementType().hashCode()) ;
    }

    @Test
    public void testUpgradeFromJavaType() {
        JavaType javaType = JavaType.construct(ArrayList.class);
        JavaType expectedElementType = JavaType.construct(int.class);
        JavaType actual = CollectionLikeType.upgradeFrom(javaType, expectedElementType);
        assertEquals(JavaType.construct(ArrayList.class), actual.getRawType()) ;
        assertEquals(TypeBindings.emptyBindings(), actual.getBindings()) ;
        assertEquals(JavaType.construct(ArrayList.class), actual.getSuperClass()) ;
        assertEquals(new JavaType[0], actual.getSuperInterfaces()) ;
        assertEquals(expectedElementType.hashCode(), actual.getElementType().hashCode()) ;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNonTypeBaseJavaType() {
        JavaType javaType = JavaType.construct(String.class);
        JavaType elementType = JavaType.construct(int.class);
        CollectionLikeType.upgradeFrom(javaType, elementType);
    }

    @Test
    public void testRefine() {
        Class<List<String>> listType = List.class.getTypeParameters()[0].getType();
        JavaType javaType = CollectionLikeType.construct(listType, TypeBindings.emptyBindings(), null, null, JavaType.construct(String.class));
        JavaType refinedType = javaType.refine(List.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(JavaType.construct(List.class), refinedType.getRawType()) ;
        assertEquals(TypeBindings.emptyBindings(), refinedType.getBindings()) ;
        assertEquals(JavaType.construct(List.class), refinedType.getSuperClass()) ;
        assertEquals(new JavaType[0], refinedType.getSuperInterfaces()) ;
        assertEquals(JavaType.construct(String.class), refinedType.getElementType()) ;
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType javaType = JavaType.construct(ArrayList.class);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType.construct();
        assertTrue(collectionLikeType.isTrueCollectionType()) ;

        javaType = JavaType.construct(HashMap.class);
        collectionLikeType = (CollectionLikeType) javaType.construct();
        assertFalse(collectionLikeType.isTrueCollectionType()) ;
    }
}