package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

 private static final Class<java.util.Date> DATE_CLASS = java.util.Date.class;
 private JsonFactory jsonFactory;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 jsonFactory = new JsonFactory();
 objectMapper = new ObjectMapper(jsonFactory);
 }

 @Test
 public void testDateTimeSerializerBaseConstruction() {
 // Test default constructor
 DateTimeSerializerBase<?> defaultSerializer = new DateTimeSerializerBase<java.util.Date>(DATE_CLASS, null, null);
 assertEquals(DATE_CLASS, defaultSerializer.handledType());
 assertNull(defaultSerializer._useTimestamp);
 assertNull(defaultSerializer._customFormat);

 // Test constructor with useTimestamp=true and null customFormat
 DateTimeSerializerBase<java.util.Date> timestampSerializer = new DateTimeSerializerBase<java.util.Date>(DATE_CLASS, true, null);
 assertEquals(DATE_CLASS, timestampSerializer.handledType());
 assertTrue(timestampSerializer._useTimestamp);
 assertNull(timestampSerializer._customFormat);

 // Test constructor with useTimestamp=false and customFormat
 DateTimeSerializerBase<java.util.Date> customFormatSerializer = new DateTimeSerializerBase<java.util.Date>(DATE_CLASS, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
 assertEquals(DATE_CLASS, customFormatSerializer.handledType());
 assertFalse(customFormatSerializer._useTimestamp);
 assertNotNull(customFormatSerializer._customFormat);
 }

 @Test
 public void testWithFormat() {
 DateTimeSerializerBase<java.util.Date> customFormatSerializer = new DateTimeSerializerBase<java.util.Date>(DATE_CLASS, false, null);
 DateTimeSerializerBase<java.util.Date> timestampSerializer = customFormatSerializer.withFormat(true, null);
 DateTimeSerializerBase<java.util.Date> customFormatWithTimestampSerializer = customFormatSerializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

 assertEquals(DATE_CLASS, timestampSerializer.handledType());
 assertTrue(timestampSerializer._useTimestamp);
 assertNull(timestampSerializer._customFormat);

 assertEquals(DATE_CLASS, customFormatWithTimestampSerializer.handledType());
 assertFalse(customFormatWithTimestampSerializer._useTimestamp);
 assertNotNull(customFormatWithTimestampSerializer._customFormat);
 }

 @Test
 public void testCreateContextualWithNullProperty() throws JsonMappingException {
 JsonSerializer<?> serializedSerializer = objectMapper.getSerializerProvider().getSerializerProvider().getSerializerProvider()
 .findValueSerializer(DATE_CLASS, null);
 assertEquals(DateTimeSerializerBase.class, serializedSerializer.getClass());
 }

 @Test
 public void testCreateContextualWithProperty() throws JsonMappingException {
 Object testObject = new Object() {{
 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
 java.util.Date date = new java.util.Date();
 }};
 JsonSerializer<?> serializedSerializer = objectMapper.findValueSerializer(Object.class, "date");
 assertEquals(DateTimeSerializerBase.class, serializedSerializer.getClass());
 }

 @Test
 public void testSerializeWithNullValue() throws IOException {
 JavaType javaType = objectMapper.getTypeFactory().constructType(DateTimeSerializerBaseTest.class);
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 DateTimeSerializerBase<java.util.Date> serializer = new DateTimeSerializerBase<java.util.Date>(DATE_CLASS, false, null);
 serializer.serialize(null, jsonGenerator, null);
 jsonGenerator.flush();
 ByteArrayOutputStream outputStream = (ByteArrayOutputStream) jsonGenerator.getOutputSource();
 String output = outputStream.toString();
 assertTrue(output.isEmpty());
 }

 @Test
 public void testSerializeWithDefaultFormat() throws IOException {
 JavaType javaType = objectMapper.getTypeFactory().constructType(DateTimeSerializerBaseTest.class);
 java.util.Date date = new java.util.Date();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 DateTimeSerializerBase<java.util.Date> serializer = new DateTimeSerializerBase<java.util.Date>(DATE_CLASS, false, null);
 serializer.serialize(date, jsonGenerator, null);
 jsonGenerator.flush();
 ByteArrayOutputStream outputStream = (ByteArrayOutputStream) jsonGenerator.getOutputSource();
 String output = outputStream.toString();
 assertEquals("\"2023-03-14T12:10:30.000Z\"", output);
 }

 @Test
 public void testSerializeWithCustomFormat() throws IOException {
 JavaType javaType = objectMapper.getTypeFactory().constructType(DateTimeSerializerBaseTest.class);
 java.util.Date date = new java.util.Date();
 SimpleDateFormat customFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
 DateTimeSerializerBase<java.util.Date> serializer = new DateTimeSerializerBase<java.util.Date>(DATE_CLASS, false, customFormat);
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(date, jsonGenerator, null);
 jsonGenerator.flush();
 ByteArrayOutputStream outputStream = (ByteArrayOutputStream) jsonGenerator.getOutputSource();
 String output = outputStream.toString();
 assertEquals("\"Wed, Mar 15, '23\"", output);
 }

 @Test
 public void testSerializeWithTimestamp() throws IOException {
 JavaType javaType = objectMapper.getTypeFactory().constructType(DateTimeSerializerBaseTest.class);
 java.util.Date date = new java.util.Date();
 DateTimeSerializerBase<java.util.Date> serializer = new DateTimeSerializerBase<java.util.Date>(DATE_CLASS, true, null);
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(date, jsonGenerator, null);
 jsonGenerator.flush();
 ByteArrayOutputStream outputStream = (ByteArrayOutputStream) jsonGenerator.getOutputSource();
 String output = outputStream.toString();
 assertEquals("1678835200000", output);
 }

 @Test
 public void testSerializeWithTimestampAndCustomFormat() throws IOException {
 JavaType javaType = objectMapper.getTypeFactory().constructType(DateTimeSerializerBaseTest.class);
 java.util.Date date = new java.util.Date();
 SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 DateTimeSerializerBase<java.util.Date> serializer = new DateTimeSerializerBase<java.util.Date>(DATE_CLASS, true, customFormat);
 JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(date, jsonGenerator, null);
 jsonGenerator.flush();
 ByteArrayOutputStream outputStream = (ByteArrayOutputStream) jsonGenerator.getOutputSource();
 String output = outputStream.toString();
 assertEquals("\"2023-03-14T12:10:30.000Z\"", output);
 }

 @Test
 public void testGetSchema() throws IOException {
 JavaType javaType = objectMapper.getTypeFactory().constructType(DateTimeSerializerBaseTest.class);
 JsonNode schema = new DateTimeSerializerBase<java.util.Date>(DATE_CLASS, false, null).getSchema(objectMapper.getSerializerProvider(), javaType);
 assertEquals("number", schema.getType().getClassName());
 }

 @Test
 public void testAcceptJsonFormatVisitorWithNullTypeHint() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null);
 new DateTimeSerializerBase<java.util.Date>(DATE_CLASS, false, null).acceptJsonFormatVisitor(visitor, null);
 }

 @Test
 public void testAcceptJsonFormatVisitorWithTypeHint() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null);
 new DateTimeSerializerBase<java.util.Date>(DATE_CLASS, false, null).acceptJsonFormatVisitor(visitor, objectMapper.getTypeFactory().constructType(DateTimeSerializerBaseTest.class));
 }

 @Test
 public void testAcceptJsonFormatVisitorWithTimestamp() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null);
 new DateTimeSerializerBase<java.util.Date>(DATE_CLASS, true, null).acceptJsonFormatVisitor(visitor, null);
 }
}