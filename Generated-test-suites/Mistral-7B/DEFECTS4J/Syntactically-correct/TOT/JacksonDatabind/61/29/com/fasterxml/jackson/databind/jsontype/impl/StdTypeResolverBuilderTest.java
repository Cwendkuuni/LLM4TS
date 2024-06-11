package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MapperFactory mapperFactory = new MapperFactory();
        objectMapper = mapperFactory.builder()
                .build();
    }

    @Test
    public void testStdTypeResolverBuilder_DefaultConfiguration() {
        // Test default configuration
        StdTypeResolverBuilder typeResolverBuilder = new StdTypeResolverBuilder();
        TypeResolver<?> typeResolver = typeResolverBuilder.build();

        // Test serialization
        Object input = new Object();
        Object output;

        try {
            output = objectMapper.writeValueAsBytes(input);
        } catch (JsonProcessingException e) {
            fail("Failed to serialize default configuration");
            return;
        }

        Object deserializedObject = null;

        try {
            deserializedObject = objectMapper.readValue(output, Object.class);
        } catch (JsonProcessingException e) {
            fail("Failed to deserialize default configuration");
            return;
        }

        assertEquals(input, deserializedObject);

        // Test type information is not present
        ObjectTypeResolver typeResolverWithoutTypeInfo = new ObjectTypeResolver(null);
        assertFalse(typeResolverWithoutTypeInfo.canResolve(Object.class));

        // Test type resolution
        StdTypeResolverBuilder typeResolverBuilderWithTypeInfo = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null);
        TypeResolver<?> typeResolverWithTypeInfo = typeResolverBuilderWithTypeInfo.build();

        assertEquals(String.class, typeResolverWithTypeInfo.resolveValueType(
                new TypeReference<String>() {}.getType()));
    }

    @Test
    public void testStdTypeResolverBuilder_CustomIdType() {
        // Test custom idType
        StdTypeResolverBuilder typeResolverBuilder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.NAME, null);

        TypeSerializer typeSerializer = typeResolverBuilder.buildTypeSerializer(
                null, new TypeReference<String>() {}.getType(), null);

        // Test serialization
        Object input = "test";
        Object output;

        try {
            output = objectMapper.writeValueAsBytes(input);
        } catch (JsonProcessingException e) {
            fail("Failed to serialize custom idType");
            return;
        }

        ByteArrayInputStream inputStream = new ByteArrayInputStream(output);
        Object deserializedObject = null;

        try {
            deserializedObject = objectMapper.readValue(inputStream, Object.class);
        } catch (JsonProcessingException | IOException e) {
            fail("Failed to deserialize custom idType");
            return;
        }

        assertEquals(input, deserializedObject);

        // Test type resolution
        TypeDeserializer typeDeserializer = typeResolverBuilder.buildTypeDeserializer(
                null, new TypeReference<String>() {}.getType(), null);

        assertEquals(String.class, typeDeserializer.deserializeValueAs(null, null));
    }

    @Test
    public void testStdTypeResolverBuilder_CustomIncludeAs() {
        // Test custom includeAs
        StdTypeResolverBuilder typeResolverBuilder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(As.PROPERTY);

        TypeSerializer typeSerializer = typeResolverBuilder.buildTypeSerializer(
                null, new TypeReference<String>() {}.getType(), null);

        // Test serialization
        Object input = "test";
        Object output;

        try {
            output = objectMapper.writeValueAsBytes(input);
        } catch (JsonProcessingException e) {
            fail("Failed to serialize custom includeAs");
            return;
        }

        ByteArrayInputStream inputStream = new ByteArrayInputStream(output);
        Object deserializedObject = null;

        try {
            deserializedObject = objectMapper.readValue(inputStream, Map.class);
        } catch (JsonProcessingException | IOException e) {
            fail("Failed to deserialize custom includeAs");
            return;
        }

        Map<String, Object> map = (Map<String, Object>) deserializedObject;
        assertEquals("test", map.get("test"));

        // Test type resolution
        TypeDeserializer typeDeserializer = typeResolverBuilder.buildTypeDeserializer(
                null, new TypeReference<Map<String, Object>>() {}.getType(), null);

        assertEquals(Map.class, typeDeserializer.deserializeValueAs(null, null));
    }
}