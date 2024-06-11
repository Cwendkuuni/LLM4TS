package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsPropertyTypeDeserializerTest {

 private ObjectMapper objectMapper;
 private AsPropertyTypeDeserializer asPropertyTypeDeserializer;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 AsPropertyTypeDeserializer asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(
 Object.class, null, "_type", true, Object.class, As.PROPERTY);
 this.asPropertyTypeDeserializer = asPropertyTypeDeserializer;
 }

 @After
 public void teardown() throws Exception {
 objectMapper = null;
 asPropertyTypeDeserializer = null;
 }

 @Test
 public void testDeserializeTypedFromObjectWithTypeId() throws Exception {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("_type", "com.example.TestClass");
 JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);
 Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, new TestContext());
 assertInstanceOf(TestClass.class, result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithoutTypeId() throws Exception {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);
 Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, new TestContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithTokenBuffer() throws Exception {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("_type", "com.example.TestClass");
 JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);
 TokenBuffer tokenBuffer = new TokenBuffer(jsonParser, new TestContext());
 Object result = asPropertyTypeDeserializer.deserializeTypedForId(jsonParser, new TestContext(), tokenBuffer);
 assertInstanceOf(TestClass.class, result);
 }

 @Test
 public void testDeserializeTypedFromObjectWithNullTokenBuffer() throws Exception {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("_type", "com.example.TestClass");
 JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);
 Object result = asPropertyTypeDeserializer.deserializeTypedForId(jsonParser, new TestContext(), null);
 assertInstanceOf(TestClass.class, result);
 }

 @Test
 public void testDeserializeTypedUsingDefaultImpl() throws Exception {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);
 Object result = asPropertyTypeDeserializer.deserializeTypedUsingDefaultImpl(jsonParser, new TestContext(), null);
 assertInstanceOf(Object.class, result);
 }

 @Test
 public void testDeserializeTypedFromAny() throws Exception {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);
 Object result = asPropertyTypeDeserializer.deserializeTypedFromAny(jsonParser, new TestContext());
 assertInstanceOf(Object.class, result);
 }

 @Test
 public void testDeserializeTypedFromArray() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(String[].class, new StringArrayDeserializer());
 objectMapper.registerModule(module);
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("values", new ObjectNode().putArray("value1", "value2"));
 JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);
 TypeDeserializer deserializer = asPropertyTypeDeserializer.forProperty(null);
 Object result = deserializer.deserializeTypedFromArray(jsonParser, new TestContext());
 assertArrayEquals(new String[]{"value1", "value2"}, (String[])result);
 }

 @Test
 public void testDeserializeTypedFromScalar() throws Exception {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("value", "TestValue");
 JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);
 Object result = asPropertyTypeDeserializer.deserializeTypedFromScalar(jsonParser, new TestContext());
 assertEquals("TestValue", result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithMissingProperty() throws Exception {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);
 asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, new TestContext());
 fail("Expected JsonMappingException to be thrown");
 }

 @Test
 public void testDeserializeWithInvalidTypeId() throws Exception {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("_type", "com.example.InvalidTypeId");
 JsonParser jsonParser = objectMapper.treeAsTokens(jsonNode);
 asPropertyTypeDeserializer.deserializeTypedForId(jsonParser, new TestContext(), null);
 fail("Expected JsonMappingException to be thrown");
 }

 private static class TestContext extends DeserializationContext {
 }

 private static class StringArrayDeserializer extends StdDeserializer<String[]> {
 protected StringArrayDeserializer() {
 this(null);
 }

 protected StringArrayDeserializer(Class<?> vc) {
 super(String[].class, vc);
 }

 @Override
 public StringArray deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new String[]{p.getText()};
 }
 }
}