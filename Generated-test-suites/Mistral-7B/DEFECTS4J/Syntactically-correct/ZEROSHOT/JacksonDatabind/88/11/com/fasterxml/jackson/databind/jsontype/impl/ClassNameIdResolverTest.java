package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

 private static final TypeFactory typeFactory = new TypeFactoryBuilder()
         .build();
 private static final JavaType baseType = typeFactory.constructType(ArrayList.class);
 private static final ClassNameIdResolver classResolver = new ClassNameIdResolver(baseType, typeFactory);

 @org.junit.BeforeClass
 public static void setup() {
 }

 @org.junit.Test
 public void testIdFromValue_Class() throws IOException {
 final String expected = "java.util.ArrayList";
 Object value = new ArrayList<>();
 String actual = classResolver.idFromValue(value);
 assertEquals(expected, actual);
 }

 @org.junit.Test
 public void testIdFromValue_String() throws IOException {
 final String expected = "java.lang.String";
 Object value = "Test String";
 String actual = classResolver.idFromValue(value);
 assertEquals(expected, actual);
 }

 @org.junit.Test
 public void testIdFromValue_Enum() throws IOException {
 final EnumSet<DaysOfWeek> days = EnumSet.allOf(DaysOfWeek.class);
 final String expected = "java.util.EnumSet";
 String actual = classResolver.idFromValue(days);
 assertEquals(expected, actual);
 }

 @org.junit.Test
 public void testIdFromValueAndType() throws IOException {
 final String value = "Test String";
 final String expected = "java.lang.String";
 final String actual = classResolver.idFromValueAndType(value, String.class);
 assertEquals(expected, actual);
 }

 @org.junit.Test
 public void testTypeFromId_ValidId() throws IOException {
 final String id = "java.util.ArrayList";
 JavaType expected = typeFactory.constructType(ArrayList.class);
 JavaType actual = classResolver.typeFromId(mockContext(), id);
 assertEquals(expected.getName(), actual.getName());
 }

 @org.junit.Test
 public void testTypeFromId_UnknownClass() throws IOException {
 final String id = "com.test.UnknownClass";
 JavaType actual = classResolver.typeFromId(mockContext(), id);
 assertNull(actual);
 }

 @org.junit.Test
 public void testTypeFromId_UnknownClassWithContext() throws IOException {
 final String id = "com.test.UnknownClass";
 JavaType actual = classResolver.typeFromId(mockContextWithHandler(), id);
 assertNotNull(actual);
 }

 @org.junit.Test
 public void testIdFromValueAndType_ComplexClassWithGenericType() throws IOException {
 final Map<DaysOfWeek, Integer> map = new HashMap<>();
 final String expected = "java.util.Map";
 Class<Map> mapClass = Map.class;
 String actual = classResolver.idFromValueAndType(map, mapClass.getTypeParameters()[0].getType());
 assertEquals(expected, actual);
 }

 @org.junit.Test
 public void testIdFromValueAndType_ComplexClassWithSuperClass() throws IOException {
 final List<MyOuterClass.InnerClass> list = new ArrayList<>();
 final String expected = "com.test.MyOuterClass$InnerClass";
 String actual = classResolver.idFromValueAndType(list, MyOuterClass.InnerClass.class);
 assertEquals(expected, actual);
 }

 private DatabindContext mockContext() {
 return new JacksonAnnotationIntrospector().findContextForIntrospection(new Object());
 }

 private DatabindContext mockContextWithHandler() {
 final ObjectMapper mapper = new ObjectMapper();
 final MyUnknownClassHandler handler = new MyUnknownClassHandler();
 mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
 mapper.addHandler(handler);
 return mapper.getFactory().findValueSerializerProvider().getSerializerProvider().getDeserializationContext();
 }

 static class MyUnknownClassHandler extends JsonDeserializer<Object> {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt)
 throws IOException {
 return new com.test.MyUnknownClass();
 }
 }

 static class MyOuterClass {
 static class InnerClass {
 }
 }

 static enum DaysOfWeek {
 SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
 }
}