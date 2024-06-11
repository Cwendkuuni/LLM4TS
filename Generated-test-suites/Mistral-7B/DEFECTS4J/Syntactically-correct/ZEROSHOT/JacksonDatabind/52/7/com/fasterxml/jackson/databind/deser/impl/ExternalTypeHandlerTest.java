package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.util.TokenBuffer.RootName;

public class ExternalTypeHandlerTest {

 private static class ExternalTypeHandlerSpy extends ExternalTypeHandler {
 private Map<String, Integer> nameToPropertyIndexSpy = new HashMap<>();
 private List<ExtTypedProperty> propertiesSpy = new ArrayList<>();
 private String[] typeIds;
 private TokenBuffer[] tokens;

 public ExternalTypeHandlerSpy(List<ExtTypedProperty> properties, Map<String, Integer> nameToPropertyIndex,
 String[] typeIds, TokenBuffer[] tokens) {
 super(properties, nameToPropertyIndex, typeIds, tokens);
 this.propertiesSpy = properties;
 this.nameToPropertyIndexSpy = nameToPropertyIndex;
 this.typeIds = typeIds;
 this.tokens = tokens;
 }

 public Map<String, Integer> getNameToPropertyIndex() {
 return this.nameToPropertyIndexSpy;
 }

 public List<ExtTypedProperty> getProperties() {
 return this.propertiesSpy;
 }

 @Override
 public ExternalTypeHandler start() {
 throw new UnsupportedOperationException();
 }

 @Override
 public boolean handleTypePropertyValue(JsonParser p, DeserializationContext ctxt, String propName, Object bean)
 throws IOException {
 return false;
 }

 @Override
 public boolean handlePropertyValue(JsonParser p, DeserializationContext ctxt, String propName, Object bean)
 throws IOException {
 return false;
 }

 @Override
 public Object complete(JsonParser p, DeserializationContext ctxt, Object bean) throws IOException {
 return bean;
 }

 @Override
 public Object complete(JsonParser p, DeserializationContext ctxt, PropertyValueBuffer buffer,
 PropertyBasedCreator creator) throws IOException {
 return null;
 }

 @Override
 protected Object _deserialize(JsonParser p, DeserializationContext ctxt, int index, String typeId)
 throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override
 protected void _deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object bean, int index, String typeId)
 throws IOException {
 throw new UnsupportedOperationException();
 }
 }

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testExternalTypeHandler() throws IOException, JsonMappingException, JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();

 // given
 ExtTypedProperty prop1 = mock(ExtTypedProperty.class);
 when(prop1.getProperty()).thenReturn(new SettableBeanProperty(new PropertyName("p1"), String.class));
 when(prop1.getTypePropertyName()).thenReturn(new PropertyName("tp1"));

 ExtTypedProperty prop2 = mock(ExtTypedProperty.class);
 when(prop2.getProperty()).thenReturn(new SettableBeanProperty(new PropertyName("p2"), Number.class));
 when(prop2.getTypePropertyName()).thenReturn(new PropertyName("tp2"));
 when(prop2.getDefaultTypeId()).thenReturn("NumberType");

 List<ExtTypedProperty> properties = new ArrayList<>();
 properties.add(prop1);
 properties.add(prop2);

 Map<String, Integer> nameToPropertyIndex = new HashMap<>();
 nameToPropertyIndex.put("p1", 0);
 nameToPropertyIndex.put("tp1", 0);
 nameToPropertyIndex.put("p2", 1);
 nameToPropertyIndex.put("tp2", 1);
 nameToPropertyIndex.put("tp1", 0); // for typeId

 String[] typeIds = new String[]{"StringType", "NumberType"};
 TokenBuffer[] tokens = new TokenBuffer[2];

 ExternalTypeHandlerSpy externalTypeHandlerSpy = new ExternalTypeHandlerSpy(properties, nameToPropertyIndex, typeIds, tokens);

 // when
 JsonParser jsonParser = mapper.getFactory().createParser("{\"p1\":\"value1\",\"tp1\":\"StringType\",\"p2\":null}");
 Object result = externalTypeHandlerSpy.complete(jsonParser, mapper.getDeserializationContext(), null);

 // then
 assertEquals(2, externalTypeHandlerSpy.getProperties().size());
 assertEquals(prop1, externalTypeHandlerSpy.getProperties().get(0));
 assertEquals(prop2, externalTypeHandlerSpy.getProperties().get(1));

 assertEquals(2, externalTypeHandlerSpy.getNameToPropertyIndex().size());
 assertEquals(0, externalTypeHandlerSpy.getNameToPropertyIndex().get("p1").intValue());
 assertEquals(0, externalTypeHandlerSpy.getNameToPropertyIndex().get("tp1").intValue());
 assertEquals(1, externalTypeHandlerSpy.getNameToPropertyIndex().get("p2").intValue());
 assertEquals(1, externalTypeHandlerSpy.getNameToPropertyIndex().get("tp2").intValue());
 assertEquals(0, externalTypeHandlerSpy.getNameToPropertyIndex().get("tp1").intValue());

 assertEquals("value1", result.getClass().getDeclaredField("p1").get(result));
 assertNull(result.getClass().getDeclaredField("p2").get(result));

 // check tokens
 assertEquals(2, externalTypeHandlerSpy.tokens.length);
 assertEquals("StringType", externalTypeHandlerSpy.tokens[0].getText());
 assertNull(externalTypeHandlerSpy.tokens[0].nextToken());
 TokenBuffer tokenBuffer1 = externalTypeHandlerSpy.tokens[0];
 assertEquals(RootName.VALUE_STRING, tokenBuffer1.getCurrentName());
 assertEquals(JsonToken.START_OBJECT, tokenBuffer1.nextToken());
 assertEquals("p1", tokenBuffer1.getCurrentName());
 assertEquals("value1", tokenBuffer1.nextTextValue());
 assertEquals(':', tokenBuffer1.nextToken().asInt());
 assertEquals("StringType", tokenBuffer1.nextTextValue());
 assertEquals(',', tokenBuffer1.nextToken().asInt());
 assertEquals(JsonToken.END_OBJECT, tokenBuffer1.nextToken());

 assertEquals(null, externalTypeHandlerSpy.tokens[1].getText());
 TokenBuffer tokenBuffer2 = externalTypeHandlerSpy.tokens[1];
 assertEquals(RootName.VALUE_NULL, tokenBuffer2.getCurrentName());
 assertEquals(JsonToken.START_OBJECT, tokenBuffer2.nextToken());
 assertEquals("p2", tokenBuffer2.getCurrentName());
 assertEquals(JsonToken.VALUE_NULL, tokenBuffer2.nextToken());
 assertEquals(',', tokenBuffer2.nextToken().asInt());
 assertEquals(JsonToken.END_OBJECT, tokenBuffer2.nextToken());

 verify(prop1).deserialize(jsonParser, mapper.getDeserializationContext());
 verify(prop2, never()).deserialize(jsonParser, mapper.getDeserializationContext());

 // when
 jsonParser = mapper.getFactory().createParser("{\"p1\":null,\"tp1\":null,\"p2\":123}");
 result = externalTypeHandlerSpy.complete(jsonParser, mapper.getDeserializationContext(), null);

 // then
 assertEquals(2, externalTypeHandlerSpy.getProperties().size());
 assertEquals(prop1, externalTypeHandlerSpy.getProperties().get(0));
 assertEquals(prop2, externalTypeHandlerSpy.getProperties().get(1));

 assertEquals(2, externalTypeHandlerSpy.getNameToPropertyIndex().size());
 assertEquals(0, externalTypeHandlerSpy.getNameToPropertyIndex().get("p1").intValue());
 assertEquals(0, externalTypeHandlerSpy.getNameToPropertyIndex().get("tp1").intValue());
 assertEquals(1, externalTypeHandlerSpy.getNameToPropertyIndex().get("p2").intValue());
 assertEquals(1, externalTypeHandlerSpy.getNameToPropertyIndex().get("tp2").intValue());
 assertEquals(0, externalTypeHandlerSpy.getNameToPropertyIndex().get("tp1").intValue());

 assertEquals(null, result.getClass().getDeclaredField("p1").get(result));
 assertEquals(123, result.getClass().getDeclaredField("p2").get(result).intValue());

 // check tokens
 assertEquals(2, externalTypeHandlerSpy.tokens.length);
 assertEquals(null, externalTypeHandlerSpy.tokens[0].getText());
 TokenBuffer tokenBuffer3 = externalTypeHandlerSpy.tokens[0];
 assertEquals(RootName.VALUE_NULL, tokenBuffer3.getCurrentName());
 assertEquals(JsonToken.START_OBJECT, tokenBuffer3.nextToken());
 assertEquals("p1", tokenBuffer3.getCurrentName());
 assertEquals(JsonToken.VALUE_NULL, tokenBuffer3.nextToken());
 assertEquals(',', tokenBuffer3.nextToken().asInt());
 assertEquals("tp1", tokenBuffer3.nextName());
 assertEquals(JsonToken.VALUE_NULL, tokenBuffer3.nextToken());
 assertEquals(',', tokenBuffer3.nextToken().asInt());
 assertEquals(JsonToken.END_OBJECT, tokenBuffer3.nextToken());

 assertEquals("123", externalTypeHandlerSpy.tokens[1].getText());
 TokenBuffer tokenBuffer4 = externalTypeHandlerSpy.tokens[1];
 assertEquals(RootName.VALUE_NUMBER, tokenBuffer4.getCurrentName());
 assertEquals(JsonToken.START_OBJECT, tokenBuffer4.nextToken());
 assertEquals("p2", tokenBuffer4.getCurrentName());
 assertEquals("123", tokenBuffer4.nextTextValue());
 assertEquals(':', tokenBuffer4.nextToken().asInt());
 assertEquals(JsonToken.VALUE_NUMBER, tokenBuffer4.nextToken());
 assertEquals(',', tokenBuffer4.nextToken().asInt());
 assertEquals(JsonToken.END_OBJECT, tokenBuffer4.nextToken());

 verify(prop1, never()).deserialize(jsonParser, mapper.getDeserializationContext());
 verify(prop2).deserialize(jsonParser, mapper.getDeserializationContext());

 // when
 jsonParser = mapper.getFactory().createParser("{\"p1\":null,\"tp1\":null,\"p2\":\"value2\"}");
 result = externalTypeHandlerSpy.complete(jsonParser, mapper.getDeserializationContext(), null);

 // then
 assertEquals(2, externalTypeHandlerSpy.getProperties().size());
 assertEquals(prop1, externalTypeHandlerSpy.getProperties().get(0));
 assertEquals(prop2, externalTypeHandlerSpy.getProperties().get(1));

 assertEquals(2, externalTypeHandlerSpy.getNameToPropertyIndex().size());
 assertEquals(0, externalTypeHandlerSpy.getNameToPropertyIndex().get("p1").intValue());
 assertEquals(0, externalTypeHandlerSpy.getNameToPropertyIndex().get("tp1").intValue());
 assertEquals(1, externalTypeHandlerSpy.getNameToPropertyIndex().get("p2").intValue());
 assertEquals(1, externalTypeHandlerSpy.getNameToPropertyIndex().get("tp2").intValue());
 assertEquals(0, externalTypeHandlerSpy.getNameToPropertyIndex().get("tp1").intValue());

 assertEquals(null, result.getClass().getDeclaredField("p1").get(result));
 assertEquals("value2", result.getClass().getDeclaredField("p2").get(result));

 // check tokens
 assertEquals(2, externalTypeHandlerSpy.tokens.length);
 assertEquals(null, externalTypeHandlerSpy.tokens[0].getText());
 TokenBuffer tokenBuffer5 = externalTypeHandlerSpy.tokens[0];
 assertEquals(RootName.VALUE_NULL, tokenBuffer5.getCurrentName());
 assertEquals(JsonToken.START_OBJECT, tokenBuffer5.nextToken());
 assertEquals("p1", tokenBuffer5.getCurrentName());
 assertEquals(JsonToken.VALUE_NULL, tokenBuffer5.nextToken());
 assertEquals(',', tokenBuffer5.nextToken().asInt());
 assertEquals("tp1", tokenBuffer5.nextName());
 assertEquals(JsonToken.VALUE_NULL, tokenBuffer5.nextToken());
 assertEquals(',', tokenBuffer5.nextToken().asInt());
 assertEquals(JsonToken.END_OBJECT, tokenBuffer5.nextToken());

 assertEquals("value2", externalTypeHandlerSpy.tokens[1].getText());
 TokenBuffer tokenBuffer6 = externalTypeHandlerSpy.tokens[1];
 assertEquals(RootName.VALUE_STRING, tokenBuffer6.getCurrentName());
 assertEquals(JsonToken.START_OBJECT, tokenBuffer6.nextToken());
 assertEquals("p2", tokenBuffer6.getCurrentName());
 assertEquals("value2", tokenBuffer6.nextTextValue());
 assertEquals(':', tokenBuffer6.nextToken().asInt());
 assertEquals(JsonToken.VALUE_STRING, tokenBuffer6.nextToken());
 assertEquals(',', tokenBuffer6.nextToken().asInt());
 assertEquals(JsonToken.END_OBJECT, tokenBuffer6.nextToken());

 verify(prop1, never()).deserialize(jsonParser, mapper.getDeserializationContext());
 verify(prop2).deserialize(jsonParser, mapper.getDeserializationContext());

 // when
 jsonParser = mapper.getFactory().createParser("{\"tp1\":null,\"p1\":null,\"p2\":null}");
 result = externalTypeHandlerSpy.complete(jsonParser, mapper.getDeserializationContext(), null);

 // then
 assertEquals(2, externalTypeHandlerSpy.getProperties().size());
 assertEquals(prop1, externalTypeHandlerSpy.getProperties().get(0));
 assertEquals(prop2, externalTypeHandlerSpy.getProperties().get(1));

 assertEquals(2, externalTypeHandlerSpy.getNameToPropertyIndex().size());
 assertEquals(0, externalTypeHandlerSpy.getNameToPropertyIndex().get("p1").intValue());
 assertEquals(0, externalTypeHandlerSpy.getNameToPropertyIndex().get("tp1").intValue());
 assertEquals(1, externalTypeHandlerSpy.getNameToPropertyIndex().get("p2").intValue());
 assertEquals(1, externalTypeHandlerSpy.getNameToPropertyIndex().get("tp2").intValue());
 assertEquals(0, externalTypeHandlerSpy.getNameToPropertyIndex().get("tp1").intValue());

 assertEquals(null, result.getClass().getDeclaredField("p1").get(result));
 assertEquals(null, result.getClass().getDeclaredField("p2").get(result));

 // check tokens
 assertEquals(2, externalTypeHandlerSpy.tokens.length);
 assertEquals(null, externalTypeHandlerSpy.tokens[0].getText());
 TokenBuffer tokenBuffer7 = externalTypeHandlerSpy.tokens[0];
 assertEquals(RootName.VALUE_NULL, tokenBuffer7.getCurrentName());
 assertEquals(JsonToken.START_OBJECT, tokenBuffer7.nextToken());
 assertEquals("tp1", tokenBuffer7.getCurrentName());
 assertEquals(JsonToken.VALUE_NULL, tokenBuffer7.nextToken());
 assertEquals(',', tokenBuffer7.nextToken().asInt());
 assertEquals("p1", tokenBuffer7.nextName());
 assertEquals(JsonToken.VALUE_NULL, tokenBuffer7.nextToken());
 assertEquals(',', tokenBuffer7.nextToken().asInt());
 assertEquals(JsonToken.END_OBJECT, tokenBuffer7.nextToken());

 assertEquals(null, externalTypeHandlerSpy.tokens[1].getText());
 TokenBuffer tokenBuffer8 = externalTypeHandlerSpy.tokens[1];
 assertEquals(RootName.VALUE_NULL, tokenBuffer8.getCurrentName());
 assertEquals(JsonToken.START_OBJECT, tokenBuffer8.nextToken());
 assertEquals("p1", tokenBuffer8.getCurrentName());
 assertEquals(JsonToken.VALUE_NULL, tokenBuffer8.nextToken());
 assertEquals(',', tokenBuffer8.nextToken().asInt());
 assertEquals("p2", tokenBuffer8.nextName());
 assertEquals(JsonToken.VALUE_NULL, tokenBuffer8.nextToken());
 assertEquals(',', tokenBuffer8.nextToken().asInt());
 assertEquals(JsonToken.END_OBJECT, tokenBuffer8.nextToken());

 verify(prop1, never()).deserialize(jsonParser, mapper.getDeserializationContext());
 verify(prop2, never()).deserialize(jsonParser, mapper.getDeserializationContext());
 }
}