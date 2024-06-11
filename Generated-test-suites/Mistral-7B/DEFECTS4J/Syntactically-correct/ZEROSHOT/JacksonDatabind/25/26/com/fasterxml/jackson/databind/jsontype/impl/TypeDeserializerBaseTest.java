package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase typeDeserializerBase;
 private JsonDeserializer<Object> jsonDeserializerMock;
 private TypeIdResolver typeIdResolverMock;
 private DeserializationContext deserializationContextMock;

 @Before
 public void setUp() {
 typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, Object.class) {
 @Override
 public JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
 return jsonDeserializerMock;
 }
 };
 typeIdResolverMock = mock(TypeIdResolver.class);
 deserializationContextMock = mock(DeserializationContext.class);
 jsonDeserializerMock = mock(JsonDeserializer.class);
 }

 @Test
 public void testForProperty() {
 BeanProperty beanPropertyMock = mock(BeanProperty.class);
 TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(beanPropertyMock);
 assertEquals(TypeDeserializerBase.class, typeDeserializer.getClass());
 }

 @Test
 public void testGetTypeInclusion() {
 JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
 assertEquals(JsonTypeInfo.As.WRAP_EXISTING_ARRAY_TYPES, typeInclusion);
 }

 @Test
 public void testBaseTypeName() {
 String baseTypeName = typeDeserializerBase.baseTypeName();
 assertEquals("java.lang.Object", baseTypeName);
 }

 @Test
 public void testPropertyName() {
 String propertyName = typeDeserializerBase.getPropertyName();
 assertNull(propertyName);
 }

 @Test
 public void testTypeIdResolver() {
 TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();
 assertEquals(typeIdResolverMock, typeDeserializerBase.getTypeIdResolver());
 }

 @Test
 public void testDefaultImpl() {
 Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
 assertNull(defaultImpl);
 }

 @Test
 public void testToString() {
 String toString = typeDeserializerBase.toString();
 assertTrue(toString.contains("[TypeDeserializerBase"));
 }

 @Test
 public void testFindDeserializerWithNullTypeId() throws IOException {
 when(deserializationContextMock.findContextualValueDeserializer(any(), any())).thenReturn(jsonDeserializerMock);
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(deserializationContextMock, null);
 assertEquals(jsonDeserializerMock, deserializer);
 }

 @Test
 public void testFindDeserializerWithTypeId() throws IOException {
 String typeId = "TestTypeId";
 when(typeIdResolverMock.idFromValue(any(), any())).thenReturn(typeId);
 when(deserializationContextMock.findContextualValueDeserializer(any(), any())).thenReturn(jsonDeserializerMock);
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(deserializationContextMock, "TestTypeId");
 assertEquals(jsonDeserializerMock, deserializer);
 }

 @Test(expected = IOException.class)
 public void testHandleUnknownTypeId() throws IOException {
 when(typeIdResolverMock.idFromValue(any(), any())).thenThrow(new IOException("TypeId resolution failed"));
 IOException exception = (IOException) typeDeserializerBase._handleUnknownTypeId(deserializationContextMock, "TestTypeId", typeIdResolverMock, typeDeserializerBase.getBaseType());
 assertEquals("TypeId resolution failed", exception.getMessage());
 }
}