package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

    @Test
    public void testDefaultTypeDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[]");
        TypeDeserializerBase deserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();

        // Test deserializing without type id
        JsonTreeReader reader = mapper.treeReader();
        Object readValue = reader.readValue(node, deserializer);
        assertEquals(TypeDeserializerBase.class, readValue.getClass());

        // Test deserializing with default type id
        JsonNode nodeWithDefaultTypeId = mapper.readTree("{\"@type\":\"com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase\"}");
        Object readValueWithTypeId = idResolver.deserializeValue(nodeWithDefaultTypeId, deserializer);
        assertEquals(TypeDeserializerBase.class, readValueWithTypeId.getClass());
    }

    @Test
    public void testTypeIdDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"@type\":\"com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase\", \"typeId\":\"subType\"}");
        TypeDeserializerBase deserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();

        // Test deserializing with custom type id
        TypeDeserializer subTypeDeserializer = idResolver.deserializeValue(node, deserializer);
        assertEquals(SubTypeDeserializer.class, subTypeDeserializer.getClass());
    }

    @Test
    public void testUnknownTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"@type\":\"com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase\", \"typeId\":\"unknownType\"}");
        TypeDeserializerBase deserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();

        try {
            idResolver.deserializeValue(node, deserializer);
            assert false;
        } catch (JsonMappingException e) {
            assertEquals("Unknown type id: unknownType", e.getMessage());
        }
    }

    @Test
    public void testMissingTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"@type\":\"com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase\"}");
        TypeDeserializerBase deserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();

        try {
            idResolver.deserializeValue(node, deserializer);
            assert false;
        } catch (JsonMappingException e) {
            assertEquals("Type id is missing", e.getMessage());
        }
    }

    @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.EXISTING_PROPERTY, property="@type")
    static class SubTypeDeserializer extends TypeDeserializerBase {
        @Override
        public TypeDeserializer forProperty(BeanProperty property) {
            // Implement this method to return a sub-type deserializer
            return this;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.WRAPPER_OBJECT;
        }
    }
}