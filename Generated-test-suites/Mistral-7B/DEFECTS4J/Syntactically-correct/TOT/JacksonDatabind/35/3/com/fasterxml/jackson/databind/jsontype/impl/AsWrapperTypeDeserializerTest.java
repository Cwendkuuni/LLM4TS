package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AsWrapperTypeDeserializerTest {

 private ObjectMapper mapper;
 private AsWrapperTypeDeserializer deserializer;

 @Before
 public void setUp() throws Exception {
 mapper = new ObjectMapper();
 deserializer = new AsWrapperTypeDeserializer(
 MapperFeature.AUTO_DETECT_TYPES, mapper.getTypeIdResolver(), "_class", true, String.class);
 }

 @Test
 public void testDeserializeObject() throws IOException {
 String json = "{\"type\":\"java.lang.String\",\"value\":\"Hello World\"}";
 Object result = mapper.readValue(json, deserializer);
 assertEquals("Hello World", result);
 }

 @Test
 public void testDeserializeObjectWithNullTypeId() throws IOException {
 String json = "{\"value\":\"Hello World\"}";
 Object result = mapper.readValue(json, deserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeObjectWithInvalidTypeId() throws IOException {
 String json = "{\"type\":\"java.util.List\",\"value\":[1,2,3]}";
 Object result = mapper.readValue(json, deserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeObjectWithEmptyTypeId() throws IOException {
 String json = "{\"type\":\"\",\"value\":\"Hello World\"}";
 Object result = mapper.readValue(json, deserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeArray() throws IOException {
 String json = "[{\"type\":\"java.lang.String\",\"value\":\"Hello World\"}]";
 Object result = mapper.readValue(json, deserializer.forProperty(null));
 assertEquals(String.class, result.getClass());
 assertEquals("Hello World", ((Object[]) result)[0]);
 }

 @Test
 public void testDeserializeArrayWithNullTypeId() throws IOException {
 String json = "[{\"value\":\"Hello World\"}]";
 Object result = mapper.readValue(json, deserializer.forProperty(null));
 assertNull(result);
 }

 @Test
 public void testDeserializeArrayWithInvalidTypeId() throws IOException {
 String json = "[{\"type\":\"java.util.List\",\"value\":[1,2,3]}]";
 Object result = mapper.readValue(json, deserializer.forProperty(null));
 assertNull(result);
 }

 @Test
 public void testDeserializeArrayWithEmptyTypeId() throws IOException {
 String json = "[{\"type\":\"\",\"value\":\"Hello World\"}]";
 Object result = mapper.readValue(json, deserializer.forProperty(null));
 assertNull(result);
 }

 @Test
 public void testDeserializeScalar() throws IOException {
 String json = "\"Hello World\"";
 Object result = mapper.readValue(json, deserializer);
 assertEquals("Hello World", result);
 }

 @Test
 public void testDeserializeScalarWithNullTypeId() throws IOException {
 String json = "\"Hello World\"";
 Object result = mapper.readValue(json, deserializer);
 assertNull(result);
 }
}