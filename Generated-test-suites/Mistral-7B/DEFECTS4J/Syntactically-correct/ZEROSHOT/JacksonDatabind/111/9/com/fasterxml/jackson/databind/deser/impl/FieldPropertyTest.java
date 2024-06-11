package com.example.junit;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;

public class FieldPropertyTest {

 private static class TestContext extends DeserializationContext {
 @Override
 public ObjectInstanceFinder defaultInstanceFinder() {
 return mock(ObjectInstanceFinder.class);
 }

 @Override
 public JavaType typeDeserializerForProperty(BeanProperty property) {
 throw new UnsupportedOperationException();
 }
 }

 @org.junit.Test
 public void testFieldProperty() throws IOException {
 // Given
 Object instance = new Object();
 Field testField = instance.getClass().getDeclaredField("testField");
 testField.setAccessible(true);
 BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
 AnnotatedField annotatedField = mock(AnnotatedField.class);
 when(annotatedField.getAnnotated()).thenReturn(testField);
 JavaType javaType = mock(JavaType.class);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 Annotations contextAnnotations = mock(Annotations.class);
 Annotation testAnnotation = mock(Annotation.class);
 when(annotatedField.getAnnotation(testAnnotation.getClass())).thenReturn(testAnnotation);

 FieldProperty fieldProperty = new FieldProperty(propDef, javaType, typeDeserializer, contextAnnotations, annotatedField);

 // When
 Object value = fieldProperty.deserializeAndSet(mock(JsonParser.class), new TestContext(), instance);

 // Then
 assertNotNull(value);
 verify(testField).set(instance, value);

 // Given - null value
 when(mock(JsonParser.class).hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

 // When
 value = fieldProperty.deserializeAndSet(mock(JsonParser.class), new TestContext(), instance);

 // Then
 assertNull(value);
 verifyNoMoreInteractions(testField);

 // Given - empty string
 when(mock(JsonParser.class).getValueAsString()).thenReturn("");

 // When
 value = fieldProperty.deserializeAndSet(mock(JsonParser.class), new TestContext(), instance);

 // Then
 assertNull(value);
 verifyNoMoreInteractions(testField);

 // Given - non-empty string
 when(mock(JsonParser.class).getValueAsString()).thenReturn("test");

 // When
 value = fieldProperty.deserializeAndSet(mock(JsonParser.class), new TestContext(), instance);

 // Then
 assertNotNull(value);
 verify(testField).set(instance, value);
 }

 @org.junit.Test
 public void testSetAndReturn() throws IOException {
 // Given
 Object instance = new Object();
 Field testField = instance.getClass().getDeclaredField("testField");
 testField.setAccessible(true);
 BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
 AnnotatedField annotatedField = mock(AnnotatedField.class);
 when(annotatedField.getAnnotated()).thenReturn(testField);
 JavaType javaType = mock(JavaType.class);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 Annotations contextAnnotations = mock(Annotations.class);
 Annotation testAnnotation = mock(Annotation.class);
 when(annotatedField.getAnnotation(testAnnotation.getClass())).thenReturn(testAnnotation);
 String testValue = "testValue";
 FieldProperty fieldProperty = new FieldProperty(propDef, javaType, typeDeserializer, contextAnnotations, annotatedField);

 // When
 Object result = fieldProperty.setAndReturn(instance, testValue);

 // Then
 assertSame(instance, result);
 assertNotNull(testField.get(instance));
 assertSame(testValue, testField.get(instance));
 }

 @org.junit.Test
 public void testDeserializeSetAndReturn() throws IOException {
 // Given
 Object instance = new Object();
 Field testField = instance.getClass().getDeclaredField("testField");
 testField.setAccessible(true);
 BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
 AnnotatedField annotatedField = mock(AnnotatedField.class);
 when(annotatedField.getAnnotated()).thenReturn(testField);
 JavaType javaType = mock(JavaType.class);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 Annotations contextAnnotations = mock(Annotations.class);
 Annotation testAnnotation = mock(Annotation.class);
 when(annotatedField.getAnnotation(testAnnotation.class)).thenReturn(testAnnotation);
 String testValue = "testValue";
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getValueAsString()).thenReturn(testValue);
 when(jsonParser.hasToken(JsonToken.VALUE_STRING)).thenReturn(true);
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getText()).thenReturn(testValue);

 FieldProperty fieldProperty = new FieldProperty(propDef, javaType, typeDeserializer, contextAnnotations, annotatedField);

 // When
 Object result = fieldProperty.deserializeSetAndReturn(jsonParser, new TestContext(), instance);

 // Then
 assertSame(instance, result);
 assertNotNull(testField.get(instance));
 assertSame(testValue, testField.get(instance));
 }

 @org.junit.Test
 public void testFixAccess() {
 // Given
 Object instance = new Object();
 Field testField = instance.getClass().getDeclaredField("testField");
 testField.setAccessible(true);
 BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
 AnnotatedField annotatedField = mock(AnnotatedField.class);
 when(annotatedField.getAnnotated()).thenReturn(testField);
 FieldProperty fieldProperty = new FieldProperty(propDef, null, null, null, annotatedField);
 DeserializationConfig config = mock(DeserializationConfig.class);

 // When
 fieldProperty.fixAccess(config);

 // Then
 verify(testField).SetAccessible(true);
 }
}