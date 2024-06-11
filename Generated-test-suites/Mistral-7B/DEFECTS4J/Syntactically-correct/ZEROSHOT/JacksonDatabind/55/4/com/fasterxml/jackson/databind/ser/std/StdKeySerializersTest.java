package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.util.StdSerializer;

public class StdKeySerializersTest {

 @Test
 public void testgetDefaultKeySerializer() throws Exception {
 SerializationConfig config = new JsonMapper().getSerializationConfig();
 JsonSerializer<Object> serializer = StdKeySerializers.getDefaultKeySerializer(config, null, false);
 assertNotNull(serializer);
 }

 @Test
 public void testGetStringSerializer() throws Exception {
 SerializationConfig config = new JsonMapper().getSerializationConfig();
 JsonSerializer<Object> serializer = StdKeySerializers.getDefaultKeySerializer(config, String.class, false);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.StringKeySerializer.class, serializer);
 }

 @Test
 public void testGetSerializerForPrimitiveTypes() throws Exception {
 SerializationConfig config = new JsonMapper().getSerializationConfig();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, int.class, false);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer);
 }

 @Test
 public void testGetSerializerForClass() throws Exception {
 SerializationConfig config = new JsonMapper().getSerializationConfig();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Class.class, false);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);
 }

 @Test
 public void testGetSerializerForDate() throws Exception {
 SerializationConfig config = new JsonMapper().getSerializationConfig();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);
 }

 @Test
 public void testGetSerializerForCalendar() throws Exception {
 SerializationConfig config = new JsonMapper().getSerializationConfig();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);
 }

 @Test
 public void testGetSerializerForUuid() throws Exception {
 SerializationConfig config = new JsonMapper().getSerializationConfig();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, false);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);
 }

 @Test
 public void testGetFallbackKeySerializer() throws Exception {
 SerializationConfig config = new JsonMapper().getSerializationConfig();
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, null);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer);
 }

 @Test
 public void testGetFallbackKeySerializerForEnum() throws Exception {
 SerializationConfig config = new JsonMapper().getSerializationConfig();
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, Enum.class);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);
 }

 @Test
 public void testDynamicKeySerializer() throws Exception {
 SerializationConfig config = new JsonMapper().getSerializationConfig();
 ObjectMapper mapper = new JsonMapper();
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Object.class, false);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Dynamic.class, serializer);

 JsonGenerator gen = mapper.getFactory().createGenerator();
 SerializerProvider provider = mapper.getSerializerProvider();
 provider.setDefaultSerializer(String.class, mapper.getFactory().getValueSerializerFactory().findValueSerializer(String.class, provider));
 serializer.serialize(new Object(), gen, provider);
 gen.flush();
 }
}