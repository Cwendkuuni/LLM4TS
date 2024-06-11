package com.example.jackson.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ConcurrentHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;
    private JsonDeserializer<Object> jsonDeserializerMock;
    private DeserializationContext deserializationContextMock;
    private TypeIdResolver typeIdResolverMock;
    private JavaType javaTypeMock;

    @Before
    public void setUp() {
        jsonDeserializerMock = mock(JsonDeserializer.class);
        deserializationContextMock = mock(DeserializationContext.class);
        typeIdResolverMock = mock(TypeIdResolver.class);
        javaTypeMock = mock(JavaType.class);

        when(typeIdResolverMock.typeFromId(deserializationContextMock, anyString())).thenReturn(javaTypeMock);
        when(javaTypeMock.getRawClass()).thenReturn(Object.class);

        typeDeserializerBase = new TypeDeserializerBase(null, typeIdResolverMock, "typePropertyName", true, null) {
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return jsonDeserializerMock;
            }
        };
    }

    @Test
    public void testForProperty() {
        BeanProperty beanPropertyMock = mock(BeanProperty.class);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(beanPropertyMock);
        assertEquals(TypeDeserializerBase.class, typeDeserializer.getClass());
        assertSame(typeDeserializerBase._idResolver, typeDeserializer._idResolver);
        assertSame(typeDeserializerBase._typePropertyName, typeDeserializer._typePropertyName);
        assertSame(typeDeserializerBase._typeIdVisible, typeDeserializer._typeIdVisible);
        assertSame(typeDeserializerBase._deserializers, typeDeserializer._deserializers);
        assertSame(typeDeserializerBase._defaultImplDeserializer, typeDeserializer._defaultImplDeserializer);
        assertSame(beanPropertyMock, typeDeserializer._property);
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, typeInclusion);
    }

    @Test
    public void testBaseTypeName() {
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals("java.lang.Object", baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        String propertyName = typeDeserializerBase.getPropertyName();
        assertEquals("typePropertyName", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();
        assertSame(typeIdResolverMock, typeIdResolver);
    }

    @Test
    public void testDefaultImpl() {
        JavaType defaultImpl = typeDeserializerBase.getDefaultImpl();
        assertEquals(Object.class, defaultImpl.getRawClass());
    }

    @Test
    public void testBaseType() {
        JavaType baseType = typeDeserializerBase.baseType();
        assertEquals(Object.class, baseType.getRawClass());
    }

    @Test
    public void testToString() {
        String toString = typeDeserializerBase.toString();
        String expectedToString = "[TypeDeserializerBase; base-type:java.lang.Object; id-resolver:...]";
        assertEquals(expectedToString, toString);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        when(typeIdResolverMock.typeFromId(deserializationContextMock, "typeId")).thenReturn(javaTypeMock);

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(deserializationContextMock, "typeId");
        verify(jsonDeserializerMock).deserialize(any(JsonParser.class), any(DeserializationContext.class));
        assertSame(jsonDeserializerMock, deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        when(typeIdResolverMock.typeFromId(deserializationContextMock, anyString())).thenReturn(javaTypeMock);

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(deserializationContextMock);
        assertNull(deserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        when(typeIdResolverMock.typeFromId(deserializationContextMock, anyString())).thenReturn(javaTypeMock);

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(null, deserializationContextMock, "typeId");
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdWithNullTypeId() throws IOException {
        when(typeIdResolverMock.typeFromId(deserializationContextMock, null)).thenThrow(new IOException("Expected IOException"));

        typeDeserializerBase._deserializeWithNativeTypeId(null, deserializationContextMock, null);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdWithUnknownTypeId() throws IOException {
        when(typeIdResolverMock.typeFromId(deserializationContextMock, "unknownTypeId")).thenReturn(null);

        typeDeserializerBase._deserializeWithNativeTypeId(null, deserializationContextMock, "unknownTypeId");
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        when(typeIdResolverMock.typeFromId(deserializationContextMock, anyString())).thenReturn(null);

        JavaType javaType = typeDeserializerBase._handleUnknownTypeId(deserializationContextMock, "unknownTypeId");
        assertNull(javaType);
    }

    @Test(expected = IOException.class)
    public void testHandleMissingTypeId() throws IOException {
        when(typeIdResolverMock.typeFromId(deserializationContextMock, anyString())).thenThrow(new IOException("Expected IOException"));

        typeDeserializerBase._handleMissingTypeId(deserializationContextMock, "missingTypeId");
    }
}