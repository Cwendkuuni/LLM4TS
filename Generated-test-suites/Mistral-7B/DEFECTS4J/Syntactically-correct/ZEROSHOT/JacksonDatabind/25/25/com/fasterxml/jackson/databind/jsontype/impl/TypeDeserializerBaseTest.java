package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseTest;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

public class TypeDeserializerBaseTest extends BaseTest {

    private TypeDeserializerBase typeDeserializerBase;
    private JsonDeserializer<Object> jsonDeserializerMock;

    @Before
    public void setup() {
        TypeIdResolver typeIdResolverMock = mock(TypeIdResolver.class);
        JavaType javaTypeMock = mock(JavaType.class);
        BeanProperty beanPropertyMock = mock(BeanProperty.class);
        DeserializationContext deserializationContextMock = mock(DeserializationContext.class);

        when(typeIdResolverMock.typeFromId(deserializationContextMock, "someTypeId")).thenReturn(javaTypeMock);
        when(javaTypeMock.getRawClass()).thenReturn(String.class);
        when(deserializationContextMock.findContextualValueDeserializer(javaTypeMock, beanPropertyMock)).thenReturn(jsonDeserializerMock);

        typeDeserializerBase = new TypeDeserializerBase(null, typeIdResolverMock, "propertyName", false, String.class) {
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return super._findDeserializer(ctxt, typeId);
            }
        };
    }

    @Test
    public void testForProperty() {
        TypeDeserializerBase newTypeDeserializerBase = typeDeserializerBase.forProperty(beanPropertyMock);
        assertEquals(TypeDeserializerBase.class, newTypeDeserializerBase.getClass());
        assertEquals("propertyName", newTypeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertNull(typeInclusion);
    }

    @Test
    public void testBaseTypeName() {
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals("java.lang.String", baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        String propertyName = typeDeserializerBase.getPropertyName();
        assertEquals("propertyName", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();
        assertEquals(typeIdResolverMock, typeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
        assertEquals(String.class, defaultImpl.getName());
    }

    @Test
    public void testToString() {
        String toString = typeDeserializerBase.toString();
        assertTrue(toString.contains("[TypeDeserializerBase"));
        assertTrue(toString.contains("; base-type:"));
        assertTrue(toString.contains("; id-resolver:"));
    }

    @Test
    public void testFindDeserializerWithValidTypeId() throws IOException {
        String typeId = "someTypeId";
        when(deserializationContextMock.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(deserializationContextMock, typeId);
        verify(deserializationContextMock).findContextualValueDeserializer(javaTypeMock, beanPropertyMock);
        assertEquals(jsonDeserializerMock, deserializer);
    }

    @Test
    public void testFindDeserializerWithNullTypeId() throws IOException {
        when(deserializationContextMock.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(deserializationContextMock, null);
        verify(deserializationContextMock).findContextualValueDeserializer(eq(NullifyingDeserializer.class), eq(beanPropertyMock));
        assertEquals(NullifyingDeserializer.instance, deserializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindDeserializerWithUnknownTypeId() throws IOException {
        when(deserializationContextMock.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(true);

        typeDeserializerBase._findDeserializer(deserializationContextMock, "unknownTypeId");
        fail("Should have thrown JsonMappingException");
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(deserializationContextMock);
        verify(deserializationContextMock).findContextualValueDeserializer(eq(String.class), eq(beanPropertyMock));
        assertEquals(jsonDeserializerMock, deserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(mock(JsonParser.class), deserializationContextMock, "someTypeId");
        verify(deserializationContextMock).findContextualValueDeserializer(javaTypeMock, beanPropertyMock);
    }
}