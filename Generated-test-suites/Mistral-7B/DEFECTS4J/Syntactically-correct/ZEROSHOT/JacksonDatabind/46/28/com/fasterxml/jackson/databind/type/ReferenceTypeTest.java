package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link ReferenceType}.
 */
public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = new SimpleType(String.class);

        ReferenceType referenceType = ReferenceType.construct(stringClass, stringJavaType, null, null);
        assertEquals("Expected reference type class to be same", stringClass, referenceType.getRawClass());
        assertEquals("Expected referenced type to be same", stringJavaType, referenceType.getContentType());
    }

    @Test
    public void testReferenceTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = new SimpleType(String.class);
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(stringClass, stringJavaType, null, null, false);
        assertEquals("Expected reference type without type handler", referenceType, referenceType.withTypeHandler(typeHandler));

        ReferenceType referenceTypeWithHandler = new ReferenceType(stringClass, stringJavaType, null, typeHandler, false);
        assertNotSame("Expected different reference types", referenceType, referenceTypeWithHandler);
        assertEquals("Expected referenced types to be same", stringJavaType, referenceTypeWithHandler.getContentType());
    }

    @Test
    public void testReferenceTypeWithContentTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = new SimpleType(String.class);
        Object contentTypeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(stringClass, stringJavaType, null, null, false);
        assertEquals("Expected reference type without content type handler", referenceType, referenceType.withContentTypeHandler(contentTypeHandler));

        ReferenceType referenceTypeWithContentTypeHandler = new ReferenceType(stringClass, stringJavaType, null, null, false)
                .withContentTypeHandler(contentTypeHandler);
        assertNotSame("Expected different reference types", referenceType, referenceTypeWithContentTypeHandler);
        assertEquals("Expected content type handlers to be same", contentTypeHandler, referenceTypeWithContentTypeHandler.getContentTypeHandler());
    }

    @Test
    public void testReferenceTypeWithValueHandler() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = new SimpleType(String.class);
        Object valueHandler = new Object();

        ReferenceType referenceType = new ReferenceType(stringClass, stringJavaType, null, null, false);
        assertEquals("Expected reference type without value handler", referenceType, referenceType.withValueHandler(valueHandler));

        ReferenceType referenceTypeWithHandler = new ReferenceType(stringClass, stringJavaType, valueHandler, null, false);
        assertNotSame("Expected different reference types", referenceType, referenceTypeWithHandler);
        assertEquals("Expected value handlers to be same", valueHandler, referenceTypeWithHandler.getValueHandler());
    }

    @Test
    public void testReferenceTypeWithContentValueHandler() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = new SimpleType(String.class);
        Object contentValueHandler = new Object();

        ReferenceType referenceType = new ReferenceType(stringClass, stringJavaType, null, null, false);
        assertEquals("Expected reference type without content value handler", referenceType, referenceType.withContentValueHandler(contentValueHandler));

        ReferenceType referenceTypeWithContentValueHandler = new ReferenceType(stringClass, stringJavaType, null, null, false)
                .withContentValueHandler(contentValueHandler);
        assertNotSame("Expected different reference types", referenceType, referenceTypeWithContentValueHandler);
        assertEquals("Expected content value handlers to be same", contentValueHandler, referenceTypeWithContentValueHandler.getContentValueHandler());
    }

    @Test
    public void testReferenceTypeWithStaticTyping() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = new SimpleType(String.class);

        ReferenceType referenceType = new ReferenceType(stringClass, stringJavaType, null, null, false);
        assertFalse("Expected reference type to be static", referenceType.isStatic());

        ReferenceType referenceTypeWithStaticTyping = referenceType.withStaticTyping();
        assertTrue("Expected reference type to be static", referenceTypeWithStaticTyping.isStatic());

        // Check that the content type is the same
        assertEquals("Expected content types to be same", stringJavaType, referenceTypeWithStaticTyping.getContentType());
    }

    @Test
    public void testReferenceTypeHashCode() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = new SimpleType(String.class);

        ReferenceType referenceType1 = new ReferenceType(stringClass, stringJavaType, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(stringClass, stringJavaType, null, null, false);

        assertEquals("Expected reference types with same properties to have the same hashcode", referenceType1.hashCode(), referenceType2.hashCode());
    }

    @Test
    public void testReferenceTypeEquals() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = new SimpleType(String.class);

        ReferenceType referenceType1 = new ReferenceType(stringClass, stringJavaType, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(stringClass, stringJavaType, null, null, false);

        assertTrue("Expected reference types to be equal", referenceType1.equals(referenceType2));
    }

    @Test
    public void testReferenceTypeNotEqualDifferentClasses() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = new SimpleType(String.class);
        JavaType intJavaType = new SimpleType(int.class);

        ReferenceType referenceTypeString = new ReferenceType(stringClass, stringJavaType, null, null, false);
        ReferenceType referenceTypeInt = new ReferenceType(intJavaType, stringJavaType, null, null, false);

        assertFalse("Expected reference types to be different", referenceTypeString.equals(referenceTypeInt));
    }

    @Test
    public void testReferenceTypeNarrow() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = new SimpleType(String.class);
        Class<Integer> intClass = Integer.class;

        ReferenceType referenceType = new ReferenceType(stringClass, stringJavaType, null, null, false);
        JavaType narrowedType = referenceType.narrow(intClass);
        assertEquals("Expected narrowed type to be a new reference type instance", ReferenceType.class, narrowedType.getClass());
        assertEquals("Expected referenced types to be different", intClass, narrowedType.getContentType().getRawClass());
    }

    @Test
    public void testReferenceTypeIsReferenceType() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = new SimpleType(String.class);

        ReferenceType referenceType = new ReferenceType(stringClass, stringJavaType, null, null, false);
        assertTrue("Expected reference type to be a reference type", referenceType.isReferenceType());
    }

    @Test
    public void testReferenceTypeContainedTypeCount() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = new SimpleType(String.class);

        ReferenceType referenceType = new ReferenceType(stringClass, stringJavaType, null, null, false);
        assertEquals("Expected contained type count to be 1", 1, referenceType.containedTypeCount());
    }

    @Test
    public void testReferenceTypeContainedType() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = new SimpleType(String.class);

        ReferenceType referenceType = new ReferenceType(stringClass, stringJavaType, null, null, false);
        JavaType containedType = referenceType.containedType(0);
        assertEquals("Expected contained type to be the referenced type", containedType, referenceType.getContentType());
    }

    @Test
    public void testReferenceTypeContainedTypeName() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = new SimpleType(String.class);

        ReferenceType referenceType = new ReferenceType(stringClass, stringJavaType, null, null, false);
        String containedTypeName = referenceType.containedTypeName(0);
        assertEquals("Expected contained type name to be 'T'", "T", containedTypeName);
    }

    @Test
    public void testReferenceTypeGetParameterSource() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = new SimpleType(String.class);

        ReferenceType referenceType = new ReferenceType(stringClass, stringJavaType, null, null, false);
        assertEquals("Expected parameter source to be the raw class", stringClass, referenceType.getParameterSource());
    }

    @Test
    public void testReferenceTypeGetErasedSignature() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = new SimpleType(String.class);

        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(stringClass, stringJavaType, null, null, false);
        referenceType.getErasedSignature(sb);
        assertEquals("Expected erased signature to be '[Ljava/lang/String;', '" + sb.toString() + "'", "[Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testReferenceTypeGetGenericSignature() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = new SimpleType(String.class);

        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(stringClass, stringJavaType, null, null, false);
        referenceType.getGenericSignature(sb);
        assertEquals("Expected generic signature to be 'Ljava/lang/String;<TT;>', '" + sb.toString() + "'", "Ljava/lang/String;<TT;>", sb.toString());
    }
}