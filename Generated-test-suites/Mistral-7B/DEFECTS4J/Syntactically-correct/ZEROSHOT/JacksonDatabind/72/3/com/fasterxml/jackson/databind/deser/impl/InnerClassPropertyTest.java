package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class InnerClassPropertyTest {

 private InnerClassProperty property;
 private Object bean;
 private JsonParser jsonParser;
 private DeserializationContext context;
 private SettableBeanProperty delegate;
 private Constructor<?> constructor;
 private AnnotatedConstructor annotatedConstructor;
 private JsonDeserializer<?> deserializer;
 private PropertyName propertyName;
 private Object value;

 @Before
 public void setup() throws Exception {
 Class<?> innerClass = Mockito.mock(Class.class);
 delegate = Mockito.mock(SettableBeanProperty.class);
 constructor = Mockito.mock(Constructor.class);
 annotatedConstructor = Mockito.mock(AnnotatedConstructor.class);
 deserializer = Mockito.mock(JsonDeserializer.class);
 propertyName = Mockito.mock(PropertyName.class);
 jsonParser = Mockito.mock(JsonParser.class);
 context = Mockito.mock(DeserializationContext.class);
 bean = Mockito.mock(Object.class);

 when(constructor.newInstance(bean)).thenReturn(value);
 when(annotatedConstructor.getAnnotated()).thenReturn(constructor);
 when(annotatedConstructor.getDeclaringClass()).thenReturn(innerClass);
 when(constructor.getDeclaringClass()).thenReturn(innerClass);
 when(innerClass.getName()).thenReturn("InnerClass");
 when(context.findValueDeserializer(innerClass)).thenReturn(deserializer);
 when(context.findBeanProperty(propertyName)).thenReturn(delegate);
 when(delegate.getMember()).thenReturn(propertyName);
 when(delegate.getValueDeserializer()).thenReturn(deserializer);
 when(delegate.withValueDeserializer(deserializer)).thenReturn(delegate);
 when(delegate.withName(propertyName)).thenReturn(property);
 when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

 property = new InnerClassProperty(delegate, constructor);
 }

 @Test
 public void testConstructor() {
 assertNull(property._annotated);
 assertNull(property._valueDeserializer);
 assertNotNull(property._delegate);
 assertNotNull(property._creator);
 }

 @Test
 public void testSerialization() throws Exception {
 property = new InnerClassProperty(property, annotatedConstructor);
 verify(property, times(1)).writeReplace();
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 String valueString = "Test Value";
 property.deserializeAndSet(jsonParser, context, bean);
 verify(jsonParser, times(1)).nextToken();
 verify(jsonParser, times(1)).getValueAsString();
 verify(deserializer, times(1)).deserialize(jsonParser, context, value);
 verify(delegate, times(1)).set(bean, value);
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 property.deserializeSetAndReturn(jsonParser, context, bean);
 verify(jsonParser, times(1)).nextToken();
 verify(jsonParser, times(1)).getValueAsString();
 verify(deserializer, times(1)).deserialize(jsonParser, context, value);
 }

 @Test
 public void testSet() throws Exception {
 property.set(bean, value);
 verify(delegate, times(1)).set(bean, value);
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object returnValue = new Object();
 when(delegate.setAndReturn(bean, value)).thenReturn(returnValue);
 Object result = property.setAndReturn(bean, value);
 verify(delegate, times(1)).setAndReturn(bean, value);
 assertThat(result, is(returnValue));
 }
}