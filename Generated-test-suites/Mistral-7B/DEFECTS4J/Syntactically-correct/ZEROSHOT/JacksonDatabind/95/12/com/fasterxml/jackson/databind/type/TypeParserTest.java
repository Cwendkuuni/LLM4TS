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

/**
 * Test file for TypeParser class
 */
public class TypeParserTest {

 private TypeFactory typeFactory;
 private TypeParser typeParser;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 typeFactory = new TypeFactoryBuilder()
 .baseClass(Object.class)
 .build();
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testParseSimpleType() {
 String canonicalType = "java.lang.String";
 JavaType expectedType = typeFactory.constructType(canonicalType);
 JavaType actualType = typeParser.parse(canonicalType);
 assertEquals(expectedType.getRawClass(), actualType.getRawClass());
 }

 @Test
 public void testParseGenericType() {
 String canonicalType = "java.util.List<java.lang.Integer>";
 JavaType expectedType = typeFactory.constructType(canonicalType);
 JavaType actualType = typeParser.parse(canonicalType);
 assertEquals(expectedType.getRawClass(), actualType.getRawClass());
 assertEquals(expectedType.getTypeBindings().getBindings(),
 actualType.getTypeBindings().getBindings());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() {
 String canonicalType = "java.lang.String<Integer>";
 String message = "Failed to parse type '(remaining: )': Unexpected token '<', expected ',', or ')'";
 try {
 typeParser.parse(canonicalType);
 } catch (IllegalArgumentException e) {
 assertEquals(message, e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseTypeWithUnexpectedToken() {
 String canonicalType = "java.lang.String<Integer >";
 String message = "Failed to parse type 'java.lang.String<Integer >' (remaining: '>'): Unexpected token ' '";
 try {
 typeParser.parse(canonicalType);
 } catch (IllegalArgumentException e) {
 assertEquals(message, e.getMessage());
 }
 }

 @Test
 public void testParseTypeWithEmptyInput() {
 String canonicalType = "";
 JavaType expectedType = typeFactory.constructType(Object.class);
 JavaType actualType = typeParser.parse(canonicalType);
 assertEquals(expectedType.getRawClass(), actualType.getRawClass());
 }

 @Test
 public void testParseNestedGenerics() {
 String canonicalType = "java.util.List<java.util.List<java.lang.String>>";
 JavaType expectedType = typeFactory.constructType(canonicalType);
 JavaType actualType = typeParser.parse(canonicalType);
 assertEquals(expectedType.getRawClass(), actualType.getRawClass());
 assertEquals(expectedType.getTypeBindings().getBindings(),
 actualType.getTypeBindings().getBindings());
 }

 @Test
 public void testParseTypeWithParameters() {
 String canonicalType = "java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>";
 JavaType expectedType = typeFactory.constructType(canonicalType);
 JavaType actualType = typeParser.parse(canonicalType);
 assertEquals(expectedType.getRawClass(), actualType.getRawClass());
 assertEquals(expectedType.getTypeBindings().getBindings(),
 actualType.getTypeBindings().getBindings());
 }

 @Test
 public void testParseTypeWithArray() {
 String canonicalType = "[Ljava.lang.String;";
 JavaType expectedType = typeFactory.constructType(canonicalType);
 JavaType actualType = typeParser.parse(canonicalType);
 assertEquals(expectedType.getRawClass(), actualType.getRawClass());
 }

 @Test
 public void testParseTypeWithArrayAndGenerics() {
 String canonicalType = "[Ljava.util.List<java.lang.String>;";
 JavaType expectedType = typeFactory.constructType(canonicalType);
 JavaType actualType = typeParser.parse(canonicalType);
 assertEquals(expectedType.getRawClass(), actualType.getRawClass());
 assertEquals(expectedType.getTypeBindings().getBindings(),
 actualType.getTypeBindings().getBindings());
 }

 @Test
 public void testParseTypeWithNestedParentheses() {
 String canonicalType = "java.util.List<java.util.List<java.lang.String>>(java.util.List<java.lang.String>)";
 JavaType expectedType = typeFactory.constructType(canonicalType);
 JavaType actualType = typeParser.parse(canonicalType);
 assertEquals(expectedType.getRawClass(), actualType.getRawClass());
 assertEquals(expectedType.getTypeBindings().getBindings(),
 actualType.getTypeBindings().getBindings());
 }

 @Test
 public void testParseTypeWithInvalidNestedParentheses() {
 String canonicalType = "java.util.List<java.util.List<java.lang.String>)(java.util.List<java.lang.String>)";
 String message = "Failed to parse type 'java.util.List<java.util.List<java.lang.String>)(java.util.List<java.lang.String>)' (remaining: ')'): Unexpected token '('";
 try {
 typeParser.parse(canonicalType);
 } catch (IllegalArgumentException e) {
 assertEquals(message, e.getMessage());
 }
 }
}