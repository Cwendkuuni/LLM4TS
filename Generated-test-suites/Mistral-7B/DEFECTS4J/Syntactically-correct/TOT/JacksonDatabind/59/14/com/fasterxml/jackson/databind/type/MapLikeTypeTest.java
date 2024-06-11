package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeCreation() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(
                Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(javaType, String.class,
                Integer.class);
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapLikeType.toString());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeCreationWithTypeBindings() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(
                Map.class, String[].class, Integer[].class);
        TypeBindings bindings = TypeBindings.create(javaType, String[].class,
                Integer[].class);
        MapLikeType mapLikeType = new MapLikeType(javaType, bindings);
        assertEquals("com.fasterxml.jackson.databind.type.MapLikeType@2b78a601", mapLikeType.toString());
        assertEquals(String[].class, mapLikeType.getKeyType());
        assertEquals(Integer[].class, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(
                HashMap.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(javaType.getType(String.class),
                javaType.getType(Integer.class));
        assertEquals("com.fasterxml.jackson.databind.type.MapLikeType@467325c5", mapLikeType.toString());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithValueType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(
                Map.class, String.class, String.class);
        MapLikeType mapLikeType = new MapLikeType(javaType, javaType.getValueType().getType(String.class));
        assertEquals("com.fasterxml.jackson.databind.type.MapLikeType@467325c5", mapLikeType.toString());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(String.class, mapLikeType.getValueType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeInvalidBaseType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(
                List.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(javaType);
    }

    @Test
    public void testMapLikeTypeWithKeyTypeHandler() {
        JavaType keyTypeHandler = TypeFactory.defaultInstance().constructType(
                String[].class);
        MapLikeType mapLikeType = new MapLikeType(TypeFactory.defaultInstance().constructType(
                Map.class, String.class, Integer.class), keyTypeHandler);
        assertEquals("com.fasterxml.jackson.databind.type.MapLikeType@467325c5", mapLikeType.toString());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getValueType());
        assertEquals(keyTypeHandler, mapLikeType.getKeyTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithValueTypeHandler() {
        JavaType valueTypeHandler = TypeFactory.defaultInstance().constructType(
                String.class);
        MapLikeType mapLikeType = new MapLikeType(TypeFactory.defaultInstance().constructType(
                Map.class, String.class, Integer.class), null, null, null, null, valueTypeHandler);
        assertEquals("com.fasterxml.jackson.databind.type.MapLikeType@467325c5", mapLikeType.toString());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(String.class, mapLikeType.getValueType());
        assertEquals(valueTypeHandler, mapLikeType.getValueTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        JavaType contentTypeHandler = TypeFactory.defaultInstance().constructType(
                List.class);
        MapLikeType mapLikeType = new MapLikeType(TypeFactory.defaultInstance().constructType(
                Map.class, String.class, Integer.class), null, null, null, null, null, null, contentTypeHandler, false);
        assertEquals("com.fasterxml.jackson.databind.type.MapLikeType@467325c5", mapLikeType.toString());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(List.class, mapLikeType.getValueType());
        assertEquals(contentTypeHandler, mapLikeType.getValueTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithContentValueHandler() {
        JavaType contentValueHandler = TypeFactory.defaultInstance().constructType(
                List.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(TypeFactory.defaultInstance().constructType(
                Map.class, String.class, Integer.class), null, null, null, null, null, contentValueHandler, null, false);
        assertEquals("com.fasterxml.jackson.databind.type.MapLikeType@467325c5", mapLikeType.toString());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(List.class, mapLikeType.getValueType());
        assertEquals(contentValueHandler, mapLikeType.getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(
                Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(javaType, javaType.getKeyType(), javaType.getValueType(), null, null, null, null, null, false);
        MapLikeType staticMapLikeType = mapLikeType.withStaticTyping();
        assertEquals("com.fasterxml.jackson.databind.type.MapLikeType@269a7a95", staticMapLikeType.toString());
        assertEquals(Map.class, staticMapLikeType.getClass());
    }
}