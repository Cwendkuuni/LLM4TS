package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.config.TypeFactoryBuilder;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeCreation() throws Exception {
        TypeFactory tf = new TypeFactoryBuilder()
                .build();

        JavaType javaMapType = tf.constructType(Map.class, String.class, Integer.class);
        JavaType mapLikeType = MapLikeType.upgradeFrom(javaMapType, String.class, Integer.class);

        MapLikeType actual = (MapLikeType) mapLikeType;
        JavaType expectedKeyType = String.class;
        JavaType expectedValueType = Integer.class;

        assertEquals(expectedKeyType, actual.getKeyType());
        assertEquals(expectedValueType, actual.getContentType());
    }

    @Test
    public void testMapLikeTypeWithCustomHandlers() throws Exception {
        TypeFactory tf = new TypeFactoryBuilder()
                .build();

        JavaType javaMapType = tf.constructType(Map.class, String.class, Integer.class);
        JavaType mapLikeType = MapLikeType.construct(javaMapType.getRawClass(), String.class, Integer.class);

        MapLikeType actual = (MapLikeType) mapLikeType;
        JavaType expectedKeyType = String.class;
        JavaType expectedValueType = Integer.class;

        assertEquals(expectedKeyType, actual.getKeyType());
        assertEquals(expectedValueType, actual.getContentType());

        TypeFactory typeFactory = new TypeFactoryBuilder()
                .build();
        Object customValueHandler = new Object() {};
        JavaType customValueHandlerType = typeFactory.constructType(customValueHandler.getClass());
        actual = actual.withValueHandler(customValueHandlerType);

        JavaType actualCustomValueType = actual.getContentType();
        assertEquals(customValueHandlerType, actualCustomValueType);

        Object customTypeHandler = new Object() {};
        JavaType customTypeHandlerType = typeFactory.constructType(customTypeHandler.getClass());
        actual = actual.withTypeHandler(customTypeHandlerType);

        JavaType actualCustomTypeType = actual.getTypeHandler();
        assertEquals(customTypeHandlerType, actualCustomTypeType);
    }

    @Test
    public void testMapLikeTypeWithKeyType() throws Exception {
        TypeFactory tf = new TypeFactoryBuilder()
                .build();

        JavaType javaMapType = tf.constructType(Map.class, String.class, Integer.class);
        JavaType mapLikeType = MapLikeType.construct(javaMapType.getRawClass(), String.class, Integer.class);

        MapLikeType actual = (MapLikeType) mapLikeType;
        JavaType expectedKeyType = String.class;
        JavaType expectedValueType = Integer.class;

        assertEquals(expectedKeyType, actual.getKeyType());
        assertEquals(expectedValueType, actual.getContentType());

        JavaType newKeyType = tf.constructType(Long.class);
        actual = actual.withKeyType(newKeyType);

        JavaType actualNewKeyType = actual.getKeyType();
        assertEquals(newKeyType, actualNewKeyType);
    }

    @Test
    public void testMapLikeTypeWithContentType() throws Exception {
        TypeFactory tf = new TypeFactoryBuilder()
                .build();

        JavaType javaMapType = tf.constructType(Map.class, String.class, Integer.class);
        JavaType mapLikeType = MapLikeType.construct(javaMapType.getRawClass(), String.class, Integer.class);

        MapLikeType actual = (MapLikeType) mapLikeType;
        JavaType expectedKeyType = String.class;
        JavaType expectedValueType = Integer.class;

        assertEquals(expectedKeyType, actual.getKeyType());
        assertEquals(expectedValueType, actual.getContentType());

        JavaType newValueType = tf.constructType(Long.class);
        actual = actual.withContentType(newValueType);

        JavaType actualNewValueType = actual.getContentType();
        assertEquals(newValueType, actualNewValueType);
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() throws Exception {
        TypeFactory tf = new TypeFactoryBuilder()
                .build();

        JavaType javaMapType = tf.constructType(HashMap.class, String.class, Integer.class);
        JavaType mapLikeType = MapLikeType.upgradeFrom(javaMapType, String.class, Integer.class);

        MapLikeType actual = (MapLikeType) mapLikeType;
        JavaType expectedKeyType = String.class;
        JavaType expectedValueType = Integer.class;

        assertEquals(expectedKeyType, actual.getKeyType());
        assertEquals(expectedValueType, actual.getContentType());

        MapLikeType staticTyping = actual.withStaticTyping();
        assertEquals(expectedKeyType, staticTyping.getKeyType());
        assertEquals(expectedValueType, staticTyping.getContentType());

        assertTrue(staticTyping.isStaticTyping());
        assertFalse(actual.isStaticTyping());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeInvalidUpgrade() throws Exception {
        TypeFactory tf = new TypeFactoryBuilder()
                .build();

        JavaType javaListType = tf.constructType(List.class, Integer.class);
        JavaType mapLikeType = MapLikeType.upgradeFrom(javaListType, String.class, Integer.class);

        fail("Should throw IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeInvalidConstruct() throws Exception {
        TypeFactory tf = new TypeFactoryBuilder()
                .build();

        JavaType javaListType = tf.constructType(List.class, Integer.class);
        JavaType mapLikeType = MapLikeType.construct(javaListType.getRawClass(), String.class, Integer.class);

        fail("Should throw IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeInvalidNarrow() throws Exception {
        TypeFactory tf = new TypeFactoryBuilder()
                .build();

        JavaType javaMapType = tf.constructType(Map.class, String.class, Integer.class);
        JavaType mapLikeType = MapLikeType.construct(javaMapType.getRawClass(), String.class, Integer.class);

        MapLikeType actual = (MapLikeType) mapLikeType;

        TypeFactory typeFactory = new TypeFactoryBuilder()
                .subType(String.class, "StringSubClass").build();
        JavaType subClass = typeFactory.constructType(String.class);

        try {
            actual = actual.withStaticTyping();
            actual = actual.narrow(subClass);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}