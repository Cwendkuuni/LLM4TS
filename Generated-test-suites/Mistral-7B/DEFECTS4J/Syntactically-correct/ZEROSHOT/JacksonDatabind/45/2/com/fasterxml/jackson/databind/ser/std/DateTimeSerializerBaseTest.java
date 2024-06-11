package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Long> longSerializer;
 private DateTimeSerializerBase<java.util.Date> dateSerializer;

 @Before
 public void setup() throws JsonProcessingException {
 SimpleModule module = new SimpleModule();
 ObjectMapper mapper = new ObjectMapper();
 mapper.registerModule(module);
 this.longSerializer = mapper.getTypeFactory().constructValueType(DateTimeSerializerBase.class, Long.class);
 this.dateSerializer = mapper.getTypeFactory().constructValueType(DateTimeSerializerBase.class, java.util.Date.class);
 }

 @Test
 public void testCreateContextualWithDefaults() throws JsonGenerationException, JsonMappingException, IOException {
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 JsonSerializer<Long> serializer = longSerializer.createContextual(null, null);
 Long value = new Long(System.currentTimeMillis());
 String json = writer.writeValueAsString(value);
 JsonParser parser = new ObjectMapper().getFactory().createParser(json);
 Long deserializedValue = parser.readValueAs(Long.class);
 assertEquals(value, deserializedValue);
 }

 @Test
 public void testCreateContextualWithCustomFormat() throws JsonGenerationException, JsonMappingException, IOException {
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 format.setTimeZone(timeZone);
 JsonSerializer<Long> serializer = longSerializer.withFormat(false, format).createContextual(null, null);
 Long value = new Long(System.currentTimeMillis());
 String json = writer.writeValueAsString(value);
 JsonParser parser = new ObjectMapper().getFactory().createParser(json);
 Long deserializedValue = parser.readValueAs(Long.class);
 assertEquals(value, deserializedValue);
 String formattedJson = writer.writeValueAsString(value, new SimpleObjectWriter.SerializationConfig("customFormat", format));
 JsonParser customFormatParser = new ObjectMapper().getFactory().createParser(formattedJson);
 Long deserializedCustomFormatValue = customFormatParser.readValueAs(Long.class);
 assertEquals(value, deserializedCustomFormatValue);
 }

 @Test
 public void testCreateContextualWithJsonFormatAnnotation() throws JsonGenerationException, JsonMappingException, IOException {
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 JsonFormat jsonFormat = new JsonFormat();
 jsonFormat.setShape(JsonFormat.Shape.STRING);
 jsonFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
 jsonFormat.setLocale(Locale.US);
 jsonFormat.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 SimpleDateFormat format = new SimpleDateFormat(jsonFormat.getPattern(), jsonFormat.getLocale());
 format.setTimeZone(jsonFormat.getTimeZone());
 AnnotatedMember property = JsonTestUtils.findProperty(DateTimeSerializerBaseTest.class, "dateField");
 JsonSerializer<java.util.Date> serializer = dateSerializer.createContextual(null, property);
 java.util.Date value = Calendar.getInstance().getTime();
 String json = writer.writeValueAsString(value);
 JsonParser parser = new ObjectMapper().getFactory().createParser(json);
 java.util.Date deserializedValue = parser.readValueAs(java.util.Date.class);
 assertEquals(value, deserializedValue);
 String formattedJson = writer.writeValueAsString(value, new SimpleObjectWriter.SerializationConfig("jsonFormat", jsonFormat));
 JsonParser jsonFormatParser = new ObjectMapper().getFactory().createParser(formattedJson);
 java.util.Date deserializedJsonFormatValue = jsonFormatParser.readValueAs(java.util.Date.class);
 assertEquals(value, deserializedJsonFormatValue);
 }

 @Test
 public void testIsEmpty() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Long> serializer = longSerializer.createContextual(null, null);
 assertTrue(serializer.isEmpty(null));
 Long value = new Long(0);
 assertTrue(serializer.isEmpty(null, value));
 }

 @Test
 public void testGetSchema() throws JsonProcessingException {
 JsonSerializer<Long> serializer = longSerializer.createContextual(null, null);
 assertEquals("number", serializer.getSchema(null, Long.class.getType()).asText());
 JsonSerializer<java.util.Date> dateSerializer = dateSerializer.createContextual(null, null);
 assertEquals("string", dateSerializer.getSchema(null, java.util.Date.class.getType()).asText());
 }

 @Test
 public void testSerialize() throws JsonGenerationException, JsonMappingException, IOException {
 ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
 String json = writer.writeValueAsString(new Long(System.currentTimeMillis()));
 JsonParser parser = new ObjectMapper().getFactory().createParser(json);
 JsonSerializer<Long> serializer = longSerializer.createContextual(null, null);
 Long value = parser.readValueAs(Long.class);
 Long deserializedValue = new Long(System.currentTimeMillis());
 SerializationTestUtils.serializeDeserializeRoundtripTest(serializer, value, deserializedValue);
 json = writer.writeValueAsString(Calendar.getInstance().getTime());
 parser = new ObjectMapper().getFactory().createParser(json);
 JsonSerializer<java.util.Date> dateSerializer = dateSerializer.createContextual(null, null);
 java.util.Date valueDate = parser.readValueAs(java.util.Date.class);
 java.util.Date deserializedDateValue = new java.util.Date();
 SerializationTestUtils.serializeDeserializeRoundtripTest(dateSerializer, valueDate, deserializedDateValue);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonGenerationException, JsonMappingException, IOException {
 JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null, null, null, new SimpleBeanPropertyFilter());
 JsonSerializer<Long> serializer = longSerializer.createContextual(null, null);
 serializer.acceptJsonFormatVisitor(visitor, Long.class.getType());
 JsonSerializer<java.util.Date> dateSerializer = dateSerializer.createContextual(null, null);
 dateSerializer.acceptJsonFormatVisitor(visitor, java.util.Date.class.getType());
 }

 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
 private java.util.Date dateField;
}