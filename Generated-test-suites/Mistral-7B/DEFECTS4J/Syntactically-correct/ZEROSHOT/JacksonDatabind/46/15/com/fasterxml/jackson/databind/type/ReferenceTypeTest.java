package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, javaType, null, null);

        assertNotNull(referenceType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(javaType.getTypeId(), referenceType.getTypeId());
        assertEquals(javaType, referenceType.getContentType());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStatic());
    }

    @Test
    public void testReferenceTypeTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, handler, false);

        JavaType newTypeHandler = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);

        assertEquals(handler, referenceType.getTypeHandler());
        assertEquals(newTypeHandler, newReferenceType.getTypeHandler());
    }

    @Test
    public void testReferenceTypeValueHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, handler, null, false);

        JavaType newValueHandler = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType newReferenceType = referenceType.withValueHandler(newValueHandler);

        assertEquals(handler, referenceType.getValueHandler());
        assertEquals(newValueHandler, newReferenceType.getValueHandler());
    }

    @Test
    public void testReferenceTypeStaticTyping() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);

        ReferenceType staticReferenceType = referenceType.withStaticTyping();

        assertFalse(referenceType.isStatic());
        assertTrue(staticReferenceType.isStatic());
    }

    @Test
    public void testReferenceTypeCanonicalName() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);

        assertEquals("class java.lang.String<T>", referenceType.buildCanonicalName());
    }

    @Test
    public void testReferenceTypeNarrow() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType narrowType = TypeFactory.defaultInstance().constructType(StringBuilder.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);

        JavaType narrowedType = referenceType._narrow(StringBuilder.class);

        assertEquals(narrowedType.getTypeId(), "class java.lang.StringBuilder");
    }

    @Test
    public void testReferenceTypeGetReferencedType() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);

        assertEquals(javaType, referenceType.getReferencedType());
    }

    @Test
    public void testReferenceTypeIsReferenceType() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);

        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testReferenceTypeContainedTypeCount() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);

        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testReferenceTypeContainedType() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);

        JavaType containedType = referenceType.containedType(0);

        assertEquals(javaType, containedType);
    }

    @Test
    public void testReferenceTypeContainedTypeName() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);

        String containedTypeName = referenceType.containedTypeName(0);

        assertEquals("T", containedTypeName);
    }

    @Test
    public void testReferenceTypeParameterSource() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);

        assertEquals(stringClass, referenceType.getParameterSource());
    }

    @Test
    public void testReferenceTypeErasedSignature() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);

        StringBuilder expectedSignature = new StringBuilder("Ljava/lang/String;");
        StringBuilder actualSignature = referenceType.getErasedSignature(new StringBuilder());

        assertEquals(expectedSignature.toString(), actualSignature.toString());
    }

    @Test
    public void testReferenceTypeGenericSignature() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);

        StringBuilder expectedSignature = new StringBuilder("Ljava/lang/String;<T:Ljava/lang/Object;>T");
        StringBuilder actualSignature = referenceType.getGenericSignature(new StringBuilder());

        assertEquals(expectedSignature.toString(), actualSignature.toString());
    }
}