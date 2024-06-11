package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProviders;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Dynamic;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.StringKeySerializer;

public class StdKeySerializersTest {

 @Test
 public void testGetStdKeySerializer() throws JsonMappingException, JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();

 // Test with null rawKeyType
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), null, true);
 assertNotNull(serializer);

 // Test with String rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), String.class, true);
 assertInstanceOf(StringKeySerializer.class, serializer);

 // Test with int rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), int.class, true);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with Date rawKeyType
 Date date = new Date();
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Date.class, true);
 assertInstanceOf(Default.class, serializer);

 // Test with Calendar rawKeyType
 Calendar calendar = Calendar.getInstance();
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Calendar.class, true);
 assertInstanceOf(Default.class, serializer);

 // Test with Enum rawKeyType
 enum MyEnum { A, B, C }
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), MyEnum.class, true);
 assertInstanceOf(Default.class, serializer);

 // Test with Class rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Class.class, true);
 assertInstanceOf(Default.class, serializer);

 // Test with long rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), long.class, true);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with double rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), double.class, true);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with boolean rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), boolean.class, true);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with char rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), char.class, true);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with short rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), short.class, true);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with byte rawKeyType
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), byte.class, true);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with custom class rawKeyType
 class MyCustomClass {
 @JsonSerialize(using = MyCustomSerializer.class)
 private String name;
 }
 class MyCustomSerializer extends StdSerializer<String> {
 @Override
 public void serialize(String value, JsonGenerator g, SerializerProvider provider) throws IOException {
 g.writeFieldName("MyCustomClass_" + value);
 }
 }
 serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), MyCustomClass.class, true);
 assertInstanceOf(Dynamic.class, serializer);
 }

 @Test
 public void testGetFallbackKeySerializer() throws JsonMappingException, JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();

 // Test with null rawKeyType
 Class<?> rawKeyType = null;
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), rawKeyType);
 assertNull(serializer);

 // Test with String rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), String.class);
 assertInstanceOf(StringKeySerializer.class, serializer);

 // Test with int rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), int.class);
 assertInstanceOf(StdKeySerializers.Default.class, serializer);

 // Test with Date rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Date.class);
 assertInstanceOf(Default.class, serializer);

 // Test with Calendar rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Calendar.class);
 assertInstanceOf(Default.class, serializer);

 // Test with Enum rawKeyType
 enum MyEnum { A, B, C }
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), MyEnum.class);
 assertInstanceOf(Default.class, serializer);

 // Test with Class rawKeyType
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Class.class);
 assertInstanceOf(Default.class, serializer);

 // Test with custom class rawKeyType
 class MyCustomClass {
 @JsonSerialize(using = MyCustomSerializer.class)
 private String name;
 }
 class MyCustomSerializer extends StdSerializer<String> {
 @Override
 public void serialize(String value, JsonGenerator g, SerializerProvider provider) throws IOException {
 g.writeFieldName("MyCustomClass_" + value);
 }
 }
 serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), MyCustomClass.class);
 assertInstanceOf(Dynamic.class, serializer);
 }

 @Test
 public void testDynamicSerializer() throws JsonMappingException, JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 SerializerProvider provider = SerializerProviders.baseProvider();

 // Test with String
 String str1 = "test";
 JsonSerializer<Object> serializer = provider.getSerializerProvider().findValueSerializer(String.class);
 assertInstanceOf(StringKeySerializer.class, serializer);
 serializer.serialize(str1, new JsonGenerator(mapper.getFactory()), provider);

 // Test with int
 int intValue = 1;
 serializer = provider.getSerializerProvider().findValueSerializer(intValue.getClass());
 assertInstanceOf(StdKeySerializers.Default.class, serializer);
 serializer.serialize(intValue, new JsonGenerator(mapper.getFactory()), provider);

 // Test with Date
 Date date = new Date();
 serializer = provider.getSerializerProvider().findValueSerializer(Date.class);
 assertInstanceOf(Default.class, serializer);
 serializer.serialize(date, new JsonGenerator(mapper.getFactory()), provider);

 // Test with Calendar
 Calendar calendar = Calendar.getInstance();
 serializer = provider.getSerializerProvider().findValueSerializer(Calendar.class);
 assertInstanceOf(Default.class, serializer);
 serializer.serialize(calendar, new JsonGenerator(mapper.getFactory()), provider);

 // Test with Enum
 enum MyEnum { A, B, C }
 serializer = provider.getSerializerProvider().findValueSerializer(MyEnum.class);
 assertInstanceOf(Default.class, serializer);
 serializer.serialize(MyEnum.A, new JsonGenerator(mapper.getFactory()), provider);

 // Test with Class
 Class<?> cls = String.class;
 serializer = provider.getSerializerProvider().findValueSerializer(cls);
 assertInstanceOf(Default.class, serializer);
 serializer.serialize(cls, new JsonGenerator(mapper.getFactory()), provider);

 // Test with long
 long longValue = 1L;
 serializer = provider.getSerializerProvider().findValueSerializer(longValue.getClass());
 assertInstanceOf(StdKeySerializers.Default.class, serializer);
 serializer.serialize(longValue, new JsonGenerator(mapper.getFactory()), provider);

 // Test with double
 double doubleValue = 1.0;
 serializer = provider.getSerializerProvider().findValueSerializer(doubleValue.getClass());
 assertInstanceOf(StdKeySerializers.Default.class, serializer);
 serializer.serialize(doubleValue, new JsonGenerator(mapper.getFactory()), provider);

 // Test with boolean
 boolean boolValue = true;
 serializer = provider.getSerializerProvider().findValueSerializer(boolValue.getClass());
 assertInstanceOf(StdKeySerializers.Default.class, serializer);
 serializer.serialize(boolValue, new JsonGenerator(mapper.getFactory()), provider);

 // Test with char
 char charValue = 'A';
 serializer = provider.getSerializerProvider().findValueSerializer(charValue.getClass());
 assertInstanceOf(StdKeySerializers.Default.class, serializer);
 serializer.serialize(charValue, new JsonGenerator(mapper.getFactory()), provider);

 // Test with short
 short shortValue = 1;
 serializer = provider.getSerializerProvider().findValueSerializer(shortValue.getClass());
 assertInstanceOf(StdKeySerializers.Default.class, serializer);
 serializer.serialize(shortValue, new JsonGenerator(mapper.getFactory()), provider);

 // Test with byte
 byte byteValue = 1;
 serializer = provider.getSerializerProvider().findValueSerializer(byteValue.getClass());
 assertInstanceOf(StdKeySerializers.Default.class, serializer);
 serializer.serialize(byteValue, new JsonGenerator(mapper.getFactory()), provider);

 // Test with custom class
 class MyCustomClass {
 @JsonSerialize(using = MyCustomSerializer.class)
 private String name;
 }
 class MyCustomSerializer extends StdSerializer<String> {
 @Override
 public void serialize(String value, JsonGenerator g, SerializerProvider provider) throws IOException {
 g.writeFieldName("MyCustomClass_" + value);
 }
 }
 serializer = provider.getSerializerProvider().findValueSerializer(MyCustomClass.class);
 assertInstanceOf(Dynamic.class, serializer);
 serializer.serialize(new MyCustomClass(), new JsonGenerator(mapper.getFactory()), provider);
 }
}