package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;

public class StdKeySerializersTest {

 private ObjectMapper mapper;

 @BeforeClass
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testGetStdKeySerializer() throws JsonMappingException, JsonProcessingException {
 SerializationConfig config = mapper.getSerializationConfig();

 // Test for null and Object.class
 Class<?> rawKeyTypeNull = null;
 JsonSerializer<Object> serializerNull = StdKeySerializers.getStdKeySerializer(config, rawKeyTypeNull, true);
 assertNotNull(serializerNull);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializerNull.getClass());

 rawKeyTypeNull = Object.class;
 serializerNull = StdKeySerializers.getStdKeySerializer(config, rawKeyTypeNull, true);
 assertNotNull(serializerNull);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializerNull.getClass());

 // Test for String
 Class<?> rawKeyTypeString = String.class;
 JsonSerializer<Object> serializerString = StdKeySerializers.getStdKeySerializer(config, rawKeyTypeString, true);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializerString.getClass());

 // Test for primitive and Number
 Class<?> rawKeyTypeInt = int.class;
 JsonSerializer<Object> serializerInt = StdKeySerializers.getStdKeySerializer(config, rawKeyTypeInt, true);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializerInt.getClass());

 Class<?> rawKeyTypeDouble = double.class;
 JsonSerializer<Object> serializerDouble = StdKeySerializers.getStdKeySerializer(config, rawKeyTypeDouble, true);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializerDouble.getClass());

 // Test for Class
 Class<?> rawKeyTypeClass = Class.class;
 JsonSerializer<Object> serializerClass = StdKeySerializers.getStdKeySerializer(config, rawKeyTypeClass, true);
 assertEquals(StdKeySerializers.getDefault().getClass(), serializerClass.getClass());

 // Test for Date
 Class<?> rawKeyTypeDate = Date.class;
 JsonSerializer<Object> serializerDate = StdKeySerializers.getStdKeySerializer(config, rawKeyTypeDate, true);
 assertEquals(StdKeySerializers.getDefault(Default.TYPE_DATE, rawKeyTypeDate).getClass(), serializerDate.getClass());

 // Test for Calendar
 Class<?> rawKeyTypeCalendar = Calendar.class;
 JsonSerializer<Object> serializerCalendar = StdKeySerializers.getStdKeySerializer(config, rawKeyTypeCalendar, true);
 assertEquals(StdKeySerializers.getDefault(Default.TYPE_CALENDAR, rawKeyTypeCalendar).getClass(), serializerCalendar.getClass());

 // Test for java.util.UUID
 Class<?> rawKeyTypeUUID = java.util.UUID.class;
 JsonSerializer<Object> serializerUUID = StdKeySerializers.getStdKeySerializer(config, rawKeyTypeUUID, true);
 assertEquals(StdKeySerializers.getDefault(Default.TYPE_TO_STRING, rawKeyTypeUUID).getClass(), serializerUUID.getClass());
 }

 @Test
 public void testGetFallbackKeySerializer() throws JsonMappingException, JsonProcessingException {
 SerializationConfig config = mapper.getSerializationConfig();

 // Test for null
 Class<?> rawKeyTypeNull = null;
 JsonSerializer<Object> serializerNull = StdKeySerializers.getFallbackKeySerializer(config, rawKeyTypeNull);
 assertNull(serializerNull);

 // Test for String
 Class<?> rawKeyTypeString = String.class;
 JsonSerializer<Object> serializerString = StdKeySerializers.getFallbackKeySerializer(config, rawKeyTypeString);
 assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializerString.getClass());

 // Test for primitive and Number
 Class<?> rawKeyTypeInt = int.class;
 JsonSerializer<Object> serializerInt = StdKeySerializers.getFallbackKeySerializer(config, rawKeyTypeInt);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializerInt.getClass());

 Class<?> rawKeyTypeDouble = double.class;
 JsonSerializer<Object> serializerDouble = StdKeySerializers.getFallbackKeySerializer(config, rawKeyTypeDouble);
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializerDouble.getClass());

 // Test for Class
 Class<?> rawKeyTypeClass = Class.class;
 JsonSerializer<Object> serializerClass = StdKeySerializers.getFallbackKeySerializer(config, rawKeyTypeClass);
 assertEquals(StdKeySerializers.getDefault().getClass(), serializerClass.getClass());

 // Test for Date
 Class<?> rawKeyTypeDate = Date.class;
 JsonSerializer<Object> serializerDate = StdKeySerializers.getFallbackKeySerializer(config, rawKeyTypeDate);
 assertEquals(StdKeySerializers.getDefault(Default.TYPE_DATE, rawKeyTypeDate).getClass(), serializerDate.getClass());

 // Test for Calendar
 Class<?> rawKeyTypeCalendar = Calendar.class;
 JsonSerializer<Object> serializerCalendar = StdKeySerializers.getFallbackKeySerializer(config, rawKeyTypeCalendar);
 assertEquals(StdKeySerializers.getDefault(Default.TYPE_CALENDAR, rawKeyTypeCalendar).getClass(), serializerCalendar.getClass());

 // Test for java.util.UUID
 Class<?> rawKeyTypeUUID = java.util.UUID.class;
 JsonSerializer<Object> serializerUUID = StdKeySerializers.getFallbackKeySerializer(config, rawKeyTypeUUID);
 assertEquals(StdKeySerializers.getDefault(Default.TYPE_TO_STRING, rawKeyTypeUUID).getClass(), serializerUUID.getClass());
 }

 @Test
 public void testGetDefault() throws JsonMappingException, JsonProcessingException {
 JsonSerializer<Object> defaultSerializer = StdKeySerializers.getDefault();
 assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), defaultSerializer.getClass());
 }

 @Test
 public void testDefaultSerializeDateKey() throws JsonGenerationException, JsonMappingException,
 JsonProcessingException {
 Date date = new Date();
 ObjectNode root = mapper.createObjectNode();
 SerializerProvider provider = mapper.getSerializerProvider();
 provider.setDefaultSerializeDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
 StdKeySerializers.getDefault().serialize(date, provider.getJsonGenerator(root), provider);
 String expectedOutput = "\"2023-03-20T14:15:16.171Z\"";
 assertEquals(expectedOutput, root.get("").asText());
 }

 @Test
 public void testDefaultSerializeDateKeyWithWriteDatesUsingLocaleFormat() throws JsonGenerationException,
 JsonMappingException, JsonProcessingException {
 Date date = new Date();
 ObjectNode root = mapper.createObjectNode();
 SerializerProvider provider = mapper.getSerializerProvider();
 provider.setDefaultSerializeDateFormat(new SimpleDateFormat("dd/MM/yyyy"));
 StdKeySerializers.getDefault().serialize(date, provider.getJsonGenerator(root), provider);
 String expectedOutput = "\"20/03/2023\"";
 assertEquals(expectedOutput, root.get("").asText());
 }

 @Test
 public void testDefaultSerializeDateKeyWithWriteDatesUsingCustomFormat() throws JsonGenerationException,
 JsonMappingException, JsonProcessingException {
 Date date = new Date();
 ObjectNode root = mapper.createObjectNode();
 SerializerProvider provider = mapper.getSerializerProvider();
 provider.setDefaultSerializeDateFormat(new SimpleDateFormat("yyyyMMdd"));
 StdKeySerializers.getDefault().serialize(date, provider.getJsonGenerator(root), provider);
 String expectedOutput = "\"20230320\"";
 assertEquals(expectedOutput, root.get("").asText());
 }

 @Test
 public void testDefaultSerializeCalendarKey() throws JsonGenerationException, JsonMappingException,
 JsonProcessingException {
 Calendar calendar = Calendar.getInstance();
 ObjectNode root = mapper.createObjectNode();
 StdKeySerializers.getDefault().serialize(calendar, provider.getJsonGenerator(root), provider);
 String expectedOutput = "\"2023-03-20T14:15:16.171Z\"";
 assertEquals(expectedOutput, root.get("").asText());
 }

 @Test
 public void testDefaultSerializeStringKey() throws JsonGenerationException, JsonMappingException,
 JsonProcessingException {
 String key = "test";
 ObjectNode root = mapper.createObjectNode();
 StdKeySerializers.DEFAULT_STRING_SERIALIZER.serialize(key, provider.getJsonGenerator(root), provider);
 String expectedOutput = "\"test\"";
 assertEquals(expectedOutput, root.get("").asText());
 }

 @Test
 public void testDefaultSerializeClassKey() throws JsonProcessingException {
 Class<?> testClass = String.class;
 ObjectNode root = mapper.createObjectNode();
 StdKeySerializers.getDefault().serialize(testClass, provider.getJsonGenerator(root), provider);
 String expectedOutput = testClass.getName();
 assertEquals(expectedOutput, root.get("").asText());
 }

 @Test
 public void testDynamicSerializer() throws JsonMappingException, JsonProcessingException {
 SerializationConfig config = mapper.getSerializationConfig();
 config.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 Class<?> testClass = CustomObject.class;
 ObjectNode root = mapper.createObjectNode();
 Object testObj = new CustomObject();
 JsonSerializer<Object> dynamicSerializer = StdKeySerializers.getFallbackKeySerializer(config, testClass);
 dynamicSerializer.serialize(testObj, provider.getJsonGenerator(root), provider);
 String expectedOutput = "\"com.test.CustomObject\"";
 assertEquals(expectedOutput, root.get("").asText());
 }
}

 class CustomObject {
 private String name;
 }