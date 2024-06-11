package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.AtomicBoolean;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.deser.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.AtomicBooleanDeserializer;
import com.fasterxml.jackson.databind.deser.std.ByteBufferDeserializer;
import com.fasterxml.jackson.databind.deser.std.StackTraceElementDeserializer;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;

public class JdkDeserializersTest {

 private ObjectMapper objectMapper;

 @BeforeClass
 public static void initOnce() {
 JdkDeserializers.find(UUID.class, "java.util.UUID");
 JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean");
 JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement");
 JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer");
 }

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testFindDeserializerForUUID() throws IOException {
 JsonDeserializer<UUID> deserializer = JdkDeserializers.find(UUID.class, "java.util.UUID");
 assertNotNull(deserializer);
 assertInstanceOf(UUIDDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializerForAtomicBoolean() throws IOException {
 JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean");
 assertNotNull(deserializer);
 assertInstanceOf(AtomicBooleanDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializerForStackTraceElement() throws IOException {
 JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement");
 assertNotNull(deserializer);
 assertInstanceOf(StackTraceElementDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializerForByteBuffer() throws IOException {
 JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer");
 assertNotNull(deserializer);
 assertInstanceOf(ByteBufferDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializerForUnknownClass() throws IOException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(Collections.class, "java.util.Collections");
 assertNull(deserializer);
 }

 @Test
 public void testFindDeserializerForFromStringDeserializer() throws IOException {
 Class<?> stringEnumClass = Enum.class.getComponentType();
 JsonDeserializer<Enum> deserializer = FromStringDeserializer.findDeserializer(stringEnumClass);
 JsonDeserializer<?> jdkDeserializer = JdkDeserializers.find(stringEnumClass, stringEnumClass.getName());
 assertNotNull(deserializer);
 assertNotSame(deserializer, jdkDeserializer);
 }

 @Test
 public void testFindDeserializerForByteBufferWithArgument() throws IOException {
 Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
 TypeDeserializer deserializer = JdkDeserializers.find(byteBufferClass, byteBufferClass.getName());
 assertNotNull(deserializer);
 assertInstanceOf(ByteBufferDeserializer.class, deserializer);
 }
}