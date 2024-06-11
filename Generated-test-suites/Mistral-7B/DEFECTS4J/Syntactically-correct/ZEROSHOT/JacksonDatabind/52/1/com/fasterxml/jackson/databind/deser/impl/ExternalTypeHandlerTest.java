package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.*;

public class ExternalTypeHandlerTest {

 private ExternalTypeHandler handler;
 private JsonParser parser;
 private DeserializationContext context;
 private Object bean;
 private SettableBeanProperty property;
 private TypeDeserializer typeDeserializer;
 private ExtTypedProperty extProperty;
 private Builder builder;

 @Before
 public void setup() {
 property = mock(SettableBeanProperty.class);
 typeDeserializer = mock(TypeDeserializer.class);
 extProperty = new ExtTypedProperty(property, typeDeserializer);
 builder = new Builder();
 handler = new ExternalTypeHandler(new ExtTypedProperty[0], new HashMap<>(), null, null);
 parser = mock(JsonParser.class);
 context = mock(DeserializationContext.class);
 bean = new Object();
 }

 @Test
 public void testStart() {
 handler = handler.start();
 }

 @Test
 public void testHandleTypePropertyValueFalse() throws IOException {
 // Given
 String propName = "notMatchingPropertyName";
 when(parser.getCurrentName()).thenReturn(propName);

 // When
 boolean result = handler.handleTypePropertyValue(parser, context, bean);

 // Then
 verify(parser, times(1)).getCurrentName();
 verifyZeroInteractions(parser);
 verifyZeroInteractions(context);
 verify(bean, times(0)).set(any(), any());
 assertFalse(result);
 }

 @Test
 public void testHandleTypePropertyValueTrue() throws IOException {
 // Given
 String propName = "typePropertyName";
 String typeId = "expectedTypeId";
 when(parser.getText()).thenReturn(typeId);
 when(parser.getCurrentName()).thenReturn(propName);
 Integer index = 0;
 ExtTypedProperty prop = new ExtTypedProperty(property, typeDeserializer);
 prop.linkTypeProperty(property);
 List<ExtTypedProperty> properties = new ArrayList<>();
 properties.add(prop);
 Map<String, Integer> nameToPropertyIndex = new HashMap<>();
 nameToPropertyIndex.put(propName, index);
 handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]), nameToPropertyIndex, null, null);

 // When
 handler.handleTypePropertyValue(parser, context, bean);

 // Then
 verify(parser, times(1)).getText();
 verify(parser, times(1)).nextToken(); // Skip children
 verify(parser, times(1)).getText(); // Get typeId
 verifyZeroInteractions(parser).skipChildren();
 verify(bean, times(1)).set(eq(index), eq(typeId));
 }

 @Test
 public void testHandlePropertyValueFalse() throws IOException {
 // Given
 String propName = "notMatchingPropertyName";
 when(parser.getText()).thenReturn("someValue");
 when(parser.getCurrentName()).thenReturn(propName);

 // When
 boolean result = handler.handlePropertyValue(parser, context, bean);

 // Then
 verify(parser, times(1)).getText();
 verify(parser, times(1)).getCurrentName();
 verifyZeroInteractions(parser).skipChildren();
 verifyZeroInteractions(context);
 verifyZeroInteractions(bean);
 assertFalse(result);
 }

 @Test
 public void testHandlePropertyValueTrue() throws IOException {
 // Given
 String propName = "propertyName";
 String typeId = "expectedTypeId";
 when(parser.getText()).thenReturn(typeId);
 when(parser.getCurrentName()).thenReturn(propName);
 ExtTypedProperty prop = new ExtTypedProperty(property, typeDeserializer);
 prop.linkTypeProperty(property);
 List<ExtTypedProperty> properties = new ArrayList<>();
 properties.add(prop);
 Map<String, Integer> nameToPropertyIndex = new HashMap<>();
 nameToPropertyIndex.put(propName, 0);
 handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]), nameToPropertyIndex, null, null);

 // When
 handler.handlePropertyValue(parser, context, bean);

 // Then
 verify(parser, times(1)).getText();
 verify(parser, times(1)).getCurrentName();
 verify(parser, times(1)).skipChildren();
 verifyZeroInteractions(parser).nextToken(); // Expected to be a value after skipping children
 verify(bean, times(1)).set(eq(0), any());
 }

 @Test
 public void testCompleteNullBean() throws IOException {
 // Given
 handler = builder.build();

 // When
 Object result = handler.complete(parser, context, null);

 // Then
 verifyZeroInteractions(parser);
 verifyZeroInteractions(context);
 assertNull(result);
 }

 @Test
 public void testCompleteBeanWithNoMissingProperties() throws IOException {
 // Given
 handler = builder.build();
 property.setValue(bean, "value");
 builder.addExternal(property, typeDeserializer);
 builder.addExternal(property, typeDeserializer);
 handler = builder.build();
 when(parser.getText()).thenReturn("typeId1");
 when(parser.getText()).thenReturn("value1");
 when(parser.getText()).thenReturn("typeId2");
 when(parser.getText()).thenReturn("value2");

 // When
 Object result = handler.complete(parser, context, bean);

 // Then
 verify(parser, times(4)).getText();
 verifyZeroInteractions(parser).nextToken(); // Expected to be a value after getting the typeId
 verify(bean, times(2)).set(eq(0), any());
 verify(bean, times(1)).set(eq(1), any());
 assertEquals(bean, result);
 }

 @Test
 public void testCompleteBeanWithMissingTypeIdProperty() throws IOException {
 // Given
 handler = builder.build();
 property.setValue(bean, "value");
 builder.addExternal(property, typeDeserializer);
 builder.addExternal(property, typeDeserializer);
 handler = builder.build();
 when(parser.getText()).thenReturn("typeId1");
 when(parser.getText()).thenReturn("value1");
 when(parser.getText()).thenReturn("typeId2");
 when(parser.getText()).thenReturn(null); // Missing typeId for last property

 // When
 Object result = handler.complete(parser, context, bean);

 // Then
 verify(parser, times(4)).getText();
 verify(bean, times(2)).set(eq(0), any());
 verify(bean, times(1)).set(eq(1), any());
 verify(parser, times(1)).nextToken(); // Expected to be a value after getting the null typeId
 SettableBeanProperty lastProperty = property;
 verify(lastProperty, times(1)).set(bean, null);
 assertEquals(bean, result);
 }

 @Test
 public void testCompleteBeanWithMissingProperty() throws IOException {
 // Given
 handler = builder.build();
 property.setValue(bean, "value");
 builder.addExternal(property, typeDeserializer);
 builder.addExternal(property, typeDeserializer);
 handler = builder.build();
 when(parser.getText()).thenReturn("typeId1");
 when(parser.getText()).thenReturn("value1");
 when(parser.getText()).thenReturn("typeId2");
 when(parser.getText()).thenReturn("value2");
 when(parser.getText()).thenReturn(null); // Missing property for last typeId

 // When
 Object result = handler.complete(parser, context, bean);

 // Then
 verify(parser, times(4)).getText();
 verify(bean, times(2)).set(eq(0), any());
 verify(bean, times(1)).set(eq(1), any());
 verify(parser, times(1)).nextToken(); // Expected to be a value after getting the null typeId
 SettableBeanProperty lastTypeProperty = extProperty.getTypeProperty();
 verify(lastTypeProperty, times(1)).set(bean, null);
 SettableBeanProperty lastProperty = property;
 verify(lastProperty, times(1)).set(bean, null);
 assertEquals(bean, result);
 }

 @Test
 public void testCompleteBeanWithMissingBothTypeIdAndProperty() throws IOException {
 // Given
 handler = builder.build();
 property.setValue(bean, "value");
 builder.addExternal(property, typeDeserializer);
 builder.addExternal(property, typeDeserializer);
 handler = builder.build();
 when(parser.getText()).thenReturn("typeId1");
 when(parser.getText()).thenReturn("value1");
 when(parser.getText()).thenReturn("typeId2");
 when(parser.getText()).thenReturn(null); // Missing typeId for last property
 when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 // When
 Object result = handler.complete(parser, context, bean);

 // Then
 verify(parser, times(4)).getText();
 verify(parser, times(2)).nextToken(); // Expected to be START\_OBJECT after getting the null typeId
 SettableBeanProperty lastTypeProperty = extProperty.getTypeProperty();
 verify(lastTypeProperty, times(1)).set(bean, null);
 SettableBeanProperty lastProperty = property;
 verify(lastProperty, times(1)).set(bean, null);
 assertEquals(bean, result);
 }

 @Test
 public void testCompleteBeanWithNaturalType() throws IOException {
 // Given
 handler = builder.build();
 property.setValue(bean, "value");
 builder.addExternal(property, typeDeserializer);
 builder.addExternal(property, typeDeserializer);
 handler = builder.build();
 when(parser.getText()).thenReturn("typeId1");
 when(parser.getText()).thenReturn("value1");
 when(parser.getText()).thenReturn("typeId2");
 when(parser.getText()).thenReturn("42"); // Natural type number
 when(parser.getText()).thenReturn("value2");

 // When
 Object result = handler.complete(parser, context, bean);

 // Then
 verify(parser, times(4)).getText();
 verify(parser, times(1)).nextToken(); // Expected to be a value after getting the natural type number
 SettableBeanProperty lastProperty = property;
 verify(lastProperty, times(1)).set(bean, "42");
 assertEquals(bean, result);
 }
}