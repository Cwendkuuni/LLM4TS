package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.util.Map;

public class TypeDeserializerBaseTest {

    private static final String TYPE_PROPERTY_NAME = "typePropertyName";
    private static final TypeIdResolver idResolver = new StubTypeIdResolver();
    private static final JsonDeserializer<Object> defaultImplDeserializer = new StubJsonDeserializer<>();
    private static final JavaType defaultImpl = JsonTypeFactory.valueType(Object.class);
    private static final TypeDeserializerBase typeDeserializerBase = new StubTypeDeserializerBase(defaultImpl, idResolver, TYPE_PROPERTY_NAME, true, defaultImpl);

    @org.junit.Test
    public void testForProperty() throws Exception {
        BeanProperty property = new StubBeanProperty("propertyName");
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(property);
        assertEquals(TypeDeserializerBase.class, typeDeserializer.getClass());
        assertEquals(TypeDeserializerBase.class, typeDeserializerBase.getClass());
    }

    @org.junit.Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertNull(typeInclusion);
    }

    @org.junit.Test
    public void testBaseTypeName() {
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals(Object.class.getName(), baseTypeName);
    }

    @org.junit.Test
    public void testgetPropertyName() {
        String propertyName = typeDeserializerBase.getPropertyName();
        assertEquals(TYPE_PROPERTY_NAME, propertyName);
    }

    @org.junit.Test
    public void testGetTypeIdResolver() {
        TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();
        assertEquals(idResolver.getClass(), typeIdResolver.getClass());
    }

    @org.junit.Test
    public void testGetDefaultImpl() {
        Class<?> defaultImplClass = typeDeserializerBase.getDefaultImpl();
        assertEquals(Object.class, defaultImplClass);
    }

    @org.junit.Test
    public void testToString() throws Exception {
        String toString = typeDeserializerBase.toString();
        assertTrue(toString.contains(TypeDeserializerBase.class.getName()));
        assertTrue(toString.contains("; id-resolver: " + idResolver.getClass().getName()));
    }

    @org.junit.Test(expected = IOException.class)
    public void testFindDeserializerWithNullId() throws Exception {
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(new StubDeserializationContext(), null);
        fail("Expected IOException but got " + deserializer);
    }

    @org.junit.Test
    public void testFindDeserializerWithEmptyId() throws Exception {
        String emptyId = "";
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(new StubDeserializationContext(), emptyId);
        assertNull(deserializer);
    }

    @org.junit.Test
    public void testFindDeserializerWithValidId() throws Exception {
        String validId = "validId";
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(new StubDeserializationContext(), validId);
        assertEquals(StubJsonDeserializer.class, deserializer.getClass());
        assertTrue(deserializer instanceof StubJsonDeserializer);
    }

    @org.junit.Test(expected = IOException.class)
    public void testFindDefaultImplDeserializerWithNullContext() throws Exception {
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(null);
        fail("Expected IOException but got " + deserializer);
    }

    @org.junit.Test
    public void testFindDefaultImplDeserializerWithEmptyContext() throws Exception {
        DeserializationContext context = new StubDeserializationContext();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertNull(deserializer);
    }

    @org.junit.Test
    public void testFindDefaultImplDeserializerWithValidContext() throws Exception {
        DeserializationContext context = new StubDeserializationContext();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertEquals(defaultImplDeserializer, deserializer);
    }

    @org.junit.Test
    public void testDeserializeWithNativeTypeIdNull() throws Exception {
        JsonParser parser = new StubJsonParser();
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(parser, new StubDeserializationContext(), null);
        assertNull(result);
    }

    @org.junit.Test
    public void testDeserializeWithNativeTypeIdEmptyString() throws Exception {
        JsonParser parser = new StubJsonParser();
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(parser, new StubDeserializationContext(), "");
        assertNull(result);
    }

    @org.junit.Test
    public void testDeserializeWithNativeTypeIdValidId() throws Exception {
        JsonParser parser = new StubJsonParser();
        String validId = "validId";
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(parser, new StubDeserializationContext(), validId);
        assertEquals(StubJsonDeserializer.class, result.getClass());
        assertTrue(result instanceof StubJsonDeserializer);
    }

    @org.junit.Test(expected = IOException.class)
    public void testHandleUnknownTypeIdNull() throws Exception {
        DeserializationContext context = new StubDeserializationContext();
        JavaType result = typeDeserializerBase._handleUnknownTypeId(context, null, idResolver, defaultImpl);
        fail("Expected IOException but got " + result);
    }

    @org.junit.Test
    public void testHandleUnknownTypeIdEmptyId() throws Exception {
        DeserializationContext context = new StubDeserializationContext();
        JavaType result = typeDeserializerBase._handleUnknownTypeId(context, "", idResolver, defaultImpl);
        assertNull(result);
    }

    @org.junit.Test
    public void testHandleUnknownTypeIdValidId() throws Exception {
        DeserializationContext context = new StubDeserializationContext();
        JavaType result = typeDeserializerBase._handleUnknownTypeId(context, "validId", idResolver, defaultImpl);
        assertNull(result);
    }
}