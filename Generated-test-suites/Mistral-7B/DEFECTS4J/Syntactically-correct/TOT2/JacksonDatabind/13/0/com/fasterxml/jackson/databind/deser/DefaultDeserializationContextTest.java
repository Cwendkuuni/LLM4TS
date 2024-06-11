package com.example.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.databind.DeserializationContext.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.SimpleObjectIdResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import static org.mockito.Mockito.*;

public class DefaultDeserializationContextTest {

 private DefaultDeserializationContext context;
 private ObjectMapper mapper;
 private DeserializerFactory factory;
 private JsonParser parser;
 private InjectableValues values;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 factory = mock(DeserializerFactory.class);
 parser = mock(JsonParser.class);
 values = new InjectableValues();
 context = new DefaultDeserializationContext(factory, null);
 }

 @Test
 public void testFindObjectId() {
 Object id = new Object();
 ObjectIdGenerator<?> gen = mock(ObjectIdGenerator.class);
 ObjectIdResolver resolver = new SimpleObjectIdResolver();

 when(factory.findValueDeserializer(eq(ObjectIdGenerator.IdKey.class), any(JsonDeserializer.MusicClass.class))).thenReturn(gen);
 when(gen.key(id)).thenReturn(IdKey.ID_CLASS);

 ReadableObjectId actual = context.findObjectId(id, gen, resolver);
 IdKey expected = IdKey.ID_CLASS;
 Assert.assertSame(expected, actual.getKey());
 }

 @Test
 public void testFindObjectIdWithNullId() {
 ObjectIdGenerator<?> gen = mock(ObjectIdGenerator.class);
 ObjectIdResolver resolver = new SimpleObjectIdResolver();

 when(factory.findValueDeserializer(eq(ObjectIdGenerator.IdKey.class), any(JsonDeserializer.MusicClass.class))).thenReturn(gen);

 ReadableObjectId actual = context.findObjectId(null, gen, resolver);
 assertNull(actual);
 }

 @Test
 public void testFindObjectIdWithEmptyListOfIdResolvers() {
 ObjectIdGenerator<?> gen = mock(ObjectIdGenerator.class);
 ObjectIdResolver resolver = new SimpleObjectIdResolver();

 when(factory.findValueDeserializer(eq(ObjectIdGenerator.IdKey.class), any(JsonDeserializer.MusicClass.class))).thenReturn(gen);

 context.setObjectIdResolvers(Collections.emptyList());

 ReadableObjectId actual = context.findObjectId(new Object(), gen, resolver);
 assertNull(actual);
 }

 @Test
 public void testFindObjectIdWithMultipleIdResolvers() {
 ObjectIdGenerator<?> gen = mock(ObjectIdGenerator.class);
 ObjectIdResolver resolver1 = new SimpleObjectIdResolver();
 ObjectIdResolver resolver2 = new SimpleObjectIdResolver();

 when(factory.findValueDeserializer(eq(ObjectIdGenerator.IdKey.class), any(JsonDeserializer.MusicClass.class))).thenReturn(gen);

 context.setObjectIdResolvers(Arrays.asList(resolver1, resolver2));

 Object id = new Object();
 ReadableObjectId actual = context.findObjectId(id, gen, resolver1);
 Assert.assertNotNull(actual);

 // The first resolver should have been used
 verify(resolver1).canUseFor(eq(SimpleObjectIdResolver.class));
 verifyNoInteractions(resolver2);
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testCheckUnresolvedObjectId() {
 LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId> objectIds = new LinkedHashMap<>();
 objectIds.put(IdKey.ID_CLASS, new ReadableObjectId(IdKey.ID_CLASS));
 context.setObjectIds(objectIds);
 context.checkUnresolvedObjectId();
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializerInstanceWithNullAnnotated() {
 when(factory.findValueDeserializer(any(Object.class), any(Class.class))).thenThrow(new JsonMappingException("Test error"));

 try {
 context.deserializerInstance(null, null);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testDeserializerInstanceWithNullDeserDef() {
 when(factory.findValueDeserializer(any(Object.class), any(Class.class))).thenReturn(null);
 JsonDeserializer<?> deser = context.deserializerInstance(mock(Annotated.class), null);
 assertNull(deser);
 }

 @Test
 public void testDeserializerInstanceWithJsonDeserializer() {
 JsonDeserializer mockDeser = mock(JsonDeserializer.class);
 when(factory.findValueDeserializer(any(Object.class), any(Class.class))).thenReturn(mockDeser);
 JsonDeserializer<String> actual = context.deserializerInstance(mock(Annotated.class), mock(JsonDeserializer.class));
 assertEquals(mockDeser, actual);
 }

 @Test
 public void testDeserializerInstanceWithClass() {
 JsonDeserializer<String> deser = mock(JsonDeserializer.class);
 when(factory.findValueDeserializer(any(Class.class), eq(JsonDeserializer.class))).thenReturn(deser);
 JsonDeserializer<String> actual = context.deserializerInstance(null, JsonDeserializer.class);
 assertEquals(deser, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testDeserializerInstanceWithNonJsonDeserializerClass() {
 when(factory.findValueDeserializer(any(Class.class), eq(JsonDeserializer.class))).thenThrow(new IllegalStateException("Test error"));

 try {
 context.deserializerInstance(null, Object.class);
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testKeyDeserializerInstanceWithNullAnnotated() {
 when(factory.keyDeserializerInstance(any(Annotated.class), any(Object.class))).thenThrow(new JsonMappingException("Test error"));

 try {
 context.keyDeserializerInstance(null, null);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testKeyDeserializerInstanceWithNullDeserDef() {
 when(factory.keyDeserializerInstance(any(Annotated.class), any(Object.class))).thenReturn(null);
 KeyDeserializer actual = context.keyDeserializerInstance(mock(Annotated.class), null);
 assertNull(actual);
 }

 @Test
 public void testKeyDeserializerInstanceWithJsonDeserializer() {
 KeyDeserializer mockDeser = mock(KeyDeserializer.class);
 when(factory.keyDeserializerInstance(any(Annotated.class), any(Class.class))).thenReturn(mockDeser);
 KeyDeserializer<String> actual = context.keyDeserializerInstance(mock(Annotated.class), mock(JsonDeserializer.class));
 assertEquals(mockDeser, actual);
 }

 @Test
 public void testKeyDeserializerInstanceWithClass() {
 KeyDeserializer<String> deser = mock(KeyDeserializer.class);
 when(factory.keyDeserializerInstance(any(Annotated.class), any(Class.class))).thenReturn(deser);
 KeyDeserializer<String> actual = context.keyDeserializerInstance(null, JsonDeserializer.class);
 assertEquals(deser, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testKeyDeserializerInstanceWithNonJsonDeserializerClass() {
 when(factory.keyDeserializerInstance(any(Class.class), eq(JsonDeserializer.class))).thenThrow(new IllegalStateException("Test error"));

 try {
 context.keyDeserializerInstance(null, Object.class);
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 }
}