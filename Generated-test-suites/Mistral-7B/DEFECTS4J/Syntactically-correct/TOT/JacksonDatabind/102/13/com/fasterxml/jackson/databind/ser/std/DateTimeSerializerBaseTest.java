package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class DateTimeSerializerBaseTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 ObjectMapper.DefaultTypingResolver defaultTypingResolver = new ObjectMapper.DefaultTypingResolver();
 SimpleModule module = new SimpleModule();
 module.setDefaultTyping(defaultTypingResolver);
 mapper = new ObjectMapper(module);
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 }

 @Test
 public void testDefaultSerializer() throws JsonProcessingException, JsonMappingException, IOException {
 DateTimeSerializerBase<Date> dateSerializer = mapper.findValueSerializer(Date.class);
 Date date = new Date();
 String json = mapper.writeValueAsString(date);
 Date deserializedDate = mapper.readValue(json, Date.class);
 assertEquals(date, deserializedDate);
 }

 @Test
 public void testSerializeWithCustomFormat() throws JsonProcessingException, JsonMappingException, IOException {
 DateTimeSerializerBase<Date> dateSerializer = mapper.findValueSerializer(Date.class);
 DateTimeSerializerBase<Date> customFormatSerializer = dateSerializer.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss zzz"));
 Date date = new Date();
 String json = mapper.writeValueAsString(date, customFormatSerializer);
 assertEquals("24-12-2021 12:34:56 EST", json);
 Date deserializedDate = mapper.readValue(json, Date.class);
 assertEquals(date, deserializedDate);
 }

 @Test
 public void testSerializeAsTimestamp() throws JsonProcessingException, JsonMappingException, IOException {
 DateTimeSerializerBase<Long> longSerializer = mapper.findValueSerializer(Long.class);
 DateTimeSerializerBase<Date> dateSerializer = mapper.findValueSerializer(Date.class);
 DateTimeSerializerBase<Date> timestampSerializer = dateSerializer.withFormat(Boolean.TRUE, null);
 Date date = new Date();
 long timestamp = date.getTime();
 String jsonWithTimestamp = mapper.writeValueAsString(timestampSerializer, timestamp);
 assertEquals("1647487516000", jsonWithTimestamp);
 Date deserializedDate = mapper.readValue(jsonWithTimestamp, Date.class);
 assertEquals(date, deserializedDate);
 }

 @Test
 public void testNullValue() throws JsonProcessingException, JsonMappingException, IOException {
 DateTimeSerializerBase<Date> dateSerializer = mapper.findValueSerializer(Date.class);
 assertNull(mapper.writeValueAsString(dateSerializer, null));
 assertNull(mapper.readValue(mapper.writeValueAsString(null), Date.class));
 }

 @Test
 public void testEmptyString() throws JsonProcessingException, JsonMappingException, IOException {
 DateTimeSerializerBase<String> stringSerializer = mapper.findValueSerializer(String.class);
 assertEquals("\"\"", mapper.writeValueAsString(stringSerializer, ""));
 String emptyString = mapper.readValue(mapper.writeValueAsString(""), String.class);
 assertEquals("", emptyString);
 }

 @Test
 public void testDateFormatWithEmptyPattern() throws JsonProcessingException, JsonMappingException, IOException {
 DateTimeSerializerBase<Date> dateSerializer = mapper.findValueSerializer(Date.class);
 SimpleDateFormat emptyPatternFormat = new SimpleDateFormat("");
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
 customFormat.setTimeZone(timeZone);
 DateTimeSerializerBase<Date> customFormatSerializer = dateSerializer.withFormat(Boolean.FALSE, customFormat);
 Date date = new Date();
 String json = mapper.writeValueAsString(date, customFormatSerializer);
 assertNull(json);
 }
}