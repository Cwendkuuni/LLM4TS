package com.fasterxml.jackson.databind.deser.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyTypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Annotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SetterlessPropertyTest {

 private ObjectMapper objectMapper;
 private SetterlessProperty setterlessProperty;
 private JavaType javaType;
 private DeserializationContext deserializationContext;
 private JsonParser jsonParser;
 private Object instance;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
 deserializationContext = Mockito.mock(DeserializationContext.class);
 jsonParser = Mockito.mock(JsonParser.class);
 instance = Mockito.mock(Object.class);

 setterlessProperty = new SetterlessProperty(
 Mockito.mock(BeanPropertyDefinition.class), javaType,
 Mockito.mock(TypeDeserializer.class), Annotations.EMPTY_ANNOTATIONS,
 Mockito.mock(AnnotatedMethod.class)
 );
 }

 @After
 public void tearDown() throws Exception {
 setterlessProperty = null;
 instance = null;
 jsonParser = null;
 deserializationContext = null;
 objectMapper = null;
 }

 @Test
 public void testSetterlessProperty() throws Exception {
 // Test constructor
 SettableBeanProperty settableBeanProperty = new SettableBeanProperty(
 Mockito.mock(BeanPropertyDefinition.class), javaType,
 Mockito.mock(TypeDeserializer.class), Annotations.EMPTY_ANNOTATIONS,
 Mockito.mock(AnnotatedMember.class)
 );
 SetterlessProperty setterlessProperty1 = new SetterlessProperty(settableBeanProperty, null, null);

 assertEquals(settableBeanProperty, setterlessProperty1.getPropertyDefinition());
 assertEquals(javaType, setterlessProperty1.getRawType());
 assertEquals(null, setterlessProperty1._annotated);
 assertEquals(null, setterlessProperty1._getter);

 // Test withName method
 PropertyName propertyName = PropertyName.construct("testProperty");
 SetterlessProperty setterlessProperty2 = setterlessProperty1.withName(propertyName);
 assertEquals(propertyName, setterlessProperty2.getName());

 // Test withValueDeserializer method
 JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);
 SetterlessProperty setterlessProperty3 = setterlessProperty2.withValueDeserializer(jsonDeserializer);
 assertEquals(jsonDeserializer, setterlessProperty3._valueDeserializer);

 // Test withNullProvider method
 NullValueProvider nullValueProvider = mock(NullValueProvider.class);
 SetterlessProperty setterlessProperty4 = setterlessProperty3.withNullProvider(nullValueProvider);
 assertEquals(nullValueProvider, setterlessProperty4._nullProvider);

 // Test fixAccess method
 BeanDescription beanDescription = mock(BeanDescription.class);
 setterlessProperty4.fixAccess(beanDescription);
 Mockito.verify(beanDescription).fixAccess(Mockito.anyBoolean());

 // Test getter and setter
 JsonToken jsonToken = JsonToken.START_ARRAY;
 when(jsonParser.getCurrentToken()).thenReturn(jsonToken);
 when(jsonParser.nextToken()).thenAnswer(invocation -> {
 JsonToken result = JsonToken.VALUE_STRING;
 jsonToken = result;
 return result;
 });
 when(jsonParser.getValueAsString()).thenReturn("test");

 Object result = setterlessProperty4.deserializeAndSet(jsonParser, deserializationContext, instance);
 assertEquals(instance, result);
 assertEquals("test", _getValue(instance, "testProperty"));

 // Test deserializeSetAndReturn method
 result = setterlessProperty4.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
 assertEquals(instance, result);
 assertEquals("test", _getValue(instance, "testProperty"));

 // Test set and setAndReturn methods (should throw UnsupportedOperationException)
 try {
 setterlessProperty4.set(instance, "test");
 fail();
 } catch (UnsupportedOperationException e) {
 // Expected
 }

 try {
 result = setterlessProperty4.setAndReturn(instance, "test");
 fail();
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 private Object _getValue(Object instance, String propertyName) {
 try {
 Object value = setterlessProperty.getGetter().invoke(instance, new Object[0]);
 return value == null ? null : ClassUtil.convertValue(value, javaType.getRawClass());
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
}
}