package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testSerialization() throws JsonProcessingException {
 // Test serialization for Integer
 Integer intValue = 10;
 String intJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(intValue);
 assertEquals("10", intJson);

 // Test serialization for Long
 Long longValue = 20L;
 String longJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(longValue);
 assertEquals("20", longJson);

 // Test serialization for Short
 Short shortValue = 30;
 String shortJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(shortValue);
 assertEquals("30", shortJson);

 // Test serialization for Float
 Float floatValue = 40.5f;
 String floatJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(floatValue);
 assertEquals("40.5", floatJson);

 // Test serialization for Double
 Double doubleValue = 50.0;
 String doubleJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(doubleValue);
 assertEquals("50.0", doubleJson);
 }

 @Test
 public void testDeserialization() throws JsonProcessingException {
 // Test deserialization for Integer
 String intJson = "10";
 Integer intValue = objectMapper.readValue(intJson, Integer.class);
 assertEquals(10, intValue.intValue());

 // Test deserialization for Long
 String longJson = "20";
 Long longValue = objectMapper.readValue(longJson, Long.class);
 assertEquals(20, longValue.longValue());

 // Test deserialization for Short
 String shortJson = "30";
 Short shortValue = objectMapper.readValue(shortJson, Short.class);
 assertEquals(30, shortValue.shortValue());

 // Test deserialization for Float
 String floatJson = "40.5";
 Float floatValue = objectMapper.readValue(floatJson, Float.class);
 assertEquals(40.5f, floatValue.floatValue());

 // Test deserialization for Double
 String doubleJson = "50.0";
 Double doubleValue = objectMapper.readValue(doubleJson, Double.class);
 assertEquals(50.0, doubleValue.doubleValue(), 0.0);
 }

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 // Test IntegerSerializer serialization
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> intSerializer = mapper.findValueSerializer(Integer.class);
 String intJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(10);
 Object intValue = mapper.readValue(intJson, Object.class);
 assertEquals(10, intValue.intValue());
 assertEquals(Integer.class, intValue.getClass());

 // Test IntegerSerializer deserialization
 ObjectMapper mapper2 = new ObjectMapper();
 JsonDeserializer<Object> intDeserializer = mapper2.findValueDeserializer(Object.class, Integer.class);
 String intJson2 = "10";
 Object intValue2 = mapper2.readValue(intJson2, Object.class);
 assertEquals(10, intValue2.intValue());
 assertEquals(Integer.class, intValue2.getClass());
 }

 @Test
 public void testIntLikeSerializer() throws JsonProcessingException {
 // Test IntLikeSerializer serialization
 ObjectMapper mapper = new ObjectMapper();
 JsonSerializer<Object> intLikeSerializer = mapper.findValueSerializer(Number.class);
 Number intLikeValue = 10;
 String intLikeJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(intLikeValue);
 Object intLikeValueDeserialized = mapper.readValue(intLikeJson, Object.class);
 assertEquals(10, intLikeValueDeserialized.intValue());

 // Test IntLikeSerializer deserialization
 ObjectMapper mapper2 = new ObjectMapper();
 JsonDeserializer<Object> intLikeDeserializer = mapper2.findValueDeserializer(Object.class, Number.class);
 String intLikeJson2 = "10";
 Object intLikeValueDeserialized2 = mapper2.readValue(intLikeJson2, Object.class);
 assertEquals(10, intLikeValueDeserialized2.intValue());
 }
}