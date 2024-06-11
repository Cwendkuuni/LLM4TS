package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private ObjectMapper objectMapper;
 private TypeDeserializerBase typeDeserializerBase;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 TypeIdResolver typeIdResolver = new TypeIdResolverBase();
 typeDeserializerBase = new TypeDeserializerBaseBase(
 JavaType.constructType(Object.class), typeIdResolver, "typeProperty", true, null);
 }

 @Test
 public void testForProperty() throws JsonProcessingException {
 BeanProperty property = new BeanProperty("testProperty", String.class);
 TypeDeserializer deserializer = typeDeserializerBase.forProperty(property);
 assertNotNull(deserializer);
 assertEquals(TypeDeserializerBase.class, deserializer.getClass());
 }

 @Test
 public void testGetTypeInclusion() {
 JsonTypeInfo.As inclusion = typeDeserializerBase.getTypeInclusion();
 assertEquals(JsonTypeInfo.As.WRAP_ROOT_VALUE, inclusion);
 }

 @Test
 public void testBaseTypeName() throws Exception {
 String baseTypeName = typeDeserializerBase.baseTypeName();
 assertEquals("java.lang.Object", baseTypeName);
 }

 @Test
 public void testGetPropertyName() {
 String propertyName = typeDeserializerBase.getPropertyName();
 assertEquals("typeProperty", propertyName);
 }

 @Test
 public void testgetTypeIdResolver() {
 TypeIdResolver idResolver = typeDeserializerBase.getTypeIdResolver();
 assertNotNull(idResolver);
 }

 @Test
 public void testDefaultImpl() throws Exception {
 Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
 assertNull(defaultImpl);
 }

 @Test
 public void testFindDeserializer() throws Exception {
 String typeId = "com.example.MyClass";
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(
 objectMapper.getDeserializationContext(), typeId);
 assertNull(deserializer);
 }

 @Test
 public void testFindDefaultImplDeserializer() throws Exception {
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(
 objectMapper.getDeserializationContext());
 assertNull(deserializer);
 }

 @Test(expected = JsonMappingException.class)
 public void testHandleUnknownTypeId() throws Exception {
 String typeId = "unknownTypeId";
 typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), typeId);
 fail("Should have thrown JsonMappingException");
 }

 @Test
 public void testHandleMissingTypeId() throws Exception {
 String extraDesc = "missing type id";
 JavaType javaType = typeDeserializerBase._handleMissingTypeId(
 objectMapper.getDeserializationContext(), extraDesc);
 assertNotNull(javaType);
 assertEquals(TypeDeserializerBase.class, javaType.getRawClass());
 }
}