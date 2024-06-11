package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

 private JavaType elementType;
 private JavaType collectionType;
 private JavaType collectionLikeType;
 private JavaTypeFactory typeFactory;

 @Before
 public void setup() {
 typeFactory = new JavaTypeFactory();
 elementType = typeFactory.constructType(String.class);
 collectionType = typeFactory.constructCollectionType(List.class, elementType);
 }

 @Test
 public void testConstruct() {
 collectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, elementType);
 assertEquals("collection-like type; class class java.util.Collection, contains [Ljava.lang.String;]", collectionLikeType.toString());
 assertEquals(elementType, collectionLikeType.getContentType());
 assertTrue(collectionLikeType.isCollectionLikeType());
 }

 @Test
 public void testConstructWithBindings() {
 collectionLikeType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, elementType);
 assertEquals("collection-like type; class class java.util.ArrayList, contains [Ljava.lang.String;]", collectionLikeType.toString());
 assertEquals(elementType, collectionLikeType.getContentType());
 assertTrue(collectionLikeType.isCollectionLikeType());
 }

 @Test
 public void testUpgradeFrom() {
 JavaType listType = typeFactory.constructCollectionType(List.class, elementType);
 collectionLikeType = CollectionLikeType.upgradeFrom(listType, elementType);
 assertEquals("collection-like type; class class java.util.List, contains [Ljava.lang.String;]", collectionLikeType.toString());
 assertEquals(elementType, collectionLikeType.getContentType());
 assertTrue(collectionLikeType.isCollectionLikeType());
 }

 @Test
 public void testNarrow() {
 JavaType arrayListType = typeFactory.constructType(ArrayList.class);
 JavaType narrowedType = collectionLikeType.narrow(ArrayList.class);
 assertEquals("collection-like type; class class java.util.ArrayList, contains [Ljava.lang.String;]", narrowedType.toString());
 assertEquals(elementType, narrowedType.getContentType());
 }

 @Test
 public void testIsTrueCollectionType() {
 assertTrue(collectionLikeType.isTrueCollectionType());
 assertFalse(CollectionBaseType.construct(Collection.class, TypeBindings.emptyBindings()).isTrueCollectionType());
 }
}