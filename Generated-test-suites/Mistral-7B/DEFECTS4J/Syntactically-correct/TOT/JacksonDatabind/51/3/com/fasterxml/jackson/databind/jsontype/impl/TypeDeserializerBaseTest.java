package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase typeDeserializer;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() throws Exception {
 ObjectMapper.SetupHelper setupHelper = new ObjectMapper().setupHandler();
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(JsonType.constructType(String.class), new StandardTypeIdResolver(), "", true, null);
 setupHelper.setTypeDeserializers(typeDeserializerBase);
 objectMapper = setupHelper.build();
 this.typeDeserializer = typeDeserializerBase;
 }

 @Test
 public void testForProperty() throws Exception {
 BeanProperty property = new BeanProperty(new Object(), "name");
 TypeDeserializer typeDeserializerForProperty = typeDeserializer.forProperty(property);
 assertEquals(String.class, typeDeserializerForProperty.getType(objectMapper.getTypeFactory()));
 }

 @Test
 public void testGetTypeInclusion() throws Exception {
 assertEquals(JsonTypeInfo.As.WRAP_INCLUDE, typeDeserializer.getTypeInclusion());
 }

 @Test
 public void testBaseTypeName() throws Exception {
 assertEquals("java.lang.String", typeDeserializer.baseTypeName());
 }

 @Test
 public void testPropertyName() throws Exception {
 assertEquals("", typeDeserializer.getPropertyName());
 }

 @Test
 public void testTypeIdResolver() throws Exception {
 TypeIdResolver typeIdResolver = typeDeserializer.getTypeIdResolver();
 assertNotNull(typeIdResolver);
 }

 @Test
 public void testDefaultImpl() throws Exception {
 assertEquals(null, typeDeserializer.getDefaultImpl());
 }

 @Test
 public void testFindDeserializerNullTypeId() throws Exception {
 assertNull(typeDeserializer._findDeserializer(objectMapper.getDeserializationContext(), null));
 }

 @Test
 public void testFindDeserializerEmptyTypeId() throws Exception {
 assertNull(typeDeserializer._findDeserializer(objectMapper.getDeserializationContext(), ""));
 }

 @Test(expected = IOException.class)
 public void testFindDeserializerUnknownTypeId() throws Exception {
 typeDeserializer._findDeserializer(objectMapper.getDeserializationContext(), "UnknownTypeId");
 }

 @Test
 public void testFindDefaultImplDeserializer() throws Exception {
 Class<?> defaultImpl = typeDeserializer._defaultImpl.getRawClass();
 assertEquals(Void.class, defaultImpl);
 }

 @Test
 public void testHandleUnknownTypeId() throws Exception {
 TypeIdResolver standardTypeIdResolver = new StandardTypeIdResolver();
 JavaType javaType = typeDeserializer._handleUnknownTypeId(objectMapper.getDeserializationContext(), "UnknownTypeId", standardTypeIdResolver, typeDeserializer._baseType);
 assertNull(javaType);
 }

 @Test
 public void testDeserializeWithNativeTypeIdNull() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{}");
 Object result = typeDeserializer._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeWithNativeTypeIdString() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"typeId\":\"java.lang.String\"}");
 Object result = typeDeserializer._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext(), "java.lang.String");
 assertEquals("", result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNativeTypeIdUnknownTypeId() throws Exception {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"typeId\":\"UnknownTypeId\"}");
 typeDeserializer._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext(), "UnknownTypeId");
 }
}