package com.example.test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class DateTimeSerializerBaseTest {

 @SuppressWarnings("unchecked")
 private static <T> DateTimeSerializerBase<T> createSerializer(Class<T> type, boolean useTimestamp,
 DateFormat customFormat) {
 return new DateTimeSerializerBase<T>(type, useTimestamp, customFormat) {
 @Override
 protected long _timestamp(T value) {
 throw new UnsupportedOperationException();
 }
 };
 }

 @Test
 public void testDefaultSerialization() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

 LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 30);
 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

 DateTimeSerializerBase<LocalDateTime> serializer = createSerializer(LocalDateTime.class, false, null);
 String json = mapper.writeValueAsString(dateTime, new TypeSerializer() {
 @Override
 public void serialize(SerializerProvider serializers, Object value, Type typeHint, JsonGenerator gen)
 throws IOException {
 serializer.defaultSerializeDateValue((Date) value, gen);
 }
 });
 assertEquals("2022-01-01 12:30", json);
 }

 @Test
 public void testCustomSerialization() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

 LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 30);
 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

 DateTimeSerializerBase<LocalDateTime> serializer = createSerializer(LocalDateTime.class, false, formatter);
 String json = mapper.writeValueAsString(dateTime, serializer);
 assertEquals("2022-01-01 12:30", json);
 }

 @Test
 public void testTimestampSerialization() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

 LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 30);
 DateTimeSerializerBase<LocalDateTime> serializer = createSerializer(LocalDateTime.class, true, null);
 String json = mapper.writeValueAsString(dateTime, serializer);
 assertEquals("1645247600000", json);

 Date date = new Date(1645247600000L);
 assertEquals(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), dateTime);
 }

 @Test
 public void testCustomFormatSerialization() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

 LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 30);
 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm z");

 DateTimeSerializerBase<LocalDateTime> serializer = createSerializer(LocalDateTime.class, false, formatter);
 String json = mapper.writeValueAsString(dateTime, serializer);
 assertEquals("2022-01-01 12:30 EST", json);
 }

 @Test
 public void testSerializationWithJsonFormatAnnotations() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

 @JsonFormat(pattern = "yyyy-MM-dd HH:mm z")
 LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 30);
 String json = mapper.writeValueAsString(dateTime);
 assertEquals("2022-01-01 12:30 EST", json);
 }

 @Test
 public void testDeserialization() throws Exception {
 ObjectMapper mapper = new ObjectMapper();

 DateTimeSerializerBase<LocalDateTime> deserializer = createSerializer(LocalDateTime.class, false, null);

 String json = "\"2022-01-01 12:30\"";
 LocalDateTime dateTime = mapper.readValue(json, LocalDateTime.class);
 assertEquals(LocalDateTime.of(2022, 1, 1, 12, 30), dateTime);

 json = "1645247600000";
 dateTime = mapper.readValue(json, LocalDateTime.class);
 assertEquals(LocalDateTime.of(2022, 1, 1, 12, 30), dateTime);
 }

 @Test
 public void testDeserializationWithJsonFormatAnnotations() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.disable(DeserializationFeature.FAIL_ON_EMPTY_BEANS);

 @JsonFormat(pattern = "uuuu-MM-dd HH:mm z")
 LocalDateTime dateTime = mapper.readValue("\"2022-01-01 12:30 EST\"", LocalDateTime.class);
 assertEquals(LocalDateTime.of(2022, 1, 1, 12, 30), dateTime);

 json = "1645247600000";
 dateTime = mapper.readValue(json, LocalDateTime.class);
 assertEquals(LocalDateTime.of(2022, 1, 1, 12, 30), dateTime);
 }
}