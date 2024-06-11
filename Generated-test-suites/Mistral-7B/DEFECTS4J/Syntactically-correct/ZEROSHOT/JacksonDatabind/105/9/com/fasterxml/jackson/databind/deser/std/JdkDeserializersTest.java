package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import org.junit.Test;

/**
 * Junit 4 test file for JdkDeserializers class.
 */
public class JdkDeserializersTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testFindDeserializerForUUID() throws IOException {
 Class<UUID> uuidClass = UUID.class;
 String clsName = UUID.class.getName();
 JsonDeserializer<UUID> deserializer = JdkDeserializers.find(uuidClass, clsName);
 assertNotNull(deserializer);
 assertInstanceOf(UUIDDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializerForAtomicBoolean() throws IOException {
 Class<AtomicBoolean> atomicBooleanClass = AtomicBoolean.class;
 String clsName = AtomicBoolean.class.getName();
 JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(atomicBooleanClass, clsName);
 assertNotNull(deserializer);
 assertInstanceOf(AtomicBooleanDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializerForStackTraceElement() throws IOException {
 Class<StackTraceElement> stackTraceElementClass = StackTraceElement.class;
 String clsName = StackTraceElement.class.getName();
 JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(stackTraceElementClass, clsName);
 assertNotNull(deserializer);
 assertInstanceOf(StackTraceElementDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializerForByteBuffer() throws IOException {
 Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
 String clsName = ByteBuffer.class.getName();
 JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(byteBufferClass, clsName);
 assertNotNull(deserializer);
 assertInstanceOf(ByteBufferDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializerForUnknownClass() throws IOException {
 Class<String> stringClass = String.class;
 String clsName = Object.class.getName();
 JsonDeserializer<String> deserializer = JdkDeserializers.find(stringClass, clsName);
 assertNull(deserializer);
 }

 @Test
 public void testFindDeserializerForFromStringDeserializer() throws IOException {
 Class<List<Integer>> listOfIntClass = new TypeReference<List<Integer>>(){}.getType();
 JsonDeserializer<List<Integer>> deserializer = JdkDeserializers.find(listOfIntClass, "java.util.List");
 assertNotNull(deserializer);
 assertInstanceOf(FromStringDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializerForArrayType() throws IOException {
 Class<int[]> intArrayClass = int[].class;
 String clsName = "[I";
 JsonDeserializer<int[]> deserializer = JdkDeserializers.find(intArrayClass, clsName);
 assertNotNull(deserializer);
 assertInstanceOf(BeanDeserializer.class, deserializer);
 }
}