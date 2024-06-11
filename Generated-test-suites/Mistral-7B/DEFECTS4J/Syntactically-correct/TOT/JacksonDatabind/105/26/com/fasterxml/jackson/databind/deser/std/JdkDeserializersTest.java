package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JdkDeserializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testFindForUUID() throws IOException {
 Class<UUID> uuidClass = UUID.class;
 String clsName = UUID.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(uuidClass, clsName);
 assertEquals(UUIDDeserializer.class, deserializer.getClass());
 }

 @Test
 public void testFindForAtomicBoolean() throws IOException {
 Class<AtomicBoolean> atomicBooleanClass = AtomicBoolean.class;
 String clsName = AtomicBoolean.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(atomicBooleanClass, clsName);
 assertEquals(AtomicBooleanDeserializer.class, deserializer.getClass());
 }

 @Test
 public void testFindForStackTraceElement() throws IOException {
 Class<StackTraceElement> stackTraceElementClass = StackTraceElement.class;
 String clsName = StackTraceElement.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(stackTraceElementClass, clsName);
 assertEquals(StackTraceElementDeserializer.class, deserializer.getClass());
 }

 @Test
 public void testFindForByteBuffer() throws IOException {
 Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
 String clsName = ByteBuffer.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(byteBufferClass, clsName);
 assertEquals(ByteBufferDeserializer.class, deserializer.getClass());
 }

 @Test
 public void testFindForUnknownClass() throws IOException {
 Class<Integer> integerClass = Integer.class;
 String clsName = String.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(integerClass, clsName);
 assertNull(deserializer);
 }

 @Test
 public void testFromStringDeserializerPriority() throws IOException {
 Class<Integer> integerClass = Integer.class;
 String clsName = Integer.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(integerClass, clsName);
 assertNull(deserializer);

 String jsonString = "123";
 Object deserializedObject = objectMapper.readValue(jsonString, new TypeReference<Object>() {});
 assertEquals(123, deserializedObject);
 }
}