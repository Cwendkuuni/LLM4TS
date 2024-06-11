package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NumberSerializerTest {

 @Test
 public void testSerializeBigDecimal() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 BigDecimal testValue = new BigDecimal("123.456");
 JsonGenerator gen = mapper.getFactory().createGenerator();
 serializer.serialize(testValue, gen, null);
 gen.flush();
 String jsonString = gen.getOutputContent();
 assertEquals("123.456", jsonString);
 }

 @Test
 public void testSerializeBigInteger() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 BigInteger testValue = new BigInteger("12345678901234567890");
 JsonGenerator gen = mapper.getFactory().createGenerator();
 serializer.serialize(testValue, gen, null);
 gen.flush();
 String jsonString = gen.getOutputContent();
 assertEquals("12345678901234567890", jsonString);
 }

 @Test
 public void testSerializeLong() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 Long testValue = 1234567890L;
 JsonGenerator gen = mapper.getFactory().createGenerator();
 serializer.serialize(testValue, gen, null);
 gen.flush();
 String jsonString = gen.getOutputContent();
 assertEquals("1234567890", jsonString);
 }

 @Test
 public void testSerializeDouble() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 Double testValue = 123.456;
 JsonGenerator gen = mapper.getFactory().createGenerator();
 serializer.serialize(testValue, gen, null);
 gen.flush();
 String jsonString = gen.getOutputContent();
 assertEquals("123.456", jsonString);
 }

 @Test
 public void testSerializeFloat() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 Float testValue = 123.45f;
 JsonGenerator gen = mapper.getFactory().createGenerator();
 serializer.serialize(testValue, gen, null);
 gen.flush();
 String jsonString = gen.getOutputContent();
 assertEquals("123.45", jsonString);
 }

 @Test
 public void testSerializeByte() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 Byte testValue = 127;
 JsonGenerator gen = mapper.getFactory().createGenerator();
 serializer.serialize(testValue, gen, null);
 gen.flush();
 String jsonString = gen.getOutputContent();
 assertEquals("127", jsonString);
 }

 @Test
 public void testSerializeShort() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 Short testValue = 32767;
 JsonGenerator gen = mapper.getFactory().createGenerator();
 serializer.serialize(testValue, gen, null);
 gen.flush();
 String jsonString = gen.getOutputContent();
 assertEquals("32767", jsonString);
 }

 @Test
 public void testSerializeInteger() throws JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 Integer testValue = 123;
 JsonGenerator gen = mapper.getFactory().createGenerator();
 serializer.serialize(testValue, gen, null);
 gen.flush();
 String jsonString = gen.getOutputContent();
 assertEquals("123", jsonString);
 }

 @Test
 public void testSerializeCustomNumber() throws JsonMappingException, IOException {
 NumberSerializer serializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 CustomNumber testValue = new CustomNumber(123);
 JsonGenerator gen = mapper.getFactory().createGenerator();
 serializer.serialize(testValue, gen, null);
 gen.flush();
 String jsonString = gen.getOutputContent();
 assertEquals("123", jsonString);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeInvalidNumber() throws JsonMappingException, JsonProcessingException {
 NumberSerializer serializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 String testValue = "invalidNumber";
 JsonGenerator gen = mapper.getFactory().createGenerator();
 try {
 serializer.serialize(testValue, gen, null);
 gen.flush();
 } catch (JsonGenerationException e) {
 // expected
 } catch (Exception e) {
 throw new JsonMappingException("Unexpected exception: " + e.getMessage());
 }
 }
}