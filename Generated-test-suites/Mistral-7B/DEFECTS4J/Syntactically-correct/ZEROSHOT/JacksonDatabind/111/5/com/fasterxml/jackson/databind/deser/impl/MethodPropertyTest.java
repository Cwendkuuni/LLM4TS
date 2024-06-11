package com.example.junit.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanProperty.MethodProperty;

public class MethodPropertyTest {

 private static Object testInstance;
 private static Method testSetter;
 private static AnnotatedMethod testAnnotated;
 private static MethodProperty testMethodProperty;
 private static JsonParser testJsonParser;
 private static DeserializationContext testContext;
 private static Object testValue;
 private static JsonDeserializer<?> testValueDeserializer;
 private static NullValueProvider testNullValueProvider;
 private static TypeDeserializer testTypeDeserializer;
 private static PropertyName testPropertyName;

 @BeforeClass
 public static void setUp() throws Exception {
 testInstance = mock(Object.class);
 testSetter = mock(Method.class);
 testAnnotated = mock(AnnotatedMethod.class);
 when(testAnnotated.getAnnotated()).thenReturn(testSetter);
 testMethodProperty = new MethodProperty(null, null, null, null, testAnnotated);
 testJsonParser = mock(JsonParser.class);
 testContext = mock(DeserializationContext.class);
 testValue = mock(Object.class);
 testValueDeserializer = mock(JsonDeserializer.class);
 testNullValueProvider = mock(NullValueProvider.class);
 testTypeDeserializer = mock(TypeDeserializer.class);
 testPropertyName = mock(PropertyName.class);
 }

 @Test
 public void testConstructorWithDefinition() throws Exception {
 MethodProperty methodProperty = new MethodProperty(null, null, null, null, testAnnotated);
 assertSame(testAnnotated, methodProperty.getAnnotated());
 assertSame(testSetter, methodProperty.getSetter());
 assertFalse(methodProperty.isSkipNulls());
 }

 @Test
 public void testCopyConstructor() throws Exception {
 MethodProperty src = new MethodProperty(null, null, null, null, testAnnotated);
 MethodProperty dest = new MethodProperty(src, testValueDeserializer, testNullValueProvider);
 assertSame(src.getAnnotated(), dest.getAnnotated());
 assertSame(src.getSetter(), dest.getSetter());
 assertFalse(dest.isSkipNulls());
 }

 @Test
 public void testNameConstructor() throws Exception {
 PropertyName newName = mock(PropertyName.class);
 MethodProperty methodProperty = new MethodProperty(testMethodProperty, newName);
 assertSame(testMethodProperty, methodProperty);
 assertSame(newName, methodProperty.getName());
 }

 @Test
 public void testJdkSerializationConstructor() throws Exception {
 MethodProperty src = new MethodProperty(testMethodProperty);
 MethodProperty dest = new MethodProperty(src, testSetter);
 assertSame(src, dest);
 assertSame(testSetter, dest.getSetter());
 assertFalse(dest.isSkipNulls());
 }

 @Test
 public void testGetAnnotation() throws Exception {
 Class<?> annotationType = mock(Annotation.class);
 when(testAnnotated.getAnnotation(annotationType)).thenReturn(newInstanceOf(annotationType));
 Annotation actual = testMethodProperty.getAnnotation(annotationType);
 assertSame(newInstanceOf(annotationType), actual);
 }

 @Test
 public void testGetMember() throws Exception {
 assertSame(testAnnotated, testMethodProperty.getMember());
 }

 @Test
 public void testSetterDeserializeAndSet() throws Exception {
 when(testSetter.invoke(testInstance, testValue)).thenReturn(testValue);
 testMethodProperty.deserializeAndSet(testJsonParser, testContext, testInstance);
 assertSame(testValue, testInstance);
 }

 @Test
 public void testSetterDeserializeAndSetWithNullValue() throws Exception {
 when(testJsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
 when(testNullValueProvider.getNullValue(testContext)).thenReturn(null);
 testMethodProperty.deserializeAndSet(testJsonParser, testContext, testInstance);
 assertNull(testInstance);
 }

 @Test
 public void testSetterDeserializeAndSetWithValueDeserializer() throws Exception {
 when(testSetter.invoke(testInstance, testValue)).thenReturn(testValue);
 when(testValueDeserializer.deserialize(testJsonParser, testContext)).thenReturn(testValue);
 testMethodProperty = new MethodProperty(testMethodProperty, testValueDeserializer, testNullValueProvider);
 testMethodProperty.deserializeAndSet(testJsonParser, testContext, testInstance);
 assertSame(testValue, testInstance);
 }

 @Test
 public void testSetterDeserializeAndSetWithTypeDeserializer() throws Exception {
 when(testSetter.invoke(testInstance, testValue)).thenReturn(testValue);
 when(testValueDeserializer.deserializeWithType(testJsonParser, testContext, testTypeDeserializer)).thenReturn(testValue);
 testMethodProperty = new MethodProperty(testMethodProperty, testValueDeserializer, testNullValueProvider, testTypeDeserializer);
 testMethodProperty.deserializeAndSet(testJsonParser, testContext, testInstance);
 assertSame(testValue, testInstance);
 }

 @Test
 public void testSetterDeserializeAndReturn() throws Exception {
 when(testSetter.invoke(testInstance, testValue)).thenReturn(testValue);
 Object actual = testMethodProperty.deserializeSetAndReturn(testJsonParser, testContext, testInstance);
 assertSame(testInstance, actual);
 }

 @Test
 public void testSetterDeserializeAndReturnWithNullValue() throws Exception {
 when(testJsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
 when(testNullValueProvider.getNullValue(testContext)).thenReturn(null);
 Object actual = testMethodProperty.deserializeSetAndReturn(testJsonParser, testContext, testInstance);
 assertSame(testInstance, actual);
 }

 @Test
 public void testSetterDeserializeAndReturnWithValueDeserializer() throws Exception {
 when(testSetter.invoke(testInstance, testValue)).thenReturn(testValue);
 when(testValueDeserializer.deserializeWithType(testJsonParser, testContext, testTypeDeserializer)).thenReturn(testValue);
 Object actual = testMethodProperty.deserializeSetAndReturn(testJsonParser, testContext, testInstance);
 assertSame(testValue, actual);
 }

 @Test
 public void testSetterDeserializeAndReturnWithNullValueAndNullProvider() throws Exception {
 when(testJsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
 when(testNullValueProvider.getNullValue(testContext)).thenReturn(null);
 Object actual = testMethodProperty.deserializeSetAndReturn(testJsonParser, testContext, testInstance);
 assertSame(testInstance, actual);
 }

 @Test
 public void testSetterSet() throws Exception {
 Object expected = testValue;
 testMethodProperty.set(testInstance, expected);
 assertSame(expected, testInstance);
 }

 @Test
 public void testSetterSetWithNullValue() throws Exception {
 testMethodProperty.set(testInstance, null);
 assertNull(testInstance);
 }

 @Test
 public void testSetterSetWithValueDeserializer() throws Exception {
 when(testJsonParser.getValueAsString()).thenReturn("testValueString");
 when(testValueDeserializer.deserialize(testJsonParser, testContext)).thenReturn(testValue);
 testMethodProperty = new MethodProperty(testMethodProperty, testValueDeserializer, testNullValueProvider);
 testMethodProperty.set(testInstance, testJsonParser);
 assertSame(testValue, testInstance);
 }

 @Test
 public void testSetterSetWithTypeDeserializer() throws Exception {
 when(testJsonParser.getValueAsString()).thenReturn("testValueString");
 when(testValueDeserializer.deserializeWithType(testJsonParser, testContext, testTypeDeserializer)).thenReturn(testValue);
 testMethodProperty = new MethodProperty(testMethodProperty, testValueDeserializer, testNullValueProvider, testTypeDeserializer);
 testMethodProperty.set(testInstance, testJsonParser);
 assertSame(testValue, testInstance);
 }

 @Test
 public void testSetterSetWithNullValueAndNullProvider() throws Exception {
 testMethodProperty.set(testInstance, null);
 assertNull(testInstance);
 }

 @Test
 public void testSetterSetWithValueDeserializerAndNullProvider() throws Exception {
 when(testJsonParser.getValueAsString()).thenReturn("testValueString");
 when(testValueDeserializer.deserialize(testJsonParser, testContext)).thenReturn(testValue);
 testMethodProperty = new MethodProperty(testMethodProperty, testValueDeserializer, testNullValueProvider);
 testMethodProperty.set(testInstance, testJsonParser);
 assertSame(testValue, testInstance);
 }

 @Test
 public void testSetterSetWithTypeDeserializerAndNullProvider() throws Exception {
 when(testJsonParser.getValueAsString()).thenReturn("testValueString");
 when(testValueDeserializer.deserializeWithType(testJsonParser, testContext, testTypeDeserializer)).thenReturn(testValue);
 testMethodProperty = new MethodProperty(testMethodProperty, testValueDeserializer, testNullValueProvider, testTypeDeserializer);
 testMethodProperty.set(testInstance, testJsonParser);
 assertSame(testValue, testInstance);
 }

}