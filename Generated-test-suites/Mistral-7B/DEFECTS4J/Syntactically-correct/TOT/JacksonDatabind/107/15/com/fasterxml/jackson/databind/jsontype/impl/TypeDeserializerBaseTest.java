package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBase() throws Exception {
        // Create a test class with a property that will be deserialized using TypeDeserializerBase
        class TestClass {
            @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
            private String type;
            private Object value;
        }

        // Create test data
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeDeserializerFactory().findValueDeserializer(TestClass.class, "type", null);
        JsonTypeInfo typeInfo = mapper.getSerializerProvider().findValueSerializer(TestClass.class, null);

        // Test serialization
        TestClass testClass = new TestClass();
        testClass.type = "CustomType";
        testClass.value = new Object();
        String json = mapper.writeValueAsString(testClass);
        TestClass deserialized = mapper.readValue(json, TestClass.class);
        assertEquals(testClass.type, deserialized.type);
        assertEquals(testClass.value, deserialized.value);

        // Test deserialization with known type id
        String jsonWithTypeId = json.replace("CustomType", "customTypeId");
        deserialized = mapper.readValue(jsonWithTypeId, TestClass.class);
        assertEquals(testClass.type, deserialized.type);
        assertEquals(testClass.value, deserialized.value);

        // Test deserialization with unknown type id
        String jsonWithUnknownTypeId = json.replace("CustomType", "unknownTypeId");
        try {
            deserialized = mapper.readValue(jsonWithUnknownTypeId, TestClass.class);
            fail("Expected JsonMappingException with unknown type id");
        } catch (JsonMappingException e) {
            // Expected
        }

        // Test deserialization with missing type id
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        typeDeserializerBase = mapper.getTypeDeserializerFactory().findValueDeserializer(TestClass.class, null, typeIdResolver);
        String jsonWithoutTypeId = json.replace("\"type\":\"CustomType\"", "");
        try {
            deserialized = mapper.readValue(jsonWithoutTypeId, TestClass.class, null);
            fail("Expected JsonMappingException with missing type id");
        } catch (JsonMappingException e) {
            // Expected
        }

        // Test deserialization with default implementation
        JsonDeserializer<Object> defaultImplDeserializer = mapper.getTypeDeserializerFactory().findValueDeserializer(Object.class, null);
        try {
            deserialized = mapper.readValue(json, TestClass.class, null, null, defaultImplDeserializer);
            fail("Expected null when deserializing with default implementation");
        } catch (IOException e) {
            // Expected
        }

        // Test deserialization with custom deserializer
        JsonDeserializer<Object> customDeserializer = mapper.findValueDeserializer(Object.class, "customDeserializer");
        try {
            deserialized = mapper.readValue(json, TestClass.class, null, null, customDeserializer);
            fail("Expected JsonMappingException when deserializing with custom deserializer");
        } catch (JsonMappingException e) {
            // Expected
        }

        // Test deserialization with valid but unregistered type id
        try {
            typeIdResolver.registerDeserializer(TestClass.class, "customTypeId2", customDeserializer);
            deserialized = mapper.readValue(jsonWithTypeId.replace("customTypeId", "customTypeId2"), TestClass.class);
            assertEquals(testClass.type, deserialized.type);
            assertEquals(testClass.value, deserialized.value);
        } finally {
            typeIdResolver.removeDeserializer(TestClass.class, "customTypeId2");
        }

        // Test deserialization with invalid type id
        try {
            typeIdResolver.registerDeserializer(TestClass.class, "invalidTypeId", customDeserializer);
            deserialized = mapper.readValue(jsonWithTypeId.replace("customTypeId", "invalidTypeId"), TestClass.class);
            fail("Expected JsonMappingException with invalid type id");
        } catch (JsonMappingException e) {
            // Expected
        } finally {
            typeIdResolver.removeDeserializer(TestClass.class, "invalidTypeId");
        }

        // Test deserialization with null value
        String jsonWithNullValue = json.replace("{\"type\":\"CustomType\",\"value\":\"\"}", "{\"type\":\"CustomType\",\"value\":null}");
        deserialized = mapper.readValue(jsonWithNullValue, TestClass.class);
        assertNull(deserialized.value);

        // Test deserialization with empty value
        String jsonWithEmptyValue = json.replace("{\"type\":\"CustomType\",\"value\":\"\"}", "{\"type\":\"CustomType\",\"value\":\"\"}");
        deserialized = mapper.readValue(jsonWithEmptyValue, TestClass.class);
        assertNull(deserialized.value);

        // Test deserialization with missing value
        String jsonWithMissingValue = json.replace("{\"type\":\"CustomType\",\"value\":\"\"}", "{\"type\":\"CustomType\"}");
        try {
            deserialized = mapper.readValue(jsonWithMissingValue, TestClass.class);
            fail("Expected JsonMappingException with missing value");
        } catch (JsonMappingException e) {
            // Expected
        }
    }
}