package com.example.jackson.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseTest;

public class TypeDeserializerBaseTest extends BaseTest {

    private TypeDeserializerBase typeDeserializerBase;
    private DeserializationContext context;
    private JsonParser parser;

    @Before
    public void setUp() throws Exception {
        context = mock(DeserializationContext.class);
        parser = mock(JsonParser.class);

        JavaType baseType = mapType(String.class, Map.class);
        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        when(typeIdResolver.typeFromId(context, "mapStringToMapString")).thenReturn(mapType(String.class, String.class));

        JsonDeserializer<Object> stringDeserializer = mock(JsonDeserializer.class);
        JsonDeserializer<Object> mapStringToStringDeserializer = mock(JsonDeserializer.class);
        when(stringDeserializer.getClass()).thenReturn(String.class);
        when(mapStringToStringDeserializer.getClass()).thenReturn(Map.class);

        typeDeserializerBase = new TypeDeserializerBase(baseType, typeIdResolver, "typePropertyName", true, mapType(String.class, Object.class)) {
            private static final long serialVersionUID = 1;

            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                if ("mapStringToMapString".equals(typeId)) {
                    return mapStringToStringDeserializer;
                }
                return stringDeserializer;
            }

            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
    }

    @Test
    public void testForProperty() throws IOException, JsonProcessingException {
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(property);
        assertNotNull(typeDeserializer);
        assertEquals(TypeDeserializer.class, typeDeserializer.getClass());
    }

    @Test
    public void testGetTypeInclusion() {
        // This method is abstract, so cannot be tested directly
    }

    @Test
    public void testBaseTypeName() throws Exception {
        String result = typeDeserializerBase.baseTypeName();
        assertEquals("java.util.Map", result);
    }

    @Test
    public void testGetPropertyName() throws Exception {
        String result = typeDeserializerBase.getPropertyName();
        assertEquals("typePropertyName", result);
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        TypeIdResolver result = typeDeserializerBase.getTypeIdResolver();
        assertNotNull(result);
        assertEquals(typeIdResolver, result);
    }

    @Test
    public void testDefaultImpl() throws Exception {
        JavaType result = typeDeserializerBase.getDefaultImpl();
        assertEquals(mapType(String.class, Object.class), result);
    }

    @Test
    public void testToString() throws Exception {
        String result = typeDeserializerBase.toString();
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:java.util.Map; id-resolver:....]";
        assertEquals(expected, result);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        when(typeIdResolver.typeFromId(context, "mapStringToMapString")).thenReturn(mapType(String.class, String.class));
        when(context.findContextualValueDeserializer(mapType(String.class, String.class), property)).thenReturn(mapStringToStringDeserializer);

        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(context, "mapStringToMapString");
        assertNotNull(result);
        assertEquals(mapStringToStringDeserializer, result);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        when(context.findContextualValueDeserializer(_defaultImpl, property)).thenReturn(null);

        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeId() throws IOException {
        when(typeIdResolver.getTypeFromId(context, "unknownTypeId")).thenReturn(null);

        typeDeserializerBase._handleUnknownTypeId(context, "unknownTypeId");
    }
}