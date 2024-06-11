package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Tests for {@link NumberSerializer} class.
 */
public class NumberSerializerTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeNumber() throws JsonProcessingException, JsonGenerationException {
 Number number = 123;
 String expected = "123.0";
 JsonGenerator gen = mapper.getPolymorphicSerializer(NumberSerializer.class).serializeValues(number, new JsonGenerator(), new SerializerProvider(), null);
 assertEquals(expected, gen.getOutputContent().toString());
 }

 @Test
 public void testSerializeBigDecimal() throws JsonProcessingException, JsonGenerationException {
 BigDecimal bigDecimal = new BigDecimal("123.45");
 String expected = "123.45";
 JsonGenerator gen = mapper.getPolymorphicSerializer(NumberSerializer.class).serializeValues(bigDecimal, new JsonGenerator(), new SerializerProvider(), null);
 assertEquals(expected, gen.getOutputContent().toString());
 }

 @Test
 public void testSerializeBigInteger() throws JsonProcessingException, JsonGenerationException {
 BigInteger bigInteger = new BigInteger("12345678901234567890");
 String expected = "12345678901234567890";
 JsonGenerator gen = mapper.getPolymorphicSerializer(NumberSerializer.class).serializeValues(bigInteger, new JsonGenerator(), new SerializerProvider(), null);
 assertEquals(expected, gen.getOutputContent().toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testSerializeInvalidNumber() throws JsonProcessingException, JsonGenerationException {
 Object invalidNumber = new Object();
 JsonGenerator gen = mapper.getPolymorphicSerializer(NumberSerializer.class).serializeValues(invalidNumber, new JsonGenerator(), new SerializerProvider(), null);
 gen.flush(); // check that serialization doesn't throw an exception
 fail("Expected JsonProcessingException");
 }

 @Test
 public void testGetSchema() throws JsonProcessingException {
 JsonSchema schema = mapper.getPolymorphicSerializer(NumberSerializer.class).getSchema(new SerializerProvider(), Number.class);
 assertEquals("number", schema.getType());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = mapper.getSerializers().findValueSerializer(JsonParser.NumberType.BIG_DECIMAL);
 NumberSerializer serializer = (NumberSerializer) mapper.getPolymorphicSerializer(NumberSerializer.class);
 serializer.acceptJsonFormatVisitor(visitor, BigDecimal.class.getType());
 assertTrue(visitor.wasVisited(JsonNumberFormatVisitor.class));
 }
}