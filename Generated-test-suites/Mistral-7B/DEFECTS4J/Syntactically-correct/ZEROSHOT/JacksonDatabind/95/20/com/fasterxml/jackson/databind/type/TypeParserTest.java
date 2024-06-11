package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.TypeParser;

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
 JavaType javaType = typeParser.parse("int");
 Class<?> clazz = javaType.getRawClass();
 assertEquals(int.class, clazz);
 }

 @Test
 public void testParseClassType() {
 JavaType javaType = typeParser.parse("java.lang.String");
 Class<?> clazz = javaType.getRawClass();
 assertEquals(String.class, clazz);
 }

 @Test
 public void testParseListType() {
 JavaType javaType = typeParser.parse("java.util.List<java.lang.Integer>");
 JavaType componentType = javaType.getContentType();
 JavaType elementType = componentType.getRawClass();
 assertEquals(Integer.class, elementType);
 }

 @Test
 public void testParseCollectionType() {
 JavaType javaType = typeParser.parse("java.util.Map<java.lang.String, java.lang.Integer>");
 JavaType mapClass = javaType.getRawClass();
 Class<? extends Map<?, ?>> mapInterface = (Class<? extends Map<?, ?>>) Map.class;
 assertTrue(mapInterface.isInstance(mapClass));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType1() {
 String invalidType = "java.util.Map<java.lang.String, java.lang.Integer<java.lang.String>";
 typeParser.parse(invalidType);
 fail("Unexpected exception not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType2() {
 String invalidType = "java.util.Map<java.lang.String, java.lang.Integer>java.util.List<java.lang.String>";
 typeParser.parse(invalidType);
 fail("Unexpected exception not thrown");
 }

 @Test
 public void testParseGenericTypeWithWildcard() {
 JavaType javaType = typeParser.parse("java.util.List<?>");
 JavaType componentType = javaType.getContentType();
 assertEquals(Object.class, componentType.getRawClass());
 }

 @Test
 public void testParseArrayType() {
 JavaType javaType = typeParser.parse("int[]");
 Class<?> clazz = javaType.getRawClass();
 assertArrayClass(clazz);
 assertEquals(int[].class, clazz);
 }

 @Test
 public void testParseArrayTypeWithGenericType() {
 JavaType javaType = typeParser.parse("java.util.List<java.lang.Integer>[]");
 JavaType componentType = javaType.getContentType();
 JavaType elementType = componentType.getContentType();
 JavaType arrayElementType = elementType.getRawClass();
 assertEquals(List.class, componentType.getRawClass());
 assertEquals(Integer.class, arrayElementType);
 }

 @Test
 public void testParseNamedType() {
 JavaType javaType = typeParser.parse("java.util.Collections$UnmodifiableList");
 Class<?> clazz = javaType.getRawClass();
 assertEquals(java.util.Collections.UnmodifiableList.class, clazz);
 }

 @Test
 public void testParseTypeWithAnnotations() {
 JavaType javaType = typeParser.parse("java.util.List<java.util.Map<java.lang.String, java.lang.Integer>>");
 JavaType mapType = javaType.getContentType();
 JavaType mapElementType = mapType.getContentType();
 JavaType listType = javaType.getRawClass();

 List<Class<?>> annotations = Arrays.asList(mapElementType.getAnnotations());
 assertTrue(annotations.contains(TypeBindings.MapSupplier.class));
 assertTrue(annotations.contains(TypeBindings.MapKeySerializer.class));
 assertTrue(annotations.contains(TypeBindings.MapValueSerializer.class));

 List<Class<?>> listAnnotations = Arrays.asList(listType.getAnnotations());
 assertTrue(listAnnotations.contains(TypeBindings.CollectionLike.class));
 }

 private void assertArrayClass(Class<?> clazz) {
 assertTrue(clazz.isArray());
 }
}