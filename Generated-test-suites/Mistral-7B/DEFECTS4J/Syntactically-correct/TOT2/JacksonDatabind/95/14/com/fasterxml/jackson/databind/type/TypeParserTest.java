import org.junit.Assert.*; // for Assert.assertEquals
import org.junit.Test;
import org.junit.experimental.annotations.ExpectedException;

import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {
    @Test
    public void testParse_ValidType() {
        TypeFactory typeFactory = new DefaultTypeFactory();
        TypeParser typeParser = new TypeParser(typeFactory);
        String canonicalType = "java.lang.Integer";
        JavaType expectedType = typeFactory.constructType(canonicalType);

        JavaType actualType = typeParser.parse(canonicalType);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testParseType_ValidClass() {
        // ...
    }

    @Test
    public void testParseType_ClassWithGenerics() {
        // ...
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidInput() {
        TypeFactory typeFactory = new DefaultTypeFactory();
        TypeParser typeParser = new TypeParser(typeFactory);

        String canonicalType = "";
        try {
            JavaType actualType = typeParser.parse(canonicalType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_UnknownClassName() {
        TypeFactory typeFactory = new DefaultTypeFactory();
        TypeParser typeParser = new TypeParser(typeFactory);

        String canonicalType = "com.unknown.UnknownClass";
        try {
            JavaType actualType = typeParser.parse(canonicalType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Can not locate class 'com.unknown.UnknownClass'";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_MissingClosingGreaterThan() {
        TypeFactory typeFactory = new DefaultTypeFactory();
        TypeParser typeParser = new TypeParser(typeFactory);

        String canonicalType = "java.lang.String<";
        try {
            JavaType actualType = typeParser.parse(canonicalType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Failed to parse type 'java.lang.String<': Unexpected end-of-string";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_MissingCommas() {
        TypeFactory typeFactory = new DefaultTypeFactory();
        TypeParser typeParser = new TypeParser(typeFactory);

        String canonicalType = "java.lang.String List<java.lang.Integer>";
        try {
            JavaType actualType = typeParser.parse(canonicalType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Failed to parse type 'java.lang.String List<java.lang.Integer>': Unexpected token 'List'";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_UnrecognizedToken() {
        TypeFactory typeFactory = new DefaultTypeFactory();
        TypeParser typeParser = new TypeParser(typeFactory);

        String canonicalType = "java.lang.String @MyAnnotation<java.lang.Integer>";
        try {
            JavaType actualType = typeParser.parse(canonicalType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Failed to parse type 'java.lang.String @MyAnnotation<java.lang.Integer>': Unexpected token '@'";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testParse_ValidTypeWithGenerics() {
        // ...
    }

    // ...
}