package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBaseConstruction() {
        JavaType baseType = Object.class.getType();
        TypeIdResolver idResolver = new JsonTypeInfo.DefaultTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = String.class;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());
        assertEquals(typePropertyName, typeDeserializerBase.getPropertyName());
        assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
        assertNull(typeDeserializerBase.getDefaultImpl());
        assertNotNull(typeDeserializerBase._deserializers);
        assertEquals(typeIdVisible, typeDeserializerBase._typeIdVisible);

        TypeDeserializerBase copiedTypeDeserializerBase = new TypeDeserializerBase(typeDeserializerBase, null);
        assertEquals(typeDeserializerBase.baseTypeName(), copiedTypeDeserializerBase.baseTypeName());
        assertEquals(typeDeserializerBase.getPropertyName(), copiedTypeDeserializerBase.getPropertyName());
        assertEquals(typeDeserializerBase.getTypeIdResolver(), copiedTypeDeserializerBase.getTypeIdResolver());
        assertEquals(typeDeserializerBase._deserializers, copiedTypeDeserializerBase._deserializers);
        assertEquals(typeDeserializerBase._typeIdVisible, copiedTypeDeserializerBase._typeIdVisible);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        JavaType baseType = Object.class.getType();
        TypeIdResolver idResolver = new JsonTypeInfo.DefaultTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = String.class;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        String typeId = "TestType";
        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {};
        typeDeserializerBase._deserializers.put(typeId, deserializer);

        Object result = typeDeserializerBase._findDeserializer(null, typeId);
        assertEquals(deserializer, result);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerUnknownTypeId() throws IOException {
        JavaType baseType = Object.class.getType();
        TypeIdResolver idResolver = new JsonTypeInfo.DefaultTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = String.class;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        String typeId = "UnknownTypeId";

        Object result = typeDeserializerBase._findDeserializer(null, typeId);
        fail("Expected IOException but got " + result.getClass().getName());
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JavaType baseType = Object.class.getType();
        TypeIdResolver idResolver = new JsonTypeInfo.DefaultTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = String.class;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        JsonDeserializer<Object> defaultImplDeserializer = new JsonDeserializer<Object>() {};
        typeDeserializerBase._defaultImplDeserializer = defaultImplDeserializer;

        Object result = typeDeserializerBase._findDefaultImplDeserializer(null);
        assertEquals(defaultImplDeserializer, result);
    }

    @Test(expected = IOException.class)
    public void testFindDefaultImplDeserializerNull() throws IOException {
        JavaType baseType = Object.class.getType();
        TypeIdResolver idResolver = new JsonTypeInfo.DefaultTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        Object result = typeDeserializerBase._findDefaultImplDeserializer(null);
        fail("Expected IOException but got " + result.getClass().getName());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JavaType baseType = Object.class.getType();
        TypeIdResolver idResolver = new JsonTypeInfo.DefaultTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = String.class;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        String json = "{\"type\":\"TestType\",\"value\":\"Test Value\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);

        Object result
            = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, null, "TestType");

        assertEquals(String.class, result.getClass());
        assertEquals("Test Value", result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdUnknownTypeId() throws IOException {
        JavaType baseType = Object.class.getType();
        TypeIdResolver idResolver = new JsonTypeInfo.DefaultTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = String.class;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        String json = "{\"value\":\"Test Value\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);

        Object result
            = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, null, null);

        fail("Expected IOException but got " + result.getClass().getName());
    }
}