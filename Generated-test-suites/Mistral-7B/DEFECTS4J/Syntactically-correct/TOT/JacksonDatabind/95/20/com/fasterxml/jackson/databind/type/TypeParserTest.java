package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeDescriptor;
import com.fasterxml.jackson.databind.TypeFactory.DefaultTypeFactory;

public class TypeParserTest {

 private TypeFactory _factory = new DefaultTypeFactory();
 private TypeParser _parser;

 @Test
 public void testParseSimpleClass() {
 _parser = new TypeParser(_factory);
 JavaType type = _parser.parse("int");
 TypeDescriptor td = _factory.constructType(type.getRawClass());
 assertEquals(Integer.class, td.getRawClass());
 }

 @Test
 public void testParseClassWithGenerics() {
 _parser = new TypeParser(_factory);
 JavaType listType = _parser.parse("java.util.List<java.lang.Integer>");
 TypeDescriptors td = (TypeDescriptors) _factory.constructType(listType.getRawClass());
 assertEquals(List.class, td.getRawClass());
 assertEquals(Integer.class, td.getType().getComponentType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidType() {
 _parser = new TypeParser(_factory);
 try {
 _parser.parse("InvalidType");
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Failed to parse type 'InvalidType' (remaining: ')', problem: Can not locate class 'InvalidType'", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidToken() {
 _parser = new TypeParser(_factory);
 MyTokenizer tokens = new MyTokenizer("int<");
 try {
 _parser.parseType(tokens);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Failed to parse type '<' (remaining: 'int<'): Unexpected token '<'", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingEndBracket() {
 _parser = new TypeParser(_factory);
 MyTokenizer tokens = new MyTokenizer("int<java.util.List");
 try {
 _parser.parseType(tokens);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Failed to parse type 'java.util.List' (remaining: ''): Unexpected end-of-string", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingEndOfType() {
 _parser = new TypeParser(_factory);
 JavaType type = _parser.parse("int<java.util.List>java.util.List<Integer>");
 fail();
 }

 @Test
 public void testParseComplexType() {
 _parser = new TypeParser(_factory);
 JavaType type = _parser.parse("java.util.List<java.util.Map<String,java.util.List<java.lang.Integer>>>");
 TypeDescriptors td = (TypeDescriptors) _factory.constructType(type.getRawClass());
 List<TypeDescriptors> expected = Arrays.asList(
 TypeDescriptor.collectionDescriptor(Map.class, String.class,
 List.class, Integer.class),
 TypeDescriptor.collectionDescriptor(List.class, Integer.class)
 );
 List<TypeDescriptors> actual = new ArrayList<>();
 for (JavaType javaType : _parser.parseTypes(new MyTokenizer("java.util.List<java.util.Map<String,java.util.List<java.lang.Integer>>>".trim()))) {
 actual.add((TypeDescriptors) _factory.constructType(javaType.getRawClass()));
 }
 assertEquals(expected, actual);
 }
}