package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperConfig;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

 private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

 @Test
 public void testSerializationWithDefaultFormat() throws IOException {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 ObjectMapper mapper = new ObjectMapper();
 SerializerProvider serializers = mapper.getSerializationConfig().getDefaultSerializerProvider();
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, SerializerProvider.StdIOProvider.getInstance());

 Date date = new Date();
 serializer.serialize(date, gen, serializers);
 gen.flush();

 JsonParser parser = mapper.getFactory().createParser(System.in);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token);
 Date parsedDate = mapper.readValue(parser, Date.class);
 assertEquals(date, parsedDate);
 }

 @Test
 public void testSerializationWithCustomFormat() throws IOException {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, DATE_FORMAT);
 ObjectMapper mapper = new ObjectMapper();
 JsonMapperConfig config = mapper.getSerializationConfig();
 config.setDateFormat(DATE_FORMAT);
 SerializerProvider serializers = mapper.getSerializationConfig().getDefaultSerializerProvider();
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, SerializerProvider.StdIOProvider.getInstance());

 Date date = new Date();
 serializer.serialize(date, gen, serializers);
 gen.flush();

 JsonParser parser = mapper.getFactory().createParser(System.in);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token);
 Date parsedDate = mapper.readValue(parser, Date.class);
 assertEquals(date, parsedDate);
 }

 @Test
 public void testSerializationWithTimestampEnabled() throws IOException {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
 ObjectMapper mapper = new ObjectMapper();
 SerializerProvider serializers = mapper.getSerializationConfig().getDefaultSerializerProvider();
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, SerializerProvider.StdIOProvider.getInstance());

 Date date = new Date();
 serializer.serialize(date, gen, serializers);
 gen.flush();

 JsonParser parser = mapper.getFactory().createParser(System.in);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 long timestamp = parser.numberValue();
 assertEquals(date.getTime(), timestamp);
 }

 @Test
 public void testSerializationWithCustomFormatAndTimeZone() throws IOException, ParseException {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, DATE_FORMAT);
 ObjectMapper mapper = new ObjectMapper();
 JsonMapperConfig config = mapper.getSerializationConfig();
 config.setDateFormat(DATE_FORMAT);
 config.setTimeZone(TimeZone.getTimeZone("UTC"));
 SerializerProvider serializers = mapper.getSerializationConfig().getDefaultSerializerProvider();
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, SerializerProvider.StdIOProvider.getInstance());

 Date date = new Date();
 serializer.serialize(date, gen, serializers);
 gen.flush();

 JsonParser parser = mapper.getFactory().createParser(System.in);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token);
 Date parsedDate = mapper.readValue(parser, Date.class);
 assertEquals(date, parsedDate);

 // Check serialization with custom time zone
 DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
 serializer = new DateTimeSerializerBase<Date>(Date.class, false, DATE_FORMAT);
 config.setDateFormat(DATE_FORMAT);
 serializers = mapper.getSerializationConfig().getDefaultSerializerProvider();
 gen = mapper.getFactory().createGenerator(System.out, SerializerProvider.StdIOProvider.getInstance());

 date = new Date();
 serializer.serialize(date, gen, serializers);
 gen.flush();

 parser = mapper.getFactory().createParser(System.in);
 token = parser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token);
 parsedDate = mapper.readValue(parser, Date.class);
 // Check that the parsed date is in the proper time zone
 assertEquals("Europe/Paris", parsedDate.toZoneString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializationWithNullProvider() throws IOException {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 // This should throw an IllegalArgumentException
 serializer.serialize(new Date(), null, null);
 }
}