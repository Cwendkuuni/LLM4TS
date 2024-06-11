package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;

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
 public void testParseSimpleClass() throws IllegalArgumentException {
 JavaType javaType = typeParser.parse("int");
 Class<?> expectedClass = Integer.TYPE;
 assertEquals(expectedClass, javaType.getRawClass());
 }

 @Test
 public void testParseClassWithGenerics() throws IllegalArgumentException {
 JavaType javaType = typeParser.parse("java.util.List<java.lang.Integer>");
 Class<?> expectedClass = List.class;
 TypeBindings expectedBindings = TypeBindings.from(Integer.TYPE);
 JavaType expectedParameterType = typeFactory.constructType(Integer.TYPE);
 assertEquals(expectedClass, javaType.getRawClass());
 assertEquals(expectedBindings, javaType.getTypeBindings());
 assertEquals(expectedParameterType, javaType.getParameterTypes().get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidInput() throws IllegalArgumentException {
 typeParser.parse("invalidType");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidInputWithGenerics() throws IllegalArgumentException {
 typeParser.parse("java.util.List<invalidType>");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingRightBracket() throws IllegalArgumentException {
 typeParser.parse("java.util.List<java.lang.Integer> <");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingComma() throws IllegalArgumentException {
 typeParser.parse("java.util.List<java.lang.Integer, java.lang.String>");
 }

 @Test
 public void testParseMultipleTypes() throws IllegalArgumentException {
 JavaType javaType = typeParser.parse("java.util.List<java.lang.Integer>, java.util.Set<java.lang.String>");
 List<JavaType> expectedTypes = Arrays.asList(List.class, Set.class);
 List<Class<?>> rawTypes = Arrays.asList(List.class, Set.class);
 List<JavaType> actualParameterTypes = javaType.getParameterTypes();
 assertEquals(expectedTypes, actualParameterTypes);
 List<Class<?>> actualRawTypes = Arrays.asList(javaType.getRawClasses());
 assertEquals(rawTypes, actualRawTypes);
 }
}