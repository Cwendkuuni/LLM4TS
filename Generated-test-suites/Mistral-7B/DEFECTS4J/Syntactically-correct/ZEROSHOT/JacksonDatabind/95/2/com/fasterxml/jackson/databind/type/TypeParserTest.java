package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeDescriptor;
import com.fasterxml.jackson.databind.jsontype.TypeDescriptor$UnknownType;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeParserTest {

 private TypeFactory typeFactory;
 private TypeParser typeParser;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 typeFactory = new TypeFactoryBuilder()
 .baseAttributeFinder(ClassUtil::findCreator)
 .build();
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testEmptyString() {
 JavaType type = typeParser.parse(" ");
 assertNull(type);
 }

 @Test
 public void testSimpleClass() {
 JavaType type = typeParser.parse("int");
 assertEquals(TypeDescriptors.primitiveValueType(Integer.TYPE), type);
 }

 @Test
 public void testFullyQualifiedClass() {
 JavaType type = typeParser.parse("java.lang.String");
 assertEquals(TypeDescriptors.fromClass(String.class), type);
 }

 @Test
 public void testClassWithGenerics() {
 JavaType type = typeParser.parse("java.util.List<java.lang.String>");
 TypeBindings bindings = ((TypeDescriptor) type).getBindings();
 assertEquals(String.class, bindings.getGenericTypeArguments().get(0).getRawClass());
 }

 @Test
 public void testClassWithGenericsAndInnerClass() {
 JavaType type = typeParser.parse("java.util.List<java.util.ArrayList<java.lang.String>>");
 TypeBindings bindings = ((TypeDescriptor) type).getBindings();
 assertEquals(ArrayList.class, bindings.getGenericTypeArguments().get(1).getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidClass() {
 typeParser.parse("java.x.String");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnmatchedClosingBracket() {
 typeParser.parse("java.lang.String<");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingClosingBracket() {
 typeParser.parse("java.lang.String<java.util.List<java.lang.String>");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingComma() {
 typeParser.parse("java.lang.String<java.util.List java.lang.String>");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnexpectedToken() {
 typeParser.parse("java.lang.String<java.util.List<java.lang.String>+");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnexpectedEndOfString() {
 typeParser.parse("java.lang.String<java.util.List<java.lang.String>");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnknownType() {
 typeParser.parse("com.unknown.MyUnknownType<java.util.List<java.lang.String>>");
 }

 @Test
 public void testTypeParserWithFactory() {
 TypeParser parser1 = new TypeParser(typeFactory);
 JavaType type1 = parser1.parse("java.lang.String<java.util.List<java.lang.String>>");

 TypeParser parser2 = typeParser.withFactory(typeFactory);
 JavaType type2 = parser2.parse("java.lang.String<java.util.List<java.lang.String>>");

 assertEquals(type1, type2);
 }
}