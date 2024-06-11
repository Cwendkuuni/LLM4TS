package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testBaseTypeDeserialization() throws JsonProcessingException {
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeDeserializer typeDeserializerInstance = typeDeserializer.forProperty(null);

        String json = "\"hello world\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        Object result = typeDeserializerInstance.deserialize(parser, null, null);

        assertEquals("hello world", result);
    }

    @Test
    public void testMissingTypeIdDeserialization() throws JsonProcessingException {
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeDeserializer typeDeserializerInstance = typeDeserializer.forProperty(null);

        String json = "{}";
        JsonParser parser = mapper.getFactory().createParser(json);
        Object result = typeDeserializerInstance.deserialize(parser, null, null);

        assertNull(result);

        // test exception handling
        try {
            Object result2 = typeDeserializerInstance.deserialize(parser, null, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testTypeIdDeserialization() throws JsonProcessingException {
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeDeserializer typeDeserializerInstance = typeDeserializer.forProperty(null);

        String json = "{\"typeId\":\"java.lang.Integer\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        Object result = typeDeserializerInstance.deserialize(parser, null, null);

        assertEquals(Integer.class, result.getClass());
    }

    @Test
    public void testTypeIdDeserializationWithUnknownTypeId() throws JsonProcessingException {
        JsonTypeInfo typeInfoAnnotation = new JsonTypeInfo();
        typeInfoAnnotation.use(JsonTypeInfo.Id.NAME);
        typeInfoAnnotation.property("typeId");

        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class, String.class, typeInfoAnnotation);
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory().constructType(baseType);
        TypeDeserializer typeDeserializerInstance = typeDeserializer.forProperty(null);

        String json = "{\"typeId\":\"UnknownType\"}";
        JsonParser parser = mapper.getFactory().createParser(json);

        try {
            Object result = typeDeserializerInstance.deserialize(parser, null, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
            String expectedMessage = "Unknown type id: 'UnknownType' (for class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase)";
            assertTrue(e.getMessage().contains(expectedMessage));
        }
    }

    @Test
    public void testTypeIdDeserializationWithCustomDeserializer() throws JsonProcessingException {
        JsonTypeInfo typeInfoAnnotation = new JsonTypeInfo();
        typeInfoAnnotation.use(JsonTypeInfo.Id.NAME);
        typeInfoAnnotation.property("typeId");
        typeInfoAnnotation.deserializer(CustomIntegerDeserializer.class);

        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class, String.class, typeInfoAnnotation);
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory().constructType(baseType);
        TypeDeserializer typeDeserializerInstance = typeDeserializer.forProperty(null);

        String json = "{\"typeId\":\"java.lang.Integer\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        Object result = typeDeserializerInstance.deserialize(parser, null, null);

        assertEquals(42, result);
    }

    static class CustomIntegerDeserializer extends JsonDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return 42;
        }
    }

    @Test
    public void testTypeIdDeserializationWithNullTypeId() throws JsonProcessingException {
        JsonTypeInfo typeInfoAnnotation = new JsonTypeInfo();
        typeInfoAnnotation.use(JsonTypeInfo.Id.NAME);
        typeInfoAnnotation.property("typeId");

        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class, String.class, typeInfoAnnotation);
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory().constructType(baseType);
        TypeDeserializer typeDeserializerInstance = typeDeserializer.forProperty(null);

        String json = "{}";
        JsonParser parser = mapper.getFactory().createParser(json);
        Object result = typeDeserializerInstance.deserialize(parser, null, null);

        assertNull(result);
    }

    @Test
    public void testTypeIdDeserializationWithMissingTypeId() throws JsonProcessingException {
        JsonTypeInfo typeInfoAnnotation = new JsonTypeInfo();
        typeInfoAnnotation.use(JsonTypeInfo.Id.NAME);
        typeInfoAnnotation.property("typeId");

        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class, String.class, typeInfoAnnotation);
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory().constructType(baseType);
        TypeDeserializer typeDeserializerInstance = typeDeserializer.forProperty(null);

        String json = "{\"value\":\"hello world\"}";
        JsonParser parser = mapper.getFactory().createParser(json);

        try {
            Object result = typeDeserializerInstance.deserialize(parser, null, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
            String expectedMessage = "Missing type id property 'typeId'";
            assertTrue(e.getMessage().contains(expectedMessage));
        }
    }

    @Test
    public void testTypeIdDeserializationWithMissingTypeIdAndDefaultImpl() throws JsonProcessingException {
        JsonTypeInfo typeInfoAnnotation = new JsonTypeInfo();
        typeInfoAnnotation.use(JsonTypeInfo.Id.NAME);
        typeInfoAnnotation.property("typeId");

        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class, String.class, typeInfoAnnotation);
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory().constructType(baseType);
        TypeDeserializer typeDeserializerInstance = typeDeserializer.forProperty(null);

        String json = "{\"value\":\"hello world\"}";
        JsonParser parser = mapper.getFactory().createParser(json);

        Object result = typeDeserializerInstance.deserialize(parser, null, null);

        assertEquals("", result);
    }

    @Test
    public void testTypeIdDeserializationWithMissingTypeIdAndNullDefaultImpl() throws JsonProcessingException {
        JsonTypeInfo typeInfoAnnotation = new JsonTypeInfo();
        typeInfoAnnotation.use(JsonTypeInfo.Id.NAME);
        typeInfoAnnotation.property("typeId");

        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class, String.class, typeInfoAnnotation);
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory().constructType(baseType);
        TypeDeserializer typeDeserializerInstance = typeDeserializer.forProperty(null);

        String json = "{\"value\":\"hello world\"}";
        JsonParser parser = mapper.getFactory().createParser(json);

        try {
            Object result = typeDeserializerInstance.deserialize(parser, null, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
            String expectedMessage = "Missing type id property 'typeId'";
            assertTrue(e.getMessage().contains(expectedMessage));
        }
    }

    @Test
    public void testTypeIdDeserializationWithMissingTypeIdAndCustomDefaultImpl() throws JsonProcessingException {
        JsonTypeInfo typeInfoAnnotation = new JsonTypeInfo();
        typeInfoAnnotation.use(JsonTypeInfo.Id.NAME);
        typeInfoAnnotation.property("typeId");

        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class, String.class, typeInfoAnnotation);
        TypeDeserializerBase typeDeserializer = mapper.getTypeFactory().constructType(baseType);
        TypeDeserializer typeDeserializerInstance = typeDeserializer.forProperty(null);

        String json = "{\"value\":\"hello world\"}";
        JsonParser parser = mapper.getFactory().createParser(json);

        Object result = typeDeserializerInstance.deserialize(parser, null, new CustomNullObjectDeserializer(String.class));

        assertNull(result);
    }

    static class CustomNullObjectDeserializer extends NullifyingDeserializer<Object> {
        protected Class<?> _nullValueType;

        public CustomNullObjectDeserializer(Class<?> nullValueType) {
            super(nullValueType);
            _nullValueType = nullValueType;
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return _nullValueType;
        }
    }
}