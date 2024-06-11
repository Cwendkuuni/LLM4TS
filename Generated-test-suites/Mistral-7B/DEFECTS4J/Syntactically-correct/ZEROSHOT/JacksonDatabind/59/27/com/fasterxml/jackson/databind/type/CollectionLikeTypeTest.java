package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

 @Test
 public void testCollectionLikeTypeConstruction() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(java.util.Collection.class, null, null, null, null);
 assertEquals(Collection.class, collectionLikeType.getRawClass());
 assertNull(collectionLikeType.getBindings());
 assertNull(collectionLikeType.getSuperClass());
 assertNull(collectionLikeType.getSuperInterfaces());
 assertNull(collectionLikeType.getElementType());

 JavaType javaType = JavaType.construct(java.util.ArrayList.class);
 collectionLikeType = CollectionLikeType.construct(ArrayList.class, null, null, null, javaType);
 assertEquals(ArrayList.class, collectionLikeType.getRawClass());
 assertEquals(javaType, collectionLikeType.getElementType());

 TypeBindings bindings = TypeBindings.create(String[].class, Integer.TYPE);
 JavaType elementType = JavaType.construct(String.class);
 collectionLikeType = CollectionLikeType.construct(List.class, bindings, null, null, elementType);
 assertEquals(List.class, collectionLikeType.getRawClass());
 assertEquals(bindings, collectionLikeType.getBindings());
 assertEquals(elementType, collectionLikeType.getElementType());
 }

 @Test
 public void testCollectionLikeTypeUpgradeFrom() {
 JavaType javaType = JavaType.construct(java.util.List.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, String.class);
 assertEquals(javaType.getRawClass(), collectionLikeType.getRawClass());
 assertEquals(String.class, collectionLikeType.getElementType().getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCollectionLikeTypeUpgradeFromInvalidType() {
 JavaType javaType = JavaType.construct(Object.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, String.class);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testCollectionLikeTypeIsContainerType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
 assertTrue(collectionLikeType.isContainerType());

 collectionLikeType = new CollectionLikeType(String[].class, null, null, null, null, null, null, false);
 assertTrue(collectionLikeType.isContainerType());

 collectionLikeType = new CollectionLikeType(Map.class, null, null, null, null, null, null, false);
 assertFalse(collectionLikeType.isContainerType());
 }

 @Test
 public void testCollectionLikeTypeIsTrueCollectionType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
 assertTrue(collectionLikeType.isTrueCollectionType());

 collectionLikeType = new CollectionLikeType(ArrayList.class, null, null, null, null, null, null, false);
 assertTrue(collectionLikeType.isTrueCollectionType());

 collectionLikeType = new CollectionLikeType(LinkedHashSet.class, null, null, null, null, null, null, false);
 assertTrue(collectionLikeType.isTrueCollectionType());

 collectionLikeType = new CollectionLikeType(HashMap.class, null, null, null, null, null, null, false);
 assertFalse(collectionLikeType.isTrueCollectionType());
 }

 @Test
 public void testCollectionLikeTypeEquality() {
 CollectionLikeType collectionLikeType1 = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
 CollectionLikeType collectionLikeType2 = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
 assertEquals(collectionLikeType1, collectionLikeType2);

 JavaType javaType = JavaType.construct(java.util.ArrayList.class);
 collectionLikeType1 = new CollectionLikeType(ArrayList.class, null, null, null, javaType, null, null, false);
 collectionLikeType2 = new CollectionLikeType(ArrayList.class, null, null, null, javaType, null, null, false);
 assertEquals(collectionLikeType1, collectionLikeType2);

 collectionLikeType1 = new CollectionLikeType(ArrayList.class, null, null, null, javaType, null, null, true);
 collectionLikeType2 = new CollectionLikeType(ArrayList.class, null, null, null, javaType, null, null, true);
 assertEquals(collectionLikeType1, collectionLikeType2);

 collectionLikeType1 = new CollectionLikeType(ArrayList.class, null, null, null, javaType, null, null, false);
 collectionLikeType2 = new CollectionLikeType(ArrayList.class, null, null, null, javaType, null, null, true);
 assertNotEquals(collectionLikeType1, collectionLikeType2);
 }

 @Test
 public void testCollectionLikeTypeToString() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
 assertEquals("[collection-like type; class java.util.Collection, contains null]", collectionLikeType.toString());

 JavaType javaType = JavaType.construct(java.util.ArrayList.class);
 collectionLikeType = new CollectionLikeType(ArrayList.class, null, null, null, javaType, null, null, false);
 assertEquals("[collection-like type; class java.util.ArrayList, contains class java.lang.Object]", collectionLikeType.toString());
 }
}