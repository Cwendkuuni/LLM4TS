package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.Arrays;

public class TypeDeserializerBaseTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        module.setSerializerFactory(NullifyingDeserializer.instance);
        MAPPER.registerModule(module);
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @org.junit.Test
    public void testBaseTypeDeserialization() throws IOException, JsonProcessingException {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                MAPPER.getTypeFactory().constructType(String.class),
                new NoClassIdResolver(),
                null,
                false,
                null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }
        };

        String json = "{\"@type\":\"java.lang.String\",\"value\":\"Hello\"}";
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        Object result = deserializer.deserialize(MAPPER.readTree(json), ctxt);
        assertNotNull(result);
        assertEquals("Hello", result);
    }

    @org.junit.Test
    public void testTypeIdResolverDeserialization() throws IOException, JsonProcessingException {
        TypeIdResolver idResolver = new SimpleTypeIdResolver();
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                MAPPER.getTypeFactory().constructType(String.class),
                idResolver,
                null,
                false,
                null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }
        };

        String json = "{\"@type\":\"com.example.MyClass\",\"value\":\"Hello\"}";
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        ctxt.registerTypeIdResolver(idResolver);
        Object result = deserializer.deserialize(MAPPER.readTree(json), ctxt);
        assertNotNull(result);
        // Replace MyClass with the actual class name and implementation
    }

    @org.junit.Test
    public void testMissingTypeId() throws IOException, JsonProcessingException {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                MAPPER.getTypeFactory().constructType(String.class),
                null,
                null,
                false,
                null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }
        };

        String json = "{\"value\":\"Hello\"}";
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        Object result = deserializer.deserialize(MAPPER.readTree(json), ctxt);
        assertNull(result);
    }

    @org.junit.Test
    public void testNullTypeId() throws IOException, JsonProcessingException {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                MAPPER.getTypeFactory().constructType(String.class),
                null,
                null,
                false,
                null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }
        };

        String json = "{\"value\":\"Hello\"}";
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        Object result = deserializer.deserialize(MAPPER.readTree(json), ctxt);
        assertNull(result);
    }

    @org.junit.Test
    public void testDefaultImplDeserialization() throws IOException, JsonProcessingException {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                MAPPER.getTypeFactory().constructType(String.class),
                new NoClassIdResolver(),
                null,
                false,
                MAPPER.getTypeFactory().constructType(Integer.class)) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }
        };

        String json = "{\"@type\":null,\"value\":42}";
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        Object result = deserializer.deserialize(MAPPER.readTree(json), ctxt);
        assertNotNull(result);
        assertEquals(42, result);
    }

    @org.junit.Test
    public void testUnknownTypeId() throws IOException, JsonProcessingException {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                MAPPER.getTypeFactory().constructType(String.class),
                new NoClassIdResolver(),
                null,
                false,
                null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }
        };

        String json = "{\"@type\":\"UnknownType\",\"value\":\"Hello\"}";
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        try {
            Object result = deserializer.deserialize(MAPPER.readTree(json), ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testTypeIdVisibility() throws IOException, JsonProcessingException {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                MAPPER.getTypeFactory().constructType(String.class),
                new NoClassIdResolver(),
                "_type",
                true,
                null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }
        };

        String json = "{\"value\":\"Hello\",\"_type\":\"java.lang.String\"}";
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        Object result = deserializer.deserialize(MAPPER.readTree(json), ctxt);
        assertNotNull(result);
        assertEquals("Hello", result);
    }

    @org.junit.Test
    public void testTypeIdVisibilityHidden() throws IOException, JsonProcessingException {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                MAPPER.getTypeFactory().constructType(String.class),
                new NoClassIdResolver(),
                "_type",
                false,
                null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }
        };

        String json = "{\"value\":\"Hello\",\"_type\":\"java.lang.String\"}";
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        Object result = deserializer.deserialize(MAPPER.readTree(json), ctxt);
        assertNotNull(result);
        assertEquals("Hello", result);
    }

    @org.junit.Test
    public void testTypeIdVisibilityMultiple() throws IOException, JsonProcessingException {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                MAPPER.getTypeFactory().constructType(Object[].class),
                new NoClassIdResolver(),
                "_types",
                true,
                null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }
        };

        String json = "{\"value\":[{\"value\":1,\"_type\":\"java.lang.Integer\"},{\"value\":\"Hello\",\"_type\":\"java.lang.String\"}]}";
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        Object result = deserializer.deserialize(MAPPER.readTree(json), ctxt);
        assertNotNull(result);
        assertArrayEquals(new Object[]{1, "Hello"}, result);
    }

    @org.junit.Test
    public void testTypeIdVisibilityHiddenMultiple() throws IOException, JsonProcessingException {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                MAPPER.getTypeFactory().constructType(Object[].class),
                new NoClassIdResolver(),
                "_types",
                false,
                null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }
        };

        String json = "{\"value\":[{\"value\":1,\"_type\":\"java.lang.Integer\"},{\"value\":\"Hello\",\"_type\":\"java.lang.String\"}]}";
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        Object result = deserializer.deserialize(MAPPER.readTree(json), ctxt);
        assertNotNull(result);
        assertArrayEquals(new Object[]{1, "Hello"}, result);
    }

    private static class NoClassIdResolver extends TypeIdResolverBase {
        @Override
        public String idFromValue(Object value) {
            return null;
        }

        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
            if (typeId != null) {
                throw new IOException("Unexpected type id: " + typeId);
            }
            return null;
        }

        @Override
        public String idFromValueAndType(DeserializationContext ctxt, JavaType type) throws IOException {
            return null;
        }
    }

    private static void assertArrayEquals(Object[] expected, Object[] actual) {
        if (!Arrays.deepEquals(expected, actual)) {
            fail("Expected: " + Arrays.toString(expected) + "; Actual: " + Arrays.toString(actual));
        }
    }

    private static void assertNotNull(Object obj) {
        if (obj == null) {
            fail("Expected non-null object");
        }
    }
}