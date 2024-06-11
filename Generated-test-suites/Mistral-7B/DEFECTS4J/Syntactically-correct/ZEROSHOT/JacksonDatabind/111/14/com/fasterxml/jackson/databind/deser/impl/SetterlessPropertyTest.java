package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SetterlessPropertyTest {

 private SettableBeanProperty property;
 private Object testInstance;

 @Before
 public void setup() throws Exception {
 testInstance = Mockito.spy(new TestClass());
 property = new SetterlessProperty(
 Mockito.mock(BeanPropertyDefinition.class),
 Mockito.mock(JavaType.class),
 Mockito.mock(TypeDeserializer.class),
 Mockito.mock(Annotations.class),
 Mockito.mock(AnnotatedMethod.class)
 );
 when(property.getName()).thenReturn("testProperty");
 when(property.getAnnotated()).thenReturn(Mockito.mock(AnnotatedMethod.class));
 when(property.getGetter()).thenReturn(Mockito.mock(Method.class));
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 JsonParser mockParser = mock(JsonParser.class);
 when(mockParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockParser.getValueAsString()).thenReturn("testValue");

 Object oldValue = new Object();
 when(testInstance.getClass()).thenReturn(Object.class);
 when(property.getGetter().invoke(testInstance, (Object[]) null)).thenReturn(oldValue);

 property.deserializeAndSet(mockParser, Mockito.mock(DeserializationContext.class), testInstance);

 Object newValue = "testValue";
 when(property.getValueDeserializer().deserialize(mockParser, Mockito.mock(DeserializationContext.class), new Object()))
         .thenReturn(newValue);
 when(property.getGetter().invoke(testInstance, (Object[]) null)).thenReturn(newValue);
 Assert.assertEquals(newValue, BeanUtil.findPropertyValue(testInstance, "testProperty"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() throws IOException {
 property.set(testInstance, new Object());
 }

 @Test
 public void testSetterlessPropertyWithNameChange() throws IOException {
 property = new SetterlessProperty(property, new PropertyName("newName"));
 property.deserializeAndSet(mockParser, Mockito.mock(DeserializationContext.class), testInstance);
 Assert.assertEquals("newName", BeanUtil.findPropertyName(testInstance, "newName"));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object result = property.deserializeSetAndReturn(mockParser, Mockito.mock(DeserializationContext.class), testInstance);
 Assert.assertEquals(testInstance, result);
 }

 @Test
 public void testFixAccess() {
 property.fixAccess(Mockito.mock(DeserializationConfig.class));
 }

 @Test
 public void testGetAnnotation() {
 JsonProperty jsonProperty = new JsonProperty();
 when(property.getAnnotated().getAnnotation(JsonProperty.class)).thenReturn(jsonProperty);
 Assert.assertEquals(jsonProperty, property.getAnnotation(JsonProperty.class));
 }

 @Test
 public void testGetMember() {
 AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
 when(property.getMember()).thenReturn(annotatedMethod);
 Assert.assertEquals(annotatedMethod, property.getMember());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testValueDeserializer() throws IOException {
 property.withValueDeserializer(Mockito.mock(JsonDeserializer.class));
 }

 @Test(expected = NullPointerException.class)
 public void testNullProvider() throws IOException {
 property.withNullProvider(null);
 }
}

 class TestClass {
 @JsonProperty("testProperty")
 private Object testProperty;
 }