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
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDeserializationContextTest {

 // Test for findObjectId method
 @Test
 public void testFindObjectId() throws JsonProcessingException {
 DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerFactory());
 ObjectIdGenerator generator = new ObjectIdGenerator() {
 @Override
 public ObjectIdGenerator.IdKey key(Object id) {
 return new IdKey("test");
 }
 };
 ObjectIdResolver resolver = new SimpleObjectIdResolver(); // edge case, default resolver
 ReadableObjectId id = context.findObjectId(new Object(), generator, resolver);
 assertEquals("test", id.getId().toString());
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testFindObjectIdWithUnresolvedRef() throws JsonProcessingException {
 DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerFactory());
 ObjectIdGenerator generator = new ObjectIdGenerator() {
 @Override
 public ObjectIdGenerator.IdKey key(Object id) {
 return new IdKey("test");
 }
 };
 ObjectIdResolver resolver = new SimpleObjectIdResolver(); // edge case, default resolver
 context._objectIds = new LinkedHashMap<>(); // modify state to simulate unresolved reference
 ReadableObjectId id = context.findObjectId(new Object(), generator, resolver);
 fail("Should throw UnresolvedForwardReference exception");
 }

 // Test for copy method
 @Test
 public void testCopy() throws JsonProcessingException {
 DefaultDeserializationContext srcContext = new DefaultDeserializationContext.Impl(new StandardDeserializerFactory());
 DefaultDeserializationContext dstContext = srcContext.copy();
 assertNotSame(srcContext, dstContext);
 }

 // Test for deserializerInstance method
 @Test
 public void testDeserializerInstance() throws JsonProcessingException {
 Annotated ann = null; // edge case, null annotated
 Object deserDef = AnnotationIntrospector.findDeserializerAnnotation(null, null, null); // edge case, null deserializer definition
 DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerFactory());
 JsonDeserializer<?> deser = context.deserializerInstance(ann, deserDef);
 assertNull(deser);
 }

 @Test
 public void testDeserializerInstanceWithValidDefinition() throws JsonProcessingException {
 Annotated ann = mock(Annotated.class); // use mockito for better control
 Object deserDef = new MyJsonDeserializer();
 DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new StandardDeserializerFactory());
 JsonDeserializer<?> deser = context.deserializerInstance(ann, deserDef);
 assertSame(deserDef, deser);
 }

}

class MyJsonDeserializer implements JsonDeserializer<Object> {}