package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() throws Exception {
        // Test constructing a SimpleType using the public constructor
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        assertEquals("java.lang.String", simpleType.toString());

        // Test constructing a SimpleType using the static factory method
        JavaType simpleTypeFromStatic = TypeFactory.defaultInstance().constructType(String.class.getName());
        assertEquals("java.lang.String", simpleTypeFromStatic.toString());

        // Test constructing a SimpleType using the copy constructor
        JavaType baseType = TypeFactory.defaultInstance().constructType(Number.class);
        JavaType copy = new SimpleType(baseType);
        assertEquals(Number.class, copy.getRawClass());
    }

    @Test
    public void testSimpleTypeNarrowing() throws Exception {
        // Test narrowing a SimpleType to a subclass
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType narrowed = intType.narrow(Integer[].class);
        assertEquals("java.lang.Integer[]", narrowed.toString());

        // Test narrowing a SimpleType to a superclass
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType wider = stringType.narrow(Object.class);
        assertEquals("java.lang.Object", wider.toString());

        // Test narrowing a SimpleType to an interface
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType interfaceType = listType.narrow(Iterable.class);
        assertEquals("java.lang.Iterable<?>", interfaceType.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeInvalidConstruction() throws Exception {
        // Test constructing a SimpleType for a map or collection class
        JavaType mapType = TypeFactory.defaultInstance().constructType(HashMap.class);
        JavaType collectionType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        TypeFactory.defaultInstance().constructType(mapType.getTypeId());
        TypeFactory.defaultInstance().constructType(collectionType.getTypeId());

        // Test constructing a SimpleType for an array class
        JavaType arrayType = TypeFactory.defaultInstance().constructType(int[].class);
        TypeFactory.defaultInstance().constructType(arrayType.getTypeId());
    }

    @Test
    public void testSimpleTypeWithContentTypeHandler() throws Exception {
        // Test setting and getting the content type handler
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType handlerStringType = TypeFactory.defaultInstance().constructType(StringHandler.class);
        JavaType stringTypeWithHandler = stringType.withContentTypeHandler(handlerStringType);
        assertEquals(StringHandler.class, stringTypeWithHandler.getContentTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeInvalidContentTypeHandler() throws Exception {
        // Test setting an invalid content type handler
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType handlerIntType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringTypeWithHandler = stringType.withContentTypeHandler(handlerIntType);
    }

    @Test
    public void testSimpleTypeWithValueHandler() throws Exception {
        // Test setting and getting the value handler
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType handlerStringType = TypeFactory.defaultInstance().constructType(StringHandler.class);
        JavaType stringTypeWithHandler = stringType.withValueHandler(handlerStringType);
        assertEquals(StringHandler.class, stringTypeWithHandler.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeInvalidValueHandler() throws Exception {
        // Test setting an invalid value handler
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType handlerIntType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringTypeWithHandler = stringType.withValueHandler(handlerIntType);
    }

    @Test
    public void testSimpleTypeWithStaticTyping() throws Exception {
        // Test setting and getting static typing
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType stringTypeStatic = stringType.withStaticTyping();
        assertEquals(true, stringTypeStatic.isStatic());

        JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType listTypeStatic = listType.withStaticTyping();
        assertEquals(false, listTypeStatic.isStatic());
    }

    @Test
    public void testSimpleTypeToString() throws Exception {
        // Test the toString method
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        assertEquals("[simple type, class java.lang.String]", stringType.toString());
    }
}