package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("unchecked")
public class DateTimeSerializerBaseTest {

 private static final Class<java.util.Date> DATE_CLASS = java.util.Date.class;

 @SuppressWarnings("deprecation")
 @Test
 public void testDateTimeSerializerBaseConstructor() {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(DATE_CLASS, false, null);
 assertNotNull(serializer);
 }

 @Test
 public void testDateTimeSerializerBaseConstructorWithUseTimestamp() {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(DATE_CLASS, true, null);
 assertTrue(serializer._useTimestamp);
 }

 @Test
 public void testDateTimeSerializerBaseConstructorWithCustomFormat() {
 SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.US);
 customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(DATE_CLASS, false, customFormat);
 assertSame(customFormat, serializer._customFormat);
 }

 @Test
 public void testDateTimeSerializerBaseWithFormat() {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(DATE_CLASS, false, null);
 DateTimeSerializerBase<Date> withFormatSerializer = serializer.withFormat(false, null);
 assertEquals(serializer, withFormatSerializer);

 DateTimeSerializerBase<Date> withFormatCustomSerializer = serializer.withFormat(false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.US));
 assertNotEquals(serializer, withFormatCustomSerializer);
 assertSame(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.US), withFormatCustomSerializer._customFormat);
 }

 @Test
 public void testCreateContextualWithNullProperty() throws JsonMappingException {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(DATE_CLASS, false, null);
 JsonSerializer<?> result = serializer.createContextual(null, null);
 assertEquals(serializer, result);
 }

 @Test
 public void testCreateContextualWithNullTypeHint() throws JsonMappingException {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(DATE_CLASS, false, null);
 JsonSerializer<?> result = serializer.createContextual(null, null);
 assertEquals(serializer, result);
 }

 @Test
 public void testCreateContextualWithoutFormatOverride() throws JsonMappingException {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(DATE_CLASS, false, null);
 JsonFormat.Value format = new JsonFormat.Value();
 SerializerProvider serializers = new SerializerProvider();
 JsonSerializer<?> result = serializer.createContextual(serializers, null);
 assertTrue(result instanceof DateTimeSerializerBase);
 assertFalse(result._useTimestamp);
 assertNull(result._customFormat);
 }

 @Test
 public void testCreateContextualWithFormatOverrideShapeNumeric() throws JsonMappingException {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(DATE_CLASS, false, null);
 JsonFormat.Value format = new JsonFormat.Value().shape(JsonFormat.Shape.NUMBER);
 SerializerProvider serializers = new SerializerProvider();
 JsonSerializer<?> result = serializer.createContextual(serializers, null);
 assertTrue(result instanceof DateTimeSerializerBase);
 assertTrue(result._useTimestamp);
 assertNull(result._customFormat);
 }

 @Test
 public void testCreateContextualWithFormatOverrideShapeString() throws JsonMappingException {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(DATE_CLASS, false, null);
 JsonFormat.Value format = new JsonFormat.Value().shape(JsonFormat.Shape.STRING);
 SerializerProvider serializers = new SerializerProvider();
 JsonSerializer<?> result = serializer.createContextual(serializers, null);
 assertTrue(result instanceof DateTimeSerializerBase);
 assertFalse(result._useTimestamp);
 assertNull(result._customFormat);
 }

 @Test
 public void testCreateContextualWithFormatOverridePattern() throws JsonMappingException {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(DATE_CLASS, false, null);
 JsonFormat.Value format = new JsonFormat.Value().pattern("dd-MM-yyyy HH:mm:ss z").shape(JsonFormat.Shape.STRING);
 SerializerProvider serializers = new SerializerProvider();
 JsonSerializer<?> result = serializer.createContextual(serializers, null);
 assertTrue(result instanceof DateTimeSerializerBase);
 assertFalse(result._useTimestamp);
 SimpleDateFormat expectedCustomFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.US);
 assertSame(expectedCustomFormat, result._customFormat);
 }

 @Test
 public void testCreateContextualWithFormatOverrideLocale() throws JsonMappingException {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(DATE_CLASS, false, null);
 JsonFormat.Value format = new JsonFormat.Value().locale(Locale.FRENCH).shape(JsonFormat.Shape.STRING);
 SerializerProvider serializers = new SerializerProvider();
 JsonSerializer<?> result = serializer.createContextual(serializers, null);
 assertTrue(result instanceof DateTimeSerializerBase);
 assertFalse(result._useTimestamp);
 SimpleDateFormat expectedCustomFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.FRENCH);
 assertSame(expectedCustomFormat, result._customFormat);
 }

 @Test
 public void testCreateContextualWithFormatOverrideTimeZone() throws JsonMappingException {
 DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(DATE_CLASS, false, null);
 JsonFormat.Value format = new JsonFormat.Value().timeZone(TimeZone.getTimeZone("Europe/London")).shape(JsonFormat.Shape.STRING);
 SerializerProvider serializers = new SerializerProvider();
 JsonSerializer<?> result = serializer.createContextual(serializers, null);
 assertTrue(result instanceof DateTimeSerializerBase);
 assertFalse(result._useTimestamp);
 SimpleDateFormat expectedCustomFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.US);
 expectedCustomFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
 assertSame(expectedCustomFormat, result._customFormat);
 }

}