package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

/**
 * Test class for DefaultDeserializationContext.
 */
public class DefaultDeserializationContextTest {

 private DefaultDeserializationContext context;
 private HandlerInstantiator handlerInst;

 @Before
 public void setup() {
 context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());
 handlerInst = context.getConfig().getHandlerInstantiator();
 }

 /**
 * Test constructor with null factory and cache.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testNullFactory() {
 context = new DefaultDeserializationContext(null, null);
 }

 /**
 * Test constructor with null factory.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testNullCache() {
 context = new DefaultDeserializationContext(new StandardDeserializerProvider(), null);
 }

 /**
 * Test constructor with valid factory and cache.
 */
 @Test
 public void testValidConstruction() {
 context = new DefaultDeserializationContext(new StandardDeserializerProvider(), new SoftDeserializerCache());
 assertNotNull(context);
 }

 /**
 * Test copy method.
 */
 @Test
 public void testCopy() throws Exception {
 DefaultDeserializationContext src = context;
 DefaultDeserializationContext dest = src.copy();
 assertNotSame(src, dest);
 assertEquals(src, dest);
 }

 /**
 * Test findObjectId method with key and valid resolver.
 */
 @Test
 public void testFindObjectIdValidResolver() throws Exception {
 Object id = new Object();
 ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
 @Override
 public ObjectIdGenerator.IdKey key(Object id) {
 return new IdKey[]{IdKey.STD, IdKey.INT};
 }
 };
 ObjectIdResolver resolver = new ObjectIdResolver() {
 @Override
 public ObjectId idFromObject(Object id) {
 return new ReadableObjectId(idGenerator.key(id), null);
 }
 };
 ReadableObjectId result = context.findObjectId(id, idGenerator, resolver);
 assertNotNull(result);
 }

 /**
 * Test findObjectId method with key and null resolver.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testFindObjectIdNullResolver() {
 Object id = new Object();
 ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
 @Override
 public ObjectIdGenerator.IdKey key(Object id) {
 return new IdKey[]{IdKey.STD, IdKey.INT};
 }
 };
 ObjectIdResolver resolver = null;
 ReadableObjectId result = context.findObjectId(id, idGenerator, resolver);
 fail("Expected IllegalArgumentException");
 }

 /**
 * Test findObjectId method with key and no resolver.
 */
 @Test(expected = IllegalStateException.class)
 public void testFindObjectIdNoResolver() throws Exception {
 Object id = new Object();
 ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
 @Override
 public ObjectIdGenerator.IdKey key(Object id) {
 return new IdKey[]{IdKey.STD, IdKey.INT};
 }
 };
 ReadableObjectId result = context.findObjectId(id, idGenerator);
 fail("Expected IllegalStateException");
 }

 /**
 * Test findObjectId method with null id.
 */
 @Test
 public void testFindObjectIdNullId() {
 ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
 @Override
 public ObjectIdGenerator.IdKey key(Object id) {
 return new IdKey[]{IdKey.STD, IdKey.INT};
 }
 };
 ReadableObjectId result = context.findObjectId(null, idGenerator);
 assertNull(result);
 }

 /**
 * Test checkUnresolvedObjectId method with no unresolved ids.
 */
 @Test
 public void testCheckUnresolvedObjectIdNoUnresolvedIds() throws Exception {
 context._objectIds = new HashMap<>();
 context.checkUnresolvedObjectId();
 }

 /**
 * Test checkUnresolvedObjectId method with unresolved ids.
 */
 @Test(expected = UnresolvedForwardReference.class)
 public void testCheckUnresolvedObjectIdUnresolvedIds() throws Exception {
 IdKey idKey1 = new IdKey[]{IdKey.STD, IdKey.INT};
 ObjectIdGenerator idGenerator1 = new ObjectIdGenerator() {
 @Override
 public ObjectIdGenerator.IdKey key(Object id) {
 return idKey1;
 }
 };
 ReadableObjectId id1 = new ReadableObjectId(idKey1, null);
 id1.addReferringProperty("property1", String.class, new Location());

 IdKey idKey2 = new IdKey[]{IdKey.STD, IdKey.LONG};
 ObjectIdGenerator idGenerator2 = new ObjectIdGenerator() {
 @Override
 public ObjectIdGenerator.IdKey key(Object id) {
 return idKey2;
 }
 };
 ReadableObjectId id2 = new ReadableObjectId(idKey2, null);
 id2.addReferringProperty("property2", Long.class, new Location());

 context._objectIds = new HashMap<>();
 context._objectIds.put(idKey1, id1);
 context._objectIds.put(idKey2, id2);
 context.checkUnresolvedObjectId();
 }

 /**
 * Test deserializerInstance method.
 */
 @Test
 public void testDeserializerInstance() throws Exception {
 Annotated annotated = AnnotatedClassTest.createAnnotated("class TestClass { @JsonDeserialize TestDeserializer; }");
 JsonDeserializer<Object> deserializer = context.deserializerInstance(annotated, TestDeserializer.class);
 assertNotNull(deserializer);
 assertSame(TestDeserializer.class, deserializer.getClass());
 }

 /**
 * Test keyDeserializerInstance method.
 */
 @Test
 public void testKeyDeserializerInstance() throws Exception {
 Annotated annotated = AnnotatedClassTest.createAnnotated("class TestClass { @JsonDeserialize KeyTestDeserializer; }");
 KeyDeserializer deserializer = context.keyDeserializerInstance(annotated, KeyTestDeserializer.class);
 assertNotNull(deserializer);
 assertSame(KeyTestDeserializer.class, deserializer.getClass());
 }

}