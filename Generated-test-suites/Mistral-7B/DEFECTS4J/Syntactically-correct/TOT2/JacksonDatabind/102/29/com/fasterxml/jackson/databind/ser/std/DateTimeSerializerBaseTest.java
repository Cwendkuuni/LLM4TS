package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DateTimeSerializerBaseTest {

 // Test for constructor
 @Test
 public void testDateTimeSerializerBaseConstructor() {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
 assertNotNull(dateTimeSerializerBase);
 }

 // Test for withFormat method
 @Test
 public void testWithFormat() throws JsonMappingException {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
 DateTimeSerializerBase<Date> dateTimeSerializerBaseWithFormat = dateTimeSerializerBase.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy"));
 assertNotSame(dateTimeSerializerBase, dateTimeSerializerBaseWithFormat);
 }

 // Test for isEmpty method
 @Test
 public void testIsEmpty() {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
 assertFalse(dateTimeSerializerBase.isEmpty(null, null));
 }

 // Test for getSchema method
 @Test
 public void testGetSchema() throws JsonMappingException {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
 ObjectNode schema = dateTimeSerializerBase.getSchema(null, Date.class);
 assertEquals("string", schema.get("type").asText());
 }

 // Test for serialize method with default format
 @Test
 public void testSerializeWithDefaultFormat() throws JsonGenerationException, IOException {
 ObjectMapper mapper = JsonMapperFactory.getMapper();
 SerializerProvider serializers = mapper.getSerializerProvider();
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
 Date date = new Date();
 ObjectNode result = mapper.writeValueAsTree(date, serializers, dateTimeSerializerBase);
 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 assertEquals(sdf.format(date), result.get("@date").asText());
 }

 // Test for serialize method with custom format
 @Test
 public void testSerializeWithCustomFormat() throws JsonGenerationException, IOException, ParseException {
 ObjectMapper mapper = JsonMapperFactory.getMapper();
 SerializerProvider serializers = mapper.getSerializerProvider();
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy"));
 Date date = new Date();
 ObjectNode result = mapper.writeValueAsTree(date, serializers, dateTimeSerializerBase);
 assertEquals("dd-MM-yyyy", result.get("@format").asText());
 assertEquals("dd-MM-yyyy", result.get("dd-MM-yyyy").asText());
 }

 // Test for serialize method with timestamp
 @Test
 public void testSerializeWithTimestamp() throws JsonGenerationException, IOException {
 ObjectMapper mapper = JsonMapperFactory.getMapper();
 SerializerProvider serializers = mapper.getSerializerProvider();
 DateTimeSerializerBase<Long> dateTimeSerializerBase = new DateTimeSerializerBase<Long>(Long.class, Boolean.TRUE, null);
 Long timestamp = 1633022099000L;
 ObjectNode result = mapper.writeValueAsTree(timestamp, serializers, dateTimeSerializerBase);
 assertEquals("number", result.get("type").asText());
 assertEquals("1633022099000", result.get("numberValue").asText());
 }

 // Test for serialize method with locale and timezone
 @Test
 public void testSerializeWithLocaleAndTimeZone() throws JsonGenerationException, IOException, ParseException {
 ObjectMapper mapper = JsonMapperFactory.getMapper();
 SerializerProvider serializers = mapper.getSerializerProvider();
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US, TimeZone.getTimeZone("UTC")));
 Date date = new Date();
 ObjectNode result = mapper.writeValueAsTree(date, serializers, dateTimeSerializerBase);
 assertEquals("dd-MM-yyyy HH:mm", result.get("@format").asText());
 assertEquals("dd-MM-yyyy HH:mm", result.get("dd-MM-yyyy HH:mm").asText());
 }

 // Test for serialize method with null value
 @Test
 public void testSerializeWithNullValue() throws JsonGenerationException, IOException {
 ObjectMapper mapper = JsonMapperFactory.getMapper();
 SerializerProvider serializers = mapper.getSerializerProvider();
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
 ObjectNode result = mapper.writeValueAsTree(null, serializers, dateTimeSerializerBase);
 assertTrue(result.isEmpty());
 }

 // Test for serialize method with empty string value
 @Test
 public void testSerializeWithEmptyStringValue() throws JsonGenerationException, IOException {
 ObjectMapper mapper = JsonMapperFactory.getMapper();
 SerializerProvider serializers = mapper.getSerializerProvider();
 DateTimeSerializerBase<String> dateTimeSerializerBase = new DateTimeSerializerBase<String>(String.class, false, null);
 ObjectNode result = mapper.writeValueAsTree("", serializers, dateTimeSerializerBase);
 assertTrue(result.isEmpty());
 }

}