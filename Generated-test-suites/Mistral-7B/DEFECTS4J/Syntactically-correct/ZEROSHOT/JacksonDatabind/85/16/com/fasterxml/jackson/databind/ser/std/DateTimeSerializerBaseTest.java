package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase.DateTimeSerializerBase_Criteria;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

 private static final long TEST_TIMESTAMP = 1234567890L;
 private static final DateFormat TEST_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
 private static final Locale TEST_LOCALE = Locale.US;
 private static final TimeZone TEST_TIME_ZONE = TimeZone.getTimeZone("UTC");

 @SuppressWarnings("unchecked")
 @org.junit.Before
 public void setUp() {
 DateTimeSerializerBase_Criteria criteria = mock(DateTimeSerializerBase_Criteria.class);
 when(criteria.handledType()).thenReturn(String.class);
 when(criteria.findFormatOverrides((SerializerProvider) mock(SerializerProvider.class), (BeanProperty) null, criteria.handledType())).thenReturn(null);

 ObjectMapper mapper = new ObjectMapper();
 mapper.setSerializationInclusion(Include.NON_NULL);
 mapper.setDateFormat(TEST_DATE_FORMAT);
 mapper.setLocale(TEST_LOCALE);
 mapper.setTimeZone(TEST_TIME_ZONE);
 SerializerProvider serializers = mapper.getSerializerProvider();

 _serializer = new DateTimeSerializerBase<String>(String.class, false, null) {
 @Override
 protected long _timestamp(String value) {
 throw new UnsupportedOperationException();
 }

 @Override
 protected void _acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, Type typeHint, boolean asNumber) throws JsonMappingException {
 super._acceptJsonFormatVisitor(visitor, typeHint, asNumber);
 if (asNumber) {
 visitor.visitIntFormat(JsonParser.NumberType.LONG, JsonValueFormat.UTC_MILLISEC);
 } else {
 visitor.visitStringFormat(JsonValueFormat.DATE_TIME);
 }
 }

 @Override
 public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 super.serialize(value, gen, serializers);
 gen.writeNumber(TEST_TIMESTAMP);
 }
 };
 }

 @org.junit.Test
 public void testEmpty() {
 assertEquals(null, _serializer.serialize(null, new JsonGenerator(new ByteArrayOutputStream()), serializers));
 }

 @org.junit.Test
 public void testSerializeToTimestamp() throws IOException {
 String testValue = "2021-05-03 12:34:56.789Z";
 assertEquals(TEST_TIMESTAMP, _serializer.serialize(testValue, new JsonGenerator(new ByteArrayOutputStream()), serializers).getNumberValues().nextDoubleValue(), 0.0);
 }

 @org.junit.Test
 public void testSerializeToString() throws IOException {
 String testValue = "2021-05-03 12:34:56.789Z";
 String expected = TEST_DATE_FORMAT.format(TEST_DATE_FORMAT.parse(testValue));
 assertEquals(expected, _serializer.serialize(testValue, new JsonGenerator(new ByteArrayOutputStream()), serializers).getValueAsString());
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testSerializeInvalid() {
 _serializer.serialize("invalid", new JsonGenerator(new ByteArrayOutputStream()), serializers);
 }

 @org.junit.Test
 public void testCreateContextual() throws JsonMappingException {
 JsonSerializer<String> serializer = _serializer.createContextual(serializers, null);
 assertEquals(_serializer, serializer);
 }

 @org.junit.Test
 public void testCreateContextualWithFormat() throws JsonMappingException {
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 format.setPattern("yyyy-MM-dd HH:mm:ss.SSS z");
 format.setLocale(TEST_LOCALE);
 format.setTimeZone(TEST_TIME_ZONE);
 JsonSerializer<String> serializer = _serializer.createContextual(serializers, null, format);
 assertEquals(_serializer, serializer);
 EquivalenceUtil.assertEquals(TEST_DATE_FORMAT, serializer.getValueAsFormatter());
 }
}