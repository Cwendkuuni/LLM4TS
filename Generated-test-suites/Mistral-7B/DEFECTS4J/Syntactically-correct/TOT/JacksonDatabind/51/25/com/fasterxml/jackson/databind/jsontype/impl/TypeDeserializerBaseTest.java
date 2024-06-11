package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBaseWithDefaultImpl() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Test deserialization with default implementation
        String jsonWithDefaultImpl = "{\"type\":\"MyClass\", \"data\":\"default\"}";
        MyClass defaultObj = mapper.readValue(new JsonFactory(), MyClass.class, new TypeReference<MyClass>() {});
        assertEquals("default", defaultObj.getData());

        // Test deserialization with custom implementation
        String jsonWithCustomImpl = "{\"type\":\"MyClassCustomImpl\", \"data\":\"custom\"}";
        MyClassCustomImpl customObj = mapper.readValue(new JsonFactory(), MyClassCustomImpl.class, new TypeReference<MyClassCustomImpl>() {});
        assertEquals("custom", customObj.getData());

        // Test deserialization with unknown type id
        String jsonWithUnknownTypeId = "{\"type\":\"UnknownType\"}";
        try {
            mapper.readValue(new JsonFactory(), MyClass.class, new TypeReference<MyClass>() {});
            fail("Expected JsonMappingException for unknown type id");
        } catch (JsonMappingException e) {
            assertEquals("Unknown type id 'UnknownType' found, but no Text deserializer found for class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", e.getMessage());
        }
    }

    @Test
    public void testTypeDeserializerBaseWithNullifyingDeserializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);

        // Test deserialization with nullifying deserializer
        String jsonWithNullifyingDeserializer = "{\"type\":\"MyClassNull\", \"data\":null}";
        MyClassNull nullObj = mapper.readValue(new JsonFactory(), MyClassNull.class, new TypeReference<MyClassNull>() {});
        assertNull(nullObj.getData());
    }

    static class MyClass {
        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    static class MyClassCustomImpl extends MyClass {
        public String getData() {
            return super.getData() + "_custom";
        }
    }

    static class MyClassNull extends MyClass {
    }
}