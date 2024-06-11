package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

 @Test
 public void testAddAll() {
 Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
 NumberSerializers.addAll(allDeserializers);

 assertEquals(7, allDeserializers.size());

 Class<?>[] intTypes = {Integer.class, Integer.TYPE};
 Class<?>[] longTypes = {Long.class, Long.TYPE};
 Class<?>[] byteShortTypes = {Byte.class, Byte.TYPE, Short.class, Short.TYPE};
 Class<?>[] doubleFloatTypes = {Double.class, Double.TYPE, Float.class, Float.TYPE};

 for (Class<?> type : intTypes) {
 String className = type.getName();
 JsonSerializer<?> serializer = allDeserializers.get(className);
 assertNotNull(serializer);
 assertInstanceOf(NumberSerializer.class, serializer);
 }

 for (Class<?> type : longTypes) {
 String className = type.getName();
 JsonSerializer<?> serializer = allDeserializers.get(className);
 assertNotNull(serializer);
 assertInstanceOf(NumberSerializer.class, serializer);
 }

 for (Class<?> type : byteShortTypes) {
 String className = type.getName();
 JsonSerializer<?> serializer = allDeserializers.get(className);
 assertNotNull(serializer);
 assertInstanceOf(IntLikeSerializer.class, serializer);
 }

 for (Class<?> type : doubleFloatTypes) {
 String className = type.getName();
 JsonSerializer<?> serializer = allDeserializers.get(className);
 assertNotNull(serializer);
 assertInstanceOf(NumberSerializer.class, serializer);
 }
 }

 // Test Base class
 @Test
 public void testBaseClass() {
 Base<Object> base = new ShortSerializer();
 assertEquals(JsonParser.NumberType.INT, base.getNumberType());
 assertEquals("number", base.getSchemaType());
 assertTrue(base._isInt);
 }

 // Test IntegerSerializer
 @Test
 public void testIntegerSerializer() {
 IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
 assertEquals(Integer.class, integerSerializer.handledType());
 }

 // Test IntLikeSerializer
 @Test
 public void testIntLikeSerializer() {
 IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
 assertEquals(Number.class, intLikeSerializer.handledType());
 }

 // Test JsonFormatVisitorWrapper
 @Test
 public void testJsonFormatVisitorWrapper() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 JsonSerializer<Object> intSerializer = NumberSerializers.IntegerSerializer(Integer.class);
 module.addSerializer(Integer.class, intSerializer);
 mapper.registerModule(module);

 JsonFormat format = new JsonFormat();
 format.setShape(JsonFormat.Shape.NUMBER);
 format.setNumberFormat(new NumberFormat("0.##E0"));

 JsonSerializer<Object> serializer = mapper.findValueSerializer(Double.class, null);
 JsonFormatVisitorWrapper wrapper = new JsonFormatVisitorWrapper(mapper.getSerializationConfig(), null);
 serializer.acceptJsonFormatVisitor(wrapper, null);
 assertNull(wrapper.getIntFormat());

 format.setShape(JsonFormat.Shape.STRING);
 serializer = mapper.findValueSerializer(Integer.class, null);
 serializer.acceptJsonFormatVisitor(wrapper, null);
 assertSame(ToStringSerializer.instance, wrapper.getStringSerializer());

 format.setShape(JsonFormat.Shape.NUMBER);
 serializer = mapper.findValueSerializer(Long.class, null);
 serializer.acceptJsonFormatVisitor(wrapper, null);
 assertNull(wrapper.getIntFormat());

 format.setShape(JsonFormat.Shape.NUMBER);
 serializer = mapper.findValueSerializer(Integer.class, null);
 serializer.acceptJsonFormatVisitor(wrapper, null);
 assertSame(NumberSerializers.IntegerSerializer(Integer.class), wrapper.getIntSerializer());
 }
}