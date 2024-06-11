package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdDateSerializers;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
 }

 @Test
 public void testSerializationWithDefaultFormat() throws JsonGenerationException, IOException {
 // Given
 DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();

 // When
 String json = mapper.writerWithRootValueSerializer(dateSerializer.getClass())
 .writeValueAsString(new Date(System.currentTimeMillis()));

 // Then
 JsonParser jsonParser = mapper.getFactory().createParser(json);
 assertEquals(JsonNodeType.VALUE_STRING, jsonParser.nextValue().getNodeType());
 assertEquals("2023-03-28T15:31:31.234Z", jsonParser.getValueAsString());
 }

 @Test
 public void testSerializationWithCustomFormat() throws JsonGenerationException, IOException {
 // Given
 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss z")
 static class CustomDate {
 }

 AnnotatedClass annotatedClass = mapper.getAnnotationIntrospector().findAnnotationHandlers(CustomDate.class)[0];
 BeanProperty property = annotatedClass.findProperty("class");
 DateTimeSerializerBase<CustomDate> customDateSerializer = property.getAnnotation(JsonSerialize.class).withSerializers(new SerializerProvider())
 .findValueSerializer(mapper.getSerializerProvider().getFactory(), CustomDate.class);

 // When
 String json = mapper.writerWithRootValueSerializer(customDateSerializer.getClass())
 .writeValueAsString(new CustomDate());

 // Then
 JsonParser jsonParser = mapper.getFactory().createParser(json);
 assertEquals(JsonNodeType.VALUE_STRING, jsonParser.nextValue().getNodeType());
 assertEquals("28-03-2023 15:31:31 PDT", jsonParser.getValueAsString());
 }

 @Test
 public void testSerializationWithTimestampFormat() throws JsonGenerationException, IOException {
 // Given
 DateTimeSerializerBase<Long> timestampSerializer = new StdSerializers.StdLongSerializer();

 // When
 String json = mapper.writerWithRootValueSerializer(timestampSerializer.getClass())
 .writeValueAsString(System.currentTimeMillis());

 // Then
 JsonParser jsonParser = mapper.getFactory().createParser(json);
 assertEquals(JsonNodeType.VALUE_NUMBER, jsonParser.nextValue().getNodeType());
 assertEquals(1679832591323L, jsonParser.getValueAsLong());
 }

 @Test
 public void testDeserializationWithDefaultFormat() throws JsonGenerationException, IOException {
 // Given
 String json = "\"2023-03-28T15:31:31.234Z\"";

 // When
 Date date = mapper.readValue(json, Date.class);

 // Then
 assertEquals(System.currentTimeMillis(), date.getTime());
 }

 @Test
 public void testDeserializationWithCustomFormat() throws JsonGenerationException, IOException {
 // Given
 String json = "\"28-03-2023 15:31:31 PDT\"";

 // When
 @JsonDeserialize(using = StdDateDeserializers.DateDeserializer.class)
 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss z")
 static class CustomDateDeserializer {
 }
 CustomDateDeserializer customDateDeserializer = new CustomDateDeserializer();
 Date date = mapper.readerForUpdating(Date.class)
 .readValue(json, customDateDeserializer);

 // Then
 assertEquals("Tue Mar 28 15:31:31 PDT 2023", date.toString());
 }

 @Test
 public void testDeserializationWithTimestampFormat() throws JsonGenerationException, IOException {
 // Given
 String json = "1679832591323";

 // When
 Date date = mapper.readValue(json, Date.class);

 // Then
 assertEquals(System.currentTimeMillis(), date.getTime());
 }

 @Test(expected = IOException.class)
 public void testDeserializationWithInvalidFormat() throws JsonGenerationException, IOException {
 // Given
 String json = "{ \"invalid\": \"format\" }";

 // When & Then
 mapper.readerForUpdating(Date.class).readValue(json);
 }
}