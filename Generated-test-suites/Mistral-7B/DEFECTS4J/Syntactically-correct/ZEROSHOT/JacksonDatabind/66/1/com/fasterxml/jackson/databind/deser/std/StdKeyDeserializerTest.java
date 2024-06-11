package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testBooleanKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer booleanKeyDeserializer = StdKeyDeserializer.forType(Boolean.class);
 assertEquals(Boolean.TRUE, booleanKeyDeserializer.deserializeKey("true", objectMapper.getDeserializationContext()));
 assertEquals(Boolean.FALSE, booleanKeyDeserializer.deserializeKey("false", objectMapper.getDeserializationContext()));
 }

 @Test
 public void testByteKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer byteKeyDeserializer = StdKeyDeserializer.forType(Byte.class);
 assertEquals((byte) 5, byteKeyDeserializer.deserializeKey("5", objectMapper.getDeserializationContext()));
 }

 @Test
 public void testShortKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer shortKeyDeserializer = StdKeyDeserializer.forType(Short.class);
 assertEquals((short) 10, shortKeyDeserializer.deserializeKey("10", objectMapper.getDeserializationContext()));
 }

 @Test
 public void testCharKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer charKeyDeserializer = StdKeyDeserializer.forType(Character.class);
 assertEquals((char) 'a', charKeyDeserializer.deserializeKey("a", objectMapper.getDeserializationContext()));
 }

 @Test
 public void testIntKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer intKeyDeserializer = StdKeyDeserializer.forType(Integer.class);
 assertEquals(15, intKeyDeserializer.deserializeKey("15", objectMapper.getDeserializationContext()));
 }

 @Test
 public void testLongKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer longKeyDeserializer = StdKeyDeserializer.forType(Long.class);
 assertEquals(20L, longKeyDeserializer.deserializeKey("20", objectMapper.getDeserializationContext()));
 }

 @Test
 public void testFloatKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer floatKeyDeserializer = StdKeyDeserializer.forType(Float.class);
 assertEquals(3.14f, floatKeyDeserializer.deserializeKey("3.14", objectMapper.getDeserializationContext()), 0.01f);
 }

 @Test
 public void testDoubleKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer doubleKeyDeserializer = StdKeyDeserializer.forType(Double.class);
 assertEquals(4.56, doubleKeyDeserializer.deserializeKey("4.56", objectMapper.getDeserializationContext()), 0.01);
 }

 @Test
 public void testLocaleKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer localeKeyDeserializer = StdKeyDeserializer.forType(Locale.class);
 assertEquals(Locale.US, localeKeyDeserializer.deserializeKey("en_US", objectMapper.getDeserializationContext()));
 }

 @Test
 public void testDateKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer dateKeyDeserializer = StdKeyDeserializer.forType(Date.class);
 assertEquals(new Date(System.currentTimeMillis()), dateKeyDeserializer.deserializeKey("2023-03-26T14:15:30Z", objectMapper.getDeserializationContext()));
 }

 @Test
 public void testCalendarKeyDeserializer() throws JsonProcessingException {
 StdKeyDeserializer calendarKeyDeserializer = StdKeyDeserializer.forType(Calendar.class);
 Calendar calendar = Calendar.getInstance();
 assertEquals(calendar, calendarKeyDeserializer.deserializeKey("2023-03-26", objectMapper.getDeserializationContext()));
 }

 @Test(expected = JsonMappingException.class)
 public void testUnknownKeyType() throws JsonProcessingException {
 StdKeyDeserializer unknownKeyDeserializer = new StdKeyDeserializer(-1, Object.class);
 unknownKeyDeserializer.deserializeKey("test", objectMapper.getDeserializationContext());
 }
}