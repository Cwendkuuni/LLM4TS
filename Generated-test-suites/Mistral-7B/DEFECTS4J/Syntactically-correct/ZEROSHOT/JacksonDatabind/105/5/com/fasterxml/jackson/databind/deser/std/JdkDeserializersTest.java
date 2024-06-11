package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JdkDeserializersTest {

 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testFind_UUID() throws IOException {
 Class<UUID> uuidClass = UUID.class;
 String clsName = UUID.class.getName();
 JsonDeserializer<UUID> deserializer = JdkDeserializers.find(uuidClass, clsName);
 assertNotNull(deserializer);
 assertInstanceOf(UUIDDeserializer.class, deserializer);
 }

 @Test
 public void testFind_AtomicBoolean() throws IOException {
 Class<AtomicBoolean> atomicBooleanClass = AtomicBoolean.class;
 String clsName = AtomicBoolean.class.getName();
 JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(atomicBooleanClass, clsName);
 assertNotNull(deserializer);
 assertInstanceOf(AtomicBooleanDeserializer.class, deserializer);
 }

 @Test
 public void testFind_StackTraceElement() throws IOException {
 Class<StackTraceElement> stackTraceElementClass = StackTraceElement.class;
 String clsName = StackTraceElement.class.getName();
 JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(stackTraceElementClass, clsName);
 assertNotNull(deserializer);
 assertInstanceOf(StackTraceElementDeserializer.class, deserializer);
 }

 @Test
 public void testFind_ByteBuffer() throws IOException {
 Class<ByteBuffer> byteBufferClass = ByteBuffer.class;
 String clsName = ByteBuffer.class.getName();
 JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(byteBufferClass, clsName);
 assertNotNull(deserializer);
 assertInstanceOf(ByteBufferDeserializer.class, deserializer);
 }

 @Test
 public void testFind_Other() throws IOException {
 Class<String> stringClass = String.class;
 String clsName = String.class.getName();
 JsonDeserializer<String> deserializer = JdkDeserializers.find(stringClass, clsName);
 assertNull(deserializer);
 }

 @Test
 public void testFind_FromStringDeserializer() throws Exception {
 Class<Integer> integerClass = Integer.class;
 String clsName = Integer.class.getName();
 JsonDeserializer<Integer> deserializer = mock(JsonDeserializer.class);
 when(FromStringDeserializer.findDeserializer(integerClass)).thenReturn(deserializer);
 JsonDeserializer<Integer> actualDeserializer = JdkDeserializers.find(integerClass, clsName);
 assertSame(deserializer, actualDeserializer);
 }

 @Test
 public void testFind_Null() throws IOException {
 Class<Object> objectClass = Object.class;
 String clsName = Object.class.getName();
 JsonDeserializer<Object> deserializer = JdkDeserializers.find(objectClass, clsName);
 assertNull(deserializer);
 }
}