package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NumberSerializerTest {

 @BeforeAll
 static void setup() {
 ObjectMapper objectMapper = new ObjectMapper();
 }

 @Test
 void testSerializeBigDecimal() throws IOException {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 BigDecimal bigDecimal = new BigDecimal("1234567890.123456");
 NumberSerializer.instance.serialize(bigDecimal, jsonGenerator, new SerializerProvider());
 jsonGenerator.flush();
 String json = System.out.getInputStream().readAllBytes();
 String expectedJson = "\"1234567890.123456\"";
 assertEquals(expectedJson, json);
 }

 @Test
 void testSerializeBigInteger() throws IOException {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 BigInteger bigInteger = BigInteger.valueOf(1234567890123456L);
 NumberSerializer.instance.serialize(bigInteger, jsonGenerator, new SerializerProvider());
 jsonGenerator.flush();
 String json = System.out.getInputStream().readAllBytes();
 String expectedJson = "1234567890123456";
 assertEquals(expectedJson, json);
 }

 @Test
 void testSerializeLong() throws IOException {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 long longValue = 1234567890123456L;
 numberSerializer.serialize(longValue, jsonGenerator, new SerializerProvider());
 jsonGenerator.flush();
 String json = System.out.getInputStream().readAllBytes();
 String expectedJson = "1234567890123456";
 assertEquals(expectedJson, json);
 }

 @Test
 void testSerializeInteger() throws IOException {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 int intValue = 123;
 numberSerializer.serialize(intValue, jsonGenerator, new SerializerProvider());
 jsonGenerator.flush();
 String json = System.out.getInputStream().readAllBytes();
 String expectedJson = "123";
 assertEquals(expectedJson, json);
 }

 @Test
 void testSerializeDouble() throws IOException {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 double doubleValue = 123.456;
 numberSerializer.serialize(doubleValue, jsonGenerator, new SerializerProvider());
 jsonGenerator.flush();
 String json = System.out.getInputStream().readAllBytes();
 String expectedJson = "123.456";
 assertEquals(expectedJson, json);
 }

 @Test
 void testSerializeFloat() throws IOException {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 float floatValue = 123.456f;
 numberSerializer.serialize(floatValue, jsonGenerator, new SerializerProvider());
 jsonGenerator.flush();
 String json = System.out.getInputStream().readAllBytes();
 String expectedJson = "123.456"; // expected float to be serialized as double
 assertEquals(expectedJson, json);
 }

 @Test
 void testSerializeCurrency() throws IOException {
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
 Currency currency = Currency.getInstance("USD");
 numberSerializer.serialize(currency.getHarMonUnit(), jsonGenerator, new SerializerProvider());
 jsonGenerator.flush();
 String json = System.out.getInputStream().readAllBytes();
 String expectedJson = "\"USD\"" + System.lineSeparator() + "1";
 assertEquals(expectedJson, json);
 }

 @Test
 void testJsonFormatVisit() throws IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonParser jsonParser = objectMapper.getFactory().createParser(
 "{\"@class\":\"com.fasterxml.jackson.databind.ser.std.NumberSerializer\",\"format\":{\"type\":\"number\",\"precision\":3}}");
 ObjectCodec objectCodec = objectMapper.getSerializerProvider().getCodec();
 JsonFormatVisitorWrapper visitor = objectCodec.getFormatVisitor(JsonParser.Feature.FAIL_ON_EMPTY_BEANS, null);
 NumberSerializer numberSerializer = new NumberSerializer(Number.class);
 numberSerializer.acceptJsonFormatVisitor(visitor, null);
 assertNotNull(visitor.getNumberFormat());
 assertEquals(3, visitor.getNumberFormat().getPrecision());
 }

 @Test
 void testSerializingUnknownTypeThrowsError() throws IOException {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
 Object unknownObject = new Object();
 try {
 NumberSerializer.instance.serialize(unknownObject, jsonGenerator, new SerializerProvider());
 fail("Expected JsonProcessingException to be thrown");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }

 @AfterAll
 void tearDown() {
 System.out.close();
 }
}