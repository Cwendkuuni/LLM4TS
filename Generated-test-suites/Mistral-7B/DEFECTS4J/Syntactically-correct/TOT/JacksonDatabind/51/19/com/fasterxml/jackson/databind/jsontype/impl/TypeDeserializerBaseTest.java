package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Test class for TypeDeserializerBase.
 */
public class TypeDeserializerBaseTest {

    private TypeIdResolver idResolver;
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setUp() {
        idResolver = mock(TypeIdResolver.class);
        JavaType baseType = new TypeReference<Object>() {}.getType();
        typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "", false, null);
    }

    @Test
    public void testForProperty() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(property);
        assertEquals(TypeDeserializer.class, typeDeserializer.getClass());
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertNull(typeInclusion);
    }

    @Test
    public void testBaseTypeName() {
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals("java.lang.Object", baseTypeName);
    }

    @Test
    public void testPropertyName() {
        String propertyName = typeDeserializerBase.getPropertyName();
        assertNull(propertyName);
    }

    @Test
    public void testgetTypeIdResolver() {
        TypeIdResolver actualTypeIdResolver = typeDeserializerBase.getTypeIdResolver();
        assertEquals(idResolver, actualTypeIdResolver);
    }

    @Test
    public void testDefaultImpl() {
        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
        assertNull(defaultImpl);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        String typeId = "com.example.TestClass";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(idResolver.typeFromId(any(DeserializationContext.class), eq(typeId))).thenReturn(new TypeReference<Object>() {}.getType());
        when(idResolver.findValueDeserializer(any(DeserializationContext.class), any(JavaType.class))).thenReturn(deserializer);

        JsonDeserializer<Object> actualDeserializer = typeDeserializerBase._findDeserializer(mock(DeserializationContext.class), typeId);
        assertEquals(deserializer, actualDeserializer);
        verify(idResolver).typeFromId(any(DeserializationContext.class), eq(typeId));
        verify(idResolver).findValueDeserializer(any(DeserializationContext.class), any(JavaType.class));
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        when(idResolver.findValueDeserializer(eq(mock(DeserializationContext.class)), any(JavaType.class)))
                .thenReturn(mock(JsonDeserializer.class));

        JsonDeserializer<Object> actualDeserializer = typeDeserializerBase._findDefaultImplDeserializer(mock(DeserializationContext.class));
        assertNotNull(actualDeserializer);
        verify(idResolver).findValueDeserializer(eq(mock(DeserializationContext.class)), any(JavaType.class));
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeId() throws IOException {
        when(idResolver.typeFromId(any(DeserializationContext.class), anyString()))
                .thenThrow(new IOException("Type id not found"));

        typeDeserializerBase._handleUnknownTypeId(mock(DeserializationContext.class), "", idResolver, null);
    }
}