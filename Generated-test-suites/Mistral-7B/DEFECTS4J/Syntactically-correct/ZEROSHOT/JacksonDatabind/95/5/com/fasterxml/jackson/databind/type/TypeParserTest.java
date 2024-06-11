package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBinding;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser.MyTokenizer;

public class TypeParserTest {

 private TypeFactory typeFactory;
 private TypeParser typeParser;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 typeFactory = new TypeFactoryBuilder().build();
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testParseSimpleType() throws Exception {
 String canonical = "java.lang.String";
 JavaType javaType = typeParser.parse(canonical);
 assertEquals(String.class, javaType.getRawClass());
 }

 @Test
 public void testParseGenericType() throws Exception {
 String canonical = "java.util.List<java.lang.String>";
 JavaType javaType = typeParser.parse(canonical);
 List<JavaType> typeArguments = javaType.getTypeArguments();
 assertEquals(String.class, typeArguments.get(0).getRawClass());
 assertEquals(List.class, javaType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() throws Exception {
 String canonical = "java.util.List>";
 typeParser.parse(canonical);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidTypeWithArguments() throws Exception {
 String canonical = "java.util.List<java.lang.String,java.lang.Integer>";
 typeParser.parse(canonical);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidInput() throws Exception {
 String canonical = "java.util.List java.lang.String";
 typeParser.parse(canonical);
 }

 @Test
 public void testParseTokenizer() throws Exception {
 MyTokenizer tokenizer = new MyTokenizer("java.util.List<java.lang.String,java.lang.Integer>");
 List<JavaType> types = typeParser.parseTypes(tokenizer);
 assertEquals(2, types.size());
 assertEquals(Integer.class, types.get(1).getRawClass());
 }

 @Test
 public void testFindClass() throws Exception {
 String className = "java.util.ArrayList";
 JavaType javaType = typeParser.parseType(new MyTokenizer(className));
 assertEquals(ArrayList.class, javaType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindClassUnknownClass() throws Exception {
 String className = "com.unknown.Class";
 typeParser.parseType(new MyTokenizer(className));
 }
}