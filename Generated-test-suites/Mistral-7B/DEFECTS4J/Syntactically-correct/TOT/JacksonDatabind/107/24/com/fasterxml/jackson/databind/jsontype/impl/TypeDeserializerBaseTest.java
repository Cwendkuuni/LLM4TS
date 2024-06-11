package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

import java.io.IOException;
import java.util.Arrays;

public class TypeDeserializerBaseTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPPER.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @org.junit.Test
    public void testBaseType() throws IOException {
        TypeDeserializerBase base = new BaseTypeDeserializerBase(
                MAPPER.getTypeFactory().constructType(String.class),
                new DummyTypeIdResolver(), "typePropertyName", false, null);
        assertEquals("java.lang.String", base.baseTypeName());
    }

    @org.junit.Test
    public void testForProperty() throws IOException {
        TypeDeserializerBase base = new BaseTypeDeserializerBase(
                MAPPER.getTypeFactory().constructType(String.class),
                new DummyTypeIdResolver(), "typePropertyName", false, null);
        TypeDeserializerBase property = base.forProperty(new DummyBeanProperty());
        assertEquals("java.lang.String", property.getPropertyName());
        assertEquals(String.class, property.baseType().getRawClass());
    }

    @org.junit.Test
    public void testFindDeserializer() throws IOException {
        TypeDeserializerBase base = new BaseTypeDeserializerBase(
                MAPPER.getTypeFactory().constructType(String.class),
                new DummyTypeIdResolver(), "typePropertyName", false, null);
        JsonDeserializer<Object> deserializer = new DummyJsonDeserializer();
        base._deserializers.put("testTypeId", deserializer);

        JsonParser parser = MAPPER.getFactory().createParser("{\"typeId\":\"testTypeId\"}");
        Object result = base.deserialize(parser, new DummyDeserializationContext());
        assertEquals(deserializer.getClass(), result.getClass());
    }

    @org.junit.Test(expected = IOException.class)
    public void testFindDeserializerUnknownTypeId() throws IOException {
        TypeDeserializerBase base = new BaseTypeDeserializerBase(
                MAPPER.getTypeFactory().constructType(String.class),
                new DummyTypeIdResolver(), "typePropertyName", false, null);
        JsonParser parser = MAPPER.getFactory().createParser("{\"typeId\":\"nonExistentTypeId\"}");
        base.deserialize(parser, new DummyDeserializationContext());
    }

    @org.junit.Test
    public void testFindDefaultImplDeserializer() throws IOException {
        TypeDeserializerBase base = new BaseTypeDeserializerBase(
                MAPPER.getTypeFactory().constructType(String.class),
                new DummyTypeIdResolver(), "typePropertyName", false, null);
        JsonDeserializer<Object> deserializer = new DummyJsonDeserializer();
        base._defaultImplDeserializer = deserializer;

        Object result = base.deserialize(MAPPER.readTree("{}"), new DummyDeserializationContext());
        assertEquals(deserializer.getClass(), result.getClass());
    }

    @org.junit.Test
    public void testHandleUnknownTypeId() throws IOException {
        TypeDeserializerBase base = new BaseTypeDeserializerBase(
                MAPPER.getTypeFactory().constructType(String.class),
                new DummyTypeIdResolver(), "typePropertyName", false, null);
        String unexpectedTypeId = "unexpectedTypeId";
        try {
            base.handleUnknownTypeId(new DummyDeserializationContext(), unexpectedTypeId);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testHandleMissingTypeId() throws IOException {
        TypeDeserializerBase base = new BaseTypeDeserializerBase(
                MAPPER.getTypeFactory().constructType(String.class),
                new DummyTypeIdResolver(), "typePropertyName", false, null);
        String missingTypeId = null;
        try {
            base.handleMissingTypeId(new DummyDeserializationContext(), "missingTypeId");
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    private static class DummyTypeIdResolver implements TypeIdResolver {
        @Override
        public String idFromValue(Object value) {
            return null;
        }

        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId) {
            return null;
        }
    }

    private static class DummyBeanProperty {
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "typePropertyName")
        private String propertyName;
    }

    private static class DummyJsonDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return "dummy";
        }
    }

    private static class DummyDeserializationContext {
    }

    private static void assertEquals(Class<?> expected, Object actual) {
        assertEquals(expected.getName(), actual.getClass().getName());
    }
}