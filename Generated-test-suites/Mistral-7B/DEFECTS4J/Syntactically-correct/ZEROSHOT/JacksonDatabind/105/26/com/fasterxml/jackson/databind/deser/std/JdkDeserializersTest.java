package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.base.ClassIdResolver;
import com.fasterxml.jackson.core.base.ClassIdResolverBase;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Junit 4 test file for JdkDeserializers class.
 */
public class JdkDeserializersTest {

 private static ObjectMapper mapper;

 @BeforeClass
 public static void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testFindDeserializerForUUID() throws IOException {
 Class<?> rawType = UUID.class;
 String clsName = UUID.class.getName();
 JsonDeserializer<?> expected = new UUIDDeserializer();
 JsonDeserializer<?> actual = JdkDeserializers.find(rawType, clsName);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindDeserializerForAtomicBoolean() throws IOException {
 Class<?> rawType = AtomicBoolean.class;
 String clsName = AtomicBoolean.class.getName();
 JsonDeserializer<?> expected = new AtomicBooleanDeserializer();
 JsonDeserializer<?> actual = JdkDeserializers.find(rawType, clsName);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindDeserializerForStackTraceElement() throws IOException {
 Class<?> rawType = StackTraceElement.class;
 String clsName = StackTraceElement.class.getName();
 JsonDeserializer<?> expected = new StackTraceElementDeserializer();
 JsonDeserializer<?> actual = JdkDeserializers.find(rawType, clsName);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindDeserializerForByteBuffer() throws IOException {
 Class<?> rawType = ByteBuffer.class;
 String clsName = ByteBuffer.class.getName();
 JsonDeserializer<?> expected = new ByteBufferDeserializer();
 JsonDeserializer<?> actual = JdkDeserializers.find(rawType, clsName);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindDeserializerForUnknownType() throws IOException {
 Class<?> rawType = Integer.class;
 String clsName = UUID.class.getName();
 JsonDeserializer<?> actual = JdkDeserializers.find(rawType, clsName);
 assertNull(actual);
 }

 @Test
 public void testFindDeserializerForFromStringDeserializer() throws IOException {
 Class<?> rawType = String.class;
 String clsName = "java.util.Date";
 JsonDeserializer<?> expected = new BeanDeserializer.Std(mapper.findValueType(
 ClassIdResolver.constructClassId(ClassIdResolverBase.DEFAULT_CLASS_ID, "java.util.Date")));
 JsonDeserializer<?> actual = JdkDeserializers.find(rawType, clsName);
 assertEquals(expected.getClass(), actual.getClass());
 }
}