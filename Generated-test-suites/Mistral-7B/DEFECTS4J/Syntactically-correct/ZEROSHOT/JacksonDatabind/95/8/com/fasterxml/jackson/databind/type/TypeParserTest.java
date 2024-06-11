package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {

 private TypeFactory typeFactory;
 private TypeParser typeParser;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 typeFactory = new TypeFactory();
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testParseSimpleClass() throws Exception {
 String canonical = "java.lang.String";
 JavaType javaType = typeParser.parse(canonical);
 assertEquals(String.class, javaType.getRawClass());
 }

 @Test
 public void testParseClassWithGenerics() throws Exception {
 String canonical = "java.util.ArrayList<java.lang.Integer>";
 JavaType javaType = typeParser.parse(canonical);
 List<JavaType> typeArguments = javaType.getGenerics();
 assertEquals(1, typeArguments.size());
 JavaType intType = typeArguments.get(0);
 assertEquals(Integer.class, intType.getRawClass());
 }

 @Test
 public void testParseClassWithMultipleGenerics() throws Exception {
 String canonical = "java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>";
 JavaType javaType = typeParser.parse(canonical);
 List<JavaType> typeArguments = javaType.getGenerics();
 assertEquals(2, typeArguments.size());
 JavaType stringType = typeArguments.get(0);
 assertEquals(String.class, stringType.getRawClass());
 JavaType listType = typeArguments.get(1);
 List<JavaType> listTypeArguments = listType.getGenerics();
 assertEquals(1, listTypeArguments.size());
 JavaType integerType = listTypeArguments.get(0);
 assertEquals(Integer.class, integerType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() throws Exception {
 String canonical = "java.util.ArrayList<java.lang.String>abc";
 typeParser.parse(canonical);
 fail("Expected IllegalArgumentException but no exception was thrown.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingClosingBracket() throws Exception {
 String canonical = "java.util.ArrayList<java.lang.String<java.lang.String>";
 typeParser.parse(canonical);
 fail("Expected IllegalArgumentException but no exception was thrown.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingComma() throws Exception {
 String canonical = "java.util.ArrayList<java.lang.String java.lang.Integer>";
 typeParser.parse(canonical);
 fail("Expected IllegalArgumentException but no exception was thrown.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnknownClass() throws Exception {
 String canonical = "com.unknown.UnknownClass<java.lang.String, java.util.List<java.lang.Integer>>";
 typeParser.parse(canonical);
 fail("Expected IllegalArgumentException but no exception was thrown.");
 }
}