package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.type.JavaType;

public class NumberSerializerTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializeBigDecimal() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigDecimal bigDecimal = new BigDecimal("123.456");
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getFactory().createGenerator(null);
 numberSerializer.serialize(bigDecimal, jsonGenerator, new SerializerProvider(mapper));
 JsonParser jsonParser = mapper.getFactory().createParser("123.456");
 assertEquals(bigDecimal, mapper.readValue(jsonParser, Number.class));
 }

 @Test
 public void testSerializeBigInteger() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 BigInteger bigInteger = new BigInteger("12345678901234567890");
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getFactory().createGenerator(null);
 numberSerializer.serialize(bigInteger, jsonGenerator, new SerializerProvider(mapper));
 JsonParser jsonParser = mapper.getFactory().createParser("12345678901234567890");
 assertEquals(bigInteger, mapper.readValue(jsonParser, Number.class));
 }

 @Test
 public void testSerializeInteger() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 int integer = 123;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getFactory().createGenerator(null);
 numberSerializer.serialize(integer, jsonGenerator, new SerializerProvider(mapper));
 JsonParser jsonParser = mapper.getFactory().createParser("123");
 assertEquals(integer, mapper.readValue(jsonParser, Number.class));
 }

 @Test
 public void testSerializeLong() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 long longValue = 1234567890123456789L;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getFactory().createGenerator(null);
 numberSerializer.serialize(longValue, jsonGenerator, new SerializerProvider(mapper));
 JsonParser jsonParser = mapper.getFactory().createParser("1234567890123456789");
 assertEquals(longValue, mapper.readValue(jsonParser, Number.class));
 }

 @Test
 public void testSerializeFloat() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 float floatValue = 123.45f;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getFactory().createGenerator(null);
 numberSerializer.serialize(floatValue, jsonGenerator, new SerializerProvider(mapper));
 JsonParser jsonParser = mapper.getFactory().createParser("123.45");
 assertEquals(floatValue, mapper.readValue(jsonParser, Number.class), 0.001f);
 }

 @Test
 public void testSerializeDouble() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 double doubleValue = 123.456;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getFactory().createGenerator(null);
 numberSerializer.serialize(doubleValue, jsonGenerator, new SerializerProvider(mapper));
 JsonParser jsonParser = mapper.getFactory().createParser("123.456");
 assertEquals(doubleValue, mapper.readValue(jsonParser, Number.class), 0.0001);
 }

 @Test
 public void testSerializeByteShort() throws JsonGenerationException, JsonMappingException, IOException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 byte byteValue = 127;
 short shortValue = 123;
 JsonGenerator jsonGenerator = mapper.getPolymorphicSerializer(Number.class).getFactory().createGenerator(null);
 numberSerializer.serialize(byteValue, jsonGenerator, new SerializerProvider(mapper));
 numberSerializer.serialize(shortValue, jsonGenerator, new SerializerProvider(mapper));
 JsonParser jsonParser = mapper.getFactory().createParser("127");
 JsonParser jsonParser2 = mapper.getFactory().createParser("123");
 assertEquals(byteValue, mapper.readValue(jsonParser, Number.class));
 assertEquals(shortValue, mapper.readValue(jsonParser2, Number.class));
 }

 @Test
 public void testAcceptJsonFormatVisitorInteger() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 when(visitor.expectNumberFormat(any())).thenReturn(visitor);
 NumberSerializer numberSerializer = NumberSerializer.instance;
 numberSerializer.acceptJsonFormatVisitor(visitor, JavaType.constructUnboundedType(Number.class));
 verify(visitor).expectNumberFormat(eq(JavaType.constructType(Integer.class)), any(JsonParser.NumberType.class));
 }

 @Test
 public void testAcceptJsonFormatVisitorBigDecimal() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 when(visitor.expectNumberFormat(any())).thenReturn(visitor);
 NumberSerializer numberSerializer = NumberSerializer.instance;
 numberSerializer.acceptJsonFormatVisitor(visitor, JavaType.constructType(BigDecimal.class));
 verify(visitor).expectNumberFormat(eq(JavaType.constructType(BigDecimal.class)), any(JsonParser.NumberType.class));
 }

}