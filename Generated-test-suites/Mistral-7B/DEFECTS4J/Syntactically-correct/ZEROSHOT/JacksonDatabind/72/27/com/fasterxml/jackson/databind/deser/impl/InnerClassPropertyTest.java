package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.JsonTestUtils;

import org.junit.Before;
import org.junit.Test;

public class InnerClassPropertyTest {

 private SettableBeanProperty delegate;
 private Constructor<?> constructor;
 private InnerClassProperty innerClassProperty;
 private AnnotatedConstructor annotated;
 private Object bean;
 private JsonDeserializer<?> deserializer;
 private JsonParser jsonParser;
 private Object value;

 @Before
 public void setUp() throws Exception {
 Class<?> innerClassType = new Object() {}.getClass().getEnclosingClass();
 delegate = mock(SettableBeanProperty.class);
 constructor = mock(Constructor.class, innerClassType.getDeclaredConstructors()[0].getParameterTypes());
 annotated = mock(AnnotatedConstructor.class, innerClassType);
 innerClassProperty = new InnerClassProperty(delegate, constructor);
 bean = new Object();
 deserializer = mock(JsonDeserializer.class);
 jsonParser = mock(JsonParser.class);
 value = new Object();
 }

 @Test
 public void testInnerClassPropertyConstruction() {
 assertSame(delegate, innerClassProperty._delegate);
 assertSame(constructor, innerClassProperty._creator);
 }

 @Test
 public void testInnerClassPropertyWithAnnotatedConstructor() {
 innerClassProperty = new InnerClassProperty(innerClassProperty, annotated);
 assertSame(innerClassProperty._delegate, delegate);
 assertNull(innerClassProperty._creator);
 assertSame(annotated, innerClassProperty._annotated);
 }

 @Test
 public void testInnerClassPropertyWithJsonDeserializer() {
 innerClassProperty = new InnerClassProperty(innerClassProperty, deserializer);
 assertSame(innerClassProperty._delegate, delegate);
 assertSame(constructor, innerClassProperty._creator);
 assertSame(deserializer, innerClassProperty._valueDeserializer);
 }

 @Test
 public void testInnerClassPropertyWithPropertyName() {
 PropertyName newName = PropertyName.construct("newName");
 innerClassProperty = new InnerClassProperty(innerClassProperty, newName);
 assertSame(innerClassProperty._delegate, delegate);
 assertSame(constructor, innerClassProperty._creator);
 assertSame(newName, innerClassProperty.getName());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 when(constructor.newInstance(bean)).thenReturn(value);
 when(deserializer.deserialize(jsonParser, JsonTestUtils.createContext(), value)).thenReturn(value);

 innerClassProperty.deserializeAndSet(jsonParser, JsonTestUtils.createContext(), bean);
 verify(delegate).set(bean, value);
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 when(constructor.newInstance(bean)).thenReturn(value);
 when(deserializer.deserialize(jsonParser, JsonTestUtils.createContext(), value)).thenReturn(value);

 Object result = innerClassProperty.deserializeSetAndReturn(jsonParser, JsonTestUtils.createContext(), bean);
 assertSame(value, result);
 }

 @Test
 public void testSet() throws Exception {
 innerClassProperty.set(bean, value);
 verify(delegate).set(bean, value);
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object result = innerClassProperty.setAndReturn(bean, value);
 assertSame(value, result);
 }

 @Test
 public void testReadResolve() throws Exception {
 Object result = innerClassProperty.readResolve();
 assertSame(innerClassProperty, result);
 }

 @Test
 public void testWriteReplace() throws Exception {
 Object result = innerClassProperty.writeReplace();
 assertSame(innerClassProperty, result);
 }
}