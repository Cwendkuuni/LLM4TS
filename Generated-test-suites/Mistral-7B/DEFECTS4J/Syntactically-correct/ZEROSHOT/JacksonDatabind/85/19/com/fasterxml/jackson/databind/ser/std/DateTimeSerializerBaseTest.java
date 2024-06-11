package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializer.Typing;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer.ValueType;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

 private static final Locale LOCALE_US = Locale.US;
 private static final TimeZone TIME_ZONE_UTC = TimeZone.getTimeZone("UTC");

 @SuppressWarnings("unchecked")
 private static final DateTimeSerializerBase<Instant> DATE_TIME_SERIALIZER_UTc = new UtcDateTimeSerializerBase(Instant.class, false, null);
 private static final DateTimeSerializerBase<Instant> DATE_TIME_SERIALIZER_STRING_FORMAT = new UtcDateTimeSerializerBase(Instant.class, false, new StdDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", LOCALE_US, TIME_ZONE_UTC));

 @SuppressWarnings("unchecked")
 private static final DateTimeSerializerBase<Instant> TIMESTAMP_SERIALIZER = new UtcTimestampSerializerBase(Instant.class, true, null);

 @SuppressWarnings("unchecked")
 private static final DateTimeSerializerBase<Instant> TIMESTAMP_SERIALIZER_UTC_MILLISEC = new UtcTimestampSerializerBase(Instant.class, true, JsonValueFormat.UTC_MILLISEC);

 @SuppressWarnings("unchecked")
 private static final DateTimeSerializerBase<Instant> TIMESTAMP_SERIALIZER_UTC_ISO8601 = new UtcTimestampSerializerBase(Instant.class, true, StdDateFormat.DATE_FORMAT_STR_ISO8601);

 @SuppressWarnings("unchecked")
 private static final ObjectMapper MAPPER = new ObjectMapper();

 @SuppressWarnings("unchecked")
 private static final JsonSerializer<Instant> DATE_TIME_SERIALIZER = (JsonSerializer<Instant>) MAPPER.findValueSerializer(Instant.class);

 @SuppressWarnings("unchecked")
 private static final JsonSerializer<Instant> TIMESTAMP_SERIALIZER = (JsonSerializer<Instant>) MAPPER.findValueSerializer(Instant.class);

 @org.junit.Before
 public void setUp() {
 MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 }

 @org.junit.Test
 public void test_createContextual() throws JsonProcessingException {
 Instant instant = Instant.now();
 SerializerProvider serializers = mock(SerializerProvider.class);
 BeanProperty property = mock(BeanProperty.class);

 when(DATE_TIME_SERIALIZER.createContextual(serializers, property)).thenReturn(DATE_TIME_SERIALIZER_UTc);
 when(DATE_TIME_SERIALIZER_UTc.createContextual(serializers, property)).thenReturn(DATE_TIME_SERIALIZER_STRING_FORMAT);
 when(DATE_TIME_SERIALIZER_STRING_FORMAT.createContextual(serializers, property)).thenReturn(TIMESTAMP_SERIALIZER);
 when(TIMESTAMP_SERIALIZER.createContextual(serializers, property)).thenReturn(TIMESTAMP_SERIALIZER_UTC_MILLISEC);
 when(TIMESTAMP_SERIALIZER_UTC_MILLISEC.createContextual(serializers, property)).thenReturn(TIMESTAMP_SERIALIZER_UTC_ISO8601);

 JsonFormat.Value format = JsonFormat.value().withShape(JsonFormat.Shape.STRING).withLocale(LOCALE_US).withTimeZone(TIME_ZONE_UTC);
 JsonNode schema = DATE_TIME_SERIALIZER.acceptJsonFormatVisitor(JsonNodeFactory.instance, null, null);
 assertEquals("string", schema.getClass().getSimpleName());
 assertEquals(ValueType.STRING, schema.asTextType());

 instant = Instant.now();
 String json = MAPPER.writeValueAsString(instant);
 Instant deserialized = MAPPER.readValue(json, Instant.class);
 assertEquals(instant, deserialized);
 }

 @org.junit.Test
 public void test_asTimestamp() {
 assertEquals(false, DATE_TIME_SERIALIZER_UTc.asTimestamp(null));
 assertEquals(false, DATE_TIME_SERIALIZER_STRING_FORMAT.asTimestamp(null));
 assertEquals(true, TIMESTAMP_SERIALIZER.asTimestamp(null));
 }

 @org.junit.Test
 public void test_serialize() throws JsonProcessingException {
 Instant instant = Instant.now();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider serializers = mock(SerializerProvider.class);
 when(TIMESTAMP_SERIALIZER_UTC_ISO8601.serialize(instant, gen, serializers)).thenReturn(null);

 DateTimeSerializerBase<Instant> serializer = TIMESTAMP_SERIALIZER_UTC_ISO8601;
 serializer.serialize(instant, gen, serializers);
 assertNotNull(gen.getOutputBuffer());
 }

 @org.junit.Test
 public void test_acceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormat.Value format = JsonFormat.value().withShape(JsonFormat.Shape.STRING).withLocale(LOCALE_US).withTimeZone(TIME_ZONE_UTC);
 JsonNode node = DATE_TIME_SERIALIZER.acceptJsonFormatVisitor(JsonNodeFactory.instance, null, null);
 assertEquals("string", node.getClass().getSimpleName());
 assertEquals(ValueType.STRING, node.asTextType());

 format = JsonFormat.value().withShape(JsonFormat.Shape.NUMBER);
 node = DATE_TIME_SERIALIZER.acceptJsonFormatVisitor(JsonNodeFactory.instance, null, null);
 assertEquals("number", node.getClass().getSimpleName());
 assertEquals(ValueType.NUMBER, node.asNumberType());
 }

 @org.junit.Test
 public void test_serializeEmpty() throws JsonProcessingException {
 SerializerProvider serializers = mock(SerializerProvider.class);
 JsonGenerator gen = mock(JsonGenerator.class);
 when(TIMESTAMP_SERIALIZER_UTC_ISO8601.serialize(null, gen, serializers)).thenReturn("null");

 DateTimeSerializerBase<Instant> serializer = TIMESTAMP_SERIALIZER_UTC_ISO8601;
 serializer.serialize(null, gen, serializers);
 assertNotNull(gen.getOutputBuffer());
 String json = gen.getOutputBuffer().toString();
 assertEquals("null", json);
 }
}