package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings());
        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType constructedJavaType = new SimpleType(stringType).getRawType();
        assertEquals(stringJavaType, constructedJavaType);
    }

    @Test
    public void testNarrowingSimpleType() {
        Class<String> stringClass = String.class;
        Class<Integer> intClass = Integer.class;
        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings());
        SimpleType intType = new SimpleType(intClass, TypeBindings.emptyBindings());

        JavaType narrowedType = stringType.narrow(intClass);
        assertNull(narrowedType);

        intType = new SimpleType(intType.getRawType())._narrow(stringClass);
        JavaType intStringType = new SimpleType(intClass, TypeBindings.emptyBindings()).withContentType(stringType.getRawType());
        assertEquals(intStringType.getRawType(), intType.getRawType());
    }

    @Test
    public void testGetCanonicalName() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings());

        String canonicalName = stringType.getCanonicalName();
        assertEquals("java.lang.String", canonicalName);
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings());
        StringBuilder sb = new StringBuilder();
        stringType.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings());
        StringBuilder sb = new StringBuilder();
        stringType.getGenericSignature(sb);
        assertEquals("java.lang.String", sb.toString());

        Class<List<String>> listOfStringClass = new ArrayList<String>().getClass();
        SimpleType listOfStringType = new SimpleType(listOfStringClass, TypeBindings.emptyBindings());
        sb.setLength(0);
        listOfStringType.getGenericSignature(sb);
        assertEquals("java.util.List<java.lang.String>", sb.toString());
    }

    @Test
    public void testEquals() {
        Class<String> stringClass = String.class;
        SimpleType stringType1 = new SimpleType(stringClass, TypeBindings.emptyBindings());
        SimpleType stringType2 = new SimpleType(stringClass, TypeBindings.emptyBindings());
        SimpleType intType = new SimpleType(Integer.class, TypeBindings.emptyBindings());

        assertTrue(stringType1.equals(stringType1));
        assertTrue(stringType1.equals(stringType2));
        assertFalse(intType.equals(stringType1));
        assertFalse(stringType1.equals(intType));
    }

    @Test
    public void testConstructUnsafe() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings());
        SimpleType unsafeStringType = SimpleType.constructUnsafe(stringClass);
        assertEquals(stringType.getRawType(), unsafeStringType.getRawType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructDeprecated() {
        Class<String> stringClass = String.class;
        SimpleType.construct(stringClass);
    }
}