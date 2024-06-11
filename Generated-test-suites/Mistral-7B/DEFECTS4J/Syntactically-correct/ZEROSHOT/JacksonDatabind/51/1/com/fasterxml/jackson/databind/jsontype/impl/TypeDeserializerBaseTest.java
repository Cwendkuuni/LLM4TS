package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ConcurrentHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * Test class for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest extends TestBase {

    private static final Class<?> BASE_CLASS = TypeDeserializerBase.class;
    private static final String TYPE_PROPERTY_NAME = "typePropertyName";
    private static final boolean TYPE_ID_VISIBLE = true;
    private static final JavaType DEFAULT_IMPL = getCollectionType(ArrayList.class, Integer.class);

    private ObjectMapper mapper;
    private TypeIdResolver idResolver;
    private TypeDeserializerBase typeDeserializer;
    private BeanProperty property;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        idResolver = mapper.getTypeFactory().constructTypeIdResolver();
        typeDeserializer = new TypeDeserializerBase(getCollectionType(List.class, Number.class), idResolver,
                TYPE_PROPERTY_NAME, TYPE_ID_VISIBLE, DEFAULT_IMPL);
        property = new BeanProperty.Std(BASE_CLASS.getDeclaredField("propertyName"), getTypeFactory());
    }

    @Test
    public void testForProperty() throws Exception {
        TypeDeserializer expected = new TypeDeserializerBase(typeDeserializer, property);
        assertEquals(expected.getClass(), typeDeserializer.forProperty(property).getClass());
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As expected = JsonTypeInfo.As.PROPERTY;
        assertEquals(expected, typeDeserializer.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {
        String expected = List.class.getName();
        assertEquals(expected, typeDeserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        String expected = TYPE_PROPERTY_NAME;
        assertEquals(expected, typeDeserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertEquals(idResolver, typeDeserializer.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        assertNull(typeDeserializer.getDefaultImpl());
    }

    @Test
    public void testToString() throws Exception {
        String expected = "[TypeDeserializerBase; base-type:class java.util.List<java.lang.Number>; id-resolver:com.fasterxml.jackson.core.TypeFactory@665d3369]";
        assertEquals(expected, typeDeserializer.toString());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        String typeId = "TypeId1";
        JsonDeserializer<Object> deserializer = mapper.findValueDeserializer(JavaType.constructType(String.class));
        _deserializerMap.put(typeId, deserializer);

        deserializer = typeDeserializer._findDeserializer(mapper.getDeserializationContext(), typeId);
        assertEquals(deserializer, typeDeserializer._deserializers.get(typeId));
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithUnknownTypeId() throws Exception {
        String typeId = "UnknownTypeId";
        typeDeserializer._findDeserializer(mapper.getDeserializationContext(), typeId);
        fail("Should throw IOException");
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        // Default implementation is null, deserializer should be null
        assertNull(typeDeserializer._findDefaultImplDeserializer(mapper.getDeserializationContext()));

        // Set default implementation
        JavaType defaultImpl = getCollectionType(ArrayList.class, String.class);
        typeDeserializer = new TypeDeserializerBase(typeDeserializer, property) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return mapper.findValueDeserializer(defaultImpl);
            }
        };

        // Verify default impl deserializer is not null
        JsonDeserializer<Object> deserializer = typeDeserializer._findDefaultImplDeserializer(mapper.getDeserializationContext());
        assertNotNull(deserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        // Test with null typeId
        JsonParser jp = mapper.getFactory().createParser("[]");
        assertNull(typeDeserializer._deserializeWithNativeTypeId(jp, mapper.getDeserializationContext()));

        // Test with valid typeId
        JsonParser jpTypeId = mapper.getFactory().createParser("[]\"" + TypeIdResolver.class.getName() + "\"");
        jpTypeId.nextToken();
        jpTypeId.nextValue("TypeId1");
        Object result = typeDeserializer._deserializeWithNativeTypeId(jpTypeId, mapper.getDeserializationContext());
        assertNotNull(result);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        // Test with null idResolver
        assertNull(typeDeserializer._handleUnknownTypeId(mapper.getDeserializationContext(), null, null, null));

        // Test with known type id
        JavaType knownTypeId = mapper.getTypeFactory().constructType("java.lang.String");
        String expected = String.class.getName();
        JavaType result = typeDeserializer._handleUnknownTypeId(mapper.getDeserializationContext(), "TypeId1", idResolver, null);
        assertEquals(expected, result.getClassName());

        // Test with unknown type id
        String unknownTypeId = "UnknownTypeId";
        JavaType resultUnknown = typeDeserializer._handleUnknownTypeId(mapper.getDeserializationContext(), unknownTypeId, idResolver, null);
        assertNull(resultUnknown);
    }

    private JavaType getCollectionType(Class<?> collectionClass, Class<?> elementClass) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClass.getTypeParameters());
    }

    private ObjectMapper getObjectMapper() {
        return mapper;
    }

    private ObjectMapper getFactory() {
        return mapper.getFactory();
    }

    private TypeFactory getTypeFactory() {
        return mapper.getTypeFactory();
    }

    private Map<String, JsonDeserializer<Object>> _deserializerMap = new ConcurrentHashMap<>();
}