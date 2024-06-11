package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.BeforeClass;
import org.junit.Test;

public class DateTimeSerializerBaseTest {

 private static ObjectMapper mapper;

 @BeforeClass
 public static void setup() {
 SimpleModule module = new SimpleModule();
 try {
 mapper = new ObjectMapper();
 mapper.registerModule(module);
 } catch (JsonMappingException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testDefaultSerializer() throws JsonGenerationException, JsonMappingException, IOException {
 DateTimeSerializerBase<ZonedDateTime> zdtSerializer = mapper.findValueSerializer(ZonedDateTime.class);
 ZonedDateTime zdt = ZonedDateTime.now();
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(zdt);
 ZonedDateTime deserializedZdt = mapper.readValue(json, ZonedDateTime.class);
 assertEquals(zdt, deserializedZdt);
 }

 @Test
 public void testTimestampSerializer() throws JsonGenerationException, JsonMappingException, IOException {
 DateTimeSerializerBase<Instant> instantSerializer = mapper.findValueSerializer(Instant.class);
 Instant instant = Instant.now();
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(instant);
 Instant deserializedInstant = mapper.readValue(json, Instant.class);
 assertEquals(instant, deserializedInstant);
 }

 @Test
 public void testCustomFormatSerializer() throws JsonGenerationException, JsonMappingException, IOException {
 DateTimeSerializerBase<String> dateStringSerializer = mapper.findValueSerializer(String.class);
 TimeZone tz = TimeZone.getTimeZone("UTC");
 Locale locale = Locale.US;
 SimpleDateFormat customDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale);
 customDateFormat.setTimeZone(tz);
 DateTimeSerializerBase<String> serializedDateStringSerializer = mapper.findValueSerializer(String.class)
 .withFormat(false, customDateFormat);
 String originalDateString = "2023-01-01T12:12:12.123Z";
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(originalDateString);
 String deserializedDateString = mapper.readValue(json, String.class);
 assertEquals(originalDateString, deserializedDateString);
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingFormatAnnotation() throws JsonGenerationException, JsonMappingException, IOException {
 DateTimeSerializerBase<ZonedDateTime> missingFormatAnnotationSerializer = mapper.findValueSerializer(ZonedDateTime.class);
 ZonedDateTime zdt = ZonedDateTime.now();
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(zdt);
 try {
 mapper.readValue(json, Integer.class);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testUnexpectedFormat() throws JsonGenerationException, JsonMappingException, IOException {
 DateTimeSerializerBase<String> unexpectedFormatSerializer = mapper.findValueSerializer(String.class);
 TimeZone tz = TimeZone.getTimeZone("UTC");
 Locale locale = Locale.US;
 SimpleDateFormat customDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS z", locale);
 customDateFormat.setTimeZone(tz);
 DateTimeSerializerBase<String> serializedDateStringSerializer = mapper.findValueSerializer(String.class)
 .withFormat(false, customDateFormat);
 String originalDateString = "01-01-2023 12:12:12.123 MST";
 String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(originalDateString);
 try {
 TextNode deserializedNode = mapper.readValue(json, TextNode.class);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }
}