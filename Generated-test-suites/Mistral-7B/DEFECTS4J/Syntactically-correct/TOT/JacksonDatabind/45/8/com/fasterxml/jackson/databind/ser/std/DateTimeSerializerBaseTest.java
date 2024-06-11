package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Date> dateTimeSerializer;
 private JsonSerializerProvider jsonSerializerProvider;

 @Before
 public void setup() {
 jsonSerializerProvider = Mockito.mock(JsonSerializerProvider.class);
 dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
 @Override
 protected long _timestamp(Date value) {
 return value.getTime();
 }
 };
 }

 @Test
 public void testSerializationWithCustomFormat() throws JsonProcessingException, JsonGenerationException {
 // Given
 Date date = new Date();
 SimpleDateFormat customFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss z", Locale.ENGLISH);
 TimeZone customTimeZone = TimeZone.getTimeZone("UTC");
 customFormat.setTimeZone(customTimeZone);

 // When
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 dateTimeSerializer = dateTimeSerializer.withFormat(false, customFormat);
 dateTimeSerializer.serialize(date, jsonGenerator, jsonSerializerProvider);

 // Then
 String expectedOutput = "25/12/2021 14:15:26 UTC";
 assertEquals(expectedOutput, jsonGenerator.getOutputContent());
 }

 @Test
 public void testSerializationWithDefaultFormat() throws JsonProcessingException, JsonGenerationException {
 // Given
 Date date = new Date();

 // When
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 dateTimeSerializer.serialize(date, jsonGenerator, jsonSerializerProvider);

 // Then
 String expectedOutput = "2021-12-25T14:15:26.000Z";
 assertEquals(expectedOutput, jsonGenerator.getOutputContent());
 }

 @Test
 public void testSerializationWithTimestampFormat() throws JsonProcessingException, JsonGenerationException {
 // Given
 Date date = new Date();

 // When
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 dateTimeSerializer = dateTimeSerializer.withFormat(true, null);
 dateTimeSerializer.serialize(date, jsonGenerator, jsonSerializerProvider);

 // Then
 assertEquals(date.getTime(), jsonGenerator.getOutputContentAsLong());
 }

 @Test
 public void testSerializationWithJsonFormatAnnotation() throws JsonProcessingException, JsonGenerationException {
 // Given
 @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "UTC")
 Date date = new Date();

 // When
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 dateTimeSerializer = dateTimeSerializer.withFormat(false, null);
 dateTimeSerializer.serialize(date, jsonGenerator, jsonSerializerProvider);

 // Then
 String expectedOutput = "2021-12-25T14:15:26.000Z";
 assertEquals(expectedOutput, jsonGenerator.getOutputContent());
 }

 @Test
 public void testSerializationWithNullValue() throws JsonProcessingException, JsonGenerationException {
 // Given

 // When
 JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
 dateTimeSerializer.serialize(null, jsonGenerator, jsonSerializerProvider);

 // Then
 assertNull(jsonGenerator.getOutputContent());
 }

 @Test
 public void testSerializationWithInvalidFormat() throws JsonProcessingException, JsonGenerationException {
 // Given
 JsonFormat.Value jsonFormat = Mockito.mock(JsonFormat.Value.class);
 // When  [...]
 // Then
 assertFalse(dateTimeSerializer.createContextual(jsonSerializerProvider, null).isEnabled());
 }
}