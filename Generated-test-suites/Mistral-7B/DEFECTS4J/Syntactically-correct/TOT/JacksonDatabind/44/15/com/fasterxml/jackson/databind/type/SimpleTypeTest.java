package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test default constructor
        SimpleType stringType = new SimpleType(String.class);
        assertEquals("java.lang.String", stringType.toCanonical().toString());
        assertTrue(stringType.isContainerType());
        assertFalse(stringType.hasSuperClass());
        assertNull(stringType.getSuperClass());

        // Test constructor with superClass and empty bindings
        SimpleType listType = new SimpleType(ArrayList.class, TypeBindings.emptyBindings());
        assertEquals("java.util.ArrayList", listType.toCanonical().toString());
        assertTrue(listType.isContainerType());
        assertTrue(listType.hasSuperClass());
        assertNotNull(listType.getSuperClass());
        assertEquals(ArrayList.class, listType.getSuperClass());

        // Test copy constructor
        SimpleType stringTypeCopy = new SimpleType(stringType);
        assertEquals("java.lang.String", stringTypeCopy.toCanonical().toString());

        // Test constructor with bindings and superClass
        SimpleType mapType = new SimpleType(HashMap.class, TypeBindings.emptyBindings(), String.class);
        assertEquals("java.util.Map<java.lang.String,?>", mapType.toCanonical().toString());
        assertTrue(mapType.isContainerType());
        assertTrue(mapType.hasSuperClass());
        assertNotNull(mapType.getSuperClass());
        assertEquals(Map.class, mapType.getSuperClass());

        // Test constructor with bindings, superClass, superInterfaces, valueHandler, typeHandler, and asStatic
        SimpleType intType = new SimpleType(int.class, TypeBindings.emptyBindings(), Integer.class, new Class[0], null, null, false);
        assertEquals("int", intType.toCanonical().toString());
        assertFalse(intType.isContainerType());
        assertFalse(intType.hasSuperClass());
        assertNull(intType.getSuperClass());
    }

    @Test
    public void testConstructUnsafeMethod() {
        // Test constructUnsafe method
        SimpleType stringTypeUnsafe = SimpleType.constructUnsafe(String.class);
        assertEquals("java.lang.String", stringTypeUnsafe.toCanonical().toString());
        assertTrue(stringTypeUnsafe.isContainerType());
        assertFalse(stringTypeUnsafe.hasSuperClass());
        assertNull(stringTypeUnsafe.getSuperClass());

        try {
            SimpleType arrayListTypeUnsafe = SimpleType.constructUnsafe(ArrayList.class);
            fail("Should not be able to construct ArrayList type using constructUnsafe method");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMethodDeprecated() {
        // Test construct method (deprecated)
        SimpleType stringTypeDeprecated = SimpleType.construct(String.class);
    }

    @Test
    public void testNarrowMethod() {
        // Test narrow method
        SimpleType stringType = new SimpleType(String.class);
        SimpleType stringSubclassType = new SimpleType(StringBuffer.class);
        JavaType narrowedType = stringType.narrow(StringBuffer.class);
        assertEquals(stringSubclassType.toCanonical().toString(), narrowedType.toCanonical().toString());
        assertSame(stringSubclassType, narrowedType);

        try {
            JavaType narrowedTypeFail = stringType.narrow(ArrayList.class);
            fail("Should not be able to narrow String to ArrayList");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithContentTypeMethod() {
        // Test withContentType method
        SimpleType stringType = new SimpleType(String.class);
        JavaType stringTypeWithContentType = stringType.withContentType(String.class);
        assertEquals("java.lang.String", stringTypeWithContentType.toCanonical().toString());
        assertEquals(stringType, stringTypeWithContentType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeMethodError() {
        // Test withContentType method error
        SimpleType stringType = new SimpleType(String.class);
        stringType.withContentType(ArrayList.class);
    }

    @Test
    public void testWithTypeHandlerMethod() {
        // Test withTypeHandler method
        SimpleType intType = new SimpleType(int.class);
        JavaType intTypeWithTypeHandler = intType.withTypeHandler(new IntTypeHandler());
        assertNotSame(intType, intTypeWithTypeHandler);
        assertEquals("int", intTypeWithTypeHandler.toCanonical().toString());
    }

    @Test
    public void testWithContentTypeHandlerMethod() {
        // Test withContentTypeHandler method
        SimpleType listType = new SimpleType(ArrayList.class);
        JavaType listTypeWithContentTypeHandler = listType.withContentTypeHandler(new ArrayListContentTypeHandler());
        assertNotSame(listType, listTypeWithContentTypeHandler);
        assertEquals("java.util.ArrayList<java.lang.String>", listTypeWithContentTypeHandler.toCanonical().toString());
    }

    @Test
    public void testWithValueHandlerMethod() {
        // Test withValueHandler method
        SimpleType intType = new SimpleType(int.class);
        JavaType intTypeWithValueHandler = intType.withValueHandler(new IntValueHandler());
        assertNotSame(intType, intTypeWithValueHandler);
        assertEquals("int", intTypeWithValueHandler.toCanonical().toString());
    }

    @Test
    public void testWithStaticTypingMethod() {
        // Test withStaticTyping method
        SimpleType intType = new SimpleType(int.class);
        JavaType intTypeWithStaticTyping = intType.withStaticTyping();
        assertNotSame(intType, intTypeWithStaticTyping);
        assertFalse(intTypeWithStaticTyping.isContainerType());
    }
}