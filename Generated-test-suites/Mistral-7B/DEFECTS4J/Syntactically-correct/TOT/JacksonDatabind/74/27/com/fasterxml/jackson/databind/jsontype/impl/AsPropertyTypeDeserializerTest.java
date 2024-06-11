package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver.StdTypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializePropertyTypeWithObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdTypeIdResolver typeIdResolver = new StdTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                typeIdResolver, "_typePropertyName", true, String.class);

        String json = "{\"_typePropertyName\":\"StringType\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        Object result = deserializer.deserializeTypedFromObject(parser, null);
        assertEquals("StringType", result);
    }

    @Test
    public void testDeserializePropertyTypeWithInvalidObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdTypeIdResolver typeIdResolver = new StdTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                typeIdResolver, "_typePropertyName", true, String.class);

        String json = "{\"invalidPropertyName\":\"StringType\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        try {
            deserializer.deserializeTypedFromObject(parser, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializePropertyTypeWithArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdTypeIdResolver typeIdResolver = new StdTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(Integer[].class),
                typeIdResolver, "_typePropertyName", true, Integer[].class);

        String json = "[{\"_typePropertyName\":\"IntType\"}, 1]";
        JsonParser parser = mapper.getFactory().createParser(json);
        Object result = deserializer.deserializeTypedFromObject(parser, null);
        assertEquals(Arrays.asList(new Integer[]{"IntType", 1}), result);
    }

    @Test
    public void testDeserializePropertyTypeWithInvalidArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdTypeIdResolver typeIdResolver = new StdTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(Integer[].class),
                typeIdResolver, "_typePropertyName", true, Integer[].class);

        String json = "[{\"invalidPropertyName\":\"IntType\"}, 1]";
        JsonParser parser = mapper.getFactory().createParser(json);
        try {
            deserializer.deserializeTypedFromObject(parser, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializePropertyTypeWithNativeTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdTypeIdResolver typeIdResolver = new StdTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                typeIdResolver, "_typePropertyName", true, String.class);

        String json = "{\"_typePropertyName\":{\"type\":\"String\",\"id\":\"StringType\"}}";
        JsonParser parser = mapper.getFactory().createParser(json);
        Object result = deserializer.deserializeTypedFromObject(parser, null);
        assertEquals("StringType", result);
    }

    @Test
    public void testDeserializePropertyTypeWithInvalidNativeTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdTypeIdResolver typeIdResolver = new StdTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                typeIdResolver, "_typePropertyName", true, String.class);

        String json = "{\"_typePropertyName\":{\"type\":\"InvalidType\",\"id\":\"InvalidType\"}}";
        JsonParser parser = mapper.getFactory().createParser(json);
        try {
            deserializer.deserializeTypedFromObject(parser, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializePropertyTypeWithDefaultImpl() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdTypeIdResolver typeIdResolver = new StdTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                typeIdResolver, "_typePropertyName", true, String.class);

        String json = "{\"_typePropertyName\":\"DefaultImplType\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        Object result = deserializer.deserializeTypedUsingDefaultImpl(parser, null, null);
        assertEquals("DefaultImplType", result);
    }
}