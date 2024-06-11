package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ClassNameIdResolverTest {

 private ObjectMapper objectMapper;
 private ClassNameIdResolver classResolver;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 classResolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
 }

 @Test
 public void idFromValueTest() {
 String testString = "java.util.ArrayList";
 Object value = new ArrayList<>();
 assertEquals(testString, classResolver.idFromValue(value));
 }

 @Test
 public void idFromValueAndTypeTest() {
 String testString = "java.util.HashMap";
 Class<?> type = HashMap.class;
 Object value = new HashMap<>();
 assertEquals(testString, classResolver.idFromValueAndType(value, type));
 }

 @Test
 public void typeFromIdTest() {
 String testId = "java.util.ArrayList";
 JavaType javaType = classResolver.typeFromId(objectMapper.getDeserializationContext(), testId);
 assertEquals(ArrayList.class, javaType.getRawClass());
 }

 @Test
 public void invalidTypeIdTest() {
 String testId = "invalidTypeId";
 JavaType javaType = classResolver.typeFromId(objectMapper.getDeserializationContext(), testId);
 assertNull(javaType);
 }

 @Test
 public void genericTypeTest() {
 TypeReference<List<Integer>> typeRef = new TypeReference<List<Integer>>() {};
 String testId = typeRef.getTypeId();
 JavaType javaType = classResolver.typeFromId(objectMapper.getDeserializationContext(), testId);
 assertEquals(ArrayList.class, javaType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void invalidTypeIdFormatTest() {
 String testId = "invalidTypeIdFormat";
 JavaType javaType = classResolver.typeFromId(objectMapper.getDeserializationContext(), testId);
 }
}