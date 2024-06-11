package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer.FromStringDeserializerType;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import com.fasterxml.jackson.databind.deser.std.StackTraceElementDeserializer;
import com.fasterxml.jackson.databind.deser.std.AtomicBooleanDeserializer;
import com.fasterxml.jackson.databind.deser.std.ByteBufferDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdkDeserializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testFind_UUID() throws IOException {
 Class<UUID> uuidClass = UUID.class;
 JsonDeserializer<UUID> deserializer = JdkDeserializers.find(uuidClass, UUID.class.getName());
 assertEquals(UUIDDeserializer.class, deserializer.getClass());
 }

 @Test
 public void testFind_UUIDWithClassName() throws IOException {
 Class<UUID> uuidClass = UUID.class;
 JsonDeserializer<UUID> deserializer = JdkDeserializers.find(uuidClass, "java.util.UUID");
 assertEquals(UUIDDeserializer.class, deserializer.getClass());
 }

 @Test
 public void testFind_AtomicBoolean() throws IOException {
 Class<AtomicBoolean> atomicBooleanClass = AtomicBoolean.class;
 JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(atomicBooleanClass, AtomicBoolean.class.getName());
 assertEquals(AtomicBooleanDeserializer.class, deserializer.getClass());
 }

 @Test
 public void testFind_AtomicBooleanWithClassName() throws IOException {
 Class<AtomicBoolean> atomicBooleanClass = AtomicBoolean.class;
 JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(atomicBooleanClass, "java.util.concurrent.atomic.AtomicBoolean");
 assertEquals(AtomicBooleanDeserializer.class, deserializer.getClass());
 }

 @Test
 public void testFind_StackTraceElement() throws IOException {
 Class<StackTraceElement> stackTraceElementClass = StackTraceElement.class;
 JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(stackTraceElementClass, StackTraceElement.class.getName());
 assertEquals(StackTraceElementDeserializer.class, deserializer.getClass());
 }

 @Test
 public void testFind_StackTraceElementWithClassName() throws IOException {
 Class<StackTraceElement> stackTraceElementClass = StackTraceElement.class;
 JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(stackTraceElementClass, "java.lang.StackTraceElement");
 assertEquals(StackTraceElementDeserializer.class, deserializer.getClass());
 }

 @Test
 public void testFind_ByteBuffer() throws IOException {
 Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
 JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(byteBufferClass, ByteBuffer.class.getName());
 assertEquals(ByteBufferDeserializer.class, deserializer.getClass());
 }

 @Test
 public void testFind_ByteBufferWithClassName() throws IOException {
 Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
 JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(byteBufferClass, "java.nio.ByteBuffer");
 assertEquals(ByteBufferDeserializer.class, deserializer.getClass());
 }

 @Test
 public void testFind_UnknownClass() throws IOException {
 Class<String> stringClass = String.class;
 JsonDeserializer<String> deserializer = JdkDeserializers.find(stringClass, "java.util.UUID.class");
 assertNull(deserializer);
 }

 @Test
 public void testFind_FromStringDeserializer() throws IOException {
 Class<?> unknownClass = Object.class;
 JsonDeserializer<?> fromStringDeserializer = FromStringDeserializer.findDeserializer(unknownClass);
 assertNotNull(fromStringDeserializer);
 JsonDeserializer<?> jdkDeserializer = JdkDeserializers.find(unknownClass, unknownClass.getName());
 assertNull(jdkDeserializer);
 }

 @Test
 public void testFind_Null() {
 Class<?> nullClass = null;
 JsonDeserializer<?> deserializer = JdkDeserializers.find(nullClass, null);
 assertNull(deserializer);
 }
}