package com.yourpackagename.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Test class for NumberSerializer
 */
public class NumberSerializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testSerializeBigDecimal() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 BigDecimal bigDecimal = new BigDecimal("123.45");
 numberSerializer.serialize(bigDecimal, jsonGenerator, objectMapper.getSerializerProvider());
 assertEquals("123.45", jsonGenerator.getOutputStream().toString());
 }

 @Test
 public void testSerializeBigInteger() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 BigInteger bigInteger = new BigInteger("12345678901234567890");
 numberSerializer.serialize(bigInteger, jsonGenerator, objectMapper.getSerializerProvider());
 assertEquals("12345678901234567890", jsonGenerator.getOutputStream().toString());
 }

 @Test
 public void testSerializeInteger() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 int integer = 123;
 numberSerializer.serialize(integer, jsonGenerator, objectMapper.getSerializerProvider());
 assertEquals("123", jsonGenerator.getOutputStream().toString());
 }

 @Test
 public void testSerializeDouble() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 double doubleValue = 123.45;
 numberSerializer.serialize(doubleValue, jsonGenerator, objectMapper.getSerializerProvider());
 assertEquals("123.45", jsonGenerator.getOutputStream().toString());
 }

 @Test
 public void testSerializeFloat() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 float floatValue = 123.45f;
 numberSerializer.serialize(floatValue, jsonGenerator, objectMapper.getSerializerProvider());
 assertEquals("123.45", jsonGenerator.getOutputStream().toString());
 }

 @Test
 public void testSerializeContextualSerializer() throws IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addSerializer(BigDecimal.class, new ToStringSerializer());
 objectMapper.registerModule(module);
 NumberSerializer numberSerializer = objectMapper.findValueSerializer(BigDecimal.class);
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 BigDecimal bigDecimal = new BigDecimal("123.45");
 numberSerializer.serialize(bigDecimal, jsonGenerator, objectMapper.getSerializerProvider());
 assertEquals("123.45", jsonGenerator.getOutputStream().toString());
 }

 @Test
 public void testAcceptJsonFormatVisitorBigDecimal() throws JsonProcessingException {
 JsonFormatVisitorWrapper jsonFormatVisitorWrapper = objectMapper.getFactory().getPolymorphicSerializerProvider().findValueSerializerProvider(BigDecimal.class).getProviderInstance(JsonFormat.Value.class);
 NumberSerializer numberSerializer = (NumberSerializer) NumberSerializer.instance;
 assertEquals("number", jsonFormatVisitorWrapper.getCurrentName());
 numberSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
 assertEquals(JsonParser.NumberType.BIG_DECIMAL, jsonFormatVisitorWrapper.getCurrentType());
 }

 @Test(expected = JsonMappingException.class)
 public void testAcceptJsonFormatVisitorUnexpectedType() throws JsonMappingException, JsonProcessingException {
 JsonFormatVisitorWrapper jsonFormatVisitorWrapper = objectMapper.getFactory().getPolymorphicSerializerProvider().findValueSerializerProvider(String.class).getProviderInstance(JsonFormat.Value.class);
 NumberSerializer numberSerializer = (NumberSerializer) NumberSerializer.instance;
 numberSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
 }

 @Test
 public void testCreateContextualSerializer() throws JsonMappingException {
 JsonSerializer<Number> serializer = NumberSerializer.instance.createContextual(objectMapper.getSerializerProvider(), null);
 assertEquals(NumberSerializer.class, serializer.getClass());
 }

 @Test
 public void testFindFormatOverrides() throws JsonMappingException {
 JsonFormat format = new JsonFormat();
 JsonSerializerProvider serializerProvider = new SimpleSerializerProvider();
 ObjectMapper objectMapper = new ObjectMapper();
 JsonFormatVisitorWrapper jsonFormatVisitorWrapper = objectMapper.getFactory().getPolymorphicSerializerProvider().findValueSerializerProvider(Number.class).getProviderInstance(JsonFormat.Value.class);
 NumberSerializer numberSerializer = (NumberSerializer) NumberSerializer.instance;
 JsonSerializer<Number> serializer = numberSerializer.createContextual(serializerProvider, null);
 assertEquals(serializer, numberSerializer.findFormatOverrides(serializerProvider, null, Number.class));
 }

 @Test
 public void testSerializeFallback() throws IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 String value = "123";
 numberSerializer.serialize(value, jsonGenerator, objectMapper.getSerializerProvider());
 assertEquals("123", jsonGenerator.getOutputStream().toString());
 }
}