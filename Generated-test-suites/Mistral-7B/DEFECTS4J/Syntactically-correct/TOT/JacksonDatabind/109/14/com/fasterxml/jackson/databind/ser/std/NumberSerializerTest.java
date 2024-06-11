package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link NumberSerializer} class.
 */
@JacksonStdImpl
public class NumberSerializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 }

 /**
 * Test serialization of {@link BigDecimal} using default settings.
 */
 @Test
 public void testSerializationOfBigDecimal() throws Exception {
 BigDecimal bigDecimal = new BigDecimal("123.45");
 JsonGenerator jsonGenerator = objectMapper.getFactory()
 .createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializerProvider());
 NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
 numberSerializer.serialize(bigDecimal, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.close();
 String json = new String(((ByteArrayOutputStream) jsonGenerator.getOutputStream()).toByteArray());
 assertEquals("123.45", json);
 }

 /**
 * Test serialization of {@link BigDecimal} using custom {@link JsonFormat}.
 */
 @Test
 public void testSerializationOfBigDecimalWithJsonFormat() throws Exception {
 BigDecimal bigDecimal = new BigDecimal("123.45");
 JsonFormat jsonFormat = new JsonFormat(NumberSerializer.class);
 jsonFormat.setShape(JsonFormat.Shape.STRING);
 ObjectWriter objectWriter = objectMapper.writerWithView(MyView.class)
 .withSerializer(NumberSerializer.class, JsonSerializer.EmptyContext.instance())
 .withFormat(jsonFormat);
 String json = objectWriter.writeValueAsString(bigDecimal);
 assertEquals("\"123.45\"", json);
 }

 /**
 * Test serialization of {@link BigInteger}.
 */
 @Test
 public void testSerializationOfBigInteger() throws Exception {
 BigInteger bigInteger = new BigInteger("123456789012345678901234567890");
 JsonGenerator jsonGenerator = objectMapper.getFactory()
 .createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializerProvider());
 NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
 numberSerializer.serialize(bigInteger, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.close();
 String json = new String(((ByteArrayOutputStream) jsonGenerator.getOutputStream()).toByteArray());
 assertEquals("123456789012345678901234567890", json);
 }

 /**
 * Test serialization of {@link Long}.
 */
 @Test
 public void testSerializationOfLong() throws Exception {
 Long longValue = 123L;
 JsonGenerator jsonGenerator = objectMapper.getFactory()
 .createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializerProvider());
 NumberSerializer numberSerializer = new NumberSerializer(Long.class);
 numberSerializer.serialize(longValue, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.close();
 String json = new String(((ByteArrayOutputStream) jsonGenerator.getOutputStream()).toByteArray());
 assertEquals("123", json);
 }

 /**
 * Test serialization of {@link Double}.
 */
 @Test
 public void testSerializationOfDouble() throws Exception {
 Double doubleValue = 123.45;
 JsonGenerator jsonGenerator = objectMapper.getFactory()
 .createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializerProvider());
 NumberSerializer numberSerializer = new NumberSerializer(Double.class);
 numberSerializer.serialize(doubleValue, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.close();
 String json = new String(((ByteArrayOutputStream) jsonGenerator.getOutputStream()).toByteArray());
 assertEquals("123.45", json);
 }

 /**
 * Test serialization of {@link Float}.
 */
 @Test
 public void testSerializationOfFloat() throws Exception {
 Float floatValue = 123.45f;
 JsonGenerator jsonGenerator = objectMapper.getFactory()
 .createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializerProvider());
 NumberSerializer numberSerializer = new NumberSerializer(Float.class);
 numberSerializer.serialize(floatValue, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.close();
 String json = new String(((ByteArrayOutputStream) jsonGenerator.getOutputStream()).toByteArray());
 assertEquals("123.45", json);
 }

 /**
 * Test serialization of {@link Integer}.
 */
 @Test
 public void testSerializationOfInteger() throws Exception {
 Integer integerValue = 123;
 JsonGenerator jsonGenerator = objectMapper.getFactory()
 .createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializerProvider());
 NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
 numberSerializer.serialize(integerValue, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.close();
 String json = new String(((ByteArrayOutputStream) jsonGenerator.getOutputStream()).toByteArray());
 assertEquals("123", json);
 }

 /**
 * Test serialization of {@link Byte}.
 */
 @Test
 public void testSerializationOfByte() throws Exception {
 Byte byteValue = (byte) 123;
 JsonGenerator jsonGenerator = objectMapper.getFactory()
 .createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializerProvider());
 NumberSerializer numberSerializer = new NumberSerializer(Byte.class);
 numberSerializer.serialize(byteValue, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.close();
 String json = new String(((ByteArrayOutputStream) jsonGenerator.getOutputStream()).toByteArray());
 assertEquals("123", json);
 }

 /**
 * Test serialization of {@link Short}.
 */
 @Test
 public void testSerializationOfShort() throws Exception {
 Short shortValue = (short) 123;
 JsonGenerator jsonGenerator = objectMapper.getFactory()
 .createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializerProvider());
 NumberSerializer numberSerializer = new NumberSerializer(Short.class);
 numberSerializer.serialize(shortValue, jsonGenerator, objectMapper.getSerializerProvider());
 jsonGenerator.close();
 String json = new String(((ByteArrayOutputStream) jsonGenerator.getOutputStream()).toByteArray());
 assertEquals("123", json);
 }

 /**
 * Test deserialization of {@link BigDecimal} using default settings.
 */
 @Test
 public void testDeserializationOfBigDecimal() throws Exception {
 String json = "123.45";
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser(new ByteArrayInputStream(json.getBytes()));
 JsonDeserializer<BigDecimal> jsonDeserializer = new NumberDeserializer(BigDecimal.class);
 BigDecimal deserializedBigDecimal = jsonDeserializer.deserialize(jsonParser, BigDecimal.class);
 assertEquals(new BigDecimal("123.45"), deserializedBigDecimal);
 }

 /**
 * Test deserialization of {@link BigDecimal} using custom {@link JsonFormat}.
 */
 @Test
 public void testDeserializationOfBigDecimalWithJsonFormat() throws Exception {
 String json = "\"123.45\"";
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser(new ByteArrayInputStream(json.getBytes()));
 JsonDeserializer<BigDecimal> jsonDeserializer = new NumberDeserializer(BigDecimal.class);
 JsonFormat jsonFormat = new JsonFormat(NumberSerializer.class);
 JsonDeserializer<BigDecimal> customJsonDeserializer = new NumberDeserializer(BigDecimal.class) {
 @Override
 public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return super.deserialize(p, ctxt).toBigDecimal();
 }
 };
 customJsonDeserializer.setContentVisible(false);
 customJsonDeserializer.setFormatShape(JsonFormat.Shape.STRING);
 jsonDeserializer = customJsonDeserializer;
 BigDecimal deserializedBigDecimal = jsonDeserializer.deserialize(jsonParser, BigDecimal.class);
 assertEquals(new BigDecimal("123.45"), deserializedBigDecimal);
 }

 /**
 * Test deserialization of {@link BigInteger}.
 */
 @Test
 public void testDeserializationOfBigInteger() throws Exception {
 String json = "123456789012345678901234567890";
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser(new ByteArrayInputStream(json.getBytes()));
 JsonDeserializer<BigInteger> jsonDeserializer = new NumberDeserializer(BigInteger.class);
 BigInteger deserializedBigInteger = jsonDeserializer.deserialize(jsonParser, BigInteger.class);
 assertEquals(new BigInteger("123456789012345678901234567890"), deserializedBigInteger);
 }

 /**
 * Test deserialization of {@link Long}.
 */
 @Test
 public void testDeserializationOfLong() throws Exception {
 String json = "123";
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser(new ByteArrayInputStream(json.getBytes()));
 JsonDeserializer<Long> jsonDeserializer = new NumberDeserializer(Long.class);
 Long deserializedLong = jsonDeserializer.deserialize(jsonParser, Long.class);
 assertEquals(123L, deserializedLong);
 }

 /**
 * Test deserialization of {@link Double}.
 */
 @Test
 public void testDeserializationOfDouble() throws Exception {
 String json = "123.45";
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser(new ByteArrayInputStream(json.getBytes()));
 JsonDeserializer<Double> jsonDeserializer = new NumberDeserializer(Double.class);
 Double deserializedDouble = jsonDeserializer.deserialize(jsonParser, Double.class);
 assertEquals(123.45, deserializedDouble, 0.001);
 }

 /**
 * Test deserialization of {@link Float}.
 */
 @Test
 public void testDeserializationOfFloat() throws Exception {
 String json = "123.45";
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser(new ByteArrayInputStream(json.getBytes()));
 JsonDeserializer<Float> jsonDeserializer = new NumberDeserializer(Float.class);
 Float deserializedFloat = jsonDeserializer.deserialize(jsonParser, Float.class);
 assertEquals(123.45f, deserializedFloat, 0.001f);
 }

 /**
 * Test deserialization of {@link Integer}.
 */
 @Test
 public void testDeserializationOfInteger() throws Exception {
 String json = "123";
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser(new ByteArrayInputStream(json.getBytes()));
 JsonDeserializer<Integer> jsonDeserializer = new NumberDeserializer(Integer.class);
 Integer deserializedInteger = jsonDeserializer.deserialize(jsonParser, Integer.class);
 assertEquals(123, deserializedInteger);
 }

 /**
 * Test deserialization of {@link Byte}.
 */
 @Test
 public void testDeserializationOfByte() throws Exception {
 String json = "123";
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser(new ByteArrayInputStream(json.getBytes()));
 JsonDeserializer<Byte> jsonDeserializer = new NumberDeserializer(Byte.class);
 Byte deserializedByte = jsonDeserializer.deserialize(jsonParser, Byte.class);
 assertEquals((byte) 123, deserializedByte);
 }

 /**
 * Test deserialization of {@link Short}.
 */
 @Test
 public void testDeserializationOfShort() throws Exception {
 String json = "123";
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser(new ByteArrayInputStream(json.getBytes()));
 JsonDeserializer<Short> jsonDeserializer = new NumberDeserializer(Short.class);
 Short deserializedShort = jsonDeserializer.deserialize(jsonParser, Short.class);
 assertEquals((short) 123, deserializedShort);
 }

 /**
 * Test deserialization of invalid number using default settings.
 */
 @Test(expected = JsonProcessingException.class)
 public void testDeserializationOfInvalidNumber() throws Exception {
 String json = "invalidNumber";
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser(new ByteArrayInputStream(json.getBytes()));
 JsonDeserializer<Number> jsonDeserializer = new NumberDeserializer(Number.class);
 jsonDeserializer.deserialize(jsonParser, Number.class);
 }

 /**
 * Test deserialization of invalid number using custom {@link JsonFormat}.
 */
 @Test(expected = JsonProcessingException.class)
 public void testDeserializationOfInvalidNumberWithJsonFormat() throws Exception {
 String json = "\"invalidNumber\"";
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser(new ByteArrayInputStream(json.getBytes()));
 JsonDeserializer<Number> jsonDeserializer = new NumberDeserializer(Number.class) {
 @Override
 public Number deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return super.deserialize(p, ctxt).toNumber();
 }
 };
 JsonFormat jsonFormat = new JsonFormat(NumberSerializer.class);
 JsonDeserializer<Number> customJsonDeserializer = new NumberDeserializer(Number.class);
 customJsonDeserializer.setContentVisible(false);
 customJsonDeserializer.setFormatShape(JsonFormat.Shape.STRING);
 jsonDeserializer = customJsonDeserializer;
 jsonDeserializer.deserialize(jsonParser, Number.class);
 }
}