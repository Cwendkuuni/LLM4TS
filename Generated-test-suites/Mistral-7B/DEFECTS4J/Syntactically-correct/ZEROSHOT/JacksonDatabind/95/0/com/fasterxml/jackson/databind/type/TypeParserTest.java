package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeParser;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeDescriptor;

/**
 * Test class for {@link TypeParser}
 */
public class TypeParserTest {

 private TypeFactory typeFactory;
 private TypeParser typeParser;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 typeFactory = new TypeFactory();
 typeParser = new TypeParser(typeFactory);
 }

 @Test
 public void testParseSimpleClass() throws Exception {
 String canonicalType = "java.lang.String";
 JavaType javaType = typeParser.parse(canonicalType);
 TypeBindings bindings = javaType.getTypeBindings();
 assertEquals(TypeBindings.empty(), bindings);
 assertEquals(String.class, javaType.getRawClass());
 }

 @Test
 public void testParseList() throws Exception {
 String canonicalType = "java.util.List<java.lang.Integer>";
 JavaType javaType = typeParser.parse(canonicalType);
 TypeBindings bindings = javaType.getTypeBindings();
 assertEquals(TypeBindings.empty(), bindings);
 JavaType elementType = ((TypeDescriptor) javaType.getParameterTypes()[0])
 .getRawType();
 assertEquals(Integer.class, elementType.getRawClass());
 assertEquals(List.class, javaType.getRawClass());
 }

 @Test
 public void testParseMap() throws Exception {
 String canonicalType = "java.util.Map<java.lang.String, java.lang.Integer>";
 JavaType javaType = typeParser.parse(canonicalType);
 TypeBindings bindings = javaType.getTypeBindings();
 assertEquals(TypeBindings.empty(), bindings);
 JavaType keyType = ((TypeDescriptor) javaType.getParameterTypes()[0])
 .getRawType();
 assertEquals(String.class, keyType.getRawClass());
 JavaType valueType = ((TypeDescriptor) javaType.getParameterTypes()[1])
 .getRawType();
 assertEquals(Integer.class, valueType.getRawClass());
 assertEquals(Map.class, javaType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingClosingBracket() throws Exception {
 String canonicalType = "java.util.List<java.lang.Integer<";
 typeParser.parse(canonicalType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingOpeningBracket() throws Exception {
 String canonicalType = "java.util.List java.lang.Integer>";
 typeParser.parse(canonicalType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMalformedCanonicalType() throws Exception {
 String canonicalType = "java.util.List<java.lang.Integer<java.lang.String>";
 typeParser.parse(canonicalType);
 }

 @Test
 public void testParseNestedTypes() throws Exception {
 String canonicalType = "java.util.List<java.util.Map<java.lang.String, " +
 "java.util.List<java.lang.Integer, java.lang.String>>>";
 JavaType javaType = typeParser.parse(canonicalType);
 TypeBindings bindings = javaType.getTypeBindings();
 JavaType keyType1 = ((TypeDescriptor) javaType.getParameterTypes()[0])
 .getRawType();
 assertEquals(Map.class, keyType1.getRawClass());
 JavaType valueType1 = ((TypeDescriptor) keyType1.getParameterTypes()[0])
 .getRawType();
 assertEquals(String.class, valueType1.getRawClass());
 JavaType listType1 = ((TypeDescriptor) keyType1.getParameterTypes()[1])
 .getRawType();
 JavaType elementType1 = ((TypeDescriptor) listType1.getParameterTypes()[0])
 .getRawType();
 assertEquals(Integer.class, elementType1.getRawClass());
 assertEquals(List.class, listType1.getRawClass());
 JavaType listType2 = ((TypeDescriptor) javaType.getParameterTypes()[1])
 .getRawType();
 assertEquals(Map.class, listType2.getRawClass());
 }
}