package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
 public void testParseEmptyString() {
 JavaType javaType = typeParser.parse("");
 assertEquals(null, javaType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidString() {
 String invalidInput = "invalidInput";
 JavaType javaType = typeParser.parse(invalidInput);
 fail("Expected IllegalArgumentException but got " + javaType);
 }

 @Test
 public void testParsePrimitiveTypes() {
 JavaType intType = typeParser.parse("int");
 assertEquals(int.class, intType.getRawClass());
 JavaType booleanType = typeParser.parse("boolean");
 assertEquals(boolean.class, booleanType.getRawClass());
 JavaType charType = typeParser.parse("char");
 assertEquals(char.class, charType.getRawClass());
 JavaType longType = typeParser.parse("long");
 assertEquals(long.class, longType.getRawClass());
 JavaType doubleType = typeParser.parse("double");
 assertEquals(double.class, doubleType.getRawClass());
 JavaType floatType = typeParser.parse("float");
 assertEquals(float.class, floatType.getRawClass());
 }

 @Test
 public void testParseClass() {
 JavaType stringType = typeParser.parse("String");
 assertEquals(String.class, stringType.getRawClass());
 JavaType listType = typeParser.parse("java.util.List");
 JavaType listTypeWithGenericType = typeParser.parse("java.util.List<String>");
 assertEquals(List.class, listType.getRawClass());
 JavaType listTypeWithGenericTypeParameter = typeParser.parse("java.util.List<java.util.List<Integer>>");
 assertEquals(List.class, listTypeWithGenericTypeParameter.getRawClass());
 assertEquals(List.class, listTypeWithGenericTypeParameter.getRawType());
 JavaType mapType = typeParser.parse("java.util.Map");
 JavaType mapTypeWithKeyValueTypes = typeParser.parse("java.util.Map<String, Integer>");
 assertEquals(Map.class, mapType.getRawClass());
 JavaType mapTypeWithKeyValueTypesParameter = typeParser.parse("java.util.Map<String, java.util.List<Integer>>");
 assertEquals(Map.class, mapTypeWithKeyValueTypesParameter.getRawClass());
 assertEquals(Map.class, mapTypeWithKeyValueTypesParameter.getRawType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidClass() {
 JavaType invalidType = typeParser.parse("InvalidClass");
 fail("Expected IllegalArgumentException but got " + invalidType);
 }

 @Test
 public void testParseClassWithGenerics() {
 JavaType listOfStrings = typeParser.parse("java.util.List<String>");
 JavaType mapOfStringsToListsOfIntegers = typeParser.parse("java.util.Map<String, java.util.List<Integer>>");
 JavaType listOfListsOfIntegers = typeParser.parse("java.util.List<java.util.List<Integer>>");
 assertEquals(List.class, listOfStrings.getRawClass());
 assertEquals(String.class, listOfStrings.getType().getParameterTypes()[0].getRawClass());
 assertEquals(Map.class, mapOfStringsToListsOfIntegers.getRawClass());
 assertEquals(String.class, mapOfStringsToListsOfIntegers.getType().getParameterTypes()[0].getRawClass());
 assertEquals(List.class, mapOfStringsToListsOfIntegers.getType().getParameterTypes()[1].getRawClass());
 assertEquals(List.class, listOfListsOfIntegers.getRawClass());
 assertEquals(List.class, listOfListsOfIntegers.getType().getParameterTypes()[0].getRawClass());
 assertEquals(Integer.class, listOfListsOfIntegers.getType().getParameterTypes()[0].getParameterTypes()[0].getRawClass());
 }
}