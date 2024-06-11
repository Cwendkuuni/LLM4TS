package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.TypeParser;

/**
 * Test class for TypeParser.
 */
public class TypeParserTest {

 private TypeFactory typeFactory;
 private TypeParser typeParser;

 @Before
 public void setUp() {
 typeFactory = new TypeFactory();
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testParseType_SimpleClass() throws Exception {
 String canonical = "int";
 JavaType expected = typeFactory.getIntType();
 JavaType actual = typeParser.parse(canonical);
 assertEquals(expected.getRawClass(), actual.getRawClass());
 }

 @Test
 public void testParseType_ClassWithGenericType() throws Exception {
 String canonical = "java.util.List<java.lang.String>";
 JavaType expected = typeFactory.constructType(List.class, String.class);
 JavaType actual = typeParser.parse(canonical);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseType_InvalidCanonicalType() throws Exception {
 String canonical = "java.util.List<java.lang.String>abc";
 typeParser.parse(canonical);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseType_MissingClosingBracket() throws Exception {
 String canonical = "java.util.List<java.lang.String<";
 typeParser.parse(canonical);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseType_MissingComma() throws Exception {
 String canonical = "java.util.List<java.lang.String,java.lang.String>";
 typeParser.parse(canonical + "java.lang.Integer");
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseTypes_EmptyList() throws Exception {
 String canonical = "java.util.List<>";
 List<JavaType> expected = new ArrayList<>();
 List<JavaType> actual = typeParser.parseTypes(new MyTokenizer(canonical));
 assertEquals(expected, actual);
 }

 @Test
 public void testParseTypes_SingleElement() throws Exception {
 String canonical = "java.util.List<java.lang.String>";
 JavaType expected = typeFactory.constructType(List.class, String.class);
 List<JavaType> actual = typeParser.parseTypes(new MyTokenizer(canonical));
 assertEquals(expected, actual.get(0));
 }

 @Test
 public void testParseTypes_MultipleElements() throws Exception {
 String canonical = "java.util.List<java.lang.String,java.lang.Integer,java.lang.Long>";
 List<JavaType> expected = new ArrayList<>();
 expected.add(typeFactory.constructType(List.class, String.class));
 expected.add(typeFactory.getIntType());
 expected.add(typeFactory.getLongType());
 List<JavaType> actual = typeParser.parseTypes(new MyTokenizer(canonical));
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseTypes_MissingClosingBracket() throws Exception {
 String canonical = "java.util.List<java.lang.String,java.lang.Integer<";
 typeParser.parseTypes(new MyTokenizer(canonical));
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseTypes_MissingComma() throws Exception {
 String canonical = "java.util.List<java.lang.String,java.lang.String,java.lang.Integer>";
 typeParser.parseTypes(new MyTokenizer(canonical + "java.lang.Long"));
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testFindClass() throws Exception {
 String className = "java.util.ArrayList";
 JavaType actual = typeParser.parseType(new MyTokenizer("" + className));
 JavaType expected = typeFactory._fromClass(null, Class.forName(className), null);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindClass_InvalidClassName() throws Exception {
 String className = "java.util.ArrayListfoo";
 typeParser.parseType(new MyTokenizer("" + className));
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testToString() throws Exception {
 String canonical = "java.util.List<java.lang.String>";
 JavaType javaType = typeParser.parse(canonical);
 String expected = "class java.util.List<Ljava.lang.String;>";
 String actual = javaType.toString();
 assertEquals(expected, actual);
 }

}

 final static class MyTokenizer extends TypeParser.MyTokenizer {
 }