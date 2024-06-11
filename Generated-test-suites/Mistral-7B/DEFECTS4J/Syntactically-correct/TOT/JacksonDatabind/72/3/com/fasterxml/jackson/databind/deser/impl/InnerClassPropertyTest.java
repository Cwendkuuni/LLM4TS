package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.InnerClassProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.json.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class InnerClassPropertyTest {

 private InnerClassProperty property;
 private Object bean;
 private JsonDeserializer<?> valueDeserializer;
 private Object value;
 private JsonParser jsonParser;
 private DeserializationContext deserializationContext;
 private AnnotatedConstructor annotatedConstructor;
 private SettableBeanProperty delegate;

 @Before
 public void setup() throws Exception {
 Class<?> innerClass = new Object() {}.getClass();
 Class<?> outerClass = new Object() {
 public Inner inner = new Inner();
 };
 class Inner {
 public InnerInner innerInner;
 }
 class InnerInner {
 }
 DELEGATE_FIELD = "innerInner";
 valueDeserializer = mock(JsonDeserializer.class);
 jsonParser = mock(JsonParser.class);
 deserializationContext = mock(DeserializationContext.class);
 annotatedConstructor = mock(AnnotatedConstructor.class);
 bean = mock(outerClass.getClass());
 delegate = mock(SettableBeanProperty.class);
 property = new InnerClassProperty(delegate, annotatedConstructor);
 }

 @Test
 public void testDefaultConstructor() throws Exception {
 // Test when deserializing without any constructor or valueDeserializer
 when(jsonParser.getCurrentName()).thenReturn(DELEGATE_FIELD);
 when(jsonParser.getValueAsToken()).thenReturn(null);
 when(deserializationContext.findValueDeserializer(innerClass)).thenReturn(null);
 when(annotatedConstructor.getAnnotated()).thenReturn(null);
 property.deserializeAndSet(jsonParser, deserializationContext, bean);
 verify(delegate).set(bean, null);
 }

 @Test
 public void testValueDeserializer() throws Exception {
 // Test when deserializing with a valueDeserializer
 when(jsonParser.getCurrentName()).thenReturn(DELEGATE_FIELD);
 when(jsonParser.getValueAsToken()).thenReturn(jsonParserMock("{\"innerInner\":1}"));
 when(deserializationContext.findValueDeserializer(innerClass)).thenReturn(valueDeserializer);
 when(valueDeserializer.deserialize(jsonParser, deserializationContext, anyObject())).thenReturn(value);
 property.deserializeAndSet(jsonParser, deserializationContext, bean);
 verify(delegate).set(bean, value);
 }

 @Test
 public void testValueDeserializerWithType() throws Exception {
 // Test when deserializing with a valueDeserializer and a type
 when(jsonParser.getCurrentName()).thenReturn(DELEGATE_FIELD);
 when(jsonParser.getValueAsToken()).thenReturn(jsonParserMock("{\"innerInner\":{\"@class\":\"com.fasterxml.jackson.databind.deser.impl.InnerInner\"}}"));
 when(deserializationContext.findValueDeserializer(innerClass)).thenReturn(valueDeserializer);
 when(valueDeserializer.deserializeWithType(jsonParser, deserializationContext, eq(InnerInner.class))).thenReturn(value);
 property.deserializeAndSet(jsonParser, deserializationContext, bean);
 verify(delegate).set(bean, value);
 }

 @Test
 public void testNoCreator() throws Exception {
 // Test when deserializing with a missing constructor
 when(jsonParser.getCurrentName()).thenReturn(DELEGATE_FIELD);
 when(jsonParser.getValueAsToken()).thenReturn(jsonParserMock("{\"innerInner\":1}"));
 when(deserializationContext.findValueDeserializer(innerClass)).thenReturn(null);
 when(annotatedConstructor.getAnnotated()).thenReturn(null);
 property = new InnerClassProperty(delegate, null);
 property.deserializeAndSet(jsonParser, deserializationContext, bean);
 verify(jsonParser).reportError(anyString());
 }

 @Test
 public void testCreatorWithNullValue() throws Exception {
 // Test when deserializing with a constructor and a null value
 when(jsonParser.getCurrentName()).thenReturn(DELEGATE_FIELD);
 when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);
 property = new InnerClassProperty(delegate, annotatedConstructor);
 property.deserializeAndSet(jsonParser, deserializationContext, bean);
 verify(jsonParser).reportError(anyString());
 }

 @Test
 public void testCreatorWithNonInstanceException() throws Exception {
 // Test when deserializing with a constructor and an exception
 doThrow(new InstantiationException("Cannot instantiate class")).when(annotatedConstructor).newInstance(bean);
 property = new InnerClassProperty(delegate, annotatedConstructor);
 property.deserializeAndSet(jsonParser, deserializationContext, bean);
 verify(deserializationContext).handleUnexpectedValue(eq(InnerClassProperty.class), any(), anyString());
 }

 @Test
 public void testReadResolve() throws Exception {
 property = (InnerClassProperty) property.readResolve();
 verify(property).<InnerClassProperty>constructor(delegate, annotatedConstructor);
 }

 @Test
 public void testWriteReplace() throws Exception {
 property = property.writeReplace();
 verify(property).<InnerClassProperty>constructor(property, annotatedConstructor);
 }

 private JsonParser jsonParserMock(String jsonString) throws IOException {
 JavaType type = new TypeFactory().constructType(InnerInner.class);
 ObjectReader objectReader = new ObjectMapper().readerForType(type);
 JsonParser jsonParser = objectReader.getParser();
 jsonParser.readValueAs(String.class, jsonString);
 return jsonParser;
 }
}