package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SimpleType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ReferenceTypeTest {

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = SimpleType.construct(int[].class);
        JavaType refType = JavaType.construct(int[].class, int.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(referenceType.getClass(), ReferenceType.class);
        assertEquals(referenceType.getReferencedType(), refType);
    }

    @Test
    public void testConstructor() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = JavaType.construct(String.class);
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);
        assertEquals(referenceType.getClass(), ReferenceType.class);
        assertEquals(referenceType.getReferencedType(), refType);
        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testConstructorWithAnchorType() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = JavaType.construct(String.class);
        JavaType anchorType = JavaType.construct(Integer.class);
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);
        assertEquals(referenceType.getClass(), ReferenceType.class);
        assertEquals(referenceType.getReferencedType(), refType);
        assertEquals(referenceType.getAnchorType(), anchorType);
    }

    @Test
    public void testWithContentType() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = JavaType.construct(String.class);
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);
        JavaType newContentType = JavaType.construct(Integer.class);
        ReferenceType newReferenceType = referenceType.withContentType(newContentType);
        assertEquals(newReferenceType.getClass(), ReferenceType.class);
        assertEquals(newReferenceType.getReferencedType(), refType);
        assertEquals(newReferenceType.getContentType(), newContentType);
        assertNull(newReferenceType.getAnchorType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = JavaType.construct(String.class);
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = new Object() {};
        boolean asStatic = false;
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);
        Object newTypeHandler = new Object() {};
        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);
        assertEquals(newReferenceType.getClass(), ReferenceType.class);
        assertEquals(newReferenceType.getReferencedType(), refType);
        assertEquals(newReferenceType.getTypeHandler(), newTypeHandler);
        assertNull(newReferenceType.getAnchorType());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = JavaType.construct(String.class);
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = new Object() {};
        boolean asStatic = false;
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);
        Object newContentTypeHandler = new Object() {};
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newContentTypeHandler);
        assertEquals(newReferenceType.getClass(), ReferenceType.class);
        assertEquals(newReferenceType.getReferencedType(), refType);
        assertEquals(newReferenceType.getContentTypeHandler(), newContentTypeHandler);
        assertNull(newReferenceType.getAnchorType());
    }

    @Test
    public void testWithValueHandler() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = JavaType.construct(String.class);
        JavaType anchorType = null;
        Object valueHandler = new Object() {};
        Object typeHandler = null;
        boolean asStatic = false;
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);
        Object newValueHandler = new Object() {};
        ReferenceType newReferenceType = referenceType.withValueHandler(newValueHandler);
        assertEquals(newReferenceType.getClass(), ReferenceType.class);
        assertEquals(newReferenceType.getReferencedType(), refType);
        assertEquals(newReferenceType.getValueHandler(), newValueHandler);
        assertNull(newReferenceType.getAnchorType());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = JavaType.construct(String.class);
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = new Object() {};
        boolean asStatic = false;
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);
        Object newContentValueHandler = new Object() {};
        ReferenceType newReferenceType = referenceType.withContentValueHandler(newContentValueHandler);
        assertEquals(newReferenceType.getClass(), ReferenceType.class);
        assertEquals(newReferenceType.getReferencedType(), refType);
        assertEquals(newReferenceType.getContentValueHandler(), newContentValueHandler);
        assertNull(newReferenceType.getAnchorType());
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = JavaType.construct(String.class);
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertEquals(newReferenceType.getClass(), ReferenceType.class);
        assertEquals(newReferenceType.getReferencedType(), refType);
        assertTrue(newReferenceType.isStaticTyping());
        assertNull(newReferenceType.getAnchorType());
    }

    @Test
    public void testRefine() {
        Class<String> rawType = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType refType = JavaType.construct(String.class);
        JavaType newReferenceType = refType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(newReferenceType.getClass(), ReferenceType.class);
        assertEquals(newReferenceType.getReferencedType(), refType);
        assertEquals(newReferenceType.getContentType(), refType);
        assertEquals(newReferenceType.getTypeHandler(), refType.<Object>getTypeHandler());
        assertEquals(newReferenceType.getValueHandler(), refType.<Object>getValueHandler());
        assertNull(newReferenceType.getAnchorType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        referenceType.getErasedSignature(sb);
        assertEquals(sb.toString(), "[Ljava/lang/String;");
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        referenceType.getGenericSignature(sb);
        assertEquals(sb.toString(), "[Ljava/lang/String;<T extends java.lang.Object;>T;");
    }

    @Test
    public void testGetCanonicalName() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        assertEquals(referenceType.buildCanonicalName(), "[Ljava/lang/String;");
    }
}