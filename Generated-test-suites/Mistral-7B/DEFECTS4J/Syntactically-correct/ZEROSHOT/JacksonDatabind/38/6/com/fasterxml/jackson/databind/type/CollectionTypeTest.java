package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CollectionTypeTest {

 @Test
 public void testCollectionTypeConstruction() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Object.class;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemType = Integer.TYPE;
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemType);

 assertEquals(listClass, collectionType.getRawClass());
 assertEquals(bindings, collectionType.getTypeBindings());
 assertEquals(superClass, collectionType.getSuperClass());
 assertNotNull(collectionType.getSuperInterfaces());
 assertEquals(elemType, collectionType.getContentType());
 }

 @Test
 public void testCollectionTypeConstructDeprecated() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = Integer.TYPE;
 CollectionType collectionType = CollectionType.construct(listClass, elemType);

 assertEquals(listClass, collectionType.getRawClass());
 assertNull(collectionType.getTypeBindings());
 assertEquals(null, collectionType.getSuperClass());
 assertNull(collectionType.getSuperInterfaces());
 assertEquals(elemType, collectionType.getContentType());
 }

 @Test
 public void testCollectionTypeNarrow() {
 Class<?> listClass = ArrayList.class;
 Class<?> linkedListClass = LinkedList.class;
 CollectionType collectionType = CollectionType.construct(listClass, null, Object.class, new JavaType[0], Integer.TYPE);

 CollectionType narrowType = collectionType.narrow(linkedListClass);
 assertEquals(linkedListClass, narrowType.getRawClass());
 assertArrayEquals(new JavaType[0], narrowType.getSuperInterfaces());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCollectionTypeNarrowThrowsException() {
 Class<?> listClass = ArrayList.class;
 CollectionType collectionType = CollectionType.construct(listClass, null, Object.class, new JavaType[0], Integer.TYPE);

 collectionType.narrow(String.class);
 }

 @Test
 public void testCollectionTypeWithContentType() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Object.class;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemType = Integer.TYPE;
 JavaType contentType = String.class;
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemType);

 CollectionType withContentType = collectionType.withContentType(contentType);
 assertEquals(listClass, withContentType.getRawClass());
 assertEquals(bindings, withContentType.getTypeBindings());
 assertEquals(superClass, withContentType.getSuperClass());
 assertNotNull(withContentType.getSuperInterfaces());
 assertEquals(contentType, withContentType.getContentType());
 }

 @Test
 public void testCollectionTypeWithTypeHandler() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Object.class;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemType = Integer.TYPE;
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemType);

 Object typeHandler = new Object();
 CollectionType withTypeHandler = collectionType.withTypeHandler(typeHandler);
 assertEquals(listClass, withTypeHandler.getRawClass());
 assertEquals(bindings, withTypeHandler.getTypeBindings());
 assertEquals(superClass, withTypeHandler.getSuperClass());
 assertNotNull(withTypeHandler.getSuperInterfaces());
 assertEquals(elemType, withTypeHandler.getContentType());
 assertEquals(typeHandler, withTypeHandler.getTypeHandler());
 }

 @Test
 public void testCollectionTypeWithContentTypeHandler() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Object.class;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemType = Integer.TYPE;
 JavaType contentType = String.class;
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemType);

 Object contentTypeHandler = new Object();
 CollectionType withContentTypeHandler = collectionType.withContentTypeHandler(contentTypeHandler);
 assertEquals(listClass, withContentTypeHandler.getRawClass());
 assertEquals(bindings, withContentTypeHandler.getTypeBindings());
 assertEquals(superClass, withContentTypeHandler.getSuperClass());
 assertNull(withContentTypeHandler.getSuperInterfaces());
 assertEquals(elemType, withContentTypeHandler.getContentType());
 assertEquals(contentType, withContentTypeHandler.getContentType().getType());
 assertEquals(contentTypeHandler, withContentTypeHandler.getContentTypeHandler());
 }

 @Test
 public void testCollectionTypeWithValueHandler() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Object.class;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemType = Integer.TYPE;
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemType);

 Object valueHandler = new Object();
 CollectionType withValueHandler = collectionType.withValueHandler(valueHandler);
 assertEquals(listClass, withValueHandler.getRawClass());
 assertEquals(bindings, withValueHandler.getTypeBindings());
 assertEquals(superClass, withValueHandler.getSuperClass());
 assertNull(withValueHandler.getSuperInterfaces());
 assertEquals(elemType, withValueHandler.getContentType());
 assertEquals(valueHandler, withValueHandler.getValueHandler());
 }

 @Test
 public void testCollectionTypeWithContentValueHandler() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Object.class;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemType = Integer.TYPE;
 JavaType contentType = String.class;
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemType);

 Object contentValueHandler = new Object();
 CollectionType withContentValueHandler = collectionType.withContentValueHandler(contentValueHandler);
 assertEquals(listClass, withContentValueHandler.getRawClass());
 assertEquals(bindings, withContentValueHandler.getTypeBindings());
 assertEquals(superClass, withContentValueHandler.getSuperClass());
 assertNull(withContentValueHandler.getSuperInterfaces());
 assertEquals(elemType, withContentValueHandler.getContentType());
 assertEquals(contentType, withContentValueHandler.getContentType().getType());
 assertEquals(contentValueHandler, withContentValueHandler.getContentValueHandler());
 }

 @Test
 public void testCollectionTypeWithStaticTyping() {
 Class<?> listClass = ArrayList.class;
 CollectionType collectionType = CollectionType.construct(listClass, null, Object.class, new JavaType[0], Integer.TYPE);

 CollectionType withStaticTyping = collectionType.withStaticTyping();
 assertEquals(listClass, withStaticTyping.getRawClass());
 assertEquals(null, withStaticTyping.getTypeBindings());
 }

 @Test
 public void testCollectionTypeRefine() {
 Class<?> listClass = ArrayList.class;
 Class<?> linkedListClass = LinkedList.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Object.class;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemType = Integer.TYPE;
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemType);

 CollectionType refinedType = collectionType.refine(linkedListClass, bindings, superClass, new JavaType[0]);
 assertEquals(linkedListClass, refinedType.getRawClass());
 assertArrayEquals(new JavaType[0], refinedType.getSuperInterfaces());
 }
}