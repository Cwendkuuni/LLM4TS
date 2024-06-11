package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class AsWrapperTypeDeserializerTest {

 private AsWrapperTypeDeserializer deserializer;
 private ObjectMapper objectMapper;
 private TypeIdResolver typeIdResolver;
 private JsonFactory jsonFactory;
 private JavaType javaType;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 jsonFactory = objectMapper.getFactory();
 typeIdResolver = mock(TypeIdResolver.class);
 javaType = mock(JavaType.class);
 deserializer = new AsWrapperTypeDeserializer(javaType, typeIdResolver, "_typePropertyName", true, Object.class);
 }

 @Test
 public void testDeserializeTypedFromObject() throws IOException {
 JsonParser jsonParser = createMockParser();
 when(typeIdResolver.resolveDeserializer(eq(javaType), any(DeserializationContext.class)))
 .thenReturn(mock(JsonDeserializer.class));
 Object result = deserializer.deserializeTypedFromObject(jsonParser, mockContext());
 verify(jsonParser).nextToken();
 verify(jsonParser).nextToken();
 verify(jsonParser).getText();
 verify(jsonParser).nextToken();
 verify(jsonParser).nextToken();
 verify(jsonParser, times(2)).close();
 assertEquals(Object.class, result.getClass());
 }

 @Test
 public void testDeserializeTypedFromArray() throws IOException {
 JsonParser jsonParser = createMockParser();
 when(typeIdResolver.resolveDeserializer(eq(javaType), any(DeserializationContext.class)))
 .thenReturn(mock(JsonDeserializer.class));
 Object result = deserializer.deserializeTypedFromArray(jsonParser, mockContext());
 verify(jsonParser).nextToken();
 verify(jsonParser).nextToken();
 verify(jsonParser).getText();
 verify(jsonParser, times(2)).close();
 assertEquals(Object.class, result.getClass());
 }

 @Test
 public void testDeserializeTypedFromScalar() throws IOException {
 JsonParser jsonParser = createMockParser();
 when(typeIdResolver.resolveDeserializer(eq(javaType), any(DeserializationContext.class)))
 .thenReturn(mock(JsonDeserializer.class));
 Object result = deserializer.deserializeTypedFromScalar(jsonParser, mockContext());
 verify(jsonParser).nextToken();
 verify(jsonParser).close();
 assertEquals(Object.class, result.getClass());
 }

 @Test
 public void testDeserializeTypedFromAny() throws IOException {
 JsonParser jsonParser = createMockParser();
 when(typeIdResolver.resolveDeserializer(eq(javaType), any(DeserializationContext.class)))
 .thenReturn(mock(JsonDeserializer.class));
 Object result = deserializer.deserializeTypedFromAny(jsonParser, mockContext());
 verify(jsonParser).nextToken();
 verify(jsonParser).nextToken();
 verify(jsonParser).getText();
 verify(jsonParser).nextToken();
 verify(jsonParser).nextToken();
 verify(jsonParser, times(2)).close();
 assertEquals(Object.class, result.getClass());
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws IOException {
 JsonParser jsonParser = createMockParser();
 when(typeIdResolver.resolveDeserializer(eq(javaType), any(DeserializationContext.class)))
 .thenReturn(mock(JsonDeserializer.class));
 when(jsonParser.canReadTypeId()).thenReturn(true);
 when(jsonParser.getTypeId()).thenReturn("myTypeId");
 Object result = deserializer.deserializeTypedFromObject(jsonParser, mockContext());
 verify(jsonParser).nextToken();
 verify(jsonParser).nextToken();
 verify(jsonParser).getText();
 verify(jsonParser, times(2)).close();
 verify(typeIdResolver).resolveDeserializer(eq(javaType), any(DeserializationContext.class));
 verify(typeIdResolver).resolveTypeId(jsonParser, javaType, mockContext());
 assertEquals("myTypeId", result.getClass().getName());
 }

 private JsonParser createMockParser() {
 return mock(JsonParser.class);
 }

 private DeserializationContext mockContext() {
 return mock(DeserializationContext.class);
 }
}