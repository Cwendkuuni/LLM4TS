package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonSerializer.Typing;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.*;

public class StdKeySerializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testDefaultKeySerializer() throws JsonGenerationException, IOException {
 JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
 Object testObject = new Object();
 JsonGenerator generator = objectMapper.getFactory().createGenerator();
 serializer.serialize(testObject, generator, objectMapper.getSerializationConfig());
 generator.flush();
 assertEquals("[]", generator.getOutputContent().toString());
 }

 @Test
 public void testStringKeySerializer() throws JsonGenerationException, IOException {
 String testString = "testString";
 JsonSerializer<Object> serializer = StdKeySerializers.DEFAULT_STRING_SERIALIZER;
 Object testObject = testString;
 JsonGenerator generator = objectMapper.getFactory().createGenerator();
 serializer.serialize(testObject, generator, objectMapper.getSerializationConfig());
 generator.flush();
 assertEquals("\"testString\"", generator.getOutputContent().toString());
 }

 @Test
 public void testDateKeySerializer() throws JsonGenerationException, IOException {
 Date testDate = new Date();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 Date.class, true);
 Object testObject = testDate;
 JsonGenerator generator = objectMapper.getFactory().createGenerator();
 serializer.serialize(testObject, generator, objectMapper.getSerializationConfig());
 generator.flush();
 assertEquals("\"2023-03-29T15:49:37.149Z\"", generator.getOutputContent().toString());
 }

 @Test
 public void testCalendarKeySerializer() throws JsonGenerationException, IOException {
 Calendar testCalendar = Calendar.getInstance();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 Calendar.class, true);
 Object testObject = testCalendar;
 JsonGenerator generator = objectMapper.getFactory().createGenerator();
 serializer.serialize(testObject, generator, objectMapper.getSerializationConfig());
 generator.flush();
 assertEquals("\"2023-03-29T15:49:37.149Z\"", generator.getOutputContent().toString());
 }

 @Test
 public void testDynamicKeySerializer() throws JsonGenerationException, IOException {
 Object testObject = new Object() {
 @JsonProperty
 int value;
 };
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(),
 Object.class);
 JsonGenerator generator = objectMapper.getFactory().createGenerator();
 serializer.serialize(testObject, generator, objectMapper.getSerializationConfig());
 generator.flush();
 assertEquals("[]", generator.getOutputContent().toString());
 }

 @Test
 public void testEnumKeySerializer() throws JsonGenerationException, IOException {
 Enum testEnum = Enum.valueOf(String.class, "TEST_ENUM");
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(),
 Enum.class);
 Object testObject = testEnum;
 JsonGenerator generator = objectMapper.getFactory().createGenerator();
 serializer.serialize(testObject, generator, objectMapper.getSerializationConfig());
 generator.flush();
 assertEquals("\"TEST_ENUM\"", generator.getOutputContent().toString());
 }

 @Test
 public void testClassKeySerializer() throws JsonGenerationException, IOException {
 Class<?> testClass = String[].class;
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(),
 testClass, true);
 Object testObject = testClass;
 JsonGenerator generator = objectMapper.getFactory().createGenerator();
 serializer.serialize(testObject, generator, objectMapper.getSerializationConfig());
 generator.flush();
 assertEquals("\"[Ljava.lang.String;\"", generator.getOutputContent().toString());
 }
}