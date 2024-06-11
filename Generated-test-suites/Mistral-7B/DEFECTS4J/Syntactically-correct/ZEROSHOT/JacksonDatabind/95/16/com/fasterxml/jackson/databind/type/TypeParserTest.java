package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeParserTest {

 private TypeFactory typeFactory;
 private TypeParser typeParser;

 @Before
 public void setup() {
 typeFactory = new TypeFactory();
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testParseSimpleType() throws Exception {
 String canonical = "java.lang.String";
 JavaType javaType = typeParser.parse(canonical);
 assertEquals(String.class, javaType.getRawClass());
 }

 @Test
 public void testParseListType() throws Exception {
 String canonical = "java.util.List<java.lang.Integer>";
 JavaType javaType = typeParser.parse(canonical);
 TypeBindings typeBindings = javaType.getTypeBindings();
 assertEquals(List.class, typeBindings.getRawClass());
 assertEquals(Integer.class, typeBindings.getTypeArguments().get(0).getRawClass());
 }

 @Test
 public void testParseArrayType() throws Exception {
 String canonical = "int[]";
 JavaType javaType = typeParser.parse(canonical);
 assertEquals(int[].class, javaType.getRawClass());
 }

 @Test
 public void testParseMapType() throws Exception {
 String canonical = "java.util.Map<java.lang.String, java.lang.Integer>";
 JavaType javaType = typeParser.parse(canonical);
 TypeBindings typeBindings = javaType.getTypeBindings();
 assertEquals(Map.class, typeBindings.getRawClass());
 TypeBindings mapKeyTypeBindings = typeBindings.getTypeArguments().get(0).getTypeBindings();
 assertEquals(String.class, mapKeyTypeBindings.getRawClass());
 TypeBindings mapValueTypeBindings = typeBindings.getTypeArguments().get(1).getTypeBindings();
 assertEquals(Integer.class, mapValueTypeBindings.getRawClass());
 }

 @Test
 public void testParseGenericTypeWithCustomTypeFactory() throws Exception {
 TypeFactory customTypeFactory = new TypeFactory() {
 @Override
 public JavaType _fromClass(Class<?> raw, Class<?> implementation, TypeBindings typeBindings) {
 if (List.class.isAssignableFrom(raw)) {
 return typeFactory._fromClass(raw, List.class, typeBindings);
 }
 return super._fromClass(raw, implementation, typeBindings);
 }
 };
 typeParser = new TypeParser(customTypeFactory);
 String canonical = "java.util.List<java.lang.String>";
 JavaType javaType = typeParser.parse(canonical);
 TypeBindings typeBindings = javaType.getTypeBindings();
 assertEquals(List.class, typeBindings.getRawClass());
 assertEquals(String.class, typeBindings.getTypeArguments().get(0).getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() throws Exception {
 String canonical = "java.util.List<java.lang.String>abc";
 typeParser.parse(canonical);
 fail("Unexpected exception not thrown.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnexpectedToken() throws Exception {
 String canonical = "java.util.List<java.lang.String> <abc";
 typeParser.parse(canonical);
 fail("Unexpected exception not thrown.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingGenericArguments() throws Exception {
 String canonical = "java.util.List<>";
 typeParser.parse(canonical);
 fail("Unexpected exception not thrown.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidTokenInGenericArguments() throws Exception {
 String canonical = "java.util.List<java.lang.String> <abc, 123>";
 typeParser.parse(canonical);
 fail("Unexpected exception not thrown.");
 }

 @Test
 public void testParseComplexType() throws Exception {
 String canonical = "java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>";
 JavaType javaType = typeParser.parse(canonical);
 TypeBindings mapTypeBindings = javaType.getTypeBindings();
 assertEquals(Map.class, mapTypeBindings.getRawClass());
 TypeBindings mapKeyTypeBindings = mapTypeBindings.getTypeArguments().get(0).getTypeBindings();
 assertEquals(String.class, mapKeyTypeBindings.getRawClass());
 TypeBindings mapValueTypeBindings = mapTypeBindings.getTypeArguments().get(1).getTypeBindings();
 TypeBindings listTypeBindings = mapValueTypeBindings.getTypeArguments().get(0).getTypeBindings();
 assertEquals(List.class, listTypeBindings.getRawClass());
 assertEquals(Integer.class, listTypeBindings.getTypeArguments().get(0).getRawClass());
 }

 @Test
 public void testParseTypeWithGenericTypeReference() throws Exception {
 JavaType javaType = typeFactory.constructType(new TypeReference<List<List<Integer>>>(){}.getType());
 String canonical = javaType.canonicalName();
 JavaType parsedJavaType = typeParser.parse(canonical);
 TypeBindings parsedTypeBindings = parsedJavaType.getTypeBindings();
 TypeBindings listTypeBindings = parsedTypeBindings.getTypeArguments().get(0).getTypeBindings();
 assertEquals(List.class, listTypeBindings.getRawClass());
 TypeBindings innerListTypeBindings = listTypeBindings.getTypeArguments().get(0).getTypeBindings();
 assertEquals(List.class, innerListTypeBindings.getRawClass());
 TypeBindings innerIntegerTypeBindings = innerListTypeBindings.getTypeArguments().get(0).getTypeBindings();
 assertEquals(Integer.class, innerIntegerTypeBindings.getRawClass());
 }
}