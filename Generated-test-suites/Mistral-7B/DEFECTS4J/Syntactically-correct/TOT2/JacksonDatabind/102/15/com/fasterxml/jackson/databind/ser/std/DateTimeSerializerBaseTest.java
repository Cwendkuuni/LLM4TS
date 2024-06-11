package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 }

 @Test
 public void testSerializeDateWithDefaultFormat() throws IOException {
 DateTimeSerializerBase<Date> serializer = new StdDateSerializer();
 Date date = new Date();
 String expected = "2022-12-12T12:12:12.123Z";
 JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
 serializer.serialize(date, gen, null);
 gen.flush();
 String actual = gen.getOutputContent().toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeDateWithCustomFormat() throws IOException, ParseException {
 DateTimeSerializerBase<Date> serializer = new StdDateSerializer();
 Date date = new Date();
 Locale locale = Locale.US;
 TimeZone timeZone = TimeZone.getTimeZone("GMT-08:00");
 SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", locale);
 customFormat.setTimeZone(timeZone);
 serializer = (DateTimeSerializerBase<Date>) serializer.withFormat(Boolean.FALSE, customFormat);
 String expected = "2022-12-12 12:12:12 PST";
 JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
 serializer.serialize(date, gen, null);
 gen.flush();
 String actual = gen.getOutputContent().toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeNullDate() throws IOException {
 DateTimeSerializerBase<Date> serializer = new StdDateSerializer();
 JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
 serializer.serialize(null, gen, null);
 String actual = gen.getOutputContent().toString();
 assertEquals("null", actual);
 }

 @Test(expected = IOException.class)
 public void testSerializeNonDateValue() throws IOException {
 DateTimeSerializerBase<String> serializer = new StdScalarSerializer<String>(String.class);
 String value = "Hello World";
 JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
 serializer.serialize(value, gen, null);
 }

 @Test
 public void testDeserializeDateWithDefaultFormat() throws IOException, ParseException {
 DateTimeSerializerBase<Date> serializer = new StdDateSerializer();
 String json = "\"2022-12-12T12:12:12.123Z\"";
 Date date = mapper.readValue(json, Date.class);
 assertEquals(new Date(1670971612123L), date);
 }

 @Test
 public void testDeserializeDateWithCustomFormat() throws IOException, ParseException {
 DateTimeSerializerBase<Date> serializer = new StdDateSerializer();
 String json = "\"2022-12-12 12:12:12 PST\"";
 Locale locale = Locale.US;
 TimeZone timeZone = TimeZone.getTimeZone("GMT-08:00");
 SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", locale);
 customFormat.setTimeZone(timeZone);
 serializer = (DateTimeSerializerBase<Date>) mapper.findValueSerializer(Date.class, mapper.findRootValueDeserializer());
 JsonParser parser = mapper.getFactory().createParser(json);
 Date date = serializer.deserialize(parser, null);
 assertEquals(new Date(1670971612123L), date);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidDateFormat() throws IOException, ParseException {
 DateTimeSerializerBase<Date> serializer = new StdDateSerializer();
 String json = "\"InvalidDateFormat\"";
 JsonParser parser = mapper.getFactory().createParser(json);
 serializer = (DateTimeSerializerBase<Date>) mapper.findValueSerializer(Date.class, mapper.findRootValueDeserializer());
 serializer.deserialize(parser, null);
 }
}