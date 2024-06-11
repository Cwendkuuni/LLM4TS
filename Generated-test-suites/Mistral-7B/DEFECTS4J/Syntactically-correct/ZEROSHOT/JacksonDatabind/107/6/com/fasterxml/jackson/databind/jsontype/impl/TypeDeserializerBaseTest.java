package com.example;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private DeserializationContext ctxt;
    private JsonParser jp;
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() {
        ctxt = mock(DeserializationContext.class);
        jp = mock(JsonParser.class);

        JavaType baseType = mapType(String.class, int.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }

            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId)
                    throws IOException {
                return null;
            }

            @Override
            protected JavaType _handleMissingTypeId(DeserializationContext ctxt, String extraDesc)
                    throws IOException {
                return null;
            }
        };
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializer deserializer = typeDeserializerBase.forProperty(property);
        assertEquals(typeDeserializerBase, deserializer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForPropertyNullProperty() throws Exception {
        typeDeserializerBase.forProperty(null);
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws Exception {
        assertEquals("java.lang.String", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() throws Exception {
        assertEquals(null, typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() throws Exception {
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() throws Exception {
        String expected = "[com.example.TypeDeserializerBase; base-type:class java.lang.String; id-resolver:<mocked>]";
        assertEquals(expected, typeDeserializerBase.toString());
    }

    @Test
    public void testFindDeserializerNullContext() throws Exception {
        assertNull(typeDeserializerBase._findDeserializer(null, "typeId"));
    }

    @Test
    public void testFindDeserializerEmptyDeserializersMap() throws Exception {
        when(ctxt.getTypeFactory().constructType(String.class, String.class)).thenReturn(mapType(String.class, String.class));
        when(ctxt.findContextualValueDeserializer(mapType(String.class, String.class), null)).thenReturn(mock(JsonDeserializer.class));

        typeDeserializerBase._deserializers = new ConcurrentHashMap<>();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, "typeId");
        assertEquals(mock(JsonDeserializer.class), deserializer);
        verify(ctxt, times(1)).findContextualValueDeserializer(mapType(String.class, String.class), null);
    }

    @Test
    public void testFindDeserializerNonEmptyDeserializersMap() throws Exception {
        when(ctxt.getTypeFactory().constructType(String.class, String.class)).thenReturn(mapType(String.class, String.class));
        when(ctxt.findContextualValueDeserializer(mapType(String.class, String.class), null)).thenReturn(mock(JsonDeserializer.class));

        typeDeserializerBase._deserializers = new ConcurrentHashMap<>();
        typeDeserializerBase._deserializers.put("typeId", mock(JsonDeserializer.class));

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, "typeId");
        assertEquals(mock(JsonDeserializer.class), deserializer);
        verify(ctxt, never()).findContextualValueDeserializer(mapType(String.class, String.class), null);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerNullTypeId() throws Exception {
        typeDeserializerBase._findDeserializer(ctxt, null);
    }

    @Test
    public void testFindDefaultImplDeserializerNullContext() throws Exception {
        assertNull(typeDeserializerBase._findDefaultImplDeserializer(null));
    }

    @Test
    public void testFindDefaultImplDeserializerEmptyDeserializersMap() throws Exception {
        when(ctxt.findContextualValueDeserializer(eq(null), any())).thenReturn(null);

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNull(deserializer);
        verify(ctxt, times(1)).findContextualValueDeserializer(eq(null), any());
    }

    @Test
    public void testFindDefaultImplDeserializerNonEmptyDeserializersMap() throws Exception {
        when(ctxt.findContextualValueDeserializer(eq(null), any())).thenReturn(mock(JsonDeserializer.class));

        typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        verify(ctxt, never()).findContextualValueDeserializer(eq(null), any());
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdNullContext() throws Exception {
        typeDeserializerBase._handleUnknownTypeId(null, "extraDesc");
    }

    @Test
    public void testHandleUnknownTypeIdEmptyDeserializersMap() throws Exception {
        JavaType javaType = mapType(String.class, int.class);

        when(ctxt.handleUnknownTypeId(javaType, "unknownTypeId", idResolver, "extraDesc")).thenReturn(mapType(String.class, int.class));

        JavaType result = typeDeserializerBase._handleUnknownTypeId(ctxt, "unknownTypeId");
        assertEquals(mapType(String.class, int.class), result);
        verify(ctxt, times(1)).handleUnknownTypeId(javaType, "unknownTypeId", idResolver, "extraDesc");
    }

    @Test
    public void testHandleUnknownTypeIdNonEmptyDeserializersMap() throws Exception {
        JavaType javaType = mapType(String.class, int.class);

        when(ctxt.handleUnknownTypeId(javaType, "unknownTypeId", idResolver, "extraDesc")).thenReturn(mapType(String.class, int.class));

        typeDeserializerBase._handleUnknownTypeId(ctxt, "unknownTypeId");
        verify(ctxt, never()).handleUnknownTypeId(javaType, "unknownTypeId", idResolver, "extraDesc");
    }

    @Test(expected = IOException.class)
    public void testHandleMissingTypeIdNullContext() throws Exception {
        typeDeserializerBase._handleMissingTypeId(null, "extraDesc");
    }

    @Test
    public void testHandleMissingTypeIdEmptyDeserializersMap() throws Exception {
        JavaType javaType = mapType(String.class, int.class);

        when(ctxt.handleMissingTypeId(javaType, idResolver, "extraDesc")).thenReturn(mapType(String.class, int.class));

        JavaType result = typeDeserializerBase._handleMissingTypeId(ctxt, "extraDesc");
        assertEquals(mapType(String.class, int.class), result);
        verify(ctxt, times(1)).handleMissingTypeId(javaType, idResolver, "extraDesc");
    }

    @Test
    public void testHandleMissingTypeIdNonEmptyDeserializersMap() throws Exception {
        JavaType javaType = mapType(String.class, int.class);

        when(ctxt.handleMissingTypeId(javaType, idResolver, "extraDesc")).thenReturn(mapType(String.class, int.class));

        typeDeserializerBase._handleMissingTypeId(ctxt, "extraDesc");
        verify(ctxt, never()).handleMissingTypeId(javaType, idResolver, "extraDesc");
    }

    private JavaType mapType(Class<?>... componentTypes) {
        SimpleType simpleType = new SimpleType();
        simpleType.setContentType(ClassUtil.makeReferenceType(componentTypes));
        return simpleType;
    }
}