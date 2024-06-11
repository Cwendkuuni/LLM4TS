package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.*;
import static org.junit.Assert.*;

public class AsPropertyTypeDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();
 private static final TypeFactory typeFactory = mapper.getTypeFactory();
 private static final JavaType javaType = typeFactory.constructType(AsPropertyTypeDeserializer.class);
 private static final TypeIdResolver idResolver = mapper.getTypeIdResolver();
 private static final As inclusion = As.PROPERTY;

 @BeforeClass
 public static void setupClass() throws Exception {
 mapper.registerModule(new SimpleModule()
 .addDeserializer(AsPropertyTypeDeserializer.class, new AsPropertyTypeDeserializer(javaType, idResolver, "typeProperty", true, null, inclusion)));
 }

 @AfterClass
 public static void teardownClass() throws Exception {
 mapper.clearDeserializers();
 }

 @Test
 public void testDeserializeWithTypeId() throws Exception {
 ObjectNode sourceNode = mapper.createObjectNode();
 sourceNode.put("typeProperty", "ValidTypeId");
 sourceNode.put("data", "someData");
 String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(sourceNode);
 AsPropertyTypeDeserializer deserializer = mapper.readValue(jsonString, AsPropertyTypeDeserializer.class);
 assertEquals(inclusion, deserializer.getTypeInclusion());
 }

 @Test
 public void testDeserializeWithMissingTypeProperty() throws Exception {
 ObjectNode sourceNode = mapper.createObjectNode();
 sourceNode.put("data", "someData");
 String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(sourceNode);
 try {
 AsPropertyTypeDeserializer deserializer = mapper.readValue(jsonString, AsPropertyTypeDeserializer.class);
 fail("expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("Missing property 'typeProperty' for type [simple type, class com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer]", e.getMessage());
 }
 }

 @Test
 public void testDeserializeWithEmptyTypeProperty() throws Exception {
 ObjectNode sourceNode = mapper.createObjectNode();
 sourceNode.put("typeProperty", "");
 sourceNode.put("data", "someData");
 String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(sourceNode);
 try {
 AsPropertyTypeDeserializer deserializer = mapper.readValue(jsonString, AsPropertyTypeDeserializer.class);
 fail("expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("Cannot construct instance of [simple type, class com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer]: can not instantiate value of type [simple type, class java.lang.String] from JSON String; no single-value constructor/factory method", e.getMessage());
 }
 }

 @Test
 public void testDeserializeWithInvalidTypeId() throws Exception {
 ObjectNode sourceNode = mapper.createObjectNode();
 sourceNode.put("typeProperty", "InvalidTypeId");
 sourceNode.put("data", "someData");
 String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(sourceNode);
 try {
 AsPropertyTypeDeserializer deserializer = mapper.readValue(jsonString, AsPropertyTypeDeserializer.class);
 fail("expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("Unknown type id: 'InvalidTypeId' (for class com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer)", e.getMessage());
 }
 }

 @Test
 public void testDeserializeWithTypeIdNotVisible() throws Exception {
 ObjectNode sourceNode = mapper.createObjectNode();
 sourceNode.put("typeProperty", "ValidTypeId");
 ObjectNode dataNode = mapper.createObjectNode();
 dataNode.put("data", "someData");
 sourceNode.set("data", dataNode);
 String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(sourceNode);AsPropertyTypeDeserializer deserializer = mapper.readValue(jsonString, AsPropertyTypeDeserializer.class);
 assertNull(deserializer.getTypeInclusion());
 }

 @Test
 public void testDeserializeWithDefaultImpl() throws Exception {
 ObjectNode sourceNode = mapper.createObjectNode();
 sourceNode.put("data", "someData");
 String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(sourceNode);
 AsPropertyTypeDeserializer deserializer = mapper.readValue(jsonString, AsPropertyTypeDeserializer.class);
 // Default implementation should be used
 }
}