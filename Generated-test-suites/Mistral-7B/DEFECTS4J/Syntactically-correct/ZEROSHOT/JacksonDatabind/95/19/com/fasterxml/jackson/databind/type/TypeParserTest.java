package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;

/**
 * Test class for TypeParser.java
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
 public void testParseSimpleClass() {
 String canonicalType = "java.lang.String";
 JavaType javaType = typeParser.parse(canonicalType);
 Class<?> actualClass = javaType.getRawClass();
 assertEquals("java.lang.String", canonicalType);
 assertEquals(String.class, actualClass);
 }

 @Test
 public void testParseClassWithGenerics() {
 String canonicalType = "java.util.ArrayList<java.lang.Integer>";
 JavaType javaType = typeParser.parse(canonicalType);
 TypeBindings typeBindings = javaType.getTypeBindings();
 assertNotNull(typeBindings);
 assertEquals(Integer.class, typeBindings.getParameterType(0));
 assertEquals(ArrayList.class, typeBindings.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidCanonicalType() {
 String canonicalType = "java.lang.String<java.lang.String>";
 typeParser.parse(canonicalType);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnexpectedToken() {
 String canonicalType = "java.lang.String<java.lang.Integer, java.lang.String>";
 MyTokenizer tokens = new MyTokenizer(canonicalType);
 typeParser.parseType(tokens);
 tokens.nextToken(); // consume '>'
 tokens.pushBack("{");
 typeParser.parseTypes(tokens);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testParseMultipleClasses() {
 String canonicalType = "java.lang.String, java.util.ArrayList<java.lang.Integer>";
 List<JavaType> javaTypes = typeParser.parseTypes(new MyTokenizer(canonicalType));
 assertEquals(2, javaTypes.size());
 JavaType stringType = javaTypes.get(0);
 JavaType listType = javaTypes.get(1);
 assertEquals(String.class, stringType.getRawClass());
 assertEquals(ArrayList.class, listType.getRawClass());
 assertEquals(Integer.class, listType.getTypeBindings().getParameterType(0));
 }

}

class MyTokenizer extends TypeParserTest.TokenizerWrapper {
 // empty
}

class TokenizerWrapper extends StringTokenizer {
 // empty
}