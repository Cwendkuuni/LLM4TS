package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.FromStringDeserializer.FromStringSerializer;
import com.fasterxml.jackson.databind.deser.std.*;

public class JdkDeserializersTest {

 private static ObjectMapper mapper;
 static {
 mapper = new ObjectMapper();
 }

 @org.junit.BeforeClass
 public static void setup() {
 // setup ObjectMapper for testing
 mapper.registerModule(new SimpleModule().addDeserializers(JdkDeserializers.class));
 }

 @org.junit.Test
 public void testFindNull() {
 Class<?> rawType = null;
 String clsName = "java.lang.String";
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNull(deserializer);
 }

 @org.junit.Test
 public void testFindString() {
 Class<?> rawType = String.class;
 String clsName = "java.lang.String";
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertInstanceOf(FromStringDeserializer.class, deserializer);
 }

 @org.junit.Test
 public void testFindUuid() throws JsonProcessingException {
 Class<?> rawType = UUID.class;
 String clsName = "java.util.UUID";
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertInstanceOf(UUIDDeserializer.class, deserializer);
 }

 @org.junit.Test
 public void testFindAtomicBoolean() {
 Class<?> rawType = AtomicBoolean.class;
 String clsName = "java.util.concurrent.atomic.AtomicBoolean";
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertInstanceOf(AtomicBooleanDeserializer.class, deserializer);
 }

 @org.junit.Test
 public void testFindStackTraceElement() {
 Class<?> rawType = StackTraceElement.class;
 String clsName = "java.lang.StackTraceElement";
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertInstanceOf(StackTraceElementDeserializer.class, deserializer);
 }

 @org.junit.Test
 public void testFindByteBuffer() {
 Class<?> rawType = ByteBuffer.class;
 String clsName = "java.nio.ByteBuffer";
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertInstanceOf(ByteBufferDeserializer.class, deserializer);
 }

 @org.junit.Test
 public void testFindUnknownDeserializer() {
 Class<?> rawType = Integer.class;
 String clsName = "java.lang.UUID";
 JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
 assertNull(deserializer);
 }
}