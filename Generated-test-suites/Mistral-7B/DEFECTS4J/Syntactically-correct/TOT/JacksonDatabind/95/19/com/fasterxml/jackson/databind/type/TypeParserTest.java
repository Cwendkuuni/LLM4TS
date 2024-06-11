package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit. Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

 private static final TypeFactory TYPE_FACTORY = new SimpleTypeFactory();

 @Test
 public void testParseSimpleType() throws Exception {
 TypeParser parser = new TypeParser(TYPE_FACTORY);
 JavaType type = parser.parse("int");
 assertEquals(Integer.TYPE, type.getRawClass());
 }

 @Test
 public void testParseFullyQualifiedType() throws Exception {
 TypeParser parser = new TypeParser(TYPE_FACTORY);
 JavaType type = parser.parse("java.lang.String");
 assertEquals(String.class, type.getRawClass());
 }

 @Test
 public void testParseGenericType() throws Exception {
 TypeParser parser = new TypeParser(TYPE_FACTORY);
 JavaType type = parser.parse("java.util.List<java.lang.Integer>");
 List<JavaType> typeArguments = type.getParameterTypes();
 assertEquals(Integer.TYPE, typeArguments.get(0).getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() throws Exception {
 TypeParser parser = new TypeParser(TYPE_FACTORY);
 parser.parse("bla"); // missing ';' or '{'
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingTypeArgument() throws Exception {
 TypeParser parser = new TypeParser(TYPE_FACTORY);
 parser.parse("java.util.List<>"); // missing type argument
 }

 @Test
 public void testParseNestedTypes() throws Exception {
 TypeParser parser = new TypeParser(TYPE_FACTORY);
 JavaType type = parser.parse("java.util.List<java.util.List<java.lang.Integer>>");
 List<JavaType> typeArguments = type.getParameterTypes();
 JavaType nestedType = typeArguments.get(0);
 List<JavaType> nestedTypeArguments = nestedType.getParameterTypes();
 assertEquals(Integer.TYPE, nestedTypeArguments.get(0).getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidTokenInType() throws Exception {
 TypeParser parser = new TypeParser(TYPE_FACTORY);
 parser.parse("java.util.List<java.lang.Integer<bla>"); // invalid token 'bla'
 }

 @Test
 public void testFindClass() throws Exception {
 TypeParser parser = new TypeParser(TYPE_FACTORY);
 JavaType type = parser.parse("java.lang.String[][]");
 Class<?> clazz = type.getRawClass();
 assertEquals(clazz.getComponentType().getComponentType(), clazz.getComponentType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindClassWithInvalidClassName() throws Exception {
 TypeParser parser = new TypeParser(TYPE_FACTORY);
 parser.parse("java.doesnotexist.String[][]"); // class does not exist
 }

 @Test
 public void testMyTokenizer() {
 MyTokenizer tokenizer = new MyTokenizer("int   java.lang.String<java.util.List<java.lang.Integer>>[]");
 List<String> tokens = new ArrayList<>();
 String token;
 while (tokenizer.hasMoreTokens()) {
 tokens.add(tokenizer.nextToken());
 }
 List<String> expected = Arrays.asList("int", "java.lang.String", "<", "java.util.List", "<", "java.lang.Integer", ">", "[]");
 assertEquals(expected, tokens);
 }

}