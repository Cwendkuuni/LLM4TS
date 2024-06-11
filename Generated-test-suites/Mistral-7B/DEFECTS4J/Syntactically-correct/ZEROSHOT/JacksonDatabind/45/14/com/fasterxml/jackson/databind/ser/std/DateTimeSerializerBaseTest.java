package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonParser.NumberType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonValueFormat;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class DateTimeSerializerBaseTest {

 private static final long TEST_TIMESTAMP = 1234567890L;
 private static final DateFormat TEST_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.ENGLISH);
 private static final TimeZone TEST_TIMEZONE = TimeZone.getTimeZone("UTC");

 @Before
 public void setup() {
 TEST_FORMAT.setTimeZone(TEST_TIMEZONE);
 }

 @Test
 public void testSerializationWithDefaults() throws IOException, JsonMappingException {
 DateTimeSerializerBase<Long> serializer = new StdDateSerializer(Long.TYPE, false, null);
 Long value = 1234567890L;

 JsonGenerator generator = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 when(provider.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);
 when(generator.writeNumber(TEST_TIMESTAMP)).thenReturn(null);

 serializer.serialize(value, generator, provider);
 assertEquals("1234567890", TEST_FORMAT.format(TEST_TIMESTAMP));
 }

 @Test
 public void testSerializationWithDefaultFormat() throws IOException, JsonMappingException {
 DateTimeSerializerBase<Long> serializer = new StdDateSerializer(Long.TYPE, false, TEST_FORMAT);
 Long value = 1234567890L;

 JsonGenerator generator = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 when(generator.writeString(TEST_FORMAT.format(TEST_TIMESTAMP))).thenReturn(null);

 serializer.serialize(value, generator, provider);
 assertEquals(TEST_FORMAT.format(TEST_TIMESTAMP), TEST_FORMAT.format(TEST_TIMESTAMP));
 }

 @Test
 public void testSerializationWithCustomFormat() throws IOException, JsonMappingException {
 DateTimeSerializerBase<Long> serializer = new StdDateSerializer(Long.TYPE, false, TEST_FORMAT);
 Long value = 1234567890L;

 JsonGenerator generator = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 when(generator.writeString(TEST_FORMAT.format(TEST_TIMESTAMP))).thenReturn(null);
 when(provider.getLocale()).thenReturn(Locale.ENGLISH);
 when(provider.getTimeZone()).thenReturn(TEST_TIMEZONE);

 serializer.serialize(value, generator, provider);
 assertEquals(TEST_FORMAT.format(TEST_TIMESTAMP), TEST_FORMAT.format(TEST_TIMESTAMP));
 }

 @Test
 public void testSerializationWithTimestamp() throws IOException, JsonMappingException {
 DateTimeSerializerBase<Long> serializer = new StdDateSerializer(Long.TYPE, true, null);
 Long value = 1234567890L;

 JsonGenerator generator = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 when(provider.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);
 when(generator.writeNumber(TEST_TIMESTAMP)).thenReturn(null);

 serializer.serialize(value, generator, provider);
 assertEquals(TEST_TIMESTAMP, TEST_TIMESTAMP);
 }

 @Test
 public void testSerializationWithCustomFormatAndTimestamp() throws IOException, JsonMappingException {
 DateTimeSerializerBase<Long> serializer = new StdDateSerializer(Long.TYPE, true, TEST_FORMAT);
 Long value = 1234567890L;

 JsonGenerator generator = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 when(provider.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);
 when(generator.writeNumber(TEST_TIMESTAMP)).thenReturn(null);

 serializer.serialize(value, generator, provider);
 assertEquals(TEST_FORMAT.format(TEST_TIMESTAMP), TEST_FORMAT.format(TEST_TIMESTAMP));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializationWithNullProvider() throws IOException, JsonMappingException {
 DateTimeSerializerBase<Long> serializer = new StdDateSerializer(Long.TYPE, false, null);
 Long value = 1234567890L;

 JsonGenerator generator = mock(JsonGenerator.class);
 SerializerProvider provider = null;
 serializer.serialize(value, generator, provider);
 }

 @Test
 public void testSerializationContextual() throws IOException, JsonMappingException {
 DateTimeSerializerBase<Long> serializer = new StdDateSerializer(Long.TYPE, false, null);
 Long value = 1234567890L;

 JsonParser parser = mock(JsonParser.class);
 ObjectCodec codec = mock(ObjectCodec.class);
 AnnotationIntrospector introspector = mock(AnnotationIntrospector.class);
 JsonDeserializer<Long> deserializer = mock(JsonDeserializer.class);
 JsonParser.NumberType numberType = JsonParser.NumberType.LONG;
 JsonValueFormat jsonValueFormat = JsonValueFormat.UTC_MILLISEC;
 BeanProperty property = mock(BeanProperty.class);
 JavaType javaType = TypeFactory.defaultInstance().constructType(Long.class);

 when(parser.getCodec()).thenReturn(codec);
 when(codec.findSerializer(Long.class)).thenReturn(serializer);
 when(parser.getProvider().isEnabled(SerializationFeature.FIND_PROPERTIES_ON_NULL_OBJECTS)).thenReturn(true);
 when(introspector.findFormat((Annotated)property)).thenReturn(new JsonFormat.Value(JsonFormat.Shape.STRING, TEST_FORMAT, null, null, null));
 when(codec.findDeserializer(Long.class)).thenReturn(deserializer);
 when(deserializer.deserialize(parser, javaType)).thenReturn(value);

 serializer.createContextual(new SerializerProvider(), property);
 assertEquals(value, value);
 }

 @Test
 public void testSerializationContextualWithTimestamp() throws IOException, JsonMappingException {
 DateTimeSerializerBase<Long> serializer = new StdDateSerializer(Long.TYPE, true, null);
 Long value = 1234567890L;

 JsonParser parser = mock(JsonParser.class);
 ObjectCodec codec = mock(ObjectCodec.class);
 AnnotationIntrospector introspector = mock(AnnotationIntrospector.class);
 JsonDeserializer<Long> deserializer = mock(JsonDeserializer.class);
 JsonParser.NumberType numberType = JsonParser.NumberType.LONG;
 JsonValueFormat jsonValueFormat = JsonValueFormat.UTC_MILLISEC;
 BeanProperty property = mock(BeanProperty.class);
 JavaType javaType = TypeFactory.defaultInstance().constructType(Long.class);

 when(parser.getCodec()).thenReturn(codec);
 when(codec.findSerializer(Long.class)).thenReturn(serializer);
 when(parser.getProvider().isEnabled(SerializationFeature.FIND_PROPERTIES_ON_NULL_OBJECTS)).thenReturn(true);
 when(introspector.findFormat((Annotated)property)).thenReturn(new JsonFormat.Value(JsonFormat.Shape.NUMBER, null, null, null, null));
 when(codec.findDeserializer(Long.class)).thenReturn(deserializer);
 when(deserializer.deserialize(parser, javaType)).thenReturn(value);

 serializer.createContextual(new SerializerProvider(), property);
 assertEquals(value, value);
 }

 @Test
 public void testSerializationContextualWithFormat() throws IOException, JsonMappingException {
 DateTimeSerializerBase<Long> serializer = new StdDateSerializer(Long.TYPE, false, null);
 Long value = 1234567890L;

 JsonParser parser = mock(JsonParser.class);
 ObjectCodec codec = mock(ObjectCodec.class);
 AnnotationIntrospector introspector = mock(AnnotationIntrospector.class);
 JsonDeserializer<Long> deserializer = mock(JsonDeserializer.class);
 JsonParser.NumberType numberType = JsonParser.NumberType.LONG;
 JsonValueFormat jsonValueFormat = JsonValueFormat.DATE_TIME;
 SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.ENGLISH);
 BeanProperty property = mock(BeanProperty.class);
 JavaType javaType = TypeFactory.defaultInstance().constructType(Long.class);

 when(parser.getCodec()).thenReturn(codec);
 when(codec.findSerializer(Long.class)).thenReturn(serializer);
 when(parser.getProvider().isEnabled(SerializationFeature.FIND_PROPERTIES_ON_NULL_OBJECTS)).thenReturn(true);
 when(introspector.findFormat((Annotated)property)).thenReturn(new JsonFormat.Value(JsonFormat.Shape.STRING, customFormat, null, null, null));
 when(codec.findDeserializer(Long.class)).thenReturn(deserializer);
 when(deserializer.deserialize(parser, javaType)).thenReturn(value);
 when(customFormat.parse("2022-01-01 12:00:00 UTC")).thenReturn(TEST_TIMESTAMP);
 when(parser.getValueAsLong()).thenReturn(TEST_TIMESTAMP);

 serializer.createContextual(new SerializerProvider(), property);
 assertEquals(value, value);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 DateTimeSerializerBase<Long> serializer = new StdDateSerializer(Long.TYPE, false, null);

 SerializerProvider provider = mock(SerializerProvider.class);
 JavaType javaType = TypeFactory.defaultInstance().constructType(Long.class);

 assertEquals(JsonNode.ValueType.NUMBER, serializer.getSchema(provider, javaType).getType());
 }

 @Test
 public void testGetSchemaWithFormat() throws JsonMappingException {
 DateTimeSerializerBase<Long> serializer = new StdDateSerializer(Long.TYPE, false, TEST_FORMAT);

 SerializerProvider provider = mock(SerializerProvider.class);
 JavaType javaType = TypeFactory.defaultInstance().constructType(Long.class);

 assertEquals(JsonNode.ValueType.STRING, serializer.getSchema(provider, javaType).getType());
 }
}