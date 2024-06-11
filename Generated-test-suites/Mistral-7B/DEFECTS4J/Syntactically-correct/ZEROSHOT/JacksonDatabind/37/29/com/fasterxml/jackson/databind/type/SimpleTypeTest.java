package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = SimpleType.construct(String.class);
        SimpleType simpleType3 = SimpleType.constructUnsafe(String.class);

        assertEquals(simpleType1.getRawClass(), String.class);
        assertEquals(simpleType2.getRawClass(), String.class);
        assertEquals(simpleType3.getRawClass(), String.class);

        assertEquals(simpleType1, simpleType2);
        assertEquals(simpleType1, simpleType3);
    }

    @Test
    public void testNarrowingSimpleType() {
        SimpleType stringType = new SimpleType(String.class);
        SimpleType stringSubType = new SimpleType(String[].class);

        JavaType narrowedType = stringType._narrow(String[].class);

        assertNull(narrowedType);

        narrowedType = stringSubType._narrow(String.class);
        assertEquals(stringSubType, narrowedType);
    }

    @Test
    public void testSimpleTypeWithHandlers() {
        SimpleType simpleType = new SimpleType(String.class);

        JavaType withTypeHandler = simpleType.withTypeHandler(null);
        JavaType withValueHandler = simpleType.withValueHandler(null);

        assertEquals(simpleType, withTypeHandler);
        assertEquals(simpleType, withValueHandler);

        SimpleType withTypeHandlerNull = new SimpleType(String.class, null, null, null, null, null, false);
        SimpleType withValueHandlerNull = new SimpleType(String.class, null, null, null, null, null, false);

        JavaType withTypeHandlerInstance = simpleType.withTypeHandler(new Object() {});
        JavaType withValueHandlerInstance = simpleType.withValueHandler(new Object() {});

        assertNotEquals(simpleType, withTypeHandlerNull);
        assertNotEquals(simpleType, withValueHandlerNull);
        assertNotEquals(simpleType, withTypeHandlerInstance);
        assertNotEquals(simpleType, withValueHandlerInstance);

        assertEquals(simpleType.getClass(), withTypeHandlerInstance.getClass());
        assertEquals(simpleType.getClass(), withValueHandlerInstance.getClass());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);

        JavaType withStaticTyping = simpleType.withStaticTyping();
        JavaType withoutStaticTyping = simpleType.withStaticTyping().withoutStaticTyping();

        assertFalse(simpleType.isStaticTyping());
        assertTrue(withStaticTyping.isStaticTyping());
        assertFalse(withoutStaticTyping.isStaticTyping());

        assertEquals(simpleType.getClass(), withStaticTyping.getClass());
        assertEquals(simpleType.getClass(), withoutStaticTyping.getClass());
    }

    @Test
    public void testRefineSimpleType() {
        SimpleType simpleType = new SimpleType(String.class);

        JavaType refinedType = simpleType.refine(Object.class, null, null, null);

        assertNull(refinedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRefineWithInvalidClass() {
        SimpleType simpleType = new SimpleType(String.class);

        simpleType.refine(Map.class, null, null, null);
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType unsafeSimpleType = SimpleType.constructUnsafe(String.class);

        assertEquals(simpleType, unsafeSimpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidClass() {
        SimpleType.construct(Map.class);
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);

        String toString = simpleType.toString();

        assertTrue(toString.contains("[simple type"));
        assertTrue(toString.contains("class " + String.class.getName()));
    }
}