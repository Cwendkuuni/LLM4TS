package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

 @Test
 public void testConstruct() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = new JavaType[] {List.class};
 JavaType elemT = IntegerType.instance;
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 assertEquals(listClass, collectionType.getRawClass());
 assertEquals(bindings, collectionType.getTypeBindings());
 assertEquals(superClass, collectionType.getSuperClass());
 assertEquals(superInterfaces, collectionType.getSuperInterfaces());
 assertEquals(elemT, collectionType.getElementType());
 }

 @Test
 public void testConstructDeprecated() {
 Class<?> listClass = ArrayList.class;
 JavaType elemT = IntegerType.instance;
 CollectionType collectionType = CollectionType.construct(listClass, elemT);
 assertEquals(listClass, collectionType.getRawClass());
 assertNull(collectionType.getTypeBindings());
 assertNull(collectionType.getSuperClass());
 assertNull(collectionType.getSuperInterfaces());
 assertEquals(elemT, collectionType.getElementType());
 }

 @Test
 public void testRefine() {
 Class<?> linkedListClass = LinkedList.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = new JavaType[] {List.class};
 JavaType elemT = IntegerType.instance;
 CollectionType collectionType = new CollectionType(LinkedList.class, bindings);
 CollectionType refinedType = collectionType.refine(linkedListClass, bindings, superClass, superInterfaces);
 assertEquals(linkedListClass, refinedType.getRawClass());
 assertEquals(bindings, refinedType.getTypeBindings());
 assertEquals(superClass, refinedType.getSuperClass());
 assertEquals(superInterfaces, refinedType.getSuperInterfaces());
 assertEquals(elemT, refinedType.getElementType());
 }

 @Test
 public void testWithContentType() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = new JavaType[] {List.class};
 JavaType elemT = IntegerType.instance;
 JavaType contentType = LongType.instance;
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 CollectionType withContentType = collectionType.withContentType(contentType);
 assertEquals(listClass, withContentType.getRawClass());
 assertEquals(bindings, withContentType.getTypeBindings());
 assertEquals(superClass, withContentType.getSuperClass());
 assertEquals(superInterfaces, withContentType.getSuperInterfaces());
 assertEquals(contentType, withContentType.getElementType());
 }

 @Test
 public void testWithTypeHandler() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = new JavaType[] {List.class};
 JavaType elemT = IntegerType.instance;
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 Object typeHandler = new Object() {};
 CollectionType withTypeHandler = collectionType.withTypeHandler(typeHandler);
 assertEquals(listClass, withTypeHandler.getRawClass());
 assertEquals(bindings, withTypeHandler.getTypeBindings());
 assertEquals(superClass, withTypeHandler.getSuperClass());
 assertEquals(superInterfaces, withTypeHandler.getSuperInterfaces());
 assertEquals(elemT, withTypeHandler.getElementType());
 assertEquals(typeHandler, withTypeHandler.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = new JavaType[] {List.class};
 JavaType elemT = IntegerType.instance;
 JavaType contentType = LongType.instance;
 Object contentTypeHandler = new Object() {};
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 CollectionType withContentTypeHandler = collectionType.withContentTypeHandler(contentTypeHandler);
 assertEquals(listClass, withContentTypeHandler.getRawClass());
 assertEquals(bindings, withContentTypeHandler.getTypeBindings());
 assertEquals(superClass, withContentTypeHandler.getSuperClass());
 assertEquals(superInterfaces, withContentTypeHandler.getSuperInterfaces());
 assertEquals(elemT, withContentTypeHandler.getElementType());
 assertEquals(contentType, withContentTypeHandler.getContentType());
 assertEquals(contentTypeHandler, withContentTypeHandler.getContentTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = new JavaType[] {List.class};
 JavaType elemT = IntegerType.instance;
 Object valueHandler = new Object() {};
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 CollectionType withValueHandler = collectionType.withValueHandler(valueHandler);
 assertEquals(listClass, withValueHandler.getRawClass());
 assertEquals(bindings, withValueHandler.getTypeBindings());
 assertEquals(superClass, withValueHandler.getSuperClass());
 assertEquals(superInterfaces, withValueHandler.getSuperInterfaces());
 assertEquals(elemT, withValueHandler.getElementType());
 assertEquals(valueHandler, withValueHandler.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = new JavaType[] {List.class};
 JavaType elemT = IntegerType.instance;
 Object valueHandler = new Object() {};
 JavaType contentType = LongType.instance;
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 CollectionType withContentValueHandler = collectionType.withContentValueHandler(valueHandler);
 assertEquals(listClass, withContentValueHandler.getRawClass());
 assertEquals(bindings, withContentValueHandler.getTypeBindings());
 assertEquals(superClass, withContentValueHandler.getSuperClass());
 assertEquals(superInterfaces, withContentValueHandler.getSuperInterfaces());
 assertEquals(elemT, withContentValueHandler.getElementType());
 assertEquals(valueHandler, withContentValueHandler.getValueHandler());
 assertEquals(contentType, withContentValueHandler.getContentType());
 }

 @Test
 public void testWithStaticTyping() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = TypeBindings.empty();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = new JavaType[] {List.class};
 JavaType elemT = IntegerType.instance;
 CollectionType collectionType = new CollectionType(listClass, bindings);
 CollectionType withStaticTyping = collectionType.withStaticTyping();
 assertEquals(listClass, withStaticTyping.getRawClass());
 assertEquals(bindings, withStaticTyping.getTypeBindings());
 assertEquals(superClass, withStaticTyping.getSuperClass());
 assertEquals(superInterfaces, withStaticTyping.getSuperInterfaces());
 assertEquals(elemT, withStaticTyping.getElementType());
 assertTrue(withStaticTyping.isStatic());
 }
}