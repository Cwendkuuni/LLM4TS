package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TypeParserTest {

 private TypeParser _parser;
 private TypeFactory _factory;

 @Before
 public void setUp() {
 _factory = new DefaultTypeFactory();
 _parser = new TypeParser(_factory);
 }

 @Test
 public void testParseSimpleType() {
 String canonical = "int";
 JavaType type = _parser.parse(canonical);
 assertEquals(Integer.TYPE, type.getRawClass());
 }

 @Test
 public void testParseClassType() {
 String canonical = "java.util.List";
 JavaType type = _parser.parse(canonical);
 assertEquals(List.class, type.getRawClass());
 }

 @Test
 public void testParseGenericType() {
 String canonical = "java.util.List<Integer>";
 JavaType type = _parser.parse(canonical);
 List<JavaType> parameterTypes = Arrays.asList(Integer.TYPE);
 TypeBindings bindings = TypeBindings.create(List.class, parameterTypes);
 assertEquals(List.class, type.getRawClass());
 assertEquals(bindings, type.getTypeParameters());
 }

 @Test
 public void testParseComplexGenericType() {
 String canonical = "java.util.Map<Integer, java.util.List<Integer>>";
 JavaType type = _parser.parse(canonical);
 List<JavaType> parameterTypes = Arrays.asList(
 Integer.TYPE,
 new TypeReference<List<Integer>>(){}.getType()
 );
 TypeBindings bindings = TypeBindings.create(Map.class, Arrays.asList(
 Integer.TYPE, new TypeReference<List<Integer>>(){}.getType()
 ));
 assertEquals(Map.class, type.getRawClass());
 assertEquals(bindings, type.getTypeParameters());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() {
 String canonical = "java.util.List<int";
 try {
 _parser.parse(canonical);
 } catch (IllegalArgumentException e) {
 assertEquals("Failed to parse type 'java.util.List<int' (remaining: ')'>': Unexpected token 'int'", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingCloseBracket() {
 String canonical = "java.util.List<Integer>";
 _parser.parse(canonical);
 try {
 _parser.parse(canonical + ";"); // missing '>'.
 } catch (IllegalArgumentException e) {
 assertEquals("Unexpected end-of-string", e.getMessage());
 }
 }
}