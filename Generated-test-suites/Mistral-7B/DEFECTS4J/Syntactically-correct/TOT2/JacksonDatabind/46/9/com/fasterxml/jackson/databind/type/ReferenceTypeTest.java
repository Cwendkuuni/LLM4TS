package com.example.junit;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstructor() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = null;
        Object typeHandler = null;
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        assertEquals(String.class, referenceType.getParameterSource());
        assertEquals(int.class, referenceType.getReferencedType().getType().getRawClass());
    }

    @Test
    public void testNullConstructorParameters() {
        assertThrows(NullPointerException.class, () -> new ReferenceType(null, _referencedType, _valueHandler, _typeHandler, _asStatic));
    }

    @Test
    public void testDifferentParameterTypes() {
        Class<String> cls1 = String.class;
        Class<Integer> cls2 = Integer.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = null;
        Object typeHandler = null;

        assertThrows(IllegalArgumentException.class, () -> new ReferenceType(cls1, refType, valueHandler, typeHandler, true));
        assertThrows(IllegalArgumentException.class, () -> new ReferenceType(cls2, refType, valueHandler, typeHandler, false));
    }

    @Test
    public void testNullValueHandlerAndTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = null;
        Object typeHandler = null;

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
    }

    @Test
    public void testNullHandlerInSetterMethods() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = null;
        Object typeHandler = null;
        Object h = null;

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        assertThrows(NullPointerException.class, () -> referenceType.withTypeHandler(h));
        assertThrows(NullPointerException.class, () -> referenceType.withContentTypeHandler(h));
        assertThrows(NullPointerException.class, () -> referenceType.withValueHandler(h));
        assertThrows(NullPointerException.class, () -> referenceType.withContentValueHandler(h));
    }

    @Test
    public void testEmptySbInGetErasedSignatureAndGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        String result = new ReferenceType(String.class, JavaType.construct(int.class), null, null, false).getErasedSignature(sb).toString();
        assertEquals("[Ljava/lang/String;", result);

        sb.setLength(0);
        result = new ReferenceType(String.class, JavaType.construct(int.class), null, null, false).getGenericSignature(sb).toString();
        assertEquals("[Ljava/lang/String;<T>Lcom.fasterxml.jackson.databind.JavaType;>", result);
    }

    @Test
    public void testNarrowWithSubclassOfPrimitiveType() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        JavaType subclass1 = JavaType.construct(byte.class);
        JavaType subclass2 = JavaType.construct(short.class);

        assertThrows(IllegalArgumentException.class, () -> referenceType._narrow(subclass1));
        assertThrows(IllegalArgumentException.class, () -> referenceType._narrow(subclass2));
    }

    @Test
    public void testNarrowWithSubclassNotOfReferencedType() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        Class<Integer> subclass = Integer.class;

        assertThrows(IllegalArgumentException.class, () -> referenceType._narrow(subclass));
    }

    @Test
    public void testIsReferenceTypeWithNullObject() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.construct(int.class), null, null, false);
        assertFalse(referenceType.isReferenceType());
        assertThrows(NullPointerException.class, () -> referenceType.isReferenceType(null));
    }

    @Test
    public void testGetReferencedTypeWhenNull() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.construct(int.class), null, null, false);
        assertNull(referenceType.getReferencedType());
    }

    @Test
    public void testContainsTypeWhenNoContainedTypes() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.construct(int.class), null, null, false);
        assertEquals(0, referenceType.containedTypeCount());
    }

    @Test
    public void testContainsTypeWithValidIndex() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.construct(int[].class), null, null, false);
        JavaType containedType = referenceType.containedType(0);
        assertEquals(int[].class, containedType.getType().getRawClass());
    }

    @Test
    public void testContainsTypeWithInvalidIndex() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.construct(int[].class), null, null, false);
        JavaType containedType = referenceType.containedType(1);
        assertNull(containedType);
    }

    @Test
    public void testContainsTypeNameWithValidIndex() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.construct(int[].class), null, null, false);
        String containedTypeName = referenceType.containedTypeName(0);
        assertEquals("T", containedTypeName);
    }

    @Test
    public void testContainsTypeNameWithInvalidIndex() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.construct(int[].class), null, null, false);
        String containedTypeName = referenceType.containedTypeName(1);
        assertNull(containedTypeName);
    }

    @Test
    public void testGetParameterSourceWhenNull() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.construct(int.class), null, null, false);
        assertNull(referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.construct(int.class), null, null, false);
        StringBuilder sb = new StringBuilder();
        String result = referenceType.getErasedSignature(sb).toString();
        assertEquals("[Ljava/lang/String;", result);
    }

    @Test
    public void testGetGenericSignature() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.construct(int.class), null, null, false);
        StringBuilder sb = new StringBuilder();
        String result = referenceType.getGenericSignature(sb).toString();
        assertEquals("[Ljava/lang/String;<T>Lcom.fasterxml.jackson.databind.JavaType;>", result);
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.construct(int.class), null, null, false);
        String result = referenceType.toString();
        String expected = "[reference type, class [Ljava.lang.String;<T>Lcom.fasterxml.jackson.databind.JavaType;>, int]";
        assertEquals(expected, result);
    }

    @Test
    public void testEqualsWithSameObject() {
        ReferenceType referenceType1 = new ReferenceType(String.class, JavaType.construct(int.class), null, null, false);
        ReferenceType referenceType2 = referenceType1;
        assertTrue(referenceType1.equals(referenceType2));
    }

    @Test
    public void testEqualsWithDifferentObjectButSameValues() {
        ReferenceType referenceType1 = new ReferenceType(String.class, JavaType.construct(int.class), null, null, false);
        ReferenceType referenceType2 = new ReferenceType(String.class, JavaType.construct(int.class), null, null, false);
        assertTrue(referenceType1.equals(referenceType2));
    }

    @Test
    public void testEqualsWithNullObject() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.construct(int.class), null, null, false);
        assertFalse(referenceType.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.construct(int.class), null, null, false);
        assertFalse(referenceType.equals(new Object()));
    }

    @Test
    public void testEqualsWithDifferentReferencedType() {
        ReferenceType referenceType1 = new ReferenceType(String.class, JavaType.construct(int.class), null, null, false);
        ReferenceType referenceType2 = new ReferenceType(String.class, JavaType.construct(double.class), null, null, false);
        assertFalse(referenceType1.equals(referenceType2));
    }
}