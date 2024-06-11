package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.std.*;

public class StdKeySerializersTest {

 private static ObjectMapper objectMapper;

 @BeforeClass
 public static void setUp() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testDefaultKeySerializer() throws IOException {
 JsonSerializer<Object> keySerializer = StdKeySerializers.getDefaultKeySerializer(objectMapper.getSerializationConfig(), null, true);
 Object testObject = new Object();
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializationConfig());
 keySerializer.serialize(testObject, jsonGenerator, objectMapper.getSerializationConfig().getDefaultSerializerProvider());
 jsonGenerator.flush();
 String result = new String(jsonGenerator.getOutputBuffer().toString());
 assertEquals("\"\",null", result);
 }

 @Test
 public void testStringKeySerializer() throws IOException {
 JsonSerializer<Object> keySerializer = StdKeySerializers.getDefaultStringSerializer(objectMapper.getSerializationConfig());
 Object testObject = "Test String";
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializationConfig());
 keySerializer.serialize(testObject, jsonGenerator, objectMapper.getSerializationConfig().getDefaultSerializerProvider());
 jsonGenerator.flush();
 String result = new String(jsonGenerator.getOutputBuffer().toString());
 assertEquals("\"Test String\"", result);
 }

 @Test
 public void testGetStdKeySerializerForDate() throws IOException {
 JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), Date.class, true);
 Object testObject = new Date();
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializationConfig());
 keySerializer.serialize(testObject, jsonGenerator, objectMapper.getSerializationConfig().getDefaultSerializerProvider());
 jsonGenerator.flush();
 String result = new String(jsonGenerator.getOutputBuffer().toString());
 assertEquals("\"2023-03-16T14:35:53.000Z\"", result);
 }

 @Test
 public void testGetStdKeySerializerForCalendar() throws IOException {
 Calendar calendar = Calendar.getInstance();
 JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), Calendar.class, true);
 Object testObject = calendar;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializationConfig());
 keySerializer.serialize(testObject, jsonGenerator, objectMapper.getSerializationConfig().getDefaultSerializerProvider());
 jsonGenerator.flush();
 String result = new String(jsonGenerator.getOutputBuffer().toString());
 assertEquals("\"2023-03-16T14:35:53.000Z\"", result);
 }

 @Test
 public void testGetStdKeySerializerForOtherClasses() throws IOException {
 JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), String[].class, true);
 Object testObject = new String[]{"Test1", "Test2"};
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializationConfig());
 keySerializer.serialize(testObject, jsonGenerator, objectMapper.getSerializationConfig().getDefaultSerializerProvider());
 jsonGenerator.flush();
 String result = new String(jsonGenerator.getOutputBuffer().toString());
 assertEquals("\"[Test1, Test2]\"" + System.lineSeparator() + "    ,", result);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetStdKeySerializerForNull() throws IOException {
 JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), null, true);
 Object testObject = null;
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializationConfig());
 keySerializer.serialize(testObject, jsonGenerator, objectMapper.getSerializationConfig().getDefaultSerializerProvider());
 jsonGenerator.flush();
 fail("Should throw JsonMappingException when trying to serialize null key");
 }

 @Test
 public void testGetFallbackKeySerializerForClass() throws IOException {
 JsonSerializer<Object> keySerializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), String[].class);
 Object testObject = new String[]{"Test1", "Test2"};
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializationConfig());
 keySerializer.serialize(testObject, jsonGenerator, objectMapper.getSerializationConfig().getDefaultSerializerProvider());
 jsonGenerator.flush();
 String result = new String(jsonGenerator.getOutputBuffer().toString());
 assertEquals("\"[Test1, Test2]\"" + System.lineSeparator() + "    ,", result);
 }

 @Test
 public void testGetFallbackKeySerializerForEnum() throws IOException {
 Enum testEnum = Enum.valueOf(TestEnum.class, "TestEnumValue");
 JsonSerializer<Object> keySerializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), TestEnum.class);
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializationConfig());
 keySerializer.serialize(testEnum, jsonGenerator, objectMapper.getSerializationConfig().getDefaultSerializerProvider());
 jsonGenerator.flush();
 String result = new String(jsonGenerator.getOutputBuffer().toString());
 assertEquals("\"TestEnumValue\"", result);
 }

 @Test
 public void testGetFallbackKeySerializerForNullEnum() throws IOException {
 Enum testEnum = null;
 JsonSerializer<Object> keySerializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), TestEnum.class);
 JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getSerializationConfig());
 keySerializer.serialize(testEnum, jsonGenerator, objectMapper.getSerializationConfig().getDefaultSerializerProvider());
 jsonGenerator.flush();
 fail("Should not serialize null Enum key");
 }
}

enum TestEnum {
 TestEnumValue
}