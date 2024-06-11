package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for CollectionType.
 */
public class CollectionTypeTest {

 @Test
 public void testConstructWithAllArgs() {
 Class<Integer> integerListClass = java.util.List.class.getTypeParameters()[0].getType();
 JavaType javaType = CollectionType.construct(integerListClass, null, null, null, java.type.TypeFactory.defaultInstance.getCollectionType(Integer.TYPE));
 CollectionType collectionType = (CollectionType) javaType;
 assertNotNull(collectionType);
 assertEquals(java.util.List.class, collectionType.getRawClass());
 assertEquals(java.util.List.class, collectionType.getRawClassWithoutArgs());
 assertEquals(Integer.TYPE, collectionType.getContentType().getRawClass());
 assertEquals(null, collectionType.getValueHandler());
 assertEquals(null, collectionType.getTypeHandler());
 }

 @Test
 public void testConstructWithDeprecatedArgs() {
 Class<Integer> integerListClass = java.util.List.class.getTypeParameters()[0].getType();
 JavaType javaType = CollectionType.construct(integerListClass, java.type.TypeFactory.defaultInstance.getCollectionType(Integer.TYPE));
 CollectionType collectionType = (CollectionType) javaType;
 assertNotNull(collectionType);
 assertEquals(java.util.List.class, collectionType.getRawClass());
 assertEquals(java.util.List.class, collectionType.getRawClassWithoutArgs());
 assertEquals(Integer.TYPE, collectionType.getContentType().getRawClass());
 assertEquals(null, collectionType.getValueHandler());
 assertEquals(null, collectionType.getTypeHandler());
 }

 @Test
 public void testRefine() {
 Class<Integer> integerArrayListClass = java.util.ArrayList.class;
 JavaType javaType = CollectionType.construct(java.util.List.class, null, null, null, java.type.TypeFactory.defaultInstance.getCollectionType(Integer.TYPE));
 CollectionType collectionType = (CollectionType) javaType;
 JavaType refinedType = collectionType.refine(integerArrayListClass, null, null, null);
 CollectionType refinedCollectionType = (CollectionType) refinedType;
 assertNotNull(refinedCollectionType);
 assertEquals(java.util.ArrayList.class, refinedCollectionType.getRawClass());
 assertEquals(java.util.ArrayList.class, refinedCollectionType.getRawClassWithoutArgs());
 assertEquals(Integer.TYPE, refinedCollectionType.getContentType().getRawClass());
 assertEquals(null, refinedCollectionType.getValueHandler());
 assertEquals(null, refinedCollectionType.getTypeHandler());
 }

 @Test
 public void testNarrow() {
 Class<Integer> integerArrayListClass = java.util.ArrayList.class;
 JavaType javaType = CollectionType.construct(java.util.List.class, null, null, null, java.type.TypeFactory.defaultInstance.getCollectionType(Integer.TYPE));
 CollectionType collectionType = (CollectionType) javaType;
 JavaType narrowedType = collectionType.narrow(integerArrayListClass);
 CollectionType narrowedCollectionType = (CollectionType) narrowedType;
 assertNotNull(narrowedCollectionType);
 assertEquals(java.util.ArrayList.class, narrowedCollectionType.getRawClass());
 assertEquals(java.util.ArrayList.class, narrowedCollectionType.getRawClassWithoutArgs());
 assertEquals(Integer.TYPE, narrowedCollectionType.getContentType().getRawClass());
 assertEquals(null, narrowedCollectionType.getValueHandler());
 assertEquals(null, narrowedCollectionType.getTypeHandler());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNarrowWithDifferentType() {
 Class<Integer> integerArrayListClass = java.util.ArrayList.class;
 JavaType javaType = CollectionType.construct(java.util.List.class, null, null, null, java.type.TypeFactory.defaultInstance.getCollectionType(String.class));
 CollectionType collectionType = (CollectionType) javaType;
 JavaType narrowedType = collectionType.narrow(integerArrayListClass);
 }

 @Test
 public void testWithContentType() {
 Class<Integer> integerListClass = java.util.List.class.getTypeParameters()[0].getType();
 JavaType javaType = CollectionType.construct(integerListClass, null, null, null, java.type.TypeFactory.defaultInstance.getCollectionType(Integer.TYPE));
 CollectionType collectionType = (CollectionType) javaType;
 JavaType collectionTypeWithStringContentType = collectionType.withContentType(java.type.TypeFactory.defaultInstance.getCollectionType(String.class));
 CollectionType refinedCollectionTypeWithStringContentType = (CollectionType) collectionTypeWithStringContentType.refine(java.util.ArrayList.class, null, null, null);
 assertNotNull(refinedCollectionTypeWithStringContentType);
 assertEquals(java.util.ArrayList.class, refinedCollectionTypeWithStringContentType.getRawClass());
 assertEquals(java.util.ArrayList.class, refinedCollectionTypeWithStringContentType.getRawClassWithoutArgs());
 assertEquals(java.util.Collection.class, refinedCollectionTypeWithStringContentType.getContentType().getRawClass());
 assertEquals(null, refinedCollectionTypeWithStringContentType.getValueHandler());
 assertEquals(null, refinedCollectionTypeWithStringContentType.getTypeHandler());
 }

 @Test
 public void testWithTypeHandler() {
 Class<Integer> integerListClass = java.util.List.class.getTypeParameters()[0].getType();
 JavaType javaType = CollectionType.construct(integerListClass, null, null, null, java.type.TypeFactory.defaultInstance.getCollectionType(Integer.TYPE));
 CollectionType collectionType = (CollectionType) javaType;
 Object testTypeHandler = new Object(){};
 CollectionType collectionTypeWithTypeHandler = collectionType.withTypeHandler(testTypeHandler);
 assertNotNull(collectionTypeWithTypeHandler);
 assertEquals(java.util.List.class, collectionTypeWithTypeHandler.getRawClass());
 assertEquals(java.util.List.class, collectionTypeWithTypeHandler.getRawClassWithoutArgs());
 assertEquals(Integer.TYPE, collectionTypeWithTypeHandler.getContentType().getRawClass());
 assertEquals(testTypeHandler, collectionTypeWithTypeHandler.getTypeHandler());
 assertEquals(null, collectionTypeWithTypeHandler.getValueHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<Integer> integerListClass = java.util.List.class.getTypeParameters()[0].getType();
 JavaType javaType = CollectionType.construct(integerListClass, null, null, null, java.type.TypeFactory.defaultInstance.getCollectionType(Integer.TYPE));
 CollectionType collectionType = (CollectionType) javaType;
 Object testContentTypeHandler = new Object(){};
 CollectionType collectionTypeWithContentTypeHandler = collectionType.withContentTypeHandler(testContentTypeHandler);
 assertNotNull(collectionTypeWithContentTypeHandler);
 assertEquals(java.util.List.class, collectionTypeWithContentTypeHandler.getRawClass());
 assertEquals(java.util.List.class, collectionTypeWithContentTypeHandler.getRawClassWithoutArgs());
 assertEquals(Integer.TYPE, collectionTypeWithContentTypeHandler.getContentType().getRawClass());
 assertEquals(null, collectionTypeWithContentTypeHandler.getValueHandler());
 assertEquals(testContentTypeHandler, collectionTypeWithContentTypeHandler.getTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 Class<Integer> integerListClass = java.util.List.class.getTypeParameters()[0].getType();
 JavaType javaType = CollectionType.construct(integerListClass, null, null, null, java.type.TypeFactory.defaultInstance.getCollectionType(Integer.TYPE));
 CollectionType collectionType = (CollectionType) javaType;
 Object testValueHandler = new Object(){};
 CollectionType collectionTypeWithValueHandler = collectionType.withValueHandler(testValueHandler);
 assertNotNull(collectionTypeWithValueHandler);
 assertEquals(java.util.List.class, collectionTypeWithValueHandler.getRawClass());
 assertEquals(java.util.List.class, collectionTypeWithValueHandler.getRawClassWithoutArgs());
 assertEquals(Integer.TYPE, collectionTypeWithValueHandler.getContentType().getRawClass());
 assertEquals(null, collectionTypeWithValueHandler.getTypeHandler());
 assertEquals(testValueHandler, collectionTypeWithValueHandler.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<Integer> integerListClass = java.util.List.class.getTypeParameters()[0].getType();
 JavaType javaType = CollectionType.construct(integerListClass, null, null, null, java.type.TypeFactory.defaultInstance.getCollectionType(Integer.TYPE));
 CollectionType collectionType = (CollectionType) javaType;
 Object testContentValueHandler = new Object(){};
 CollectionType collectionTypeWithContentValueHandler = collectionType.withContentValueHandler(testContentValueHandler);
 assertNotNull(collectionTypeWithContentValueHandler);
 assertEquals(java.util.List.class, collectionTypeWithContentValueHandler.getRawClass());
 assertEquals(java.util.List.class, collectionTypeWithContentValueHandler.getRawClassWithoutArgs());
 assertEquals(Integer.TYPE, collectionTypeWithContentValueHandler.getContentType().getRawClass());
 assertEquals(testContentValueHandler, collectionTypeWithContentValueHandler.getValueHandler());
 assertEquals(null, collectionTypeWithContentValueHandler.getTypeHandler());
 }

 @Test
 public void testStaticTyping() {
 Class<Integer> integerListClass = java.util.List.class.getTypeParameters()[0].getType();
 JavaType javaType = CollectionType.construct(integerListClass, null, null, null, java.type.TypeFactory.defaultInstance.getCollectionType(Integer.TYPE));
 CollectionType collectionType = (CollectionType) javaType;
 CollectionType collectionTypeWithStaticTyping = collectionType.withStaticTyping();
 assertNotNull(collectionTypeWithStaticTyping);
 assertEquals(java.util.List.class, collectionTypeWithStaticTyping.getRawClass());
 assertEquals(java.util.List.class, collectionTypeWithStaticTyping.getRawClassWithoutArgs());
 assertEquals(Integer.TYPE, collectionTypeWithStaticTyping.getContentType().getRawClass());
 assertEquals(null, collectionTypeWithStaticTyping.getValueHandler());
 assertEquals(null, collectionTypeWithStaticTyping.getTypeHandler());
 assertTrue(collectionTypeWithStaticTyping._asStatic);
 }

 @Test
 public void testToString() {
 Class<Integer> integerListClass = java.util.List.class.getTypeParameters()[0].getType();
 JavaType javaType = CollectionType.construct(integerListClass, null, null, null, java.type.TypeFactory.defaultInstance.getCollectionType(Integer.TYPE));
 CollectionType collectionType = (CollectionType) javaType;
 String expected = "[collection type; class java.util.List, contains class java.lang.Integer]";
 String result = collectionType.toString();
 assertEquals(expected, result);
 }
}