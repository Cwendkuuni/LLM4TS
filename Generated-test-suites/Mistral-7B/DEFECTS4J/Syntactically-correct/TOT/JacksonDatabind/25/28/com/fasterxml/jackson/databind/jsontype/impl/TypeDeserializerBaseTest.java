package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

    private ObjectMapper objectMapper;
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() throws JsonProcessingException {
        ObjectMapper.DEFAULT_INITIALIZER = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        TypeIdResolver typeIdResolver = new TypeIdResolverBase() {
            @Override
            public JavaType resolveTypeId(DeserializationContext ctxt, String id) throws IOException {
                if ("MyClass".equals(id)) {
                    return ctxt.constructType(MyClass.class);
                }
                return null;
            }
        };
        JavaType baseType = ctxt.constructType(TypeDeserializerBase.class);
        typeDeserializerBase = new TypeDeserializerBase(baseType, typeIdResolver, null, false, null);
    }

    @Test
    public void testForProperty() throws IOException {
        BeanProperty beanProperty = new BeanProperty("propertyName", String.class);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(beanProperty);
        assertEquals(TypeDeserializerBase.class, typeDeserializer.getClass());
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.WRAP_ROOT_VALUE, typeInclusion);
    }

    @Test
    public void testBaseTypeName() throws IOException {
        assertEquals(TypeDeserializerBase.class.getName(), typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testgetPropertyName() throws IOException {
        assertNull(typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() throws IOException {
        assertEquals(typeIdResolverBase.getClass(), typeDeserializerBase.getTypeIdResolver().getClass());
    }

    @Test
    public void testGetDefaultImpl() throws IOException {
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testGetStringProperty() throws IOException {
        String json = "{\"propertyName\": \"test\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext());
        assertEquals("test", result);
    }

    @Test
    public void testGetDefaultImpl_WhenNoTypeId() throws IOException {
        String json = "{}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext());
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetDefaultImpl_WhenUnknownTypeId() throws IOException {
        String json = "{\"typeId\": \"UnknownTypeId\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext());
    }

    @Test
    public void testGetDeserializerWithTypeId() throws IOException {
        String json = "{\"typeId\": \"MyClass\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(MyClass.class, result.getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testGetDeserializerWithUnknownTypeId() throws IOException {
        String json = "{\"typeId\": \"UnknownTypeId\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        String json = "{\"typeId\": \"MyClass\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        Object result = typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), "MyClass").deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(MyClass.class, result.getClass());
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        Object result = typeDeserializerBase._findDefaultImplDeserializer(objectMapper.getDeserializationContext()).deserialize(null, objectMapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        JavaType jsonType = objectMapper.getFactory().getSimpleType(Map.class);
        String json = "{\"typeId\": \"" + jsonType.getName() + "\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        try {
            typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext());
        } catch (JsonMappingException e) {
            Map<String, Object> result = (Map<String, Object>) e.getDeserializationContext().readValueAs(HashMap.class, e.getDeserializationContext());
            assertEquals(TypeDeserializerBase.class.getName(), result.get("path")[0].getClass().getName());
            assertEquals("expected type id of class [simple type, class java.util.Map]", result.get("message"));
            throw e;
        }
    }
}