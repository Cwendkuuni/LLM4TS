package com.example.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {

    static TypeFactory simpleTypeFactory = new SimpleTypeFactory();

    @Test
    public void testParse() throws Exception {
        String canonical = "java.lang.String";
        TypeParser parser = new TypeParser(simpleTypeFactory);
        JavaType actual = parser.parse(canonical);
        JavaType expected = simpleTypeFactory.constructType(canonical);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseType() throws Exception {
        MyTokenizer tokens = new MyTokenizer("java.lang.String");
        JavaType actual = new TypeParser(simpleTypeFactory).parseType(tokens);
        JavaType expected = simpleTypeFactory.constructType("java.lang.String");
        assertEquals(expected, actual);
    }

    @Test
    public void testParseTypes() throws Exception {
        MyTokenizer tokens = new MyTokenizer("[Ljava.lang.String;");
        List<JavaType> actual = new TypeParser(simpleTypeFactory).parseTypes(tokens);
        JavaType[] expected = {simpleTypeFactory.constructType("java.lang.String"), simpleTypeFactory.constructType("java.lang.String")};
        assertEquals(Arrays.asList(expected), actual);
    }

    @Test
    public void testEmptyString() {
        TypeParser parser = new TypeParser(simpleTypeFactory);
        assertThrows(IllegalArgumentException.class, () -> parser.parse(""));
    }

    @Test
    public void testInvalidClassName() {
        TypeParser parser = new TypeParser(simpleTypeFactory);
        assertThrows(IllegalArgumentException.class, () -> parser.parse("invalidClassName"));
    }

    @Test
    public void testParseTypesMissingGreaterThan() {
        MyTokenizer tokens = new MyTokenizer("[Ljava.lang.String");
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(simpleTypeFactory).parseTypes(tokens));
    }

    @Test
    public void testUnexpectedToken() {
        MyTokenizer tokens = new MyTokenizer("java.lang.String<int>");
        tokens.pushBack("com");
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(simpleTypeFactory).parseTypes(tokens));
    }

    @Test
    public void testParseGenericTypeNoArguments() throws Exception {
        TypeParser parser = new TypeParser(simpleTypeFactory);
        JavaType actual = parser.parse("<java.lang.String>");
        JavaType expected = simpleTypeFactory.constructType("java.lang.String");
        assertEquals(expected, actual);
    }

    @Test
    public void testParseGenericTypeMultipleArguments() throws Exception {
        TypeParser parser = new TypeParser(simpleTypeFactory);
        JavaType actual = parser.parse("<java.lang.String, java.util.List<java.lang.Integer>>");
        JavaType expected = simpleTypeFactory.constructType("java.lang.String, java.util.List<java.lang.Integer>");
        assertEquals(expected, actual);
    }
}