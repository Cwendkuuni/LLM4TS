package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProviders;
import com.fasterxml.jackson.databind.method.ParamName;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Date> serializer;
 private SerializerProvider serializers;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 serializers = new SerializerProviders.Base();
 mapper = new ObjectMapper();
 serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
 @Override
 protected long _timestamp(Date value) {
 return value.getTime();
 }

 @Override
 protected void _serializeAsString(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override
 public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
 throw new UnsupportedOperationException();
 }
 };
 }

 @Test
 public void testCreateContextual() throws JsonProcessingException {
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 format.setLocale(Locale.US);
 format.setTimeZone(TimeZone.getTimeZone("UTC"));
 format.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

 Object typeHint = mapper.getTypeFactory().constructType(Date.class);
 serializer.acceptJsonFormatVisitor(mock(JsonFormatVisitorWrapper.class), typeHint);

 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = serializers;

 DateTimeSerializerBase<Date> serializerContextual = serializer.createContextual(serializers, null);
 serializerContextual.serialize(new Date(), gen, provider);

 JsonParser parser = mapper.getFactory().createParser("2023-03-28T12:00:00.000Z");
 Date date = mapper.readValue(parser, Date.class);
 assertEquals("Tue Mar 28 12:00:00 UTC 2023", serializerContextual.format(date, serializers.getLocale(), null));
 }

 @Test
 public void testIsEmpty() {
 assertNull(serializer.serialize(null, null, serializers));
 }

 @Test
 public void testSerialize() throws IOException {
 Date date = new Date();
 SerializerProvider provider = serializers;
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, null);
 serializer.serialize(date, gen, provider);
 assertEquals(date.getTime(), serializer.getSchema(serializers, null).getNumberValue().longValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithFormat() {
 serializer.withFormat(true, null);
 }
}