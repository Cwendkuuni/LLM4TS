package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Map;

/**
 * Tests for the base class for all standard Jackson {@link TypeDeserializer}s.
 */
public class TypeDeserializerBaseTest {

    /**
     * Verify that base type resolution works correctly.
     */
    @Test
    public void testBaseTypeResolution() throws IOException, JsonProcessingException {
        // Set up test data
        TypeDeserializerBase deserializer = new TestTypeDeserializerBase(TypeFactory.defaultInstance.constructType(String.class), new TypeIdResolverBase() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) {
                if (typeId.equals("StringType")) {
                    return TypeFactory.defaultInstance.constructType(String.class);
                }
                return null;
            }
        }, "", false, null);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Test deserialization
        String json = "{\"typeId\" : \"StringType\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        TypeDeserializer deser = deserializer.forProperty(null);
        Object result = deser.deserialize(parser, ctxt);

        // Verify result
        assertEquals("String", result.getClass().getName());
    }

    /**
     * Verify that handling of missing or invalid type id results in appropriate error or null value.
     */
    @Test
    public void testMissingOrInvalidTypeId() throws IOException, JsonProcessingException {
        // Set up test data
        TypeDeserializerBase deserializer = new TestTypeDeserializerBase(TypeFactory.defaultInstance.constructType(String.class), null, "", false, null);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Test deserialization with missing type id
        String json1 = "{}";
        JsonParser parser1 = mapper.getFactory().createParser(json1);
        DeserializationContext ctxt1 = mapper.getDeserializationContext();

        TypeDeserializer deser1 = deserializer.forProperty(null);
        Object result1 = deser1.deserialize(parser1, ctxt1);

        assertNull(result1);

        // Test deserialization with invalid type id
        String json2 = "{\"typeId\" : \"InvalidType\"}";
        JsonParser parser2 = mapper.getFactory().createParser(json2);
        DeserializationContext ctxt2 = mapper.getDeserializationContext();

        TypeDeserializer deser2 = deserializer.forProperty(null);
        Object result2 = deser2.deserialize(parser2, ctxt2);

        assertNull(result2);
    }

    /**
     * Verify that handling of unknown type id results in appropriate error or null value.
     */
    @Test
    public void testUnknownTypeId() throws IOException, JsonProcessingException {
        // Set up test data
        TypeDeserializerBase deserializer = new TestTypeDeserializerBase(TypeFactory.defaultInstance.constructType(String.class), null, "", false, null);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.addHandler(new TypeIdHandler());

        // Test deserialization with unknown type id
        String json = "{\"typeId\" : \"UnknownType\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        TypeDeserializer deser = deserializer.forProperty(null);
        Object result = deser.deserialize(parser, ctxt);

        // Verify result
        assertNull(result);
    }

    /**
     * Custom type id handler for testing purposes.
     */
    private static class TypeIdHandler extends TypeIdResolverBase {
        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId) {
            if (typeId.equals("StringType")) {
                return TypeFactory.defaultInstance.constructType(String.class);
            }
            return null;
        }
    }

    /**
     * Test type for TypeDeserializerBase tests.
     */
    private static class TestTypeDeserializerBase extends TypeDeserializerBase {
        public TestTypeDeserializerBase(JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
            if (typeId.equals("TestType")) {
                return new TestJsonDeserializer();
            }
            return super._findDeserializer(ctxt, typeId);
        }
    }

    /**
     * Test deserializer for TypeDeserializerBase tests.
     */
    private static class TestJsonDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return new Object();
        }
    }
}