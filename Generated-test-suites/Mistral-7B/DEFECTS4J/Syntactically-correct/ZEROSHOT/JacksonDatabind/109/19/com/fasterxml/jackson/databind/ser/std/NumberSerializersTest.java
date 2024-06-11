package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.Base;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;

public class NumberSerializersTest {

 private static ObjectMapper mapper;

 static {
 try {
 mapper = new ObjectMapper();
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testNumberSerializers() throws Exception {
 Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
 NumberSerializers.addAll(allDeserializers);

 // Test serialization of integers
 JsonSerializer<Object> intSerializer = allDeserializers.get(Integer.class.getName());
 Object intValue = 123;
 JsonNode intNode = mapper.writeValueAsTree(intValue, intSerializer);
 assertEquals("123", intNode.getTextValue());

 // Test serialization of longs
 JsonSerializer<Object> longSerializer = allDeserializers.get(Long.class.getName());
 Object longValue = 456L;
 JsonNode longNode = mapper.writeValueAsTree(longValue, longSerializer);
 assertEquals("456", longNode.getTextValue());

 // Test serialization of shorts
 JsonSerializer<Object> shortSerializer = allDeserializers.get(Short.class.getName());
 Object shortValue = (short) 78;
 JsonNode shortNode = mapper.writeValueAsTree(shortValue, shortSerializer);
 assertEquals("78", shortNode.getTextValue());

 // Test serialization of floats
 JsonSerializer<Object> floatSerializer = allDeserializers.get(Float.class.getName());
 Object floatValue = 1.23f;
 JsonNode floatNode = mapper.writeValueAsTree(floatValue, floatSerializer);
 assertEquals("1.23", floatNode.getTextValue());

 // Test serialization of doubles
 JsonSerializer<Object> doubleSerializer = allDeserializers.get(Double.class.getName());
 Object doubleValue = 3.14;
 JsonNode doubleNode = mapper.writeValueAsTree(doubleValue, doubleSerializer);
 assertEquals("3.14", doubleNode.getTextValue());
 }
}