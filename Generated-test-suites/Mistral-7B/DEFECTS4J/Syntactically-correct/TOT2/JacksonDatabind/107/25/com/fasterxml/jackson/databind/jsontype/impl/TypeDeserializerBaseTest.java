package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TypeDeserializerBaseTest {

 // Given: A new instance of TypeDeserializerBase
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
         TypeFactory.defaultInstance().constructType(String.class),
         new StandardSerializer.StdTypeIdResolver(),
         "_typePropertyName",
         true,
         TypeFactory.defaultInstance().constructType(MyDefaultImpl.class)) {
     @Override
     protected TypeDeserializer forProperty(BeanProperty prop) {
         return this;
     }
 };

 // When: We test the default behavior of the TypeDeserializerBase
 @org.junit.Test
 public void testDefaultBehavior() throws IOException, JsonProcessingException {
     ObjectMapper mapper = new ObjectMapper();
     mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
     JsonParser jsonParser = mapper.getFactory().createParser("{}");

     assertEquals(String.class, typeDeserializerBase.baseType());
     assertEquals("_typePropertyName", typeDeserializerBase.getPropertyName());
     assertEquals(true, typeDeserializerBase._typeIdVisible);

     JsonNode result = typeDeserializerBase.deserialize(jsonParser, new DeserializationContext(), null);
     assertNull(result);
 }

 // When: We test the behavior when the type id is known
 @org.junit.Test
 public void testKnownTypeId() throws IOException, JsonProcessingException {
     ObjectMapper mapper = new ObjectMapper();
     mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
     ObjectNode node = mapper.createObjectNode();
     node.put("typeId", "MyDefaultImpl");

     JsonParser jsonParser = mapper.getFactory().createParser(node.toString());
     assertEquals(MyDefaultImpl.class, typeDeserializerBase.deserialize(jsonParser, new DeserializationContext(), null).getClass());
 }

 // When: We test the behavior when the type id is unknown
 @org.junit.Test(expected = com.fasterxml.jackson.databind.JsonMappingException.class)
 public void testUnknownTypeId() throws IOException, JsonProcessingException {
     ObjectMapper mapper = new ObjectMapper();
     mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
     ObjectNode node = mapper.createObjectNode();
     node.put("typeId", "UnknownType");

     JsonParser jsonParser = mapper.getFactory().createParser(node.toString());
     typeDeserializerBase.deserialize(jsonParser, new DeserializationContext(), null);
 }

 // When: We test the behavior when the type id is null
 @org.junit.Test
 public void testNullTypeId() throws IOException, JsonProcessingException {
     ObjectMapper mapper = new ObjectMapper();
     mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
     JsonParser jsonParser = mapper.getFactory().createParser("{}");

     JsonNode result = typeDeserializerBase.deserialize(jsonParser, new DeserializationContext(), null);
     assertNull(result);
 }

 // When: We test the behavior when the deserializer is not found
 @org.junit.Test(expected = IOException.class)
 public void testNotFoundDeserializer() throws IOException, JsonProcessingException {
     ObjectMapper mapper = new ObjectMapper();
     mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
     ObjectNode node = mapper.createObjectNode();
     node.put("typeId", "NonExistentType");

     typeDeserializerBase = new TypeDeserializerBase(
             TypeFactory.defaultInstance().constructType(String.class),
             new StandardSerializer.StdTypeIdResolver(),
             "_typePropertyName",
             true,
             TypeFactory.defaultInstance().constructType(MyDefaultImpl.class)) {
         @Override
         protected TypeDeserializer forProperty(BeanProperty prop) {
             return this;
         }
         @Override
         protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
             throw new IOException("Deserializer not found");
         }
     };

     JsonParser jsonParser = mapper.getFactory().createParser(node.toString());
     typeDeserializerBase.deserialize(jsonParser, new DeserializationContext(), null);
 }

 // When: We test the behavior when the default implementation is not found
 @org.junit.Test(expected = IOException.class)
 public void testNotFoundDefaultImpl() throws IOException, JsonProcessingException {
     ObjectMapper mapper = new ObjectMapper();
     mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
     ObjectNode node = mapper.createObjectNode();
     node.put("typeId", null);

     typeDeserializerBase = new TypeDeserializerBase(
             TypeFactory.defaultInstance().constructType(String.class),
             new StandardSerializer.StdTypeIdResolver(),
             "_typePropertyName",
             true,
             null) {
         @Override
         protected TypeDeserializer forProperty(BeanProperty prop) {
             return this;
         }
         @Override
         protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
             throw new IOException("Default implementation not found");
         }
     };

     JsonParser jsonParser = mapper.getFactory().createParser(node.toString());
     typeDeserializerBase.deserialize(jsonParser, new DeserializationContext(), null);
 }

 // When: We test the behavior when the type id is missing
 @org.junit.Test(expected = IOException.class)
 public void testMissingTypeId() throws IOException, JsonProcessingException {
     ObjectMapper mapper = new ObjectMapper();
     mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
     ObjectNode node = mapper.createObjectNode();
     node.remove("typeId");

     typeDeserializerBase = new TypeDeserializerBase(
             TypeFactory.defaultInstance().constructType(String.class),
             new StandardSerializer.StdTypeIdResolver(),
             "_typePropertyName",
             true,
             TypeFactory.defaultInstance().constructType(MyDefaultImpl.class)) {
         @Override
         protected TypeDeserializer forProperty(BeanProperty prop) {
             return this;
         }
         @Override
         protected JavaType _handleMissingTypeId(DeserializationContext ctxt, String extraDesc) throws IOException {
             throw new IOException("Type id is missing");
         }
     };

     JsonParser jsonParser = mapper.getFactory().createParser(node.toString());
     typeDeserializerBase.deserialize(jsonParser, new DeserializationContext(), null);
 }

 // When: We test the behavior when the type id is invalid
 @org.junit.Test(expected = IOException.class)
 public void testInvalidTypeId() throws IOException, JsonProcessingException {
     ObjectMapper mapper = new ObjectMapper();
     mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
     ObjectNode node = mapper.createObjectNode();
     node.put("typeId", "InvalidTypeId");

     typeDeserializerBase = new TypeDeserializerBase(
             TypeFactory.defaultInstance().constructType(String.class),
             new StandardSerializer.StdTypeIdResolver(),
             "_typePropertyName",
             true,
             TypeFactory.defaultInstance().constructType(MyDefaultImpl.class)) {
         @Override
         protected TypeDeserializer forProperty(BeanProperty prop) {
             return this;
         }
         @Override
         protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId) throws IOException {
             throw new IOException("Unknown type id: " + typeId);
         }
     };

     JsonParser jsonParser = mapper.getFactory().createParser(node.toString());
     typeDeserializerBase.deserialize(jsonParser, new DeserializationContext(), null);
 }
}