package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(String.class);
        SimpleType simpleType = SimpleType.construct(String.class, javaType);
        ReferenceType referenceType = ReferenceType.construct(Integer.class, javaType, null, null);

        assertNotNull(referenceType);
        assertEquals(Integer.class, referenceType.getRawClass());
        assertNotNull(referenceType.getReferencedType());
        assertEquals(javaType, referenceType.getReferencedType());
    }

    @Test
    public void testReferenceTypeTypeHandler() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(String.class);
        SimpleType simpleType = SimpleType.construct(String.class, javaType);
        ReferenceType referenceType = ReferenceType.construct(Integer.class, javaType, null, null);

        JavaType withTypeHandler = referenceType.withTypeHandler(new ClassType(List.class));

        assertNotNull(withTypeHandler);
        assertEquals(List.class, withTypeHandler.getRawClass());
        assertNotNull(withTypeHandler.getReferencedType());
        assertEquals(javaType, withTypeHandler.getReferencedType());
    }

    @Test
    public void testReferenceTypeValueHandler() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(String.class);
        SimpleType simpleType = SimpleType.construct(String.class, javaType);
        ReferenceType referenceType = ReferenceType.construct(Integer.class, javaType, null, null);

        JavaType withValueHandler = referenceType.withValueHandler(new ClassType(ArrayList.class));

        assertNotNull(withValueHandler);
        assertEquals(ArrayList.class, withValueHandler.getRawClass());
        assertNotNull(withValueHandler.getReferencedType());
        assertEquals(javaType, withValueHandler.getReferencedType());
    }

    @Test
    public void testReferenceTypeWithStaticTyping() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(String.class);
        SimpleType simpleType = SimpleType.construct(String.class, javaType);
        ReferenceType referenceType = ReferenceType.construct(Integer.class, javaType, null, null, false);

        JavaType withStaticTyping = referenceType.withStaticTyping();

        assertNotNull(withStaticTyping);
        assertTrue(withStaticTyping.isStatic());
        assertEquals(Integer.class, withStaticTyping.getRawClass());
        assertNotNull(withStaticTyping.getReferencedType());
        assertEquals(javaType, withStaticTyping.getReferencedType());
    }

    @Test
    public void testReferenceTypeBuildCanonicalName() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType1 = typeFactory.constructType(String.class);
        JavaType javaType2 = typeFactory.constructType(Integer.class);

        SimpleType simpleType = SimpleType.construct(String.class, javaType1);
        ReferenceType referenceType = ReferenceType.construct(String[].class, javaType2);

        String canonicalName = referenceType.buildCanonicalName();

        assertEquals("[Ljava.lang.String;", canonicalName);
    }

    @Test
    public void testReferenceTypeContainedTypeCount() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(List.class);
        JavaType javaTypeElementType = typeFactory.constructType(String.class);

        SimpleType simpleType = SimpleType.construct(List.class, javaType);
        ReferenceType referenceType = ReferenceType.construct(List.class, javaTypeElementType);

        int containedTypeCount = referenceType.containedTypeCount();

        assertEquals(1, containedTypeCount);
    }

    @Test
    public void testReferenceTypeContainedType() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(List.class);
        JavaType javaTypeElementType = typeFactory.constructType(String.class);

        SimpleType simpleType = SimpleType.construct(List.class, javaType);
        ReferenceType referenceType = ReferenceType.construct(List.class, javaTypeElementType);

        JavaType containedType = referenceType.containedType(0);

        assertNotNull(containedType);
        assertEquals(javaTypeElementType, containedType);
    }

    @Test
    public void testReferenceTypeContainedTypeName() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(List.class);
        JavaType javaTypeElementType = typeFactory.constructType(String.class);

        SimpleType simpleType = SimpleType.construct(List.class, javaType);
        ReferenceType referenceType = ReferenceType.construct(List.class, javaTypeElementType);

        String containedTypeName = referenceType.containedTypeName(0);

        assertEquals("T", containedTypeName);
    }

    @Test
    public void testReferenceTypeGetParameterSource() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(List.class);
        JavaType javaTypeElementType = typeFactory.constructType(String.class);

        SimpleType simpleType = SimpleType.construct(List.class, javaType);
        ReferenceType referenceType = ReferenceType.construct(List[].class, javaTypeElementType);

        Class<?> parameterSource = referenceType.getParameterSource();

        assertEquals(List[].class, parameterSource);
    }

    @Test
    public void testReferenceTypeGetErasedSignature() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(List.class);
        JavaType javaTypeElementType = typeFactory.constructType(String.class);

        SimpleType simpleType = SimpleType.construct(List.class, javaType);
        ReferenceType referenceType = ReferenceType.construct(List[].class, javaTypeElementType);

        StringBuilder erasedSignature = referenceType.getErasedSignature(new StringBuilder());

        assertEquals("Ljava.util.List;", erasedSignature.toString());
    }

    @Test
    public void testReferenceTypeGetGenericSignature() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(List.class);
        JavaType javaTypeElementType = typeFactory.constructType(String.class);

        SimpleType simpleType = SimpleType.construct(List.class, javaType);
        ReferenceType referenceType = ReferenceType.construct(List[].class, javaTypeElementType);

        StringBuilder genericSignature = referenceType.getGenericSignature(new StringBuilder());

        assertEquals("List<T>", genericSignature.toString());
    }

    @Test
    public void testReferenceTypeToString() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(List.class);
        JavaType javaTypeElementType = typeFactory.constructType(String.class);

        SimpleType simpleType = SimpleType.construct(List.class, javaType);
        ReferenceType referenceType = ReferenceType.construct(List[].class, javaTypeElementType);

        String stringRepresentation = referenceType.toString();

        assertEquals("[Ljava.lang.Object;<T extends java.lang.Object>]", stringRepresentation);
    }

    @Test
    public void testReferenceTypeEquals() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType1 = typeFactory.constructType(List.class);
        JavaType javaType1ElementType = typeFactory.constructType(String.class);
        JavaType javaType2 = typeFactory.constructType(List.class);
        JavaType javaType2ElementType = typeFactory.constructType(Integer.class);

        SimpleType simpleType1 = SimpleType.construct(List.class, javaType1);
        ReferenceType referenceType1 = ReferenceType.construct(List[].class, javaType1ElementType);
        SimpleType simpleType2 = SimpleType.construct(List.class, javaType2);
        ReferenceType referenceType2 = ReferenceType.construct(List[].class, javaType2ElementType);

        assertTrue(referenceType1.equals(referenceType1));
        assertFalse(referenceType1.equals(simpleType1));
        assertFalse(referenceType1.equals(referenceType2));
    }
}