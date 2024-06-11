package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

@RunWith(MockitoJUnitRunner.class)
public class ExternalTypeHandlerTest {

 private JsonParser mockParser;
 private DeserializationContext mockContext;
 private Object mockBean;
 private Map<String, Integer> nameToPropertyIndexMock;
 private List<ExtTypedProperty> propertiesMock;
 private ExternalTypeHandler handler;

 @Before
 public void setup() throws Exception {
 mockParser = Mockito.mock(JsonParser.class);
 mockContext = Mockito.mock(DeserializationContext.class);
 mockBean = Mockito.mock(Object.class);
 nameToPropertyIndexMock = new HashMap<>();
 propertiesMock = new ArrayList<>();

 handler = new ExternalTypeHandler(propertiesMock.toArray(new ExtTypedProperty[0]),
 nameToPropertyIndexMock, null, null);
 }

 @Test
 public void testStart() throws IOException {
 ExternalTypeHandler result = handler.start();
 assertEquals(handler, result);
 }

 @Test
 public void testHandleTypePropertyValue() throws IOException {
 String propName = "propName1";
 int index = 0;
 String typeId = "typeId1";
 SettableBeanProperty prop = Mockito.mock(SettableBeanProperty.class);
 TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
 ExtTypedProperty extProp = new ExtTypedProperty(prop, typeDeserializer);
 propertiesMock.add(extProp);
 nameToPropertyIndexMock.put(propName, index);
 nameToPropertyIndexMock.put(typeDeserializer.getPropertyName(), index);

 Mockito.when(mockParser.getText()).thenReturn(propName);
 Mockito.when(mockParser.isExpectedStartStructureFeature(Mockito.eq(JsonParser.Feature.QUIRKS_MODE_OFF))).thenReturn(true);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 Mockito.when(mockBean != null).thenReturn(true);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 Mockito.when(mockParser.getText()).thenReturn(typeId);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 assertTrue(handler.handleTypePropertyValue(mockParser, mockContext, propName, mockBean));
 assertEquals(typeId, handler._typeIds[index]);
 }

 @Test
 public void testHandlePropertyValue() throws IOException {
 String propName = "propName1";
 int index = 0;
 SettableBeanProperty prop = Mockito.mock(SettableBeanProperty.class);
 ExtTypedProperty extProp = new ExtTypedProperty(prop, null);
 propertiesMock.add(extProp);
 nameToPropertyIndexMock.put(propName, index);

 Mockito.when(mockParser.getText()).thenReturn(propName);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 Mockito.when(mockBean != null).thenReturn(true);

 assertTrue(handler.handlePropertyValue(mockParser, mockContext, propName, mockBean));
 }

 @Test(expected = IOException.class)
 public void testCompleteWithNullBean() throws IOException {
 handler.complete(mockParser, mockContext, null);
 }

 @Test(expected = IOException.class)
 public void testCompleteWithEmptyNameToPropertyIndex() throws IOException {
 handler = new ExternalTypeHandler(propertiesMock.toArray(new ExtTypedProperty[0]), null, null, null);
 handler.complete(mockParser, mockContext, mockBean);
 }

 @Test(expected = IOException.class)
 public void testCompleteWithMissingProperty() throws IOException {
 String propName = "missingPropName";
 SettableBeanProperty prop = Mockito.mock(SettableBeanProperty.class);
 ExtTypedProperty extProp = new ExtTypedProperty(prop, null);
 propertiesMock.add(extProp);
 nameToPropertyIndexMock.put(propName, 0);

 Mockito.when(mockParser.getText()).thenReturn(propName);
 Mockito.when(mockBean != null).thenReturn(true);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 handler.complete(mockParser, mockContext, mockBean);
 }

 @Test(expected = IOException.class)
 public void testCompleteWithMissingTypeId() throws IOException {
 String propName = "propName1";
 SettableBeanProperty prop = Mockito.mock(SettableBeanProperty.class);
 ExtTypedProperty extProp = new ExtTypedProperty(prop, null);
 propertiesMock.add(extProp);
 nameToPropertyIndexMock.put(propName, 0);

 Mockito.when(mockParser.getText()).thenReturn(propName);
 Mockito.when(mockBean != null).thenReturn(true);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 handler.complete(mockParser, mockContext, mockBean);
 }

 @Test
 public void testComplete() throws IOException, JsonProcessingException {
 String propName1 = "propName1";
 String typeId1 = "typeId1";
 SettableBeanProperty prop1 = Mockito.mock(SettableBeanProperty.class);
 ExtTypedProperty extProp1 = new ExtTypedProperty(prop1, null);
 propertiesMock.add(extProp1);
 nameToPropertyIndexMock.put(propName1, 0);
 nameToPropertyIndexMock.put("typeId1", 0);

 String propName2 = "propName2";
 SettableBeanProperty prop2 = Mockito.mock(SettableBeanProperty.class);
 ExtTypedProperty extProp2 = new ExtTypedProperty(prop2, null);
 propertiesMock.add(extProp2);
 nameToPropertyIndexMock.put(propName2, 1);

 Mockito.when(mockParser.getText()).thenReturn(propName1);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 Mockito.when(mockBean != null).thenReturn(true);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 Mockito.when(mockParser.getText()).thenReturn(typeId1);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 Mockito.when(mockParser.getText()).thenReturn(propName2);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 Mockito.when(mockBean != null).thenReturn(true);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 handler.complete(mockParser, mockContext, mockBean);

 Object result = mockBean;
 assertEquals(typeId1, handler._typeIds[0]);
 assertEquals(prop1, handler._properties[0].getProperty());
 assertEquals(prop2, handler._properties[1].getProperty());
 }

 @Test
 public void testCompleteWithNullTypeId() throws IOException, JsonProcessingException {
 String propName1 = "propName1";
 SettableBeanProperty prop1 = Mockito.mock(SettableBeanProperty.class);
 ExtTypedProperty extProp1 = new ExtTypedProperty(prop1, null);
 propertiesMock.add(extProp1);
 nameToPropertyIndexMock.put(propName1, 0);
 nameToPropertyIndexMock.put("typeId1", 0);

 String propName2 = "propName2";
 SettableBeanProperty prop2 = Mockito.mock(SettableBeanProperty.class);
 ExtTypedProperty extProp2 = new ExtTypedProperty(prop2, null);
 propertiesMock.add(extProp2);
 nameToPropertyIndexMock.put(propName2, 1);

 Mockito.when(mockParser.getText()).thenReturn(propName1);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 Mockito.when(mockBean != null).thenReturn(true);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 Mockito.when(mockParser.getText()).thenReturn(propName2);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 Mockito.when(mockBean != null).thenReturn(true);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 handler.complete(mockParser, mockContext, mockBean);

 Object result = mockBean;
 assertEquals(null, handler._typeIds[0]);
 assertEquals(prop1, handler._properties[0].getProperty());
 assertEquals(prop2, handler._properties[1].getProperty());
 }

 @Test
 public void testCompleteWithNaturalType() throws IOException, JsonProcessingException {
 String propName1 = "propName1";
 SettableBeanProperty prop1 = Mockito.mock(SettableBeanProperty.class);
 ExtTypedProperty extProp1 = new ExtTypedProperty(prop1, null);
 propertiesMock.add(extProp1);
 nameToPropertyIndexMock.put(propName1, 0);
 nameToPropertyIndexMock.put("typeId1", 0);

 String propName2 = "propName2";
 SettableBeanProperty prop2 = Mockito.mock(SettableBeanProperty.class);
 ExtTypedProperty extProp2 = new ExtTypedProperty(prop2, null);
 propertiesMock.add(extProp2);
 nameToPropertyIndexMock.put(propName2, 1);

 JsonParser parser = Mockito.mock(JsonParser.class);
 DeserializationContext context = Mockito.mock(DeserializationContext.class);
 Object bean = Mockito.mock(Object.class);
 PropertyValueBuffer buffer = Mockito.mock(PropertyValueBuffer.class);
 PropertyBasedCreator creator = Mockito.mock(PropertyBasedCreator.class);

 Mockito.when(mockParser.getText()).thenReturn(propName1);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 Mockito.when(mockBean != null).thenReturn(true);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
 Mockito.when(mockParser.getIntValue()).thenReturn(123);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
 Mockito.when(mockParser.getText()).thenReturn(propName2);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 Mockito.when(mockBean != null).thenReturn(true);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
 Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 handler.complete(parser, context, buffer, creator);

 Object result = creator.build(context, buffer);
 assertEquals(123, handler._properties[0].getProperty().getValue(result));
 }
}