package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.AtomicBoolean;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;

public class JdkDeserializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testFind_UUID() throws IOException {
 Class<UUID> uuidClass = UUID.class;
 String clsName = uuidClass.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(uuidClass, clsName);
 assertNotNull(deserializer);
 assertInstanceOf(UUIDDeserializer.class, deserializer);
 }

 @Test
 public void testFind_AtomicBoolean() throws IOException {
 Class<AtomicBoolean> atomicBooleanClass = AtomicBoolean.class;
 String clsName = atomicBooleanClass.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(atomicBooleanClass, clsName);
 assertNotNull(deserializer);
 assertInstanceOf(AtomicBooleanDeserializer.class, deserializer);
 }

 @Test
 public void testFind_StackTraceElement() throws IOException {
 Class<StackTraceElement> stackTraceElementClass = StackTraceElement.class;
 String clsName = stackTraceElementClass.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(stackTraceElementClass, clsName);
 assertNotNull(deserializer);
 assertInstanceOf(StackTraceElementDeserializer.class, deserializer);
 }

 @Test
 public void testFind_ByteBuffer() throws IOException {
 Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
 String clsName = byteBufferClass.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(byteBufferClass, clsName);
 assertNotNull(deserializer);
 assertInstanceOf(ByteBufferDeserializer.class, deserializer);
 }

 @Test
 public void testFind_OtherClass() throws IOException {
 Class<String> stringClass = String.class;
 String clsName = stringClass.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(stringClass, clsName);
 assertNull(deserializer);
 }

 @Test
 public void testFind_FromStringDeserializer() throws IOException {
 Class<List<Integer>> listIntClass = new TypeReference<List<Integer>>(){}.getType();
 String clsName = List.class.getName();
 JsonDeserializer<?> deserializer = JdkDeserializers.find(listIntClass, clsName);
 assertNotNull(deserializer);
 assertInstanceOf(FromStringDeserializer.class, deserializer);
 }

 @Test
 public void testFind_NullInput() {
 Class<Object> objectClass = Object.class;
 String clsName = null;
 JsonDeserializer<?> deserializer = JdkDeserializers.find(objectClass, clsName);
 assertNull(deserializer);
 }

 @Test
 public void testFind_UnknownClass() {
 Class<Object> objectClass = Object.class;
 String clsName = "com.example.UnknownClass".replace(".", "/");
 JsonDeserializer<?> deserializer = JdkDeserializers.find(objectClass, clsName);
 assertNull(deserializer);
 }
}