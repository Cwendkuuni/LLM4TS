package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer.NullValueProvider;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

 private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
 private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

 @Test
 public void testDateTimeSerializerBase_default() throws JsonProcessingException, ParseException {
 Date date = new Date();
 DateTimeSerializerBase<Date> serializer = new StdDateSerializer();

 // Test serialization
 JsonGenerator gen = new JsonFactory().createGenerator(System.out);
 serializer.serialize(date, gen, new ObjectMapper().getSerializerProvider());
 JsonParser parser = new JsonFactory().createParser(new StringReader(gen.getOutputStr()));
 assertEquals(DATE_FORMAT.format(date), parser.getText());

 // Test deserialization
 ObjectNode node = new ObjectMapper().readValue(new StringReader(DATE_FORMAT.format(date)), ObjectNode.class);
 Date deserializedDate = new StdDateSerializer().deserialize(node, DateTimeSerializerBase.NullValueProvider.INSTANCE, new ObjectMapper().getSerializerProvider());
 assertEquals(date, deserializedDate);
 }

 @Test
 public void testDateTimeSerializerBase_useTimestamp() throws JsonProcessingException, ParseException {
 Date date = new Date();
 DateTimeSerializerBase<Date> serializer = new StdDateSerializer(true, null);

 // Test serialization
 JsonGenerator gen = new JsonFactory().createGenerator(System.out);
 serializer.serialize(date, gen, new ObjectMapper().getSerializerProvider());
 assertEquals("" + date.getTime(), gen.getOutputStr());

 // Test deserialization
 ObjectNode node = new ObjectMapper().readValue(new StringReader("1612345678000"), ObjectNode.class);
 Date deserializedDate = new StdDateSerializer().deserialize(node, DateTimeSerializerBase.NullValueProvider.INSTANCE, new ObjectMapper().getSerializerProvider());
 assertEquals(date, deserializedDate);
 }

 @Test
 public void testDateTimeSerializerBase_customFormat() throws JsonProcessingException, ParseException {
 Date date = new Date();
 DateTimeSerializerBase<Date> serializer = new StdDateSerializer(false, DATE_FORMAT);

 // Test serialization
 JsonGenerator gen = new JsonFactory().createGenerator(System.out);
 serializer.serialize(date, gen, new ObjectMapper().getSerializerProvider());
 assertEquals(DATE_FORMAT.format(date), gen.getOutputStr());

 // Test deserialization
 ObjectNode node = new ObjectMapper().readValue(new StringReader(DATE_FORMAT.format(date)), ObjectNode.class);
 Date deserializedDate = new StdDateSerializer().deserialize(node, DateTimeSerializerBase.NullValueProvider.INSTANCE, new ObjectMapper().getSerializerProvider());
 assertEquals(date, deserializedDate);
 }

 @Test
 public void testDateTimeSerializerBase_customFormat_locale() throws JsonProcessingException, ParseException {
 Date date = new Date();
 DateTimeSerializerBase<Date> serializer = new StdDateSerializer(false, DATE_FORMAT);
 DATE_FORMAT.setLocale(Locale.GERMAN);

 // Test serialization
 JsonGenerator gen = new JsonFactory().createGenerator(System.out);
 serializer.serialize(date, gen, new ObjectMapper().getSerializerProvider());
 assertEquals("01.12.2021 12:34:56 +0000", gen.getOutputStr());

 // Test deserialization
 ObjectNode node = new ObjectMapper().readValue(new StringReader("01.12.2021 12:34:56 +0000"), ObjectNode.class);
 Date deserializedDate = new StdDateSerializer().deserialize(node, DateTimeSerializerBase.NullValueProvider.INSTANCE, new ObjectMapper().getSerializerProvider());
 assertEquals(date, deserializedDate);
 }

 @Test
 public void testDateTimeSerializerBase_customFormat_timezone() throws JsonProcessingException, ParseException {
 Date date = new Date();
 DateTimeSerializerBase<Date> serializer = new StdDateSerializer(false, DATE_FORMAT);
 DATE_FORMAT.setTimeZone(UTC);

 // Test serialization
 JsonGenerator gen = new JsonFactory().createGenerator(System.out);
 serializer.serialize(date, gen, new ObjectMapper().getSerializerProvider());
 assertEquals("2021-12-01T12:34:56.000Z", gen.getOutputStr());

 // Test deserialization
 ObjectNode node = new ObjectMapper().readValue(new StringReader("2021-12-01T12:34:56.000Z"), ObjectNode.class);
 Date deserializedDate = new StdDateSerializer().deserialize(node, DateTimeSerializerBase.NullValueProvider.INSTANCE, new ObjectMapper().getSerializerProvider());
 assertNotNull(deserializedDate);
 assertEquals(date.getTime(), deserializedDate.getTime());
 }

 @Test
 public void testDateTimeSerializerBase_customFormat_invalidPattern() throws JsonProcessingException {
 DateTimeSerializerBase<Date> serializer = new StdDateSerializer(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss xxx"));

 // Test serialization
 JsonGenerator gen = new JsonFactory().createGenerator(System.out);
 try {
 serializer.serialize(new Date(), gen, new ObjectMapper().getSerializerProvider());
 fail();
 } catch (JsonGenerationException e) {
 // Expected
 }

 // Test deserialization
 ObjectNode node = new ObjectMapper().readValue(new StringReader("2021-12-01 12:34:56 EST"), ObjectNode.class);
 try {
 new StdDateSerializer().deserialize(node, DateTimeSerializerBase.NullValueProvider.INSTANCE, new ObjectMapper().getSerializerProvider());
 fail();
 } catch (JsonMappingException e) {
 // Expected
 }
 }
}