package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

 private static final SerializerProvider serializers = mock(SerializerProvider.class);
 private static final ObjectMapper objectMapper = new ObjectMapper();
 private static final TypeSerializer typeSerializer = mock(TypeSerializer.class);

 @Test
 public void testDateTimeSerializerBaseInstance() {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
 assertNotNull(dateTimeSerializerBase);
 }

 @Test
 public void testDateTimeSerializerBaseWithFormat() {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
 assertNotNull(dateTimeSerializerBase);
 }

 @Test
 public void testDateTimeSerializerBaseWithUseTimestamp() {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, true, null);
 assertNotNull(dateTimeSerializerBase);
 }

 @Test
 public void testCreateContextual() throws IOException {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
 JsonFormat.Value jsonFormat = new JsonFormat.Value();
 jsonFormat.setShape(JsonFormat.Shape.STRING);
 jsonFormat.setLocale(Locale.ENGLISH);
 jsonFormat.setTimeZone(TimeZone.getDefault());
 JsonFormatVisitorWrapper visitorWrapper = mock(JsonFormatVisitorWrapper.class);
 when(visitorWrapper.getProvider()).thenReturn(serializers);
 when(visitorWrapper.getTypeHint()).thenReturn(Date.class);
 when(visitorWrapper.acceptJsonFormat(anyBoolean(), anyBoolean())).thenReturn(typeSerializer);
 when(typeSerializer.writeNull(any(JsonGenerator.class), any(SerializerProvider.class))).thenReturn(null);

 JsonNode result = dateTimeSerializerBase.createContextual(serializers, null).acceptJsonFormatVisitor(visitorWrapper, Date.class);
 assertEquals("null", result.asText());

 jsonFormat.setPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
 result = dateTimeSerializerBase.createContextual(serializers, null).acceptJsonFormatVisitor(visitorWrapper, Date.class);
 assertEquals("\"\\u0022\\u0022\"", result.asText());
 }

 @Test
 public void testSerialize() throws IOException {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
 Date date = new Date();
 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 when(jsonGenerator.writeString(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

 dateTimeSerializerBase.serialize(date, jsonGenerator, serializers);
 assertEquals(simpleDateFormat.format(date), jsonGenerator.getOutputValue().asText());
 }

 @Test
 public void testSerializeUseTimestamp() throws IOException {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, true, null);
 Date date = new Date();
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 when(jsonGenerator.writeNumber(anyLong())).thenAnswer(invocation -> Long.toString(invocation.getArgument(0)));

 dateTimeSerializerBase.serialize(date, jsonGenerator, serializers);
 assertEquals(Long.toString(date.getTime()), jsonGenerator.getOutputValue().asText());
 }

 @Test
 public void testIsEmpty() {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
 assertFalse(dateTimeSerializerBase.isEmpty(serializers, null));
 }

 @Test
 public void testGetSchema() {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
 JsonNode schema = dateTimeSerializerBase.getSchema(serializers, Date.class);
 assertEquals("string", schema.asText());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws IOException {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
 JsonFormatVisitorWrapper visitorWrapper = mock(JsonFormatVisitorWrapper.class);
 when(visitorWrapper.getProvider()).thenReturn(serializers);
 when(visitorWrapper.getTypeHint()).thenReturn(Date.class);
 when(visitorWrapper.acceptJsonFormat(anyBoolean(), anyBoolean())).thenReturn(typeSerializer);
 when(typeSerializer.writeNull(any(JsonGenerator.class), any(SerializerProvider.class))).thenReturn(null);

 dateTimeSerializerBase.acceptJsonFormatVisitor(visitorWrapper, Date.class);
 verify(visitorWrapper, times(1)).acceptJsonFormat(false, false);
 }

 @Test
 public void testSerializeAsString() throws IOException {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
 Date date = new Date();
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 when(jsonGenerator.writeString(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

 dateTimeSerializerBase._serializeAsString(date, jsonGenerator, serializers);
 assertEquals(date.toString(), jsonGenerator.getOutputValue().asText());
 }

 @Test
 public void testReusedCustomFormat() throws IOException {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
 Date date = new Date();
 SerializerProvider serializerProvider = mock(SerializerProvider.class);
 when(serializerProvider.getDefaultDateFormat()).thenReturn(new StdDateFormat());
 when(serializerProvider.findValueSerializer(Date.class)).thenReturn(dateTimeSerializerBase);
 when(serializerProvider.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);
 when(serializerProvider.getConfig()).thenReturn(objectMapper.getConfig());

 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
 when(objectMapper.getSerializerProvider()).thenReturn(serializerProvider);

 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 when(jsonGenerator.writeString(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

 dateTimeSerializerBase._serializeAsString(date, jsonGenerator, serializerProvider);
 assertEquals(simpleDateFormat.format(date), jsonGenerator.getOutputValue().asText());

 dateFormat = dateFormat.clone();
 dateTimeSerializerBase._serializeAsString(date, jsonGenerator, serializerProvider);
 assertEquals(simpleDateFormat.format(date), jsonGenerator.getOutputValue().asText());
 }
}