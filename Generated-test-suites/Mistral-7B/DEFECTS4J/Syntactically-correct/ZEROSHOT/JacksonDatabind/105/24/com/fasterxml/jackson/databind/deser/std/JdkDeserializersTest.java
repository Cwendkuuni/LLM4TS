package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testFindDeserializerUuid() {
 Class<UUID> uuidClass = UUID.class;
 String clsName = UUID.class.getName();
 JsonDeserializer<UUID> deserializer = JdkDeserializers.find(uuidClass, clsName);
 assertNotNull(deserializer);
 assertInstanceOf(UUIDDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializerAtomicBoolean() {
 Class<AtomicBoolean> atomicBooleanClass = AtomicBoolean.class;
 String clsName = AtomicBoolean.class.getName();
 JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(atomicBooleanClass, clsName);
 assertNotNull(deserializer);
 assertInstanceOf(AtomicBooleanDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializerStackTraceElement() {
 Class<StackTraceElement> stackTraceElementClass = StackTraceElement.class;
 String clsName = StackTraceElement.class.getName();
 JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(stackTraceElementClass, clsName);
 assertNotNull(deserializer);
 assertInstanceOf(StackTraceElementDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializerByteBuffer() {
 Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
 String clsName = ByteBuffer.class.getName();
 JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(byteBufferClass, clsName);
 assertNotNull(deserializer);
 assertInstanceOf(ByteBufferDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializerUnknownClass() {
 Class<String> stringClass = String.class;
 String clsName = Object.class.getName();
 JsonDeserializer<String> deserializer = JdkDeserializers.find(stringClass, clsName);
 assertNull(deserializer);
 }

 @Test
 public void testFindDeserializerFromStringDeserializer() {
 Class<Integer> integerClass = Integer.class;
 String clsName = "java.lang.Integer"; // using fully qualified name to test fromStringDeserializer
 JsonDeserializer<Integer> deserializer = JdkDeserializers.find(integerClass, clsName);
 assertNotNull(deserializer);
 assertInstanceOf(FromStringDeserializer.class, deserializer.getClass());
 }
}