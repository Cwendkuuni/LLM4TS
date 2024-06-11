package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

public class DefaultDeserializationContextTest {

 private DeserializerFactory deserializerFactory;
 private DeserializationConfig config;
 private JsonParser jsonParser;
 private InjectableValues injectableValues;
 private DefaultDeserializationContext context;
 private DefaultDeserializationContext srcContext;

 @Before
 public void setUp() throws Exception {
 deserializerFactory = new DeserializerFactory();
 config = new DeserializationConfig();
 jsonParser = new TestJsonParser();
 injectableValues = new TestInjectableValues();
 context = new DefaultDeserializationContext(deserializerFactory, config);
 srcContext = new DefaultDeserializationContext.Impl(context);
 }

 @Test
 public void testFindObjectId() {
 Object id = new Object();
 ObjectIdGenerator<?> gen = new StubObjectIdGenerator();
 ObjectIdResolver resolverType = new StubObjectIdResolver();

 // Test findObjectId with null id
 assertEquals(null, context.findObjectId(null, gen, resolverType));

 // Test findObjectId with valid id and resolver
 ObjectIdGenerator.IdKey key = gen.key(id);
 ReadableObjectId objectId = context.findObjectId(id, gen, resolverType);
 assertEquals(id, objectId.getObjectId());
 assertEquals(resolverType, objectId.getResolver());

 // Test findObjectId with the same id and different resolvers
 ObjectIdResolver resolverType2 = new StubObjectIdResolver2();
 ReadableObjectId objectId2 = context.findObjectId(id, gen, resolverType2);
 assertNotEquals(objectId, objectId2);
 assertEquals(resolverType2, objectId2.getResolver());

 // Test findObjectId with an existing id in the cache
 context._objectIds = new LinkedHashMap<>();
 context._objectIds.put(key, new ReadableObjectId(key));
 ObjectIdResolver resolver = new StubObjectIdResolver();
 ReadableObjectId objectId3 = context.findObjectId(id, gen, resolver);
 assertEquals(id, objectId3.getObjectId());
 assertEquals(resolver, objectId3.getResolver());
 assertEquals(objectId3, context._objectIds.get(key));
 }

 @Test
 public void testCheckUnresolvedObjectId() throws UnresolvedForwardReference {
 context._objectIds = new LinkedHashMap<>();
 context._objectIds.put(new ObjectIdKey("testKey"), new ReadableObjectId(new ObjectIdKey("testKey")));
 ObjectIdGenerator<?> gen = new StubObjectIdGenerator();
 ObjectIdResolver resolver = new StubObjectIdResolver();
 ReadableObjectId objectId = context.findObjectId(new Object(), gen, resolver);
 ReadableObjectId unresolvedObjectId = new ReadableObjectId(new ObjectIdKey("testKey"));
 unresolvedObjectId.setReferringProperties(Collections.singletonList(new Referring("testProperty", TestClass.class, new Object())));
 context._objectIds.put(new ObjectIdKey("testKey2"), unresolvedObjectId);

 try {
 context.checkUnresolvedObjectId();
 fail("Expected UnresolvedForwardReference exception");
 } catch (UnresolvedForwardReference e) {
 assertEquals("Unresolved forward references for: [testKey]", e.getMessage());
 }
 }

 @Test
 public void testDeserializerInstance() throws JsonMappingException {
 Annotated ann = createAnnotatedMock();
 Object deserDef = new JsonDeserializerTestClass();
 JsonDeserializer<?> deser = context.deserializerInstance(ann, deserDef);
 assertNotNull(deser);
 assertTrue(deser instanceof JsonDeserializer);

 deserDef = new Class<?>[] { JsonDeserializerTestClass.class };
 deser = context.deserializerInstance(ann, deserDef);
 assertNotNull(deser);
 assertTrue(deser instanceof JsonDeserializer);

 deserDef = null;
 deser = context.deserializerInstance(ann, deserDef);
 assertNull(deser);

 deserDef = new Object();
 deser = context.deserializerInstance(ann, deserDef);
 assertNull(deser);
 }

 @Test
 public void testKeyDeserializerInstance() throws JsonMappingException {
 Annotated ann = createAnnotatedMock();
 Object deserDef = new KeyDeserializerTestClass();
 KeyDeserializer deser = context.keyDeserializerInstance(ann, deserDef);
 assertNotNull(deser);
 assertTrue(deser instanceof KeyDeserializer);

 deserDef = new Class<?>[] { KeyDeserializerTestClass.class };
 deser = context.keyDeserializerInstance(ann, deserDef);
 assertNotNull(deser);
 assertTrue(deser instanceof KeyDeserializer);

 deserDef = null;
 deser = context.keyDeserializerInstance(ann, deserDef);
 assertNull(deser);

 deserDef = new Object();
 deser = context.keyDeserializerInstance(ann, deserDef);
 assertNull(deser);
 }

 @After
 public void tearDown() {
 // Clear the static serialVersionUID to reset test environment
 DefaultDeserializationContext.Impl.serialVersionUID = 0L;
 }

 private static Annotated createAnnotatedMock() {
 return mock(Annotated.class);
 }

 private static class StubObjectIdGenerator implements ObjectIdGenerator<Object> {
 @Override
 public ObjectIdGenerator.IdKey key(Object id) {
 return new ObjectIdKey("testKey");
 }
 }

 private static class StubObjectIdResolver implements ObjectIdResolver {
 @Override
 public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
 return this;
 }
 }

 private static class StubObjectIdResolver2 implements ObjectIdResolver {
 @Override
 public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
 return this;
 }
 }

 private static class ObjectIdKey {
 public ObjectIdKey(String key) {
 }
 }

 private static class TestJsonParser extends JsonParser {
 }

 private static class TestInjectableValues extends InjectableValues {
 }

 private static class JsonDeserializerTestClass {
 }

 private static class KeyDeserializerTestClass {
 }
}