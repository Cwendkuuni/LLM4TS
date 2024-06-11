package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.TypeParser;

public class TypeParserTest {

 private static TypeFactory typeFactory = new TypeFactory();
 private static TypeParser parser = new TypeParser(typeFactory);

 @Test
 public void testParseSimpleClass() {
 String canonical = "int";
 JavaType expected = typeFactory.constructType(int.class);
 JavaType actual = parser.parse(canonical);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseClassWithGenericType() {
 String canonical = "java.util.List<java.lang.Integer>";
 JavaType expected = new TypeReference<List<Integer>>() {}.getType();
 JavaType actual = parser.parse(canonical);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseClassWithMultipleGenerics() {
 String canonical = "java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>";
 JavaType expected = new TypeReference<Map<String, List<Integer>>>() {}.getType();
 JavaType actual = parser.parse(canonical);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnexpectedTokenAfterCompleteType() {
 String canonical = "java.util.List<java.lang.Integer>";
 MyTokenizer tokens = new MyTokenizer(canonical);
 parser.parseType(tokens);
 tokens.nextToken(); // this should be '>' but we force it to be something else
 JavaType type = parser.parseType(tokens);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnexpectedEndOfString() {
 String incorrectCanonical = "java.util.List<java.lang.Integer>";
 MyTokenizer tokens = new MyTokenizer(incorrectCanonical.substring(0, incorrectCanonical.length() - 1));
 JavaType type = parser.parseType(tokens);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidClassName() {
 String incorrectCanonical = "java.util.List<java.lang.InvalidClass>";
 JavaType type = null;
 try {
 type = parser.parse(incorrectCanonical);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertEquals("Can not locate class 'InvalidClass'", e.getMessage());
 }
}