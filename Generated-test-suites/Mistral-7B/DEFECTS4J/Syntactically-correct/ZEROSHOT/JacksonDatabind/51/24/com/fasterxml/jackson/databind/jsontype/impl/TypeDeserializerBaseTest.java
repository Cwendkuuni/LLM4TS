package com.fasterxml.jackson.databind.jsontype.impl;

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
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

    private JsonDeserializer<Object> defaultImplDeserializer;
    private DeserializationContext context;
    private TypeDeserializerBase typeDeserializerBase;
    private TypeIdResolver typeIdResolver;

    @Before
    public void setup() throws Exception {
        initMocks(this);
        typeIdResolver = mock(TypeIdResolver.class);
        context = mock(DeserializationContext.class);
        JavaType baseType = mock(JavaType.class);
        typeDeserializerBase = new TypeDeserializerBase(baseType, typeIdResolver, "", false, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return defaultImplDeserializer;
            }
        };
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(property);
        assertEquals(TypeDeserializer.class, typeDeserializer.getClass());
        verify(typeDeserializerBase, times(1)).forProperty(property);
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.WRAP_WITH_ARRAY, typeInclusion);
    }

    @Test
    public void testBaseTypeName() throws Exception {
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals("", baseTypeName);
    }

    @Test
    public void testGetPropertyName() throws Exception {
        String propertyName = typeDeserializerBase.getPropertyName();
        assertEquals("", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        TypeIdResolver idResolver = typeDeserializerBase.getTypeIdResolver();
        assertEquals(typeIdResolver, idResolver);
    }

    @Test
    public void testGetDefaultImpl() throws Exception {
        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
        assertNull(defaultImpl);
    }

    @Test
    public void testToString() throws Exception {
        String toString = typeDeserializerBase.toString();
        assertEquals("[TypeDeserializerBase; base-type:null; id-resolver:null]", toString);
    }

    @Test
    public void testFindDeserializer() throws Exception {
        String typeId = "testTypeId";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(typeIdResolver.typeFromId(context, typeId)).thenReturn(null);
        when(typeIdResolver.findDeserializerByTypeId(context, typeId)).thenReturn(deserializer);

        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(context, typeId);
        assertEquals(deserializer, result);
        verify(typeIdResolver, times(1)).typeFromId(context, typeId);
        verify(typeIdResolver, times(1)).findDeserializerByTypeId(context, typeId);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        defaultImplDeserializer = mock(JsonDeserializer.class);
        when(context.findContextualValueDeserializer(eq(_defaultImpl), any(BeanProperty.class))).thenReturn(defaultImplDeserializer);

        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertEquals(defaultImplDeserializer, result);
        verify(context, times(1)).findContextualValueDeserializer(eq(_defaultImpl), any(BeanProperty.class));
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        String typeId = "testTypeId";
        JavaType javaType = mock(JavaType.class);
        when(typeIdResolver.typeFromId(context, typeId)).thenReturn(javaType);
        when(context.handleUnknownTypeId(eq(_baseType), eq(typeId), any(TypeIdResolver.class), anyString())).thenReturn(javaType);

        JavaType result = typeDeserializerBase._handleUnknownTypeId(context, typeId, typeIdResolver, _baseType);
        assertEquals(javaType, result);
        verify(context, times(1)).handleUnknownTypeId(eq(_baseType), eq(typeId), any(TypeIdResolver.class), anyString());
    }
}