package com.fasterxml.jackson.databind.tests.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.TestBase;

import org.testng.annotations.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test cases for {@link AsWrapperTypeDeserializer} class
 */
public class AsWrapperTypeDeserializerTest extends TestBase {

    @Test
    public void testWrapperObjectDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule()
                .addSerializer(String.class, new StringSerializer())
                .addKeyDeserializer(As.WRAPPER_OBJECT.id(), new AsWrapperTypeDeserializer(String.class, new IdentityTypeIdResolver(), "_type", true, String.class)));

        String json = "{\"_type\":\"com.test.StringType\",\"value\":\"test\"}";
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        assertEquals(mapper.readValue(node, String.class), "test");
    }

    @Test(expectedExceptions = IOException.class)
    public void testMissingTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule()
                .addSerializer(String.class, new StringSerializer())
                .addKeyDeserializer(As.WRAPPER_OBJECT.id(), new AsWrapperTypeDeserializer(String.class, new IdentityTypeIdResolver(), "_type", true, String.class)));

        String json = "{\"value\":\"test\"}";
        mapper.readValue(json, String.class); // should throw IOException
    }

    @Test(expectedExceptions = IOException.class)
    public void testInvalidTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule()
                .addSerializer(String.class, new StringSerializer())
                .addKeyDeserializer(As.WRAPPER_OBJECT.id(), new AsWrapperTypeDeserializer(String.class, new IdentityTypeIdResolver(), "_type", true, String.class)));

        String json = "{\"_type\":\"com.invalid.Type\",\"value\":\"test\"}";
        mapper.readValue(json, String.class); // should throw IOException
    }

    @Test
    public void testVisibleTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule()
                .addSerializer(String.class, new StringSerializer())
                .addKeyDeserializer(As.WRAPPER_OBJECT.id(), new AsWrapperTypeDeserializer(String.class, new IdentityTypeIdResolver(), "_type", true, String.class)));

        String json = "{\"_type\":{\"_class\":\"com.test.StringType\",\"value\":\"test\"},\"value\":\"test\"}";
        ObjectNode node = mapper.readValue(json, ObjectNode.class);
        assertEquals(node.get("value").asText(), "test");
        assertEquals(node.get("_type").get("value").asText(), "test.StringType");
    }

    @Test
    public void testInvisibleTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule()
                .addSerializer(String.class, new StringSerializer())
                .addKeyDeserializer(As.WRAPPER_OBJECT.id(), new AsWrapperTypeDeserializer(String.class, new IdentityTypeIdResolver(), "_type", false, String.class)));

        String json = "{\"_type\":{\"_class\":\"com.test.StringType\",\"value\":\"test\"},\"value\":\"test\"}";
        Object result = mapper.readValue(json, String.class);
        assertNull(result);
    }

    static class StringSerializer extends StdSerializer<String> {
        public StringSerializer() { this(null); }
        public StringSerializer(Class<String> t) { super(t); }

        @Override
        public void serialize(String value, JsonGenerator jg, SerializerProvider sp) throws IOException {
            jg.writeString(value);
        }
    }
}