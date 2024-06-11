package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

public class StdKeySerializersTest {

 private StdKeySerializers serializers;
 private ObjectMapper objectMapper;
 private SerializationConfig config;

 @Before
 public void setup() {
 serializers = new StdKeySerializers();
 objectMapper = new ObjectMapper();
 config = objectMapper.getSerializationConfig();
 }

 @Test
 public void testDefaultKeySerializer() throws Exception {
 JsonSerializer<Object> serializer = serializers.getDefaultKeySerializer(config, null, false);
 assertNotNull(serializer);
 }

 @Test
 public void testGetStringSerializer() throws Exception {
 JsonSerializer<Object> serializer = serializers.getDefaultKeySerializer(config, String.class, false);
 assertSame(serializers.getDefaultStringSerializer(), serializer);
 }

 @Test
 public void testPrimitiveAndNumberSerializer() throws Exception {
 JsonSerializer<Object> serializer = serializers.getDefaultKeySerializer(config, int.class, false);
 assertSame(serializers.getDefaultKeySerializer(), serializer);

 serializer = serializers.getDefaultKeySerializer(config, Double.TYPE, false);
 assertSame(serializers.getDefaultKeySerializer(), serializer);
 }

 @Test
 public void testClassSerializer() throws Exception {
 JsonSerializer<Object> serializer = serializers.getDefaultKeySerializer(config, Class.class, false);
 assertSame(serializers.new Default(Default.TYPE_CLASS, Class.class), serializer);
 }

 @Test
 public void testDateSerializer() throws Exception {
 JsonSerializer<Object> serializer = serializers.getDefaultKeySerializer(config, Date.class, false);
 assertSame(serializers.new Default(Default.TYPE_DATE, Date.class), serializer);
 }

 @Test
 public void testCalendarSerializer() throws Exception {
 JsonSerializer<Object> serializer = serializers.getDefaultKeySerializer(config, Calendar.class, false);
 assertSame(serializers.new Default(Default.TYPE_CALENDAR, Calendar.class), serializer);
 }

 @Test
 public void testUuidSerializer() throws Exception {
 JsonSerializer<Object> serializer = serializers.getDefaultKeySerializer(config, java.util.UUID.class, false);
 assertNull(serializer);
 }

 @Test
 public void testEnumSerializer() throws Exception {
 Enum testEnum = Enum.values()[0];
 JsonSerializer<Object> serializer = serializers.getFallbackKeySerializer(config, testEnum.getClass());
 assertSame(serializers.new Default(Default.TYPE_ENUM, testEnum.getClass()), serializer);
 }

 @Test
 public void testDynamicSerializer() throws Exception {
 PropertySerializerMap map = new PropertySerializerMap();
 JsonSerializer<Object> serializer = serializers.new Dynamic().serialize(new Object(), objectMapper.getSerializerProvider(), config);
 assertNotNull(serializer);

 map = new PropertySerializerMap();
 serializer = serializers.new Dynamic()._findAndAddDynamic(map, String.class, objectMapper.getSerializerProvider());
 assertEquals(serializers.getDefaultStringSerializer(), serializer);

 map = new PropertySerializerMap();
 serializer = serializers.new Dynamic()._findAndAddDynamic(map, Integer.TYPE, objectMapper.getSerializerProvider());
 assertEquals(serializers.getDefaultKeySerializer(), serializer);
 }

 @Test
 public void testStringKeySerializer() throws Exception {
 JsonSerializer<Object> serializer = serializers.getDefaultKeySerializer(config, String.class, false);
 assertNotSame(serializers.getDefaultStringSerializer(), serializer);
 assertSame(serializers.new StringKeySerializer(), serializer);
 }
}