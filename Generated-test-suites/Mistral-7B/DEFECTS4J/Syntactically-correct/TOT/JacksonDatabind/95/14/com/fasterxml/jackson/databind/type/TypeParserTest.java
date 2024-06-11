package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {

 private TypeFactory typeFactory = new TypeFactory();
 private TypeParser typeParser;

 @Test
 public void testParseSimpleType() {
 typeParser = new TypeParser(typeFactory);
 JavaType javaType = typeParser.parse("int");
 assertEquals(Integer.TYPE, javaType.getRawClass());
 }

 @Test
 public void testParseClassType() {
 typeParser = new TypeParser(typeFactory);
 JavaType javaType = typeParser.parse("java.lang.String");
 assertEquals(String.class, javaType.getRawClass());
 }

 @Test
 public void testParseArrayType() {
 typeParser = new TypeParser(typeFactory);
 JavaType javaType = typeParser.parse("int[]");
 assertEquals(int[].class, javaType.getRawClass());
 }

 @Test
 public void testParseListType() {
 typeParser = new TypeParser(typeFactory);
 JavaType javaType = typeParser.parse("java.util.List<java.lang.String>");
 assertEquals(new TypeReference<List<String>>(){}.getType(), javaType.getRawType());
 }

 @Test
 public void testParseNestedGenericsType() {
 typeParser = new TypeParser(typeFactory);
 JavaType javaType = typeParser.parse("java.util.List<java.util.List<java.lang.String>>");
 assertEquals(new TypeReference<List<List<String>>>() {}.getType(), javaType.getRawType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() {
 typeParser = new TypeParser(typeFactory);
 String invalidType = "java.util.List<int[]>";
 JavaType javaType = null;
 try {
 javaType = typeParser.parse(invalidType);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertEquals("Failed to parse type 'java.util.List<int[]>(remaining: ')': Unexpected token '['", e.getMessage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnclosedType() {
 typeParser = new TypeParser(typeFactory);
 String unclosedType = "java.util.List<java.lang.String>";
 JavaType javaType = null;
 try {
 javaType = typeParser.parse(unclosedType);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertEquals("Failed to parse type 'java.util.List<java.lang.String>(remaining: )': Unexpected end-of-string", e.getMessage());

 // push back the '>' token and try again
 typeParser.parseType(typeParser.new MyTokenizer(unclosedType));
 javaType = typeParser.parseType(typeParser.new MyTokenizer(">"));
 assertEquals(new TypeReference<List<String>>(){}.getType(), javaType.getRawType());
 }
}