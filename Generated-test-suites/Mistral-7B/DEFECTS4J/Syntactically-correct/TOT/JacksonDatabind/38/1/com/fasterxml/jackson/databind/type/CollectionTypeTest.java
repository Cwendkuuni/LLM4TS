package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CollectionTypeTest {

    @Test
    public void testConstructWithValidArguments() {
        JavaType listType = CollectionType.construct(ArrayList.class, null,
                Object.class, new JavaType[0], Integer.TYPE);
        assertNotNull(listType);
        assertEquals("java.util.ArrayList<java.lang.Integer>", listType.toString());
    }

    @Test
    public void testConstructDeprecatedWithValidArguments() {
        JavaType listType = CollectionType.construct(ArrayList.class, Integer.TYPE);
        assertNotNull(listType);
        assertEquals("java.util.ArrayList<java.lang.Integer>", listType.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidArguments() {
        JavaType listType = CollectionType.construct(Object.class, null,
                Integer.TYPE, new JavaType[0], String.class);
    }

    @Test
    public void testWithContentType() {
        JavaType listType = new CollectionType(ArrayList.class, null,
                Object.class, new JavaType[0], Integer.TYPE).withContentType(String.class);
        assertEquals("java.util.ArrayList<java.lang.String>", listType.toString());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType listType = new CollectionType(ArrayList.class, null,
                Object.class, new JavaType[0], Integer.TYPE)
                .withTypeHandler(Collections.emptyList().getClass());
        assertEquals("java.util.ArrayList<java.util.List<java.lang.Integer>>", listType.toString());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType listType = new CollectionType(ArrayList.class, null,
                Object.class, new JavaType[0], Integer.TYPE)
                .withContentTypeHandler(Collections.emptyMap().getClass());
        assertEquals("java.util.ArrayList<java.util.Map<?, java.lang.Integer>>", listType.toString());
    }

    @Test
    public void testWithValueHandler() {
        JavaType listType = new CollectionType(ArrayList.class, null,
                Object.class, new JavaType[0], Integer.TYPE)
                .withValueHandler(new Object(){});
        assertEquals("java.util.ArrayList<java.lang.Object>", listType.toString());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType listType = new CollectionType(ArrayList.class, null,
                Object.class, new JavaType[0], Integer.TYPE)
                .withContentValueHandler(new Object(){});
        assertEquals("java.util.ArrayList<java.lang.Object>", listType.toString());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType listType = new CollectionType(ArrayList.class, null,
                Object.class, new JavaType[0], Integer.TYPE).withStaticTyping();
        assertEquals("java.util.ArrayList<java.lang.Integer>", listType.toString());
    }

    @Test
    public void testRefine() {
        JavaType listType = new CollectionType(ArrayList.class, null,
                Object.class, new JavaType[0], Integer.TYPE).refine(LinkedList.class, null,
                Object.class, new JavaType[0]);
        assertEquals("java.util.LinkedList<java.lang.Integer>", listType.toString());
    }
}