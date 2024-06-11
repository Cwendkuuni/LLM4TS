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
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseMapTest;

public class AsPropertyTypeDeserializerTest extends BaseMapTest {

 private AsPropertyTypeDeserializer _deserializer;
 private ObjectMapper _mapper;

 @Before
 public void setUp() throws Exception {
 _mapper = new ObjectMapper();
 JavaType javaType = _mapper.getTypeFactory().constructType(AsPropertyTypeDeserializer.class);
 TypeIdResolver idResolver = _mapper.getTypeFactory().getExtendedXmlTypeIdResolver(null, true);
 String typePropertyName = "type";
 JavaType defaultImpl = _mapper.getTypeFactory().constructType(String.class);
 As inclusion = As.PROPERTY;
 _deserializer = new AsPropertyTypeDeserializer(javaType, idResolver, typePropertyName, true, defaultImpl, inclusion);
 }

 @Test
 public void testDeserializeTypedFromObject() throws IOException {
 JsonParser p = _mapper.getFactory().createParser("{\"type\":\"String\",\"value\":\"test\"}");
 Object result = _deserializer.deserializeTypedFromObject(p, _mapper.getDeserializationContext());
 assertEquals("test", result);
 p.close();
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws IOException {
 JsonParser p = _mapper.getFactory().createParser("{\"@class\":\"java.lang.String\",\"value\":\"test\"}");
 Object result = _deserializer.deserializeTypedFromObject(p, _mapper.getDeserializationContext());
 assertEquals("test", result);
 p.close();
 }

 @Test
 public void testDeserializeWithMissingTypeProperty() throws IOException {
 JsonParser p = _mapper.getFactory().createParser("{\"value\":\"test\"}");
 assertNull(_deserializer.deserializeTypedFromObject(p, _mapper.getDeserializationContext()));
 p.close();
 }

 @Test
 public void testDeserializeWithMissingTypePropertyInArray() throws IOException {
 JsonParser p = _mapper.getFactory().createParser("[{\"value\":\"test\"}]");
 assertNull(_deserializer.deserializeTypedFromAny(p, _mapper.getDeserializationContext()));
 p.close();
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeWithInvalidTypeProperty() throws IOException {
 JsonParser p = _mapper.getFactory().createParser("{\"invalidProperty\":\"test\"}");
 _deserializer.deserializeTypedFromObject(p, _mapper.getDeserializationContext());
 fail("Expected JsonMappingException but it was not thrown");
 p.close();
 }

 @Test
 public void testDeserializeWithDefaultImpl() throws IOException {
 JsonParser p = _mapper.getFactory().createParser("{ \"value\": 123 }");
 Object result = _deserializer.deserializeTypedFromObject(p, _mapper.getDeserializationContext());
 assertEquals(123, result);
 p.close();
 }

 @Test
 public void testDeserializeWithTokenBuffer() throws IOException {
 JsonParser p = _mapper.getFactory().createParser("{\"type\":\"String\",\"value\":\"test\"}");
 Object result = _deserializer.deserializeTypedFromObject(p, _mapper.getDeserializationContext());
 assertEquals("test", result);
 // Create a copy of parser
 JsonParser copyParser = new JsonParser(_mapper.getFactory().createCodec());
 copyParser.setCodec(_mapper.getCodec());
 copyParser.setCurrentLocation(p.getCurrentLocation());
 copyParser.nextToken(); // Move to START_OBJECT
 TokenBuffer tb = new TokenBuffer(p, _mapper.getDeserializationContext());
 tb.copyCurrentStructure(p);
 // Reset parser
 p.clearCurrentLocation();
 p.nextToken(); // Move to FIELD_NAME
 // Set new parser to parse from tokenbuffer
 JsonParser newParser = JsonParserSequence.createFlattened(false, tb.asParser(copyParser), copyParser);
 result = _deserializer.deserializeTypedForId(newParser, _mapper.getDeserializationContext(), tb);
 assertEquals("test", result);
 p.close();
 copyParser.close();
 newParser.close();
 }
}