package com.example.junit;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBase() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Test with valid JSON and no type id
        String json1 = "{\"value\": 1}";
        TypeDeserializerBase deserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        JsonParser jp = mapper.getFactory().createParser(json1);
        Object readValue = deserializer.deserialize(jp, null);
        assertEquals(Integer.class, readValue.getClass());
        assertEquals(1, readValue);

        // Test with valid JSON and type id
        String json2 = "{\"typeId\": \"IntegerClass\", \"value\": 2}";
        deserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        jp = mapper.getFactory().createParser(json2);
        readValue = deserializer.deserialize(jp, null);
        assertEquals(Integer.class, readValue.getClass());
        assertEquals(2, readValue);

        // Test with invalid JSON and no type id
        String json3 = "{\"value\": [1, 2, 3]}";
        try {
            deserializer.deserialize(mapper.getFactory().createParser(json3), null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Unable to instantiate value of type [simple type, class java.lang.Integer]", e.getMessage());
        }

        // Test with invalid JSON and type id
        String json4 = "{\"typeId\": \"InvalidType\", \"value\": 3}";
        try {
            deserializer.deserialize(mapper.getFactory().createParser(json4), null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Unknown type id [class java.lang.String, type InvalidType]: not handling [simple type, class java.lang.InvalidType]", e.getMessage());
        }

        // Test with missing type property name
        TypeDeserializerBase deserializerMissingProperty = new TypeDeserializerBase(null, null, null, false, null);
        String json5 = "{\"typeId\": \"IntegerClass\", \"value\": 4}";
        try {
            deserializerMissingProperty.deserialize(mapper.getFactory().createParser(json5), null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("No property 'typePropertyName' found for class [com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase]", e.getMessage());
        }

        // Test with null _idResolver
        TypeDeserializerBase deserializerNullIdResolver = new TypeDeserializerBase(null, null, "typePropertyName", false, null);
        String json6 = "{\"typeId\": \"IntegerClass\", \"value\": 5}";
        try {
            deserializerNullIdResolver.deserialize(mapper.getFactory().createParser(json6), null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Null TypeIdResolver not supported", e.getMessage());
        }

        // Test with null _defaultImpl
        TypeDeserializerBase deserializerNullDefaultImpl = new TypeDeserializerBase(null, null, "typePropertyName", false, null);
        String json7 = "{\"value\": null}";
        try {
            deserializerNullDefaultImpl.deserialize(mapper.getFactory().createParser(json7), null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("No default implementation for type [simple type, class java.lang.Object]", e.getMessage());
        }

        // Test with missing type id
        String json8 = "{\"value\": 6}";
        try {
            deserializer.deserialize(mapper.getFactory().createParser(json8), null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Missing type id: use @JsonTypeInfo and @JsonSubTypes for type [class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase]", e.getMessage());
        }

        // Test with empty type id
        String json9 = "{\"typeId\": \"\"}";
        try {
            deserializer.deserialize(mapper.getFactory().createParser(json9), null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Empty type id not allowed", e.getMessage());
        }
    }
}