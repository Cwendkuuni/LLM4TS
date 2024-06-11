package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

 @Test
 public void testConstruct() {
 Class<List<Integer>> listClass = java.util.List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = null;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = JavaType.constructCollectionType(List.class, Integer.TYPE);
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);
 assertEquals(List.class, collectionType.getRawClass());
 assertEquals(bindings, collectionType.getBindings());
 assertEquals(superClass, collectionType.getSuperclass());
 assertEquals(superInts, collectionType.getSuperInterfaces());
 assertEquals(Integer.class, collectionType.getContentType());
 assertNull(collectionType.getValueHandler());
 assertNull(collectionType.getTypeHandler());
 assertFalse(collectionType.isStatic());
 }

 @Test
 public void testConstructWithBindings() {
 Class<List<Integer>> listClass = java.util.List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = null;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = JavaType.constructCollectionType(List.class, Integer.TYPE);
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);
 assertEquals(List.class, collectionType.getRawClass());
 assertEquals(bindings, collectionType.getBindings());
 assertEquals(superClass, collectionType.getSuperclass());
 assertEquals(superInts, collectionType.getSuperInterfaces());
 assertEquals(Integer.class, collectionType.getContentType());
 assertNull(collectionType.getValueHandler());
 assertNull(collectionType.getTypeHandler());
 assertFalse(collectionType.isStatic());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullClass() {
 JavaType elemT = JavaType.constructCollectionType(List.class, Integer.TYPE);
 CollectionType collectionType = CollectionType.construct(null, null, null, null, elemT);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullBindings() {
 Class<List<Integer>> listClass = java.util.List.class;
 JavaType elemT = JavaType.constructCollectionType(List.class, Integer.TYPE);
 CollectionType collectionType = CollectionType.construct(listClass, null, null, null, elemT);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullSuperclass() {
 Class<List<Integer>> listClass = java.util.List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = JavaType.constructCollectionType(List.class, Integer.TYPE);
 CollectionType collectionType = CollectionType.construct(listClass, bindings, null, superInts, elemT);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullSuperInterfaces() {
 Class<List<Integer>> listClass = java.util.List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.constructType(Object.class);
 JavaType[] superInts = null;
 JavaType elemT = JavaType.constructCollectionType(List.class, Integer.TYPE);
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithInvalidContentType() {
 Class<List<Integer>> listClass = java.util.List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.constructType(Object.class);
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = JavaType.constructType(String.class);
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testConstructDeprecated() {
 Class<List<Integer>> listClass = java.util.List.class;
 JavaType elemT = JavaType.constructCollectionType(List.class, Integer.TYPE);
 CollectionType collectionType = CollectionType.construct(listClass, elemT);
 assertEquals(List.class, collectionType.getRawClass());
 assertNull(collectionType.getBindings());
 assertNull(collectionType.getSuperclass());
 assertNull(collectionType.getSuperInterfaces());
 assertEquals(Integer.class, collectionType.getContentType());
 assertNull(collectionType.getValueHandler());
 assertNull(collectionType.getTypeHandler());
 assertFalse(collectionType.isStatic());
 }

 @Test
 public void testNarrow() {
 Class<ArrayList<Integer>> arrayListClass = java.util.ArrayList.class;
 Class<List<Integer>> listClass = java.util.List.class;
 JavaType elemT = JavaType.constructCollectionType(List.class, Integer.TYPE);
 CollectionType listType = CollectionType.construct(listClass, null, null, null, elemT);
 CollectionType arrayListType = listType.narrow(ArrayList.class);
 assertEquals(ArrayList.class, arrayListType.getRawClass());
 assertEquals(null, arrayListType.getBindings());
 assertNull(arrayListType.getSuperclass());
 assertNull(arrayListType.getSuperInterfaces());
 assertEquals(Integer.class, arrayListType.getContentType());
 assertNull(arrayListType.getValueHandler());
 assertNull(arrayListType.getTypeHandler());
 assertFalse(arrayListType.isStatic());
 }

 @Test
 public void testWithContentType() {
 Class<List<Integer>> listClass = java.util.List.class;
 JavaType elemT = JavaType.constructCollectionType(List.class, Integer.class);
 CollectionType listType = CollectionType.construct(listClass, null, null, null, elemT);
 JavaType newContentType = JavaType.constructCollectionType(List.class, String.class);
 CollectionType newListType = listType.withContentType(newContentType);
 assertEquals(List.class, newListType.getRawClass());
 assertEquals(null, newListType.getBindings());
 assertNull(newListType.getSuperclass());
 assertNull(newListType.getSuperInterfaces());
 assertEquals(String.class, newListType.getContentType());
 assertNull(newListType.getValueHandler());
 assertNull(newListType.getTypeHandler());
 assertFalse(newListType.isStatic());
 }

 @Test
 public void testWithTypeHandler() {
 Class<List<Integer>> listClass = java.util.List.class;
 JavaType elemT = JavaType.constructCollectionType(List.class, Integer.class);
 CollectionType listType = CollectionType.construct(listClass, null, null, null, elemT);
 Object typeHandler = new Object() {};
 CollectionType newListType = listType.withTypeHandler(typeHandler);
 assertEquals(List.class, newListType.getRawClass());
 assertEquals(null, newListType.getBindings());
 assertNull(newListType.getSuperclass());
 assertNull(newListType.getSuperInterfaces());
 assertEquals(Integer.class, newListType.getContentType());
 assertEquals(typeHandler, newListType.getTypeHandler());
 assertNull(newListType.getValueHandler());
 assertFalse(newListType.isStatic());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<List<Integer>> listClass = java.util.List.class;
 JavaType elemT = JavaType.constructCollectionType(List.class, Integer.class);
 CollectionType listType = CollectionType.construct(listClass, null, null, null, elemT);
 Object valueHandler = new Object() {};
 Object typeHandler = new Object() {};
 CollectionType newListType = listType.withContentTypeHandler(valueHandler).withTypeHandler(typeHandler);
 assertEquals(List.class, newListType.getRawClass());
 assertEquals(null, newListType.getBindings());
 assertNull(newListType.getSuperclass());
 assertNull(newListType.getSuperInterfaces());
 assertEquals(Integer.class, newListType.getContentType());
 assertEquals(valueHandler, newListType.getValueHandler());
 assertEquals(typeHandler, newListType.getTypeHandler());
 assertFalse(newListType.isStatic());
 }

 @Test
 public void testWithValueHandler() {
 Class<List<Integer>> listClass = java.util.List.class;
 JavaType elemT = JavaType.constructCollectionType(List.class, Integer.class);
 CollectionType listType = CollectionType.construct(listClass, null, null, null, elemT);
 Object valueHandler = new Object() {};
 CollectionType newListType = listType.withValueHandler(valueHandler);
 assertEquals(List.class, newListType.getRawClass());
 assertEquals(null, newListType.getBindings());
 assertNull(newListType.getSuperclass());
 assertNull(newListType.getSuperInterfaces());
 assertEquals(Integer.class, newListType.getContentType());
 assertNull(newListType.getTypeHandler());
 assertEquals(valueHandler, newListType.getValueHandler());
 assertFalse(newListType.isStatic());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<List<Integer>> listClass = java.util.List.class;
 JavaType elemT = JavaType.constructCollectionType(List.class, Integer.class);
 CollectionType listType = CollectionType.construct(listClass, null, null, null, elemT);
 Object valueHandler = new Object() {};
 Object typeHandler = new Object() {};
 CollectionType newListType = listType.withContentValueHandler(valueHandler).withTypeHandler(typeHandler);
 assertEquals(List.class, newListType.getRawClass());
 assertEquals(null, newListType.getBindings());
 assertNull(newListType.getSuperclass());
 assertNull(newListType.getSuperInterfaces());
 assertEquals(Integer.class, newListType.getContentType());
 assertEquals(valueHandler, newListType.getValueHandler());
 assertEquals(typeHandler, newListType.getTypeHandler());
 assertFalse(newListType.isStatic());
 }

 @Test
 public void testWithStaticTyping() {
 Class<List<Integer>> listClass = java.util.List.class;
 JavaType elemT = JavaType.constructCollectionType(List.class, Integer.class);
 CollectionType listType = CollectionType.construct(listClass, null, null, null, elemT);
 CollectionType newListType = listType.withStaticTyping();
 assertEquals(List.class, newListType.getRawClass());
 assertEquals(null, newListType.getBindings());
 assertNull(newListType.getSuperclass());
 assertNull(newListType.getSuperInterfaces());
 assertEquals(Integer.class, newListType.getContentType());
 assertNull(newListType.getValueHandler());
 assertNull(newListType.getTypeHandler());
 assertTrue(newListType.isStatic());
 }

 @Test
 public void testRefine() {
 Class<ArrayList<Integer>> arrayListClass = java.util.ArrayList.class;
 Class<List<Integer>> listClass = java.util.List.class;
 JavaType elemT = JavaType.constructCollectionType(List.class, Integer.TYPE);
 CollectionType listType = CollectionType.construct(listClass, null, null, null, elemT);
 CollectionType newListType = listType.refine(arrayListClass, new TypeBindings(), null, null);
 assertEquals(ArrayList.class, newListType.getRawClass());
 assertEquals(null, newListType.getBindings());
 assertNull(newListType.getSuperclass());
 assertNull(newListType.getSuperInterfaces());
 assertEquals(Integer.class, newListType.getContentType());
 assertNull(newListType.getValueHandler());
 assertNull(newListType.getTypeHandler());
 assertFalse(newListType.isStatic());
 }
}