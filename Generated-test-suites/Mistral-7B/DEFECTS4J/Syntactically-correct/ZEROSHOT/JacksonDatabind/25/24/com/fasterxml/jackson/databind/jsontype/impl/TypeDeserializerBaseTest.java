package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseTest;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Test class for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest extends BaseTest {

    private static final String CLASS_NAME = TypeDeserializerBase.class.getName();

    @SuppressWarnings("unchecked")
    private static final JavaType BASE_TYPE = TypeFactory.defaultInstance().constructType(String.class);

    private static final TypeIdResolver ID_RESOLVER = new StubTypeIdResolver();

    /**
     * Test constructor with null arguments.
     */
    @Test
    public void testEmptyConstructor() {
        try {
            new TypeDeserializerBase(null, null, null, false, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            new TypeDeserializerBase(BASE_TYPE, null, null, false, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testValidConstructor() {
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = String[].class;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(BASE_TYPE, ID_RESOLVER, typePropertyName, typeIdVisible, defaultImpl);

        assertEquals(BASE_TYPE, typeDeserializerBase._baseType);
        assertEquals(ID_RESOLVER, typeDeserializerBase._idResolver);
        assertEquals(typePropertyName, typeDeserializerBase._typePropertyName);
        assertEquals(typeIdVisible, typeDeserializerBase._typeIdVisible);
        assertEquals(defaultImpl, typeDeserializerBase._defaultImpl);
        assertNull(typeDeserializerBase._property);
        assertNull(typeDeserializerBase._deserializers);
        assertNull(typeDeserializerBase._defaultImplDeserializer);
    }

    /**
     * Test constructor with non-null arguments and forProperty call.
     */
    @Test
    public void testForPropertyConstructor() {
        BeanProperty property = mock(BeanProperty.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = String[].class;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                new TypeDeserializerBase(BASE_TYPE, ID_RESOLVER, typePropertyName, typeIdVisible, defaultImpl), property);

        assertEquals(BASE_TYPE, typeDeserializerBase._baseType);
        assertEquals(ID_RESOLVER, typeDeserializerBase._idResolver);
        assertEquals(typePropertyName, typeDeserializerBase._typePropertyName);
        assertEquals(typeIdVisible, typeDeserializerBase._typeIdVisible);
        assertEquals(defaultImpl, typeDeserializerBase._defaultImpl);
        assertEquals(property, typeDeserializerBase._property);
        assertNotNull(typeDeserializerBase._deserializers);
        assertNull(typeDeserializerBase._defaultImplDeserializer);
    }

    /**
     * Test forProperty method.
     */
    @Test
    public void testForProperty() {
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(BASE_TYPE, ID_RESOLVER, null, false, null);

        TypeDeserializerBase result = typeDeserializerBase.forProperty(property);

        assertEquals(TYPE_DESERIALIZER_CLASS, result.getClass());
        assertEquals(BASE_TYPE, result._baseType);
        assertEquals(ID_RESOLVER, result._idResolver);
        assertEquals(null, result._typePropertyName);
        assertEquals(false, result._typeIdVisible);
        assertEquals(null, result._defaultImpl);
        assertEquals(property, result._property);
        assertNotNull(result._deserializers);
        assertNull(result._defaultImplDeserializer);
    }

    /**
     * Test getTypeInclusion method.
     */
    @Test
    public void testGetTypeInclusion() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(BASE_TYPE, ID_RESOLVER, null, false, null);

        JsonTypeInfo.As result = typeDeserializerBase.getTypeInclusion();

        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, result);
    }

    /**
     * Test baseTypeName method.
     */
    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(BASE_TYPE, ID_RESOLVER, null, false, null);

        String result = typeDeserializerBase.baseTypeName();

        assertEquals("java.lang.String", result);
    }

    /**
     * Test getPropertyName method.
     */
    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(BASE_TYPE, ID_RESOLVER, "typePropertyName", false, null);

        String result = typeDeserializerBase.getPropertyName();

        assertEquals("typePropertyName", result);
    }

    /**
     * Test getTypeIdResolver method.
     */
    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(BASE_TYPE, ID_RESOLVER, null, false, null);

        TypeIdResolver result = typeDeserializerBase.getTypeIdResolver();

        assertEquals(ID_RESOLVER, result);
    }

    /**
     * Test getDefaultImpl method.
     */
    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(BASE_TYPE, ID_RESOLVER, null, false, null);

        Class<?> result = typeDeserializerBase.getDefaultImpl();

        assertNull(result);
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(BASE_TYPE, ID_RESOLVER, null, false, null);

        String result = typeDeserializerBase.toString();

        assertTrue(result.startsWith("[TypeDeserializerBase; base-type:"));
        assertTrue(result.endsWith("]"));
    }

    /**
     * Test _findDeserializer method.
     */
    @Test
    public void testFindDeserializer() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(BASE_TYPE, ID_RESOLVER, null, false, null);

        String typeId = "typeId";
        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(mockContext(), typeId);

        assertNull(result);
    }

    /**
     * Test _findDeserializer method with registered deserializer.
     */
    @Test
    public void testFindDeserializerWithRegisteredDeserializer() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(BASE_TYPE, ID_RESOLVER, null, false, null);

        String typeId = "typeId";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        typeDeserializerBase._deserializers.put(typeId, deserializer);

        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(mockContext(), typeId);

        assertEquals(deserializer, result);
    }

    /**
     * Test _findDefaultImplDeserializer method.
     */
    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(BASE_TYPE, ID_RESOLVER, null, false, null);

        Class<?> defaultImpl = String[].class;
        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(mockContext());

        assertNull(result);
    }

    /**
     * Test _findDefaultImplDeserializer method with registered deserializer.
     */
    @Test
    public void testFindDefaultImplDeserializerWithRegisteredDeserializer() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(BASE_TYPE, ID_RESOLVER, null, false, null);

        Class<?> defaultImpl = String[].class;
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        typeDeserializerBase._defaultImplDeserializer = deserializer;

        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(mockContext());

        assertEquals(deserializer, result);
    }

    /**
     * Test _deserializeWithNativeTypeId method with null arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeWithNativeTypeIdNull() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(BASE_TYPE, ID_RESOLVER, null, false, null);

        typeDeserializerBase._deserializeWithNativeTypeId(null, mockContext());
    }

    /**
     * Test _deserializeWithNativeTypeId method with null typeId.
     */
    @Test
    public void testDeserializeWithNativeTypeIdNullTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(BASE_TYPE, ID_RESOLVER, null, false, null);

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(mockParser(), mockContext());

        assertNull(result);
    }

    /**
     * Test _deserializeWithNativeTypeId method with registered deserializer.
     */
    @Test
    public void testDeserializeWithNativeTypeIdRegisteredDeserializer() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(BASE_TYPE, ID_RESOLVER, null, false, null);
        String typeId = "typeId";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        typeDeserializerBase._deserializers.put(typeId, deserializer);

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(mockParser(), mockContext(), typeId);

        assertEquals(deserializer.deserialize(mockParser(), mockContext()), result);
    }

    /**
     * Test _deserializeWithNativeTypeId method with unknown typeId.
     */
    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNativeTypeIdUnknownTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(BASE_TYPE, ID_RESOLVER, null, false, null);

        typeDeserializerBase._deserializeWithNativeTypeId(mockParser(), mockContext(), "unknownTypeId");
    }

    /**
     * Test _handleUnknownTypeId method.
     */
    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(BASE_TYPE, ID_RESOLVER, null, false, null);
        String typeId = "typeId";

        typeDeserializerBase._handleUnknownTypeId(mockContext(), typeId, ID_RESOLVER, BASE_TYPE);
    }

    private DeserializationContext mockContext() {
        return createContextBuilder().buildContext(ObjectMapper.getDefaultInstance());
    }

    private JsonParser mockParser() {
        return createParserBuilder().buildParser();
    }

    private static class StubTypeIdResolver implements TypeIdResolver {
        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String id) throws IOException {
            // stub
            return null;
        }

        @Override
        public String idFromValue(DeserializationContext ctxt, Object value) throws IOException {
            // stub
            return null;
        }

        @Override
        public String getDescForKnownTypeIds() {
            // stub
            return null;
        }
    }
}