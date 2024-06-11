package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.util.ObjectMapperTestBase;

public class AsPropertyTypeDeserializerTest extends ObjectMapperTestBase {

    @Test
    public void testDeserializeTypedFromObjectWhenTypePropertyNameMatches() throws IOException {
        // Given
        String jsonInput = "{\"typePropertyName\": \"StringType\", \"someData\": \"test\"}";
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver idRes = mapper.getTypeIdResolver();
        String typePropertyName = "typePropertyName";
        As inclusion = As.PROPERTY;

        // When
        Object result = mapper.readValue(jsonInput, AsPropertyTypeDeserializer.class, javaType, idRes, typePropertyName, false, null, inclusion);

        // Then
        assertNotNull(result);
        assertEquals("test", result);
    }

    @Test
    public void testDeserializeTypedFromObjectWhenTypePropertyNameDoesNotMatch() throws IOException {
        // Given
        String jsonInput = "{\"otherPropertyName\": \"StringType\", \"someData\": \"test\"}";
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver idRes = mapper.getTypeIdResolver();
        String typePropertyName = "typePropertyName";
        As inclusion = As.PROPERTY;

        // When
        Object result = mapper.readValue(jsonInput, AsPropertyTypeDeserializer.class, javaType, idRes, typePropertyName, false, null, inclusion);

        // Then
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObjectWhenTypeIdVisibleIsTrue() throws IOException {
        // Given
        String jsonInput = "{\"typePropertyName\": \"StringType\", \"someData\": \"test\"}";
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver idRes = mapper.getTypeIdResolver();
        String typePropertyName = "typePropertyName";
        As inclusion = As.PROPERTY;
        boolean typeIdVisible = true;

        // When
        Object result = mapper.readValue(jsonInput, AsPropertyTypeDeserializer.class, javaType, idRes, typePropertyName, typeIdVisible, null, inclusion);

        // Then
        assertNotNull(result);
        assertEquals("test", result);
        // Verify that the type id was included in the result
        assertEquals("StringType", mapper.writerWithTypeId(false).writeValueAsString(result).split(":")[1]);
    }

    @Test
    public void testDeserializeTypedFromObjectWithDefaultImpl() throws IOException {
        // Given
        String jsonInput = "{\"someData\": 123}";
        JavaType javaType = mapper.getTypeFactory().constructType(Integer.class);
        TypeIdResolver idRes = mapper.getTypeIdResolver();
        String typePropertyName = "typePropertyName";
        As inclusion = As.PROPERTY;
        JavaType defaultImpl = mapper.getTypeFactory().constructType(Long.class);

        // When
        Object result = mapper.readValue(jsonInput, AsPropertyTypeDeserializer.class, javaType, idRes, typePropertyName, false, defaultImpl, inclusion);

        // Then
        assertNotNull(result);
        assertEquals(123, result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithTokenBuffer() throws IOException {
        // Given
        String jsonInput = "{\"typePropertyName\": \"StringType\", \"someData\": \"test\"}";
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver idRes = mapper.getTypeIdResolver();
        String typePropertyName = "typePropertyName";
        As inclusion = As.PROPERTY;

        // When
        Object result = mapper.readValue(jsonInput, AsPropertyTypeDeserializer.class, javaType, idRes, typePropertyName, false, null, inclusion);

        // Then
        assertNotNull(result);
        assertEquals("test", result);

        // Given - reuse the same JsonParser for the next test
        JsonParser parser = mapper.getFactory().createParser(jsonInput);

        // When - create a new AsPropertyTypeDeserializer instance
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, idRes, typePropertyName, false, null, inclusion);

        // And - create a new TokenBuffer
        TokenBuffer tokenBuffer = new TokenBuffer(parser, mapper.getDeserializationContext());

        // When - deserialize again with the same JsonParser and the new deserializer
        Object result2 = deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());

        // Then - the deserialization should have consumed the input
        assertEquals(JsonToken.END_OBJECT, parser.getCurrentToken());

        // And - the result should be the same as before
        assertNotNull(result2);
        assertEquals("test", result2);
    }

    @Test
    public void testDeserializeTypedFromObjectWithMissingProperty() throws IOException {
        // Given
        String jsonInput = "{\"someData\": 123}";
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver idRes = mapper.getTypeIdResolver();
        String typePropertyName = "typePropertyName";
        As inclusion = As.PROPERTY;

        // When
        Object result = mapper.readValue(jsonInput, AsPropertyTypeDeserializer.class, javaType, idRes, typePropertyName, false, null, inclusion);

        // Then
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithMissingTypePropertyNameButDefaultImplIsPresent() throws IOException {
        // Given
        String jsonInput = "{\"someData\": 123}";
        JavaType javaType = mapper.getTypeFactory().constructType(Integer.class);
        TypeIdResolver idRes = mapper.getTypeIdResolver();
        String typePropertyName = "typePropertyName";
        As inclusion = As.PROPERTY;
        JavaType defaultImpl = mapper.getTypeFactory().constructType(Long.class);

        // When
        Object result = mapper.readValue(jsonInput, AsPropertyTypeDeserializer.class, javaType, idRes, typePropertyName, false, defaultImpl, inclusion);

        // Then
        assertNotNull(result);
        assertEquals(123, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeTypedFromObjectWithMissingPropertyAndNoDefaultImpl() throws IOException {
        // Given
        String jsonInput = "{\"someData\": 123}";
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver idRes = mapper.getTypeIdResolver();
        String typePropertyName = "typePropertyName";
        As inclusion = As.PROPERTY;

        // When
        Object result = mapper.readValue(jsonInput, AsPropertyTypeDeserializer.class, javaType, idRes, typePropertyName, false, null, inclusion);

        // Then - the deserialization should fail
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        // Given
        String jsonInput = "[\"StringType\", \"test\"]";
        JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
        TypeIdResolver idRes = mapper.getTypeIdResolver();
        String typePropertyName = "typePropertyName";
        As inclusion = As.PROPERTY;

        // When
        Object result = mapper.readValue(jsonInput, AsPropertyTypeDeserializer.class, javaType, idRes, typePropertyName, false, null, inclusion);

        // Then
        assertNotNull(result);
        assertArrayEquals(new String[]{"test"}, result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        // Given
        String jsonInput = "42";
        JavaType javaType = mapper.getTypeFactory().constructType(Integer.class);
        TypeIdResolver idRes = mapper.getTypeIdResolver();
        String typePropertyName = "typePropertyName";
        As inclusion = As.PROPERTY;

        // When
        Object result = mapper.readValue(jsonInput, AsPropertyTypeDeserializer.class, javaType, idRes, typePropertyName, false, null, inclusion);

        // Then
        assertNotNull(result);
        assertEquals(42, result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithMissingTypePropertyNameButDefaultImplIsPresentAndDeserializerPresent() throws IOException {
        // Given
        String jsonInput = "{\"someData\": 123}";
        JavaType javaType = mapper.getTypeFactory().constructType(Integer.class);
        TypeIdResolver idRes = mapper.getTypeIdResolver();
        String typePropertyName = "typePropertyName";
        JavaType defaultImpl = mapper.getTypeFactory().constructType(Long.class);
        JsonDeserializer<Object> customDeserializer = new StringToLongDeserializer();

        // When
        Object result = mapper.readValue(jsonInput, AsPropertyTypeDeserializer.class, javaType, idRes, typePropertyName, false, defaultImpl, inclusion, customDeserializer);

        // Then
        assertNotNull(result);
        assertEquals(123, result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithMissingTypePropertyNameButDeserializerPresent() throws IOException {
        // Given
        String jsonInput = "{\"someData\": 123}";
        JavaType javaType = mapper.getTypeFactory().constructType(Integer.class);
        TypeIdResolver idRes = mapper.getTypeIdResolver();
        String typePropertyName = "typePropertyName";
        JavaType defaultImpl = null;
        JsonDeserializer<Object> customDeserializer = new StringToIntDeserializer();

        // When
        Object result = mapper.readValue(jsonInput, AsPropertyTypeDeserializer.class, javaType, idRes, typePropertyName, false, defaultImpl, inclusion, customDeserializer);

        // Then
        assertNotNull(result);
        assertEquals(123, result);
    }

    private static class StringToIntDeserializer extends StdDeserializer<Integer> {
        public StringToIntDeserializer() { this(null); }
        public StringToIntDeserializer(Class<Integer> t) { super(t); }

        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return Integer.parseInt(p.getText());
        }
    }

    private static class StringToLongDeserializer extends StdDeserializer<Long> {
        public StringToLongDeserializer() { this(null); }
        public StringToLongDeserializer(Class<Long> t) { super(t); }

        @Override
        public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return Long.parseLong(p.getText());
        }
    }
}