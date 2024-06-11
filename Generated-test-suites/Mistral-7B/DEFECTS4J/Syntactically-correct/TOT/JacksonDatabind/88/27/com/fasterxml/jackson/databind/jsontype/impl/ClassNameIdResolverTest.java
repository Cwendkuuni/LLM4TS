package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClassNameIdResolverTest {

 private ObjectMapper objectMapper;
 private ClassNameIdResolver classNameIdResolver;

 @BeforeAll
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 classNameIdResolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
 }

 @Test
 public void testIdFromValueSimpleClass() throws Exception {
 Object simpleObject = new SimpleClass();
 String id = classNameIdResolver.idFromValue(simpleObject);
 JavaType javaType = classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), id);
 assertEquals(SimpleClass.class, javaType.getRawClass());
 }

 @Test
 public void testIdFromValueEnum() throws Exception {
 EnumTest enumTest = EnumTest.ONE;
 String id = classNameIdResolver.idFromValue(enumTest);
 JavaType javaType = classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), id);
 assertEquals(EnumTest.class, javaType.getRawClass());
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
 Object enumObject = new EnumTest("TWO");
 String id = classNameIdResolver.idFromValueAndType(enumObject, String.class);
 JavaType javaType = classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), id);
 assertEquals(EnumTest.class, javaType.getRawClass());
 }

 @Test
 public void testTypeFromIdSimpleClass() throws Exception {
 String id = "java.lang.String";
 JavaType javaType = classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), id);
 assertEquals(String.class, javaType.getRawClass());
 }

 @Test
 public void testTypeFromIdGenericClass() throws Exception {
 String id = "java.util.List<java.lang.Integer>";
 JavaType javaType = classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), id);
 assertEquals(java.util.List.class, javaType.getRawClass());
 }

 @Test
 public void testTypeFromIdEnum() throws Exception {
 String id = EnumTest.class.getName();
 JavaType javaType = classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), id);
 assertEquals(EnumTest.class, javaType.getRawClass());
 }

 @Test
 public void testTypeFromIdUnknownClass() throws Exception {
 String id = "com.unknown.UnknownClass";
 JavaType javaType = classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), id);
 assertNull(javaType);
 }

 @Test
 public void testTypeFromIdNullId() throws Exception {
 JavaType javaType = classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), null);
 assertNull(javaType);
 }
}