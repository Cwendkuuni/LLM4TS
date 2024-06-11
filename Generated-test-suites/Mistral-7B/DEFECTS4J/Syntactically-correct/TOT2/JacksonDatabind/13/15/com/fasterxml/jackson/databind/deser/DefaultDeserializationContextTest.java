package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
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

import java.util.*;
import java.io.Serializable;

/**
 * Test class for DefaultDeserializationContext
 */
public class DefaultDeserializationContextTest {

 private DefaultDeserializationContext context;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 context = objectMapper.getDeserializationContext();
 }

 /**
 * Test method for {@link DefaultDeserializationContext#findObjectId(Object, ObjectIdGenerator, ObjectIdResolver)}
 */
 @Test
 public void testFindObjectId() {
 Object id = new Object();
 ObjectIdGenerator<?> gen = new ObjectIdGenerator() {
 @Override
 public ObjectIdGenerator.IdKey key(Object id) {
 return new ObjectIdGenerator.IdKey();
 }
 };
 ObjectIdResolver resolver = new ObjectIdResolver() {
 @Override
 public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
 return new SimpleObjectIdResolver();
 }
 };
 Object expectedId = new Object();
 ReadableObjectId actualId = context.findObjectId(id, gen, resolver);
 assertSame(expectedId, actualId.getId());
 }

 /**
 * Test method for {@link DefaultDeserializationContext#findObjectId(Object, ObjectIdGenerator)}
 */
 @Test
 public void testFindObjectId_Deprecated() {
 Object id = new Object();
 ObjectIdGenerator<?> gen = new ObjectIdGenerator() {
 @Override
 public ObjectIdGenerator.IdKey key(Object id) {
 return new ObjectIdGenerator.IdKey();
 }
 };
 Object expectedId = new Object();
 ReadableObjectId actualId = context.findObjectId(id, gen);
 assertSame(expectedId, actualId.getId());
 }

 /**
 * Test method for {@link DefaultDeserializationContext#checkUnresolvedObjectId()}
 */
 @Test(expected = UnresolvedForwardReference.class)
 public void testCheckUnresolvedObjectId() {
 context._objectIds = new LinkedHashMap<>();
 context._objectIds.put(new ObjectIdGenerator.IdKey(), null);
 context.checkUnresolvedObjectId();
 }

 /**
 * Test method for {@link DefaultDeserializationContext#deserializerInstance(Annotated, Object)}
 */
 @Test
 public void testDeserializerInstance() {
 Annotated ann = null;
 Object deserDef = JsonDeserializer.None.class;
 JsonDeserializer<Object> actualDeserializer = context.deserializerInstance(ann, deserDef);
 assertNull(actualDeserializer);

 ann = new AnnotatedClassImpl();
 deserDef = new JsonDeserializerImpl();
 actualDeserializer = context.deserializerInstance(ann, deserDef);
 assertEquals("JsonDeserializerImpl", actualDeserializer.getClass().getName());
 }

 /**
 * Test method for {@link DefaultDeserializationContext#keyDeserializerInstance(Annotated, Object)}
 */
 @Test
 public void testKeyDeserializerInstance() {
 Annotated ann = null;
 Object deserDef = KeyDeserializer.None.class;
 KeyDeserializer actualDeserializer = context.keyDeserializerInstance(ann, deserDef);
 assertNull(actualDeserializer);

 ann = new AnnotatedClassImpl();
 deserDef = new KeyDeserializerImpl();
 actualDeserializer = context.keyDeserializerInstance(ann, deserDef);
 assertEquals("KeyDeserializerImpl", actualDeserializer.getClass().getName());
 }

 /**
 * Test method for {@link DefaultDeserializationContext#with(DeserializerFactory)}
 */
 @Test
 public void testWith() {
 DefaultDeserializationContext contextWithFactory = context.with(new DeserializerFactory() {});
 assertNotSame(context, contextWithFactory);
 }

 /**
 * Test method for {@link DefaultDeserializationContext#createInstance(DeserializationConfig, JsonParser, InjectableValues)}
 */
 @Test
 public void testCreateInstance() {
 DeserializationConfig config = null;
 JsonParser jp = null;
 InjectableValues values = null;
 DefaultDeserializationContext actualContext = context.createInstance(config, jp, values);
 assertSame(context, actualContext);
 }
}

class AnnotatedClassImpl implements Annotated {
 @Override
 public Class<?> getRawClass() {
 return JsonDeserializer.class;
 }

 @Override
 public JsonDeserializer<?> getAnnotation(Class<? extends Annotation> annotations) {
 return null;
 }

 @Override
 public JsonDeserializer<?> findAnnotation(Class<? extends Annotation> annotations) {
 return null;
 }

 @Override
 public JsonDeserializer<?> forDeserializer() {
 return null;
 }

 @Override
 public ObjectIdResolver forPropertyIdResolver() {
 return null;
 }

 @Override
 public ObjectIdGenerator<?> forIdGenerator() {
 return null;
 }

 @Override
 public ObjectIdResolver forIdResolver() {
 return null;
 }

 @Override
 public ObjectIdGenerator<?> forValueIdGenerator() {
 return null;
 }

 @Override
 public boolean isContainerType() {
 return false;
 }

 @Override
 public boolean hasAnnotation(Class<? extends Annotation> annotations) {
 return false;
 }

 @Override
 public boolean hasAnnotation(String annotationName) {
 return false;
 }

 @Override
 public boolean hasMixInAnnotation(Class<? extends Annotation> annotations) {
 return false;
 }

 @Override
 public boolean hasValueIdAnnotation(Class<? extends Annotation> annotations) {
 return false;
 }

 @Override
 public boolean isValueIdAnnotation(Class<? extends Annotation> annotations) {
 return false;
 }
}

class JsonDeserializerImpl implements JsonDeserializer<Object> {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return null;
 }
}

class KeyDeserializerImpl implements KeyDeserializer {
 @Override
 public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
 return null;
 }
}