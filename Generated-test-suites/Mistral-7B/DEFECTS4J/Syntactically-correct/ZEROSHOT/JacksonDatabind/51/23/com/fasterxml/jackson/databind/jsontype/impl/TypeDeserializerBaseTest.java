package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    private ObjectMapper mapper;
    private JsonFactory jsonFactory;
    private DeserializationContext ctxt;
    private TypeDeserializerBase typeDeserializer;

    @Before
    public void setup() throws Exception {
        jsonFactory = new JsonFactory();
        mapper = new ObjectMapper();
        ctxt = mapper.getDeserializationContext();
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        JavaType javaType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        typeDeserializer = (TypeDeserializerBase) mapper.readValue(jsonFactory.createParser(getClass().getResourceAsStream("/TypeDeserializerBase.class")), javaType);
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty property = mapper.findBeanProperty("testPropertyName", String.class);
        TypeDeserializerBase newTypeDeserializer = typeDeserializer.forProperty(property);
        assertNotNull(newTypeDeserializer);
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        JsonTypeInfo.As inclusion = typeDeserializer.getTypeInclusion();
        assertNotNull(inclusion);
    }

    @Test
    public void testBaseTypeName() throws Exception {
        String baseTypeName = typeDeserializer.baseTypeName();
        assertNotNull(baseTypeName);
    }

    @Test
    public void testGetPropertyName() throws Exception {
        String propertyName = typeDeserializer.getPropertyName();
        assertNotNull(propertyName);
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        TypeIdResolver typeIdResolver = typeDeserializer.getTypeIdResolver();
        assertNotNull(typeIdResolver);
    }

    @Test
    public void testDefaultImpl() throws Exception {
        Class<?> defaultImpl = typeDeserializer.getDefaultImpl();
        assertNull(defaultImpl);
    }

    @Test
    public void testToString() throws Exception {
        String toString = typeDeserializer.toString();
        assertNotNull(toString);
    }

    @Test
    public void testFindDeserializer() throws Exception {
        String typeId = "com.test.TestClass";
        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(ctxt, typeId);
        assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        JsonDeserializer<Object> defaultImplDeserializer = typeDeserializer._findDefaultImplDeserializer(ctxt);
        assertNull(defaultImplDeserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        String json = "{\"typeId\":\"com.test.TestClass\"}";
        JsonParser jsonParser = jsonFactory.createParser(json);
        Object result = typeDeserializer._deserializeWithNativeTypeId(jsonParser, ctxt);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        String typeId = "unknownTypeId";
        JavaScriptObject unknownTypeId = null;
        try {
            JavaType javaType = typeDeserializer._handleUnknownTypeId(ctxt, typeId, idResolver, typeDeserializer._baseType);
            unknownTypeId = mapper.readValue(jsonFactory.createParser(getClass().getResourceAsStream("/unknownTypeId.json")), new TypeReference<JavaScriptObject>() {});
            assertNotNull(javaType);
            assertNotNull(unknownTypeId);
            assertEquals(javaType.getRawClass().getName(), unknownTypeId.getClass().getName());
        } catch (IOException e) {
            // expected
        }
    }
}