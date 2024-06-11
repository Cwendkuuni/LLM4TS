package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test for {@link CollectionType} class.
 */
public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        JavaType javaType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        assertNotNull(javaType);
        assertEquals(ArrayList.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getContentType());
        assertEquals(String.class, javaType.getContentType().getRawClass());
    }

    @Test
    public void testConstructDeprecated() {
        try {
            JavaType javaType = CollectionType.construct(ArrayList.class, String.class);
            fail("Expected deprecated constructor to throw exception");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }

    @Test
    public void testWithContentType() {
        JavaType javaType = new TypeReference<List<Integer>>() {}.getType();
        JavaType newJavaType = javaType.withContentType(Byte.TYPE);
        assertEquals(new TypeReference<List<Byte>>() {}.getType(), newJavaType);
    }

    @Test
    public void testWithTypeHandler() {
        JavaType javaType = new TypeReference<List<String>>() {}.getType();
        JavaType newJavaType = javaType.withTypeHandler(new IdentityObjectMapper.DefaultTyping());
        assertNotNull(newJavaType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType javaType = new TypeReference<List<String>>() {}.getType();
        JavaType newJavaType = javaType.withContentTypeHandler(new IdentityObjectMapper.DefaultTyping());
        assertNotNull(newJavaType.getContentType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType javaType = new TypeReference<List<String>>() {}.getType();
        JavaType newJavaType = javaType.withValueHandler(new IdentityObjectMapper.DefaultTyping());
        assertNotNull(newJavaType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType javaType = new TypeReference<List<String>>() {}.getType();
        JavaType newJavaType = javaType.withContentValueHandler(new IdentityObjectMapper.DefaultTyping());
        assertNotNull(newJavaType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType javaType = new TypeReference<List<String>>() {}.getType();
        JavaType newJavaType = javaType.withStaticTyping();
        assertTrue(newJavaType.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType javaType = new TypeReference<List<String>>() {}.getType();
        JavaType newJavaType = javaType.refine(ArrayList.class, new TypeBindings(), null, null);
        assertEquals(ArrayList.class, newJavaType.getRawClass());
    }
}