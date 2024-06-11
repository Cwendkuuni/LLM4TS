package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.MethodProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanPropertyUtil;

public class MethodPropertyTest {

 private static class TestClass {
 private int value;

 @JsonSetter(name = "testProperty")
 public void setTestProperty(int value) {
 this.value = value;
 }
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testMethodProperty() throws IOException {
 // Given
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getCurrentName()).thenReturn("testProperty");
 when(jsonParser.hasToken(JsonToken.VALUE_NUMBER_INT)).thenReturn(true);
 when(jsonParser.nextIntValue()).thenReturn(42);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectWriter objectWriter = objectMapper.writer();
 TestClass testClass = new TestClass();
 PropertyName propertyName = PropertyName.construct("testProperty");
 MethodProperty methodProperty = new MethodProperty(
 new BeanPropertyDefinition(propertyName, testClass.getClass(), null, null, null),
 ObjectTypeTokens.INT_TYPE, null, Annotations.empty(),
 new AnnotatedMethod(TestClass.class.getMethod("setTestProperty"), TestClass.class)
 );
 DeserializationContext deserializationContext = mock(DeserializationContext.class);
 JsonDeserializer<Integer> jsonDeserializer = mock(JsonDeserializer.class);
 when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(42);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 NullValueProvider nullValueProvider = mock(NullValueProvider.class);
 Object instance = testClass;

 // When
 methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

 // Then
 assertEquals(42, testClass.value);

 // Cleanup
 verify(jsonParser).nextToken();
 verify(jsonParser).nextIntValue();
 verify(jsonParser).nextToken();
 verifyNoMoreInteractions(jsonParser);
 verify(deserializationContext).findContextualValueDeserializer(Integer.class);
 verify(jsonDeserializer).deserialize(jsonParser, deserializationContext);
 verify(jsonDeserializer).deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
 verify(instance).setValue(eq(42));
 }

 @org.junit.Test
 public void testMethodPropertyWithNullValue() throws IOException {
 // Given
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getCurrentName()).thenReturn("testProperty");
 when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectWriter objectWriter = objectMapper.writer();
 TestClass testClass = new TestClass();
 PropertyName propertyName = PropertyName.construct("testProperty");
 MethodProperty methodProperty = new MethodProperty(
 new BeanPropertyDefinition(propertyName, testClass.getClass(), null, null, null),
 ObjectTypeTokens.INT_TYPE, null, Annotations.empty(),
 new AnnotatedMethod(TestClass.class.getMethod("setTestProperty"), TestClass.class)
 );
 DeserializationContext deserializationContext = mock(DeserializationContext.class);
 NullValueProvider nullValueProvider = mock(NullValueProvider.class);
 Object instance = testClass;

 // When
 methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

 // Then
 assertNull(testClass.value);

 // Cleanup
 verify(jsonParser).nextToken();
 verify(jsonParser).nextToken();
 verifyNoMoreInteractions(jsonParser);
 verify(deserializationContext).findContextualNullValueProvider();
 verify(nullValueProvider).getNullValue(deserializationContext);
 verify(instance).setValue(null);
 }

 @org.junit.Test
 public void testMethodPropertyWithTypeDeserializer() throws IOException {
 // Given
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getCurrentName()).thenReturn("testProperty");
 when(jsonParser.hasToken(JsonToken.VALUE_STRING)).thenReturn(true);
 when(jsonParser.getValueAsString()).thenReturn("ABC");
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectWriter objectWriter = objectMapper.writer();
 TestClass testClass = new TestClass();
 PropertyName propertyName = PropertyName.construct("testProperty");
 MethodProperty methodProperty = new MethodProperty(
 new BeanPropertyDefinition(propertyName, testClass.getClass(), null, null, null),
 ObjectTypeTokens.STRING_TYPE,
 new SimpleTypeDeserializer(String.class),
 Annotations.empty(),
 new AnnotatedMethod(TestClass.class.getMethod("setTestProperty"), TestClass.class)
 );
 DeserializationContext deserializationContext = mock(DeserializationContext.class);
 JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);
 when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("ABC");
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 when(typeDeserializer.deserialize(jsonParser, deserializationContext, String.class)).thenReturn("XYZ");
 Object instance = testClass;

 // When
 String result = (String) methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

 // Then
 assertEquals("XYZ", result);
 assertEquals("ABC", testClass.value);

 // Cleanup
 verify(jsonParser).nextToken();
 verify(jsonParser).getValueAsString();
 verify(jsonParser).nextToken();
 verify(jsonParser).nextToken();
 verifyNoMoreInteractions(jsonParser);
 verify(deserializationContext).findContextualValueDeserializer(String.class);
 verify(jsonDeserializer).deserialize(jsonParser, deserializationContext);
 verify(jsonDeserializer).deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
 verify(instance).setValue(eq("XYZ"));
 }

 @org.junit.Test
 public void testMethodPropertyWithValueDeserializer() throws IOException {
 // Given
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getCurrentName()).thenReturn("testProperty");
 when(jsonParser.hasToken(JsonToken.VALUE_NUMBER_INT)).thenReturn(true);
 when(jsonParser.nextIntValue()).thenReturn(42);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectWriter objectWriter = objectMapper.writer();
 TestClass testClass = new TestClass();
 PropertyName propertyName = PropertyName.construct("testProperty");
 MethodProperty methodProperty = new MethodProperty(
 new BeanPropertyDefinition(propertyName, testClass.getClass(), null, null, null),
 ObjectTypeTokens.INT_TYPE,
 null,
 Annotations.empty(),
 new AnnotatedMethod(TestClass.class.getMethod("setTestProperty"), TestClass.class)
 );
 DeserializationContext deserializationContext = mock(DeserializationContext.class);
 JsonDeserializer<Integer> jsonDeserializer = mock(JsonDeserializer.class);
 when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(42);
 Object instance = testClass;

 // When
 methodProperty = new MethodProperty(methodProperty, jsonDeserializer, nullValueProvider());

 // Then
 assertEquals(methodProperty.getClass(), MethodProperty.class);
 assertEquals(methodProperty.getPropertyName(), propertyName);
 assertEquals(methodProperty.getBeanType(), testClass.getClass());
 assertEquals(methodProperty.getDeserializer(), jsonDeserializer);
 assertEquals(methodProperty.getType(), ObjectTypeTokens.INT_TYPE);
 assertEquals(methodProperty.getNullProvider(), nullValueProvider());
 assertEquals(methodProperty.getAnnotated(), methodProperty._annotated);
 assertEquals(methodProperty.getSetter(), methodProperty._setter);
 assertEquals(methodProperty.isSkipNulls(), false);

 // Cleanup
 verify(jsonParser).nextToken();
 verify(jsonParser).nextIntValue();
 verify(jsonParser).nextToken();
 verifyNoMoreInteractions(jsonParser);
 verify(deserializationContext).findContextualValueDeserializer(Integer.class);
 verify(jsonDeserializer).deserialize(jsonParser, deserializationContext);
 verify(jsonDeserializer).deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
 verify(instance).setValue(eq(42));
 }

 @org.junit.Test
 public void testMethodPropertyWithNameChange() throws IOException {
 // Given
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getCurrentName()).thenReturn("oldPropertyName");
 when(jsonParser.hasToken(JsonToken.VALUE_NUMBER_INT)).thenReturn(true);
 when(jsonParser.nextIntValue()).thenReturn(42);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectWriter objectWriter = objectMapper.writer();
 TestClass testClass = new TestClass();
 PropertyName oldPropertyName = PropertyName.construct("oldPropertyName");
 PropertyName newPropertyName = PropertyName.construct("newPropertyName");
 MethodProperty methodProperty = new MethodProperty(
 new BeanPropertyDefinition(oldPropertyName, testClass.getClass(), null, null, null),
 ObjectTypeTokens.INT_TYPE,
 null,
 Annotations.empty(),
 new AnnotatedMethod(TestClass.class.getMethod("setTestProperty"), TestClass.class)
 );
 DeserializationContext deserializationContext = mock(DeserializationContext.class);
 Object instance = testClass;

 // When
 methodProperty = new MethodProperty(methodProperty, newPropertyName);

 // Then
 assertEquals(methodProperty.getPropertyName(), newPropertyName);
 assertEquals(methodProperty.getAnnotated(), methodProperty._annotated);
 assertEquals(methodProperty.getSetter(), methodProperty._setter);
 assertEquals(methodProperty.isSkipNulls(), false);

 // Cleanup
 verify(jsonParser).nextToken();
 verify(jsonParser).nextIntValue();
 verify(jsonParser).nextToken();
 verifyNoMoreInteractions(jsonParser);
 verifyNoInteractions(deserializationContext);
 }

 @org.junit.Test
 public void testMethodPropertyWithSetterChange() throws IOException {
 // Given
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getCurrentName()).thenReturn("testProperty");
 when(jsonParser.hasToken(JsonToken.VALUE_NUMBER_INT)).thenReturn(true);
 when(jsonParser.nextIntValue()).thenReturn(42);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectWriter objectWriter = objectMapper.writer();
 TestClass testClass = new TestClass();
 PropertyName propertyName = PropertyName.construct("testProperty");
 MethodProperty methodProperty = new MethodProperty(
 new BeanPropertyDefinition(propertyName, testClass.getClass(), null, null, null),
 ObjectTypeTokens.INT_TYPE,
 null,
 Annotations.empty(),
 new AnnotatedMethod(TestClass.class.getMethod("setTestPropertyElse"), TestClass.class)
 );
 DeserializationContext deserializationContext = mock(DeserializationContext.class);
 Object instance = testClass;

 // When
 methodProperty = new MethodProperty(methodProperty, jsonDeserializer(), nullValueProvider());

 // Then
 assertEquals(methodProperty.getPropertyName(), propertyName);
 assertEquals(methodProperty.getBeanType(), testClass.getClass());
 assertEquals(methodProperty.getDeserializer(), jsonDeserializer());
 assertEquals(methodProperty.getType(), ObjectTypeTokens.INT_TYPE);
 assertEquals(methodProperty.getNullProvider(), nullValueProvider());
 assertEquals(methodProperty.getAnnotated(), methodProperty._annotated);
 assertEquals(methodProperty.getSetter(), methodProperty._setter.getDeclaringClass().getMethod("setTestPropertyElse", int.class));
 assertEquals(methodProperty.isSkipNulls(), false);

 // Cleanup
 verify(jsonParser).nextToken();
 verify(jsonParser).nextIntValue();
 verify(jsonParser).nextToken();
 verifyNoMoreInteractions(jsonParser);
 verify(deserializationContext).findContextualValueDeserializer(Integer.class);
 verify(jsonDeserializer).deserialize(jsonParser, deserializationContext);
 verify(jsonDeserializer).deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
 verify(instance).setValue(eq(42));
 }

 @org.junit.Test
 public void testMethodPropertyWithNullValueProvider() {
 // Given
 MethodProperty methodProperty = new MethodProperty(null, null, nullValueProvider());

 // Then
 assertNull(methodProperty.getPropertyName());
 assertNull(methodProperty.getBeanType());
 assertNull(methodProperty.getDeserializer());
 assertNull(methodProperty.getType());
 assertNull(methodProperty.getNullProvider());
 assertNull(methodProperty.getAnnotated());
 assertNull(methodProperty.getSetter());
 assertFalse(methodProperty.isSkipNulls());
 }

 private static JsonDeserializer<Integer> jsonDeserializer() {
 return new StdDeserializer<Integer>(Integer.class) {};
 }

 private static NullValueProvider nullValueProvider() {
 return new NullValueProvider() {
 @Override
 public Object getNullValue(DeserializationContext ctxt) throws IOException {
 return null;
 }
 };
 }
}