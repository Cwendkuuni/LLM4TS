package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolverBase;
import com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    private static final String TYPE_ID_STR = "testTypeId";
    private static final String PROPERTY_NAME = "testPropertyName";
    private static final String TEST_CLASS_NAME = "com.example.TestClass";
    private static final JavaType BASE_TYPE = ClassUtil.constructType(JavaType.ValueType.ANY, TEST_CLASS_NAME);
    private static final TypeIdResolver idResolver = mock(TypeIdResolverBase.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private TypeDeserializerBase typeDeserializer;

    @Before
    public void setup() {
        when(idResolver.typeFromId(any(DeserializationContext.class), eq(TYPE_ID_STR)))
                .thenReturn(BASE_TYPE);
        typeDeserializer = new TypeDeserializerBase(BASE_TYPE, idResolver, PROPERTY_NAME, false, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXTERNAL_PROPERTY;
            }
        };
    }

    @After
    public void tearDown() throws Exception {
        objectMapper.close();
    }

    @Test
    public void testForProperty() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializerBase forProperty = typeDeserializer.forProperty(property);
        assertEquals(typeDeserializer.getClass(), forProperty.getClass());
        assertEquals(property, forProperty._property);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetTypeInclusionIsNull() throws IOException {
        TypeDeserializerBase base = new TypeDeserializerBase(BASE_TYPE, idResolver, PROPERTY_NAME, false, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        base.getTypeInclusion();
    }

    @Test
    public void testBaseTypeName() throws IOException {
        assertEquals(TEST_CLASS_NAME, typeDeserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() throws IOException {
        assertEquals(PROPERTY_NAME, typeDeserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() throws IOException {
        assertEquals(idResolver, typeDeserializer.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() throws IOException {
        assertNull(typeDeserializer.getDefaultImpl());
    }

    @Test
    public void testFindDeserializerNull() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        assertNull(typeDeserializer._findDeserializer(context, null));
    }

    @Test
    public void testFindDeserializerTypeIdFound() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(typeDeserializer._findDeserializer(context, TYPE_ID_STR)).thenReturn(deserializer);
        assertEquals(deserializer, typeDeserializer._findDeserializer(context, TYPE_ID_STR));
    }

    @Test
    public void testFindDeserializerTypeIdNotFound() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        assertNull(typeDeserializer._findDeserializer(context, "unknownTypeId"));
    }

    @Test(expected = JsonMappingException.class)
    public void testFindDeserializerTypeIdNotFoundWithContextualDeserializerNotFound()
            throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        JsonDeserializer<Object> defaultImplDeserializer = mock(JsonDeserializer.class);
        when(typeDeserializer._findDeserializer(context, "unknownTypeId"))
                .thenThrow(new JsonMappingException("Test exception"));
        when(context.findContextualValueDeserializer(any(JavaType.class), any(BeanProperty.class)))
                .thenReturn(null);
        typeDeserializer._handleUnknownTypeId(context, "unknownTypeId");
    }

    @Test(expected = JsonMappingException.class)
    public void testFindDeserializerTypeIdNotFoundWithContextualDeserializerNull()
            throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        when(typeDeserializer._findDeserializer(context, "unknownTypeId"))
                .thenThrow(new JsonMappingException("Test exception"));
        when(context.findContextualValueDeserializer(any(JavaType.class), any(BeanProperty.class)))
                .thenReturn(null);
        typeDeserializer._handleUnknownTypeId(context, "unknownTypeId");
    }

    @Test(expected = JsonMappingException.class)
    public void testFindDeserializerTypeIdNotFoundWithContextualDeserializerNotFoundForBaseType()
            throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        when(typeDeserializer._findDeserializer(context, "unknownTypeId"))
                .thenThrow(new JsonMappingException("Test exception"));
        when(context.findContextualValueDeserializer(eq(BASE_TYPE), any(BeanProperty.class)))
                .thenReturn(null);
        typeDeserializer._handleUnknownTypeId(context, "unknownTypeId");
    }

    @Test(expected = JsonMappingException.class)
    public void testFindDeserializerTypeIdNotFoundWithContextualDeserializerNotNullButWrongType()
            throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        when(typeDeserializer._findDeserializer(context, "unknownTypeId"))
                .thenThrow(new JsonMappingException("Test exception"));
        when(context.findContextualValueDeserializer(any(JavaType.class), any(BeanProperty.class)))
                .thenReturn(mock(JsonDeserializer.class));
        typeDeserializer._handleUnknownTypeId(context, "unknownTypeId");
    }

    @Test
    public void testFindDefaultImplDeserializerNull() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        assertNull(typeDeserializer._findDefaultImplDeserializer(context));
    }

    @Test
    public void testFindDefaultImplDeserializerNotNull() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        JsonDeserializer<Object> defaultImplDeserializer = mock(JsonDeserializer.class);
        when(typeDeserializer._findDefaultImplDeserializer(context))
                .thenReturn(defaultImplDeserializer);
        assertEquals(defaultImplDeserializer, typeDeserializer._findDefaultImplDeserializer(context));
    }

    @Test
    public void testDeserializeWithNativeTypeIdNull() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        when(parser.getTypeId()).thenReturn(null);
        assertNull(typeDeserializer._deserializeWithNativeTypeId(parser, null));
    }

    @Test
    public void testDeserializeWithNativeTypeIdTypeIdFound() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        when(parser.getTypeId()).thenReturn(TYPE_ID_STR);
        Object result = typeDeserializer._deserializeWithNativeTypeId(parser, null, null);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeIdTypeIdNotFound() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        when(parser.getTypeId()).thenReturn(null);
        assertNull(typeDeserializer._deserializeWithNativeTypeId(parser, null, null));
    }

    @Test
    public void testHandleUnknownTypeIdNullTypeId() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        when(typeDeserializer._handleMissingTypeId(context, null)).thenThrow(new IOException("Test exception"));
        assertThrows(JsonParseException.class, () -> typeDeserializer._handleMissingTypeId(context, null));
    }

    @Test
    public void testHandleUnknownTypeIdTypeIdFound() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        when(typeDeserializer._handleMissingTypeId(context, TYPE_ID_STR)).thenReturn(BASE_TYPE);
        assertEquals(BASE_TYPE, typeDeserializer._handleMissingTypeId(context, TYPE_ID_STR));
    }

}