package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ObjectMapperUtil;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {

 private ObjectMapper objectMapper;
 private TypeIdResolver typeIdResolver;
 private AsWrapperTypeDeserializer asWrapperTypeDeserializer;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 typeIdResolver = ObjectMapperUtil.createDefaultTypeIdResolver(objectMapper);
 asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
 JavaType.construct(String.class), typeIdResolver, "_class", true, String.class);
 }

 @Test
 public void testDeserializeTypedFromObject() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"type\":\"java.lang.String\",\"value\":\"test\"}");
 Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, new TestContext());
 assertEquals("test", result);
 }

 @Test
 public void testDeserializeTypedFromArray() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("[\"" + AsWrapperTypeDeserializer.class.getName() + "\",{\"type\":\"java.lang.String\",\"value\":\"test\"}]");
 Object result = asWrapperTypeDeserializer.deserializeTypedFromArray(jsonParser, new TestContext());
 assertEquals("test", result);
 }

 @Test
 public void testDeserializeTypedFromScalar() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"type\":\"java.lang.String\",\"value\":\"test\"}");
 jsonParser.nextToken(); // consume opening brace
 Object result = asWrapperTypeDeserializer.deserializeTypedFromScalar(jsonParser, new TestContext());
 assertEquals("test", result);
 }

 @Test
 public void testDeserializeTypedFromAny() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"type\":\"java.lang.Object\",\"value\":\"test\"}");
 Object result = asWrapperTypeDeserializer.deserializeTypedFromAny(jsonParser, new TestContext());
 assertEquals("test", result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithInvalidObject() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"type\":\"java.lang.String\",\"value\":\"test\"}");
 jsonParser.nextToken(); // consume opening brace
 jsonParser.nextToken(); // consume field name
 jsonParser.nextToken(); // consume string with type id
 jsonParser.nextToken(); // consume comma
 jsonParser.nextToken(); // consume opening brace
 jsonParser.nextToken(); // consume closing brace
 asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, new TestContext());
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithInvalidArray() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("[{\"type\":\"java.lang.String\",\"value\":\"test\"},\"test\"]");
 asWrapperTypeDeserializer.deserializeTypedFromArray(jsonParser, new TestContext());
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithInvalidScalar() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"type\":\"java.lang.String\",\"value\":\"test\"}");
 jsonParser.nextToken(); // consume opening brace
 jsonParser.nextToken(); // consume field name
 jsonParser.nextToken(); // consume string with type id
 jsonParser.nextToken(); // consume comma
 jsonParser.nextToken(); // consume opening brace
 jsonParser.nextToken(); // consume value
 jsonParser.nextToken(); // consume closing brace
 asWrapperTypeDeserializer.deserializeTypedFromScalar(jsonParser, new TestContext());
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithInvalidAny() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"type\":\"java.lang.Object\",\"value\":\"test\"}");
 jsonParser.nextToken(); // consume opening brace
 jsonParser.nextToken(); // consume field name
 jsonParser.nextToken(); // consume string with type id
 jsonParser.nextToken(); // consume comma
 jsonParser.nextToken(); // consume opening brace
 jsonParser.nextToken(); // consume closing brace
 asWrapperTypeDeserializer.deserializeTypedFromAny(jsonParser, new TestContext());
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNullTypeId() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"type\":null,\"value\":\"test\"}");
 asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, new TestContext());
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithEmptyTypeId() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"type\":\"\",\"value\":\"test\"}");
 asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, new TestContext());
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithInvalidTypeId() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"type\":\"java.lang.InvalidType\",\"value\":\"test\"}");
 asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, new TestContext());
 fail("Expected IOException");
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"type\":\"_class\",\"value\":{\"name\":\"java.lang.String\",\"values\":[],\"properties\":{}}}");
 Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, new TestContext());
 assertEquals("test", result);
 }

 @Test
 public void testDeserializeWithMergedTypeId() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"type\":\"java.lang.String\",\"_class\":\"java.lang.Object\",\"value\":\"test\"}");
 Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, new TestContext());
 assertEquals("test", result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithMissingEndObject() throws IOException {
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"type\":\"java.lang.String\",\"value\":\"test\"}");
 jsonParser.nextToken(); // consume opening brace
 jsonParser.nextToken(); // consume field name
 jsonParser.nextToken(); // consume string with type id
 jsonParser.nextToken(); // consume comma
 jsonParser.nextToken(); // consume value
 asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, new TestContext());
 fail("Expected IOException");
 }

 private static class TestContext extends DeserializationContext {
 @Override
 public ObjectMapper _defaultInstance() {
 return objectMapper;
 }
 }
}