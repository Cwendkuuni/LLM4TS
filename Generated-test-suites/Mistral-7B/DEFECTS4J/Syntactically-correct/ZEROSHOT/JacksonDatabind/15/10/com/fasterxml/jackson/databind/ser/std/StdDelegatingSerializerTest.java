package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

 private StdDelegatingSerializer<Object> serializer;
 private Object input;
 private JsonGenerator mockJsonGenerator;
 private SerializerProvider mockSerializerProvider;
 private TypeSerializer mockTypeSerializer;
 private Converter<Object, ?> mockConverter;
 private JavaType mockDelegateType;
 private JsonSerializer<Object> mockDelegateSerializer;

 @Before
 public void setUp() {
 mockJsonGenerator = mock(JsonGenerator.class);
 mockSerializerProvider = mock(SerializerProvider.class);
 mockTypeSerializer = mock(TypeSerializer.class);
 mockConverter = mock(Converter.class);
 mockDelegateType = mock(JavaType.class);
 mockDelegateSerializer = mock(JsonSerializer.class);
 serializer = new StdDelegatingSerializer<Object>(mockConverter, mockDelegateType, mockDelegateSerializer);
 }

 @Test
 public void testSerialization() throws JsonGenerationException, JsonMappingException {
 // Given
 when(mockConverter.convert(input)).thenReturn(new Object());
 when(mockDelegateSerializer.serialize(any(), eq(mockJsonGenerator), eq(mockSerializerProvider)))
 .thenReturn(null);

 // When
 serializer.serialize(input, mockJsonGenerator, mockSerializerProvider);

 // Then
 verify(mockDelegateSerializer, times(1)).serialize(any(), eq(mockJsonGenerator), eq(mockSerializerProvider));
 }

 @Test
 public void testSerializationWithType() throws JsonGenerationException, JsonMappingException {
 // Given
 when(mockConverter.convert(input)).thenReturn(new Object());
 when(mockDelegateSerializer.serializeWithType(any(), eq(mockJsonGenerator), eq(mockSerializerProvider), eq(mockTypeSerializer)))
 .thenReturn(null);

 // When
 serializer.serializeWithType(input, mockJsonGenerator, mockSerializerProvider, mockTypeSerializer);

 // Then
 verify(mockDelegateSerializer, times(1)).serializeWithType(any(), eq(mockJsonGenerator), eq(mockSerializerProvider), eq(mockTypeSerializer));
 }

 @Test
 public void testIsEmpty() {
 // Given
 when(mockDelegateSerializer.isEmpty(any(), any())).thenReturn(true);

 // When
 boolean result = serializer.isEmpty(mockSerializerProvider, null);

 // Then
 assertTrue(result);
 }

 @Test
 public void testGetSchema() {
 // Given
 when(mockDelegateSerializer.getSchema(any(), any())).thenReturn(new Object());

 // When
 JsonNode result = serializer.getSchema(mockSerializerProvider, Object.class);

 // Then
 assertEquals(new Object(), result);
 }

 @Test
 public void testGetSchemaWithIsOptional() {
 // Given
 when(mockDelegateSerializer.getSchema(any(), any(), anyBoolean())).thenReturn(new Object());

 // When
 JsonNode result = serializer.getSchema(mockSerializerProvider, Object.class, true);

 // Then
 assertEquals(new Object(), result);
 }

 @Test
 public void testAcceptJsonFormatVisitor() {
 // Given
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 when(mockDelegateSerializer.acceptJsonFormatVisitor(eq(visitor), any())).thenReturn(null);

 // When
 serializer.acceptJsonFormatVisitor(visitor, Object.class);

 // Then
 verify(mockDelegateSerializer, times(1)).acceptJsonFormatVisitor(eq(visitor), any());
 }

 @Test
 public void testConvertValue() throws Exception {
 // Given
 when(mockConverter.convert(input)).thenReturn(new Object());

 // When
 Object result = serializer.convertValue(input);

 // Then
 assertEquals(new Object(), result);
 }

}