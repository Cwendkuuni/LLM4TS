package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        TypeIdResolver idResolver = new DefaultTypeIdResolver();
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        typeDeserializerBase = new TypeDeserializerBase(javaType, idResolver, "", false, null);
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty property = new BeanProperty("propertyName", String.class);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(property);
        assertEquals(TypeDeserializerBase.class, typeDeserializer.getClass());
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.WRAP_OBJECT, typeInclusion);
    }

    @Test
    public void testBaseTypeName() throws Exception {
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals(String.class.getName(), baseTypeName);
    }

    @Test
    public void testPropertyName() throws Exception {
        assertEquals("", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testTypeIdResolver() throws Exception {
        TypeIdResolver idResolver = typeDeserializerBase.getTypeIdResolver();
        assertNotNull(idResolver);
    }

    @Test
    public void testDefaultImpl() throws Exception {
        assertEquals(null, typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        String typeId = "com.example.ExampleClass";
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), typeId);
        assertNull(deserializer);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithTypeIdNotFound() throws Exception {
        String typeId = "com.example.unknownType";
        typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), typeId);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        assertEquals(NullifyingDeserializer.instance, typeDeserializerBase._findDefaultImplDeserializer(objectMapper.getDeserializationContext()));
    }

    @Test(expected = IOException.class)
    public void testFindDefaultImplDeserializerNull() throws Exception {
        typeDeserializerBase._findDefaultImplDeserializer(objectMapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        JsonParser parser = objectMapper.getFactory().createParser("{\"typeId\":\"java.lang.String\"}");
        parser.nextToken();
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(parser, objectMapper.getDeserializationContext());
        assertEquals("", result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdInvalidTypeId() throws Exception {
        JsonParser parser = objectMapper.getFactory().createParser("{\"typeId\":\"invalidTypeId\"}");
        parser.nextToken();
        typeDeserializerBase._deserializeWithNativeTypeId(parser, objectMapper.getDeserializationContext());
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        TypeIdResolver idResolver = new DefaultTypeIdResolver();
        JavaType javaType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(javaType, idResolver, "", false, null);
        String typeId = "unknownType";
        JavaType result = typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), typeId, idResolver, javaType);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeIdWithCustomIdResolver() throws Exception {
        TypeIdResolver idResolver = new CustomTypeIdResolver();
        JavaType javaType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(javaType, idResolver, "", false, null);
        String typeId = "customType";
        JavaType result = typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), typeId, idResolver, javaType);
        assertNotNull(result);
    }
}

class CustomTypeIdResolver extends TypeIdResolverBase {
    @Override
    public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
        if ("customType".equals(typeId)) {
            return TypeFactory.defaultInstance().constructType(CustomType.class);
        }
        return super.typeFromId(ctxt, typeId);
    }
}

class CustomType {
}