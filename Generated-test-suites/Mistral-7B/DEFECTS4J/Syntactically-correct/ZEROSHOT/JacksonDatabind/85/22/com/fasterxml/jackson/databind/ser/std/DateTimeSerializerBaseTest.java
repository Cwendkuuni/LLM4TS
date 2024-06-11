package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testDateTimeSerializerBase() throws IOException {
 DateTimeSerializerBase<Long> longSerializer = new StdDateSerializer(Long.TYPE, false, null);
 DateTimeSerializerBase<java.util.Date> dateSerializer = new StdDateSerializer(Date.class, false, null);

 // Test with default settings
 Long defaultLongValue = 1623355200000L; // Timestamp for Wed, 27 Jan 2022 00:00:00 GMT
 Date defaultDateValue = new Date(defaultLongValue);

 JsonNode longNode = MAPPER.writeValueAsTree(defaultLongValue);
 JsonNode dateNode = MAPPER.writeValueAsTree(defaultDateValue);

 JsonSerializer<Long> serializedLong = longSerializer.createContextual(MAPPER.getSerializerProvider(), null);
 JsonSerializer<Date> serializedDate = dateSerializer.createContextual(MAPPER.getSerializerProvider(), null);

 JsonNode serializedLongValue = MAPPER.treeToValues(MAPPER.writeValueAsTree(serializedLong, defaultLongValue), true, true)[0];
 JsonNode serializedDateValue = MAPPER.treeToValues(MAPPER.writeValueAsTree(serializedDate, defaultDateValue), true, true)[0];

 assertEquals("1.401297E12", serializedLongValue.textValue());
 assertEquals("2022-01-27T00:00:00.000Z", serializedDateValue.textValue());

 // Test with timestamp serialization
 DateTimeSerializerBase<Long> timestampLongSerializer = longSerializer.withFormat(Boolean.TRUE, null);
 JsonNode serializedTimestampLongValue = MAPPER.treeToValues(MAPPER.writeValueAsTree(timestampLongSerializer, defaultLongValue), true, true)[0];
 assertEquals("1.401297E12", serializedTimestampLongValue.textValue());

 // Test with custom format serialization
 DateTimeFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
 TimeZone customTimeZone = TimeZone.getTimeZone("UTC");
 DateTimeSerializerBase<Long> customLongSerializer = longSerializer.withFormat(Boolean.FALSE, customFormat);
 customFormat.setTimeZone(customTimeZone);
 JsonNode serializedCustomLongValue = MAPPER.treeToValues(MAPPER.writeValueAsTree(customLongSerializer, defaultLongValue), true, true)[0];
 assertEquals("27-01-2022 00:00:00", serializedCustomLongValue.textValue());

 // Test with JsonFormat annotation
 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "UTC")
 Long annotatedLongValue = defaultLongValue;
 JsonNode annotatedLongNode = MAPPER.writeValueAsTree(annotatedLongValue);
 JsonSerializer<Long> annotatedLongSerializer = MAPPER.findValueSerializer(Long.class, MAPPER.getSerializerProvider());
 JsonNode serializedAnnotatedLongValue = MAPPER.treeToValues(MAPPER.writeValueAsTree(annotatedLongSerializer, annotatedLongValue), true, true)[0];
 assertEquals("27-01-2022 00:00:00", serializedAnnotatedLongValue.textValue());

 // Test serialization when provider is null
 Long nullProviderLongValue = defaultLongValue;
 JsonSerializer<Long> nullProviderLongSerializer = longSerializer.withFormat(Boolean.FALSE, null);
 try {
 MAPPER.writeValueAsTree(nullProviderLongSerializer, nullProviderLongValue);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}