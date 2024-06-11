package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

 @Test
 public void testCollectionLikeTypeConstruction() {
 // Test constructor with arguments
 JavaType elemType = JavaType.construct(int[].class);
 TypeBindings bindings = new TypeBindings();
 Class<?> rawType = ArrayList.class;
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, null, null, elemType);
 assertEquals(rawType, collectionLikeType.getRawClass());
 assertEquals(elemType, collectionLikeType.getContentType());

 // Test constructor without arguments
 JavaType elemType2 = JavaType.construct(String.class);
 TypeBindings bindings2 = new TypeBindings();
 Class<?> rawType2 = LinkedList.class;
 CollectionLikeType collectionLikeType2 = new CollectionLikeType(TypeBase.construct(rawType2), elemType2);
 assertEquals(rawType2, collectionLikeType2.getRawClass());
 assertEquals(elemType2, collectionLikeType2.getContentType());
 }

 @Test
 public void testUpgradeFrom() {
 JavaType baseType = JavaType.construct(ArrayList.class);
 JavaType elemType = JavaType.construct(int.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elemType);
 assertEquals(ArrayList.class, collectionLikeType.getRawClass());
 assertEquals(elemType, collectionLikeType.getContentType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromNullBaseType() {
 JavaType elemType3 = JavaType.construct(char.class);
 CollectionLikeType collectionLikeType3 = CollectionLikeType.upgradeFrom(null, elemType3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromInvalidBaseType() {
 JavaType elemType4 = JavaType.construct(int[].class);
 CollectionLikeType collectionLikeType4 = CollectionLikeType.upgradeFrom(JavaType.construct(String.class), elemType4);
 }

 @Test
 public void testIsTrueCollectionType() {
 JavaType listType = JavaType.construct(ArrayList.class);
 assertTrue(listType.isTrueCollectionType());
 JavaType setType = JavaType.construct(HashSet.class);
 assertTrue(setType.isTrueCollectionType());
 JavaType mapType = JavaType.construct(HashMap.class);
 assertFalse(mapType.isTrueCollectionType()); // Maps do not implement Collection
 }

 @Test
 public void testIsCollectionLikeType() {
 JavaType listType2 = JavaType.construct(ArrayList.class);
 assertTrue(listType2.isCollectionLikeType());
 JavaType mapType2 = JavaType.construct(HashMap.class);
 assertTrue(mapType2.isCollectionLikeType());
 }

 @Test
 public void testGetContentType() {
 JavaType listType3 = JavaType.construct(ArrayList.class);
 JavaType intType = JavaType.construct(int.class);
 assertEquals(intType, listType3.getContentType());
 JavaType mapType3 = JavaType.construct(HashMap.class);
 JavaType StringType = JavaType.construct(String.class);
 assertEquals(StringType, mapType3.getContentType());
 }

 @Test
 public void testEquals() {
 JavaType listType4 = JavaType.construct(ArrayList.class);
 JavaType listType5 = JavaType.construct(ArrayList.class);
 JavaType listType6 = JavaType.construct(ArrayList.class);
 JavaType listType7 = JavaType.construct(LinkedList.class);
 JavaType listType8 = JavaType.construct(LinkedList.class);
 assertEquals(listType4, listType4);
 assertEquals(listType4, listType5);
 assertNotEquals(listType4, listType6);
 assertNotEquals(listType4, listType7);
 }

 @Test
 public void testCanonicalName() {
 JavaType listType9 = JavaType.construct(ArrayList.class);
 assertEquals("java.util.ArrayList", listType9.getCanonicalName());
 JavaType mapType4 = JavaType.construct(LinkedHashMap.class);
 assertEquals("java.util.LinkedHashMap", mapType4.getCanonicalName());
 }

 @Test
 public void testToString() {
 JavaType listType10 = JavaType.construct(ArrayList.class);
 assertEquals("[collection-like type; class java.util.ArrayList, contains class [I]", listType10.toString());
 JavaType mapType5 = JavaType.construct(LinkedHashMap.class);
 assertEquals("[collection-like type; class java.util.LinkedHashMap, contains class Ljava/lang/String;]", mapType5.toString());
 }

}