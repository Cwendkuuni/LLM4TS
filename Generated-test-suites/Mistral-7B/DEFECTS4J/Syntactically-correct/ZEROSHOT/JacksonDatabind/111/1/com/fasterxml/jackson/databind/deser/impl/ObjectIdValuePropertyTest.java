package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectIdReader;
import com.fasterxml.jackson.databind.ser.std.StdBasicSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class ObjectIdValuePropertyTest {

 private ObjectIdValueProperty property;
 private ObjectIdReader objectIdReader;
 private ObjectIdGenerator generator;
 private ObjectIdResolver resolver;
 private BeanPropertyDefinition idPropertyDefinition;

 @Mock
 private JsonDeserializer<?> idDeserializer;
 @Mock
 private NullValueProvider nullValueProvider;
 @Mock
 private ObjectIdGeneratorMock generatorMock;
 @Mock
 private ObjectIdResolverMock resolverMock;
 @Mock
 private JsonParser mockJsonParser;
 @Mock
 private DeserializationContext mockContext;

 @Before
 public void setUp() throws Exception {
 MockitoAnnotations.initMocks(this);
 objectIdReader = new ObjectIdReader(new NameTransformer("id"), idDeserializer, nullValueProvider, generator, resolver);
 idPropertyDefinition = new BeanPropertyDefinition(new AnnotatedMember(new AnnotationIntrospector().findProperty("id", NopAnnotationIntrospector.instance), null), new PropertyName("id"), StdBasicSerializer.instance, null, null, null, null);
 property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(idPropertyDefinition)) {
 @Override
 public ObjectIdValueProperty copy() {
 return this;
 }
 };
 }

 @Test
 public void testGetAnnotation() throws Exception {
 assertNull(property.getAnnotation(Object.class));
 }

 @Test
 public void testGetMember() throws Exception {
 assertNull(property.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 Object id = Mockito.mock(Object.class);
 Mockito.when(mockJsonParser.getValueAsString()).thenReturn("123");
 Object idInstance = Mockito.mock(Object.class);
 Mockito.when(mockContext.findObjectId(eq(id), Mockito.eq(generatorMock), Mockito.eq(resolverMock))).thenReturn(Mockito.mock(ReadableObjectId.class));
 Mockito.when(mockContext.findBeanPropertyDefinition("id")).thenReturn(idPropertyDefinition);
 Mockito.when(mockContext.findProperty("id")).thenReturn(idPropertyDefinition);
 Mockito.when(idPropertyDefinition.getBeanProperty()).thenReturn(idPropertyDefinition);
 Mockito.when(idPropertyDefinition.getMember()).thenReturn(null);
 Mockito.when(idPropertyDefinition.getSerializationVisible()).thenReturn(true);
 Mockito.when(idPropertyDefinition.getBeanMember()).thenReturn(null);

 property.deserializeAndSet(mockJsonParser, mockContext, idInstance);
 Mockito.verify(mockJsonParser, Mockito.times(1)).nextToken();
 Mockito.verify(mockJsonParser, Mockito.times(1)).getValueAsString();
 Mockito.verify(mockContext, Mockito.times(1)).findBeanPropertyDefinition("id");
 Mockito.verify(mockContext, Mockito.times(1)).findProperty("id");
 Mockito.verify(mockContext, Mockito.times(1)).findObjectId(eq(id), Mockito.eq(generatorMock), Mockito.eq(resolverMock));
 Mockito.verify(mockContext, Mockito.times(1)).bindObjectId(eq(idInstance), eq(id), Mockito.eq(generatorMock), Mockito.eq(resolverMock));
 }

 @Test
 public void testDeserializeSetAndReturnNull() throws Exception {
 Mockito.when(mockJsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

 Object idInstance = Mockito.mock(Object.class);
 Object result = property.deserializeSetAndReturn(mockJsonParser, mockContext, idInstance);
 assertNull(result);
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 Object id = Mockito.mock(Object.class);
 Object idInstance = Mockito.mock(Object.class);
 Mockito.when(mockJsonParser.getValueAsString()).thenReturn("123");
 Mockito.when(mockContext.findObjectId(eq(id), Mockito.eq(generatorMock), Mockito.eq(resolverMock))).thenReturn(Mockito.mock(ReadableObjectId.class));
 Mockito.when(mockContext.findBeanPropertyDefinition("id")).thenReturn(idPropertyDefinition);
 Mockito.when(mockContext.findProperty("id")).thenReturn(idPropertyDefinition);
 Mockito.when(idPropertyDefinition.getBeanProperty()).thenReturn(idPropertyDefinition);
 Mockito.when(idPropertyDefinition.getMember()).thenReturn(null);
 Mockito.when(idPropertyDefinition.getSerializationVisible()).thenReturn(true);
 Mockito.when(idPropertyDefinition.getBeanMember()).thenReturn(idProperty);
 Mockito.when(idProperty.getMember()).thenReturn(null);
 Mockito.when(idProperty.getName()).thenReturn("name");
 Mockito.when(idProperty.getBeanProperty()).thenReturn(idPropertyDefinition);

 Object result = property.deserializeSetAndReturn(mockJsonParser, mockContext, idInstance);
 Mockito.verify(mockJsonParser, Mockito.times(1)).nextToken();
 Mockito.verify(mockJsonParser, Mockito.times(1)).getValueAsString();
 Mockito.verify(mockContext, Mockito.times(1)).findBeanPropertyDefinition("id");
 Mockito.verify(mockContext, Mockito.times(1)).findProperty("id");
 Mockito.verify(mockContext, Mockito.times(1)).findObjectId(eq(id), Mockito.eq(generatorMock), Mockito.eq(resolverMock));
 Mockito.verify(mockContext, Mockito.times(1)).bindObjectId(eq(idInstance), eq(id), Mockito.eq(generatorMock), Mockito.eq(resolverMock));
 Mockito.verify(idProperty).setAndReturn(idInstance, id);
 assertEquals(idInstance, result);
 }

 @Test
 public void testSet() throws Exception {
 Object idInstance = Mockito.mock(Object.class);
 Mockito.when(mockContext.findBeanPropertyDefinition("id")).thenReturn(idPropertyDefinition);
 Mockito.when(idPropertyDefinition.getBeanProperty()).thenReturn(idPropertyDefinition);
 Mockito.when(idPropertyDefinition.getMember()).thenReturn(null);
 Mockito.when(idPropertyDefinition.getSerializationVisible()).thenReturn(true);
 Mockito.when(idPropertyDefinition.getBeanMember()).thenReturn(idProperty);
 Mockito.when(idProperty.getMember()).thenReturn(null);
 Mockito.when(idProperty.getName()).thenReturn("name");
 Mockito.when(idProperty.getBeanProperty()).thenReturn(idPropertyDefinition);

 property.set(idInstance, id);
 Mockito.verify(idProperty).setAndReturn(idInstance, id);
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object idInstance = Mockito.mock(Object.class);
 Object result = Mockito.mock(Object.class);
 Mockito.when(mockContext.findBeanPropertyDefinition("id")).thenReturn(idPropertyDefinition);
 Mockito.when(idPropertyDefinition.getBeanProperty()).thenReturn(idPropertyDefinition);
 Mockito.when(idPropertyDefinition.getMember()).thenReturn(null);
 Mockito.when(idPropertyDefinition.getSerializationVisible()).thenReturn(true);
 Mockito.when(idPropertyDefinition.getBeanMember()).thenReturn(idProperty);
 Mockito.when(idProperty.getMember()).thenReturn(null);
 Mockito.when(idProperty.getName()).thenReturn("name");
 Mockito.when(idProperty.getBeanProperty()).thenReturn(idPropertyDefinition);
 Mockito.when(idProperty.setAndReturn(idInstance, Mockito.anyObject())).thenReturn(result);

 Object result2 = property.setAndReturn(idInstance, id);
 assertEquals(result, result2);
 }

 @Test
 public void testWithName() throws Exception {
 PropertyName newName = Mockito.mock(PropertyName.class);
 ObjectIdValueProperty newProperty = property.withName(newName);
 assertEquals(newName, newProperty.getName());
 }

 @Test
 public void testWithValueDeserializer() throws Exception {
 JsonDeserializer<?> deserializer = Mockito.mock(JsonDeserializer.class);
 ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);
 assertEquals(deserializer, newProperty.getValueDeserializer());
 }

 @Test
 public void testWithNullProvider() throws Exception {
 NullValueProvider newNullProvider = Mockito.mock(NullValueProvider.class);
 ObjectIdValueProperty newProperty = property.withNullProvider(newNullProvider);
 assertEquals(newNullProvider, newProperty.getNullValueProvider());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithValueDeserializerWhenDeserializerIsSame() throws Exception {
 property.withValueDeserializer(property.getValueDeserializer());
 }

 @Test
 public void testWithNullProviderWhenNullProviderIsSame() throws Exception {
 property.withNullProvider(property.getNullValueProvider());
 }

 private ArgumentMatcher<JsonParser> jsonParserArgumentMatcher() {
 return new ArgumentMatcher<JsonParser>() {
 @Override
 public boolean matches(JsonParser jsonParser) {
 return mockJsonParser == jsonParser;
 }
 };
 }

 private ArgumentMatcher<JsonDeserializer<?>> deserializerArgumentMatcher() {
 return new ArgumentMatcher<JsonDeserializer<?>>() {
 @Override
 public boolean matches(JsonDeserializer<?> deserializer) {
 return idDeserializer == deserializer;
 }
 };
 }
}