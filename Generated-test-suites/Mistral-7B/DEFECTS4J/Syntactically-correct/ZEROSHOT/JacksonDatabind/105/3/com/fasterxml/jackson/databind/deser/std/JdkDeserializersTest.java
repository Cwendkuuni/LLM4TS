package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Junit 4 test file for JdkDeserializers class.
 */
public class JdkDeserializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testFindDeserializerUuid() throws IOException {
 Class<?> rawType = UUID.class;
 String clsName = UUID.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNotNull(deserializer);
 assertTrue(deserializer instanceof UUIDDeserializer);
 }

 @Test
 public void testFindDeserializerAtomicBoolean() throws IOException {
 Class<?> rawType = AtomicBoolean.class;
 String clsName = AtomicBoolean.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNotNull(deserializer);
 assertTrue(deserializer instanceof AtomicBooleanDeserializer);
 }

 @Test
 public void testFindDeserializerStackTraceElement() throws IOException {
 Class<?> rawType = StackTraceElement.class;
 String clsName = StackTraceElement.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNotNull(deserializer);
 assertTrue(deserializer instanceof StackTraceElementDeserializer);
 }

 @Test
 public void testFindDeserializerByteBuffer() throws IOException {
 Class<?> rawType = ByteBuffer.class;
 String clsName = ByteBuffer.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNotNull(deserializer);
 assertTrue(deserializer instanceof ByteBufferDeserializer);
 }

 @Test
 public void testFindDeserializerUnknownClass() throws IOException {
 Class<?> rawType = String.class;
 String clsName = Integer.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNull(deserializer);
 }

 @Test
 public void testFindDeserializerFromStringDeserializer() throws IOException {
 Class<?> rawType = String.class;
 String clsName = String.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNull(deserializer);

 JsonDeserializer<?> fromStringDeserializer = FromStringDeserializer.findDeserializer(rawType);
 assertNotNull(fromStringDeserializer);
 }
}