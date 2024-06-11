package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.util.ConcurrentHashMap;
import java.util.Map;

public class TypeDeserializerBaseTest {

    private static final Class<TypeDeserializerBase> CLASS_TYPE_DESERIALIZER_BASE = TypeDeserializerBase.class;

    @org.junit.BeforeClass
    public static void setup() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @org.junit.Test
    public void testTypeDeserializerBaseConstruction() throws Exception {
        TypeIdResolver idResolver = mapper.getTypeFactory();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = String.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mapper.constructType(Object.class), idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertNotNull(typeDeserializerBase);
        assertEquals(mapper.constructType(Object.class), typeDeserializerBase.getBaseType());
        assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
        assertEquals(typePropertyName, typeDeserializerBase.getPropertyName());
        assertEquals(typeIdVisible, typeDeserializerBase.isTypeIdVisible());
        assertNull(typeDeserializerBase.getProperty());
        assertNull(typeDeserializerBase.getDefaultImpl());
        assertNull(typeDeserializerBase.getDefaultImplDeserializer());
        assertNotNull(typeDeserializerBase._deserializers);
        assertFalse(typeDeserializerBase._typeIdVisible);
    }

    @org.junit.Test
    public void testTypeDeserializerBaseForProperty() throws Exception {
        TypeIdResolver idResolver = mapper.getTypeFactory();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = String.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mapper.constructType(Object.class), idResolver, typePropertyName, typeIdVisible, defaultImpl);
        BeanProperty beanProperty = mapper.findProperty("name", String.class);
        TypeDeserializerBase forProperty = typeDeserializerBase.forProperty(beanProperty);
        assertNotNull(forProperty);
        assertEquals(mapper.constructType(Object.class), forProperty.getBaseType());
        assertEquals(idResolver, forProperty.getTypeIdResolver());
        assertEquals(typePropertyName, forProperty.getPropertyName());
        assertEquals(typeIdVisible, forProperty.isTypeIdVisible());
        assertNotNull(forProperty.getProperty());
        assertNotNull(forProperty.getDefaultImpl());
        assertNotNull(forProperty.getDefaultImplDeserializer());
        assertNotNull(forProperty._deserializers);
        assertTrue(forProperty._typeIdVisible);
    }

    @org.junit.Test
    public void testGetTypeInclusion() throws Exception {
        TypeIdResolver idResolver = mapper.getTypeFactory();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = String.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mapper.constructType(Object.class), idResolver, typePropertyName, typeIdVisible, defaultImpl);
        JsonTypeInfo.As getTypeInclusion = typeDeserializerBase.getTypeInclusion();
        assertNull(getTypeInclusion);
    }

    @org.junit.Test
    public void testBaseTypeName() throws Exception {
        TypeIdResolver idResolver = mapper.getTypeFactory();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = String.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mapper.constructType(Object.class), idResolver, typePropertyName, typeIdVisible, defaultImpl);
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals("java.lang.Object", baseTypeName);
    }

    @org.junit.Test
    public void testGetPropertyName() throws Exception {
        TypeIdResolver idResolver = mapper.getTypeFactory();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = String.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mapper.constructType(Object.class), idResolver, typePropertyName, typeIdVisible, defaultImpl);
        String propertyName = typeDeserializerBase.getPropertyName();
        assertEquals("type", propertyName);
    }

    @org.junit.Test
    public void testGetTypeIdResolver() throws Exception {
        TypeIdResolver idResolver = mapper.getTypeFactory();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = String.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mapper.constructType(Object.class), idResolver, typePropertyName, typeIdVisible, defaultImpl);
        TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();
        assertEquals(idResolver, typeIdResolver);
    }

    @org.junit.Test
    public void testGetDefaultImpl() throws Exception {
        TypeIdResolver idResolver = mapper.getTypeFactory();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = String.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mapper.constructType(Object.class), idResolver, typePropertyName, typeIdVisible, defaultImpl);
        Class<?> defaultClass = typeDeserializerBase.getDefaultImpl();
        assertEquals(String.class, defaultClass);
    }

    @org.junit.Test
    public void testToString() throws Exception {
        TypeIdResolver idResolver = mapper.getTypeFactory();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = String.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mapper.constructType(Object.class), idResolver, typePropertyName, typeIdVisible, defaultImpl);
        String toString = typeDeserializerBase.toString();
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:java.lang.Object; id-resolver: com.fasterxml.jackson.databind.JsonTypeFactory@5d270a2d]";
        assertEquals(expected, toString);
    }

    @org.junit.Test
    public void testFindDeserializer() throws Exception {
        TypeIdResolver idResolver = mapper.getTypeFactory();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = String.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mapper.constructType(Object.class), idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = mapper.getDeserializationContext();
        String typeId = "com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase";
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, typeId);
        assertNull(deserializer);

        JavaType javaType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        deserializer = typeDeserializerBase._findDeserializer(context, javaType.getId().toString());
        assertNotNull(deserializer);
        assertInstanceOf(TypeDeserializerBase.class, deserializer);
    }

    @org.junit.Test
    public void testFindDefaultImplDeserializer() throws Exception {
        TypeIdResolver idResolver = mapper.getTypeFactory();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = String.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mapper.constructType(Object.class), idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = mapper.getDeserializationContext();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertNull(deserializer);

        JavaType javaType = mapper.constructType(Object.class);
        deserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertNull(deserializer);

        JavaType javaTypeString = mapper.getTypeFactory().constructType(String.class);
        deserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertNotNull(deserializer);
        assertInstanceOf(NullifyingDeserializer.class, deserializer);
    }

    @org.junit.Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        TypeIdResolver idResolver = mapper.getTypeFactory();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = String.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mapper.constructType(Object.class), idResolver, typePropertyName, typeIdVisible, defaultImpl);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.readerForUpdating(TypeDeserializerBase.class);
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"type\": \"com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase\"}");
        Object readValue = objectReader.readValue(jsonParser);
        assertNotNull(readValue);
        assertEquals(TypeDeserializerBase.class, readValue.getClass());
    }

    @org.junit.Test
    public void testHandleUnknownTypeId() throws Exception {
        TypeIdResolver idResolver = mapper.getTypeFactory();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = String.class;
        String typeId = "unknownTypeId";
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                mapper.constructType(Object.class), idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = mapper.getDeserializationContext();
        JavaType javaType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        try {
            typeDeserializerBase._handleUnknownTypeId(context, typeId, idResolver, javaType);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertNotNull(e.getMessage());
        }
    }
}