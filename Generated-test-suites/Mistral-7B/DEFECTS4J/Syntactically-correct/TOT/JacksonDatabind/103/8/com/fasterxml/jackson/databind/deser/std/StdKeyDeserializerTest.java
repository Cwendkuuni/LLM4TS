package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testBooleanKeyDeserialization() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 DeserializationContext ctx = new TestDeserializationContext();
 String key = "true";
 Object result = keyDeserializer.deserializeKey(key, ctx);
 assertTrue((Boolean) result);
 key = "false";
 result = keyDeserializer.deserializeKey(key, ctx);
 assertFalse((Boolean) result);
 }

 @Test
 public void testByteKeyDeserialization() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
 DeserializationContext ctx = new TestDeserializationContext();
 String key = "127";
 Object result = keyDeserializer.deserializeKey(key, ctx);
 assertEquals((Byte) (byte) 127, result);
 key = "-128";
 result = keyDeserializer.deserializeKey(key, ctx);
 assertEquals((Byte) (byte) -128, result);
 key = "256";
 try {
 result = keyDeserializer.deserializeKey(key, ctx);
 fail("Expected IOException: value cannot be represented as 8-bit value");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testShortKeyDeserialization() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
 DeserializationContext ctx = new TestDeserializationContext();
 String key = "32767";
 Object result = keyDeserializer.deserializeKey(key, ctx);
 assertEquals((Short) Short.MAX_VALUE, result);
 key = "-32768";
 result = keyDeserializer.deserializeKey(key, ctx);
 assertEquals((Short) Short.MIN_VALUE, result);
 key = "65536";
 try {
 result = keyDeserializer.deserializeKey(key, ctx);
 fail("Expected IOException: overflow, value cannot be represented as 16-bit value");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testCharKeyDeserialization() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
 DeserializationContext ctx = new TestDeserializationContext();
 String key = "A";
 Object result = keyDeserializer.deserializeKey(key, ctx);
 assertEquals((Character) 'A', result);
 key = "Ａ";
 Object result2 = keyDeserializer.deserializeKey(key, ctx);
 assertEquals((Character) '\u0041', result2);
 key = "abc";
 try {
 result = keyDeserializer.deserializeKey(key, ctx);
 fail("Expected IOException: can only convert 1-character Strings");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testIntKeyDeserialization() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
 DeserializationContext ctx = new TestDeserializationContext();
 String key = "2147483647";
 Object result = keyDeserializer.deserializeKey(key, ctx);
 assertEquals(Integer.MAX_VALUE, result);
 key = "-2147483648";
 result = keyDeserializer.deserializeKey(key, ctx);
 assertEquals(Integer.MIN_VALUE, result);
 }

 @Test
 public void testLongKeyDeserialization() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
 DeserializationContext ctx = new TestDeserializationContext();
 String key = "9223372036854775807";
 Object result = keyDeserializer.deserializeKey(key, ctx);
 assertEquals(Long.MAX_VALUE, result);
 key = "-9223372036854775808";
 result = keyDeserializer.deserializeKey(key, ctx);
 assertEquals(Long.MIN_VALUE, result);
 }

 @Test
 public void testDateKeyDeserialization() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
 DeserializationContext ctx = new TestDeserializationContext();
 String key = "2022-01-01";
 Object result = keyDeserializer.deserializeKey(key, ctx);
 assertEquals(new Date(2022, 0, 1), result);
 key = "2022-12-31";
 result = keyDeserializer.deserializeKey(key, ctx);
 assertEquals(new Date(2022, 11, 30), result); // Note: Java Date uses zero-based months
 }

 @Test
 public void testUriKeyDeserialization() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
 DeserializationContext ctx = new TestDeserializationContext();
 String key = "https://www.example.com/path/to/resource";
 Object result = keyDeserializer.deserializeKey(key, ctx);
 assertEquals(new URI("https://www.example.com/path/to/resource"), result);
 }

 @Test
 public void testUrlKeyDeserialization() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
 DeserializationContext ctx = new TestDeserializationContext();
 String key = "http://www.example.com/path/to/resource";
 Object result = keyDeserializer.deserializeKey(key, ctx);
 assertEquals(new URL("http://www.example.com/path/to/resource"), result);
 }

 @Test
 public void testEnumKeyDeserialization() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(MyEnum.class);
 DeserializationContext ctx = new TestDeserializationContext();
 String key = "VALUE_ONE";
 Object result = keyDeserializer.deserializeKey(key, ctx);
 assertEquals(MyEnum.VALUE_ONE, result);
 key = "VALUE_TWO";
 result = keyDeserializer.deserializeKey(key, ctx);
 assertEquals(MyEnum.VALUE_TWO, result);
 key = "INVALID_VALUE";
 try {
 result = keyDeserializer.deserializeKey(key, ctx);
 fail("Expected IOException: not one of values excepted for Enum class");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testStringKeyDeserialization() throws JsonProcessingException {
 KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
 DeserializationContext ctx = new TestDeserializationContext();
 String key = "test";
 Object result = keyDeserializer.deserializeKey(key, ctx);
 assertEquals("test", result);
 }
}

class TestDeserializationContext extends DeserializationContext {
 @Override
 public JsonParser _parser() {
 throw new UnsupportedOperationException();
 }

 @Override
 public JsonParser _constructParser(Object c, DeserializationConfig config) {
 throw new UnsupportedOperationException();
 }

 @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
 public void _findValueInMap(Map<?, ?> map, Class<?> keyType) {
 throw new UnsupportedOperationException();
 }

 @JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
 public Object _findValueInMap(Map<?, ?> map, String keyName) {
 throw new UnsupportedOperationException();
 }

 @Override
 public Object _findValueInMap(Map<?, ?> map, Object key) {
 throw new UnsupportedOperationException();
 }
}