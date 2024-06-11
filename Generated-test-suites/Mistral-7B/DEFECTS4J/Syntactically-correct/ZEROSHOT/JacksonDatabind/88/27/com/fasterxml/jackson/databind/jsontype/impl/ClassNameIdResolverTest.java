package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ConstructorUtils;

public class ClassNameIdResolverTest {

 private TypeIdResolver classResolver;
 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() throws Exception {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), new TypeFactory());
 this.classResolver = resolver;
 this.objectMapper = new ObjectMapper();
 }

 @Test
 public void testgetIdFromValue() throws Exception {
 Object obj = new Object();
 String id = classResolver.idFromValue(obj);
 assertEquals("java.lang.Object", id);
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
 String id = classResolver.idFromValueAndType(new Object(), Object.class);
 assertEquals("java.lang.Object", id);
 }

 @Test
 public void testTypeFromIdNullContext() throws Exception {
 assertNull(classResolver.typeFromId(null, "java.lang.String"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTypeFromIdInvalidId() throws Exception {
 JavaType idType = objectMapper.getTypeFactory().constructType(Integer.TYPE);
 JavaType type = classResolver.typeFromId(mock(DatabindContext.class), "invalidId");
 assertNull(type);
 }

 @Test
 public void testTypeFromIdBasic() throws Exception {
 JavaType intType = objectMapper.getTypeFactory().constructType(Integer.TYPE);
 JavaType intTypeId = classResolver.typeFromId(mock(DatabindContext.class), intType.getId().getId());
 assertEquals(intType, intTypeId);
 }

 @Test
 public void testTypeFromIdCanonicalClassName() throws Exception {
 JavaType stringType = objectMapper.getTypeFactory().constructType(String.class);
 JavaType stringTypeId = classResolver.typeFromId(mock(DatabindContext.class), stringType.toCanonicalName());
 assertEquals(stringType, stringTypeId);
 }

 @Test
 public void testTypeFromIdGenericType() throws Exception {
 JavaType listType = objectMapper.getTypeFactory().constructType(List.class);
 JavaType listIntType = objectMapper.getTypeFactory().constructType(List.class, Integer.TYPE);
 JavaType listIntTypeId = classResolver.typeFromId(mock(DatabindContext.class), listIntType.getId().getId());
 assertEquals(listIntType, listIntTypeId);
 }

 @Test
 public void testTypeFromIdEnum() throws Exception {
 EnumSet<MyEnum> enumSet = EnumSet.allOf(MyEnum.class);
 JavaType enumSetType = objectMapper.getTypeFactory().constructCollectionType(EnumSet.class, MyEnum.class);
 JavaType enumSetTypeId = classResolver.typeFromId(mock(DatabindContext.class), enumSetType.getId().getId());
 assertEquals(enumSetType, enumSetTypeId);
 }

 @Test
 public void testTypeFromIdMap() throws Exception {
 Map<MyEnum, String> map = new HashMap<>();
 JavaType mapType = objectMapper.getTypeFactory().constructMapType(Map.class, MyEnum.class, String.class);
 JavaType mapTypeId = classResolver.typeFromId(mock(DatabindContext.class), mapType.getId().getId());
 assertEquals(mapType, mapTypeId);
 }

 @Test
 public void testTypeFromIdMapNested() throws Exception {
 Map<MyEnum, Map<String, Integer>> nestedMap = new HashMap<>();
 JavaType mapType = objectMapper.getTypeFactory().constructMapType(Map.class, MyEnum.class, Map.class);
 JavaType nestedMapType = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Integer.TYPE);
 JavaType mapTypeId = classResolver.typeFromId(mock(DatabindContext.class), mapType.getId().getId());
 JavaType nestedMapTypeId = classResolver.typeFromId(mock(DatabindContext.class), nestedMapType.getId().getId());
 JavaType expectedType = objectMapper.getTypeFactory().constructType(Map.class, MyEnum.class, Map.class);
 JavaType actualType = mapTypeId.getContentType();
 assertEquals(expectedType, actualType);
 JavaType nestedExpectedType = expectedType.getContentType();
 JavaType nestedActualType = nestedMapTypeId.getContentType();
 assertEquals(nestedExpectedType, nestedActualType);
 }

 @Test
 public void testTypeFromIdClassWithInnerClass() throws Exception {
 Class<Outer> outerClass = Outer.class;
 Class<Inner> innerClass = Inner.class;
 JavaType outerClassType = objectMapper.getTypeFactory().constructType(outerClass);
 JavaType innerClassType = outerClassType.getDeclaredMemberType("inner");
 JavaType outerClassTypeId = classResolver.typeFromId(mock(DatabindContext.class), outerClassType.getName());
 JavaType innerClassTypeId = classResolver.typeFromId(mock(DatabindContext.class), innerClassType.getName());
 JavaType expectedType = objectMapper.getTypeFactory().constructType(outerClass, innerClass);
 JavaType actualType = outerClassTypeId.getContentType();
 assertEquals(expectedType, actualType);
 JavaType innerExpectedType = expectedType.getContentType();
 JavaType innerActualType = innerClassTypeId.getContentType();
 assertEquals(innerExpectedType, innerActualType);
 }
}

class MyEnum {
 @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "name")
 enum NameableEnum {
 FOOBAR,
 BAZ;
}

 @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "enumClass")
 enum EnumWithClassAnnotation {
 FOO,
 BAR;
}
}

class Outer {
 private static class Inner {
 }
}