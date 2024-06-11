package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.SimpleType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ReferenceTypeTest {

    private JavaType simpleType;
    private JavaType refType;
    private JavaType baseType;

    @Before
    public void setUp() {
        JavaTypeFactory typeFactory = TypeFactory.defaultInstance();
        simpleType = typeFactory.constructCollectionType(List.class, Integer.class);
        baseType = new SimpleType(Integer.class, TypeBindings.emptyBindings());
        refType = ReferenceType.upgradeFrom(baseType, simpleType);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType upgradedType = ReferenceType.upgradeFrom(simpleType, refType);
        assertEquals(refType, upgradedType);
    }

    @Test
    public void testGetContentType() {
        assertEquals(simpleType, refType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        assertEquals(simpleType, refType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        assertTrue(refType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        assertTrue(refType.hasContentType());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder expected = new StringBuilder();
        expected.append("[Ljava.lang.Integer;");
        StringBuilder actual = refType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder expected = new StringBuilder();
        expected.append("[Ljava.util.List;<Ljava.lang.Integer;>Ljava.lang.Object;");
        StringBuilder actual = refType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testConstruct() {
        JavaType newRefType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, simpleType);
        assertEquals(refType, newRefType);
    }

    @Test
    public void testWithContentType() {
        JavaType newRefType = refType.withContentType(simpleType);
        assertEquals(refType, newRefType);
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        JavaType newRefType = refType.withTypeHandler(handler);
        assertNotEquals(refType, newRefType);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        JavaType newRefType = refType.withContentTypeHandler(handler);
        assertNotEquals(refType, newRefType);
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        JavaType newRefType = refType.withValueHandler(handler);
        assertNotEquals(refType, newRefType);
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        JavaType newRefType = refType.withContentValueHandler(handler);
        assertNotEquals(refType, newRefType);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType newRefType = refType.withStaticTyping();
        assertNotEquals(refType, newRefType);
    }

    @Test
    public void testRefine() {
        JavaType newRefType = refType.refine(Integer[].class, TypeBindings.emptyBindings(), null, new Class[0]);
        assertEquals(new SimpleType(Integer[].class, TypeBindings.emptyBindings()), newRefType);
    }

    @Test
    public void testGetAnchorType() {
        assertNull(refType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        assertFalse(refType.isAnchorType());
    }

    @Test
    public void testToString() {
        String expected = "[reference type, class java.lang.Integer, referenced type [class java.util.List, class java.lang.Integer]]";
        String actual = refType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        JavaType anotherRefType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, simpleType, simpleType);
        assertTrue(refType.equals(refType));
        assertTrue(refType.equals(anotherRefType));
        assertFalse(refType.equals(null));
        assertFalse(refType.equals(simpleType));
    }
}