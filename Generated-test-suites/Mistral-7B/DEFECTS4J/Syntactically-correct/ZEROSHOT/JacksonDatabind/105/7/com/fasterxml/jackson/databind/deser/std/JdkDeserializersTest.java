package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonDeserializer.EmptyContextTracker;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * Test file for {@link JdkDeserializers}.
 */
public class JdkDeserializersTest {

 private static ObjectMapper mapper;

 @BeforeClass
 public static void setup() {
 mapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(UUID.class, new UUIDDeserializer());
 module.addDeserializer(AtomicBoolean.class, new AtomicBooleanDeserializer());
 module.addDeserializer(StackTraceElement.class, new StackTraceElementDeserializer());
 module.addDeserializer(ByteBuffer.class, new ByteBufferDeserializer());
 mapper.registerModule(module);
 }

 @Test
 public void testFindDeserializerForUUID() throws JsonProcessingException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
 assertNotNull(deserializer);
 assertInstanceOf(UUIDDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializerForAtomicBoolean() throws JsonProcessingException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
 assertNotNull(deserializer);
 assertInstanceOf(AtomicBooleanDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializerForStackTraceElement() throws JsonProcessingException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(StackTraceElement[].class, StackTraceElement.class.getName());
 assertNotNull(deserializer);
 assertInstanceOf(StackTraceElementDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializerForByteBuffer() throws JsonProcessingException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
 assertNotNull(deserializer);
 assertInstanceOf(ByteBufferDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializerForUnknownType() {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(String.class, "String.class");
 assertNull(deserializer);
 }

 @Test
 public void testFindDeserializerForFromStringDeserializer() throws JsonProcessingException {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(int[].class, "[I.class".substring(1));
 assertNotNull(deserializer);
 assertInstanceOf(FromStringDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserializerForEmptyContextTracker() {
 Class<?> emptyContextTrackerType = EmptyContextTracker.class;
 JsonDeserializer<?> deserializer = JdkDeserializers.find(emptyContextTrackerType, emptyContextTrackerType.getName());
 assertNotNull(deserializer);
 assertInstanceOf(EmptyContextTracker.class, deserializer);
 }
}