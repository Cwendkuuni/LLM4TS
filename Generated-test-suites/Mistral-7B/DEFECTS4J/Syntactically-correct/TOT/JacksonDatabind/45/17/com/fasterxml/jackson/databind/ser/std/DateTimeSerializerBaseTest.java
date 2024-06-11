package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperWithSerializers;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class DateTimeSerializerBaseTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mapper = new JsonMapperWithSerializers();
 }

 @Test
 public void testDateTimeSerializerBaseWithTimestamp() throws JsonProcessingException {
 DateTimeSerializerBase<Date> dateTimeSerializer = mapper.findValueSerializer(Date.class);
 Date date = new Date();
 JsonGenerator gen = mapper.getPolymorphicSerializerProvider().getGenerator();
 dateTimeSerializer.serialize(date, gen, mapper.getSerializerProvider());
 assertNotNull(gen.getCurrentOutputValue());
 assertEquals("\"\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}Z\"", gen.getCurrentOutputValue().asText());
 }

 @Test
 public void testDateTimeSerializerBaseWithCustomFormat() throws JsonProcessingException {
 DateTimeSerializerBase<ZonedDateTime> dateTimeSerializer = mapper.findValueSerializer(ZonedDateTime.class);
 ZonedDateTime zdt = ZonedDateTime.now();
 JsonGenerator gen = mapper.getPolymorphicSerializerProvider().getGenerator();
 dateTimeSerializer.serialize(zdt, gen, mapper.getSerializerProvider());
 assertNotNull(gen.getCurrentOutputValue());
 String expected = String.format(Locale.US, "%s%s%s", zdt.getYear(), zdt.getMonthValue(), zdt.getDayOfMonth());
 assertEquals(expected, gen.getCurrentOutputValue().asText());
 }

 @Test
 public void testDateTimeSerializerBaseWithJsonFormatAnnotation() throws JsonProcessingException {
 DateTimeSerializerBase<Date> dateTimeSerializer = mapper.findValueSerializer(Date.class);
 Date date = new Date();
 AnnotatedMember property = new AnnotatedMember("testDate", Date.class);
 ObjectMapper.SerializedFormat serializedFormat = new ObjectMapper.SerializedFormat();
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 format.setLocale(Locale.US);
 format.setTimeZone(TimeZone.getDefault());
 format.setPattern("yyyy-MM-dd HH:mm:ss z");
 serializedFormat.setSerializationView(format);
 property.setValue(serializedFormat);
 JsonGenerator gen = mapper.getPolymorphicSerializerProvider().getGenerator();
 dateTimeSerializer.serialize(date, gen, mapper.getSerializerProvider(), property);
 assertNotNull(gen.getCurrentOutputValue());
 assertEquals("\"2023-03-31 10:20:30 EDT\"", gen.getCurrentOutputValue().asText());
 }

 @Test
 public void testDateTimeSerializerBaseWithEmptyValue() throws JsonProcessingException {
 DateTimeSerializerBase<Date> dateTimeSerializer = mapper.findValueSerializer(Date.class);
 JsonGenerator gen = mapper.getPolymorphicSerializerProvider().getGenerator();
 dateTimeSerializer.serialize(null, gen, mapper.getSerializerProvider());
 assertNull(gen.getCurrentOutputValue());
 }

 @Test(expected = JsonMappingException.class)
 public void testDateTimeSerializerBaseWithInvalidFormat() throws JsonProcessingException, JsonMappingException {
 DateTimeSerializerBase<String> dateTimeSerializer = mapper.findValueSerializer(String.class);
 String invalidFormat = "{@invalidFormat}";
 JsonParser parser = mapper.getFactory().createParser(new TextNode(invalidFormat).toString());
 Object readValue = mapper.readValue(parser, String.class);
 dateTimeSerializer.serialize(readValue, mapper.getPolymorphicSerializerProvider().getGenerator(), mapper.getSerializerProvider());
 fail("Should throw JsonMappingException for invalid format");
 }

 @Test
 public void testDateTimeSerializerBaseWithCustomTimestamp() throws JsonProcessingException {
 DateTimeSerializerBase<Instant> dateTimeSerializer = mapper.findValueSerializer(Instant.class);
 Instant instant = Instant.now();
 JsonGenerator gen = mapper.getPolymorphicSerializerProvider().getGenerator();
 dateTimeSerializer.serialize(instant, gen, mapper.getSerializerProvider());
 assertNotNull(gen.getCurrentOutputValue());
 assertTrue(gen.getCurrentOutputValue().isNumber());
 long timestamp = instant.getEpochSecond();
 assertEquals(Long.toString(timestamp), gen.getCurrentOutputValue().asText());
 }

 @Test
 public void testDateTimeSerializerBaseWithCustomSerializer() throws JsonProcessingException {
 DateTimeSerializerBase<Date> dateTimeSerializer = mapper.findValueSerializer(Date.class);
 ObjectMapper.SerializedFormat serializedFormat = new ObjectMapper.SerializedFormat();
 ToStringSerializer toStringSerializer = new ToStringSerializer(Date.class);
 serializedFormat.setSerializationView(toStringSerializer);
 JsonGenerator gen = mapper.getPolymorphicSerializerProvider().getGenerator();
 dateTimeSerializer.serialize(new Date(), gen, mapper.getSerializerProvider(), serializedFormat);
 assertNotNull(gen.getCurrentOutputValue());
 assertEquals("\"Sat Mar 31 10:20:30 EDT 2023\"", gen.getCurrentOutputValue().asText());
 }
}