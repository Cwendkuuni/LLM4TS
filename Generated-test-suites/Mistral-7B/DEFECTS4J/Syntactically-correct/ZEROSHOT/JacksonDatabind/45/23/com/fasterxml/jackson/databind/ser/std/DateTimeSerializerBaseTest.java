package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.test.BaseMapTest;

public class DateTimeSerializerBaseTest extends BaseMapTest {

 private static final Long ZERO_TIMESTAMP = 0L;

 @SuppressWarnings("unchecked")
 private DateTimeSerializerBase<LocalDateTime> createSerializer() {
 return new DateTimeSerializerBase<LocalDateTime>() {
 @Override
 protected long _timestamp(LocalDateTime value) {
 return value != null ? value.getMillis() : ZERO_TIMESTAMP;
 }

 @Override
 public DateTimeSerializerBase<LocalDateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
 return this;
 }
 };
 }

 @SuppressWarnings("unchecked")
 private DateTimeSerializerBase<LocalDateTime> createSerializerWithFormat(DateFormat customFormat) {
 return new DateTimeSerializerBase<LocalDateTime>() {
 @Override
 protected long _timestamp(LocalDateTime value) {
 return value != null ? value.getMillis() : ZERO_TIMESTAMP;
 }

 @Override
 public DateTimeSerializerBase<LocalDateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
 if (customFormat != null) {
 this._customFormat = customFormat;
 }
 return this;
 }
 };
 }

 @Test
 public void testSerializerBaseCreation() {
 DateTimeSerializerBase<LocalDateTime> serializer = createSerializer();
 assertNotNull(serializer);
 }

 @Test
 public void testDefaultSerializerBehavior() throws IOException {
 DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 LocalDateTime dateTime = LocalDateTime.parse("2022-01-01T12:00:01.234Z", formatter);
 ObjectMapper mapper = new ObjectMapper();
 String json = mapper.writeValueAsString(dateTime);

 JsonParser parser = mapper.getFactory().createParser(json);
 DateTimeSerializerBase<LocalDateTime> deserializer = mapper.findValueSerializer(LocalDateTime.class, null);
 LocalDateTime deserializedDateTime = deserializer.deserialize(parser, null);

 assertEquals(dateTime, deserializedDateTime);
 }

 @Test
 public void testCustomFormatSerialization() throws IOException {
 DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
 DateFormat customFormat = new SimpleDateFormat(formatter.getPattern(), Locale.ENGLISH);
 customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

 DateTimeSerializerBase<LocalDateTime> serializer = createSerializerWithFormat(customFormat);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setSerializerProvider(new StandardSerializerProvider()); // to avoid default serialization
 mapper.registerSerializer(LocalDateTime.class, serializer);
 LocalDateTime dateTime = LocalDateTime.parse("2022-01-01 12:00:01", formatter);
 String json = mapper.writeValueAsString(dateTime);

 assertEquals("\"2022-01-01T12:00:01Z\"", json);
 }

 @Test
 public void testCustomFormatDeserialization() throws IOException {
 DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
 DateFormat customFormat = new SimpleDateFormat(formatter.getPattern(), Locale.ENGLISH);
 customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

 DateTimeSerializerBase<LocalDateTime> deserializer = createSerializerWithFormat(customFormat);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setSerializerProvider(new StandardSerializerProvider()); // to avoid default deserialization
 mapper.registerSerializer(LocalDateTime.class, deserializer);
 String json = "\"2022-01-01T12:00:01Z\"";
 LocalDateTime dateTime = mapper.readValue(json, LocalDateTime.class);

 assertEquals(LocalDateTime.parse("2022-01-01 12:00:01", formatter), dateTime);
 }

 @Test
 public void testDefaultFormatDeserialization() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "2022-01-01T12:00:01.234Z";
 LocalDateTime dateTime = mapper.readValue(json, LocalDateTime.class);

 assertEquals(LocalDateTime.parse("2022-01-01T12:00:01.234Z"), dateTime);
 }

 @Test
 public void testEmptyValue() throws IOException {
 DateTimeSerializerBase<LocalDateTime> serializer = createSerializer();
 ObjectMapper mapper = new ObjectMapper();
 LocalDateTime dateTime = null;
 String json = mapper.writeValueAsString(dateTime);

 assertEquals("null", json);

 JsonParser parser = mapper.getFactory().createParser(json);
 dateTime = serializer.deserialize(parser, null);

 assertNull(dateTime);
 }

 @Test
 public void testEmptyValueWithProvider() throws IOException {
 DateTimeSerializerBase<LocalDateTime> serializer = createSerializer();
 ObjectMapper mapper = new ObjectMapper();
 LocalDateTime dateTime = null;
 SerializerProvider serializers = mapper.getSerializerProvider();
 JsonParser parser = mapper.getFactory().createParser("null");

 dateTime = serializer.deserialize(parser, serializers);

 assertNull(dateTime);
 }

 @Test
 public void testTimestampSerialization() throws IOException {
 DateTimeSerializerBase<LocalDateTime> serializer = createSerializer();
 ObjectMapper mapper = new ObjectMapper();
 LocalDateTime dateTime = LocalDateTime.parse("2022-01-01 12:00:01", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
 String json = mapper.writeValueAsString(dateTime);

 assertEquals("1645344001000", json);
 }

 @Test
 public void testTimestampDeserialization() throws IOException {
 DateTimeSerializerBase<LocalDateTime> deserializer = createSerializer();
 ObjectMapper mapper = new ObjectMapper();
 String json = "1645344001000";
 LocalDateTime dateTime = deserializer.deserialize(null, mapper.getFactory().getParser(json));

 assertEquals(LocalDateTime.parse("2022-01-01 12:00:01"), dateTime);
 }

 @Test
 public void testJsonFormatAnnotation() throws IOException {
 JsonFormat jsonFormat = new JsonFormat();
 jsonFormat.shape(JsonFormat.Shape.STRING);
 jsonFormat.timeZone(TimeZone.getTimeZone("UTC"));
 jsonFormat.pattern("yyyy-MM-dd HH:mm:ss");

 DateTimeSerializerBase<LocalDateTime> serializer = new DateTimeSerializerBase<LocalDateTime>() {
 @Override
 public DateTimeSerializerBase<LocalDateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
 if (customFormat != null) {
 super.withFormat(timestamp, customFormat);
 } else if (jsonFormat != null) {
 this._useTimestamp = jsonFormat.isNumeric();
 this._customFormat = jsonFormat.getFormat();
 }
 return this;
 }

 @Override
 public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
 throws IOException {
 super.serialize(value, gen, serializers);
 }
 };

 ObjectMapper mapper = new ObjectMapper();
 mapper.setSerializerProvider(new StandardSerializerProvider()); // to avoid default serialization
 mapper.registerSerializer(LocalDateTime.class, serializer);
 LocalDateTime dateTime = LocalDateTime.parse("2022-01-01 12:00:01", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
 String json = mapper.writeValueAsString(dateTime);

 assertEquals("\"2022-01-01T12:00:01Z\"", json);
 }
}