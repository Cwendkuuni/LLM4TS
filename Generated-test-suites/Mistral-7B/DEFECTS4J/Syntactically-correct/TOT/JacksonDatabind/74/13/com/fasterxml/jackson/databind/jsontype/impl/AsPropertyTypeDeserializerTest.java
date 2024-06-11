package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsPropertyTypeDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDeserializeTypedFromObjectWithProperty() throws Exception {
 JavaType javaType = MAPPER.getTypeFactory().constructType(MyClassWithTypeProperty.class);
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 javaType, MAPPER.getTypeIdResolver(), "typePropertyName", true, MyDefaultImpl.class, As.PROPERTY);

 String json = "{\"typePropertyName\":\"MyType\",\"someField\":\"Value\"}";
 Object result = deserializer.deserializeTypedFromObject(MAPPER.getFactory().createParser(json), MAPPER.getDeserializationContext());
 assertTrue(result instanceof MyType);
 }

 @Test
 public void testDeserializeTypedFromObjectWithoutProperty() throws Exception {
 JavaType javaType = MAPPER.getTypeFactory().constructType(MyClassWithoutTypeProperty.class);
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 javaType, MAPPER.getTypeIdResolver(), "typePropertyName", true, MyDefaultImpl.class, As.PROPERTY);

 String json = "{\"someField\":\"Value\"}";
 Object result = deserializer.deserializeTypedFromObject(MAPPER.getFactory().createParser(json), MAPPER.getDeserializationContext());
 assertNotNull(result);
 assertFalse(result instanceof MyType);
 }

 @Test
 public void testDeserializeTypedFromObjectWithWrongPropertyName() throws Exception {
 JavaType javaType = MAPPER.getTypeFactory().constructType(MyClassWithTypeProperty.class);
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 javaType, MAPPER.getTypeIdResolver(), "wrongTypePropertyName", true, MyDefaultImpl.class, As.PROPERTY);

 String json = "{\"wrongTypePropertyName\":\"MyType\",\"someField\":\"Value\"}";
 assertThrows(JsonMappingException.class, () -> deserializer.deserializeTypedFromObject(MAPPER.getFactory().createParser(json), MAPPER.getDeserializationContext()));
 }

 @Test
 public void testDeserializeTypedFromObjectWithNullTypeId() throws Exception {
 JavaType javaType = MAPPER.getTypeFactory().constructType(MyClassWithTypeProperty.class);
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 javaType, MAPPER.getTypeIdResolver(), "typePropertyName", true, MyDefaultImpl.class, As.PROPERTY);

 String json = "{}";
 assertNull(deserializer.deserializeTypedFromObject(MAPPER.getFactory().createParser(json), MAPPER.getDeserializationContext()));
 }

 @Test
 public void testDeserializeTypedFromObjectWithInvalidTypeId() throws Exception {
 JavaType javaType = MAPPER.getTypeFactory().constructType(MyClassWithTypeProperty.class);
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 javaType, MAPPER.getTypeIdResolver(), "typePropertyName", true, MyDefaultImpl.class, As.PROPERTY);

 String json = "{\"typePropertyName\":\"InvalidTypeId\",\"someField\":\"Value\"}";
 assertThrows(JsonMappingException.class, () -> deserializer.deserializeTypedFromObject(MAPPER.getFactory().createParser(json), MAPPER.getDeserializationContext()));
 }

 @Test
 public void testDeserializeTypedFromObjectWithInvalidToken() throws Exception {
 JavaType javaType = MAPPER.getTypeFactory().constructType(MyClassWithTypeProperty.class);
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 javaType, MAPPER.getTypeIdResolver(), "typePropertyName", true, MyDefaultImpl.class, As.PROPERTY);

 String json = "{\"typePropertyName\":[\"MyType\",\"someField\":\"Value\"]}";
 assertThrows(JsonMappingException.class, () -> deserializer.deserializeTypedFromObject(MAPPER.getFactory().createParser(json), MAPPER.getDeserializationContext()));
 }

 @Test
 public void testDeserializeTypedFromObjectWithDefaultImpl() throws Exception {
 JavaType javaType = MAPPER.getTypeFactory().constructType(MyClassWithTypeProperty.class);
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 javaType, MAPPER.getTypeIdResolver(), "typePropertyName", true, MyDefaultImpl.class, As.PROPERTY);

 String json = "{\"someField\":\"Value\"}";
 Object result = deserializer.deserializeTypedFromObject(MAPPER.getFactory().createParser(json), MAPPER.getDeserializationContext());
 assertNotNull(result);
 assertInstanceOf(MyDefaultImpl.class, result);
 }
}