package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Before;
import org.junit.Test;

public class DateTimeSerializerBaseTest {

 private SerializerProvider serializers;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 serializers = new SerializerProvider();
 ObjectMapperFactory.instance.findValueSerializer(serializers, Date.class);
 mapper = new ObjectMapper(serializers);
 }

 @Test
 public void testSerializationWithDefaultSettings() throws IOException {
 DateTimeSerializerBase<Date> serializer = new StdDateSerializer();
 ObjectNode json = mapper.writeValueAsTree(new Date(), serializer);
 String jsonString = json.toString();
 assertEquals("\"2023-03-15T12:34:56.789Z\"", jsonString);
 }

 @Test
 public void testSerializationWithCustomFormat() throws IOException {
 DateTimeSerializerBase<Date> serializer = new StdDateSerializer()
 .withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss zzz", Locale.ENGLISH));
 ObjectNode json = mapper.writeValueAsTree(new Date(), serializer);
 String jsonString = json.toString();
 assertEquals("\"15-03-2023 12:34:56 EDT\"", jsonString);
 }

 @Test
 public void testSerializationAsTimestamp() throws IOException {
 DateTimeSerializerBase<Date> serializer = new StdDateSerializer().withFormat(Boolean.TRUE, null);
 ObjectNode json = mapper.writeValueAsTree(new Date(), serializer);
 String jsonString = json.toString();
 assertEquals("1678846456000", jsonString);
 }

 @Test
 public void testSerializationWithJsonFormatAnnotation() throws IOException {
 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm z")
 Date date = new Date();
 ObjectNode json = mapper.writeValueAsTree(date);
 String jsonString = json.toString();
 assertEquals("\"15-03-2023 12:34 PM\"", jsonString);
 }

 @Test
 public void testDeserializationWithDefaultSettings() throws IOException {
 DateTimeSerializerBase<Date> deserializer = new StdDateSerializer();
 Date date = mapper.readValue(
 "{ \"dt\": \"2023-03-15T12:34:56.789Z\" }", Date.class, deserializer);
 assertEquals(new Date(), date);
 }

 @Test
 public void testDeserializationWithCustomFormat() throws IOException {
 DateTimeSerializerBase<Date> deserializer = new StdDateSerializer()
 .withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss zzz", Locale.ENGLISH));
 Date date = mapper.readValue(
 "{ \"dt\": \"15-03-2023 12:34:56 EDT\" }", Date.class, deserializer);
 assertEquals(new Date(), date);
 }

 @Test
 public void testDeserializationAsTimestamp() throws IOException {
 DateTimeSerializerBase<Date> deserializer = new StdDateSerializer().withFormat(Boolean.TRUE, null);
 Date date = mapper.readValue("1678846456000", Date.class, deserializer);
 assertEquals(new Date(), date);
 }

 @Test
 public void testDeserializationWithJsonFormatAnnotation() throws IOException {
 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm z")
 Date date;
 String jsonString = "{\"dt\": \"15-03-2023 12:34 PM\"}";
 ObjectMapper objectMapper = new ObjectMapper();
 date = objectMapper.readValue(jsonString, Date.class);
 assertEquals(new Date(), date);
 }

 @Test(expected = IOException.class)
 public void testDeserializationWithInvalidJsonFormat() throws IOException {
 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm zzz")
 Date date;
 String jsonString = "{\"dt\": \"15-03-2023 12:34 PM\"}";
 mapper.readValue(jsonString, Date.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeserializationWithNullSerializerProvider() throws IOException {
 DateTimeSerializerBase<Date> deserializer = new StdDateSerializer();
 mapper.readValue("1678846456000", Date.class, deserializer);
 }
}