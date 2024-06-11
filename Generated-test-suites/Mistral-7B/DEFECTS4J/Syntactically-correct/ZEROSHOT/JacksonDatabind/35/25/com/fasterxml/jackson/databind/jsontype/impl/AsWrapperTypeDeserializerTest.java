package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.util.TypeFactory;
import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testAsWrapperTypeDeserializer() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 TypeFactory.defaultInstance.constructType(String.class),
 mapper.getTypeIdResolver(), "typeId", true, String.class);

 // Test deserialization with valid input
 String jsonString = "{\"typeId\":\"java.lang.String\",\"value\":\"test\"}";
 JsonParser parser = mapper.getFactory().createParser(jsonString);
 Object result = deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
 assertEquals("test", result);

 // Test deserialization with null typeId
 parser.nextToken(); // skip START_OBJECT
 parser.nextToken(); // skip typeId field
 parser.nextToken(); // read value field
 JsonNode valueNode = mapper.readTree(parser);
 parser.close();
 Object result2 = deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
 assertNull(result2);

 // Test deserialization with invalid typeId
 parser = mapper.getFactory().createParser(jsonString.replace("java.lang.String", "InvalidType"));
 Object result3 = deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
 assertNull(result3);
 }
}