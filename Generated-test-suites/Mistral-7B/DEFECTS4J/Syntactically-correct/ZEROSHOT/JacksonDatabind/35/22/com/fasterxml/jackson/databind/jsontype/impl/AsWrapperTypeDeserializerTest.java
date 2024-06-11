package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import static com.fasterxml.jackson.core.JsonToken.*;

public class AsWrapperTypeDeserializerTest {

 private AsWrapperTypeDeserializer deserializer;
 private ObjectMapper mapper;
 private JsonFactory jsonFactory;
 private TypeIdResolver typeIdResolver;
 private JavaType baseType;

 @Before
 public void setUp() throws Exception {
 mapper = new ObjectMapper();
 jsonFactory = mapper.getFactory();
 typeIdResolver = mock(TypeIdResolver.class);
 baseType = mapper.getTypeFactory().constructType(String.class);
 deserializer = new AsWrapperTypeDeserializer(baseType, typeIdResolver, "_typePropertyName", true, Object.class);
 }

 @Test
 public void testDeserializeTypedFromObject() throws Exception {
 JsonParser jp = createMockParser();
 when(jp.getToken()).thenReturn(START_OBJECT);
 when(jp.nextToken()).thenReturn(FIELD_NAME);
 when(jp.getText()).thenReturn("typeId");
 when(typeIdResolver.idFromValueAndType("typeId", baseType)).thenReturn("typeIdValue");
 JsonDeserializer<Object> deserializerSpy = spy(new SimpleObjectDeserializer(baseType));
 Object subTypeValue = new Object();
 when(deserializerSpy.deserialize(any(), any())).thenReturn(subTypeValue);
 when(typeIdResolver.resolveDeserializer("typeIdValue", baseType)).thenReturn(deserializerSpy);

 Object result = deserializer.deserializeTypedFromObject(jp, new TestContext());
 assertEquals("SubType value", result);

 verify(jp, times(2)).nextToken();
 verify(jp, times(3)).getText();
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromObjectWithWrongToken() throws Exception {
 JsonParser jp = createMockParser();
 when(jp.getToken()).thenReturn(START_OBJECT);
 when(jp.nextToken()).thenReturn(FIELD_NAME);
 when(jp.getText()).thenReturn("typeId");
 when(typeIdResolver.idFromValueAndType("typeId", baseType)).thenReturn("typeIdValue");
 when(typeIdResolver.resolveDeserializer("typeIdValue", baseType)).thenReturn(mock(JsonDeserializer.class));
 when(jp.nextToken()).thenReturn(VALUE_NUMBER_INT);

 deserializer.deserializeTypedFromObject(jp, new TestContext());
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromObjectWithMissingEndObject() throws Exception {
 JsonParser jp = createMockParser();
 when(jp.getToken()).thenReturn(START_OBJECT);
 when(jp.nextToken()).thenReturn(FIELD_NAME);
 when(jp.getText()).thenReturn("typeId");
 when(typeIdResolver.idFromValueAndType("typeId", baseType)).thenReturn("typeIdValue");
 when(typeIdResolver.resolveDeserializer("typeIdValue", baseType)).thenReturn(mock(JsonDeserializer.class));
 when(jp.nextToken()).thenReturn(START_ARRAY);

 deserializer.deserializeTypedFromObject(jp, new TestContext());
 }

 @Test
 public void testDeserializeTypedFromArray() throws Exception {
 JsonParser jp = createMockParser();
 when(jp.getToken()).thenReturn(START_ARRAY);
 when(jp.nextToken()).thenReturn(VALUE_STRING);
 when(typeIdResolver.idFromValueAndType("typeIdValue", baseType)).thenReturn("typeIdValue");
 JsonDeserializer<Object> deserializerSpy = spy(new SimpleObjectDeserializer(baseType));
 Object subTypeValue = new Object();
 when(deserializerSpy.deserialize(any(), any())).thenReturn(subTypeValue);
 when(typeIdResolver.resolveDeserializer("typeIdValue", baseType)).thenReturn(deserializerSpy);

 Object result = deserializer.deserializeTypedFromArray(jp, new TestContext());
 assertEquals("SubType value", result);

 verify(jp, times(3)).nextToken();
 verify(jp, times(3)).getText();
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromArrayWithWrongToken() throws Exception {
 JsonParser jp = createMockParser();
 when(jp.getToken()).thenReturn(START_ARRAY);
 when(jp.nextToken()).thenReturn(VALUE_STRING);
 when(jp.nextToken()).thenReturn(START_OBJECT);

 deserializer.deserializeTypedFromArray(jp, new TestContext());
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromScalar() throws Exception {
 JsonParser jp = createMockParser();
 when(jp.getToken()).thenReturn(VALUE_STRING);

 deserializer.deserializeTypedFromScalar(jp, new TestContext());
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromAny() throws Exception {
 JsonParser jp = createMockParser();
 when(jp.getToken()).thenReturn(VALUE_NULL);

 deserializer.deserializeTypedFromAny(jp, new TestContext());
 }

 private JsonParser createMockParser() {
 return mock(JsonParser.class);
 }

 private static class TestContext extends DeserializationContext {
 @Override
 public ObjectFactory getConfig() {
 return mock(ObjectFactory.class);
 }

 @Override
 public ObjectReader getRootValueFactory() {
 return mock(ObjectReader.class);
 }

 @Override
 public BeanDescription getBeanDescription() {
 return null;
 }
 }
}