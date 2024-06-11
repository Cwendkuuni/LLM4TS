package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.BigDecimalDeserializer;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.BigIntegerDeserializer;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;

@SuppressWarnings("resultofmethodcallsignature")
public class NumberSerializerTest {

 private ObjectMapper mapper = new ObjectMapper();
 private JsonNodeFactory nodeFactory = mapper.getNodeFactory();

 @Test
 public void testSerializeBigDecimal() throws JsonGenerationException, IOException {
 BigDecimal number = new BigDecimal("3.14");
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getFactory().createGenerator(new StringWriter());
 serializer.serialize(number, generator, null);
 String json = generator.getOutputContent();
 JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
 assertEquals("3.14", parser.getText());
 }

 @Test
 public void testSerializeBigInteger() throws JsonGenerationException, IOException {
 BigInteger number = new BigInteger("12345678901234567890");
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getFactory().createGenerator(new StringWriter());
 serializer.serialize(number, generator, null);
 String json = generator.getOutputContent();
 JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
 assertEquals("12345678901234567890", parser.getText());
 }

 @Test
 public void testSerializeInt() throws JsonGenerationException, IOException {
 int number = 123;
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getFactory().createGenerator(new StringWriter());
 serializer.serialize(number, generator, null);
 String json = generator.getOutputContent();
 JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
 assertEquals("123", parser.getText());
 }

 @Test
 public void testSerializeLong() throws JsonGenerationException, IOException {
 long number = 1234567890123456789L;
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getFactory().createGenerator(new StringWriter());
 serializer.serialize(number, generator, null);
 String json = generator.getOutputContent();
 JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
 assertEquals("1234567890123456789", parser.getText());
 }

 @Test
 public void testSerializeDouble() throws JsonGenerationException, IOException {
 double number = 3.14;
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getFactory().createGenerator(new StringWriter());
 serializer.serialize(number, generator, null);
 String json = generator.getOutputContent();
 JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
 assertEquals("3.14", parser.getText());
 }

 @Test
 public void testSerializeFloat() throws JsonGenerationException, IOException {
 float number = 3.14f;
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getFactory().createGenerator(new StringWriter());
 serializer.serialize(number, generator, null);
 String json = generator.getOutputContent();
 JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
 assertEquals("3.14", parser.getText());
 }

 @Test
 public void testSerializeByte() throws JsonGenerationException, IOException {
 byte number = 127;
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getFactory().createGenerator(new StringWriter());
 serializer.serialize(number, generator, null);
 String json = generator.getOutputContent();
 JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
 assertEquals("127", parser.getText());
 }

 @Test
 public void testSerializeShort() throws JsonGenerationException, IOException {
 short number = 123;
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getFactory().createGenerator(new StringWriter());
 serializer.serialize(number, generator, null);
 String json = generator.getOutputContent();
 JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
 assertEquals("123", parser.getText());
 }

 @Test
 public void testSerializeString() throws JsonGenerationException, IOException {
 String number = "3.14";
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getFactory().createGenerator(new StringWriter());
 serializer.serialize(number, generator, null);
 String json = generator.getOutputContent();
 JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
 assertEquals("3.14", parser.getText());
 }

 @Test(expected = JsonProcessingException.class)
 public void testSerializeUnsupportedType() throws JsonGenerationException, IOException {
 Object number = new Object();
 NumberSerializer serializer = NumberSerializer.instance;
 JsonGenerator generator = mapper.getFactory().createGenerator(new StringWriter());
 try {
 serializer.serialize(number, generator, null);
 } catch (JsonProcessingException e) {
 assertEquals("Unsupported class: class java.lang.Object", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testDeserializeBigDecimal() throws IOException {
 String json = "3.14";
 JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
 ObjectDeserializer deserializer = mapper.findValueDeserializer(JsonParser.NumberType.BIG_DECIMAL, null);
 Object result = deserializer.deserialize(parser, null);
 assertTrue(result instanceof BigDecimal);
 assertEquals("3.14", ((BigDecimal) result).toString());
 }

 @Test
 public void testDeserializeBigInteger() throws IOException {
 String json = "12345678901234567890";
 JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
 ObjectDeserializer deserializer = mapper.findValueDeserializer(JsonParser.NumberType.BIG_INTEGER, null);
 Object result = deserializer.deserialize(parser, null);
 assertTrue(result instanceof BigInteger);
 assertEquals("12345678901234567890", ((BigInteger) result).toString());
 }

 @Test
 public void testDeserializeInt() throws IOException {
 String json = "123";
 JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
 ObjectDeserializer deserializer = mapper.findValueDeserializer(JsonParser.NumberType.INT, null);
 Object result = deserializer.deserialize(parser, null);
 assertTrue(result instanceof Integer);
 assertEquals(123, ((Integer) result).intValue());
 }

 @Test
 public void testDeserializeLong() throws IOException {
 String json = "1234567890123456789";
 JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
 ObjectDeserializer deserializer = mapper.findValueDeserializer(JsonParser.NumberType.LONG, null);
 Object result = deserializer.deserialize(parser, null);
 assertTrue(result instanceof Long);
 assertEquals(1234567890123456789L, ((Long) result).longValue());
 }

 @Test
 public void testDeserializeDouble() throws IOException {
 String json = "3.14";
 JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
 ObjectDeserializer deserializer = mapper.findValueDeserializer(JsonParser.NumberType.DOUBLE, null);
 Object result = deserializer.deserialize(parser, null);
 assertTrue(result instanceof Double);
 assertEquals(3.14, ((Double) result).doubleValue(), 0.0);
 }

 @Test
 public void testDeserializeFloat() throws IOException {
 String json = "3.14";
 JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
 ObjectDeserializer deserializer = mapper.findValueDeserializer(JsonParser.NumberType.FLOAT, null);
 Object result = deserializer.deserialize(parser, null);
 assertTrue(result instanceof Float);
 assertEquals(3.14f, ((Float) result).floatValue(), 0.0f);
 }

 @Test
 public void testDeserializeByte() throws IOException {
 String json = "127";
 JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
 ObjectDeserializer deserializer = mapper.findValueDeserializer(JsonParser.NumberType.INT, null);
 Object result = deserializer.deserialize(parser, null);
 assertTrue(result instanceof Byte);
 assertEquals((byte) 127, ((Byte) result).byteValue());
 }

 @Test
 public void testDeserializeShort() throws IOException {
 String json = "123";
 JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
 ObjectDeserializer deserializer = mapper.findValueDeserializer(JsonParser.NumberType.INT, null);
 Object result = deserializer.deserialize(parser, null);
 assertTrue(result instanceof Short);
 assertEquals((short) 123, ((Short) result).shortValue());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeUnsupportedType() throws IOException {
 String json = "3.14";
 JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
 ObjectDeserializer deserializer = mapper.findValueDeserializer(JsonParser.NumberType.STRING, null);
 Object result = deserializer.deserialize(parser, null);
 fail("Unexpected result: " + result);
 }

 @Test
 public void testDeserializeNull() throws IOException {
 String json = "null";
 JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
 ObjectDeserializer deserializer = mapper.findValueDeserializer(JsonParser.NumberType.NULL, null);
 Object result = deserializer.deserialize(parser, null);
 assertNull(result);
 }
}