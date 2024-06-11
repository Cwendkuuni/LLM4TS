package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TypeDeserializerBaseTest {

    private ObjectMapper objectMapper;
    private JsonParser jsonParser;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        jsonParser = mock(JsonParser.class);
    }

    @Test
    public void testFindDeserializerWithNullTypeId() throws JsonProcessingException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mock(JavaType.class), mock(TypeIdResolver.class), null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }
        };

        when(jsonParser.getTypeId()).thenReturn(null);

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(mock(DeserializationContext.class), null);

        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerWithKnownTypeId() throws JsonProcessingException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mock(JavaType.class), mock(TypeIdResolver.class), null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                if ("typeA".equals(typeId)) {
                    return mock(JsonDeserializer.class);
                }
                return null;
            }
        };

        when(jsonParser.getTypeId()).thenReturn("typeA");

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(mock(DeserializationContext.class), jsonParser.getTypeId());

        assertEquals(mock(JsonDeserializer.class), deserializer);
    }

    @Test
    public void testFindDeserializerWithUnknownTypeId() throws JsonProcessingException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mock(JavaType.class), mock(TypeIdResolver.class), null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }

            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
                throw new RuntimeException("Not supported");
            }

            @Override
            protected Object _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return new Object();
            }
        };

        when(jsonParser.getTypeId()).thenReturn("unknownTypeId");

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, mock(DeserializationContext.class), jsonParser.getTypeId());

        assertEquals(new Object(), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindDeserializerWithUnknownTypeIdThrowException() throws JsonProcessingException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mock(JavaType.class), mock(TypeIdResolver.class), null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };

        when(jsonParser.getTypeId()).thenReturn("unknownTypeId");

        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, mock(DeserializationContext.class), jsonParser.getTypeId());
    }

    @Test
    public void testFindDefaultImplDeserializer() throws JsonProcessingException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mock(JavaType.class), mock(TypeIdResolver.class), null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }
        };

        when(jsonParser.getTypeId()).thenReturn(null);

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(mock(DeserializationContext.class));

        assertEquals(NullifyingDeserializer.instance, deserializer);
    }
}