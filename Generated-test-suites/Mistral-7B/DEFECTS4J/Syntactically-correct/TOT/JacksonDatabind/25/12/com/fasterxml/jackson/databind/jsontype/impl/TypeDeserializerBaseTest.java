package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase typeDeserializerBase;
 private TypeIdResolver typeIdResolver;
 private DeserializationContext deserializationContext;

 @Before
 public void setup() throws Exception {
 typeIdResolver = new StdTypeIdResolver();
 deserializationContext = new TestDeserializationContext();
 typeDeserializerBase = new StandardTypeDeserializerBase(
 ClassUtil.weaken(Object.class), typeIdResolver, "_typePropertyName", true, Object.class);
 }

 @Test
 public void testForProperty() throws Exception {
 BeanProperty property = new BeanProperty(Object.class, "testProperty");
 TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(property);
 assertNotNull(typeDeserializer);
 }

 @Test
 public void testIdResolver() throws Exception {
 JavaType type = typeIdResolver.typeFromId(deserializationContext, "java.lang.String");
 assertEquals(String.class, type.getRawClass());
 }

 @Test
 public void testFindDeserializer() throws Exception {
 String typeId = "java.lang.String";
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(deserializationContext, typeId);
 assertNotNull(deserializer);
 assertEquals(StringDeserializer.class, deserializer.getClass());
 }

 @Test
 public void testFindDefaultImplDeserializer() throws Exception {
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(deserializationContext);
 assertNull(deserializer);
 }

 @Test(expected = IOException.class)
 public void testHandleUnknownTypeId() throws Exception {
 typeDeserializerBase._handleUnknownTypeId(deserializationContext, "invalidTypeId", typeIdResolver, Object.class);
 fail("Should have thrown exception");
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNativeTypeIdNull() throws Exception {
 JsonParser jsonParser = createParser();
 Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext, null);
 fail("Should have thrown exception");
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNativeTypeIdUnknownId() throws Exception {
 JsonParser jsonParser = createParser();
 jsonParser.setValue("{\"typeId\":\"invalidTypeId\"}");
 Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext, null);
 fail("Should have thrown exception");
 }

 @Test
 public void testDeserializeWithNativeTypeIdKnownId() throws Exception {
 JsonParser jsonParser = createParser();
 jsonParser.setValue("{\"typeId\":\"java.lang.String\"}");
 String result = (String) typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext, null);
 assertEquals("", result);
 }

 private JsonParser createParser() throws IOException {
 JsonFactory jsonFactory = new JsonFactory();
 return jsonFactory.createParser("{}");
 }
}