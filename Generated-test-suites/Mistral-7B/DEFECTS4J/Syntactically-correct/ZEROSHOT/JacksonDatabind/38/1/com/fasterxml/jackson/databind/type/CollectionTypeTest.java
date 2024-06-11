package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class CollectionTypeTest {

 @Test
 public void testConstructor() {
 CollectionType collectionType = CollectionType.construct(ArrayList.class, null, null, null, null);
 assertNotNull(collectionType);
 assertEquals(ArrayList.class, collectionType.getRawClass());
 }

 @Test
 public void testDeprecatedConstruct() {
 CollectionType collectionType = CollectionType.construct(ArrayList.class, null);
 assertNotNull(collectionType);
 assertEquals(ArrayList.class, collectionType.getRawClass());
 }

 @Test
 public void testNarrow() {
 CollectionType listType = CollectionType.construct(ArrayList.class, null, null, null, null);
 CollectionType linkedListType = new CollectionType(LinkedList.class, null, null, null, null);
 CollectionType narrowedListType = listType.narrow(LinkedList.class);
 assertNotNull(narrowedListType);
 assertSame(listType, narrowedListType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNarrowException() {
 CollectionType listType = CollectionType.construct(ArrayList.class, null, null, null, null);
 CollectionType stringType = CollectionType.construct(String.class, null, null, null, null);
 listType.narrow(String.class);
 }

 @Test
 public void testWithContentType() {
 CollectionType listType = CollectionType.construct(ArrayList.class, null, null, null, null);
 JavaType stringType = BasicTypeFactory.intType();
 CollectionType stringListType = listType.withContentType(stringType);
 assertNotNull(stringListType);
 assertEquals(ArrayList.class, stringListType.getRawClass());
 assertEquals(stringType, stringListType.getContentType());
 }

 @Test
 public void testWithTypeHandler() {
 CollectionType listType = CollectionType.construct(ArrayList.class, null, null, null, null);
 CollectionType stringListType = listType.withTypeHandler(StringHandler.class);
 assertNotNull(stringListType);
 assertSame(listType, stringListType);
 }

 @Test
 public void testWithContentTypeHandler() {
 CollectionType listType = CollectionType.construct(ArrayList.class, null, null, null, null);
 CollectionType stringListType = listType.withContentTypeHandler(StringHandler.class);
 assertNotNull(stringListType);
 assertSame(listType, stringListType);
 assertEquals(StringHandler.class, stringListType.getTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 CollectionType listType = CollectionType.construct(ArrayList.class, null, null, null, null);
 CollectionType stringListType = listType.withValueHandler(StringHandler.class);
 assertNotNull(stringListType);
 assertSame(listType, stringListType);
 }

 @Test
 public void testWithContentValueHandler() {
 CollectionType listType = CollectionType.construct(ArrayList.class, null, null, null, null);
 CollectionType stringListType = listType.withContentValueHandler(StringHandler.class);
 assertNotNull(stringListType);
 assertSame(listType, stringListType);
 assertEquals(StringHandler.class, stringListType.getValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 CollectionType listType = CollectionType.construct(ArrayList.class, null, null, null, null);
 CollectionType staticListType = listType.withStaticTyping();
 assertNotNull(staticListType);
 assertTrue(staticListType.isStatic());
 }

 @Test
 public void testRefine() {
 CollectionType listType = CollectionType.construct(ArrayList.class, null, null, null, null);
 JavaType linkedListType = BasicTypeFactory.constructCollectionType(LinkedList.class, null);
 CollectionType refinedListType = listType.refine(LinkedList.class, null, null, new JavaType[] { linkedListType });
 assertNotNull(refinedListType);
 assertSame(linkedListType, refinedListType.getSuperClass());
 }
}