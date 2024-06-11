package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsonschema.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class StdSerializerTest {

 private StdSerializer<String> stringSerializer;
 private JsonSerializerProvider provider;
 private JsonNodeFactory nodeFactory;

 @Before
 public void setUp() {
 stringSerializer = new StdSerializer<String>(String.class) {
 @Override
 public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
 jgen.writeString(value);
 }
 };
 provider = mock(SerializerProvider.class);
 nodeFactory = new StandardJsonNodeFactory();
 }

 @Test
 public void testHandleType() throws JsonMappingException {
 Class<String> handledType = stringSerializer.handledType();
 assertEquals(String.class, handledType);
 }

 @Test
 public void testEmptySerializer() throws JsonMappingException {
 assertNull(stringSerializer.getSchema(provider, null));
 }

 @Test
 public void testStringSchema() throws JsonMappingException {
 ObjectNode schema = (ObjectNode) stringSerializer.getSchema(provider, String.class);
 assertEquals("string", schema.get("type").asText());
 }

 @Test
 public void testStringOptionalSchema() throws JsonMappingException {
 ObjectNode schema = (ObjectNode) stringSerializer.getSchema(provider, String.class, false);
 assertEquals("string", schema.get("type").asText());
 assertTrue(schema.get("required").asBoolean());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 ObjectNode schema = createSchemaNode("string");
 when(provider.schemaInstanceCreator()).thenReturn(nodeFactory);
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 when(visitor.expectAnyFormat(any(JavaType.class))).thenReturn(null);
 stringSerializer.acceptJsonFormatVisitor(visitor, String.class);
 verifyNoMoreInteractions(visitor);
 }

 @Test(expected = JsonMappingException.class)
 public void testAcceptJsonFormatVisitorWithNullTypeHint() throws JsonMappingException {
 ObjectNode schema = createSchemaNode("string");
 when(provider.schemaInstanceCreator()).thenReturn(nodeFactory);
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 stringSerializer.acceptJsonFormatVisitor(visitor, null);
 }

 @Test
 public void testWrapAndThrow() throws IOException {
 Throwable t = new RuntimeException("Test exception");
 stringSerializer.wrapAndThrow(provider, t, null, null);
 verify(provider).signalProcessingError(any(JsonProcessingException.class));
 }

 @Test
 public void testWrapAndThrowWithJsonMappingException() throws IOException {
 JsonMappingException jsonMappingException = new JsonMappingException("Test exception");
 stringSerializer.wrapAndThrow(provider, jsonMappingException, null, null);
 verify(provider).wrapAndThrow(jsonMappingException);
 }

 @Test
 public void testIsDefaultSerializer() {
 assertTrue(StdSerializer.class.isInstance(stringSerializer));
 assertTrue(StdSerializer.class.isInstance(stringSerializer));
 assertFalse(StdSerializer.class.isInstance(new ObjectMapper()));
 }

 @Test
 public void testFindConvertingContentSerializer() throws JsonMappingException {
 // test with no existing serializer
 JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
 when(provider.findValueSerializer(Object.class)).thenReturn(delegateSerializer);
 JsonSerializer<String> convertingSerializer = new StdSerializer<String>(String.class) {
 @Override
 public JsonSerializer<?> createContentSerializer(SerializerProvider provider, Type typeToSerialize, BeanProperty property, JsonSerializer<?> serializer) throws JsonMappingException {
 return new StdSerializer<Object>(Object.class) {
 @Override
 public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
 jgen.writeNumber(1);
 }
 };
 }
 };
 JsonSerializer<String> newSerializer = new StdSerializer<String>(String.class) {
 @Override
 public JsonSerializer<?> createContentSerializer(SerializerProvider provider, Type typeToSerialize, BeanProperty property, JsonSerializer<?> serializer) throws JsonMappingException {
 return convertingSerializer;
 }
 };
 assertEquals(convertingSerializer, stringSerializer.findConvertingContentSerializer(provider, null, null));

 // test with existing serializer
 JsonSerializer<String> existingSerializer = new StdSerializer<String>(String.class);
 assertEquals(existingSerializer, stringSerializer.findConvertingContentSerializer(provider, null, null));
 }

 @Test
 public void testFindPropertyFilter() throws JsonMappingException {
 PropertyFilter filter = mock(PropertyFilter.class);
 when(provider.getFilterProvider()).thenReturn(new SimpleFilterProvider());
 when(provider.getFilterProvider().findPropertyFilter("test", any(Object.class))).thenReturn(filter);
 assertEquals(filter, stringSerializer.findPropertyFilter(provider, "test", null));
 }

 @Test(expected = JsonMappingException.class)
 public void testFindPropertyFilterWithNullFilterId() throws JsonMappingException {
 when(provider.getFilterProvider()).thenReturn(new SimpleFilterProvider());
 stringSerializer.findPropertyFilter(provider, null, null);
 }

 private ObjectNode createSchemaNode(String type) {
 return nodeFactory.objectNode();
 }
}