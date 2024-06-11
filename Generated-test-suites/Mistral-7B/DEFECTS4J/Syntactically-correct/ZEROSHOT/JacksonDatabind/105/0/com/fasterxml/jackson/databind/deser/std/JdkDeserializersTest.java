package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdkDeserializersTest {

 private ObjectMapper mapper;

 @BeforeClass
 public static void setupBeforeClass() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testFindDeserializer_UUID() throws IOException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
 assertNotNull(deserializer);
 assertInstanceOf(UUIDDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializer_AtomicBoolean() throws IOException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
 assertNotNull(deserializer);
 assertInstanceOf(AtomicBooleanDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializer_StackTraceElement() throws IOException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
 assertNotNull(deserializer);
 assertInstanceOf(StackTraceElementDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializer_ByteBuffer() throws IOException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
 assertNotNull(deserializer);
 assertInstanceOf(ByteBufferDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializer_UnknownClass() throws IOException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(String.class, "java.util.Date.class");
 assertNull(deserializer);
 }

 @Test
 public void testFindDeserializer_NullClass() throws IOException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(null, "java.util.Date.class");
 assertNull(deserializer);
 }

 @Test
 public void testFindDeserializer_NullClassName() throws IOException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, null);
 assertNull(deserializer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindDeserializer_InvalidClassName() throws IOException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, "java.util.Stack.class");
 fail("Should throw IllegalArgumentException");
 }
}