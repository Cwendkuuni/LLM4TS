package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {

 private TypeFactory typeFactory = new TypeFactory();
 private TypeParser typeParser;

 @Test
 public void testParseSimpleClass() {
     typeParser = new TypeParser(typeFactory);
     JavaType javaType = typeParser.parse("String");
     assertEquals("java.lang.String", javaType.getClassName());
 }

 @Test
 public void testParseClassWithGenerics() {
     typeParser = new TypeParser(typeFactory);
     JavaType javaType = typeParser.parse("java.util.ArrayList<Integer>");
     assertTrue(javaType instanceof TypeBindings.TypeBindingsJavaType);
     TypeBindings typeBindings = (TypeBindings) javaType;
     assertEquals("java.util.ArrayList", typeBindings.getRawType().getClassName());
     assertEquals(JavaType.constructCollectionType(typeFactory, List.class, Integer.class), typeBindings.getTypeArgs()[0]);
 }

 @Test
 public void testParseEmptyList() {
     typeParser = new TypeParser(typeFactory);
     JavaType javaType = typeParser.parse("java.util.List<>()");
     assertTrue(javaType instanceof TypeBindings.TypeBindingsJavaType);
     TypeBindings typeBindings = (TypeBindings) javaType;
     assertEquals("java.util.List", typeBindings.getRawType().getClassName());
     assertNull(typeBindings.getTypeArgs());
 }

 @Test
 public void testParseInvalidType() {
     typeParser = new TypeParser(typeFactory);
     try {
         JavaType javaType = typeParser.parse("InvalidType");
         fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException e) {
         String message = e.getMessage();
         assertTrue(message.contains("Can not locate class 'InvalidType'"));
     }
 }

 @Test
 public void testParseInvalidTypeWithParams() {
     typeParser = new TypeParser(typeFactory);
     try {
         JavaType javaType = typeParser.parse("InvalidType<InvalidParam1, InvalidParam2>");
         fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException e) {
         String message = e.getMessage();
         assertTrue(message.contains("Can not locate class 'InvalidType'"));
     }
 }

 @Test
 public void testParseInvalidToken() {
     typeParser = new TypeParser(typeFactory);
     try {
         MyTokenizer tokens = new MyTokenizer("#{invalidToken}");
         JavaType javaType = typeParser.parseType(tokens);
         fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException e) {
         String message = e.getMessage();
         assertTrue(message.contains("Unexpected token '#'"));
     }
 }

 @Test
 public void testParseInvalidTokenWithTrailingComma() {
     typeParser = new TypeParser(typeFactory);
     try {
         MyTokenizer tokens = new MyTokenizer("java.util.ArrayList<Integer, String>, ");
         JavaType javaType = typeParser.parseType(tokens);
         fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException e) {
         String message = e.getMessage();
         assertTrue(message.contains("Unexpected token ','"));
     }
 }

 @Test
 public void testParseInvalidEndOfString() {
     typeParser = new TypeParser(typeFactory);
     try {
         MyTokenizer tokens = new MyTokenizer("java.util.ArrayList<Integer>");
         JavaType javaType = typeParser.parseType(tokens);
         tokens.nextToken(); // consume '>'
         JavaType javaType2 = typeParser.parseType(tokens);
         fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException e) {
         String message = e.getMessage();
         assertTrue(message.contains("Unexpected end-of-string"));
     }
 }

 @Test
 public void testParseClassWithArrayAsParameter() {
     typeParser = new TypeParser(typeFactory);
     JavaType javaType = typeParser.parse("java.util.List<java.util.ArrayList<Integer>>");
     assertTrue(javaType instanceof TypeBindings.TypeBindingsJavaType);
     TypeBindings typeBindings = (TypeBindings) javaType;
     assertEquals("java.util.List", typeBindings.getRawType().getClassName());
     JavaType listItemType = typeBindings.getTypeArgs()[0];
     assertEquals("java.util.ArrayList", listItemType.getClassName());
     JavaType arrayItemType = listItemType.getTypeArgs()[0];
     assertEquals("java.lang.Integer", arrayItemType.getClassName());
 }

 @Test
 public void testParseComplexType() {
     typeParser = new TypeParser(typeFactory);
     JavaType javaType = typeParser.parse("java.util.Map<java.lang.String, java.util.List<java.util.ArrayList<java.lang.Integer>>>");
     assertTrue(javaType instanceof TypeBindings.TypeBindingsJavaType);
     TypeBindings typeBindings = (TypeBindings) javaType;
     assertEquals("java.util.Map", typeBindings.getRawType().getClassName());
     JavaType keyType = typeBindings.getTypeArgs()[0];
     assertEquals("java.lang.String", keyType.getClassName());
     JavaType valueType = typeBindings.getTypeArgs()[1];
     assertTrue(valueType instanceof TypeBindings.TypeBindingsJavaType);
     TypeBindings valueBindings = (TypeBindings) valueType;
     assertEquals("java.util.List", valueBindings.getRawType().getClassName());
     JavaType listItemType = valueBindings.getTypeArgs()[0];
     assertEquals("java.util.ArrayList", listItemType.getClassName());
     JavaType arrayItemType = listItemType.getTypeArgs()[0];
     assertEquals("java.lang.Integer", arrayItemType.getClassName());
 }

 @Test
 public void testParseListWithNestedGenerics() {
     typeParser = new TypeParser(typeFactory);
     JavaType javaType = typeParser.parse("java.util.List<java.util.List<java.util.List<java.lang.Integer>>>");
     assertTrue(javaType instanceof TypeBindings.TypeBindingsJavaType);
     TypeBindings typeBindings = (TypeBindings) javaType;
     assertEquals("java.util.List", typeBindings.getRawType().getClassName());
     JavaType listType = typeBindings.getTypeArgs()[0];
     assertTrue(listType instanceof TypeBindings.TypeBindingsJavaType);
     TypeBindings listBindings = (TypeBindings) listType;
     assertEquals("java.util.List", listBindings.getRawType().getClassName());
     JavaType innerListType = listBindings.getTypeArgs()[0];
     assertTrue(innerListType instanceof TypeBindings.TypeBindingsJavaType);
     TypeBindings innerListBindings = (TypeBindings) innerListType;
     assertEquals("java.util.List", innerListBindings.getRawType().getClassName());
     JavaType innerListItemType = innerListBindings.getTypeArgs()[0];
     assertEquals("java.util.List", innerListItemType.getClassName());
     JavaType innerListInnerItemType = innerListItemType.getTypeArgs()[0];
     assertEquals("java.lang.Integer", innerListInnerItemType.getClassName());
 }

 @Test
 public void testParseCollectionTypeWithCustomClass() {
     typeParser = new TypeParser(typeFactory);
     JavaType javaType = typeParser.parse("java.util.List<com.custom.CustomClass>");
     assertTrue(javaType instanceof TypeBindings.TypeBindingsJavaType);
     TypeBindings typeBindings = (TypeBindings) javaType;
     assertEquals("java.util.List", typeBindings.getRawType().getClassName());
     JavaType customClassType = typeBindings.getTypeArgs()[0];
     assertEquals("com.custom.CustomClass", customClassType.getClassName());
 }

 @Test
 public void testParseTypeWithNestedClasses() {
     typeParser = new TypeParser(typeFactory);
     JavaType javaType = typeParser.parse("com.example.MyClass$InnerClass$NestedClass");
     assertEquals("com.example.MyClass$InnerClass$NestedClass", javaType.getClassName());
 }

 @Test
 public void testParseArrayType() {
     typeParser = new TypeParser(typeFactory);
     JavaType javaType = typeParser.parse("int[]");
     assertEquals(Integer[].class, javaType.getRawClass());
 }

 @Test
 public void testParseArrayTypeWithGenericType() {
     typeParser = new TypeParser(typeFactory);
     JavaType javaType = typeParser.parse("java.util.List<java.lang.String>[]");
     assertEquals(JavaType.constructArrayType(JavaType.constructCollectionType(typeFactory, List.class, String.class), null), javaType);
 }

 @Test
 public void testParseArrayTypeWithNestedGenerics() {
     typeParser = new TypeParser(typeFactory);
     JavaType javaType = typeParser.parse("java.util.List<java.util.List<java.lang.Integer>>[]");
     assertEquals(JavaType.constructArrayType(JavaType.constructCollectionType(typeFactory, List.class, TypeReference.constructType(List.class, TypeReference.constructType(List.class, Integer.class))), null), javaType);
 }

 @Test
 public void testParseInvalidArrayType() {
     typeParser = new TypeParser(typeFactory);
     try {
         JavaType javaType = typeParser.parse("java.util.List<java.util.ArrayList<java.lang.Integer>>[]");
         fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException e) {
         String message = e.getMessage();
         assertTrue(message.contains("java.lang.ArrayStoreException: java.util.List<java.util.ArrayList<java.lang.Integer>> cannot be cast to [Ljava.lang.Object;"));
     }
 }

 @Test
 public void testParseEmptyArray() {
     typeParser = new TypeParser(typeFactory);
     JavaType javaType = typeParser.parse("int[][]");
     assertTrue(javaType.isArray());
     assertTrue(javaType.getRawClass().isAssignableFrom(int[].class));
     assertTrue(javaType.getRawClass().getComponentType().isArray());
     assertTrue(javaType.getRawClass().getComponentType().getComponentType() == null);
 }

 @Test
 public void testParseEmptyArrayWithTypes() {
     typeParser = new TypeParser(typeFactory);
     JavaType javaType = typeParser.parse("java.util.List<java.lang.Integer>[]");
     assertTrue(javaType.isArray());
     assertTrue(javaType.getRawClass().isAssignableFrom(JavaType.constructArrayType(JavaType.constructCollectionType(typeFactory, List.class, Integer.class), null).getRawClass()));
 }

 @Test
 public void testParseArrayTypeWithSingleElement() {
     typeParser = new TypeParser(typeFactory);
     JavaType javaType = typeParser.parse("int[]");
     assertEquals(Integer[].class, javaType.getRawClass());
     assertEquals(Integer.TYPE, javaType.getContentType());
 }

 @Test
 public void testParseArrayTypeWithSingleElementAndType() {
     typeParser = new TypeParser(typeFactory);
     JavaType javaType = typeParser.parse("java.lang.String[]");
     assertEquals(String[].class, javaType.getRawClass());
     assertEquals(String.class, javaType.getContentType());
 }

 @Test
 public void testParseArrayTypeWithMultipleElements() {
     typeParser = new TypeParser(typeFactory);
     JavaType javaType = typeParser.parse("int[][]");
     assertEquals(int[][].class, javaType.getRawClass());
 }

 @Test
 public void testParseArrayTypeWithMultipleElementsAndTypes() {
     typeParser = new TypeParser(typeFactory);
     JavaType javaType = typeParser.parse("java.lang.String[][]");
     assertEquals(String[][].class, javaType.getRawClass());
 }

 @Test
 public void testParseArrayTypeWithMultipleElementsAndNestedTypes() {
     typeParser = new TypeParser(typeFactory);
     JavaType javaType = typeParser.parse("java.util.List<java.util.List<java.lang.Integer>>[]");
     assertEquals(JavaType.constructArrayType(JavaType.constructCollectionType(typeFactory, List.class, TypeReference.constructType(List.class, TypeReference.constructType(List.class, Integer.class))), null).getRawClass(), javaType.getRawClass());
 }

 @Test
 public void testParseArrayTypeWithMultipleElementsAndNestedTypesAndMixedTypes() {
     typeParser = new TypeParser(typeFactory);
     JavaType javaType = typeParser.parse("java.util.List<java.util.List<java.lang.Integer, java.lang.String>>[]");
     JavaType[] expectedTypes = new JavaType[] { JavaType.constructArrayType(JavaType.constructCollectionType(typeFactory, List.class, TypeReference.constructType(List.class, TypeReference.constructType(List.class, Integer.class, String.class))), null), JavaType.constructArrayType(JavaType.constructCollectionType(typeFactory, List.class, TypeReference.constructType(List.class, Integer.class), TypeReference.constructType(List.class, String.class)), null) };
     assertEquals(Arrays.asList(expectedTypes), javaType.getTypeArguments());
 }

 @Test
 public void testParseArrayTypeWithMultipleElementsAndNestedTypesAndMixedTypesAndArrayType() {
     typeParser = new TypeParser(typeFactory);
     JavaType javaType = typeParser.parse("java.util.List<java.util.List<java.lang.Integer, java.lang.String>[], java.util.List<java.util.ArrayList<java.lang.Integer>>>[]");
     JavaType[] expectedTypes = new JavaType[] { JavaType.constructArrayType(JavaType.constructCollectionType(typeFactory, List.class, TypeReference.constructType(List.class, TypeReference.constructType(List.class, Integer.class, String.class))), null), JavaType.constructArrayType(JavaType.constructCollectionType(typeFactory, List.class, JavaType.constructCollectionType(typeFactory, ArrayList.class, Integer.class)), null) };
     assertEquals(Arrays.asList(expectedTypes), javaType.getTypeArguments());
 }
}