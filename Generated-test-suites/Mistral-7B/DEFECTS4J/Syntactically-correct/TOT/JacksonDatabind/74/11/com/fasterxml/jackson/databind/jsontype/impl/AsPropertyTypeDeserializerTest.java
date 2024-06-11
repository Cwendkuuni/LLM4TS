package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AsPropertyTypeDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Before
 public void setUp() throws Exception {
 MAPPER.registerModule(new SimpleModule() {{
 addDeserializer(String.class, new StringDeserializer());
 addDeserializer(Integer.class, new IntDeserializer());
 addDeserializer(MyClass.class, new MyClassDeserializer());
 }});
 }

 @Test
 public void testDeserializeWithPropertyNameAndTypeId() throws Exception {
 // Given
 String json = "{\"typePropertyName\":\"StringClass\",\"typeId\":\"StringClassId\"}";

 // When
 Object actual = MAPPER.readValue(json, AsPropertyTypeDeserializer.class);

 // Then
 Assert.assertInstanceOf(String.class, actual);
 }

 @Test
 public void testDeserializeWithPropertyNameButNoTypeId() throws Exception {
 // Given
 String json = "{\"typePropertyName\":\"StringClass\"}";

 // When
 Object actual = MAPPER.readValue(json, AsPropertyTypeDeserializer.class);

 // Then
 Assert.assertNull(actual);
 }

 @Test
 public void testDeserializeWithPropertyNameAndInvalidTypeId() throws Exception {
 // Given
 String json = "{\"typePropertyName\":\"StringClass\",\"typeId\":\"InvalidTypeId\"}";

 // When
 Object actual = MAPPER.readValue(json, AsPropertyTypeDeserializer.class);

 // Then
 Assert.assertNull(actual);
 }

 @Test
 public void testDeserializeWithPropertyNameAndMissingField() throws Exception {
 // Given
 String json = "{\"typePropertyName\":\"StringClass\"}";

 // When
 Object actual = MAPPER.readValue(json, AsPropertyTypeDeserializer.class);

 // Then
 Assert.assertNull(actual);
 }

 @Test
 public void testDeserializeWithArrayWrapper() throws Exception {
 // Given
 String json = "[{\"typePropertyName\":\"IntegerClass\",\"id\":1}]";

 // When
 Object actual = MAPPER.readValue(json, AsPropertyTypeDeserializer.class);

 // Then
 Assert.assertArrayOf(Integer.class, actual);
 }

 @Test
 public void testDeserializeWithNull() throws Exception {
 // Given
 String json = "null";

 // When
 Object actual = MAPPER.readValue(json, AsPropertyTypeDeserializer.class);

 // Then
 Assert.assertNull(actual);
 }

 @Test
 public void testDeserializeWithEmptyObject() throws Exception {
 // Given
 String json = "{}";

 // When
 Object actual = MAPPER.readValue(json, AsPropertyTypeDeserializer.class);

 // Then
 Assert.assertNull(actual);
 }

 @Test
 public void testDeserializeWithInvalidJson() throws Exception {
 // Given
 String json = "{\"invalidJson\":\"invalid\"}";

 // When
 Object actual = MAPPER.readValue(json, AsPropertyTypeDeserializer.class);

 // Then
 Assert.assertNull(actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithMissingTypePropertyName() throws Exception {
 // Given
 String json = "{\"id\":\"InvalidTypeId\"}";

 // When
 Object actual = MAPPER.readValue(json, AsPropertyTypeDeserializer.class);

 // Then
 // JsonMappingException is expected
 }

 @Test
 public void testDeserializeWithTypeIdVisible() throws Exception {
 // Given
 String json = "{\"typePropertyName\":\"StringClass\",\"typeId\":\"StringClassId\",\"name\":\"John Doe\"}";

 // When
 Object actual = MAPPER.readValue(json, AsPropertyTypeDeserializer.class);

 // Then
 String expected = "John Doe";
 Assert.assertInstanceOf(String.class, actual);
 Assert.assertEquals(expected, actual.toString());
 }

 @Test
 public void testDeserializeWithDefaultImpl() throws Exception {
 // Given
 String json = "[]";
AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(MAPPER.getTypeFactory().constructType(MyClass.class),
        MAPPER.getTypeIdResolver(), "_typePropertyName", false, MAPPER.getTypeFactory().constructType(MyDefaultImpl.class), As.PROPERTY);

 // When
 Object actual = deserializer.deserializeTypedFromAny(MAPPER.getFactory().createParser(json), MAPPER.getDeserializationContext());

 // Then
 Assert.assertInstanceOf(MyDefaultImpl.class, actual);
 }

 private static class MyClass {
 }

 private static class MyDefaultImpl {
 }

 private static class StringDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return p.getValueAsString();
 }
 }

 private static class IntDeserializer extends StdDeserializer<Integer> {
 @Override
 public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return p.getValueAsInt();
 }
 }
}