package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.FieldProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class FieldPropertyTest {

 private JsonParser mockJsonParser;
 private ObjectMock objectMock;
 private ObjectMock instanceMock;
 private FieldProperty fieldProperty;
 private AnnotatedField annotatedField;
 private BeanPropertyDefinition beanPropertyDefinition;
 private TypeDeserializer typeDeserializer;
 private JsonDeserializer<?> valueDeserializer;
 private NullValueProvider nullValueProvider;
 private static final Object NULL_VALUE = new Object();;

 @Before
 public void setUp() {
 mockJsonParser = mock(JsonParser.class);
 objectMock = mock(ObjectMock.class);
 instanceMock = mock(ObjectMock.class);
 annotatedField = mock(AnnotatedField.class);
 beanPropertyDefinition = mock(BeanPropertyDefinition.class);
 typeDeserializer = mock(TypeDeserializer.class);
 valueDeserializer = mock(JsonDeserializer.class);
 nullValueProvider = mock(NullValueProvider.class);

 fieldProperty = new FieldProperty(beanPropertyDefinition, JsonTypeFactory.defaultInstance.constructType(Object.class),
 typeDeserializer, Annotations.empty(), annotatedField);
 }

 @Test
 public void testFieldProperty() {
 // Test default constructor
 FieldProperty defaultFieldProperty = new FieldProperty(beanPropertyDefinition, JsonTypeFactory.defaultInstance.constructType(Object.class),
 typeDeserializer, Annotations.empty(), annotatedField);
 assertSame(beanPropertyDefinition, defaultFieldProperty.getBeanPropertyDefinition());
 assertSame(JsonTypeFactory.defaultInstance.constructType(Object.class), defaultFieldProperty.getType());
 assertNull(defaultFieldProperty.getValueDeserializer());
 assertNull(defaultFieldProperty.getNullProvider());
 assertSame(annotatedField, defaultFieldProperty.getAnnotated());
 assertSame(annotatedField.getAnnotated(), defaultFieldProperty.getField());
 assertSame(annotatedField.getAnnotated(), defaultFieldProperty._annotated);
 assertSame(annotatedField.getAnnotated(), defaultFieldProperty._field);

 // Test withValueDeserializer constructor
 fieldProperty = new FieldProperty(defaultFieldProperty, valueDeserializer, nullValueProvider);
 assertSame(defaultFieldProperty, fieldProperty.getBase());
 assertSame(valueDeserializer, fieldProperty.getValueDeserializer());
 assertSame(nullValueProvider, fieldProperty.getNullProvider());

 // Test withName constructor
 PropertyName newName = PropertyName.construct("newName");
 FieldProperty withNameFieldProperty = new FieldProperty(fieldProperty, newName);
 assertSame(fieldProperty, withNameFieldProperty.getBase());
 assertSame(newName, withNameFieldProperty.getName());

 // Test withJdkSerialization constructor
 FieldProperty withJdkSerializationFieldProperty = new FieldProperty(fieldProperty);
 assertSame(fieldProperty, withJdkSerializationFieldProperty.getBase());
 }

 @Test
 public void testDeserializeAndSet() throws JsonProcessingException {
 String jsonString = "{\"fieldName\":\"testString\"}";
 when(mockJsonParser.getText()).thenReturn(jsonString);

 // Test with no null provider
 fieldProperty.deserializeAndSet(mockJsonParser, mock(DeserializationContext.class), instanceMock);
 verify(instanceMock).set(eq(objectMock), eq("testString"));

 // Test with null provider
 Annotation nullAnnotation = mock(Annotation.class);
 when(annotatedField.getAnnotations()).thenReturn(new Annotation[] { nullAnnotation });
 Annotations contextAnnotations = mock(Annotations.class);
 when(Annotations.findAnnotation(nullAnnotation.getClass(), contextAnnotations)).thenReturn(Nulls.WITH_NULL);
 fieldProperty = new FieldProperty(beanPropertyDefinition, JsonTypeFactory.defaultInstance.constructType(Object.class),
 typeDeserializer, contextAnnotations, annotatedField);
 fieldProperty.deserializeAndSet(mockJsonParser, mock(DeserializationContext.class), instanceMock);
 verifyZeroInteractions(instanceMock);
 }

 @Test
 public void testDeserializeSetAndReturn() throws JsonProcessingException {
 String jsonString = "{\"fieldName\":\"testString\"}";
 when(mockJsonParser.getText()).thenReturn(jsonString);

 // Test with no null provider
 Object testInstance = instanceMock.objects()[0];
 Object result = fieldProperty.deserializeSetAndReturn(mockJsonParser, mock(DeserializationContext.class), testInstance);
 assertEquals(testInstance, result);
 verify(instanceMock).set(eq(objectMock), eq("testString"));

 // Test with null provider
 Object nullTestInstance = null;
 Object resultNull = fieldProperty.deserializeSetAndReturn(mockJsonParser, mock(DeserializationContext.class), nullTestInstance);
 assertSame(nullTestInstance, resultNull);
 }

 @Test
 public void testSet() throws JsonProcessingException {
 String jsonString = "{\"fieldName\":\"testString\"}";
 when(mockJsonParser.getText()).thenReturn(jsonString);
 String testString = "testString";

 // Test with no null provider
 fieldProperty.set(instanceMock, mockJsonParser);
 verifyZeroInteractions(instanceMock);
 verify(instanceMock).set(eq(objectMock), eq(testString));

 // Test with null provider
 when(mockJsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
 fieldProperty.set(instanceMock, mockJsonParser);
 verifyZeroInteractions(instanceMock);
 verify(nullValueProvider).getNullValue(mock(DeserializationContext.class));
 }
}