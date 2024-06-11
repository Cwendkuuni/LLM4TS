package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.base.ClassIdResolver;
import com.fasterxml.jackson.core.base.ClassIdResolverBase;
import com.fasterxml.jackson.core.base.JsonLocation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.*; // Import all necessary deserializers for testing

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.fasterxml.jackson.databind.JsonMapping.Builder.stdSerializer;

/**
 * Test class for JdkDeserializers.
 */
public class JdkDeserializersTest {

 private static ObjectMapper objectMapper;

 @BeforeClass
 public static void setup() throws Exception {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testFindDeserializerExists() {
 Class<?>[] types = new Class<?>[] { UUID.class, AtomicBoolean.class, StackTraceElement.class, ByteBuffer.class };
 String[] className = { UUID.class.getName(), AtomicBoolean.class.getName(), StackTraceElement.class.getName(), ByteBuffer.class.getName() };
 for (int i = 0; i < types.length; i++) {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(types[i], className[i]);
 assertNotNull(deserializer);
 }
 }

 @Test
 public void testFindDeserializerNull() {
 String unknownClassName = "com.unknown.UnknownClass";
 JsonDeserializer<?> deserializer = JdkDeserializers.find(Object.class, unknownClassName);
 assertNull(deserializer);
 }

 @Test
 public void testFindDeserializerFromStringDeserializer() throws Exception {
 Class<?> stringEnumClass = String.class;
 TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {};
 JsonDeserializer<List<String>> stringEnumDeserializer = FromStringDeserializer.findDeserializer(stringEnumClass);
 assertNotNull(stringEnumDeserializer);

 JsonDeserializer<?> deserializer = JdkDeserializers.find(List.class, stringEnumClass.getName());
 assertEquals(stringEnumDeserializer, deserializer);
 }

 @Test
 public void testFindDeserializerUUID() {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
 assertInstanceOf(UUIDDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserialatorAtomicBoolean() {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
 assertInstanceOf(AtomicBooleanDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserialatorStackTraceElement() {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
 assertInstanceOf(StackTraceElementDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserialatorByteBuffer() {
 JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
 assertInstanceOf(ByteBufferDeserializer.class, deserializer);
 }

 @Test
 public void testFindDeserialatorNotFound() {
 Class<?> notFoundClass = int[].class;
 JsonDeserializer<?> deserializer = JdkDeserializers.find(notFoundClass, notFoundClass.getName());
 assertNull(deserializer);
 }

 @Test
 public void testFindDeserialatorWithCustomDeserializer() throws Exception {
 // Test that JdkDeserializers finds previously registered deserializer
 JsonSerializer<List<Integer>> intListSerializer = stdSerializer(new TypeReference<List<Integer>>() {});
 ObjectMapper objectMapperWithCustomSerializer = new ObjectMapper();
 objectMapperWithCustomSerializer.registerSerializer(List.class, intListSerializer);

 ClassIdResolver idResolver = new ClassIdResolverBase() {
 @Override
 public Class<?> findClass(int id, boolean unchecked) throws IOException {
 return int[].class;
 }
 };
 ObjectReader reader = objectMapperWithCustomSerializer.readerFor(new TypeReference<List<Integer>>() {});
 reader.setIdEventHandler(new JsonReader.DefaultIdEventHandler(idResolver));

 JsonDeserializer<?> deserializer = JdkDeserializers.find(List.class, int[].class.getName());
 assertNull(deserializer);
 }
}