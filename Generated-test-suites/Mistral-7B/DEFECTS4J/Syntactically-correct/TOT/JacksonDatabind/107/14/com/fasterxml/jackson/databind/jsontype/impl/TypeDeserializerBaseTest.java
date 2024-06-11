package com.fasterxml.jackson.databind.tests.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.impl.*;
import org.testng.annotations.*;

import static org.testng.Assert.*;

@Test(singleThreaded = true)
public class TypeDeserializerBaseTest {

 private TypeDeserializerBase<Object> typeDeserializer;
 private ObjectMapper mapper;

 @BeforeClass
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 JsonTypeInfo.Default implTypeInfo = new JsonTypeInfo.Default();
 implTypeInfo.setUseWrappedName(true);
 typeDeserializer = new TypeDeserializerBaseImpl(
 mapper.getTypeFactory().constructType(Object.class),
 mapper.getTypeIdResolver(),
 "typeProperty",
 true,
 mapper.getTypeFactory().constructType(String.class),
 implTypeInfo
 );
 }

 @Test
 public void testDefaultImplDeserializer() throws Exception {
 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", null);
 Object readValue = mapper.readValue(node, typeDeserializer.getType());
 assertNull(readValue);
 }

 @Test
 public void testFindDeserializer() throws Exception {
 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", "TypeA");
 TypeDeserializer deserializer = typeDeserializer.forProperty(new BeanProperty.Std("testProperty", String.class));
 JsonDeserializer<Object> findDeserializer = deserializer.findDeserializer(mapper.getDeserializationContext(), "TypeA");
 assertEquals(String.class, findDeserializer.getValueClass());
 }

 @Test(expectedExceptions = JsonMappingException.class)
 public void testFindDeserializerUnknownType() throws Exception {
 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", "UnknownType");
 typeDeserializer.findDeserializer(mapper.getDeserializationContext(), "UnknownType");
 }

 @Test
 public void testFindDefaultImplDeserializer() throws Exception {
 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", null);
 JsonDeserializer<Object> defaultImplDeserializer = typeDeserializer.findDefaultImplDeserializer(mapper.getDeserializationContext());
 assertEquals(String.class, defaultImplDeserializer.getValueClass());
 }

 @Test(expectedExceptions = IOException.class)
 public void testFindDefaultImplDeserializerNullifyingDeserializer() throws Exception {
 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", null);
 typeDeserializer.setFailOnEmptyTypeId(true);
 typeDeserializer.findDefaultImplDeserializer(mapper.getDeserializationContext());
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws Exception {
 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", "TypeA");
 Object readValue = typeDeserializer._deserializeWithNativeTypeId(
 mapper.getFactory().createParser(node.toString()),
 mapper.getDeserializationContext()
 );
 assertEquals("TypeA", readValue.getClass().getName());
 }

 @Test(expectedExceptions = IOException.class)
 public void testDeserializeWithNativeTypeIdUnknownType() throws Exception {
 ObjectNode node = mapper.createObjectNode();
 node.put("typeId", "UnknownType");
 typeDeserializer._deserializeWithNativeTypeId(
 mapper.getFactory().createParser(node.toString()),
 mapper.getDeserializationContext()
 );
 }

 @Test(enabled = false)
 public void testDeserializeWithNativeTypeIdMissingTypeId() throws Exception {
 ObjectNode node = mapper.createObjectNode();
 Object readValue = typeDeserializer._deserializeWithNativeTypeId(
 mapper.getFactory().createParser(node.toString()),
 mapper.getDeserializationContext()
 );
 fail("Should throw JsonMappingException");
 }
}