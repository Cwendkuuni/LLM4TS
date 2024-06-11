package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

 private ObjectMapper mapper;
 private ObjectNode testNode;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 try {
 testNode = mapper.createObjectNode();
 } catch (JsonProcessingException e) {
 throw new RuntimeException(e);
 }
 }

 @After
 public void teardown() {
 // Clear any set properties
 testNode = null;
 mapper = null;
 }

 /**
 * Test for handleTypePropertyValue method with valid input.
 */
 @Test
 public void testHandleTypePropertyValue() throws IOException {
 // Given: ExternalTypeHandler with valid properties
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SimplePOJO().getClass().getDeclaredField("typeId"), SimpleTypeDeserializer.INSTANCE)
 .build())
 .start();

 // When: JSON parser contains valid type id property
 JsonParser jp = mapper.getFactory().createParser(mapper.writeValueAsString(testNode.put("typeId", "StringType")));
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object bean = new SimplePOJO();

 // Then: handleTypePropertyValue should return true and set typeId
 boolean result = handler.handleTypePropertyValue(jp, ctxt, "typeId", bean);
 assertTrue(result);
 assertEquals("StringType", bean.getClass().getDeclaredField("typeId").get(bean));
 }

 /**
 * Test for handleTypePropertyValue method with invalid input.
 */
 @Test
 public void testHandleTypePropertyValueInvalid() throws IOException {
 // Given: ExternalTypeHandler with valid properties
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SimplePOJO().getClass().getDeclaredField("typeId"), SimpleTypeDeserializer.INSTANCE)
 .build())
 .start();

 // When: JSON parser does not contain valid property name
 JsonParser jp = mapper.getFactory().createParser(mapper.writeValueAsString(testNode.put("invalidProperty", "StringType")));
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object bean = new SimplePOJO();

 // Then: handleTypePropertyValue should return false
 boolean result = handler.handleTypePropertyValue(jp, ctxt, "invalidProperty", bean);
 assertFalse(result);
 }

 /**
 * Test for handlePropertyValue method with valid input.
 */
 @Test
 public void testHandlePropertyValue() throws IOException {
 // Given: ExternalTypeHandler with valid properties
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SimplePOJO().getClass().getDeclaredField("property"), SimpleTypeDeserializer.INSTANCE)
 .build())
 .start();

 // When: JSON parser contains valid property value
 JsonParser jp = mapper.getFactory().createParser(mapper.writeValueAsString(testNode.put("property", "TestValue")));
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object bean = new SimplePOJO();

 // Then: handlePropertyValue should return true and set property
 boolean result = handler.handlePropertyValue(jp, ctxt, "property", bean);
 assertTrue(result);
 assertEquals("TestValue", bean.getClass().getDeclaredField("property").get(bean));
 }

 /**
 * Test for handlePropertyValue method with invalid input.
 */
 @Test
 public void testHandlePropertyValueInvalid() throws IOException {
 // Given: ExternalTypeHandler with valid properties
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SimplePOJO().getClass().getDeclaredField("property"), SimpleTypeDeserializer.INSTANCE)
 .build())
 .start();

 // When: JSON parser does not contain valid property name
 JsonParser jp = mapper.getFactory().createParser(mapper.writeValueAsString(testNode.put("invalidProperty", "TestValue")));
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object bean = new SimplePOJO();

 // Then: handlePropertyValue should return false
 boolean result = handler.handlePropertyValue(jp, ctxt, "invalidProperty", bean);
 assertFalse(result);
 }

 /**
 * Test for handlePropertyValue method with missing type id.
 */
 @Test
 public void testHandlePropertyValueMissingTypeId() throws IOException {
 // Given: ExternalTypeHandler with valid properties
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SimplePOJO().getClass().getDeclaredField("property"), SimpleTypeDeserializer.INSTANCE)
 .build())
 .start();

 // When: JSON parser contains valid property value, but type id is missing
 JsonParser jp = mapper.getFactory().createParser(mapper.writeValueAsString(testNode.put("property", "TestValue")));
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object bean = new SimplePOJO();

 // Then: handlePropertyValue should return false
 boolean result = handler.handlePropertyValue(jp, ctxt, "property", bean);
 assertFalse(result);
 // Verify that property was not set
 assertEquals(null, bean.getClass().getDeclaredField("property").get(bean));
 }

 /**
 * Test for complete method with valid input.
 */
 @Test
 public void testCompleteValidInput() throws IOException {
 // Given: ExternalTypeHandler with valid properties
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SimplePOJO().getClass().getDeclaredField("property"), SimpleTypeDeserializer.INSTANCE)
 .build())
 .start();

 // When: JSON parser contains valid property values
 testNode.put("typeId", "StringType");
 testNode.put("property", "TestValue");

 // Then: complete method should return the POJO with typeId and property set
 Object bean = handler.complete(mapper.getFactory().createParser(mapper.writeValueAsString(testNode)), mapper.getDeserializationContext(), null);
 assertEquals("TestValue", bean.getClass().getDeclaredField("property").get(bean));
 assertEquals("StringType", bean.getClass().getDeclaredField("typeId").get(bean));
 }

 /**
 * Test for complete method with missing type id.
 */
 @Test
 public void testCompleteMissingTypeId() throws IOException {
 // Given: ExternalTypeHandler with valid properties
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SimplePOJO().getClass().getDeclaredField("property"), SimpleTypeDeserializer.INSTANCE)
 .build())
 .start();

 // When: JSON parser contains valid property values, but type id is missing
 testNode.put("property", "TestValue");

 // Then: complete method should throw an exception
 try {
 handler.complete(mapper.getFactory().createParser(mapper.writeValueAsString(testNode)), mapper.getDeserializationContext(), null);
 fail("Should have thrown an exception");
 } catch (MappingException e) {
 // Expected
 }
 }

 /**
 * Test for complete method with missing property.
 */
 @Test
 public void testCompleteMissingProperty() throws IOException {
 // Given: ExternalTypeHandler with valid properties
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SimplePOJO().getClass().getDeclaredField("typeId"), SimpleTypeDeserializer.INSTANCE)
 .build())
 .start();

 // When: JSON parser contains valid type id, but property is missing
 testNode.put("typeId", "StringType");

 // Then: complete method should throw an exception
 try {
 handler.complete(mapper.getFactory().createParser(mapper.writeValueAsString(testNode)), mapper.getDeserializationContext(), null);
 fail("Should have thrown an exception");
 } catch (MappingException e) {
 // Expected
 }
 }

 /**
 * Test for complete method with null property.
 */
 @Test
 public void testCompleteNullProperty() throws IOException {
 // Given: ExternalTypeHandler with valid properties
 ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
 .addExternal(new SimplePOJO().getClass().getDeclaredField("property"), SimpleTypeDeserializer.INSTANCE)
 .build())
 .start();

 // When: JSON parser contains valid type id and null property value
 testNode.put("typeId", "StringType");
 testNode.put("property", null);

 // Then: complete method should return the POJO with null property
 Object bean = handler.complete(mapper.getFactory().createParser(mapper.writeValueAsString(testNode)), mapper.getDeserializationContext(), null);
 assertEquals(null, bean.getClass().getDeclaredField("property").get(bean));
 }
}