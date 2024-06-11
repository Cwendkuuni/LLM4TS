package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase testee;

 @Before
 public void setUp() throws Exception {
 JsonFactory factory = new JsonFactory();
 TypeIdResolver idResolver = factory.getPolymorphicTypeIdResolver();
 testee = new TypeDeserializerBase(
 Class.forName("com.example.Parent"), idResolver, "typePropertyName", true,
 Class.forName("com.example.Child")) {
 @Override
 protected JsonDeserializer<Object> forProperty(BeanProperty prop) {
 return new StubJsonDeserializer();
 }
 };
 }

 @Test
 public void testDefaultDeserializer() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String json = "{}";
 JsonParser parser = mapper.getFactory().createParser(json);
 Object result = testee.deserialize(parser, null);
 assertNull(result);
 }

 @Test
 public void testDeserializationWithTypeId() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String json = "{\"typeId\":\"com.example.Child\"}";
 JsonParser parser = mapper.getFactory().createParser(json);
 Object result = testee.deserialize(parser, null);
 assertTrue(result instanceof Child);
 }

 @Test(expected = IOException.class)
 public void testDeserializationWithoutTypeId() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String json = "{}";
 JsonParser parser = mapper.getFactory().createParser(json);
 testee.deserialize(parser, null);
 }

 @Test
 public void testNotFoundDeserializer() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String json = "{\"typeId\":\"com.example.UnknownType\"}";
 JsonParser parser = mapper.getFactory().createParser(json);
 try {
 testee.deserialize(parser, null);
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testDefaultImplDeserializer() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 String json = "{}";
 JsonParser parser = mapper.getFactory().createParser(json);
 Object result = testee._findDefaultImplDeserializer(parser.getCodec(), null);
 assertTrue(result instanceof NullifyingDeserializer);
 }

}

class StubJsonDeserializer extends JsonDeserializer<Object> {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new Object();
 }
}