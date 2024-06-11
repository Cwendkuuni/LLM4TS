package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Test;

public class DateTimeSerializerBaseTest<T> {

 DateTimeSerializerBase<Long> longSerializer = new StdLongSerializer(Long.TYPE, false, null);
 DateTimeSerializerBase<String> stringSerializer = new StdStringSerializer(String.class, false, null);

 @Test
 public void testCreateContextual() throws Exception {
 JsonSerializerProvider serializers = new JsonSerializerProvider();
 ObjectCodec objectMapper = serializers.getObjectMapper();

 // Test serializing with default settings
 JsonNode node = objectMapper.writeValueAsTree(new Date());
 JsonParser parser = objectMapper.getFactory().createParser(node.toString());
 JsonToken token = parser.nextToken();
 assertTrue(token.isNumber());
 long value = parser.numberValue();
 assertEquals(System.currentTimeMillis(), value);

 // Test serializing with custom DateFormat
 DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
 DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat) {
 @Override
 protected long _timestamp(Date value) {
 return value.getTime();
 }
 };
 JsonSerializer<Date> serializer = dateSerializer.withFormat(false, customFormat);
 ObjectWriter writer = serializers.getObjectWriter();
 String json = writer.writeValueAsString(new Date());
 assertNotNull(json);
 assertEquals("\"2023-03-05T14:30:21.345-06:00\"", json);

 // Test serializing as timestamp with custom DateFormat
 DateTimeSerializerBase<Date> dateSerializerTimestamp = new DateTimeSerializerBase<Date>(Date.class, true, customFormat) {
 @Override
 protected long _timestamp(Date value) {
 return value.getTime();
 }
 };
 JsonSerializer<Date> serializerTimestamp = dateSerializerTimestamp.withFormat(true, null);
 JsonNode nodeTimestamp = objectMapper.writeValueAsTree(new Date());
 JsonParser parserTimestamp = objectMapper.getFactory().createParser(nodeTimestamp.toString());
 token = parserTimestamp.nextToken();
 assertTrue(token.isNumber());
 value = parserTimestamp.numberValue().longValue();
 assertEquals(System.currentTimeMillis(), value);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws Exception {
 JsonSerializerProvider serializers = new JsonSerializerProvider();
 ObjectCodec objectMapper = serializers.getObjectMapper();
 JsonFormatVisitorWrapper visitor = objectMapper.getSerializationConfig().findConfig(JsonFormat.Feature.FAIL_ON_EMPTY_BEANS, null);
 longSerializer.acceptJsonFormatVisitor(visitor, Long.TYPE);
 String expected = "number";
 assertEquals(expected, visitor.getFormatName());
 visitor.clear();
 stringSerializer.acceptJsonFormatVisitor(visitor, String.class);
 expected = "string";
 assertEquals(expected, visitor.getFormatName());
 }

 @Test
 public void testSerialize() throws Exception {
 JsonSerializerProvider serializers = new JsonSerializerProvider();
 ObjectCodec objectMapper = serializers.getObjectMapper();
 JsonGenerator generator = serializers.getFactory().createGenerator(new StringWriter());
 longSerializer.serialize(new Date(), generator, serializers);
 assertEquals("\"2023-03-05T14:30:21.345-06:00\"", generator.getOutputContent().toString());
 }

 @Test
 public void testIsEmpty() throws Exception {
 assertTrue(longSerializer.isEmpty(null));
 assertTrue(longSerializer.isEmpty(new Date(0)));
 }

 @Test
 public void testGetSchema() throws Exception {
 JsonSerializerProvider serializers = new JsonSerializerProvider();
 ObjectCodec objectMapper = serializers.getObjectMapper();
 assertEquals("number", longSerializer.getSchema(serializers, Long.TYPE).getType().getSimpleName());
 }
}