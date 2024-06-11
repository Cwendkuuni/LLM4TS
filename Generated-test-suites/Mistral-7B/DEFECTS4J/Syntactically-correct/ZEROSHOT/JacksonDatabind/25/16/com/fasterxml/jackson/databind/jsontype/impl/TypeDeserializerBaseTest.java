package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase testee;
 private DeserializationContext context;
 private TypeIdResolver typeIdResolver;
 private BeanProperty beanProperty;
 private JsonParser jsonParser;

 @Before
 public void setUp() {
 context = mock(DeserializationContext.class);
 typeIdResolver = mock(TypeIdResolver.class);
 jsonParser = mock(JsonParser.class);
 beanProperty = mock(BeanProperty.class);
 testee = new TypeDeserializerBase(Null.class.getGenericSuperclass(), typeIdResolver, null, false, null) {
 @Override
 protected TypeDeserializer forProperty(BeanProperty prop) {
 return this;
 }

 @Override
 public JsonTypeInfo.As getTypeInclusion() {
 return JsonTypeInfo.As.WRAP_ONLY;
 }
 };
 }

 @Test
 public void testBaseTypeName() {
 assertEquals("java.lang.Object", testee.baseTypeName());
 }

 @Test
 public void testPropertyName() {
 assertNull(testee.getPropertyName());
 }

 @Test
 public void testDefaultImplDeserializerIsNull() throws Exception {
 assertTrue(testee._defaultImplDeserializer == null);
 }

 @Test
 public void testDefaultImplDeserializerIsNotNull() throws Exception {
 JavaType javaType = mock(JavaType.class);
 JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
 when(javaType.getRawClass()).thenReturn(String.class);
 when(testee.findContextualValueDeserializer(javaType, beanProperty)).thenReturn(deserializer);
 testee = new TypeDeserializerBase(testee, beanProperty);
 assertTrue(testee._defaultImplDeserializer != null);
 assertEquals(deserializer, testee._defaultImplDeserializer);
 }

 @Test
 public void testFindDeserializer() throws Exception {
 String typeId = "typeId";
 when(typeIdResolver.typeFromId(context, typeId)).thenReturn(mock(JavaType.class));
 JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
 when(testee._deserializers.get(typeId)).thenReturn(deserializer);
 Object result = testee._findDeserializer(context, typeId);
 assertEquals(deserializer, result);
 }

 @Test
 public void testFindDeserializerWhenNull() throws Exception {
 Object result = testee._findDeserializer(context, null);
 assertNull(result);
 }

 @Test
 public void testFindDefaultImplDeserializer() throws Exception {
 assertTrue(testee._findDefaultImplDeserializer(context) == NullifyingDeserializer.instance);
 }

 @Test
 public void testFindDefaultImplDeserializerWhenNotNull() throws Exception {
 JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
 when(testee.findContextualValueDeserializer(Null.class, beanProperty)).thenReturn(deserializer);
 testee = new TypeDeserializerBase(testee, beanProperty);
 assertEquals(deserializer, testee._defaultImplDeserializer);
 }

 @Test
 public void testFindDeserializerWithNativeTypeId() throws Exception {
 String typeId = "typeId";
 when(jsonParser.getTypeId()).thenReturn(typeId);
 JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
 when(testee._findDeserializer(context, typeId)).thenReturn(deserializer);
 Object result = testee._deserializeWithNativeTypeId(jsonParser, context);
 assertEquals(deserializer.deserialize(jsonParser, context), result);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindDeserializerWithUnknownTypeId() throws Exception {
 String typeId = "unknownTypeId";
 when(typeIdResolver.typeFromId(context, typeId)).thenReturn(null);
 JsonMappingException exception = assertThrows(JsonMappingException.class, () -> testee._deserializeWithNativeTypeId(jsonParser, context, typeId));
 assertEquals("Unknown type id: [" + typeId + "]", exception.getMessage());
 }
}