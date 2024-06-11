package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<ZonedDateTime> serializer;
 private DateTimeSerializerBase<Long> timestampSerializer;

 @Before
 public void setup() throws JsonMappingException {
 serializer = new StdDateTimeSerializer(ZonedDateTime.class, false, null);
 timestampSerializer = new StdDateTimeSerializer(Long.TYPE, true, null);
 }

 @Test
 public void testSerializationWithDefaultFormat() throws JsonGenerationException, IOException {
 ZonedDateTime zdt = ZonedDateTime.now();
 ObjectMapper objectMapper = new JsonMapper();
 SerializerProvider serializers = objectMapper.getSerializerProvider();
 ObjectNode result = objectMapper.writeValueAsTree(zdt, serializer, serializers);
 String jsonString = result.toString();
 JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
 Long timestamp = jsonParser.readValueAs(Long.class);
 ZonedDateTime deserialized = serializer.deserialize(_valueSerializerProvider, jsonParser);
 assertEquals(zdt, deserialized);
 assertEquals(timestamp.longValue(), zdt.toInstant().toEpochMilli());
 }

 @Test
 public void testSerializationWithCustomFormat() throws JsonGenerationException, IOException {
 ZonedDateTime zdt = ZonedDateTime.now();
 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
 serializer = new StdDateTimeSerializer(ZonedDateTime.class, false, formatter);
 ObjectMapper objectMapper = new JsonMapper();
 SerializerProvider serializers = objectMapper.getSerializerProvider();
 ObjectNode result = objectMapper.writeValueAsTree(zdt, serializer, serializers);
 String jsonString = result.toString();
 JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
 ZonedDateTime deserialized = serializer.deserialize(_valueSerializerProvider, jsonParser);
 assertEquals(zdt, deserialized);
 }

 @Test
 public void testSerializationWithTimestampFormat() throws JsonGenerationException, IOException {
 ZonedDateTime zdt = ZonedDateTime.now();
 Instant instant = zdt.toInstant();
 serializer = new StdDateTimeSerializer(ZonedDateTime.class, true, null);
 ObjectMapper objectMapper = new JsonMapper();
 SerializerProvider serializers = objectMapper.getSerializerProvider();
 ObjectNode result = objectMapper.writeValueAsTree(zdt, serializer, serializers);
 String jsonString = result.toString();
 Long timestamp = jsonParser.readValueAs(Long.class);
 ZonedDateTime deserialized = serializer.deserialize(_valueSerializerProvider, jsonParser);
 assertEquals(zdt, deserialized);
 assertEquals(timestamp.longValue(), zdt.toInstant().toEpochMilli());
 }

 @Test
 public void testSerializationWithEmptyValue() throws JsonGenerationException, IOException {
 ZonedDateTime zdt = null;
 ObjectMapper objectMapper = new JsonMapper();
 SerializerProvider serializers = objectMapper.getSerializerProvider();
 ObjectNode result = objectMapper.writeValueAsTree(zdt, serializer, serializers);
 String jsonString = result.toString();
 JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
 assertNull(serializer.deserialize(_valueSerializerProvider, jsonParser));
 }

 @Test
 public void testSerializationWithInvalidFormat() throws JsonGenerationException, IOException {
 ZonedDateTime zdt = ZonedDateTime.now();
 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("InvalidPattern");
 serializer = new StdDateTimeSerializer(ZonedDateTime.class, false, formatter);
 ObjectMapper objectMapper = new JsonMapper();
 SerializerProvider serializers = objectMapper.getSerializerProvider();
 ObjectNode result = objectMapper.writeValueAsTree(zdt, serializer, serializers);
 String jsonString = result.toString();
 try {
 JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
 serializer.deserialize(_valueSerializerProvider, jsonParser);
 } catch (IOException e) {
 assertEquals("Failed to parse date: InvalidPattern", e.getMessage());
 }
 }

 @Test
 public void testDeserializationWithDefaultFormat() throws JsonGenerationException, IOException {
 Long timestamp = 1616035116000L;
 String jsonString = Long.toString(timestamp);
 ObjectMapper objectMapper = new JsonMapper();
 ObjectNode jsonNode = objectMapper.readTree(jsonString);
 SerializerProvider serializers = objectMapper.getSerializerProvider();
 ZonedDateTime deserialized = serializer.deserialize(_valueSerializerProvider, jsonParser);
assertEquals(Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime(), deserialized.toLocalDateTime());
 }

 @Test
 public void testDeserializationWithCustomFormat() throws JsonGenerationException, IOException {
 String jsonString = "2022-01-01 01:01:01.123";
 ObjectMapper objectMapper = new JsonMapper();
 ObjectNode jsonNode = objectMapper.readTree(jsonString);
 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
 serializer = new StdDateTimeSerializer(ZonedDateTime.class, false, formatter);
 SerializerProvider serializers = objectMapper.getSerializerProvider();
 ZonedDateTime deserialized = serializer.deserialize(_valueSerializerProvider, jsonParser);
 assertEquals(ZonedDateTime.parse(jsonString, formatter), deserialized);
 }

 @Test
 public void testDeserializationWithTimestampFormat() throws JsonGenerationException, IOException {
 String jsonString = "1616035116000";
 ObjectMapper objectMapper = new JsonMapper();
 ObjectNode jsonNode = objectMapper.readTree(jsonString);
 serializer = new StdDateTimeSerializer(Long.TYPE, true, null);
 SerializerProvider serializers = objectMapper.getSerializerProvider();
 Long deserialized = (Long) timestampSerializer.deserialize(_valueSerializerProvider, jsonParser);
 assertEquals(deserialized.longValue(), Long.parseLong(jsonString));
 }

 @Test
 public void testDeserializationWithEmptyValue() throws JsonGenerationException, IOException {
 ObjectMapper objectMapper = new JsonMapper();
 ObjectNode jsonNode = objectMapper.createObjectNode();
 SerializerProvider serializers = objectMapper.getSerializerProvider();
 assertNull(serializer.deserialize(_valueSerializerProvider, jsonParser));
 }

 @Test
 public void testDeserializationWithInvalidFormat() throws JsonGenerationException, IOException {
 String jsonString = "InvalidPattern";
 ObjectMapper objectMapper = new JsonMapper();
 ObjectNode jsonNode = objectMapper.readTree(jsonString);
 assertNull(serializer.deserialize(_valueSerializerProvider, jsonParser));
 }

 @Test
 public void testSerializationWithNullValue() throws JsonGenerationException, IOException {
 serializer = new StdDateTimeSerializer(ZonedDateTime.class, false, null);
 ObjectMapper objectMapper = new JsonMapper();
 SerializerProvider serializers = objectMapper.getSerializerProvider();
 ObjectNode result = objectMapper.writeValueAsTree(null, serializer, serializers);
 String jsonString = result.toString();
 JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
 assertNull(serializer.deserialize(_valueSerializerProvider, jsonParser));
 }
}