package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class DateTimeSerializerBaseTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testSerializerWithDefaultSettings() throws JsonGenerationException, IOException {
 DateTimeSerializerBase<Date> dateTimeSerializer = mapper.getSerializerProvider().findValueSerializer(Date.class);
 Date date = new Date();
 String serializedDate = dateTimeSerializer.serializeValueAsString(date, mapper.getSerializerProvider(), null);
 assertEquals("2023-03-21 13:34:56.789", serializedDate); // assume current date and time

 Date deserializedDate = mapper.readValue(serializedDate, Date.class);
 assertEquals(date, deserializedDate);
 }

 @Test
 public void testSerializerWithCustomFormat() throws JsonGenerationException, IOException {
 DateTimeSerializerBase<Date> dateTimeSerializer = mapper.getSerializerProvider().findValueSerializer(Date.class);
 Date date = new Date();
 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withLocale(Locale.US);
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 String serializedDate = dateTimeSerializer.serializeValueAsString(date, mapper.getSerializerProvider(), formatter);
 assertEquals("2023-03-21 13:34:56.789", serializedDate);

 Date deserializedDate = mapper.readValue(serializedDate, Date.class);
 assertEquals(date, deserializedDate);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializerWithNullProvider() throws JsonGenerationException, IOException {
 DateTimeSerializerBase<Date> dateTimeSerializer = mapper.getSerializerProvider().findValueSerializer(Date.class);
 String serializedDate = dateTimeSerializer.serializeValueAsString(null, null, null);
 fail("Serialization should have failed with an IllegalArgumentException");
 }

 @Test
 public void testSerializerWithUseTimestamp() throws JsonGenerationException, IOException {
 DateTimeSerializerBase<Instant> instantSerializer = mapper.getSerializerProvider().findValueSerializer(Instant.class);
 Instant instant = Instant.now();
 long serializedInstant = instantSerializer.serializeValueAsNumber(instant, mapper.getSerializerProvider(), null);
 assertEquals(instant.getEpochSecond(), serializedInstant);

 Instant deserializedInstant = mapper.readValue(Long.toString(serializedInstant), Instant.class);
 assertEquals(instant, deserializedInstant);
 }

 @Test
 public void testSerializerWithUseTimestampAndCustomFormat() throws JsonGenerationException, IOException {
 DateTimeSerializerBase<Instant> instantSerializer = mapper.getSerializerProvider().findValueSerializer(Instant.class);
 Instant instant = Instant.now();
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSXXX");
 String serializedInstant = instantSerializer.serializeValueAsString(instant, mapper.getSerializerProvider(), formatter);
 assertEquals(instant.format(formatter), serializedInstant);

 Instant deserializedInstant = mapper.readValue(serializedInstant, Instant.class);
 assertEquals(instant, deserializedInstant);
 }

 @Test
 public void testSerializerWithJsonFormatAnnotation() throws JsonGenerationException, IOException {
 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS Z")
 Date dateWithAnnotation = new Date();
 DateTimeSerializerBase<Date> dateTimeSerializer = mapper.getSerializerProvider().findValueSerializer(Date.class);
 String serializedDate = dateTimeSerializer.serializeValueAsString(dateWithAnnotation, mapper.getSerializerProvider(), null);
 assertEquals("2023-03-21 13:34:56.789+02:00", serializedDate);

 Date deserializedDate = mapper.readValue(serializedDate, Date.class);
 assertEquals(dateWithAnnotation, deserializedDate);
 }

 @Test
 public void testDeserializerWithDefaultSettings() throws JsonGenerationException, IOException {
 DateTimeSerializerBase<Date> dateTimeSerializer = mapper.getDeserializerProvider().findValueDeserializer(Date.class, null);
 String jsonString = "\"2023-03-21 13:34:56.789\"";
 Date deserializedDate = dateTimeSerializer.deserialize(JsonParser.parseString(jsonString), null);
 assertEquals(new Date(), deserializedDate);
 }

 @Test
 public void testDeserializerWithCustomFormat() throws JsonGenerationException, IOException {
 DateTimeSerializerBase<Date> dateTimeSerializer = mapper.getDeserializerProvider().findValueDeserializer(Date.class, null);
 String jsonString = "\"2023-03-21 13:34:56.789\"";
 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
 Date deserializedDate = dateTimeSerializer.deserialize(JsonParser.parseString(jsonString), formatter);
 assertEquals(new Date(), deserializedDate);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializerWithInvalidFormat() throws JsonGenerationException, IOException {
 DateTimeSerializerBase<Date> dateTimeSerializer = mapper.getDeserializerProvider().findValueDeserializer(Date.class, null);
 String jsonString = "\"invalid_date\"";
 try {
 dateTimeSerializer.deserialize(JsonParser.parseString(jsonString), null);
 fail("Deserialization should have failed with a JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testDeserializerWithNullFormatter() throws JsonGenerationException, IOException {
 DateTimeSerializerBase<Date> dateTimeSerializer = mapper.getDeserializerProvider().findValueDeserializer(Date.class, null);
 String jsonString = "\"2023-03-21 13:34:56.789\"";
 Date deserializedDate = dateTimeSerializer.deserialize(JsonParser.parseString(jsonString), null);
 assertEquals(new Date(), deserializedDate);
 }
}