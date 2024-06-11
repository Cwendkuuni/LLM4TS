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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Test class for {@link NumberSerializer}
 */
public class NumberSerializerTest {

 @Test
 public void testSerializeNumber() throws JsonProcessingException, IOException {
 NumberSerializer serializer = new NumberSerializer(Number.class);
 JsonGenerator generator = null;
 ObjectWriter writer = null;

 try {
 generator = new JsonFactory().createGenerator(new StringWriter());
 writer = new ObjectMapper().writerWithDefaultPrettyPrinter();

 // Test serialization of various Number types
 Number number = 123;
 NumberSerializer.instance.serialize(number, generator, new SerializerProvider());
 generator.flush();
 String result = generator.getOutputStream().toString();
 assertEquals("123", result);

 BigInteger bigInteger = BigInteger.valueOf(12345);
 NumberSerializer.instance.serialize(bigInteger, generator, new SerializerProvider());
 generator.flush();
 result = generator.getOutputStream().toString();
 assertEquals("12345", result);

 BigDecimal bigDecimal = new BigDecimal("12345.67");
 NumberSerializer.instance.serialize(bigDecimal, generator, new SerializerProvider());
 generator.flush();
 result = generator.getOutputStream().toString();
 assertEquals("12345.67", result);

 Long longNumber = 12345L;
 NumberSerializer serializerForLong = new NumberSerializer(Long.class);
 serializerForLong.serialize(longNumber, generator, new SerializerProvider());
 generator.flush();
 result = generator.getOutputStream().toString();
 assertEquals("12345", result);

 Float floatNumber = 123.45F;
 NumberSerializer serializerForFloat = new NumberSerializer(Float.class);
 serializerForFloat.serialize(floatNumber, generator, new SerializerProvider());
 generator.flush();
 result = generator.getOutputStream().toString();
 assertEquals("123.45", result);

 Double doubleNumber = 123.45;
 NumberSerializer serializerForDouble = new NumberSerializer(Double.class);
 serializerForDouble.serialize(doubleNumber, generator, new SerializerProvider());
 generator.flush();
 result = generator.getOutputStream().toString();
 assertEquals("123.45", result);

 Integer intNumber = 123;
 NumberSerializer serializerForInt = new NumberSerializer(Integer.class);
 serializerForInt.serialize(intNumber, generator, new SerializerProvider());
 generator.flush();
 result = generator.getOutputStream().toString();
 assertEquals("123", result);

 Short shortNumber = Short.valueOf((short) 123);
 NumberSerializer serializerForShort = new NumberSerializer(Short.class);
 serializerForShort.serialize(shortNumber, generator, new SerializerProvider());
 generator.flush();
 result = generator.getOutputStream().toString();
 assertEquals("123", result);

 Byte byteNumber = Byte.valueOf((byte) 123);
 NumberSerializer serializerForByte = new NumberSerializer(Byte.class);
 serializerForByte.serialize(byteNumber, generator, new SerializerProvider());
 generator.flush();
 result = generator.getOutputStream().toString();
 assertEquals("123", result);
 } finally {
 if (generator != null) {
 generator.close();
 }
 if (writer != null) {
 writer.close();
 }
 }
 }

 @Test
 public void testSerializeBigDecimalWithJsonFormat() throws JsonProcessingException, IOException {
 NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
 JsonGenerator generator = null;
 ObjectWriter writer = null;
 ObjectMapper mapper = null;

 try {
 mapper = new ObjectMapper();
 generator = new JsonFactory().createGenerator(new StringWriter());
 writer = mapper.writerWithDefaultPrettyPrinter();

 // Test serialization of BigDecimal with JsonFormat
 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "###,##0.00")
 BigDecimal bigDecimal = new BigDecimal("1234567.123456");
 NumberSerializer.instance
 .withContextualSerializer(new NumberSerializer(String.class))
 .serialize(bigDecimal, generator, new SerializerProvider());
 generator.flush();
 String result = generator.getOutputStream().toString();
 assertEquals("1,234,567.12", result);

 @JsonFormat(shape = JsonFormat.Shape.NUMBER)
 BigDecimal bigDecimal2 = new BigDecimal("1234567.123456");
 NumberSerializer.instance
 .withContextualSerializer(new NumberSerializer(BigDecimal.class))
 .serialize(bigDecimal2, generator, new SerializerProvider());
 generator.flush();
 result = generator.getOutputStream().toString();
 assertEquals("1234567.123456", result);
 } finally {
 if (generator != null) {
 generator.close();
 }
 if (writer != null) {
 writer.close();
 }
 if (mapper != null) {
 mapper.close();
 }
 }
}

 @Test
 public void testDeserializeNumber() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = null;

 try {
 parser = mapper.getFactory().createParser(new StringReader("123"));
 Number deserializedNumber = mapper.readValue(parser, Number.class);
 assertEquals(123, deserializedNumber.intValue());

 parser = mapper.getFactory().createParser(new StringReader("12345"));
 deserializedNumber = mapper.readValue(parser, Number.class);
 assertEquals(12345, deserializedNumber.intValue());

 parser = mapper.getFactory().createParser(new StringReader("1234567"));
 deserializedNumber = mapper.readValue(parser, BigInteger.class);
 assertEquals(BigInteger.valueOf(1234567L), deserializedNumber);

 parser = mapper.getFactory().createParser(new StringReader("12345.67"));
 deserializedNumber = mapper.readValue(parser, BigDecimal.class);
 assertEquals(new BigDecimal("12345.67"), deserializedNumber);

 parser = mapper.getFactory().createParser(new StringReader("123.45"));
 deserializedNumber = mapper.readValue(parser, Float.class);
 assertEquals(123.45f, deserializedNumber, 0.001f);

 parser = mapper.getFactory().createParser(new StringReader("123.45"));
 deserializedNumber = mapper.readValue(parser, Double.class);
 assertEquals(123.45, deserializedNumber, 0.001);

 parser = mapper.getFactory().createParser(new StringReader("123"));
 deserializedNumber = mapper.readValue(parser, Integer.class);
 assertEquals(123, deserializedNumber.intValue());

 parser = mapper.getFactory().createParser(new StringReader("123"));
 deserializedNumber = mapper.readValue(parser, Byte.class);
 assertEquals((byte) 123, deserializedNumber.byteValue());

 parser = mapper.getFactory().createParser(new StringReader("123"));
 deserializedNumber = mapper.readValue(parser, Short.class);
 assertEquals((short) 123, deserializedNumber.shortValue());
 } finally {
 if (parser != null) {
 parser.close();
 }
 }
}

 @Test
 public void testDeserializeBigDecimalWithCustomDeserializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = null;

 try {
 reader = mapper.readerForType(BigDecimal.class);
 reader = reader.withValueDeserializer(new FromStringDeserializer(new BigDecimalParser()));

 // Test deserialization of BigDecimal with custom deserializer
 String json = "\"1,234,567.12\"";
 BigDecimal deserializedNumber = reader.readValue(new StringReader(json));
 assertEquals(new BigDecimal("1,234,567.12"), deserializedNumber);
 } finally {
 if (reader != null) {
 reader.close();
 }
 }
}

 @Test(expected = JsonMappingException.class)
 public void testDeserializeUnsupportedType() throws JsonProcessingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 JsonParser parser = null;

 try {
 parser = mapper.getFactory().createParser(new StringReader("true"));
 mapper.readValue(parser, Number.class);
 fail("Expected JsonMappingException");
 } finally {
 if (parser != null) {
 parser.close();
 }
 }
}

 @Test
 public void testDeserializeBigDecimalWithJsonFormat() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = null;

 try {
 reader = mapper.readerForType(BigDecimal.class);

 // Test deserialization of BigDecimal with JsonFormat
 BigDecimal deserializedNumber = reader.readValue(new StringReader("\"1,234,567.12\""));
 assertEquals(new BigDecimal("1,234,567.12"), deserializedNumber);
 } finally {
 if (reader != null) {
 reader.close();
 }
 }
}

 private static class BigDecimalParser extends StdDeserializer<BigDecimal> {
 @Override
 public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 String text = p.getText();
 return new BigDecimal(text.substring(1, text.length() - 1));
 }
 }
}