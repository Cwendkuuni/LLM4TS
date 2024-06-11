package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testNumberSerializers() throws Exception {
 // Test serialization of various numeric types
 Integer intValue = 123;
 Long longValue = 456L;
 Short shortValue = 78;
 Byte byteValue = 1;
 Float floatValue = 1.23f;
 Double doubleValue = 3.14;

 JsonNode intNode = mapper.writeValueAsTree(intValue);
 JsonNode longNode = mapper.writeValueAsTree(longValue);
 JsonNode shortNode = mapper.writeValueAsTree(shortValue);
 JsonNode byteNode = mapper.writeValueAsTree(byteValue);
 JsonNode floatNode = mapper.writeValueAsTree(floatValue);
 JsonNode doubleNode = mapper.writeValueAsTree(doubleValue);

 // Verify serialization results are as expected
 assertEquals("123", intNode.toString());
 assertEquals("456", longNode.toString());
 assertEquals("78", shortNode.toString());
 assertEquals("1", byteNode.toString());
 assertEquals("1.23", floatNode.toString());
 assertEquals("3.14", doubleNode.toString());

 // Test deserialization
 Integer deserializedInt = mapper.readValue(intNode.toString(), Integer.class);
 Long deserializedLong = mapper.readValue(longNode.toString(), Long.class);
 Short deserializedShort = mapper.readValue(shortNode.toString(), Short.class);
 Byte deserializedByte = mapper.readValue(byteNode.toString(), Byte.class);
 Float deserializedFloat = mapper.readValue(floatNode.toString(), Float.class);
 Double deserializedDouble = mapper.readValue(doubleNode.toString(), Double.class);

 // Verify deserialization results match original values
 assertEquals(intValue, deserializedInt);
 assertEquals(longValue, deserializedLong);
 assertEquals(shortValue, deserializedShort);
 assertEquals(byteValue, deserializedByte);
 assertEquals(floatValue, deserializedFloat);
 assertEquals(doubleValue, deserializedDouble);
 }

 @Test
 public void testJsonFormatAnnotation() throws Exception {
 // Test serialization with JsonFormat annotation
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 Integer stringInt = 123;
 @JsonFormat(shape = JsonFormat.Shape.NUMBER)
 Float numberFloat = 1.23f;

 JsonNode stringIntNode = mapper.writeValueAsTree(stringInt);
 JsonNode numberFloatNode = mapper.writeValueAsTree(numberFloat);

 // Verify serialization results are as expected
 assertEquals("123", stringIntNode.toString());
 assertEquals("1.23", numberFloatNode.toString());

 // Test deserialization
 Integer deserializedStringInt = mapper.readValue(stringIntNode.toString(), Integer.class);
 Float deserializedNumberFloat = mapper.readValue(numberFloatNode.toString(), Float.class);

 // Verify deserialization results match original values
 assertEquals(stringInt, deserializedStringInt);
 assertEquals(numberFloat, deserializedNumberFloat);
 }
}