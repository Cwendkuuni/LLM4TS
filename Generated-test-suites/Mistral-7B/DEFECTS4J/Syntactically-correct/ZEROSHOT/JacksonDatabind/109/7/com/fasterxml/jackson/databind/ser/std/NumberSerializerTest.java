package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class NumberSerializerTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeAndDeserializeNumber() throws JsonProcessingException {
 Number number = 123;
 NumberSerializer numberSerializer = NumberSerializer.instance;

 // Test serialization for various number types
 assertEquals("123", numberSerializer.serializeValueAsString(number));

 BigDecimal bigDecimal = new BigDecimal("123.45");
 assertEquals("123.45", numberSerializer.serializeValueAsString(bigDecimal));

 BigInteger bigInteger = new BigInteger("98765432109876543210");
 assertEquals("98765432109876543210", numberSerializer.serializeValueAsString(bigInteger));

 Long longNumber = 123L;
 assertEquals("123", numberSerializer.serializeValueAsString(longNumber));

 Double doubleNumber = 123.45;
 assertEquals("123.45", numberSerializer.serializeValueAsString(doubleNumber));

 Float floatNumber = 123.45f;
 assertEquals("123.45", numberSerializer.serializeValueAsString(floatNumber));

 Integer integerNumber = 123;
 assertEquals("123", numberSerializer.serializeValueAsString(integerNumber));

 Byte byteNumber = 123;
 assertEquals("123", numberSerializer.serializeValueAsString(byteNumber));

 Short shortNumber = 123;
 assertEquals("123", numberSerializer.serializeValueAsString(shortNumber));

 // Test deserialization for various number types
 Number deserializedNumber = mapper.readValue("123", Number.class);
 assertEquals(123, deserializedNumber.intValue());

 deserializedNumber = mapper.readValue("123.45", Number.class);
 assertEquals(123.45, deserializedNumber.doubleValue());

 deserializedNumber = mapper.readValue("98765432109876543210", Number.class);
 BigInteger deserializedBigInteger = (BigInteger) deserializedNumber;
 assertEquals("98765432109876543210", deserializedBigInteger.toString());

 deserializedNumber = mapper.readValue("123.45", BigDecimal.class);
 BigDecimal deserializedBigDecimal = (BigDecimal) deserializedNumber;
 assertEquals("123.45", deserializedBigDecimal.toString());
 }

 @Test
 public void testSerializeContextualSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();

 // Test serialize with JsonFormat annotation
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 Number jsonFormatStringNumber = 123;
 JsonNode result = mapper.writeValueAsTree(jsonFormatStringNumber);
 assertEquals("\"123\"", result.getClass().getSimpleName());
 assertEquals("123", result.asText());

 // Test serialize with custom contextual serializer
 Number contextualSerializerNumber = new BigDecimal("123.45");
 JsonNode resultWithCustomSerializer = mapper.writeValueAsTree(contextualSerializerNumber, ToStringSerializer.instance);
 assertEquals("\"123.45\"", resultWithCustomSerializer.getClass().getSimpleName());
 assertEquals("\"123.45\"", resultWithCustomSerializer.asText());
 }

 @Test
 public void testDeserializeWithJsonFormatAnnotation() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();

 // Test deserialize with JsonFormat annotation
 String jsonString = "\"123\"";
 Number deserializedNumberWithJsonFormatAnnotation = mapper.readValue(jsonString, Number.class);
 assertEquals(123, deserializedNumberWithJsonFormatAnnotation.intValue());

 // Test deserialize with custom contextual serializer
 String jsonStringWithCustomSerializer = "\"123.45\"";
 Number deserializedNumberWithCustomSerializer = mapper.readValue(jsonStringWithCustomSerializer, Number.class);
 assertEquals(123.45, deserializedNumberWithCustomSerializer.doubleValue());
 }

 @Test
 public void testSerializeAndDeserializeListsWithNumberTypes() throws JsonProcessingException {
 List<Number> numberList = new ArrayList<>();
 numberList.add(123);
 numberList.add(new BigDecimal("123.45"));
 numberList.add(new BigInteger("98765432109876543210"));
 numberList.add(123L);
 numberList.add(123.45);
 numberList.add(123.45f);
 numberList.add(123);
 numberList.add((byte) 123);
 numberList.add((short) 123);

 ObjectNode rootNode = mapper.valueToTree(numberList);
 List<Number> deserializedNumberList = mapper.readValue(mapper.writeValueAsString(rootNode), List.class);

 for (int i = 0; i < numberList.size(); i++) {
 Number expectedNumber = numberList.get(i);
 Number actualNumber = deserializedNumberList.get(i);
 assertEquals(expectedNumber, actualNumber);
 }
}

 @Test
 public void testSerializeAndDeserializeMixedListWithNumberTypes() throws JsonProcessingException {
 List<Number> numberList = new ArrayList<>();
 numberList.add(123L);
 numberList.add(new BigDecimal("123.45"));
 numberList.add(new BigInteger("98765432109876543210"));
 numberList.add(123);
 numberList.add(123.45);
 numberList.add(123.45f);
 numberList.add(new BigDecimal("123.45"));
 numberList.add((byte) 123);
 numberList.add((short) 123);

 ObjectNode rootNode = mapper.valueToTree(numberList);
 List<Number> deserializedNumberList = mapper.readValue(mapper.writeValueAsString(rootNode), List.class);

 for (int i = 0; i < numberList.size(); i++) {
 Number expectedNumber = numberList.get(i);
 Number actualNumber = deserializedNumberList.get(i);
 assertEquals(expectedNumber, actualNumber);
 }
}

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode rootNode = mapper.createObjectNode();
 ObjectNode numberNode = mapper.valueToTree(123);
 rootNode.set("number", numberNode);

 JsonNode schemaNode = NumberSerializer.instance.acceptJsonFormatVisitor(mapper.getSerializationConfig(), rootNode.getType());
 assertEquals("{\"number\":{\"type\":\"number\",\"description\":\"Number serialization configuration\"}}", schemaNode.toString());
 }
}