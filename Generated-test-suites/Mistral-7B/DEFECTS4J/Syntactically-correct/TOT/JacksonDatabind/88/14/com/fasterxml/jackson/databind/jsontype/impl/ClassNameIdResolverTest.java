package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

    private ObjectMapper objectMapper;

    @BeforeClass
    public void setup() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new StringSerializer());
        module.addDeserializer(String.class, new StringDeserializer());
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
    }

    // Test for idFromValue method
    @Test
    public void testIdFromValue() throws IOException {
        TypeIdResolver resolver = new ClassNameIdResolver(String.class.getTypeName(), objectMapper.getTypeFactory());
        Object value = new String("Hello World");

        String id = resolver.idFromValue(value);
        assertEquals("java.lang.String", id);

        // Test for null value
        id = resolver.idFromValue(null);
        assertNull(id);

        // Test for non-String value
        value = new ArrayList<>();
        id = resolver.idFromValue(value);
        assertNull(id);
    }

    // Test for idFromValueAndType method
    @Test
    public void testIdFromValueAndType() throws IOException {
        TypeIdResolver resolver = new ClassNameIdResolver(String.class.getTypeName(), objectMapper.getTypeFactory());
        Object value = new String("Hello World");
        Class<?> type = ArrayList.class;

        String id = resolver.idFromValueAndType(value, type);
        assertNull(id);

        // Test for same value and type
        id = resolver.idFromValueAndType(value, value.getClass());
        assertEquals("java.lang.String", id);

        // Test for null value and type
        id = resolver.idFromValueAndType(null, null);
        assertNull(id);

        // Test for non-String value and type
        value = new ArrayList<>();
        id = resolver.idFromValueAndType(value, String.class);
        assertNull(id);
    }

    // Test for typeFromId method
    @Test
    public void testTypeFromId() throws IOException {
        TypeIdResolver resolver = new ClassNameIdResolver(String.class.getTypeName(), objectMapper.getTypeFactory());

        // Test for basic class name
        JavaType type = resolver.typeFromId(objectMapper.getDeserializationContext(), "java.lang.String");
        assertEquals(String.class, type.getRawClass());

        // Test for generic class name
        type = resolver.typeFromId(objectMapper.getDeserializationContext(), "java.util.ArrayList<java.lang.String>");
        assertEquals(ArrayList.class, type.getRawClass());

        // Test for non-existent class name
        try {
            type = resolver.typeFromId(objectMapper.getDeserializationContext(), "com.non.existent.package.NonExistentClass");
            fail();
        } catch (IOException e) {
            // Expected
        }

        // Test for null context
        try {
            type = resolver.typeFromId(null, "java.lang.String");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test for null id
        try {
            type = resolver.typeFromId(objectMapper.getDeserializationContext(), null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Helper classes for Serialization and Deserialization tests
    static class StringSerializer extends StdSerializer<String> {
        public StringSerializer() {
            this(null);
        }

        public StringSerializer(Class<String> t) {
            super(t);
        }

        @Override
        public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString(value);
        }
    }

    static class StringDeserializer extends StdDeserializer<String> {
        public StringDeserializer() {
            this(null);
        }

        public StringDeserializer(Class<String> t) {
            super(t);
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }
}