package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

 // Test for serializing a Date using the default serializer
 @Test
 public void testDefaultSerialization() throws Exception {
  ObjectMapper mapper = new ObjectMapper();
  mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

  DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();

  Date date = new Date();
  String jsonString = mapper.writeValueAsString(date);

  SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
  Date expectedDate = expectedFormat.parse(jsonString);

  assertEquals(expectedDate.getTime(), date.getTime());
 }

 // Test for serializing a Date using a custom format with the default TimeZone
 @Test
 public void testCustomSerializationWithDefaultTimeZone() throws Exception {
  ObjectMapper mapper = new ObjectMapper();
  mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

  DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer()
          .withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm"));

  Date date = new Date();
  String jsonString = mapper.writeValueAsString(date);

  SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
  Date expectedDate = expectedFormat.parse(jsonString);

  assertEquals(expectedDate.getTime(), date.getTime());
 }

 // Test for serializing a Date using a custom format with a specific TimeZone
 @Test
 public void testCustomSerializationWithCustomTimeZone() throws Exception {
  ObjectMapper mapper = new ObjectMapper();
  mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

  DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer()
          .withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm")).withTimeZone(TimeZone.getTimeZone("UTC"));

  Date date = new Date();
  String jsonString = mapper.writeValueAsString(date);

  SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm'Z'");
  Date expectedDate = expectedFormat.parse(jsonString);

  assertEquals(expectedDate.getTime(), date.getTime());
 }

 // Test for serializing a null Date
 @Test
 public void testSerializationOfNullDate() throws Exception {
  ObjectMapper mapper = new ObjectMapper();
  mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

  DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();

  assertNull(mapper.writeValueAsString(null));
 }
}