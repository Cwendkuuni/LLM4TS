package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

 @Test
 public void testConstruct() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = BasicTypeFactory.intType();
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType superClass = Object.class;
 JavaType[] superInts = new JavaType[0];
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemType);
 assertEquals(listClass, collectionType.getRawClass());
 assertEquals(bindings, collectionType.getTypeBindings());
 assertEquals(superClass, collectionType.getSuperClass());
 assertEquals(superInts, collectionType.getSuperInterfaces());
 assertEquals(elemType, collectionType.getContentType());
 }

 @Test
 public void testConstructDeprecated() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = BasicTypeFactory.intType();
 TypeBindings bindings = null;
 JavaType superClass = _bogusSuperClass(listClass);
 CollectionType collectionType = new CollectionType(listClass, bindings, superClass, null, elemType);
 assertEquals(listClass, collectionType.getRawClass());
 assertEquals(bindings, collectionType.getTypeBindings());
 assertEquals(superClass, collectionType.getSuperClass());
 assertEquals(new JavaType[0], collectionType.getSuperInterfaces());
 assertEquals(elemType, collectionType.getContentType());
 }

 @Test
 public void testNarrow() {
 Class<?> listClass = ArrayList.class;
 Class<?> subListClass = ArrayListSubClass.class;
 JavaType elemType = BasicTypeFactory.intType();
 CollectionType collectionType = new CollectionType(listClass, null, _bogusSuperClass(listClass), null, elemType);
 CollectionType narrowedType = collectionType.narrow(subListClass);
 assertEquals(subListClass, narrowedType.getRawClass());
 assertEquals(collectionType.getTypeBindings(), narrowedType.getTypeBindings());
 assertEquals(collectionType.getSuperClass(), narrowedType.getSuperClass());
 assertEquals(collectionType.getSuperInterfaces(), narrowedType.getSuperInterfaces());
 assertEquals(elemType, narrowedType.getContentType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNarrowDeprecated() {
 Class<?> listClass = ArrayList.class;
 Class<?> subListClass = ArrayListSubClass.class;
 JavaType elemType = BasicTypeFactory.intType();
 CollectionType collectionType = new CollectionType(listClass, null, _bogusSuperClass(listClass), null, elemType);
 collectionType.narrow(subListClass);
 }

 @Test
 public void testWithContentType() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = BasicTypeFactory.intType();
 JavaType contentType = BasicTypeFactory.longType();
 TypeBindings bindings = TypeBindings.defaultBindings();
 CollectionType collectionType = new CollectionType(listClass, bindings, _bogusSuperClass(listClass), null, elemType);
 CollectionType withContentType = collectionType.withContentType(contentType);
 assertEquals(listClass, withContentType.getRawClass());
 assertEquals(bindings, withContentType.getTypeBindings());
 assertEquals(_bogusSuperClass(listClass), withContentType.getSuperClass());
 assertEquals(new JavaType[0], withContentType.getSuperInterfaces());
 assertEquals(contentType, withContentType.getContentType());
 }

 @Test
 public void testWithTypeHandler() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = BasicTypeFactory.intType();
 Object typeHandler = new Object() {};
 TypeBindings bindings = TypeBindings.defaultBindings();
 CollectionType collectionType = new CollectionType(listClass, bindings, _bogusSuperClass(listClass), null, elemType);
 CollectionType withTypeHandler = collectionType.withTypeHandler(typeHandler);
 assertEquals(listClass, withTypeHandler.getRawClass());
 assertEquals(bindings, withTypeHandler.getTypeBindings());
 assertEquals(_bogusSuperClass(listClass), withTypeHandler.getSuperClass());
 assertEquals(new JavaType[0], withTypeHandler.getSuperInterfaces());
 assertEquals(elemType, withTypeHandler.getContentType());
 assertEquals(typeHandler, withTypeHandler.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = BasicTypeFactory.intType();
 JavaType contentType = BasicTypeFactory.longType();
 Object contentTypeHandler = new Object() {};
 TypeBindings bindings = TypeBindings.defaultBindings();
 CollectionType collectionType = new CollectionType(listClass, bindings, _bogusSuperClass(listClass), null, elemType);
 CollectionType withContentTypeHandler = collectionType.withContentTypeHandler(contentTypeHandler);
 assertEquals(listClass, withContentTypeHandler.getRawClass());
 assertEquals(bindings, withContentTypeHandler.getTypeBindings());
 assertEquals(_bogusSuperClass(listClass), withContentTypeHandler.getSuperClass());
 assertEquals(new JavaType[0], withContentTypeHandler.getSuperInterfaces());
 assertEquals(elemType, withContentTypeHandler.getContentType());
 assertEquals(contentType, withContentTypeHandler.getContentType().getType());
 assertEquals(contentTypeHandler, withContentTypeHandler.getContentTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = BasicTypeFactory.intType();
 Object valueHandler = new Object() {};
 TypeBindings bindings = TypeBindings.defaultBindings();
 CollectionType collectionType = new CollectionType(listClass, bindings, _bogusSuperClass(listClass), null, elemType);
 CollectionType withValueHandler = collectionType.withValueHandler(valueHandler);
 assertEquals(listClass, withValueHandler.getRawClass());
 assertEquals(bindings, withValueHandler.getTypeBindings());
 assertEquals(_bogusSuperClass(listClass), withValueHandler.getSuperClass());
 assertEquals(new JavaType[0], withValueHandler.getSuperInterfaces());
 assertEquals(elemType, withValueHandler.getContentType());
 assertEquals(valueHandler, withValueHandler.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = BasicTypeFactory.intType();
 JavaType contentType = BasicTypeFactory.longType();
 Object contentValueHandler = new Object() {};
 TypeBindings bindings = TypeBindings.defaultBindings();
 CollectionType collectionType = new CollectionType(listClass, bindings, _bogusSuperClass(listClass), null, elemType);
 CollectionType withContentValueHandler = collectionType.withContentValueHandler(contentValueHandler);
 assertEquals(listClass, withContentValueHandler.getRawClass());
 assertEquals(bindings, withContentValueHandler.getTypeBindings());
 assertEquals(_bogusSuperClass(listClass), withContentValueHandler.getSuperClass());
 assertEquals(new JavaType[0], withContentValueHandler.getSuperInterfaces());
 assertEquals(elemType, withContentValueHandler.getContentType());
 assertEquals(contentType, withContentValueHandler.getContentType().getType());
 assertEquals(contentValueHandler, withContentValueHandler.getContentValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = BasicTypeFactory.intType();
 CollectionType collectionType = new CollectionType(listClass, null, _bogusSuperClass(listClass), null, elemType);
 CollectionType withStaticTyping = collectionType.withStaticTyping();
 assertEquals(listClass, withStaticTyping.getRawClass());
 assertTrue(withStaticTyping.isStatic());
 }

 @Test
 public void testRefine() {
 Class<?> listClass = ArrayList.class;
 JavaType elemType = BasicTypeFactory.intType();
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType superClass = Collection.class;
 JavaType[] superInterfaces = {Collection.class};
 CollectionType collectionType = new CollectionType(listClass, bindings, _bogusSuperClass(listClass), null, elemType);
 CollectionType refinedType = collectionType.refine(listClass, bindings, superClass, superInterfaces);
 assertEquals(listClass, refinedType.getRawClass());
 assertEquals(bindings, refinedType.getTypeBindings());
 assertEquals(superClass, refinedType.getSuperClass());
 assertEquals(superInterfaces, refinedType.getSuperInterfaces());
 assertEquals(elemType, refinedType.getContentType());
 }

}