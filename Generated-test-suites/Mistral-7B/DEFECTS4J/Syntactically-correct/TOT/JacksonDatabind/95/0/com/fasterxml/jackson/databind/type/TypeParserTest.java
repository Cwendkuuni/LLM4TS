package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;

import org.junit.Test;

public class TypeParserTest {

 private static TypeFactory factory = new TypeFactory();
 private static TypeParser parser = new TypeParser(factory);

 @Test
 public void testParseSimpleType() {
 String canonical = "int";
 JavaType type = parser.parse(canonical);
 assertEquals(Integer.TYPE, type.getRawClass());
 }

 @Test
 public void testParseClassType() {
 String canonical = "java.lang.String";
 JavaType type = parser.parse(canonical);
 assertEquals(String.class, type.getRawClass());
 }

 @Test
 public void testParseListType() {
 String canonical = "[Ljava.util.List;";
 JavaType type = parser.parse(canonical);
 TypeReference<List<?>> ref = new TypeReference<List<?>>() {};
 assertEquals(ref.getType(), type);
 }

 @Test
 public void testParseListWithGenericType() {
 String canonical = "[Ljava.util.ArrayList<java.lang.String>;";
 JavaType type = parser.parse(canonical);
 TypeReference<List<String>> ref = new TypeReference<List<String>>() {};
 assertEquals(ref.getType(), type);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() {
 String canonical = "int<>";
 JavaType type = parser.parse(canonical);
 fail("Expected IllegalArgumentException but got: " + type);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidToken() {
 String canonical = "int< #";
 MyTokenizer tokens = new MyTokenizer(canonical.trim());
 try {
 JavaType type = parser.parseType(tokens);
 fail("Expected IllegalArgumentException but got: " + type);
 } catch (IllegalArgumentException e) {
 assertEquals("Failed to parse type 'int< #' (remaining: ' #'): Unexpected token '#'", e.getMessage());
 }
 }

 @Test
 public void testPushBackToken() {
 MyTokenizer tokens = new MyTokenizer("int<,>");
 tokens.pushBack(",");
 assertEquals(",", tokens.nextToken());
 }
}