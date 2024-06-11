package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class TypeDeserializerBaseTest {

    private static final String TYPE_ID_PROPERTY = "typeId";
    private static final String TYPE_ID_VALUE = "idVal";

    @SuppressWarnings("unchecked")
    private static TypeDeserializerBase createTestInstance(JavaType baseType, TypeIdResolver idResolver) {
        return new TypeDeserializerBase(baseType, idResolver, TYPE_ID_PROPERTY, true, null) {
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId)
                    throws IOException {
                return super._findDeserializer(ctxt, typeId);
            }

            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt)
                    throws IOException {
                return NullifyingDeserializer.instance;
            }
        };
    }

    @org.junit.Test
    public void testFindDeserializerWithNullTypeId() throws IOException {
        TypeDeserializerBase deserializerBase = createTestInstance(mock(JavaType.class), mock(TypeIdResolver.class));
        DeserializationContext ctx = mock(DeserializationContext.class);
        when(ctx.findContextualValueDeserializer(mock(JavaType.class), mock(BeanProperty.class)))
                .thenReturn(mock(JsonDeserializer.class));

        JsonDeserializer<Object> result = deserializerBase._findDeserializer(ctx, null);
        assertNull(result);
    }

    @org.junit.Test
    public void testFindDeserializerWithTypeId() throws IOException {
        TypeDeserializerBase deserializerBase = createTestInstance(mock(JavaType.class), mock(TypeIdResolver.class));
        DeserializationContext ctx = mock(DeserializationContext.class);
        JavaType javaType = mock(JavaType.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<Object> jsonDeserializer = mock(JsonDeserializer.class);

        when(ctx.findContextualValueDeserializer(javaType, property)).thenReturn(jsonDeserializer);
        when(deserializerBase._idResolver.typeFromId(ctx, TYPE_ID_VALUE)).thenReturn(javaType);

        JsonDeserializer<Object> result = deserializerBase._findDeserializer(ctx, TYPE_ID_VALUE);
        assertEquals(jsonDeserializer, result);
    }

    @org.junit.Test
    public void testFindDefaultImplDeserializer() throws IOException {
        TypeDeserializerBase deserializerBase = createTestInstance(mock(JavaType.class), mock(TypeIdResolver.class));

        JsonDeserializer<Object> result = deserializerBase._findDefaultImplDeserializer(mock(DeserializationContext.class));
        assertEquals(NullifyingDeserializer.instance, result);
    }

    @org.junit.Test
    public void testHandleUnknownTypeId() throws IOException {
        TypeDeserializerBase deserializerBase = createTestInstance(mock(JavaType.class), mock(TypeIdResolver.class));
        DeserializationContext ctx = mock(DeserializationContext.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(ctx.handleUnknownTypeId(mock(JavaType.class), null, idResolver, null)).thenThrow(IOException.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[{\"typeId\":\"foo\"}]");
        JsonDeserializer<Object> result = deserializerBase._deserializeWithNativeTypeId(parser, ctx);

        assertNull(result);
    }

    @org.junit.Test
    public void testHandleUnknownTypeIdWithKnownTypeResolver() throws IOException {
        TypeDeserializerBase deserializerBase = createTestInstance(mock(JavaType.class), mock(TypeIdResolverBase.class));
        DeserializationContext ctx = mock(DeserializationContext.class);
        TypeIdResolverBase idResolver = mock(TypeIdResolverBase.class);

        JsonDeserializer<Object> jsonDeserializer = mock(JsonDeserializer.class);
        when(idResolver.getTypeIdResolverBase()).thenReturn("knownTypeIds");
        when(idResolver.getDescForKnownTypeIds()).thenReturn("TypeIdResolverBase with known type ids");
        when(ctx.findContextualValueDeserializer(mock(JavaType.class), mock(BeanProperty.class))).thenReturn(jsonDeserializer);
        when(deserializerBase._idResolver.typeFromId(ctx, TYPE_ID_VALUE)).thenReturn(mock(JavaType.class));

        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[{\"typeId\":\"foo\"}]");
        JsonDeserializer<Object> result = deserializerBase._deserializeWithNativeTypeId(parser, ctx);

        assertEquals(jsonDeserializer, result);
    }

    @org.junit.Test
    public void testHandleUnknownTypeIdWithUnknownTypeResolver() throws IOException {
        TypeDeserializerBase deserializerBase = createTestInstance(mock(JavaType.class), mock(TypeIdResolver.class));
        DeserializationContext ctx = mock(DeserializationContext.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(ctx.handleUnknownTypeId(mock(JavaType.class), null, idResolver, null)).thenReturn(mock(JavaType.class));

        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[{\"typeId\":\"foo\"}]");
        JsonDeserializer<Object> result = deserializerBase._deserializeWithNativeTypeId(parser, ctx);

        assertNull(result);
    }

    @org.junit.Test
    public void testToString() throws IOException {
        TypeDeserializerBase deserializerBase = createTestInstance(mock(JavaType.class), mock(TypeIdResolver.class));
        String result = deserializerBase.toString();
        String expected = "[TypeDeserializerBase; base-type:<...>; id-resolver:<...>]";
        assertEquals(expected, result);
    }

}