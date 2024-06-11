package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

/**
 * Test class for {@link SimpleType}
 */
public class SimpleTypeTest {

    /**
     * Test constructor with single class
     */
    @Test
    public void testConstructSingleClass() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        SimpleType expectedSimpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(expectedSimpleType, simpleType);
    }

    /**
     * Test constructor with class, bindings, superclass and superinterfaces
     */
    @Test
    public void testConstructMultipleArguments() {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructType(String[].class, TypeBindings.emptyBindings(), Integer.class, new Class[0], null, null, false);
        SimpleType expectedSimpleType = new SimpleType(String[].class, TypeBindings.emptyBindings(),
                Integer.class, new Class[0], null, null, false);
        assertEquals(expectedSimpleType, javaType);
    }

    /**
     * Test copy constructor
     */
    @Test
    public void testCopyConstructor() {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructType(String[].class, TypeBindings.emptyBindings(), Integer.class, new Class[0], null, null, false);
        SimpleType originalSimpleType = (SimpleType) javaType;
        JavaType copiedJavaType = originalSimpleType.withStaticTyping();
        SimpleType expectedCopiedSimpleType = new SimpleType(originalSimpleType);
        assertEquals(expectedCopiedSimpleType, copiedJavaType);
    }

    /**
     * Test constructUnsafe method
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeIllegalArgumentException() {
        SimpleType.constructUnsafe(Arrays.asList("String").getClass());
    }

    /**
     * Test construct method (deprecated)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructIllegalArgumentException() {
        SimpleType.construct(String[].class);
    }

    /**
     * Test narrow method
     */
    @Test
    public void testNarrow() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType narrowedJavaType = javaType._narrow(String.class);
        SimpleType expectedNarrowedSimpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertEquals(expectedNarrowedSimpleType, narrowedJavaType);
        assertNotSame(javaType, narrowedJavaType);

        SimpleType originalSimpleType = (SimpleType) javaType;
        JavaType narrowedJavaTypeWithSameClass = originalSimpleType._narrow(String.class);
        assertEquals(narrowedJavaType, narrowedJavaTypeWithSameClass);
    }

    /**
     * Test withContentType method
     */
    @Test
    public void testWithContentType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        assertNull(javaType.getContentType());
        JavaType withContentTypeJavaType = javaType.withContentType(TypeFactory.defaultInstance().constructType(Integer.class));
        assertNotNull(withContentTypeJavaType.getContentType());
        assertEquals(Integer.class, withContentTypeJavaType.getContentType().getRawClass());

        SimpleType originalSimpleType = (SimpleType) javaType;
        SimpleType withContentTypeSimpleType = originalSimpleType.withContentType(TypeFactory.defaultInstance().constructType(Integer.class));
        assertEquals(withContentTypeSimpleType, withContentTypeJavaType);
    }

    /**
     * Test withTypeHandler method
     */
    @Test
    public void testWithTypeHandler() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object();
        JavaType withTypeHandlerJavaType = javaType.withTypeHandler(handler);
        SimpleType originalSimpleType = (SimpleType) javaType;
        SimpleType withTypeHandlerSimpleType = originalSimpleType.withTypeHandler(handler);
        assertEquals(withTypeHandlerSimpleType, withTypeHandlerJavaType);
    }

    /**
     * Test withContentTypeHandler method
     */
    @Test
    public void testWithContentTypeHandler() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object();
        JavaType withContentTypeHandlerJavaType = javaType.withContentTypeHandler(handler);
        assertNull(withContentTypeHandlerJavaType.getContentTypeHandler());
        assertEquals(javaType, withContentTypeHandlerJavaType);

        SimpleType originalSimpleType = (SimpleType) javaType;
        SimpleType withContentTypeHandlerSimpleType = originalSimpleType.withContentTypeHandler(handler);
        assertEquals(withContentTypeHandlerSimpleType, withContentTypeHandlerJavaType);
    }

    /**
     * Test withValueHandler method
     */
    @Test
    public void testWithValueHandler() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object();
        JavaType withValueHandlerJavaType = javaType.withValueHandler(handler);
        SimpleType originalSimpleType = (SimpleType) javaType;
        SimpleType withValueHandlerSimpleType = originalSimpleType.withValueHandler(handler);
        assertEquals(withValueHandlerSimpleType, withValueHandlerJavaType);
    }

    /**
     * Test withContentValueHandler method
     */
    @Test
    public void testWithContentValueHandler() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object();
        JavaType withContentValueHandlerJavaType = javaType.withContentValueHandler(handler);
        assertNull(withContentValueHandlerJavaType.getContentValueHandler());
        assertEquals(javaType, withContentValueHandlerJavaType);

        SimpleType originalSimpleType = (SimpleType) javaType;
        SimpleType withContentValueHandlerSimpleType = originalSimpleType.withContentValueHandler(handler);
        assertEquals(withContentValueHandlerSimpleType, withContentValueHandlerJavaType);
    }

    /**
     * Test withStaticTyping method
     */
    @Test
    public void testWithStaticTyping() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        SimpleType withStaticTypingSimpleType = javaType.withStaticTyping();
        SimpleType originalSimpleType = (SimpleType) javaType;
        assertEquals(withStaticTypingSimpleType, originalSimpleType.withStaticTyping());
    }

    /**
     * Test refine method
     */
    @Test
    public void testRefine() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refinedJavaType = javaType.refine(Integer.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedJavaType);
    }

    /**
     * Test buildCanonicalName method
     */
    @Test
    public void testBuildCanonicalName() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        String expectedCanonicalName = "[java.lang.String[]]";
        assertEquals(expectedCanonicalName, javaType.getCanonicalName());
    }

    /**
     * Test getErasedSignature method
     */
    @Test
    public void testGetErasedSignature() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        StringBuilder expectedErasedSignature = new StringBuilder("[Ljava.lang.String;");
        assertEquals(expectedErasedSignature.toString(), javaType.getErasedSignature(new StringBuilder()).toString());
    }

    /**
     * Test getGenericSignature method
     */
    @Test
    public void testGetGenericSignature() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        StringBuilder expectedGenericSignature = new StringBuilder("<Ljava.lang.String;[>");
        assertEquals(expectedGenericSignature.toString(), javaType.getGenericSignature(new StringBuilder()).toString());
    }
}