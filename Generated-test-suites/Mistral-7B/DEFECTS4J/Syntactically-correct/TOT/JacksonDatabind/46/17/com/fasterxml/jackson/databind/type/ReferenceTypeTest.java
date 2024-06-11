package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test for {@link ReferenceType} class.
 */
public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        // Test default constructor
        Class<String> stringType = String.class;
        JavaType javaType = JavaTypeFactory.instance.constructType(String[].class);
        ReferenceType referenceType = new ReferenceType(stringType, javaType, null, null, false);
        assertNotNull(referenceType);
        assertEquals(String[].class, referenceType.getRawClass());
        assertEquals(javaType, referenceType.getContentType());

        // Test static constructor
        ReferenceType referenceType2 = ReferenceType.construct(String.class, JavaType.construct(String.class), null, null);
        assertEquals(referenceType, referenceType2);
    }

    @Test
    public void testTypeHandler() {
        // Test with null typeHandler
        Class<String> stringType = String.class;
        JavaType javaType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(stringType, javaType, null, null, false);
        assertNotNull(referenceType);

        // Test with non-null typeHandler
        JavaType typeHandler = JavaType.construct(Integer.class);
        ReferenceType referenceType2 = referenceType.withTypeHandler(typeHandler);
        assertNotNull(referenceType2);
        assertNotNull(referenceType2.getTypeHandler());
        assertEquals(typeHandler, referenceType2.getTypeHandler());
    }

    @Test
    public void testContentTypeHandler() {
        // Test with null contentTypeHandler
        Class<String> stringType = String.class;
        JavaType javaType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(stringType, javaType, null, null, false);
        assertNotNull(referenceType);

        // Test with non-null contentTypeHandler
        JavaType contentTypeHandler = JavaType.construct(Integer[].class);
        ReferenceType referenceType2 = referenceType.withContentTypeHandler(contentTypeHandler);
        assertNotNull(referenceType2);
        assertNotNull(referenceType2.getContentType());
        assertEquals(contentTypeHandler, referenceType2.getContentType());
    }

    @Test
    public void testValueHandler() {
        // Test with null valueHandler
        Class<String> stringType = String.class;
        JavaType javaType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(stringType, javaType, null, null, false);
        assertNotNull(referenceType);

        // Test with non-null valueHandler
        Object valueHandler = new Object() {};
        ReferenceType referenceType2 = referenceType.withValueHandler(valueHandler);
        assertNotNull(referenceType2);
        assertNotNull(referenceType2.getValueHandler());
        assertEquals(valueHandler, referenceType2.getValueHandler());
    }

    @Test
    public void testContentValueHandler() {
        // Test with null contentValueHandler
        Class<String> stringType = String.class;
        JavaType javaType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(stringType, javaType, null, null, false);
        assertNotNull(referenceType);

        // Test with non-null contentValueHandler
        Object contentValueHandler = new Object() {};
        ReferenceType referenceType2 = referenceType.withContentValueHandler(contentValueHandler);
        assertNotNull(referenceType2);
        assertNotNull(referenceType2.getContentValueHandler());
        assertEquals(contentValueHandler, referenceType2.getContentValueHandler());
    }

    @Test
    public void testStaticTyping() {
        // Test with false staticTyping
        Class<String> stringType = String.class;
        JavaType javaType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(stringType, javaType, null, null, false);
        assertFalse(referenceType.isStaticTyping());

        // Test with true staticTyping
        ReferenceType referenceType2 = referenceType.withStaticTyping();
        assertTrue(referenceType2.isStaticTyping());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgument() {
        // Test with null class
        JavaType javaType = JavaType.construct(String.class);
        new ReferenceType(null, javaType, null, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgument2() {
        // Test with null referencedType
        Class<String> stringType = String.class;
        new ReferenceType(stringType, null, null, null, false);
    }

    @Test
    public void testGetReferencedType() {
        // Test getReferencedType method
        Class<String> stringType = String.class;
        JavaType javaType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(stringType, javaType, null, null, false);
        assertEquals(javaType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        // Test isReferenceType method
        Class<String> stringType = String.class;
        JavaType javaType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(stringType, javaType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }
}