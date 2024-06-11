package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.types.ArrayType;
import com.fasterxml.jackson.databind.type.types.CollectionType;
import com.fasterxml.jackson.databind.type.types.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeCreation() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        ReferenceType referenceType = ReferenceType.construct(String[].class, javaType, null, null);
        assertEquals(String[].class, referenceType.getRawClass());
        assertEquals(javaType, referenceType.getContentType());
        assertFalse(referenceType.isStatic());
    }

    @Test
    public void testReferenceTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        Object typeHandler = new Object() {};
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        ReferenceType referenceTypeWithTypeHandler = referenceType.withTypeHandler(typeHandler);
        assertEquals(referenceType, referenceTypeWithTypeHandler);
        assertNotSame(typeHandler, referenceTypeWithTypeHandler._typeHandler);
        assertSame(typeHandler, referenceTypeWithTypeHandler.getTypeHandler());
    }

    @Test
    public void testReferenceTypeWithContentTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(String.class);
        Object contentTypeHandler = new Object() {};
        ReferenceType referenceType = new ReferenceType(String[].class, stringJavaType, null, null, false);
        ReferenceType referenceTypeWithContentTypeHandler = referenceType.withContentTypeHandler(contentTypeHandler);
        assertEquals(referenceType, referenceTypeWithContentTypeHandler);
        assertNotSame(contentTypeHandler, referenceTypeWithContentTypeHandler._referencedType.getTypeHandler());
        assertSame(contentTypeHandler, referenceTypeWithContentTypeHandler.getContentTypeHandler());
    }

    @Test
    public void testReferenceTypeWithValueHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        Object valueHandler = new Object() {};
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        ReferenceType referenceTypeWithValueHandler = referenceType.withValueHandler(valueHandler);
        assertEquals(referenceType, referenceTypeWithValueHandler);
        assertNotSame(valueHandler, referenceTypeWithValueHandler._valueHandler);
        assertSame(valueHandler, referenceTypeWithValueHandler.getValueHandler());
    }

    @Test
    public void testReferenceTypeWithContentValueHandler() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(String.class);
        Object contentValueHandler = new Object() {};
        ReferenceType referenceType = new ReferenceType(String[].class, stringJavaType, null, null, false);
        ReferenceType referenceTypeWithContentValueHandler = referenceType.withContentValueHandler(contentValueHandler);
        assertEquals(referenceType, referenceTypeWithContentValueHandler);
        assertNotSame(contentValueHandler, referenceTypeWithContentValueHandler._referencedType.getValueHandler());
        assertSame(contentValueHandler, referenceTypeWithContentValueHandler.getContentValueHandler());
    }

    @Test
    public void testReferenceTypeWithStaticTyping() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        ReferenceType referenceTypeWithStaticTyping = referenceType.withStaticTyping();
        assertEquals(referenceType, referenceTypeWithStaticTyping);
        assertTrue(referenceTypeWithStaticTyping.isStatic());
    }

    @Test
    public void testReferenceTypeGetReferencedType() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        assertEquals(javaType, referenceType.getReferencedType());
    }

    @Test
    public void testReferenceTypeIsReferenceType() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testReferenceTypeContainedTypeCount() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        int containedTypeCount = referenceType.containedTypeCount();
        assertEquals(1, containedTypeCount);
    }

    @Test
    public void testReferenceTypeContainedType() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        JavaType containedType = referenceType.containedType(0);
        assertEquals(javaType, containedType);
    }

    @Test
    public void testReferenceTypeContainedTypeName() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        String containedTypeName = referenceType.containedTypeName(0);
        assertEquals("T", containedTypeName);
    }

    @Test
    public void testReferenceTypeGetParameterSource() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        assertEquals(stringClass, referenceType.getParameterSource());
    }

    @Test
    public void testReferenceTypeGetErasedSignature() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        StringBuilder erasedSignature = referenceType.getErasedSignature(new StringBuilder());
        assertEquals("[Ljava.lang.Object;", erasedSignature.toString());
    }

    @Test
    public void testReferenceTypeGetGenericSignature() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        StringBuilder genericSignature = referenceType.getGenericSignature(new StringBuilder());
        assertEquals("[Ljava.util.List<Ljava.lang.String;>;", genericSignature.toString());
    }

    @Test
    public void testReferenceTypeToString() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        String toString = referenceType.toString();
        String expected = "[reference type, class [Ljava.lang.String;<T> T]"
                + "<T>"
                + "[Ljava.util.List<Ljava.lang.String;>;]";
        assertEquals(expected, toString);
    }

    @Test
    public void testReferenceTypeEquals() {
        Class<String> stringClass = String.class;
        JavaType javaType1 = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, String.class);
        ReferenceType referenceType1 = new ReferenceType(String[].class, javaType1, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(String[].class, javaType2, null, null, false);
        ReferenceType referenceType3 = new ReferenceType(String[].class, javaType1, null, null, false);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType3));
        assertFalse(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(new Object() {}));
    }

    @Test
    public void testReferenceTypeNarrow() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JavaType stringArrayType = TypeFactory.defaultInstance().constructType(String[].class);
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        JavaType narrowType = referenceType._narrow(String[].class);
        assertEquals(stringArrayType, narrowType);
    }

    @Test
    public void testReferenceTypeConstruct() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        Object typeHandler = new Object() {};
        Object valueHandler = new Object() {};
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, typeHandler, valueHandler, false);
        assertEquals(String[].class, referenceType.getRawClass());
        assertEquals(javaType, referenceType.getContentType());
        assertFalse(referenceType.isStatic());
        assertSame(typeHandler, referenceType.getTypeHandler());
        assertSame(valueHandler, referenceType.getValueHandler());
        assertNull(referenceType.getContentValueHandler());
        assertNull(referenceType.getValueHandler());
        assertSame(typeHandler, referenceType.getTypeHandler());
    }

    @Test
    public void testReferenceTypeWithStaticTypingAndBuildCanonicalName() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, javaType, null, null, false);
        ReferenceType referenceTypeWithStaticTyping = referenceType.withStaticTyping();
        String canonicalName = referenceTypeWithStaticTyping.buildCanonicalName();
        String expected = "[reference type, class [Ljava.lang.String;<T> T]"
                + "<T>"
                + "[Ljava.util.List<Ljava.lang.String;>;]"
                + "^T^";
        assertEquals(expected, canonicalName);
    }
}