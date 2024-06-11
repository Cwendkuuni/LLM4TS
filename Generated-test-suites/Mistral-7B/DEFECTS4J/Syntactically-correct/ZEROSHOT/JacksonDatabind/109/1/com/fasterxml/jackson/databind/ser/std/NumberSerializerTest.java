package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * Unit tests for {@link NumberSerializer}.
 */
public class NumberSerializerTest extends TestBase {

 private JsonGenerator generator;
 private JsonParser parser;
 private NumberSerializer serializer;

 @Before
 public void setUp() throws Exception {
 super.setUp();
 serializer = NumberSerializer.instance;
 generator = createMyJsonGenerator();
 parser = createMyJsonParser();
 }

 @Test
 public void testSerializeBigDecimal() throws JsonProcessingException {
 BigDecimal value = new BigDecimal("123.45");
 ObjectWriter writer = jsonObjectWriter(new ObjectMapper());
 String json = writer.writeValueAsString(new NumberTestBean("bd", value));
 JsonNode node = mapper.readTree(json);
 JsonParser p = mapper.getFactory().createParser(json);
 assertEquals(value.toString(), serializer.serializeValue(value, generator));
 assertEquals(value.toString(), p.getValueAsString());
 }

 @Test
 public void testSerializeBigInteger() throws JsonProcessingException {
 BigInteger value = new BigInteger("12345678901234567890");
 ObjectWriter writer = jsonObjectWriter(new ObjectMapper());
 String json = writer.writeValueAsString(new NumberTestBean("bi", value));
 JsonNode node = mapper.readTree(json);
 JsonParser p = mapper.getFactory().createParser(json);
 assertEquals(value.toString(), serializer.serializeValue(value, generator));
 assertEquals(value.toString(), p.getValueAsString());
 }

 @Test
 public void testSerializeLong() throws JsonProcessingException {
 long value = 1234567890L;
 ObjectWriter writer = jsonObjectWriter(new ObjectMapper());
 String json = writer.writeValueAsString(new NumberTestBean("l", value));
 JsonNode node = mapper.readTree(json);
 JsonParser p = mapper.getFactory().createParser(json);
 assertEquals(Long.toString(value), serializer.serializeValue(value, generator));
 assertEquals(Long.toString(value), p.getValueAsString());
 }

 @Test
 public void testSerializeDouble() throws JsonProcessingException {
 double value = 123.45;
 ObjectWriter writer = jsonObjectWriter(new ObjectMapper());
 String json = writer.writeValueAsString(new NumberTestBean("d", value));
 JsonNode node = mapper.readTree(json);
 JsonParser p = mapper.getFactory().createParser(json);
 assertEquals(Double.toString(value), serializer.serializeValue(value, generator));
 assertEquals(Double.toString(value), p.getValueAsString());
 }

 @Test
 public void testSerializeFloat() throws JsonProcessingException {
 float value = 123.45F;
 ObjectWriter writer = jsonObjectWriter(new ObjectMapper());
 String json = writer.writeValueAsString(new NumberTestBean("f", value));
 JsonNode node = mapper.readTree(json);
 JsonParser p = mapper.getFactory().createParser(json);
 assertEquals(Float.toString(value), serializer.serializeValue(value, generator));
 assertEquals(Float.toString(value), p.getValueAsString());
 }

 @Test
 public void testSerializeInt() throws JsonProcessingException {
 int value = 123;
 ObjectWriter writer = jsonObjectWriter(new ObjectMapper());
 String json = writer.writeValueAsString(new NumberTestBean("i", value));
 JsonNode node = mapper.readTree(json);
 JsonParser p = mapper.getFactory().createParser(json);
 assertEquals(Integer.toString(value), serializer.serializeValue(value, generator));
 assertEquals(Integer.toString(value), p.getValueAsString());
 }

 @Test
 public void testSerializeByte() throws JsonProcessingException {
 byte value = 123;
 ObjectWriter writer = jsonObjectWriter(new ObjectMapper());
 String json = writer.writeValueAsString(new NumberTestBean("b", value));
 JsonNode node = mapper.readTree(json);
 JsonParser p = mapper.getFactory().createParser(json);
 assertEquals(Byte.toString(value), serializer.serializeValue(value, generator));
 assertEquals(Byte.toString(value), p.getValueAsString());
 }

 @Test
 public void testSerializeShort() throws JsonProcessingException {
 short value = 123;
 ObjectWriter writer = jsonObjectWriter(new ObjectMapper());
 String json = writer.writeValueAsString(new NumberTestBean("s", value));
 JsonNode node = mapper.readTree(json);
 JsonParser p = mapper.getFactory().createParser(json);
 assertEquals(Short.toString(value), serializer.serializeValue(value, generator));
 assertEquals(Short.toString(value), p.getValueAsString());
 }

 @Test
 public void testContextualSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Number> serializer = mapper.findValueSerializer(BigDecimal.class);
 assertEquals(ToStringSerializer.instance, serializer);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 when(visitor.expectNumberFormat(any(JavaType.class))).thenReturn(visitor);
 serializer.acceptJsonFormatVisitor(visitor, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testAcceptJsonFormatVisitorWithFormat() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 JsonFormat format = new JsonFormat();
 when(visitor.expectNumberFormat(any(JavaType.class))).thenReturn(visitor);
 when(visitor.expectNumberFormat(any(JavaType.class), anyInt())).thenThrow(JsonMappingException.class);
 serializer.acceptJsonFormatVisitor(visitor, null);
 }

 @JsonSerialize(using = NumberSerializer.class)
 static class NumberTestBean {
 String name;
 Number number;

 public NumberTestBean(String name, Number number) {
 this.name = name;
 this.number = number;
 }
 }
}