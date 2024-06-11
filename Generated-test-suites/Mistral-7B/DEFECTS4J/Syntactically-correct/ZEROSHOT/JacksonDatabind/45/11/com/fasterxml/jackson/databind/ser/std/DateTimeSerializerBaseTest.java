package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

public class DateTimeSerializerBaseTest {

 private static final Long ZERO_TIMESTAMP = 0L;
 private static final Object TEST_DATE_OBJ = new Object(){
     private Long timestamp;
     DateTimeSerializerBaseTest(Long timestamp){ this.timestamp = timestamp; }
     public Long getTimestamp(){ return timestamp; }
 };

 @SuppressWarnings("unchecked")
 @Test
 public void testDateTimeSerializerBaseInstanceCreation() throws JsonProcessingException {
     // Test default constructor
     DateTimeSerializerBase<Long> defaultSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
     assertEquals(Long.TYPE, defaultSerializer.getType());
     assertNull(defaultSerializer._useTimestamp);
     assertNull(defaultSerializer._customFormat);

     // Test constructor with useTimestamp=true
     DateTimeSerializerBase<Long> useTimestampSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);
     assertTrue(useTimestampSerializer._useTimestamp.booleanValue());
     assertNull(useTimestampSerializer._customFormat);

     // Test constructor with useTimestamp=false and customFormat
     DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a z");
     Locale locale = Locale.US;
     TimeZone timeZone = TimeZone.getTimeZone("EST");
     DateTimeSerializerBase<Long> customFormatSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, customFormat);
     assertFalse(customFormatSerializer._useTimestamp.booleanValue());
     assertEquals(customFormat, customFormatSerializer._customFormat);
     assertEquals(locale, customFormatSerializer._customFormat.getLocale());
     assertEquals(timeZone, customFormatSerializer._customFormat.getTimeZone());
 }

 @Test
 public void testDateTimeSerializerBaseIsEmpty() throws JsonProcessingException {
     assertTrue(DateTimeSerializerBase.isEmpty(null));
     assertTrue(DateTimeSerializerBase.isEmpty(new DateTimeSerializerBase<Long>(Long.TYPE, false, null), null));
     assertTrue(DateTimeSerializerBase.isEmpty(new DateTimeSerializerBase<Long>(Long.TYPE, false, null), TEST_DATE_OBJ));
     assertTrue(DateTimeSerializerBase.isEmpty(new DateTimeSerializerBase<Long>(Long.TYPE, true, null), TEST_DATE_OBJ));
     assertFalse(DateTimeSerializerBase.isEmpty(new Object()));
     assertFalse(DateTimeSerializerBase.isEmpty(new DateTimeSerializerBase<Long>(Long.TYPE, false, null), new Object()));
     assertFalse(DateTimeSerializerBase.isEmpty(new DateTimeSerializerBase<Long>(Long.TYPE, true, null), new Object()));
 }

 @Test
 public void testDateTimeSerializerBaseGetSchema() throws JsonProcessingException {
     DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
     JsonNode schema = serializer.getSchema(null, Long.TYPE);
     assertEquals("string", schema.getClass().getName().getSimpleName());
     assertTrue(schema.isTextual());
 }

 @Test
 public void testDateTimeSerializerBaseAcceptJsonFormatVisitor() throws JsonMappingException {
     DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
     ObjectMapper mapper = new ObjectMapper();
     JsonFormatVisitorWrapper visitor = mapper.schemaGenerator().findSerializerProvider().getSchemaSerializer();
     serializer.acceptJsonFormatVisitor(visitor, Long.TYPE);
     assertEquals(JsonFormatTypes.DATE_TIME, visitor.getFormatTypes().get(0));
 }

 @Test
 public void testDateTimeSerializerBaseSerializeWithDefaultFormat() throws IOException, JsonGenerationException {
     DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
     ObjectMapper mapper = new ObjectMapper();
     JsonGenerator gen = mapper.getFactory().createGenerator();
     serializer.serialize(TEST_DATE_OBJ, gen, mapper.getSerializerProvider());
     String result = gen.getOutputContent();
     assertEquals("\"2023-03-23T14:25:36.000Z\"", result);
 }

 @Test
 public void testDateTimeSerializerBaseSerializeWithCustomFormat() throws IOException, JsonGenerationException {
     DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a z");
     DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, customFormat);
     ObjectMapper mapper = new ObjectMapper();
     JsonGenerator gen = mapper.getFactory().createGenerator();
     serializer.serialize(TEST_DATE_OBJ, gen, mapper.getSerializerProvider());
     String result = gen.getOutputContent();
     assertEquals("\"25-03-2023 14:25:36 PM EST\"", result);
 }

 @Test
 public void testDateTimeSerializerBaseSerializeWithUseTimestamp() throws IOException, JsonGenerationException {
     DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);
     ObjectMapper mapper = new ObjectMapper();
     JsonGenerator gen = mapper.getFactory().createGenerator();
     serializer.serialize(TEST_DATE_OBJ, gen, mapper.getSerializerProvider());
     String result = gen.getOutputContent();
     assertEquals("1679206136000", result);
 }

 @Test
 public void testDateTimeSerializerBaseCreateContextual() throws JsonMappingException {
     ObjectMapper mapper = new ObjectMapper();
     BeanPropertyDefinition property = new BeanPropertyDefinition("test", Long.class);
     JsonFormat.Value format = new JsonFormat.Value();
     format.setShape(JsonFormat.Shape.STRING);
     format.setTimeZone(TimeZone.getTimeZone("EST"));
     format.setLocale(Locale.US);
     format.setPattern("dd-MM-yyyy hh:mm:ss a z");
     JsonSerializer<Long> resultSerializer = mapper.getSerializerProvider().findValueSerializer(mapper.getSerializerProvider().isEnabledForSerialization(Long.class));
     JsonSerializer<Long> customSerializer = mapper.getSerializerProvider().findValueSerializer(Long.class);
     assertEquals(resultSerializer, mapper.getSerializerProvider().getSerializerProvider().getBeanPropertyWriter(property).getSerializer());
     JsonSerializer<Long> serializer = customSerializer.withFormat(Boolean.FALSE, format);
     assertEquals(serializer, mapper.getSerializerProvider().getSerializerProvider().getBeanPropertyWriter(property).getSerializer());
     JsonSerializer<Long> timestampSerializer = mapper.getSerializerProvider().getSerializerProvider().findValueSerializer(Long.class);
     assertNotEquals(timestampSerializer, mapper.getSerializerProvider().getSerializerProvider().getBeanPropertyWriter(property).getSerializer());
 }

}