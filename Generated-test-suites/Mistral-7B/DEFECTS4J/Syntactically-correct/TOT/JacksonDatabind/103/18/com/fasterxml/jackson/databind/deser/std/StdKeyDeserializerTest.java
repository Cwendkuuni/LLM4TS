package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;
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
 public void testStringDeserializer() throws JsonProcessingException {
 String key = "testKey";
 StdKeyDeserializer deserializer = StringKD.forType(String.class);
 Object result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertEquals(key, result);
 }

 @Test
 public void testObjectDeserializer() throws JsonProcessingException {
 String key = "testObject";
 StdKeyDeserializer deserializer = StringKD.forType(Object.class);
 Object result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertNotNull(result);
 }

 @Test
 public void testIntDeserializer() throws JsonProcessingException {
 String key = "123";
 StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
 Object result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertEquals(123, result);
 }

 @Test
 public void testDoubleDeserializer() throws JsonProcessingException {
 String key = "3.14";
 StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
 Object result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertEquals(3.14, result, 0.001);
 }

 @Test
 public void testEnumDeserializer() throws JsonProcessingException {
 AnnotatedClass ac = objectMapper.getAnnotationIntrospector().findClassAnnotations(MyEnum.class);
 AnnotatedMethod am = ac.findMethod("valueOf", String.class);
 JsonDeserializer<MyEnum> jsonDeserializer = objectMapper.findValueDeserializer(MyEnum.class);
 StdKeyDeserializer deserializer = new EnumKD(new EnumResolver(ac, am), null, jsonDeserializer);
 String key = "MyEnum.ONE";
 Object result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertEquals(MyEnum.ONE, result);
 }

 @Test
 public void testClassDeserializer() throws JsonProcessingException {
 String key = "java.util.Date";
 StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, Class.class);
 Object result = deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 assertInstanceof(Date.class, result);
 }

 @Test
 public void testInvalidDeserializer() throws JsonProcessingException {
 String key = "InvalidType";
 StdKeyDeserializer deserializer = new StdKeyDeserializer(-1, String.class);
 try {
 deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("not a valid representation", e.getMessage());
 }
 }

 @Test
 public void testInvalidEnumDeserializer() throws JsonProcessingException {
 String key = "InvalidEnumValue";
 StdKeyDeserializer deserializer = new EnumKD(null, null).forType(MyEnum.class);
 try {
 deserializer.deserializeKey(key, objectMapper.getDeserializationContext());
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("not one of values excepted for Enum class: [", e.getMessage());
 }
 }
}