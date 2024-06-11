package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.FromStringDeserializer.Std;
import com.fasterxml.jackson.databind.ser.std.FromStringSerializer.Base;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;

import org.junit.Test;

public class JdkDeserializersTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testFindNull() throws IOException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(null, null);
 assertNull(deserializer);
 }

 @Test
 public void testFindUnknownClass() throws IOException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(String.class, "UnknownClass");
 assertNull(deserializer);
 }

 @Test
 public void testFindUuidDeserializer() throws IOException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
 assertNotNull(deserializer);
 assertInstanceOf(UUIDDeserializer.class, deserializer);
 }

 @Test
 public void testFindAtomicBooleanDeserializer() throws IOException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
 assertNotNull(deserializer);
 assertInstanceOf(AtomicBooleanDeserializer.class, deserializer);
 }

 @Test
 public void testFindStackTraceElementDeserializer() throws IOException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
 assertNotNull(deserializer);
 assertInstanceOf(StackTraceElementDeserializer.class, deserializer);
 }

 @Test
 public void testFindByteBufferDeserializer() throws IOException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
 assertNotNull(deserializer);
 assertInstanceOf(ByteBufferDeserializer.class, deserializer);
 }

 @Test
 public void testFindFromStringDeserializer() throws IOException {
 Class<?> rawType = FromStringDeserializer.getType(String.class);
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, Std.DATE_TIME.getClass().getName());
 assertNotNull(deserializer);
 assertInstanceOf(Base.class, deserializer);

 Object deserialized = MAPPER.readValue(MAPPER.writeValueAsString("2022-01-01T00:00:00Z"), new TypeReference<Object>() {});
 assertNotNull(deserialized);
 }
}