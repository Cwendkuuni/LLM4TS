package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsonschema.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSerializerTest {

 private static final Class<Object> HANDLED_TYPE = Object.class;

 @Test
 public void testStdSerializerConstructor() {
 StdSerializer<Object> serializer = new StdSerializer<>(HANDLED_TYPE);
 assertEquals(Object.class, serializer.handledType());
 }

 @Test
 public void testStdSerializerConstructorWithType() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Object.class);
 StdSerializer<Object> serializer = new StdSerializer<>(javaType);
 assertEquals(Object.class, serializer.handledType());
 }

 @Test
 public void testStdSerializerConstructorWithTypeAndDummy() {
 Class<?> type = Object.class;
 StdSerializer<Object> serializer = new StdSerializer<>(type, false);
 assertEquals(Object.class, serializer.handledType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStdSerializerInvalidTypeArgument() {
 StdSerializer<String> serializer = new StdSerializer<>(Object.class);
 fail("Should not be able to instantiate StdSerializer with wrong type argument.");
 }

 @Test
 public void testGetSchemaStringType() {
 StdSerializer<Object> serializer = new StdSerializer<>(HANDLED_TYPE);
 JsonNode schema = serializer.getSchema(null, Object.class);
 assertEquals("string", schema.get("type").asText());
 }

 @Test
 public void testGetSchemaStringTypeOptionalFalse() {
 StdSerializer<Object> serializer = new StdSerializer<>(HANDLED_TYPE);
 JsonNode schema = serializer.getSchema(null, Object.class, false);
 assertEquals("string", schema.get("type").asText());
 assertEquals(false, schema.get("required").asBoolean());
 }

 @Test
 public void testGetSchemaStringTypeOptionalTrue() {
 StdSerializer<Object> serializer = new StdSerializer<>(HANDLED_TYPE);
 JsonNode schema = serializer.getSchema(null, Object.class, true);
 assertEquals("string", schema.get("type").asText());
 assertEquals(true, schema.get("required").asBoolean());
 }

 @Test
 public void testCreateObjectNode() {
 StdSerializer<Object> serializer = new StdSerializer<>(HANDLED_TYPE);
 ObjectNode node = serializer.createObjectNode();
 assertNotNull(node);
 }

 @Test
 public void testCreateSchemaNodeStringType() {
 StdSerializer<Object> serializer = new StdSerializer<>(HANDLED_TYPE);
 ObjectNode schemaNode = serializer.createSchemaNode("string");
 assertEquals("string", schemaNode.get("type").asText());
 }

 @Test
 public void testCreateSchemaNodeStringTypeOptionalFalse() {
 StdSerializer<Object> serializer = new StdSerializer<>(HANDLED_TYPE);
 ObjectNode schemaNode = serializer.createSchemaNode("string", false);
 assertEquals("string", schemaNode.get("type").asText());
 assertEquals(false, schemaNode.get("required").asBoolean());
 }

 @Test
 public void testCreateSchemaNodeStringTypeOptionalTrue() {
 StdSerializer<Object> serializer = new StdSerializer<>(HANDLED_TYPE);
 ObjectNode schemaNode = serializer.createSchemaNode("string", true);
 assertEquals("string", schemaNode.get("type").asText());
 assertEquals(true, schemaNode.get("required").asBoolean());
 }

 @Test
 public void testAcceptJsonFormatVisitor() {
 StdSerializer<Object> serializer = new StdSerializer<>(HANDLED_TYPE);
 JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null, null, null, null, null, null, null);
 serializer.acceptJsonFormatVisitor(visitor, Object.class);
 }

 @Test
 public void testWrapAndThrowNullProvider() throws IOException {
 StdSerializer<Object> serializer = new StdSerializer<>(HANDLED_TYPE);
 assertThrows(IllegalArgumentException.class, () -> serializer.wrapAndThrow(null, new RuntimeException("Test"), null, "fieldName"));
 }

 @Test
 public void testWrapAndThrowNullBean() throws IOException {
 StdSerializer<Object> serializer = new StdSerializer<>(HANDLED_TYPE);
 assertThrows(NullPointerException.class, () -> serializer.wrapAndThrow(null, new RuntimeException("Test"), null, "fieldName"));
 }

 @Test
 public void testWrapAndThrowNullFieldName() throws IOException {
 StdSerializer<Object> serializer = new StdSerializer<>(HANDLED_TYPE);
 assertThrows(NullPointerException.class, () -> serializer.wrapAndThrow(null, new RuntimeException("Test"), null, null));
 }

 @Test(expected = JsonMappingException.class)
 public void testWrapAndThrowIOException() throws IOException {
 StdSerializer<Object> serializer = new StdSerializer<>(HANDLED_TYPE);
 IOException ioException = new IOException("Test IOException");
 serializer.wrapAndThrow(null, ioException, null, "fieldName");
 }

 @Test(expected = JsonMappingException.class)
 public void testWrapAndThrowJsonMappingException() throws IOException {
 StdSerializer<Object> serializer = new StdSerializer<>(HANDLED_TYPE);
 JsonMappingException jsonMappingException = new JsonMappingException("Test JsonMappingException");
 serializer.wrapAndThrow(null, jsonMappingException, null, "fieldName");
 }

 @Test(expected = JsonMappingException.class)
 public void testWrapAndThrowJsonMappingExceptionWithPath() throws IOException {
 StdSerializer<Object> serializer = new StdSerializer<>(HANDLED_TYPE);
 JsonMappingException jsonMappingException = new JsonMappingException("Test JsonMappingException");
 serializer.wrapAndThrow(null, jsonMappingException, null, 0);
 }

 @Test(expected = JsonMappingException.class)
 public void testWrapAndThrowNonIOException() throws IOException {
 StdSerializer<Object> serializer = new StdSerializer<>(HANDLED_TYPE);
 RuntimeException runtimeException = new RuntimeException("Test RuntimeException");
 serializer.wrapAndThrow(null, runtimeException, null, "fieldName");
 }

 @Test
 public void testIsDefaultSerializer() {
 StdSerializer<Object> serializer = new StdSerializer<>(HANDLED_TYPE);
 assertTrue(StdSerializer.class.isInstance(new StdSerializer<>(String.class)));
 assertFalse(StdSerializer.class.isInstance(new StringSerializer()));
 assertTrue(StdSerializer.class.isInstance(serializer));
 }

 @Test
 public void testFindConvertingContentSerializer() throws JsonMappingException {
 ObjectNode testNode = JsonNodeFactory.instance.objectNode();
 testNode.put("foo", 123);
 JsonSerializer<?> existingSerializer = null;
 SerializerProvider provider = new SerializerProvider();
 provider.setDefaultSerializerProvider(true);
 StdSerializer<ObjectNode> serializer = new StdSerializer<ObjectNode>(ObjectNode.class) {
 @Override
 public void serialize(ObjectNode value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
 jgen.writeString("Test");
 }
 };
 JsonSerializer<?> converterSerializer = serializer.findConvertingContentSerializer(provider, null, testNode);
 assertNotNull(converterSerializer);
 assertEquals(IntegerSerializer.class, converterSerializer.getClass());
 existingSerializer = converterSerializer;

 provider.setContentSerializer(JsonNode.class, new DefaultStringSerializer());
 converterSerializer = serializer.findConvertingContentSerializer(provider, null, testNode);
 assertNotNull(converterSerializer);
 assertEquals(StringSerializer.class, converterSerializer.getClass());
 assertEquals(existingSerializer,provider.getSerializerProvider().getSerializerForClass(JsonNode.class));
}

 @Test
 public void testFindPropertyFilter() throws JsonMappingException {
 Object testObject = new Object() {{
 @JsonFilter("TestFilter")
 int testField = 123;
 }};
 SerializerProvider provider = new SerializerProvider();
 PropertyFilter filter = new PropertyFilter("TestFilter", Object.class, null);
 StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class);
 assertEquals(filter, serializer.findPropertyFilter(provider, "TestFilter", testObject));
}

 @Test(expected = JsonMappingException.class)
 public void testFindPropertyFilterNullFilterId() throws JsonMappingException {
 SerializerProvider provider = new SerializerProvider();
 StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class);
 serializer.findPropertyFilter(provider, null, null);
 fail("Should have thrown JsonMappingException.");
 }

 @Test(expected = JsonMappingException.class)
 public void testFindPropertyFilterNullValue() throws JsonMappingException {
 SerializerProvider provider = new SerializerProvider();
 StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class);
 serializer.findPropertyFilter(provider, "TestFilter", null);
 fail("Should have thrown JsonMappingException.");
 }

 @Test(expected = JsonMappingException.class)
 public void testFindPropertyFilterNullProvider() throws JsonMappingException {
 StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class);
 serializer.findPropertyFilter(null, "TestFilter", new Object());
 fail("Should have thrown JsonMappingException.");
 }
}