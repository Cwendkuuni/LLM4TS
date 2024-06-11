package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormat;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormat;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

@SuppressWarnings("unchecked")
public class DateTimeSerializerBaseTest {

 private static final DateTimeSerializerBase<Date> DATE_SERIALIZER = new DateTimeSerializerBase<Date>(Date.class, false, null);
 private static final DateTimeSerializerBase<ZonedDateTime> ZONED_DATE_TIME_SERIALIZER = new DateTimeSerializerBase<ZonedDateTime>(ZonedDateTime.class, false, null);

 @Test
 public void testDateSerializationWithDefaultSettings() throws JsonProcessingException {
 Date date = new Date();
 String expected = "\"2022-12-31T12:34:56.789Z\"";
 ObjectMapper objectMapper = new ObjectMapper();
 String json = objectMapper.writeValueAsString(date);
 assertEquals(expected, json);
 }

 @Test
 public void testDateSerializationWithCustomFormat() throws JsonProcessingException {
 Date date = new Date();
 DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 String expected = formatter.print(date);
 ObjectMapper objectMapper = new ObjectMapper();
 SerializerProvider serializers = objectMapper.getSerializerProvider();
 DateTimeSerializerBase<Date> customSerializer = DATE_SERIALIZER.withFormat(false, formatter.getLocalizedDateFormat());
 String json = objectMapper.writerWithContextualSerializer(serializers.findValueSerializer(Date.class)).writeValueAsString(date);
 assertEquals(expected, json);
 }

 @Test
 public void testDateSerializationWithCustomFormatAndLocale() throws JsonProcessingException {
 Date date = new Date();
 DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withLocale(Locale.US);
 String expected = formatter.print(date);
 ObjectMapper objectMapper = new ObjectMapper();
 SerializerProvider serializers = objectMapper.getSerializerProvider();
 DateTimeSerializerBase<Date> customSerializer = DATE_SERIALIZER.withFormat(false, formatter.getLocalizedDateFormat());
 String json = objectMapper.writerWithContextualSerializer(serializers.findValueSerializer(Date.class)).writeValueAsString(date);
 assertEquals(expected, json);
 }

 @Test
 public void testDateSerializationWithCustomFormatAndTimeZone() throws JsonProcessingException {
 Date date = new Date();
 DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Europe/London"));
 String expected = formatter.print(date.toInstant().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Europe/London")));
 ObjectMapper objectMapper = new ObjectMapper();
 SerializerProvider serializers = objectMapper.getSerializerProvider();
 DateTimeSerializerBase<Date> customSerializer = DATE_SERIALIZER.withFormat(false, formatter.getLocalizedDateTimeFormatter());
 String json = objectMapper.writerWithContextualSerializer(serializers.findValueSerializer(Date.class)).writeValueAsString(date);
 assertEquals(expected, json);
 }

 @Test
 public void testZonedDateTimeSerializationWithDefaultSettings() throws JsonProcessingException {
 ZonedDateTime zonedDateTime = ZonedDateTime.now();
 String expected = "\"2022-12-31T12:34:56.789+01:00\"";
 ObjectMapper objectMapper = new ObjectMapper();
 String json = objectMapper.writeValueAsString(zonedDateTime);
 assertEquals(expected, json);
 }

 @Test
 public void testZonedDateTimeSerializationWithCustomFormat() throws JsonProcessingException {
 ZonedDateTime zonedDateTime = ZonedDateTime.now();
 DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'ZZ'");
 String expected = formatter.print(zonedDateTime);
 ObjectMapper objectMapper = new ObjectMapper();
 String json = objectMapper.writerWithContextualSerializer(ZONED_DATE_TIME_SERIALIZER.withFormat(false, formatter.getLocalizedDateTimeFormatter())).writeValueAsString(zonedDateTime);
 assertEquals(expected, json);
 }

 @Test
 public void testZonedDateTimeSerializationWithCustomFormatAndLocale() throws JsonProcessingException {
 ZonedDateTime zonedDateTime = ZonedDateTime.now();
 DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").withLocale(Locale.US);
 String expected = formatter.print(zonedDateTime);
 ObjectMapper objectMapper = new ObjectMapper();
 String json = objectMapper.writerWithContextualSerializer(ZONED_DATE_TIME_SERIALIZER.withFormat(false, formatter.getLocalizedDateTimeFormatter())).writeValueAsString(zonedDateTime);
 assertEquals(expected, json);
 }

 @Test
 public void testZonedDateTimeSerializationWithCustomFormatAndTimeZone() throws JsonProcessingException {
 ZonedDateTime zonedDateTime = ZonedDateTime.now();
 DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'ZZ'").withZone(ZoneId.of("Europe/London"));
 String expected = formatter.print(zonedDateTime);
 ObjectMapper objectMapper = new ObjectMapper();
 String json = objectMapper.writerWithContextualSerializer(ZONED_DATE_TIME_SERIALIZER.withFormat(false, formatter.getLocalizedDateTimeFormatter())).writeValueAsString(zonedDateTime);
 assertEquals(expected, json);
 }

 @Test
 public void testSerializationAsTimestamp() throws JsonGenerationException, JsonMappingException {
 Date date = new Date();
 String expected = "1669145646000";
 ObjectMapper objectMapper = new ObjectMapper();
 SerializerProvider serializers = objectMapper.getSerializerProvider();
 DateTimeSerializerBase<Date> customSerializer = DATE_SERIALIZER.withFormat(true, null);
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
 customSerializer.serialize(date, jsonGenerator, serializers);
 jsonGenerator.flush();
 String json = jsonGenerator.getOutputBuffer().toString();
 assertEquals(expected, json);
 }

 @Test
 public void testSerializationAsTimestampWithCustomFormat() throws JsonProcessingException {
 Date date = new Date();
 DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 String expected = formatter.print(new Date(date.getTime() * 1000L));
 ObjectMapper objectMapper = new ObjectMapper();
 SerializerProvider serializers = objectMapper.getSerializerProvider();
 DateTimeSerializerBase<Date> customSerializer = DATE_SERIALIZER.withFormat(true, formatter.getLocalizedDateFormat());
 ObjectWriter writer = objectMapper.writerWithContextualSerializer(customSerializer);
 String json = writer.writeValueAsString(date);
 assertEquals(expected, json);
 }
}