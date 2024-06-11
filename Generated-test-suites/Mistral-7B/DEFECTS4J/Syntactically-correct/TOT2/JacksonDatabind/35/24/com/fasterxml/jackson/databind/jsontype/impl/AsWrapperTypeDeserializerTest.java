package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;

import static org.junit.Assert.*;

public class AsWrapperTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"typeId\":\"com.test.SomeClass\", \"value\":{\"name\":\"John\", \"age\":30}}");
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(SomeClass.class), mapper.getTypeIdResolver(), "_class", true, SomeClassImpl.class);

        // When
        Object result = deserializer.deserializeTypedFromObject(jp, mapper.getDeserializationContext());

        // Then
        assertInstanceOf(SomeClass.class, result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("[{\"typeId\":\"com.test.SomeClass\", \"value\":{\"name\":\"John\", \"age\":30}}]");
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(SomeClass[].class), mapper.getTypeIdResolver(), "_class", true, SomeClassImpl.class);

        // When
        Object result = deserializer.deserializeTypedFromArray(jp, mapper.getDeserializationContext());

        // Then
        assertArrayEquals(new SomeClass[]{new SomeClass()}, result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"com.test.SomeClass:1\"");
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(SomeClass.class), mapper.getTypeIdResolver(), "_class", true, SomeClassImpl.class);

        // When
        Object result = deserializer.deserializeTypedFromScalar(jp, mapper.getDeserializationContext());

        // Then
        assertInstanceOf(SomeClass.class, result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"someOtherField\":\"value\", \"typeId\":\"com.test.SomeClass\", \"value\":{\"name\":\"John\", \"age\":30}}");
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(SomeClass.class), mapper.getTypeIdResolver(), "_class", true, SomeClassImpl.class);

        // When
        Object result = deserializer.deserializeTypedFromAny(jp, mapper.getDeserializationContext());

        // Then
        assertInstanceOf(SomeClass.class, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNullTypeId() throws IOException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{}");
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(SomeClass.class), mapper.getTypeIdResolver(), "_class", true, SomeClassImpl.class);

        // When & Then
        deserializer.deserializeTypedFromObject(jp, mapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithEmptyTypeId() throws IOException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"typeId\":\"\"}");
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(SomeClass.class), mapper.getTypeIdResolver(), "_class", true, SomeClassImpl.class);

        // When & Then
        deserializer.deserializeTypedFromObject(jp, mapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithUnknownTypeId() throws IOException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"typeId\":\"com.test.UnknownClass\", \"value\":{\"name\":\"John\", \"age\":30}}");
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(SomeClass.class), mapper.getTypeIdResolver(), "_class", true, SomeClassImpl.class);

        // When & Then
        deserializer.deserializeTypedFromObject(jp, mapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeWithMissingEndObject() throws IOException {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"typeId\":\"com.test.SomeClass\", \"value\":{\"name\":\"John\", \"age\":30}");
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(SomeClass.class), mapper.getTypeIdResolver(), "_class", true, SomeClassImpl.class);

        // When & Then
        try {
            deserializer.deserializeTypedFromObject(jp, mapper.getDeserializationContext());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }
}