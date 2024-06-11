package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test constructing a SimpleType instance using the constructor that takes a Class<?> argument
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        assertEquals(String.class, stringType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), stringType.getBindings());
        assertNull(stringType.getSuperClass());
        assertNull(stringType.getSuperInterfaces());
        assertFalse(stringType.isStatic());
    
        // Test constructing a SimpleType instance using the copy constructor
        SimpleType intType = new SimpleType(Integer.TYPE);
        SimpleType stringTypeFromIntType = new SimpleType(intType);
        assertEquals(String.class, stringTypeFromIntType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), stringTypeFromIntType.getBindings());
        assertNull(stringTypeFromIntType.getSuperClass());
        assertNull(stringTypeFromIntType.getSuperInterfaces());
        assertFalse(stringTypeFromIntType.isStatic());
    }

    @Test
    public void testSimpleTypeMethods() {
        // Test getErasedSignature and getGenericSignature methods
        Class<List<String>> listStringClass = new ArrayList<String>().getClass();
        SimpleType listStringType = new SimpleType(listStringClass);
        StringBuilder erasedSignature = new StringBuilder();
        listStringType.getErasedSignature(erasedSignature);
        assertEquals("java.util.List", erasedSignature.toString());
        
        StringBuilder genericSignature = new StringBuilder();
        listStringType.getGenericSignature(genericSignature);
        assertEquals("[Ljava.lang.Object;", genericSignature.toString());

        // Test isContainerType method
        assertFalse(listStringType.isContainerType());

        // Test equals method
        SimpleType sameType = new SimpleType(listStringClass);
        assertTrue(listStringType.equals(sameType));
        SimpleType differentType = new SimpleType(Integer.TYPE);
        assertFalse(listStringType.equals(differentType));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionConstruct() {
        // Test the deprecated construct method which does not properly handle inspection of super-types
        Class<Map<String, Integer>> mapClass = new HashMap<String, Integer>().getClass();
        SimpleType mapType = SimpleType.construct(mapClass);
    }
}