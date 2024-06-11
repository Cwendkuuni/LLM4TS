package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer.FromStringSerializer;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers.UUIDDeserializer;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers.AtomicBooleanDeserializer;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers.StackTraceElementDeserializer;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers.ByteBufferDeserializer;

@RunWith(MockitoJUnitRunner.class)
public class JdkDeserializersTest {

 private DeserializationContext context;
 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() throws Exception {
 context = Mockito.mock(DeserializationContext.class);
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testFindForNullRawType() throws Exception {
 assertNull(JdkDeserializers.find(null, "java.util.UUID"));
 }

 @Test
 public void testFindForUnknownClassName() throws Exception {
 assertNull(JdkDeserializers.find(String.class, "java.util.UUID"));
 }

 @Test
 public void testFindForKnownClassNameButWrongType() throws Exception {
 assertNull(JdkDeserializers.find(Integer.class, "java.util.UUID"));
 }

 @Test
 public void testFindForUuid() throws Exception {
 JsonDeserializer<UUID> uuidDeserializer = JdkDeserializers.find(UUID.class, "java.util.UUID");
 assertSame(UUIDDeserializer.class, uuidDeserializer.getClass());
 }

 @Test
 public void testFindForAtomicBoolean() throws Exception {
 JsonDeserializer<AtomicBoolean> atomicBooleanDeserializer =
 JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean");
 assertSame(AtomicBooleanDeserializer.class, atomicBooleanDeserializer.getClass());
 }

 @Test
 public void testFindForStackTraceElement() throws Exception {
 JsonDeserializer<StackTraceElement> stackTraceElementDeserializer =
 JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement");
 assertSame(StackTraceElementDeserializer.class, stackTraceElementDeserializer.getClass());
 }

 @Test
 public void testFindForByteBuffer() throws Exception {
 JsonDeserializer<ByteBuffer> byteBufferDeserializer =
 JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer");
 assertSame(ByteBufferDeserializer.class, byteBufferDeserializer.getClass());
 }

 @Test
 public void testFindForFromStringDeserializer() throws Exception {
 JsonDeserializer<?> fromStringDeserializer = JdkDeserializers.find(String.class, "com.fasterxml.jackson.databind.deser.std.FromStringDeserializer");
 assertNotNull(fromStringDeserializer);
 }

 @Test
 public void testFindForUnknownJdkClassInFromStringDeserializer() throws Exception {
 assertNull(JdkDeserializers.find(String.class, "java.foobar.Foo"));
 }

 @Test
 public void testFindForCustomClass() throws Exception {
 assertNull(JdkDeserializers.find(CustomClass.class, "java.lang.Object"));
 }

 @Test
 public void testFromStringDeserializerFindForCustomClass() throws Exception {
 CustomClass customClass = new CustomClass();
 FromStringSerializer serializer = Mockito.spy(FromStringSerializer.class);
 JsonDeserializer<CustomClass> deserializer =
 JdkDeserializers.FromStringDeserializer.findDeserializer(CustomClass.class);
 assertSame(CustomClassDeserializer.class, deserializer.getClass());
 Mockito.verify(serializer).createContext(context);
 Mockito.verify(serializer).createDeserializer(context, CustomClass.class);
 }

 private static class CustomClass {
 }
}