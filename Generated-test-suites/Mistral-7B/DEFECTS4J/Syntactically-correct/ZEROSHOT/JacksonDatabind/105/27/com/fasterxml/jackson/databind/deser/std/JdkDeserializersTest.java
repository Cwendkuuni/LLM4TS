package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.*;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdkDeserializersTest {

 private static ObjectMapper objectMapper;

 @BeforeClass
 public static void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testFindDeserializerForUUID() throws IOException {
 Class<UUID> uuidClass = UUID.class;
 String clsName = UUID.class.getName();
 JsonDeserializer<UUID> expectedDeserializer = new UUIDDeserializer();
 JsonDeserializer<UUID> actualDeserializer = JdkDeserializers.find(uuidClass, clsName);
 assertEquals(expectedDeserializer, actualDeserializer);
 }

 @Test
 public void testFindDeserializerForAtomicBoolean() throws IOException {
 Class<AtomicBoolean> atomicBooleanClass = AtomicBoolean.class;
 String clsName = AtomicBoolean.class.getName();
 JsonDeserializer<AtomicBoolean> expectedDeserializer = new AtomicBooleanDeserializer();
 JsonDeserializer<AtomicBoolean> actualDeserializer = JdkDeserializers.find(atomicBooleanClass, clsName);
 assertEquals(expectedDeserializer, actualDeserializer);
 }

 @Test
 public void testFindDeserializerForStackTraceElement() throws IOException {
 Class<StackTraceElement> stackTraceElementClass = StackTraceElement.class;
 String clsName = StackTraceElement.class.getName();
 JsonDeserializer<StackTraceElement> expectedDeserializer = new StackTraceElementDeserializer();
 JsonDeserializer<StackTraceElement> actualDeserializer = JdkDeserializers.find(stackTraceElementClass, clsName);
 assertEquals(expectedDeserializer, actualDeserializer);
 }

 @Test
 public void testFindDeserializerForByteBuffer() throws IOException {
 Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
 String clsName = ByteBuffer.class.getName();
 JsonDeserializer<ByteBuffer> expectedDeserializer = new ByteBufferDeserializer();
 JsonDeserializer<ByteBuffer> actualDeserializer = JdkDeserializers.find(byteBufferClass, clsName);
 assertEquals(expectedDeserializer, actualDeserializer);
 }

 @Test
 public void testFindDeserializerForUnknownClass() throws IOException {
 Class<Integer> integerClass = Integer.class;
 String clsName = String.class.getName();
 JsonDeserializer<Integer> actualDeserializer = JdkDeserializers.find(integerClass, clsName);
 assertNull(actualDeserializer);
 }

 @Test
 public void testFindDeserializerForFromStringDeserializer() throws IOException {
 Class<String> stringClass = String.class;
 String clsName = String.class.getName();
 JsonDeserializer<String> expectedDeserializer = FromStringDeserializer.findDeserializer(stringClass);
 JsonDeserializer<String> actualDeserializer = JdkDeserializers.find(stringClass, clsName);
 assertEquals(expectedDeserializer, actualDeserializer);
 }
}