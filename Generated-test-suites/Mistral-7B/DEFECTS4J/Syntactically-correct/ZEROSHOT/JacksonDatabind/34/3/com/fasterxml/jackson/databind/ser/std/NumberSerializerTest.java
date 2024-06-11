package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test class for NumberSerializer
 */
public class NumberSerializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = JsonMapperFactory.getMapperWithFullDefaultProvider();
 objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 }

 @Test
 public void testSerializeBigDecimal() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigDecimal bigDecimal = new BigDecimal("123.456");
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(null);
 numberSerializer.serialize(bigDecimal, jsonGenerator, objectMapper.getSerializerProvider());
 assertEquals("123.456", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeBigInteger() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigInteger bigInteger = new BigInteger("98765432109876543210");
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(null);
 numberSerializer.serialize(bigInteger, jsonGenerator, objectMapper.getSerializerProvider());
 assertEquals("98765432109876543210", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeInt() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 int intValue = 123;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(null);
 numberSerializer.serialize(intValue, jsonGenerator, objectMapper.getSerializerProvider());
 assertEquals("123", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeLong() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 long longValue = 9876543210987654321L;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(null);
 numberSerializer.serialize(longValue, jsonGenerator, objectMapper.getSerializerProvider());
 assertEquals("9876543210987654321", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeDouble() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 double doubleValue = 123.456;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(null);
 numberSerializer.serialize(doubleValue, jsonGenerator, objectMapper.getSerializerProvider());
 assertEquals("123.456", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeFloat() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 float floatValue = 123.456f;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(null);
 numberSerializer.serialize(floatValue, jsonGenerator, objectMapper.getSerializerProvider());
 assertEquals("123.456", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeByte() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 byte byteValue = 127;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(null);
 numberSerializer.serialize(byteValue, jsonGenerator, objectMapper.getSerializerProvider());
 assertEquals("127", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeShort() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 short shortValue = 32767;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(null);
 numberSerializer.serialize(shortValue, jsonGenerator, objectMapper.getSerializerProvider());
 assertEquals("32767", jsonGenerator.getOutputBuffer().toString());
 }

 @Test
 public void testSerializeCustomNumber() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 CustomNumber customNumber = new CustomNumber(123);
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(null);
 numberSerializer.serialize(customNumber, jsonGenerator, objectMapper.getSerializerProvider());
 assertEquals("123", jsonGenerator.getOutputBuffer().toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeUnsupportedType() throws JsonGenerationException, JsonMappingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 String stringValue = "not a number";
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(null);
 // This should throw a JsonMappingException
 numberSerializer.serialize(stringValue, jsonGenerator, objectMapper.getSerializerProvider());
 }

}

class CustomNumber {
 private final int value;

 CustomNumber(int value) {
 this.value = value;
 }

 public int intValue() {
 return value;
 }
}