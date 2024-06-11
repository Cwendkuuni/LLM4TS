package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {

 private TypeFactory _factory = new TypeFactory();
 private TypeParser _parser = new TypeParser(_factory);

 @Test
 public void testParseSimpleClass() {
 String canonical = "java.lang.String";
 JavaType type = _parser.parse(canonical);
 assertEquals(String.class, type.getRawClass());
 }

 @Test
 public void testParseClassWithGenerics() {
 String canonical = "java.util.List<java.lang.Integer>";
 JavaType type = _parser.parse(canonical);
 TypeBindings bindings = type.getTypeBindings();
 assertEquals(Integer.class, bindings.getParameterType(0));
 assertEquals(List.class, bindings.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidClass() {
 String canonical = "java.util.List<java.lang.InvalidClass>";
 _parser.parse(canonical);
 }

 @Test
 public void testParseClassWithMultipleTypes() {
 String canonical = "java.util.List<java.lang.Integer, java.lang.String>";
 JavaType type = _parser.parse(canonical);
 TypeBindings bindings = type.getTypeBindings();
 List<JavaType> types = bindings.getParameterTypes();
 assertEquals(2, types.size());
 assertEquals(Integer.class, types.get(0));
 assertEquals(String.class, types.get(1));
 }

 @Test
 public void testParseClassWithComments() {
 String canonical = "//This is a test\njava.util.List<java.lang.Integer>";
 JavaType type = _parser.parse(canonical);
 assertEquals(List.class, type.getRawClass());
 }

 @Test
 public void testParseClassWithEmptyInput() {
 JavaType type = _parser.parse("");
 assertEquals(Void.class, type.getRawClass());
 }

 @Test
 public void testParseClassWithInvalidInput() {
 String canonical = "InvalidClass";
 _parser.parse(canonical);
 }

 @Test
 public void testParseClassWithInvalidToken() {
 String canonical = "java.util.List<java.lang.Integer<";
 _parser.parse(canonical);
 }

 @Test
 public void testParseClassWithEOF() {
 String canonical = "java.util.List<java.lang.Integer>";
 MyTokenizer tokens = new MyTokenizer(canonical);
 _parser.parseType(tokens);
 assertTrue(tokens.hasMoreTokens());
 }

 @Test
 public void testParseClassWithMalformedTypes() {
 String canonical = "java.util.List<java.lang.Integer, java.lang.String<";
 _parser.parse(canonical);
 }

 @Test
 public void testParseClassWithMalformedToken() {
 String canonical = "java.util.List<java.lang.Integer, java.lang.String<>";
 _parser.parse(canonical);
 }

 @Test
 public void testParseClassWithMissingClosingBrace() {
 String canonical = "java.util.List<java.lang.Integer, java.lang.String>";
 MyTokenizer tokens = new MyTokenizer(canonical);
 _parser.parseType(tokens);
 tokens.pushBack(">");
 _parser.parseTypes(tokens);
 assertFalse(tokens.hasMoreTokens());
 }

 @Test
 public void testParseClassWithExtraClosingBrace() {
 String canonical = "java.util.List<java.lang.Integer, java.lang.String>";
 MyTokenizer tokens = new MyTokenizer(canonical);
 _parser.parseType(tokens);
 _parser.parseTypes(tokens);
 tokens.pushBack(">");
 assertFalse(tokens.hasMoreTokens());
 }

 @Test
 public void testParseClassWithMissingComma() {
 String canonical = "java.util.List<java.lang.Integer java.lang.String>";
 _parser.parse(canonical);
 }

 @Test
 public void testParseClassWithExtraComma() {
 String canonical = "java.util.List<java.lang.Integer, java.lang.String, java.lang.String>";
 _parser.parse(canonical);
 }

 @Test
 public void testParseClassWithMissingEndOfInput() {
 String canonical = "java.util.List<java.lang.Integer>";
 MyTokenizer tokens = new MyTokenizer(canonical);
 _parser.parseType(tokens);
 _parser.parseTypes(tokens);
 assertFalse(tokens.hasMoreTokens());
 }

 @Test
 public void testParseClassWithExtraEndOfInput() {
 String canonical = "java.util.List<java.lang.Integer>";
 MyTokenizer tokens = new MyTokenizer(canonical);
 _parser.parseType(tokens);
 _parser.parseTypes(tokens);
 tokens.pushBack("");
 assertFalse(tokens.hasMoreTokens());
 }

 @Test
 public void testParseClassWithInvalidInputAfterTypes() {
 String canonical = "java.util.List<java.lang.Integer, java.lang.String>InvalidClass";
 _parser.parse(canonical);
 }

 @Test
 public void testParseClassWithInvalidInputBeforeTypes() {
 String canonical = "InvalidClass java.util.List<java.lang.Integer, java.lang.String>";
 _parser.parse(canonical);
 }

 @Test
 public void testParseClassWithInvalidInputBetweenTypes() {
 String canonical = "java.util.List<java.lang.Integer, java.lang.StringInvalidClass>";
 _parser.parse(canonical);
 }

 @Test
 public void testParseClassWithEmptyListTypes() {
 String canonical = "java.util.List<>";
 JavaType type = _parser.parse(canonical);
 assertTrue(type.isRaw());
 }

 @Test
 public void testParseClassWithEmptyListTypesAndGenerics() {
 String canonical = "java.util.List<>()<java.lang.Integer>";
 JavaType type = _parser.parse(canonical);
 assertTrue(type.isRaw());
 assertEquals(Integer.class, type.getContentType());
 }

 @Test
 public void testParseClassWithArrayTypes() {
 String canonical = "[java.lang.Integer[], java.lang.String[]]";
 JavaType type = _parser.parse(canonical);
 TypeBindings bindings = type.getTypeBindings();
 List<JavaType> types = bindings.getParameterTypes();
 assertEquals(2, types.size());
 assertEquals(Arrays.asList(Integer[].class, String[].class), Arrays.asList(types.get(0), types.get(1)));
 }

 @Test
 public void testParseClassWithArrayTypesAndGenerics() {
 String canonical = "[java.util.List<java.lang.Integer>, java.util.List<java.lang.String>]";
 JavaType type = _parser.parse(canonical);
 TypeBindings bindings = type.getTypeBindings();
 List<JavaType> types = bindings.getParameterTypes();
 assertEquals(2, types.size());
 TypeBindings innerBindings = types.get(0).getTypeBindings();
 List<JavaType> innerTypes = innerBindings.getParameterTypes();
 assertEquals(1, innerTypes.size());
 assertEquals(Integer.class, innerTypes.get(0));
 TypeBindings outerBindings = types.get(1).getTypeBindings();
 List<JavaType> outerTypes = outerBindings.getParameterTypes();
 assertEquals(1, outerTypes.size());
 assertEquals(List.class, outerTypes.get(0));
 }

 @Test
 public void testParseClassWithArrayTypesAndGenericsAndEmptyTypes() {
 String canonical = "[java.util.List<>, java.util.List<java.lang.String>]";
 JavaType type = _parser.parse(canonical);
 TypeBindings bindings = type.getTypeBindings();
 List<JavaType> types = bindings.getParameterTypes();
 assertEquals(2, types.size());
 TypeBindings innerBindings = types.get(0).getTypeBindings();
 List<JavaType> innerTypes = innerBindings.getParameterTypes();
 assertTrue(innerTypes.isEmpty());
 TypeBindings outerBindings = types.get(1).getTypeBindings();
 List<JavaType> outerTypes = outerBindings.getParameterTypes();
 assertEquals(1, outerTypes.size());
 assertEquals(List.class, outerTypes.get(0));
 }

 @Test
 public void testParseClassWithArrayTypesAndGenericsAndEmptyTypesAndEmptyInput() {
 String canonical = "[java.util.List<>, java.util.List<>]";
 JavaType type = _parser.parse(canonical);
 TypeBindings bindings = type.getTypeBindings();
 List<JavaType> types = bindings.getParameterTypes();
 assertEquals(2, types.size());
 TypeBindings innerBindings = types.get(0).getTypeBindings();
 List<JavaType> innerTypes = innerBindings.getParameterTypes();
 assertTrue(innerTypes.isEmpty());
 TypeBindings outerBindings = types.get(1).getTypeBindings();
 List<JavaType> outerTypes = outerBindings.getParameterTypes();
 assertTrue(outerTypes.isEmpty());
 }

 @Test
 public void testParseClassWithArrayTypesAndGenericsAndEmptyTypesAndEmptyInputAndInvalidInput() {
 String canonical = "[java.util.List<>, java.util.List<>InvalidClass]";
 _parser.parse(canonical);
 }

 @Test
 public void testParseClassWithArrayTypesAndGenericsAndEmptyTypesAndEmptyInputAndInvalidInputAndEOF() {
 String canonical = "[java.util.List<>, java.util.List<>]";
 MyTokenizer tokens = new MyTokenizer(canonical);
 _parser.parseType(tokens);
 _parser.parseTypes(tokens);
 assertTrue(tokens.hasMoreTokens());
 }
}