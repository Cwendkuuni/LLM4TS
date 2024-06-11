package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;

/**
 * Comprehensive JUnit 4 test file for TypeParser class
 */
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
 public void testParseSimpleType() {
 String canonicalType = "java.lang.String";
 JavaType parsedType = typeParser.parse(canonicalType);
 JavaType expectedType = typeFactory.constructType(canonicalType);
 assertEquals(expectedType, parsedType);
 }

 @Test
 public void testParseArrayType() {
 String canonicalType = "[Ljava.lang.Integer;";
 JavaType parsedType = typeParser.parse(canonicalType);
 JavaType expectedType = typeFactory.constructArrayType(Integer.TYPE);
 assertEquals(expectedType, parsedType);
 }

 @Test
 public void testParseGenericType() {
 String canonicalType = "java.util.List<java.lang.Integer>";
 JavaType parsedType = typeParser.parse(canonicalType);
 JavaType expectedType = typeFactory.constructType(List.class, Integer.TYPE);
 TypeBindings bindings = parsedType.getBindings();
 assertEquals(Integer.TYPE, bindings.getRawType());
 assertEquals(List.class, bindings.getRawClass());
 assertEquals(Integer.TYPE, bindings.getTypeVariableBindings().get(0).getType());
 assertEquals(List.class, parsedType.getRawClass());
 assertEquals(expectedType, parsedType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() {
 String canonicalType = "java.lang.String<Integer>";
 typeParser.parse(canonicalType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnexpectedToken() {
 String canonicalType = "java.lang.String<Integer, Integer>";
 typeParser.parse(canonicalType);
 }

 @Test
 public void testParseEmptyType() {
 String canonicalType = "";
 JavaType parsedType = typeParser.parse(canonicalType);
 assertNull(parsedType);
 }

 @Test
 public void testParseTypeWithClassNotFound() {
 String canonicalType = "com.fasterxml.jackson.databind.type.TypeParserTest$NonExistentClass";
 try {
 typeParser.parse(canonicalType);
 fail("Expected IllegalArgumentException due to class not found");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Can not locate class 'com.fasterxml.jackson.databind.type.TypeParserTest$NonExistentClass', problem: ";
 assertTrue(e.getMessage().startsWith(expectedMessage));
 }
 }

 @Test
 public void testParseArrayOfGenericTypes() {
 String canonicalType = "[Ljava.util.List<java.lang.String>;";
 JavaType parsedType = typeParser.parse(canonicalType);
 JavaType expectedType = typeFactory.constructArrayType(List.class, String.class);
 assertEquals(expectedType, parsedType);
 }

 @Test
 public void testParseNestedGenericTypes() {
 String canonicalType = "java.util.List<java.util.List<java.lang.String>>";
 JavaType parsedType = typeParser.parse(canonicalType);
 JavaType expectedType = typeFactory.constructType(List.class, List.class, String.class);
 TypeBindings bindings = parsedType.getBindings();
 assertEquals(List.class, bindings.getRawClass());
 assertEquals(List.class, bindings.getTypeVariableBindings().get(0).getType());
 assertEquals(List.class, bindings.getTypeVariableBindings().get(1).getType());
 assertEquals(expectedType, parsedType);
 }

 @Test
 public void testParseTypeWithInvalidToken() {
 String canonicalType = "java.lang.String<Integer, Integer>";
 try {
 typeParser.parse(canonicalType);
 fail("Expected IllegalArgumentException due to invalid token ','");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Unexpected token ','";
 assertTrue(e.getMessage().startsWith(expectedMessage));
 }
 }

 @Test
 public void testParseTypeWithEmptyTypesList() {
 String canonicalType = "<>";
 try {
 typeParser.parseTypes(new MyTokenizer(canonicalType));
 fail("Expected IllegalArgumentException due to empty types list");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Unexpected end-of-string";
 assertTrue(e.getMessage().startswith(expectedMessage));
 }
 }

 @Test
 public void testParseTypesWithSingleType() {
 String canonicalType = "<java.lang.String>";
 List<JavaType> parsedTypes = typeParser.parseTypes(new MyTokenizer(canonicalType));
 JavaType parsedType = parsedTypes.get(0);
 JavaType expectedType = String.class;
 assertEquals(expectedType, parsedType);
 }

 @Test
 public void testParseTypesWithMultipleTypes() {
 String canonicalType = "<java.lang.String, java.lang.Integer>";
 List<JavaType> parsedTypes = typeParser.parseTypes(new MyTokenizer(canonicalType));
 JavaType parsedType1 = parsedTypes.get(0);
 JavaType parsedType2 = parsedTypes.get(1);
 JavaType expectedType1 = String.class;
 JavaType expectedType2 = Integer.TYPE;
 assertEquals(expectedType1, parsedType1);
 assertEquals(expectedType2, parsedType2);
 }

 @Test
 public void testParseTypesWithMissingClosingBracket() {
 String canonicalType = "<java.lang.String, java.lang.Integer>";
 try {
 typeParser.parseTypes(new MyTokenizer(canonicalType));
 fail("Expected IllegalArgumentException due to missing closing '>')'");
 } catch (IllegalArgumentException e) {
 String expectedMessage = "Unexpected end-of-string";
 assertTrue(e.getMessage().startsWith(expectedMessage));
 }
 }
}