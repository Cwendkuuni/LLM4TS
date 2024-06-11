package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

 @Test
 public void testConstructWithClassAndBindingsAndSuperClassAndSuperInterfacesAndElementType() {
 Class<List<Integer>> listClass = List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.construct(ArrayList.class, bindings);
 JavaType[] superInterfaces = new JavaType[] { Collection.class, List.class };
 JavaType elemT = JavaType.construct(Integer.class);
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 assertEquals(listClass, collectionType.getRawClass());
 assertEquals(bindings, collectionType.getTypeBindings());
 assertEquals(superClass, collectionType.getSuperClass());
 assertArrayEquals(superInterfaces, collectionType.getSuperInterfaces());
 assertEquals(elemT, collectionType.getContentType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithClassAndElementTypeWithNullBindings() {
 Class<List<Integer>> listClass = List.class;
 JavaType elemT = JavaType.construct(Integer.class);
 try {
 CollectionType collectionType = CollectionType.construct(listClass, null, _bogusSuperClass(listClass), null, elemT);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testToString() {
 Class<List<Integer>> listClass = List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.construct(ArrayList.class, bindings);
 JavaType[] superInterfaces = new JavaType[] { Collection.class, List.class };
 JavaType elemT = JavaType.construct(Integer.class);
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 String toString = collectionType.toString();
 assertTrue(toString.startsWith("[collection type; class "));
 assertTrue(toString.endsWith("]"));
 }

 @Test
 public void testConstructWithClassAndElementType() {
 Class<List<Integer>> listClass = List.class;
 JavaType elemT = JavaType.construct(Integer.class);
 CollectionType collectionType = CollectionType.construct(listClass, null, _bogusSuperClass(listClass), null, elemT);
 assertEquals(listClass, collectionType.getRawClass());
 assertEquals(null, collectionType.getTypeBindings());
 assertEquals(_bogusSuperClass(listClass), collectionType.getSuperClass());
 assertArrayEquals(new JavaType[0], collectionType.getSuperInterfaces());
 assertEquals(elemT, collectionType.getContentType());
 }

 @Test
 public void testWithContentType() {
 Class<List<Integer>> listClass = List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.construct(ArrayList.class, bindings);
 JavaType[] superInterfaces = new JavaType[] { Collection.class, List.class };
 JavaType elemT = JavaType.construct(Integer.class);
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 JavaType collectionTypeWithString = collectionType.withContentType(JavaType.construct(String.class));
 assertEquals(listClass, collectionTypeWithString.getRawClass());
 assertEquals(bindings, collectionTypeWithString.getTypeBindings());
 assertEquals(superClass, collectionTypeWithString.getSuperClass());
 assertArrayEquals(superInterfaces, collectionTypeWithString.getSuperInterfaces());
 assertEquals(JavaType.construct(String.class), collectionTypeWithString.getContentType());
 }

 @Test
 public void testWithTypeHandler() {
 Class<List<Integer>> listClass = List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.construct(ArrayList.class, bindings);
 JavaType[] superInterfaces = new JavaType[] { Collection.class, List.class };
 JavaType elemT = JavaType.construct(Integer.class);
 Object handler = new Object();
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 CollectionType collectionTypeWithHandler = collectionType.withTypeHandler(handler);
 assertEquals(listClass, collectionTypeWithHandler.getRawClass());
 assertEquals(bindings, collectionTypeWithHandler.getTypeBindings());
 assertEquals(superClass, collectionTypeWithHandler.getSuperClass());
 assertArrayEquals(superInterfaces, collectionTypeWithHandler.getSuperInterfaces());
 assertEquals(elemT, collectionTypeWithHandler.getContentType());
 assertEquals(handler, collectionTypeWithHandler.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<List<Integer>> listClass = List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.construct(ArrayList.class, bindings);
 JavaType[] superInterfaces = new JavaType[] { Collection.class, List.class };
 JavaType elemT = JavaType.construct(Integer.class);
 Object handler = new Object();
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 JavaType contentTypeHandler = JavaType.construct(String.class);
 CollectionType collectionTypeWithContentTypeHandler = collectionType.withContentTypeHandler(contentTypeHandler);
 assertEquals(listClass, collectionTypeWithContentTypeHandler.getRawClass());
 assertEquals(bindings, collectionTypeWithContentTypeHandler.getTypeBindings());
 assertEquals(superClass, collectionTypeWithContentTypeHandler.getSuperClass());
 assertArrayEquals(superInterfaces, collectionTypeWithContentTypeHandler.getSuperInterfaces());
 assertEquals(elemT.withTypeHandler(contentTypeHandler), collectionTypeWithContentTypeHandler.getContentType());
 assertEquals(handler, collectionTypeWithContentTypeHandler.getValueHandler());
 assertEquals(contentTypeHandler, collectionTypeWithContentTypeHandler.getTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 Class<List<Integer>> listClass = List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.construct(ArrayList.class, bindings);
 JavaType[] superInterfaces = new JavaType[] { Collection.class, List.class };
 JavaType elemT = JavaType.construct(Integer.class);
 Object handler = new Object();
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 CollectionType collectionTypeWithValueHandler = collectionType.withValueHandler(handler);
 assertEquals(listClass, collectionTypeWithValueHandler.getRawClass());
 assertEquals(bindings, collectionTypeWithValueHandler.getTypeBindings());
 assertEquals(superClass, collectionTypeWithValueHandler.getSuperClass());
 assertArrayEquals(superInterfaces, collectionTypeWithValueHandler.getSuperInterfaces());
 assertEquals(elemT, collectionTypeWithValueHandler.getContentType());
 assertEquals(handler, collectionTypeWithValueHandler.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<List<Integer>> listClass = List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.construct(ArrayList.class, bindings);
 JavaType[] superInterfaces = new JavaType[] { Collection.class, List.class };
 JavaType elemT = JavaType.construct(Integer.class);
 Object handler = new Object();
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 JavaType contentType = JavaType.construct(String.class);
 Object contentValueHandler = new Object();
 CollectionType collectionTypeWithContentValueHandler = collectionType.withContentValueHandler(contentValueHandler);
 assertEquals(listClass, collectionTypeWithContentValueHandler.getRawClass());
 assertEquals(bindings, collectionTypeWithContentValueHandler.getTypeBindings());
 assertEquals(superClass, collectionTypeWithContentValueHandler.getSuperClass());
 assertArrayEquals(superInterfaces, collectionTypeWithContentValueHandler.getSuperInterfaces());
 assertEquals(elemT.withValueHandler(contentValueHandler), collectionTypeWithContentValueHandler.getContentType());
 assertEquals(contentType, collectionTypeWithContentValueHandler.getTypeHandler());
 assertEquals(handler, collectionTypeWithContentValueHandler.getValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 Class<List<Integer>> listClass = List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.construct(ArrayList.class, bindings);
 JavaType[] superInterfaces = new JavaType[] { Collection.class, List.class };
 JavaType elemT = JavaType.construct(Integer.class);
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 CollectionType collectionTypeWithStaticTyping = collectionType.withStaticTyping();
 assertEquals(listClass, collectionTypeWithStaticTyping.getRawClass());
 assertEquals(bindings, collectionTypeWithStaticTyping.getTypeBindings());
 assertEquals(superClass, collectionTypeWithStaticTyping.getSuperClass());
 assertArrayEquals(superInterfaces, collectionTypeWithStaticTyping.getSuperInterfaces());
 assertEquals(elemT, collectionTypeWithStaticTyping.getContentType());
 assertTrue(collectionTypeWithStaticTyping.isStaticTyping());
 assertTrue(!collectionType.isStaticTyping());
 }

 @Test
 public void testRefine() {
 Class<List<Integer>> listClass = List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.construct(ArrayList.class, bindings);
 JavaType[] superInterfaces = new JavaType[] { Collection.class, List.class };
 JavaType elemT = JavaType.construct(Integer.class);
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 Class<List<String>> stringListClass = List.class;
 TypeBindings stringBindings = new TypeBindings();
 JavaType stringSuperClass = JavaType.construct(ArrayList.class, stringBindings);
 JavaType[] stringSuperInterfaces = new JavaType[] { Collection.class, List.class };
 JavaType stringElemT = JavaType.construct(String.class);
 CollectionType refinedCollectionType = collectionType.refine(stringListClass, stringBindings, stringSuperClass, stringSuperInterfaces);
 assertEquals(stringListClass, refinedCollectionType.getRawClass());
 assertEquals(stringBindings, refinedCollectionType.getTypeBindings());
 assertEquals(stringSuperClass, refinedCollectionType.getSuperClass());
 assertArrayEquals(stringSuperInterfaces, refinedCollectionType.getSuperInterfaces());
 assertEquals(stringElemT, refinedCollectionType.getContentType());
 }
}