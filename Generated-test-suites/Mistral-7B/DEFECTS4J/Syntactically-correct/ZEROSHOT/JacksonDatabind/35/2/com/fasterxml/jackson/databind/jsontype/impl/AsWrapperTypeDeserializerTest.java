package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ObjectMapperTestBase;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseTest;

import org.junit.Test;

/**
 * Test class for AsWrapperTypeDeserializer.
 */
public class AsWrapperTypeDeserializerTest extends ObjectMapperTestBase {

    public AsWrapperTypeDeserializerTest() throws Exception {
        super(new ObjectMapper());
    }

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        String json = "{\"type\":\"com.test.SubType\",\"value\":{\"name\":\"Test\"}}";
        JavaType baseType = mapper.getTypeFactory().constructType(AsWrapperTypeDeserializer.class);
        JavaType subType = mapper.getTypeFactory().constructType(SubType.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(subType, mapper.getTypeIdResolver(), "type", true, SubTypeImpl.class);

        Object result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(SubType.class, result.getClass());
        assertEquals("Test", ((SubType) result).getName());
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        String json = "[{\"type\":\"com.test.SubType\",\"value\":{\"name\":\"Test1\"}}]";
        JavaType baseType = mapper.getTypeFactory().constructType(AsWrapperTypeDeserializer.class);
        JavaType subType = mapper.getTypeFactory().constructType(SubType.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(subType, mapper.getTypeIdResolver(), "type", true, SubTypeImpl.class);

        Object result = deserializer.deserializeTypedFromArray(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(SubType.class, result.getClass());
        assertEquals("Test1", ((SubType) result).getName());
    }

    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        String json = "{\"type\":\"com.test.SubType\",\"value\":42}";
        JavaType baseType = mapper.getTypeFactory().constructType(AsWrapperTypeDeserializer.class);
        JavaType subType = mapper.getTypeFactory().constructType(SubType.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(subType, mapper.getTypeIdResolver(), "type", true, SubTypeImpl.class);

        Object result = deserializer.deserializeTypedFromScalar(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(SubType.class, result.getClass());
        fail("Should have thrown an exception");
    }

    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        String json = "{\"type\":\"java.lang.String\",\"value\":42}";
        JavaType baseType = mapper.getTypeFactory().constructType(AsWrapperTypeDeserializer.class);
        JavaType subType = mapper.getTypeFactory().constructType(SubType.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(subType, mapper.getTypeIdResolver(), "type", true, SubTypeImpl.class);

        Object result = deserializer.deserializeTypedFromAny(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(String.class, result.getClass());
        fail("Should have thrown an exception");
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        String json = "{\"type\":\"com.test.SubType\",\"value\":{\"name\":\"Test\"}}";
        JavaType baseType = mapper.getTypeFactory().constructType(AsWrapperTypeDeserializer.class);
        JavaType subType = mapper.getTypeFactory().constructType(SubType.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        idResolver.registerId(SubType.class, "subType");
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(subType, idResolver, "type", true, SubTypeImpl.class);

        Object result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(SubType.class, result.getClass());
        assertEquals("Test", ((SubType) result).getName());
    }

    @Test
    public void testDeserializeWithMissingTypeId() throws Exception {
        String json = "{\"value\":{\"name\":\"Test\"}}";
        JavaType baseType = mapper.getTypeFactory().constructType(AsWrapperTypeDeserializer.class);
        JavaType subType = mapper.getTypeFactory().constructType(SubType.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(subType, mapper.getTypeIdResolver(), "type", true, SubTypeImpl.class);

        try {
            deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
            fail("Should have thrown an exception");
        } catch (IOException e) {
            assertEquals("need JSON Object to contain As.WRAPPER_OBJECT type information for class " + SubType.class.getName(), e.getMessage());
        }
    }

    @Test
    public void testDeserializeWithMissingValue() throws Exception {
        String json = "{\"type\":\"com.test.SubType\"}";
        JavaType baseType = mapper.getTypeFactory().constructType(AsWrapperTypeDeserializer.class);
        JavaType subType = mapper.getTypeFactory().constructType(SubType.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(subType, mapper.getTypeIdResolver(), "type", true, SubTypeImpl.class);

        try {
            deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
            fail("Should have thrown an exception");
        } catch (IOException e) {
            assertEquals("expected closing END_OBJECT after type information and deserialized value", e.getMessage());
        }
    }

    @Test
    public void testDeserializeWithMissingEndObject() throws Exception {
        String json = "{\"type\":\"com.test.SubType\",\"value\":{\"name\":\"Test\"}}";
        JavaType baseType = mapper.getTypeFactory().constructType(AsWrapperTypeDeserializer.class);
        JavaType subType = mapper.getTypeFactory().constructType(SubType.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(subType, mapper.getTypeIdResolver(), "type", true, SubTypeImpl.class);

        ObjectReader objectReader = mapper.readerForType(baseType);
        try {
            objectReader.readValue(new StringReader(json));
            fail("Should have thrown an exception");
        } catch (IOException e) {
            assertEquals("need JSON Object to contain As.WRAPPER_OBJECT type information for class " + SubType.class.getName(), e.getMessage());
        }
    }

    static class SubType {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.WRAPPER_OBJECT)
        static class SubTypeImpl {}
    }
}