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

public class TypeParserTest {

 private TypeFactory typeFactory = new TypeFactoryBuilder().build();
 private TypeParser typeParser = new TypeParser(typeFactory);

 @Test
 public void testParseSimpleType() {
 String canonicalType = "int";
 JavaType javaType = typeParser.parse(canonicalType);
 assertEquals(Integer.TYPE, javaType.getRawClass());
 }

 @Test
 public void testParseClassType() {
 String canonicalType = "java.lang.String";
 JavaType javaType = typeParser.parse(canonicalType);
 assertEquals(String.class, javaType.getRawClass());
 }

 @Test
 public void testParseListType() {
 String canonicalType = "java.util.List<java.lang.Integer>";
 JavaType javaType = typeParser.parse(canonicalType);
 TypeReference<List<Integer>> typeRef = new TypeReference<List<Integer>>() {};
 assertEquals(typeRef.getType(), javaType.getType());
 }

 @Test
 public void testParseNestedGenericsType() {
 String canonicalType = "java.util.List<java.util.List<java.lang.Integer>>";
 JavaType javaType = typeParser.parse(canonicalType);
 TypeReference<List<List<Integer>>> typeRef = new TypeReference<List<List<Integer>>>() {};
 assertEquals(typeRef.getType(), javaType.getType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() {
 String canonicalType = "java.util.List<java.lang.Integer<java.lang.String>";
 typeParser.parse(canonicalType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingClosingBracket() {
 String canonicalType = "java.util.List<java.lang.Integer<java.lang.String";
 typeParser.parse(canonicalType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnexpectedCharacter() {
 String canonicalType = "java.util.List<java.lang.Integer<java.lang.String#";
 typeParser.parse(canonicalType);
 }
}