package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.Properties;

public class StdKeySerializersTest {

 private static ObjectMapper mapper;

 @BeforeClass
 public static void setup() {
 mapper = new ObjectMapper();
 }

 @AfterClass
 public static void teardown() {
 mapper = null;
 }

 @Test
 public void testGetStdKeySerializer() throws JsonMappingException, JsonProcessingException {
 // Test with null rawKeyType
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, null, true);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Dynamic.class, serializer);

 // Test with String rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), String.class, true);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.StringKeySerializer.class, serializer);

 // Test with Object rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Object.class, true);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Dynamic.class, serializer);

 // Test with Number rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Number.class, true);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.DEFAULT_KEY_SERIALIZER.class, serializer);

 // Test with Class rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Class.class, true);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with Date rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Date.class, true);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with Calendar rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Calendar.class, true);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with UUID rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), UUID.class, true);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with Enum rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Enum.class, true);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);
 }

 @Test
 public void testGetFallbackKeySerializer() throws JsonMappingException, JsonProcessingException {
 // Test with null rawKeyType
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), null);
 assertNull(serializer);

 // Test with String rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), String.class);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.StringKeySerializer.class, serializer);

 // Test with Number rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Number.class);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.DEFAULT_KEY_SERIALIZER.class, serializer);

 // Test with Class rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Class.class);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with Date rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Date.class);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with Calendar rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Calendar.class);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with UUID rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), UUID.class);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with Enum rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Enum.class);
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);
 }

 @Test
 public void testgetDefault() throws JsonMappingException, JsonProcessingException {
 JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
 assertNotNull(serializer);
 assertInstanceOf(StdKeySerializers.DEFAULT_KEY_SERIALIZER.class, serializer);
 }

 @Test
 public void testDefaultSerialization() throws JsonMappingException, JsonProcessingException {
 Date date = new Date();
 Calendar calendar = Calendar.getInstance();
 Properties properties = new Properties();
 EnumSet<MyEnum> enumSet = EnumSet.allOf(MyEnum.class);

 // Test with Date key
 String json = mapper.writeValueAsString(new PropertyName("key").getValue(date));
 JsonParser parser = mapper.getFactory().createParser(json);
 Date result = parser.readValueAs(Date.class);
 assertEquals(date, result);

 // Test with Calendar key
 json = mapper.writeValueAsString(new PropertyName("key").getValue(calendar));
 parser = mapper.getFactory().createParser(json);
 Calendar resultCalendar = parser.readValueAs(Calendar.class);
 assertEquals(calendar, resultCalendar);

 // Test with Properties key
 json = mapper.writeValueAsString(new PropertyName("key").getValue(properties));
 parser = mapper.getFactory().createParser(json);
 Properties resultProperties = parser.readValueAs(Properties.class);
 assertArrayEquals(properties.stringPropertyNames().toArray(new String[0]), resultProperties.stringPropertyNames().toArray(new String[0]));

 // Test with EnumSet key
 json = mapper.writeValueAsString(new PropertyName("key").getValue(enumSet));
 parser = mapper.getFactory().createParser(json);
 EnumSet<MyEnum> resultEnumSet = parser.readValueAs(EnumSet.class);
 assertEquals(enumSet, resultEnumSet);
 }

 @Test
 public void testDynamicSerialization() throws JsonMappingException, JsonProcessingException {
 Object obj = new MyClass("Dynamic");
 String json = mapper.writeValueAsString(new PropertyName("key").getValue(obj));
 JsonParser parser = mapper.getFactory().createParser(json);
 Object result = parser.readValueAs(Object.class);
 assertEquals(obj, result);
 }

 @Test
 public void testDynamicSerializerMap() throws JsonMappingException, JsonProcessingException {
 Object obj = new MyClass("Dynamic1");
 Object obj2 = new MyClass("Dynamic2");
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 ObjectWriter writer = mapper.writerWithRootValueSerializer(new MyMapSerializer());
 String json = writer.writeValueAsString(new PropertyName("key1").setValue(obj));
 String json2 = writer.writeValueAsString(new PropertyName("key2").setValue(obj2));
 JsonParser parser = mapper.getFactory().createParser(json);
 MyMap map = parser.readValueAs(MyMap.class);
 assertEquals(obj, map.get("key1"));
 assertEquals(obj2, map.get("key2"));
 }

 @Test
 public void testStringKeySerializerSerialization() throws JsonMappingException, JsonProcessingException {
 String json = mapper.writeValueAsString(new PropertyName("key").setValue("StringKey"));
 JsonParser parser = mapper.getFactory().createParser(json);
 String result = parser.readValueAs(String.class);
 assertEquals("StringKey", result);
 }

 private static class MyClass {
 private final String name;

 MyClass(String name) {
 this.name = name;
 }

 @Override
 public String toString() {
 return name;
 }
 }

 private static class MyMapSerializer extends StdSerializer<MyMap> {
 private final PropertySerializerMap _dynamicSerializers;

 public MyMapSerializer() {
 this(null);
 }

 public MyMapSerializer(PropertySerializerMap dynamicSerializers) {
 super(MyMap.class);
 _dynamicSerializers = dynamicSerializers;
 }

 @Override
 public void serialize(MyMap value, JsonGenerator g, SerializerProvider provider) throws IOException {
 g.writeStartObject();
 for (Map.Entry<String, Object> entry : value.entrySet()) {
 String key = entry.getKey();
 Object value = entry.getValue();
 PropertySerializerMap map = _dynamicSerializers;
 JsonSerializer<Object> serializer = map.serializerFor(value.getClass());
 serializer.serialize(value, g, provider);
 g.writeFieldName(key);
 }
 g.writeEndObject();
 }
 }

 private static class MyMap extends HashMap<String, Object> {
 }

 private static enum MyEnum {
 VALUE1, VALUE2, VALUE3
 }
}