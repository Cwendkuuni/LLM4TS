package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeParser;
import com.fasterxml.jackson.databind.type.TypeUtils;

/**
 * Test class for TypeParser class.
 */
public class TypeParserTest {

    private static TypeFactory typeFactory;
    private static TypeParser typeParser;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setUpBeforeClass() {
        typeFactory = new TypeFactoryBuilder().build();
        typeParser = new TypeParser(typeFactory);
    }

    @Test
    public void testParseSimpleType() throws Exception {
        JavaType javaType = typeParser.parse("int");
        assertEquals(javaType.getRawClass(), Integer.TYPE);

        javaType = typeParser.parse("java.lang.String");
        assertEquals(javaType.getRawClass(), String.class);
    }

    @Test
    public void testParseClassWithGenerics() throws Exception {
        JavaType javaType = typeParser.parse("java.util.List<java.lang.Integer>");
        JavaType expectedType = TypeUtils.constructType(List.class, Integer.TYPE);
        assertEquals(javaType, expectedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidType() throws Exception {
        typeParser.parse("invalidType");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeWithUnexpectedTokens() throws Exception {
        String canonical = "java.lang.List<java.lang.Integer> unexpected";
        typeParser.parse(canonical);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeWithMissingEndParenthesis() throws Exception {
        String canonical = "java.lang.List<java.lang.Integer<";
        typeParser.parse(canonical);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeWithMissingComma() throws Exception {
        String canonical = "java.lang.List<java.lang.Integer java.lang.String>";
        typeParser.parse(canonical);
    }

    @Test
    public void testParseTypeWithNestedGenerics() throws Exception {
        JavaType javaType = typeParser.parse("java.util.List<java.util.Map<java.lang.String, java.lang.Integer>>");
        JavaType expectedType = TypeUtils.constructType(List.class, Map.class, String.class, Integer.TYPE);
        assertEquals(javaType, expectedType);
    }

    @Test
    public void testParseTypeWithMultipleTypes() throws Exception {
        JavaType javaType = typeParser.parse("java.util.List<java.lang.Integer, java.lang.String>");
        JavaType expectedType1 = TypeUtils.constructType(List.class, Integer.TYPE, String.class);
        JavaType expectedType2 = TypeUtils.constructType(List.class, String.class, Integer.TYPE);
        List<JavaType> expectedTypes = new ArrayList<>();
        expectedTypes.add(expectedType1);
        expectedTypes.add(expectedType2);
        JavaType expectedType = TypeBindings.create(List.class, expectedTypes);
        assertEquals(javaType, expectedType);
    }
}