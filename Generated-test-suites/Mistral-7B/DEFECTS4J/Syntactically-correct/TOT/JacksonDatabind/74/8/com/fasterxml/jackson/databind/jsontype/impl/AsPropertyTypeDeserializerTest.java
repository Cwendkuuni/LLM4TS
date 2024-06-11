package com.fasterxml.jackson.databind.jsontype.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @SuppressWarnings("unchecked")
 static JavaType createTestType() {
 return MAPPER.getTypeFactory().constructType(AsPropertyTypeDeserializer.class);
 }

 @SuppressWarnings("unchecked")
 static JavaType getTestType(Class<?> clazz) {
 return MAPPER.getTypeFactory().constructType(clazz);
 }

 @SuppressWarnings("resource")
 static Object deserializeFromJson(String json, Class<?> expectedType) throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 return mapper.readValue(json, AsPropertyTypeDeserializer.class, expectedType);
 }

 /**
 * Test for deserializing with a valid type id.
 */
 @Test
 public void testDeserializeWithValidTypeId() throws IOException {
 ObjectNode node = MAPPER.createObjectNode();
 node.put("typeId", "com.example.Foo");
 String json = node.toString();
 AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) deserializeFromJson(json, AsPropertyTypeDeserializer.class);
 AsPropertyTypeDeserializer expected = new AsPropertyTypeDeserializer(null, null, "typePropertyName", false, null, As.PROPERTY);
 assertEquals(expected, deserializer);
 }

 /**
 * Test for deserializing without a type id.
 */
 @Test
 public void testDeserializeWithoutTypeId() throws IOException {
 String json = "{}";
 try {
 AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) deserializeFromJson(json, AsPropertyTypeDeserializer.class);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 /**
 * Test for deserializing with an invalid type id.
 */
 @Test
 public void testDeserializeWithInvalidTypeId() throws IOException {
 ObjectNode node = MAPPER.createObjectNode();
 node.put("typeId", "com.example.Bar");
 String json = node.toString();
 try {
 AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) deserializeFromJson(json, AsPropertyTypeDeserializer.class);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 /**
 * Test for deserializing with a missing property.
 */
 @Test
 public void testDeserializeWithMissingProperty() throws IOException {
 ObjectNode node = MAPPER.createObjectNode();
 node.put("typePropertyName", "com.example.Foo");
 String json = node.toString();
 try {
 AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) deserializeFromJson(json, AsPropertyTypeDeserializer.class);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 /**
 * Test for deserializing with a property with incorrect name.
 */
 @Test
 public void testDeserializeWithIncorrectPropertyName() throws IOException {
 ObjectNode node = MAPPER.createObjectNode();
 node.put("wrongPropertyName", "com.example.Foo");
 String json = node.toString();
 try {
 AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) deserializeFromJson(json, AsPropertyTypeDeserializer.class);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 /**
 * Test for deserializing with a property of the wrong type.
 */
 @Test
 public void testDeserializeWithWrongPropertyType() throws IOException {
 ObjectNode node = MAPPER.createObjectNode();
 node.put("typePropertyName", 42);
 String json = node.toString();
 try {
 AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) deserializeFromJson(json, AsPropertyTypeDeserializer.class);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 /**
 * Test for deserializing with a null type property.
 */
 @Test
 public void testDeserializeWithNullTypeProperty() throws IOException {
 ObjectNode node = MAPPER.createObjectNode();
 node.put("typePropertyName", null);
 String json = node.toString();
 try {
 AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) deserializeFromJson(json, AsPropertyTypeDeserializer.class);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 /**
 * Test for deserializing with an empty type property.
 */
 @Test
 public void testDeserializeWithEmptyTypeProperty() throws IOException {
 ObjectNode node = MAPPER.createObjectNode();
 node.put("typePropertyName", "");
 String json = node.toString();
 try {
 AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) deserializeFromJson(json, AsPropertyTypeDeserializer.class);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 /**
 * Test for deserializing with a type property of a different class.
 */
 @Test
 public void testDeserializeWithTypePropertyOfDifferentClass() throws IOException {
 ObjectNode node = MAPPER.createObjectNode();
 node.put("typePropertyName", MAPPER.createValueInstance(Integer.TYPE));
 String json = node.toString();
 try {
 AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) deserializeFromJson(json, AsPropertyTypeDeserializer.class);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 /**
 * Test for deserializing with a type property of the correct class.
 */
 @Test
 public void testDeserializeWithTypePropertyOfCorrectClass() throws IOException {
 ObjectNode node = MAPPER.createObjectNode();
 node.put("typePropertyName", MAPPER.createValueInstance(AsPropertyTypeDeserializer.class));
 String json = node.toString();
 AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) deserializeFromJson(json, AsPropertyTypeDeserializer.class);
 AsPropertyTypeDeserializer expected = new AsPropertyTypeDeserializer(null, null, "typePropertyName", false, null, As.PROPERTY);
 assertEquals(expected, deserializer);
 }

 /**
 * Test for deserializing with a type id visible.
 */
 @Test
 public void testDeserializeWithTypeIdVisible() throws IOException {
 ObjectNode node = MAPPER.createObjectNode();
 node.put("typePropertyName", "com.example.Foo");
 node.put("typeId", "com.example.Bar");
 String json = node.toString();
 try {
 AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) deserializeFromJson(json, AsPropertyTypeDeserializer.class);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 /**
 * Test for deserializing with a type id not visible.
 */
 @Test
 public void testDeserializeWithTypeIdNotVisible() throws IOException {
 ObjectNode node = MAPPER.createObjectNode();
 node.put("typePropertyName", "com.example.Foo");
 node.put("typePropertyName", "com.example.Bar");
 node.put("typePropertyName", AsPropertyTypeDeserializer.class);
 String json = node.toString();
 AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) deserializeFromJson(json, AsPropertyTypeDeserializer.class);
 assertEquals(As.PROPERTY, deserializer.getTypeInclusion());
 }

 /**
 * Test for deserializing with a default implementation.
 */
 @Test
 public void testDeserializeWithDefaultImpl() throws IOException {
 ObjectNode node = MAPPER.createObjectNode();
 node.put("typePropertyName", "com.example.Foo");
 String json = node.toString();
 AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) deserializeFromJson(json, AsPropertyTypeDeserializer.class);
 Object deserialized = deserializer.deserializeTypedFromObject(MAPPER.createParser(json), MAPPER.getContext(new StdSerializer<?>() {}));
 assertEquals(DeserializationTest.class, deserialized.getClass());
 }
}