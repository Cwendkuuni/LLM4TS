package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CollectionLikeTypeTest {

 @Test
 public void testBasicConstruction() {
 CollectionLikeType type = CollectionLikeType.construct(java.util.ArrayList.class, new TypeBindings.Base(),
 new JavaType(java.util.List.class), null, new JavaType(String.class));
 assertEquals(java.util.ArrayList.class, type.getRawClass());
 assertEquals(String.class, type.getContentType().getRawClass());
 assertFalse(type.isTrueCollectionType());
 }

 @Test
 public void testCollectionLikeTypeConstruction() {
 CollectionLikeType type = new CollectionLikeType(java.util.ArrayList.class, new TypeBindings.Base(),
 new JavaType(java.util.List.class), null, new JavaType(String.class), null, null, false);
 assertEquals(java.util.ArrayList.class, type.getRawClass());
 assertEquals(String.class, type.getContentType().getRawClass());
 assertFalse(type.isTrueCollectionType());
 }

 @Test
 public void testUpgradeFrom() {
 JavaType listType = new JavaType(java.util.List.class);
 JavaType stringType = new JavaType(String.class);
 CollectionLikeType collectionType = CollectionLikeType.upgradeFrom(listType, stringType);
 assertEquals(listType.getRawClass(), collectionType.getRawClass());
 assertEquals(stringType, collectionType.getContentType());
 assertFalse(collectionType.isTrueCollectionType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromInvalidType() {
 JavaType intType = new JavaType(int.class);
 JavaType stringType = new JavaType(String.class);
 CollectionLikeType collectionType = CollectionLikeType.upgradeFrom(intType, stringType);
 }

 @Test
 public void testNarrowing() {
 JavaType listType = new JavaType(java.util.List.class);
 JavaType stringType = new JavaType(String.class);
 CollectionLikeType collectionType = CollectionLikeType.upgradeFrom(listType, stringType);
 CollectionLikeType narrowedType = collectionType.narrow(ArrayList.class);
 assertEquals(ArrayList.class, narrowedType.getRawClass());
 assertEquals(String.class, narrowedType.getContentType().getRawClass());
 assertFalse(narrowedType.isTrueCollectionType());
 }

 @Test
 public void testRefining() {
 JavaType arrayListType = new JavaType(ArrayList.class);
 JavaType stringType = new JavaType(String.class);
 CollectionLikeType collectionType = new CollectionLikeType(arrayListType.getRawClass(), new TypeBindings.Base(),
 new JavaType(java.util.List.class), null, stringType, null, null, false);
 CollectionLikeType refinedType = collectionType.refine(ArrayList.class, new TypeBindings.Base(),
 new JavaType(java.util.ArrayList.class), null);
 assertEquals(ArrayList.class, refinedType.getRawClass());
 assertEquals(String.class, refinedType.getContentType().getRawClass());
 assertFalse(refinedType.isTrueCollectionType());
 }

 @Test
 public void testIsContainerTypeAndIsCollectionLikeType() {
 CollectionLikeType type = CollectionLikeType.construct(java.util.ArrayList.class, new TypeBindings.Base(),
 new JavaType(java.util.List.class), null, new JavaType(String.class));
 assertTrue(type.isContainerType());
 assertTrue(type.isCollectionLikeType());
 }

 @Test
 public void testGetContentType() {
 CollectionLikeType type = CollectionLikeType.construct(java.util.ArrayList.class, new TypeBindings.Base(),
 new JavaType(java.util.List.class), null, new JavaType(String.class));
 assertEquals(String.class, type.getContentType().getRawClass());
 }

 @Test
 public void testGetContentValueHandler() {
 CollectionLikeType type = CollectionLikeType.construct(java.util.ArrayList.class, new TypeBindings.Base(),
 new JavaType(java.util.List.class), null, new JavaType(String.class));
 assertNull(type.getContentValueHandler());
 }

 @Test
 public void testHasHandlers() {
 CollectionLikeType type = CollectionLikeType.construct(java.util.ArrayList.class, new TypeBindings.Base(),
 new JavaType(java.util.List.class), null, new JavaType(String.class));
 assertFalse(type.hasHandlers());
 }

 @Test
 public void testGetErasedSignature() {
 CollectionLikeType type = CollectionLikeType.construct(java.util.ArrayList.class, new TypeBindings.Base(),
 new JavaType(java.util.List.class), null, new JavaType(String.class));
 StringBuilder sb = new StringBuilder();
 type.getErasedSignature(sb);
 assertEquals("java.util.ArrayList", sb.toString());
 }

 @Test
 public void testGetGenericSignature() {
 CollectionLikeType type = CollectionLikeType.construct(java.util.ArrayList.class, new TypeBindings.Base(),
 new JavaType(java.util.List.class), null, new JavaType(String.class));
 StringBuilder sb = new StringBuilder();
 type.getGenericSignature(sb);
 assertEquals("[Ljava.lang.String;", sb.toString());
 }

 @Test
 public void testToString() {
 CollectionLikeType type = CollectionLikeType.construct(java.util.ArrayList.class, new TypeBindings.Base(),
 new JavaType(java.util.List.class), null, new JavaType(String.class));
 assertEquals("[collection-like type; class java.util.ArrayList, contains class java.lang.String]", type.toString());
 }

 @Test
 public void testIsTrueCollectionType() {
 CollectionLikeType type = CollectionLikeType.construct(java.util.ArrayList.class, new TypeBindings.Base(),
 new JavaType(java.util.List.class), null, new JavaType(String.class));
 assertFalse(type.isTrueCollectionType());
 CollectionLikeType listType = new CollectionLikeType(java.util.ArrayList.class, new TypeBindings.Base(),
 new JavaType(java.util.List.class), null, new JavaType(String.class), null, null, false);
 assertTrue(listType.isTrueCollectionType());
 }
}