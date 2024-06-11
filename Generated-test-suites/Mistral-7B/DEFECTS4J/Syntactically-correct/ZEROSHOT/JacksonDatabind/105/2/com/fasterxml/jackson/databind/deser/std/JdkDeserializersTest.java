package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.node.TextNode;

public class JdkDeserializersTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @org.junit.BeforeClass
 static public void setup() {
 // Initialize the ObjectMapper
 objectMapper.registerModule(new StandardDeserializerProvider.Finder());
 }

 @org.junit.Test
 public void testFindUUIDDeserializer() throws IOException {
 Class<UUID> uuidClass = UUID.class;
 String clsName = UUID.class.getName();
 JsonDeserializer<UUID> deserializer = JdkDeserializers.find(uuidClass, clsName);
 assertNotNull(deserializer);
 assertTrue(deserializer instanceof UUIDDeserializer);
 }

 @org.junit.Test
 public void testFindStackTraceElementDeserializer() throws IOException {
 Class<StackTraceElement> stackTraceElementClass = StackTraceElement.class;
 String clsName = StackTraceElement.class.getName();
 JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(stackTraceElementClass, clsName);
 assertNotNull(deserializer);
 assertTrue(deserializer instanceof StackTraceElementDeserializer);
 }

 @org.junit.Test
 public void testFindAtomicBooleanDeserializer() throws IOException {
 Class<AtomicBoolean> atomicBooleanClass = AtomicBoolean.class;
 String clsName = AtomicBoolean.class.getName();
 JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(atomicBooleanClass, clsName);
 assertNotNull(deserializer);
 assertTrue(deserializer instanceof AtomicBooleanDeserializer);
 }

 @org.junit.Test
 public void testFindByteBufferDeserializer() throws IOException {
 Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
 String clsName = ByteBuffer.class.getName();
 JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(byteBufferClass, clsName);
 assertNotNull(deserializer);
 assertTrue(deserializer instanceof ByteBufferDeserializer);
 }

 @org.junit.Test
 public void testFindUnknownDeserializer() throws IOException {
 Class<Object> objectClass = Object.class;
 String clsName = Object.class.getName();
 JsonDeserializer<Object> deserializer = JdkDeserializers.find(objectClass, clsName);
 assertNull(deserializer);
 }

 @org.junit.Test
 public void testFindDeserializerFromString() throws IOException {
 Class<String> stringClass = String.class;
 JsonDeserializer<String> deserializer = objectMapper.findValueDeserializer(String.class, TextNode.class);
 assertNotNull(deserializer);
 assertEquals(FromStringDeserializer.class.getName(), deserializer.getClass().getName());
 }
}